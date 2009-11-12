
/*
 * MobileHttpConnection.java
 *
 * Created on 20 de febrero de 2007, 17:32
 */

package edu.upc.BDAccessRemot.j2meApi;

import edu.upc.BDAccessRemot.RemoteProxyConnection;
import edu.upc.BDAccessRemot.RemoteProxyListener;
import edu.upc.BDAccessRemot.http.HTTPProxyConnection;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * MobileHTTPException is an interface between a mobile device and a proxy server used to simplify the comunication between them.
 * This class is based on an asynchronous communication, it means that the mobile device calls methods in order to send information to the server and it doesn't have to wait for an immediate response. It can retrieve the response whenever it needs.
 * @author Néstor Giménez Segura
 * @version 1.0
 */
public class MobileHTTPConnection implements RemoteProxyListener{
    
    private static final String PROXY_SERVICE_NAME = "J2MEServer/servlets/servlet/BDServer";
    //private static final String PROXY_SERVICE_NAME = "MobileBDServer/MobileBDServer.php";
    
    private static final int CLOSED=0;
    private static final int OPENING=1;
    private static final int OPEN=2;
    private static final int SELECT=3;
    private static final int WRITE=4;
    private static final int CLOSING=5;
       
    private RemoteProxyConnection conn;
    private int state=CLOSED;
    
    private String ip=null;
    private String serviceName=null;
    private String bdd=null;
    private int sgbd;
    private String user=null;
    private String pwd="";
    private String sessionId=null;
    
    private boolean lostConnectionError;
    private boolean opened;
    private boolean openError;
    private boolean closeError;
    private MobileHTTPStatementResult availableDB = null;
    private Hashtable statementResults;
    //private Vector statementStrings;
    
    private int ticketCounter;
        
    /**
     * Constructs a new MobileHTTPConnection that creates a connection between the API and the specified ip/defaultServiceName. It also specifies the DataBase that is going to be opened using the given username and password.
     * @param sgbd The number of database management system that contains the specified database.
     * @param db The DataBase that is going to be opened.
     * @param user The username used to open the specified DataBase.
     * @param pwd The password used to open the specified DataBase.
     * @param ip The ip that MobileHTTPConnection should connect to.
     */
    public MobileHTTPConnection(String ip, String db, int sgbd, String user, String pwd){		
        this(ip, PROXY_SERVICE_NAME, db, sgbd, user, pwd);
    }
        
    /**
     * Constructs a new MobileHTTPConnection that creates a connection between the API and the specific ip/serviceName. It also specifies the DataBase that is going to be opened using the given username and password.
     * @param sgbd The number of database management system that contains the specified database.
     * @param db The DataBase that is going to be opened.
     * @param user The username used to open the specified DataBase.
     * @param pwd The password used to open the specified DataBase.
     * @param ip The ip that MobileHTTPConnection should connect to
     * @param serviceName The service name that MobileHTTPConnection should connect to.
     */
    public MobileHTTPConnection(String ip, String serviceName, String db, int sgbd, String user, String pwd){
        ticketCounter = 0;
        this.bdd=db;
        this.sgbd=sgbd;
        this.user=user;
        this.pwd=pwd;
        this.ip = ip;
        this.serviceName = serviceName;
        lostConnectionError = false;
        openError = false;
        closeError = false;
        opened = false;
        statementResults = new Hashtable();
        conn = new HTTPProxyConnection("http://"+ip+"/"+serviceName);
        conn.addListener(this);
    }
    
    /**
     * Reinitializes the MobileHTTPConnection.
     */
    public void DBReinit(){
        lostConnectionError = false;
        openError = false;
        closeError = false;
        opened = false;
        state = MobileHTTPConnection.CLOSED;
    }
    
    /**
     * Sends a connection request to the server, in order to open the DataBase specified in the constructor.
     * @throws edu.upc.BDAccessRemot.j2meApi.MobileHTTPException If the specified Database is already open or if the connection has been lost.
     */
    public void DBConnectionRequest() throws MobileHTTPException{
        if(!lostConnectionError){
            if(state == MobileHTTPConnection.CLOSED){
                
                openError = false;
                state=OPENING;

                String msg="action=open&";
                msg+="bdd="+bdd+"&";
                msg+="user="+user+"&";
                msg+="pwd="+pwd+"&";
                msg+="sgbd="+sgbd;
                conn.sendMessage(msg);
            }
            else{
                //No s'ha pogut obrir la base de dades
                MobileHTTPException ex = new MobileHTTPException(1);
                throw (ex);
            }
        }
        else{
            MobileHTTPException ex = new MobileHTTPException(-1);
            throw (ex);
        }
        
    }
    
