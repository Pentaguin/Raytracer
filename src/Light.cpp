//https://www.scratchapixel.com/lessons/3d-basic-rendering/introduction-to-shading/shading-lights
#include "Light.h"

//functions
Vect Light::getLightPosition () { return position; }
Color Light::getLightColor () { return color; }

Light::Light (Vect p, Color c):
position(p),color(c)
{
	
}
