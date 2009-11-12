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

import java.util.Enumeration;
import java.util.Vector;

/**
 * Classe que representa un �ndex carregat a mem�ria sobre un PDAStore.
 * @author  N�ria Lara Arana
 */
public class PDAStoreIndexVector extends PDAStoreIndex {
	
	/** Implementaci� escollida per a l'�ndex: un vector de PDAStoreRecordIndex. */
	protected Vector vectorindex;
		
	/** Constructura de la classe. */
	protected PDAStoreIndexVector()
	{
		this.vectorindex = new Vector();
	}
	
	protected boolean put(PDAStoreRecordIndex p)
	{
		long posicio = this.getFreeRecordIndex();
		if(posicio == -1 ) return false;
		
		this.vectorindex.insertElementAt(p,(int)posicio);
		
		return true;
	}

	protected PDAStoreRecordIndex get(long index)
	{
		Enumeration e = this.vectorindex.elements();	
		
		while(e.hasMoreElements())
		{
			PDAStoreRecordIndex entrada = (PDAStoreRecordIndex)e.nextElement();
			if(entrada.getIndex() == index) return entrada;
		}
		
		return null;
	}

	protected boolean update(PDAStoreRecordIndex p)
	{
		PDAStoreRecordIndex entradaaactualitzar = get(p.getIndex());		
		if(entradaaactualitzar == null) return false;
		
		// Comprovo que el nou tamany no sigui superior al que ja hi havia, perqu� si ho �s,
		// potser caldria moure el PDAStoreable al final del PDAStore per problemes d'espai.
		// Aquest �ltim cas no est� tractat, en aquesta versi� de l'API simplement es retorna fals.		
		//if(entradaaactualitzar.getSize()<p.getSize()) return false;

		entradaaactualitzar.update(p);
		
		return true;
	}

	protected boolean delete(long index)
	{
		int size = this.vectorindex.size();
		for(int i=0; i<size; i++)
		{
			PDAStoreRecordIndex entrada = (PDAStoreRecordIndex)this.vectorindex.elementAt(i);
			
			if(entrada.getIndex() == index)
			{
				this.vectorindex.removeElementAt(i);
				return true;
			}
		}
			
		return false;
	}

	/**
	 * Obt� la posici� lliure de l'�ndex. <br/>
	 * Es suposa que la posici� lliure sempre �s la seg�ent a la de l'�ltim element de l'�ndex. 
	 * @return Posici� lliure a l'�ndex.
	 */
	private long getFreeRecordIndex()
	{
		if(!getFreeSpace()) return -1;
		else return this.vectorindex.size();
	}
	
	/**
	 * Consulta l'espai lliure de l'�ndex. 
	 * @return Cert si hi ha espai lliure. Fals en cas contrari.
	 */
	protected boolean getFreeSpace()
	{
		if(this.vectorindex.size() == this.vectorindex.capacity())
		{
			this.vectorindex.ensureCapacity(this.vectorindex.size()+1);
		}
		
		if(this.vectorindex.size() < this.vectorindex.capacity()) return true;
		
		return false;
	}
	

}
