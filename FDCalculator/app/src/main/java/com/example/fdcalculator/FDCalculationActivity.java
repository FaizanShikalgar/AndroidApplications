package com.example.fdcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FDCalculationActivity extends AppCompatActivity {

    EditText editText1,editText2,editText3,editText4,editText5;
    private DatabaseReference mDatabaseReference,databaseReference;
    Button calc_btn,save_btn,prev_result;
    FDstore fd;
    int p,n;
    double r,simple_intrest;
    String date,principal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User info");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("User info").child(currentuser).child("age").getValue() == null){
                    Intent intent = new Intent(FDCalculationActivity.this,SignInCompleteDetails.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        setContentView(R.layout.activity_fdcalculation);
        editText1 = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);

        calc_btn = findViewById(R.id.calc_btn);
        save_btn = findViewById(R.id.save_btn);
        prev_result = findViewById(R.id.button);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        editText4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(FDCalculationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month = month +1;
                                String date = day+"/"+month+"/"+year;
                                editText4.setText(date);

                            }
                        },year,month,day);
                datePickerDialog.show();
            }
        });

        calc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
                if (TextUtils.isEmpty(principal)) {

                    Toast.makeText(FDCalculationActivity.this, "Complete all details first", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    final String key;
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    key = mDatabaseReference.child("Users").child(userId).push().getKey();
                    fd = new FDstore(p, r, n, date, simple_intrest, key);

                    //mDatabaseReference.child("Users").child(userId).push().setValue(fd)
                    mDatabaseReference.child("Users").child(userId).child(key).setValue(fd)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(FDCalculationActivity.this, "Saved Successfully!! ", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(FDCalculationActivity.this, "Oops..Unable to save data!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        prev_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FDCalculationActivity.this,ShowHistoryActivity.class);
                startActivity(intent);
                //finish();
            }

        });

    }

    private void verify(){
        principal = editText1.getText().toString();
        String intrest = editText2.getText().toString();
        String duration = editText3.getText().toString();
        date = editText4.getText().toString();

        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date d = null;
        try {
            d = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(d); // Sat Jan 02 00:00:00 GMT 2010
        if(TextUtils.isEmpty(principal)) {
            editText1.setError("Amount Cannot Be Empty");
            return;
        }

        if(TextUtils.isEmpty(intrest)) {
            editText2.setError("Intrest Cannot Be Empty");
            return;
        }

        if(TextUtils.isEmpty(duration)) {
            editText3.setError("Duration Cannot Be Empty");
            return;
        }

        if(TextUtils.isEmpty(date)) {
            editText4.setError("Date Cannot Be Empty");
            return;
        }

        p = Integer.parseInt(principal);
        r = Double.parseDouble(intrest);
        n = Integer.parseInt(duration);

        simple_intrest = (p*n*r)/100;

        editText5.setText(""+simple_intrest);
    }

}
