package com.example.raytracer.raytracer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Ray {

  private Vec3 origin;
  private Vec3 direction;

  public Vec3 at(double t) {
    return origin.add(direction.multiply(t));
  }
}
