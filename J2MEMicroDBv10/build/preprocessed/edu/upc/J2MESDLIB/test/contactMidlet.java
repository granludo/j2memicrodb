/*
 * contactMidlet.java
 *
 * Created on 19 de septiembre de 2006, 8:40
 */

package edu.upc.J2MESDLIB.test;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.Vector;
import edu.upc.J2MESDLIB.*;


/**
 *
 * @author se
 */
public class contactMidlet extends MIDlet implements CommandListener {
    
    /** Creates a new instance of contactMidlet */
    public contactMidlet() {
        initialize();
    }
    
    private Form mainForm;//GEN-BEGIN:MVDFields
    private Command exitCommand1;
    private TextField findContact;
    private Command okCommand1;
    private org.netbeans.microedition.lcdui.TableItem tableItem1;
    private org.netbeans.microedition.lcdui.SimpleTableModel simpleTableModel1;
    private Command itemCommand1;
    private Form newContactForm;
    private TextField textFieldName;
    private TextField textFieldPhone;
    private Command cancelCommand1;
    private Command okCommand2;
    private Form contactDataForm;
    private StringItem stringItemName;
    private StringItem stringItemPhone;
    private Command okCommand3;
    private Command okCommand4;
    private Command okCommand5;
    private Form formEditContact;
    private StringItem stringItemName2;
    private TextField textFieldPhone2;
    private Command cancelCommand2;
    private Command okCommand6;
    private Command okCommand7;//GEN-END:MVDFields
    private ManageContacts listContacts;
    private int rows=0, cols=0;
    
//GEN-LINE:MVDMethods

