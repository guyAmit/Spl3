package bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks;

import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Connections;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Signout implements Task {

    public static final String fail="ERROR signout failed";
    public static final String succ="ACK signout succeeded";
    private ConcurrentHashMap<String,Integer> loggedIn;
    private int connectionId;
    private Connections connections;

    public Signout(ConcurrentHashMap<String,Integer> loggedIn, int connectionId) {
        this.connectionId=connectionId;
        this.loggedIn=loggedIn;
    }

    @Override
    public String run() {
      if(loggedIn.containsValue(connectionId))
      {
          for(Map.Entry<String,Integer> toFind:loggedIn.entrySet() )
              if(toFind.getValue().intValue()==connectionId)
                  loggedIn.remove(toFind.getKey());

          return succ;
      }
      return fail;
    }
}
