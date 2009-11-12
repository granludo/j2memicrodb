/** 
* Copyright 2007 Universitat Polit�cnica de Catalunya 
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
* Authors:  Juan Antonio Sosa, Mariona Fec�ndez, Marc Alier, Maria Jos� Casany UPC 
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
 */
package edu.upc.J2MESDLIB.utilssgbd;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

/**
 * Classe que gestiona les constants que usa l'API.<br/>
 * Les constants es defineixen en un fitxer de propietats que es troben al mateix directori que aquesta classe.
 * El nom del fitxer �s del tipus properties_<i>microedition.locale</i>.properties on <i>microedition.locale</i> �s la propietat del sistema que retorna el codi de la llengua i del pa�s definits al sistema on s'executa l'aplicaci�.<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;- En el cas del castell� la cadena seria <i>es_ES</i> i el nom del fitxer <i>properties_es-ES.properties</i>.<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;- En el cas de que no hi hagi un fitxer per a l'idioma definit, sempre hi haur� un fitxer <i>properties.properties</i> amb l'idioma per defecte definit pel programador de l'API i ser�aquest el que faci servir la classe.<br/> 
 * <br/>El contingut del fitxer s�n l�nies (acabades en un salt de l�nia -car�cter '\n'- fins i tot l'�ltima) del tipus <i>clau = missatge</i>, on:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;- <i>clau</i> �s un enter<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;- <i>missatge</i> �s una cadena. En el cas de constants de les excepcions, el missatge �s de la forma <i>MissatgeExcepci� | M�todeCausantExcepci�</i> i �s la pr�pia classe de l'excepci� qui tracta convenientment aquesta cadena, no pas la classe PDAProperties.<br/>
 * <br/>Per �ltim, dir que aquesta classe est� inspirada en la classe java.util.Properties que no es troba al Java per a dispositius m�bils.
 * Aquesta classe �s filla de java.util.Hashtable, d'aqu� l'atribut <i>properties</i> de tipus Hashtable, que permet guardar valors i accedir-los mitjan�ant una clau.
 * @author N�ria Lara Arana
 *
 */
public class PDAProperties{
	
	/** Cadena que forma el nom del fitxer de propietats, en el aquest cas "properties". */
	private static final String propertiesFileDefault = "properties";
	
	/** Hashtable que permet a la classe guardar cadenes i accedir-les mitjan�ant una clau de tipus enter. */
	private static Hashtable properties;
	
	static
	{
		// Inicialitzar el Hashtable
		properties = new Hashtable();
		
		/* 
		 * Llegir el fitxer de properties corresponent.
		 * language �s del tipus es-ES
		 */
		String language = System.getProperty("microedition.locale");
		
		/*
		 * Intentem accedir al fitxer de l'idioma corresponent
		 * (ex: properties_es-ES.properties) , sino agafem el fitxer properties.properties
		 */
		String nomfitxer = propertiesFileDefault+"_"+language+"."+propertiesFileDefault;
		
		try
		{
			if(!load(nomfitxer))
			{
				load(propertiesFileDefault+"."+propertiesFileDefault);
			}
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}	
	
	/**
	 * Mitjan�ant una clau accedeix a la cadena definida al fitxer de propietats.
	 * @param key Enter que representa la clau que permet l'acc�s a la cadena buscada.
	 * @return La cadena que t� com a clau el par�metre.
	 */
	public static String getProperty(int key)
	{
		return (String)properties.get(new Integer(key));
	}
	
	/**
	 * Mitjan�ant una clau guarda una cadena al fitxer de propietats.
	 * @param key Enter que representa la clau que permet guardar la cadena passada com a par�metre.
	 * @param message String amb la cadena a guardar.
	 */
	private static void setProperty(Integer key, String message)
	{
		properties.put(key,message);
	}

	/**
	 * Carrega el fitxer de propietats corresponent a la taula de hash de la classe.
	 * @param filename String amb el nom del fitxer de propietats. Es pren com a directori del fitxer el mateix directori que la classe PDAProperties.
	 * @return Fals si no s'ha pogut carregar el fitxer. Cert en cas contrari.
	 */	
	private static boolean load(String filename)throws IOException
	{				
		InputStream inputstream = null;
		
		try
		{
			inputstream = Object.class.getResourceAsStream("/edu/upc/lsdatalib/utilssgbd/"+filename);
			if(inputstream == null) return false;
			
			StringBuffer sb = new StringBuffer();
		    int chr = 0;

		    while ((chr = inputstream.read()) != -1)
		    {	    
		    	sb.append((char) chr);
		          
		        if(chr == '\n') //Tinc una l�nia de definici� de propietat, amb la forma: key = message
		        {
		        	String line = sb.toString(); 
		        	line = line.trim();
		        	
		        	int igual = line.indexOf(" = ");
		        	boolean comentari = line.startsWith("#");
		        	
		        	if((igual>-1) && (!comentari))
		        	{
		        		Integer key = Integer.valueOf(line.substring(0,igual));
		        		String message = line.substring(igual+3,line.length());
		        		setProperty(key,message);
		        	}
		        	
		        	sb = new StringBuffer();
		          }
		    }		    
		    
		    inputstream.close();
		    return true;
		}
		catch( IOException ioe )
		{
			inputstream.close();
			ioe.printStackTrace();
			return false;
		}
	}
	
}