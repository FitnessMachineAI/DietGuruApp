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

public class KgAdapter extends BaseAdapter{

    private Context mContext;
    private List<KgBean.KgBeanSub> kgList = new ArrayList<KgBean.KgBeanSub>();

    public KgAdapter(Context context){
        mContext = context;
        upKgListTask();
    }

    @Override
    public int getCount() {
        return kgList.size();
    }

    @Override
    public Object getItem(int position) {
        return kgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void upKgListTask(){
        new GraphKgTask().execute();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final KgBean.KgBeanSub bean = kgList.get(position);
        Intent intent = new Intent(mContext, MyWeightActivity.class);
        intent.putExtra("kgBean", bean);
        mContext.startActivity(intent);

        return null;
    }

    //그래프 데이터 받아오는!
    private class GraphKgTask extends AsyncTask<String, Void, String> {

        private String URL_KG_LIST = Constants.BASE_URL+"/rest/selectBoardList.do";


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

                return restTemplate.postForObject(URL_KG_LIST, request, String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }   //end doInBackground

        @Override
        protected void onPostExecute(String s) {
            Gson gson = new Gson();
            try {
                KgBean kgBean = gson.fromJson(s, KgBean.class);

                if (kgBean != null) {
                    if (kgBean.getResult().equals("ok")) {
                        kgList = kgBean.getSelectBoardList();
                    } else {
                        if( kgBean.getResultMsg().equals("fail") ){
                            Toast.makeText(mContext, "몸무게 불러오기 실패", Toast.LENGTH_SHORT).show();
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
