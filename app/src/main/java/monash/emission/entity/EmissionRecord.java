package monash.emission.entity;

import java.util.Date;

/**
 * Created by 80576 on 31/08/2017.
 */

public class EmissionRecord {
    private String emissionType;
    private String emissionName;
    private Date startDate;
    private Date endDate;
   // private Date recordDate;
    private double level;

    public EmissionRecord(String emissionType, String emissionName, Date startDate, Date endDate, double level) {
        this.emissionType = emissionType;
        this.emissionName = emissionName;
        this.startDate = startDate;
        this.endDate = endDate;
      //  this.recordDate = recordDate;
        this.level = level;
    }

    public String getEmissionType() {
        return emissionType;
    }

    public void setEmissionType(String emissionType) {
        this.emissionType = emissionType;
    }

    public String getEmissionName() {
        return emissionName;
    }

    public void setEmissionName(String emissionName) {
        this.emissionName = emissionName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
