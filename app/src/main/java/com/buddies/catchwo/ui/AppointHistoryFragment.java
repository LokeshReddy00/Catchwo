package com.buddies.catchwo.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.buddies.catchwo.Adapter.AppointInfoAdapter;
import com.buddies.catchwo.Model.AppointinfoModel;
import com.buddies.catchwo.Model.FriendModel;
import com.buddies.catchwo.R;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointHistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AppointHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointHistoryFragment newInstance(String param1, String param2) {
        AppointHistoryFragment fragment = new AppointHistoryFragment();
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

    String url = "http://catchwo.com/retriveappointsinfo.php";
    AppointInfoAdapter adapter;
    ArrayList<AppointinfoModel> arrayList;
    RecyclerView app_his;
    AppointinfoModel model;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_history, container, false);


        app_his = view.findViewById(R.id.app_his);

        arrayList = new ArrayList<>();

        adapter = new AppointInfoAdapter(getActivity(),arrayList);
        app_his.setAdapter(adapter);

        LoadHistory();

        return view;
    }

    private void LoadHistory() {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                arrayList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("sucess");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String id = object.getString("id");
                            String uidto = object.getString("uid");
                            String customername = object.getString("customername");
                            String customerphone = object.getString("customerphone");
                            String done = object.getString("done");
                            String personname = object.getString("personname");
                            String peronaddress = object.getString("peronaddress");
                            String profid = object.getString("profid");
                            String profname = object.getString("profname");
                            String query = object.getString("query");
                            String slot = object.getString("slot");
                            String time = object.getString("time");
                            String timestamp = object.getString("timestamp");
                            String place = object.getString("place");
                            String Cuid = object.getString("Cuid");
                            String Date = object.getString("Date");

//                            String urlImage= "Image folder link"+image;


                            if(Cuid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                model = new AppointinfoModel(id,uidto,customername,customerphone,done,personname,peronaddress,profid,profname,query,slot,time,timestamp,place
                                ,Cuid,Date);
                                arrayList.add(model);
                                adapter.notifyDataSetChanged();
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
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);


    }
}