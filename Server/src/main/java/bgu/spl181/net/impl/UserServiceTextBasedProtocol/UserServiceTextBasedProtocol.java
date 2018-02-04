package bgu.spl181.net.impl.UserServiceTextBasedProtocol;

import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.*;
import bgu.spl181.net.srv.ActorThreadPool;

import java.util.concurrent.ConcurrentHashMap;


public class UserServiceTextBasedProtocol implements BidiMessagingProtocol<String>{

    protected int connectionId;
    protected ConnectionsForBidi<String> connections;
    protected ConcurrentHashMap<String,Integer> loggedIn;
    protected boolean shouldTerminate = false;
    protected FileManager fileManager;

    public UserServiceTextBasedProtocol(ConcurrentHashMap<String,Integer> loggedIn, FileManager fileManager)
    {
        this.loggedIn=loggedIn;
        this.fileManager=fileManager;
    }

    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connectionId=connectionId;
        this.connections=(ConnectionsForBidi<String>) connections;
    }

    @Override
    public void process(String message) {
        String replay = parseMessage(message);
        connections.send(connectionId,replay);
        if(replay.compareTo(Signout.fail)==0)
            this.connections.disconnect(this.connectionId);
    }


    protected String parseMessage(String message)
    {
       String[] cuttedString =message.replace("\n","").split(" ");
       Task task=null;
       String reply=new String("");
       switch (cuttedString[0])
       {
           case"REQUEST":{
               break;
           }
           case"LOGIN":{
               task=new Login(cuttedString[1],cuttedString[2], loggedIn,connectionId, this.fileManager);
               reply=task.run();
               break;
           }
           case"REGISTER":{
               if(cuttedString.length>=4) {
                   task = new Register(cuttedString[1], cuttedString[2], cuttedString[3], this.fileManager,this.loggedIn,this.connectionId);
                   reply=task.run();
               }
               else if(cuttedString.length>=3) {
                   task = new Register(cuttedString[1], cuttedString[2], null, this.fileManager, this.loggedIn,this.connectionId);
                   reply = task.run();
               }
               else
                   reply=Register.fail;
               break;
           }
           case"SIGNOUT":{
               task=new Signout(this.loggedIn,this.connectionId);
               reply=task.run();
               break;
           }
       }

       return reply;
    }



    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
