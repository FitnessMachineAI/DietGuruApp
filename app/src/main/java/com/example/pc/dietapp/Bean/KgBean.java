package com.example.pc.dietapp.Bean;

import java.util.List;

/**
 * Created by pc on 2017-07-28.
 */

public class KgBean extends CommonBean {
    private KgBeanSub kgBean;
    private List<KgBeanSub> selectBoardList;

    public class KgBeanSub{
        private String d_kg;

        public String getD_kg() {
            return d_kg;
        }

        public void setD_kg(String d_kg) {
            this.d_kg = d_kg;
        }
    }

    public KgBeanSub getKgBean() {
        return kgBean;
    }

    public void setKgBean(KgBeanSub kgBean) {
        this.kgBean = kgBean;
    }

    public List<KgBeanSub> getSelectBoardList() {
        return selectBoardList;
    }

    public void setSelectBoardList(List<KgBeanSub> selectBoardList) {
        this.selectBoardList = selectBoardList;
    }
}
