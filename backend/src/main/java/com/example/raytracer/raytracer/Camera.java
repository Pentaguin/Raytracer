package com.example.raytracer.raytracer;

import static com.example.raytracer.raytracer.Vec3.cross;
import static com.example.raytracer.raytracer.Vec3.randomInUnitDisk;
import static com.example.raytracer.raytracer.Vec3.unitVector;
import static com.example.raytracer.util.MathUtil.degreesToRadians;
import static com.example.raytracer.util.MathUtil.randomDouble;

import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

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

  private double defocusAngle = 0; // Variation angle of rays through each pixel
  private double focusDist = 10; // Distance from camera lookfrom point to plane of perfect focus

  private Vec3 defocusDiskU; // Defocus disk horizontal radius
  private Vec3 defocusDiskV; // Defocus disk vertical radius

  public Camera(
      int width,
      int height,
      int samplesPerPixel,
      int maxDepth,
      double vfov,
      Vec3 lookfrom,
      Vec3 lookat,
      Vec3 vup,
      double defocusAngle,
      double focusDist) {
    this.width = width;
    this.height = height;
    this.samplesPerPixel = samplesPerPixel;
    this.maxDepth = maxDepth;
    this.vfov = vfov;
    this.lookfrom = lookfrom;
    this.lookat = lookat;
    this.vup = vup;
    this.defocusAngle = defocusAngle;
    this.focusDist = focusDist;
    setupCamera();
  }

  private void setupCamera() {
    center = lookfrom;

    // Determine viewport dimensions.
    double theta = degreesToRadians(vfov);
    double h = Math.tan(theta / 2);
    double viewportHeight = 2 * h * focusDist;
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
            .subtract(w.multiply(focusDist))
            .subtract(viewportU.divide(2))
            .subtract(viewportV.divide(2));

    originPixel = viewportUpperLeft.add(pixelDeltaU.add(pixelDeltaV).multiply(0.5));

    // Calculate the camera defocus disk basis vectors.
    double defocusRadius = focusDist * Math.tan(degreesToRadians(defocusAngle / 2));
    defocusDiskU = u.multiply(defocusRadius);
    defocusDiskV = v.multiply(defocusRadius);
  }

  public BufferedImage render(Hittable world) {
    int[] pixels = new int[width * height];

    IntStream.range(0, height)
        .parallel()
        .forEach(
            j -> {
              for (int i = 0; i < width; i++) {
                Vec3 pixelColor = new Vec3(0, 0, 0);
                for (int s = 0; s < samplesPerPixel; s++) {
                  Ray r = getRay(i, j);
                  pixelColor = pixelColor.add(rayColor(r, maxDepth, world));
                }
                pixelColor = pixelColor.multiply(1.0 / samplesPerPixel);
                pixels[j * width + i] = new Color().toRGB(pixelColor);
              }
            });

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    image.setRGB(0, 0, width, height, pixels, 0, width);
    return image;
  }

  // Construct a camera ray originating from the defocus disk and directed at a randomly
  // sampled point around the pixel location i, j.
  private Ray getRay(int i, int j) {
    Vec3 offset = sampleSquare();

    Vec3 pixelSample =
        originPixel.add(pixelDeltaU.multiply(i + offset.x)).add(pixelDeltaV.multiply(j + offset.y));

    Vec3 rayOrigin = (defocusAngle <= 0) ? center : defocusDiskSample();
    Vec3 rayDirection = pixelSample.subtract(rayOrigin);

    return new Ray(rayOrigin, rayDirection);
  }

  private Vec3 sampleSquare() {
    // Returns the vector to a random point in the [-.5,-.5]-[+.5,+.5] unit square.
    return new Vec3(randomDouble() - 0.5, randomDouble() - 0.5, 0);
  }

  private Vec3 defocusDiskSample() {
    // Returns a random point in the camera defocus disk.
    Vec3 p = randomInUnitDisk();
    return center.add(defocusDiskU.multiply(p.x)).add(defocusDiskV.multiply(p.y));
  }

  private Vec3 rayColor(Ray r, int depth, Hittable world) {
    if (depth <= 0) return new Vec3(0, 0, 0);

    HitRecord rec = new HitRecord();
    if (world.hit(r, new Interval(0.001, Double.POSITIVE_INFINITY), rec)) {
      ScatterResult s = rec.material.scatter(r, rec);
      if (s == null) return new Vec3(0, 0, 0);

      // Russian roulette: after a few bounces, randomly kill low-contribution paths
      if (depth < maxDepth - 3) {
        double p = Math.max(s.attenuation().x, Math.max(s.attenuation().y, s.attenuation().z));
        if (randomDouble() > p) return new Vec3(0, 0, 0);
        return rayColor(s.scattered(), depth - 1, world).multiply(s.attenuation()).divide(p);
      }

      return rayColor(s.scattered(), depth - 1, world).multiply(s.attenuation());
    }

    Vec3 unitDirection = r.getDirection().normalize();
    double t = 0.5 * (unitDirection.y + 1.0);
    return new Vec3(1.0, 1.0, 1.0).multiply(1.0 - t).add(new Vec3(0.5, 0.7, 1.0).multiply(t));
  }
}
