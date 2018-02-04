package bgu.spl181.net.impl.UserServiceTextBasedProtocol;

import com.google.gson.annotations.SerializedName;

public class User {


    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    private String additionalData;


    public User(String username, String password,String additionalData) {
        this.username = username;
        this.password = password;
        this.additionalData=additionalData;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }
}
