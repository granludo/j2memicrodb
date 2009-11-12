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
import java.io.*;


/**
 * Classe que encapsula la gestió de les metadades d'un conjunt de taules i 
 * permet la creació/modificació de taules dins d'aquest conjunt.
 * @author Juan Antonio Sosa & Mariona Fecúndez
 */
public class J2MESDLIB{

	/*Definicio de constants*/
	
	/**
     * Nom de la taula de metadades que guarda informació de les taules.
     */	
	public static final String SCHEMA_TABLES_FILENAME = "SDLIB_schema_tables";
        /**
     * Nom de la taula de metadades que guarda la informació de les columnes.
     */	
	public static final String SCHEMA_COLS_FILENAME = "SDLIB_schema_cols";
        /**
     * Nom de la taula temporal generada al executar querys contra el servidor.
     */
        public static final String TEMP_TABLE_FILENAME = "SDLIB_temp_table";

	/* Indexos dels atributs de la taula SCHEMA_TABLES*/
	private static final int TableName_Column 	= 0;
	private static final int NumCols_Column   	= 1;
	private static final int NumKeys_Column   	= 2;

	/* Indexos dels atributs de la taula SCHEMA_COLS (TableName_Column es comu)*/
	private static final int RowId_Column      	= 1;
	private static final int ColName_Column         = 2;   	 
	private static final int ColType_Column         = 3;
	private static final int IsKey_Column		= 4;
        private static final int ColSize_Column         = 5;

	/* Noms dels atributs de la taula SCHEMA_TABLES */
	private static final String TableName_Name 	= "tablename";
	private static final String NumCols_Name   	= "numcols";
	private static final String NumKeys_Name   	= "numkeys";

	/* Noms dels atributs de la taula SCHEMA_COLS (TableName_Name es comuna) */
	private static final String RowId_Name      	= "rowid";
	private static final String ColName_Name	= "colname";   	 
	private static final String ColType_Name	= "coltype";
	private static final String IsKey_Name        = "iskey";
        private static final String ColSize_Name      = "colsize";
        
        /* Tamany maxim de les columnes de tipus varchar a la taula SCHEMA_TABLES */
        private static final int TableName_Size         = 50;
        
        /* Tamany maxim de les columnes de tipus varchar a la taula SCHEMA_COLS (Table_Name es comuna) */
        private static final int ColName_Size           = 50;
        private static final int ColType_Size           = 50;
        

	/* Numero d'atributs de les taules de metadades */
	private static final int SCHEMA_TABLES_NumColumns = 3;
	private static final int SCHEMA_COLS_NumColumns = 6;

	/* Vectors amb la informacio de noms i tipus de les columnes de les taules de metadades */
	private static Vector SCHEMA_TABLES_Vector = null;
	private static Vector SCHEMA_COLS_Vector = null;

	/**
	 * Taula de metadades del sistema amb informaciï¿½ de les taules
	 */
	private static J2METable SCHEMA_TABLES_Table = null;
        
        /**
	 * Taula de metadades del sistema amb informaciï¿½ de les columnes de <br/>
         * cada taula 
	 */
	private static J2METable SCHEMA_COLS_Table = null;
        
        /**
         * Indica si el sistema de sgdb esta inicialitzat
         */
        private static boolean DBGenerated = false;
        
        /**
     * Indica si es guardaran a un Log les accions i excepcions produïdes.
     */
        protected static boolean DBLogs      = true;

	/**
     * Funció encarregada de comprobar si les taules de metadades existeixen
     * i crear-les si no ho estan.
     * @param logs true si volem que es generi la taula de Logs, false en cas contrari.
     */
	public static void generateDB(boolean logs){
		createSchemaTables();
		createSchemaColumns();
                DBGenerated = true;
                DBLogs = logs;
                if (DBLogs) J2MELog.getLog().addLog("Sistema Inicialitzat correctament","" ,  "J2MESDLIB | generateDB",  J2MELog.ACTION, "");
	}
        
