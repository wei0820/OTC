package com.tools.payhelper.pay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateManager {

    public  DateManager() {

    }
    public static  DateManager dateManagerInstance = null;

    public static  DateManager getDateManagerInstance(){
        if (dateManagerInstance ==null){
            synchronized(DateManager.class){
                if (dateManagerInstance == null){
                    dateManagerInstance = new DateManager();
                }
            }
        }
        return dateManagerInstance;
    }


}
