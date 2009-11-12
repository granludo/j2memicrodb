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
package edu.upc.lsdatalib.fitxervalor;

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
 * Classe que implementa el FITXER PER VALOR.
 * @author N�ria Lara Arana
 *
 */
public class PDALabeledStore extends PDAStore {
	
	/** Implementaci� escollida per a l'�ndex: un vector de PDAStoreRecordIndex */
	private PDAStoreIndexRMS pdastoreindexvector;
	
	/** RecordStore on es guarda el PDALabeledStore */
	private RecordStore recordstore;

	/**
	 * String amb el nom del RecordStore.<br/>
	 * Es guarda per tal de poder fer les operacions de <i>save</i>,<i>delete</i> i <i>delete</i> del fitxer per separat.
	 * La ra� �s que l'operaci� delete necessita saber el nom del RecordStore (que implementa el fitxer) per tal de poder esborrar-lo i pr�viament s'ha 
	 * hagut de fer un close. Si s'ha fet ja el close en un m�tode, no es pot obtenir el nom al m�tode de delete perqu� el RecordStore est� tancat.  
	 */
	private String recordstorename;
	
	
	/** 
	 * Constructora de la classe<br/>
	 * Crea un �ndex sobre el PDALabeledStore.  
	 * @throws PDAException 
	 * */
	public PDALabeledStore(String name) throws PDAException
	{		
		/*
		 * El nom d'un RecordStore no pot ser >32.
		 * Per a formar el nom del RecordStore que implementa l'�ndex concatenem el par�metre name a la cadena "Index".
		 * Per tant, la longitud total no pot superar els 32 car�cters. 
		 */
		if(name.length() + ("Index".length()) > 32) throw new PDAException(146);
		
		this.pdastoreindexvector = new PDAStoreIndexRMS(name+"Index");
		
		try 
		{
			this.recordstore = RecordStore.openRecordStore(name,true);
			this.recordstorename = name;
		}
		catch (RecordStoreFullException e)
		{
			throw new PDAException(147);
		}
		catch (RecordStoreNotFoundException e)
		{
			throw new PDAException(148);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(149);
		}
	}

	/**
	 * Escriu un objecte PDAStoreable a la primera posici� lliure i li assigna una posici� f�sica.<br/>
	 * @param object PDAStoreable que cont� l'objecte que es vol guardar al PDAStore. El PDAStoreable no pot ser null o tenir tamany 0.
	 * @param label Etiqueta del PDAStoreable i identificador �nic del PDAStoreable (no pot haver dues entrades amb la mateixa etiqueta). L'etiqueta no pot ser null o tenir tamany 0.
	 */		
	public void write(PDAStoreable object, String label) throws PDAException
	{
		// Comprovaci� dels par�metres
		if(object == null) throw new PDAException(150);
		if(object.size()<=0) throw new PDAException(151);
		if(label == null) throw new PDAException(195);
		if(label.length()<=0) throw new PDAException(196);
		
		// Comprovem si la posici� est� ocupada
		if(existsPDAStoreable(label)) throw new PDAException(152); 
		
		// Guardem el PDAStoreable al PDALabeledStore
		try
		{
			// Esbrinem la posici� f�sica on anir� el nou PDAStoreable : escrivim a la primera posici� lliure, no a la marcada pel Current
			long posicioFisica = this.recordstore.getNextRecordID();
			
			// Afegir el nou Record al RecordStore: recordstore.addRecord(byte[] datos, int offset, int numBytes);
			this.recordstore.addRecord(object.getData(),0,object.size());
			
			// Modifiquem conseq�entment l'�ndex : crear l'entrada i inserir-la.
			// El seu next l'assignar� convenientment el propi �ndex a l'operaci� de put.
			int tamany = object.size();
			String nomClasse = object.getClass().getName();
			
			PDAStoreRecordIndex entradaIndex = new PDAStoreRecordIndex(label, posicioFisica, tamany, nomClasse); 
			
			boolean exit = this.pdastoreindexvector.put(entradaIndex);
			if(!exit) throw new PDAException(178);
		}
		catch (RecordStoreNotOpenException e)
		{
			throw new PDAException(153);
		}
		catch (RecordStoreFullException e)
		{
			throw new PDAException(154);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(155);
		}
	}

