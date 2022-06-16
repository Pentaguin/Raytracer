#include "Camera.h"

	
//functions
Vect Camera::getCameraPosition () { return cameraPosition; }
Vect Camera::getCameraDirection () { return cameraDirection; }
Vect Camera::getCameraRight () { return cameraRight; }
Vect Camera::getCameraDown () { return cameraDown; }
	
Camera::Camera (Vect pos, Vect dir, Vect right, Vect down):
cameraPosition(pos),cameraDirection(dir),cameraRight(right),cameraDown(down)
{

}

