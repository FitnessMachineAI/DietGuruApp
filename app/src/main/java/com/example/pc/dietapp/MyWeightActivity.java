package com.example.pc.dietapp;

//sj
//그래프 참고 사이트 http://namgh.blogspot.kr/2016/05/mpandroidchart-1.html

//1 날짜는 어떻게 받아와야하지?
//2 가장 최신 데이터는 어떻게 받아와야하지?
//3 서버에서 데이터를 15개 불러와서 어떻게 함수를 구현해야하지?


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pc.dietapp.Adapter.WeightAdapter;
import com.example.pc.dietapp.Bean.JoinBean;
import com.example.pc.dietapp.Bean.KgBean;
import com.example.pc.dietapp.Bean.WeightBean;
import com.example.pc.dietapp.Util.Constants;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyWeightActivity extends AppCompatActivity {

    private EditText mEdtTodayWeight;
    private ProgressBar mProgressBar;
    private LineChart mChart;
    private String mDate, mD_kg;


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

        showChart();
        new GraphKgTask().execute(Constants.BASE_URL+"/rest/seleceBoardList.do");
    }

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


    //오늘의 몸무게관리 클래스
    private class WeightProc extends AsyncTask<String, Void, String> {

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
                //map.add(" " <- 이부분은 memberBean의 이름과 같게 해주어야함!!!!! 꼭!!!!!!!
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
                JoinBean bean = gson.fromJson(s, JoinBean.class);
                if (bean != null) {
                    if (bean.getResult().equals("ok")) {
                    } else {
                        Toast.makeText(MyWeightActivity.this, bean.getResultMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(MyWeightActivity.this, "파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }//end onPostExecute
    }//end class WeightProc


    //몸무게 그래프구현
    public void showChart() {
        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();

        valsComp1.add(new Entry(100.0f, 0));
        valsComp1.add(new Entry(500.0f, 1));
        valsComp1.add(new Entry(75.0f, 2));
        valsComp1.add(new Entry(50.0f, 3));

        LineDataSet setCom1 = new LineDataSet(valsComp1, "몸무게/날짜");
        setCom1.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setCom1);

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("1.0");
        xVals.add("1.0");
        xVals.add("1.0");
        xVals.add("1.0");


        LineData data = new LineData(xVals, dataSets);

        mChart.setData(data);
        mChart.invalidate();
    }



    private class GraphKgTask extends AsyncTask<String, Void, String> {

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
                //map.add(" " <- 이부분은 memberBean의 이름과 같게 해주어야함!!!!! 꼭!!!!!!!
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
                JoinBean bean = gson.fromJson(s, JoinBean.class);
                if (bean != null) {
                    if (bean.getResult().equals("ok")) {
                    } else {
                        Toast.makeText(MyWeightActivity.this, bean.getResultMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(MyWeightActivity.this, "파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }//end onPostExecute
    }//end UpTask

}
