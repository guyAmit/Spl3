package bgu.spl181.net.impl.MovieRentalProtocol.Tasks;

import bgu.spl181.net.impl.MovieRentalProtocol.Movie;
import bgu.spl181.net.impl.MovieRentalProtocol.MovieUser;
import bgu.spl181.net.impl.MovieRentalProtocol.RentedMovie;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.FileManager;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.Task;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.User;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MovieRegister implements Task{

    public static final String fail="ERROR registration failed";
    public static final String succ="ACK registration succeeded";

    private String username,password,country;
    private FileManager fileManager;
    private ConcurrentHashMap<String,Integer> loggedIn;
    private Integer connectionId;


    public MovieRegister(String username, String password, String country, FileManager fileManager, ConcurrentHashMap<String, Integer> loggedIn, int connectionId) {
        this.username=username;
        this.password=password;
        this.country=country.replace("country=","").replace("\"","");
        this.fileManager=fileManager;
        this.loggedIn=loggedIn;
        this.connectionId=connectionId;
    }


    @Override
    public String run() {
        if(loggedIn.containsKey(this.username) | loggedIn.containsValue(this.connectionId))
            return fail;
        ConcurrentLinkedQueue<User> users = this.fileManager.acquireUserWritelock();
        for (User user : users){
            if(user.getUsername().compareTo(this.username)==0){
                fileManager.releaseUserWriteLock(false);
                return fail;
            }
        }
        MovieUser user = new MovieUser(this.username,this.password,"normal",this.country,"0",new ArrayList<RentedMovie>());
        users.add(user);
        this.fileManager.releaseUserWriteLock(true);
        return succ;
    }
}
