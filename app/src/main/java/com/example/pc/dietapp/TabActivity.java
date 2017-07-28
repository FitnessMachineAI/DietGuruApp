package com.example.pc.dietapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.pc.dietapp.Adapter.PagerAdapter;

public class TabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        TabLayout tabLayout=(TabLayout)findViewById(R.id.tab_layout);

        // 탭 텍스트
        tabLayout.addTab(tabLayout.newTab().setText("상체"));
        tabLayout.addTab(tabLayout.newTab().setText("하체"));
        tabLayout.addTab(tabLayout.newTab().setText("전신"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //탭에 아이콘 넣기
        tabLayout.getTabAt(0).setIcon(R.drawable.back);
        tabLayout.getTabAt(1).setIcon(R.drawable.leg);
        tabLayout.getTabAt(2).setIcon(R.drawable.all);


        final ViewPager viewPager=(ViewPager)findViewById(R.id.pager); //viewPager 찾기
        PagerAdapter adapter=new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount()); //adpater 찾기
        viewPager.setAdapter(adapter);
        //탭 동기화
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        }); //end tabLayout.setOnTabSelectedListener

    } //end onCreate

} //end MainActivity

