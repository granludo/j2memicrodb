/*
 * AccesBDRemot.java
 *
 * Created on 20 de febrero de 2007, 18:47
 */

package edu.upc.BDAccessRemot.test;

import edu.upc.BDAccessRemot.RemoteProxyListener;
import edu.upc.BDAccessRemot.j2meApi.MobileHTTPConnection;
import edu.upc.BDAccessRemot.j2meApi.MobileHTTPException;
import edu.upc.BDAccessRemot.j2meApi.MobileHTTPStatementResult;
import edu.upc.J2MESDLIB.J2METable;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 *
 * @author Pelsman
 */
public class AccesBDRemot extends MIDlet implements CommandListener {
    
    /** Creates a new instance of AccesBDRemot */
    public AccesBDRemot() {
        initialize();
    }
    
    private org.netbeans.microedition.lcdui.SimpleTableModel BDTableModel;//GEN-BEGIN:MVDFields
    private Command CVeureBD;
    private Command CExitPrincipal;
    private Form FHTTP;
    private Form FBDDisp;
    private Command CConnectarDisp;
    private Command CBackDisp;
    private TextField TUser;
    private StringItem SEstatBD;
    private TextField TSQL;
    private Command CEnviarSt;
    private Form FHTTPResult;
    private org.netbeans.microedition.lcdui.TableItem TResultat;
    private StringItem TComentari;
    private Command CBackResult;
    private TextField TPsw;
    private org.netbeans.microedition.lcdui.SimpleTableModel ResultTableModel;
    private Command CRecollirResult;
    private Command CTancarBD;
    private StringItem stringItem1;
    private StringItem SStateBDResult;
    private Spacer spacer1;
    private TextField TBD;
    private TextField TSGBD;
    private TextField TIP;//GEN-END:MVDFields
    
    
    /*private BDRemoteClient client2;*/
    private MobileHTTPConnection client;
    private MobileHTTPStatementResult selectResult = null;
    private Vector tickets;    
    
//GEN-LINE:MVDMethods

