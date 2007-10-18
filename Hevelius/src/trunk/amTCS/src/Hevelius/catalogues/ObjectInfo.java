package Hevelius.catalogues;

public class ObjectInfo {
	int seq;
	double ra, dec;
	public ObjectInfo(int seq, int rah, int ram, double ras, int decg, int decm, double decs)
	{
		this.seq = seq;
		ra = (double)rah + ram/60.0d + ras/3600;
		if(decg > 0)
			dec = (double)decg + decm/60.0d + decs/3600;
		else
			dec = (double)decg - decm/60.0d - decs/3600;
	}
	public int getSeq()
	{
		return seq;
	}
	public double getRa()
	{
		return ra;
	}
	public double getDec()
	{
		return dec;
	}
}
