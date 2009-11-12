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

package fpc.j2me.server;

/**
 *
 * @author Pelsman
 */
public class BDServerTimer extends Thread{
    
    private final int TIMEOUT = 900;
    private String id;
    private int time;
    private ServerInterface si;
    /** Creates a new instance of BDServerTimer */
    public BDServerTimer(ServerInterface si, String id) {
        this.si = si;
        this.id = id.toString();
        this.time = TIMEOUT*1000;
    }
    
    public void restart(){
        this.time = TIMEOUT*1000;
    }
    
    public void run(){
        try{
            while (time > 0){
                sleep(1000);
                time -= 1000;
            }
            si.deleteId(this.id);
        }
        catch (InterruptedException e){
            
        }
    }
    
}
