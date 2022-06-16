#ifndef _SOURCE_H
#define _SOURCE_H

class Source {
	public:
	//no implementation. only declareration.
	virtual Vect getLightPosition() =0;
	virtual Color getLightColor() =0;
	
};

#endif