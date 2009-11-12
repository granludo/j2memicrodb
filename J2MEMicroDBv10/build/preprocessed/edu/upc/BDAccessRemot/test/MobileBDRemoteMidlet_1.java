/*
 * MobileBDRemoteMidlet.java
 *
 * Created on 22 de abril de 2007, 17:24
 */

package edu.upc.BDAccessRemot.test;

import edu.upc.BDAccessRemot.forumWikiAPI.mobileForumWikiAPI;
import edu.upc.BDAccessRemot.j2meApi.MobileHTTPConnection;
import edu.upc.BDAccessRemot.j2meApi.MobileHTTPException;
import edu.upc.BDAccessRemot.j2meApi.MobileHTTPStatementResult;
import edu.upc.J2MESDLIB.J2MEDBException;
import edu.upc.J2MESDLIB.J2MESDLIB;
import edu.upc.J2MESDLIB.J2METable;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 *
 * @author Pelsman
 */
public class MobileBDRemoteMidlet_1 extends MIDlet implements CommandListener {
    
    /** Creates a new instance of MobileBDRemoteMidlet */
    public MobileBDRemoteMidlet_1() {
        initialize();
    }
    
    //private final String ip = "127.0.0.1:8080";
    private final String ip = "127.0.0.1";
    private final String bdd = "proves2";
    private final int sgbd = 0;
    private final String user = "nestor";
    private final String pwd = "nestor";
    
    private mobileForumWikiAPI mc = null;
    private MobileHTTPStatementResult result = null;
    
    private Form FPrincipal;//GEN-BEGIN:MVDFields
    private Command enviar;
    private Command exitPrincipal;
    private Spacer spacer1;
    private StringItem TMainMessage;
    private Form FSqlResult;
    private StringItem TSqlSt;
    private org.netbeans.microedition.lcdui.TableItem TableSqlResult;
    private StringItem TMessage;
    private org.netbeans.microedition.lcdui.SimpleTableModel TMSqlResult;
    private Command exitResult;//GEN-END:MVDFields
    
//GEN-LINE:MVDMethods

    /** Called by the system to indicate that a command has been invoked on a particular displayable.//GEN-BEGIN:MVDCABegin
     * @param command the Command that ws invoked
     * @param displayable the Displayable on which the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:MVDCABegin
    // Insert global pre-action code here
        if (displayable == FPrincipal) {//GEN-BEGIN:MVDCABody
            if (command == enviar) {//GEN-END:MVDCABody
                // Insert pre-action code here
                // Do nothing//GEN-LINE:MVDCAAction6
                try{
                    if(TMainMessage!=null){
                        TMainMessage.setText("Waiting");
                    }
                    mc.open();
                    while(!mc.opened()){}
                    mc.requestForum();
                    //tickets.addElement(new Integer(ticket));
                    MobileHTTPStatementResult result = mc.resultForum();
                    while(result.getStatus() == MobileHTTPStatementResult.WAITING){
                        result = mc.resultForum();
                    }
                    switch(result.getStatus()){
                        case MobileHTTPStatementResult.OK:
                            if (result.isSelect()){
                                this.result = result;
                                if(TMainMessage!=null){
                                    TMainMessage.setText("");
                                }
                                
                                ///////////////Resultat en J2ME Table///////////////
                                try{
                                    result.generateJ2METable();
                                    J2MESDLIB.generateDB(false);
                                    if (J2MESDLIB.existsTable("SDLIB_temp_table")){
                                        try{
                                            J2METable t = J2MESDLIB.loadTable("SDLIB_temp_table");
                                            System.out.println(t.printTable());
                                        }
                                        catch(J2MEDBException e){
                                            System.out.println("Error al carregar la bd");
                                        }
                                    }
                                    else{
                                        System.out.println("No s'ha trobat la taula SDLIB_temp_table");
                                    }
                                    J2MESDLIB.closeDB();
                                }
                                catch(J2MEDBException e){
                                    e.printStackTrace();
                                }
                                //////////////////////////////////////////////////////
                                
                                getDisplay().setCurrent(get_FSqlResult());
                            }
                            if (result.isWrite()){
                                TMainMessage.setText("Escriptura feta satisfactoriament: "+result.getRows()+" files afectades");
                            }
                            break;
                        case MobileHTTPStatementResult.KO:
                            TMainMessage.setText(result.getErrorMessage());
                            break;
                        default:
                            break;
                    }
                    
                    mc.close();
                    while(!mc.closed()){}
                    
                }
                catch(MobileHTTPException e){
                    e.printStackTrace();
                    TMainMessage.setText(e.getMessage());
                    if (e.getCode() == -1){
                        mc.reinit();
                    }
                }



                // Insert post-action code here
            } else if (command == exitPrincipal) {//GEN-LINE:MVDCACase6
                // Insert pre-action code here
                exitMIDlet();//GEN-LINE:MVDCAAction7
                // Insert post-action code here
            }//GEN-BEGIN:MVDCACase7
        } else if (displayable == FSqlResult) {
            if (command == exitResult) {//GEN-END:MVDCACase7
                // Insert pre-action code here
                getDisplay().setCurrent(get_FPrincipal());//GEN-LINE:MVDCAAction16
                // Insert post-action code here
            }//GEN-BEGIN:MVDCACase16
        }//GEN-END:MVDCACase16
    // Insert global post-action code here
}//GEN-LINE:MVDCAEnd

    /** This method initializes UI of the application.//GEN-BEGIN:MVDInitBegin
     */
    private void initialize() {//GEN-END:MVDInitBegin
        // Insert pre-init code here
//GEN-LINE:MVDInitInit
        // Insert post-init code here
    }//GEN-LINE:MVDInitEnd
    
