package com.buddies.catchwo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    private LinearLayout profile_image_ed;
    String url = "http://catchwo.com/retriverdate.php";
    String upUrl = "http://catchwo.com/updateuserdata.php";
    String Comm_Url = "http://catchwo.com/updateCommitteeinfo.php";
    Uri selectedUri;
    Bitmap bitmap;
    String encodeImage;
    String id_sql,name_sql,uid,time_sql,email_sql,dob_sql,gender_sql,phone_sql,cover_sql,image_,
            value,time,image_sql;
    String id,type,image,abouts,createdBy,CTime,email,comm_name,desc_comm,phone,ComID,abouts_user;
    ScrollView scrollView6;
    TextInputEditText sur,name,about;
    private ProgressDialog progressDialog;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("User Details");



    private ImageView profile_img;
    private Button pick_profile_image,update_profile_image,update_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        profile_image_ed = findViewById(R.id.profile_image_ed);
        update_text = findViewById(R.id.update_text);
        profile_img = findViewById(R.id.profile_img);
        pick_profile_image = findViewById(R.id.pick_profile_image);
        update_profile_image = findViewById(R.id.update_profile_image);
        sur = findViewById(R.id.sur);
        progressDialog = new ProgressDialog(this);
        name = findViewById(R.id.name);
        about = findViewById(R.id.about);
        scrollView6 = findViewById(R.id.scrollView6);



        update_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(about.getText().toString())){
                    SavedataToServer();
                }else {
                    Toast.makeText(EditActivity.this, "Update Some thing about YourSelf", Toast.LENGTH_SHORT).show();
                }


            }
        });

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        time = savedate + ":" + savetime;




        Intent intent = getIntent();
        value = intent.getStringExtra("edit");
        id = intent.getStringExtra("id");
        type = intent.getStringExtra("type");
        image = intent.getStringExtra("image");
        abouts = intent.getStringExtra("abouts");
        createdBy = intent.getStringExtra("createdBy");
        CTime = intent.getStringExtra("CTime");
        email = intent.getStringExtra("email");
        comm_name = intent.getStringExtra("comm_name");
        desc_comm = intent.getStringExtra("desc_comm");
        phone = intent.getStringExtra("phone");
        ComID = intent.getStringExtra("ComID");

        if(value.equals("profile")){
            profile_image_ed.setVisibility(View.VISIBLE);
            scrollView6.setVisibility(View.GONE);
            initProfile();

//            Glide.with(EditActivity.this).load(image_).into(profile_img);

        }else if(value.equals("cover")){
            profile_image_ed.setVisibility(View.VISIBLE);
            scrollView6.setVisibility(View.GONE);
            CinitProfile();

//            Glide.with(EditActivity.this).load(image_).into(profile_img);

        }else if(value.equals("committee_pic")){
            profile_image_ed.setVisibility(View.VISIBLE);
            scrollView6.setVisibility(View.GONE);
            initProfile();
        }else if(value.equals("text")){
            profile_image_ed.setVisibility(View.GONE);
            scrollView6.setVisibility(View.VISIBLE);

            name.setText(name_sql);
            about.setText(abouts_user);

        }



    }

    private void CinitProfile() {

        CUpdateProfile();
    }

    private void SavedataToServer() {
//
//        StringRequest request = new StringRequest(Request.Method.POST, upUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//
//                finish();
//
//                Toast.makeText(EditActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(EditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        }){
//
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//
//
//                Map<String, String> hashMap = new HashMap<String, String>();
//                hashMap.put("id",id_sql);
//                hashMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
//                hashMap.put("name",name_sql);
//                hashMap.put("time",time);
//                hashMap.put("email",email_sql);
//                hashMap.put("dob",dob_sql);
//                hashMap.put("gender",gender_sql);
//                hashMap.put("phone",phone_sql);
//                hashMap.put("image",image_);
//                hashMap.put("about",about.getText().toString());
//
//
//                return hashMap;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(EditActivity.this);
//        requestQueue.add(request);
        progressDialog.setMessage("Please wait ....");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("about",about.getText().toString());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                onBackPressed();
                progressDialog.dismiss();
            }
        });

    }

    private void initProfile() {

        pick_profile_image.setOnClickListener(v -> {
            askPermission();
        });

        update_profile_image.setOnClickListener(v -> {

            if(value.equals("profile")){
                UpdateProfile();

//            Glide.with(EditActivity.this).load(image_).into(profile_img);

            }else if(value.equals("committee_pic")){
              updateCommitteePic(ComID);
            }


        });

    }

    private void updateCommitteePic(String comID) {

        progressDialog.setMessage("Please wait ....");
        progressDialog.show();


        final StorageReference reference1 = storageReference.child("All Users").child(System.currentTimeMillis() + "Users");

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
                    hashMap.put("image",DownloadUrl.toString());

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    reference.child("Committee").child(comID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            onBackPressed();
                            progressDialog.dismiss();
                        }
                    });


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

