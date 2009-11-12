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
 */package edu.upc.J2MESDLIB;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.Vector;
import java.io.*;

/**
 * Midlet per accedir a les dades dels Logs de la API
 * @author Juan Antonio Sosa && Mariona Fecundez
 */
public class LogInspector extends MIDlet implements CommandListener {
    
    private String selectedLogDate;
    private J2METable table;
    
    /** Crea una nova instància de LogInspector*/
    public LogInspector() {
        initialize();        
    }
    
    private Form fInici;//GEN-BEGIN:MVDFields
    private Command cmdExit;
    private ChoiceGroup cgLogDate;
    private ChoiceGroup cgAction;
    private Spacer spacer1;
    private Command cmdAction;
    private List lLogs;
    private Command Enrere;
    private ChoiceGroup cgType;
    private Spacer spacer2;
    private Spacer spacer3;
    private Alert aConfirmation;
    private Alert aWarning;
    private Command cmdConfirmOK;
    private Command cmdConfirmCancel;//GEN-END:MVDFields
    
//GEN-LINE:MVDMethods

    /** Called by the system to indicate that a command has been invoked on a particular displayable.//GEN-BEGIN:MVDCABegin
     * @param command the Command that ws invoked
     * @param displayable the Displayable on which the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:MVDCABegin
    // Insert global pre-action code here
        if (displayable == fInici) {//GEN-BEGIN:MVDCABody
            if (command == cmdExit) {//GEN-END:MVDCABody
                // Insert pre-action code here
                exitMIDlet();//GEN-LINE:MVDCAAction4
                // Insert post-action code here
            } else if (command == cmdAction) {//GEN-LINE:MVDCACase4
                // Insert pre-action code here
                getDisplay().setCurrent(get_lLogs());//GEN-LINE:MVDCAAction15
                executeAction();
                // Insert post-action code here
            }//GEN-BEGIN:MVDCACase15
        } else if (displayable == lLogs) {
            if (command == cmdAction) {//GEN-END:MVDCACase15
                // Insert pre-action code here
                // Do nothing//GEN-LINE:MVDCAAction19
                // Insert post-action code here
            } else if (command == Enrere) {//GEN-LINE:MVDCACase19
                // Insert pre-action code here
                getDisplay().setCurrent(get_fInici());//GEN-LINE:MVDCAAction21
                // Insert post-action code here
            }//GEN-BEGIN:MVDCACase21
        } else if (displayable == aConfirmation) {
            if (command == cmdConfirmOK) {//GEN-END:MVDCACase21
                // Insert pre-action code here                           
                getDisplay().setCurrent(get_fInici());//GEN-LINE:MVDCAAction33
                // Insert post-action code here
                esborrarLog();
            } else if (command == cmdConfirmCancel) {//GEN-LINE:MVDCACase33
                // Insert pre-action code here
                getDisplay().setCurrent(get_fInici());//GEN-LINE:MVDCAAction34
                // Insert post-action code here
            }//GEN-BEGIN:MVDCACase34
        }//GEN-END:MVDCACase34
    // Insert global post-action code here
}//GEN-LINE:MVDCAEnd

    /** This method initializes UI of the application.//GEN-BEGIN:MVDInitBegin
     */
    private void initialize() {//GEN-END:MVDInitBegin
        // Insert pre-init code here
        J2MESDLIB.generateDB(true);
        aWarning = new Alert("Warning", "<Enter Text>", null, AlertType.WARNING);//GEN-BEGIN:MVDInitInit
        aWarning.setTimeout(-2);
        aConfirmation = new Alert("Confirm", "<Enter Text>", null, AlertType.CONFIRMATION);
        aConfirmation.addCommand(get_cmdConfirmOK());
        aConfirmation.addCommand(get_cmdConfirmCancel());
        aConfirmation.setCommandListener(this);
        aConfirmation.setTimeout(-2);
        getDisplay().setCurrent(get_fInici());//GEN-END:MVDInitInit
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
        J2MESDLIB.closeDB();
    }//GEN-LAST:MVDExitMidlet

    /** This method returns instance for fInici component and should be called instead of accessing fInici field directly.//GEN-BEGIN:MVDGetBegin2
     * @return Instance for fInici component
     */
    public Form get_fInici() {
        if (fInici == null) {//GEN-END:MVDGetBegin2
            // Insert pre-init code here
            fInici = new Form("Log Inspector", new Item[] {//GEN-BEGIN:MVDGetInit2
                get_spacer3(),
                get_cgLogDate(),
                get_spacer1(),
                get_cgAction(),
                get_spacer2(),
                get_cgType()
            });
            fInici.addCommand(get_cmdExit());
            fInici.addCommand(get_cmdAction());
            fInici.setCommandListener(this);//GEN-END:MVDGetInit2
            // Insert post-init code here
            this.loadLogDates();
        }//GEN-BEGIN:MVDGetEnd2
        return fInici;
    }//GEN-END:MVDGetEnd2

