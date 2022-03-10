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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.buddies.catchwo.Adapter.MemoryAdapter;
import com.buddies.catchwo.AddPostActivity;
import com.buddies.catchwo.Model.PostModel;
import com.buddies.catchwo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MeoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeoFragment newInstance(String param1, String param2) {
        MeoFragment fragment = new MeoFragment();
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

    LottieAnimationView memory;

    MemoryAdapter adapter;
    ArrayList<PostModel> arrayList;

    LottieAnimationView loading;

    private static final int TOTAL_ITEM_EACH_LOAD = 8;
    private int currentPage = 1;
    Button more;
    long initial;
    private TextView nothing;
    RecyclerView post_rec;
    EditText search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meo, container, false);

        memory = view.findViewById(R.id.memory);
        nothing = view.findViewById(R.id.nothing);
        search = view.findViewById(R.id.search);
        more = view.findViewById(R.id.more);
        loading = view.findViewById(R.id.loading);
        post_rec = view.findViewById(R.id.post_rec);

        arrayList = new ArrayList<>();
        memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
                intent.putExtra("value", "meo");
                startActivity(intent);
            }
        });

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


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    i++;
                }
                initial = i;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        loadPost();

        more.setOnClickListener(v -> loadMoreData());


        return view;
    }

    private void loadMoreData() {
        currentPage++;
        loadPost();
    }

    private void loadPost() {

        arrayList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        reference.child("memory").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).limitToLast(currentPage * TOTAL_ITEM_EACH_LOAD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    PostModel postModel = ds.getValue(PostModel.class);
                    arrayList.add(postModel);
                    adapter = new MemoryAdapter(getActivity(), arrayList);
                    post_rec.setAdapter(adapter);
                    loading.setVisibility(View.GONE);

                    if (adapter.getItemCount() == 0) {
                        loading.setVisibility(View.GONE);
                        post_rec.setVisibility(View.GONE);
                        nothing.setVisibility(View.VISIBLE);
                    } else {
                        loading.setVisibility(View.GONE);
                        post_rec.setVisibility(View.VISIBLE);
                        nothing.setVisibility(View.GONE);
                        if (adapter.getItemCount() == initial) {
                            more.setVisibility(View.GONE);
                            currentPage--;
                        } else {
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