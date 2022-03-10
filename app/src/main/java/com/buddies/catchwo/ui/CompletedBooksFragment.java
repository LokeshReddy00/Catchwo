package com.buddies.catchwo.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.buddies.catchwo.Adapter.BookAdapter;
import com.buddies.catchwo.AddBookActivity;
import com.buddies.catchwo.AddCategoryActivity;
import com.buddies.catchwo.Model.AppointinfoModel;
import com.buddies.catchwo.Model.BookModel;
import com.buddies.catchwo.PDFActivity;
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
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompletedBooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompletedBooksFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CompletedBooksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompletedBooksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompletedBooksFragment newInstance(String param1, String param2) {
        CompletedBooksFragment fragment = new CompletedBooksFragment();
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

    LottieAnimationView add_book;
    EditText search;
    RecyclerView book_rec;

    String value,id,name,written,time,book,cat,bid,image,desc;
    BookAdapter adapter;
    ArrayList<BookModel> arrayList;
    BookModel model;
    String url = "http://catchwo.com/retrivecompletedbooks.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_completed_books, container, false);

        search = view.findViewById(R.id.search);
        add_book = view.findViewById(R.id.add_book);
        book_rec = view.findViewById(R.id.book_rec);

        arrayList = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        book_rec.setLayoutManager(gridLayoutManager);

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


        add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddBookActivity.class);
                intent.putExtra("type","complete");
                startActivity(intent);
            }
        });
        loadBooks();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadBooks();
    }

    private void loadBooks() {


        FirebaseDatabase.getInstance().getReference().child("complete").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    arrayList.clear();

                            BookModel model = ds.getValue(BookModel.class);
                            arrayList.add(model);
                            adapter = new BookAdapter(getActivity(), arrayList);
                            book_rec.setAdapter(adapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


//        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
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
//
//                            bid = object.getString("bookID");
//                            String uid = object.getString("uid");
//
//
////                            if(value.equals(bid) && uid.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())){
//
//                                id = object.getString("id");
//                                name = object.getString("name");
//                                written = object.getString("written");
//                                time = object.getString("time");
//                                book = object.getString("book");
//                                cat = object.getString("category");
//                                bid = object.getString("bookID");
//                                image = object.getString("image");
//                                desc = object.getString("description");
//                                String type = object.getString("type");
//
//                                String image_ = "http://catchwo.com/images/" + image;
//
//                                if(book.isEmpty() && type.isEmpty()){
//                                    model = new BookModel(id,image_,uid,bid,name,written,cat,desc,time,book,type);
//                                    arrayList.add(model);
//                                    adapter.notifyDataSetChanged();
//                                }
//
//
//
////                            }
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