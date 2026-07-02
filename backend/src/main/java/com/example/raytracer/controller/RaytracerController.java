package com.example.raytracer.controller;

import com.example.raytracer.api.RenderApi;
import com.example.raytracer.api.dto.RenderRequest;
import com.example.raytracer.service.RaytracerService;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RaytracerController implements RenderApi {

  private final RaytracerService raytracerService;

  @Override
  public ResponseEntity<byte[]> renderImage(RenderRequest request) {

    BufferedImage image = raytracerService.render(request);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try {
      ImageIO.write(image, "png", baos);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
        .body(baos.toByteArray());
  }
}
