package com.example.pc.dietapp.Util;

import android.content.Context;

import com.example.pc.dietapp.Bean.JoinBean;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by pc on 2017-08-01.
 */

public class FileUtil {
    public static void setJoinBean(Context context, JoinBean joinBean){
        try {
            FileOutputStream fos = context.openFileOutput(JoinBean.class.getName(), Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(joinBean);
            os.close();
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static JoinBean getMemberBean(Context context){
        JoinBean joinBean = null;
        try{
            FileInputStream fis = context.openFileInput(JoinBean.class.getName());
            ObjectInputStream is = new ObjectInputStream(fis);
            joinBean = (JoinBean) is.readObject();
            is.close();
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return joinBean;
    }

}
