#include "scene_reader.h"


Scene_reader::Scene_reader(){

std::cout << "[sphere : " << "{x : 1.75," << " y : 0.25, "<< "z : 0, " << "radius : 0.5 }]"  <<"\n";
std::cout << "[surface : " << "{x : 0," << " y : 1, "<< "z : 0, " << "distanceValue : -1 }]"  <<"\n";
std::cout << "[sceneLight : " << "{x : -2," << " y : 5, "<< "z : 10  }]"  <<"\n";
std::cout << "[sceneLight2 : " << "{x : 5," << " y : 2, "<< "z : 5  }]"  <<"\n";
std::cout << "[sceneLight3 : " << "{x : 5," << " y : 2, "<< "z : -1  }]"  <<"\n";
std::cout << "[sceneTriangle : " << "{x1 : 3," << " y1 : 0, "<< "z1 : 0, " << "x2 : 0, " << "y2 : 2.4, " << "z2 : 0, " << "x3 : 0, " << "y3 : 0, " << "z3 : 2 }]"  <<"\n";
std::cout << "[cubeTriangle1 : " << "{x1 : -0.5," << " y1 : -0.5, "<< "z1 : 0.5, " << "x2 : -0.5, " << "y2 : 0.5, " << "z2 : 0.5, " << "x3 : 0.5, " << "y3 : 0.5, " << "z3 : 0.5 }]"  <<"\n";
std::cout << "[cubeTriangle2 : " << "{x1 : 0.5," << " y1 : 0.5, "<< "z1 : 0.5, " << "x2 : 0.5, " << "y2 : -0.5, " << "z2 : 0.5, " << "x3 : -0.5, " << "y3 : -0.5, " << "z3 : 0.5 }]"  <<"\n";
std::cout << "[cubeTriangle3 : " << "{x1 : -0.5," << " y1 : -0.5, "<< "z1 : -0.5, " << "x2 : -0.5, " << "y2 : 0.5, " << "z2 : -0.5, " << "x3 : -0.5, " << "y3 : 0.5, " << "z3 : 0.5 }]"  <<"\n";
std::cout << "[cubeTriangle4 : " << "{x1 : -0.5," << " y1 : 0.5, "<< "z1 : 0.5, " << "x2 : -0.5, " << "y2 : -0.5, " << "z2 : 0.5, " << "x3 : -0.5, " << "y3 : -0.5, " << "z3 : -0.5 }]"  <<"\n";
std::cout << "[cubeTriangle5 : " << "{x1 : 0.5," << " y1 : -0.5, "<< "z1 : -0.5, " << "x2 : 0.5, " << "y2 : 0.5, " << "z2 : -0.5, " << "x3 : -0.5, " << "y3 : 0.5, " << "z3 : -0.5 }]"  <<"\n";
std::cout << "[cubeTriangle6 : " << "{x1 : -0.5," << " y1 : 0.5, "<< "z1 : -0.5, " << "x2 : -0.5, " << "y2 : -0.5, " << "z2 : -0.5, " << "x3 : 0.5, " << "y3 : -0.5, " << "z3 : -0.5 }]"  <<"\n";
std::cout << "[cubeTriangle7 : " << "{x1 : 0.5," << " y1 : -0.5, "<< "z1 : 0.5, " << "x2 : 0.5, " << "y2 : 0.5, " << "z2 : 0.5, " << "x3 : 0.5, " << "y3 : 0.5, " << "z3 : -0.5 }]"  <<"\n";
std::cout << "[cubeTriangle8 : " << "{x1 : 0.5," << " y1 : 0.5, "<< "z1 : -0.5, " << "x2 : 0.5, " << "y2 : -0.5, " << "z2 : -0.5, " << "x3 : 0.5, " << "y3 : -0.5, " << "z3 : 0.5 }]"  <<"\n";
std::cout << "[cubeTriangle9 : " << "{x1 : -0.5," << " y1 : -0.5, "<< "z1 : 0.5, " << "x2 : 0.5, " << "y2 : -0.5, " << "z2 : 0.5, " << "x3 : 0.5, " << "y3 : -0.5, " << "z3 : -0.5 }]"  <<"\n";
std::cout << "[cubeTriangle10 : " << "{x1 : 0.5," << " y1 : -0.5, "<< "z1 : -0.5, " << "x2 : -0.5, " << "y2 : -0.5, " << "z2 : -0.5, " << "x3 : -0.5, " << "y3 :- 0.5, " << "z3 : 0.5 }]"  <<"\n";
std::cout << "[cubeTriangle11 : " << "{x1 : 0.5," << " y1 : -0.5, "<< "z1 : 0.5, " << "x2 : 0.5, " << "y2 : 0.5, " << "z2 : 0.5, " << "x3 : -0.5, " << "y3 : 0.5, " << "z3 : -0.5 }]"  <<"\n";
std::cout << "[cubeTriangle12 : " << "{x1 : -0.5," << " y1 : 0.5, "<< "z1 : -0.5, " << "x2 : 0.5, " << "y2 : 0.5, " << "z2 :- 0.5, " << "x3 : 0.5, " << "y3 : 0.5, " << "z3 : 0.5 }]"  <<"\n";

}