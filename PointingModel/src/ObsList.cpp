#include "ObsList.h"
#include <gsl/gsl_linalg.h>
#include <gsl/gsl_vector.h>
#include <gsl/gsl_matrix.h>
#include <gsl/gsl_multifit.h>
#include <gsl/gsl_blas.h>


ObsList::ObsList(int mount, double lat)
{
	this->n = 0;
	this->dn = 9;
	this->hn = 0;
	this->cn = 0;
	this->nterms = 6;//6;
	if(mount == 0)
	{
		this->dn = 7;
	//	this->nterms = 7;
	}
	this->mount = mount;
	this->lat = lat;
	this->obs = NULL;
	this->mat = NULL;
	this->vec = NULL;
	this->coeffs = NULL;
	this->inuseTerms = NULL;
	this->harmonicTerms = NULL;
	this->customTerms = NULL;
	this->offsets[0] = 0;
	this->offsets[1] = 0;
	this->initDefaultTerms();
	//defaultTerms[0]->setState(false);
	//defaultTerms[1]->setState(false);
	//defaultTerms[2]->setState(false);
	//defaultTerms[3]->setState(false);
	//defaultTerms[4]->setState(false);
	//defaultTerms[5]->setState(false);
	//defaultTerms[6]->setState(false);
}

ObsList::~ObsList()
{
	int i;
	for(i=0;i<this->dn;i++)
		delete defaultTerms[i];
}

void ObsList::add(double traT,double tdecT,double traE,double tdecE, double st)
{
	int i;
	double r;
	Obs **tobs;
	double lat = this->lat*PI/180.;

	tobs = this->obs;
	this->obs = new Obs*[this->n+1];
	if(this->n > 0)
		memcpy(obs,tobs,n*sizeof(Obs*));
	if(tobs != NULL)
		delete[] tobs;
	tobs = NULL;
	this->obs[this->n] = new Obs(traT, tdecT, traE, tdecE, st,this->lat);
	this->n++;
	if(this->mat != NULL)
		delete[] this->mat;
	if(this->vec != NULL)
		delete[] this->vec;
	this->mat = NULL;
	this->vec = NULL;
}

void ObsList::createMatrix()
{
	int i,j;
	double lat = this->lat*PI/180.;
	double dec, h, alt, azm;
	if(this->mat != NULL)
		delete[] this->mat;
	this->mat = NULL;
	if(this->n > 0)
		this->mat = new double[2*this->nterms*this->n];
	for(i=0;i<this->n;i++)
	{
		h = this->obs[i]->gethT()*PI/180.;
		dec = this->obs[i]->getDecT()*PI/180.;
		alt = this->obs[i]->getAltT()*PI/180.;
		azm = this->obs[i]->getAzmT()*PI/180.;
		for(j=0;j<this->nterms;j++)
		{
			if(mount == 1)
			{
				this->mat[this->nterms*i+j] = cos((this->obs[i]->getDecT()+this->obs[i]->getDecE())/2*PI/180.)*inuseTerms[j]->Off1(h,dec,lat);
				this->mat[this->nterms*this->n+this->nterms*i+j] = inuseTerms[j]->Off2(h,dec,lat);
			}
			else if(mount == 0)
			{
				this->mat[this->nterms*i+j] = inuseTerms[j]->Off1(alt,azm,lat);
				this->mat[this->nterms*this->n+this->nterms*i+j] = inuseTerms[j]->Off2(alt,azm,lat);
			}
			//printf("%lf ",this->mat[this->nterms*i+j]);
			//printf("%lf ",this->mat[this->nterms*this->n+this->nterms*i+j]);
		}
		//printf("xx\n");
	}
}

void ObsList::createVector()
{
	int i,j;
	if(this->vec != NULL)
		delete[] this->vec;
	this->vec = NULL;
	if(this->n > 0)
		this->vec = new double[2*this->n];
	for(i=0;i<this->n;i++)
	{
		if(mount == 1)
		{
			this->vec[i] = cos((this->obs[i]->getDecT()+this->obs[i]->getDecE())/2*PI/180.)*(this->obs[i]->gethE()-this->obs[i]->gethT());
			this->vec[this->n+i] = (this->obs[i]->getDecE() - this->obs[i]->getDecT());
		}
		else if(mount == 0)
		{
			this->vec[i] = (this->obs[i]->getAltE() - this->obs[i]->getAltT());
			this->vec[this->n+i] = (this->obs[i]->getAzmE() - this->obs[i]->getAzmT());
			printf("Vec %lf %lf\n", this->vec[i],this->vec[this->n+i]);
		}
	}
}

