package monash.emission.entity;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by 80576 on 30/08/2017.
 */

public class UserInfo {
    private String username;
    private String password;
    private String factoryType;
    private ArrayList<EmissionRecord> emissionRecords;

    /*
    按用户输入起始日期和终止日期筛选，对于筛选结果按照起始日期升序排序
     */
    public ArrayList<EmissionRecord> sortByDate(Date startDate,Date endDate)
    {
        ArrayList<EmissionRecord> tempList = new ArrayList<>();
        Iterator iterator = emissionRecords.iterator();
        while (iterator.hasNext())
        {
            EmissionRecord e = (EmissionRecord)iterator.next();
           if (e.getStartdate().compareTo(startDate)>=0 && e.getEnddate().compareTo(endDate)<=0) //记录中起始日期大于等于用户输入日期 && 记录终止日期小于等于用户输入终止日期
           {
               tempList.add(e);
           }
        }
        Log.i("Filter",tempList.size()+"");
        Collections.sort(tempList,new dateComparator());
        return tempList;
    }
/*
        Comparator for sortByDate
 */
    private class dateComparator implements Comparator{
        @Override
        public int compare(Object o1, Object o2) {
            EmissionRecord d1 = (EmissionRecord)o1;
            EmissionRecord d2 = (EmissionRecord)o2;
           return d1.getStartdate().compareTo(d2.getStartdate());
        }
    }

    public UserInfo(String username, String password, String factoryType, ArrayList<EmissionRecord> emissionRecords) {
        this.username = username;
        this.password = password;
        this.factoryType = factoryType;
        this.emissionRecords = emissionRecords;
    }

    public String getUsername() {
        return username;
    }
public void addEmission(EmissionRecord e){
    emissionRecords.add(e);
}
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFactoryType() {
        return factoryType;
    }

    public void setFactoryType(String factoryType) {
        this.factoryType = factoryType;
    }

    public ArrayList<EmissionRecord> getEmissionRecords() {
        return emissionRecords;
    }

    public void setEmissionRecords(ArrayList<EmissionRecord> emissionRecords) {
        this.emissionRecords = emissionRecords;
    }

    public void addEmissionRecords(ArrayList<EmissionRecord> result) {
        emissionRecords.addAll(result);
    }
}
