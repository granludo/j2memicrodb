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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;

public class Receiver {

	Parser parser=new Parser();
	ServerInterface si;
	
	public Receiver(ServerInterface si){
		this.si=si;
	}

	public String receive(Map arg0,PrintWriter out){

            String cmd=(String)arg0.get("action");
            System.out.println("action "+cmd);

            //try{

            if (cmd==null){
                return "error~Debes mandar un parametro action: http://127.0.0.1:8080/J2MEServer/servlets/servlet/BDServer?=hello";
            } 
            else{
                if (cmd.equalsIgnoreCase("hello")){
                    return "hello world";
                }
                else if (cmd.equalsIgnoreCase("open")){
                    try{
                        String bdd=(String)arg0.get("bdd");
                        String sgbd=(String)arg0.get("sgbd");
                        String user=(String)arg0.get("user");
                        String pwd=(String)arg0.get("pwd");
                        BDConn dbavailable=new BDConn("dbavailable","com.mysql.jdbc.Driver","jdbc:mysql://localhost:3306","nestor","nestor");
                        Statement st= dbavailable.getConn().createStatement();
                        ResultSet res=st.executeQuery("select driver,url from db where nombd=\""+bdd+"\" and sgbd=\""+sgbd+"\";");
                        
                        String driver = "";
                        String url = "";
                        res.beforeFirst();
                        while(res.next()){
                            driver = res.getString("driver");
                            url = res.getString("url");
                        }
                        res.close();
                        st.close();
                        dbavailable.getConn().close();
                        
                        if(driver.equals("")||url.equals("")){
                            return "error~open~La base de dades no existeix o no esta disponible";
                        }
                        else{
                            BDConn bdconn=new BDConn(bdd,driver,url,user,pwd);
                            Date date=new Date();
                            String id="ID"+date.getTime();
                            si.saveConnection(id,bdconn);
                            //out.println("id = "+id);
                            return "open~"+id;    
                        }

                    }
                    catch(Exception e){
                        return "error~open~"+e.getMessage();
                    }

                }
                else if (cmd.equalsIgnoreCase("close")){
                    try{
                        String id=(String)arg0.get("id");
                        BDConn conn=si.getBDConn(id);
                        if(conn==null){
                            return "error~connection";
                        }
                        else{
                            conn.getConn().close();
                            si.removeBDConn(id);
                            return "close~0";
                        }

                    }
                    catch(Exception e){
                        return "error~close~"+e.getMessage();
                    }
                }
                else if (cmd.equalsIgnoreCase("select")){
                    try{
                        String id=(String)arg0.get("id");
                        //out.println("id = "+id);
                        String sql=(String)arg0.get("sql");
                        //out.println("sql = "+sql);

                        //out.println(sql);
                        //out.println(id);
                        BDConn conn=si.getBDConn(id);

                        if(conn==null){
                            return "error~connection~"+(String)arg0.get("ticket");
                        }
                        else{
                            //out.print("Bdd = ");
                            //out.println(conn.getBdd());
                            Statement st= conn.getConn().createStatement();
                            //out.print("Bdd = ");
                            //out.println(conn.getBdd());
                            //out.println("User = "+conn.getUser());
                            //out.println("Pwd = "+conn.getPwd());

                            ResultSet res=st.executeQuery(sql);
                            String salida="select~"+(String)arg0.get("ticket")+"~"+parser.parsear(res);
                            //out.println("Salida = "+salida);
                            res.close();
                            st.close();
                            return salida;
                        }
                    }
                    catch(Exception e){
                        return "error~select~"+(String)arg0.get("ticket")+"~"+e.getMessage();
                    }
                }
                else if (cmd.equalsIgnoreCase("write")){
                    try{
                        String id=(String)arg0.get("id");
                        String sql=(String)arg0.get("sql");
                        BDConn conn=si.getBDConn(id);
                        if(conn==null){
                            return "error~connection~"+(String)arg0.get("ticket");
                        }
                        else{
                            Statement st= conn.getConn().createStatement();
                            int rows=st.executeUpdate(sql);
                            st.close();
                            return "write~"+(String)arg0.get("ticket")+"~"+rows;
                        }
                    }
                    catch(Exception e){
                        return "error~write~"+(String)arg0.get("ticket")+"~"+e.getMessage();
                    }
                }
                else {
                    return "error~Action "+cmd+" no valido";			
                }
            /*}catch (Exception ex){
                ByteArrayOutputStream a = new ByteArrayOutputStream();
                PrintStream aa = new PrintStream(a);
                ex.printStackTrace(aa);
                return "error~"+a.toString();
            }*/
            }
	}
}
