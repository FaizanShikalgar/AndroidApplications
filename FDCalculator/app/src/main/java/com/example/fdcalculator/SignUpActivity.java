package com.example.fdcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private Button signin,signup;
    private TextView emailText,passwordText,cnfpasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth =FirebaseAuth.getInstance();

        signup = findViewById(R.id.button3);
        signin = findViewById(R.id.button5);

        emailText = findViewById(R.id.editText4);
        passwordText = findViewById(R.id.editText5);
        cnfpasswordText = findViewById(R.id.editText6);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser(){
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String cnfpassword = cnfpasswordText.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            emailText.setError("Email Required");
            emailText.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password)){
            passwordText.setError("Password required");
            passwordText.requestFocus();
            return;
        }

        if(cnfpassword.equals(password)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                Toast.makeText(SignUpActivity.this, "Sign in Failed!!! : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });

        }
        else
            Toast.makeText(this, "Password do not match!!", Toast.LENGTH_SHORT).show();
    }

}