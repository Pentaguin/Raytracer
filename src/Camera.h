
#include "Vect.h"

class Camera {
	// Variables
	Vect cameraPosition, cameraDirection, cameraRight, cameraDown;
	
	public:
	//constructor
	Camera (Vect, Vect, Vect, Vect);
	
	//functions
	Vect getCameraPosition ();
	Vect getCameraDirection ();
	Vect getCameraRight ();
	Vect getCameraDown ();
	
};

