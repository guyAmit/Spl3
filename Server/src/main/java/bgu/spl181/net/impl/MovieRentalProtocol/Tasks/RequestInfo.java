package bgu.spl181.net.impl.MovieRentalProtocol.Tasks;

import bgu.spl181.net.impl.MovieRentalProtocol.Movie;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.FileManager;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RequestInfo implements Task {

    public static final String succ="ACK info ";
    public static final String fail="ERROR request info failed";

    private FileManager fileManager;
    private String movieName;

    public RequestInfo(String movieName, FileManager fileManager) {
        this.movieName=movieName;
        this.fileManager=fileManager;
    }


    public RequestInfo(FileManager fileManager) {
        this.fileManager=fileManager;
        this.movieName=null;

    }
    @Override
    public String run() {
        if(this.movieName==null){
            ConcurrentLinkedQueue<Movie> movies =this.fileManager.acquireMovieReadlock();
            String info = succ;
            for (Movie movie : movies){
                info+="\""+movie.getName()+"\" ";
            }
            info=info.substring(0,info.length()-1);
            this.fileManager.releaseMovieReadLock();
            return info;
        }
        else{
            ConcurrentLinkedQueue<Movie> movies =this.fileManager.acquireMovieReadlock();
            String info=succ;
            for (Movie movie : movies){
                if(movie.getName().compareTo(this.movieName)==0){
                    info+="\""+this.movieName+"\" "+movie.getAvailableAmount()+" "+movie.getPrice();
                    for(String country : movie.getBannedCountries()){
                        info+=" \""+country+"\"";
                    }
                    fileManager.releaseMovieReadLock();
                    return info;
                }
            }
            fileManager.releaseMovieReadLock();
            return fail;
        }
    }
}
