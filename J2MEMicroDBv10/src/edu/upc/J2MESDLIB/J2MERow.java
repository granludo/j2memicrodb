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
import java.util.Vector;

/**
 * Classe que simula el comportament d'una tupla(record/fila) d'una taula.
 * @author Juan Antonio Sosa & Mariona Fecúndez
 */
public class J2MERow implements PDAStoreable{
	
	/**
	 * Vector que conté els valors de la tupla
	 */
       
    
	Vector values;
	
	/**
	 * Clau de la tupla
	 */
      
        
	private J2MEKey keyValue;

	/**
	 * Referència a J2METable a la que pertany
	 */
     
        
	J2METable parentTable;
	
	/**
     * Constructor buit.
     * S'utilitza per carregar les dades de la taula.
     */
       
     
        
	public J2MERow(){
		keyValue = null;
	}
	
	/**
     * Constructor de la classe. 
     * 
     * L'utilitza la classe J2METable per crear les seves tuples amb el format
     * que hauran de tenir segons la definició de les columnes de la taula.
     * @param parentTable nom de la taula a la qual pertany la tupla.
     */
        
       
        
	protected J2MERow(J2METable parentTable){		
		this.parentTable = parentTable;
		keyValue = null;
		initValuesVector(parentTable.getNumCols());
	}	

	/**
     * Asocia una taula a la columna creada.
     * S'utilitza quan  es recuperen les dades de la tupla de disc.
     * Métode protegit, no el pot usar el programador.
     * @param parent Taula a la que pertany la tupla.
     */
	protected void setParentTable(J2METable parent){
		this.parentTable = parent;                    
	}

	/**
     * Retorna el valor del camp situat a la posició 'index' 
     * de la tupla.
     * @param index Posició del camp.
     * @return String amb el valor del camp situat a la posició 'index'.
     * @exception J2MEDBException si l'índex és més gran que el número de
     * camps que té la tupla.
     */
	public String getColValue(int index) throws J2MEDBException{
		
		if(index>=values.size()){
                    
                    J2MEDBException excepcio = new J2MEDBException(304);
                    excepcio.addLogException(this.parentTable.tableName);
                    throw (excepcio);
		}
		
		return values.elementAt(index).toString();		
	}
		
	/**
     * Retorna el valor de la columna anomenada colName de la tupla.
     *
     * @param colName de la columna.
     * @return String amb el valor del camp.
     * @exception J2MEDBException si no hi ha cap columna de nom colName 
     * a la taula.
     */
	public String getColValueByName(String colName) throws J2MEDBException{
		
          
            Vector f=new Vector();
            for (int i=0; i<this.parentTable.getColumns().size(); i++) {
              
                J2MEColumn col = (J2MEColumn)this.parentTable.getColumns().elementAt(i);
                String s = col.getFieldName();
                f.addElement(s);  
                
            }
            
            int fieldIndex = f.indexOf(colName);
            
            
		if (fieldIndex==-1){
                    J2MEDBException excepcio = new J2MEDBException(305);
                    excepcio.addLogException(this.parentTable.tableName);
                    throw (excepcio);
		}
		
		return values.elementAt(fieldIndex).toString();
	}
		
	/**
     * Estableix en el camp de la posició 'index' el valor indicat;
     * 
     * No comprova que el tipus donat coincideixi amb la descripció de la 
     * columna, es suposa que el programador sap els tipus que ha d'inserir
     * Només comproba que el tamany no sobrepassi l'indicat;
     * 
     * Deixa canviar el valor de tots els camps (clau o no clau);
     * 
     * Pels valors booleans: true:"1", false:"0".
     * @param value Nou valor del camp.
     * @param index índex del camp que volem actualitzar.
     * @exception J2MEDBException si la mida del valor supera el tamamy màxim
     * permés segons el tipus del camp.
     */
	public void setColValue(String value, int index) throws J2MEDBException{
		
		if(index>=values.size()){
                    J2MEDBException excepcio = new J2MEDBException(306);
                    excepcio.addLogException(this.parentTable.tableName);
                    throw (excepcio);                    
                }			
                
		J2MEColumn col = (J2MEColumn)parentTable.getColumns().elementAt(index);
                if(col.getFieldSize()<value.length()){
                    J2MEDBException excepcio = new J2MEDBException(307);
                    excepcio.addLogException(this.parentTable.tableName);
                    throw (excepcio);
                }

		values.setElementAt(value, index);
	}
	
