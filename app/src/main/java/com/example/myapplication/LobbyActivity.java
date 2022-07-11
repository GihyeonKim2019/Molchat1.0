package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LobbyActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        tabLayout = findViewById(R.id.tabLayout);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(Fragment1.newInstance(0));
        fragments.add(Fragment2.newInstance(1));
        fragments.add(Fragment3.newInstance(2));

        viewPager2 = (ViewPager2) findViewById(R.id.viewPager2_container);
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this, fragments);
        viewPager2.setAdapter(viewPager2Adapter);

        final List<String> tabElement = Arrays.asList("Tab1","Tab2","Tab3");

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy(){
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                TextView textView = new TextView(LobbyActivity.this);
                textView.setText(tabElement.get(position));


            }
        }).attach();

    }

}
