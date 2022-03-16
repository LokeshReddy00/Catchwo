package com.buddies.catchwo.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.buddies.catchwo.Adapter.ReelsAdapter;
import com.buddies.catchwo.Model.ReelsModel;
import com.buddies.catchwo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class SlideshowFragment extends Fragment {

    ViewPager2 viewPager;
    ReelsAdapter adapter;
    ArrayList<ReelsModel> arrayList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_slideshow, container, false);

        viewPager = root.findViewById(R.id.viewPager);
        arrayList = new ArrayList<>();



        FirebaseDatabase.getInstance().getReference().child("reels").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){


                    ReelsModel model = ds.getValue(ReelsModel.class);
                    arrayList.add(model);
                    Collections.reverse(arrayList);
                    adapter = new ReelsAdapter(getActivity(),arrayList);
                    viewPager.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return root;
    }


}