        /**
     * Tanca les taules de metadades. <br/> 
     * És convenient executar-la sempre abans de tancar l'aplicació <br/>
     * que l'utilitzi per tal d'evitar inconsistencies
     */
        public static void closeDB()
        {
            SCHEMA_COLS_Table.closeTable();
            SCHEMA_TABLES_Table.closeTable();
            SCHEMA_COLS_Table = null;
            SCHEMA_TABLES_Table = null;
            DBGenerated = false;
        }
        
       
         /**
     * Crea una nova taula i les entrades corresponents a les taules de
     * metadadades.
     * @param tableName Nom de la nova taula.
     * 
     * @param columns Vector de J2MEColumn amb la definició del atributs de la taula.
     * 
     * @param numKeys Número d'atributs que son clau.
     * 
     * @return Taula creada.
     * 
     * @exception J2MEDBException si la taula ja existia o si el nom de taula 
     * no és correcte (si ens passen un string buït o ens passen el nom de les 
     * taules de metadades) o si la definició de les columnes tampoc es correcta.
     * 
     */
	public static J2METable createTable(String tableName, Vector columns, int numKeys) throws J2MEDBException{

		if (!DBGenerated){
                    //llanÃ§em una excepciÃ³.
                    J2MEDBException excepcio = new J2MEDBException(321);
                    excepcio.addLogException(tableName);
                    throw excepcio;
		}

		if(tableName.trim().equalsIgnoreCase(SCHEMA_TABLES_FILENAME) ||
		   tableName.trim().equalsIgnoreCase(SCHEMA_COLS_FILENAME)){
			//llanÃ§em excepciÃ³: Nom de fitxer incorrecte
                     J2MEDBException excepcio = new J2MEDBException(322);
                     excepcio.addLogException(tableName);
                     throw excepcio;
		}
                else if (tableName.trim().equals("")){
                    J2MEDBException excepcio = new J2MEDBException(323);
                    excepcio.addLogException(tableName);
                    throw (excepcio);
                }
		else if (columns.size() == 0){
			//llanÃ§em excepciÃ³: No hi ha columnes
                    J2MEDBException excepcio =  new J2MEDBException(324);
                    excepcio.addLogException(tableName);
                    throw excepcio;
		}
		else if(existsTable(tableName)){
                    //hem de comprovar si la taula ja hi era a les taules 
                    //de metadades		                    
                   J2MEDBException excepcio = new J2MEDBException(325);
                   excepcio.addLogException(tableName);
                   throw excepcio;
		}

		insertTable(tableName, columns.size(), numKeys); //<-Afegim les entrades a les taules
		insertColumns(tableName, columns);		
                SCHEMA_COLS_Table.saveTable();                  //<-Guardem els canvis 
                SCHEMA_TABLES_Table.saveTable();
		J2METable temp = new J2METable(tableName);	//<-Creem l'RMS
                 if (DBLogs) J2MELog.getLog().addLog("Creada taula " + tableName, "", "J2MESDLIB | createTable", J2MELog.ACTION, tableName);
		return temp;                
	}

       
	/**
     * 
     * Carrega i retorna la taula. <br/>
     * La taula ha d'existir previament.
     * @param tableName Nom de la nova taula que volem carregar.
     * 
     * @return Taula amb nom tableName.
     * @exception J2MEDBException si la taula no existia o si el nom de taula 
     * no és correcte (si ens passen un string buit o ens passen el nom de les 
     * taules de metadades).
     * 
     * 
     */
	public static J2METable loadTable(String tableName) throws J2MEDBException{
		if(!DBGenerated){
                        J2MEDBException excepcio = new J2MEDBException(314);
                        excepcio.addLogException(tableName);
                        throw excepcio;			
		}
		if(tableName.trim().equalsIgnoreCase(SCHEMA_TABLES_FILENAME) ||
		   tableName.trim().equalsIgnoreCase(SCHEMA_COLS_FILENAME)){
			//llanÃ§em excepciÃ³: Nom de fitxer incorrecte
                    J2MEDBException excepcio = new J2MEDBException(315);
                     excepcio.addLogException(tableName);
                    throw excepcio;			                     
		}		
		else if(!existsTable(tableName)){
                    J2MEDBException excepcio = new J2MEDBException(316);
                    excepcio.addLogException(tableName);
                    throw excepcio;			
		}
		
                J2METable temp = new J2METable(tableName); //<-Obre l'RMS i retorna la taula                
		return temp;
	}

	/**
     * Esborra la taula físicament del disc i les seves entrades a les taules 
     * de metadades. <br/>
     * Per tornar a utilitzar la taula s'haurà de fer un createTable().
     * @param tableName Nom de la taula a esborrar.
     * 
     * @return Cert si la taula s'ha pogut esborrar correctament.
     * 
     * @throws J2MEDBException Si no s'ha pogut esborrar la taula.
     */
	public static boolean dropTable(String tableName) throws J2MEDBException{

		boolean bDeleted = false;
		if(!DBGenerated){		
                    J2MEDBException excepcio = new J2MEDBException(317);
                    if(DBLogs) excepcio.addLogException(tableName);
                    throw excepcio;			
		}
		if(tableName.trim().equalsIgnoreCase(SCHEMA_TABLES_FILENAME) ||
		   tableName.trim().equalsIgnoreCase(SCHEMA_COLS_FILENAME)){
			//llanÃ§em excepciÃ³: Nom de fitxer incorrecte
                     J2MEDBException excepcio = new J2MEDBException(318);
                    if(DBLogs) excepcio.addLogException(tableName);
                    throw excepcio;			
		}
                else if (tableName.trim().equals("")){
                    J2MEDBException excepcio = new J2MEDBException(319);
                    if(DBLogs) excepcio.addLogException(tableName);
                    throw excepcio;			
                }	
		else if(!existsTable(tableName)){
                    J2MEDBException excepcio = new J2MEDBException(320);
                    if(DBLogs) excepcio.addLogException(tableName);
                    throw excepcio;			
		}

		J2METable temp = new J2METable(tableName); //<-Obre l'RMS i retorna la taula					
		if(temp.deleteRMS()){
			//El nÃºmero de columnes de la taula es el nÃºmero de files que hem d'esborrar
                        // de la taula de columnes
			deleteCols(tableName, getNumCols(tableName)); 
			deleteTable(tableName);
                        SCHEMA_COLS_Table.saveTable();
                        SCHEMA_TABLES_Table.saveTable();
			bDeleted = true;			
		}
		
                temp = null;
                if(bDeleted && DBLogs) J2MELog.getLog().addLog("Esborrada taula " + tableName, "","J2MESDLIB | dropTable", J2MELog.ACTION, tableName);
		return bDeleted;
	}

