package com.example.raytracer.raytracer;

import static com.example.raytracer.raytracer.Vec3.cross;
import static com.example.raytracer.raytracer.Vec3.unitVector;
import static com.example.raytracer.util.MathUtil.degreesToRadians;
import static com.example.raytracer.util.MathUtil.randomDouble;

import java.awt.image.BufferedImage;

public class Camera {

  private final int width;
  private final int height;
  private final int samplesPerPixel; // Count of random samples for each pixel
  private Vec3 originPixel; // Location of pixel 0, 0
  private Vec3 pixelDeltaU; // Offset to pixel to the right
  private Vec3 pixelDeltaV; // Offset to pixel below
  private Vec3 center; // Camera center
  private int maxDepth; // Maximum number of ray bounces into scene
  private double vfov; // Vertical view angle (field of view)
  private Vec3 u, v, w; // Camera frame basis vectors
  private Vec3 lookfrom; // Point camera is looking from
  private Vec3 lookat; // Point camera is looking at
  private Vec3 vup; // Camera-relative "up" direction

  public Camera(
      int width,
      int height,
      int samplesPerPixel,
      int maxDepth,
      double vfov,
      Vec3 lookfrom,
      Vec3 lookat,
      Vec3 vup) {
    this.width = width;
    this.height = height;
    this.samplesPerPixel = samplesPerPixel;
    this.maxDepth = maxDepth;
    this.vfov = vfov;
    this.lookfrom = lookfrom;
    this.lookat = lookat;
    this.vup = vup;
  }

  public BufferedImage render(Hittable world) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    center = lookfrom;

    // Determine viewport dimensions.
    double focalLength = lookfrom.subtract(lookat).length();
    double theta = degreesToRadians(vfov);
    double h = Math.tan(theta / 2);
    double viewportHeight = 2 * h * focalLength;
    double viewportWidth = viewportHeight * width / height;

    // Calculate the u,v,w unit basis vectors for the camera coordinate frame.
    w = unitVector(lookfrom.subtract(lookat));
    u = unitVector(cross(vup, w));
    v = cross(w, u);

    // Calculate the vectors across the horizontal and down the vertical viewport edges.
    Vec3 viewportU = u.multiply(viewportWidth); // Vector across viewport horizontal edge
    Vec3 viewportV = v.negate().multiply(viewportHeight); // Vector down viewport vertical edge

    // Calculate the horizontal and vertical delta vectors from pixel to pixel.
    pixelDeltaU = viewportU.divide(width);
    pixelDeltaV = viewportV.divide(height);

    // Calculate the location of the upper left pixel.
    Vec3 viewportUpperLeft =
        center
            .subtract(w.multiply(focalLength))
            .subtract(viewportU.divide(2))
            .subtract(viewportV.divide(2));

    originPixel = viewportUpperLeft.add(pixelDeltaU.add(pixelDeltaV).multiply(0.5));

    // Render loop
    for (int j = 0; j < height; j++) {
      for (int i = 0; i < width; i++) {

        Vec3 pixelColor = new Vec3(0, 0, 0);

        for (int s = 0; s < samplesPerPixel; s++) {

          Ray r = getRay(i, j);
          pixelColor = pixelColor.add(rayColor(r, maxDepth, world));
        }
        double pixelScale = 1.0 / samplesPerPixel;
        pixelColor = pixelColor.multiply(pixelScale);

        image.setRGB(i, j, new Color().toRGB(pixelColor));
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

  private Vec3 rayColor(Ray r, int depth, Hittable world) {
    // If we've exceeded the ray bounce limit, no more light is gathered.
    if (depth <= 0) return new Vec3(0, 0, 0);

    HitRecord rec = new HitRecord();

    if (world.hit(r, new Interval(0.001, Double.POSITIVE_INFINITY), rec)) {
      ScatterResult s = rec.material.scatter(r, rec);

      if (s != null) {
        return rayColor(s.scattered(), depth - 1, world).multiply(s.attenuation());
      }

      return new Vec3(0, 0, 0);
    }

    Vec3 unitDirection = r.getDirection().normalize();
    double t = 0.5 * (unitDirection.y + 1.0);

    return new Vec3(1.0, 1.0, 1.0).multiply(1.0 - t).add(new Vec3(0.5, 0.7, 1.0).multiply(t));
  }
}
