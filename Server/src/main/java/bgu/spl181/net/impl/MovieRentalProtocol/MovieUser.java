package bgu.spl181.net.impl.MovieRentalProtocol;

import bgu.spl181.net.impl.UserServiceTextBasedProtocol.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieUser extends User {

    //todo: find a way to make all integer fields string based s.t. in the json file they will appear in ""

    @SerializedName("type")
    private String type;
    @SerializedName("country")
    private String country;
    @SerializedName("balance")
    private String balance;
    @SerializedName("movies")
    private ArrayList<RentedMovie> movies;

    public MovieUser(String username, String password,String type,String country,String balance,ArrayList<RentedMovie> movies) {
        super(username, password,"");
        this.type = type;
        this.country = country;
        this.balance = balance;
        this.movies = movies;
    }

    public String getType() {
        return type;
    }

    public String getCountry() {
        return country;
    }

    public String getBalance() {
        return balance;
    }

    public ArrayList<RentedMovie> getMovies() {
        return movies;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setMovies(ArrayList<RentedMovie> movies) {
        this.movies = movies;
    }
}
