package com.buddies.catchwo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.buddies.catchwo.ui.CompletedBooksFragment;
import com.buddies.catchwo.ui.OnGoingBookFragment;

public class BooksActivity extends AppCompatActivity {

    private Button completed,ongoing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);


        ongoing = findViewById(R.id.ongoing);
        completed = findViewById(R.id.completed);

        ongoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fram_lay, new OnGoingBookFragment()).commit();

            }
        });

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fram_lay, new CompletedBooksFragment()).commit();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportFragmentManager().beginTransaction().replace(R.id.fram_lay, new CompletedBooksFragment()).commit();

    }
}