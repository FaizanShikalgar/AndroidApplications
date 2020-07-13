package com.example.liker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DisplayImage extends AppCompatActivity {

    Bitmap bitmap;
    private String encodeImage;
    private SessionManager sessionManager;
    String personId;
    String personName;
    ProgressBar progressBar;
    Button btnUpload, btnGenerate;
    ImageView image;
    LikeButton likeButton;
    ImageView ring;
    String uid, imgid;
    Boolean isLiked,isVisible;
    ImageView info;
    String likes;
    TextView txt2;
    Integer like;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        sessionManager = new SessionManager(this);
        final HashMap<String, String> user = sessionManager.getUserDetail();
        personId = user.get(SessionManager.ID);
        isLiked = false;
        isVisible = false;
        personName = user.get(SessionManager.NAME);
        Uri resultUri = (Uri) getIntent().getParcelableExtra("uri");
        image = findViewById(R.id.image);
        btnUpload = findViewById(R.id.btnUpload);
        btnGenerate = findViewById(R.id.generateBtn);
        txt2 = findViewById(R.id.likes);
        likeButton = findViewById(R.id.star_button);
        ring = findViewById(R.id.ring);
        progressBar = findViewById(R.id.progressbar);
        info = findViewById(R.id.info);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateImage();
            }
        });

        try {
            InputStream inputStream = getContentResolver().openInputStream(resultUri);
            bitmap = BitmapFactory.decodeStream(inputStream);
            image.setImageBitmap(bitmap);
//            imageStore(bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageStore(bitmap);
            }
        });

        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                like = Integer.parseInt(likes);
                like = like + 1;
                txt2.setText(" Likes : "+like);
                isLiked = true;
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                like = like - 1;
                txt2.setText(" Likes : "+like);
                isLiked = false;
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = findViewById(R.id.linearlayout);
                if(isVisible){
                    linearLayout.setVisibility(View.GONE);
                    isVisible = false;
                }else{
                    linearLayout.setVisibility(View.VISIBLE);
                    isVisible = true;
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        if(isLiked == true) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.serverUrl)+"like.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DisplayImage.this, "Error in Liking! ", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("uid", uid);
                    params.put("imgid", imgid);
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(DisplayImage.this);
            queue.add(stringRequest);
            super.onBackPressed();
        }
    }

    public void showSnackBar(String msg) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
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

    private void generateImage() {
        btnGenerate.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.serverUrl)+"generateimage.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String succes = jsonObject.getString("success");

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (succes.equals("1")) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            imgid = object.getString("id");
                            String imageurl = object.getString("image");
                            uid = object.getString("userid");
                            String username = object.getString("username");
                            likes = object.getString("likes");
                            TextView txt1 = findViewById(R.id.postedby);

                            txt1.setText(" Posted By : "+username);
                            txt2.setText(" Likes : "+likes);
                            String url = getString(R.string.serverUrl)+"Images/" + imageurl;
                            info.setVisibility(View.VISIBLE);

                            try {
                                Glide.with(getApplicationContext()).load(url).into(image);
                                showSnackBar("Random Image has been Generated");
                                progressBar.setVisibility(View.GONE);
                                likeButton.setVisibility(View.VISIBLE);
                                ring.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                btnGenerate.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                showSnackBar("Failed to Generate Image");
                                e.printStackTrace();
                            }
                        }


                    } else {
                        btnGenerate.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        showSnackBar("Failed to Generate Image");
                    }


                } catch (JSONException e) {
                    btnGenerate.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnGenerate.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                showSnackBar("Failed to Generate Image");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uid", personId);
                params.put("username", personName);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(DisplayImage.this);
        queue.add(stringRequest);
    }

    private void imageStore(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        encodeImage = android.util.Base64.encodeToString(imageByte, Base64.DEFAULT);
        uploadImage();
    }


    private void uploadImage() {

        btnUpload.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, getString(R.string.serverUrl)+"process.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                                try{
//                                    JSONObject jsonObject = new JSONObject(response);
//                                    String Response = jsonObject.getString("response");
//                                    Log.d("Response", "onResponse: "+Response);
//                                    Toast.makeText(Main2Activity.this, "Response : "+Response, Toast.LENGTH_LONG).show();
//                                }catch (JSONException e){
//                                    e.printStackTrace();
//                                }

                        showSnackBar("Image Uploaded Successfully");
                        progressBar.setVisibility(View.GONE);
                        btnGenerate.setVisibility(View.VISIBLE);

//                                Log.d("Response", "onResponse: "+response);
                        //progressBar.setVisibility(View.GONE);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        btnUpload.setVisibility(View.VISIBLE);
                        Toast.makeText(DisplayImage.this, "Error " + error.getMessage() + error.toString(), Toast.LENGTH_LONG).show();
                        //progressBar.setVisibility(View.GONE);
                        Log.d("Response", "onResponse: Error " + error.getMessage());
                        showSnackBar("Failed to Upload Image");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("image", encodeImage);
                params.put("id", personId);
                params.put("username", personName);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(DisplayImage.this);
        queue.add(request);

    }

}

