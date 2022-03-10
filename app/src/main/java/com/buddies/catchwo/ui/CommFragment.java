package com.buddies.catchwo.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buddies.catchwo.Adapter.CommitteeAdapter;
import com.buddies.catchwo.CreateCommScreen;
import com.buddies.catchwo.Model.CommitteeModel;
import com.buddies.catchwo.Model.Users;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CommFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommFragment newInstance(String param1, String param2) {
        CommFragment fragment = new CommFragment();
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

    private LottieAnimationView lottieAnimationView;
    private RecyclerView comm_rec;
    ArrayList<CommitteeModel> arrayList;
    CommitteeAdapter adapter;
    EditText search;
    CommitteeModel Model;
    private String url = "http://catchwo.com/retrivecommitteeinfo.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_comm, container, false);

        lottieAnimationView  = root.findViewById(R.id.lottieAnimationView);
        comm_rec  = root.findViewById(R.id.comm_rec);
        search  = root.findViewById(R.id.search);
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

        lottieAnimationView.setOnClickListener(v -> {
           Intent intent = new Intent(getActivity(), CreateCommScreen.class);
           startActivity(intent);
        });

        loadCommittee();

        return root;
    }

    private void loadCommittee() {

        FirebaseDatabase.getInstance().getReference().child("Committee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){

                    CommitteeModel model = ds.getValue(CommitteeModel.class);
                    arrayList.add(model);
                    adapter = new CommitteeAdapter(getActivity(),arrayList);
                    comm_rec.setAdapter(adapter);

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
//                            String uid = object.getString("uid");
//                            String createdBy = object.getString("createdBy");
//                            String time = object.getString("time");
//                            String email = object.getString("email");
//                            String comm_name = object.getString("comm_name");
//                            String desc_comm = object.getString("desc_comm");
//                            String phone = object.getString("phone");
//                            String ComID = object.getString("ComID");
//                            String type = object.getString("type");
//                            String image = object.getString("image");
//                            String about = object.getString("about");
//
//                            String urlImage= "http://catchwo.com/images/"+image;
//
//                                Model = new CommitteeModel(id,uid,createdBy,time,email,comm_name,desc_comm,phone,ComID,type,urlImage,about);
//                                arrayList.add(Model);
//                                adapter.notifyDataSetChanged();
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



    }
}