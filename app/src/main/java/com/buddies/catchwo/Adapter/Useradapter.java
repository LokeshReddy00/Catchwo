package com.buddies.catchwo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buddies.catchwo.Filter.UserSearch;
import com.buddies.catchwo.Model.FriendModel;
import com.buddies.catchwo.Model.Users;
import com.buddies.catchwo.ProfileActivity;
import com.buddies.catchwo.R;
import com.buddies.catchwo.SignUp3rdClass;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Useradapter extends RecyclerView.Adapter<Useradapter.Holder> implements Filterable {

    Context context;
    public ArrayList<Users> arrayList,FilterList;
    private UserSearch filter;

    public Useradapter(Context context, ArrayList<Users> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.FilterList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_row,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.name.setText(arrayList.get(position).getName());
        String image = arrayList.get(position).getImage();
        String name = arrayList.get(position).getName();

        //profile Images
        Glide.with(context).load(arrayList.get(position).getImage()).placeholder(R.drawable.name).into(holder.image);


        String uid = arrayList.get(position).getUid();
        holder.catcho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("uid",uid);
                intent.putExtra("type","view");
                context.startActivity(intent);
            }
        });

        CheckUserRequest(holder.request,uid);
        CheckUserFriend(holder.request,uid);



        holder.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cdate = Calendar.getInstance();

                SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

                final String savedate = currentdates.format(cdate.getTime());

                Calendar ctime = Calendar.getInstance();
                SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

                final String savetime = currenttime.format(ctime.getTime());

                String time = savedate + ":" + savetime;

                FirebaseDatabase.getInstance().getReference()
                        .child("Users").child( FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        holder.name_ = ""+snapshot.child("name").getValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if(holder.request.getText().toString().equals("Request")){
                    sendRequest(image,uid,name, FirebaseAuth.getInstance().getCurrentUser().getUid(),time,holder.request,holder.name_);
                }else  if(holder.request.getText().toString().equals("Delete Request")){
                    DeleteRequest(image,uid,name, FirebaseAuth.getInstance().getCurrentUser().getUid(),time,holder.request,holder.name_);
                }



            }
        });


    }

    private void CheckUserFriend(Button request, String uid) {

        FirebaseDatabase.getInstance().getReference().child("Friends").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String req = ""+snapshot.child("task").getValue();

                if(req.equals("Requested")){

                    request.setVisibility(View.VISIBLE);

                }else if(req.equals("Accepted")){
                    request.setVisibility(View.GONE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void CheckUserRequest(Button requests, String uid) {

//        StringRequest request = new StringRequest(Request.Method.POST, "http://catchwo.com/retrivefriends.php", new Response.Listener<String>() {
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
//                             if(uidfrom.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && uidto.equals(uid)){
//                                requests.setEnabled(false);
//                                requests.setText("Requested");
////                                requests.setVisibility(View.GONE);
//                            }else {
//                                    requests.setEnabled(true);
//                                    requests.setText("Request");
////                                requests.setVisibility(View.VISIBLE);
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
//                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.add(request);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Queries").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
//                    requests.setVisibility(View.GONE);
                    requests.setText("Delete Request");
                }else {
//                    requests.setVisibility(View.VISIBLE);
                    requests.setText("Request");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void sendRequest(String image, String uid, String name, String uid1, String time, Button requests,final String name_) {

        Long timestamp = System.currentTimeMillis();
        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String times = savedate + ":" + savetime;

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uidto", uid);
        hashMap.put("image", image);
        hashMap.put("name", name);
        hashMap.put("uidfrom", uid1);
        hashMap.put("senttime", time);
        hashMap.put("id", String.valueOf(timestamp));
        hashMap.put("acctime", "");
        hashMap.put("task", "Requested");

//        FirebaseDatabase.getInstance().getReference().child("FRequests").child(String.valueOf(timestamp)).updateChildren(hashMap);
        FirebaseDatabase.getInstance().getReference().child("Friends").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uid).updateChildren(hashMap);
        FirebaseDatabase.getInstance().getReference().child("catchers").child(String.valueOf(timestamp)).updateChildren(hashMap);


        FirebaseDatabase.getInstance().getReference().child("Users").child(uid1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("title",snapshot.child("name").getValue() + " Sent you a friend request");
                hashMap1.put("uid",uid1);
                hashMap1.put("id",String.valueOf(timestamp));
                hashMap1.put("type","Request");
                hashMap1.put("time",times);

                FirebaseDatabase.getInstance().getReference().child("Notifications").child(uid).child(String.valueOf(timestamp)).updateChildren(hashMap1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Queries").child(uid1).child(uid).setValue(true);

//        StringRequest request = new StringRequest(Request.Method.POST, "http://catchwo.com/communicationdb.php", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
//                requests.setEnabled(false);
//                requests.setText("Requested");
//
//
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//                reference.child("Queries").child(uid1).child(uid).setValue(true);
//
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        }){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//
//                Map<String, String> hashMap = new HashMap<String, String>();
//                hashMap.put("uidto", uid);
//                hashMap.put("image", image);
//                hashMap.put("name", name);
//                hashMap.put("uidfrom", uid1);
//                hashMap.put("senttime", time);
//                hashMap.put("acctime", "");
//                hashMap.put("task", "Requested");
//
//
//                return hashMap;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.add(request);

    }

    private void DeleteRequest(String image, String uid, String name, String uid1, String time, Button requests,final String name_) {

        Long timestamp = System.currentTimeMillis();
        Calendar cdate = Calendar.getInstance();

        SimpleDateFormat currentdates = new SimpleDateFormat("dd-MMMM-yyyy");

        final String savedate = currentdates.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");

        final String savetime = currenttime.format(ctime.getTime());

        String times = savedate + ":" + savetime;

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uidto", uid);
        hashMap.put("image", image);
        hashMap.put("name", name);
        hashMap.put("uidfrom", uid1);
        hashMap.put("senttime", time);
        hashMap.put("id", String.valueOf(timestamp));
        hashMap.put("acctime", "");
        hashMap.put("task", "Requested");

//        FirebaseDatabase.getInstance().getReference().child("FRequests").child(String.valueOf(timestamp)).updateChildren(hashMap);
        FirebaseDatabase.getInstance().getReference().child("Friends").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uid).removeValue();
        FirebaseDatabase.getInstance().getReference().child("catchers").child(String.valueOf(timestamp)).removeValue();

//        FirebaseDatabase.getInstance().getReference().child("Notifications").child(uid).child(String.valueOf(timestamp)).removeValue();

        FirebaseDatabase.getInstance().getReference().child("Users").child(uid1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("title",snapshot.child("name").getValue() + " Sent you a friend request");
                hashMap1.put("uid",uid1);
                hashMap1.put("id",String.valueOf(timestamp));
                hashMap1.put("type","Request");
                hashMap1.put("time",times);

                FirebaseDatabase.getInstance().getReference().child("Notifications").child(uid).child(String.valueOf(timestamp)).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Queries").child(uid1).child(uid).removeValue();

//        StringRequest request = new StringRequest(Request.Method.POST, "http://catchwo.com/communicationdb.php", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
//                requests.setEnabled(false);
//                requests.setText("Requested");
//
//
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//                reference.child("Queries").child(uid1).child(uid).setValue(true);
//
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        }){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//
//                Map<String, String> hashMap = new HashMap<String, String>();
//                hashMap.put("uidto", uid);
//                hashMap.put("image", image);
//                hashMap.put("name", name);
//                hashMap.put("uidfrom", uid1);
//                hashMap.put("senttime", time);
//                hashMap.put("acctime", "");
//                hashMap.put("task", "Requested");
//
//
//                return hashMap;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.add(request);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new UserSearch(this,FilterList);
        }

        return filter;
    }

    public class Holder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView name;
        private Button catcho,request;
        private String name_;


        public Holder(@NonNull View itemView) {
            super(itemView);

            request = itemView.findViewById(R.id.request);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            catcho = itemView.findViewById(R.id.catcho);
        }
    }
}
