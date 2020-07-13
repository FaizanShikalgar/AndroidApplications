package com.example.fdcalculator;

import androidx.appcompat.app.AppCompatActivity;

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

public class SignInCompleteDetails extends AppCompatActivity {

    TextView f_name,l_name,age;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_complete_details);

        f_name = findViewById(R.id.editText);
        l_name = findViewById(R.id.editText2);

        Button done = findViewById(R.id.button2);

        age = findViewById(R.id.editText3);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strAge = age.getText().toString().trim();
                String strFname = f_name.getText().toString();
                String strLname = l_name.getText().toString();
                int ageInt = Integer.parseInt(strAge);
                if(TextUtils.isEmpty(strLname)) {
                    l_name.setError("Last Name Cannot Be Empty");
                    return;
                }

                if(TextUtils.isEmpty(strFname)) {
                    f_name.setError("First Name Cannot Be Empty");
                    return;
                }

                if(TextUtils.isEmpty(strAge) && (ageInt>0)) {
                    age.setError("Invalid Age");
                    return;
                }



                UserInfo userInfo = new UserInfo(strFname,strLname,ageInt);
                String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("User info").child(currentuser).setValue(userInfo);

                Intent intent = new Intent(SignInCompleteDetails.this,FDCalculationActivity.class);
                intent.putExtra("First name",strFname);
                intent.putExtra("Last Name",strLname);
                intent.putExtra("Age",strAge);
                startActivity(intent);
                finish();
            }
        });
    }
}
