package edu.upc.BDAccessRemot;


import java.io.IOException;
/**
 * Any class sending asynchronous responses from the HTTP Server, must implement this interface.
 */
public interface RemoteProxyConnection{

    /**
     * This method is used to add listeners who will be notified when a server's response arrives.
     * @param listener A listener who is going to be notified when a server's response arrives.
     */
	public void addListener(RemoteProxyListener listener);
	
    /**
     * Sends a message to the server.
     * @param msg The message that will be sent to the server.
     */
	public void sendMessage(String msg);


}
