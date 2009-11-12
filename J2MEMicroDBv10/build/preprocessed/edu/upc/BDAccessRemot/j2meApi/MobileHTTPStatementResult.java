/*
 * MobileHTTPStatementResult.java
 *
 * Created on 3 de marzo de 2007, 13:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.upc.BDAccessRemot.j2meApi;

import edu.upc.J2MESDLIB.J2MEColumn;
import edu.upc.J2MESDLIB.J2MEDBException;
import edu.upc.J2MESDLIB.J2MERow;
import edu.upc.J2MESDLIB.J2MESDLIB;
import edu.upc.J2MESDLIB.J2METable;
import java.util.Vector;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * This class represents the result of a SQL statement. It could be a select result, a write result or an error result. The status atribute will tell if the result is not ready, ready or ready but an error.
 * If the statement is a select it is possible to create a J2ME table called SDLIB_temp_table that contains a materialized view of the result of the statement.
 * @author Pelsman
 */
public class MobileHTTPStatementResult{ //extends MIDlet{
    
    
    /**
     * Status of MobileHTTPStatementResult that means that the result is not ready yet.
     */
    public static final int WAITING=0;
    /**
     * Status of MobileHTTPStatementResult that means that the result is ready.
     */
    public static final int OK = 1;
    /**
     * Status of MobileHTTPStatementResult that means that the result is an error.
     */
    public static final int KO = 2;
    
    private int status;
    private int type;
    private String errorMessage;
    private String statment;
    private int cols;
    private int rows;
    private String[] colNames = null;
    private String[] colTypes = null;
    private int[] colLength = null;
    private String[][] stringResult = null;
    
    private String J2METableString;
    
    /**
     * Constructs a new instance of MobileHTTPStatementResult with the specified SQL statement. By default, the status of this MobileHTTPStatementResult is WAITING.
     * @param statement SQL statement associated to the result.
     */
    public MobileHTTPStatementResult(String statement) {
        this.statment = statement.toString();
        this.type = -1;
        this.status = WAITING;
        this.errorMessage = "";
        createVoidResult();
    }
    
    /**
     * Sets the result of this MobileHTTPStatementResult instance.
     * @param result The result that is going to be associated to this MobileHTTPStatementResult instance.
     * @param type Type of the result:
     * 3-Select
     * 4-Write
     * 5-Error
     */
    public void setResult(String result,int type){
        this.type = type;
        switch(type){
            case 3: //Select
                resultSelectProcess(result);
                //generateJ2METable();
                this.status = OK;
                break;
            case 4: //Write
                resultWriteProcess(result);
                this.status = OK;
                break;
            case 5: //Error
                this.errorMessage = result;
                createVoidResult();
                this.status = KO;
                break;
            default:
                break;
        }
    }
    
    /**
     * Returns the statement associated to this MobileHTTPStatementResult instance.
     * @return The statement associated to this MobileHTTPStatementResult instance.
     */
    public String getStatment(){
        return statment.toString();
    }
    
    /**
     * Returns the number of columns of this MobileHTTPStatementResult instance.
     * @return The number of columns of this MobileHTTPStatementResult instance.
     */
    public int getCols(){
        return this.cols;
    }
    
    /**
     * Returns the number of rows of this MobileHTTPStatementResult instance.
     * @return The number of rows of this MobileHTTPStatementResult instance.
     */
    public int getRows(){
        return this.rows;
    }
    
    /**
     * Tests whether this MobileHTTPStatementResult is a select or not.
     * @return True if this MobileHTTPStatementResult is a select, False otherwise.
     */
    public boolean isSelect(){
        if(this.type == 3){
            return true;
        }
        else return false;
    }
    
    /**
     * Tests whether this MobileHTTPStatementResult is a write or not.
     * @return True if this MobileHTTPStatementResult is a write, False otherwise.
     */
    public boolean isWrite(){
        if(this.type == 4){
            return true;
        }
        else return false;
    }
    
    /**
     * Returns the status of this MobileHTTPStatementResult instance (WAITING, OK or KO).
     * @return The status of this MobileHTTPStatementResult instance (WAITING, OK or KO).
     */
    public int getStatus(){
        return this.status;
    }
    
    /**
     * In case that this MobileHTTPStatementResult instance is an error, returns the error message.
     * @return The error message of this MobileHTTPStatementResult instance.
     */
    public String getErrorMessage(){
        return errorMessage.toString();
    }
    
    /**
     * If this MobileHTTPStatementResult instance is a result, returns the result in a string matrix format. If it is not a select, returns an empty string matrix of 1 row and 1 column.
     * @return The result of this MobileHTTPStatementResult instance in a string matrix format.
     */
    public String[][] getStringResult(){
        return stringResult;
    }
        
    /**
     * If this MobileHTTPStatementResult instance is a select, returns a vector with the column names. If it is not, an empty vector od 1 position.
     * @return A vector with the column names of this MobileHTTPStatementResult instance.
     */
    public String[] getColumnNames(){
        return colNames;
    }
    
    /**
     * If this MobileHTTPStatementResult instance is a select, returns a vector with the column types. If it is not, returns an empty vector of 1 position.
     * @return A vector with the column types of this MobileHTTPStatementResult instance.
     */
    public String[] getColumnTypes(){
        return colTypes;
    }
    
