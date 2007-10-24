package Hevelius.catalogues;
import java.util.Vector;

public class CatalogueInfo {
	private Vector<ObjectInfo> objects = new Vector<ObjectInfo>();
	private String catalogue;
	private int elements = 0;
	public CatalogueInfo(String cat)
	{
		catalogue = cat;
	}
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
	public int getLength()
	{
		return elements;
	}
	public ObjectInfo get(int i)
	{
		return objects.get(i);
	}
}
