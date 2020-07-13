package com.example.liker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    Button signup;
    Button login;
    EditText emailText,passwordText;
    SessionManager sessionManager;
    TextView guest;
    ProgressBar progress;
    public  static  GoogleSignInOptions gso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
        progress = findViewById(R.id.loading);


         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInButton signInButton = findViewById(R.id.sign_in_button);

        guest = findViewById(R.id.guest);
        guest.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                StringBuilder rand = new StringBuilder();
                for(int i = 0;i<5;i++){
                    rand.append(new Random().nextInt(9));
                }
                sessionManager.createSession("Guest"+rand.toString() ,"Guest"+rand.toString()+"@Guest.com", rand.toString(),false);
                updateUI();
            }
        });
        emailText = findViewById(R.id.editText4);
        passwordText = findViewById(R.id.editText5);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                userLogin();
            }
        });

    }

    private void userLogin() {
        final String email = emailText.getText().toString().trim();
        final String password = passwordText.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            emailText.setError("Email Required");
            emailText.requestFocus();
            login.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            return;
        }

        if(TextUtils.isEmpty(password)){
            passwordText.setError("Password required");
            passwordText.requestFocus();
            login.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.serverUrl)+"login.php" ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")){
                                Toast.makeText(MainActivity.this, "Logged in Successfully!!" + jsonObject.getString("name"), Toast.LENGTH_LONG).show();
                                String name = jsonObject.getString("name");
                                String email = jsonObject.getString("email");
                                String id = jsonObject.getString("id");
                                sessionManager.createSession(name,email,id,false);
                                updateUI();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            login.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Log in Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        login.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Volley Error : "+error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> mp = new HashMap<>();
                mp.put("email",email);
                mp.put("password",password);

                return mp;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            sessionManager.createSession(account.getDisplayName(),account.getEmail(),account.getId(),true);
            // Signed in successfully, show authenticated UI.
            //updateUI();
//            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
//            if (acct != null) {
//                personName = acct.getDisplayName();
//                String personEmail = acct.getEmail();
//                personId = acct.getId();
//
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                editor.putString(Name, personName);
//                editor.putString(Email, personEmail);
//                editor.putString(ID,personId);
//                editor.commit();
                showSnackBar("Logged in Successfully");
                updateUI();
//            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Sign in result", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this,"Error SignIng in",Toast.LENGTH_LONG);
        }
    }

    void updateUI(){
        Intent intent = new Intent(MainActivity.this,MainActivity2.class);
        startActivity(intent);
    }

    public  void showSnackBar(String msg){
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),msg, Snackbar.LENGTH_LONG)
                .setAction("Close", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .setActionTextColor(Color.RED)
                .setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE);

        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.LTGRAY);
        TextView textView = (TextView) snackBarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.BLACK);
        snackbar.show();

    }

}