	/**
     * Buida el contingut de la taula.
     * @param tableName Nom de la taula.
     * 
     * @return Cert si el procés de buidat acaba correctament, falst altrament.
     * 
     * @throws J2MEDBException Si no s'ha generat correctament la Base de Dades.
     */
	public static boolean flushTable(String tableName) throws J2MEDBException{
		boolean bFlushed = false;		
		if(!DBGenerated){
                    J2MEDBException excepcio = new J2MEDBException(310);
                    excepcio.addLogException(tableName);
                    throw excepcio;			
                }
		if(tableName.trim().equalsIgnoreCase(SCHEMA_TABLES_FILENAME) ||
		   tableName.trim().equalsIgnoreCase(SCHEMA_COLS_FILENAME)){		
                        J2MEDBException excepcio = new J2MEDBException(311);
                        excepcio.addLogException(tableName);
                        throw excepcio;			
		}
                else if (tableName.trim().equals("")){
                    J2MEDBException excepcio = new J2MEDBException(312);
                    excepcio.addLogException(tableName);
                    throw excepcio;			
                }	
		else if(!existsTable(tableName)){
                    J2MEDBException excepcio = new J2MEDBException(313);
                    excepcio.addLogException(tableName);
                    throw excepcio;			
		}

		J2METable temp = new J2METable(tableName); //<-Obre l'RMS i retorna la taula
		bFlushed = temp.deleteRMS();                
                temp = null;
                if(bFlushed && DBLogs) J2MELog.getLog().addLog("Buidada taula " + tableName, "", "J2MESDLIB | flushTable", J2MELog.ACTION, tableName);
		return bFlushed;
	}
        
        
        /**
         * Cambia el nom de la taula
         * @param tableName Nom actual de la taula
         * @param newTableName Nom que volem posar a la taula
         * @exception J2MEDBException si no es pot renombrar la taula
         
        /*public static void renameTable(String tableName, String newTableName) throws J2MEDBException{
            
            if(!DBGenerated){
                throw new J2MEDBException(346); //DB no inicialitzada
            }
            
            if(tableName.toLowerCase().trim().equals(SCHEMA_TABLES_FILENAME) ||
		   tableName.toLowerCase().trim().equals(SCHEMA_COLS_FILENAME)){			
                     throw new J2MEDBException(347);    //no es pot renombrar una taula de l'esquema
            }
            
            if (!existsTable(tableName)){
                throw new J2MEDBException(348); //No existeix la taula a renombrar
            }            
            else if(existsTable(newTableName)){
                throw new J2MEDBException(349); //Ja existeix una taula amb el nom que volem donar-li
            }
            
            renameCols_Table(tableName, newTableName);
            renameTable_Table(tableName, newTableName);
            //renamePDAStore();
        }*/
        
        /**
         * Cambia el nom de la taula a la taula de columnes
         * @param tableName nom de la taula
         * @param newTableName nom que volem posar a la taula
         */
       /* private static void renameCols_Table(String tableName, String newTableName) throws J2MEDBException{
            
            int numCols = getNumCols(tableName);
            J2MEKey key = new J2MEKey();
            
            for(int i=0;i<numCols;i++){
                key.addValueKey(tableName);
		key.addValueKey(String.valueOf(i));			
	
                if(SCHEMA_COLS_Table.seek(key)){
                    J2MERow row = SCHEMA_COLS_Table.read();
                    row.setColValueByName(newTableName, TableName_Name);
                    SCHEMA_COLS_Table.update(row);
                }                
                
                key.clearKey();
             }
        }  */          
        
         /**
         * Cambia el nom de la taula a la taula de taules
         * @param tableName nom de la taula
         * @param newTableName nom que volem posar a la taula
         * @exception J2MEDBException si hi ha algï¿½n error
         */
        /*private static void renameTable_Table(String tableName, String newTableName) throws J2MEDBException{
            J2MEKey key = new J2MEKey();
            key.addValueKey(tableName);
            
            if(SCHEMA_TABLES_Table.seek(key)){
                J2MERow row = SCHEMA_TABLES_Table.read();
                row.setColValueByName(newTableName, TableName_Name);
                SCHEMA_TABLES_Table.update(row);
           }            
        }*/
        
        
         /**
     * Retorna un vector d'Strings amb els noms de totes les taules.
     * @return Vector d'Strings.<br/>
     * Si no hi ha cap taula es retorna un vector de 0 elements
     * 
     * 
     * @exception J2MEDBException si no està  generat el sistema o si hi ha algun error al procés.
     * 
     * 
     */
        public static Vector getTables() throws J2MEDBException{
            
            if(!DBGenerated)
            {
                J2MEDBException excepcio = new J2MEDBException(347);
                excepcio.addLogException("");
                throw excepcio;			
            }
                        
            Vector v = new Vector();
            String name = "";
            
            try{
                if(SCHEMA_TABLES_Table.first()){
                    while(!SCHEMA_TABLES_Table.EOF()){
                         name = SCHEMA_TABLES_Table.read().getColValue(TableName_Column).trim();
                         if(!name.startsWith("SDLIB"))
                            v.addElement(name);
                         SCHEMA_TABLES_Table.next();
                    }
                    
                    //llegim l'ï¿½ltim                    
                    name = SCHEMA_TABLES_Table.read().getColValue(TableName_Column).trim();
                    if(!name.startsWith("SDLIB"))
                        v.addElement(name);
                }
            }
            catch(J2MEDBException ex){
                throw(ex);
            }
            
            return v;
        }        
       
