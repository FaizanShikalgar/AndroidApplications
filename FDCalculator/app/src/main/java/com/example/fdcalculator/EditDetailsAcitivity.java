package com.example.fdcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EditDetailsAcitivity extends AppCompatActivity {

    EditText principal_amt,rate,duration,date,et5;
    TextView keyView;
    Button calc,save_btn,back_btn;
    String p,r,n,d,key;
    int pr,dn;
    double rt,simple_intrest;
    FDstore fd;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details_acitivity);

        principal_amt = findViewById(R.id.editText);
        rate = findViewById(R.id.editText2);
        duration = findViewById(R.id.editText3);
        date = findViewById(R.id.editText4);
        et5 = findViewById(R.id.editText5);
        calc = findViewById(R.id.calc_btn);
        save_btn = findViewById(R.id.save_btn);
        keyView = findViewById(R.id.keyView);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        back_btn = findViewById(R.id.button);
        Intent intent = getIntent();

            p = intent.getStringExtra("Principal");
            r = intent.getStringExtra("intrest");
            n= intent.getStringExtra("duration");
            d =intent.getStringExtra("date");
            key =intent.getStringExtra("key");
        //Toast.makeText(this, "Principal : "+p+"date : "+d, Toast.LENGTH_SHORT).show();
            principal_amt.setText(p);
            rate.setText(r);
            duration.setText(n);
            date.setText(d);
            keyView.setText(key);


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditDetailsAcitivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month = month +1;
                                String dt = day+"/"+month+"/"+year;
                                date.setText(dt);

                            }
                        },year,month,day);
                datePickerDialog.show();
            }
        });
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
            }
        });


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
                //final String key;
                if (TextUtils.isEmpty(et5.getText().toString().trim())) {

                    Toast.makeText(EditDetailsAcitivity.this, "Complete all details first", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                //key =mDatabaseReference.child("Users").child(userId).push().getKey();

                fd = new FDstore(pr,rt,dn,d,simple_intrest,key);

                //mDatabaseReference.child("Users").child(userId).push().setValue(fd)
                mDatabaseReference.child("Users").child(userId).child(key).setValue(fd)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditDetailsAcitivity.this, "Saved Successfully!!", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(EditDetailsAcitivity.this,FDCalculationActivity.class);
                                startActivity(intent1);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditDetailsAcitivity.this, "Oops..Unable to save data!", Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(EditDetailsAcitivity.this,FDCalculationActivity.class);
                startActivity(intent1);
            }
        });

    }

    private void verify(){
        p = principal_amt.getText().toString();
        r =rate.getText().toString();
        n = duration.getText().toString();
        d = date.getText().toString();
        if(TextUtils.isEmpty(p)) {
            principal_amt.setError("Amount Cannot Be Empty");
            return;
        }

        if(TextUtils.isEmpty(r)) {
            rate.setError("Intrest Cannot Be Empty");
            return;
        }

        if(TextUtils.isEmpty(n)) {
            duration.setError("Duration Cannot Be Empty");
            return;
        }

        if(TextUtils.isEmpty(d)) {
            date.setError("Date Cannot Be Empty");
            return;
        }

        pr = Integer.parseInt(p);
        rt = Double.parseDouble(r);
        dn = Integer.parseInt(n);

        simple_intrest = (pr*rt*dn)/100;

        et5.setText(""+simple_intrest);
    }


}
