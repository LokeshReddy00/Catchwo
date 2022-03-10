package com.buddies.catchwo;

import androidx.annotation.NonNull;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginScreen extends AppCompatActivity {

    private EditText email,password;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    AlertDialog dialog;
    Button forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sign In..... ");


        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.login_phone_number_editText);
        password = findViewById(R.id.login_password_editText);
        forgot = findViewById(R.id.forgot);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(LoginScreen.this, "hello", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(LoginScreen.this, android.R.style.Theme_Material_Dialog_Alert);

        } else {
            alert = new AlertDialog.Builder(LoginScreen.this);
        }

        @SuppressLint("InflateParams") View root = LayoutInflater.from(LoginScreen.this).inflate(R.layout.forgot_pass, null);

                alert.setView(root);

                EditText Name = root.findViewById(R.id.login_email_editText);
                Button apply = root.findViewById(R.id.conform);

                apply.setOnClickListener(v1 -> {

            if(TextUtils.isEmpty(Name.getText().toString())){
                Name.setError("Please fill the email");
            }
            else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(Name.getText().
                toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //  Log.d(TAG, "Email sent.");
                                    Toast.makeText(getApplicationContext(), "Email As been Sent", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                }else {
                                    Toast.makeText(getApplicationContext(),"Email Doesn't found",Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
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
        });


    }

//    public void callForgetPassword(View view) {
//
//
//
//        AlertDialog.Builder alert;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
//
//        } else {
//            alert = new AlertDialog.Builder(this);
//        }
//
//        View root = LayoutInflater.from(this).inflate(R.layout.forgot_pass, null);
//
//
//        EditText Name = view.findViewById(R.id.login_email_editText);
//        Button apply = view.findViewById(R.id.conform);
//
//        apply.setOnClickListener(v -> {
//
////            if(!TextUtils.isEmpty(Name.getText().toString())){
////                Name.setError("Please fill the email");
////            }
////            else {
////                FirebaseAuth.getInstance().sendPasswordResetEmail(Name.getText().
////                toString())
////                        .addOnCompleteListener(new OnCompleteListener<Void>() {
////                            @Override
////                            public void onComplete(@NonNull Task<Void> task) {
////                                if (task.isSuccessful()) {
////                                    //  Log.d(TAG, "Email sent.");
////
////                                    Toast.makeText(getApplicationContext(), "Email As been Sent", Toast.LENGTH_SHORT).show();
////                                    dialog.dismiss();
////
////                                }
////                            }
////                        });
////            }
//
//        });
//
//
//
//
//        alert.setView(root);
//
//        alert.setCancelable(true);
//
//
//
//        dialog = alert.create();
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        dialog.show();
//
//    }

    public void letTheUserLoggedIn(View view) {



        if(TextUtils.isEmpty(email.getText().toString()) && TextUtils.isEmpty(password.getText().toString())){
            Toast.makeText(LoginScreen.this, "Please Fill the Login Details", Toast.LENGTH_SHORT).show();
        }else {

            progressDialog.setMessage("Please wait Login ... ");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(LoginScreen.this, SplashScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(LoginScreen.this, "We Didn't find any Records!!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    public void callSignUpFromLogin(View view) {

        startActivity(new Intent(LoginScreen.this, RegisterScreen.class));
        finish();

    }

    public void callForgetPassword(View view) {

        if(!TextUtils.isEmpty(email.getText().toString())){
            FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(LoginScreen.this, "Please check your mail", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginScreen.this, "No Data found", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this, "Please fill the email to sent the reset link", Toast.LENGTH_SHORT).show();
        }



    }
}