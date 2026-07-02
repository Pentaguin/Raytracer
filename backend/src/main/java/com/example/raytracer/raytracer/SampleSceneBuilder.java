package com.example.raytracer.raytracer;

import static com.example.raytracer.util.MathUtil.randomDouble;

import com.example.raytracer.raytracer.materials.Dielectric;
import com.example.raytracer.raytracer.materials.Lambertian;
import com.example.raytracer.raytracer.materials.Metal;

public class SampleSceneBuilder {

  public static HittableList randomScene() {
    HittableList world = new HittableList();

    // Ground
    Material groundMaterial = new Lambertian(new Vec3(0.5, 0.5, 0.5));
    world.add(new Sphere(new Vec3(0, -1000, 0), 1000, groundMaterial));

    // Small spheres
    for (int a = -11; a < 11; a++) {
      for (int b = -11; b < 11; b++) {

        double chooseMat = randomDouble();

        Vec3 center = new Vec3(a + 0.9 * randomDouble(), 0.2, b + 0.9 * randomDouble());

        if (center.subtract(new Vec3(4, 0.2, 0)).length() > 0.9) {

          Material sphereMaterial;

          if (chooseMat < 0.8) {
            // diffuse
            Vec3 albedo = Vec3.random().multiply(Vec3.random());
            sphereMaterial = new Lambertian(albedo);

            world.add(new Sphere(center, 0.2, sphereMaterial));

          } else if (chooseMat < 0.95) {
            // metal
            Vec3 albedo = Vec3.random(0.5, 1.0);
            double fuzz = randomDouble(0, 0.5);

            sphereMaterial = new Metal(albedo, fuzz);

            world.add(new Sphere(center, 0.2, sphereMaterial));

          } else {
            // glass
            sphereMaterial = new Dielectric(1.5);

            world.add(new Sphere(center, 0.2, sphereMaterial));
          }
        }
      }
    }

    // Big center spheres
    world.add(new Sphere(new Vec3(0, 1, 0), 1.0, new Dielectric(1.5)));
    world.add(new Sphere(new Vec3(-4, 1, 0), 1.0, new Lambertian(new Vec3(0.4, 0.2, 0.1))));
    world.add(new Sphere(new Vec3(4, 1, 0), 1.0, new Metal(new Vec3(0.7, 0.6, 0.5), 0.0)));

    return world;
  }
}
