package com.buddies.catchwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import com.buddies.catchwo.ui.NotificationsFragment;
import com.buddies.catchwo.ui.ProfileFragment;
import com.buddies.catchwo.ui.ViewProfileFragment;
import com.buddies.catchwo.ui.gallery.GalleryFragment;
import com.buddies.catchwo.ui.home.HomeFragment;
import com.buddies.catchwo.ui.slideshow.SlideshowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class HomeScreen extends AppCompatActivity {

ChipNavigationBar bottom_navigation;
ImageView notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        bottom_navigation = findViewById(R.id.bottom_navigation);

        notification = findViewById(R.id.notification);

        getSupportFragmentManager().beginTransaction().replace(R.id.fram_lay, new HomeFragment()).commit();

        bottom_navigation.setItemSelected(R.id.nav_home,true);

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fram_lay, new NotificationsFragment()).commit();

            }
        });

        bottom();
    }

    private void bottom() {

        bottom_navigation.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment selected = null;
                switch (i){
                    case R.id.nav_home:
                        selected = new HomeFragment();
                        break;
                    case R.id.nav_gallery:
                        selected = new GalleryFragment();
                        break;
                    case R.id.nav_slideshow:
                        selected = new SlideshowFragment();
                        break;
                    case R.id.profile:
                        selected = new ProfileFragment();
                        break;
                    case R.id.notification:
                        selected = new ViewProfileFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fram_lay, selected).commit();


            }
        });

    }


    public void menu(View view) {

        startActivity(new Intent(HomeScreen.this,HomeActivity.class));
    }
}