package com.buddies.catchwo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR).withListener(new MultiplePermissionsListener(){

            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                onStart();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();


    }

    @Override
    protected void onStart() {
        super.onStart();


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (user == null) {
                Intent intent = new Intent(SplashScreen.this,WelComeScreen.class);
                startActivity(intent);
                finish();
            }else {
            Intent intent = new Intent(SplashScreen.this,HomeScreen.class);
            startActivity(intent);
            }
        },4000);


    }

}