package com.example.hac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    Button login;
    TextView signup;
    EditText username,password;
    String usern,pass;
    private FirebaseAuth mAuth;
    public void toPage (View view){
        if(spinner.getSelectedItem().toString() == "Farmer"){
            mAuth.signInWithEmailAndPassword(usern, pass)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Intent i1 = new Intent(getApplicationContext(),Farmer.class);
                                startActivity(i1);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this, "Sign in Unsuccessful", Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });



        }
        else{
            mAuth.signInWithEmailAndPassword(usern, pass)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Intent i2 = new Intent(getApplicationContext(),Driver.class);
                                startActivity(i2);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this, "Sign in Unsuccessful", Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });


        }
    }
    public void gotoSignup (View view){
        //Toast.makeText(this,spinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),SignUpPage.class);
        startActivity(intent);

    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //if(currentUser!= null)
            updateUI(currentUser);
    }
    private void updateUI(FirebaseUser user){
        Intent intent = new Intent(MainActivity.this,FirstPage.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        String[] arraySpinner = new String[] {
                "1", "2", "3", "4", "5", "6", "7"
        };
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.spinner);
        login = findViewById(R.id.button);
        signup = findViewById(R.id.textView2);
        username = findViewById(R.id.editText);
        password = findViewById(R.id.editText4);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usern = username.getText().toString().trim();
                 pass = password.getText().toString().trim();
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
                else{
                    toPage(v);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSignup(v);
            }
        });

    }
}
