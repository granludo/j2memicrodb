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

import java.util.Vector;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import edu.upc.lsdatalib.comunsfitxer.PDAException;
import edu.upc.lsdatalib.comunsfitxer.PDAStoreable;

/**
 * Classe que implementa el FITXER RELATIU.
 * @author Núria Lara Arana
 *
 */
public class PDAStoreRMS extends PDAStore {
	
	/** Implementació escollida per a l'índex: un vector de PDAStoreRecordIndex. */
	private PDAStoreIndexRMS pdastoreindexvector;
	
	/** RecordStore on es guarda el PDAStoreRMS. */
	private RecordStore recordstore;

	/**
	 * String amb el nom del RecordStore.<br/>
	 * Es guarda per tal de poder fer les operacions de <i>save</i>,<i>delete</i> i <i>delete</i> del fitxer per separat.
	 * La raó és que l'operació delete necessita saber el nom del RecordStore (que implementa el fitxer) per tal de poder esborrar-lo i prèviament s'ha 
	 * hagut de fer un close. Si s'ha fet ja el close en un mètode, no es pot obtenir el nom al mètode de delete perquè el RecordStore està tancat.  
	 */
	private String recordstorename;

	/** 
	 * Constructora de la classe.<br/>
	 * Crea un índex sobre el PDAStoreRMS.
	 * @param name String amb el nom del PDAStoreRMS. La seva longitud no pot ser >27 caràcters. 
	 * @throws PDAException
	 * */
	public PDAStoreRMS(String name) throws PDAException
	{		
		/*
		 * El nom d'un RecordStore no pot ser >32.
		 * Per a formar el nom del RecordStore que implementa l'índex concatenem el paràmetre name a la cadena "Index".
		 * Per tant, la longitud total no pot superar els 32 caràcters. 
		 */
		if(name.length() + ("Index".length()) > 32) throw new PDAException(3);
		
		this.pdastoreindexvector = new PDAStoreIndexRMS(name+"Index");
		
		try 
		{
			this.recordstore = RecordStore.openRecordStore(name,true);
			this.recordstorename = name;
		}
		catch (RecordStoreFullException e) {
			throw new PDAException(4);
		}
		catch (RecordStoreNotFoundException e) {
			throw new PDAException(5);
		}
		catch (RecordStoreException e) {
			throw new PDAException(6);
		}
	}

	/**
	 * Escriu un objecte PDAStoreable a la posició passada per paràmetre i li assigna una posició física.<br/>
	 * Només es permet l'escriptura a posicions lliures.
	 * @param object PDAStoreable que conté l'objecte que es vol guardar al PDAStore. El PDAStoreable no pot ser null o tenir tamany 0.
	 * @param pos Posició lògica del PDAStore on es vol escriure el PDAStoreable. La posició lògica ha de ser >=0.
	 */	
	public void write(PDAStoreable object, int pos) throws PDAException
	{
		// Comprovació dels paràmetres
		if(object == null) throw new PDAException(7);
		if(object.size()<=0) throw new PDAException(8);
		if(pos<0) throw new PDAException(9);		
		
		// Comprovem si la posició està ocupada
		if(existsPDAStoreable(pos)) throw new PDAException(10); 
				
		// Guardem el PDAStoreable al PDAStoreRMS
		try
		{
			// Esbrinem la posició física on anirà el nou PDAStoreable
			long posicioFisica = this.recordstore.getNextRecordID();

			// Afegir el nou Record al RecordStore: recordstore.addRecord(byte[] datos, int offset, int numBytes);
			this.recordstore.addRecord(object.getData(),0,object.size());
			
			// Modifiquem conseqüentment l'índex : crear l'entrada i inserir-la
			long posicioLogica = pos;
			int tamany = object.size();
			String nomClasse = object.getClass().getName();
			
			PDAStoreRecordIndex entradaIndex = new PDAStoreRecordIndex(posicioLogica, posicioFisica, tamany, nomClasse); 
			
			boolean exit = this.pdastoreindexvector.put(entradaIndex);
			if(!exit) throw new PDAException(39);
		}
		catch (RecordStoreNotOpenException e)
		{
			throw new PDAException(11);
		}
		catch (RecordStoreFullException e)
		{
			throw new PDAException(12);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(13);
		}	
	}

