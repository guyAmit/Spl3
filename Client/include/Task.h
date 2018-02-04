//
// Created by ariepav@wincs.cs.bgu.ac.il on 12/31/17.
//

#ifndef BOOST_ECHO_CLIENT_TASK_H
#define BOOST_ECHO_CLIENT_TASK_H


#include "connectionHandler.h"

class Task {
private:
    ConnectionHandler* connectionHandler;
    std::atomic<bool>* isConnected;
public:
    Task(ConnectionHandler* connectionHandler,std::atomic<bool>* isConnected);
    void operator()();
};


#endif //BOOST_ECHO_CLIENT_TASK_H
