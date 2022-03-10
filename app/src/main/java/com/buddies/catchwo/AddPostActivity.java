package com.buddies.catchwo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buddies.catchwo.ui.AppointFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddPostActivity extends AppCompatActivity {

    private TextView post;
    private RelativeLayout  add;
    private EditText text;
    private ImageView image;
    private VideoView video;
    Uri selectedUri;
    Bitmap bitmap;
    String url = "http://catchwo.com/post.php";
    String encodeImage,enchodeVedio;
    private ProgressDialog progressDialog;
    MediaController mediaController;
    String type,id;
    String videoData;
    private static final int PICK_FILE = 1;
    private LinearLayout private_lay;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("User Details");

    private Switch privatesSwitch,reelSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        post = findViewById(R.id.post);
        image = findViewById(R.id.image_lay);
        video = findViewById(R.id.video_lay);
        private_lay = findViewById(R.id.private_lay);
        add = findViewById(R.id.add);
        text = findViewById(R.id.text);
        privatesSwitch = findViewById(R.id.privatesSwitch);
        reelSwitch = findViewById(R.id.reel);
        progressDialog = new ProgressDialog(this);
        mediaController = new MediaController(AddPostActivity.this);

        Intent intent = getIntent();
        String vaule = intent.getStringExtra("value");
        id = intent.getStringExtra("Id");



        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(type.equals("iv")) {
//                    UploadData();
//                }else if(type.equals("vv")){
//                    UploadVideoData();
//                }
//

                if(vaule.equals("pub")){
                    UploadDataToFirebase();
                    private_lay.setVisibility(View.GONE);
                }else if(vaule.equals("meo")){
                    MemoryUpdate();
//                    private_lay.setVisibility(View.GONE);
                }else if(vaule.equals("committee")){

                    String name = intent.getStringExtra("name");

                    committeeUpdate(name);
                    private_lay.setVisibility(View.GONE);
                }



            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission();
            }
        });



    }

    private void committeeUpdate(String name) {

        progressDialog.setMessage("Please wait ....");
        progressDialog.show();


        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        Long times = System.currentTimeMillis();


        if(!TextUtils.isEmpty(text.getText().toString()) && selectedUri != null){

            final StorageReference reference1 = storageReference.child("All Posts").child(System.currentTimeMillis() + "posts");

            UploadTask uploadTask = reference1.putFile(selectedUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference1.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()){
                        Uri DownloadUrl = task.getResult();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("type",type);
                        hashMap.put("time",time);
                        hashMap.put("desc", text.getText().toString());
                        hashMap.put("cat","comt");
                        hashMap.put("name",name);
                        hashMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("post",DownloadUrl.toString());
                        hashMap.put("id",String.valueOf(times));
                        hashMap.put("comt",id);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        reference.child("Posts").child(String.valueOf(times)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(AddPostActivity.this, HomeScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                progressDialog.dismiss();
                            }
                        });


                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

        }
        else if(selectedUri == null && !TextUtils.isEmpty(text.getText().toString())) {

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("type","txt");
            hashMap.put("time",time);
            hashMap.put("desc", text.getText().toString());
            hashMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
            hashMap.put("post","");
            hashMap.put("cat","comt");
            hashMap.put("name",name);
            hashMap.put("id",String.valueOf(times));
            hashMap.put("comt",id);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Posts").child(String.valueOf(times)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent intent = new Intent(AddPostActivity.this, HomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    progressDialog.dismiss();
                }
            });



        }else if(selectedUri != null && TextUtils.isEmpty(text.getText().toString())) {

            final StorageReference reference1 = storageReference.child("All Posts").child(System.currentTimeMillis() + "posts");

            UploadTask uploadTask = reference1.putFile(selectedUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference1.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()){
                        Uri DownloadUrl = task.getResult();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("type",type);
                        hashMap.put("time",time);
                        hashMap.put("desc", "");
                        hashMap.put("cat","comt");
                        hashMap.put("name",name);
                        hashMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("post",DownloadUrl.toString());
                        hashMap.put("id",String.valueOf(times));
                        hashMap.put("comt",id);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        reference.child("Posts").child(String.valueOf(times)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(AddPostActivity.this, HomeScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                progressDialog.dismiss();
                            }
                        });


                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

        }else {

            Toast.makeText(AddPostActivity.this, "Please Post something", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }


    }

    private void MemoryUpdate() {

        progressDialog.setMessage("Please wait ....");
        progressDialog.show();


        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        Long times = System.currentTimeMillis();


        if(!TextUtils.isEmpty(text.getText().toString()) && selectedUri != null){

            final StorageReference reference1 = storageReference.child("All Posts").child(System.currentTimeMillis() + "posts");

            UploadTask uploadTask = reference1.putFile(selectedUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference1.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()){
                        Uri DownloadUrl = task.getResult();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("type",type);
                        hashMap.put("time",time);
                        hashMap.put("cat","mome");
                        hashMap.put("desc", text.getText().toString());
                        hashMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("post",DownloadUrl.toString());
                        hashMap.put("id",String.valueOf(times));

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        reference.child("memory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(AddPostActivity.this, HomeScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                progressDialog.dismiss();
                            }
                        });


                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

        }
        else if(selectedUri == null && !TextUtils.isEmpty(text.getText().toString())) {

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("type","txt");
            hashMap.put("time",time);
            hashMap.put("desc", text.getText().toString());
            hashMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
            hashMap.put("post","");
            hashMap.put("cat","mome");
            hashMap.put("id",String.valueOf(times));

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("memory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent intent = new Intent(AddPostActivity.this, HomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    progressDialog.dismiss();
                }
            });



        }else if(selectedUri != null && TextUtils.isEmpty(text.getText().toString())) {

            final StorageReference reference1 = storageReference.child("All Posts").child(System.currentTimeMillis() + "posts");

            UploadTask uploadTask = reference1.putFile(selectedUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference1.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()){
                        Uri DownloadUrl = task.getResult();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("type",type);
                        hashMap.put("time",time);
                        hashMap.put("desc", "");
                        hashMap.put("cat","mome");
                        hashMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("post",DownloadUrl.toString());
                        hashMap.put("id",String.valueOf(times));

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        reference.child("memory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(AddPostActivity.this, HomeScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                progressDialog.dismiss();
                            }
                        });


                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

        }else {

            Toast.makeText(AddPostActivity.this, "Please Post something", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }


    }

    private void UploadDataToFirebase() {

        progressDialog.setMessage("Please wait ....");
        progressDialog.show();


        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        Long times = System.currentTimeMillis();


        if(!TextUtils.isEmpty(text.getText().toString()) && selectedUri != null){

            final StorageReference reference1 = storageReference.child("All Posts").child(System.currentTimeMillis() + "posts");

            UploadTask uploadTask = reference1.putFile(selectedUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference1.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()){
                        Uri DownloadUrl = task.getResult();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("type",type);
                        hashMap.put("time",time);
                        hashMap.put("cat","post");
                        hashMap.put("desc", text.getText().toString());
                        hashMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("post",DownloadUrl.toString());
                        hashMap.put("id",String.valueOf(times));

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        reference.child("Posts").child(String.valueOf(times)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(AddPostActivity.this, HomeScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                progressDialog.dismiss();
                            }
                        });


                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

        }
        else if(selectedUri == null && !TextUtils.isEmpty(text.getText().toString())) {

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("type","txt");
            hashMap.put("time",time);
            hashMap.put("desc", text.getText().toString());
            hashMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
            hashMap.put("post","");
            hashMap.put("cat","post");
            hashMap.put("id",String.valueOf(times));

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Posts").child(String.valueOf(times)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent intent = new Intent(AddPostActivity.this, HomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    progressDialog.dismiss();
                }
            });



        }else if(selectedUri != null && TextUtils.isEmpty(text.getText().toString())) {

            final StorageReference reference1 = storageReference.child("All Posts").child(System.currentTimeMillis() + "posts");

            UploadTask uploadTask = reference1.putFile(selectedUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference1.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()){
                        Uri DownloadUrl = task.getResult();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("type",type);
                        hashMap.put("time",time);
                        hashMap.put("desc", "");
                        hashMap.put("cat","post");
                        hashMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("post",DownloadUrl.toString());
                        hashMap.put("id",String.valueOf(times));

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        reference.child("Posts").child(String.valueOf(times)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(AddPostActivity.this, HomeScreen.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                progressDialog.dismiss();
                            }
                        });


                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

        }else {

            Toast.makeText(AddPostActivity.this, "Please Post something", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }



    }

    private void UploadVideoData() {
        progressDialog.setMessage("Please waite Posting... ");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        Long times = System.currentTimeMillis();

        StringRequest request = new StringRequest(Request.Method.POST, "http://catchwo.com/VideoPostUpload.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Intent intent = new Intent(AddPostActivity.this, HomeScreen.class);
                startActivity(intent);
                progressDialog.dismiss();

                Toast.makeText(AddPostActivity.this, response, Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(AddPostActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> hashMap = new HashMap<String, String>();


                if (!encodeImage.isEmpty() && !TextUtils.isEmpty(text.getText().toString())) {
                    hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    hashMap.put("time", time);
                    hashMap.put("description", text.getText().toString());
                    hashMap.put("image", videoData);
                    hashMap.put("type", "iv");
                } else if (!TextUtils.isEmpty(text.getText().toString()) && encodeImage.isEmpty()) {
                    hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    hashMap.put("type", "tv");
                    hashMap.put("time", time);
                    hashMap.put("description", text.getText().toString());
                    hashMap.put("image", "");
                } else if (!encodeImage.isEmpty() && TextUtils.isEmpty(text.getText().toString())) {
                    hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    hashMap.put("time", time);
                    hashMap.put("description", "");
                    hashMap.put("type", "iv");
                    hashMap.put("image", videoData);
                } else  if (encodeImage.isEmpty() || TextUtils.isEmpty(text.getText().toString())) {
                    Toast.makeText(AddPostActivity.this, "Post something", Toast.LENGTH_SHORT).show();
                }


                return hashMap;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(AddPostActivity.this);
        requestQueue.add(request);


    }

    private void askPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                ChooseImage();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

                Toast.makeText(AddPostActivity.this, "Permission Required", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(AddPostActivity.this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

    }

    private void UploadData() {


        progressDialog.setMessage("Please waite Posting... ");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        Long times = System.currentTimeMillis();


//        String phone = country_code_picker.getSelectedCountryCode() + " " + signup_phone_number.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Intent intent = new Intent(AddPostActivity.this, HomeScreen.class);
                startActivity(intent);
                progressDialog.dismiss();

                Toast.makeText(AddPostActivity.this, response, Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(AddPostActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> hashMap = new HashMap<String, String>();


                    if (image.getDrawable() != null && !TextUtils.isEmpty(text.getText().toString())) {
                        hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("time", time);
                        hashMap.put("description", text.getText().toString());
                        hashMap.put("image", encodeImage);
                        hashMap.put("type", "iv");
                    } else if (!TextUtils.isEmpty(text.getText().toString())) {
                        hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("type", "tv");
                        hashMap.put("time", time);
                        hashMap.put("description", text.getText().toString());
                        hashMap.put("image", "");
                    } else if (image.getDrawable() != null && TextUtils.isEmpty(text.getText().toString())) {
                        hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("time", time);
                        hashMap.put("description", "");
                        hashMap.put("type", "iv");
                        hashMap.put("image", encodeImage);
                    } else  if (image.getDrawable() == null || TextUtils.isEmpty(text.getText().toString())) {
                        Toast.makeText(AddPostActivity.this, "Post something", Toast.LENGTH_SHORT).show();
                    }


                return hashMap;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(AddPostActivity.this);
        requestQueue.add(request);
        
    }

    private void ChooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*");
        //  intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_FILE || resultCode == RESULT_OK || data != null
                || data.getData() != null){

            selectedUri = data.getData();
            if(selectedUri.toString().contains("image")){
//                Picasso.get().load(selectedUri).into(image);
//                image.setVisibility(View.VISIBLE);
//                imageStore(bitmap);

                try {
                    InputStream inputStream = getContentResolver().openInputStream(selectedUri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    image.setImageBitmap(bitmap);
                    image.setVisibility(View.VISIBLE);
                    video.setVisibility(View.GONE);
                    imageStore(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

//                reel_lay.setVisibility(View.GONE);
                type = "iv";
            }else if(selectedUri.toString().contains("video")){
                video.setMediaController(mediaController);
                image.setVisibility(View.GONE);
                video.setVisibility(View.VISIBLE);
                video.setVideoURI(selectedUri);
                video.start();

                selectedUri = Objects.requireNonNull(data).getData();
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(getApplicationContext(), selectedUri);
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long timeInMilli = Long.parseLong(time);
                retriever.release();

                if (timeInMilli > 600000){
//                    Snackbar.make(findViewById(R.id.main), "Video must be of 10 minutes or less", Snackbar.LENGTH_LONG).show();
                    Toast.makeText(AddPostActivity.this, "Video must be of 10 minutes or less", Toast.LENGTH_SHORT).show();
                }else {
                    vedio(selectedUri);
                }
//                reel_lay.setVisibility(View.VISIBLE);
                type = "vv";
            }
        }
        else{
            Toast.makeText(AddPostActivity.this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void vedio(Uri selectedUri) {

//        try {
//            InputStream inputStream = AddPostActivity.this.getContentResolver().openInputStream(selectedUri);
//            byte[] pdfInBytes = new byte[inputStream.available()];
//            inputStream.read(pdfInBytes);
//            encodeImage = android.util.Base64.encodeToString(pdfInBytes, Base64.DEFAULT);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Log.d("ADD",encodeImage);
//        Toast.makeText(AddPostActivity.this, encodeImage, Toast.LENGTH_SHORT).show();
//


        String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};
        Cursor cursor = managedQuery(selectedUri, projection, null, null, null);

        cursor.moveToFirst();
        String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
        Log.d("File Name:",filePath);

        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
        // Setting the thumbnail of the video in to the image view
//        msImage.setImageBitmap(thumb);
//        imageStore(thumb);
        InputStream inputStream = null;
// Converting the video in to the bytes
        try
        {
            inputStream = getContentResolver().openInputStream(selectedUri);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int len = 0;
        try
        {
            while ((len = inputStream.read(buffer)) != -1)
            {
                byteBuffer.write(buffer, 0, len);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("converted!");


        //Converting bytes into base64
        videoData = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
        Log.d("VideoData**>  " , videoData);

        String sinSaltoFinal2 = videoData.trim();
        String sinsinSalto2 = sinSaltoFinal2.replaceAll("\n", "");
        Log.d("VideoData**>  " , sinsinSalto2);

//        baseVideo = sinsinSalto2;



    }

    private void imageStore(Bitmap bitmap) {


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] imagebytes = stream.toByteArray();

        encodeImage = android.util.Base64.encodeToString(imagebytes, Base64.DEFAULT);

    }
}