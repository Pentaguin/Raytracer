package com.example.raytracer.raytracer;

import java.util.Comparator;
import java.util.List;

/**
 * A node in a Bounding Volume Hierarchy (BVH) — a binary tree that organizes a scene's objects
 * spatially so that ray intersection tests can skip large portions of the scene that a ray couldn't
 * possibly hit.
 *
 * <p>Without a BVH, testing a ray against a scene means checking it against every single object
 * (see {@link HittableList#hit}), which is {@code O(n)} in the number of objects. A BVH instead
 * arranges objects into a tree of nested bounding boxes: the root box contains the whole scene, and
 * each level splits the objects roughly in half. A ray that misses a node's box is guaranteed to
 * miss everything inside it, so the whole subtree is skipped in one cheap check. This turns
 * intersection testing into roughly {@code O(log n)}.
 *
 * <p>A {@code BvhNode} is itself a {@link Hittable}, so once built it can be dropped in anywhere a
 * {@link HittableList} was used — the rest of the renderer doesn't need to know the difference.
 */
public class BvhNode implements Hittable {

  /** The left child, which may be another {@code BvhNode} or a leaf object (e.g. a sphere). */
  private final Hittable left;

  /** The right child, which may be another {@code BvhNode} or a leaf object (e.g. a sphere). */
  private final Hittable right;

  /** The bounding box enclosing both children, used to quickly reject rays that miss entirely. */
  private final Aabb bbox;

  /**
   * Builds a BVH from every object currently in the given list.
   *
   * @param list the flat list of objects to organize into a tree
   */
  public BvhNode(HittableList list) {
    this(list.getObjects(), 0, list.getObjects().size());
  }

  /**
   * Recursively builds a BVH subtree from the range {@code [start, end)} of the given list.
   *
   * <p>The objects in this range are sorted along whichever axis their combined bounding box is
   * longest on, then split into two halves at the midpoint. Each half becomes a child subtree,
   * built by recursing on this same constructor. The recursion bottoms out at one or two objects,
   * which become direct leaf children instead of further subtrees.
   *
   * <p>Note: this sorts objects in place within the shared list (via {@code subList}), which is
   * safe here since the list is only used to build the tree and isn't touched afterward.
   *
   * @param objects the full list of objects being partitioned
   * @param start the inclusive start index of the range this node covers
   * @param end the exclusive end index of the range this node covers
   */
  private BvhNode(List<Hittable> objects, int start, int end) {
    Aabb box = Aabb.EMPTY;
    for (int i = start; i < end; i++) {
      box = new Aabb(box, objects.get(i).boundingBox());
    }
    int axis = box.longestAxis();

    int span = end - start;

    if (span == 1) {
      // Only one object in range: it becomes both children (a degenerate leaf).
      left = right = objects.get(start);
    } else if (span == 2) {
      // Exactly two objects: each becomes a leaf child directly, no further splitting needed.
      left = objects.get(start);
      right = objects.get(start + 1);
    } else {
      // More than two objects: sort along the longest axis and split into two subtrees.
      objects
          .subList(start, end)
          .sort(Comparator.comparingDouble(o -> o.boundingBox().axis(axis).min));
      int mid = start + span / 2;
      left = new BvhNode(objects, start, mid);
      right = new BvhNode(objects, mid, end);
    }

    bbox = new Aabb(left.boundingBox(), right.boundingBox());
  }

  /**
   * Tests whether a ray hits anything within this subtree.
   *
   * <p>First checks the ray against this node's bounding box; if it misses, the whole subtree is
   * skipped immediately. Otherwise, both children are tested. The right child's search interval is
   * narrowed to end at the left child's hit distance (if any), so that once a closer hit is found,
   * farther objects behind it are automatically ignored — the same "closest hit wins" logic used in
   * {@link HittableList#hit}.
   *
   * @param ray the ray to test
   * @param interval the valid range of {@code t} values to consider along the ray
   * @param rec the hit record to populate if an intersection is found
   * @return {@code true} if the ray hits something within this subtree
   */
  @Override
  public boolean hit(Ray ray, Interval interval, HitRecord rec) {
    if (!bbox.hit(ray, interval)) return false;

    boolean hitLeft = left.hit(ray, interval, rec);
    Interval rightInterval = new Interval(interval.min, hitLeft ? rec.t : interval.max);
    boolean hitRight = right.hit(ray, rightInterval, rec);

    return hitLeft || hitRight;
  }

  /**
   * Returns the bounding box enclosing everything in this subtree, so that a parent node can
   * include this subtree in its own bounding box.
   *
   * @return this node's bounding box
   */
  @Override
  public Aabb boundingBox() {
    return bbox;
  }
}
