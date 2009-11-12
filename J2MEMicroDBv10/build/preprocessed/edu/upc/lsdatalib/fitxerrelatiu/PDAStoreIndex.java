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

/**
 * Classe que representa un �ndex sobre un PDAStore.<br/>
 * El PDAStoreIndex est� format per entrades tipus PDAStoreRecordIndex.
 * Cada entrada de l'�ndex cont� informaci� d'un PDAStoreable guardat al PDAStore al qual est� associat l'�ndex.
 * L'identificador de les entrades �s la posici� l�gica dels PDAStoreables al PDAStore.<br/>
 * Les entrades de l'�ndex relacionen l'adre�a f�sica i l�gica d'un PDAStoreable dins del PDAStore 
 * i contenen la informaci� necess�ria per guardar i recuperar els PDAStoreables.
 * @author N�ria Lara Arana
 *
 */
abstract class PDAStoreIndex {
	
	/**
	 * Insereix una entrada a l'�ndex.
	 * @param p Entrada a inserir.
	 * @return Cert si s'ha pogut inserir l'entrada. Fals en cas contrari.
	 */
	abstract boolean put(PDAStoreRecordIndex p);
	 
	/**
	 * Retorna l'entrada de l'�ndex identificada pel par�metre �ndex.
	 * @param index Identificador de l'entrada que es vol obtenir (posici� l�gica del PDAStoreable associat a aquesta entrada).
	 * @return L'entrada de l'�ndex que es vol obtenir. O b� null si l'entrada no s'ha pogut llegir.
	 */
	abstract PDAStoreRecordIndex get(long index);
	
	/**
	 * Actualitza una entrada de l'�ndex.
	 * @param p Entrada actualitzada.
	 * @return Cert si s'ha pogut fer la modificaci� de l'entrada.  Fals en cas contrari.
	 */
	abstract boolean update(PDAStoreRecordIndex p);
	
	/**
	 * Esborra l'entrada de l'�ndex identificada pel par�metre �ndex.
	 * @param index Identificador de l'entrada a esborrar (posici� l�gica del PDAStoreable associat a aquesta entrada).
	 * @return Cert si s'ha pogut esborrar l'entrada de l'�ndex. Fals en cas contrari.
	 */
	abstract boolean delete(long index);
}
