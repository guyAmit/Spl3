#include <stdlib.h>
#include <iostream>
#include "../include/connectionHandler.h"
#include "../include/Task.h"
#include <boost/thread.hpp>


class thread;

class thread;

int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    std::atomic<bool> isConnected(false);

    Task listen(&connectionHandler,&isConnected);
    boost::thread listen_thread{listen};

	//From here we will see the rest of the ehco client implementation:
    while (1) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        if (!connectionHandler.sendLine(line)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            connectionHandler.close();
            break;
        }
        if(isConnected.load() && line=="SIGNOUT") {
            break;
        }
    }
    listen_thread.join();
    return 0;
}
