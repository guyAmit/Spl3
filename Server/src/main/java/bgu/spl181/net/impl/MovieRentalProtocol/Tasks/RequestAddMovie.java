package bgu.spl181.net.impl.MovieRentalProtocol.Tasks;

import bgu.spl181.net.impl.MovieRentalProtocol.Movie;
import bgu.spl181.net.impl.MovieRentalProtocol.MovieUser;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.FileManager;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.Task;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.User;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RequestAddMovie implements Task {

    public static final String fail="ERROR request addmovie failed";
    public static final String succ="ACK request addmovie";//remeber to add movie name+"success"
    public static final String BROADCAST="BROADCAST movie";//remember to add movie name, number of copies left, price of movie


    private FileManager fileManager;
    private String userName;
    private String movieName;
    private Integer price;
    private Integer availableAmount;
    private ArrayList<String> bannedCountries;

    public RequestAddMovie(FileManager fileManager, String userName, String movieName,  Integer availableAmount,Integer price, ArrayList<String> bannedCountries) {
        this.fileManager = fileManager;
        this.userName = userName;
        this.movieName = movieName;
        this.price = price;
        this.availableAmount = availableAmount;
        this.bannedCountries = bannedCountries;
    }


    @Override
    public String run(){
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
        if(user.getType().compareTo("admin")==0 && movie==null && this.price>=0 && this.availableAmount>=0){
            FileManager.MaxId++;
            movie = new Movie(String.valueOf(FileManager.MaxId),this.movieName,String.valueOf(this.price),this.bannedCountries,String.valueOf(this.availableAmount),String.valueOf(this.availableAmount));
            movies.add(movie);
            this.fileManager.releaseUserWriteLock(true);
            this.fileManager.releaseMovieWriteLock(true);
            return succ+" \""+this.movieName+"\" success"+"  BROADCAST movie \""+movieName+"\" "+this.availableAmount+" "+price;
        }
        else{
            this.fileManager.releaseUserWriteLock(false);
            this.fileManager.releaseMovieWriteLock(false);
            return fail;
        }
    }
}
