/**
 * ReadCatalogue is a class for retrieveing data from star catalogues
 * that are placed on default directories. The first place where it 
 * looks for catalogues is the relative path "Hevelius/catalogues/*".
 * This path is relative to the package so it has some built in 
 * catalogues. After that it looks for catalogues on a user folder 
 * to allow customization of catalogues. The path it will look in 
 * is "/home/userloign/.hevelius/catalogues/*". It will only consider 
 * files with ".cat" extension and only if there is a file with the 
 * same name but ".par" extension. Those files are the catalogue data 
 * and the parsing information for that particular catalogue.
 * <p>
 * Parsing information is as follows:
 * ra_i|ra_f,ram_i|ram_f,ras_i|ras_f,dec_sign,decg_i|decg_f,decm_i|decm_f,decs_i|decs_f,Type,Format
 * <table>
 * <tr><td>ra_i|ra_f</td><td>Those are the bytes where ra resides on file, either on hour or degree.</td></td></tr>
 * <tr><td>ram_i|ram_f</td><td>Those are the bytes where ra/60 resides on file, either on hour/60 or degree/60.</td><td></tr>
 * <tr><td>ras_i|ras_f</td><td>Those are the bytes where ram/60 resides on file, either on hour/3600 or degree/3600.</td><td></tr>
 * <tr><td>dec_sign</td><td>This is the position of the declination sign.</td></tr>
 * <tr><td>decg_i|decg_f</td><td>Those are the bytes where dec resides on file as degree.</td></tr>
 * <tr><td>decm_i|decm_f</td><td>Those are the bytes where dec/60 resides on file as degree/60.</td></tr>
 * <tr><td>decs_i|decs_f</td><td>Those are the bytes where decm/60 resides on file as degree/3600.</td></tr>
 * <tr><td>Type</td><td>This is the byte where the more precise coordinate is decided. 0.- Hr/Deg 1.- Min 2.- Sec.</td></tr>
 * <tr><td>Format</td><td>This is the format of ra coordinate. 0.- Hour 1.- Degree.</td></tr>
 * </table>
 */

package Hevelius.catalogues;

import java.util.regex.*;
import java.io.*;
public class ReadCatalogue {
	/**
	 * Looks for catalogues within specified directory. It returns a
	 * string array with all the catalogues satisfying the filter. The filter 
	 * looks for files with extension ".cat" and makes sure that a file 
	 * with same name but extension ".par" also exists.
	 * @param dir	The directory to look for catalogues.
	 * @return	String[] with all catalogues available in that dir.
	 */
	public static String[] searchCatalogues(File dir)
	{
		String[] children;

		FilenameFilter filter = new FilenameFilter() 
		{
			public boolean accept(File dir, String name) 
			{
				Pattern pat;
				Matcher mat;
				pat = Pattern.compile("(.*)\\.cat$");
				File cat;
				mat = pat.matcher(name);
				if(mat.find())
				{
					cat = new File(dir+"/"+mat.group(1)+".par");
					if(cat.exists())
					{
						return true;
					}
					else return false;
				}
				else
					return false;
			}
		};
		children = dir.list(filter);
		return children;
	}

