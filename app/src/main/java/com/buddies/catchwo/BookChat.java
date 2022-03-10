package com.buddies.catchwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.buddies.catchwo.Adapter.BookChatAdapter;
import com.buddies.catchwo.Adapter.ChatUsersAdapter;
import com.buddies.catchwo.Model.BookChatModel;
import com.buddies.catchwo.Model.FriendModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookChat extends AppCompatActivity {

    EditText search;
    RecyclerView rec_chat;

    ArrayList<BookChatModel> arrayList;
    BookChatAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_chat);

        rec_chat = findViewById(R.id.rec_chat);
        search = findViewById(R.id.search);

        arrayList = new ArrayList<>();
        adapter = new BookChatAdapter(this,arrayList);
        rec_chat.setAdapter(adapter);

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

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LoadChats();

    }

    private void LoadChats() {

        arrayList.clear();
        FirebaseDatabase.getInstance().getReference().child("Book_chat").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){

                    BookChatModel model = ds.getValue(BookChatModel.class);
                    arrayList.add(model);
                    adapter = new BookChatAdapter(BookChat.this,arrayList);
                    rec_chat.setAdapter(adapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BookChat.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}