package bgu.spl181.net.impl.MovieRentalProtocol;

import bgu.spl181.net.impl.MovieRentalProtocol.Tasks.*;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.Tasks.*;
import bgu.spl181.net.impl.UserServiceTextBasedProtocol.UserServiceTextBasedProtocol;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieRentalProtocol extends UserServiceTextBasedProtocol {


    public MovieRentalProtocol( ConcurrentHashMap<String, Integer> loggedIn,FileManager fileManager) {
        super(loggedIn,fileManager);
    }


    /**
     * <h1>process</h1>
     * this method calls the parseMessage method and get the result of the user
     * REQUEST and send back the replay, if there is a need to BROADCAST part of the message it will do so.
     * @param message
     */
    @Override
    public void process(String message) {
        String replay = parseMessage(message);
        String[] replayAndBROADCAST = replay.split("  ");
        connections.send(connectionId,replayAndBROADCAST[0]);
        if(replayAndBROADCAST.length>1){
            for(Map.Entry<String,Integer> entry:loggedIn.entrySet())
                connections.send(entry.getValue(),replayAndBROADCAST[1]);
        }
        if(replay.compareTo(Signout.fail)==0)
            this.connections.disconnect(this.connectionId);
    }

    /**
     * <h1>parseMessage</h1>
     * simple method for opreating the client commands. this method will decipher whitch command
     * the user is trying to preform and create a {@link Task} class to handle it.
     * @param message
     * @return
     */
    @Override
    protected String parseMessage(String message){
        ArrayList<String> analyzedString = this.analyzeString(message.replace("\n",""));
        Task task=null;
        String reply=new String("");
        switch (analyzedString.get(0))
        {
            case"SIGNOUT":{
                reply=super.parseMessage(message);
                break;
            }
            case"LOGIN":{
                reply=super.parseMessage(message);
                break;
            }
            case"REGISTER":{
                if(analyzedString.size()>=4) {
                    String county="";
                    for (int i=3;i<analyzedString.size();i++){
                        county+=analyzedString.get(i)+" ";
                    }
                    county=county.substring(0,county.length()-1);
                    task = new MovieRegister(analyzedString.get(1), analyzedString.get(2),county, this.fileManager,this.loggedIn,this.connectionId);
                    reply=task.run();
                }
                else
                    reply=Register.fail;
                break;
            }
            case"REQUEST":{

                    switch (analyzedString.get(1)) {
                        case "balance"://balance info

                            if (analyzedString.get(2).compareTo("info") == 0) {
                                if(!this.loggedIn.containsValue(this.connectionId))
                               {
                                   return RequestBalanceInfo.fail;
                               }
                                task = new RequestBalanceInfo(getUserFromLoggedIn(),fileManager);
                                reply = task.run();
                            } else {//add  balance
                                if(!this.loggedIn.containsValue(this.connectionId))
                                {
                                    return RequestAddBalance.fail;
                                }
                                task = new RequestAddBalance(getUserFromLoggedIn(),analyzedString.get(3),fileManager);
                                reply = task.run();
                            }
                            break;
                        case "info":
                            if(!this.loggedIn.containsValue(this.connectionId))
                            {
                                return RequestInfo.fail;
                            }
                            if(analyzedString.size()==2)
                            {
                                task=new RequestInfo(fileManager);
                                reply=task.run();
                            }
                            else if (analyzedString.size()==3)
                            {
                                task=new RequestInfo(analyzedString.get(2),fileManager);
                                reply=task.run();
                            }
                            break;
                        case "rent":
                            if(!this.loggedIn.containsValue(this.connectionId))
                            {
                                return RequestRent.fail;
                            }
                            if(analyzedString.size()==3) {
                                task = new RequestRent(getUserFromLoggedIn(), analyzedString.get(2), fileManager);
                                reply = task.run();
                            }
                            break;
                        case "return":
                            if(!this.loggedIn.containsValue(this.connectionId))
                            {
                                return RequestReturn.fail;
                            }
                            if(analyzedString.size()==3) {
                                task = new RequestReturn(getUserFromLoggedIn(),analyzedString.get(2),fileManager);
                                reply = task.run();
                            }
                            break;
                        case "addmovie":
                            if(!this.loggedIn.containsValue(this.connectionId))
                            {
                                return RequestAddMovie.fail;
                            }
                            if(analyzedString.size()>=3){
                                ArrayList<String> countries = new ArrayList<>();
                                if(analyzedString.size()>=5){
                                    for (int i=5;i<analyzedString.size();i++)
                                        countries.add(analyzedString.get(i));
                                }
                                task = new RequestAddMovie(fileManager,getUserFromLoggedIn(),analyzedString.get(2),Integer.parseInt(analyzedString.get(3)),
                                        Integer.parseInt(analyzedString.get(4)),countries);
                                reply=task.run();
                            }
                            break;
                        case "remmovie":
                            if(!this.loggedIn.containsValue(this.connectionId))
                            {
                                return RequestReMovie.fail;
                            }
                            if(analyzedString.size()==3) {
                                task = new RequestReMovie(getUserFromLoggedIn(),analyzedString.get(2),fileManager);
                                reply = task.run();
                            }
                        case "changeprice":
                            if(!this.loggedIn.containsValue(this.connectionId))
                            {
                                return RequestChangePrice.fail;
                            }
                          if(analyzedString.size()==4){
                              task = new RequestChangePrice(fileManager,getUserFromLoggedIn(),analyzedString.get(2),
                                      Integer.parseInt(analyzedString.get(3)));
                              reply = task.run();
                          }
                    }
                }

            }

        return reply;
    }

    /**
     * <h1>getUserFromLoggedIn</h1>
     * simple method to get the userName of a connected user by his ID.
     * @return the userName
     */
    private String getUserFromLoggedIn(){
        for(Map.Entry<String,Integer> toFind:loggedIn.entrySet() )
            if(toFind.getValue().intValue()==this.connectionId)
                return toFind.getKey();
        return null;
    }

    /**
     * <h1>analyzeString</h1>
     * this method will analyze the messge from the client according to relevant parts.
     * parts between " " will stay together. each other word will get her own space at the array list.
     * this method will be used for {DparseMessage} method.
     * @param msg
     * @return arraylist of strings representing the command
     */
    private ArrayList<String> analyzeString(String msg){
        String regex = "\"[^\"]+\"|[^\\s]+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(msg);
        ArrayList<String> command = new ArrayList<>();
        int counter=0;
        while(m.find()){
            String next = m.group(0);
            if(next.compareTo("")!=0){
                if(counter>0 && command.get(0).compareTo("LOGIN")!=0 & command.get(0).compareTo("REGISTER")!=0 )
                    command.add(next.replaceAll("\"", ""));
                else
                    command.add(next);
                counter++;
            }
        }
        return command;
    }


}
