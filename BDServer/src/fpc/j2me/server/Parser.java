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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Date;

/**
 * Parsea el resultado de la select para que el cliente pueda decodificarlo e insertarlo en un recorset
 * segun nuestra dll.
 */
public class Parser {
	
	/**
	 * Parsea el resultado de la select
	 * @param rs resulset con el resultado de la consulta sql
	 * @return devuelve un string con la informacion del resulset parseada
	 */
	public String parsear(ResultSet rs){
		String buffer="";
		try{
			ResultSetMetaData meta=rs.getMetaData();
			int colsSize=meta.getColumnCount();
			buffer+="'"+colsSize+"'~";
			rs.last();
			buffer+="'"+rs.getRow()+"'~"; //getFetchSize()+";";
			rs.beforeFirst();
			
			for (int i=1;i<=meta.getColumnCount();i++){
				buffer+="'"+meta.getColumnName(i)+"'~";
				buffer+="'"+translate(meta.getColumnType(i))+"'~";
				if (meta.getColumnType(i)==Types.VARCHAR){
					buffer+="'"+meta.getColumnDisplaySize(i)+"'~";//if string #chars else 0
				} else {
					buffer+="'0'~";
				}
			}
			
			
			while (rs.next()){
				buffer+="~";
				for (int i=1;i<=colsSize;i++){
                                    if(meta.getColumnType(i)==Types.BOOLEAN ||meta.getColumnType(i)==Types.BIT || meta.getColumnType(i)==Types.BINARY){
                                        buffer+="'"+rs.getByte(i)+"'~";
                                    }
                                    else{
					buffer+="'"+rs.getObject(i)+"'~";
                                    }
				}
				buffer+="~";
			}
			
			buffer+="~";
			
			return buffer;
		}catch (Exception ex){
			return "Error~"+ex.getMessage();
		}
	}
	
	/**
	 * Transforma el tipo de datos de int a string
	 * @param type tipo de datos del resulset
	 * @return string con el tipo de datos del resulset
	 */
	private String translate(int type){
		switch (type){
		case Types.BIGINT:
			return Long.class.getName();
		case Types.INTEGER:
			return Integer.class.getName();
		case Types.BINARY:
			return Boolean.class.getName();
		case Types.SMALLINT:
			return Short.class.getName();
                case Types.BIT:
                    return Boolean.class.getName();
                case Types.BOOLEAN:
			return Boolean.class.getName();
		case Types.CHAR:
			return Character.class.getName();
		case Types.DATE:
			return Date.class.getName();
		case Types.DOUBLE:
			return Double.class.getName();
		case Types.FLOAT:
			return Float.class.getName();
		case Types.NUMERIC:
			return Integer.class.getName();
		case Types.TIMESTAMP:
			return Date.class.getName();
		case Types.VARCHAR:
			return String.class.getName();
		case Types.TIME:
			return Date.class.getName();
		}
		return String.class.getName();
		
	}
	
}
