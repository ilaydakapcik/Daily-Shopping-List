package com.example.lenovo.dailyshoppinglist;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.lang.reflect.Type;


public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private FloatingActionButton fab_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar=findViewById(R.id.home_toolbar);
        fab_btn= findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Günlük Alışveriş Listesi");


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

                dialog.dismiss();

            }
        });
        dialog.show();
    }
}
