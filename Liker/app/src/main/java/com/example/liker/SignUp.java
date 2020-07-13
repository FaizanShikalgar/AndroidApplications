package com.example.liker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class SignUp extends AppCompatActivity {

    private Button signin,signup;
    private TextView emailText,passwordText,cnfpasswordText,nameText;
    private ProgressBar progress;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        progress = findViewById(R.id.loading);
        signup = findViewById(R.id.button3);
        signin = findViewById(R.id.button5);

        emailText = findViewById(R.id.editText4);
        passwordText = findViewById(R.id.editText5);
        cnfpasswordText = findViewById(R.id.editText6);
        nameText = findViewById(R.id.editText0);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                registerUser();
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void registerUser() {
        final String email = emailText.getText().toString().trim();
        final String password = passwordText.getText().toString().trim();
        String cnfpassword = cnfpasswordText.getText().toString().trim();
        final String name = nameText.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            emailText.setError("Email Required");
            emailText.requestFocus();
            signup.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            return;
        }

        if(TextUtils.isEmpty(password)){
            passwordText.setError("Password required");
            passwordText.requestFocus();
            signup.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            return;
        }

        if(TextUtils.isEmpty(cnfpassword)){
            cnfpasswordText.setError("Password required");
            cnfpasswordText.requestFocus();
            signup.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            return;
        }

        if(TextUtils.isEmpty(name)){
            nameText.setError("Name required");
            nameText.requestFocus();
            signup.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            return;
        }

        if(cnfpassword.equals(password)){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.serverUrl)+"register.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if(success.equals("1")){
                                    Toast.makeText(SignUp.this, "Registered Successfully!!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(SignUp.this,MainActivity.class);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                signup.setVisibility(View.VISIBLE);
                                progress.setVisibility(View.GONE);
                                Toast.makeText(SignUp.this, "Registration Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            signup.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.GONE);
                            Toast.makeText(SignUp.this, "Volley Error : "+error.toString(), Toast.LENGTH_LONG).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> mp = new HashMap<>();
                    mp.put("name",name);
                    mp.put("email",email);
                    mp.put("password",password);
                    return mp;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(SignUp.this);
            queue.add(stringRequest);

        }

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
