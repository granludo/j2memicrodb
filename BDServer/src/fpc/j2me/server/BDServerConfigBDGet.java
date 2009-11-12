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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Pelsman
 */
public class BDServerConfigBDGet extends HttpServlet{
    
    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        this.doPost(arg0, arg1);
    }

    protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        PrintWriter out=arg1.getWriter();
        
        try{
            BDConn conn=new BDConn("dbavailable","com.mysql.jdbc.Driver","jdbc:mysql://localhost:3306","nestor","nestor");
            Statement st = conn.getConn().createStatement();
            ResultSet res=st.executeQuery("select * from db");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>J2ME Server configuration: Get DataBase</title>");
            out.println("</head>");
            out.println("<body>");
            int i = 0;
            res.beforeFirst();
            while(res.next()){
                out.println("<form name=\"row"+i+"\" action=\"http://127.0.0.1:8080/J2MEServer/servlets/servlet/BDServerConfigBDModif\" method=post>");
                out.println("<input type=\"text\" name=\"nombd\" value=\""+res.getString(1)+"\" readonly><input type=\"text\" name=\"sgbd\" value=\""+res.getString(2)+"\" readonly><input type=\"text\" name=\"nomsgbd\" value=\""+res.getString(3)+"\" readonly><input type=text name=\"driver\" value=\""+res.getString(4)+"\" readonly><input type=\"text\" name=\"url\" value=\""+res.getString(5)+"\" readonly><input type=submit value=\"Esborrar\">");
                out.println("</form>");
                
                i++;
            }
            out.println("<br>");
            out.println("<br>");
            out.println("<a href=\"../../index.html\">Tornar</a>");
            
            
            out.println("</body>");
            out.println("</html>");
            conn.getConn().close();
            st.close();
            res.close();
        }
        catch(Exception e){
            e.printStackTrace(out);
        }
        
        
    }
    
    protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        super.service(arg0,arg1);
    }
    
}
