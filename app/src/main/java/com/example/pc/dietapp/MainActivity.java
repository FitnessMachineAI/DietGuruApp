package com.example.pc.dietapp;

//마이페이지 눌렀을 때 정보안넘어가는 거 수정필요

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.dietapp.Adapter.GridViewAdapter;
import com.example.pc.dietapp.Bean.JoinBean;
import com.example.pc.dietapp.Bean.VideoDataBean;
import com.example.pc.dietapp.Util.FileUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //현재 날짜 표시
        mtxtToday = (TextView)findViewById(R.id.txtToday);
        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        String currentDateString = dateFormat.format(new Date());
        mtxtToday.setText(currentDateString);


        sharedPreferences = getSharedPreferences(PREF_NAME, MemUpActivity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
//
        join = FileUtil.getMemberBean(this);
        JoinBean.JoinBeanSub joinBean = (JoinBean.JoinBeanSub)getIntent().getSerializableExtra("joinBean");

        mbtnPlusKg = (Button)findViewById(R.id.btnPlusKg);

        mbtnExer = (Button)findViewById(R.id.btnExer);
        mtxtName = (TextView)findViewById(R.id.txtName);
        mPresentWeight = (TextView)findViewById(R.id.presentWeight);
        mGoalWeight = (TextView)findViewById(R.id.goalWeight);

        mPresentWeight.setText(join.getJoinBean().getKg() + "Kg");
        mGoalWeight.setText(join.getJoinBean().getH_kg() + "Kg");


        findViewById(R.id.btnPlusKg).setOnClickListener(btnPlusKgClick);
        findViewById(R.id.btnExer).setOnClickListener(btnExerClick);

        //
        GridView gridView = (GridView)findViewById(R.id.gridViewMain);

        VideoDataBean video1 = new VideoDataBean(R.drawable.upper1, "옆구리, 허리 운동", "상체- 허리");
        video1.setVideoUrl("<div style=\"position:relative;height:0;padding-bottom:56.25%\"><iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/jpTQdM7okkI\" frameborder=\"0\" width=\"640\" height=\"360\" style=\"position:absolute;width:100%;height:100%;left:0\" allowfullscreen></iframe></div>");
        VideoDataBean video2 = new VideoDataBean(R.drawable.lower1, "다리살 허벅지살 빼는 최고의 운동 BEST5 (하체살 빼는 하체비만 다이어트 운동)", "하체- 허벅지, 다리");
        video2.setVideoUrl("<div style=\"position:relative;height:0;padding-bottom:56.25%\"><iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/59ylvWlszgg\" frameborder=\"0\" width=\"640\" height=\"360\" style=\"position:absolute;width:100%;height:100%;left:0\" allowfullscreen></iframe></div>");
        VideoDataBean video3 = new VideoDataBean(R.drawable.all1, "상체비만, 하체비만순환 루틴 홈트레이닝 다이어트운동", "전신");
        video3.setVideoUrl("<div style=\"position:relative;height:0;padding-bottom:56.25%\"><iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/sL0DCxjxppU\" frameborder=\"0\" width=\"640\" height=\"360\" style=\"position:absolute;width:100%;height:100%;left:0\" allowfullscreen></iframe></div>");

        List<VideoDataBean> list = new ArrayList<VideoDataBean>();
        list.add(video1);
        list.add(video2);
        list.add(video3);

        GridViewAdapter adapter = new GridViewAdapter(this, list);
        gridView.setAdapter(adapter);


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
            finish();

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
            finishAffinity();
            finish();
            Toast.makeText(MainActivity.this, "정상적으로 로그아웃되었습니다.", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
