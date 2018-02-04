package bgu.spl181.net.impl.MovieRentalProtocol.Tasks;

import bgu.spl181.net.impl.MovieRentalProtocol.Movie;
import bgu.spl181.net.impl.MovieRentalProtocol.MovieUser;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.FileManager;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.Task;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.User;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RequestReMovie implements Task {

    public static final String fail="ERROR request remmovie failed";
    public static final String succ="ACK remmovie";//remeber to add movie name+"success"
    public static final String BROADCAST="BROADCAST movie";//remember to add movie name, number of copies left, price of movie


    private FileManager fileManager;
    private String userName;
    private String movieName;

    public RequestReMovie(String userName, String movieName,FileManager fileManager) {
        this.fileManager = fileManager;
        this.userName = userName;
        this.movieName = movieName;
    }

    @Override
    public String run() {
        ConcurrentLinkedQueue<User> users = this.fileManager.acquireUserWritelock();
        ConcurrentLinkedQueue<Movie> movies =this.fileManager.acquireMovieWritelock();

        MovieUser user=null;
        for(User u : users){
            if(u.getUsername().compareTo(this.userName)==0) {
                user = (MovieUser) u;
                break;
            }
        }
        Movie movie=null;
        for (Movie m : movies){
            if(m.getName().compareTo(this.movieName)==0){
                movie=m;
                break;
            }
        }
        if(user.getType().compareTo("admin")==0 && movie!=null && movie.getAvailableAmount().compareTo(movie.getTotalAmount())==0){
            movies.remove(movie);
            this.fileManager.releaseUserWriteLock(true);
            this.fileManager.releaseMovieWriteLock(true);
            return succ+" \""+this.movieName+"\" success"+"  BROADCAST movie \""+movieName+"\" "+"removed";
        }
        else{
            this.fileManager.releaseUserWriteLock(false);
            this.fileManager.releaseMovieWriteLock(false);
            return fail;
        }
    }
}
