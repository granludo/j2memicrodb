
package edu.upc.BDAccessRemot;


/**
 * Any class receiving asynchronous responses from the HTTP Server, must implement this interface.
 */
public interface RemoteProxyListener {

	/**
     * Any class receiving asynchronous responses from the HTTP Server, must implement this method.
     * @param response Response from the server.
     * @param exception True if the response is an exception, False otherwise.
     */
	public void performResponse(String response, boolean exception);
}
