#ifndef _LIGHT_H
#define _LIGHT_H

#include "Vect.h"
#include "Color.h"
#include "Source.h"

class Light : public Source {//derived class
	Vect position;
	Color color;
	
	public:
	
	Light (Vect, Color);
	
	// method functions
	 Vect getLightPosition ();
	 Color getLightColor ();
	
};


#endif