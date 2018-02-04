package bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks;
import bgu.spl181.net.impl.MovieRentalProtocol.Movie;
import bgu.spl181.net.impl.MovieRentalProtocol.MovieUser;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.User;
import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileManager {
    private Gson gson;
    private UserManager usersManager=null;
    private MoviesManager moviesManager =null;
    private ReentrantReadWriteLock usersRWLock;
    private ReentrantReadWriteLock moviesRWLock;
    public static int MaxId=0;
    static boolean getMaxID=true;

    /**
     * <h1>FileManger constructor</h1>
     * initiate a new instance of the @FileManger class
     * and load the json files
     */
    public FileManager()
    {
        this.gson = new Gson();
        this.usersRWLock=new ReentrantReadWriteLock(true);
        this.moviesRWLock=new ReentrantReadWriteLock(true);
        this.loadUsers();
        this.loadMovies();

    }

    /**
     * <h1>loadUsers method</h1>
     * load the users json file
     */
    private void loadUsers()
    {
        try (JsonReader reader = new JsonReader(new FileReader("Database/Users.json")))
        {
            usersManager=gson.fromJson(reader,UserManager.class);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * <h1>loadMovies method</h1>
     * load the users json file
     */
    private void loadMovies()
    {
        try (JsonReader reader2 = new JsonReader(new FileReader("Database/Movies.json")))
        {
            moviesManager=gson.fromJson(reader2,MoviesManager.class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * <h1>acquireUserReadlock method</h1>
     * acquire the read lock for the {@link UserManager} user list
     * @return {@link UserManager} user list
     */
    public ConcurrentLinkedQueue<User> acquireUserReadlock(){
        this.usersRWLock.readLock().lock();
        return usersManager.getRealUsers();
    }

    /**
     * <h1>realeaseUserReadLock method</h1>
     * release the read lock for the {@link UserManager} user list
     */
    public void releaseUserReadLock() {
        this.usersRWLock.readLock().unlock();
    }

    /**
     * <h1>acquireUserWritelock method</h1>
     * acquire the write lock for the {@link UserManager} user list
     * @return {@link UserManager} user list
     */
    public ConcurrentLinkedQueue<User> acquireUserWritelock()
    {
        this.usersRWLock.writeLock().lock();
        return usersManager.getRealUsers();
    }

    /**
     * <h1>releaseUserWriteLock method</h1>
     * --save the changed list into the json file
     * --reloads the users list from the json
     * --release the write lock for the {@link UserManager} user list
     * @param changed-true if the the class should write the data base.
     */
    public void releaseUserWriteLock(boolean changed)
    {
        if(changed) {
            usersManager.saveDatabase();
            this.loadUsers();
        }
        this.usersRWLock.writeLock().unlock();
    }


    /**
     * <h1>acquireMoviesReadlock method</h1>
     * acquire the read lock for the {@link MoviesManager} movie list
     * @return the list of movies
     */
    public ConcurrentLinkedQueue<Movie> acquireMovieReadlock(){
        this.moviesRWLock.readLock().lock();
        return moviesManager.getRealMovies();
    }

    /**
     * <h1>releaseMovieReadLock method</h1>
     * release the read lock for the {@link MoviesManager} movie list
     */
    public void releaseMovieReadLock() {
        this.moviesRWLock.readLock().unlock();
    }

    /**
     * <h1>acquireMovieWritelock method</h1>
     * acquire the write lock for the {@link MoviesManager} user list
     * @return {@link MoviesManager} user list
     */
    public ConcurrentLinkedQueue<Movie> acquireMovieWritelock()
    {
        this.moviesRWLock.writeLock().lock();
        return moviesManager.getRealMovies();

    }

    /**
     * <h1>releaseMovieWriteLock method</h1>
     * --save the changed list into the json file
     * --reloads the movies list from the json
     * --release the write lock for the {@link MoviesManager} user list
     * @param changed-true if the the class should write the data base.
     */
    public void releaseMovieWriteLock(boolean changed)
    {
        if(changed) {
            moviesManager.saveDatabase();
            this.loadMovies();
        }
        this.moviesRWLock.writeLock().unlock();
    }



    public class UserManager
    {   @SerializedName("users")
        private ArrayList<JsonObject> JsonUsers;

        private Gson gsonUsers;
        private ConcurrentLinkedQueue<User> realUsers;

        public ConcurrentLinkedQueue<User> getRealUsers()
        {
            if(realUsers==null) {
                this.realUsers = new ConcurrentLinkedQueue<>();
                this.gsonUsers = new Gson();
                for (JsonObject userJson : JsonUsers) {
                    User user = this.gsonUsers.fromJson(userJson, MovieUser.class);
                    realUsers.add(user);
                }
            }
            return realUsers;
        }
        public void saveDatabase()
        {
            try(Writer writer=new FileWriter("Database/Users.json"))
            {
                Gson gsonBuilder=new GsonBuilder().create();
                users users = new users(realUsers);
                gsonBuilder.toJson(users,writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private class users{
            @SerializedName("users")
            ConcurrentLinkedQueue<User> users;

            public users(ConcurrentLinkedQueue<User> realUsers){
                this.users=realUsers;
            }
        }

    }




    public class MoviesManager
    {
        @SerializedName("movies")
        private ArrayList<JsonObject> jsonMovies;

        private ConcurrentLinkedQueue<Movie> realMovies;
        private Gson gsonMovies;

        public ConcurrentLinkedQueue<Movie> getRealMovies()
        {
            if (realMovies==null)
            {
                this.realMovies=new ConcurrentLinkedQueue<>();
                this.gsonMovies=new Gson();
                for(JsonObject movieJson : jsonMovies){
                    Movie movie = this.gsonMovies.fromJson(movieJson,Movie.class);
                    this.realMovies.add(movie);
                }
                if(FileManager.getMaxID){
                    for (Movie m : this.realMovies){
                        if(Integer.valueOf(m.getId())>FileManager.MaxId)
                            FileManager.MaxId=Integer.valueOf(m.getId());
                    }
                    FileManager.getMaxID=false;
                }
            }
            return realMovies;
        }
        public void saveDatabase()
        {
            try(Writer writer=new FileWriter("Database/Movies.json"))
            {
                Gson gsonBuilder=new GsonBuilder().create();
                movies movies = new movies(realMovies);
                gsonBuilder.toJson(movies,writer);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private class movies{
            @SerializedName("movies")
            ConcurrentLinkedQueue<Movie> movies;

            public movies(ConcurrentLinkedQueue<Movie> realMovies){
                this.movies=realMovies;
            }
        }
    }

}
