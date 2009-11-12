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

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import edu.upc.lsdatalib.fitxersequencialvalor.*;
import edu.upc.lsdatalib.comunsfitxer.PDAException;
import java.util.Vector;

/**
 *
 * @author se
 */
public class ContactMidlet extends MIDlet implements CommandListener {
    
    /** Creates a new instance of HelloMidlet */
    public ContactMidlet() {
    }
    
    private Command exitCommand;//GEN-BEGIN:MVDFields
    private Command addContact;
    private Command EditContacts;
    private Form formNewContact;
    private TextField textFieldName;
    private TextField textFieldPhone;
    private Command cancelCommand1;
    private List listContactes;
    private Command okCommand2;
    private Command deleteContactsCommand;
    private Command okCommand1;
    private Command viewContactsCommand;
    private Form formVewContactData;
    private StringItem stringItemName;
    private StringItem stringItemPhone;
    private Command cancelCommand2;
    private Command okCommand3;
    private Command okCommand4;//GEN-END:MVDFields
    private ManageContacts contacts;
    private PDALabeledStoreSequence fitxerContactes;
    private List listContactesCerca;
//GEN-LINE:MVDMethods

    /** This method initializes UI of the application.//GEN-BEGIN:MVDInitBegin
     */
    private void initialize() {//GEN-END:MVDInitBegin
       try{
          listContactes=get_listContactes();
          initContactFile();
          showContactList();
          getDisplay().setCurrent(listContactes);                      
        }catch(PDAException e){
            showAlert(e.getUsermessage());
        }
    }
    
