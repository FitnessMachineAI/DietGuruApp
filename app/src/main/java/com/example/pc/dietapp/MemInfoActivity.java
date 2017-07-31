package com.example.pc.dietapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class MemInfoActivity extends AppCompatActivity {

    private TextView mtxtInfoId, mtxtInfoCm, mtxtInfoKg, mtxtInfoHkg, mtxtInfoWord;
    private ProgressBar mProgressBar;
    JoinBean.JoinBeanSub joinBean = new JoinBean.JoinBeanSub();
    private String URL_MEMBER = Constants.BASE_URL + "/rest/selectMember.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_info);

        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

        findViewById(R.id.btnEdt).setOnClickListener(btnEdtClick);
        new MemberTask().execute(URL_MEMBER);
    }//end OnCreate

    private View.OnClickListener btnEdtClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MemInfoActivity.this, MemUpActivity.class);
            intent.putExtra( "memberBean",  JoinBean.JoinBeanSub.class);
            startActivity(intent);
        }
    };

    //회원관리를 위한 task -> 회원정보수정/탈퇴
    private class MemberTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.ALL.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

                return restTemplate.postForObject(URL_MEMBER, request, String.class);
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
                        joinBean = bean.getJoinBean();

                        Toast.makeText(MemInfoActivity.this, "ok이긴 함", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MemInfoActivity.this, bean.getResultMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception e){
                Toast.makeText(MemInfoActivity.this, "파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }//end onPostExecute
    }//end MemberTask
}
