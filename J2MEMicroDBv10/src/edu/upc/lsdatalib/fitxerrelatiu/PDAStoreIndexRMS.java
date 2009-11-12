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
package edu.upc.lsdatalib.fitxerrelatiu;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import edu.upc.lsdatalib.comunsfitxer.PDAException;

/**
 * Classe que implementa un índex guardat a disc sobre un PDAStore.
 * @author Núria Lara Arana
 */
public class PDAStoreIndexRMS extends PDAStoreIndexVector {

	/** RecordStore que usa el PDAStoreIndexRMS per a guardar la seva informació al disc */
	private RecordStore recordstore;

	/**
	 * String amb el nom del RecordStore.<br/>
	 * Es guarda per tal de poder fer les operacions de <i>save</i>,<i>delete</i> i <i>delete</i> de l'índex per separat.
	 * La raó és que l'operació delete necessita saber el nom del RecordStore (que implementa l'índex) per tal de poder esborrar-lo i prèviament s'ha 
	 * hagut de fer un close. Si s'ha fet ja el close en un mètode, no es pot obtenir el nom al mètode de delete perquè el RecordStore està tancat.  
	 */
	private String recordstorename;

	/**
	 * Constructura de la classe.<br/>
	 * Si existeix l'índex que es demana construir a disc, el recupera i el porta a memòria, sinó, en crea un de nou.
	 * @throws PDAException 
	 * */
	PDAStoreIndexRMS(String name) throws PDAException
	{
		super();
		
		//Comprovem si existeix l'índex al disc, sinó fem un de nou
		try
		{
			this.recordstore 	 = RecordStore.openRecordStore(name,false);
			this.recordstorename = name;
			
			// Recuperem el de disc i copiem l'índex recuperat del disc al PDAStoreIndexVector
			copyIndexToMemory();
		}
		catch (RecordStoreNotFoundException nf) //L'índex no existeix, fem un de nou
		{
			try
			{
				this.recordstore 	 = RecordStore.openRecordStore(name,true);
				this.recordstorename = name;
			}
			catch (RecordStoreFullException e)
			{
				throw new PDAException(40);
			}
			catch (RecordStoreNotFoundException e)
			{
				throw new PDAException(41);
			}
			catch (RecordStoreException e)
			{
				throw new PDAException(42);
			}			
		}
		catch (RecordStoreFullException e)
		{
			throw new PDAException(40);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(42);
		}
	}

	/**
	 * Recupera l'índex de disc i el carrega a memòria.
	 * @throws PDAException 
	 */
	private void copyIndexToMemory() throws PDAException
	{
		// Recorrem el RecordStore associat i anem recuperant PDAStoreRecordIndex per a afegir-los a l'índex de memòria (PDAStoreIndexVector)
		try
		{
			RecordEnumeration re = this.recordstore.enumerateRecords(null,null,false);
			
			while(re.hasNextElement())
			{
				byte[] byteInputData = re.nextRecord();
				
				// Afegir el PDAStoreRecordIndex a l'índex
				PDAStoreRecordIndex pdari = new PDAStoreRecordIndex(byteInputData);
				
				boolean exit = this.put(pdari);
				if(!exit) throw new PDAException(43);
			}
		}
		catch (RecordStoreNotOpenException e)
		{
			throw new PDAException(44);
		}
		catch (InvalidRecordIDException e)
		{
			throw new PDAException(45);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(46);
		}	
	}

	/**
	 * Guarda l'índex al disc.
	 * @throws PDAException 
	 */
	protected void saveIndex() throws PDAException
	{		
		// Esborrem l'antic per crear un de nou amb les noves dades
		String name = this.recordstorename;
		
		try
		{
			this.recordstore.closeRecordStore();
			RecordStore.deleteRecordStore(name);
			
			this.recordstore = RecordStore.openRecordStore(name, true);
		}
		catch (RecordStoreNotOpenException e)
		{
			throw new PDAException(47);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(48);
		}		
		
		int size = this.vectorindex.size();
		for(int i=size-1; i>-1; i--)
		{
			PDAStoreRecordIndex recordindex = (PDAStoreRecordIndex)this.vectorindex.elementAt(i);
			
			// Guardem el PDAStoreRecordIndex a un Record
			saveRecordIndex(recordindex);
		}
	}

	/**
	 * Guarda un PDAStoreRecordIndex a un Record
	 * @param recordindex PDAStoreRecordIndex a guardar
	 * @throws PDAException 
	 */
	private void saveRecordIndex(PDAStoreRecordIndex recordindex) throws PDAException
	{
		try
		{
			byte[] bri = recordindex.makeData();
			this.recordstore.addRecord(bri,0,bri.length);
		}
		catch (RecordStoreNotOpenException e)
		{
			throw new PDAException(47);
		}
		catch (RecordStoreFullException e)
		{
			throw new PDAException(49);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(48);
		}
	}

	/**
	 * Tanca el RecordStore de l'índex
	 * @throws PDAException 
	 */
	protected void closeIndex() throws PDAException
	{
		try
		{
			this.recordstore.closeRecordStore();
		}
		catch (RecordStoreNotOpenException e)
		{
			throw new PDAException(50);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(51);
		}
	}

	/**
	 * Esborra el RecordStore de l'índex
	 * @throws PDAException 
	 */	
	protected void deleteIndex() throws PDAException
	{
		try
		{	
			String name = this.recordstorename;
			
			//Esborrem el RecordStore
			RecordStore.deleteRecordStore(name);			 
		}
		catch (RecordStoreNotOpenException e)
		{
			throw new PDAException(52);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(53);
		}
	}

	/**
	 * Mètode de la desenvolupadora per fer-lo servir a mode d'ajuda durant la creació de l'API. Obté el contingut del PDAStoreIndexRMS en un String.
	 * @return String amb el contingut del PDAStoreIndexRMS
	 * @throws PDAException de tipus desconegut, ja que aquest mètode no pertany oficialment a l'API.
	 */	
	protected String printToString()
	{
		String s = "";

		int size = this.vectorindex.size();
		
		for(int i=0; i<size; i++)
		{
			PDAStoreRecordIndex r = (PDAStoreRecordIndex)this.vectorindex.elementAt(i);
			s += "["+r.getIndex()+"],["+r.getPos()+"],["+r.getSize()+"],["+r.getClassName()+"]\n";
		}
		return "\nPDAStoreIndex (índex):\n[index PosicioLogica i identificador],[pos PosicioFisica],[size Tamany PDAStoreable],[className]\n"+s;
	}

}
