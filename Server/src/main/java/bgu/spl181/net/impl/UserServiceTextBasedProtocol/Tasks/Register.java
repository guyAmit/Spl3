package bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks;

import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Connections;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Register implements Task {


    public static final String fail="ERROR registration failed";
    public static final String succ="ACK registration succeeded";

    private String username,password,additionalData;
    private FileManager fileManager;
    private ConcurrentHashMap<String,Integer> loggedIn;
    private Integer connectionId;

    public Register(String username, String password, String additionalData, FileManager fileManager, ConcurrentHashMap<String,Integer> loggedIn,Integer conncteionId) {

        this.username=username;
        this.password=password;
        this.additionalData=additionalData;
        this.fileManager=fileManager;
        this.loggedIn=loggedIn;
        this.connectionId=conncteionId;
    }

    @Override
    public String run() {
        if(loggedIn.containsKey(this.username) | loggedIn.containsValue(this.connectionId))
            return fail;
        ConcurrentLinkedQueue<User> users = fileManager.acquireUserWritelock();
        for (User user : users){
            if(user.getUsername().compareTo(this.username)==0){
                fileManager.releaseUserWriteLock(false);
                return fail;
            }
        }
        User user = new User(this.username,this.password,this.additionalData);
        users.add(user);
        this.fileManager.releaseUserWriteLock(true);
        return succ;
    }
}
