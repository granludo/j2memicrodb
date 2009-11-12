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
 * Authors: Núria Lara, Marc Alier, Maria José Casany, UPC
 *
 * Contact: http://morfeo.upc.edu/crom  malier@lsi.upc.edu, and
 * info@upc.edu / Jordi Girona Salgado, 1-3 E-08034 Barcelona SPAIN
 */
package edu.upc.lsdatalib.fitxersequencial;

import edu.upc.lsdatalib.comunsfitxer.PDAException;
import edu.upc.lsdatalib.comunsfitxer.PDAStoreable;


/**
 * Classe que encapsula l'accés a fitxers(o vectors o BBDD) dels objectes que compleixen la interface PDAStoreable. 
 * @author Marc Alier  & MJ Casany <br/>
 * Postgrau de Desenvolupament d'Aplicacions Mobils. Fundació UPC <br/>
 * http://www.lsi.upc.edu/~malier
 *
 */
public abstract class PDAStore
{
	/**
	 * Escriu un PDAStoreable a la primera posició lliure del PDAStore.
	 * @param object PDAStoreable que conté l'objecte que es vol guardar al PDAStore.. El PDAStoreable no pot ser null o tenir tamany 0.
	 */
	public abstract void write(PDAStoreable object) throws PDAException;
	
	/**
	 * Llegeix el PDAStoreable que es troba a la posició que s'està tractant.
	 * @return PDAStoreable llegit o bé null si no s'ha pogut llegir el PDAStoreable demanat.
	 */
	public abstract PDAStoreable read() throws PDAException;

	/**
	 * Esborra el PDAStoreable que es troba a la posició que s'està tractant.
	 * @return Cert si l'operació ha tingut éxit. Fals en cas contrari.
	 */
	public abstract boolean delete() throws PDAException;

	/**
	 * Modifica el PDAStoreable que es troba a la posició que s'està tractant.
	 * @param object PDAStoreable amb la informació modificada.
	 * @return Cert si l'operació ha tingut éxit. Fals en cas contrari.
	 */
	public abstract boolean update(PDAStoreable object) throws PDAException; 	
}
