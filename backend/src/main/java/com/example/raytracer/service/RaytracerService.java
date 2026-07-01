package com.example.raytracer.service;

import com.example.raytracer.raytracer.Camera;
import com.example.raytracer.raytracer.HittableList;
import com.example.raytracer.raytracer.materials.Lambertian;
import com.example.raytracer.raytracer.Material;
import com.example.raytracer.raytracer.materials.Metal;
import com.example.raytracer.raytracer.Sphere;
import com.example.raytracer.raytracer.Vec3;
import java.awt.image.BufferedImage;
import org.springframework.stereotype.Service;

// https://raytracing.github.io/books/RayTracingInOneWeekend.html#thevec3class

@Service
public class RaytracerService {

  public BufferedImage render(int width, int height, int pixelPerSamples) {
    // World
    HittableList world = new HittableList();

    Material materialGround = new Lambertian(new Vec3(0.8, 0.8, 0.0));
    Material materialCenter = new Lambertian(new Vec3(0.1, 0.2, 0.5));
    Material materialLeft   = new Metal(new Vec3(0.8, 0.8, 0.8), 0.3);
    Material materialRight = new Metal(new Vec3(0.8, 0.6, 0.2), 1);

    world.add(new Sphere(new Vec3( 0.0, -100.5, -1.0), 100.0, materialGround));
    world.add(new Sphere(new Vec3( 0.0,    0.0, -1.2), 0.5, materialCenter));
    world.add(new Sphere(new Vec3(-1.0,    0.0, -1.0), 0.5, materialLeft));
    world.add(new Sphere(new Vec3( 1.0,    0.0, -1.0), 0.5, materialRight));

    Camera camera = new Camera(width, height, pixelPerSamples, 50);
    return camera.render(world);
  }
}
