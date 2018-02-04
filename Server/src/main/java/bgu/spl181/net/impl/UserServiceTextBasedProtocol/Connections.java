package bgu.spl181.net.impl.UserServiceTextBasedProtocol;

public interface Connections<T> {

    boolean send(int connectionId, T msg);

    void broadcast(T msg);

    void disconnect(int connectionId);
}
