package com.example.fdcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
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

public class SignInActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private Button signin,signup;
    private TextView emailText,passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth =FirebaseAuth.getInstance();

        signin = findViewById(R.id.button3);
        signup = findViewById(R.id.button5);

        emailText = findViewById(R.id.editText4);
        passwordText = findViewById(R.id.editText5);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();

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

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Intent intent = new Intent(SignInActivity.this,SignInCompleteDetails.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignInActivity.this, "Sign in Unsuccessful", Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(inent);
                finish();
            }
        });
    }

}
