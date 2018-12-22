package com.example.lenovo.dailyshoppinglist;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.dailyshoppinglist.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;


public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private FloatingActionButton fab_btn;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;

    private TextView totalsumResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar=findViewById(R.id.home_toolbar);
        fab_btn= findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Günlük Alışveriş Listesi");


        totalsumResult=findViewById(R.id.total_amount);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uId=mUser.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Shopping List").child(uId);

        mDatabase.keepSynced(true);

        recyclerView=findViewById(R.id.recycler_home);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //toplam fiyat hesaplama

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int totalamount=0;

                for(DataSnapshot snap:dataSnapshot.getChildren()){

                    Data data=snap.getValue(Data.class);
                    totalamount+=data.getAmount();

                    String sttotal=String.valueOf(totalamount+".00");

                    totalsumResult.setText(sttotal);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customDialog();
            }
        });
    }

    private void customDialog(){
        AlertDialog.Builder mydialog=new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        View myview = inflater.inflate(R.layout.input_data,null);
        final AlertDialog dialog=mydialog.create();
        dialog.setView(myview);

        final EditText type=myview.findViewById(R.id.edt_type);
        final EditText amount=myview.findViewById(R.id.edt_amount);
        final EditText note=myview.findViewById(R.id.edt_note);
        Button btnSave =myview.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mType= type.getText().toString().trim();
                String mAmount=amount.getText().toString().trim();
                String mNote=note.getText().toString().trim();

                int amint=Integer.parseInt(mAmount);


                if(TextUtils.isEmpty(mType)){
                    type.setError("Doldurulması Gerekli !");
                    return;
                    }

                if(TextUtils.isEmpty(mAmount)){
                    amount.setError("Doldurulması Gerekli !");
                    return;
                }

                if(TextUtils.isEmpty(mNote)){
                    note.setError("Doldurulması Gerekli !");
                    return;
                }


                String id=mDatabase.push().getKey();
                String date= DateFormat.getDateInstance().format(new Date());
                Data data=new Data(mType,amint,mNote,date,id);
                mDatabase.child(id).setValue(data);

                Toast.makeText(getApplicationContext(),"Veri Eklendi",Toast.LENGTH_SHORT).show();

                dialog.dismiss();

            }
        });
        dialog.show();
    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>

                (
                        Data.class,
                        R.layout.item_data,
                        MyViewHolder.class,
                        mDatabase


                ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, Data model, int position) {

                viewHolder.setDate(model.getDate());
                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setAmount(model.getAmount());

                viewHolder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateData();
                    }
                });

            }
        };

        recyclerView.setAdapter(adapter);


     }

     public static class MyViewHolder extends RecyclerView.ViewHolder{

        View myview;

        public MyViewHolder(View itemView){
            super(itemView);
            myview=itemView;


        }
        public void setType(String type){
            TextView mType=myview.findViewById(R.id.type);
            mType.setText(type);

        }

        public void setNote(String note){
            TextView mNote=myview.findViewById(R.id.note);
            mNote.setText(note);

        }
        public void setDate(String date){
            TextView mDate=myview.findViewById(R.id.date);
            mDate.setText(date);
        }

        public void setAmount(int amount){
            TextView mAmount=myview.findViewById(R.id.amount);
            String stam=String.valueOf(amount);
            mAmount.setText(stam);
            }



     }


     public void updateData(){
        AlertDialog.Builder mydialog=new AlertDialog.Builder(HomeActivity.this);

        LayoutInflater inflater=LayoutInflater.from(HomeActivity.this);

        View mView=inflater.inflate(R.layout.update_inputfield,null);

        AlertDialog dialog=mydialog.create();
        dialog.setView(mView);
        dialog.show();

     }



}