	/**
     * Retorna el numero de columnes de la taula.
     * @param tableName Nom de la taula.
     * 
     * 
     * @return Enter amb el número de columnes.
     */
	protected static int getNumCols(String tableName){
		if(tableName.trim().equalsIgnoreCase(SCHEMA_TABLES_FILENAME))
			return SCHEMA_TABLES_NumColumns;
		if(tableName.trim().equalsIgnoreCase(SCHEMA_COLS_FILENAME))
			return SCHEMA_COLS_NumColumns;
		else{
			//mirem a les metadades el nÃºmero de camps que tÃ©.
			J2MEKey key = new J2MEKey();
			key.addValueKey(tableName);
			if(SCHEMA_TABLES_Table.seek(key)){
				try{
                                  J2MERow row = SCHEMA_TABLES_Table.read();                                       
                                   return Integer.parseInt(row.getColValue(NumCols_Column));
                                }
                                catch(J2MEDBException ex){
                                   return 0;
                                }
			}
			else return 0;
		}
	}

	/**
     * Retorna un vector amb la informació de les columnes de la taula.
     * @param tableName nom de la taula que s'han de llistar les columnes.
     * 
     * 
     * @return Vector de J2ME columns amb la informació de les columnes de la taula.
     * 
     * 
     */
	protected static Vector getColumnsInfo(String tableName){
	                
		if(tableName.trim().equalsIgnoreCase(SCHEMA_TABLES_FILENAME)){                        
			if(SCHEMA_TABLES_Vector==null){
				 createSchemaTablesVector();
			}	
			
			return SCHEMA_TABLES_Vector;
		}
		if(tableName.trim().equalsIgnoreCase(SCHEMA_COLS_FILENAME)){                    
			if(SCHEMA_COLS_Vector==null){
				 createSchemaColumnsVector();
			}	
			
			return SCHEMA_COLS_Vector;
		}
		else{
                        //Estem a una taula d'usuari. Cerquem la info a les taules de metadades
			return createColumnsInfo(tableName);			
		}
	}
        
        /**
     * Retorna un String amb la informació de la taula d'esquema que es passa
     * com a paràmetre.
     * @param tableName Nom de la taula d'esquema que volem.
     * 
     * @return String informació en format per imprimir per pantalla de la taula indicada.
     * 
     */
        public static String printSchemaTables(String tableName){
               if(tableName.equalsIgnoreCase(SCHEMA_TABLES_FILENAME)) return SCHEMA_TABLES_Table.printTable();
               else if(tableName.equalsIgnoreCase(SCHEMA_COLS_FILENAME)) return SCHEMA_COLS_Table.printTable();               
               return "";
        }
        
         /**
	 * Creacio de la taula que conte la definicio de totes les taules del sistema <br/>
         * Si la taula ja existia, nomes obrim l'RMS (aixo ho fa el PDAStore) <br/>
         * Aquesta taula tindraï¿½ la seguent estructura:<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;-SCHEMA_TABLES (tableName(PK), nCols, nKeys)	 
	 */
	private static void createSchemaTables(){
		/*  obrim la taula schema_tables. Si no existeix el PDAStore la crea */
            if (SCHEMA_TABLES_Table == null )
		SCHEMA_TABLES_Table = new J2METable(SCHEMA_TABLES_FILENAME);		            
	}	

	/**
	 * Creaciï¿½ de la taula que conte la definicio de les columnes de cada
	 * taula del sistema <br/>
         * Si la taula ja existia, nomes obrim l'RMS (aixo ho fa el PDAStore) <br/>
	 * La estructura de la taula es la seguent:<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;-SCHEMA_COLS (tableName(PK),idRow(PK),colName,colType,isKey)	
     	 */
	private static void createSchemaColumns(){
            if (SCHEMA_COLS_Table == null )
		SCHEMA_COLS_Table = new J2METable(SCHEMA_COLS_FILENAME);
	}

	/**
	 * Inicialitza el vector que conte la informacio dels atributs de la metataula de taules<br/>
	 * Hi ha les metadades de la taula de taules carregades a memoria.
	 */
	private static void createSchemaTablesVector(){
                
            try{
                J2MEColumn tableName = new J2MEColumn(TableName_Name, true,J2MEColumn.ColumnType.VARCHAR, (J2MEColumn.ColumnType.CHAR_SIZE*TableName_Size));
                J2MEColumn numCols   = new J2MEColumn(NumCols_Name, false, J2MEColumn.ColumnType.INT, J2MEColumn.ColumnType.INT_SIZE);
                J2MEColumn numKeys = new J2MEColumn(NumKeys_Name, false, J2MEColumn.ColumnType.INT, J2MEColumn.ColumnType.INT_SIZE);
                
                SCHEMA_TABLES_Vector = new Vector();
                SCHEMA_TABLES_Vector.addElement(tableName);
                SCHEMA_TABLES_Vector.addElement(numCols);
                SCHEMA_TABLES_Vector.addElement(numKeys);    
            }
            catch(J2MEDBException ex){
                 
            }
	}