    /** Called by the system to indicate that a command has been invoked on a particular displayable.//GEN-BEGIN:MVDCABegin
     * @param command the Command that ws invoked
     * @param displayable the Displayable on which the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:MVDCABegin
        if (displayable == mainForm) {//GEN-BEGIN:MVDCABody
            if (command == okCommand1) {//GEN-END:MVDCABody
                // Call the Add New Contact Form
                clearNewContactForm();
                getDisplay().setCurrent(get_newContactForm());//GEN-LINE:MVDCAAction7
            } else if (command == exitCommand1) {//GEN-LINE:MVDCACase7
                // Exit midlet
                exitMIDlet(); 
                // Do nothing//GEN-LINE:MVDCAAction4
            } else if (command == itemCommand1) {//GEN-LINE:MVDCACase4
                // Find a contact by name
                 J2MERow row=null;
                 if (!(findContact.getString()).equals("")){
                    try{
                     row=listContacts.getContact(findContact.getString());
                     if (row!=null){
                         Form form=get_contactDataForm();
                         setDataContact(listContacts.getName(row),listContacts.getTelephone(row));
                         getDisplay().setCurrent(get_contactDataForm()); 
                     }else
                          showAlert("The contact does not exists!");
                    }catch (Exception e){
                          showAlert(e.getMessage());
                    } 
                    getDisplay().setCurrent(get_contactDataForm());//GEN-LINE:MVDCAAction13
                }else //No data to seach
                      showAlert("You must write the name of the contact!");
                 
            } else if (command == okCommand7) {//GEN-LINE:MVDCACase13
                // Insert pre-action code here
                // Do nothing//GEN-LINE:MVDCAAction41
                // Insert post-action code here
                //esborrar contactes.
                try{
                 listContacts.dropContacts();
                }catch(Exception e){
                      showAlert("Can't drop contact list");
                }
            }//GEN-BEGIN:MVDCACase41
        } else if (displayable == newContactForm) {
            if (command == cancelCommand1) {//GEN-END:MVDCACase41
                getDisplay().setCurrent(get_mainForm());//GEN-LINE:MVDCAAction18
            } else if (command == okCommand2) {//GEN-LINE:MVDCACase18
                // Add the new contact to the list
                try{  
                if (!(textFieldName.getString()).equals("")){
                   listContacts.NewContact(textFieldName.getString(),textFieldPhone.getString());
                   String[][] data=initTable();
                   if (data!=null){
                      simpleTableModel1.setValues(data);
                      getDisplay().setCurrent(get_mainForm());
                   }
                }else {//no name for the contact           
                     showAlert("You must write the name of the contact!");
                }    
                }catch (Exception ex){
                      showAlert(ex.getMessage());
                }
                getDisplay().setCurrent(get_mainForm());//GEN-LINE:MVDCAAction20
            }//GEN-BEGIN:MVDCACase20
        } else if (displayable == contactDataForm) {
            if (command == okCommand3) {//GEN-END:MVDCACase20
                getDisplay().setCurrent(get_mainForm());//GEN-LINE:MVDCAAction25
            } else if (command == okCommand4) {//GEN-LINE:MVDCACase25
                // Delete the selected contact
                try{
                  listContacts.deleteContact(stringItemName.getText());
                  String[][] data=initTable();
                  if (data!=null)
                     simpleTableModel1.setValues(data);
                     getDisplay().setCurrent(get_mainForm());
                }catch (Exception ex)  {
                    showAlert(ex.getMessage());
                }
                // Do nothing
                // Do nothing//GEN-LINE:MVDCAAction27
            } else if (command == okCommand5) {//GEN-LINE:MVDCACase27
                // Print data of the selected contact
                Form form=get_formEditContact();
                stringItemName2.setText(stringItemName.getText());
                textFieldPhone2.setString(stringItemPhone.getText());
                getDisplay().setCurrent(get_formEditContact());//GEN-LINE:MVDCAAction29
            }//GEN-BEGIN:MVDCACase29
        } else if (displayable == formEditContact) {
            if (command == cancelCommand2) {//GEN-END:MVDCACase29
                getDisplay().setCurrent(get_mainForm());//GEN-LINE:MVDCAAction35
            } else if (command == okCommand6) {//GEN-LINE:MVDCACase35
                // Update data of the selected contact
                try{
                  String n=stringItemName2.getText();
                  String p=textFieldPhone2.getString();
                  listContacts.uptadeContact(n,p);
                  String[][] data=initTable();
                  if (data!=null){
                     simpleTableModel1.setValues(data);
                     getDisplay().setCurrent(get_mainForm());
                  }   
                }catch (Exception ex) {
                    showAlert(ex.getMessage());
                }
                getDisplay().setCurrent(get_mainForm());//GEN-LINE:MVDCAAction37
            }//GEN-BEGIN:MVDCACase37
        }//GEN-END:MVDCACase37
}//GEN-LINE:MVDCAEnd

    /** This method initializes UI of the application.//GEN-BEGIN:MVDInitBegin
     */
    private void initialize() {//GEN-END:MVDInitBegin
        listContacts=new ManageContacts();
        getDisplay().setCurrent(get_mainForm());//GEN-LINE:MVDInitInit
        
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

    /** This method returns instance for mainForm component and should be called instead of accessing mainForm field directly.//GEN-BEGIN:MVDGetBegin2
     * @return Instance for mainForm component
     */
    public Form get_mainForm() {
        if (mainForm == null) {//GEN-END:MVDGetBegin2
            mainForm = new Form(null, new Item[] {//GEN-BEGIN:MVDGetInit2
                get_findContact(),
                get_tableItem1()
            });
            mainForm.addCommand(get_exitCommand1());
            mainForm.addCommand(get_okCommand1());
            mainForm.addCommand(get_itemCommand1());
            mainForm.addCommand(get_okCommand7());
            mainForm.setCommandListener(this);//GEN-END:MVDGetInit2
        }//GEN-BEGIN:MVDGetEnd2
        return mainForm;
    }//GEN-END:MVDGetEnd2

    /** This method returns instance for exitCommand1 component and should be called instead of accessing exitCommand1 field directly.//GEN-BEGIN:MVDGetBegin3
     * @return Instance for exitCommand1 component
     */
    public Command get_exitCommand1() {
        if (exitCommand1 == null) {//GEN-END:MVDGetBegin3
            exitCommand1 = new Command("Exit", Command.EXIT, 1);//GEN-LINE:MVDGetInit3
        }//GEN-BEGIN:MVDGetEnd3
        return exitCommand1;
    }//GEN-END:MVDGetEnd3

    /** This method returns instance for findContact component and should be called instead of accessing findContact field directly.//GEN-BEGIN:MVDGetBegin5
     * @return Instance for findContact component
     */
    public TextField get_findContact() {
        if (findContact == null) {//GEN-END:MVDGetBegin5
            findContact = new TextField("Find Contact:", null, 120, TextField.ANY);//GEN-LINE:MVDGetInit5
        }//GEN-BEGIN:MVDGetEnd5
        return findContact;
    }//GEN-END:MVDGetEnd5

    //Command used to add a new Contact
    /** This method returns instance for okCommand1 component and should be called instead of accessing okCommand1 field directly.//GEN-BEGIN:MVDGetBegin6
     * @return Instance for okCommand1 component
     */
    public Command get_okCommand1() {
        if (okCommand1 == null) {//GEN-END:MVDGetBegin6
            okCommand1 = new Command("Add Contact", "Add Contact", Command.OK, 1);//GEN-LINE:MVDGetInit6
        }//GEN-BEGIN:MVDGetEnd6
        return okCommand1;
    }//GEN-END:MVDGetEnd6

    /** This method returns instance for tableItem1 component and should be called instead of accessing tableItem1 field directly.//GEN-BEGIN:MVDGetBegin10
     * @return Instance for tableItem1 component
     */
    public org.netbeans.microedition.lcdui.TableItem get_tableItem1() {
        if (tableItem1 == null) {//GEN-END:MVDGetBegin10
            tableItem1 = new org.netbeans.microedition.lcdui.TableItem(getDisplay(), "List of Contacts", get_simpleTableModel1());//GEN-LINE:MVDGetInit10
        }//GEN-BEGIN:MVDGetEnd10
        return tableItem1;
    }//GEN-END:MVDGetEnd10

     //Init the TableItem of contacts
    String[][] initTable(){
            rows=0;
            try{
              Vector contacts=listContacts.getContacts();
              if (contacts!=null){
                 int NumContacts=contacts.size();  
                 String[][] data=new String[NumContacts][2];    
                 for (int i=0;i<contacts.size();i++){
                      J2MERow row=(J2MERow)contacts.elementAt(i);  
                      String name=listContacts.getName(row);
                      data[rows][cols]=name;
                      cols++;
                      String telephone=listContacts.getTelephone(row);
                      data[rows][cols]=telephone;
                      rows++;
                      cols=0;
                 }
                 return data;
              }
            }catch (Exception e){
              showAlert(e.getMessage());
            }
        return null;
    }
    
    /** This method returns instance for simpleTableModel1 component and should be called instead of accessing simpleTableModel1 field directly.//GEN-BEGIN:MVDGetBegin11
     * @return Instance for simpleTableModel1 component
     */
    public org.netbeans.microedition.lcdui.SimpleTableModel get_simpleTableModel1() {
        if (simpleTableModel1 == null) {//GEN-END:MVDGetBegin11
            simpleTableModel1 = new org.netbeans.microedition.lcdui.SimpleTableModel();//GEN-BEGIN:MVDGetInit11
            simpleTableModel1.setValues(new String[][] {
                new String[] {
                    null,
                    null,
                },
                new String[] {
                    null,
                    null,
                },
                new String[] {
                    null,
                    null,
                },
                new String[] {
                    null,
                    null,
                },
            });
            simpleTableModel1.setColumnNames(new String[] {
                "Name",
                "Phone",
            });//GEN-END:MVDGetInit11
           String[][] data=initTable();
           if (data!=null)
              simpleTableModel1.setValues(data);
        }//GEN-BEGIN:MVDGetEnd11
        return simpleTableModel1;
    }//GEN-END:MVDGetEnd11

     // Command used to seach a contact by name
    /** This method returns instance for itemCommand1 component and should be called instead of accessing itemCommand1 field directly.//GEN-BEGIN:MVDGetBegin12
     * @return Instance for itemCommand1 component
     */
    public Command get_itemCommand1() {
        if (itemCommand1 == null) {//GEN-END:MVDGetBegin12
            itemCommand1 = new Command("Find", "Find", Command.ITEM, 1);//GEN-LINE:MVDGetInit12
        }//GEN-BEGIN:MVDGetEnd12
        return itemCommand1;
    }//GEN-END:MVDGetEnd12

    /** This method returns instance for newContactForm component and should be called instead of accessing newContactForm field directly.//GEN-BEGIN:MVDGetBegin14
     * @return Instance for newContactForm component
     */
    public Form get_newContactForm() {
        if (newContactForm == null) {//GEN-END:MVDGetBegin14
            newContactForm = new Form(null, new Item[] {//GEN-BEGIN:MVDGetInit14
                get_textFieldName(),
                get_textFieldPhone()
            });
            newContactForm.addCommand(get_cancelCommand1());
            newContactForm.addCommand(get_okCommand2());
            newContactForm.setCommandListener(this);//GEN-END:MVDGetInit14
        }//GEN-BEGIN:MVDGetEnd14
        return newContactForm;
    }//GEN-END:MVDGetEnd14

    public void clearNewContactForm(){
        if (textFieldName!=null)
           textFieldName.setString("");
        if (textFieldPhone!=null)
           textFieldPhone.setString("");
    }
    /** This method returns instance for textFieldName component and should be called instead of accessing textFieldName field directly.//GEN-BEGIN:MVDGetBegin15
     * @return Instance for textFieldName component
     */
    public TextField get_textFieldName() {
        if (textFieldName == null) {//GEN-END:MVDGetBegin15
            textFieldName = new TextField("Name:", null, 120, TextField.ANY);//GEN-LINE:MVDGetInit15
        }//GEN-BEGIN:MVDGetEnd15
        return textFieldName;
    }//GEN-END:MVDGetEnd15

    /** This method returns instance for textFieldPhone component and should be called instead of accessing textFieldPhone field directly.//GEN-BEGIN:MVDGetBegin16
     * @return Instance for textFieldPhone component
     */
    public TextField get_textFieldPhone() {
        if (textFieldPhone == null) {//GEN-END:MVDGetBegin16
            textFieldPhone = new TextField("Phone:", null, 120, TextField.ANY);//GEN-LINE:MVDGetInit16
        }//GEN-BEGIN:MVDGetEnd16
        return textFieldPhone;
    }//GEN-END:MVDGetEnd16

    /** This method returns instance for cancelCommand1 component and should be called instead of accessing cancelCommand1 field directly.//GEN-BEGIN:MVDGetBegin17
     * @return Instance for cancelCommand1 component
     */
    public Command get_cancelCommand1() {
        if (cancelCommand1 == null) {//GEN-END:MVDGetBegin17
            cancelCommand1 = new Command("Cancel", Command.CANCEL, 1);//GEN-LINE:MVDGetInit17
        }//GEN-BEGIN:MVDGetEnd17
        return cancelCommand1;
    }//GEN-END:MVDGetEnd17

    //Command used to add a new contact to the table of contacts
    /** This method returns instance for okCommand2 component and should be called instead of accessing okCommand2 field directly.//GEN-BEGIN:MVDGetBegin19
     * @return Instance for okCommand2 component
     */
    public Command get_okCommand2() {
        if (okCommand2 == null) {//GEN-END:MVDGetBegin19
            okCommand2 = new Command("add", "add", Command.OK, 1);//GEN-LINE:MVDGetInit19
        }//GEN-BEGIN:MVDGetEnd19
        return okCommand2;
    }//GEN-END:MVDGetEnd19

    /** This method returns instance for contactDataForm component and should be called instead of accessing contactDataForm field directly.//GEN-BEGIN:MVDGetBegin21
     * @return Instance for contactDataForm component
     */
    public Form get_contactDataForm() {
        if (contactDataForm == null) {//GEN-END:MVDGetBegin21
            contactDataForm = new Form(null, new Item[] {//GEN-BEGIN:MVDGetInit21
                get_stringItemName(),
                get_stringItemPhone()
            });
            contactDataForm.addCommand(get_okCommand3());
            contactDataForm.addCommand(get_okCommand4());
            contactDataForm.addCommand(get_okCommand5());
            contactDataForm.setCommandListener(this);//GEN-END:MVDGetInit21
        }//GEN-BEGIN:MVDGetEnd21
        return contactDataForm;
    }//GEN-END:MVDGetEnd21

    /** This method returns instance for stringItemName component and should be called instead of accessing stringItemName field directly.//GEN-BEGIN:MVDGetBegin22
     * @return Instance for stringItemName component
     */
    public StringItem get_stringItemName() {
        if (stringItemName == null) {//GEN-END:MVDGetBegin22
            stringItemName = new StringItem("contact Name:", "<Enter Text>");//GEN-LINE:MVDGetInit22
        }//GEN-BEGIN:MVDGetEnd22
        return stringItemName;
    }//GEN-END:MVDGetEnd22

    /** This method returns instance for stringItemPhone component and should be called instead of accessing stringItemPhone field directly.//GEN-BEGIN:MVDGetBegin23
     * @return Instance for stringItemPhone component
     */
    public StringItem get_stringItemPhone() {
        if (stringItemPhone == null) {//GEN-END:MVDGetBegin23
            stringItemPhone = new StringItem("contact Phone:", "<Enter Text>");//GEN-LINE:MVDGetInit23
        }//GEN-BEGIN:MVDGetEnd23
        return stringItemPhone;
    }//GEN-END:MVDGetEnd23

    public void setDataContact(String name, String phone){
            stringItemName.setText(name);
            stringItemPhone.setText(phone);
    }
  
    /** This method returns instance for okCommand3 component and should be called instead of accessing okCommand3 field directly.//GEN-BEGIN:MVDGetBegin24
     * @return Instance for okCommand3 component
     */
    public Command get_okCommand3() {
        if (okCommand3 == null) {//GEN-END:MVDGetBegin24
            okCommand3 = new Command("Ok", Command.OK, 1);//GEN-LINE:MVDGetInit24
        }//GEN-BEGIN:MVDGetEnd24
        return okCommand3;
    }//GEN-END:MVDGetEnd24

    //Command used to delete from the table of contacts the selected contact
    /** This method returns instance for okCommand4 component and should be called instead of accessing okCommand4 field directly.//GEN-BEGIN:MVDGetBegin26
     * @return Instance for okCommand4 component
     */
    public Command get_okCommand4() {
        if (okCommand4 == null) {//GEN-END:MVDGetBegin26
            okCommand4 = new Command("delete", "delete", Command.OK, 1);//GEN-LINE:MVDGetInit26
        }//GEN-BEGIN:MVDGetEnd26
        return okCommand4;
    }//GEN-END:MVDGetEnd26

    //Command used to enter the edit data mode of the selected contact
    /** This method returns instance for okCommand5 component and should be called instead of accessing okCommand5 field directly.//GEN-BEGIN:MVDGetBegin28
     * @return Instance for okCommand5 component
     */
    public Command get_okCommand5() {
        if (okCommand5 == null) {//GEN-END:MVDGetBegin28
            okCommand5 = new Command("Edit", "Edit", Command.OK, 1);//GEN-LINE:MVDGetInit28
        }//GEN-BEGIN:MVDGetEnd28
        return okCommand5;
    }//GEN-END:MVDGetEnd28

    /** This method returns instance for formEditContact component and should be called instead of accessing formEditContact field directly.//GEN-BEGIN:MVDGetBegin30
     * @return Instance for formEditContact component
     */
    public Form get_formEditContact() {
        if (formEditContact == null) {//GEN-END:MVDGetBegin30
            formEditContact = new Form(null, new Item[] {//GEN-BEGIN:MVDGetInit30
                get_stringItemName2(),
                get_textFieldPhone2()
            });
            formEditContact.addCommand(get_cancelCommand2());
            formEditContact.addCommand(get_okCommand6());
            formEditContact.setCommandListener(this);//GEN-END:MVDGetInit30
        }//GEN-BEGIN:MVDGetEnd30
        return formEditContact;
    }//GEN-END:MVDGetEnd30

    /** This method returns instance for stringItemName2 component and should be called instead of accessing stringItemName2 field directly.//GEN-BEGIN:MVDGetBegin32
     * @return Instance for stringItemName2 component
     */
    public StringItem get_stringItemName2() {
        if (stringItemName2 == null) {//GEN-END:MVDGetBegin32
            stringItemName2 = new StringItem("Name:", "<Enter Text>");//GEN-LINE:MVDGetInit32
        }//GEN-BEGIN:MVDGetEnd32
        return stringItemName2;
    }//GEN-END:MVDGetEnd32

    /** This method returns instance for textFieldPhone2 component and should be called instead of accessing textFieldPhone2 field directly.//GEN-BEGIN:MVDGetBegin33
     * @return Instance for textFieldPhone2 component
     */
    public TextField get_textFieldPhone2() {
        if (textFieldPhone2 == null) {//GEN-END:MVDGetBegin33
            textFieldPhone2 = new TextField("Phone", null, 120, TextField.ANY);//GEN-LINE:MVDGetInit33
        }//GEN-BEGIN:MVDGetEnd33
        return textFieldPhone2;
    }//GEN-END:MVDGetEnd33

    /** This method returns instance for cancelCommand2 component and should be called instead of accessing cancelCommand2 field directly.//GEN-BEGIN:MVDGetBegin34
     * @return Instance for cancelCommand2 component
     */
    public Command get_cancelCommand2() {
        if (cancelCommand2 == null) {//GEN-END:MVDGetBegin34
            cancelCommand2 = new Command("Cancel", Command.CANCEL, 1);//GEN-LINE:MVDGetInit34
        }//GEN-BEGIN:MVDGetEnd34
        return cancelCommand2;
    }//GEN-END:MVDGetEnd34

    /** This method returns instance for okCommand6 component and should be called instead of accessing okCommand6 field directly.//GEN-BEGIN:MVDGetBegin36
     * @return Instance for okCommand6 component
     */
    public Command get_okCommand6() {
        if (okCommand6 == null) {//GEN-END:MVDGetBegin36
            okCommand6 = new Command("Ok", Command.OK, 1);//GEN-LINE:MVDGetInit36
        }//GEN-BEGIN:MVDGetEnd36
        return okCommand6;
    }//GEN-END:MVDGetEnd36

    /** This method returns instance for okCommand7 component and should be called instead of accessing okCommand7 field directly.//GEN-BEGIN:MVDGetBegin40
     * @return Instance for okCommand7 component
     */
    public Command get_okCommand7() {
        if (okCommand7 == null) {//GEN-END:MVDGetBegin40
            // Insert pre-init code here
            okCommand7 = new Command("Esborrar Contactes", "Esborrar Contactes", Command.OK, 1);//GEN-LINE:MVDGetInit40
            // Insert post-init code here
        }//GEN-BEGIN:MVDGetEnd40
        return okCommand7;
    }//GEN-END:MVDGetEnd40
    
    public void startApp() {
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
    
    private void showAlert(String err) {
        Alert a = new Alert("");
        a.setString(err);
        a.setTimeout(Alert.FOREVER);
        a.setType(AlertType.WARNING);
        //Display d=getDisplay();
         getDisplay().setCurrent(a);    
         destroyApp(false);
    }
}
