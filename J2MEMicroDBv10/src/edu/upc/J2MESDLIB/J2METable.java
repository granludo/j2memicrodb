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
import edu.upc.lsdatalib.fitxersequencialvalor.*;
import java.io.*;
import java.util.Vector;

/**
 * Classe que conté la informació d'una taula.
 * @author Juan Antonio Sosa && Mariona Fecúndez
 */
public class J2METable {

	/**
	 * Repositori de les dades. Guarda/Obté les dades d'un RMS
	 */	 
	private PDALabeledStoreSequence PDArepositori;

	/**
	 * Marcador del número de files de la taula
	 */
	private	int numRows = -1;

	/**
     * Nom del fitxer RMS amb les dades i clau primària a la taula de metadades
     */
	public String tableName;

	/**
	 * Vector amb la informació de les columnes. Ho portem a memÃ²ria la 
         * primera vegada no hem d'accedir al PDAStore cada vegada
	 */
	private Vector tableColumns;

	/**
     * Constructor de la classe J2METable.
     * Crea el fitxer a disc que 
     * contindrà  les dades de la taula. (Si no existia).
     * @param tableName Nom de la taula.
     * Nombre de la tabla.
     */
	protected J2METable(String tableName){
		try{
			this.tableName = new String(tableName);
			this.PDArepositori  = new PDALabeledStoreSequence(tableName);
			this.numRows   = getNumRows();
                        this.tableColumns = J2MESDLIB.getColumnsInfo(this.tableName); //<-Carreguem la info de les columnes
		}
		catch(PDAException pdae){		
			J2MEDBException excepcio = new J2MEDBException(pdae.getCode());
                        excepcio.addLogException(tableName);                        
		}
	}	

	/**
     * Crea una nova fila amb l'estructura (nom d'atributs) de la taula
     * @return Registre amb l'estructura de la taula, per inserir els valors.
     * 
     */
	public J2MERow createRow(){
		return new J2MERow(this);
	}

	/**
     * Afegeix una nova fila a la taula
     * @param row Fila a afegir a la taula
     * 
     * @exception J2MEDBException Si no es pot afegir la fila a la taula
     * 
     */
	public void addRow(J2MERow row) throws J2MEDBException{
		try{
                         String keyLabel = row.keyLabel();                          
                         if(PDArepositori.seek(keyLabel)) throw new J2MEDBException(332);
			 PDArepositori.write(row, keyLabel);                         
			 numRows++;
                         if (J2MESDLIB.DBLogs) J2MELog.getLog().addLog("Afegida fila a la taula " + tableName, "Clau: " + keyLabel , "J2METable | addRow", J2MELog.ACTION, tableName);
		}
		catch(PDAException pdae){
                    J2MEDBException excepcio = new J2MEDBException(pdae.getCode());
                    excepcio.addLogException(this.tableName);
                    throw (excepcio);
		}
	}

	/**
     * Situa el cursor a la primera posició del Recordset
     * @return Cert si el posicionament es correcte, fals altrament.
     * 
     */
	public boolean first(){
		return (PDArepositori.First());
	}

	/**
     * Avança el cursor a la següent posició de la taula
     * @return Cert si el posicionament es correcte, fals altrament.
     * 
     */
	public boolean next(){
		return (PDArepositori.Next());
	}

	/**
     * Comproba si estem al final de la taula
     * @return Cert si no queden més tuples per visitar.
     * 
     */
	public boolean EOF(){
		return (PDArepositori.EOF());
	}
	
	/**
     * Llegeix la tupla de la posició actual
     * @return tupla actual
     * @exception J2MEDBException Si no es pot llegir la fila.
     * 
     */
	public J2MERow read() throws J2MEDBException{
		try{
			J2MERow row = (J2MERow) PDArepositori.read();                        
			row.setParentTable(this);
			return row;
		}
		catch(PDAException pdae){			
                    J2MEDBException excepcio = new J2MEDBException(pdae.getCode());
                    excepcio.addLogException(this.tableName);
                    throw (excepcio);
		}
	}
        
