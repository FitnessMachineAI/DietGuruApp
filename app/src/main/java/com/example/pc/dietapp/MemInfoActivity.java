package com.example.pc.dietapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.pc.dietapp.Bean.JoinBean;
import com.example.pc.dietapp.Util.ActivityUtil;
import com.example.pc.dietapp.Util.FileUtil;

public class MemInfoActivity extends AppCompatActivity {

    private TextView mtxtInfoId, mtxtInfoCm, mtxtInfoKg, mtxtInfoHkg, mtxtInfoWord;
    private JoinBean join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_info);

//        JoinBean.JoinBeanSub bean = (JoinBean.JoinBeanSub) getIntent().getSerializableExtra("joinBean");
        join = FileUtil.getMemberBean(this);

        mtxtInfoId = (TextView)findViewById(R.id.txtInfoId);
        mtxtInfoCm = (TextView)findViewById(R.id.txtInfoCm);
        mtxtInfoKg = (TextView)findViewById(R.id.txtInfoKg);
        mtxtInfoHkg = (TextView)findViewById(R.id.txtInfoHKg);
        mtxtInfoWord = (TextView)findViewById(R.id.txtInfoWord);

        mtxtInfoId.setText(join.getJoinBean().getUserId());
        mtxtInfoCm.setText(join.getJoinBean().getCm() + "\tcm");
        mtxtInfoKg.setText(join.getJoinBean().getKg() + "\tKg");
        mtxtInfoHkg.setText(join.getJoinBean().getH_kg() + "\tKg");
        mtxtInfoWord.setText(join.getJoinBean().getWord());


        findViewById(R.id.btnEdt).setOnClickListener(btnEdtClick);

        ActivityUtil.setActionBarColor(this, R.color.colorAccent);
    }//end OnCreate

    private View.OnClickListener btnEdtClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MemInfoActivity.this, MemUpActivity.class);
            startActivity(intent);
        }
    };
}