    /**
     * Tests whether the database is opened or not.
     * @return true if the database has been succesfully opened, false otherwise.
     * @throws edu.upc.BDAccessRemot.j2meApi.MobileHTTPException If DBConnectionRequest method hasn't been called, if problems have been found by the server while opening the DataBase or if the connection has been lost.
     */
    public boolean DBOpened() throws MobileHTTPException{
        if(!lostConnectionError){
            if(state == MobileHTTPConnection.OPEN || state == MobileHTTPConnection.OPENING){
                if(!openError){
                    return opened;
                }
                else{
                    MobileHTTPException ex = new MobileHTTPException(1);
                    throw (ex);
                }
            }
            else{
                MobileHTTPException ex = new MobileHTTPException(1);
                throw (ex);
            }
        }
        else{
            MobileHTTPException ex = new MobileHTTPException(-1);
            throw (ex);
        }
    }
    
    /**
     * Sends a query or update statement to the server.
     * @param statement SQL sentence that is going to be sent to the server.
     * @return A ticket or code that is needed in order to request the result of the statment sent.
     * @throws edu.upc.BDAccessRemot.j2meApi.MobileHTTPException If the DataBase hasn't been opened or if the connection has been lost.
     */
    synchronized public int DBStatement(String statement) throws MobileHTTPException{
        if(!lostConnectionError){
            if(state == MobileHTTPConnection.OPEN || state == MobileHTTPConnection.SELECT || state == MobileHTTPConnection.WRITE){
                if(statement.toLowerCase().startsWith("select")){
                    state=SELECT;
                    
                    MobileHTTPStatementResult res = new MobileHTTPStatementResult(statement);
                    statementResults.put(Integer.toString(ticketCounter),res);
                    
                    int ticket = ticketCounter;
                    ticketCounter++;
                    String msg="action=select&";
                    msg+="id="+sessionId+"&";
                    msg+="sql="+statement;
                    msg+="&ticket="+ticket;
                    conn.sendMessage(msg);


                    return ticket;
                }
                else{
                    state=WRITE;
                    
                    MobileHTTPStatementResult res = new MobileHTTPStatementResult(statement);
                    statementResults.put(Integer.toString(ticketCounter),res);
                    
                    
                    int ticket = ticketCounter;
                    ticketCounter++;

                    String msg="action=write&";
                    msg+="id="+sessionId+"&";
                    msg+="sql="+statement;
                    msg+="&ticket="+ticket;                        
                    conn.sendMessage(msg);
                    return ticket;
                }
            }
            else{
                MobileHTTPException ex = new MobileHTTPException(2);
                throw (ex);
            }
        }
        else{
            MobileHTTPException ex = new MobileHTTPException(-1);
            throw (ex);
        }
        
    }
    
    /**
     * Gets the result produced by the requested SQL statement.
     * If the statement is a select, it also can create a J2METable called SDLIB_temp_table that contains a materialized view of the result.
     * @param ticket Code needed to get the result related to this code.
     * @return A MobileHTTPStatementResult instance containing the requested result.
     * @throws edu.upc.BDAccessRemot.j2meApi.MobileHTTPException If the DataBase hasn't been opened or if the connection has been lost.
     */
    public MobileHTTPStatementResult DBResultRequest(int ticket) throws MobileHTTPException{
        if(!lostConnectionError){
            if(state == MobileHTTPConnection.OPEN || state == MobileHTTPConnection.SELECT || state == MobileHTTPConnection.WRITE){
                
                MobileHTTPStatementResult res = (MobileHTTPStatementResult)statementResults.get(Integer.toString(ticket));
                
                if(res.getStatus() != MobileHTTPStatementResult.WAITING){
                    statementResults.remove(Integer.toString(ticket));
                }
                
                if(res == null){
                    MobileHTTPException ex = new MobileHTTPException(6);
                    throw (ex);
                }
                else{
                    return res;
                }                
            }
            else{
                MobileHTTPException ex = new MobileHTTPException(2);
                throw (ex);
            }
        }
        else{
            MobileHTTPException ex = new MobileHTTPException(-1);
            throw (ex);
        }        
    }
    
    /**
     * Sends a request to the server in order to close the DataBase specified in the constructor.
     * @throws edu.upc.BDAccessRemot.j2meApi.MobileHTTPException If the DataBase hasn't been opened or if the connection has been lost.
     */
    public void DBClose() throws MobileHTTPException{
        if(!lostConnectionError){
            if(state == MobileHTTPConnection.OPEN){
                state=CLOSING;

                closeError = false;

                String msg="action=close&";
                msg+="id="+sessionId;
                conn.sendMessage(msg);
            }
            else{
                MobileHTTPException ex = new MobileHTTPException(3);
                throw (ex);
            }
        }
        else{
            MobileHTTPException ex = new MobileHTTPException(-1);
            throw (ex);
        }
    }
    
