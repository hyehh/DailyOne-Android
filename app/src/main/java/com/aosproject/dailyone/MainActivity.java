package com.aosproject.dailyone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.aosproject.dailyone.fragment.CalendarFragment;
import com.aosproject.dailyone.fragment.DiaryFragment;
import com.aosproject.dailyone.fragment.ListFragment;
import com.aosproject.dailyone.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.aosproject.dailyone.R.id.item_fragment1;
import static com.aosproject.dailyone.R.id.item_fragment2;
import static com.aosproject.dailyone.R.id.item_fragment3;
import static com.aosproject.dailyone.R.id.item_fragment4;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.tabar_nav);
        bottomNavigationView.setSelectedItemId(R.id.item_fragment1);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case item_fragment1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_view_frame, new DiaryFragment()).commit();
                        return true;
                    case item_fragment2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_view_frame, new CalendarFragment()).commit();
                        return true;
                    case item_fragment3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_view_frame, new ListFragment()).commit();
                        return true;
                    case item_fragment4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_view_frame, new SearchFragment()).commit();
                        return true;
                }
                //BottomNavigate(item.getItemId());
                return false;
            }
        });
    }

//    private void BottomNavigate(int id) {
//        String tag = String.valueOf(id);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
//        if(currentFragment != null) {
//            fragmentTransaction.hide(currentFragment);
//        }
//        Fragment fragment = fragmentManager.findFragmentByTag(tag);
//        if (fragment == null) {
//            if (id == item_fragment1){
//                fragment = new DiaryFragment();
//            } else if (id==item_fragment2){
//                fragment = new CalendarFragment();
//            } else if (id== item_fragment3){
//                fragment = new ListFragment();
//            } else {
//                fragment = new SearchFragment();
//            }
//
//            fragmentTransaction.add(R.id.content_view_frame, fragment, tag);
//        } else {
//            fragmentTransaction.show(fragment);
//        }
//
//        fragmentTransaction.setPrimaryNavigationFragment(fragment);
//        fragmentTransaction.setReorderingAllowed(true);
//        fragmentTransaction.commitNow();
//
//    }

}