        /**
     * Actualitza la tupla de la posició actual amb
     * els valors de la tupla passada com a paràmetre.
     * Els valors clau de la fila no es poden actualitzar.
     * @param row Fila amb els nous valors
     * 
     * 
     * @exception J2MEDBException Si hi ha error al actualitzar la fila o 
     * s'intenten actulitzar els valors clau.
     * 
     * 
     */
        public void update(J2MERow row) throws J2MEDBException{
            try{
                J2MERow rowTemp = this.read();
                //Fem l'update si no hi ha clau, o els camps claus no cambien o estem modificant
                //alguna taula de l'esquema
                
                if ( this.getKeyColumns().size() == 0 || row.keyLabel().equals(rowTemp.keyLabel()) ||
                        this.tableName.equalsIgnoreCase(J2MESDLIB.SCHEMA_COLS_FILENAME) ||
                        this.tableName.equalsIgnoreCase(J2MESDLIB.SCHEMA_TABLES_FILENAME)){
                        PDArepositori.update(row);
                        if (J2MESDLIB.DBLogs) J2MELog.getLog().addLog("Actualitzada fila a la taula " + tableName, "Clau: " + row.keyLabel() , "J2MERow | update", J2MELog.ACTION, tableName);
                }
                else{
                    J2MEDBException excepcio = new J2MEDBException(336);
                    excepcio.addLogException(this.tableName);
                    throw (excepcio);
                }
            }
            catch(PDAException pdae){
                if(pdae.getCode()==124){
                    //PDAStore no obert/incorrecte
                     J2MEDBException excepcio = new J2MEDBException(333);
                    excepcio.addLogException(this.tableName);
                    throw (excepcio);                    
                }
                else if(pdae.getCode()==125){
                    //PDAStore ha arribat al maxim de capacitat
                    J2MEDBException excepcio = new J2MEDBException(334);
                    excepcio.addLogException(this.tableName);
                    throw (excepcio);
                }
                else if(pdae.getCode()==126){
                    //Error general PDAStore
                     J2MEDBException excepcio = new J2MEDBException(335);
                    excepcio.addLogException(this.tableName);
                    throw (excepcio);
                }
            }
        }

	/**
     * Esborra la tupla de la posició actual.
     * @exception J2MEDBException Si no es pot esborrar la tupla.
     * 
     */
	public void delete() throws J2MEDBException{
		try{
                        String keyLabel = this.read().keyLabel(); //si no hi ha log no fem aixÃ³
			PDArepositori.delete();
			numRows--;
                       if (J2MESDLIB.DBLogs) J2MELog.getLog().addLog("Esborrada fila de la taula " + tableName, "Clau: " + keyLabel ,  "J2MERow | delete", J2MELog.ACTION, tableName);
		}
		catch(PDAException pdae){			
                    J2MEDBException excepcio = new J2MEDBException(pdae.getCode());
                    excepcio.addLogException(this.tableName);
                    throw (excepcio);
		}
	}

	/**
     * Situa el cursor fent una cerca per clau.
     * @param keyValue Objecte amb els valors clau de l'objecte que volem cercar.
     * 
     * 
     * @return Cert si el posicionament és correcte, fals altrament o si no hi ha clau primària.
     * 
     * 
     */
	public boolean seek (J2MEKey keyValue){
            if ( this.getKeyColumns().size() == 0 ){
                return false;
            }
            try{
                String keyLabel = keyValue.getKeyLabel();
                return(PDArepositori.seek(keyLabel));
            }
            catch(PDAException pdae){
                return false;
            }
	}

	/**
     * Retorna el número de registres de la taula.
     * @return Enter amb el número de registres de la taula.
     * 
     * 
     */
	public int rowCount(){
		return this.numRows;
	}
        
        /**
     * Retorna un vector de J2MEColumns amb la informació de les columnes de la taula.
     * @return Vector de J2MEColumns amb les columnes de la taula.
     * 
     */
        public Vector getColumns(){
           return tableColumns;
        }

	/**
     * Esborra el fitxer associat a la Taula.
     * @return Cert si l'esborrat es fa correctament , fals altrament.
     * 
     */
	protected boolean deleteRMS(){
		try{                                                       
                       
			PDArepositori.closePDAStore(); //<-S'ha de tancar abans d'esborrarlo.                        
			PDArepositori.deletePDAStore();	//<-Esborra RMS i Ã­ndex associat
			return true;
		}
		catch(PDAException pdae){
                        //pdae.print();
                        //System.out.println("Peta el Delete RMS de la taula: " + this.tableName);
                        try{
                            PDArepositori.deletePDAStore();
                            return true;
                        }
                        catch(PDAException pdae2){                            
                            //System.out.println("Peta el 2on intent de la taula: " + this.tableName);
                        }
			return false;
		}
	}
        
