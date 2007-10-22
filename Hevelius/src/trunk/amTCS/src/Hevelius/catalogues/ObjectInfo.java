/**
* ObjectInfo is a class that keeps information about a Celestial object or
* an entry in a star catalogue.
*/
package Hevelius.catalogues;

public class ObjectInfo {
	int seq;
	double ra, dec;
	/**
	* This is a constructor for an ObjectInfo instance. It receives 
	* information about a celestial object and calculates the correct 
	* coordinates from them.
	* @param seq	Sequence number of the object.
	* @param rah	Right Ascension Degrees.
	* @param ram	Right Ascension Arcmins.
	* @param ras	Right Ascension Arcsecs.
	* @param decg	Declination Degrees.
	* @param decm	Declination Arcmins.
	* @param decs	Declination Arcsecs.
	*/
	public ObjectInfo(int seq, int rah, int ram, double ras, int decg, int decm, double decs)
	{
		this.seq = seq;
		ra = (double)rah + ram/60.0d + ras/3600;
		if(decg > 0)
			dec = (double)decg + decm/60.0d + decs/3600;
		else
			dec = (double)decg - decm/60.0d - decs/3600;
	}

	/**
	* Returns the value of the private variable seq.
	* @return	Int with sequential number of object in catalogue.
	*/
	public int getSeq()
	{
		return seq;
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
