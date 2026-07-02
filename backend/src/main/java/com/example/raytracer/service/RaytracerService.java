package com.example.raytracer.service;

import com.example.raytracer.api.dto.RenderRequest;
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

  public BufferedImage render(RenderRequest request) {

    HittableList world = SampleSceneBuilder.randomScene();
    BvhNode bvhWorld = new BvhNode(world);

    Camera camera =
        new Camera(
            request.getWidth(),
            request.getHeight(),
            request.getSamplesPerPixel(),
            request.getMaxDepth(),
            request.getVfov(),
            new Vec3(request.getLookFromX(), request.getLookFromY(), request.getLookFromZ()),
            new Vec3(request.getLookAtX(), request.getLookAtY(), request.getLookAtZ()),
            new Vec3(request.getVupX(), request.getVupY(), request.getVupZ()),
            request.getDefocusAngle(),
            request.getFocusDistance());

    return camera.render(bvhWorld);
  }
}