    /**
     * If this MobileHTTPStatementResult instance is a select, returns a vector with the column length of each column. If it is not, returns an empty vector of 1 position.
     * @return A vector with the column length of each length of this MobileHTTPStatementResult instance.
     */
    public int[] getColumnLength(){
        return colLength;
    }
    
    private void resultWriteProcess(String result){
        StringBuffer aux = new StringBuffer();
        String current = result.trim();
        int i=0;
        int j=0;
        for(j=0;j<2;j++){
            while(current.charAt(i)!='~'){
                i++;
            }
            i++;
        }
        while(i<current.length()){
            aux.append(current.charAt(i));
            i++;
        }
        i++;
        createVoidResult();
        rows = Integer.valueOf(aux.toString()).intValue();
    }
    
    private void resultSelectProcess(String result){
        StringBuffer aux = new StringBuffer();
        String current = result.trim();
        int auxcols,auxrows;
        int i=0;
        int j=0;
        for(j=0;j<2;j++){
            while(current.charAt(i)!='~'){
                i++;
            }
            i++;
        }
        
        J2METableString = current.substring(i);
        
        /*try{
            generateJ2METable(current.substring(i));
        }
        catch(J2MEDBException e){
            
        }*/
        
        
        i++;
        while(current.charAt(i)!='\''){
            aux.append(current.charAt(i));
            i++;
        }
        
        i = i + 3;
        
        cols = Integer.valueOf(aux.toString()).intValue();
        
        aux.delete(0,aux.capacity());        
        while(current.charAt(i)!='\''){
            aux.append(current.charAt(i));
            i++;
        }
        i = i + 3;
        rows = Integer.valueOf(aux.toString()).intValue();
        
        if(cols == 0) auxcols = 1;
        else auxcols = cols;
        
        if(rows == 0) auxrows = 1;
        else auxrows = rows;
        
        colNames = new String[auxcols];
        colTypes = new String[auxcols];
        colLength = new int[auxcols];
        stringResult = new String[auxrows][auxcols];
        
        for (j=0;j<cols;j++){
            aux.delete(0,aux.capacity());        
            while(current.charAt(i)!='\''){
                aux.append(current.charAt(i));
                i++;
            }
            i = i + 3;

            colNames[j] = aux.toString();
            
            aux.delete(0,aux.capacity());        
            while(current.charAt(i)!='\''){
                aux.append(current.charAt(i));
                i++;
            }
            i = i + 3;
            
            colTypes[j] = aux.toString();
            
            aux.delete(0,aux.capacity());        
            while(current.charAt(i)!='\''){
                aux.append(current.charAt(i));
                i++;
            }
            i = i + 3;
            
            
            colLength[j] = Integer.valueOf(aux.toString()).intValue();
        }
        
        i++;        
        
        
        for(j=0;j<rows;j++){
            
            for(int k=0;k<cols;k++){
                aux.delete(0,aux.capacity());

                while(current.charAt(i)!='\''){
                    aux.append(current.charAt(i));
                    i++;
                }
                i = i + 3;
                stringResult[j][k] = aux.toString();
            }
            
            i = i + 2;
        }
        
        /*for(j=0;j<rows;j++){
            for(int k=0;k<cols;k++){
                System.out.print(StringResult[k][j]+" ");
            }
            System.out.println();
        }*/
        
        
    }
    
    /**
     * Generates a J2METable called SDLIB_temp_table that contains a materialized view of the result of the statement.
     * @throws edu.upc.J2MESDLIB.J2MEDBException Thrown if there's a problem creating the J2METable
     */
    public void generateJ2METable() throws J2MEDBException{
        
        J2MESDLIB.generateDB(false);
        J2METable t = J2MESDLIB.parseToTable("'RESULT'~"+J2METableString);
        t.closeTable();
        J2MESDLIB.closeDB();
        
    }

    private String translateType(String type){
        String res = "";
        if(type.equals("java.lang.String")){
            res = J2MEColumn.ColumnType.VARCHAR;
        }
        else if(type.equals("java.lang.Integer")){
            res = J2MEColumn.ColumnType.INT;
        }
        else if(type.equals("java.lang.Double")){
            res = J2MEColumn.ColumnType.DOUBLE;
        }
        else if(type.equals("java.lang.Boolean")){
            res = J2MEColumn.ColumnType.BOOL;
        }
        else{
            res = J2MEColumn.ColumnType.VARCHAR;
        }
        return res;
    }
    
    private int dataSize(String type, int size){
        if(type.equals(J2MEColumn.ColumnType.VARCHAR)){
            return J2MEColumn.ColumnType.CHAR_SIZE*size;
        }
        else if(type.equals(J2MEColumn.ColumnType.BOOL)){
            return J2MEColumn.ColumnType.BOOL_SIZE*size;
        }
        else if(type.equals(J2MEColumn.ColumnType.DOUBLE)){
            return J2MEColumn.ColumnType.DOUBLE_SIZE*size;
        }
        else if(type.equals(J2MEColumn.ColumnType.INT)){
            return J2MEColumn.ColumnType.INT_SIZE*size;
        }
        else{
            return J2MEColumn.ColumnType.CHAR_SIZE*size;
        }
    }
    
    private void createVoidResult(){
        rows = 0;
        cols = 0;
        stringResult = new String[1][1];
        colNames = new String[1];
        colTypes = new String[1];
        colLength = new int[1];
    }

}
