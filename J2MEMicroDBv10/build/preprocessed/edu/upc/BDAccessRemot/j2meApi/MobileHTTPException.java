/*
 * MobileHTTPException.java
 *
 * Created on 23 de febrero de 2007, 18:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.upc.BDAccessRemot.j2meApi;

/**
 * Exception thrown to indicate that an error has occurred in a MobileHTTPConnection instance.
 * @author Pelsman
 */
public class MobileHTTPException extends Exception{
    
    private String message;
    private int code;
    
    /**
     * Creates a new instance of MobileHTTPException with the specified code.
     * @param code Integer that associates the exception with a message.
     */
    public MobileHTTPException(int code){
        
        this.code = code;
        this.message = codeToString(code);
        
    }
    
    /**
     * Gets the code of this exception.
     * @return The code of this exception. If code = -1 then the MobileHTTPConnection instance must be reinitialized because the connection has been lost.
     */
    public int getCode(){
        return this.code;
    }
    
    private String codeToString(int code){
        
        switch (code){
            case -1: return ("Error de connexió amb el servidor");
            case 1: return ("Error a l'obrir la base de dades");
            case 2: return ("Error a l'enviar la sentencia sql");
            case 3: return ("Error al tancar la base de dades");
            case 4: return ("Error a l'obtenir les bases de dades disponibles");
            case 5: return ("Error al crear MobileHTTPConnection");
            case 6: return ("La dada que demaneu no existeix");
            
            default: return("Missatge per defecte");
        }
        
    }

    /**
     * Gets the message of this exception.
     * @return The message of this exception
     */
    public String getMessage() {
        return this.message;
    }
    
    
}
