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
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Establece la conexion
 */
public class BDConn {

	private String bdd;
        private String driver;
	private String user;
	private String pwd;
	private Connection conn;
	
	/**
	 * Establece la conexion
	 * @param bdd nombre de la base de datos
	 * @param user usuario de la base de datos
	 * @param pwd pasword de la base de datos
	 * @throws Exception error producido al no poder abrir la base de datos
	 */
	public BDConn(String bdd, String driver, String url, String user, String pwd) throws Exception{
	
		this.bdd=bdd;
                this.driver=driver;
		this.user=user;
		this.pwd=pwd;
                
		/*Class.forName("com.mysql.jdbc.Driver").newInstance();
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+bdd+"?user="+user+"&password="+pwd);*/
                
                Class.forName(driver);
                conn = DriverManager.getConnection(url+"/"+bdd,user, pwd);
        }
	
	/**
	 * Obtiene el nombre de la base de datos
	 * @return devuelve un string con el nombre de la base de datos
	 */
	public String getBdd() {
		return bdd;
	}
	
	/**
	 * Establece el nombre de la base de datos
	 * @param bdd el nombre de la base de datos
	 */
	public void setBdd(String bdd) {
		this.bdd = bdd;
	}
        
        	/**
	 * Obtiene el driver que contiene la base de datos
	 * @return devuelve un string con el driver que contiene la base de datos
	 */
	public String getDriver() {
		return driver;
	}
	
	/**
	 * Establece el driver que contiene la base de datos
	 * @param bdd el nombre del driver que contiene la base de datos
	 */
	public void getDriver(String sgbd) {
		this.driver = driver;
	}
	
	/**
	 * Obtiene el pasword de la base de datos
	 * @return devuelve un string con el pasword de la base de datos
	 */
	public String getPwd() {
		return pwd;
	}
	
	/**
	 * Establece el pasword de la base de datos
	 * @param pwd pasword de la base de datos
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	/**
	 * Obtiene el nombre de usuario de la base de datos
	 * @return devuelve un string con el nombre de usuario de la base de datos
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * Establece el nombre de usuario de la base de datos
	 * @param user nombre de usuario de la base de datos
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Obtiene la cadena de conexion
	 * @return devuelve un string con la cadena de conexion
	 */
	public Connection getConn() {
		return conn;
	}

	/**
	 * Establece la cadena de conexion
	 * @param conn cadena de conexion
	 */
	public void setConn(Connection conn) {
		this.conn = conn;
	}
	
	
}
