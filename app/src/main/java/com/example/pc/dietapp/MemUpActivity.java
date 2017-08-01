package com.example.pc.dietapp;

//sj

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class MemUpActivity extends AppCompatActivity {

    private EditText mEdtUpId, mEdtUpPw, mEdtUpCm, mEdtUpKg, mEdtUpHkg, mEdtUpWord;
    private ProgressBar mProgressBar;
    private JoinBean join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_up);

        join = FileUtil.getMemberBean(this);

        mEdtUpId = (EditText)findViewById(R.id.edtUpId);
        mEdtUpPw = (EditText)findViewById(R.id.edtUpPw);
        mEdtUpCm = (EditText)findViewById(R.id.edtUpCm);
        mEdtUpKg = (EditText)findViewById(R.id.edtUpKg);
        mEdtUpHkg = (EditText)findViewById(R.id.edtUpHkg);
        mEdtUpWord = (EditText)findViewById(R.id.edtUpWord);

        mEdtUpId.setText(join.getJoinBean().getUserId());
        mEdtUpPw.setText(join.getJoinBean().getUserPw());
        mEdtUpCm.setText(join.getJoinBean().getCm());
        mEdtUpKg.setText(join.getJoinBean().getKg());
        mEdtUpHkg.setText(join.getJoinBean().getH_kg());
        mEdtUpWord.setText(join.getJoinBean().getWord());


        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

        findViewById(R.id.btnUpOk).setOnClickListener(btnUpClick);
        findViewById(R.id.btnByeOK).setOnClickListener(btnByeClick);
    }//end OnCreate



    private View.OnClickListener btnUpClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MemUpActivity.this);

            builder.setTitle("회원정보 수정확인")
                    .setMessage("정보를 수정하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new UpTask().execute();
                        }
                    }) //확인버튼 클릭시 이벤트
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }); //취소버튼 클릭시 설정
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };  //end btnUpClick

    private View.OnClickListener btnByeClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MemUpActivity.this);

            builder.setTitle("회원탈퇴확인")
                    .setMessage("정말로 탈퇴하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new DeleteTask().execute();
                        }
                    }) //확인버튼 클릭시 이벤트
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }); //취소버튼 클릭시 설정
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };  //end btnByeClick



    //회원관리를 위한 task -> 회원정보수정
    private class UpTask extends AsyncTask<String, Void, String> {
        private String URL_UP_PROC = Constants.BASE_URL+"rest/updateMember.do";
        private String userId, userPw, userCm, userKg, userHkg, userWord;

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);

            userId = mEdtUpId.getText().toString();
            userPw = mEdtUpPw.getText().toString();
            userCm = mEdtUpCm.getText().toString();
            userKg = mEdtUpKg.getText().toString();
            userHkg = mEdtUpHkg.getText().toString();
            userWord = mEdtUpWord.getText().toString();
        }

        @Override
        protected String doInBackground(String... params) {

            try{
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                //map.add(" " <- 이부분은 memberBean의 이름과 같게 해주어야함!!!!! 꼭!!!!!!!
                map.add("userId", userId);
                map.add("userPw", userPw);
                map.add("cm", userCm);
                map.add("kg", userKg);
                map.add("h_kg", userHkg);
                map.add("word", userWord);


                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.ALL.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);


                return restTemplate.postForObject(URL_UP_PROC, request, String.class);
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
                        new LoadTask().execute();
                    }else {
                        Toast.makeText(MemUpActivity.this, bean.getResultMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception e){
                Toast.makeText(MemUpActivity.this, "파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }//end onPostExecute
    }//end UpTask


    class LoadTask extends AsyncTask<String, Void, String>{
        private String URL_LOAD_PROC = Constants.BASE_URL+"/rest/selectMember.do";
        private String userId, userPw, userCm, userKg, userHKg, userWord;

        @Override
        protected void onPreExecute() {
            //프로그래스 다이얼로그 표시
            mProgressBar.setVisibility(View.VISIBLE);

            userId = mEdtUpId.getText().toString();
            userPw = mEdtUpId.getText().toString();
            userCm = mEdtUpCm.getText().toString();
            userKg = mEdtUpKg.getText().toString();
            userHKg = mEdtUpHkg.getText().toString();
            userWord = mEdtUpWord.getText().toString();
        }

        @Override
        protected String doInBackground(String... params) {

            try{
                RestTemplate restTemplate = new RestTemplate();
                //restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                map.add("userId",userId);
                map.add("userPw",userPw);
                map.add("cm", userCm);
                map.add("kg",userKg);
                map.add("h_kg",userHKg);
                map.add("word",userWord);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.ALL.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

                return restTemplate.postForObject(URL_LOAD_PROC, request, String.class);
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }//end doInBackgroud

        @Override
        protected void onPostExecute(String s) {
            mProgressBar.setVisibility(View.INVISIBLE);
            Gson gson = new Gson();
            try {
                JoinBean bean = gson.fromJson(s, JoinBean.class);
                if(bean!=null){
                    if(bean.getResult().equals("ok")) {
                        //로그인성공
                        FileUtil.setJoinBean(MemUpActivity.this, bean);

                        Intent i = new Intent(MemUpActivity.this, MainActivity.class);
                        i.putExtra("joinBean", bean.getJoinBean());
                        startActivity(i);
                        finish();
                    }else {
                        //로그인실패
                        Toast.makeText(MemUpActivity.this, "로그인실패", Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception e){
                Toast.makeText(MemUpActivity.this, "파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }//end onPostExecute

    }

    class DeleteTask extends AsyncTask<String, Void, String>{

        private String URL_DELETE_PROC = Constants.BASE_URL + "rest/deleteMember.do";

        private String userId, userPw, userCm, userKg, userHkg, userWord;

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            userId = mEdtUpId.getText().toString();
            userPw = mEdtUpId.getText().toString();
            userCm = mEdtUpCm.getText().toString();
            userKg = mEdtUpKg.getText().toString();
            userHkg = mEdtUpHkg.getText().toString();
            userWord = mEdtUpWord.getText().toString();
        }

        @Override
        protected String doInBackground(String... params) {

            try{
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                map.add("userId",userId);
                map.add("userPw",userPw);
                map.add("cm", userCm);
                map.add("kg",userKg);
                map.add("h_kg",userHkg);
                map.add("word",userWord);


                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.ALL.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);


                return restTemplate.postForObject(URL_DELETE_PROC, request, String.class);
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

                        Intent intent = new Intent(MemUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(MemUpActivity.this, bean.getResultMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception e){
                Toast.makeText(MemUpActivity.this, "파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }//end onPostExecute
    }


}
