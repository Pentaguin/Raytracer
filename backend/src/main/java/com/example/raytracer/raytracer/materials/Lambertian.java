package com.example.raytracer.raytracer.materials;

import com.example.raytracer.raytracer.HitRecord;
import com.example.raytracer.raytracer.Material;
import com.example.raytracer.raytracer.Ray;
import com.example.raytracer.raytracer.ScatterResult;
import com.example.raytracer.raytracer.Vec3;

public class Lambertian implements Material {
    private Vec3 albedo;

    public Lambertian(Vec3 albedo) {
        this.albedo = albedo;
    }

    @Override
    public ScatterResult scatter(Ray rayIn, HitRecord rec) {
        Vec3 scatterDirection = rec.normal.add(Vec3.randomUnitVector());

        // Catch degenerate scatter direction
        if (scatterDirection.nearZero())
            scatterDirection = rec.normal;

        Ray scattered = new Ray(rec.point, scatterDirection);

        return new ScatterResult(albedo, scattered);
    }
}