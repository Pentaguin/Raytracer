//https://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-sphere-intersection
//https://mathinsight.org/definition/magnitude_vector
//https://www.khanacademy.org/computing/computer-programming/programming-natural-simulations/programming-vectors/a/vector-magnitude-normalization
//https://www.mathsisfun.com/algebra/vectors-cross-product.html
//https://www.mathsisfun.com/algebra/vectors-dot-product.html
//https://www.siyavula.com/read/science/grade-10/vectors-and-scalars/20-vectors-and-scalars-03

#ifndef _VECT_H
#define _VECT_H

#include "math.h"

class Vect {
    public:
	/*variables*/
	double x, y, z;

	/*constructors*/
    Vect ();
	Vect (double , double , double );
	
	//functions
	double getVectX();
	double getVectY();
	double getVectZ();
	
	double magnitude();	
	Vect normalize();	
	Vect negative();	
	double dotProduct(Vect);	
	Vect crossProduct(Vect);
	Vect vectAdd (Vect);
	Vect vectMult (double);
};

#endif