	/**
     * Estableix el valor de la columna anomenada colName;
     * 
     * No comprova que el tipus donat coincideixi amb la descripció de la 
     * columna, es suposa que el programador sap els tipus que ha d'inserir;
     * Només comproba que el tamany no sobrepassi l'indicat
     * Deixa canviar el valor de tots els camps (clau o no clau) 
     * Pels valors booleans: true:"1", false:"0";
     * @param value Nou valor que volem posar.
     * @param colName Nom de la columna que volem actualitzar.
     * @exception J2MEDBException si no hi ha cap camp amb el nom indicat
     * al PDARecord.
     */
	public void setColValueByName(String value, String colName) throws J2MEDBException {
	
             
            Vector f=new Vector();
            for (int i=0; i<this.parentTable.getColumns().size(); i++) {
              
                J2MEColumn col = (J2MEColumn)this.parentTable.getColumns().elementAt(i);
                String s = col.getFieldName();
                f.addElement(s);  
                
            }
            
            int fieldIndex = f.indexOf(colName);            
         
            if (fieldIndex==-1){
                
                J2MEDBException excepcio = new J2MEDBException(308);
                excepcio.addLogException(this.parentTable.tableName);
                throw (excepcio);
            }
                
            J2MEColumn col = (J2MEColumn)parentTable.getColumns().elementAt(fieldIndex);
                
            if(col.getFieldSize()<value.length()){
                J2MEDBException excepcio = new J2MEDBException(309);
                excepcio.addLogException(this.parentTable.tableName);
                throw (excepcio);
            }                 
		
            values.setElementAt(value, fieldIndex);
       
	}

	/**
     * Genera la clau de la tupla:
     *  - si la taula no té atributs clau es genera una clau basada en el número
     * de registre dins la taula;
     *  - si té atributs clau genera una clau J2MEKey a partir dels valors dels
     * camps clau de la tupla.
     * @return Key String
     */
	protected String keyLabel(){
		
		if (keyValue==null){
			keyValue = new J2MEKey();			
			/* agafa els valors clau (amb les metadades) i els afegeix al keyValue*/
			Vector keyColumns = parentTable.getKeyColumns();			                        
			for(int i = 0; i < keyColumns.size(); i++){
				int indexCol = Integer.parseInt(keyColumns.elementAt(i).toString());
				keyValue.addValueKey(values.elementAt(indexCol).toString());
			}
                        
                        if(keyColumns.size()==0){ //generem una clau basada en el nï¿½mero de registre
                            String numRow = String.valueOf(parentTable.rowCount() + 1);
                            while(numRow.length()<8) numRow = "0" + numRow;
                            keyValue.addValueKey(numRow);
                        }
		}

		return keyValue.getKeyLabel();
	}
	
	//mï¿½todes privats 
	
	/**
	 * Inicialitza el vector de valors del PDARecord amb el nï¿½mero 
	 * de camps que indican les metadadades i amb un valor per defecte
     * 
     * @param numValues Nï¿½mero de camps que que ha de tenir la tupla
	 */
	private void initValuesVector(int numValues){
		
		values = new Vector();
		
		for(int i=0;i<numValues;i++){
			values.addElement("");
		}
	}

	//mï¿½todes de la interface PDAStoreable
	public byte[] getData(){
		
		try{			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			DataOutputStream outputDataStream = new DataOutputStream(outputStream);
			
			//guardem el nï¿½mero de camps que tenim, perque al setData
			//el necessitarem
			outputDataStream.writeInt(values.size()); 
			
			//Escribim tots els valors que tenim al PDARecord
			for(int i=0;i<values.size();i++){                            
				outputDataStream.writeUTF(this.values.elementAt(i).toString());
			}
			
			return(outputStream.toByteArray());
		}
		catch(IOException ioe){
			ioe.printStackTrace();
			return null;
		}
	}
	
	public void setData(byte[] b, int size) throws PDAException{
		
		if(b.length!=size) throw new PDAException(2);
		try{
			
			ByteArrayInputStream inputStream = new ByteArrayInputStream(b);
			DataInputStream inputDataStream = new DataInputStream(inputStream);
			
			//Amb el numValues sabem quants valors hem de llegir
			int numValues = inputDataStream.readInt();
			values = new Vector();
			
			for(int i=0;i<numValues;i++){
				this.values.addElement(inputDataStream.readUTF());
			}
						
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public int size(){
		return this.getData().length;
	}
	
	public int compare(PDAStoreable c){
		
		byte[] data = this.getData();
		
		if(data.length > c.size()) return 1;
		else if (data.length < c.size()) return -1;

		/*Comparem valor per valor les dues arrays*/		
		int size = data.length;
		for(int i=0; i<size; i++)
		{
			if(data[i] != c.getData()[i]) return -1; 
		}
		
		return 0;
	}

   /**
     *  Retorna un String amb els valors de la tupla.
     * @return String amb els valors de la fila.
     */
	public String printRow(){
		String s=""; 
                String columnValue="";
		for (int i=0;i<values.size();i++){                  
                    columnValue = this.values.elementAt(i).toString();
                    while(columnValue.length()<20){
                            columnValue =" " + columnValue + " ";                        
                    }
                    columnValue = columnValue.substring(0,20);
                    s+= "[" + columnValue + "] ,";                  
		}
                s=s.substring(0, s.length()-1);		
		return s;
	}
}
