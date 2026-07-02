package com.example.raytracer.raytracer;

/**
 * An axis-aligned bounding box (AABB) — a rectangular box whose faces are aligned with the x, y,
 * and z axes (no rotation).
 *
 * <p>AABBs are used as a cheap pre-filter during ray intersection tests. Testing whether a ray hits
 * a box is much cheaper than testing whether it hits the actual geometry (e.g. a sphere or a whole
 * group of objects), so before doing the expensive test we first check whether the ray even comes
 * near the object at all. If it misses the box, it's guaranteed to miss everything inside it, and
 * we can skip the expensive check entirely.
 *
 * <p>This is the building block used by {@link BvhNode} to organize a scene into a tree of nested
 * boxes for fast ray intersection.
 */
public class Aabb {

  /** The interval this box spans along the x, y, and z axes, respectively. */
  public final Interval x, y, z;

  /** An empty box that contains nothing. Used as the starting point when growing a box. */
  public static final Aabb EMPTY = new Aabb(Interval.EMPTY, Interval.EMPTY, Interval.EMPTY);

  /**
   * Constructs a box directly from its three axis intervals.
   *
   * <p>Each interval is padded to a minimum thickness so that flat objects (e.g. a sphere with zero
   * extent along one axis) still produce a box with real volume, which the intersection math
   * requires.
   *
   * @param x the interval spanned along the x-axis
   * @param y the interval spanned along the y-axis
   * @param z the interval spanned along the z-axis
   */
  public Aabb(Interval x, Interval y, Interval z) {
    this.x = pad(x);
    this.y = pad(y);
    this.z = pad(z);
  }

  /**
   * Constructs the smallest box that contains both given points. The two points are treated as
   * opposite extrema, so it doesn't matter which one is "min" and which is "max" on any axis.
   *
   * @param a one corner point
   * @param b the opposite corner point
   */
  public Aabb(Vec3 a, Vec3 b) {
    this(
        new Interval(Math.min(a.x, b.x), Math.max(a.x, b.x)),
        new Interval(Math.min(a.y, b.y), Math.max(a.y, b.y)),
        new Interval(Math.min(a.z, b.z), Math.max(a.z, b.z)));
  }

  /**
   * Constructs the smallest box that fully encloses two other boxes. This is how bounding boxes are
   * "grown" as more objects are added to a group, or as a {@link BvhNode} combines its children's
   * boxes into a parent box.
   *
   * @param box0 the first box to enclose
   * @param box1 the second box to enclose
   */
  public Aabb(Aabb box0, Aabb box1) {
    this(
        new Interval(Math.min(box0.x.min, box1.x.min), Math.max(box0.x.max, box1.x.max)),
        new Interval(Math.min(box0.y.min, box1.y.min), Math.max(box0.y.max, box1.y.max)),
        new Interval(Math.min(box0.z.min, box1.z.min), Math.max(box0.z.max, box1.z.max)));
  }

  /**
   * Returns the interval for a given axis.
   *
   * @param n the axis index: 0 for x, 1 for y, 2 for z
   * @return the interval this box spans along that axis
   */
  public Interval axis(int n) {
    if (n == 1) return y;
    if (n == 2) return z;
    return x;
  }

  /**
   * Ensures an interval has at least a small minimum thickness, expanding it symmetrically if it's
   * too thin. Without this, a perfectly flat box (zero-width on some axis) can cause numerical
   * issues in the ray/box intersection test.
   *
   * @param interval the interval to pad
   * @return the original interval, or a slightly widened copy if it was too thin
   */
  private static Interval pad(Interval interval) {
    double delta = 0.0001;
    return interval.size() < delta
        ? new Interval(interval.min - delta / 2, interval.max + delta / 2)
        : interval;
  }

  /**
   * Tests whether a ray intersects this box within the given interval of the ray parameter {@code
   * t}.
   *
   * <p>This uses the "slab method": the box is treated as the intersection of three infinite slabs
   * (one per axis), and the ray's valid {@code t} range is narrowed axis-by-axis. If the range ever
   * becomes empty (min meets or exceeds max), the ray misses the box.
   *
   * @param r the ray to test
   * @param rayT the interval of valid {@code t} values to consider along the ray
   * @return {@code true} if the ray passes through the box within that interval
   */
  public boolean hit(Ray r, Interval rayT) {
    Vec3 origin = r.getOrigin();
    Vec3 dir = r.getDirection();

    for (int a = 0; a < 3; a++) {
      Interval ax = axis(a);
      double originComp = a == 0 ? origin.x : a == 1 ? origin.y : origin.z;
      double dirComp = a == 0 ? dir.x : a == 1 ? dir.y : dir.z;

      double adinv = 1.0 / dirComp;

      double t0 = (ax.min - originComp) * adinv;
      double t1 = (ax.max - originComp) * adinv;

      double tMin = rayT.min, tMax = rayT.max;

      if (t0 < t1) {
        if (t0 > tMin) tMin = t0;
        if (t1 < tMax) tMax = t1;
      } else {
        if (t1 > tMin) tMin = t1;
        if (t0 < tMax) tMax = t0;
      }

      if (tMax <= tMin) return false;

      rayT = new Interval(tMin, tMax);
    }
    return true;
  }

  /**
   * Determines which axis this box is longest along. Used by {@link BvhNode} to decide which axis
   * to sort and split objects on when building the tree — splitting along the longest axis tends to
   * produce more balanced, tighter-fitting sub-boxes.
   *
   * @return the axis index of the longest side: 0 for x, 1 for y, 2 for z
   */
  public int longestAxis() {
    double sx = x.size(), sy = y.size(), sz = z.size();
    if (sx > sy && sx > sz) return 0;
    if (sy > sz) return 1;
    return 2;
  }
}
