package com.example.raytracer.raytracer;

import static com.example.raytracer.util.MathUtil.randomDouble;

public class Vec3 {

  public double x, y, z;

  public Vec3() {
    this(0, 0, 0);
  }

  public Vec3(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vec3 add(Vec3 v) {
    return new Vec3(x + v.x, y + v.y, z + v.z);
  }

  public Vec3 subtract(Vec3 v) {
    return new Vec3(x - v.x, y - v.y, z - v.z);
  }

  public Vec3 negate() {
    return new Vec3(-x, -y, -z);
  }

  public Vec3 multiply(double t) {
    return new Vec3(x * t, y * t, z * t);
  }

  public Vec3 multiply(Vec3 v) {
    return new Vec3(x * v.x, y * v.y, z * v.z);
  }

  public Vec3 divide(double t) {
    return multiply(1.0 / t);
  }

  public double length() {
    return Math.sqrt(lengthSquared());
  }

  public double lengthSquared() {
    return x * x + y * y + z * z;
  }

  public static Vec3 random() {
    return new Vec3(randomDouble(), randomDouble(), randomDouble());
  }

  public static Vec3 random(double min, double max) {
    return new Vec3(randomDouble(min,max), randomDouble(min,max), randomDouble(min,max));
  }

  public Vec3 normalize() {
    return divide(length());
  }

  public static double dot(Vec3 u, Vec3 v) {
    return u.x * v.x + u.y * v.y + u.z * v.z;
  }

  public static Vec3 cross(Vec3 u, Vec3 v) {
    return new Vec3(u.y * v.z - u.z * v.y, u.z * v.x - u.x * v.z, u.x * v.y - u.y * v.x);
  }

  public static Vec3 unitVector(Vec3 v) {
    return v.divide(v.length());
  }

  public static Vec3 randomUnitVector() {
    while (true) {
      double minLengthSquared = 1e-160;
      Vec3 p = Vec3.random(-1,1);
      double lensq = p.lengthSquared();
      if (minLengthSquared < lensq && lensq <= 1)
        return p .divide(Math.sqrt(lensq));
    }
  }

  public static Vec3 randomOnHemisphere(Vec3 normal) {
    Vec3 onUnitSphere = randomUnitVector();
    if (dot(onUnitSphere, normal) > 0.0) // In the same hemisphere as the normal
      return onUnitSphere;
    else
      return onUnitSphere.negate();
  }

  public static Vec3 reflect(Vec3 v, Vec3 n) {
    return v.subtract(n.multiply(2.0 * Vec3.dot(v, n)));
  }

  public boolean nearZero() {
    // Return true if the vector is close to zero in all dimensions.
    double s = 1e-8;
    return (Math.abs(x) < s) && (Math.abs(y) < s) && (Math.abs(z) < s);
  }
}
