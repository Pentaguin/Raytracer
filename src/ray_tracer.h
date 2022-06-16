#include <vector>
#include "Color.h"
#include "Vect.h"
#include "Object.h"
#include "Source.h"

class Ray_tracer{

public:

//functions
int winningObjectIndex(std::vector<double>);
Color getColorAt(Vect,Vect,std::vector<Object*>,int,std::vector<Source*>, double);

};