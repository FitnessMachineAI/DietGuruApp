package com.example.pc.dietapp;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.pc.dietapp.Bean.VideoDataBean;
import com.example.pc.dietapp.Util.ActivityUtil;

public class VideoDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        TextView txtTitle = (TextView)findViewById(R.id.txtTitle);
        WebView webView = (WebView)findViewById(R.id.wbvYoutube);
        webView.getSettings().setJavaScriptEnabled(true);

        VideoDataBean video = (VideoDataBean)getIntent().getSerializableExtra("video");

        ActivityUtil.setActionBarColor(this, R.color.colorAccent);


        if(video!=null)
        {
            webView.loadData(video.getVideoUrl(), "text/html", "utf-8");
            txtTitle.setText(video.getTitle());
        }

        final Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        Button buttonStart = (Button) findViewById(R.id.buttonstart);
        Button buttonStop = (Button) findViewById(R.id.buttonstop);


        buttonStart.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
            }
        });

        buttonStop.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                chronometer.stop();
            }
        });
    }

}
