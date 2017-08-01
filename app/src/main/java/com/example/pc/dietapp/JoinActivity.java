package com.example.pc.dietapp;

//sj
//공백으로 회원가입 불가능하게 체크해야함

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pc.dietapp.Bean.JoinBean;
import com.example.pc.dietapp.Util.Constants;
import com.google.gson.Gson;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;



public class JoinActivity extends AppCompatActivity {

    private EditText mEdtJoinId, mEdtJoinPw, mEdtJoinCm, mEdtJoinKg, mEdtJoinHkg, mEdtJoinWord;
    private ProgressBar mProgressBar;
    private JoinBean joinBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        mEdtJoinId = (EditText)findViewById(R.id.edtJoinId);
        mEdtJoinPw = (EditText)findViewById(R.id.edtJoinPw);
        mEdtJoinCm = (EditText)findViewById(R.id.edtJoinCm);
        mEdtJoinKg = (EditText)findViewById(R.id.edtJoinKg);
        mEdtJoinHkg = (EditText)findViewById(R.id.edtJoinHkg);
        mEdtJoinWord = (EditText)findViewById(R.id.edtJoinWord);

        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

        findViewById(R.id.btnIdOk).setOnClickListener(btnIdOkClick);
        findViewById(R.id.btnJoinOk).setOnClickListener(btnJoinOkClick);
    }//end onCreate

    private View.OnClickListener btnIdOkClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new IdCheckTask().execute();

        }
    };  //end btnIdOkClick


    private class IdCheckTask extends AsyncTask<String, Void, String>{

        public static final String URL_JOIN_PROC= Constants.BASE_URL+"rest/idCheckMember.do";
        private String userId;

        @Override
        protected void onPreExecute() {
            userId = mEdtJoinId.getText().toString();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                //map.add(" " <- 이부분은 memberBean의 이름과 같게 해주어야함!!!!! 꼭!!!!!!!
                map.add("userId", userId);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.ALL.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

                return restTemplate.postForObject(URL_JOIN_PROC, request, String.class);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            mProgressBar.setVisibility(View.INVISIBLE);
            Gson gson = new Gson();
            try{
                JoinBean bean = gson.fromJson(s, JoinBean.class);
                if(bean!=null){
                    if(bean.getResultMsg().equals("이미 존재하는 USER_ID 입니다.")){
                        Toast.makeText(JoinActivity.this, bean.getResultMsg(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(JoinActivity.this, bean.getResultMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception e){
                Toast.makeText(JoinActivity.this, "파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }//
    }
    /////여기까지 중복체크하는 버튼


    private View.OnClickListener btnJoinOkClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new JoinProcTask().execute();
        }
    };  //end btnJoinOkClick

    private class JoinProcTask extends AsyncTask<String, Void, String> {

        public static final String URL_JOIN_PROC= Constants.BASE_URL+"rest/insertMember.do";
        private String suserId, suserPw, suserCm, suserKg, suserHkg, suserWord;

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);

            suserId = mEdtJoinId.getText().toString();
            suserPw = mEdtJoinPw.getText().toString();
            suserCm = mEdtJoinCm.getText().toString();
            suserKg = mEdtJoinKg.getText().toString();
            suserHkg = mEdtJoinHkg.getText().toString();
            suserWord = mEdtJoinWord.getText().toString();
        }

        @Override
        protected String doInBackground(String... params) {

            try{
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                //map.add(" " <- 이부분은 memberBean의 이름과 같게 해주어야함!!!!! 꼭!!!!!!!
                map.add("userId", suserId);
                map.add("userPw", suserPw);
                map.add("cm", suserCm);
                map.add("kg", suserKg);
                map.add("h_kg", suserHkg);
                map.add("word", suserWord);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.ALL.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

                return restTemplate.postForObject(URL_JOIN_PROC, request, String.class);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }   //end doInBackground

        @Override
        protected void onPostExecute(String s) {
            mProgressBar.setVisibility(View.INVISIBLE);
            Gson gson = new Gson();
            try{
                JoinBean bean = gson.fromJson(s, JoinBean.class);
                if(bean!=null){
                    if(bean.getResult().equals("ok")){
                        Intent i = new Intent(JoinActivity.this, LoginActivity.class);
                        startActivity(i);
                    }else {
                        Toast.makeText(JoinActivity.this, bean.getResultMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception e){
                Toast.makeText(JoinActivity.this, "파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }//end onPostExecute

    }
}
