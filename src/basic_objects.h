
#include "Vect.h"
#include "Color.h"
#include "Object.h"
#include <math.h>
#include <iostream>

class Triangle: public Object {	//derived class of Object
	/*variables*/
	Vect normal;
    Vect A,B,C;
	double distance;
	Color color;
	
	public:
	/*constructor*/
	Triangle (Vect, Vect,Vect, Color);

	/*functions*/
	Vect getTriangleNormal ();
	double getTriangleDistance ();
	Color getColor ();
	Vect getNormalAt(Vect);
	double findIntersection(Ray);
	
};



class Sphere: public Object {	//derived class of Object
	/*variables*/
	Vect center;
	double radius;
	Color color;
	
	public:
	/*constructor*/
	Sphere (Vect, double, Color);

	/*functions*/
	Vect getSphereCenter();
	double getSphereRadius();
	Color getColor();
	Vect getNormalAt(Vect);
	double findIntersection(Ray);

};


class Surface: public Object { //derived class of Object
	/*variables*/
	Vect normal;
	double distance;
	Color color;
	
	public:
	/*constructor*/
	Surface(Vect,double ,Color);

	/*functions*/
 	Vect getSurfaceNormal ();
    double getSurfaceDistance ();
    Color getColor ();
    Vect getNormalAt(Vect) ;
    double findIntersection(Ray);

};

