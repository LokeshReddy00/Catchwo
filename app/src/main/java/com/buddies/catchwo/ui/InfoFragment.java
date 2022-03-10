package com.buddies.catchwo.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buddies.catchwo.Adapter.InfoAdapter;
import com.buddies.catchwo.Model.InfoModel;
import com.buddies.catchwo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
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

    RecyclerView info_rec;

    String url = "http://catchwo.com/retriveinfo.php";
    ArrayList<InfoModel> arrayList;
    InfoModel model;
    InfoAdapter adapter;

    private static final int TOTAL_ITEM_EACH_LOAD = 8;
    private int currentPage = 1;
    Button more;
    long initial;
    private TextView nothing;

    LottieAnimationView loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_info, container, false);


        info_rec = view.findViewById(R.id.info_rec);
        loading = view.findViewById(R.id.loading);
        more = view.findViewById(R.id.more);
        nothing = view.findViewById(R.id.nothing);

        arrayList = new ArrayList<>();
        adapter = new InfoAdapter(getActivity(),arrayList);
        info_rec.setAdapter(adapter);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    i++;
                }
                initial = i;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LoadInfo();

        more.setOnClickListener(v -> loadMoreData());
        return view;
    }

    private void loadMoreData() {
        currentPage++;
        LoadInfo();
    }

    private void LoadInfo() {

        FirebaseDatabase.getInstance().getReference().child("info").limitToLast(currentPage*TOTAL_ITEM_EACH_LOAD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();

                for(DataSnapshot ds: snapshot.getChildren()){

                    InfoModel m = ds.getValue(InfoModel.class);

                    arrayList.add(m);


                    adapter = new InfoAdapter(getActivity(),arrayList);
                    info_rec.setAdapter(adapter);

                    if(adapter.getItemCount() == 0){
                        loading.setVisibility(View.GONE);
                        info_rec.setVisibility(View.GONE);
                        nothing.setVisibility(View.VISIBLE);
                    }else {
                        loading.setVisibility(View.GONE);
                        info_rec.setVisibility(View.VISIBLE);
                        nothing.setVisibility(View.GONE);
                        if(adapter.getItemCount() == initial){
                            more.setVisibility(View.GONE);
                            currentPage--;
                        }else {
                            more.setVisibility(View.VISIBLE);
                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
//                            String title = object.getString("title");
//                            String desc = object.getString("description");
//                            String image = object.getString("image");
//                            String link = object.getString("link");
//
////                            String urlImage= "Image folder link"+image;
//
//
//
//                            model = new InfoModel(id,image,title,desc,link);
//                            arrayList.add(model);
//                            adapter.notifyDataSetChanged();
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
}