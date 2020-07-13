package com.example.fdcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class MainActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;
    private static final String TAG = "FACELOG";
    private String f_name,l_name,full_name;
    private FirebaseAuth mAuth;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.fdcalculator",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {
        }
        catch (NoSuchAlgorithmException e) {
        }

        button = findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });



        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            updateUI2();
        }
    }

    private void updateUI2(){
        Intent intent = new Intent(MainActivity.this,FDCalculationActivity.class);
        startActivity(intent);
        finish();
    }
    private void updateUI() {
        Toast.makeText(MainActivity.this,"You'r Logged in! as "+full_name,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this,CompleteDetails.class);
        intent.putExtra("First Name",f_name);
        intent.putExtra("Last Name",l_name);
        startActivity(intent);
        finish();
    }

    // ...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {

        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Profile profile = Profile.getCurrentProfile();

        if(profile!=null) {
            f_name = "FaizanShikalgar";
            f_name = profile.getFirstName();
            full_name = profile.getName();
            l_name = profile.getLastName();
            mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override

                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        Log.d(TAG, "signInWithCredential:Success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI();

                    } else {

                        Log.d(TAG, "signInWithCredential:Failed");
                        Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
    }



}
