#ifndef _COLOR_H
#define _COLOR_H



class Color {
	/*variables*/
	double red, green, blue, special;
	
	public:
	/*constructors*/
	Color (double , double , double , double );

	//getters*/
	double getColorRed();
	double getColorGreen();
	double getColorBlue();
	double getColorSpecial();
	/*setters*/
	double setColorRed(double );
	double setColorGreen(double );
	double setColorBlue(double ) ;
	double setColorSpecial(double );
	double brightness();

	Color colorScalar(double);
	Color colorAdd(Color);
	Color colorMultiply(Color);
	Color colorAverage(Color);
	Color clip();
 };


#endif