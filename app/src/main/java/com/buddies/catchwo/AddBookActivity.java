package com.buddies.catchwo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddBookActivity extends AppCompatActivity {

    private static final String TAG = "ADD";
    private AutoCompleteTextView cat;
    private TextInputEditText book_name,writtenby,desc_book;
    private RadioButton paid,free;
    private Button cover_img,upload;
    private ImageView cover;
    private ScrollView scrollView5;
    Uri selectedUri;
    Bitmap bitmap;
    Uri path ;
    String encodeImage,time;

    ProgressDialog progressDialog;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("User Details");

    String url = "http://catchwo.com/completebook.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);


        Intent intent = getIntent();
        String value = intent.getStringExtra("type");

        progressDialog = new ProgressDialog(this);
        upload = findViewById(R.id.upload);
        cat = findViewById(R.id.cat);
        book_name = findViewById(R.id.book_name);
        writtenby = findViewById(R.id.writtenby);
        desc_book = findViewById(R.id.desc_book);
        paid = findViewById(R.id.paid);
        free = findViewById(R.id.free);
        cover_img = findViewById(R.id.cover_img);
        cover = findViewById(R.id.cover);
        scrollView5 = findViewById(R.id.scrollView5);

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        time = savedate + ":" + savetime;


        cover_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(cat.getText().toString()) &&!TextUtils.isEmpty(book_name.getText().toString()) &&!TextUtils.isEmpty(writtenby.getText().toString()) &&!TextUtils.isEmpty(desc_book.getText().toString())
                        && !encodeImage.isEmpty()){

                        saveBookToServer(value);



                }else {
                    Toast.makeText(AddBookActivity.this, "Please Fill all the details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveBookToServer(String value) {

        progressDialog.setMessage("Please wait... ");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

//        Long times = System.currentTimeMillis();


        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String time = savedate + ":" + savetime;

        Long times = System.currentTimeMillis();


//        if(!TextUtils.isEmpty(text.getText().toString()) && selectedUri != null) {

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

                    if (task.isSuccessful()) {
                        Uri DownloadUrl = task.getResult();

                        Map<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("category", cat.getText().toString());
                        hashMap.put("time", time);
                        hashMap.put("bookID", String.valueOf(times));
                        hashMap.put("image", DownloadUrl.toString());
                        hashMap.put("name", book_name.getText().toString());
                        hashMap.put("written", writtenby.getText().toString());
                        hashMap.put("description", desc_book.getText().toString());
                        hashMap.put("type",value);
                        hashMap.put("book","");

                        FirebaseDatabase.getInstance().getReference().child("Books").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(times)).setValue(hashMap);

                        FirebaseDatabase.getInstance().getReference().child(value).child(String.valueOf(times)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent i = new Intent(AddBookActivity.this, PDFActivity.class);
                                i.putExtra("id", String.valueOf(times));
                                i.putExtra("type", value);

                                startActivity(i);
//                Toast.makeText(AddBookActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception error) {
                                Toast.makeText(AddBookActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });


                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddBookActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });



//        }

//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//
////                onBackPressed();
//
//                Intent i = new Intent(AddBookActivity.this,PDFActivity.class);
//                i.putExtra("id",String.valueOf(times));
//                startActivity(i);
//                Toast.makeText(AddBookActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(AddBookActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
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
//                hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
//                hashMap.put("category",cat.getText().toString());
//                hashMap.put("time",time);
//                hashMap.put("bookID", String.valueOf(times));
//                hashMap.put("image",encodeImage);
//                hashMap.put("name",book_name.getText().toString());
//                hashMap.put("written",writtenby.getText().toString());
//                hashMap.put("description",desc_book.getText().toString());
//
//
//                return hashMap;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(AddBookActivity.this);
//        requestQueue.add(request);



    }

    private void askPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                ChooseImage();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

                Toast.makeText(AddBookActivity.this, "Permission Required", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(AddBookActivity.this)
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
                cover.setImageBitmap(bitmap);
                cover.setVisibility(View.VISIBLE);
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


}