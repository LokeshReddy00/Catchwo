package com.buddies.catchwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.buddies.catchwo.Adapter.BookAdapter;
import com.buddies.catchwo.Adapter.CommitteeAdapter;
import com.buddies.catchwo.Adapter.FriendAdapter;
import com.buddies.catchwo.Adapter.PostAdapter;
import com.buddies.catchwo.Adapter.ReelsAdapter;
import com.buddies.catchwo.Model.BookModel;
import com.buddies.catchwo.Model.CommitteeModel;
import com.buddies.catchwo.Model.FriendModel;
import com.buddies.catchwo.Model.PostModel;
import com.buddies.catchwo.Model.ReelsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {


    NestedScrollView rec_lay;
    RecyclerView rec;
    EditText search;

    BookAdapter adapter;
    ArrayList<BookModel> arrayList;
    PostAdapter Padapter;
    ArrayList<PostModel> ParrayList;

    ArrayList<CommitteeModel> CarrayList;
    CommitteeAdapter Cadapter;

    ViewPager2 viewPager;
    ReelsAdapter Radapter;
    ArrayList<ReelsModel> RarrayList;

    ArrayList<FriendModel> FarrayList;
    FriendAdapter Fadapter;

    LottieAnimationView loading;
    CardView cardView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        rec = findViewById(R.id.rec);
        cardView2 = findViewById(R.id.cardView2);
        rec_lay = findViewById(R.id.rec_lay);
        search = findViewById(R.id.search);
        loading = findViewById(R.id.loading);
        viewPager = findViewById(R.id.viewPager);


        Intent intent = getIntent();
        String value = intent.getStringExtra("value");
        String uid = intent.getStringExtra("uid");

        if(value.equals("book")){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(UserActivity.this, 2);
            rec.setLayoutManager(gridLayoutManager);
            viewPager.setVisibility(View.GONE);
            rec.setVisibility(View.VISIBLE);

            arrayList = new ArrayList<>();

            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    adapter.getFilter().filter(s);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            LoadBooks(uid);
        }else
            if(value.equals("post")){
            ParrayList = new ArrayList<>();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rec.setLayoutManager(linearLayoutManager);
            viewPager.setVisibility(View.GONE);
            rec.setVisibility(View.VISIBLE);

            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    Padapter.getFilter().filter(s);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            LoadPost(uid);


        }else
            if(value.equals("save")){
            ParrayList = new ArrayList<>();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rec.setLayoutManager(linearLayoutManager);
            viewPager.setVisibility(View.GONE);
            rec.setVisibility(View.VISIBLE);

            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    Padapter.getFilter().filter(s);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            LoadSavedPost(uid);


        }else
            if(value.equals("comm")){
            CarrayList = new ArrayList<>();

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rec.setLayoutManager(linearLayoutManager);
            viewPager.setVisibility(View.GONE);
            rec.setVisibility(View.VISIBLE);

            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    Cadapter.getFilter().filter(s);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            LoadComm(uid);

        }else
            if(value.equals("sur")){

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rec.setLayoutManager(linearLayoutManager);
            viewPager.setVisibility(View.GONE);
            rec.setVisibility(View.VISIBLE);
        } else
            if(value.equals("friend")){
            FarrayList = new ArrayList<>();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rec.setLayoutManager(linearLayoutManager);
            viewPager.setVisibility(View.GONE);
            rec.setVisibility(View.VISIBLE);

            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    Fadapter.getFilter().filter(s);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            LoadFriends(uid);

        } else  if(value.equals("cat")){
            FarrayList = new ArrayList<>();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rec.setLayoutManager(linearLayoutManager);
            viewPager.setVisibility(View.GONE);
            rec.setVisibility(View.VISIBLE);

            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    Fadapter.getFilter().filter(s);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            LoadFriend(uid);

        } else if(value.equals("reels")){
            RarrayList = new ArrayList<>();
            viewPager.setVisibility(View.VISIBLE);
            rec.setVisibility(View.GONE);
            cardView2.setVisibility(View.GONE);

            loadReels(uid);

        }else if(value.equals("prof")){
            RarrayList = new ArrayList<>();
            viewPager.setVisibility(View.GONE);
            rec.setVisibility(View.VISIBLE);
            cardView2.setVisibility(View.GONE);

            loadProf(uid);

        }

    }

    private void LoadFriend(String uid) {

        FirebaseDatabase.getInstance().getReference().child("catchers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren()){
                    FarrayList.clear();
                    if(ds.child("uidto").getValue().equals(uid)){

                        FriendModel model = ds.getValue(FriendModel.class);
                        FarrayList.add(model);

                        Fadapter = new FriendAdapter(UserActivity.this,FarrayList);
                        rec.setAdapter(Fadapter);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void loadProf(String uid) {

        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Prof").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void LoadSavedPost(String uid) {

        ParrayList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child("Saved").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ParrayList.clear();

                for(DataSnapshot ds: snapshot.getChildren()){

//                    if(ds.child("uid").getValue().equals(uid)) {

                        PostModel postModel = ds.getValue(PostModel.class);
                        ParrayList.add(postModel);
                        Padapter = new PostAdapter(UserActivity.this, ParrayList);
                        rec.setAdapter(Padapter);
                        loading.setVisibility(View.GONE);
//                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
            }
        });

    }

    private void LoadFriends(String uid) {


        FirebaseDatabase.getInstance().getReference().child("catchers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren()){
                    FarrayList.clear();
                    if(ds.child("uidfrom").getValue().equals(uid)){

                        FriendModel model = ds.getValue(FriendModel.class);
                        FarrayList.add(model);

                        Fadapter = new FriendAdapter(UserActivity.this,FarrayList);
                        rec.setAdapter(Fadapter);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadReels(String uid) {

        FirebaseDatabase.getInstance().getReference().child("reels").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){

                    if(ds.child("uid").getValue().equals(uid)) {
                        ReelsModel model = ds.getValue(ReelsModel.class);
                        RarrayList.add(model);

                        Radapter = new ReelsAdapter(UserActivity.this, RarrayList);
                        viewPager.setAdapter(Radapter);
                        loading.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void LoadComm(String uid) {

        FirebaseDatabase.getInstance().getReference().child("Committee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CarrayList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){

                    if(ds.child("uid").getValue().equals(uid)) {
                        CommitteeModel model = ds.getValue(CommitteeModel.class);
                        CarrayList.add(model);
                        Cadapter = new CommitteeAdapter(UserActivity.this, CarrayList);
                        rec.setAdapter(Cadapter);
                        loading.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void LoadPost(String uid) {
        ParrayList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ParrayList.clear();

                for(DataSnapshot ds: snapshot.getChildren()){

                    if(ds.child("uid").getValue().equals(uid)) {

                        PostModel postModel = ds.getValue(PostModel.class);
                        ParrayList.add(postModel);
                        Padapter = new PostAdapter(UserActivity.this, ParrayList);
                        rec.setAdapter(Padapter);
                        loading.setVisibility(View.GONE);
                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
            }
        });


    }

    private void LoadBooks(String uid) {

        FirebaseDatabase.getInstance().getReference().child("Books").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    arrayList.clear();

                    BookModel model = ds.getValue(BookModel.class);
                    arrayList.add(model);
                    adapter = new BookAdapter(UserActivity.this, arrayList);
                    rec.setAdapter(adapter);
                    loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
            }
        });
    }
}