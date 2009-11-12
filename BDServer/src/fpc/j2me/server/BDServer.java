/**
* Copyright 2007 Universitat Politécnica de Catalunya
*
* This program is free software; you can redistribute it and/or modify
it under the terms
* of the GNU General Public License as published by the Free Software
Foundation; either
* version 2 of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.
* See the GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
along with this program;
* if not, write to the Free Software Foundation, Inc., 51 Franklin St,
Fifth Floor, Boston, MA
* 02110-1301 USA. It is also //currently //available at
http://www.gnu.org/licenses/gpl.txt
*
* Authors:  Néstor Giménez Segura, Marc Alier, Maria José Casany UPC
*
* Contact: http://morfeo.upc.edu/crom malier@lsi.upc.edu, and
* info@upc.edu / Jordi Girona Salgado, 1-3 E-08034 Barcelona SPAIN
*/

package fpc.j2me.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servidor de datos mediante HTTP
 *
 */
public class BDServer extends HttpServlet implements ServerInterface {
    
    /*public static final int CLOSED=0;
    public static final int OPENING=1;
    public static final int OPEN=2;
    public static final int SELECT=3;
    public static final int WRITE=4;
    public static final int CLOSING=5;*/

    //Parser parser=new Parser();
    Receiver receiver=new Receiver(this);
    Map bdmap=null;
    HashMap timers = new HashMap();

    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
            this.doPost(arg0, arg1);
    }
	
/**
 * Recibe un httpservlet con un action que provocara que haga open, select, insert, close, etc.
 */
    protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
            PrintWriter out=arg1.getWriter();

            bdmap=(Map)getServletContext().getAttribute("idmap");
            if (bdmap==null){
                    bdmap=new HashMap();
                    getServletContext().setAttribute("idmap",bdmap);
            }
            
            BufferedReader br = arg0.getReader();
            String buf = br.readLine();
            Map paramsMap = getParams(buf);
            
            
            
            out.print(receiver.receive(paramsMap,out));
    }
    
    private Map getParams(String buffer){
        Map paramsMap = new HashMap();
        
        StringBuffer cmd = new StringBuffer();
        StringBuffer value = new StringBuffer();
        int i = 0;
        
        while(i<buffer.length()){
            while(buffer.charAt(i)!='='){
                cmd.append(buffer.charAt(i));
                i++;
            }
            i++;
            while(i<buffer.length() && buffer.charAt(i)!='&'){
                if(buffer.charAt(i)!='+'){
                    value.append(buffer.charAt(i));
                }
                else{
                    value.append(' ');
                }
                i++;
            }
            i++;
            paramsMap.put(cmd.toString(),value.toString());
            
            cmd.delete(0,cmd.capacity()); 
            value.delete(0,value.capacity()); 
        }
        return paramsMap;
    }

    protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        super.service(arg0,arg1);
    }

    public BDConn getBDConn(String id) {
        if(timers.containsKey(id)){
            ((BDServerTimer)timers.get(id)).restart();
        }
        return (BDConn)bdmap.get(id);
    }

    public void removeBDConn(String id) {
        if(timers.containsKey(id)){
            timers.remove(id);
        }
        bdmap.remove(id);					
    }

    public void saveConnection(String id, BDConn conn) {
        
        BDServerTimer t = new BDServerTimer(this, id);
        timers.put(id,t);
        
        bdmap.put(id,conn);
        t.start();
    }

    public void deleteId(String id){
        bdmap.remove(id);
        timers.remove(id);
    }

}
