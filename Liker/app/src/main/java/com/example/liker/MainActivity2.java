package com.example.liker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    List<ModelImage> imageList;
    ModelImage modelImage;
    LinearLayoutManager linearLayoutManager;
    SessionManager sessionManager;
    FloatingActionButton btnLogout, btnUpload, floatingActionButton;
    TextView textView1, textView2;
    boolean isOpen;
    Bitmap bitmap;
    String encodeImage;
    String personId;
    String personName;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        final HashMap<String, String> user = sessionManager.getUserDetail();

        personId = user.get(SessionManager.ID);
        personName = user.get(SessionManager.NAME);
        progressBar = findViewById(R.id.loading);
        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        imageList = new ArrayList<>();
        myAdapter = new MyAdapter(this, imageList);
        recyclerView.setAdapter(myAdapter);

        floatingActionButton = findViewById(R.id.fab);
        btnLogout = findViewById(R.id.logout);
        btnUpload = findViewById(R.id.upload);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);

        isOpen = false;

        fetchImages();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    btnLogout.hide();
                    btnUpload.hide();
                    textView1.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    isOpen = false;
                } else {
                    btnLogout.show();
                    btnUpload.show();
                    textView1.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                    isOpen = true;

                    btnUpload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectImage();

                        }
                    });

                    btnLogout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressBar.setVisibility(View.VISIBLE);
                            if (user.get(SessionManager.ISGOOGLE).equals("true")) {

                                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(MainActivity2.this, MainActivity.gso);
                                googleSignInClient.signOut();
                            }
                            sessionManager.logout();
                            showSnackBar("Logged Out Successfully");
                            progressBar.setVisibility(View.GONE);
                        }
                    });


                }
            }
        });

    }

    private void uploadImage() {

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

                        Toast.makeText(MainActivity2.this, "Res" + response, Toast.LENGTH_LONG).show();
//                                Log.d("Response", "onResponse: "+response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity2.this, "Error " + error.getMessage() + error.toString(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        Log.d("Response", "onResponse: Error " + error.getMessage());
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

        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);
        queue.add(request);

    }

    private void selectImage() {

        Dexter.withContext(MainActivity2.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        CropImage.activity()
                                .setInitialCropWindowPaddingRatio(0f)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(MainActivity2.this);

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }


    public void fetchImages() {

        StringRequest request = new StringRequest(Request.Method.POST, getString(R.string.serverUrl)+"fetchImages.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String succes = jsonObject.getString("success");

                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if (succes.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id");
                                    String imageurl = object.getString("image");
                                    String likes = object.getString("likes");

                                    String url = getString(R.string.serverUrl)+"Images/" + imageurl;

                                    modelImage = new ModelImage(id, url, likes);
                                    imageList.add(modelImage);
                                    myAdapter.notifyDataSetChanged();

                                }

                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                            }


                        } catch (JSONException e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("Response", "onErrorResponse: " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("uid", sessionManager.getUserDetail().get(SessionManager.ID));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK/*requestCode == 1 K && data!=null*/) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri filepath = data.getData();
            Uri resultUri = result.getUri();
            Intent intent = new Intent(MainActivity2.this,DisplayImage.class);
            intent.putExtra("uri",resultUri);
            progressBar.setVisibility(View.INVISIBLE);
            startActivity(intent);

//            try {
//                InputStream inputStream = getContentResolver().openInputStream(resultUri);
//                bitmap = BitmapFactory.decodeStream(inputStream);
//                imageStore(bitmap);
//
//            } catch (FileNotFoundException e) {
//                progressBar.setVisibility(View.INVISIBLE);
//                e.printStackTrace();
//            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void imageStore(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        encodeImage = android.util.Base64.encodeToString(imageByte, Base64.DEFAULT);
        uploadImage();
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