package bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks;

import bgu.spl181.net.impl.UserServiceTextBasedProtocol.User;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Login implements Task {

    public static final String fail="ERROR login failed";
    public static final String succ="ACK login succeeded";

    private String username, password;
    private ConcurrentHashMap<String,Integer> loggedIn;
    private int connectionId;
    private FileManager fileManager;

    public Login(String username,String password,ConcurrentHashMap<String,Integer> loggedIn,int connectionId,FileManager fileManager)
    {
        this.username=username;
        this.password=password;
        this.loggedIn=loggedIn;
        this.connectionId=connectionId;
        this.fileManager=fileManager;

    }

    @Override
    public String run() {
        boolean login=false;
        if (loggedIn.containsKey(username)|loggedIn.containsValue(connectionId))
            return fail;
        ConcurrentLinkedQueue<User> users= fileManager.acquireUserReadlock();
        for(User user:users)
            if(user.getUsername().compareTo(username)==0&&user.getPassword().compareTo(password)==0) {
                loggedIn.put(username,connectionId);
                login = true;
            }
        fileManager.releaseUserReadLock();
        if(login)
            return succ;
        else return fail;

    }
}