	public PDAStoreable readUnique(String label) throws PDAException
	{		
		try
		{
			// Recuperem l'entrada de l'�ndex
			PDAStoreRecordIndex entradaIndex = this.pdastoreindexvector.get(label);
			if(entradaIndex == null) return null;//throw new PDAException(156);

			// Obtenim la posici� f�sica on es troba el PDAStoreable que volem llegir
			long posicioFisica = entradaIndex.getPos();
			if(posicioFisica<1 || posicioFisica>=this.recordstore.getNextRecordID()) throw new PDAException(157);
			
			// Recuperem les dades del PDAStoreable a retornar
			int tamanyRecord = this.recordstore.getRecordSize((int)posicioFisica);
			byte[] byteInputData = new byte[tamanyRecord];
			byteInputData = this.recordstore.getRecord((int)posicioFisica);
			
			// Retornar el PDAStoreable
			String nomClasse = new String(entradaIndex.getClassName());
			Class classeobjecte = Class.forName(nomClasse);
			
			PDAStoreable objecte = (PDAStoreable)classeobjecte.newInstance();
			objecte.setData(byteInputData,entradaIndex.getSize());
			
			//NO passem al PDAStoreable seg�ent: aix� ho fa el mateix programador amb la crida a l'operaci� Next()

			return objecte;
		}
		catch (RecordStoreNotOpenException e)
		{
			throw new PDAException(158);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(159);
		}
		catch (ClassNotFoundException e)
		{
			throw new PDAException(160);
		}
		catch (InstantiationException e)
		{
			throw new PDAException(161);
		}
		catch (IllegalAccessException e)
		{
			throw new PDAException(162);
		}
	}
	
	public boolean delete(String label) throws PDAException
	{		
		try
		{
			// Recuperem l'entrada de l'�ndex
			PDAStoreRecordIndex entradaIndex = this.pdastoreindexvector.get(label);
			if(entradaIndex == null) throw new PDAException(163);

			// Obtenim la posici� f�sica on es troba el PDAStoreable que volem esborrar
			long posicioFisica = entradaIndex.getPos();
			
			if(posicioFisica<1 || posicioFisica>=this.recordstore.getNextRecordID()) throw new PDAException(164);
			
			// Esborrem el PDAStoreable
			this.recordstore.deleteRecord((int)posicioFisica);
			
			// Modifiquem conseq�entment l'�ndex : esborrar l'entrada
			return this.pdastoreindexvector.delete(label);
		}
		catch (RecordStoreNotOpenException e)
		{
			throw new PDAException(165);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(166);
		}
	}

	public boolean update(PDAStoreable object, String label) throws PDAException
	{	
		// Comprovaci� dels par�metres
		if(object == null) throw new PDAException(167);
		if(object.size()<=0) throw new PDAException(168);
				
		try
		{
			// Recuperem l'entrada de l'�ndex
			PDAStoreRecordIndex entradaIndex = this.pdastoreindexvector.get(label);
			if(entradaIndex == null) throw new PDAException(169);
			
			// Obtenim la posici� f�sica on es troba el PDAStoreable que volem llegir
			long posicioFisica = entradaIndex.getPos();

			if(posicioFisica<1 || posicioFisica>=this.recordstore.getNextRecordID()) throw new PDAException(170);
			
			// Modifiquem el PDAStoreable al PDALabeledStore
			//setRecord(int id, byte[] datonuevo, int offset, int tama�o)
			this.recordstore.setRecord((int)posicioFisica, object.getData(),0,object.size());

			// Modifiquem conseq�entment l'�ndex : modificar l'entrada
			int tamany = object.size();
			String nomClasse = object.getClass().getName();
			
			entradaIndex = new PDAStoreRecordIndex(label, posicioFisica, tamany, nomClasse);
			
			return this.pdastoreindexvector.update(entradaIndex);	
		}
		catch (RecordStoreNotOpenException e)
		{
			throw new PDAException(171);
		}
		catch (RecordStoreFullException e)
		{
			throw new PDAException(172);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(173);
		}
	}
	
	/**
	 * Posiciona el PDALabeledStore al primer PDAStoreable que guarda.
	 * @return Cert si s'ha pogut fer el posicionament.
	 */
	public boolean First()
	{
		PDAStoreRecordIndex p = this.pdastoreindexvector.First();
		
		if(p != null) return true;
		return false;
	}