void ObsList::resetObsList()
{
	int i;
	if(this->obs != NULL)
	{
		for(i = 0; i < this->n ; i++)
			if(this->obs[i] != NULL)
				delete this->obs[i];
		delete[] this->obs;
		this->obs = NULL;
	}
	this->n = 0;
}

void ObsList::resetAll()
{
	int i;
	this->resetObsList();
	if(this->mat != NULL)
		delete[] this->mat;
	if(this->vec != NULL)
		delete[] this->vec;
	this->mat = NULL;
	this->vec = NULL;
	if(this->inuseTerms != NULL)
	{
		free(inuseTerms);
		inuseTerms = NULL;
	}
	if(this->harmonicTerms != NULL)
	{
		for(i=0;i<this->hn;i++)
			delete this->harmonicTerms[i];
		free(harmonicTerms);
		harmonicTerms = NULL;
		this->hn = 0;
	}
	if(this->customTerms != NULL)
	{
		for(i=0;i<this->cn;i++)
			delete this->customTerms[i];
		free(customTerms);
		customTerms = NULL;
		this->cn = 0;
	}
	this->nterms = 0;
}

void ObsList::initDefaultTerms()
{
	if(this->mount == 1)
	{
		defaultTerms[0] = new Term("IH",IHh,IHd,false);
		defaultTerms[1] = new Term("ID",IDh,IDd,false);
		defaultTerms[2] = new Term("NP",NPh,NPd,false);
		defaultTerms[3] = new Term("CH",CHh,CHd,false);
		defaultTerms[4] = new Term("ME",MEh,MEd,false);
		defaultTerms[5] = new Term("MA",MAh,MAd,false);
		defaultTerms[6] = new Term("TF",TFh,TFd,false);
		defaultTerms[7] = new Term("FO",FOh,FOd,false);
		defaultTerms[8] = new Term("DAF",DAFh,DAFd,false);
	}
	else if(this->mount == 0)
	{
		defaultTerms[0] = new Term("IA",IAe,IAa,false);
		defaultTerms[1] = new Term("IE",IEe,IEa,false);
		defaultTerms[2] = new Term("NPAE",NPAEe,NPAEa,false);
		defaultTerms[3] = new Term("CA",CAe,CAa,false);
		defaultTerms[4] = new Term("AN",ANe,ANa,false);
		defaultTerms[5] = new Term("AW",AWe,AWa,false);
		defaultTerms[6] = new Term("TF",TFe,TFa,false);
	}
}

void ObsList::addHarmonicTerm()
{
	int i,j,len,l,m;
	char *nameh, *named;
	if(harmonicTerms != NULL)
	{
		for(i=0;i<this->hn;i++)
			delete harmonicTerms[i];
		free(harmonicTerms);
	}
	this->hn = this->hn + 2;

	//Esto no va aqui!! esta para probar nada mas...
	this->nterms += 2;
	//
	harmonicTerms = (Term **) malloc (this->hn*sizeof(Term *));
	for(i=0;i<this->hn/2;i++)
	{
		j = 0;
		len = i;
		while(len > 0)
		{
			len = len/10;
			j++;
		}
		nameh = new char[2+j+1];
		named = new char[2+j+1];
		sprintf(nameh,"HTH%d",i+1);
		sprintf(named,"HTD%d",i+1);
		harmonicTerms[2*i] = new Term(nameh,NULL,NULL,true);
		harmonicTerms[2*i+1] = new Term(named,NULL,NULL,true);
		l = 0;
		m = 0;
		while(l*(l+1)+m != i+1)
		{
			if(m < l)
				m++;
			else
			{
				l++;
				m = -l;
			}
		}
		harmonicTerms[2*i]->setlm(l,m, true, this->mount);
		harmonicTerms[2*i+1]->setlm(l,m, false, this->mount);
		delete nameh;
		delete named;
	}
}

void ObsList::selectedTerms()
{
	int i,j;
	Term *tmp;
	if(inuseTerms != NULL)
	{
		free(inuseTerms);
		inuseTerms = NULL;
	}
	if(this->nterms > 0)
		inuseTerms = (Term **) malloc (this->nterms*sizeof(Term *));
	i = 0;
	while(i<this->nterms)
	{
		for(j=0;j<this->dn && i != this->nterms;j++)
			if(defaultTerms[j]->getState())
			{
				inuseTerms[i] = defaultTerms[j];
				i++;
			}
		for(j=0;j<hn && i != this->nterms;j++)
			if(harmonicTerms[j]->getState())
			{
				inuseTerms[i] = harmonicTerms[j];
				i++;
			}
		for(j=0;j<cn && i != this->nterms;j++)
			if(customTerms[j]->getState())
			{
				inuseTerms[i] = customTerms[j];
				i++;
			}
	}
}

