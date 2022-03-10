package com.buddies.catchwo.ui;

import android.Manifest;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buddies.catchwo.HomeScreen;
import com.buddies.catchwo.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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


public class AppointFragment extends AppCompatActivity {

    ImageView store;
    Button submit;
    Uri selectedUri;
    Bitmap bitmap;
    String url = "http://catchwo.com/appointformsdata.php";
    String encodeImage;
    private TextInputEditText name, bus, email, phone, address, services,exper,nuber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_fragment);


        //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.activity_appoint_fragment, container, false);

        name = findViewById(R.id.name);
        bus = findViewById(R.id.bus);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        services = findViewById(R.id.services);
        store = findViewById(R.id.store);
        submit = findViewById(R.id.submit);
        nuber = findViewById(R.id.nuber);
        exper = findViewById(R.id.exper);

        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadData();

            }
        });


//        return view;
    }

    private void askPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                ChooseImage();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

                Toast.makeText(AppointFragment.this, "Permission Required", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(AppointFragment.this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

    }


    private void UploadData() {

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


                Intent intent = new Intent(AppointFragment.this, HomeScreen.class);
                startActivity(intent);

                Toast.makeText(AppointFragment.this, response, Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(AppointFragment.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                hashMap.put("name", name.getText().toString());
                hashMap.put("time", time);
                hashMap.put("email", email.getText().toString());
                hashMap.put("business", bus.getText().toString());
                hashMap.put("address", address.getText().toString());
                hashMap.put("phone", phone.getText().toString());
                hashMap.put("services", services.getText().toString());
                hashMap.put("image", encodeImage);
                hashMap.put("experience", exper.getText().toString());

                if(!TextUtils.isEmpty(nuber.getText().toString())){
                    hashMap.put("number", nuber.getText().toString());
                }else {
                    hashMap.put("number","");
                }


                return hashMap;
            }
        };

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("appoint", "Yes");
//        hashMap.put("time", time);
//        hashMap.put("email", email);
//        hashMap.put("dob", dob);
//        hashMap.put("gender", gender);
//        hashMap.put("uid", mAuth.getCurrentUser().getUid());
//        hashMap.put("phone", phone);
//        hashMap.put("pic", "");
//        hashMap.put("cover", "");
//        hashMap.put("appoint", "");
//        hashMap.put("appoints", "");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap);

        RequestQueue requestQueue = Volley.newRequestQueue(AppointFragment.this);
        requestQueue.add(request);
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
                store.setImageBitmap(bitmap);
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
