/*
 * ManageContacts.java
 *
 * Created on 5 de septiembre de 2006, 13:07
 *
 * 
 */

/**
 *
 * @author Mª José Casany
 */
package edu.upc.J2MESDLIB.test;

import edu.upc.J2MESDLIB.*;
import java.util.Vector;
import javax.microedition.lcdui.*;

public class ManageContacts extends J2MESDLIB{
    
    /** Creates a new instance of ManageContacts*/
    public ManageContacts() {
        
    }
    
    // This method creates a new table in the database to store
    //contacts data. The contacts table has two fields: name and
    //phone. Each row is identified by name
    public J2METable CreateContacts() throws J2MEDBException{
        J2MESDLIB.generateDB(false);  //don't generete debug information
        J2MEColumn c1=new J2MEColumn("name",true,J2MEColumn.ColumnType.VARCHAR,J2MEColumn.ColumnType.CHAR_SIZE*20);
        J2MEColumn c2=new J2MEColumn("telephone",false,J2MEColumn.ColumnType.VARCHAR,J2MEColumn.ColumnType.CHAR_SIZE*15);
        Vector columnes=new Vector ();
        columnes.addElement(c1);
        columnes.addElement(c2);
        J2METable t=J2MESDLIB.createTable("contactsData",columnes,1);
        
        return t;
    }
    
    //This method adds a new contact to the table
    public void NewContact(String name, String phone) throws J2MEDBException{
        J2METable t=null;
        
        J2MESDLIB.generateDB(false);
          
        //If the table does not exist we create it.  
        if (J2MESDLIB.existsTable("contactsData")){
           t=J2MESDLIB.loadTable("contactsData");
        }else                 
            t=CreateContacts();
            
        //add a new empty row to the table
        J2MERow row=t.createRow();
        //add values to the new row
        row.setColValue(name,0);
        row.setColValue(phone,1);
        // add row to the table
        t.addRow(row);
        //save changes and close table and database
        t.saveTable();
        t.closeTable();
        J2MESDLIB.closeDB(); 
            
    }
    
   //This method returns all the contacts in the table called
   //contacts in a vector 
   public Vector getContacts() throws J2MEDBException{  
       J2METable t=null;
       boolean fi=false;
       Vector result=new Vector();
       
       J2MESDLIB.generateDB(false);
       //If the table exists in database load data   
       if (J2MESDLIB.existsTable("contactsData")){
           t=J2MESDLIB.loadTable("contactsData"); 
           if (t.rowCount()>0){
                fi=t.first();
                //Read first row of the table
                J2MERow row=t.read();
                //add row to result
                result.addElement(row);
                while ((fi=t.EOF())==false){
                      fi=t.next();
                      row=t.read();
                      result.addElement(row);
                 }
            }
            //Close table and database
            t.closeTable();
            J2MESDLIB.closeDB(); 
            //return all the rows of the table in a vector
            return result;
         } else 
              return null;        
   }

   //This method retrieves a single contact from the table identified by
   //name   
   public J2MERow getContact(String name) throws J2MEDBException{
       J2METable t=null;
       J2MERow row=null;
       
       J2MESDLIB.generateDB(false);   
       //If table exists load it
       if (J2MESDLIB.existsTable("contactsData")){
          t=J2MESDLIB.loadTable("contactsData"); 
          if (t.rowCount()>0){
          	   //Create a new empty object of the J2MEKey class 
               J2MEKey key=new J2MEKey();
               //add the name of the contact to seach in the table 
               key.addValueKey(name);
               //search row in a table by key (name)
               if (t.seek(key)){
               	   //If the contact identified by name exists read data
                   row=t.read();   
               }
           }
         }      
         //close table and database
         t.closeTable();
         J2MESDLIB.closeDB(); 
         return row;
   }
   
   //This method deletes the contact identified by name
   public void deleteContact(String name) throws J2MEDBException{
       J2METable t=null;
      
       J2MESDLIB.generateDB(false);   
       //If the table exists load it
       if (J2MESDLIB.existsTable("contactsData")){
            t=J2MESDLIB.loadTable("contactsData"); 
            //create a new empty J2MEKey object
            J2MEKey key=new J2MEKey();
            //add the name of the contact to find
            key.addValueKey(name);
            //search the contact in the table by name
            if (t.seek(key)) 
                 //if found delete the contact
                 t.delete();
            }
            //close table and database        
            t.closeTable();
            J2MESDLIB.closeDB(); 
   }
   
   public void uptadeContact(String name, String phone)throws J2MEDBException{
       J2METable t=null;
      
       J2MESDLIB.generateDB(false);   
       //If the table exists load it
       if (J2MESDLIB.existsTable("contactsData")){
            t=J2MESDLIB.loadTable("contactsData"); 
            //create a new empty J2MEKey object
            J2MEKey key=new J2MEKey();
            //add the name of the contact to find
            key.addValueKey(name);
            //search the contact in the table by name
            if (t.seek(key)){
                //if found read contact data
                J2MERow row=t.read();
                //add values to the new row
                row.setColValue(phone,1);
                //update the current row of the table with the values stored in var. row
                t.update(row);
                //save changes
                t.saveTable();
            }
            //close table and database        
            t.closeTable();
            J2MESDLIB.closeDB(); 
       }    
   }
   
   public String getName(J2MERow fila) throws J2MEDBException{
   	   //Return the name of the contact (column 0 of the table contacts)
       return fila.getColValue(0);
   }
   
   public String getTelephone(J2MERow fila) throws J2MEDBException{
   	   //Return the phone number of the contact (colun 1 of the table)
       return fila.getColValue(1);
   }
   
   public void dropContacts() throws J2MEDBException{
        J2MESDLIB.generateDB(false);
       J2MESDLIB.dropTable("contactsData");
   }
   
   
   
   }
