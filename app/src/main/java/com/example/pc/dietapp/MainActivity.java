package com.example.pc.dietapp;

//마이페이지 눌렀을 때 정보안넘어가는 거 수정필요

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.dietapp.Bean.JoinBean;
import com.example.pc.dietapp.Util.Constants;
import com.example.pc.dietapp.Util.FileUtil;
import com.google.gson.Gson;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //자동로그인 구현을 위한
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "prefs";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_ID = "userid";
    private static final String KEY_PASS = "password";



    private Button mbtnPlusKg, mbtnExer;
    private TextView mPresentWeight, mGoalWeight, mtxtToday, mtxtName, mtxtId;
    private JoinBean join;

    //메인화면 등장
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(PREF_NAME, MemUpActivity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        join = FileUtil.getMemberBean(this);
        JoinBean.JoinBeanSub joinBean = (JoinBean.JoinBeanSub)getIntent().getSerializableExtra("joinBean");

        mbtnPlusKg = (Button)findViewById(R.id.btnPlusKg);
        mbtnExer = (Button)findViewById(R.id.btnExer);
        mtxtName = (TextView)findViewById(R.id.txtName);
        mtxtId = (TextView)findViewById(R.id.txtId);
        mPresentWeight = (TextView)findViewById(R.id.presentWeight);
        mGoalWeight = (TextView)findViewById(R.id.goalWeight);

        mPresentWeight.setText(join.getJoinBean().getKg());
        mGoalWeight.setText(join.getJoinBean().getH_kg());


        findViewById(R.id.btnPlusKg).setOnClickListener(btnPlusKgClick);
        findViewById(R.id.btnExer).setOnClickListener(btnExerClick);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }//end onCreate


    //오늘의 몸무게 입력 + 버튼을 눌렀을 때 몸무게 현황으로 이동
    private View.OnClickListener btnPlusKgClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, MyWeightActivity.class);
            startActivity(intent);
        }
    };

    //운동하기 눌렀을 때 운동 리스트로 이동
    private View.OnClickListener btnExerClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, TabActivity.class);
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

        Gson gson = new Gson();
        String s = new String();
        JoinBean bean = gson.fromJson(s, JoinBean.class);

        if (id == R.id.mypage) {               //마이페이지
            Intent i = new Intent(MainActivity.this, MemInfoActivity.class);
            startActivity(i);

        } else if (id == R.id.present_kg) {  //몸무게 현황
            Intent i = new Intent(MainActivity.this, MyWeightActivity.class);
            startActivity(i);

        } else if (id == R.id.exer_list) {  //운동 리스트
            Intent i = new Intent(MainActivity.this, TabActivity.class);
            startActivity(i);

        } else if (id == R.id.logout) {     //로그아웃

            editor.clear();
            editor.commit();

            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
            Toast.makeText(MainActivity.this, "정상적으로 로그아웃되었습니다.", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
