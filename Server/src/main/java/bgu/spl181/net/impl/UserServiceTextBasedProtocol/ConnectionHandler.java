/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl181.net.impl.UserServiceTextBasedProtocol;

import java.io.Closeable;

/**
 *
 * @author bennyl
 */
public interface ConnectionHandler<T> extends Closeable{

    void send(T msg) ;

}