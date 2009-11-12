/*
 * provaPHP2.java
 *
 * Created on 16 de abril de 2007, 14:43
 */

package edu.upc.BDAccessRemot.test;

import edu.upc.BDAccessRemot.j2meApi.MobileHTTPConnection;
import edu.upc.BDAccessRemot.j2meApi.MobileHTTPStatementResult;
import edu.upc.J2MESDLIB.J2MEColumn;
import edu.upc.J2MESDLIB.J2MEDBException;
import edu.upc.J2MESDLIB.J2MESDLIB;
import edu.upc.J2MESDLIB.J2METable;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.InputConnection;
import javax.microedition.io.OutputConnection;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 *
 * @author  Pelsman
 * @version
 */
public class provaPHP2 extends MIDlet {
    public void startApp() {
        try{
            
            J2MESDLIB.generateDB(false);
            //int num=0;
            // Els camps de l'string són: idconnexio, numfiles,numcolumnes, nom columna1, tipus columna 1, logitud, idem per columna 2.....i al final els valors
            //String dades="'idconexio11'~'2'~'2'~'nom'~'java.lang.String'~'30'~'EDAD'~'java.lang.Integer'~'4'~~'Pere'~'15'~~~'miquel'~'30'~~~";
            //String dades = "'idconexio11'~'4'~'1'~'id'~'java.lang.Integer'~'0'~'comentari'~'java.lang.String'~'45'~'cert'~'java.lang.Boolean'~'0'~'llarg'~'java.lang.Double'~'0'~~'1'~'jaja'~'1'~'23232.0'~~~";
            //String dades = "select~0~'4'~'10'~'id'~'java.lang.string'~'45'~'comentari'~'java.lang.string'~'45'~'cert'~'java.lang.string'~'45'~'llarg'~'java.lang.string'~'45'~~'1'~'jaja'~'1'~'23232'~~~'2'~'hola'~'1'~'2000333'~~~'3'~'comment'~'1'~'2000333'~~~'4'~'comment'~'0'~'2000333'~~~'12'~'comment'~'1'~'2000333'~~~'55'~'comment'~'1'~'2000333'~~~'114'~'comment'~'1'~'2000333'~~~'121'~'comment'~'0'~'1231222'~~~'123'~'comment'~'1'~'2000333'~~~'552'~'comment'~'1'~'2000333'~~~";
            String dades = "'forum'~'4'~'1'~'id'~'java.lang.String'~'45'~'comentari'~'java.lang.String'~'45'~'cert'~'java.lang.String'~'45'~'llarg'~'java.lang.String'~'45'~~'1'~'jaja'~'1'~'23232'~~~'2'~'hola'~'1'~'2000333'~~~";//'3'~'comment'~'1'~'2000333'~~~'4'~'comment'~'0'~'2000333'~~~'12'~'comment'~'1'~'2000333'~~~'55'~'comment'~'1'~'2000333'~~~'114'~'comment'~'1'~'2000333'~~~'121'~'comment'~'0'~'1231222'~~~'123'~'comment'~'1'~'2000333'~~~'151'~'paco'~'1'~'222'~~~'152'~'paco'~'2'~'222'~~~'552'~'comment'~'1'~'2000333'~~~";
            try {
                //J2MESDLIB.regenerarDB();
                J2METable t =J2MESDLIB.parseToTable(dades);
                //J2MEColumn c = new J2MEColumn("id",true,J2MEColumn.ColumnType.INT,10*J2MEColumn.ColumnType.INT_SIZE);
                //Vector v = new Vector();
                //v.addElement(c);
                //System.out.println("Comença create Table");
                //J2METable t2 = J2MESDLIB.createTable("hola",v,1);
                //t.printTable();
                String name = t.tableName;
                //String name = "hola";//t2.tableName;
                
                //t =J2MESDLIB.parseToTable(dades);
                System.out.println("Comença Drop Table");
                t.closeTable();
                J2MESDLIB.dropTable(name);
                
                
            /*if(J2MESDLIB.dropTable(name)){
                    System.out.println("esborrada");
                }
                else{
                    System.out.println("no esborrada");
                }*/
                
                if(J2MESDLIB.existsTable(name)){
                    System.out.println("existeix");
                }
                else{
                    System.out.println("no existeix");
                }
                
                
                
                /*if (J2MESDLIB.existsTable("prova")){
                    //try{
                        J2METable t = J2MESDLIB.loadTable("prova");
                        System.out.println(t.printTable());
                    //}
                    //catch(J2MEDBException e){
                    //    System.out.println("Error al carregar la bd");
                    //}
                }
                else{
                    System.out.println("No s'ha trobat la taula prova");
                }*/
                /*int num = t.getNumCols();
                System.out.println("cols = "+num);
                
                System.out.println(t.printTable());
                J2METable t2 = J2MESDLIB.createTable("prova",t.getColumns(),0);
                t.saveTable();
                t.closeTable();*/
                
            }
            catch (J2MEDBException ex) {
                ex.printStackTrace();
            }
            J2MESDLIB.closeDB();
            
            
            /*Enumeration drives = FileSystemRegistry.listRoots();
            System.out.println("The valid roots found are: ");
            while(drives.hasMoreElements()) {
             String root = (String) drives.nextElement();
             System.out.println("\t"+root);
            }*/
            
            //System.out.println("".getBytes().length);
            
            /*FileConnection conn = (FileConnection)Connector.open("file:///root1/Readme2");
            if(!conn.exists()){
                conn.create();
            }
            OutputStream out = conn.openOutputStream();
            out.write("26~".getBytes());
            out.close();
            conn.close();
            
            conn = (FileConnection)Connector.open("file:///root1/Readme2");
            
            InputStream in = conn.openInputStream();
            byte[] aux = new byte[1];
            StringBuffer num = new StringBuffer();
            int i = in.read(aux);
            while (aux[0]!=126 && i!=-1){
                System.out.println(aux[0]);
                num.append((char)aux[0]);
                i = in.read(aux);
            }
            
            System.out.println(num.toString());
            
            in.close();
            conn.close();*/
            /*int read = in.read(b);
            String s = new String(b);
            System.out.println(read+" bytes readed");
            System.out.println(s);*/
            
            //PrintStream p = new PrintStream(out);
            //p.println("Hola\nQue tal?");
            //p.close();
            //in.close();
            //conn.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        //try{
          
            //J2MESDLIB.generateDB(false);
            //int num=0;
            // Els camps de l'string són: idconnexio, numfiles,numcolumnes, nom columna1, tipus columna 1, logitud, idem per columna 2.....i al final els valors
            //String dades="'idconexio11'~'2'~'2'~'nom'~'java.lang.String'~'30'~'EDAD'~'java.lang.Integer'~'4'~~'Pere'~'15'~~~'miquel'~'30'~~~";
            //String dades = "'idconexio11'~'4'~'1'~'id'~'java.lang.Integer'~'0'~'comentari'~'java.lang.String'~'45'~'cert'~'java.lang.Boolean'~'0'~'llarg'~'java.lang.Double'~'0'~~'1'~'jaja'~'1'~'23232.0'~~~";
            /*String dades = "select~0~'4'~'10'~'id'~'java.lang.string'~'45'~'comentari'~'java.lang.string'~'45'~'cert'~'java.lang.string'~'45'~'llarg'~'java.lang.string'~'45'~~'1'~'jaja'~'1'~'23232'~~~'2'~'hola'~'1'~'2000333'~~~'3'~'comment'~'1'~'2000333'~~~'4'~'comment'~'0'~'2000333'~~~'12'~'comment'~'1'~'2000333'~~~'55'~'comment'~'1'~'2000333'~~~'114'~'comment'~'1'~'2000333'~~~'121'~'comment'~'0'~'1231222'~~~'123'~'comment'~'1'~'2000333'~~~'552'~'comment'~'1'~'2000333'~~~";
            try {
                J2METable a=J2MESDLIB.parseToTable(dades);
                num=a.getNumCols();
                System.out.println("cols = "+num);
                System.out.println("1");
                System.out.println(a.printTable());
                System.out.println("2");
            }
            catch (J2MEDBException ex) {
                ex.printStackTrace();
            }
            J2MESDLIB.closeDB();
            
            */
            /*MobileHTTPConnection m = new MobileHTTPConnection("localhost","proves2",0,"nestor","nestor");
            //System.out.println("1");
            m.DBConnectionRequest();
            //System.out.println("2");
            while(!m.DBOpened()){}
            //System.out.println("3");
            
            int ticket = m.DBStatement("select id from prova1");
            
            
            MobileHTTPStatementResult res = m.DBResultRequest(ticket);
            
            while (res.getStatus() == MobileHTTPStatementResult.WAITING){}
            
            String[][] a = res.getStringResult();
            
            for(int j=0;j<res.getRows();j++){
                for(int k=0;k<res.getCols();k++){
                    System.out.print(a[j][k]+" ");
                }
                System.out.println();
            }*/
            
            /*int ticket = m.DBStatement("insert into prova1 values(552,'comment',true,2000333);");
            MobileHTTPStatementResult res = m.DBResultRequest(ticket);
            while (res.getStatus() == MobileHTTPStatementResult.WAITING){}
            
            if(res.getStatus()==MobileHTTPStatementResult.OK){
                System.out.println("Affected rows = "+res.getRows());
            }
            else{
                System.out.println("Error en write");
                
            }*/
            
            
            //m.DBClose();
            //System.out.println("4");
        /*}
        catch(Exception e){
            e.printStackTrace();
        }*/
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
