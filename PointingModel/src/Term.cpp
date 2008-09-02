#include "Term.h"

Term::Term()
{
}

Term::Term(const char *n, term f1, term f2, bool harmonic)
{
	this->name = new char[strlen(n)+1];
	strcpy(this->name,n);
	this->t1 = f1;
	this->t2 = f2;
	this->used = 1;
	this->harmonic = harmonic;
	this->l = 0;
	this->m = 0;
}

Term::~Term()
{
	delete this->name;
}

char *Term::getName()
{
	return this->name;
}

void Term::setlm(int l, int m, bool first, int mount)
{
	this->l = l;
	this->m = m;
	this->hstate = first;
	if(mount == 1)
	{
		ht1 = hzero_eq;
		ht2 = hzero_eq;
		if(first)
			ht1 = Ylm_eq;
		else
			ht2 = Ylm_eq;
	}
	else if(mount == 0)
	{
		ht1 = hzero_aa;
		ht2 = hzero_aa;
		if(first)
			ht1 = Ylm_aa;
		else
			ht2 = Ylm_aa;
	}
}

double Term::Off1(double a1, double a2, double lat)
{
	if(harmonic)
		return ht1(this->l, this->m, a1,a2,lat);
	if(t1 != NULL)
		return t1(a1,a2,lat);
	return 0.0;
}

double Term::Off2(double a1, double a2, double lat)
{
	if(harmonic)
		return ht2(this->l, this->m, a1,a2,lat);
	if(t2 != NULL)
		return t2(a1,a2,lat);
	return 0.0;
}

void Term::setState(bool used)
{
	this->used = used;
}

bool Term::getState()
{
	return this->used;
}

int Term::getl()
{
	return this->l;
}
int Term::getm()
{
	return this->m;
}

bool Term::getHarmState()
{
	return this->hstate;
}
