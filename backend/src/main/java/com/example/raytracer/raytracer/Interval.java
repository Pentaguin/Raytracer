package com.example.raytracer.raytracer;

public class Interval {
  public final double min;
  public final double max;

  public static final Interval EMPTY =
      new Interval(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);

  public static final Interval UNIVERSE =
      new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

  public Interval() {
    this.min = Double.POSITIVE_INFINITY;
    this.max = Double.NEGATIVE_INFINITY;
  }

  public Interval(double min, double max) {
    this.min = min;
    this.max = max;
  }

  public double size() {
    return max - min;
  }

  public boolean contains(double x) {
    return min <= x && x <= max;
  }

  public boolean surrounds(double x) {
    return min < x && x < max;
  }

  public double clamp(double x) {
    if (x < min) return min;
    return Math.min(x, max);
  }
}
