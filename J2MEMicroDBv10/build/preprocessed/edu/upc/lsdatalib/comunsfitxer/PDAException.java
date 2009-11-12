/** 
 * Copyright 2007 Universitat Polit�cnica de Catalunya
 *
 * This program is free software; you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301  USA. It is also //currently //available at http://www.gnu.org/licenses/gpl.txt
 *
 * Authors: N�ria Lara, Marc Alier, Maria Jos� Casany, UPC
 *
 * Contact: http://morfeo.upc.edu/crom  malier@lsi.upc.edu, and
 * info@upc.edu / Jordi Girona Salgado, 1-3 E-08034 Barcelona SPAIN
 */
package edu.upc.lsdatalib.comunsfitxer;

import edu.upc.lsdatalib.utilsfitxer.PDAProperties;


/**
 * Classe que representa una excepci� coneguda de les operacions de l'API.
 * @author N�ria Lara Arana
 */
public class PDAException extends Exception {

	/** Codi de l'excepci�. */
	private int code;

	/** Missatge mostrat al programador que indica la causa de l'excepci�. */
	private String usermessage;
	
	/** M�tode que ha llen�at l'excepci�. */
	private String functionexception;
	
	
	/** Constructora de la classe sense par�metres.<br/>
	 * El codi de l'excepci� �s 0 i, tant el m�tode causant de l'error com la causa de l'error, s�n desconeguts. */
	public PDAException()
	{
		this.code = 0;
		
		String value = PDAProperties.getProperty(0);
		
		int iusermessage = value.indexOf(" | ");
		this.usermessage = value.substring(0,iusermessage).trim();
		
		this.functionexception = value.substring(iusermessage+3, value.length()).trim();
	}	
	
	/** 
	 * Constructora de la classe mitjan�ant un codi d'error.
	 * @param codi Codi de l'excepci�.
	 * */
	public PDAException(int codi)
	{
		if(codi<0) codi = 0;
			
		this.code = codi;
		
		String value = PDAProperties.getProperty(codi);
		
		int iusermessage = value.indexOf(" | ");
		this.usermessage = value.substring(0,iusermessage).trim();
		
		this.functionexception = value.substring(iusermessage+3, value.length()).trim();
	}	
	
	/** Retorna el codi d'error de l'excepci�. */
	public int getCode() {
		return code;
	}

	/** Retorna el missatge d'error al programador. */
	public String getUsermessage() {
		return usermessage;
	}	

	/** Retorna el nom del m�tode que ha llen�at l'excepci�. */
	public String getFunctionexception() {
		return functionexception;
	}
	
	/** Imprimeix el codi d'error, el nom del m�tode causant de l'excepci�, la causa de l'excepci� i la tra�a de l'error per la sortida est�ndard. */
	public void print()
	{
		System.out.println(PDAProperties.getProperty(-1)+this.getCode());
		System.out.println(PDAProperties.getProperty(-2)+this.getFunctionexception());
		System.out.println(PDAProperties.getProperty(-3)+this.getUsermessage());
		System.out.println(PDAProperties.getProperty(-4));
		this.printStackTrace();
	}
	
}
