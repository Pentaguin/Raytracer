#include "Ray.h"

Ray::Ray (Vect o, Vect d):
origin(o),direction(d) //prefered notations
{

}

//functions
Vect Ray::getRayOrigin () { return origin; }
Vect Ray::getRayDirection () { return direction; }
	
