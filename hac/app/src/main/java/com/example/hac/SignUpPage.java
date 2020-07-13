package com.example.hac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class SignUpPage extends AppCompatActivity {

    Spinner spinner;
    Button signup;
    String Desig;
    private FirebaseAuth mAuth;
    String usern,pass;
    EditText fname,lname,username,password,phno;
    public void toLogin (View view){

        mAuth.createUserWithEmailAndPassword(usern, pass)
                .addOnCompleteListener(SignUpPage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("Username :",usern);
                            Log.i("Password :",pass);
                            Toast.makeText(SignUpPage.this, "Sign up success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), SignUpPage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.

                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
                            Toast.makeText(SignUpPage.this, "Sign in Failed!!! : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }

    public void toPage (View view){
        if(spinner.getSelectedItem().toString() == "Farmer"){
            Desig = "Farmer";
            toLogin(view);
        }
        else{
            Desig = "Driver";
            toLogin(view);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        mAuth = FirebaseAuth.getInstance();
        fname = findViewById(R.id.editText2);
        lname = findViewById(R.id.editText3);
        username = findViewById(R.id.editText5);
        phno = findViewById(R.id.editText9);
        password = findViewById(R.id.editText8);
        spinner = findViewById(R.id.spinner2);
        signup = findViewById(R.id.button2);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usern = username.getText().toString().trim();
                pass = password.getText().toString().trim();
                String f_name = fname.getText().toString().trim();
                String l_name = lname.getText().toString().trim();
                String ph = phno.getText().toString().trim();
                if(TextUtils.isEmpty(usern)){
                    username.setError("Username Required");
                    username.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(pass)){
                    password.setError("Password Required");
                    password.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(f_name)){
                    fname.setError("Username Required");
                    fname.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(l_name)){
                    lname.setError("Password Required");
                    lname.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(ph)){
                    phno.setError("Password Required");
                    phno.requestFocus();
                    return;
                }
                else{
                        toPage(v);
                }
            }
        });
    }
}
