package bgu.spl181.net.impl.BBtpc;

import bgu.spl181.net.api.LineMessageEncoderDecoder;
import bgu.spl181.net.impl.MovieRentalProtocol.MovieRentalProtocol;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.FileManager;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.UserServiceTextBasedProtocol;
import bgu.spl181.net.srv.Server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class TPCMain  {


    static ConcurrentHashMap<String,Integer> loggedIn = new ConcurrentHashMap<>();
    static FileManager manager = new FileManager();

    public static void main(String[] args){

        Server.threadPerClient(Integer.valueOf(args[0]),()->new MovieRentalProtocol(loggedIn,manager),
                () -> new LineMessageEncoderDecoder()).serve();
    }

}
