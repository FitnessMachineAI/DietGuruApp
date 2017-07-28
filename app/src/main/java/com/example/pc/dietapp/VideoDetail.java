package com.example.pc.dietapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.pc.dietapp.Bean.VideoDataBean;

public class VideoDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        TextView txtTitle = (TextView)findViewById(R.id.txtTitle);
        TextView txtPart = (TextView)findViewById(R.id.txtPart);
        WebView webView = (WebView)findViewById(R.id.wbvYoutube);
        webView.getSettings().setJavaScriptEnabled(true);

        VideoDataBean video = (VideoDataBean)getIntent().getSerializableExtra("video");


        if(video!=null)
        {
            webView.loadData(video.getVideoUrl(), "text/html", "utf-8");
            txtTitle.setText(video.getTitle());
            txtPart.setText(video.getPart());
        }
    }

}
