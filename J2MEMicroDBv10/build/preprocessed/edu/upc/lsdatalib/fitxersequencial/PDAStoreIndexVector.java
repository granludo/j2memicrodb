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
package edu.upc.lsdatalib.fitxersequencial;

import java.util.Vector;

/**
 * Classe que representa un �ndex carregat a mem�ria d'un PDAStore.
 * @author  N�ria Lara Arana
 */
public class PDAStoreIndexVector extends PDAStoreIndex {
	
	/** Implementaci� escollida per a l'�ndex: un vector de PDAStoreRecordIndex */
	protected Vector vectorindex;

	/** Posici� f�sica del vector que implementa l��ndex on es troba l'entrada del primer PDAStoreable al PDAStoreSequence */
	protected int First;

	/** Posici� f�sica del vector que implementa l��ndex on es troba l'entrada del PDAStoreable que s'est� tractant */
	protected int Current;

	/** Constructura de la classe */
	protected PDAStoreIndexVector()
	{
		this.vectorindex = new Vector();
		this.First = -1;
		this.Current = -1;
	}
	
	protected boolean put(PDAStoreRecordIndex p)
	{
		int posicio = this.getFreeRecordIndex();
		if(posicio == -1 ) return false;
		
		//Actualitzaci� de l'�ndex
		p = updatePut(p,posicio);
		
		this.vectorindex.insertElementAt(p,(int)posicio);
		
		return true;
	}

	protected PDAStoreRecordIndex get()
	{
		if(this.Current<0 || this.Current>=this.vectorindex.size()) return null;
		return (PDAStoreRecordIndex)this.vectorindex.elementAt((int)this.Current);
	}
	
	protected boolean update(PDAStoreRecordIndex p)
	{		
		PDAStoreRecordIndex entradaaactualitzar = (PDAStoreRecordIndex)this.vectorindex.elementAt((int)this.Current);		
		if(entradaaactualitzar == null) return false;
		
		// L'entrada a actualitzar ha de ser la indicada per Current
		if(entradaaactualitzar.getIndex() != p.getIndex()) return false;
		
		// Comprovo que el nou tamany no sigui superior al que ja hi havia, perqu� si ho �s,
		// potser caldria moure el PDAStoreable al final del PDAStore per problemes d'espai.
		// Aquest �ltim cas no est� tractat, en aquesta versi� de l'API simplement es retorna fals.		
		//if(entradaaactualitzar.getSize()<p.getSize()) return false;
		
		p.setNext(entradaaactualitzar.getNext());
		entradaaactualitzar.update(p);
		
		return true;
	}
	
	protected boolean delete()
	{
		PDAStoreRecordIndex entrada = (PDAStoreRecordIndex)this.vectorindex.elementAt((int)this.Current);
		if(entrada == null) return false;
		
		int posicio = (int)this.Current;
		
		//Actualitzaci� de l'�ndex
		entrada = updateDelete(entrada);		
		
		this.vectorindex.removeElementAt(posicio);
		
		return true;
	}

	
	/**
	 * Obt� la posici� lliure de l'�ndex. <br/>
	 * Es suposa que la posici� lliure sempre �s la seg�ent a la de l'�ltim element de l'�ndex. 
	 * @return Posici� lliure a l'�ndex.
	 */
	private int getFreeRecordIndex()
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
	
	
	/**
	 * Retorna la primera entrada de l'�ndex.
	 * @return La primera entrada de l'�ndex. 
	 */
	protected PDAStoreRecordIndex First()
	{
		if((this.First < 0) || this.First>=this.vectorindex.size()) return null;
		
		this.Current = this.First;
		return (PDAStoreRecordIndex)this.vectorindex.elementAt((int)this.First);
	}

	/**
	 * Retorna l'entrada seg�ent a l'apuntada per Current.
	 * @return L'entrada seg�ent a l'apuntada per Current.
	 */
	protected PDAStoreRecordIndex Next()
	{
		if((this.Current < 0)|| this.Current>=this.vectorindex.size()) return null;
		
		PDAStoreRecordIndex ricurrent = (PDAStoreRecordIndex)this.vectorindex.elementAt((int)this.Current);
		this.Current = (int)ricurrent.getNext();
		
		if((this.Current < 0)|| this.Current>=this.vectorindex.size()) return null;
		
		return (PDAStoreRecordIndex)this.vectorindex.elementAt((int)ricurrent.getNext());
	}

	/**
	 * Consulta si s'ha arribat al final del fitxer buscant l'entrada que t� com a valor al camp Next '-1'.
	 * @return Cert si s'ha arribat al final del fitxer. Fals en cas contrari.
	 */
	protected boolean EOF()
	{
		if(this.Current < 0 || this.Current >= this.vectorindex.size()) return false;
		
		PDAStoreRecordIndex ricurrent = (PDAStoreRecordIndex)this.vectorindex.elementAt((int)this.Current);
		long nextcurrent = ricurrent.getNext();
		
		if(nextcurrent == -1) return true;
		return false;
	}

	/**
	 * Actualitza l'entrada a tractar amb l'entrada que t� la posici� l�gica especificada.
	 * Sit�a el Current a l'entrada que coincideix amb la posici� l�gica especificada.
	 * @return Cert si s'ha localitzat l'entrada. Fals en cas contrari. 
	 */
	protected boolean seek(long posicioLogica)
	{
		int size = this.vectorindex.size();	
		
		for(int i=0; i<size; i++)
		{
			PDAStoreRecordIndex entrada = (PDAStoreRecordIndex)this.vectorindex.elementAt(i);			
			
			if(entrada.getIndex() == posicioLogica)
			{
				this.Current = i;
				return true;
			}
		}
		return false;
	}	

