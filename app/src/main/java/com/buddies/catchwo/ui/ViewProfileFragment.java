package com.buddies.catchwo.ui;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.buddies.catchwo.AddReels;
import com.buddies.catchwo.AppointScreen;
import com.buddies.catchwo.EditActivity;
import com.buddies.catchwo.HomeActivity;
import com.buddies.catchwo.InformationActivity;
import com.buddies.catchwo.Model.Users;
import com.buddies.catchwo.ProfileActivity;
import com.buddies.catchwo.R;
import com.buddies.catchwo.Support.ApiClient;
import com.buddies.catchwo.Support.ApiInterface;
import com.buddies.catchwo.UserActivity;
import com.buddies.catchwo.WelComeScreen;
import com.buddies.catchwo.ui.home.HomeFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewProfileFragment newInstance(String param1, String param2) {
        ViewProfileFragment fragment = new ViewProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ImageView cover,profile;
    private TextView name,email;
    ReadMoreTextView about;
    CardView cardView4,edit;
    LottieAnimationView verified;
    String url = "http://catchwo.com/retriverdate.php";


    Button books,post,reels,comm,sur,save,friends,cat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);

        name = view.findViewById(R.id.name);

        cover = view.findViewById(R.id.cover);
        cat = view.findViewById(R.id.cat);
        profile = view.findViewById(R.id.profile);
        email = view.findViewById(R.id.email);
        about = view.findViewById(R.id.about);
        cardView4 = view.findViewById(R.id.cardView4);
        verified = view.findViewById(R.id.verified);
        edit = view.findViewById(R.id.edit);
        friends = view.findViewById(R.id.friend);
        save = view.findViewById(R.id.save);
        sur = view.findViewById(R.id.sur);
        comm = view.findViewById(R.id.comm);
        reels = view.findViewById(R.id.reels);
        post = view.findViewById(R.id.posts);
        books = view.findViewById(R.id.books);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EditActivity.class);
                intent.putExtra("edit","text");
                startActivity(intent);
            }
        });

        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), UserActivity.class);
                intent1.putExtra("value","book");
                intent1.putExtra("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent1);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),UserActivity.class);
                intent1.putExtra("value","post");
                intent1.putExtra("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent1);
            }
        });

        comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),UserActivity.class);
                intent1.putExtra("value","comm");
                intent1.putExtra("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent1);
            }
        });

        reels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),UserActivity.class);
                intent1.putExtra("value","reels");
                intent1.putExtra("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent1);
            }
        });

        sur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),UserActivity.class);
                intent1.putExtra("value","sur");
                intent1.putExtra("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent1);
            }
        });

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),UserActivity.class);
                intent1.putExtra("value","friend");
                intent1.putExtra("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent1);
            }
        });

        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),UserActivity.class);
                intent1.putExtra("value","cat");
                intent1.putExtra("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent1);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),UserActivity.class);
                intent1.putExtra("value","save");
                intent1.putExtra("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent1);
            }
        });







        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppointScreen.class);
                Pair[] pairs = new Pair[1];

                pairs[0] = new Pair<View,String>(cardView4, "transition_appoint");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditActivity.class);
                intent.putExtra("edit","profile");
                Pair[] pairs = new Pair[1];

                pairs[0] = new Pair<View,String>(profile, "transition_image");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditActivity.class);
                intent.putExtra("edit","cover");
                Pair[] pairs = new Pair[1];

                pairs[0] = new Pair<View,String>(profile, "transition_image");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });



        LoadData();

        return view;
    }

    private void LoadData() {

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                name.setText(""+snapshot.child("name").getValue());
                email.setText(""+snapshot.child("email").getValue());
                about.setText(""+snapshot.child("about").getValue());
                Glide.with(getActivity()).load(""+snapshot.child("pic").getValue()).placeholder(R.drawable.name).into(profile);
                Glide.with(getActivity()).load(""+snapshot.child("cover").getValue()).placeholder(R.drawable.name).into(cover);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
//                            String about_sql = object.getString("about");
//
//
//                            String image_ = "http://catchwo.com/images/" + image_sql;
//
//                            if(uid.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())){
//
//                                name.setText(name_sql);
//                                email.setText(email_sql);
//                                about.setText(about_sql);
//
//                                Glide.with(getActivity()).load(image_).into(profile);
//
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
//                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.add(request);


    }

    @Override
    public void onStart() {
        super.onStart();

        LoadData();
    }
}