    /**
     * Tests whether the Database is closed or not.
     * @return True if the DataBase is closed, False otherwise.
     * @throws edu.upc.BDAccessRemot.j2meApi.MobileHTTPException If DBClose method hasn't been called, if problems have been found by the server while closing the DataBase or if the connection has been lost.
     */
    public boolean DBClosed() throws MobileHTTPException{
        if(!lostConnectionError){
            if(state == MobileHTTPConnection.CLOSED || state == MobileHTTPConnection.CLOSING){
                if(!closeError){
                    return !opened;
                }
                else{
                    MobileHTTPException ex = new MobileHTTPException(3);
                    throw (ex);
                }
            }
            else{
                MobileHTTPException ex = new MobileHTTPException(3);
                throw (ex);
            }
        }
        else{
            MobileHTTPException ex = new MobileHTTPException(-1);
            throw (ex);
        }
    }
        
    /**
     * Redefined Method (from RemoteProxyListener) that is used to manage the asynchronous responses sent by the server.
     * @param resp The server's response
     * @param exception If the response has been an exception
     */
    public void performResponse(String resp, boolean exception){
        
        if(!exception){
        
            if(resp.startsWith("open")){
                sessionId=resp.substring(5);
                opened = true;
                state = MobileHTTPConnection.OPEN;
            }
            else if (resp.startsWith(("select"))){
                StringBuffer ticket = new StringBuffer();
                int i = 7;
                while(i<resp.length()&&resp.charAt(i)!='~'){
                    ticket.append(resp.charAt(i));
                    i++;
                }
                    
                MobileHTTPStatementResult result = (MobileHTTPStatementResult) statementResults.get(ticket.toString());
                result.setResult(resp,3);
                
                state = MobileHTTPConnection.OPEN;
            }
            else if (resp.startsWith("write")){
                StringBuffer ticket = new StringBuffer();
                int i = 6;
                while(i<resp.length()&&resp.charAt(i)!='~'){
                    ticket.append(resp.charAt(i));
                    i++;
                }
                                
                MobileHTTPStatementResult result = (MobileHTTPStatementResult) statementResults.get(ticket.toString());
                result.setResult(resp,4);
                
                state = MobileHTTPConnection.OPEN;
            }
            else if (resp.startsWith("close")){
                opened = false;
                state = MobileHTTPConnection.CLOSED;
            }
            else if(resp.startsWith("error")){
                String message = resp.substring(6);
                if(message.startsWith("open")){
                    openError = true;
                    state = MobileHTTPConnection.CLOSED;
                }
                else if(message.startsWith("close")){
                    closeError = true;
                    state = MobileHTTPConnection.OPEN;
                }
                else if(message.startsWith("dbavailable")){
                    availableDB.setResult("Error a l'obtenir les bases de dades disponibles",5);
                    state = MobileHTTPConnection.CLOSED;
                }
                else if(message.startsWith("select")){
                    StringBuffer ticket = new StringBuffer();
                    int i = 7;
                    while(i<message.length()&&message.charAt(i)!='~'){
                        ticket.append(message.charAt(i));
                        i++;
                    }
                   
                    MobileHTTPStatementResult result = (MobileHTTPStatementResult) statementResults.get(ticket.toString());
                    result.setResult("Error a l'enviar la sentencia sql",5);
    
                    state = MobileHTTPConnection.OPEN;
                }
                else if(message.startsWith("write")){
                    StringBuffer ticket = new StringBuffer();
                    int i = 6;
                    while(i<message.length()&&message.charAt(i)!='~'){
                        ticket.append(message.charAt(i));
                        i++;
                    }

                    MobileHTTPStatementResult result = (MobileHTTPStatementResult) statementResults.get(ticket.toString());
                    result.setResult("Error a l'enviar la sentencia sql",5);

                    state = MobileHTTPConnection.OPEN;
                }
                else if(message.startsWith("connection")){
                    lostConnectionError = true;
                    if(state == MobileHTTPConnection.SELECT || state == MobileHTTPConnection.WRITE){
                        String ticket = message.substring(11);
                        MobileHTTPStatementResult result = (MobileHTTPStatementResult)statementResults.get(ticket);
                        result.setResult("Error de connexió amb el servidor",5);
                    }
                }
            }
        }
        else{
            lostConnectionError = true;
            if(state == MobileHTTPConnection.SELECT || state == MobileHTTPConnection.WRITE){
                MobileHTTPStatementResult result = (MobileHTTPStatementResult)statementResults.get(Integer.toString(ticketCounter-1));
                result.setResult("Error de connexió amb el servidor",5);
            }
            state = MobileHTTPConnection.CLOSED;
        }
    }
       
    private String stateToString(int i){
        switch(i){
        case 0: return "CLOSED";				
        case 1: return "OPENING";
        case 2: return "OPEN";
        case 3: return "SELECT";
        case 4: return "WRITE";
        case 5: return "CLOSING";
        }
        return "ERROR";

    }
   
}
