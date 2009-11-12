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

import java.util.Date;
import javax.microedition.lcdui.DateField;
import java.util.Vector;

/**
 * Proporciona mètodes per guardar informació a una taula
 * dels esdeveniments i excepcions que succeeixen a la API<br/>
 * Cada dia es genera una nova taula de Log, que té una vigència de 7 dies, 
 * moment en el qual s'esborra.<br/>
 * El programador pot accedir a les taules de
 * logs com si fossin taules creades per ell i manipular-les al seu gust.
 * @author Juan Antonio Sosa && Mariona Fecundez
 */
public final class J2MELog {
    
    
    protected static int LogID_Column = 0;
    protected static int LogText_Column = 1;
    protected static int LogInfo_Column = 2;
    protected static int LogSource_Column = 3;
    protected static int LogDate_Column = 4;
    protected static int LogTypeColumn = 5;
    
    protected static String logDefaultName = "SDLIB_Log_";    
    
    protected static int ACTION = 0;
    protected static int EXCEPTION = 1;
    
    /** Singleton de la classe J2MELog */
    private static J2MELog log;
    
    /** Objecte utilitzat per la sincronitzaciÃ³ */
    private static Object syncObject = new Object();
    
    /** Taula  de log */
    private J2METable logTable = null;
    
    /**
     * Retorna la única instància del LOG disponible.
     * @return J2MELog amb el Log disponible.
     */
    protected static J2MELog getLog(){
        
        synchronized(syncObject){
            if (log == null )
                log = new J2MELog();
            return log;
        }
    }
    /**
     * Afegeix un nou registre a la taula de logs del dia actual.<br/>
     * Si no existeix una taula de logs per la data actual, la crea.<br/>
     * @param text Descripció de l'event.
     * 
     * @param info Informació adicional de l'event de Log
     * 
     * @param source Classe i funció on es genera l'event de log (Class | Func).
     * 
     * @param logType Enter que indica si l'event es una acció o s'ha produït per alguna excepció.
     * 
     * @param tableName Indica la taula sobre la que es treballava quan es genera l'event de Log, pot ser buït.
     */
    protected void addLog(String text, String info, String source,  int logType, String tableName){        
        
        System.out.println("LOG-> Text: " + text + "; Source: " + source + "; Table: " + tableName);
        if(tableName.startsWith("SDLIB")){ 
            System.out.println("Taula de sistema: " + tableName);
            return; //si es una taula de sistema no guardem logs
        }
        
        String todayLogName = logDefaultName + getTodayDate();
        
       // if(logTable==null || !todayLogName.equals(logTable.tableName))
        logTable  = createLogTable(todayLogName);
        
        if (logTable != null ){
            J2MERow row = logTable.createRow();
            
            try{
                row.setColValue(paddString(String.valueOf(logTable.rowCount()), 4, "0"), 0);
                row.setColValue(text, 1);
                row.setColValue(info, 2);
                row.setColValue(source, 3);
                row.setColValue(new Date().toString(), 4);
                row.setColValue(String.valueOf(logType), 5);
                
                logTable.addRow(row);                
                logTable.closeTable();
            }
            catch(J2MEDBException ex){
                ex.print();
            }            
        } 
    }
    
    /**
     * Esborra totes les taules de log que fa més de 7 dies
     * que es van crear.
     */
    protected void deleteOld(){
        
    }
    
    /**
     * Retorna un string amb la data d'avuÃ­ formatejada de 
     * la segÃ¼ent manera:<br/>
     *      yyyymmmdd<br/>
     * yyyy - l'Any en 4 dÃ­gits<br>
     * mmm  - El mes en 3 lletres (..., Apr, May, Jun...) <br/>
     * dd   - NÃºmero de dia en 2 digits
     *
     */
    private String getTodayDate(){
       
        String year, month, day, date;
        date = new Date().toString();
        
        year  = date.substring(date.length()-4);
        month = date.substring(4,7);
        day   = date.substring(8,10);        
       
        return year + month + day;
    }
    
    /**
     * Afegeix 0's al principi de l'string fins a obtenir 
     * la longitud indicada
     * @param string String al qual volem afegir els valors en blanc
     * @param length Longitud final que volguem que tingui l'String
     * @return L'string formatejat
     */
    private String paddString(String string, int length, String c){
        while(string.length()<length){
            string=c+string;            
        }            
        return string;
    }
    
    
    /**
     * Crea i retorna un vector amb les columnes de la taula de Log
     */
    private Vector getLogColums() throws J2MEDBException{
        
        Vector v = new Vector(); 
        J2MEColumn logIDColumn      = new J2MEColumn("logID",    true , J2MEColumn.ColumnType.INT, J2MEColumn.ColumnType.getSize(J2MEColumn.ColumnType.INT));
        J2MEColumn logTextColumn    = new J2MEColumn("logText",  false, J2MEColumn.ColumnType.VARCHAR, J2MEColumn.ColumnType.MAX_COLUMN_SIZE);
        J2MEColumn logInfo          = new J2MEColumn("logInfo" , false, J2MEColumn.ColumnType.VARCHAR, J2MEColumn.ColumnType.MAX_COLUMN_SIZE);
        J2MEColumn logSourceColumn  = new J2MEColumn("logSource",false, J2MEColumn.ColumnType.VARCHAR, J2MEColumn.ColumnType.MAX_COLUMN_SIZE);
        J2MEColumn logDateColumn    = new J2MEColumn("logDate",  false, J2MEColumn.ColumnType.VARCHAR, 30 );
        J2MEColumn logTypeColumn    = new J2MEColumn("logType",  false, J2MEColumn.ColumnType.VARCHAR, J2MEColumn.ColumnType.getSize(J2MEColumn.ColumnType.VARCHAR));
        
        v.addElement(logIDColumn);
        v.addElement(logTextColumn);
        v.addElement(logInfo);
        v.addElement(logSourceColumn);
        v.addElement(logDateColumn);
        v.addElement(logTypeColumn);
        
        return v;
    }
    
    private J2METable createLogTable(String logTableName){
        
        J2METable logTable = null;
        
        try{
            if(J2MESDLIB.existsTable(logTableName)){
                System.out.println("Carreguem la taula de LOG");
                logTable = J2MESDLIB.loadTable(logTableName);
            }
            else{
               System.out.println("No existeix la taula de LOG, la creem");
               logTable = J2MESDLIB.createTable(logTableName , getLogColums(), 1); 
            }            
        }
        catch(J2MEDBException ex){
                ex.print();            
        }
        
        return logTable;
    }
}
