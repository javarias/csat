package alma.SAFETY_MODULE.SafetyImpl;

public class Moon
{
	int cD,cM,cMp,cF,cL,cR,cB;
	public Moon(int cD,int cM,int cMp,int cF,int cL,int cR)
	{
		this.cD = cD;
		this.cM = cM;
		this.cMp = cMp;
		this.cF = cF;
		this.cL = cL;
		this.cR = cR;
	}
	public Moon(int cD,int cM,int cMp,int cF,int cB)
	{
                this.cD = cD;
                this.cM = cM;
                this.cMp = cMp;
                this.cF = cF;
		this.cB = cB;
	}
	public int getD()
	{
		return cD;
	}
        public int getM()
        {
                return cM;
        }
        public int getMp()
        {
                return cMp;
        }
        public int getF()
        {
                return cF;
        }
        public int getL()
        {
                return cL;
        }
        public int getR()
        {
                return cR;
        }
        public int getB()
        {
                return cB;
        }
}
