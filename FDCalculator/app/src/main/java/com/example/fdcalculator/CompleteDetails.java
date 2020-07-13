package com.example.fdcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CompleteDetails extends AppCompatActivity {

    TextView f_name,l_name,m_name,age;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_details);

        f_name = (TextView)findViewById(R.id.editText);
        l_name = (TextView)findViewById(R.id.editText2);
        //m_name = (TextView)findViewById(R.id.editText2);



        Intent intent = getIntent();
        final String first_name = intent.getStringExtra("First Name");
        f_name.setText(first_name);

        final String last_name = intent.getStringExtra("Last Name");
        l_name.setText(last_name);

        //age = findViewById(R.id.editText3);

        Button done = findViewById(R.id.button2);

        age = findViewById(R.id.editText3);




        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ageStr = age.getText().toString();
                if(TextUtils.isEmpty(ageStr)) {
                    age.setError("Age Cannot Be Empty");
                    return;
                }
                int ageInt = Integer.parseInt(ageStr);
                /*if(age.getText().toString().matches("")){
                    Toast.makeText(CompleteDetails.this, "Invalid Age!!", Toast.LENGTH_SHORT).show();
                }*/

                if(!((Integer.parseInt(age.getText().toString()) > 10) && Integer.parseInt(age.getText().toString()) < 100)){
                    Toast.makeText(CompleteDetails.this, "Invalid Age!!", Toast.LENGTH_SHORT).show();
                }else{

                    UserInfo userInfo = new UserInfo(first_name,last_name,ageInt);
                    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("User info").child(currentuser).setValue(userInfo);
                    Toast.makeText(CompleteDetails.this, "Age entered is : "+age.getText().toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CompleteDetails.this,FDCalculationActivity.class);
                    intent.putExtra("First name",first_name);
                    intent.putExtra("Last Name",last_name);
                    intent.putExtra("Age",ageStr);
                    startActivity(intent);
                    finish();
                }
            }
        });

        //str = intent.getStringExtra("Middle Name");
        //m_name.setText(str);
    }
}
