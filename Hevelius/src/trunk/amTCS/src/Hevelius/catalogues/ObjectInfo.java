/**
* ObjectInfo is a class that keeps information about a Celestial object or
* an entry in a star catalogue.
*/
package Hevelius.catalogues;

public class ObjectInfo {
	private String name;
	private double ra, dec;
	/**
	* This is a constructor for an ObjectInfo instance. It receives 
	* information about a celestial object and calculates the correct 
	* coordinates from them.
	* @param name	String with object's name in catalogue.
	* @param rah	Right Ascension Degrees.
	* @param ram	Right Ascension Arcmins.
	* @param ras	Right Ascension Arcsecs.
	* @param decg	Declination Degrees.
	* @param decm	Declination Arcmins.
	* @param decs	Declination Arcsecs.
	*/
	public ObjectInfo(String name, int rah, int ram, double ras, int decg, int decm, double decs)
	{
		this.name = name;
		ra = (double)rah + ram/60.0d + ras/3600;
		if(decg > 0)
			dec = (double)decg + decm/60.0d + decs/3600;
		else
			dec = (double)decg - decm/60.0d - decs/3600;
	}

	/**
	* Returns the value of the private variable seq.
	* @return	String with name of object in catalogue, if any.
	*/
	public String getName()
	{
		return name;
	}

	/**
	* Returns the value of private variable ra.
	* @return	Double with Right Ascension of object.
	*/
	public double getRa()
	{
		return ra;
	}

        /**
        * Returns the value of private variable ra.
        * @return       Double with Declination of object.
        */
	public double getDec()
	{
		return dec;
	}
}
