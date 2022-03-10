package com.buddies.catchwo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeActivity extends AppCompatActivity {

    LinearLayout book,sur,info,meo,fri,pro,com,club,set,help,sup,about,report,logout,profile_lay;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String url = "http://catchwo.com/retriverdate.php";
    TextView name,email;
    ImageView profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        logout = findViewById(R.id.logout);
        profile_lay = findViewById(R.id.profile_lay);
        book = findViewById(R.id.book);
        info = findViewById(R.id.info);
        sur = findViewById(R.id.sur);
        meo = findViewById(R.id.meo);
        fri = findViewById(R.id.fri);
        pro = findViewById(R.id.pro);
        com = findViewById(R.id.com);
        club = findViewById(R.id.club);
        set = findViewById(R.id.set);
        help = findViewById(R.id.help);
        sup = findViewById(R.id.sup);
        about = findViewById(R.id.about);
        report = findViewById(R.id.report);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        profile = findViewById(R.id.profile);

        loadData();


        book.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,BooksActivity.class);
            intent.putExtra("act","book");
            startActivity(intent);
        });

        club.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,InformationActivity.class);
            intent.putExtra("act","club");
            startActivity(intent);
        });

        com.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,InformationActivity.class);
            intent.putExtra("act","comm");
            startActivity(intent);
        });

        meo.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,InformationActivity.class);
            intent.putExtra("act","meo");
            startActivity(intent);
        });

        set.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,InformationActivity.class);
            intent.putExtra("act","set");
            startActivity(intent);
        });

        sur.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,InformationActivity.class);
            intent.putExtra("act","sur");
            startActivity(intent);
        });

        pro.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,InformationActivity.class);
            intent.putExtra("act","pro");
            startActivity(intent);
        });

        fri.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,InformationActivity.class);
            intent.putExtra("act","friend");
            startActivity(intent);
        });

        info.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,InformationActivity.class);
            intent.putExtra("act","link");
            startActivity(intent);
        });

        about.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,WebActivity.class);
            intent.putExtra("url","http://catchwo.com/about.html");
            startActivity(intent);
        });

        help.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,WebActivity.class);
            intent.putExtra("url","https://play.google.com/store/apps/dev?id=9103934959810891664");
            startActivity(intent);
        });

        sup.setOnClickListener(v -> {

            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/dev?id=9103934959810891664"));
            startActivity(intent);
        });

        findViewById(R.id.img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,WebActivity.class);
                intent.putExtra("url","http://propertysr.com/");
                startActivity(intent);
            }
        });

        report.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW
                    , Uri.parse("mailto: shivakumarkoshika229@gmail.com"));
            startActivity(intent);
        });

        profile_lay.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this,InformationActivity.class);
            intent.putExtra("act","profile");
            startActivity(intent);
        });
        logout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Logout")
                    .setMessage("Are you sure to logout")
                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.signOut();
                            startActivity(new Intent(HomeActivity.this,LoginScreen.class));
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(HomeActivity.this, "No", Toast.LENGTH_SHORT).show();
                }
            });

            builder.create();
            builder.show();

        });


    }

    private void loadData() {

//        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onResponse(String response) {
////                arrayList.clear();
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("sucess");
//                    JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//                    if (success.equals("1")) {
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject object = jsonArray.getJSONObject(i);
//
//                            String id_sql = object.getString("id");
//                            String name_sql = object.getString("name");
//                            String image_sql = object.getString("image");
//                            String uid = object.getString("uid");
//                            String time_sql = object.getString("time");
//                            String email_sql = object.getString("email");
//                            String dob_sql = object.getString("dob");
//                            String gender_sql = object.getString("gender");
//                            String phone_sql = object.getString("phone");
//                            String cover_sql = object.getString("cover");
//
//                            String image_ = "http://catchwo.com/images/" + image_sql;
//
//
//                            if(uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
//
//                                name.setText(name_sql);
//                                email.setText(email_sql);
//
//                                Glide.with(HomeActivity.this).load(image_).into(profile);
//
//                            }
//
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
//        requestQueue.add(request);


        FirebaseDatabase.getInstance().getReference()
                .child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(""+snapshot.child("name").getValue());
                                email.setText(""+snapshot.child("email").getValue());

                Glide.with(HomeActivity.this).load(""+snapshot.child("pic").getValue()).placeholder(R.drawable.name).into(profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }
}