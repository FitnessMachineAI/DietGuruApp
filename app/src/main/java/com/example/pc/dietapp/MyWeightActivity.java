package com.example.pc.dietapp;

//sj
//그래프 참고 사이트 http://namgh.blogspot.kr/2016/05/mpandroidchart-1.html

//1 날짜는 어떻게 받아와야하지?
//2 가장 최신 데이터는 어떻게 받아와야하지?
//3 서버에서 데이터를 15개 불러와서 어떻게 함수를 구현해야하지?


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pc.dietapp.Adapter.DateAdapter;
import com.example.pc.dietapp.Adapter.KgAdapter;
import com.example.pc.dietapp.Bean.DateBean;
import com.example.pc.dietapp.Bean.KgBean;
import com.example.pc.dietapp.Bean.WeightBean;
import com.example.pc.dietapp.Util.Constants;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import com.google.gson.Gson;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyWeightActivity extends AppCompatActivity {

    private EditText mEdtTodayWeight;
    public ProgressBar mProgressBar;
    private LineChart mChart;
    private String mDate, mD_kg;
    private int mY_kg, mT_kg;
    private List<KgBean.KgBeanSub> kgList = new ArrayList<KgBean.KgBeanSub>();
    private List<DateBean.DateBeanSub> dateList = new ArrayList<DateBean.DateBeanSub>();
    private KgAdapter kgAdapter;
    private DateAdapter dateAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_weight);


        mEdtTodayWeight = (EditText) findViewById(R.id.edtTodayWeight);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mChart = (LineChart) findViewById(R.id.chart);

        findViewById(R.id.btnAddWeight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WeightProc().execute(Constants.BASE_URL + "/rest/insertBoard.do");
            }
        });
        findViewById(R.id.btnDeleteWeight).setOnClickListener(btnDeleteClick);
        findViewById(R.id.btnResult).setOnClickListener(btnResultClick);

        kgAdapter = new KgAdapter(this);
        dateAdapter = new DateAdapter(this);

        new GraphKgTask().execute(Constants.BASE_URL+"/rest/selectBoardList.do");

    }

    @Override
    protected void onResume() {
        super.onResume();

        new GraphKgTask().execute(Constants.BASE_URL+"/rest/selectBoardList.do");

    }

    //삭제버튼 클릭
    private View.OnClickListener btnDeleteClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MyWeightActivity.this);

            builder.setTitle("")
                    .setMessage("오늘의 몸무게를 삭제하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new WeightProc().execute(Constants.BASE_URL + "/rest/deleteBoard.do");
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
    };//end btn DeleteOkClick

    //결과확인 버튼 클릭
    private View.OnClickListener btnResultClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MyWeightActivity.this);

            if(mY_kg>=mT_kg) {
                int kg = mY_kg- mT_kg;
                builder.setTitle("")
                        .setMessage("오늘은 어제보다 " + kg + "kg 감량에 성공하였습니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }); //취소버튼 클릭시 설정
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                int kg = mT_kg- mY_kg;
                builder.setTitle("")
                        .setMessage("오늘은 어제보다 " + kg + "kg 증가하였습니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }); //취소버튼 클릭시 설정
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    };


    //오늘의 몸무게관리 클래스
    private class WeightProc extends AsyncTask<String, Void, String> {

        private List<WeightBean.WeightBeanSub> weightList;

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
            Date date = new Date();
            mDate = dateFormat.format(date);
            mD_kg = mEdtTodayWeight.getText().toString();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                map.add("date", mDate);
                map.add("d_kg", mD_kg);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.ALL.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

                return restTemplate.postForObject(params[0], request, String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }   //end doInBackground

        @Override
        protected void onPostExecute(String s) {
            mProgressBar.setVisibility(View.INVISIBLE);
            Gson gson = new Gson();
            try {
                WeightBean bean = gson.fromJson(s, WeightBean.class);
                if (bean != null) {
                    if (bean.getResult().equals("ok")) {
                        weightList = bean.getWeightList();
                    } else {
                        Toast.makeText(MyWeightActivity.this, bean.getResultMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(MyWeightActivity.this, "파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            new GraphKgTask().execute(Constants.BASE_URL+"/rest/selectBoardList.do");
        }//end onPostExecute
    }//end class WeightProc



    //몸무게 그래프구현
    public void showChart() {
//
        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();

        for(int num=0; num<kgList.size(); num++){
            KgBean.KgBeanSub bean = kgList.get(num);
            try{
                if(bean.getD_kg()!=null){
                    valsComp1.add( new Entry(Integer.parseInt(bean.getD_kg()), num ) );
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        KgBean.KgBeanSub bean = kgList.get(kgList.size()-1);
        mT_kg = Integer.parseInt(bean.getD_kg());
        KgBean.KgBeanSub bean2 = kgList.get(kgList.size()-2);
        mY_kg = Integer.parseInt(bean2.getD_kg());





        LineDataSet setCom1 = new LineDataSet(valsComp1, "몸무게/날짜");
        setCom1.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setCom1);


        ArrayList<String> xVals = new ArrayList<String>();
        for(int num=0; num<dateList.size(); num++){
            DateBean.DateBeanSub bean3 = dateList.get(num);
            try{
                if(bean3.getDate()!=null){
                    xVals.add( bean3.getDate().substring(5,10) );
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }




        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(14f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);



        LineData data = new LineData(xVals, dataSets);

        mChart.setData(data);
        mChart.invalidate();
    }



    //그래프 데이터 받아오는!
    private class GraphKgTask extends AsyncTask<String, Void, String> {

        private String URL_KG_LIST = Constants.BASE_URL+"/rest/selectBoardList.do";


        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
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
            mProgressBar.setVisibility(View.INVISIBLE);
            Gson gson = new Gson();
            try {
                KgBean kgBean = gson.fromJson(s, KgBean.class);

                if (kgBean != null) {
                    if (kgBean.getResult().equals("ok")) {
                        kgList = kgBean.getSelectBoardList();
                        kgAdapter.upKgListTask();
                    } else {
                        if( kgBean.getResultMsg().equals("fail") ){
                            Toast.makeText(MyWeightActivity.this, "몸무게 불러오기 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(MyWeightActivity.this, "몸무게파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            new GraphDateTask().execute();
        }//end onPostExecute
    }//end UpTask


    //그래프 날짜 받아오는!
    private class GraphDateTask extends AsyncTask<String, Void, String> {

        private String URL_DATE_LIST = Constants.BASE_URL+"/rest/dateList.do";


        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
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
            mProgressBar.setVisibility(View.INVISIBLE);
            Gson gson = new Gson();
            try {
                DateBean dateBean = gson.fromJson(s, DateBean.class);

                if (dateBean != null) {
                    if (dateBean.getResult().equals("ok")) {
                        dateList = dateBean.getDateList();
                        dateAdapter.upDateListTask();
                    } else {
                        if(dateBean.getResultMsg().equals("fail")) {
                            Toast.makeText(MyWeightActivity.this, "날짜 불러오기 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(MyWeightActivity.this, "날짜파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            showChart();
        }//end onPostExecute
    }//end UpTask

}