    /** Called by the system to indicate that a command has been invoked on a particular displayable.//GEN-BEGIN:MVDCABegin
     * @param command the Command that ws invoked
     * @param displayable the Displayable on which the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:MVDCABegin
    // Insert global pre-action code here
        try{
            if (displayable == FBDDisp) {//GEN-BEGIN:MVDCABody
                if (command == CConnectarDisp) {//GEN-END:MVDCABody
                    // Insert pre-action code here
                    
                    try{
                        String bd = TBD.getString();
                        int sgbd = Integer.parseInt(TSGBD.getString());
                        if(client == null){
                            client = new MobileHTTPConnection(TIP.getString(),bd,sgbd,TUser.getString(),TPsw.getString());
                        } 
                        client.DBConnectionRequest();
                        while(!client.DBOpened());
                        getDisplay().setCurrent(get_FHTTP());
                    }
                    catch(MobileHTTPException e){
                        showError(e.getMessage());
                        if(e.getCode()==-1){
                            client.DBReinit();
                            getDisplay().setCurrent(get_FBDDisp());
                        }
                    }
                    catch(NumberFormatException e){
                        showError(e.getMessage());
                    }
                
                    // Do nothing//GEN-LINE:MVDCAAction24
                // Insert post-action code here
                
                
                
                } else if (command == CBackDisp) {//GEN-LINE:MVDCACase24
                // Insert pre-action code here
                    // Do nothing//GEN-LINE:MVDCAAction25
                // Insert post-action code here
                }//GEN-BEGIN:MVDCACase25
            } else if (displayable == FHTTP) {
                if (command == CEnviarSt) {//GEN-END:MVDCACase25
                // Insert pre-action code here
                    try{
                        int ticket = client.DBStatement(TSQL.getString());
                        tickets.addElement(new Integer(ticket));
                    }
                    catch(MobileHTTPException e){
                        showError(e.getMessage());
                        if(e.getCode()==-1){
                            client.DBReinit();
                            getDisplay().setCurrent(get_FBDDisp());
                        }
                    }
                    // Do nothing//GEN-LINE:MVDCAAction32
                // Insert post-action code here
                } else if (command == CRecollirResult) {//GEN-LINE:MVDCACase32
                    // Insert pre-action code here
                    try{
                        MobileHTTPStatementResult result = client.DBResultRequest(((Integer)tickets.elementAt(0)).intValue());
                        selectResult = result;
                        
                        if(selectResult.getStatus() != MobileHTTPStatementResult.WAITING){
                            if(ResultTableModel != null){
                                ResultTableModel.setColumnNames(result.getColumnNames());
                                ResultTableModel.setValues(result.getStringResult());
                            }
                            if(TComentari != null){
                                switch(selectResult.getStatus()){
                                    case MobileHTTPStatementResult.WAITING:
                                        break;
                                    case MobileHTTPStatementResult.OK:
                                        if(selectResult.isWrite()) TComentari.setText(selectResult.getRows()+" files afectades");
                                        if(selectResult.isSelect()) TComentari.setText(selectResult.getRows()+" files retornades");
                                        break;
                                    case MobileHTTPStatementResult.KO:
                                        TComentari.setText(selectResult.getErrorMessage());
                                        break;
                                    default:
                                        break;
                                }
                            }

                            if(SStateBDResult != null){
                                if (selectResult != null){
                                    SStateBDResult.setText(selectResult.getStatment());
                                }
                            }
                            
                            tickets.removeElementAt(0);

                            getDisplay().setCurrent(get_FHTTPResult());

                        }
                        else{
                            showError("El resultat no encara no esta disponible");
                        }
                    }
                    catch (MobileHTTPException e){
                        showError(e.getMessage());
                        if(e.getCode()==-1){
                            client.DBReinit();
                            getDisplay().setCurrent(get_FBDDisp());
                        }                    
                    }
                    
                    
                    // Do nothing//GEN-LINE:MVDCAAction40
                    // Insert post-action code here
                } else if (command == CTancarBD) {//GEN-LINE:MVDCACase40
                    // Insert pre-action code here
                    // Do nothing//GEN-LINE:MVDCAAction43
                    // Insert post-action code here
                    try{
                        client.DBClose();
                        while (!client.DBClosed());
                        getDisplay().setCurrent(get_FBDDisp());
                    }
                    catch(MobileHTTPException e){
                        showError(e.getMessage());
                        if(e.getCode()==-1){
                            client.DBReinit();
                            getDisplay().setCurrent(get_FBDDisp());
                        }                        
                    }
                }//GEN-BEGIN:MVDCACase43
            } else if (displayable == FHTTPResult) {
                if (command == CBackResult) {//GEN-END:MVDCACase43
                // Insert pre-action code here
                    getDisplay().setCurrent(get_FHTTP());//GEN-LINE:MVDCAAction37
                // Insert post-action code here
                }//GEN-BEGIN:MVDCACase37
            }//GEN-END:MVDCACase37
    // Insert global post-action code here
        }
        catch(Exception e){
            //System.out.println(e.getMessage());
            showError(e.getMessage());
        }
}//GEN-LINE:MVDCAEnd

    /** This method initializes UI of the application.//GEN-BEGIN:MVDInitBegin
     */
    private void initialize() {//GEN-END:MVDInitBegin
        // Insert pre-init code here
        tickets = new Vector();
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
    
    /** This method returns instance for BDTableModel component and should be called instead of accessing BDTableModel field directly.//GEN-BEGIN:MVDGetBegin12
     * @return Instance for BDTableModel component
     */
    public org.netbeans.microedition.lcdui.SimpleTableModel get_BDTableModel() {
        if (BDTableModel == null) {//GEN-END:MVDGetBegin12
            // Insert pre-init code here
            BDTableModel = new org.netbeans.microedition.lcdui.SimpleTableModel();//GEN-BEGIN:MVDGetInit12
            BDTableModel.setValues(new String[][] {
                new String[] {
                    "proves",
                },
                new String[] {
                    null,
                },
            });
            BDTableModel.setColumnNames(null);//GEN-END:MVDGetInit12
            // Insert post-init code here
            
        }//GEN-BEGIN:MVDGetEnd12
        return BDTableModel;
    }//GEN-END:MVDGetEnd12

    /** This method returns instance for CVeureBD component and should be called instead of accessing CVeureBD field directly.//GEN-BEGIN:MVDGetBegin13
     * @return Instance for CVeureBD component
     */
    public Command get_CVeureBD() {
        if (CVeureBD == null) {//GEN-END:MVDGetBegin13
            // Insert pre-init code here
            CVeureBD = new Command(" Veure Disponibles", Command.OK, 1);//GEN-LINE:MVDGetInit13
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd13
        return CVeureBD;
    }//GEN-END:MVDGetEnd13

    /** This method returns instance for CExitPrincipal component and should be called instead of accessing CExitPrincipal field directly.//GEN-BEGIN:MVDGetBegin14
     * @return Instance for CExitPrincipal component
     */
    public Command get_CExitPrincipal() {
        if (CExitPrincipal == null) {//GEN-END:MVDGetBegin14
            // Insert pre-init code here
            CExitPrincipal = new Command("Exit", Command.EXIT, 1);//GEN-LINE:MVDGetInit14
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd14
        return CExitPrincipal;
    }//GEN-END:MVDGetEnd14

    /** This method returns instance for FHTTP component and should be called instead of accessing FHTTP field directly.//GEN-BEGIN:MVDGetBegin19
     * @return Instance for FHTTP component
     */
    public Form get_FHTTP() {
        if (FHTTP == null) {//GEN-END:MVDGetBegin19
            // Insert pre-init code here
            FHTTP = new Form(null, new Item[] {//GEN-BEGIN:MVDGetInit19
                get_SEstatBD(),
                get_TSQL()
            });
            FHTTP.addCommand(get_CEnviarSt());
            FHTTP.addCommand(get_CRecollirResult());
            FHTTP.addCommand(get_CTancarBD());
            FHTTP.setCommandListener(this);//GEN-END:MVDGetInit19
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd19
        return FHTTP;
    }//GEN-END:MVDGetEnd19

    /** This method returns instance for FBDDisp component and should be called instead of accessing FBDDisp field directly.//GEN-BEGIN:MVDGetBegin20
     * @return Instance for FBDDisp component
     */
    public Form get_FBDDisp() {
        if (FBDDisp == null) {//GEN-END:MVDGetBegin20
            // Insert pre-init code here
            FBDDisp = new Form(null, new Item[] {//GEN-BEGIN:MVDGetInit20
                get_TIP(),
                get_TBD(),
                get_TSGBD(),
                get_TUser(),
                get_TPsw()
            });
            FBDDisp.addCommand(get_CConnectarDisp());
            FBDDisp.addCommand(get_CBackDisp());
            FBDDisp.setCommandListener(this);//GEN-END:MVDGetInit20
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd20
        return FBDDisp;
    }//GEN-END:MVDGetEnd20
 
    /** This method returns instance for CConnectarDisp component and should be called instead of accessing CConnectarDisp field directly.//GEN-BEGIN:MVDGetBegin22
     * @return Instance for CConnectarDisp component
     */
    public Command get_CConnectarDisp() {
        if (CConnectarDisp == null) {//GEN-END:MVDGetBegin22
            // Insert pre-init code here
            CConnectarDisp = new Command("Ok", Command.OK, 1);//GEN-LINE:MVDGetInit22
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd22
        return CConnectarDisp;
    }//GEN-END:MVDGetEnd22

    /** This method returns instance for CBackDisp component and should be called instead of accessing CBackDisp field directly.//GEN-BEGIN:MVDGetBegin23
     * @return Instance for CBackDisp component
     */
    public Command get_CBackDisp() {
        if (CBackDisp == null) {//GEN-END:MVDGetBegin23
            // Insert pre-init code here
            CBackDisp = new Command("Back", Command.BACK, 1);//GEN-LINE:MVDGetInit23
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd23
        return CBackDisp;
    }//GEN-END:MVDGetEnd23
 
    /** This method returns instance for TUser component and should be called instead of accessing TUser field directly.//GEN-BEGIN:MVDGetBegin27
     * @return Instance for TUser component
     */
    public TextField get_TUser() {
        if (TUser == null) {//GEN-END:MVDGetBegin27
            // Insert pre-init code here
            TUser = new TextField("Username:", "nestor", 120, TextField.ANY);//GEN-LINE:MVDGetInit27
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd27
        return TUser;
    }//GEN-END:MVDGetEnd27

    /** This method returns instance for TPsw component and should be called instead of accessing TPsw field directly.//GEN-BEGIN:MVDGetBegin28
     * @return Instance for TPsw component
     */
    public TextField get_TPsw() {
        if (TPsw == null) {//GEN-END:MVDGetBegin28
            // Insert pre-init code here
            TPsw = new TextField("Password:", "nestor", 120, TextField.ANY | TextField.PASSWORD);//GEN-LINE:MVDGetInit28
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd28
        return TPsw;
    }//GEN-END:MVDGetEnd28

    /** This method returns instance for SEstatBD component and should be called instead of accessing SEstatBD field directly.//GEN-BEGIN:MVDGetBegin29
     * @return Instance for SEstatBD component
     */
    public StringItem get_SEstatBD() {
        if (SEstatBD == null) {//GEN-END:MVDGetBegin29
            // Insert pre-init code here
            SEstatBD = new StringItem("Estat:", "");//GEN-LINE:MVDGetInit29
            // Insert post-init code here
            
            //SEstatBD.setText(client.DBgetState());
            
        }//GEN-BEGIN:MVDGetEnd29
        return SEstatBD;
    }//GEN-END:MVDGetEnd29

    /** This method returns instance for TSQL component and should be called instead of accessing TSQL field directly.//GEN-BEGIN:MVDGetBegin30
     * @return Instance for TSQL component
     */
    public TextField get_TSQL() {
        if (TSQL == null) {//GEN-END:MVDGetBegin30
            // Insert pre-init code here
            TSQL = new TextField("Sent\u00E8ncia SQL:", "select * from prova1", 120, TextField.ANY);//GEN-LINE:MVDGetInit30
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd30
        return TSQL;
    }//GEN-END:MVDGetEnd30

    /** This method returns instance for CEnviarSt component and should be called instead of accessing CEnviarSt field directly.//GEN-BEGIN:MVDGetBegin31
     * @return Instance for CEnviarSt component
     */
    public Command get_CEnviarSt() {
        if (CEnviarSt == null) {//GEN-END:MVDGetBegin31
            // Insert pre-init code here
            CEnviarSt = new Command("Enviar sentencia", Command.OK, 1);//GEN-LINE:MVDGetInit31
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd31
        return CEnviarSt;
    }//GEN-END:MVDGetEnd31

    /** This method returns instance for FHTTPResult component and should be called instead of accessing FHTTPResult field directly.//GEN-BEGIN:MVDGetBegin33
     * @return Instance for FHTTPResult component
     */
    public Form get_FHTTPResult() {
        if (FHTTPResult == null) {//GEN-END:MVDGetBegin33
            // Insert pre-init code here
            FHTTPResult = new Form(null, new Item[] {//GEN-BEGIN:MVDGetInit33
                get_SStateBDResult(),
                get_spacer1(),
                get_TResultat(),
                get_TComentari()
            });
            FHTTPResult.addCommand(get_CBackResult());
            FHTTPResult.setCommandListener(this);//GEN-END:MVDGetInit33
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd33
        return FHTTPResult;
    }//GEN-END:MVDGetEnd33

    /** This method returns instance for TResultat component and should be called instead of accessing TResultat field directly.//GEN-BEGIN:MVDGetBegin34
     * @return Instance for TResultat component
     */
    public org.netbeans.microedition.lcdui.TableItem get_TResultat() {
        if (TResultat == null) {//GEN-END:MVDGetBegin34
            // Insert pre-init code here
            TResultat = new org.netbeans.microedition.lcdui.TableItem(getDisplay(), "Resultat:", get_ResultTableModel());//GEN-LINE:MVDGetInit34
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd34
        return TResultat;
    }//GEN-END:MVDGetEnd34

    /** This method returns instance for TComentari component and should be called instead of accessing TComentari field directly.//GEN-BEGIN:MVDGetBegin35
     * @return Instance for TComentari component
     */
    public StringItem get_TComentari() {
        if (TComentari == null) {//GEN-END:MVDGetBegin35
            // Insert pre-init code here
            TComentari = new StringItem("Comentari:", "");//GEN-LINE:MVDGetInit35
            // Insert post-init code here
            if(selectResult != null){
                switch(selectResult.getStatus()){
                    case MobileHTTPStatementResult.WAITING:
                        break;
                    case MobileHTTPStatementResult.OK:
                        if(selectResult.isWrite()) TComentari.setText(selectResult.getRows()+" files afectades");
                        if(selectResult.isSelect()) TComentari.setText(selectResult.getRows()+" files retornades");
                        break;
                    case MobileHTTPStatementResult.KO:
                        TComentari.setText(selectResult.getErrorMessage());
                        break;
                    default:
                        break;
                }
            }
            
        }//GEN-BEGIN:MVDGetEnd35
        return TComentari;
    }//GEN-END:MVDGetEnd35

    /** This method returns instance for CBackResult component and should be called instead of accessing CBackResult field directly.//GEN-BEGIN:MVDGetBegin36
     * @return Instance for CBackResult component
     */
    public Command get_CBackResult() {
        if (CBackResult == null) {//GEN-END:MVDGetBegin36
            // Insert pre-init code here
            CBackResult = new Command("Back", Command.BACK, 1);//GEN-LINE:MVDGetInit36
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd36
        return CBackResult;
    }//GEN-END:MVDGetEnd36

    /** This method returns instance for ResultTableModel component and should be called instead of accessing ResultTableModel field directly.//GEN-BEGIN:MVDGetBegin38
     * @return Instance for ResultTableModel component
     */
    public org.netbeans.microedition.lcdui.SimpleTableModel get_ResultTableModel() {
        if (ResultTableModel == null) {//GEN-END:MVDGetBegin38
            // Insert pre-init code here
            ResultTableModel = new org.netbeans.microedition.lcdui.SimpleTableModel();//GEN-LINE:MVDGetInit38
            // Insert post-init code here
            if(selectResult != null){
                ResultTableModel.setColumnNames(selectResult.getColumnNames());
                ResultTableModel.setValues(selectResult.getStringResult());
            }
            
            
        }//GEN-BEGIN:MVDGetEnd38
        return ResultTableModel;
    }//GEN-END:MVDGetEnd38

    /** This method returns instance for CRecollirResult component and should be called instead of accessing CRecollirResult field directly.//GEN-BEGIN:MVDGetBegin39
     * @return Instance for CRecollirResult component
     */
    public Command get_CRecollirResult() {
        if (CRecollirResult == null) {//GEN-END:MVDGetBegin39
            // Insert pre-init code here
            CRecollirResult = new Command("Recollir resultat", Command.OK, 1);//GEN-LINE:MVDGetInit39
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd39
        return CRecollirResult;
    }//GEN-END:MVDGetEnd39

    /** This method returns instance for CTancarBD component and should be called instead of accessing CTancarBD field directly.//GEN-BEGIN:MVDGetBegin42
     * @return Instance for CTancarBD component
     */
    public Command get_CTancarBD() {
        if (CTancarBD == null) {//GEN-END:MVDGetBegin42
            // Insert pre-init code here
            CTancarBD = new Command("Back", Command.BACK, 1);//GEN-LINE:MVDGetInit42
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd42
        return CTancarBD;
    }//GEN-END:MVDGetEnd42

    /** This method returns instance for stringItem1 component and should be called instead of accessing stringItem1 field directly.//GEN-BEGIN:MVDGetBegin44
     * @return Instance for stringItem1 component
     */
    public StringItem get_stringItem1() {
        if (stringItem1 == null) {//GEN-END:MVDGetBegin44
            // Insert pre-init code here
            stringItem1 = new StringItem("Estat:", "<Enter Text>");//GEN-LINE:MVDGetInit44
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd44
        return stringItem1;
    }//GEN-END:MVDGetEnd44

    /** This method returns instance for SStateBDResult component and should be called instead of accessing SStateBDResult field directly.//GEN-BEGIN:MVDGetBegin45
     * @return Instance for SStateBDResult component
     */
    public StringItem get_SStateBDResult() {
        if (SStateBDResult == null) {//GEN-END:MVDGetBegin45
            // Insert pre-init code here
            SStateBDResult = new StringItem("Sentencia:", "");//GEN-LINE:MVDGetInit45
            // Insert post-init code here
            if(selectResult != null){
                SStateBDResult.setText(selectResult.getStatment());
            }
            
        }//GEN-BEGIN:MVDGetEnd45
        return SStateBDResult;
    }//GEN-END:MVDGetEnd45

    /** This method returns instance for spacer1 component and should be called instead of accessing spacer1 field directly.//GEN-BEGIN:MVDGetBegin46
     * @return Instance for spacer1 component
     */
    public Spacer get_spacer1() {
        if (spacer1 == null) {//GEN-END:MVDGetBegin46
            // Insert pre-init code here
            spacer1 = new Spacer(1000, 1);//GEN-LINE:MVDGetInit46
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd46
        return spacer1;
    }//GEN-END:MVDGetEnd46

    /** This method returns instance for TBD component and should be called instead of accessing TBD field directly.//GEN-BEGIN:MVDGetBegin47
     * @return Instance for TBD component
     */
    public TextField get_TBD() {
        if (TBD == null) {//GEN-END:MVDGetBegin47
            // Insert pre-init code here
            TBD = new TextField("Bd", "proves2", 120, TextField.ANY);//GEN-LINE:MVDGetInit47
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd47
        return TBD;
    }//GEN-END:MVDGetEnd47

    /** This method returns instance for TSGBD component and should be called instead of accessing TSGBD field directly.//GEN-BEGIN:MVDGetBegin48
     * @return Instance for TSGBD component
     */
    public TextField get_TSGBD() {
        if (TSGBD == null) {//GEN-END:MVDGetBegin48
            // Insert pre-init code here
            TSGBD = new TextField("SGBD", "0", 120, TextField.ANY);//GEN-LINE:MVDGetInit48
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd48
        return TSGBD;
    }//GEN-END:MVDGetEnd48

    /** This method returns instance for TIP component and should be called instead of accessing TIP field directly.//GEN-BEGIN:MVDGetBegin50
     * @return Instance for TIP component
     */
    public TextField get_TIP() {
        if (TIP == null) {//GEN-END:MVDGetBegin50
            // Insert pre-init code here
            TIP = new TextField("IP:", "127.0.0.1:8080", 120, TextField.ANY);//GEN-LINE:MVDGetInit50
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd50
        return TIP;
    }//GEN-END:MVDGetEnd50
    
    
    public void startApp() {
        try{
            HttpConnection conn = (HttpConnection)Connector.open("http://127.0.0.1:8080/J2MEServer/servlets/servlet/BDServer?hello");
        }
        catch(Exception e){
            
        }
        getDisplay().setCurrent(get_FBDDisp()); 	
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
 
    private void showError(String error){    
        Alert a = new Alert("");
        a.setString(error);
        a.setTimeout(Alert.FOREVER);
        a.setType(AlertType.WARNING);
        getDisplay().setCurrent(a);    
        destroyApp(false);
    }


}
