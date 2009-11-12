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
package edu.upc.lsdatalib.fitxersequencialvalor;

/**
 * Classe que representa una entrada de l'�ndex PDAStoreIndex.
 * @author  N�ria Lara Arana
 */
class PDAStoreRecordIndex
{
	/** Etiqueta del PDAStoreable i identificador no �nic de l'entrada de l'�ndex (pot haver m�s d'una entrada amb la mateixa etiqueta). */
	private String label;
	
	/** Posici� f�sica de l'objecte. */
	private long pos;
	
	/** Tamany en bytes de l'objecte PDAStoreable.  */
	private int size;
	
	/** Nom de la classe de l'objecte guardat. */
	private String className;
	
	/** Posici� f�sica de l'entrada seg�ent. */
	private int next;
	
	
	/** 
	 * Constructora de la classe que t� com a par�metres la informaci� d'una entrada.<br/>
	 * En principi, quan es crea una nova entrada, aquesta sempre ser� l'�ltima i aix� �s el que es considera a aquesta constructora. �s el propi �ndex qui gestiona el camp next.
	 * @param plabel	Etiqueta del PDAStoreable del qual es crea l'entrada a l'�ndex i identificador de l'entrada.
	 * @param ppos 		Posici� f�sica del PDAStoreable del qual es crea l'entrada a l'�ndex.
	 * @param psize		Tamany en bytes de l'objecte PDAStoreable que es guarda i pel qual es crea l'entrada a l'�ndex.
	 * @param className Nom de la classe de l'objecte guardat.	
	 * */
	protected PDAStoreRecordIndex(String plabel, long ppos, int psize, String className)
	{
		this.label 	= plabel;
		this.pos	= ppos;
		this.size	= psize;
		this.className = className;
		this.next	= -1; 
	}

	/** 
	 * Constructora de la classe a partir d'una array.<br/>
	 * Aquesta array cont� un String amb les dades d'una entrada separades pel car�cter '|'.
	 * @param data Array de bytes a partir del qual es pot reconstruir una entrada de l'�ndex.
	 * */
	protected PDAStoreRecordIndex(byte[] data)
	{
		String dada = new String(data);
		
		int i1 = dada.indexOf("|");
		int i2 = dada.indexOf("|",i1+1);
		int i3 = dada.indexOf("|",i2+1);
		int i4 = dada.indexOf("|",i3+1);
		
		this.label 	= dada.substring(0,i1);
		this.pos	= Long.parseLong(dada.substring(i1+1,i2));
		this.size	= Integer.parseInt(dada.substring(i2+1,i3));
		this.className = dada.substring(i3+1,i4);
		this.next	= Integer.parseInt(dada.substring(i4+1,dada.length()));
	}
	
	/** 
	 * Compara dues entrades (PDAStoreRecordIndex) de l'�ndex.
	 * @param o	PDAStoreRecordIndex a comparar amb l'actual.	
	 * @return Cert si els PDAStoreRecordIndex s�n iguals.
	 * */
	protected boolean equals(PDAStoreRecordIndex o)
	{
	      return (this.label.equals(o.label));
	}
	
	/**
	 * Actualitza l'entrada de l'�ndex sobre la qual s'aplica amb les dades de l'entrada passada per par�metre.<br/>
	 * Es permet fer canvis sobre els camps que afecten al tipus de PDAStoreable (canvis de tamany i de classe).
	 * @param p Entrada de l'�ndex amb les dades modificades.
	 */
	protected void update(PDAStoreRecordIndex p)
	{
		this.pos  = p.getPos();
		this.size = p.getSize();
		this.className = p.getClassName();
		this.next = p.getNext();
	}
	
	/**
	 * Transforma l'entrada de l'�ndex sobre la qual s'aplica en una array de bytes.<br/>
	 * Concatena els camps de l'entrada amb el car�cter '|' en un String i el transforma en una array.
	 * @return Array de bytes que representa l'entrada.
	 */
	protected byte[] makeData()
	{
		String data = this.label+"|"+String.valueOf(this.pos)+"|"+String.valueOf(this.size)+"|"+this.className+"|"+this.next;
		return data.getBytes();	
	}	
	
	/**
	 * @return Retorna label.
	 */
	protected String getLabel() {
		return label;
	}

	/**
	 * @return Retorna pos.
	 */
	protected long getPos() {
		return pos;
	}

	/**
	 * Actualitza pos
	 */
	protected void setPos(long pos) {
		this.pos = pos;
	}

	/**
	 * @return Retorna size.
	 */
	protected int getSize() {
		return size;
	}

	/**
	 * @return Retorna className.
	 */
	protected String getClassName() {
		return className;
	}

	/**
	 * @return Retorna next.
	 */
	protected int getNext() {
		return next;
	}

	/**
	 * Actualitza next.
	 */
	protected void setNext(int next) {
		this.next = next;
	}

}

