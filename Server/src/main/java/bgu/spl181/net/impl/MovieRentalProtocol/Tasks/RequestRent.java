package bgu.spl181.net.impl.MovieRentalProtocol.Tasks;

import bgu.spl181.net.impl.MovieRentalProtocol.Movie;
import bgu.spl181.net.impl.MovieRentalProtocol.MovieUser;
import bgu.spl181.net.impl.MovieRentalProtocol.RentedMovie;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.FileManager;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.Task;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.User;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RequestRent implements Task {

    public static final String fail="ERROR request rent failed";
    public static final String succ="ACK rent";

    private String userName;
    private String movieName;
    private FileManager fileManager;

    public RequestRent(String userName, String movieName, FileManager fileManager) {
        this.userName = userName;
        this.movieName = movieName;
        this.fileManager = fileManager;
    }

    @Override
    public String run() {
        ConcurrentLinkedQueue<User> users = this.fileManager.acquireUserWritelock();
        ConcurrentLinkedQueue<Movie> movies =this.fileManager.acquireMovieWritelock();
        Movie movie=null;
        for (Movie m : movies){
            if(m.getName().compareTo(this.movieName)==0){
                movie=m;
                break;
            }
        }
        MovieUser user=null;
        for(User u : users){
            if(u.getUsername().compareTo(this.userName)==0) {
                user = (MovieUser) u;
                break;
            }
        }//user is not null because we checked it outside-in the protocol
        if(movie!=null && Integer.valueOf(movie.getAvailableAmount())>=1 && Integer.valueOf(user.getBalance())>=Integer.valueOf(movie.getPrice())){
            for (String bannedCOuntry : movie.getBannedCountries()){
                if(bannedCOuntry.compareTo(user.getCountry())==0){
                    fileManager.releaseUserWriteLock(false);
                    fileManager.releaseMovieWriteLock(false);
                    return fail;
                }
            }
            //check if user already have the movie
            for(RentedMovie m : user.getMovies()){
                if(m.getName().compareTo(this.movieName)==0){
                    fileManager.releaseUserWriteLock(false);
                    fileManager.releaseMovieWriteLock(false);
                    return fail;
                }
            }
            movie.setAvailableAmount(String.valueOf(Integer.valueOf(movie.getAvailableAmount())-1));
            user.getMovies().add(new RentedMovie(String.valueOf(movie.getId()),movie.getName()));
            user.setBalance(String.valueOf(Integer.valueOf(user.getBalance())-Integer.valueOf(movie.getPrice())));
            String available=movie.getAvailableAmount().substring(0);
            String price=movie.getPrice().substring(0);
            fileManager.releaseUserWriteLock(true);
            fileManager.releaseMovieWriteLock(true);

            return succ+" \""+this.movieName+"\" success"+"  BROADCAST movie \""+movieName+"\" "+available+" "+price;

        }
        fileManager.releaseUserWriteLock(false);
        fileManager.releaseMovieWriteLock(false);
        return fail;
    }
}
