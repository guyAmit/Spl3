//
// Created by ariepav@wincs.cs.bgu.ac.il on 12/31/17.
//

#include "../include/Task.h"


Task::Task(ConnectionHandler *connectionHandler,std::atomic<bool>* isConnected) :connectionHandler(connectionHandler),
                                                                                 isConnected(isConnected){
}

void Task::operator()() {

    std::string answer;
 //while not signing out...
 while (1) {
     //read message from server
     if (!(connectionHandler)->getLine(answer)) {
         std::cout << "Disconnected. Exiting...\n" << std::endl;
         connectionHandler->close();
         exit(0);
     }
     unsigned int len = answer.length();
     answer.resize(len - 1);
     if(answer=="ACK login succeeded")
         isConnected->store(true);
     if (answer == "ACK signout succeeded") {
         std::cout << answer<<std::endl;
         connectionHandler->close();
         std::cout << "Disconnected. Exiting...\n" << std::endl;
         break;

     }
     std::cout << answer<<std::endl;
     answer="";
 }

}