//        StringRequest request = new StringRequest(Request.Method.POST, Comm_Url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//
//                finish();
//
//                Toast.makeText(EditActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(EditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        }){
//
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//
//
//                Map<String, String> hashMap = new HashMap<String, String>();
//                hashMap.put("uid", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
//                hashMap.put("createdBy",createdBy);
//                hashMap.put("time",CTime);
//                hashMap.put("email",email);
//                hashMap.put("comm_name",comm_name);
//                hashMap.put("desc_comm",desc_comm);
//                hashMap.put("phone",phone);
//                hashMap.put("ComID", ComID);
//                hashMap.put("type",type);
//                hashMap.put("id",id);
//                hashMap.put("image",encodeImage);
//
//
//                return hashMap;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(EditActivity.this);
//        requestQueue.add(request);
//


    }

    private void UpdateProfile() {

        progressDialog.setMessage("Please wait ....");
        progressDialog.show();


        final StorageReference reference1 = storageReference.child("All Users").child(System.currentTimeMillis() + "Users");

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
                    hashMap.put("pic",DownloadUrl.toString());

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            onBackPressed();
                            progressDialog.dismiss();
                        }
                    });


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });



//        StringRequest request = new StringRequest(Request.Method.POST, upUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//
//                finish();
//
////                Toast.makeText(EditActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(EditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        }){
//
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//
//
//                Map<String, String> hashMap = new HashMap<String, String>();
//                hashMap.put("id",id_sql);
//                hashMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
//                hashMap.put("name",name_sql);
//                hashMap.put("time",time);
//                hashMap.put("email",email_sql);
//                hashMap.put("dob",dob_sql);
//                hashMap.put("gender",gender_sql);
//                hashMap.put("phone",phone_sql);
//                hashMap.put("image",encodeImage);
//                hashMap.put("about",abouts_user);
//
//
//                return hashMap;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(EditActivity.this);
//        requestQueue.add(request);
//


    }

    private void CUpdateProfile() {

        progressDialog.setMessage("Please wait ....");
        progressDialog.show();


        final StorageReference reference1 = storageReference.child("All Users").child(System.currentTimeMillis() + "Users");

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
                    hashMap.put("cover",DownloadUrl.toString());

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            onBackPressed();
                            progressDialog.dismiss();
                        }
                    });


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });



//        StringRequest request = new StringRequest(Request.Method.POST, upUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//
//                finish();
//
////                Toast.makeText(EditActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(EditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        }){
//
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//
//
//                Map<String, String> hashMap = new HashMap<String, String>();
//                hashMap.put("id",id_sql);
//                hashMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
//                hashMap.put("name",name_sql);
//                hashMap.put("time",time);
//                hashMap.put("email",email_sql);
//                hashMap.put("dob",dob_sql);
//                hashMap.put("gender",gender_sql);
//                hashMap.put("phone",phone_sql);
//                hashMap.put("image",encodeImage);
//                hashMap.put("about",abouts_user);
//
//
//                return hashMap;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(EditActivity.this);
//        requestQueue.add(request);
//


    }

    private void askPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                ChooseImage();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

                Toast.makeText(EditActivity.this, "Permission Required", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(EditActivity.this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

    }

    private void ChooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 || resultCode == RESULT_OK || data != null || data.getData() != null) {

            selectedUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedUri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                profile_img.setImageBitmap(bitmap);
                profile_img.setVisibility(View.VISIBLE);
                imageStore(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    private void imageStore(Bitmap bitmap) {


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] imagebytes = stream.toByteArray();

        encodeImage = android.util.Base64.encodeToString(imagebytes, Base64.DEFAULT);

    }


    @Override
    protected void onStart() {
        super.onStart();

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
//                arrayList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("sucess");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);



                             uid = object.getString("uid");


                             if(uid.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())){

                                 id_sql = object.getString("id");
                                 name_sql = object.getString("name");
                                 image_sql = object.getString("image");
                                 time_sql = object.getString("time");
                                 email_sql = object.getString("email");
                                 dob_sql = object.getString("dob");
                                 gender_sql = object.getString("gender");
                                 phone_sql = object.getString("phone");
                                 cover_sql = object.getString("cover");
                                 abouts_user = object.getString("about");
                                 image_ = "http://catchwo.com/images/" + image_sql;
                             }



//                            Toast.makeText(EditActivity.this, email_sql, Toast.LENGTH_SHORT).show();

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(EditActivity.this);
        requestQueue.add(request);

    }
}