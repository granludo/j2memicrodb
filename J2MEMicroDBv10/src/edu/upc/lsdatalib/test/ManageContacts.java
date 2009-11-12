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
 * 02110-1301  USA. It is also //currently //available at http://www.gnu.org/licenses/gpl.txt
 *
 * Authors:  Marc Alier, Maria José Casany, UPC
 *
 * Contact: http://morfeo.upc.edu/crom  malier@lsi.upc.edu, and
 * info@upc.edu / Jordi Girona Salgado, 1-3 E-08034 Barcelona SPAIN
 */
package edu.upc.lsdatalib.test;

import edu.upc.lsdatalib.fitxersequencialvalor.*;
import edu.upc.lsdatalib.comunsfitxer.PDAException;
import edu.upc.lsdatalib.comunsfitxer.PDAStoreable;
import java.util.Vector;
import javax.microedition.lcdui.*;

public class ManageContacts{
    PDALabeledStoreSequence fitxerSeqValor;
    /** Creates a new instance of ManageContacts*/
    public ManageContacts(){
        
    }
    
    // This method creates a new PDALAbeledStoreSequence file to store
    //contacts data. The contacts file has two fields: name and
    //phone. Each row is identified by name although the file may store
    //several contacts with the same name. Do an exercise to practice this.
    public void OpenContacts() throws PDAException{
        //If the file does not exists it is created. Otherwise it is opened
        fitxerSeqValor=new PDALabeledStoreSequence("Contactes8");
    }
    
    //This method adds a new contact to the file
    public void NewContact(String name, String phone) throws PDAException{
        OpenContacts();
        Contact newContact=new Contact(name,phone);
        fitxerSeqValor.write(newContact,name);
        closeContactFile();           
    }
    
   //This method returns all the contacts in the file 
   public Vector getContacts() throws PDAException{  
       Vector contactData=null; 
       
       OpenContacts();
        //Obrir el fitxer
       if (!fitxerSeqValor.First()){
            closeContactFile(); 
            return contactData; 
       }else{
            contactData=new Vector();
            Contact contacte=(Contact)fitxerSeqValor.read();
            contactData.addElement(contacte);
            while (!fitxerSeqValor.EOF()){
                  fitxerSeqValor.Next();
                  contacte=(Contact)fitxerSeqValor.read();
                  contactData.addElement(contacte);
            }
            closeContactFile();
            return contactData;
       }
           
   }

   //This method retrieves a single contact from the field identified by
   //name. Be careful!. if you have two contact identified by name you should 
   //return them in a vector
   public Contact getContact(String name) throws PDAException{
      
       OpenContacts();
      if (fitxerSeqValor.seek(name)){
          PDAStoreable nga = fitxerSeqValor.read();
          closeContactFile();
          return ((Contact)nga);
      }      
      else {
            closeContactFile();
            return null;
      }
   }
   
   //This method deletes the contact identified by name
   public int deleteContact(String name) throws PDAException{
       OpenContacts();
        if (fitxerSeqValor.seek(name)){
           fitxerSeqValor.delete();
           closeContactFile();
           return 1;
        }else{
            closeContactFile();
            return 0;
        }  
   }
   
   public int uptadeContact(String name, String phone) throws PDAException{
          OpenContacts();
          if (fitxerSeqValor.seek(name)){
              fitxerSeqValor.update(new Contact(name,phone));
              closeContactFile();
              return 1;
          }else{
              closeContactFile();
              return 0;
          }
   }
  
   //This method delets the file
   public void dropContacts() throws PDAException{
        fitxerSeqValor.deletePDAStore();
   }
   
   //This method closes the file.
   public void closeContactFile() throws PDAException{
       fitxerSeqValor.closePDAStore();
   }
   
   public boolean existsContactFile() throws PDAException{
       OpenContacts();
       if (fitxerSeqValor.First()){
           closeContactFile();
           return true;   
       }else {
           closeContactFile();
           return false;
       }
   }
 }
