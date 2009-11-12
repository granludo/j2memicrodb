/*
 * mobileForumWikiAPI.java
 *
 * Created on 15 de mayo de 2007, 20:43
 */

package edu.upc.BDAccessRemot.forumWikiAPI;

import edu.upc.BDAccessRemot.RemoteProxyConnection;
import edu.upc.BDAccessRemot.RemoteProxyListener;
import edu.upc.BDAccessRemot.http.HTTPProxyConnection;
import edu.upc.BDAccessRemot.j2meApi.MobileHTTPException;
import edu.upc.BDAccessRemot.j2meApi.MobileHTTPStatementResult;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 *
 * @author  Pelsman
 * @version
 */
public class mobileForumWikiAPI implements RemoteProxyListener{
    
    private static final int CLOSED=0;
    private static final int OPENING=1;
    private static final int OPEN=2;
    private static final int FORUM=3;
    private static final int WIKI=4;
    private static final int CLOSING=5;
    
    
    private static final String PROXY_SERVICE_NAME = "MobileBDServer/MobileBDServerWikiForum.php";
    
    private int state=CLOSED;
    private boolean lostConnectionError;
    private boolean opened;
    private boolean openError;
    private boolean closeError;
    private String sessionId=null;
    private MobileHTTPStatementResult forumResult = null;
    private MobileHTTPStatementResult wikiResult = null;
    
    private String user;
    private String pwd;
    
    private RemoteProxyConnection conn;
    
    public mobileForumWikiAPI(String ip, String user, String pwd){
        this.user = user;
        this.pwd = pwd;
    
        lostConnectionError = false;
        openError = false;
        closeError = false;
        opened = false;
        
        conn = new HTTPProxyConnection("http://"+ip+"/"+PROXY_SERVICE_NAME);
        conn.addListener(this);
    }
    
    public void open() throws MobileHTTPException{
        if(!lostConnectionError){
            if(state == mobileForumWikiAPI.CLOSED){
                
                openError = false;
                state=OPENING;
                
                String msg="action=open&";
                msg+="user="+this.user+"&";
                msg+="pwd="+this.pwd+"&";
                conn.sendMessage(msg);
            }
            else{
                MobileHTTPException ex = new MobileHTTPException(6);
                throw (ex);
            }
        }
        else{
            MobileHTTPException ex = new MobileHTTPException(-1);
            throw (ex);
        }
        
    }
        