    /**
     * This method should return an instance of the display.
     */
    public Display getDisplay() {//GEN-FIRST:MVDGetDisplay
        return Display.getDisplay(this);
    }//GEN-LAST:MVDGetDisplay
    
    /**
     * This method should exit the midlet.
     */
    public void exitMIDlet() {//GEN-FIRST:MVDExitMidlet
        getDisplay().setCurrent(null);
        destroyApp(true);
        notifyDestroyed();
    }//GEN-LAST:MVDExitMidlet

    /** This method returns instance for FPrincipal component and should be called instead of accessing FPrincipal field directly.//GEN-BEGIN:MVDGetBegin2
     * @return Instance for FPrincipal component
     */
    public Form get_FPrincipal() {
        if (FPrincipal == null) {//GEN-END:MVDGetBegin2
            // Insert pre-init code here
            FPrincipal = new Form(null, new Item[] {//GEN-BEGIN:MVDGetInit2
                get_spacer1(),
                get_TMainMessage()
            });
            FPrincipal.addCommand(get_enviar());
            FPrincipal.addCommand(get_exitPrincipal());
            FPrincipal.setCommandListener(this);//GEN-END:MVDGetInit2
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd2
        return FPrincipal;
    }//GEN-END:MVDGetEnd2
 
    /** This method returns instance for enviar component and should be called instead of accessing enviar field directly.//GEN-BEGIN:MVDGetBegin4
     * @return Instance for enviar component
     */
    public Command get_enviar() {
        if (enviar == null) {//GEN-END:MVDGetBegin4
            // Insert pre-init code here
            enviar = new Command("Ok", Command.OK, 1);//GEN-LINE:MVDGetInit4
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd4
        return enviar;
    }//GEN-END:MVDGetEnd4

    /** This method returns instance for exitPrincipal component and should be called instead of accessing exitPrincipal field directly.//GEN-BEGIN:MVDGetBegin5
     * @return Instance for exitPrincipal component
     */
    public Command get_exitPrincipal() {
        if (exitPrincipal == null) {//GEN-END:MVDGetBegin5
            // Insert pre-init code here
            exitPrincipal = new Command("Exit", Command.EXIT, 1);//GEN-LINE:MVDGetInit5
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd5
        return exitPrincipal;
    }//GEN-END:MVDGetEnd5

    /** This method returns instance for spacer1 component and should be called instead of accessing spacer1 field directly.//GEN-BEGIN:MVDGetBegin8
     * @return Instance for spacer1 component
     */
    public Spacer get_spacer1() {
        if (spacer1 == null) {//GEN-END:MVDGetBegin8
            // Insert pre-init code here
            spacer1 = new Spacer(1000, 1);//GEN-LINE:MVDGetInit8
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd8
        return spacer1;
    }//GEN-END:MVDGetEnd8

    /** This method returns instance for TMainMessage component and should be called instead of accessing TMainMessage field directly.//GEN-BEGIN:MVDGetBegin9
     * @return Instance for TMainMessage component
     */
    public StringItem get_TMainMessage() {
        if (TMainMessage == null) {//GEN-END:MVDGetBegin9
            // Insert pre-init code here
            TMainMessage = new StringItem("Message:", "");//GEN-LINE:MVDGetInit9
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd9
        return TMainMessage;
    }//GEN-END:MVDGetEnd9

    /** This method returns instance for FSqlResult component and should be called instead of accessing FSqlResult field directly.//GEN-BEGIN:MVDGetBegin10
     * @return Instance for FSqlResult component
     */
    public Form get_FSqlResult() {
        if (FSqlResult == null) {//GEN-END:MVDGetBegin10
            // Insert pre-init code here
            FSqlResult = new Form(null, new Item[] {//GEN-BEGIN:MVDGetInit10
                get_TSqlSt(),
                get_TableSqlResult(),
                get_TMessage()
            });
            FSqlResult.addCommand(get_exitResult());
            FSqlResult.setCommandListener(this);//GEN-END:MVDGetInit10
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd10
        return FSqlResult;
    }//GEN-END:MVDGetEnd10

    /** This method returns instance for TSqlSt component and should be called instead of accessing TSqlSt field directly.//GEN-BEGIN:MVDGetBegin11
     * @return Instance for TSqlSt component
     */
    public StringItem get_TSqlSt() {
        if (TSqlSt == null) {//GEN-END:MVDGetBegin11
            // Insert pre-init code here
            TSqlSt = new StringItem("SQL Statement:", "");//GEN-LINE:MVDGetInit11
            // Insert post-init code here
            if(result!=null){
                TSqlSt.setText(result.getStatment());
            }
        }//GEN-BEGIN:MVDGetEnd11
        return TSqlSt;
    }//GEN-END:MVDGetEnd11

    /** This method returns instance for TableSqlResult component and should be called instead of accessing TableSqlResult field directly.//GEN-BEGIN:MVDGetBegin12
     * @return Instance for TableSqlResult component
     */
    public org.netbeans.microedition.lcdui.TableItem get_TableSqlResult() {
        if (TableSqlResult == null) {//GEN-END:MVDGetBegin12
            // Insert pre-init code here
            TableSqlResult = new org.netbeans.microedition.lcdui.TableItem(getDisplay(), "Result:", get_TMSqlResult());//GEN-LINE:MVDGetInit12
            // Insert post-init code here
            if(result!=null){
                TMSqlResult.setValues(result.getStringResult());
                TMSqlResult.setColumnNames(result.getColumnNames());
            }
        }//GEN-BEGIN:MVDGetEnd12
        return TableSqlResult;
    }//GEN-END:MVDGetEnd12

    /** This method returns instance for TMessage component and should be called instead of accessing TMessage field directly.//GEN-BEGIN:MVDGetBegin13
     * @return Instance for TMessage component
     */
    public StringItem get_TMessage() {
        if (TMessage == null) {//GEN-END:MVDGetBegin13
            // Insert pre-init code here
            TMessage = new StringItem("Message:", "");//GEN-LINE:MVDGetInit13
            // Insert post-init code here
            if(result!=null){
                TMessage.setText("Lectura feta correctament: "+result.getRows()+" files retornades");
            }
        }//GEN-BEGIN:MVDGetEnd13
        return TMessage;
    }//GEN-END:MVDGetEnd13

    /** This method returns instance for TMSqlResult component and should be called instead of accessing TMSqlResult field directly.//GEN-BEGIN:MVDGetBegin14
     * @return Instance for TMSqlResult component
     */
    public org.netbeans.microedition.lcdui.SimpleTableModel get_TMSqlResult() {
        if (TMSqlResult == null) {//GEN-END:MVDGetBegin14
            // Insert pre-init code here
            TMSqlResult = new org.netbeans.microedition.lcdui.SimpleTableModel();//GEN-LINE:MVDGetInit14
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd14
        return TMSqlResult;
    }//GEN-END:MVDGetEnd14

    /** This method returns instance for exitResult component and should be called instead of accessing exitResult field directly.//GEN-BEGIN:MVDGetBegin15
     * @return Instance for exitResult component
     */
    public Command get_exitResult() {
        if (exitResult == null) {//GEN-END:MVDGetBegin15
            // Insert pre-init code here
            exitResult = new Command("Exit", Command.EXIT, 1);//GEN-LINE:MVDGetInit15
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd15
        return exitResult;
    }//GEN-END:MVDGetEnd15
    
    public void startApp() {
        try{
            HttpConnection conn = (HttpConnection)Connector.open("http://127.0.0.1:8080/J2MEServer/servlets/servlet/BDServer?hello");
        }
        catch(Exception e){
            
        }
        
        mc = new mobileForumWikiAPI(ip,user,pwd);
        
        getDisplay().setCurrent(get_FPrincipal()); 	

    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
    
}
