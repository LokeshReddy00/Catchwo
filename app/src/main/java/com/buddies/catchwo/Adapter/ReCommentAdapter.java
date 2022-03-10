package com.buddies.catchwo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.buddies.catchwo.Model.CommentModel;
import com.buddies.catchwo.R;
import com.buddies.catchwo.ReCommentActivity;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReCommentAdapter extends RecyclerView.Adapter<ReCommentAdapter.Holder> {

    Context context;
    ArrayList<CommentModel> arrayList;

    public ReCommentAdapter(Context context, ArrayList<CommentModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        String uid = arrayList.get(position).getUid();


        StringRequest request = new StringRequest(Request.Method.POST, "http://catchwo.com/retriverdate.php", new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
//                arrayList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("sucess");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String id_sql = object.getString("id");
                            String name_sql = object.getString("name");
                            String image_sql = object.getString("image");
                            String uid2 = object.getString("uid");
                            String time_sql = object.getString("time");
                            String email_sql = object.getString("email");
                            String dob_sql = object.getString("dob");
                            String gender_sql = object.getString("gender");
                            String phone_sql = object.getString("phone");
                            String cover_sql = object.getString("cover");
                            String about_sql = object.getString("about");



                            if(uid2.equals(uid)){

                                String image = "http://catchwo.com/images/"+image_sql;
                                Glide.with(context).load(image).into(holder.profile);
                                holder.user_name.setText(name_sql);

                            }

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

        String id = arrayList.get(position).getId();
        String desc = arrayList.get(position).getComment();


        holder.post_desc.setText(arrayList.get(position).getComment());
        holder.time.setText(arrayList.get(position).getTime());
//        holder.replay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ReCommentActivity.class);
//                intent.putExtra("id",id);
//                intent.putExtra("uid",uid);
//                intent.putExtra("desc",desc);
//                context.startActivity(intent);
//
//
//            }
//        });

        holder.replay.setVisibility(View.GONE);




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private  ImageView profile;
        private TextView user_name,time,replay;
        private ReadMoreTextView post_desc;


        public Holder(@NonNull View itemView) {
            super(itemView);


            replay = itemView.findViewById(R.id.replay);
            profile = itemView.findViewById(R.id.profile);
            user_name = itemView.findViewById(R.id.user_name);
            time = itemView.findViewById(R.id.time);
            post_desc = itemView.findViewById(R.id.post_desc);

        }
    }
}
