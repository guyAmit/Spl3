package bgu.spl181.net.impl.MovieRentalProtocol.Tasks;

import bgu.spl181.net.impl.MovieRentalProtocol.Movie;
import bgu.spl181.net.impl.MovieRentalProtocol.MovieUser;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.FileManager;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.Task;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.User;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RequestAddBalance implements Task{


    public static final String succ="ACK balance ";
    public static final String fail="ERROR request balance add failed";

    private String username;
    private FileManager fileManager;
    private String amount;

    public RequestAddBalance(String username,String amount,FileManager fileManager) {
        this.username = username;
        this.fileManager = fileManager;
        this.amount=amount;
    }

    @Override
    public String run() {
        ConcurrentLinkedQueue<User> users = this.fileManager.acquireUserWritelock();
        for (User user : users){
            if(user.getUsername().compareTo(this.username)==0){
                int newBalance = (Integer.valueOf(((MovieUser)(user)).getBalance())+Integer.parseInt(this.amount));
                ((MovieUser)user).setBalance(String.valueOf(newBalance));
                fileManager.releaseUserWriteLock(true);
                return succ+newBalance+" added "+amount;
            }

        }
        fileManager.releaseUserWriteLock(false);
        return fail;
    }
}