    /** Called by the system to indicate that a command has been invoked on a particular displayable.//GEN-BEGIN:MVDCABegin
     * @param command the Command that ws invoked
     * @param displayable the Displayable on which the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:MVDCABegin
        // Create a new Contact
        if (displayable == formNewContact) {//GEN-BEGIN:MVDCABody
            if (command == cancelCommand1) {//GEN-END:MVDCABody
                // Cancel create new Contact
                getDisplay().setCurrent(get_listContactes());//GEN-LINE:MVDCAAction20
                // Return to main screen
            } else if (command == okCommand1) {//GEN-LINE:MVDCACase20
                // Show list of contacts with the new contact at the end
                try{
                    contacts.NewContact(textFieldName.getString(),textFieldPhone.getString());
                    showContactList();
                    getDisplay().setCurrent(get_listContactes());
                    
                }catch(PDAException e){
                    showAlert(e.getUsermessage());
                }/*
getDisplay ().setCurrent (get_listContactes());//GEN-LINE:MVDCAAction38
                */// Insert post-action code here
            }//GEN-BEGIN:MVDCACase38
        } else if (displayable == listContactes) {
            if (command == addContact) {//GEN-END:MVDCACase38
                // Show Form for adding a new contact
                getDisplay().setCurrent(get_formNewContact());//GEN-LINE:MVDCAAction30
                // Insert post-action code here                
            } else if (command == exitCommand) {//GEN-LINE:MVDCACase30
                // Exit application
                exitMIDlet();//GEN-LINE:MVDCAAction29
                // Insert post-action code here
            } else if (command == deleteContactsCommand) {//GEN-LINE:MVDCACase29
                // Delete contact file
                // Do nothing//GEN-LINE:MVDCAAction33
                // Insert post-action code here
                try{
                   contacts.dropContacts();
                   showContactList();
                }catch (PDAException e){
                    showAlert(e.getUsermessage());
                }
            } else if (command == viewContactsCommand) {//GEN-LINE:MVDCACase33
                // View the data of the selected contact
                    String selection=listContactes.getString(listContactes.getSelectedIndex());
                    try{
                    Contact cdata=contacts.getContact(selection);
                    getDisplay().setCurrent(get_formVewContactData());//GEN-LINE:MVDCAAction42
                    }catch (PDAException e){
                        showAlert(e.getUsermessage());
                    }
                 // Insert post-action code here
            }//GEN-BEGIN:MVDCACase42
        } else if (displayable == formVewContactData) {
            if (command == okCommand3) {//GEN-END:MVDCACase42
                // 
                
                getDisplay().setCurrent(get_listContactes());//GEN-LINE:MVDCAAction51
                // Insert post-action code here
            } else if (command == okCommand4) {//GEN-LINE:MVDCACase51
                // Delete the contact shown on screen and return to main screen
                try{
                contacts.deleteContact(stringItemName.getText());
                showContactList();
                getDisplay().setCurrent(get_listContactes());//GEN-LINE:MVDCAAction53
                }catch (PDAException e){
                    showAlert(e.getUsermessage());
                }    
               // Insert post-action code here
            }//GEN-BEGIN:MVDCACase53
        }//GEN-END:MVDCACase53
        // Insert global post-action code here
}//GEN-LINE:MVDCAEnd
    
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
    
    
    
    /** This method returns instance for exitCommand component and should be called instead of accessing exitCommand field directly.//GEN-BEGIN:MVDGetBegin5
     * @return Instance for exitCommand component
     */
    public Command get_exitCommand() {
        if (exitCommand == null) {//GEN-END:MVDGetBegin5
            // Insert pre-init code here
            exitCommand = new Command("Exit", Command.EXIT, 1);//GEN-LINE:MVDGetInit5
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd5
        return exitCommand;
    }//GEN-END:MVDGetEnd5
    /** This method returns instance for addContact component and should be called instead of accessing addContact field directly.//GEN-BEGIN:MVDGetBegin7
     * @return Instance for addContact component
     */
    public Command get_addContact() {
        if (addContact == null) {//GEN-END:MVDGetBegin7
            // Insert pre-init code here
            addContact = new Command("Add Contact", Command.OK, 1);//GEN-LINE:MVDGetInit7
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd7
        return addContact;
    }//GEN-END:MVDGetEnd7

    /** This method returns instance for EditContacts component and should be called instead of accessing EditContacts field directly.//GEN-BEGIN:MVDGetBegin12
     * @return Instance for EditContacts component
     */
    public Command get_EditContacts() {
        if (EditContacts == null) {//GEN-END:MVDGetBegin12
            // Insert pre-init code here
            EditContacts = new Command("Edit Contact", Command.OK, 1);//GEN-LINE:MVDGetInit12
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd12
        return EditContacts;
    }//GEN-END:MVDGetEnd12

    /** This method returns instance for formNewContact component and should be called instead of accessing formNewContact field directly.                         
     * @return Instance for formNewContact component
     */
    public Form get_formNewContact() {
        if (formNewContact == null) {
            // Insert pre-init code here
            formNewContact = new Form(null, new Item[] {
                get_textFieldName(),
                get_textFieldPhone()
            });
            formNewContact.addCommand(get_cancelCommand1());
            formNewContact.addCommand(get_okCommand1());
            formNewContact.setCommandListener(this);
            // Insert post-init code here
        }else {
            textFieldName.setString("");
	    textFieldPhone.setString("");
        }
        return formNewContact;
    }

    /** This method returns instance for textFieldName component and should be called instead of accessing textFieldName field directly.//GEN-BEGIN:MVDGetBegin15
     * @return Instance for textFieldName component
     */
    public TextField get_textFieldName() {
        if (textFieldName == null) {//GEN-END:MVDGetBegin15
            // Insert pre-init code here
            textFieldName = new TextField("Name", null, 120, TextField.ANY);//GEN-LINE:MVDGetInit15
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd15
        return textFieldName;
    }//GEN-END:MVDGetEnd15

    /** This method returns instance for textFieldPhone component and should be called instead of accessing textFieldPhone field directly.//GEN-BEGIN:MVDGetBegin16
     * @return Instance for textFieldPhone component
     */
    public TextField get_textFieldPhone() {
        if (textFieldPhone == null) {//GEN-END:MVDGetBegin16
            // Insert pre-init code here
            textFieldPhone = new TextField("Phone", null, 120, TextField.ANY);//GEN-LINE:MVDGetInit16
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd16
        return textFieldPhone;
    }//GEN-END:MVDGetEnd16
 
    /** This method returns instance for cancelCommand1 component and should be called instead of accessing cancelCommand1 field directly.//GEN-BEGIN:MVDGetBegin19
     * @return Instance for cancelCommand1 component
     */
    public Command get_cancelCommand1() {
        if (cancelCommand1 == null) {//GEN-END:MVDGetBegin19
            // Insert pre-init code here
            cancelCommand1 = new Command("Cancel", Command.CANCEL, 1);//GEN-LINE:MVDGetInit19
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd19
        return cancelCommand1;
    }//GEN-END:MVDGetEnd19

    /** This method returns instance for listContactes component and should be called instead of accessing listContactes field directly.//GEN-BEGIN:MVDGetBegin26
     * @return Instance for listContactes component
     */
    public List get_listContactes() {
        if (listContactes == null) {//GEN-END:MVDGetBegin26
            // Insert pre-init code here
            listContactes = new List("List of contacts", Choice.IMPLICIT, new String[0], new Image[0]);//GEN-BEGIN:MVDGetInit26
            listContactes.addCommand(get_exitCommand());
            listContactes.addCommand(get_addContact());
            listContactes.addCommand(get_deleteContactsCommand());
            listContactes.addCommand(get_viewContactsCommand());
            listContactes.setCommandListener(this);
            listContactes.setSelectedFlags(new boolean[0]);//GEN-END:MVDGetInit26
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd26
        return listContactes;
    }//GEN-END:MVDGetEnd26

    /** This method returns instance for okCommand2 component and should be called instead of accessing okCommand2 field directly.//GEN-BEGIN:MVDGetBegin31
     * @return Instance for okCommand2 component
     */
    public Command get_okCommand2() {
        if (okCommand2 == null) {//GEN-END:MVDGetBegin31
            // Insert pre-init code here
            okCommand2 = new Command("Ok", Command.OK, 1);//GEN-LINE:MVDGetInit31
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd31
        return okCommand2;
    }//GEN-END:MVDGetEnd31

    /** This method returns instance for deleteContactsCommand component and should be called instead of accessing deleteContactsCommand field directly.//GEN-BEGIN:MVDGetBegin32
     * @return Instance for deleteContactsCommand component
     */
    public Command get_deleteContactsCommand() {
        if (deleteContactsCommand == null) {//GEN-END:MVDGetBegin32
            // Insert pre-init code here
            deleteContactsCommand = new Command("Delete Contacts", Command.OK, 1);//GEN-LINE:MVDGetInit32
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd32
        return deleteContactsCommand;
    }//GEN-END:MVDGetEnd32
 
    /** This method returns instance for okCommand1 component and should be called instead of accessing okCommand1 field directly.//GEN-BEGIN:MVDGetBegin37
     * @return Instance for okCommand1 component
     */
    public Command get_okCommand1() {
        if (okCommand1 == null) {//GEN-END:MVDGetBegin37
            // Insert pre-init code here
            okCommand1 = new Command("Add new Contact", Command.OK, 1);//GEN-LINE:MVDGetInit37
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd37
        return okCommand1;
    }//GEN-END:MVDGetEnd37

    /** This method returns instance for viewContactsCommand component and should be called instead of accessing viewContactsCommand field directly.//GEN-BEGIN:MVDGetBegin41
     * @return Instance for viewContactsCommand component
     */
    public Command get_viewContactsCommand() {
        if (viewContactsCommand == null) {//GEN-END:MVDGetBegin41
            // Insert pre-init code here
            viewContactsCommand = new Command("View Contact", Command.OK, 1);//GEN-LINE:MVDGetInit41
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd41
        return viewContactsCommand;
    }//GEN-END:MVDGetEnd41

    /** This method returns instance for formVewContactData component and should be called instead of accessing formVewContactData field directly.                         
     * @return Instance for formVewContactData component
     */
    public Form get_formVewContactData(Contact c) {
        if (formVewContactData == null) {
            // Insert pre-init code here
            formVewContactData = new Form(null, new Item[] {
                get_stringItemName(c.getName()),
                get_stringItemPhone(c.getPhone())
            });
            formVewContactData.addCommand(get_okCommand3());
            formVewContactData.addCommand(get_okCommand4());
            formVewContactData.setCommandListener(this);
            // Insert post-init code here
        }else {
             stringItemName.setText(c.getName());
             stringItemPhone.setText(c.getPhone());
        }
        return formVewContactData;
    }

    /** This method returns instance for stringItemName component and should be called instead of accessing stringItemName field directly.                         
     * @return Instance for stringItemName component
     */
    public StringItem get_stringItemName(String name) {
        if (stringItemName == null) {
            // Insert pre-init code here
            stringItemName = new StringItem("Name:", name);
            // Insert post-init code here
        }
        return stringItemName;
    }

    /** This method returns instance for stringItemPhone component and should be called instead of accessing stringItemPhone field directly.                         
     * @return Instance for stringItemPhone component
     */
    public StringItem get_stringItemPhone(String phone) {
        if (stringItemPhone == null) {
            // Insert pre-init code here
            stringItemPhone = new StringItem("Phone:", phone);
            // Insert post-init code here
        }
        return stringItemPhone;
    }

    /** This method returns instance for cancelCommand2 component and should be called instead of accessing cancelCommand2 field directly.//GEN-BEGIN:MVDGetBegin48
     * @return Instance for cancelCommand2 component
     */
    public Command get_cancelCommand2() {
        if (cancelCommand2 == null) {//GEN-END:MVDGetBegin48
            // Insert pre-init code here
            cancelCommand2 = new Command("Cancel", Command.CANCEL, 1);//GEN-LINE:MVDGetInit48
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd48
        return cancelCommand2;
    }//GEN-END:MVDGetEnd48

    /** This method returns instance for okCommand3 component and should be called instead of accessing okCommand3 field directly.//GEN-BEGIN:MVDGetBegin50
     * @return Instance for okCommand3 component
     */
    public Command get_okCommand3() {
        if (okCommand3 == null) {//GEN-END:MVDGetBegin50
            // Insert pre-init code here
            okCommand3 = new Command("Ok", Command.OK, 1);//GEN-LINE:MVDGetInit50
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd50
        return okCommand3;
    }//GEN-END:MVDGetEnd50

   

    /** This method returns instance for stringItemName component and should be called instead of accessing stringItemName field directly.//GEN-BEGIN:MVDGetBegin45
     * @return Instance for stringItemName component
     */
    public StringItem get_stringItemName() {
        if (stringItemName == null) {//GEN-END:MVDGetBegin45
            // Insert pre-init code here
            stringItemName = new StringItem("Name:", "<Enter Text>");//GEN-LINE:MVDGetInit45
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd45
        return stringItemName;
    }//GEN-END:MVDGetEnd45

    /** This method returns instance for stringItemPhone component and should be called instead of accessing stringItemPhone field directly.//GEN-BEGIN:MVDGetBegin46
     * @return Instance for stringItemPhone component
     */
    public StringItem get_stringItemPhone() {
        if (stringItemPhone == null) {//GEN-END:MVDGetBegin46
            // Insert pre-init code here
            stringItemPhone = new StringItem("Phone:", "<Enter Text>");//GEN-LINE:MVDGetInit46
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd46
        return stringItemPhone;
    }//GEN-END:MVDGetEnd46

    /** This method returns instance for okCommand4 component and should be called instead of accessing okCommand4 field directly.//GEN-BEGIN:MVDGetBegin52
     * @return Instance for okCommand4 component
     */
    public Command get_okCommand4() {
        if (okCommand4 == null) {//GEN-END:MVDGetBegin52
            // Insert pre-init code here
            okCommand4 = new Command("Delete Contact", Command.OK, 1);//GEN-LINE:MVDGetInit52
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd52
        return okCommand4;
    }//GEN-END:MVDGetEnd52

    
    

    /** This method returns instance for formVewContactData component and should be called instead of accessing formVewContactData field directly.//GEN-BEGIN:MVDGetBegin43
     * @return Instance for formVewContactData component
     */
    public Form get_formVewContactData() {
        if (formVewContactData == null) {//GEN-END:MVDGetBegin43
            // Insert pre-init code here
            formVewContactData = new Form("View Data", new Item[] {//GEN-BEGIN:MVDGetInit43
                get_stringItemName(),
                get_stringItemPhone()
            });
            formVewContactData.addCommand(get_okCommand3());
            formVewContactData.addCommand(get_okCommand4());
            formVewContactData.setCommandListener(this);//GEN-END:MVDGetInit43
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd43
        return formVewContactData;
    }//GEN-END:MVDGetEnd43

    
    
    public void startApp() {
        initialize();
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
 
    private void showAlert(String err) {
        Alert alert1 = new Alert("");
        alert1.setString(err);
        alert1.setTimeout(Alert.FOREVER);
        alert1.setType(AlertType.WARNING);
        getDisplay().setCurrent(alert1,listContactes);    
        destroyApp(false);
    }
    
    public void initContactFile(){
         //create a new instance of the class that manages a file of contacts
            contacts=new ManageContacts();
    }
    
    public void showContactList() throws PDAException{
        //Retrieve the contact data from the file and put them in a list
           Vector cList=contacts.getContacts();
           if (cList!=null){
               listContactes.deleteAll();
               for(int i=0;i<cList.size();i++){
                     Contact c=(Contact)cList.elementAt(i);
                     listContactes.append(c.getName(),null);
                     //listContactes.append(c.getPhone(),null);
                }  
            }else {
               listContactes.deleteAll();
               listContactes.append("No hi ha cap contacte",null);
            }
           
    }
    
   
}
