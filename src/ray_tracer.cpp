#include "ray_tracer.h"

int Ray_tracer::winningObjectIndex(std::vector<double> objectIntersections) {
	// return the index of the winning intersection
	int indexOfMinimumValue;
	
	
	if (objectIntersections.size() == 0) {
		// no intersections
		return -1;
	}
	else if (objectIntersections.size() == 1) {
		if (objectIntersections.at(0) > 0) {
			// if that intersection is greater than zero then its our index of minimum value
			return 0;
		}
		else {
			// otherwise the only intersection value is negative
			return -1;
		}
	}
	else {
		// more than one intersection
		// first find the maximum value
		
		double max = 0;
		for (int i = 0; i < objectIntersections.size(); i++) {
			if (max < objectIntersections.at(i)) {
				max = objectIntersections.at(i);
			}
		}
		
		// then starting from the maximum value find the minimum positive value
		if (max > 0) {
			// we only want positive intersections
			for (int i = 0; i < objectIntersections.size(); i++) {
				if (objectIntersections.at(i) > 0 && objectIntersections.at(i) <= max) {
					max = objectIntersections.at(i);
					indexOfMinimumValue = i;
				}
			}
			
			return indexOfMinimumValue;
		}
		else {
			// all the intersections were negative
			return -1;
		}
	}
}


Color Ray_tracer::getColorAt(Vect intersectionPosition, Vect intersectingRayDirection, std::vector<Object*> sceneObjects, int WinningObjects, std::vector<Source*> lightSources,  double ambientlight) {
	
	Color winningObjectColor = sceneObjects.at(WinningObjects)->getColor();
	Vect winningObjectNormal = sceneObjects.at(WinningObjects)->getNormalAt(intersectionPosition);
	
	if (winningObjectColor.getColorSpecial() == 2) {
		// tile floor pattern
		
		int square = (int)floor(intersectionPosition.getVectX()) + (int)floor(intersectionPosition.getVectZ());
		
		if ((square % 2) == 0) {
			// black tile
			winningObjectColor.setColorRed(0);
			winningObjectColor.setColorGreen(0);
			winningObjectColor.setColorBlue(0);
		}
		else {
			// white tile
			winningObjectColor.setColorRed(1);
			winningObjectColor.setColorGreen(1);
			winningObjectColor.setColorRed(1);
		}
	}
	
	Color finalColor = winningObjectColor.colorScalar(ambientlight);
	
	if (winningObjectColor.getColorSpecial() > 0 && winningObjectColor.getColorSpecial() <= 1) {
		// reflection from objects with specular intensity
		double dot1 = winningObjectNormal.dotProduct(intersectingRayDirection.negative());
		Vect scalar1 = winningObjectNormal.vectMult(dot1);
		Vect add1 = scalar1.vectAdd(intersectingRayDirection);
		Vect scalar2 = add1.vectMult(2);
		Vect add2 = intersectingRayDirection.negative().vectAdd(scalar2);
		Vect reflectionDirection = add2.normalize();
		
		Ray reflectionRay (intersectionPosition, reflectionDirection);
		
		// determine what the ray intersects with first
		std::vector<double> reflectionIntersections;
		
		for (int i = 0; i < sceneObjects.size(); i++) {
			reflectionIntersections.push_back(sceneObjects.at(i)->findIntersection(reflectionRay));
		}
		
		int winningObjectsReflection = winningObjectIndex(reflectionIntersections);
		
		if (winningObjectsReflection != -1) {
			// reflection ray missed everthing else
			
				// determine the position and direction at the point of intersection with the reflection ray
				// the ray only affects the color if it reflected off something
				
				Vect reflectionIntersectionPosition = intersectionPosition.vectAdd(reflectionDirection.vectMult(reflectionIntersections.at(winningObjectsReflection)));
				Vect reflectionIntersectionRayDirection = reflectionDirection;
				
				Color reflectionIntersectionColor = getColorAt(reflectionIntersectionPosition, reflectionIntersectionRayDirection, sceneObjects, winningObjectsReflection, lightSources, ambientlight);
				
				finalColor = finalColor.colorAdd(reflectionIntersectionColor.colorScalar(winningObjectColor.getColorSpecial()));
			
		}
	}
	
	for (int lightIndex = 0; lightIndex < lightSources.size(); lightIndex++) {
		Vect lightDirection = lightSources.at(lightIndex)->getLightPosition().vectAdd(intersectionPosition.negative()).normalize();
		
		float cosineAngle = winningObjectNormal.dotProduct(lightDirection);
		
		if (cosineAngle > 0) {
			// test for shadows
			bool shadowed = false;
			
			Vect distanceToLight = lightSources.at(lightIndex)->getLightPosition().vectAdd(intersectionPosition.negative()).normalize();
			float distanceToLightMagnitude = distanceToLight.magnitude();
			
			//ray in the direction from our intersection point to the lightsource and it will look if it will intersect with anything on its way.
			//if it does that means the intersection is in the shadow.
			Ray shadowRay (intersectionPosition, lightSources.at(lightIndex)->getLightPosition().vectAdd(intersectionPosition.negative()).normalize());
			
			std::vector<double> secondIntersection;
			
			for (int i = 0; i < sceneObjects.size() && shadowed == false; i++) {
				secondIntersection.push_back(sceneObjects.at(i)->findIntersection(shadowRay));
			}
			
			for (int i = 0; i < secondIntersection.size(); i++) {
				
					if (secondIntersection.at(i) <= distanceToLightMagnitude) {	//if the intersection is smaller than the distance to light.
						shadowed = true;					//that means that the pixel is shadowed.
					}
				
				break;
			}
			
			if (shadowed == false) { // if its not shadowed. then there is color.
				finalColor = finalColor.colorAdd(winningObjectColor.colorMultiply(lightSources.at(lightIndex)->getLightColor()).colorScalar(cosineAngle));
				
			}
			
		}
	}
	
	return finalColor.clip();
}