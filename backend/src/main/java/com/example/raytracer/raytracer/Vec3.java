package com.example.raytracer.raytracer;

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

  public Vec3 divide(double t) {
    return multiply(1.0 / t);
  }

  public double length() {
    return Math.sqrt(lengthSquared());
  }

  public double lengthSquared() {
    return x * x + y * y + z * z;
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
}
