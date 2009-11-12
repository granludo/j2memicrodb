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
package edu.upc.lsdatalib.fitxervalor;

import java.util.Enumeration;
import java.util.Vector;

import edu.upc.lsdatalib.fitxervalor.PDAStoreRecordIndex;

/**
 * Classe que representa un índex carregat a memòria d'un PDAStore.
 * @author  Núria
 */
public class PDAStoreIndexVector extends PDAStoreIndex {
	
	/** Implementació escollida per a l'índex: un vector de PDAStoreRecordIndex */
	protected Vector vectorindex;

	/** Posició física del vector que implementa l´índex on es troba l'entrada del primer PDAStoreable del PDALabeledStore */
	protected int First;

	/** Posició física del vector que implementa l´índex on es troba l'entrada del PDAStoreable que s'està tractant */
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
		int posicio = this.getPositionRecordIndex(p.getLabel());
		if(posicio == -1 ) return false;
		
		//Actualització de l'índex
		p = updatePut(p,posicio);

		this.vectorindex.insertElementAt(p,(int)posicio);
		
		return true;
	}


	protected PDAStoreRecordIndex get(String label)
	{
		Enumeration e = this.vectorindex.elements();	
		
		while(e.hasMoreElements())
		{
			PDAStoreRecordIndex entrada = (PDAStoreRecordIndex)e.nextElement();			
			if(entrada.getLabel().equals(label)) return entrada;
		}
		return null;
	}
	
	protected boolean update(PDAStoreRecordIndex p)
	{		
		PDAStoreRecordIndex entradaaactualitzar = (PDAStoreRecordIndex)this.get(p.getLabel());		
		if(entradaaactualitzar == null) return false;
		
		// Comprovo que el nou tamany no sigui superior al que ja hi havia, perquè si ho és,
		// potser caldria moure el PDAStoreable al final del PDAStore per problemes d'espai.
		// Aquest últim cas no està tractat, en aquesta versió de l'API simplement es retorna fals.		
		//if(entradaaactualitzar.getSize()<p.getSize()) return false;
		
		p.setNext(entradaaactualitzar.getNext());
		entradaaactualitzar.update(p);
		
		return true;
	}
	
	protected boolean delete(String label)
	{
		int size = this.vectorindex.size();
		for(int i=0; i<size; i++)
		{
			PDAStoreRecordIndex entrada = (PDAStoreRecordIndex)this.vectorindex.elementAt(i);
			
			if(entrada.getLabel().equals(label))
			{
				//Actualització de l'índex
				entrada = updateDelete(entrada,i);
				
				this.vectorindex.removeElementAt(i);
				return true;
			}
		}
			
		return false;		
	}

	/**
	 * Retorna la primera entrada de l'índex. 
	 */
	protected PDAStoreRecordIndex First()
	{
		if( (this.First < 0) || this.First>=this.vectorindex.size())  return null;
		
		this.Current = this.First;
		return (PDAStoreRecordIndex)this.vectorindex.elementAt((int)this.First);
	}

	/**
	 * Retorna l'entrada següent a l'apuntada per Current.
	 */
	protected PDAStoreRecordIndex Next()
	{
		if( (this.Current < 0) || this.Current>=this.vectorindex.size()) return null;
		
		PDAStoreRecordIndex ricurrent = (PDAStoreRecordIndex)this.vectorindex.elementAt((int)this.Current);
		this.Current = (int)ricurrent.getNext();
		
		if((this.Current < 0)|| this.Current>=this.vectorindex.size()) return null;
		
		return (PDAStoreRecordIndex)this.vectorindex.elementAt((int)ricurrent.getNext());
	}

	/**
	 * Consulta si s'ha arribat al final del fitxer.
	 * @return Cert si s'ha arribat al final del fitxer. 
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
	 * Consulta l'espai lliure de l'índex. 
	 * @return Cert si hi ha espai lliure.
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
	 * Obté la posició física del vector que implementa l'índex on hauria d'anar la nova entrada per tal que l'índex estigui ordenat.<br/>
	 * Suposem que la posició lliure sempre és la següent a la de l'últim element de l'índex. 
	 * @return La posició física del vector on ha d'anar la nova entrada
	 */
	private int getPositionRecordIndex(String labelnovaentrada)
	{
		if(!getFreeSpace()) return -1;
		
		int size = this.vectorindex.size();	
		
		for(int i=0; i<size; i++)
		{
			PDAStoreRecordIndex entrada = (PDAStoreRecordIndex)this.vectorindex.elementAt(i);			
			
			if(labelnovaentrada.compareTo(entrada.getLabel()) < 0)
			{
				// L'etiqueta de la nova entrada va abans lexicogràficament que
				// l'entrada que es troba a la posició i del vector que implementa l'índex.
				return i;
			}
		}
		
		// La nova entrada ha de ser l'última, ja que totes les que hi ha van abans que la nova
		return this.vectorindex.size();
	}
	
	/**
	 * Obté l'entrada prèvia de l'entrada que té com a posició física al vector l'enter passat com a paràmetre. 
	 * @param posfisvector Posició física del vector de l'entrada prèvia.
	 * @param posfisentradaaconsultar Posició física al vector de l'entrada de la qual es vol esbrinar la seva entrada predecessora.
	 * @return L'entrada predecessora.
	 */
	private PDAStoreRecordIndex getPrev(int[] posfisvector, int posfisentradaaconsultar)
	{
		int posfisprevia = posfisentradaaconsultar - 1 ;
		
		if(posfisprevia < 0) return null;
		
		PDAStoreRecordIndex entrada = (PDAStoreRecordIndex)this.vectorindex.elementAt(posfisprevia);			
		posfisvector[0]= posfisprevia;
		
		return entrada;
	}
	
	
	/**
	 * Actualitza el camp next de les entrades de l'índex en afegir una nova entrada.
	 * @param novaentrada La nova entrada a l'índex
	 * @param posiciofisicavectornovaentrada Posició física al vector de l'entrada a actualitzar. 
	 * @return PDAStoreRecordIndex a inserir modificat convenientment
	 */
	private PDAStoreRecordIndex updatePut(PDAStoreRecordIndex novaentrada, int posiciofisicavectornovaentrada)
	{
		int[] vectorposfisvectorprev = new int[1];
		int sizeactualvector = this.vectorindex.size();
		
		PDAStoreRecordIndex entradaanterior = getPrev(vectorposfisvectorprev,posiciofisicavectornovaentrada);
		
		if( sizeactualvector == 0 )
		{
			// L'entrada a inserir serà l'única entrada
			novaentrada.setNext(-1);
			this.First   = posiciofisicavectornovaentrada;
		}
		else if( posiciofisicavectornovaentrada == 0 )
		{
			// L'entrada a inserir serà la primera
			this.First   = posiciofisicavectornovaentrada;
			novaentrada.setNext(1);
			
			// Actualitzar tots els camps next a un més excepte l'últim, que continuarà amb un -1
			updateNexts(0,1);			
		}
		else if( sizeactualvector == posiciofisicavectornovaentrada )
		{
			// L'entrada a inserir serà l'última
			novaentrada.setNext(-1);
			entradaanterior.setNext(posiciofisicavectornovaentrada);			
		}
		else
		{			
			// L'entrada no serà l'única ni tampoc serà la primera o l'última
			novaentrada.setNext(posiciofisicavectornovaentrada+1);
			entradaanterior.setNext(posiciofisicavectornovaentrada);
			
			// Actualitzar tots els camps next a partir de l'entrada a esborrar
			updateNexts(posiciofisicavectornovaentrada, 1);
		}
		
		// Actualització del Current
		this.Current = posiciofisicavectornovaentrada;
		
		return novaentrada;
	}
	
	/**
	 * Actualitza els atributs First i Current quan s'esborra una entrada.<br/>
	 * Si és necessari, actualitza el camp next de les entrades de l'índex.
	 * @param entradaaesborrar L'entrada a esborrar.
	 * @param posfisentradaaesborrar Posició física al vector de la entrada a esborrar.
	 * @return PDAStoreRecordIndex a esborrar.
	 */	
	private PDAStoreRecordIndex updateDelete(PDAStoreRecordIndex entradaaesborrar, int posfisentradaaesborrar)
	{
		int[] vectorposfisvectorprev = new int[1];
		int posfisvectornext = entradaaesborrar.getNext();
		
		PDAStoreRecordIndex entradaanterior = getPrev(vectorposfisvectorprev,posfisentradaaesborrar);
		PDAStoreRecordIndex entradaseguent = (posfisvectornext>0) ? (PDAStoreRecordIndex)this.vectorindex.elementAt(posfisvectornext) : null;
		
		int posfisvectorprev = vectorposfisvectorprev[0];
				
		if( (entradaanterior == null) && (entradaseguent == null) )
		{
			// L'entrada a esborrar és l'única entrada
			this.Current = -1;
			this.First   = -1;
		}
		else if( (entradaanterior == null) && (entradaseguent != null) )
		{
			// L'entrada a esborrar és la primera
			this.Current = 0;
			this.First   = 0;
			
			// Actualitzar tots els camps next a un menys excepte l'últim, que continuarà amb un -1
			updateNexts(0,-1);			
		}
		else if( (entradaanterior != null) && (entradaseguent == null) )
		{
			// L'entrada a esborrar és l'última
			entradaanterior.setNext(-1);
			this.Current = posfisvectorprev;
		}
		else if( (entradaanterior != null) && (entradaseguent != null) )
		{			
			// Actualitzar tots els camps next a partir de l'entrada a esborrar
			updateNexts(posfisentradaaesborrar+1, -1);
			this.Current = posfisvectornext;
		}
	
		return entradaaesborrar;		
	}
	
	/**
	 * Actualitza els camps <i>next</i> de les entrades de l'índex quan s'esborra una entrada. Com que el vector està ordenat, decrementa el camp next en una unitat. 
	 * @param posini Posició física del vector a partir de la qual s'ha d'actualitzar les entrades de l'índex
	 */	
	private void updateNexts(int posini, int constantincrement)
	{
		int size = this.vectorindex.size()-1;
		
		for(int i = posini; i<size; i++)
		{
			PDAStoreRecordIndex entrada = (PDAStoreRecordIndex)this.vectorindex.elementAt(i);			
			int nounext = entrada.getNext() + constantincrement;			
			entrada.setNext(nounext);
		}
	}

}
