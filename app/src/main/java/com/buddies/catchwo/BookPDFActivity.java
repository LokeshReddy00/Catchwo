package com.buddies.catchwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class BookPDFActivity extends AppCompatActivity {

    private WebView pdf_web;
    private TextView pdt_txt;
    private EditText pdt_ed;
    private ScrollView edit;
    private Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_pdfactivity);

        Intent intent = getIntent();
        String value = intent.getStringExtra("pdf");
        String type = intent.getStringExtra("type");
        String uid = intent.getStringExtra("uid");
        String id = intent.getStringExtra("id");


        pdf_web = findViewById(R.id.pdf_web);
        pdt_txt = findViewById(R.id.pdt_txt);
        pdt_ed = findViewById(R.id.pdt_ed);
        edit = findViewById(R.id.edit);
        update = findViewById(R.id.update);

        if(type.equals("complete")){
//            Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
            edit.setVisibility(View.GONE);
            pdt_txt.setVisibility(View.GONE);
            pdf_web.setVisibility(View.VISIBLE);
            pdf_web.loadUrl(value);

        }else if(type.equals("ongoing")){
//            Toast.makeText(this, value, Toast.LENGTH_SHORT).show();

            pdf_web.setVisibility(View.GONE);

            if(uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                edit.setVisibility(View.VISIBLE);
                pdt_ed.setText(value);
                edit.setVisibility(View.GONE);
                pdt_ed.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        HashMap<String, Object> hashMap = new HashMap<>();

                        hashMap.put("book",pdt_ed.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child(type).child(id).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(BookPDFActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

            }else {
                pdt_txt.setText(value);
                edit.setVisibility(View.VISIBLE);
                pdt_ed.setVisibility(View.GONE);
                update.setVisibility(View.GONE);
            }

        }

    }
}