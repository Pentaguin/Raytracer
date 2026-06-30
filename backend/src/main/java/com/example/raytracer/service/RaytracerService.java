package com.example.raytracer.service;

import com.example.raytracer.raytracer.Camera;
import com.example.raytracer.raytracer.HittableList;
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
    world.add(new Sphere(new Vec3(0, 0, -1), 0.5));
    world.add(new Sphere(new Vec3(0, -100.5, -1), 100));

    Camera camera = new Camera(width, height, pixelPerSamples);
    return camera.render(world);
  }
}
