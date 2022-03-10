package com.buddies.catchwo.ui.home;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.buddies.catchwo.Adapter.PostAdapter;
import com.buddies.catchwo.AddPostActivity;
import com.buddies.catchwo.Model.PostModel;
import com.buddies.catchwo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    LinearLayout post;
    RecyclerView post_rec;

    EditText search;

    PostAdapter adapter;
    ArrayList<PostModel> arrayList;

    LottieAnimationView loading;

    private static final int TOTAL_ITEM_EACH_LOAD = 8;
    private int currentPage = 1;
    Button more;
    long initial;
    private TextView nothing;



    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        post = root.findViewById(R.id.post);
        post_rec = root.findViewById(R.id.post_rec);
        loading = root.findViewById(R.id.loading);
        more = root.findViewById(R.id.more);
        nothing = root.findViewById(R.id.nothing);
        search = root.findViewById(R.id.search);

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



        // load all Posts


        post.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddPostActivity.class);
            intent.putExtra("value","pub");
            startActivity(intent);
        });

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

        loadPost();
        arrayList = new ArrayList<>();

        more.setOnClickListener(v -> loadMoreData());

        return root;
    }

    private void loadMoreData() {
        currentPage++;
        loadPost();
    }

    private void loadPost() {

        arrayList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child("Posts").limitToLast(currentPage*TOTAL_ITEM_EACH_LOAD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();

                for(DataSnapshot ds: snapshot.getChildren()){
                    PostModel postModel = ds.getValue(PostModel.class);
                    arrayList.add(postModel);
                    adapter = new PostAdapter(getActivity(),arrayList);
                    post_rec.setAdapter(adapter);
                    loading.setVisibility(View.GONE);

                    if(adapter.getItemCount() == 0){
                        loading.setVisibility(View.GONE);
                        post_rec.setVisibility(View.GONE);
                        nothing.setVisibility(View.VISIBLE);
                    }else {
                        loading.setVisibility(View.GONE);
                        post_rec.setVisibility(View.VISIBLE);
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
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        loading.setVisibility(View.GONE);


    }
}