	/**
	 * Llegeix el PDAStoreable que es troba a la posició sol·licitada.
	 * @param pos Posició lògica on es troba el PDAStoreable que es vol llegir. La posició lògica ha de ser >=0.
	 * @return PDAStoreable llegit o bé null si no s'ha pogut llegir el PDAStoreable demanat.
	 */
	public PDAStoreable read(long pos) throws PDAException
	{
		// Comprovació dels paràmetres
		if(pos<0) throw new PDAException(14);		

		// Recuperem l'entrada de l'índex
		PDAStoreRecordIndex entradaIndex = this.pdastoreindexvector.get(pos);
		if(entradaIndex == null) return null;//throw new PDAException(15);

		// Obtenim la posició física on es troba el PDAStoreable que volem llegir
		long posicioFisica = entradaIndex.getPos();
		
		try
		{
			if(posicioFisica<1 || posicioFisica>=this.recordstore.getNextRecordID()) throw new PDAException(16);
			
			// Recuperem les dades del PDAStoreable a retornar
			int tamanyRecord = this.recordstore.getRecordSize((int)posicioFisica);
			byte[] byteInputData = new byte[tamanyRecord];
			byteInputData = this.recordstore.getRecord((int)posicioFisica);
			
			// Retornar el PDAStoreable
			String nomClasse = new String(entradaIndex.getClassName());
			Class classeobjecte = Class.forName(nomClasse);
			
			PDAStoreable objecte = (PDAStoreable)classeobjecte.newInstance();
			objecte.setData(byteInputData,entradaIndex.getSize());
			
			return objecte;
		}
		catch (RecordStoreNotOpenException e)
		{
			throw new PDAException(17);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(18);
		}
		catch (ClassNotFoundException e)
		{
			throw new PDAException(19);
		}
		catch (InstantiationException e)
		{
			throw new PDAException(20);
		}
		catch (IllegalAccessException e)
		{
			throw new PDAException(21);
		}
	}

	/**
	 * Esborra l'objecte PDAStoreable que es troba a la posició donada.
	 * @param pos Posició lògica on es troba el PDAStoreable que es vol esborrar. La posició lògica ha de ser >=0. 
	 * @return Cert si l'operació ha tingut èxit. Fals en cas contrari.
	 */	
	public boolean delete(int pos) throws PDAException
	{
		// Comprovació dels paràmetres
		if(pos<0) throw new PDAException(22);	

		// Recuperem l'entrada de l'índex
		PDAStoreRecordIndex entradaIndex = this.pdastoreindexvector.get(pos);
		if(entradaIndex == null) throw new PDAException(23);

		// Obtenim la posició física on es troba el PDAStoreable que volem llegir
		long posicioFisica = entradaIndex.getPos();
		
		try
		{
			if(posicioFisica<1 || posicioFisica>=this.recordstore.getNextRecordID()) throw new PDAException(24);
			
			// Esborrem el PDAStoreable
			this.recordstore.deleteRecord((int)posicioFisica);
			
			// Modifiquem conseqüentment l'índex : esborrar l'entrada
			return this.pdastoreindexvector.delete(pos);
		}
		catch (RecordStoreNotOpenException e)
		{
			throw new PDAException(25);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(26);
		}		
	}

	/**
	 * Modifica la informació del PDAStoreable que es troba a una posició determinada.
	 * @param object PDAStoreable que conté l'objecte actualitzat que es vol guardar al PDAStore. El PDAStoreable no pot ser null o tenir tamany 0.
	 * @param pos Posició lògica on es troba l'objecte que es vol modificar. La posició lògica ha de ser >=0.
	 * @return Cert si l'operació ha tingut èxit. Fals en cas contrari.
	 */	
	public boolean update(PDAStoreable object, int pos) throws PDAException
	{	
		// Comprovació dels paràmetres
		if(object == null) throw new PDAException(27);
		if(object.size()<=0) throw new PDAException(28);
		if(pos<0) throw new PDAException(29);	

		// Recuperem l'entrada de l'índex
		PDAStoreRecordIndex entradaIndex = this.pdastoreindexvector.get(pos);
		if(entradaIndex == null) throw new PDAException(30);
		
		// Obtenim la posició física on es troba el PDAStoreable que volem llegir
		long posicioFisica = entradaIndex.getPos();
		
		try
		{
			if(posicioFisica<1 || posicioFisica>=this.recordstore.getNextRecordID()) throw new PDAException(31);
			
			// Modifiquem el PDAStoreable al PDAStoreRMS
			//setRecord(int id, byte[] datonuevo, int offset, int tamaño)
			this.recordstore.setRecord((int)posicioFisica, object.getData(),0,object.size());

			// Modifiquem conseqüentment l'índex : modificar l'entrada
			long posicioLogica = pos;
			int tamany = object.size();
			String nomClasse = object.getClass().getName();
			
			entradaIndex = new PDAStoreRecordIndex(posicioLogica, posicioFisica, tamany, nomClasse);
			
			return this.pdastoreindexvector.update(entradaIndex);	
		}
		catch (RecordStoreNotOpenException e)
		{
			throw new PDAException(32);
		}
		catch (RecordStoreFullException e)
		{
			throw new PDAException(33);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(34);
		}	
	}


