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

public class backupRestoreThread extends Thread {
    
    private backupRestore b;
    private boolean backup;
    private backupRestoreTest t;
    String path;
    
    public backupRestoreThread(boolean backup, String path, backupRestoreTest t){
        this.backup = backup;
        this.path = path;
        this.t = t;
        b = new backupRestore();
    }
    
    public void run(){
        int sortida = 0;
        if(this.backup){
            try{
                b.backup(this.path);
            }
            catch(Exception e){
                sortida = -1;
            }
            if(sortida == -1){
                t.notificar(true,true);
            }
            else{
                t.notificar(false,true);
            }
        }
        else{
            try{
                b.restore(this.path);
            }
            catch(Exception e){
                sortida = -1;
            }
            if(sortida == -1){
                t.notificar(true,false);
            }
            else{
                t.notificar(false,false);
            }
        }
    }
    
    
}
