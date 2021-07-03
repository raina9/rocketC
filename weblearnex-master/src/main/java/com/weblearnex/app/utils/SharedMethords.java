package com.weblearnex.app.utils;

import com.google.gson.Gson;
import com.weblearnex.app.config.AppProperty;
import com.weblearnex.app.entity.master.ApiConfig;
import com.weblearnex.app.entity.order.PacketHistory;
import com.weblearnex.app.entity.order.SaleOrder;
import com.weblearnex.app.entity.setup.Status;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SharedMethords {

    public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String ONLY_DATE_FORMAT = "yyyy-MM-dd";

    public static Pageable setPages(int pageNumber){
        return PageRequest.of(pageNumber, 100 );
    }
    public static String getCurrentDate(){

        try{
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            return dateFormat.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String getDate(Date date){
        try{
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            return dateFormat.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String getDate(Long milisecond){
        try{
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            return dateFormat.format(new Date(milisecond));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDate(String dateString, String dateFormat){
        try{
            if(dateString == null || dateString.trim().isEmpty()){
               return null;
            }
            DateFormat dateFormatObj = new SimpleDateFormat(dateFormat);
            return dateFormatObj.parse(dateString);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isValidPacketHistoryDate(Date fromDate, Date toDate){
        if(fromDate == null || toDate == null){
            return false;
        }
        if (fromDate.compareTo(toDate) > 0) {
            // When  fromDate > Date toDate
            return false;
        } else if (fromDate.compareTo(toDate) < 0) {
            // When Date fromDate < Date toDate
            return true;
        } else if (fromDate.compareTo(toDate) == 0) {
            return true;
        }
        return false;
    }
    public static PacketHistory getLastPacketHistory(SaleOrder saleOrder){

        if(saleOrder==null || saleOrder.getPacketHistory()==null||saleOrder.getPacketHistory().isEmpty()){
            return null;
        }
        return saleOrder.getPacketHistory().get(saleOrder.getPacketHistory().size()-1);
    }

    public static String getOnlyDate(Date date){
        try{
            DateFormat dateFormat = new SimpleDateFormat(ONLY_DATE_FORMAT);
            return dateFormat.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