double *ObsList::getMatrix()
{
	return this->mat;
}

double *ObsList::getVector()
{
	return this->vec;
}

int ObsList::getNumObs()
{
	return this->n;
}

double ObsList::Off1(int i,double a1,double a2,double lat)
{
	if(i >=0 && i<this->nterms)
		return inuseTerms[i]->Off1(a1,a2,lat);
	return 0.0;
}

double ObsList::Off2(int i,double a1,double a2,double lat)
{
	if(i >=0 && i<this->nterms)
		return inuseTerms[i]->Off2(a1,a2,lat);
	return 0.0;
}

int ObsList::getNumTerms()
{
	return this->nterms;
}

void ObsList::cCoeffs()
{
	int i,j;
	double *m, *v;
	double *res;
	gsl_matrix_view mat;
	gsl_matrix *V;
	gsl_vector_view vec;
	gsl_vector *S, *x;
	gsl_multifit_linear_workspace *W;
	double chi;
	gsl_matrix *cov;
	this->createVector();
	if(this->nterms > 0 && this->n > this->nterms/2)
	{
		this->selectedTerms();
		this->createMatrix();
		x = gsl_vector_alloc(this->nterms);

		W = gsl_multifit_linear_alloc(2*this->n,this->nterms);
		cov = gsl_matrix_alloc(this->nterms,this->nterms);

		m = this->mat;
		v = this->vec;
		mat = gsl_matrix_view_array(m,2*this->n,this->nterms);
		vec = gsl_vector_view_array(v,2*this->n);


//
/*
		printf("Mat Original: \n");
		for(i=0;i<mat.matrix.size1;i++)
		{
			printf("\n%d.-\t",i+1);
			for(j=0;j<mat.matrix.size2;j++)
				printf("%lf ",mat.matrix.data[j+i*mat.matrix.size2]);
		}
		printf("\n");
		for(i=0;i<vec.vector.size;i++)
			printf("%lf\n",vec.vector.data[i]);
		printf("\n%dx%d\n\n",mat.matrix.size1,mat.matrix.size2);
*/
//

		gsl_multifit_linear (&mat.matrix, &vec.vector, x, cov, &chi, W);

//
/*
		gsl_vector *C = gsl_vector_alloc(2*this->n);
		gsl_blas_dgemv(CblasNoTrans, 1.0, &mat.matrix,x, 0.0, C);
		for(i=0;i<C->size;i++)
		{
			//if(i < 11)
			//	C->data[i] = -18.4/3600.;
			printf("%lf\n",C->data[i]*3600);
		}
		gsl_vector_sub(C,&vec.vector);
		printf("\n");
		for(i=0;i<vec.vector.size;i++)
			printf("%lf\n",vec.vector.data[i]*3600);
		printf("\n");
		double rchi = 0;
		double p = 0;
		for(i=0;i<C->size;i++)
		{
			p += C->data[i];
			rchi += pow(C->data[i],2);
			printf("%lf\n",C->data[i]*3600);
		}
		p = p/C->size;
		double r = 0;
		for(i=0;i<C->size;i++)
			r += pow(C->data[i]-p,2);
		rchi = sqrt(rchi);
		printf("Chi?: %lf\n",3600*rchi);
		gsl_vector_free(C);
*/

		printf("Error cuadratico: %lf [Arcsecs]\n\n", sqrt(chi)*3600);
		delete [] this->coeffs;
		this->coeffs = new double[this->nterms];
		for(i=0;i<this->nterms;i++)
			this->coeffs[i] = x->data[i];
		//this->coeffs[0] = -252.45/3600.;
		//this->coeffs[1] = -26.53/3600.;
		//this->coeffs[2] = 53.73/3600.;
		//this->coeffs[3] = -189.64/3600.;
		//this->coeffs[4] = 8.45/3600.;
		//this->coeffs[5] = -14.86/3600.;
		//this->coeffs[6] = -9.57/3600.;
		//this->coeffs[0] = 18.4/3600.;
		//this->coeffs[0] = -15.451/3600.;
		//this->coeffs[0] = 18.14/3600.;
		for(i=0;i<this->nterms;i++)
			printf("C%d: %lf [Arcsecs]\n",i,this->coeffs[i]*3600);
		printf("\n");
		gsl_vector_free(x);
		gsl_multifit_linear_free(W);
		gsl_matrix_free(cov);
	}
	else
		for(i = 0; i < this->nterms; i++)
			this->coeffs[i] = 0;
}

