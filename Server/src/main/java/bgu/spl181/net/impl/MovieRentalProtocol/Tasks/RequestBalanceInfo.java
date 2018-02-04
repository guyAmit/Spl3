package bgu.spl181.net.impl.MovieRentalProtocol.Tasks;

import bgu.spl181.net.impl.MovieRentalProtocol.Movie;
import bgu.spl181.net.impl.MovieRentalProtocol.MovieUser;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.FileManager;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.Task;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.User;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RequestBalanceInfo implements Task {

    public static final String succ="ACK balance ";
    public static final String fail="ERROR request balance info failed";

    private String username;
    private FileManager fileManager;


    public RequestBalanceInfo(String username, FileManager fileManager) {
        this.username=username;
        this.fileManager=fileManager;
    }

    @Override
    public String run() {
        ConcurrentLinkedQueue<User> users = this.fileManager.acquireUserReadlock();
        for (User user : users){
            if(user.getUsername().compareTo(this.username)==0){
                String balanceInfo = ((MovieUser)user).getBalance();
                fileManager.releaseUserReadLock();
                return succ+balanceInfo;
            }

        }
        fileManager.releaseUserReadLock();
        return fail;
    }
}