	/**
	 * Comprova si ja existeix un PDAStoreable a la posició lògica passada per paràmetre consultant l'índex.
	 * @param pos Posició lògica a comprovar.
	 * @return Cert si la posició lògica ja està ocupada. Fals en cas contrari.
	 */
	private boolean existsPDAStoreable(int pos)
	{
		if(this.pdastoreindexvector.get(pos)!=null) return true;
		return false;
	}


	/**
	 * Tanca el PDAStoreRMS i salva l'índex.
	 */
	public void closePDAStore() throws PDAException
	{
		try
		{	
			//Guardem l´índex a disc
			saveIndex();
			
			//Tanquem l'índex
			closeIndex();
			
			//Tanquem el RecordStore
			this.recordstore.closeRecordStore();
		}
		catch (RecordStoreNotOpenException e)
		{
			throw new PDAException(35);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(36);
		}
	}

	/**
	 * Esborra el PDAStoreRMS i el seu índex associat.
	 */
	public void deletePDAStore() throws PDAException
	{
		try
		{			
			// Esborrem l'índex associat
			deleteIndex();	
			
			//Esborrem el RecordStore
			String name = this.recordstorename;								
			RecordStore.deleteRecordStore(name);			   
		}
		catch (RecordStoreNotOpenException e)
		{
			throw new PDAException(37);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(38);
		}
	}
	
	
	/**
	 * Guarda el contingut de l'índex a memòria (PDAStoreIndexVector) al disc (PDAStoreIndexRMS).<br/>
	 * El programador pot cridar-la per tal de salvar l'índex quan vulgui.
	 * @throws PDAException 
	 */
	public void saveIndex() throws PDAException
	{
		this.pdastoreindexvector.saveIndex();
	}

	/**
	 * Tanca l'índex associat.
	 */
	private void closeIndex() throws PDAException
	{
		this.pdastoreindexvector.closeIndex();
	}	

	/**
	 * Esborra l'índex associat.
	 */	
	private void deleteIndex() throws PDAException
	{
		this.pdastoreindexvector.deleteIndex();
	}

	/**
	 * Mètode privat de la desenvolupadora per fer-lo servir a mode d'ajuda durant la creació de l'API. Obté el contingut del PDAStoreRMS en un Vector.
	 * @return Vector amb el contingut del PDAStoreRMS
	 * @throws PDAException de tipus desconegut, ja que aquest mètode no pertany oficialment a l'API.
	 */
	private Vector llegirPDAStoreRMS() throws PDAException
	{
		 Vector dades = new Vector();
		 byte[] byteInputData = new byte[0];
		 
		 try
		 {
			 RecordEnumeration re = this.recordstore.enumerateRecords(null,null,false);
			 while(re.hasNextElement())
			 {
				 int ri = re.nextRecordId();
				 byteInputData = this.recordstore.getRecord(ri);
				 String dada = new String(byteInputData);
				 dades.addElement(new String("[posiciofisica(recordindex):"+ri+"] "+dada));
			 }			 
		}
		catch (RecordStoreNotOpenException e)
		{
			throw new PDAException();
		}
		catch (InvalidRecordIDException e)
		{
			throw new PDAException();
		}
		catch (RecordStoreException e)
		{
			throw new PDAException();
		}
		
		return dades;
	}


	/**
	 * Mètode de la desenvolupadora per fer-lo servir a mode d'ajuda durant la creació de l'API. Obté el contingut del PDAStoreRMS en un String.
	 * @return String amb el contingut del PDAStoreRMS
	 * @throws PDAException de tipus desconegut, ja que aquest mètode no pertany oficialment a l'API.
	 */
	public String printToString() throws PDAException
	{
		Vector dades = llegirPDAStoreRMS();
		
		StringBuffer s=new StringBuffer();
		int i=0;
		int size = dades.size(); 
			
		while (i<size)
		{
		   s.append((String)dades.elementAt(i));
		   s.append("\n");
		   i++;
		}
		
		String impressio = "\nPDAStoreRMS (fitxer):\n";
		impressio += s.toString();
		impressio += this.pdastoreindexvector.printToString();
		
		return impressio;
	}


}
