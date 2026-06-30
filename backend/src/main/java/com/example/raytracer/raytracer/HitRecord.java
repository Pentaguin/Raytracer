package com.example.raytracer.raytracer;

import static com.example.raytracer.raytracer.Vec3.dot;

public class HitRecord {
  public Vec3 point;
  public Vec3 normal;
  public double t;
  boolean frontFace;

  void setFace(Ray r, Vec3 outwardNormal) {
    frontFace = dot(r.getDirection(), outwardNormal) < 0;
    normal = frontFace ? outwardNormal : outwardNormal.multiply(-1);
  }
}
