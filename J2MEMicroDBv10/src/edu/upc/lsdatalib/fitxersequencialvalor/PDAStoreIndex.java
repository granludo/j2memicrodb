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
package edu.upc.lsdatalib.fitxersequencialvalor;


/**
 * Classe que representa un índex sobre un PDAStore.<br/>
 * El PDAStoreIndex està format per entrades tipus PDAStoreRecordIndex.
 * Cada entrada de l'índex conté informació d'un PDAStoreable guardat al PDAStore al qual està associat l'índex.
 * L'identificador de les entrades és l'etiqueta dels PDAStoreables al PDAStore, però aquesta pot ser no única.
 * És a dir, pot haver més d'una entrada amb la mateixa etiqueta.<br/>
 * Les entrades de l'índex relacionen l'adreça física i l'etiqueta d'un PDAStoreable dins del PDAStore 
 * i contenen la informació necessària per guardar i recuperar els PDAStoreables.<br/>
 * La seqüencialitat dels PDAStoreables es gestiona mitjançant el camp Next de les entrades.
 * 
 * @author Núria Lara Arana
 *
 */
abstract class PDAStoreIndex {
	
	/**
	 * Insereix una entrada a l'índex.
	 * @param p Entrada a inserir.
	 * @return Cert si s'ha pogut inserir l'entrada. Fals en cas contrari.
	 */
	abstract boolean put(PDAStoreRecordIndex p);
	 
	/**
	 * Retorna l'entrada de l'índex que actualment s'està tractant.
	 * @return L'entrada de l'índex que es vol obtenir. O bé null si l'entrada no s'ha pogut llegir.
	 */
	abstract PDAStoreRecordIndex get();
	
	/**
	 * Actualitza l'entrada de l'índex que actualment s'està tractant.
	 * @param p Entrada amb les dades modificades.
	 * @return Cert si s'ha pogut fer la modificació de l'entrada.  Fals en cas contrari.
	 */
	abstract boolean update(PDAStoreRecordIndex p);
	
	/**
	 * Esborra l'entrada de l'índex que actualment s'està tractant.
	 *@return Cert si s'ha pogut esborrar l'entrada de l'índex. Fals en cas contrari.
	 */
	abstract boolean delete(); 
	
	/**
	 * Retorna la primera entrada de l'índex.
	 * @return La primera entrada de l'índex.
	 */
	abstract PDAStoreRecordIndex First();
	
	/**
	 * Retorna l'entrada següent a la que s'està tractant de l'índex.
	 * @return L'entrada següent a l'índex.
	 */
	abstract PDAStoreRecordIndex Next();	
}
