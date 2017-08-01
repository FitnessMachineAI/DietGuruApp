package com.example.pc.dietapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


public class Tab3Fragment extends Fragment {

    public Tab3Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.gridViewUp);

        /*
        //데이터 생성
        VideoDataBean video1 = new VideoDataBean(R.drawable.sample, "영상제목1", "운동팁");
        video1.setVideoUrl("<div style=\"position:relative;height:0;padding-bottom:56.25%\"><iframe src=\"https://www.youtube.com/embed/J0h8-OTC38I?ecver=2\" width=\"640\" height=\"360\" frameborder=\"0\" style=\"position:absolute;width:100%;height:100%;left:0\" allowfullscreen></iframe></div>");
        VideoDataBean video2 = new VideoDataBean(R.drawable.sample, "영상제목2", "상체");
        video2.setVideoUrl("");
        VideoDataBean video3 = new VideoDataBean(R.drawable.sample, "영상제목3", "상체");
        video3.setVideoUrl("");
        VideoDataBean video4 = new VideoDataBean(R.drawable.sample, "영상제목4", "상체");
        video4.setVideoUrl("");
        VideoDataBean video5 = new VideoDataBean(R.drawable.sample, "영상제목4", "상체");
        video5.setVideoUrl("");
        VideoDataBean video6 = new VideoDataBean(R.drawable.sample, "영상제목4", "상체");
        video6.setVideoUrl("");
        VideoDataBean video7 = new VideoDataBean(R.drawable.sample, "영상제목4", "상체");
        video7.setVideoUrl("");
        VideoDataBean video8 = new VideoDataBean(R.drawable.sample, "영상제목4", "상체");
        video8.setVideoUrl("");


        List<VideoDataBean> list = new ArrayList<VideoDataBean>();
        list.add(video1);
        list.add(video2);
        list.add(video3);
        list.add(video4);
        list.add(video5);
        list.add(video6);
        list.add(video7);
        list.add(video8);



        GridViewAdapter adapter = new GridViewAdapter(getContext(), list);
        gridView.setAdapter(adapter);
        */
        return view;

    }
}
