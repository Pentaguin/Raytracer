package com.example.raytracer.raytracer.materials;

import com.example.raytracer.raytracer.HitRecord;
import com.example.raytracer.raytracer.Material;
import com.example.raytracer.raytracer.Ray;
import com.example.raytracer.raytracer.ScatterResult;
import com.example.raytracer.raytracer.Vec3;

public class Metal implements Material {

    private Vec3 albedo;
    private double fuzz;

    public Metal(Vec3 albedo, double fuzz) {
        this.albedo = albedo;
        this.fuzz = fuzz < 1 ? fuzz : 1;
    }

    @Override
    public ScatterResult scatter(Ray rayIn, HitRecord rec) {
        Vec3 reflected = Vec3.reflect(rayIn.getDirection(), rec.normal);
        reflected = Vec3.unitVector(reflected).add((Vec3.randomUnitVector().multiply(fuzz)));
        Ray scattered = new Ray(rec.point, reflected);

        return Vec3.dot(scattered.getDirection(), rec.normal) > 0
                ? new ScatterResult(albedo, scattered)
                : null;
    }
}
