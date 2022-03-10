package com.buddies.catchwo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.buddies.catchwo.ui.AddPostFragment;
import com.buddies.catchwo.ui.AppointFragment;
import com.buddies.catchwo.ui.BookFragment;
import com.buddies.catchwo.ui.ClubFragment;
import com.buddies.catchwo.ui.CommFragment;
import com.buddies.catchwo.ui.FriendsFragment;
import com.buddies.catchwo.ui.InfoFragment;
import com.buddies.catchwo.ui.MeoFragment;
import com.buddies.catchwo.ui.ProFragment;
import com.buddies.catchwo.ui.SettingsFragment;
import com.buddies.catchwo.ui.SurFragment;
import com.buddies.catchwo.ui.ViewProfileFragment;

public class InformationActivity extends AppCompatActivity {

    FrameLayout fram_lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        fram_lay = findViewById(R.id.fram_lay);

        Intent intent = getIntent();
        String value = intent.getStringExtra("act");

        Fragment selected = null;

        switch (value){
            case "book":
                selected = new BookFragment();
                break;
            case "club":
                selected = new ClubFragment();
                break;
            case "comm":
                selected = new CommFragment();
                break;
            case "meo":
                selected = new MeoFragment();
                break;
            case "set":
                selected = new SettingsFragment();
                break;
            case "sur":
                selected = new SurFragment();
                break;
            case "pro":
                selected = new ProFragment();
                break;
            case "profile":
                selected = new ViewProfileFragment();
                break;
            case "friend":
                selected = new FriendsFragment();
                break;
            case "add":
                selected = new AddPostFragment();
                break;
            case "link":
                selected = new InfoFragment();
                break;
            default:
                break;
        }
        assert selected != null;
        getSupportFragmentManager().beginTransaction().replace(R.id.fram_lay,selected).commit();
    }
}