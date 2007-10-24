/**
* CatalogueInfo is a class that keeps information about a list of Celestial objects or
* entries in a star catalogue. It has a vector of ObjectInfo objects which keeps 
* information about celestial objects. It has the option to add objects to the vector 
* and to obtain specific object of catalogue.
*/

package Hevelius.catalogues;
import java.util.Vector;

public class CatalogueInfo {
	private Vector<ObjectInfo> objects = new Vector<ObjectInfo>();
	private String catalogue;
	private int elements = 0;
	/**
        * This is a constructor for a CatalogueInfo instance. It receives
        * the name of catalogue.
        * @param name   String with catalogue's name.
        */
	public CatalogueInfo(String cat)
	{
		catalogue = cat;
	}

	/**
        * This is a method for adding an ObjectInfo instance to the vector. It receives
        * information about a celestial object and creates a new instance of ObjectInfo
	* and then adds it to the vector.
        * @param name   String with object's name in catalogue.
        * @param rah    Right Ascension Degrees.
        * @param ram    Right Ascension Arcmins.
        * @param ras    Right Ascension Arcsecs.
        * @param decg   Declination Degrees.
        * @param decm   Declination Arcmins.
        * @param decs   Declination Arcsecs.
        */
	public void add(String name, int rah, int ram, double ras, int decg, int decm, double decs)
	{
		//System.out.println(seq+" "+rah+" "+ram+" "+decg+" "+decm);
		//System.out.println();
		//System.out.println("RA:__"+rah+"_"+ram+"_"+ras);
		//System.out.println("DEC:__"+decg+"_"+decm+"_"+decs);
		//System.out.println(seq+"__"+ ((double)rah+ram/60.0d+ras/3600.0d) + "__" + ((double)decg+decm/60.0d+decs/3600.0d));
		ObjectInfo temp;
		temp = new ObjectInfo(name, rah, ram, ras, decg, decm, decs);
		objects.add(temp);
		elements++;
	}

	/**
        * Returns the number of objects available on catalogue.
        * @return       Int with the number of objects in catalogue, if any.
        */
	public int getLength()
	{
		return elements;
	}

	/**
        * Returns the number of objects available on catalogue.
	* @param i	Int of catalogue's object number i.
        * @return       ObjectInfo instance with information about number i catalogue's object.
        */
	public ObjectInfo get(int i)
	{
		return objects.get(i);
	}
}