	/**
	 * This methos id used to parse a given catalogue using its parser file 
	 * to know how to read it. It returns a CatalogueInfo which has a ObjectInfo vector
	 * ObjectInfo with each celestial object and its information.
	 * @param name				The name of the catalogue to parse.
	 * @return				CatalogueInfo that contains Celestial ObjectInfo of the catalogue.
	 * @throws FileNotFoundException		Exception that is thrown if File is not found.
	 * @throws IOException			Exception that is thrown if there is a Input/Output problem.
	 *
	 * @see	CatalogueInfo
	 * @see	ObjectInfo
	 */
	public static CatalogueInfo parseCatalogue(String name)
	{
		CatalogueInfo cat = null;
		Pattern pat;
		Matcher mat;
		pat = Pattern.compile("(.*)\\.cat$");
		mat = pat.matcher(name);
		File catf, parf;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		BufferedReader br = null;

		int seq_i, seq_f;
		int rah_i, rah_f, ram_i, ram_f, ras_i, ras_f;
		int decsign, decg_i, decg_f, decm_i, decm_f, decs_i, decs_f;
		int type, format;
		seq_i = 0;
		seq_f = 0;
			rah_i = 0;
			rah_f = 0;
			ram_i = 0;
			ram_f = 0;
			ras_i = 0;
			ras_f = 0;
			decsign = 0;
			decg_i = 0;
			decg_f = 0;
			decm_i = 0;
			decm_f = 0;
			decs_i = 0;
			decs_f = 0;
			type = 0;
			format = 0;

			String objname;
			int rah, ram;
			double ras;
			int decg, decm;
			double decs;
			double tempdeg, tempmin;

			try
			{
				if(mat.find())
				{

					cat = new CatalogueInfo(mat.group(1));
					catf = new File(mat.group(1)+".cat");
					parf = new File(mat.group(1)+".par");

					//pat = Pattern.compile("(.*?)|(.*?),(.*?)|(.*?),(.*?),(.*?)|(.*?),(.*?)|(.*?)");
					pat = Pattern.compile("(.*)\\|(.*),(.*)\\|(.*),(.*)\\|(.*),(.*)\\|(.*),(.*),(.*)\\|(.*),(.*)\\|(.*),(.*)\\|(.*),(.*),(.*)");

					fis = new FileInputStream(parf);
					bis = new BufferedInputStream(fis);
					dis = new DataInputStream(bis);
					br = new BufferedReader(new InputStreamReader(dis));

					while (dis.available() != 0) 
					{
						// this statement reads the line from the file and print it to
						// the console.
						mat = null;
						mat = pat.matcher(br.readLine());
						//System.out.println(mat.group());
						if(mat.find())
						{
							seq_i = Integer.parseInt(mat.group(1))-1;
							seq_f = Integer.parseInt(mat.group(2));
							rah_i = Integer.parseInt(mat.group(3))-1;
							rah_f = Integer.parseInt(mat.group(4));
							ram_i = Integer.parseInt(mat.group(5))-1;
							ram_f = Integer.parseInt(mat.group(6));
							ras_i = Integer.parseInt(mat.group(7))-1;
							ras_f =  Integer.parseInt(mat.group(8));
							decsign = Integer.parseInt(mat.group(9))-1;
							decg_i = Integer.parseInt(mat.group(10))-1;
							decg_f = Integer.parseInt(mat.group(11));
							decm_i = Integer.parseInt(mat.group(12))-1;
							decm_f = Integer.parseInt(mat.group(13));
							decs_i = Integer.parseInt(mat.group(14))-1;
							decs_f = Integer.parseInt(mat.group(15));
							type = Integer.parseInt(mat.group(16));
							format = Integer.parseInt(mat.group(17));
						}
						//System.out.println(dis.readLine());
					}
					fis = new FileInputStream(catf);
					bis = new BufferedInputStream(fis);
					dis = new DataInputStream(bis);
					br = new BufferedReader(new InputStreamReader(dis));

					while (dis.available() != 0)
					{
						// this statement reads the line from the file and print it to
						// the console.
						String temp = br.readLine();
						objname = temp.substring(seq_i, seq_f).trim();
						if(type==0)
						{
							tempdeg = Double.parseDouble(temp.substring(rah_i, rah_f).trim());
							rah = (int)tempdeg;
							tempmin = (tempdeg-rah)*60;
							ram = (int)tempmin;
							ras = (tempmin-ram)*60;
							tempdeg = Double.parseDouble(temp.substring(decg_i, decg_f).trim());
							decg = (int)tempdeg;
							tempmin = (tempdeg-decg)*60;
							decm = (int)tempmin;
							decs = (tempmin-decm)*60;
						}
						else
						{
							rah = Integer.parseInt(temp.substring(rah_i, rah_f).trim());
							decg = Integer.parseInt(temp.substring(decg_i, decg_f).trim());
							if(type==1)
							{
								tempmin = Double.parseDouble(temp.substring(ram_i, ram_f).trim());
								ram = (int)tempmin;
								ras = (tempmin-ram)*60;
								tempmin = Double.parseDouble(temp.substring(decm_i, decm_f).trim());
								decm = (int)tempmin;
								decs = (tempmin-decm)*60;
							}
							else
							{
							ram = Integer.parseInt(temp.substring(ram_i, ram_f).trim());
							decm = Integer.parseInt(temp.substring(decm_i, decm_f).trim());
							ras = Double.parseDouble(temp.substring(ras_i, ras_f).trim());
							decs = Double.parseDouble(temp.substring(decs_i, decs_f).trim());
							}
						}
						if(temp.charAt(decsign)=='-')
							decg = -decg;
						if(format==0)
						{
							rah *= 15;
							ram *= 15;
							ras *= 15;
						}
						cat.add(objname, rah, ram, ras, decg, decm, decs);
					}
				}
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			return cat;
		}
	}
