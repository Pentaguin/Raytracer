#include "Vect.h"

Vect::Vect () {
	x = 0;
	y = 0;
	z = 0;
}

Vect::Vect (double x1, double y1, double z1) :
x(x1),y(y1),z(z1)
{

}

double Vect::getVectX() { return x; }
double Vect::getVectY() { return y; }
double Vect::getVectZ() { return z; }
	
double Vect::magnitude () {			//length of vector
	return sqrt((x*x) + (y*y) + (z*z));
}
	
Vect Vect::normalize () {			//vector must be normalized. Only the magnitude change. Not the direction.
	double magnitude = sqrt((x*x) + (y*y) + (z*z));
	return Vect (x/magnitude, y/magnitude, z/magnitude);
}
	
Vect Vect::negative () {
	return Vect (-x, -y, -z);
}
	
double Vect::dotProduct(Vect v) {
	return x*v.getVectX() + y*v.getVectY() + z*v.getVectZ();
}
	
Vect Vect::crossProduct(Vect v) {
	return Vect (y*v.getVectZ() - z*v.getVectY(), z*v.getVectX() - x*v.getVectZ(), x*v.getVectY() - y*v.getVectX());
}
	
Vect Vect::vectAdd (Vect v) {
	return Vect (x + v.getVectX(), y + v.getVectY(), z + v.getVectZ());
}

Vect Vect::vectMult (double scalar) {
	return Vect (x*scalar, y*scalar, z*scalar);
}



