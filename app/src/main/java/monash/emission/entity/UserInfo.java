package monash.emission.entity;

import java.util.ArrayList;

/**
 * Created by 80576 on 30/08/2017.
 */

public class UserInfo {
    private String username;
    private String password;
    private String factoryType;
    private ArrayList<EmissionRecord> emissionRecords;

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
