package com.example.raytracer.raytracer.materials;

import static com.example.raytracer.raytracer.Vec3.dot;
import static com.example.raytracer.raytracer.Vec3.reflect;
import static com.example.raytracer.raytracer.Vec3.refract;
import static com.example.raytracer.raytracer.Vec3.unitVector;

import com.example.raytracer.raytracer.HitRecord;
import com.example.raytracer.raytracer.Material;
import com.example.raytracer.raytracer.Ray;
import com.example.raytracer.raytracer.ScatterResult;
import com.example.raytracer.raytracer.Vec3;
import com.example.raytracer.util.MathUtil;

public class Dielectric implements Material {
  // Refractive index in vacuum or air, or the ratio of the material's refractive index over
  // the refractive index of the enclosing media
  private final double refractionIndex;

  public Dielectric(double refractionIndex) {
    this.refractionIndex = refractionIndex;
  }

  @Override
  public ScatterResult scatter(Ray rayIn, HitRecord rec) {
    Vec3 attenuation = new Vec3(1.0, 1.0, 1.0);
    double ri = rec.frontFace ? (1.0 / refractionIndex) : refractionIndex;

    Vec3 unitDirection = unitVector(rayIn.getDirection());

    double cosTheta = Math.min(dot(unitDirection.negate(), rec.normal), 1);
    double sinTheta = Math.sqrt(1.0 - cosTheta * cosTheta);

    boolean cannotRefract = ri * sinTheta > 1.0;
    Vec3 direction;

    if (cannotRefract || reflectance(cosTheta, ri) > MathUtil.randomDouble()) {
      direction = reflect(unitDirection, rec.normal);
    } else {
      direction = refract(unitDirection, rec.normal, ri);
    }

    Ray scattered = new Ray(rec.point, direction);
    return new ScatterResult(attenuation, scattered);
  }

  private static double reflectance(double cosine, double refractionIndex) {
    // Use Schlick's approximation for reflectance.
    var r0 = (1 - refractionIndex) / (1 + refractionIndex);
    r0 = r0 * r0;
    return r0 + (1 - r0) * Math.pow((1 - cosine), 5);
  }
}
