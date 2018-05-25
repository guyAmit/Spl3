# SPL task3 - Client and reactor based server

homework 3 in the spl course at ben Gurion university. in this project
we will build:
  1) C++ client genral client
  2) Java, reactor based server to handle movie renting requsts  
  3) Java, thread per client server to handle movie renting requsts 

the purpose of this project is to build a fully functinal sever that will act as
an movie renting servies. 

## Getting Started

for running the client use
```
make
```
and then use
```
bin/BBclient <ip> <port>
```
for running reactor based server use:
```
mvn exec:java -Dexec.mainClass=”bgu.spl181.net.impl.BBreactor.ReactorMain” -Dexec.args=”<port>”
```
for running thread per client server use:
```
mvn exec:java -Dexec.mainClass=”bgu.spl181.net.impl.BBtpc.TPCMain” -Dexec.args=”<port>”
```

## Supported ops
* **LOGIN <username> <password** - Login into the store
* **REGISTER <username> <password> <country** - register the new user into the system
* **REQUEST info** - get information about all the movies
* **REQUEST info <”movie name”>**-get information about the movie with <"movie name">
* **REQUEST changeprice <”movie name”> <price>** - admin op - change the price of a movie
* **REQUEST removie <”movie name”>** - admin op - remove the movie with <"movie name">
* **REQUEST addmovie <”movie name”> <amount> <price> [“banned country”,...]** -admin op - add the movie <"movie name"> with amount <"amount"> and with price <"price"> wthich is bannded in the counteries: [“banned country”,...]
* **REQUEST return <”movie name”>** - client op - a client return the movie <"movie name"> to the store
* **REQUEST rent <”movie name”>** - client op - a client rent the movie <"movie name"> from the store if avaialble
* **REQUEST balance add <amount>** - client op - a client charge <"amount"> money to his acount
* **REQUEST balance info** - client op - checks how mush money he has chrged into the store
## Prerequisites

## Versioning

current version : 1.0.0

## Authors

* **Guy Amit** - [guyAmit](https://github.com/guyAmit)
* **Arie pavlov** 

## License
M.I.T License in separate file
