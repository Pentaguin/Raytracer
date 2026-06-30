package com.example.raytracer.raytracer;

public interface Hittable {
  boolean hit(Ray ray, Interval interval, HitRecord rec);
}
