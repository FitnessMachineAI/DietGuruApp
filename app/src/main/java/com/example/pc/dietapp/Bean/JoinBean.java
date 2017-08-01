package com.example.pc.dietapp.Bean;

import android.content.Context;
import android.graphics.Paint;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Created by pc on 2017-07-27.
 */

public class JoinBean extends CommonBean implements Serializable{

    private JoinBeanSub joinBean;
    private List<JoinBeanSub> joinList;

    public static class JoinBeanSub implements Serializable{
        private String userId;
        private String userPw;
        private String cm;
        private String kg;
        private String h_kg;
        private String word;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserPw() {
            return userPw;
        }

        public void setUserPw(String userPw) {
            this.userPw = userPw;
        }

        public String getCm() {
            return cm;
        }

        public void setCm(String cm) {
            this.cm = cm;
        }

        public String getKg() {
            return kg;
        }

        public void setKg(String kg) {
            this.kg = kg;
        }

        public String getH_kg() {
            return h_kg;
        }

        public void setH_kg(String h_kg) {
            this.h_kg = h_kg;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }
    }//end JoinBeanSub


    public JoinBeanSub getJoinBean() {
        return joinBean;
    }

    public void setJoinBean(JoinBeanSub joinBean) {
        this.joinBean = joinBean;
    }

    public List<JoinBeanSub> getJoinList() {
        return joinList;
    }

    public void setJoinList(List<JoinBeanSub> joinList) {
        this.joinList = joinList;
    }



    public void saveBean(Context mContext, String fileName){
        try {
            FileOutputStream fos = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            fos.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public JoinBean loadBean(Context mContext, String fileName){
        try {
            FileInputStream fis = mContext.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            JoinBean beanUtil = (JoinBean) is.readObject();
            is.close();
            fis.close();
            return  beanUtil;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
}