void ObsList::cOffs()
{
	int i,j;
	Obs *tmp;
	double lat,rms, rmso, mean, meano, psd, psdo;
	double alt, azm, dec, h, Offset1, Offset2;
	double diff1, diff2, diffo1, diffo2, disto, distn;
	FILE *fn, *fo, *gn, *go;
	fn = fopen("data/outputap.dat", "w+");
	fo = fopen("data/outputbp.dat", "w+");
	gn = fopen("data/outputapg.dat", "w+");
	go = fopen("data/outputbpg.dat", "w+");
	lat = this->lat*PI/180;
	rms = 0;
	rmso = 0;
	mean = 0;
	meano = 0;
	psd = 0;
	psdo = 0;
	fprintf(gn,"A:dZ\n");
	fprintf(go,"A:dZ\n");
	for(j=0;j<this->n;j++)
	{
		dec = this->obs[j]->getDecT()*PI/180.;
		h = this->obs[j]->gethT()*PI/180.;
		alt = this->obs[j]->getAltT()*PI/180.;
		azm = this->obs[j]->getAzmT()*PI/180.;
		Offset1 = 0;
		Offset2 = 0;
		for(i=0;i<this->nterms;i++)
		{
			if(mount == 1)
			{
				Offset1 += this->coeffs[i]*this->Off1(i,h,dec,lat);
				Offset2 += this->coeffs[i]*this->Off2(i,h,dec,lat);
			}
			else if(mount == 0)
			{
				Offset1 += this->coeffs[i]*this->Off1(i,alt,azm,lat);
				Offset2 += this->coeffs[i]*this->Off2(i,alt,azm,lat);
				//printf("O%d: %lf %lf\n",i+1,this->coeffs[i]*this->Off1(i,alt,azm,lat)*3600,this->coeffs[i]*this->Off2(i,alt,azm,lat)*3600);
			}
		}
		printf("%d\n",j+1);
		printf("Off%d: %lf [Arcsecs]\n", 1, Offset1*3600);
		printf("Off%d: %lf [Arcsecs]\n", 2, Offset2*3600);
		//diffo1 = this->vec[j]*3600;
		//diffo2 = this->vec[j+this->n]*3600;
		diffo1 = -(this->obs[j]->gethT()-this->obs[j]->gethE())*3600;
		diffo2 = -(this->obs[j]->getDecT()-this->obs[j]->getDecE())*3600;
		//rmso += pow(diffo1,2) + pow(diffo2,2);
		if(mount == 1)
		{
			diff1 = -(Offset1+this->obs[j]->gethT()-this->obs[j]->gethE())*3600;
			diff2 = -(Offset2+this->obs[j]->getDecT()-this->obs[j]->getDecE())*3600;
			//disto = 3600 * sqrt( pow(diffo1/3600 * cos((this->decT[j] + this->decE[j])/2*PI/180.),2) + pow(diffo2/3600,2));
			//distn = 3600 * sqrt( pow(diff1/3600 * cos((this->decT[j] + this->decE[j] + Offset2)/2*PI/180.),2) + pow(diff2/3600,2));
			disto = ((1-cos(diffo2/3600*PI/180.))/2 + cos(this->obs[j]->getDecT()*PI/180.)*cos(this->obs[j]->getDecE()*PI/180.)*(1-cos(diffo1/3600*PI/180.))/2);
			disto = acos(1 - 2*disto) * 3600 * 180 / PI;
			distn = ((1-cos(diff2/3600*PI/180.))/2 + cos((this->obs[j]->getDecT()+Offset2)*PI/180.)*cos(this->obs[j]->getDecE()*PI/180.)*(1-cos(diff1/3600*PI/180.))/2);
			distn = acos(1 - 2*distn) * 3600 * 180 / PI;
			printf("DiffO: ra: %lf [Arcsecs] \t dec: %lf [Arcsecs] \t rms: %lf [Arcsecs] \n",diffo1, diffo2, disto);
			printf("DiffN: ra: %lf [Arcsecs] \t dec: %lf [Arcsecs] \t rms: %lf [Arcsecs] \n",diff1, diff2, distn);
			printf("lala: %lf\n", acos(sin((this->obs[j]->getDecT()+Offset2)*PI/180.)*sin(this->obs[j]->getDecE()*PI/180.) + cos((this->obs[j]->getDecT()+Offset2)*PI/180.)*cos(this->obs[j]->getDecE()*PI/180.)*cos(diff1/3600*PI/180.))*3600*180/PI );
			fprintf(fn,"%lf:%lf\n",diff1*cos((this->obs[j]->getDecT() + this->obs[j]->getDecE()+Offset2)/2.*PI/180.),diff2);
			fprintf(fo,"%lf:%lf\n",diffo1*cos((this->obs[j]->getDecT() + this->obs[j]->getDecE())/2.*PI/180.),diffo2);
			tmp = new Obs(this->obs[j]->getRaT()-Offset1, this->obs[j]->getDecT()+Offset2, this->obs[j]->getRaE(), this->obs[j]->getDecE(), this->obs[j]->getSt(), this->obs[j]->getLat());
			fprintf(gn,"%lf:%lf\n",tmp->getAzmT(),tmp->getdZ()*3600);
			fprintf(go,"%lf:%lf\n",this->obs[j]->getAzmT(),this->obs[j]->getdZ()*3600);
			//rms += pow(diff1,2) + pow(diff2,2);
			rmso += pow(disto,2);
			rms += pow(distn,2);
			//meano += disto;
			mean  += distn;
			//meano += 0;
			//mean += disto - distn;
			psdo += pow(disto,2);
			psd += pow(distn,2);
			printf("\n");
		}
		else if(mount == 0)
		{
			diff1 = -(Offset1+this->obs[j]->getAltT()-this->obs[j]->getAltE())*3600;
			diff2 = -(Offset2+this->obs[j]->getAzmT()-this->obs[j]->getAzmE())*3600;
			printf("DiffO: ra: %lf [Arcsecs] \t dec: %lf [Arcsecs]\n",this->vec[j]*3600, this->vec[j+this->n]*3600);
			printf("DiffN: ra: %lf [Arcsecs] \t dec: %lf [Arcsecs]\n",diff1, diff2);
			fprintf(fn,"%lf:%lf\n",diff2,diff1);
			fprintf(fo,"%lf:%lf\n",diffo2,diffo1);
			rms += pow(diff1,2) + pow(diff2,2);
			printf("\n");
		}
	}
	//psd = psd - mean*mean/this->n;
	printf("%lf %lf\n", psd, mean);
	mean = mean/this->n;
	meano = meano/this->n;
	rms = sqrt(rms/this->n);
	rmso = sqrt(rmso/this->n);
	psd = psd - this->n * pow(mean,2);
	psdo = psdo - this->n * pow(meano,2);
	psd = sqrt(psd/(this->n));
	psdo = sqrt(psdo/(this->n));
	printf("Rms: %lf PSD: %lf %lf\n", rms, psd, rms+psd);
	printf("Rmso: %lf PSD: %lf %lf\n", rmso, psdo, psdo);
	fprintf(fn,"rms%lf",rms);
	fprintf(fo,"rms%lf",rmso);
	fclose(fn);
	fclose(fo);
	fclose(gn);
	fclose(go);
}