	/**
	 * Posiciona el PDALabeledStore al proper PDAStoreable que guarda.
	 * @return Cert si s'ha pogut fer el posicionament.
	 */
	public boolean Next()
	{
		PDAStoreRecordIndex p = this.pdastoreindexvector.Next();
		
		if(p != null) return true;
		return false;
	}

	/**
	 * Diu si s'est� posicionat a l'�ltim PDAStoreable.
	 * @return Cert si s'est� a l'�ltima posici�. Fals en cas contrari.
	 */	
	public boolean EOF()
	{
		return this.pdastoreindexvector.EOF();
	}

	/**
	 * Comprova si ja existeix un PDAStoreable amb l'etiqueta passada per par�metre consultant l'index.
	 * @param label String amb l'etiqueta a comprovar.
	 * @return Cert si la posici� ja est� ocupada.
	 */
	private boolean existsPDAStoreable(String label)
	{
		if(this.pdastoreindexvector.get(label)!=null) return true;
		return false;
	}	

	/**
	 * Tanca el PDALabeledStore i salva l'�ndex.
	 * @throws PDAException 
	 */
	public void closePDAStore() throws PDAException
	{
		try
		{	
			//Guardem l��ndex a disc
			saveIndex();
			
			//Tanquem l'�ndex
			closeIndex();
			
			//Tanquem el RecordStore
			this.recordstore.closeRecordStore();
		}
		catch (RecordStoreNotOpenException e)
		{
			throw new PDAException(174);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(175);
		}
	}

	/**
	 * Esborra el PDALabeledStore i el seu �ndex associat.
	 * @throws PDAException 
	 */
	public void deletePDAStore() throws PDAException
	{
		try
		{	
			// Esborrem l'�ndex associat
			deleteIndex();	
			
			//Esborrem el RecordStore
			String name = this.recordstorename;								
			RecordStore.deleteRecordStore(name);			
	    
		}
		catch (RecordStoreNotOpenException e)
		{
			throw new PDAException(176);
		}
		catch (RecordStoreException e)
		{
			throw new PDAException(177);
		}
	}
	
	
	/**
	 * Guarda el contingut de l'�ndex a mem�ria (PDAStoreIndexVector) al disc (PDAStoreIndexRMS)<br/>
	 * El programador pot cridar-la per tal de salvar l'�ndex quan vulgui.
	 * @throws PDAException 
	 */
	public void saveIndex() throws PDAException
	{
		this.pdastoreindexvector.saveIndex();
	}

	/**
	 * Tanca l'�ndex associat.
	 * @throws PDAException 
	 */
	private void closeIndex() throws PDAException
	{
		this.pdastoreindexvector.closeIndex();
	}	

	/**
	 * Esborra l'�ndex associat.
	 * @throws PDAException 
	 */	
	private void deleteIndex() throws PDAException
	{
		this.pdastoreindexvector.deleteIndex();
	}

	/**
	 * M�tode privat de la desenvolupadora per fer-lo servir a mode d'ajuda durant la creaci� de l'API. Obt� el contingut del PDALabeledStore en un Vector.
	 * @return Vector amb el contingut del PDALabeledStore.
	 * @throws PDAException de tipus desconegut, ja que aquest m�tode no pertany oficialment a l'API.
	 */
	private Vector llegirPDALabeledStore() throws PDAException
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
	 * M�tode de la desenvolupadora per fer-lo servir a mode d'ajuda durant la creaci� de l'API. Obt� el contingut del PDALabeledStore en un String.
	 * @return String amb el contingut del PDALabeledStore
	 * @throws PDAException de tipus desconegut, ja que aquest m�tode no pertany oficialment a l'API.
	 */
	public String printToString() throws PDAException
	{
		Vector dades = llegirPDALabeledStore();
		
		StringBuffer s=new StringBuffer();
		int i=0;
		int size = dades.size(); 
			
		while (i<size)
		{
		   s.append("(i="+i+") "+(String)dades.elementAt(i));
		   s.append("\n");
		   i++;
		}
		
		String impressio = "\nPDALabeledStore (fitxer):\n";
		impressio += s.toString();
		impressio += this.pdastoreindexvector.printToString();
		
		return impressio;
	}
	
}
