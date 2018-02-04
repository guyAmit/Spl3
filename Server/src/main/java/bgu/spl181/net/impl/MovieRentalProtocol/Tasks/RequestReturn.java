package bgu.spl181.net.impl.MovieRentalProtocol.Tasks;

import bgu.spl181.net.impl.MovieRentalProtocol.Movie;
import bgu.spl181.net.impl.MovieRentalProtocol.MovieUser;
import bgu.spl181.net.impl.MovieRentalProtocol.RentedMovie;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.FileManager;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.Task;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.User;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RequestReturn implements Task {

    public static final String fail="ERROR request return failed";
    public static final String succ="ACK return";

    private String userName;
    private String movieName;
    private FileManager fileManager;

    public RequestReturn(String userName, String movieName, FileManager fileManager) {
        this.userName = userName;
        this.movieName = movieName;
        this.fileManager=fileManager;
    }

    @Override
    public String run() {
        ConcurrentLinkedQueue<User> users = this.fileManager.acquireUserWritelock();
        ConcurrentLinkedQueue<Movie> movies =this.fileManager.acquireMovieWritelock();
        Movie movie =null;
        for(Movie m : movies){
            if(m.getName().compareTo(this.movieName)==0){
                movie=m;
                break;
            }
        }
        if(movie==null){
            this.fileManager.releaseUserWriteLock(false);
            this.fileManager.releaseMovieWriteLock(false);
            return fail;
        }
        MovieUser user =null;
        for(User u : users){
            if(u.getUsername().compareTo(this.userName)==0) {
                user = (MovieUser) u;
                break;
            }
        }
        RentedMovie found=null;
        for (RentedMovie m : user.getMovies()){
            if(m.getName().compareTo(this.movieName)==0){
                found=m;
                break;
            }
        }
        if(found!=null){
            user.getMovies().remove(found);
            movie.setAvailableAmount(String.valueOf(Integer.parseInt(movie.getAvailableAmount())+1));
            String available=movie.getAvailableAmount().substring(0);
            String price=movie.getPrice().substring(0);
            this.fileManager.releaseMovieWriteLock(true);
            this.fileManager.releaseUserWriteLock(true);
            return succ+" \""+this.movieName+"\" success"+"  BROADCAST movie \""+movieName+"\" "+available+" "+price;
        }else{
            this.fileManager.releaseUserWriteLock(false);
            this.fileManager.releaseMovieWriteLock(false);
            return fail;
        }

    }
}