void ObsList::cOff(double inra, double indec, double st, double &out_ra_d, double &out_dec_d)
{
	int i,j;
	double lat,rms;
	double alt, azm, dec, h, Offset1, Offset2;
	lat = this->lat*PI/180;
	rms = 0;
	dec = indec*PI/180.;
	h = (st - inra)*PI/180.;
	alt = asin(sin(lat)*sin(dec)+cos(dec)*cos(lat)*cos(h));
	azm = atan2(sin(h),cos(h)*sin(lat)-tan(dec)*cos(lat));
	while(alt < 0)
		alt += 2*PI;
	while(alt > 2*PI)
		alt -= 2*PI;
	while(azm < 0)
		azm += 2*PI;
	while(azm > 2*PI)
		azm -= 2*PI;
	Offset1 = 0;
	Offset2 = 0;
	for(i=0;i<this->nterms;i++)
	{
		if(mount == 1)
		{
			Offset1 += this->coeffs[i]*this->Off1(i,h,dec,lat);
			Offset2 += this->coeffs[i]*this->Off2(i,h,dec,lat);
		}
		else if(mount == 0)
		{
			Offset1 += this->coeffs[i]*this->Off1(i,alt,azm,lat);
			Offset2 += this->coeffs[i]*this->Off2(i,alt,azm,lat);
		}
	}
	//printf("Off%d: %lf [Arcsecs]\n", 1, Offset1*3600);
	//printf("Off%d: %lf [Arcsecs]\n", 2, Offset2*3600);
	//printf("\n");

	out_ra_d = -Offset1;
	out_dec_d = Offset2;
}
