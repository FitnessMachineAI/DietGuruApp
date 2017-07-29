package com.example.pc.dietapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.dietapp.Bean.JoinBean;
import com.example.pc.dietapp.Util.Constants;
import com.google.gson.Gson;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class MemInfoActivity extends AppCompatActivity {

    private TextView mtxtInfoId, mtxtInfoCm, mtxtInfoKg, mtxtInfoHkg, mtxtInfoWord;
    private ProgressBar mProgressBar;
    private Button mBtnUp;
    private MemAdapter memAdapter;
    private ListView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_info);

       // JoinBean.JoinBeanSub joinBean = (JoinBean.JoinBeanSub)getIntent().getSerializableExtra("joinBean");

//        mtxtInfoId = (TextView)findViewById(R.id.txtInfoId);
//        mtxtInfoCm = (TextView)findViewById(R.id.txtInfoCm);
//        mtxtInfoKg = (TextView)findViewById(R.id.txtInfoKg);
//        mtxtInfoHkg = (TextView)findViewById(R.id.txtInfoHKg);
//        mtxtInfoWord = (TextView)findViewById(R.id.txtInfoWord);
//
//        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

//       txtInfoWord.setText(joinBean.getWord());
  //      new MemAdapter.MemberTask.execute(C);
    }

    public class MemAdapter extends BaseAdapter{

        private Context context;
        private JoinBean joinBean;

        public MemAdapter(Context context, JoinBean joinBean){
            this.context = context;
            this.joinBean = joinBean;
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return joinBean.getJoinBean();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void updateMemberListTask() {
            new MemberTask().execute();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //1.
            LayoutInflater If = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = If.inflate(R.layout.activity_mem_info, null);

            //2.
            final JoinBean.JoinBeanSub item = joinBean.getJoinBean();

            //3.
            TextView txtInfoId = (TextView)convertView.findViewById(R.id.txtInfoId);
            TextView txtInfoCm = (TextView)convertView.findViewById(R.id.txtInfoCm);
            TextView txtInfoKg = (TextView)convertView.findViewById(R.id.txtInfoKg);
            TextView txtInfoHKg = (TextView)convertView.findViewById(R.id.txtInfoHKg);
            TextView txtInfoWord = (TextView)convertView.findViewById(R.id.txtInfoWord);

            txtInfoId.setText(item.getUserId());
            txtInfoCm.setText(item.getCm());
            txtInfoKg.setText(item.getKg());
            txtInfoHKg.setText(item.getH_kg());
            txtInfoWord.setText(item.getWord());

            return convertView;
        }

    //회원정보를 가져오는 Task
    class MemberTask extends AsyncTask<String, Void, String> {

        private String URL_MEMBER = Constants.BASE_URL + "/rest/selectMember.do";

        @Override
        protected String doInBackground(String... params) {

            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.ALL.APPLICATION_FORM_URLENCODED);

                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

                return restTemplate.postForObject(URL_MEMBER, request, String.class);
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Gson gson = new Gson();
            try {
                JoinBean bean = gson.fromJson(s, JoinBean.class);
                if (bean != null) {
                    if (bean.getResult().equals("ok")) {
                        JoinBean.JoinBeanSub joinBean = (JoinBean.JoinBeanSub)getIntent().getSerializableExtra("joinBean");
                        //리스트뷰 갱신
                        MemAdapter.this.notifyDataSetInvalidated();
                    } else {
                        Toast.makeText(context, bean.getResultMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(context, "파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    }

}
