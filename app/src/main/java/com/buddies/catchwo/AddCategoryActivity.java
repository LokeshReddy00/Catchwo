package com.buddies.catchwo;

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
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddCategoryActivity extends AppCompatActivity {

    private static final String TAG = "ADD";
    private AutoCompleteTextView cat;
    private TextInputEditText book_name,writtenby,desc_book;
    private RadioButton  paid,free;
    private Button select_pdf,cover_img,upload;
    private TextView conform;
    private ImageView cover;
    private ScrollView scrollView5;
    Uri selectedUri;
    Bitmap bitmap;
    Uri path ;
    String encodeImage,encodeBook,time;

    ProgressDialog progressDialog;

    String url = "http://catchwo.com/uploadcompletebook.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

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
        select_pdf = findViewById(R.id.select_pdf);
        cover_img = findViewById(R.id.cover_img);
        conform = findViewById(R.id.conform);
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

        select_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPdf();
            }
        });

        conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddCategoryActivity.this, encodeBook, Toast.LENGTH_SHORT).show();
            }
        });

        if(value.equals("complete")){
            select_pdf.setVisibility(View.VISIBLE);
            conform.setVisibility(View.VISIBLE);
            upload.setText("Upload");

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(cat.getText().toString()) &&!TextUtils.isEmpty(book_name.getText().toString()) &&!TextUtils.isEmpty(writtenby.getText().toString()) &&!TextUtils.isEmpty(desc_book.getText().toString())
                    && !encodeBook.isEmpty()){
                        saveBookToServer();
                    }else {
                        Toast.makeText(AddCategoryActivity.this, "Please Fill all the details", Toast.LENGTH_SHORT).show();
                    }


                }
            });

        }else if(value.equals("ongoing")){
            select_pdf.setVisibility(View.GONE);
            conform.setVisibility(View.GONE);
            upload.setText("Continue");
        }


    }

    private void saveBookToServer() {

        progressDialog.setMessage("Please wait... ");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        Long times = System.currentTimeMillis();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


//                onBackPressed();

                Intent i = new Intent(AddCategoryActivity.this,PDFActivity.class);
                i.putExtra("id",times);
                startActivity(i);
                Toast.makeText(AddCategoryActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(AddCategoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {



                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                hashMap.put("category",cat.getText().toString());
                hashMap.put("time",time);
                hashMap.put("book",encodeBook);
                hashMap.put("bookID", String.valueOf(times));
                hashMap.put("image","encodeImage");
                hashMap.put("name",book_name.getText().toString());
                hashMap.put("written",writtenby.getText().toString());
                hashMap.put("description",desc_book.getText().toString());


                return hashMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddCategoryActivity.this);
        requestQueue.add(request);


    }

    private void selectPdf() {

        Intent chooseFile = new Intent();
        chooseFile.setType("application/pdf");
        chooseFile.setAction(Intent.ACTION_GET_CONTENT);
        chooseFile = Intent.createChooser(chooseFile,"Choose a pdf");
        startActivityForResult(chooseFile, 2);

    }


    private void askPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                ChooseImage();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

                Toast.makeText(AddCategoryActivity.this, "Permission Required", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(AddCategoryActivity.this)
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


        if(resultCode == RESULT_OK) {

                if (requestCode == 2 || data != null || data.getData() != null) {

                    assert data != null;
                    path = data.getData();

                Log.d(TAG, "test: "+path);

                try {
                    InputStream inputStream = AddCategoryActivity.this.getContentResolver().openInputStream(path);
                    byte[] pdfInBytes = new byte[inputStream.available()];
                    inputStream.read(pdfInBytes);
                    encodeBook = android.util.Base64.encodeToString(pdfInBytes, Base64.DEFAULT);
                    Log.d(TAG,encodeBook);
                    conform.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }else {
            Toast.makeText(AddCategoryActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
        }

    }

    private void imageStore(Bitmap bitmap) {


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] imagebytes = stream.toByteArray();

        encodeImage = android.util.Base64.encodeToString(imagebytes, Base64.DEFAULT);

    }

//    private String encodeFileToBase64Binary(File yourFile) {
//        int size = (int) yourFile.length();
//        byte[] bytes = new byte[size];
//        try {
//            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(yourFile));
//            buf.read(bytes, 0, bytes.length);
//            buf.close();
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        String encoded = Base64.encodeToString(bytes,Base64.NO_WRAP);
//        return encoded;
//    }

    @Override
    protected void onStart() {
        super.onStart();

//        conform.setVisibility(View.GONE);
    }
}