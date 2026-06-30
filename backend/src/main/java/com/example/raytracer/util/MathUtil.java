package com.example.raytracer.util;

import java.util.concurrent.ThreadLocalRandom;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class MathUtil {

  public static double degreesToRadians(double degrees) {
    return degrees * Math.PI / 180.0;
  }

  public static double randomDouble() {
    return ThreadLocalRandom.current().nextDouble();
  }

  public static double randomDouble(double min, double max) {
    return min + (max - min) * randomDouble();
  }
}
