package com.buddies.catchwo.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.buddies.catchwo.Adapter.ProfAdapter;
import com.buddies.catchwo.Model.ProfModel;
import com.buddies.catchwo.R;
import com.buddies.catchwo.Support.Common;
import com.buddies.catchwo.Support.IAllProfLoad;
import com.buddies.catchwo.Support.IAllProfLoadList;
import com.buddies.catchwo.Support.SpaceItenDecoration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Step1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step1Fragment extends Fragment implements IAllProfLoadList, IAllProfLoad {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Step1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Step1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Step1Fragment newInstance(String param1, String param2) {
        Step1Fragment fragment = new Step1Fragment();
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

    static Step1Fragment instance;

    public static Step1Fragment getInstance(){

        if(instance == null){
            instance = new Step1Fragment();
        }

        return instance;
    }

    CollectionReference reference;
    CollectionReference profRef;
    RecyclerView pref_rec;
    MaterialSpinner spinner;
    IAllProfLoadList iAllProfLoadList;
    IAllProfLoad iAllProfLoad;
    LottieAnimationView loading;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_step1, container, false);

        reference = FirebaseFirestore.getInstance().collection("AllAppoints");
        iAllProfLoadList = this;
        iAllProfLoad = this;
        spinner = root.findViewById(R.id.spinner);
        pref_rec = root.findViewById(R.id.prof_rec);
        loading = root.findViewById(R.id.loading);

        intiView();
        LoadAllProf();


        return root;
    }

    private void intiView() {

        pref_rec.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        pref_rec.setLayoutManager(llm);
        pref_rec.addItemDecoration(new SpaceItenDecoration(4));

    }

    private void LoadAllProf() {

        reference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<String> list= new ArrayList<>();
                            list.add("Please choose city");
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult())
                                list.add(documentSnapshot.getId());
                            iAllProfLoadList.onAllProfLoadSuccess(list);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               iAllProfLoadList.onAllProfLoadFailed(e.getMessage());
            }
        });

    }

    @Override
    public void onAllProfLoadSuccess(List<String> areaNameList) {

        spinner.setItems(areaNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position > 0){
                    loadProfOfCity(item.toString());
                }
                else {
                    pref_rec.setVisibility(View.GONE);
                }
            }
        });

    }

    private void loadProfOfCity(String cityName) {
        loading.setVisibility(View.VISIBLE);

        Common.city = cityName;

        profRef = FirebaseFirestore.getInstance().collection("AllAppoints").document(cityName).collection("Appoints");

        profRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<ProfModel> list= new ArrayList<>();
                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        ProfModel profModel = documentSnapshot.toObject(ProfModel.class);
                        profModel.setName(documentSnapshot.getId());
                        list.add(profModel);
                    }
//                        list.add(documentSnapshot.toObject(ProfModel.class));
                    iAllProfLoad.onAllProfLoadsSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllProfLoad.onAllProfLoadsFailed(e.getMessage());

            }
        });

    }

    @Override
    public void onAllProfLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAllProfLoadsSuccess(List<ProfModel> areaNameList) {
        ProfAdapter adapter = new ProfAdapter(getActivity(),areaNameList);
        pref_rec.setAdapter(adapter);
        pref_rec.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);

    }

    @Override
    public void onAllProfLoadsFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        loading.setVisibility(View.GONE);
    }
}