	/**
	 * Inicialitza el vector que conte la informacio dels atributs de la metataula de columnes<br/>
	 * Hi ha les metadades de la taula de columnes carregades a memoria.
	 */
	private static void createSchemaColumnsVector(){

            try{
                J2MEColumn tableName = new J2MEColumn(TableName_Name, true,J2MEColumn.ColumnType.VARCHAR, (J2MEColumn.ColumnType.CHAR_SIZE*TableName_Size));
                J2MEColumn idRow   = new J2MEColumn(RowId_Name, true, J2MEColumn.ColumnType.INT, J2MEColumn.ColumnType.INT_SIZE);
                J2MEColumn colName = new J2MEColumn(ColName_Name, false, J2MEColumn.ColumnType.VARCHAR, (J2MEColumn.ColumnType.CHAR_SIZE*ColName_Size));
                J2MEColumn colType = new J2MEColumn(ColType_Name, false, J2MEColumn.ColumnType.VARCHAR, (J2MEColumn.ColumnType.CHAR_SIZE*ColType_Size));
                J2MEColumn isKey   = new J2MEColumn(IsKey_Name, false,J2MEColumn.ColumnType.BOOL, J2MEColumn.ColumnType.BOOL_SIZE);
                J2MEColumn colSize = new J2MEColumn(ColSize_Name, false, J2MEColumn.ColumnType.INT, J2MEColumn.ColumnType.INT_SIZE);

                SCHEMA_COLS_Vector = new Vector();
                SCHEMA_COLS_Vector.addElement(tableName);
                SCHEMA_COLS_Vector.addElement(idRow);
                SCHEMA_COLS_Vector.addElement(colName);
                SCHEMA_COLS_Vector.addElement(colType);
                SCHEMA_COLS_Vector.addElement(isKey);
                SCHEMA_COLS_Vector.addElement(colSize);    
            }
            catch(J2MEDBException ex){
                
            }
	}

	/**
	 * Retorna un vector de J2MEColumns amb les metadades de la taula
         * @param tableName Nom de la taula
         * @return Vector amb la informacio de les columnes de la taula
	 */
	private static Vector createColumnsInfo(String tableName){

		int numCols = getNumCols(tableName);
		
		J2MEKey key = new J2MEKey();

		Vector columsInfo = new Vector();
		
		for(int i=0;i<numCols;i++){

			key.addValueKey(tableName);
			key.addValueKey(String.valueOf(i));			
	
			if(SCHEMA_COLS_Table.seek(key)){
                            try{
				J2MERow row = SCHEMA_COLS_Table.read();
				boolean isKey = row.getColValue(IsKey_Column).trim().equalsIgnoreCase("1");
				J2MEColumn column = new J2MEColumn(
						row.getColValue(ColName_Column),
						isKey,
						row.getColValue(ColType_Column), 
                                                Integer.parseInt(row.getColValue(ColSize_Column)));
				columsInfo.addElement(column);
                            }
                            catch(J2MEDBException ex){
                                
                            }
			}

			key.clearKey();
		}

		return columsInfo;
	}

	/**
	 * Afegeix una nova entrada a la metataula de taules
         * @param fileName Nom de la taula
         * @param nCols Numero de columnes que tindra la taula
         * @param nKeys Numero d'atributs clau
	 */
	private static void insertTable(String fileName, int nCols, int nKeys) throws J2MEDBException{
                
                J2MERow newRow = SCHEMA_TABLES_Table.createRow();		
		newRow.setColValue(fileName, 		TableName_Column);
		newRow.setColValue(String.valueOf(nCols),NumCols_Column); 
		newRow.setColValue(String.valueOf(nKeys),NumKeys_Column); 
		SCHEMA_TABLES_Table.addRow(newRow);
	}

	/**
	 * Afegeix una nova entrada a la taula de columnas per cada objecte
	 * del vector.
         * @param tableName Nom de la taula
         * @param columns Vector amb la informacio de cadascun dels atributs de la taula
	 */
	private static void insertColumns(String tableName, Vector columns) throws J2MEDBException{

            String isKey="";
            for(int i=0;i<columns.size();i++){
			J2MEColumn col = (J2MEColumn) columns.elementAt(i);
			J2MERow newRow = SCHEMA_COLS_Table.createRow();
			newRow.setColValue(tableName, TableName_Column);
			newRow.setColValue(String.valueOf(i), RowId_Column);
			newRow.setColValue(col.getFieldName(), ColName_Column);
			newRow.setColValue(col.getFieldType(), ColType_Column);
                        if(col.isKeyField()) isKey="1";
                        else                 isKey="0";
			newRow.setColValue(isKey, IsKey_Column);
                        newRow.setColValue(String.valueOf(col.getFieldSize()), ColSize_Column);
			SCHEMA_COLS_Table.addRow(newRow);
		}
	}

	/**
	 * Esborra l'entrada de la taula de metadataules
         * @param fileName Nom de la taula
	 */
	private static void deleteTable(String fileName){
		
		J2MEKey key = new J2MEKey();
		key.addValueKey(fileName);	
		if(SCHEMA_TABLES_Table.seek(key)){
                    try{
			SCHEMA_TABLES_Table.delete();
                    }
                    catch(J2MEDBException e){                        
                    }
		}
	}

	/**
	 * Esborra les entrades de la taula a la taula de columnes
         * @param fileName Nom de la taula
         * @param numRows Numero de files que s'han d'esborrar
	 */
	private static void deleteCols(String fileName, int numRows){

		J2MEKey key = new J2MEKey();
		for(int i=0;i<numRows;i++){
			key.addValueKey(fileName);
			key.addValueKey(String.valueOf(i));			
			if(SCHEMA_COLS_Table.seek(key)){
                            try{
				SCHEMA_COLS_Table.delete();				
                            }
                            catch (J2MEDBException e){
                                
                            }
			}
			key.clearKey();
		}                                
	}
        
