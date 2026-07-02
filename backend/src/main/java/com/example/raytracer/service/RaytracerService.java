package com.example.raytracer.service;

import com.example.raytracer.raytracer.BvhNode;
import com.example.raytracer.raytracer.Camera;
import com.example.raytracer.raytracer.HittableList;
import com.example.raytracer.raytracer.SampleSceneBuilder;
import com.example.raytracer.raytracer.Vec3;
import java.awt.image.BufferedImage;
import org.springframework.stereotype.Service;

// https://raytracing.github.io/books/RayTracingInOneWeekend.html#thevec3class

@Service
public class RaytracerService {

  public BufferedImage render(int width, int height, int samplesPerPixel, double vfov) {

    HittableList world = SampleSceneBuilder.randomScene();
    BvhNode bvhWorld = new BvhNode(world);

    Camera camera =
        new Camera(
            width,
            height,
            samplesPerPixel,
            20,
            vfov,
            new Vec3(13, 2, 3), // lookfrom
            new Vec3(0, 0, 0), // lookat
            new Vec3(0, 1, 0), // vup
            0.6, // defocus angle
            10.0 // focus dist
            );

    return camera.render(bvhWorld);
  }
}
