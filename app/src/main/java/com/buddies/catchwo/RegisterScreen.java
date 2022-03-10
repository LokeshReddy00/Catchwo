package com.buddies.catchwo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RegisterScreen extends AppCompatActivity {

    ImageView backBtn;
    Button next;
    LinearLayout login;
    TextView titleText,slideText;
    EditText password,sur,name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        backBtn = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup_next_button);
        login = findViewById(R.id.signup_login_button);
        titleText = findViewById(R.id.signup_title_text);
        slideText = findViewById(R.id.signup_slide_text);
        password = findViewById(R.id.password);
        sur = findViewById(R.id.sur);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
    }

    public void callNextSigupScreen(View view) {

        Intent intent = new Intent(getApplicationContext(), SignUp2ndClass.class);
        intent.putExtra("sur",sur.getText().toString());
        intent.putExtra("email",email.getText().toString());
        intent.putExtra("name",name.getText().toString());
        intent.putExtra("pass",password.getText().toString());


        //Add Shared Animation
        Pair[] pairs = new Pair[5];
        pairs[0] = new Pair(backBtn, "transition_back_arrow_btn");
        pairs[1] = new Pair(next, "transition_next_btn");
        pairs[2] = new Pair(login, "transition_login_btn");
        pairs[3] = new Pair(titleText, "transition_title_text");
        pairs[4] = new Pair(slideText, "transition_slide_text");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterScreen.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }

    public void callLoginFromSignUp(View view) {
        startActivity(new Intent(getApplicationContext(), LoginScreen.class));
        finish();
    }

    public void backPress(View view) {
        onBackPressed();
    }
}