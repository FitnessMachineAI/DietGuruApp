package com.example.pc.dietapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity//
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button mbtnPlusKg, mbtnExer;


    //메인화면, 메뉴버튼 구성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //메인화면에서 버튼을 누르면 이동
        mbtnPlusKg = (Button)findViewById(R.id.btnPlusKg);  //몸무게 기록하기
        mbtnExer = (Button)findViewById(R.id.btnExer);      //운동하기

        findViewById(R.id.btnPlusKg).setOnClickListener(btnPlusKgClick);
        findViewById(R.id.btnExer).setOnClickListener(btnExerClick);
    }

    private View.OnClickListener btnPlusKgClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener btnExerClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
            startActivity(intent);
        }
    };

    //메뉴 등장
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //메뉴에서 항목들 클릭했을 때의 페이지 이동
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mypage) {               //마이페이지
            Intent i = new Intent(MainActivity.this, MemUpActivity.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.present_kg) {  //몸무게 현황
            Intent i = new Intent(MainActivity.this, MemUpActivity.class);
            startActivity(i);
        } else if (id == R.id.exer_list) {  //운동 리스트
            Intent i = new Intent(MainActivity.this, MemUpActivity.class);
            startActivity(i);
        } else if (id == R.id.logout) {     //로그아웃



            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
