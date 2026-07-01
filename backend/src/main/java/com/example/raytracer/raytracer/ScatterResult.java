package com.example.raytracer.raytracer;

public record ScatterResult(Vec3 attenuation, Ray scattered) {}