package com.example.pc.dietapp;

//마이페이지 눌렀을 때 정보안넘어가는 거 수정필요

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button mbtnPlusKg, mbtnExer;
    private TextView mPresentWeight, mGoalWeight;

    //오늘의 몸무게 입력 + 버튼을 눌렀을 때 몸무게 현황으로 이동
    private View.OnClickListener btnPlusKgClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), MyWeightActivity.class);
            startActivity(intent);
        }
    };

    //운동하기 눌렀을 때 운동 리스트로 이동
    private View.OnClickListener btnExerClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
            startActivity(intent);
        }
    };

    //현재 몸무게
    public class PresentWeightTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }

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
            Intent i = new Intent(MainActivity.this, MyWeightActivity.class);
            startActivity(i);
        } else if (id == R.id.exer_list) {  //운동 리스트
            Intent i = new Intent(MainActivity.this, MemUpActivity.class);
            startActivity(i);
        } else if (id == R.id.logout) {     //로그아웃

//            String s = new String();
//            Gson gson = new Gson();
//            JoinBean bean = gson.fromJson(s, JoinBean.class);

            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finishAffinity();
          //  ActivityCompat.finishAffinity(MainActivity.this);
          //  finish();
//            if (!bean.getResult().equals("ok"))
//                Toast.makeText(MainActivity.this, "정상적으로 로그아웃되었습니다.", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
