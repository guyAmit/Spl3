package bgu.spl181.net.impl.MovieRentalProtocol;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import java.util.concurrent.atomic.AtomicInteger;

public class Movie {

    //todo: find a way to make all integer fields string based s.t. in the json file they will appear in ""


    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private String price;
    @SerializedName("bannedCountries")
    private ArrayList<String> bannedCountries;
    @SerializedName("availableAmount")
    private String availableAmount;
    @SerializedName("totalAmount")
    private String totalAmount;

    public Movie(String id, String name, String price, ArrayList<String> bannedCountries, String availableAmount, String totalAmount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.bannedCountries = bannedCountries;
        this.availableAmount = availableAmount;
        this.totalAmount = totalAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<String> getBannedCountries() {
        return bannedCountries;
    }

    public void setBannedCountries(ArrayList<String> bannedCountries) {
        this.bannedCountries = bannedCountries;
    }

    public String getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(String availableAmount) {
        this.availableAmount=availableAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
