package com.buddies.catchwo.ui.gallery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buddies.catchwo.Adapter.ChatUsersAdapter;
import com.buddies.catchwo.BookChat;
import com.buddies.catchwo.Model.FriendModel;
import com.buddies.catchwo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {


   EditText search;
   RecyclerView rec_chat;
   String url = "http://catchwo.com/retrivefriends.php";
   ArrayList<FriendModel> arrayList;
   ChatUsersAdapter adapter;
   FriendModel model;
   private LottieAnimationView dropdown_menu;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        rec_chat = root.findViewById(R.id.rec_chat);
        search = root.findViewById(R.id.search);
        dropdown_menu = root.findViewById(R.id.dropdown_menu);

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

        dropdown_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BookChat.class));
            }
        });

        arrayList = new ArrayList<>();
        adapter = new ChatUsersAdapter(getActivity(),arrayList);
        rec_chat.setAdapter(adapter);

        LoadChats();


        return root;
    }

    private void LoadChats() {



//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onResponse(String response) {
//                arrayList.clear();
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
//                            String id = object.getString("id");
//                            String uidto = object.getString("uidto");
//                            String image = object.getString("image");
//                            String name = object.getString("name");
//                            String uidfrom = object.getString("uidfrom");
//                            String senttime = object.getString("senttime");
//                            String acctime = object.getString("acctime");
//                            String task = object.getString("task");
//
////                            String urlImage= "Image folder link"+image;
//
//
//                            if(uidfrom.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && task.equals("Accepted")){
//                                model = new FriendModel(id,uidto,image,name,uidfrom,senttime,acctime,task);
//                                arrayList.add(model);
//                                adapter.notifyDataSetChanged();
//                            }
//
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

        FirebaseDatabase.getInstance().getReference().child("Friends").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){


                    if (ds.child("task").getValue().equals("Accepted")){
                        FriendModel model = ds.getValue(FriendModel.class);
                        arrayList.add(model);
                        adapter = new ChatUsersAdapter(getActivity(),arrayList);
                        rec_chat.setAdapter(adapter);
                    }




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}