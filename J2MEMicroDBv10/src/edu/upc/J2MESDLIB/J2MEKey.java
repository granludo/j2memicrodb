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

import java.util.Vector;

/**
 * 
 * Classe que genera una clau en ASCII a partir dels valors
 * donats.
 * @author Juan Antonio Sosa && Mariona Fecúndez
 */
public class J2MEKey{

	String valueLabel="";
	
	/**
     * Crea un objecte J2MEKey buït
     */
	public J2MEKey()
	{}

	/**
     * Afegeix un nou valor a la clau.
     * @param value String d'un dels atributs que forma part de la clau.
     * 
     */
	public void addValueKey(String value){
		valueLabel+=value+";";
	}

	/**
     * Neteja el valor de la clau.
     */
	public void clearKey(){
		valueLabel="";
	} 

	/**
     * Retorna el valor de la clau.
     * @return String format a partir dels valors afegits a la clau.
     */
	public String getKeyLabel(){
		return valueLabel;
	}
}
