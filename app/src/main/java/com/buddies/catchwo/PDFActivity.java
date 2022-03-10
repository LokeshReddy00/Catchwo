package com.buddies.catchwo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PDFActivity extends AppCompatActivity {

    private Button cover_img,upload,upload_txt;
    private ImageView cover;
    TextView file;
    Uri selectedUri;
    Bitmap bitmap;
    String encodeImage;
    String up_url = "http://catchwo.com/updatecompletedbooks.php";
    String url = "http://catchwo.com/retrivecompletedbooks.php";
    String value,id,name,written,time,book,cat,bid,image,desc;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("User Details");
    private TextInputEditText book_name;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfactivity);
        progressDialog = new ProgressDialog(this);
        cover= findViewById(R.id.cover);
        cover_img= findViewById(R.id.cover_img);
        upload= findViewById(R.id.upload);
        file= findViewById(R.id.file);
        book_name= findViewById(R.id.book_name);
        upload_txt= findViewById(R.id.upload_txt);

        Intent intent = getIntent();
        value = intent.getStringExtra("id");
        String type = intent.getStringExtra("type");

        if(type.equals("complete")){
            upload_txt.setClickable(false);
//            Toast.makeText(this, "Sorry you have no permission for this", Toast.LENGTH_SHORT).show();
        }else if(type.equals("ongoing")){
            upload.setClickable(false);
//            Toast.makeText(this, "Sorry you have no permission for this", Toast.LENGTH_SHORT).show();
        }

        cover_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosepdf();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateServer(type);
            }
        });

        upload_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateServerTxt(type);
            }
        });

    }

    private void updateServerTxt(String type) {

        progressDialog.setMessage("Please wait... ");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

//                StringRequest request = new StringRequest(Request.Method.POST, up_url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//
////                onBackPressed();
//
//                Intent i = new Intent(PDFActivity.this,BooksActivity.class);
//                startActivity(i);
//                Toast.makeText(PDFActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(PDFActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
//                hashMap.put("category",cat);
//                hashMap.put("time",time);
//                hashMap.put("book", book_name.getText().toString());
//                hashMap.put("image",book);
//                hashMap.put("bookID",bid);
//                hashMap.put("name",name);
//                hashMap.put("written",written);
//                hashMap.put("id",id);
//                hashMap.put("description",desc);
//                hashMap.put("type","ongoing");
//
//
//                return hashMap;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(PDFActivity.this);
//        requestQueue.add(request);
//
//        Map<String, String> hashMap = new HashMap<String, String>();
//        hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
//        hashMap.put("category",cat);
//        hashMap.put("time",time);
//
//        hashMap.put("image",book);
//        hashMap.put("bookID",bid);
//        hashMap.put("name",name);
//        hashMap.put("written",written);
//        hashMap.put("id",id);
//        hashMap.put("description",desc);
//        hashMap.put("type","");


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("bookID",value);
        hashMap.put("book", book_name.getText().toString());
        hashMap.put("type",type);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(type).child(value).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Intent intent = new Intent(PDFActivity.this,BooksActivity.class);
                startActivity(intent);

                Toast.makeText(PDFActivity.this, "Book Uploaded Successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PDFActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });



    }

    private void choosepdf() {


        Intent chooseFile = new Intent();
        chooseFile.setType("application/pdf");
        chooseFile.setAction(Intent.ACTION_GET_CONTENT);
        chooseFile = Intent.createChooser(chooseFile,"Choose a pdf");
        startActivityForResult(chooseFile, 2);

    }

    private void updateServer(String type) {

        progressDialog.setMessage("Please wait... ");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        Long times = System.currentTimeMillis();


        if(selectedUri != null) {

            final StorageReference reference1 = storageReference.child("All PDF").child(System.currentTimeMillis() + "pdf");

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
                        Uri Download = task.getResult();


                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("bookID",value);
                        hashMap.put("pdf",Download.toString());
                        hashMap.put("type",type);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        reference.child(type).child(value).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Intent intent = new Intent(PDFActivity.this,BooksActivity.class);
                                startActivity(intent);

                                Toast.makeText(PDFActivity.this, "Book Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {

                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PDFActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });

                    }
                }
            });
        }else {
            Toast.makeText(PDFActivity.this, "Select the PDF", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {

            if (requestCode == 2 || data != null || Objects.requireNonNull(data).getData() != null) {

                selectedUri = data.getData();

                try {
                    InputStream inputStream = PDFActivity.this.getContentResolver().openInputStream(selectedUri);
                    byte[] pdfInBytes = new byte[inputStream.available()];
                    inputStream.read(pdfInBytes);
                    encodeImage = android.util.Base64.encodeToString(pdfInBytes, Base64.DEFAULT);
//                Log.d(TAG,encodeBook);
                    file.setText(selectedUri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            Toast.makeText(PDFActivity.this, "No file Selected", Toast.LENGTH_SHORT).show();
        }
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


                            bid = object.getString("bookID");
                            String uid = object.getString("uid");


                            if(value.equals(bid) && uid.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())){

                                id = object.getString("id");
                                name = object.getString("name");
                                written = object.getString("written");
                                time = object.getString("time");
                                book = object.getString("image");
                                cat = object.getString("category");
                                bid = object.getString("bookID");
                                image = object.getString("book");
                                desc = object.getString("description");

//                                String image_ = "http://catchwo.com/images/" + image;


                            }

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PDFActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(PDFActivity.this);
        requestQueue.add(request);



    }
}