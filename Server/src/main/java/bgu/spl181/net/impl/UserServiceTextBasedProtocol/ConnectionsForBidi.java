package bgu.spl181.net.impl.UserServiceTextBasedProtocol;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsForBidi<T> implements Connections<T>{

    private ConcurrentHashMap<Integer,ConnectionHandler> connections;
    private int idCounter;

    public ConnectionsForBidi()
    {
        this.connections=new ConcurrentHashMap<>();
        this.idCounter=0;
    }

    public Integer addConnection (ConnectionHandler connectionHandler)
    {
        this.idCounter++;
        this.connections.putIfAbsent(idCounter,connectionHandler);
        return this.idCounter;

    }

    @Override
    public boolean send(int connectionId, T msg) {
        try {
            connections.get(connectionId).send(msg);
            return true;
        }catch (Exception e)
        {
            return false;
        }

    }

    @Override
    public void broadcast(T msg) {
        for (Integer clientId:connections.keySet())
            send(clientId,msg);
    }

    @Override
    public void disconnect(int connectionId) {

        ConnectionHandler handler=connections.remove(connectionId);
        try{
            handler.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
