package bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks;

public interface Task  {
    /**
     *<h1>Task</h1>
     * this interface will be the base for all the commands
     * that the user will transmit. the constructor of the implementing classes will
     * receive various parameters depend on what the user requested.
     * @return the replay for the user
     */
    public String run();
}
