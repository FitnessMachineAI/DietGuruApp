package com.example.pc.dietapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.pc.dietapp.Bean.DateBean;
import com.example.pc.dietapp.Bean.KgBean;
import com.example.pc.dietapp.MyWeightActivity;
import com.example.pc.dietapp.R;
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
 * Created by pc on 2017-07-31.
 */

public class DateAdapter extends BaseAdapter{

    private Context mContext;
    private List<DateBean.DateBeanSub> dateList = new ArrayList<DateBean.DateBeanSub>();

    public DateAdapter(Context context){
        mContext = context;
        upDateListTask();
    }

    @Override
    public int getCount() {
        return dateList.size();
    }

    @Override
    public Object getItem(int position) {
        return dateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void upDateListTask(){
        new GraphDateTask().execute();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final DateBean.DateBeanSub bean = dateList.get(position);
        Intent intent = new Intent(mContext, MyWeightActivity.class);
        intent.putExtra("dateBean", bean);
        mContext.startActivity(intent);

        return null;
    }

    private class GraphDateTask extends AsyncTask<String, Void, String> {

        private String URL_DATE_LIST = Constants.BASE_URL+"/rest/dateList.do";


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();


                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.ALL.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

                return restTemplate.postForObject(URL_DATE_LIST, request, String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }   //end doInBackground

        @Override
        protected void onPostExecute(String s) {
            Gson gson = new Gson();
            try {
                DateBean dateBean = gson.fromJson(s, DateBean.class);

                if (dateBean != null) {
                    if (dateBean.getResult().equals("ok")) {
                        dateList = dateBean.getDateList();
                    } else {
                        if(dateBean.getResultMsg().equals("fail")) {
                            Toast.makeText(mContext, "날짜 불러오기 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(mContext, "파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }//end onPostExecute
    }//end UpTask
}
