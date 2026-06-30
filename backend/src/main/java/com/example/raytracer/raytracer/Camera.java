package com.example.raytracer.raytracer;

import static com.example.raytracer.util.MathUtil.randomDouble;

import java.awt.image.BufferedImage;

public class Camera {

  private final int width;
  private final int height;
  private final int samplesPerPixel;
  private Vec3 originPixel;
  private Vec3 pixelDeltaU;
  private Vec3 pixelDeltaV;
  private Vec3 center;
  private int maxDepth;

  public Camera(int width, int height, int samplesPerPixel, int maxDepth) {
    this.width = width;
    this.height = height;
    this.samplesPerPixel = samplesPerPixel;
    this.maxDepth = maxDepth;
  }

  public BufferedImage render(Hittable world) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    // Camera setup
    center = new Vec3(0, 0, 0);
    double focalLength = 1.0;
    double viewportHeight = 2.0;
    double viewportWidth = viewportHeight * width / height;

    Vec3 viewportU = new Vec3(viewportWidth, 0, 0);
    Vec3 viewportV = new Vec3(0, -viewportHeight, 0);

    pixelDeltaU = viewportU.divide(width);
    pixelDeltaV = viewportV.divide(height);

    Vec3 viewportUpperLeft =
        center
            .subtract(new Vec3(0, 0, focalLength))
            .subtract(viewportU.divide(2))
            .subtract(viewportV.divide(2));

    originPixel = viewportUpperLeft.add(pixelDeltaU.add(pixelDeltaV).multiply(0.5));

    // Render loop
    for (int j = 0; j < height; j++) {
      for (int i = 0; i < width; i++) {

        Vec3 pixelColor = new Vec3(0, 0, 0);

        for (int s = 0; s < samplesPerPixel; s++) {

          Ray r = getRay(i, j);
          pixelColor = pixelColor.add(rayColor(r, maxDepth ,world));
        }
        double pixelScale = 1.0 / samplesPerPixel;
        pixelColor = pixelColor.multiply(pixelScale);

        image.setRGB(i, j, toRGB(pixelColor));
      }
    }

    return image;
  }

  // Construct a camera ray originating from the origin and directed at randomly sampled
  // point around the pixel location i, j.
  private Ray getRay(int i, int j) {
    Vec3 offset = sampleSquare();

    Vec3 pixelSample =
        originPixel.add(pixelDeltaU.multiply(i + offset.x)).add(pixelDeltaV.multiply(j + offset.y));

    Vec3 rayOrigin = center;
    Vec3 rayDirection = pixelSample.subtract(rayOrigin);

    return new Ray(rayOrigin, rayDirection);
  }

  private Vec3 sampleSquare() {
    // Returns the vector to a random point in the [-.5,-.5]-[+.5,+.5] unit square.
    return new Vec3(randomDouble() - 0.5, randomDouble() - 0.5, 0);
  }

  // Translate the [0,1] component values to the byte range [0,255].
  private int toRGB(Vec3 pixelColor) {
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

  private Vec3 rayColor(Ray r, int depth, Hittable world) {
    // If we've exceeded the ray bounce limit, no more light is gathered.
    if (depth <= 0)
      return new Vec3(0,0,0);

    HitRecord rec = new HitRecord();

    if (world.hit(r, new Interval(0.001, Double.POSITIVE_INFINITY), rec)) {
      Vec3 direction = rec.normal.add(Vec3.randomUnitVector());
      return rayColor(new Ray(rec.point, direction), depth - 1,world).multiply(0.1);
    }

    Vec3 unitDirection = r.getDirection().normalize();
    double t = 0.5 * (unitDirection.y + 1.0);

    return new Vec3(1.0, 1.0, 1.0).multiply(1.0 - t).add(new Vec3(0.5, 0.7, 1.0).multiply(t));
  }
}
