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
package edu.upc.lsdatalib.comunsfitxer;



/**
 * Interface que defineix mètodes per a guardar un objecte en una array de bytes, accedir-hi, consultar el seu tamany i comparar dos objectes que estenguin PDAStoreable.<br/>
 * MOLT IMPORTANT: la classe que implementi PDAStoreable ha d'incorporar almenys una constructora sense paràmetres: public classeQueImplementa(){}
 * @author Marc Alier  & MJ Casany <br/>
 * Postgrau de Desenvolupament d'Aplicacions Mobils. Fundació UPC <br/>
 * http://www.lsi.upc.edu/~malier
 */
public interface PDAStoreable
{

	/** 
	 * Emmagatzema les dades de l'objecte en una array de bytes. 
	 * @return Array de bytes amb la dada que guarda el PDAStoreable
	 * */
	public byte[] getData();
	
	/**
	 * Retorna el nombre de bytes que ocupa l'objecte emmagatzemat. 
	 * @return Tamany de la dada que guarda el PDAStoreable
	 * */
	public int size();
	
	/** 
	 * Assigna les dades de l'objecte partint del contingut de l'array de bytes. <br/>
	 * Size s'usa per redundància
	 * @param b Array de bytes de l'objecte
	 * @param size Tamany en bytes de l'objecte
	 * @throws PDAException Si el paràmetre size no coincideix amb el tamany de l'array b
	 * */
	public void setData(byte[] b,int size) throws PDAException;
		
	/**
	 * Compara dos PDAStoreable.
	 * @return 	>0 indica que l'objecte és > que el paràmetre<br/>
	 *  		=0 indica que l'objecte és = que el paràmetre<br/>
	 *  		<0 indica que l'objecte és < que el paràmetre
	 * */
	public int compare(PDAStoreable c);
} 