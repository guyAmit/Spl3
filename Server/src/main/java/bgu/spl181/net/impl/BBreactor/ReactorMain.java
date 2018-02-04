package bgu.spl181.net.impl.BBreactor;

import bgu.spl181.net.api.MessageEncoderDecoder;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.FileManager;
import bgu.spl181.net.srv.*;
import bgu.spl181.net.api.LineMessageEncoderDecoder;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.UserServiceTextBasedProtocol;
import bgu.spl181.net.impl.MovieRentalProtocol.MovieRentalProtocol;
import org.omg.SendingContext.RunTime;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;


public class ReactorMain{

    static ConcurrentHashMap<String,Integer> loggedIn = new ConcurrentHashMap<>();
    static FileManager manager = new FileManager();

    public  static  void  main(String[] args){
        Server.reactor(Runtime.getRuntime().availableProcessors(), Integer.valueOf(args[0]), () -> new MovieRentalProtocol(loggedIn,manager),
                () -> new LineMessageEncoderDecoder()).serve();
    }

}
