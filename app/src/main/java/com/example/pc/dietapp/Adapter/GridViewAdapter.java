package com.example.pc.dietapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.dietapp.Bean.VideoDataBean;
import com.example.pc.dietapp.R;
import com.example.pc.dietapp.VideoDetail;

import java.util.List;

/**
 * Created by jikur on 2017-07-27.
 */

public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private List<VideoDataBean>list;


    public GridViewAdapter(Context context, List<VideoDataBean> videoDataList)
    {
        this.context=context;
        this.list=videoDataList;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //video_item 가져오기
        LayoutInflater lf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=lf.inflate(R.layout.lay_video, parent, false);

        //데이터 찾기
        final VideoDataBean video = list.get(position);

        //데이터 내용 화면에 표시
        ImageView imgView = (ImageView)convertView.findViewById(R.id.imgView);
        TextView txtTitle = (TextView)convertView.findViewById(R.id.txtTitle);
        txtTitle.setText(video.getTitle());
        imgView.setImageResource(video.getPhoto());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(context, VideoDetail.class);
                i.putExtra("video",video);

                context.startActivity(i);
            }
        });

        return convertView;
    }
}
