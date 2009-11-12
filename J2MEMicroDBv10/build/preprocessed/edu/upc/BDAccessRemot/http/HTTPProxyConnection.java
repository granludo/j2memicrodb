
package edu.upc.BDAccessRemot.http;

import edu.upc.BDAccessRemot.RemoteProxyConnection;
import edu.upc.BDAccessRemot.RemoteProxyListener;
import edu.upc.BDAccessRemot.URLEncoder;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;


/**
 * This class is a HTPP connection client. In order to create an asynchronous connection, the class implements the java.lang.Runnable interface. In order to manage the server responses, this class implements the RemoteProxyConnection interface.
 */
public class HTTPProxyConnection implements Runnable,RemoteProxyConnection{
		
    private String uri=null;
    private String msg=null;
    
    /**
     * Este vector contiene los multples Listeners
     */
    private Vector listeners=new Vector();
    /**
     * Construct an instance of HTTPProxyConnection associated with the specified Uniform Resource Identifier.
     * @param uri Uniform Resource Identifier for the connection.
     */
    public HTTPProxyConnection (String uri){		
            this.uri=uri;
    }
    /**
     * In order to manage the asynchronous response of the server, this method add listeners who will be notified when a response of the server has arrived.
     * @param listener A RemoteProxyListener who will be notified when a server response arrives.  
     */
    public void addListener(RemoteProxyListener listener) {
            listeners.addElement(listener);
    }
    /**
     * Configures the connection to the specified URI.
     * @return A configured HttpConnection to establish connection.
     * @throws IOException If a IOException occurs.
     */
    private HttpConnection openConnection() throws IOException{
            System.out.println("openConnection1!"+uri);
            System.out.println("message:"+msg);
            //HttpConnection conn = (HttpConnection)Connector.open(uri+msg);
            //conn.setRequestMethod(HttpConnection.GET);
            HttpConnection conn = (HttpConnection)Connector.open(uri,Connector.READ_WRITE);
            conn.setRequestMethod(HttpConnection.POST);
            conn.setRequestProperty("User-Agent","Profile/MIDP-1.0 Configuration/CLDC-1.1");
            conn.setRequestProperty("Content-Language", "es-ES");
            conn.setRequestProperty("Content-Type", "Application/x-www-form-urlencoded");
            OutputStream os = conn.openOutputStream();
            byte[] msgBytes = msg.getBytes();
            //int numBytes = msgBytes.length;
            /*for(int i=0;i<numBytes;i++){
                os.writeByte(msgBytes[i]);
            }*/
            os.write(msgBytes);
            //os.flush();
            os.close();
            return conn;
    }

    /**
     * This method starts the send process.
     * @param msg The message that is going to be sent.
     */
    public void sendMessage(String msg){// throws IOException{
        //Encoding español. Cambiar en caso de usar sanscrito
        //System.out.println("msg before = "+msg);
        /*try {
            this.msg=URLEncoder.encode(msg,"ISO-8859-1");
        }
        catch (Exception e) {
            this.msg=msg;
        }*/
        this.msg=msg;
        //System.out.println("msg after  = "+this.msg);
        Thread th=new Thread(this);
        th.start();		
    }

    /**
     * Creates the connection, sends the message and receives the response.
     */
    public void run(){
        HttpConnection conn=null;
        try{
            StringBuffer resp=new StringBuffer();
            conn=openConnection();
            InputStream is = conn.openDataInputStream();

            int chint=is.read();
            while (chint != -1) {
                resp.append((char)chint);
                chint=is.read();
            }

            is.close();
            conn.close();
            System.out.println("Response from Server:"+resp.toString().trim()+"#");
            
            if(resp.length()>0){
                for (int i=0;i<listeners.size();i++){
                    ((RemoteProxyListener)listeners.elementAt(i)).performResponse(resp.toString().trim(),false);
                }
            }
            

        }
        catch(Exception io){
            System.out.println("IOException sending the message:"+io);
            io.printStackTrace();
            for (int i=0;i<listeners.size();i++){
                        ((RemoteProxyListener)listeners.elementAt(i)).performResponse("error~connection~"+io.getMessage(),true);
            }
        }
        finally{
            conn=null;
        }
    }
	   

}
