package com.buddies.catchwo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buddies.catchwo.Support.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hbb20.CountryCodePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignUp3rdClass extends AppCompatActivity {

    CountryCodePicker country_code_picker;
    TextInputEditText signup_phone_number;

    String name,sur,email,password,dob,gender;
    AlertDialog dialog;
    private ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    String url = "http://catchwo.com/userdata.php";
    String phone,time;


    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3rd_class);

        signup_phone_number = findViewById(R.id.phone);
        country_code_picker = findViewById(R.id.country_code_picker);

        Intent intent = getIntent();
        mAuth = FirebaseAuth.getInstance();
        name = intent.getStringExtra("name");
        password = intent.getStringExtra("pass");
        email = intent.getStringExtra("email");
        sur = intent.getStringExtra("sur");
        dob = intent.getStringExtra("dob");
        gender = intent.getStringExtra("gender");

        progressDialog = new ProgressDialog(this);

       phone = country_code_picker.getSelectedCountryCode() + signup_phone_number.getText().toString();

        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        time = savedate + ":" + savetime;




    }

    public void callVerifyOTPScreen(View view) {

//        Toast.makeText(getApplicationContext(), email + password, Toast.LENGTH_SHORT).show();


        progressDialog.setMessage("Please wait Registration... ");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    getToken();




//                    ShowDilag();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(SignUp3rdClass.this, "Please Check your Details!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this :: updateToken);
    }

    private void updateToken(String s) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name",sur +" "+name);
        hashMap.put("time",time);
        hashMap.put("email",email);
        hashMap.put("dob",dob);
        hashMap.put("gender",gender);
        hashMap.put("uid",mAuth.getCurrentUser().getUid());
        hashMap.put("phone",country_code_picker.getSelectedCountryCode() + " " +signup_phone_number.getText().toString());
        hashMap.put("pic","");
        hashMap.put("cover","");
        hashMap.put("appoint","");
        hashMap.put("appoints","");
        hashMap.put("about","");
        hashMap.put(Common.KEY_COLLECTION_TOKEN,s);

        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hashMap);


        DocumentReference userbooking = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(country_code_picker.getSelectedCountryCode() + " " +signup_phone_number.getText().toString());

        userbooking.set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

//                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Verification email sent to " + email,
//                                    Toast.LENGTH_SHORT).show();
////                                 save Data to server //..
//
//                        } else {
////                                    Log.e(TAG, "sendEmailVerification", task.getException());
//                            Toast.makeText(getApplicationContext(),
//                                    "Failed to send verification email.",
//                                    Toast.LENGTH_SHORT).show();
//                            mAuth.signOut();
//
//
//                        }
//
//                    }
//                });

                Intent intent = new Intent(SignUp3rdClass.this,SplashScreen.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp3rdClass.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void UploadData() {


        Long times = System.currentTimeMillis();



        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                getToken();
                Intent intent = new Intent(SignUp3rdClass.this,SplashScreen.class);
                startActivity(intent);

//                Toast.makeText(SignUp3rdClass.this, response.toString(), Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(SignUp3rdClass.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {



                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("uid",mAuth.getCurrentUser().getUid());
                hashMap.put("name",sur +" "+name);
                hashMap.put("time",time);
                hashMap.put("email",email);
                hashMap.put("dob",dob);
                hashMap.put("gender",gender);
                hashMap.put("phone",country_code_picker.getSelectedCountryCode() + " " +signup_phone_number.getText().toString());


                return hashMap;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SignUp3rdClass.this);
        requestQueue.add(request);


    }



    private void ShowDilag() {


        AlertDialog.Builder alert;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(SignUp3rdClass.this, android.R.style.Theme_Material_Dialog_Alert);

        } else {
            alert = new AlertDialog.Builder(SignUp3rdClass.this);
        }

        @SuppressLint("InflateParams") View root = LayoutInflater.from(SignUp3rdClass.this).inflate(R.layout.email_otp, null);

        alert.setView(root);

        EditText Name = root.findViewById(R.id.codeEt);
        Button apply = root.findViewById(R.id.codeSubmitBtn);

        apply.setOnClickListener(v1 -> {

            if(TextUtils.isEmpty(Name.getText().toString())){
                Name.setError("Please fill the email");
            }
            else {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.sendEmailVerification()
                        .addOnCompleteListener(this, new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                // Re-enable button
                                findViewById(R.id.codeSubmitBtn).setEnabled(true);

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                            "Verification email sent to " + Name.getText().toString(),
                                            Toast.LENGTH_SHORT).show();
                                    // save Data to server //..


                                } else {
//                                    Log.e(TAG, "sendEmailVerification", task.getException());
                                    Toast.makeText(getApplicationContext(),
                                            "Failed to send verification email.",
                                            Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();


                                }
                            }
                        });
            }

        });



        alert.setCancelable(true);
        dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    public void backPress(View view) {
        onBackPressed();
    }

    public void login(View view) {

        startActivity(new Intent(getApplicationContext(), LoginScreen.class));
        finish();
    }
}