        /**
     * Guarda els canvis fets a la taula.
     * @exception J2MEDBException Si no es poden guardar els canvis fets a la taula.
     */
        public void saveTable() throws J2MEDBException{
            try{
                PDArepositori.saveIndex();
            }
            catch(PDAException pdae){                
                    J2MEDBException excepcio = new J2MEDBException(pdae.getCode());                    
                    excepcio.addLogException(this.tableName);
                    throw (excepcio);
            }
        }  
        
        /**
     * Tanca i guarda els canvis de la taula;
     * S'ha de fer cada cop que ja no es necessiti treballar més amb la taula
     * altrament, podrien haver problemes d'inconsistència.
     */
        public void closeTable(){
            try{
                //System.out.println("Close PDAStore " + tableName);
                PDArepositori.closePDAStore();                
            }
            catch(PDAException pdae){        
                    //System.out.println("Peta el closePDAStore de la taula: " + tableName);
                    J2MEDBException excepcio = new J2MEDBException(pdae.getCode());
                    excepcio.addLogException(this.tableName);                    
            }
        }
        
        /**
         * Inicialitza nÃºmero de registres de la taula
         *  @return Enter amb el nÃºmero de registres de la taula al obrirla
         */
	private int getNumRows(){
                
            int rowCount=0;
            if(PDArepositori.First()){
                rowCount=1;
                while(!PDArepositori.EOF()){
                    PDArepositori.Next();
                    rowCount++;
                }	
                PDArepositori.First();
            }                        
            return rowCount; 
	}

	/**
     * 
     * Retorna el número de camps de la taula.
     * @return Enter amb el número de columnes de la taula.
     * 
     */
	public int getNumCols(){
            return tableColumns.size();
	}

	/**
     * Retorna un vector amb els indexos de les columnes clau de la taula.
     * @return Vector d'enters amb els índexos de les columnes que son clau. 
     * Per a ús intern de la API.
     */
	public Vector getKeyColumns(){
		
		Vector tableKeys = new Vector();
		for(int i=0;i<tableColumns.size();i++){
			if( ((J2MEColumn) tableColumns.elementAt(i)).isKeyField())
                        {
				tableKeys.addElement(String.valueOf(i));                                
                        }
		}
              
		return tableKeys;
	}
        
        /**
     * Crea una copia de la taula actual amb el nom passat com a paràmetre.
     * @param tableName Nom de la nova taula.
     * 
     * @return J2METable taula clonada.
     * @exception J2MEDBException Si ja existeix una taula amb el nom indicat o si hi ha cap error
     * al crear la taula.
     */
        public J2METable clone(String tableName) throws J2MEDBException{
            J2METable temp = null;
            try
            {
                temp = J2MESDLIB.createTable(tableName, tableColumns, getKeyColumns().size());
                
                if(PDArepositori.First()){                
                    J2MERow row = this.read();
                    temp.addRow(row);                    
                    while(!PDArepositori.EOF()){
                        PDArepositori.Next();
                        row = this.read();
                        temp.addRow(row);                    
                    }	
                
                    PDArepositori.First();
                }
            }
            catch(J2MEDBException ex){
              throw(ex);   
            }    
            
            return temp;
        }

    	/**
     * Retorna un String amb les dades de la taula.
     * @return String amb la capçalera i els valors de les files de la taula.
     */
	public String printTable(){

		String s = "* * * * * * * *  * * * " + this.tableName.toUpperCase() + "* * * * * * * *  * * *  \n ( ";

		//CapÃ§alera de la taula
		for(int i=0;i<tableColumns.size();i++){
                        String columnName = ((J2MEColumn) tableColumns.elementAt(i)).getFieldName();
                        while(columnName.length()<20){
                            columnName =" " + columnName + " ";                        
                        }
                        columnName = columnName.substring(0,20);
			s+= "[" + columnName.toUpperCase() + "]" + " ,";
		}

		s=s.substring(0, s.length()-1) + " ) \n";		

		//Printem els rows de la taula
		J2MERow row;
                    
		if(this.first()){
                    try{
			row = this.read();
                        s+= "   " + row.printRow() + "   \n";
                    }
                    catch(J2MEDBException ex){
                        s+= "   " + " null" + "   \n";
                    }
			
			while(!this.EOF()){
				this.next();
                                try{
                                    row = this.read();
                                    s+= "   " + row.printRow() + "   \n";
                                }
                                catch(J2MEDBException ex){
                                    s+= "   " + "null" + "   \n";
                                }
			}
                    
		}

                s+="* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n"; 
		return s;				

	}
}