        /**
     * Comproba si la taula existeix.
     * @param tableName Nom de la taula.
     * 
     * 
     * @return Cert si la taula existeix, fals altrament.
     * 
     */        
        public static boolean existsTable(String tableName){            
            J2MEKey key = new J2MEKey();
            key.addValueKey(tableName);
            return SCHEMA_TABLES_Table.seek(key);
        }
        
        /**
     * Esborra totes les taules i la informació de les metadades i ho deixa
     * tot en l'estat inicial: <br/> només les taules de l'esquema buides.
     */
        public static void regenerarDB(){              
            String table="";
            if(SCHEMA_TABLES_Table.first()){
                try{
                    J2MERow row = SCHEMA_TABLES_Table.read();
                    table =  row.getColValue(TableName_Column);                    
                    loadTable(table).deleteRMS();
                    while(!SCHEMA_TABLES_Table.EOF()){
                        SCHEMA_TABLES_Table.next();
                        table =  SCHEMA_TABLES_Table.read().getColValue(TableName_Column);
                        loadTable(table).deleteRMS();                        
                    }
                }
                catch(J2MEDBException ex){                    
                }
               
               //esborrem els RMS, els tanquem i posem a null les taules
               SCHEMA_COLS_Table.deleteRMS();
               SCHEMA_TABLES_Table.deleteRMS();
               SCHEMA_COLS_Table = null;
               SCHEMA_TABLES_Table = null;
            }
            
            generateDB(DBLogs);            
        }
        
        /**
     * Crea i carrega una J2METable amb les dades contingudes a l'String passat com a paràmetre.
     * @param data String amb les dades per generar la taula.
     * 
     * @return J2METable creada.
     * @exception J2MEDBException Si hi ha algun error al procés.
     */       
        public static J2METable parseToTable (String data) throws J2MEDBException{
            //data = "'TABLEPRUEBA'~'3'~'4'~'idtabla'~'java.lang.Integer'~'Y'~'1'~'nombre'~'java.lang.String'~'Y'~'20'~'apellido'~'java.lang.String'~'Y'~'30'~~'1'~'pepe'~'perez'~~'2'~'a'~'b'~~'3'~'b'~'b'~~'4'~'c'~'c'~~~";
           //data = "'PRUEBA2'~'3'~'5'~'idtabla'~'java.lang.Integer'~'0'~'nombre'~'java.lang.String'~'45'~'apellido'~'java.lang.String'~'45'~~'1'~'pepe'~'perez'~~~'2'~'a'~'b'~~~'3'~'b'~'b'~~~'4'~'c'~'c'~~~'5'~'e'~'e'~~~"; 
           //data = "'PRUEBA2'~'2'~'5'~'idcliente'~'java.lang.Integer'~'0'~'nombreCliente'~'java.lang.String'~'45'~~'1'~'pepe'~~~'2'~'a'~~~'3'~'b'~~~'4'~'c'~~~'5'~'e'~~~"; 
            
            if (!DBGenerated){
                J2MEDBException excepcio = new J2MEDBException(346);
                excepcio.addLogException("");
                throw excepcio;	
            }   
            
            String dataAux = data;
            String idConnect;
            int num_cols, num_rows;
            String colName, colType, keyYN;
            boolean isKey;
            int col_tam, numKeys = 0;
            Vector columns = null;            
            J2METable temp = null;
            
            try{
            /* Bloque de extracciÃ³n de idConnect, num_cols, num_rows */
                idConnect = dataAux.substring(1, dataAux.indexOf("~")-1);
                dataAux = dataAux.substring(idConnect.length()+3);
                //System.out.println(dataAux);
            
                num_cols = Integer.parseInt(dataAux.substring(1, dataAux.indexOf("~")-1));
                dataAux = dataAux.substring(String.valueOf(num_cols).length()+3);
                //System.out.println(dataAux);
            
                num_rows = Integer.parseInt(dataAux.substring(1, dataAux.indexOf("~")-1));
                dataAux = dataAux.substring(String.valueOf(num_rows).length()+3);
                //System.out.println(dataAux);
            }
            catch(Exception e){
                J2MEDBException excepcio = new J2MEDBException(326);
                excepcio.addLogException("");
                throw excepcio;	
            }
            
            try{
                columns = new Vector();
                /*ExtracciÃ³ de la informaciÃ³ de les columnes*/
                for (int i=0; i<num_cols; i++){
                    //llegir nom columna
                    colName = dataAux.substring(1, dataAux.indexOf("~")-1);
                    dataAux = dataAux.substring(colName.length()+3);
                    //llegir tipus columna
                    colType = dataAux.substring(1, dataAux.indexOf("~")-1);
                    dataAux = dataAux.substring(colType.length()+3);
                   /* //llegir isKey
                    keyYN = dataAux.substring(1, dataAux.indexOf("~")-1);
                    dataAux = dataAux.substring(keyYN.length()+3);
                    isKey = keyYN.equals("Y");

                    if (isKey) numKeys++;*/
                    //llegir colTam
                    col_tam = Integer.parseInt(dataAux.substring(1, dataAux.indexOf("~")-1));
                    dataAux = dataAux.substring(String.valueOf(col_tam).length()+3);

                    try{
                        if(getType(colType).equals(J2MEColumn.ColumnType.VARCHAR))
                            col_tam=col_tam*J2MEColumn.ColumnType.CHAR_SIZE;
                        else
                            col_tam=J2MEColumn.ColumnType.getSize(getType(colType));
                            
                        J2MEColumn column = new J2MEColumn(colName, false, getType(colType), col_tam);
                        columns.addElement(column);                        
                    }
                    catch(J2MEDBException ex){
                        if (301 == ex.getCode()){
                            J2MEDBException excepcio = new J2MEDBException(327);
                            excepcio.addLogException("");
                            throw excepcio;
                        }
                        else {
                            J2MEDBException excepcio = new J2MEDBException(328);
                            excepcio.addLogException("");
                            throw excepcio;	
                        }   
                    }                
                }
            }
           catch(J2MEDBException j2ex){
               throw j2ex;
           }
            catch(Exception ex){
                J2MEDBException excepcio = new J2MEDBException(326);
                excepcio.addLogException("");
                throw excepcio;	                
            }            
            
            try{
                if(existsTable(TEMP_TABLE_FILENAME)){
                    //System.out.println("La tabla " + TEMP_TABLE_FILENAME + " ya existe. La borramos");
                    dropTable(TEMP_TABLE_FILENAME);
                }
                else
                {
                    System.out.println("La tabla " + TEMP_TABLE_FILENAME + " NO existe.");
                }
                temp = createTable(TEMP_TABLE_FILENAME, columns, numKeys);
            }
            catch(J2MEDBException ex){
                throw (ex);
            }
            
            if(temp!=null){
                String fieldValue="";
                J2MERow row=null;
                /*Informacio de les files*/
                for(int i=0; i<num_rows;i++){
                    dataAux = dataAux.substring(1);
                    row = temp.createRow();
                    for(int j=0;j<num_cols;j++){                        
                        fieldValue = dataAux.substring(1, dataAux.indexOf("~")-1);
                        dataAux = dataAux.substring(fieldValue.length()+3);
                        try{
                            row.setColValue(fieldValue, j);
                        }
                        catch(J2MEDBException ex){ 
                            if(ex.getCode()==307){
                                J2MEDBException excepcio = new J2MEDBException(331);
                                excepcio.addLogException("");
                                throw excepcio;	
                            }                            
                            else{
                                J2MEDBException excepcio = new J2MEDBException(329);
                                excepcio.addLogException("");
                                throw excepcio;	
                            }
                        }
                    }
                    
                    try{
                        temp.addRow(row);
                        temp.saveTable();
                    }
                    catch(J2MEDBException ex){
                        if(ex.getCode()!=329 && ex.getCode()!=332){
                            J2MEDBException excepcio = new J2MEDBException(330);
                            excepcio.addLogException("");
                            throw excepcio;	
                        }
                        else{                            
                            throw (ex);
                        }                                                   
                    }
                    
                    dataAux = dataAux.substring(1);
                }
            }
            
            if(DBLogs){
                String info="";
                    if(data.length()>255)
                        info = data.substring(0, J2MEColumn.ColumnType.MAX_COLUMN_SIZE);
                    else
                        info = data;
                J2MELog.getLog().addLog("Generada taula " + idConnect + " a partir d'una Query remota", "Data: " + info ,"J2MESDLIB | parseToTable", J2MELog.ACTION, "");
            }
            return temp;
        }
        
