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

import edu.upc.lsdatalib.comunsfitxer.*;
import java.io.*;
/**
 *
 * @author se
 */

//This class is used to implement the PDAStoreable interface used
//to store objects in a file. Whenever you want to use any of the 4 provided
//files you must implement this interface in order to store objects in the file.
public class Contact implements PDAStoreable {
    String contactName;
    String contactPhone;
    
    /** Creates a new instance of Contact */
    public Contact(String name, String phone) {
        contactName=name;
        contactPhone=phone;
    }
    
    // A constructor without parameters is necessary because it is used
    //to retrieve data from the RecordStore and create the associate object
    public Contact(){
        contactName=null;
        contactPhone=null;
    }
    
    public String getName(){
        return this.contactName;
    }
    
    public String getPhone(){
       return this.contactPhone;    
    }
    
    public int compare(PDAStoreable c){
      byte[] data = this.getData();
		
	if(data.length > c.size()) 
             return 1;
	else if (data.length < c.size()) 
                 return -1;

	/*Comparem valor per valor els dos arrays*/		
	int size = data.length;
	for(int i=0; i<size; i++){
	     if(data[i] != c.getData()[i]) 
                 return -1; 
	}
		
	return 0;
    }
    
    public void setData(byte[] b, int size) throws PDAException{
         if(b.length!=size) throw new PDAException(2);
	 try{
	    ByteArrayInputStream inputStream = new ByteArrayInputStream(b);
	    DataInputStream inputDataStream = new DataInputStream(inputStream);
				
	    this.contactName=inputDataStream.readUTF();
            this.contactPhone=inputDataStream.readUTF();
						
	 }
	 catch(IOException ioe){
		ioe.printStackTrace();
	 }     
    }
    
    public byte[] getData(){
        try{			
	  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	  DataOutputStream outputDataStream = new DataOutputStream(outputStream);
					
	  //Escribim nom i telefon del contacte
			
	  outputDataStream.writeUTF(this.contactName);
          outputDataStream.writeUTF(this.contactPhone);
          return(outputStream.toByteArray());
	}
	catch(IOException ioe){
		ioe.printStackTrace();
		return null;
	}
    }
    
    public int size(){
           return this.getData().length; 
    }
    
    
       
}
        