    /** This method returns instance for cmdExit component and should be called instead of accessing cmdExit field directly.//GEN-BEGIN:MVDGetBegin3
     * @return Instance for cmdExit component
     */
    public Command get_cmdExit() {
        if (cmdExit == null) {//GEN-END:MVDGetBegin3
            // Insert pre-init code here
            cmdExit = new Command("Exit", Command.EXIT, 1);//GEN-LINE:MVDGetInit3
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd3
        return cmdExit;
    }//GEN-END:MVDGetEnd3

    /** This method returns instance for cgLogDate component and should be called instead of accessing cgLogDate field directly.//GEN-BEGIN:MVDGetBegin5
     * @return Instance for cgLogDate component
     */
    public ChoiceGroup get_cgLogDate() {
        if (cgLogDate == null) {//GEN-END:MVDGetBegin5
            // Insert pre-init code here
            cgLogDate = new ChoiceGroup("Choose log date:", Choice.POPUP, new String[] {"Choice Element"}, new Image[] {null});//GEN-BEGIN:MVDGetInit5
            cgLogDate.setSelectedFlags(new boolean[] {false});//GEN-END:MVDGetInit5
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd5
        return cgLogDate;
    }//GEN-END:MVDGetEnd5

    /** This method returns instance for cgAction component and should be called instead of accessing cgAction field directly.//GEN-BEGIN:MVDGetBegin7
     * @return Instance for cgAction component
     */
    public ChoiceGroup get_cgAction() {
        if (cgAction == null) {//GEN-END:MVDGetBegin7
            // Insert pre-init code here
            cgAction = new ChoiceGroup("Action to do:", Choice.EXCLUSIVE, new String[] {//GEN-BEGIN:MVDGetInit7
                "View",
                "Delete (selected log)"
            }, new Image[] {
                null,
                null
            });
            cgAction.setSelectedFlags(new boolean[] {
                true,
                false
            });//GEN-END:MVDGetInit7
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd7
        return cgAction;
    }//GEN-END:MVDGetEnd7

    /** This method returns instance for spacer1 component and should be called instead of accessing spacer1 field directly.//GEN-BEGIN:MVDGetBegin8
     * @return Instance for spacer1 component
     */
    public Spacer get_spacer1() {
        if (spacer1 == null) {//GEN-END:MVDGetBegin8
            // Insert pre-init code here
            spacer1 = new Spacer(1000, 40);//GEN-LINE:MVDGetInit8
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd8
        return spacer1;
    }//GEN-END:MVDGetEnd8

    /** This method returns instance for cmdAction component and should be called instead of accessing cmdAction field directly.//GEN-BEGIN:MVDGetBegin14
     * @return Instance for cmdAction component
     */
    public Command get_cmdAction() {
        if (cmdAction == null) {//GEN-END:MVDGetBegin14
            // Insert pre-init code here
            cmdAction = new Command("Do Action", Command.OK, 1);//GEN-LINE:MVDGetInit14
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd14
        return cmdAction;
    }//GEN-END:MVDGetEnd14

    /** This method returns instance for lLogs component and should be called instead of accessing lLogs field directly.//GEN-BEGIN:MVDGetBegin16
     * @return Instance for lLogs component
     */
    public List get_lLogs() {
        if (lLogs == null) {//GEN-END:MVDGetBegin16
            // Insert pre-init code here
            lLogs = new List(null, Choice.IMPLICIT, new String[0], new Image[0]);//GEN-BEGIN:MVDGetInit16
            lLogs.addCommand(get_cmdAction());
            lLogs.addCommand(get_Enrere());
            lLogs.setCommandListener(this);
            lLogs.setSelectedFlags(new boolean[0]);//GEN-END:MVDGetInit16
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd16
        return lLogs;
    }//GEN-END:MVDGetEnd16

    /** This method returns instance for Enrere component and should be called instead of accessing Enrere field directly.//GEN-BEGIN:MVDGetBegin20
     * @return Instance for Enrere component
     */
    public Command get_Enrere() {
        if (Enrere == null) {//GEN-END:MVDGetBegin20
            // Insert pre-init code here
            Enrere = new Command("Enrere", Command.BACK, 1);//GEN-LINE:MVDGetInit20
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd20
        return Enrere;
    }//GEN-END:MVDGetEnd20

    /** This method returns instance for cgType component and should be called instead of accessing cgType field directly.//GEN-BEGIN:MVDGetBegin24
     * @return Instance for cgType component
     */
    public ChoiceGroup get_cgType() {
        if (cgType == null) {//GEN-END:MVDGetBegin24
            // Insert pre-init code here
            cgType = new ChoiceGroup("Choose log types to view:", Choice.MULTIPLE, new String[] {//GEN-BEGIN:MVDGetInit24
                "Action events",
                "Exceptions"
            }, new Image[] {
                null,
                null
            });
            cgType.setSelectedFlags(new boolean[] {
                true,
                true
            });//GEN-END:MVDGetInit24
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd24
        return cgType;
    }//GEN-END:MVDGetEnd24

    /** This method returns instance for spacer2 component and should be called instead of accessing spacer2 field directly.//GEN-BEGIN:MVDGetBegin25
     * @return Instance for spacer2 component
     */
    public Spacer get_spacer2() {
        if (spacer2 == null) {//GEN-END:MVDGetBegin25
            // Insert pre-init code here
            spacer2 = new Spacer(1000, 20);//GEN-LINE:MVDGetInit25
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd25
        return spacer2;
    }//GEN-END:MVDGetEnd25

    /** This method returns instance for spacer3 component and should be called instead of accessing spacer3 field directly.//GEN-BEGIN:MVDGetBegin28
     * @return Instance for spacer3 component
     */
    public Spacer get_spacer3() {
        if (spacer3 == null) {//GEN-END:MVDGetBegin28
            // Insert pre-init code here
            spacer3 = new Spacer(1000, 10);//GEN-LINE:MVDGetInit28
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd28
        return spacer3;
    }//GEN-END:MVDGetEnd28

    /** This method returns instance for cmdConfirmOK component and should be called instead of accessing cmdConfirmOK field directly.//GEN-BEGIN:MVDGetBegin31
     * @return Instance for cmdConfirmOK component
     */
    public Command get_cmdConfirmOK() {
        if (cmdConfirmOK == null) {//GEN-END:MVDGetBegin31
            // Insert pre-init code here
            cmdConfirmOK = new Command("Ok", Command.OK, 1);//GEN-LINE:MVDGetInit31
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd31
        return cmdConfirmOK;
    }//GEN-END:MVDGetEnd31

    /** This method returns instance for cmdConfirmCancel component and should be called instead of accessing cmdConfirmCancel field directly.//GEN-BEGIN:MVDGetBegin32
     * @return Instance for cmdConfirmCancel component
     */
    public Command get_cmdConfirmCancel() {
        if (cmdConfirmCancel == null) {//GEN-END:MVDGetBegin32
            // Insert pre-init code here
            cmdConfirmCancel = new Command("Cancel", Command.CANCEL, 1);//GEN-LINE:MVDGetInit32
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd32
        return cmdConfirmCancel;
    }//GEN-END:MVDGetEnd32
     
    public void startApp() {
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
    
    private void loadLogDates (){
        
        Vector v = new Vector();
        String sDate;
        
        cgLogDate.deleteAll();
        try{
            
            v = J2MESDLIB.getLogs();
            
            for (int i=0; i<v.size(); i++){
                sDate = v.elementAt(i).toString().substring(J2MELog.logDefaultName.length());
                cgLogDate.append(sDate, null);
            }
        }
       catch (J2MEDBException j2ex){
            showWarning("Error", "Error al carregar les dates dels Logs: " +  j2ex.getUsermessage());
       }
    }
    
    private void veureLogs(int tipusLogs){
        //tipusLogs==0 ACTION
        //tipusLogs==1 EXCEPTION
        //tipusLogs==2 ALL
        J2MERow r;
        
        J2METable t = loadSelectedLogDate();
        lLogs.deleteAll();
        try{
            t.first();

            while (!t.EOF()){
                r = t.read();
                if(tipusLogs==2 || r.getColValue(J2MELog.LogTypeColumn).equals(String.valueOf(tipusLogs)))
                {
                    lLogs.append(r.printRow(), null);
                }
                t.next();
            }            
        }
        catch (J2MEDBException j2ex){
            showWarning("Error", "Error al veure els logs: " + j2ex.getUsermessage());
        }
        finally{
            System.out.println("Tanquem la taula");
            t.closeTable();
        }
    }
    
    private J2METable loadSelectedLogDate(){
        selectedLogDate = J2MELog.logDefaultName+cgLogDate.getString(cgLogDate.getSelectedIndex());
        
        try{
            table = J2MESDLIB.loadTable(selectedLogDate);
        }
        catch(J2MEDBException j2ex){
            //problemes al carregar la taula de log
            showWarning("Error", "ExcepciÃ³ al carregar la taula de Log: " + j2ex.getUsermessage());
        }
        return table;
    }
    
    private void executeAction(){
        int tipusLog = -1;
        
        if (cgAction.getSelectedIndex()==0){
            if (cgType.isSelected(0) && cgType.isSelected(1)) tipusLog=2;
            else if (cgType.isSelected(0)) tipusLog=0;
            else if (cgType.isSelected(1)) tipusLog=1;
            if (tipusLog==-1) showWarning("Warning","Must select one log type at least.");
            veureLogs(tipusLog);
        }
        else
            showConfirm("Sure to delete "+selectedLogDate+"?");
    }
    
    private void showWarning (String title, String message){
        aWarning.setTitle(title);
        aWarning.setString(message);
        getDisplay().setCurrent(aWarning);
    }
    
    private void showConfirm(String message){
        aConfirmation.setString(message);
        getDisplay().setCurrent(aConfirmation);
    }
    
    private void esborrarLog(){
        
        try{
            J2MESDLIB.dropTable(this.selectedLogDate);
            //showWarning("Info","S'ha esborrat correctament la taula de Logs");
            loadLogDates();
        }
        catch(J2MEDBException ex){
            showWarning("Error","No s'ha pogut esborrar la taula de logs correctament: " + ex.getUsermessage());
        }
    }
}
