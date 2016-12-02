package pl.jlabs.blog.airpolution.domain;

import java.util.Date;

public class Measurement {

    private Date time;
    private String location;
    private double pm10Concentration;
    private double pm2_5Concentration;
    private double gasConcentration;
    
    public Date getTime() {
        return time;
    }
    
    public void setTime(Date time) {
        this.time = time;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public double getPm10Concentration() {
        return pm10Concentration;
    }
    
    public void setPm10Concentration(double pm10Concentration) {
        this.pm10Concentration = pm10Concentration;
    }
    
    public double getPm2_5Concentration() {
        return pm2_5Concentration;
    }
    
    public void setPm2_5Concentration(double pm2_5Concentration) {
        this.pm2_5Concentration = pm2_5Concentration;
    }
    
    public double getGasConcentration() {
        return gasConcentration;
    }
    public void setGasConcentration(double gasConcentration) {
        this.gasConcentration = gasConcentration;
    }
}
