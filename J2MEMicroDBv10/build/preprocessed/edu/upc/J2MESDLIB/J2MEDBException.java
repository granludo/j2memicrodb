/** 
* Copyright 2007 Universitat Politécnica de Catalunya 
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
* 02110-1301 USA. It is also //currently //available at http://www.gnu.org/licenses/gpl.txt 
* 
* Authors:  Juan Antonio Sosa, Mariona Fecúndez, Marc Alier, Maria José Casany UPC 
* 
* Contact: http://morfeo.upc.edu/crom malier@lsi.upc.edu, and 
* info@upc.edu / Jordi Girona Salgado, 1-3 E-08034 Barcelona SPAIN 
*/ 

/*
 * J2MEMicroDBException.java
 *
 * Created on 11 de diciembre de 2006, 12:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.upc.J2MESDLIB;

import edu.upc.J2MESDLIB.utilssgbd.PDAProperties;


/**
 * Classe que representa una excepció coneguda de les operacions de l'API.
 * @author Juan Antonio Sosa & Mariona Fecúndez (basada en la classe PDAException de Núria Lara)
 */
public class J2MEDBException extends Exception {

	/** Codi de l'excepció. */
	private int code;

	/** Missatge mostrat al programador que indica la causa de l'excepció. */
	private String usermessage;
	
	/** Métode que ha llençat l'excepció. */
	private String functionexception;
	
	
	/**
     * Constructora de la classe sense paràmetres.
     */
	public J2MEDBException()
	{
		this.code = 0;
		
		String value = PDAProperties.getProperty(0);
		
		int iusermessage = value.indexOf(" | ");
		this.usermessage = value.substring(0,iusermessage).trim();
		
		this.functionexception = value.substring(iusermessage+3, value.length()).trim();
	}	
	
	/**
     * 
     * Constructora de la classe mitjançant un codi d'error.
     * @param codi Codi de l'excepció.
     */
	public J2MEDBException(int codi)
	{
                System.out.println("Excepcio amb codi: " + codi);
            
		if(codi<0) codi = 0;
			
		this.code = codi;
		
		String value = PDAProperties.getProperty(codi);
		
		int iusermessage = value.indexOf(" | ");
		this.usermessage = value.substring(0,iusermessage).trim();
		
		this.functionexception = value.substring(iusermessage+3, value.length()).trim();
	}	
	
	/**
     * Retorna el codi d'error de l'excepció.
     * @return Enter amb el codi d'error.
     */
	public int getCode() {
		return code;
	}

	/**
     * Retorna el missatge d'error al programador.
     * @return String amb el missatge d'error.
     */
	public String getUsermessage() {
		return usermessage;
	}	

	/**
     * Retorna el nom del métode que ha llençat l'excepció.
     * @return String amb el nom del métode que ha llençat l'excepció.
     */
	public String getFunctionexception() {
		return functionexception;
	}
	
	/**
     * Imprimeix el codi d'error, el nom del mètode causant de l'excepció, 
     * la causa de l'excepció i la traça de l'error per la sortida estàndard.
     */
	public void print()
	{
		System.out.println(PDAProperties.getProperty(-1)+" "+this.getCode());
		System.out.println(PDAProperties.getProperty(-2)+" "+this.getFunctionexception());
		System.out.println(PDAProperties.getProperty(-3)+" "+this.getUsermessage());
		System.out.println(PDAProperties.getProperty(-4));
		this.printStackTrace();
	}
        
        /**
     * Afegeix al Log l'excepció.
     * @param tableName Nom de la taula.
     */
        protected void addLogException(String tableName){
            if(J2MESDLIB.DBLogs){ 
                    String info="";
                    if(usermessage.length()>255)
                        info = usermessage.substring(0, J2MEColumn.ColumnType.MAX_COLUMN_SIZE);
                    else
                       info = usermessage;
                    J2MELog.getLog().addLog("Excepció amb codi: " + this.code, info ,
                                                            functionexception, J2MELog.EXCEPTION, tableName );
                }
        }	
}