        /**
     * Retorna un string amb la SQL que s'hauria de generar per crear la taula i 
     * afegir les files a un SGBD.
     * @param tableName Nom de la taula.
     * 
     * @return String amb la sentència SQL
     * @exception J2MEDBException Si hi ha errors en el procés.
     */        
        public static String getSQLDump(String tableName) throws J2MEDBException{
            String sentenciaSQL="";
            
            if( ! DBGenerated ){
                J2MEDBException excepcio = new J2MEDBException(345);
                excepcio.addLogException(tableName);
                throw excepcio;	                
            }
            
            if ( tableName.equalsIgnoreCase(J2MESDLIB.SCHEMA_TABLES_FILENAME) || tableName.equalsIgnoreCase(J2MESDLIB.SCHEMA_COLS_FILENAME)){
                J2MEDBException excepcio = new J2MEDBException(344);
                excepcio.addLogException(tableName);
                throw excepcio;	
            }
            
            if ( ! existsTable(tableName) ){
                J2MEDBException excepcio = new J2MEDBException(340);
                excepcio.addLogException(tableName);
                throw excepcio;	
            }                

            try{
                sentenciaSQL = getSQLCapcalera(tableName);
                sentenciaSQL += getSQLInserts(tableName);
            }
            catch (J2MEDBException j2ex){
                throw j2ex;
            }
            
            return sentenciaSQL;
        }
        
