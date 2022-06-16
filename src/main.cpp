
//https://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-sphere-intersection
//https://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/minimal-ray-tracer-rendering-spheres
//https://processing.org/tutorials/pixels/

#include <iostream>
#include <fstream>
#include <vector>
#include "Vect.h"
#include "Ray.h"
#include "Camera.h"
#include "Color.h"
#include "Light.h"
#include "Object.h"
#include "basic_objects.h"
#include "renderer.h"
#include "scene_reader.h" 
#include "ray_tracer.h"
#include "Source.h"
using namespace std;

vector<Object*> sceneObjects;


void makeCube(Vect corner1, Vect corner2, Color color){
//corner 1
double c1x=corner1.getVectX();
double c1y=corner1.getVectY();
double c1z=corner1.getVectZ();
//corner 2
double c2x=corner2.getVectX();
double c2y=corner2.getVectY();
double c2z=corner2.getVectZ();

Vect A(c2x,c1y,c1z);
Vect B(c2x,c1y,c2z);
Vect C(c1x,c1y,c2z);
Vect D(c2x,c2y,c1z);
Vect E(c1x,c2y,c1z);
Vect F(c1x,c2y,c2z);

//a cube has 6 faces. 1 face is a half triangle. so we need 12 triangles to make a cube.

//left side
sceneObjects.push_back(new Triangle(D,A,corner1,color));
sceneObjects.push_back(new Triangle(corner1,E,D,color));
//far side
sceneObjects.push_back(new Triangle(corner2,B,A,color));
sceneObjects.push_back(new Triangle(A,D,corner2,color));
//right side
sceneObjects.push_back(new Triangle(F,C,B,color));
sceneObjects.push_back(new Triangle(B,corner2,F,color));
//front
sceneObjects.push_back(new Triangle(E,corner1,C,color));
sceneObjects.push_back(new Triangle(C,F,E,color));
//top
sceneObjects.push_back(new Triangle(D,E,F,color));
sceneObjects.push_back(new Triangle(F,corner2,D,color));
//bottom
sceneObjects.push_back(new Triangle(corner1,A,B,color));
sceneObjects.push_back(new Triangle(B,C,corner1,color));

}


