package com.example.raytracer.raytracer;

public class Sphere implements Hittable {

  private final Vec3 center;
  private final double radius;
  private final Material material;
  private final Aabb bbox;

  public Sphere(Vec3 center, double radius, Material material) {
    this.center = center;
    this.radius = Math.max(0, radius);
    this.material = material;
    Vec3 rVec = new Vec3(this.radius, this.radius, this.radius);
    this.bbox = new Aabb(center.subtract(rVec), center.add(rVec));
  }

  @Override
  public boolean hit(Ray r, Interval interval, HitRecord rec) {

    Vec3 oc = center.subtract(r.getOrigin());

    double a = r.getDirection().lengthSquared();
    double h = Vec3.dot(r.getDirection(), oc);
    double c = oc.lengthSquared() - radius * radius;

    double discriminant = h * h - a * c;

    if (discriminant < 0) {
      return false;
    }

    double sqrtd = Math.sqrt(discriminant);

    double root = (h - sqrtd) / a;

    if (!interval.surrounds(root)) {
      root = (h + sqrtd) / a;

      if (!interval.surrounds(root)) {
        return false;
      }
    }

    rec.t = root;
    rec.point = r.at(root);

    Vec3 outwardNormal = rec.point.subtract(center).divide(radius);
    rec.setFace(r, outwardNormal);
    rec.material = this.material;

    return true;
  }

  @Override
  public Aabb boundingBox() {
    return bbox;
  }
}
