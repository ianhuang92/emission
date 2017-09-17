package monash.emission.entity;

import java.util.Date;

/**
 * Created by 80576 on 31/08/2017.
 */

public class EmissionRecord {

    private String id;
    private String type;
    private String name;
    private Date startdate;
    private Date enddate;
   // private Date recordDate;
    private double level;
    private double beans;
    private String username;



    public EmissionRecord(String emissionType, String emissionName, Date startDate, Date endDate, double level, String username) {
        this.type = emissionType;
        this.name= emissionName;
        this.startdate = startDate;
        this.enddate = endDate;
        this.level = level;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public EmissionRecord(String id, String emissionType, String emissionName, Date startDate, Date endDate, double level, double beans) {
        this.type = emissionType;
        this.beans = beans;
        this.id = id;
        this.name = emissionName;
        this.startdate = startDate;
        this.enddate = endDate;
      //  this.recordDate = recordDate;
        this.level = level;
    }

    public double getBeans() {
        return beans;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBeans(double beans) {
        this.beans = beans;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

/*    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }*/

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }
}
