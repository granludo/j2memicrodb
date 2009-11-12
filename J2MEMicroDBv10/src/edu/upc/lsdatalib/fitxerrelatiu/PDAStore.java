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
 * 02110-1301  USA. It is also //currently //available at http://www.gnu.org/licenses/gpl.txt
 *
 * Authors: N�ria Lara, Marc Alier, Maria Jos� Casany, UPC
 *
 * Contact: http://morfeo.upc.edu/crom  malier@lsi.upc.edu, and
 * info@upc.edu / Jordi Girona Salgado, 1-3 E-08034 Barcelona SPAIN
 */
package edu.upc.lsdatalib.fitxerrelatiu;

import java.io.IOException;

import edu.upc.lsdatalib.comunsfitxer.PDAException;
import edu.upc.lsdatalib.comunsfitxer.PDAStoreable;

/**
 * Classe que encapsula l'acc�s a fitxers(o vectors o BBDD) dels objectes que compleixen la interface PDAStoreable. 
 * @author Marc Alier  & MJ Casany <br/>
 * Postgrau de Desenvolupament d'Aplicacions Mobils. Fundaci� UPC <br/>
 * http://www.lsi.upc.edu/~malier
 *
 */
public abstract class PDAStore
{
	/**
	 * Escriu un objecte PDAStoreable a la posici� passada per par�metre.
	 * @param object PDAStoreable que cont� l'objecte que es vol guardar al PDAStore.
	 * @param pos Posici� l�gica del PDAStore on es vol escriure el PDAStoreable.
	 */
	public abstract void write(PDAStoreable object, int pos) throws IOException, PDAException;
	
	/**
	 * Llegeix el PDAStoreable que es troba a la posici� sol�licitada.
	 * @param pos Posici� l�gica on es troba el PDAStoreable que es vol llegir.
	 * @return PDAStoreable llegit o b� null si no s'ha pogut llegir el PDAStoreable demanat.
	 */
	public abstract PDAStoreable read(long pos) throws IOException, PDAException ;

	/**
	 * Esborra l'objecte PDAStoreable que es troba a la posici� donada.
	 * @param pos Posici� l�gica on es troba el PDAStoreable que es vol esborrar.
	 * @return Cert si l'operaci� ha tingut �xit. Fals en cas contrari.
	 */
	public abstract boolean delete(int pos) throws PDAException;

	/**
	 * Modifica la informaci� del PDAStoreable que es troba a una posici� determinada.
	 * @param object PDAStoreable que cont� l'objecte actualitzat que es vol guardar al PDAStore.
	 * @param pos Posici� l�gica on es troba l'objecte que es vol modificar.
	 * @return Cert si l'operaci� ha tingut �xit. Fals en cas contrari.
	 */
	public abstract boolean update(PDAStoreable object , int pos) throws PDAException; 	
}
