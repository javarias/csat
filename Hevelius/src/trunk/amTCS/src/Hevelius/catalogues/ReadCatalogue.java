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
						cat = new File(mat.group(1)+".par");
						if(cat.exists())
							return true;
						else return false;
					}
					else
						return false;
				}
			};
			children = dir.list(filter);

			return children;
		}

		public static void parseCatalogue(CatalogueInfo cat, String name)
		{
			Pattern pat;
                        Matcher mat;
                        pat = Pattern.compile("(.*)\\.cat$");
			mat = pat.matcher(name);
			File catf, parf;
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			DataInputStream dis = null;

			int seq_i, seq_f;
			int rah_i, rah_f, ram_i, ram_f;
			int decs, decg_i, decg_f, decm_i, decm_f;

			int seq;
			int rah, ram;
			double ras;
			int decg, decm;
			double decs;

			try
			{
				if(mat.find())
				{
					catf = new File(mat.group(1)+".cat");
					parf = new File(mat.group(1)+".par");

					pat = Pattern.compile("(.*)|(.*),(.*)|(.*),(.*),(.*)|(.*),(.*)|(.*)");

					fis = new FileInputStream(parf);
					bis = new BufferedInputStream(fis);
					dis = new DataInputStream(bis);
					while (dis.available() != 0) 
					{
						// this statement reads the line from the file and print it to
						// the console.
						mat = pat.matcher(dis.readLine());
						seq_i = mat.group(1);
						seq_f = mat.group(2);
						rah_i = mat.group(3);
						rah_f = mat.group(4);
						decs = mat.group(5);
						decg_i = mat.group(6);
						decg_f = mat.group(7);
						decm_i = mat.group(8);
						decm_f = mat.group(9);
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
                                                //System.out.println(dis.readLine());
						seq = substring(seq_i, seq_f);
						rah = substring(rah_i, rah_f);
						ram = substring(ram_i, ram_f);
						decg = substring(decg_i, decg_f);
						decm = substring(decm_i, decm_f);

						cat.add(seq, rah, ram, decg, decm);
                                        }
				}
			}
			catch(FileNotFoundException e)
			{
			}
			catch(IOException e)
			{
			}
		}

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
	}
