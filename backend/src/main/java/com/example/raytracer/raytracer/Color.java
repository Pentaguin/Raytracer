package com.example.raytracer.raytracer;

public class Color {

    // Translate the [0,1] component values to the byte range [0,255].
    public int toRGB(Vec3 pixelColor) {
        int colorScale = 256;
        Interval intensity = new Interval(0.000, 0.999);

        // Apply a linear to gamma transform for gamma 2
        double r = linearToGamma(pixelColor.x);
        double g = linearToGamma(pixelColor.y);
        double b = linearToGamma(pixelColor.z);

        int ir = (int) (colorScale * intensity.clamp(r));
        int ig = (int) (colorScale * intensity.clamp(g));
        int ib = (int) (colorScale * intensity.clamp(b));

        return (ir << 16) | (ig << 8) | ib;
    }

    public static double linearToGamma(double linearComponent)
    {
        if (linearComponent > 0)
            return Math.sqrt(linearComponent);

        return 0;
    }
}