        /**
         * Retorna el CREATE TABLE generat per a la capï¿½alera de la taula
         * @param tableName Nom de la taula
         * @return SQL de les capï¿½alera
         */
        private static String getSQLCapcalera(String tableName) throws J2MEDBException{
            String sentenciaSQL;
            int numCols;
            J2MEKey key;
            J2MERow rowTable, rowColumn;
            String colName, colType, isKey, colSize;
            
            sentenciaSQL = "CREATE TABLE "+tableName+"(\n";
            key = new J2MEKey();
            key.addValueKey(tableName);
            
            try{
                if (!SCHEMA_TABLES_Table.seek(key))
                    throw new J2MEDBException (341); //Error en el seek de la taula
                numCols = Integer.parseInt(SCHEMA_TABLES_Table.read().getColValue(NumCols_Column));
                key = new J2MEKey();
                key.addValueKey(tableName);
                key.addValueKey("0");
                if ( !SCHEMA_COLS_Table.seek(key) ){
                    J2MEDBException excepcio = new J2MEDBException(342);
                    excepcio.addLogException(tableName);
                    throw excepcio;	                    
                }
                                        
                for (int i=0; i<numCols; i++){
                    rowColumn = SCHEMA_COLS_Table.read();
                    colName = rowColumn.getColValue(ColName_Column);
                    colType = rowColumn.getColValue(ColType_Column);
                    isKey   = rowColumn.getColValue(IsKey_Column);
                    colSize = rowColumn.getColValue(ColSize_Column);
                    
                    sentenciaSQL += colName+" "+colType;
                    if ( colType.equals(J2MEColumn.ColumnType.VARCHAR) )
                        sentenciaSQL += " ("+colSize+")";
                    if ( isKey.equals("1"))
                        sentenciaSQL += " PRIMARY KEY";
                    if (i+1 != numCols){
                        sentenciaSQL += ",\n";
                        try{
                            if ( !SCHEMA_COLS_Table.next()){
                                J2MEDBException excepcio = new J2MEDBException(343);
                                excepcio.addLogException(tableName);
                                throw excepcio;	
                            }                                
                        }
                        catch (Exception e){
                            J2MEDBException excepcio = new J2MEDBException(0);
                            excepcio.addLogException(tableName);
                        }
                    }
                }
                sentenciaSQL += ");\n\n"; 
            }
            catch (J2MEDBException j2ex){
                throw j2ex;
            }
            
            catch (Exception e){
                J2MEDBException excepcio = new J2MEDBException(0);
                excepcio.addLogException(tableName);
            }           
            
            return sentenciaSQL;
        }
        
        /**
         * Retorna el INSERTS per cada una de les files de la taula
         */
        private static String getSQLInserts(String tableName) throws J2MEDBException{
            String sentenciaSQL = "";
            J2METable taula;
            J2MERow fila;
            int numCols;
            
            try{
                taula = loadTable(tableName);
                numCols = taula.getNumCols();
                
                if (taula.first()){
                    do{
                        sentenciaSQL += "INSERT INTO "+tableName+"(";
                        fila = taula.read();
                        for (int i=0; i<numCols; i++){
                            sentenciaSQL += fila.getColValue(i);
                            if ( (i+1)!=numCols )
                                sentenciaSQL += ", ";
                        }
                        sentenciaSQL += ");\n";
                        
                        taula.next();
                    }
                    while ( !taula.EOF());
                    
                    //llegim l'Ãºltim
                    sentenciaSQL += "INSERT INTO "+tableName+"(";
                        fila = taula.read();
                        for (int i=0; i<numCols; i++){
                            sentenciaSQL += fila.getColValue(i);
                            if ( (i+1)!=numCols )
                                sentenciaSQL += ", ";
                        }
                        sentenciaSQL += ");\n";
                }
                
                 taula.closeTable();
            }
            catch (J2MEDBException j2ex){                
                throw j2ex;
            }            
            
            return sentenciaSQL;
        }
        /**
         * Retorna l'String amb el tipus de dades que admet la API a 
         * partir del tiups de dades java
         * @param javaType String amb el tipus de dada del servidor (java.lang.String,...);
         * @return String amb un dels tipus de dades admesos per la API (BOOL, INT, VARCHAR, DOUBLE)
         */
        private static String getType(String javaType){
                if(javaType.equals("java.lang.String")){
                        return J2MEColumn.ColumnType.VARCHAR;
                }
                if(javaType.equals("java.lang.Integer")){                        
                        return J2MEColumn.ColumnType.INT;
                }
                if(javaType.equals("java.lang.Double")){
                        return J2MEColumn.ColumnType.DOUBLE;
                }
                if(javaType.equals("java.lang.Boolean")){
                        return J2MEColumn.ColumnType.BOOL;
                }
                else return "";
         }
        
        /**
     * Dona el nom de totes les taules de log.
     * @return Vector amb el nom de tots els arxius de log.
     * 
     * @throws J2MEDBException Si hi ha algun error per obtenir els arxius.
     */        
        protected static Vector getLogs() throws J2MEDBException {
                   
            if(!DBGenerated){
                J2MEDBException excepcio = new J2MEDBException(348);
                excepcio.addLogException("");
                throw excepcio;	
            }
                        
            Vector v = new Vector();
            String name = "";
            
            try{
                if(SCHEMA_TABLES_Table.first()){
                    while(!SCHEMA_TABLES_Table.EOF()){
                         name = SCHEMA_TABLES_Table.read().getColValue(TableName_Column).trim();
                         if(name.startsWith(J2MELog.logDefaultName))
                            v.addElement(name);
                         SCHEMA_TABLES_Table.next();
                    }
                    
                    //llegim l'ï¿½ltim
                    name = SCHEMA_TABLES_Table.read().getColValue(TableName_Column).trim();
                    if(name.startsWith(J2MELog.logDefaultName))
                        v.addElement(name);
                }
            }
            catch(J2MEDBException ex){
                throw(ex);
            }
            
            
            return v;   
        }
}