	/**
	 * Retorna la posici� l�gica del registre que s'est� tractant actualment.
	 * @return Posici� l�gica del registre actual.<br/> -1 en cas que no hi hagi cap registre actual (fitxer buit p.ex.)
	 */	
	protected long Current()
	{
		PDAStoreRecordIndex entradaactual = (PDAStoreRecordIndex)this.get();
		if(entradaactual==null)return -1;
		
		return entradaactual.getIndex();
	}	
	
	/**
	 * Obt� l'entrada pr�via de l'entrada que t� com a posici� f�sica al vector l'enter passat com a par�metre. 
	 * @param posfisvector Posici� f�sica al vector de l'entrada de la qual es vol esbrinar la seva entrada predecessora.
	 * @return L'entrada predecessora.
	 */
	private PDAStoreRecordIndex getPrev(int[] posfisvector)
	{
		int size = this.vectorindex.size();
				
		for(int i=0; i<size; i++)
		{	
			PDAStoreRecordIndex entrada = (PDAStoreRecordIndex)this.vectorindex.elementAt(i);			
			if(entrada.getNext() == this.Current)
			{
				posfisvector[0]= i;
				return entrada;
			}
		}
		return null;
	}

	/**
	 * Obt� l'�ltima entrada de l'�ndex. 
	 * @return L'�ltima entrada.
	 */
	private PDAStoreRecordIndex getLast()
	{
		int size = this.vectorindex.size();
		for(int i=0; i<size; i++)
		{
			PDAStoreRecordIndex entrada = (PDAStoreRecordIndex)this.vectorindex.elementAt(i);			
			if(entrada.getNext() == -1) return entrada;
		}
		return null;
	}	
	
	/**
	 * Actualitza els atributs First i Current quan s'afegeix un nou registre.<br/>
	 * Si �s necessari, actualitza el camp next de les entrades de l'�ndex.
	 * @param novaentrada La nova entrada a l'�ndex
	 * @param posiciofisicavectornovaentrada Posici� f�sica al vector de la entrada a actualitzar.
	 * @return PDAStoreRecordIndex a inserir modificat convenientment
	 */
	private PDAStoreRecordIndex updatePut(PDAStoreRecordIndex novaentrada, int posiciofisicavectornovaentrada)
	{
		int sizeactualvector = this.vectorindex.size();
		
		// L'entrada de l'�ndex que representa a l'�ltim PDAStoreable del PDAStoreSequence 
		// passar� a ser la pen�ltima amb la inserci� del nou PDAStoreable
		if(sizeactualvector > 0)
		{
			PDAStoreRecordIndex entradaanterior = getLast();
			entradaanterior.setNext(posiciofisicavectornovaentrada);
		}
		
		//Actualitzaci� de First i Current
		this.Current = (int)posiciofisicavectornovaentrada;
		if(sizeactualvector == 0) this.First = (int)posiciofisicavectornovaentrada;
		
		//Aquesta nova entrada ser� ara la de l'�ltim PDAStoreable del PDAStoreSequence
		novaentrada.setNext(-1);
		
		return novaentrada;
	}
	
	/**
	 * Actualitza els atributs First i Current quan s'esborra un registre.<br/>
	 * Si �s necessari, actualitza el camp next de les entrades de l'�ndex.
	 * @param entradaaesborrar L'entrada a esborrar.
	 * @return PDAStoreRecordIndex a esborrar.
	 */
	private PDAStoreRecordIndex updateDelete(PDAStoreRecordIndex entradaaesborrar)
	{
		int[] vectorposfisvectorprev = new int[1];
		int posfisvectornext = entradaaesborrar.getNext();
		
		PDAStoreRecordIndex entradaanterior = getPrev(vectorposfisvectorprev);
		PDAStoreRecordIndex entradaseguent = (posfisvectornext>0) ? (PDAStoreRecordIndex)this.vectorindex.elementAt(posfisvectornext) : null;
		
		int posfisvectorprev = vectorposfisvectorprev[0];
				
		if( (entradaanterior == null) && (entradaseguent == null) )
		{
			// L'entrada a esborrar �s l'�nica entrada
			this.Current = -1;
			this.First   = -1;
		}
		else if( (entradaanterior == null) && (entradaseguent != null) )
		{
			// L'entrada a esborrar �s la primera
			this.Current = 0;
			this.First   = 0;
			
			// Actualitzar tots els camps next a un menys excepte l'�ltim, que continuar� amb un -1
			updateNexts(0);			
		}
		else if( (entradaanterior != null) && (entradaseguent == null) )
		{
			// L'entrada a esborrar �s l'�ltima
			entradaanterior.setNext(-1);
			this.Current = posfisvectorprev;
			
		}
		else if( (entradaanterior != null) && (entradaseguent != null) )
		{			
			// Actualitzar tots els camps next a partir de l'entrada a esborrar
			updateNexts(this.Current+1);
			this.Current = posfisvectornext;
		}
	
		return entradaaesborrar;		
	}
	
	/**
	 * Actualitza els camps <i>next</i> de les entrades de l'�ndex quan s'esborra una entrada. Com que el vector est� ordenat, decrementa el camp next en una unitat. 
	 * @param posini Posici� f�sica del vector a partir de la qual s'ha d'actualitzar les entrades de l'�ndex
	 */
	private void updateNexts(int posini)
	{
		int size = this.vectorindex.size()-1;
		
		for(int i = posini; i<size; i++)
		{
			PDAStoreRecordIndex entrada = (PDAStoreRecordIndex)this.vectorindex.elementAt(i);			
			int nounext = entrada.getNext() - 1;			
			entrada.setNext(nounext);
		}
	}

}