    public boolean opened() throws MobileHTTPException{
        if(!lostConnectionError){
            if(state == mobileForumWikiAPI.OPEN || state == mobileForumWikiAPI.OPENING){
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
    
    
    public void requestForum() throws MobileHTTPException{
        if(!lostConnectionError){
            if(state == mobileForumWikiAPI.OPEN || state == mobileForumWikiAPI.WIKI){
                state=FORUM;

                forumResult = new MobileHTTPStatementResult("forum");
                

                String msg="action=forum&";
                msg+="id="+sessionId;
                conn.sendMessage(msg);
            }
            else{
                if(state == mobileForumWikiAPI.FORUM){
                    //no fa res
                }
                else{
                    MobileHTTPException ex = new MobileHTTPException(2);
                    throw (ex);   
                }
            }
        }
        else{
            MobileHTTPException ex = new MobileHTTPException(-1);
            throw (ex);
        }
    }
    
    public MobileHTTPStatementResult resultForum() throws MobileHTTPException{
        if(!lostConnectionError){
            if(state == mobileForumWikiAPI.OPEN || state == mobileForumWikiAPI.WIKI || state == mobileForumWikiAPI.FORUM){
                
                if(forumResult == null){
                    MobileHTTPException ex = new MobileHTTPException(6);
                    throw (ex);
                }
                else{
                    state = mobileForumWikiAPI.OPEN;
                    return forumResult;
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
    
    public void requestWiki() throws MobileHTTPException{
        if(!lostConnectionError){
            if(state == mobileForumWikiAPI.OPEN || state == mobileForumWikiAPI.FORUM){
                state=FORUM;

                forumResult = new MobileHTTPStatementResult("wiki");
                

                String msg="action=wiki&";
                msg+="id="+sessionId;
                conn.sendMessage(msg);
            }
            else{
                if(state == mobileForumWikiAPI.WIKI){
                    //no fa res
                }
                else{
                    MobileHTTPException ex = new MobileHTTPException(2);
                    throw (ex);   
                }
            }
        }
        else{
            MobileHTTPException ex = new MobileHTTPException(-1);
            throw (ex);
        }
    }
    
    public MobileHTTPStatementResult resultWiki() throws MobileHTTPException{
        if(!lostConnectionError){
            if(state == mobileForumWikiAPI.OPEN || state == mobileForumWikiAPI.WIKI || state == mobileForumWikiAPI.FORUM){
                
                if(forumResult == null){
                    MobileHTTPException ex = new MobileHTTPException(6);
                    throw (ex);
                }
                else{
                    state = mobileForumWikiAPI.OPEN;
                    return forumResult;
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
    
    public void close() throws MobileHTTPException{
        if(!lostConnectionError){
            if(state == mobileForumWikiAPI.OPEN){
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
    
    public boolean closed() throws MobileHTTPException{
        if(!lostConnectionError){
            if(state == mobileForumWikiAPI.CLOSED || state == mobileForumWikiAPI.CLOSING){
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
    
    public void reinit(){
        lostConnectionError = false;
        openError = false;
        closeError = false;
        opened = false;
        state = mobileForumWikiAPI.CLOSED;
    }
    
    
    public void performResponse(String resp, boolean exception) {
    
        if(!exception){
        
            if(resp.startsWith("open")){
                sessionId=resp.substring(5);
                opened = true;
                state = mobileForumWikiAPI.OPEN;
            }
            else if (resp.startsWith(("forum"))){
                /*StringBuffer ticket = new StringBuffer();
                int i = 7;
                while(i<resp.length()&&resp.charAt(i)!='~'){
                    ticket.append(resp.charAt(i));
                    i++;
                }*/
                    
                //forumResult = (MobileHTTPStatementResult) statementResults.get(ticket.toString());
                forumResult.setResult(resp,3);
                
                //state = mobileForumWikiAPI.OPEN;
            }
            else if (resp.startsWith("wiki")){
                /*StringBuffer ticket = new StringBuffer();
                int i = 6;
                while(i<resp.length()&&resp.charAt(i)!='~'){
                    ticket.append(resp.charAt(i));
                    i++;
                }*/
                                
                //MobileHTTPStatementResult result = (MobileHTTPStatementResult) statementResults.get(ticket.toString());
                wikiResult.setResult(resp,4);
                
                //state = mobileForumWikiAPI.OPEN;
            }
            else if (resp.startsWith("close")){
                opened = false;
                state = mobileForumWikiAPI.CLOSED;
            }
            else if(resp.startsWith("error")){
                String message = resp.substring(6);
                if(message.startsWith("open")){
                    openError = true;
                    state = mobileForumWikiAPI.CLOSED;
                }
                else if(message.startsWith("close")){
                    closeError = true;
                    state = mobileForumWikiAPI.OPEN;
                }
                else if(message.startsWith("forum")){
                    /*StringBuffer ticket = new StringBuffer();
                    int i = 7;
                    while(i<message.length()&&message.charAt(i)!='~'){
                        ticket.append(message.charAt(i));
                        i++;
                    }*/
                   
                    //MobileHTTPStatementResult result = (MobileHTTPStatementResult) statementResults.get(ticket.toString());
                    forumResult.setResult("Error a l'enviar la sentencia sql",5);
    
                    //state = MobileHTTPConnection.OPEN;
                }
                else if(message.startsWith("write")){
                    /*StringBuffer ticket = new StringBuffer();
                    int i = 6;
                    while(i<message.length()&&message.charAt(i)!='~'){
                        ticket.append(message.charAt(i));
                        i++;
                    }*/

                    //MobileHTTPStatementResult result = (MobileHTTPStatementResult) statementResults.get(ticket.toString());
                    wikiResult.setResult("Error a l'enviar la sentencia sql",5);

                    //state = MobileHTTPConnection.OPEN;
                }
                else if(message.startsWith("connection")){
                    lostConnectionError = true;
                    if(state == mobileForumWikiAPI.FORUM){
                        //String ticket = message.substring(11);
                        //MobileHTTPStatementResult result = (MobileHTTPStatementResult)statementResults.get(ticket);
                        forumResult.setResult("Error de connexió amb el servidor",5);
                    }
                    else if(state == mobileForumWikiAPI.WIKI){
                        wikiResult.setResult("Error de connexió amb el servidor",5);
                    }
                    state = mobileForumWikiAPI.CLOSED;
                }
            }
        }
        else{
            lostConnectionError = true;
            if(state == mobileForumWikiAPI.FORUM){
                //String ticket = message.substring(11);
                //MobileHTTPStatementResult result = (MobileHTTPStatementResult)statementResults.get(ticket);
                forumResult.setResult("Error de connexió amb el servidor",5);
            }
            else if(state == mobileForumWikiAPI.WIKI){
                wikiResult.setResult("Error de connexió amb el servidor",5);
            }
            state = mobileForumWikiAPI.CLOSED;
        }
    
    }
       
}
