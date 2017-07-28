package com.example.pc.dietapp.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.pc.dietapp.Bean.DateBean;
import com.example.pc.dietapp.Bean.KgBean;
import com.example.pc.dietapp.Util.Constants;
import com.google.gson.Gson;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2017-07-28.
 */

public class WeightAdapter {

    private Context mContext;
    public List<KgBean.KgBeanSub> kgList = new ArrayList<KgBean.KgBeanSub>();
    public List<DateBean.DateBeanSub> dateList = new ArrayList<DateBean.DateBeanSub>();


    public WeightAdapter(Context context){ //List<MemberBean.MemberBeanSub> memberList
        mContext = context;
    }


    //몸무게 받아오는 함수
    class WeightListTask extends AsyncTask<String, Void, String> {

        private String URL_WEIGHT_LIST = Constants.BASE_URL + "";

        @Override
        protected String doInBackground(String... strings) {
            try{
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

                return restTemplate.postForObject(URL_WEIGHT_LIST, request, String.class);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            Gson gson = new Gson();
            try{
                KgBean bean = gson.fromJson(s, KgBean.class);
                if(bean != null){
                    if (bean.getResult().equals("ok")){
                        kgList = bean.getSelectBoardList();
                    }
                }
            }catch (Exception e){
                Toast.makeText(mContext, "파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };


    //날짜 받아오는 함수
    class DateListTask extends AsyncTask<String, Void, String> {

        private String URL_DATE_LIST = Constants.BASE_URL + "";

        @Override
        protected String doInBackground(String... strings) {
            try{
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

                return restTemplate.postForObject(URL_DATE_LIST, request, String.class);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            Gson gson = new Gson();
            try{
                DateBean bean = gson.fromJson(s, DateBean.class);
                if(bean != null){
                    if (bean.getResultMsg().equals("ok")){
                        dateList = bean.getDateList();
                    }
                }
            }catch (Exception e){
                Toast.makeText(mContext, "파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };
}
