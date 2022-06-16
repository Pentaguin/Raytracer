#ifndef _OBJECT_H
#define _OBJECT_H

#include "Ray.h"
#include "Vect.h"
#include "Color.h"

class Object {	
	public:
	
	//no implementation. only declareration.
	virtual Color getColor()=0;
	virtual Vect getNormalAt(Vect)=0;	
	virtual double findIntersection(Ray)=0;

};


#endif