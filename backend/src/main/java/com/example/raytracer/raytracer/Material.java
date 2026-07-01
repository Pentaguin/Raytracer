package com.example.raytracer.raytracer;

public interface Material {

    ScatterResult scatter(Ray rayIn, HitRecord rec);

}
