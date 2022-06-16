//https://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-sphere-intersection
//https://www.scratchapixel.com/lessons/3d-basic-rendering/introduction-to-shading/shading-normals
//https://math.stackexchange.com/questions/305642/how-to-find-surface-normal-of-a-triangle
//https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-rendering-a-triangle/ray-triangle-intersection-geometric-solution

#include "basic_objects.h"

/*******************************************SPHERE**************************************************/
Sphere::Sphere (Vect centerValue, double radiusValue, Color colorValue):
center(centerValue),radius(radiusValue),color(colorValue)	//preferred notation
{

}


/*getters*/
Vect Sphere::getSphereCenter () { return center; }
double Sphere::getSphereRadius () { return radius; }
Color Sphere::getColor () { return color; }


Vect Sphere::getNormalAt(Vect point) {
	// normal always points away from the center of a sphere
	Vect normalVect = point.vectAdd(center.negative()).normalize();
	return normalVect;
}
	
double Sphere::findIntersection(Ray ray) {
	// get the x,y,z vects
	Vect rayOrigin = ray.getRayOrigin();
	double rayOriginX = rayOrigin.getVectX();
	double rayOriginY = rayOrigin.getVectY();
	double rayOriginZ = rayOrigin.getVectZ();
		
	Vect rayDirection = ray.getRayDirection();
	double rayDirectionX = rayDirection.getVectX();
	double rayDirectionY = rayDirection.getVectY();
	double rayDirectionZ = rayDirection.getVectZ();
		
	Vect sphereCenter = center;
	double sphereCenterX = sphereCenter.getVectX();
	double sphereCenterY = sphereCenter.getVectY();
	double sphereCenterZ = sphereCenter.getVectZ();
		
	/****** Quadratric Formula *******/

	double a = 1; // normalized
	double b = (2*(rayOriginX - sphereCenterX)*rayDirectionX) + (2*(rayOriginY - sphereCenterY)*rayDirectionY) + (2*(rayOriginZ - sphereCenterZ)*rayDirectionZ);
	double c = pow(rayOriginX - sphereCenterX, 2) + pow(rayOriginY - sphereCenterY, 2) + pow(rayOriginZ - sphereCenterZ, 2) - (radius*radius);
		
	double discriminant = b*b - 4*a*c;	
		
		if (discriminant > 0) {
			/// the ray intersects the sphere
			
			// the first root
			double x1 = ((-b - sqrt(discriminant))/(2*a)) - 0.000001;	//the 0.000001 helps with the accuracy of the image.
			
			if (x1 > 0) {
				
				return x1;
			}
			else {
				
				double x2 = ((-b + sqrt(discriminant))/(2*a)) - 0.000001;
				return x2;
			}
		}
		else {
			// the ray doesn't intersect with the sphere = ray miss the sphere.
			return -1;
		}
	}



/**************************************************SURFACE *************************************************************/
Surface::Surface (Vect normalValue, double distanceValue, Color colorValue):
normal(normalValue),distance(distanceValue),color(colorValue)//preferred notation
{

}


	// getters methods
Vect Surface::getSurfaceNormal () { return normal; }
double Surface::getSurfaceDistance () { return distance; }
Color Surface::getColor () { return color; }
Vect Surface::getNormalAt(Vect point) {return normal;}
	
	
double Surface::findIntersection(Ray ray) {
		Vect rayDirection = ray.getRayDirection();
		
		double a = rayDirection.dotProduct(normal);
		
		if (a == 0) {
			// ray is parallel to the Surface
			return -1;
		}
		else {
			//dot product of the normal vector and another vector. the other vector is the result of the opposite direction of the ray origin.
			//the ray origin has been added with the multiplication of the distance.
			double b = normal.dotProduct(ray.getRayOrigin().vectAdd(normal.vectMult(distance).negative()));
			return -1*b/a;
		}
	}
	
/***********************TRIANGLE*****************************/

//constructor
Triangle::Triangle (Vect pointA, Vect pointB, Vect pointC,Color colorValue):
A(pointA),B(pointB),C(pointC),color(colorValue)//preferred notation
{
	
}
	
// getters
Vect Triangle::getTriangleNormal () { 
    Vect CA (C.getVectX() - A.getVectX(), C.getVectY() - A.getVectY(),C.getVectZ() -A.getVectZ());
    Vect BA (B.getVectX() - A.getVectX(), B.getVectY() - A.getVectY(),B.getVectZ() -A.getVectZ());
    normal= CA.crossProduct(BA).normalize();//normal of a triangle is crossproduct of 2 sides of a triangle
    return normal; 
}

double Triangle::getTriangleDistance () { 
    normal= getTriangleNormal();
    distance=normal.dotProduct(A);
    return distance; 
}
        

Color Triangle::getColor () { 
	return color; 
}

Vect Triangle::getNormalAt(Vect point) {//normal of any point of intersection.
   	normal=getTriangleNormal();
	return normal;
}


double Triangle::findIntersection(Ray ray) {
	Vect rayDirection = ray.getRayDirection();
    Vect rayOrigin=ray.getRayOrigin();
	normal=getTriangleNormal();
    distance=getTriangleDistance();

	double a = rayDirection.dotProduct(normal);
		
	if (a == 0) {
			// ray is parallel to the Triangle
		return -1;
	}
	else {
			
        double b = normal.dotProduct(ray.getRayOrigin().vectAdd(normal.vectMult(distance).negative()));
        double distanceToSurface=-1*b/a;

		//P = distance+ray
        double Px=rayDirection.vectMult(distanceToSurface).getVectX()+rayOrigin.getVectX();
        double Py=rayDirection.vectMult(distanceToSurface).getVectY()+rayOrigin.getVectY();
        double Pz=rayDirection.vectMult(distanceToSurface).getVectZ()+rayOrigin.getVectZ();

        Vect P  (Px,Py,Pz);	// point of intersection

		//[CAxPA]*n>=0
        Vect CA (C.getVectX() -A.getVectX(),C.getVectY() -A.getVectY(),C.getVectZ() -A.getVectZ());
        Vect PA (P.getVectX() -A.getVectX(),P.getVectY() -A.getVectY(),P.getVectZ() -A.getVectZ());
        double test1= (CA.crossProduct(PA)) .dotProduct(normal);
        //[BCxPC]*n>=0
        Vect BC (B.getVectX() -C.getVectX(),B.getVectY() -C.getVectY(),B.getVectZ() -C.getVectZ());
        Vect PC (P.getVectX() -C.getVectX(),P.getVectY() -C.getVectY(),P.getVectZ() -C.getVectZ());
        double test2= (BC.crossProduct(PC)).dotProduct(normal);
		//[ABxPB]*n>=0
        Vect AB (A.getVectX() -B.getVectX(),A.getVectY() -B.getVectY(),A.getVectZ() -B.getVectZ());
        Vect PB (P.getVectX() -B.getVectX(),P.getVectY() -B.getVectY(),P.getVectZ() -B.getVectZ());
        double test3= (AB.crossProduct(PB)).dotProduct(normal);
         
			
        if((test1>=0) && (test2>=0) && (test3>=0)){ //its inside triangle
                 
		return -1*b/a;
       
        }else{          //outside triangle
            return -1;
        }
	}
}
	


