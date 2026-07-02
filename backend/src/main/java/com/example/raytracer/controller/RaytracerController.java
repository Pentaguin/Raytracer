package com.example.raytracer.controller;

import com.example.raytracer.api.RenderApi;
import com.example.raytracer.service.RaytracerService;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RaytracerController implements RenderApi {

  private final RaytracerService raytracerService;

  @Override
  public ResponseEntity<byte[]> renderImage(
      Integer width, Integer height, Integer samplesPerPixel, Double vfov) {

    BufferedImage img = raytracerService.render(width, height, samplesPerPixel, vfov);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      ImageIO.write(img, "png", baos);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return ResponseEntity.ok(baos.toByteArray());
  }
}
