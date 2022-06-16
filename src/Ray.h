#ifndef _RAY_H
#define _RAY_H

#include "Vect.h"

class Ray {
	//private variables
	Vect origin, direction;
	
	public:
	
	//constructor
	Ray (Vect,Vect);

	//functions
	Vect getRayOrigin() ;
	Vect getRayDirection();
	
};


#endif