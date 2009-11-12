/**
* Copyright 2007 Universitat Politécnica de Catalunya
*
* This program is free software; you can redistribute it and/or modify
it under the terms
* of the GNU General Public License as published by the Free Software
Foundation; either
* version 2 of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.
* See the GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
along with this program;
* if not, write to the Free Software Foundation, Inc., 51 Franklin St,
Fifth Floor, Boston, MA
* 02110-1301 USA. It is also //currently //available at
http://www.gnu.org/licenses/gpl.txt
*
* Authors:  Néstor Giménez Segura, Marc Alier, Maria José Casany UPC
*
* Contact: http://morfeo.upc.edu/crom malier@lsi.upc.edu, and
* info@upc.edu / Jordi Girona Salgado, 1-3 E-08034 Barcelona SPAIN
*/

package edu.upc.fileUtil;

import edu.upc.lsdatalib.comunsfitxer.PDAObject;
import edu.upc.lsdatalib.comunsfitxer.PDAStoreable;
import edu.upc.lsdatalib.fitxersequencialvalor.PDALabeledStoreSequence;
import edu.upc.lsdatalib.fitxersequencialvalor.PDAStoreIndexRMS;
import edu.upc.lsdatalib.fitxersequencialvalor.PDAStoreRecordIndex;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.OutputConnection;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import javax.microedition.rms.*;
/**
 * BackupRestore is an API designed to save and load the information from the RecordStores that a Handheld Device using J2MicroDB has.
 * @author Pelsman
 * @version 1.0
 */
public class backupRestore{

    /**
     * Makes the backup of the RecordStores within the Handheld Device using J2MicroDB, saving the information in the directory specified by the parameter "path".
     * @param path The directory where this API has to save the information of the backup.
     * @throws java.lang.Exception If an general exception occurs.
     */
    public void backup(String path) throws Exception{

        PDAStoreable pda = null;
        PDALabeledStoreSequence p = null;
        PDAStoreRecordIndex rec = null;
        PDAStoreIndexRMS p2 = null;
        byte[] aux = null;
        String[] list = RecordStore.listRecordStores();
        FileConnection conn = null;
        OutputStream out = null;
        
        if(list!=null){
            FileConnection dirconn = (FileConnection)Connector.open("file:///"+path);

            if(!dirconn.exists()){
                dirconn.mkdir();
            }
        
            for(int i=0;i<list.length;i++){
                if(!list[i].endsWith("Index")){
                    //System.out.println(list[i]);
                    p = new PDALabeledStoreSequence(list[i]);
                    p2 = new PDAStoreIndexRMS(list[i]+"Index");

                    rec = p2.First();
                    conn = (FileConnection)Connector.open("file:///"+path+list[i]);
                    if (!conn.exists()){
                        conn.create();
                    }
                    else{
                        conn.delete();
                        conn.create();
                    }
                    out = conn.openOutputStream();                    
                    while(rec != null){
                        if(p.seek(rec.getLabel())){
                            pda = p.read();
                            aux = pda.getData();
                            out.write((rec.getLabel()+"~"+rec.getClassName()+"~"+String.valueOf(aux.length)+"~").getBytes());
                            String s = new String(aux);
                            out.write(aux);   
                        }
                        rec = p2.Next();
                    }
                    p2.closeIndex();
                    p.closePDAStore();
                    out.close();
                    conn.close();
                }
            }
        }
    }
        
    /**
     * Restores the information within the specified directory into the Handheld Device using J2MicroDB.
     * @param path The directory from where this API has to load the information of the backup.
     * @throws java.lang.Exception If an general exception occurs.
     */
    public void restore(String path) throws Exception{

        PDAStoreable pda = null;
        PDALabeledStoreSequence p = null;
        String filename = "";
        String label = "";
        String classname = "";
        String num = "";
        RecordStore r = null;
        byte[] record = null;
        byte[] aux = null;
        int j;
        FileConnection conn = null;
        StringBuffer auxbuf = new StringBuffer();

        deleteRecordStores();

        FileConnection dirconn = (FileConnection)Connector.open("file:///"+path);

        if(dirconn.exists()){
            Enumeration filelist = dirconn.list();

            while(filelist.hasMoreElements()){
                filename = (String)filelist.nextElement();
                conn = (FileConnection)Connector.open("file:///"+path+filename);
                if (conn.exists()){
                    InputStream in = conn.openInputStream();
                    p = new PDALabeledStoreSequence(filename);
                    aux = new byte[1];
                    j = in.read(aux);
                    while (j!=-1){
                        while(aux[0] != 126){
                            auxbuf.append((char)aux[0]);
                            j = in.read(aux);
                        }
                        label = auxbuf.toString();
                        auxbuf.delete(0,auxbuf.capacity());

                        j = in.read(aux);
                        while(aux[0] != 126){
                            auxbuf.append((char)aux[0]);
                            j = in.read(aux);
                        }
                        classname = auxbuf.toString();
                        auxbuf.delete(0,auxbuf.capacity());

                        j = in.read(aux);
                        while(aux[0] != 126){
                            auxbuf.append((char)aux[0]);
                            j = in.read(aux);
                        }
                        num = auxbuf.toString();
                        auxbuf.delete(0,auxbuf.capacity());

                        record = new byte[Integer.parseInt(num)];

                        j = in.read(record);
                        String s = new String(record);

                        Class classeobjecte = Class.forName(classname);

                        pda = (PDAStoreable)classeobjecte.newInstance();
                        pda.setData(record,record.length);

                        p.write(pda,label);
                        j = in.read(aux);
                    }
                    p.closePDAStore();
                    in.close();
                }
                conn.close();
            }
        }
        dirconn.close();
    }
    
    private void deleteRecordStores() throws Exception{
        String[] list = RecordStore.listRecordStores();
        if(list!=null){
            for(int i=0;i<list.length;i++){
                RecordStore.deleteRecordStore(list[i]);
            }
        }
    }
    
}
