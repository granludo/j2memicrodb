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
 * Classe que guarda un objecte en una array de bytes. 
 * @author Núria Lara Arana
 *
 */
public class PDAObject implements PDAStoreable {
	
	/** Array de bytes de l'objecte a guardar. */
	private byte[] data;

	/**
	 * Constructora de la classe.
	 * @param obj Objecte a guardar.
	 * @throws PDAException Si l'objecte obj a guardar és null. 
	 */
	public PDAObject(Object obj) throws PDAException
	{
		if(obj==null) throw new PDAException(1);
		this.setData(obj.toString().getBytes(),obj.toString().getBytes().length);
	}
	
	/**
	 * Constructora de la classe sense paràmetres.
	 */
	public PDAObject() throws PDAException
	{
		this.setData(new byte[0],0);
	}	
	
	public byte[] getData()
	{
		return this.data;
	}

	public void setData(byte[] b, int size) throws PDAException
	{
		 if(b.length==size)
		 {
			 this.data = b;
		 }
		 else
		 {
			 throw new PDAException(2);
		 }		
	}
	
	public int size()
	{
		return this.data.length;
	}


	public int compare(PDAStoreable c)
	{
		if(this.data.length > c.size()) return 1;
		else if (this.data.length < c.size()) return -1;

		/*Comparem valor per valor les dues arrays*/		
		int size = this.size();
		for(int i=0; i<size; i++)
		{
			if(this.getData()[i] != c.getData()[i]) return -1; 
		}
		
		return 0;		
	}
}
