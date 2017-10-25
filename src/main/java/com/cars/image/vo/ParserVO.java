package com.cars.image.vo;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by balaajiparthasarathy on 2/15/17.
 */
@Component
public class ParserVO {

    public ParserVO(){
    }
    public ParserVO(String error){
        setError(error);
    }
    public ParserVO(String vin, String licensePlate, String error){
        setVin(vin);
        setLicensePlate(licensePlate);
        setError(error);
    }


    private List<String> phoneNumber;
    private String vin;
    private String licensePlate;
    private byte[] image;
    private String parsedText;
    private String error;



    public String getParsedText() {
        return parsedText;
    }

    public void setParsedText(String parsedText) {
        this.parsedText = parsedText;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public List<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(List<String> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }



}
