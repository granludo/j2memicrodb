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

package edu.upc.test;

import edu.upc.fileUtil.backupRestore;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;


public class backupRestoreTest extends MIDlet implements CommandListener {
    
    /** Creates a new instance of backupRestoreTest */
    public backupRestoreTest() {
        initialize();
    }
    
    private Form FPrin;//GEN-BEGIN:MVDFields
    private StringItem stringItem1;
    private StringItem stringItem2;
    private Command Backup;
    private Command Restore;
    private Form FBackup;
    private Form FRestore;
    private StringItem stringItem3;
    private TextField TBackupPath;
    private Command okBackup;
    private Command backBackup;
    private StringItem stringItem4;
    private TextField TRestorePath;
    private Command okRestore;
    private Command backRestore;
    private Command exitFprin;//GEN-END:MVDFields
    
    private backupRestoreThread b;
    
    
//GEN-LINE:MVDMethods

    /** Called by the system to indicate that a command has been invoked on a particular displayable.//GEN-BEGIN:MVDCABegin
     * @param command the Command that ws invoked
     * @param displayable the Displayable on which the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:MVDCABegin
    // Insert global pre-action code here
        if (displayable == FPrin) {//GEN-BEGIN:MVDCABody
            if (command == Backup) {//GEN-END:MVDCABody
                // Insert pre-action code here
                getDisplay().setCurrent(get_FBackup());//GEN-LINE:MVDCAAction7
                // Insert post-action code here
            } else if (command == Restore) {//GEN-LINE:MVDCACase7
                // Insert pre-action code here
                getDisplay().setCurrent(get_FRestore());//GEN-LINE:MVDCAAction8
                // Insert post-action code here
            } else if (command == exitFprin) {//GEN-LINE:MVDCACase8
                // Insert pre-action code here
                exitMIDlet();//GEN-LINE:MVDCAAction24
                // Insert post-action code here
            }//GEN-BEGIN:MVDCACase24
        } else if (displayable == FBackup) {
            if (command == backBackup) {//GEN-END:MVDCACase24
                // Insert pre-action code here
                getDisplay().setCurrent(get_FPrin());//GEN-LINE:MVDCAAction16
                // Insert post-action code here
            } else if (command == okBackup) {//GEN-LINE:MVDCACase16
                // Insert pre-action code here
                b = new backupRestoreThread(true,TBackupPath.getString(),this);
                b.start();
                
                
                // Do nothing//GEN-LINE:MVDCAAction15
                // Insert post-action code here
            }//GEN-BEGIN:MVDCACase15
        } else if (displayable == FRestore) {
            if (command == backRestore) {//GEN-END:MVDCACase15
                // Insert pre-action code here
                getDisplay().setCurrent(get_FPrin());//GEN-LINE:MVDCAAction22
                // Insert post-action code here
            } else if (command == okRestore) {//GEN-LINE:MVDCACase22
                // Insert pre-action code here
                
                    b = new backupRestoreThread(false,TRestorePath.getString(),this);
                    b.start();
                // Do nothing//GEN-LINE:MVDCAAction21
                // Insert post-action code here
            }//GEN-BEGIN:MVDCACase21
        }//GEN-END:MVDCACase21
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

    /** This method returns instance for FPrin component and should be called instead of accessing FPrin field directly.//GEN-BEGIN:MVDGetBegin2
     * @return Instance for FPrin component
     */
    public Form get_FPrin() {
        if (FPrin == null) {//GEN-END:MVDGetBegin2
            // Insert pre-init code here
            FPrin = new Form(null, new Item[] {//GEN-BEGIN:MVDGetInit2
                get_stringItem1(),
                get_stringItem2()
            });
            FPrin.addCommand(get_Backup());
            FPrin.addCommand(get_Restore());
            FPrin.addCommand(get_exitFprin());
            FPrin.setCommandListener(this);//GEN-END:MVDGetInit2
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd2
        return FPrin;
    }//GEN-END:MVDGetEnd2

    /** This method returns instance for stringItem1 component and should be called instead of accessing stringItem1 field directly.//GEN-BEGIN:MVDGetBegin3
     * @return Instance for stringItem1 component
     */
    public StringItem get_stringItem1() {
        if (stringItem1 == null) {//GEN-END:MVDGetBegin3
            // Insert pre-init code here
            stringItem1 = new StringItem("Menu-1:", "Fer Backup");//GEN-LINE:MVDGetInit3
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd3
        return stringItem1;
    }//GEN-END:MVDGetEnd3

    /** This method returns instance for stringItem2 component and should be called instead of accessing stringItem2 field directly.//GEN-BEGIN:MVDGetBegin4
     * @return Instance for stringItem2 component
     */
    public StringItem get_stringItem2() {
        if (stringItem2 == null) {//GEN-END:MVDGetBegin4
            // Insert pre-init code here
            stringItem2 = new StringItem("Menu-2:", "Fer Restore");//GEN-LINE:MVDGetInit4
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd4
        return stringItem2;
    }//GEN-END:MVDGetEnd4

    /** This method returns instance for Backup component and should be called instead of accessing Backup field directly.//GEN-BEGIN:MVDGetBegin5
     * @return Instance for Backup component
     */
    public Command get_Backup() {
        if (Backup == null) {//GEN-END:MVDGetBegin5
            // Insert pre-init code here
            Backup = new Command("Backup", Command.OK, 1);//GEN-LINE:MVDGetInit5
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd5
        return Backup;
    }//GEN-END:MVDGetEnd5

    /** This method returns instance for Restore component and should be called instead of accessing Restore field directly.//GEN-BEGIN:MVDGetBegin6
     * @return Instance for Restore component
     */
    public Command get_Restore() {
        if (Restore == null) {//GEN-END:MVDGetBegin6
            // Insert pre-init code here
            Restore = new Command("Restore", Command.OK, 1);//GEN-LINE:MVDGetInit6
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd6
        return Restore;
    }//GEN-END:MVDGetEnd6

    /** This method returns instance for FBackup component and should be called instead of accessing FBackup field directly.//GEN-BEGIN:MVDGetBegin9
     * @return Instance for FBackup component
     */
    public Form get_FBackup() {
        if (FBackup == null) {//GEN-END:MVDGetBegin9
            // Insert pre-init code here
            FBackup = new Form(null, new Item[] {//GEN-BEGIN:MVDGetInit9
                get_stringItem3(),
                get_TBackupPath()
            });
            FBackup.addCommand(get_okBackup());
            FBackup.addCommand(get_backBackup());
            FBackup.setCommandListener(this);//GEN-END:MVDGetInit9
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd9
        return FBackup;
    }//GEN-END:MVDGetEnd9

    /** This method returns instance for FRestore component and should be called instead of accessing FRestore field directly.//GEN-BEGIN:MVDGetBegin10
     * @return Instance for FRestore component
     */
    public Form get_FRestore() {
        if (FRestore == null) {//GEN-END:MVDGetBegin10
            // Insert pre-init code here
            FRestore = new Form(null, new Item[] {//GEN-BEGIN:MVDGetInit10
                get_stringItem4(),
                get_TRestorePath()
            });
            FRestore.addCommand(get_okRestore());
            FRestore.addCommand(get_backRestore());
            FRestore.setCommandListener(this);//GEN-END:MVDGetInit10
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd10
        return FRestore;
    }//GEN-END:MVDGetEnd10

    /** This method returns instance for stringItem3 component and should be called instead of accessing stringItem3 field directly.//GEN-BEGIN:MVDGetBegin11
     * @return Instance for stringItem3 component
     */
    public StringItem get_stringItem3() {
        if (stringItem3 == null) {//GEN-END:MVDGetBegin11
            // Insert pre-init code here
            stringItem3 = new StringItem("Backup", "");//GEN-LINE:MVDGetInit11
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd11
        return stringItem3;
    }//GEN-END:MVDGetEnd11

    /** This method returns instance for TBackupPath component and should be called instead of accessing TBackupPath field directly.//GEN-BEGIN:MVDGetBegin12
     * @return Instance for TBackupPath component
     */
    public TextField get_TBackupPath() {
        if (TBackupPath == null) {//GEN-END:MVDGetBegin12
            // Insert pre-init code here
            TBackupPath = new TextField("Directori dest\u00ED", "SDCard/RecordStores/", 120, TextField.ANY);//GEN-LINE:MVDGetInit12
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd12
        return TBackupPath;
    }//GEN-END:MVDGetEnd12

    /** This method returns instance for okBackup component and should be called instead of accessing okBackup field directly.//GEN-BEGIN:MVDGetBegin13
     * @return Instance for okBackup component
     */
    public Command get_okBackup() {
        if (okBackup == null) {//GEN-END:MVDGetBegin13
            // Insert pre-init code here
            okBackup = new Command("Ok", Command.OK, 1);//GEN-LINE:MVDGetInit13
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd13
        return okBackup;
    }//GEN-END:MVDGetEnd13

    /** This method returns instance for backBackup component and should be called instead of accessing backBackup field directly.//GEN-BEGIN:MVDGetBegin14
     * @return Instance for backBackup component
     */
    public Command get_backBackup() {
        if (backBackup == null) {//GEN-END:MVDGetBegin14
            // Insert pre-init code here
            backBackup = new Command("Back", Command.BACK, 1);//GEN-LINE:MVDGetInit14
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd14
        return backBackup;
    }//GEN-END:MVDGetEnd14

    /** This method returns instance for stringItem4 component and should be called instead of accessing stringItem4 field directly.//GEN-BEGIN:MVDGetBegin17
     * @return Instance for stringItem4 component
     */
    public StringItem get_stringItem4() {
        if (stringItem4 == null) {//GEN-END:MVDGetBegin17
            // Insert pre-init code here
            stringItem4 = new StringItem("Restore", "");//GEN-LINE:MVDGetInit17
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd17
        return stringItem4;
    }//GEN-END:MVDGetEnd17

    /** This method returns instance for TRestorePath component and should be called instead of accessing TRestorePath field directly.//GEN-BEGIN:MVDGetBegin18
     * @return Instance for TRestorePath component
     */
    public TextField get_TRestorePath() {
        if (TRestorePath == null) {//GEN-END:MVDGetBegin18
            // Insert pre-init code here
            TRestorePath = new TextField("Directori font:", "SDCard/RecordStores/", 120, TextField.ANY);//GEN-LINE:MVDGetInit18
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd18
        return TRestorePath;
    }//GEN-END:MVDGetEnd18

    /** This method returns instance for okRestore component and should be called instead of accessing okRestore field directly.//GEN-BEGIN:MVDGetBegin19
     * @return Instance for okRestore component
     */
    public Command get_okRestore() {
        if (okRestore == null) {//GEN-END:MVDGetBegin19
            // Insert pre-init code here
            okRestore = new Command("Ok", Command.OK, 1);//GEN-LINE:MVDGetInit19
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd19
        return okRestore;
    }//GEN-END:MVDGetEnd19

    /** This method returns instance for backRestore component and should be called instead of accessing backRestore field directly.//GEN-BEGIN:MVDGetBegin20
     * @return Instance for backRestore component
     */
    public Command get_backRestore() {
        if (backRestore == null) {//GEN-END:MVDGetBegin20
            // Insert pre-init code here
            backRestore = new Command("Back", Command.BACK, 1);//GEN-LINE:MVDGetInit20
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd20
        return backRestore;
    }//GEN-END:MVDGetEnd20

    /** This method returns instance for exitFprin component and should be called instead of accessing exitFprin field directly.//GEN-BEGIN:MVDGetBegin23
     * @return Instance for exitFprin component
     */
    public Command get_exitFprin() {
        if (exitFprin == null) {//GEN-END:MVDGetBegin23
            // Insert pre-init code here
            exitFprin = new Command("Exit", Command.EXIT, 1);//GEN-LINE:MVDGetInit23
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd23
        return exitFprin;
    }//GEN-END:MVDGetEnd23
    
    public void startApp() {
        getDisplay().setCurrent(get_FPrin()); 
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
    
    public void notificar(boolean error, boolean backup){
        if(error){
            if(backup){
                showError("Error al fer backup",FPrin);
            }
            else{
                showError("Error al fer restore",FPrin);
            }
        }
        else{
            if(backup){
                showError("El backup ha estat realitzat correctament",FPrin);
            }
            else{
                showError("El restore ha estat realitzat correctament",FPrin);
            }
        }
    }
    
    private void showError(String error, Displayable d){
        Alert a = new Alert("");
        a.setString(error);
        a.setTimeout(Alert.FOREVER);
        a.setType(AlertType.WARNING);
        getDisplay().setCurrent(a,d);
        destroyApp(false);
    }
    
}
