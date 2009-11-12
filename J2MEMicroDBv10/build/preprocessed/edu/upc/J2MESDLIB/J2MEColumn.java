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

import edu.upc.lsdatalib.comunsfitxer.*;
import java.io.*;

/**
 * Classe que emmagatzema les valors d'una columna d'una J2METable.
 * Guarda el nom de la columna, si el camp es clau i el tipus de la dada.
 * @author Juan Antonio Sosa & Mariona Fecúndez
 */
public class J2MEColumn{	

	private boolean isKey;
	private String	fieldName;
	private String	fieldType;
        private int dataSize;    
        
	/**
     * Constructor privat de la classe.
     * @param fieldName Nom de la columna.
     * @param isKey Indica si el camp es clau de la taula.
     * @param fieldType Tipus de dada del camp en ASCII (ColumnType.INT, ColumnType.VARCHAR, ColumnType.DOUBLE, COlumnType.BOOL).
     * @param dataSize Numero maxim de caracters ASCII de la columna si la columna es de tipus VARCHAR. Si no s'ignora.
     * @throws J2MEDBException Si hi ha cap error al crear la columna.
     */
	public J2MEColumn(String fieldName, boolean isKey, String fieldType, int dataSize) throws J2MEDBException{
		
                
                if(fieldType.equals(ColumnType.VARCHAR) && dataSize > ColumnType.MAX_COLUMN_SIZE){ 
                    J2MEDBException excepcio = new J2MEDBException(301);
                    excepcio.addLogException("");
                    throw (excepcio);
                }
                if ( ! typeOK(fieldType) ){ 
                    J2MEDBException excepcio = new J2MEDBException(302);
                    excepcio.addLogException("");
                    throw (excepcio);
                }
		this.fieldName  = fieldName;
		this.fieldType  = fieldType;
		this.isKey      = isKey;
                if(fieldType.equals(ColumnType.VARCHAR))
                    this.dataSize   = dataSize;
                else this.dataSize = ColumnType.getSize(fieldType);                
	}

	/**
     * Obté el nom del camp.
     * @return String amb el nom del camp.
     */
	public String getFieldName(){
	
		return this.fieldName;
	}

	/**
     * Obté el tipus de dades del camp.
     * @return String amb el nom del camp.
     */
	public String getFieldType(){
		
		return this.fieldType;
	}

	/**
     * Indica si el camp es clau de la tupla.
     * @return Cert si ho es, Fals altrament.
     */
	public boolean isKeyField(){
		
		return this.isKey;
	}
        
        /**
     * Retorna el tamany màxim de les dades en bytes.
     * @return Enter amb el tamany màxim que ocuparan les dades en bytes.
     * 
     */
        public int getFieldSize(){
            
            return this.dataSize;
        }
        

	/**
     * Sobreescriptura del mètode equals;
     * Compara les columnes utilitzant l'atribut fieldName.
     * Si l'objecte no és del tipus J2MEColumn retorna sempre fals.
     * @param o J2MEColumn a comparar.
     * @return Cert si el fieldName de l'objecte o és igual al de l'objecte actual
     */	
	public boolean equals(Object o) {   	
                if (o.getClass().getName().equalsIgnoreCase(this.getClass().getName()))
                    return (this.fieldName==((J2MEColumn)o).fieldName); // funciÃ³ de comparaciÃ³   	
                return false;
   	} 
        
        /**
         * Comproba si el tipus fieldType es un dels tipus permesos per la API
         * @param fieldType string amb el tipus de dada
         * @return Cert si es valid, Fals altrament
         */
        private boolean typeOK(String fieldType){
            if (fieldType.equals(ColumnType.BOOL) || fieldType.equals(ColumnType.VARCHAR) || fieldType.equals(ColumnType.INT) || fieldType.equals(ColumnType.DOUBLE))
                return true;
            else 
                return false;
        }
        
        /**
     * Classe que emmagatzema les constants dels tipus de columna i dels tamanys màxims.
     */
        public static class ColumnType{
            /*String que indica el tipus de la dada que guardarem*/
        /**
         * Tipo entero para el contenido de la columna.
         */
            public static String INT    = "int";
        /**
         * Tipo booleano para el contenido de la columna.
         */
            public static String BOOL   = "boolean";
        /**
         * Tipo double para el contenido de la columna.
         */
            public static String DOUBLE = "double";
        /**
         * Tipo varchar para el contenido de la columna. (String)
         */
            public static String VARCHAR = "varchar"; 
            
            /**
         * Tamany màxim d'un Int32 codificat com ASCII;
         * [-2147483647,+2147483647] <- 11 caràcters.
         */
            public static int INT_SIZE  = 11;
            
            /**
         * Tamany màxim d'un boolean codificat com ASCII;
         * {0,1} <- 1 caràcter
         */
            public static int BOOL_SIZE = 1;
            
            /**
         * Tamany màxim d'un caràcter codificat com ASCII.
         */
            public static int CHAR_SIZE = 1;
            
            /**
         * Tamany màxim d'un Double (amb 6 decimals) codificat com ASCII;
         * [-2147483647.000000,+2147483647.000000] <- 18 caràcters.
         */
            public static int DOUBLE_SIZE = 18;
            
            
            /**
         * Tamany màxim d'una cadena de caràcters.
         */
            public static int MAX_COLUMN_SIZE = 255;
            
        /**
         * Retorna un enter amb la mida màxima en caràcters
         * del tipus passat com a paràmetre.
         * @param columnType String amb el tipus de dades permesos 
         * (ColumnType.INT, ColumnType.VARCHAR, ColumnType.DOUBLE, COlumnType.BOOL).
         * @return int amb el valor màxim de caràcters per aquella dada, 
         * 0 si el tipus de dada no és cap dels permesos.
         */
        public static int getSize(String columnType){
            if(columnType.equals(ColumnType.INT))                
                    return ColumnType.INT_SIZE;
            else if (columnType.equals(ColumnType.BOOL))
                    return ColumnType.BOOL_SIZE;
            else if(columnType.equals(ColumnType.DOUBLE))
                    return ColumnType.DOUBLE_SIZE;
            else if(columnType.equals(ColumnType.VARCHAR))
                    return ColumnType.CHAR_SIZE;
            else return 0;
            }
        }
        
}
