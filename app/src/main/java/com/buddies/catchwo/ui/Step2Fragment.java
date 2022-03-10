package com.buddies.catchwo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buddies.catchwo.Adapter.PersonAdapter;
import com.buddies.catchwo.Model.Professional;
import com.buddies.catchwo.R;
import com.buddies.catchwo.Support.Common;
import com.buddies.catchwo.Support.SpaceItenDecoration;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Step2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step2Fragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static Step2Fragment instance;
    RecyclerView rec;
    LocalBroadcastManager localBroadcastManager;
    private final BroadcastReceiver proDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Professional> professionals = intent.getParcelableArrayListExtra(Common.KEY_PROF_LOAD_DONE);

            PersonAdapter adapter = new PersonAdapter(getContext(),professionals);
            rec.setAdapter(adapter);
        }
    };
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Step2Fragment() {
        // Required empty public constructor
    }

    public static Step2Fragment getInstance() {

        if (instance == null) {
            instance = new Step2Fragment();
        }

        return instance;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Step2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Step2Fragment newInstance(String param1, String param2) {
        Step2Fragment fragment = new Step2Fragment();
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


        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(proDoneReceiver, new IntentFilter(Common.KEY_PROF_LOAD_DONE));

    }

    @Override
    public void onDestroy() {

        localBroadcastManager.unregisterReceiver(proDoneReceiver);

        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_step2, container, false);

        rec = root.findViewById(R.id.rec);
        initView();


        return root;
    }

    private void initView() {

        rec.setHasFixedSize(true);
        GridLayoutManager llm = new GridLayoutManager(getActivity(),2);
        rec.setLayoutManager(llm);
        rec.addItemDecoration(new SpaceItenDecoration(4));
    }
}