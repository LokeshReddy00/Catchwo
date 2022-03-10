package com.buddies.catchwo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp2ndClass extends AppCompatActivity {

    ImageView backBtn;
    Button next;
    TextView titleText, slideText;
    RadioGroup radioGroup;
    int selectedGender;
    private RadioButton radioSexButton;
    DatePicker datePicker;
    LinearLayout login;
    String name,sur,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up2nd_class);

        backBtn = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup_next_button);
        login = findViewById(R.id.signup_login_button);
        titleText = findViewById(R.id.signup_title_text);
        slideText = findViewById(R.id.signup_slide_text);
        radioGroup = findViewById(R.id.radio_group);
        datePicker = findViewById(R.id.age_picker);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginScreen.class));
                finish();
            }
        });

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        password = intent.getStringExtra("pass");
        email = intent.getStringExtra("email");
        sur = intent.getStringExtra("sur");

    }

    public void call3rdSigupScreen(View view) {

        selectedGender = radioGroup.getCheckedRadioButtonId();
        radioSexButton=(RadioButton)findViewById(selectedGender);

        Intent intent = new Intent(getApplicationContext(), SignUp3rdClass.class);
        intent.putExtra("gender",radioSexButton.getText().toString());
        intent.putExtra("sur",sur);
        intent.putExtra("email",email);
        intent.putExtra("name",name);
        intent.putExtra("pass",password);
        intent.putExtra("dob",datePicker.getDayOfMonth()+ "/"+ datePicker.getMonth() + "/"+ datePicker.getYear());


        //Add Transition and call next activity
        Pair[] pairs = new Pair[5];
        pairs[0] = new Pair(backBtn, "transition_back_arrow_btn");
        pairs[1] = new Pair(next, "transition_next_btn");
        pairs[2] = new Pair(login, "transition_login_btn");
        pairs[3] = new Pair(titleText, "transition_title_text");
        pairs[4] = new Pair(slideText, "transition_slide_text");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp2ndClass.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }



    }


    public void backPress(View view) {
        onBackPressed();
    }
}