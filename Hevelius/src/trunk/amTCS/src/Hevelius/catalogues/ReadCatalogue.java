package Hevelius.catalogues;

import java.util.regex.*;
import java.io.*;

	public class ReadCatalogue {
		public static String[] searchCatalogues(File dir)
		{
			//File dir = new File("directoryName");
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

			int seq;
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
					while (dis.available() != 0) 
					{
						// this statement reads the line from the file and print it to
						// the console.
						mat = null;
						mat = pat.matcher(dis.readLine());
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

					while (dis.available() != 0)
					{
						// this statement reads the line from the file and print it to
						// the console.
						String temp = dis.readLine();
						seq = Integer.parseInt(temp.substring(seq_i, seq_f).trim());
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
						cat.add(seq, rah, ram, ras, decg, decm, decs);
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
/*
		public static void main(String[] args)
		{
			String[] list;
			String[] custom;
			int j = 0;
			custom = searchCatalogues(new File("~/.hevelius/catalogues/"));
			list = searchCatalogues(new File("."));
			int objs = 0;
			if(list!=null)
				objs = list.length;
			if(custom!=null)
				objs += custom.length;
			CatalogueInfo[] catData = new CatalogueInfo[objs];
			if (list == null) 
			{
				// Either dir does not exist or is not a directory
				System.out.println("No match");
			} 
			else 
			{
				for (int i=0; i<list.length; i++) 
				{
					// Get filename of file or directory
					String filename = list[i];
					System.out.println(filename);
					parseCatalogue(catData[j], filename);
					j++;
				}
			}

			if (custom == null)
			{
				// Either dir does not exist or is not a directory
				System.out.println("No match");
			}
			else
			{
				for (int i=0; i<custom.length; i++)
				{
					// Get filename of file or directory
					String filename = custom[i];
					System.out.println(filename);
					parseCatalogue(catData[j], filename);
					j++;
				}
			}
		}
*/
	}
