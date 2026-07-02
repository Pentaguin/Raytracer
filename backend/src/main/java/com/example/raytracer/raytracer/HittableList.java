package com.example.raytracer.raytracer;

import java.util.ArrayList;
import java.util.List;

public class HittableList implements Hittable {
  private final List<Hittable> hittableList = new ArrayList<>();
  private Aabb bbox = Aabb.EMPTY;

  public HittableList() {}

  public HittableList(Hittable object) {
    this.add(object);
  }

  public void clear() {
    hittableList.clear();
  }

  public void add(Hittable object) {
    hittableList.add(object);
    bbox = new Aabb(bbox, object.boundingBox());
  }

  public List<Hittable> getObjects() {
    return hittableList;
  }

  @Override
  public boolean hit(Ray ray, Interval interval, HitRecord rec) {
    HitRecord tempRec = new HitRecord();

    boolean hitAnything = false;
    double closestSoFar = interval.max;
    for (Hittable object : hittableList) {

      if (object.hit(ray, new Interval(interval.min, closestSoFar), tempRec)) {
        hitAnything = true;
        closestSoFar = tempRec.t;

        rec.t = tempRec.t;
        rec.point = tempRec.point;
        rec.normal = tempRec.normal;
        rec.material = tempRec.material;
        rec.frontFace = tempRec.frontFace;
      }
    }

    return hitAnything;
  }

  @Override
  public Aabb boundingBox() {
    return bbox;
  }
}