int main(){

	struct RGBType {	
	double r;
	double g;
	double b;
	};
	
	int width = 1000;
	int height = 1000;
	int wh = width*height;
	
	int aadepth = 1;
	double aspectratio = (double)width/(double)height;
	double ambientlight = 0.3;	//used to scale the color

	
	/** colors**/
	Color prettyWhite (1.0, 1.0, 1.0, 0.3);
	Color prettyGreen (0.5, 1.0, 0.5, 0.3);
	Color red (0.5, 0.25, 0.25, 0.8);
	Color tileFloorColor (1, 1, 1, 2);
	Color gray (0.5, 0.5, 0.5, 0);
	Color black (0.0, 0.0, 0.0, 0);
	Color purple(0.98,0.66,0.93,0);
	Color whiteLight (1.0, 1.0, 1.0, 0);
	Color orange(0.94,0.75,0.31,0);

	Vect vect (0,1,0);
	Vect lightPosition (-2,5,-10);
	Vect lightPosition2 (5,2,5);
	Vect lightPosition3 (5,2,-1);
	Vect sphereLocation (1.75, -0.25, 0);
	Vect cameraPosition (5, 2.0, -6);//to view the world in the image
	Vect lookAt (0, 0, 0);
	Vect cameraDifference (cameraPosition.getVectX() - lookAt.getVectX(), cameraPosition.getVectY() - lookAt.getVectY(), cameraPosition.getVectZ() - lookAt.getVectZ());	//cameraPosition - lookAt
	
	Vect cameraDirection = cameraDifference.negative().normalize();
	Vect cameraRight = vect.crossProduct(cameraDirection).normalize();
	Vect cameraDown = cameraRight.crossProduct(cameraDirection);
	Camera sceneCam (cameraPosition, cameraDirection, cameraRight, cameraDown);

	// scene objects
	Sphere sphere (sphereLocation, 0.5, red);
	Surface surface (vect, -1, tileFloorColor);
	Light sceneLight (lightPosition, orange);
	Light sceneLight2 (lightPosition2, whiteLight);
	Light sceneLight3 (lightPosition3, whiteLight);
	Triangle sceneTriangle (Vect(3,0,0) ,Vect(0,2.4,0), Vect(0,0,2),purple);
	vector<Source*> lightSources;

	//store the scene objects.
	sceneObjects.push_back(&sphere);
	sceneObjects.push_back(&surface);
	sceneObjects.push_back(&sceneTriangle);
	lightSources.push_back(&sceneLight);
	lightSources.push_back(&sceneLight2);
	lightSources.push_back(&sceneLight3);
	
	makeCube(Vect(0.5,0.5,0.5),Vect(-0.5,-0.5,-0.5),orange);

	Ray_tracer ray_tracer;
	Scene_reader scene_reader;

	int location, aa_index;
	double NDCx, NDCy;//NDC= normalized Device Coordinates
	double tempRed, tempGreen, tempBlue;
	
	RGBType *pixels = new RGBType[wh];

	for (int x = 0; x < width; x++) {	
		for (int y = 0; y < height; y++) {
			location = y*width + x;
			
			// start with a blank pixel
			double tempRed[1];
			double tempGreen[1];
			double tempBlue[1];
			
			for (int aax = 0; aax < aadepth; aax++) {
				for (int aay = 0; aay < aadepth; aay++) {
			
					aa_index = aay*aadepth + aax;
					
					// create the ray from the camera to this pixel
					if (aadepth == 1) {
						
						if (width > height) {
							// the image is wider than it is tall
							NDCx = ((x+0.5)/width)*aspectratio - (((width-height)/(double)height)/2);
							NDCy = ((height - y) + 0.5)/height;
						}
						else if (height > width) {
							// the imager is taller than it is wide
							NDCx = (x + 0.5)/ width;
							NDCy = (((height - y) + 0.5)/height)/aspectratio - (((height - width)/(double)width)/2);
						}
						else {
							// the image is square
							NDCx = (x + 0.5)/width;
							NDCy = ((height - y) + 0.5)/height;
						}
					}
					
					Vect cameraRayOrigin = sceneCam.getCameraPosition();
					Vect cameraRayDirection = cameraDirection.vectAdd(cameraRight.vectMult(NDCx - 0.5).vectAdd(cameraDown.vectMult(NDCy - 0.5))).normalize();
					Ray cameraRay (cameraRayOrigin, cameraRayDirection);//used to look for intersections.
					
					vector<double> intersections;
					
					for (int i = 0; i < sceneObjects.size(); i++) {
					 	intersections.push_back(sceneObjects.at(i)->findIntersection(cameraRay));//arrow operator to access the member of the class.
					}//loops through all the sceneObjects. to see if the cameraRay intersects with the objects and push it all into the intersection vector.
					
					
					int winningObjects = ray_tracer.winningObjectIndex(intersections);
					//The winningObjects variable is the result of sorting all possible objects in the scene that could be in front 
					//of the current ray which comes out of the camera and then determining which of those objects is DIRECTLY in front of that ray
					
					if (winningObjects == -1) {	// no objects found. set background color to black.
					
						tempRed[aa_index] = 0;
						tempGreen[aa_index] = 0;
						tempBlue[aa_index] = 0;
					}
					else{
						// index coresponds to an object in our scene
							// determine the position and direction vectors at the point of intersection
							
							Vect intersectionPosition = cameraRayOrigin.vectAdd(cameraRayDirection.vectMult(intersections.at(winningObjects)));
							Vect intersectionRayDirection = cameraRayDirection;
		
							Color intersectionColor = ray_tracer.getColorAt(intersectionPosition, intersectionRayDirection, sceneObjects, winningObjects,lightSources, ambientlight);
							
							tempRed[aa_index] = intersectionColor.getColorRed();
							tempGreen[aa_index] = intersectionColor.getColorGreen();
							tempBlue[aa_index] = intersectionColor.getColorBlue();
						
					}
					vector<double>().swap(intersections);	//to allocate memory
				}
			}
			

			// average the pixel color
			double totalRed = 0;
			double totalGreen = 0;
			double totalBlue = 0;
			
			for (int iRed = 0; iRed < 1; iRed++) {
				totalRed = totalRed + tempRed[iRed];
			}
			for (int iGreen = 0; iGreen < 1; iGreen++) {
				totalGreen = totalGreen + tempGreen[iGreen];
			}
			for (int iBlue = 0; iBlue < 1; iBlue++) {
				totalBlue = totalBlue + tempBlue[iBlue];
			}
			
			double avgRed = totalRed;
			double avgGreen = totalGreen;
			double avgBlue = totalBlue;
					
			pixels[location].r = avgRed;
			pixels[location].g = avgGreen;
			pixels[location].b = avgBlue;
	
	
		}
	}
	
	
	//RENDERING BMP FILE
	FILE *f;
	f = fopen("raytracer.bmp","wb");// bmp must be included
	Renderer render(f,width,height);
	for (int i = 0; i < wh; i++) {	//loops through the whole image. width x height
		RGBType rgb = pixels[i];
		
		//rgb collors always has a limit of 255. but we use 1 instead of 255. So now we change the 0-1 to 0-255.
		double red = (pixels[i].r)*255;
		double green = (pixels[i].g)*255;
		double blue = (pixels[i].b)*255;
		char color[3] = {(char)floor(blue), (char)floor(green), (char)floor(red)};	//floor to round down the values.
		
		fwrite(color,1,3,f);	//write to image
	}
	fclose(f);
	vector<Object*>().swap(sceneObjects);	//allocate memory from the vectors
	vector<Source*>().swap(lightSources);
	delete pixels, tempRed, tempGreen, tempBlue,f;	//delete the pointers and arrays to deallocate memories.
	
	return 0;
}