package com.example.pc.dietapp.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pc on 2017-07-28.
 */

public class DateBean extends CommonBean implements Serializable{
    private DateBeanSub dateBean;
    private List<DateBeanSub> dateList;

    public class DateBeanSub implements Serializable{
        private String date;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    public DateBeanSub getDateBean() {
        return dateBean;
    }

    public void setDateBean(DateBeanSub dateBean) {
        this.dateBean = dateBean;
    }

    public List<DateBeanSub> getDateList() {
        return dateList;
    }

    public void setDateList(List<DateBeanSub> dateList) {
        this.dateList = dateList;
    }
}
