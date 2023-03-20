import java.util.List;

import processing.core.PImage;

public abstract class Dude extends Movable implements Transformable, Animated {

    public static final String DUDE_KEY = "dude";
    public static final int DUDE_ACTION_PERIOD = 0;
    public static final int DUDE_ANIMATION_PERIOD = 1;
    public static final int DUDE_LIMIT = 2;
    public static final int DUDE_NUM_PROPERTIES = 3;

    private int resourceLimit;
    private int resourceCount;
    private final double animationPeriod;

    public Dude(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
        this.resourceLimit = resourceLimit;
    }


    public Point nextPosition(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.getPosition().x);
        Point newPos = new Point(this.getPosition().x + horiz, this.getPosition().y);

        if (horiz == 0 || world.isOccupied(newPos) && !(world.getOccupancyCell(newPos) instanceof Stump)) {
            int vert = Integer.signum(destPos.y - this.getPosition().y);
            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);

            if (vert == 0 || world.isOccupied(newPos) && !(world.getOccupancyCell(newPos) instanceof Stump)) {
                newPos = this.getPosition();
            }
        }
        return newPos;
    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
//        if (this.getPosition().adjacent(target.getPosition())) {
//            return true;
//        } else {
//            // check if there is a path
//            if (this.path == null || this.path.isEmpty()) {
//                this.path = this.pathingStrategy.computePath(
//                        this.getPosition(),
//                        target.getPosition(),
//                        p -> !world.isOccupied(p),
//                        (p1, p2) -> p1.adjacent(p2),
//                        PathingStrategy.CARDINAL_NEIGHBORS
//                );
//            }
//
//            // follow the path
//            if (!this.path.isEmpty()) {
//                Point nextPos = this.path.remove(0);
//                if (!world.isOccupied(nextPos)) {
//                    world.moveEntity(scheduler, this, nextPos);
//                }
//                return false;
//            } else {
//                // no path found, stop moving
//                return true;
//            }
//        }
        return super.moveTo(world, target, scheduler);
    }

    public int getResourceLimit() {
        return this.resourceLimit;
    }

    public int getResourceCount() {
        return this.resourceCount;
    }

    public void setResourceCount(int c) {
        this.resourceCount = c;
    }

    public void setResourceLimit(int l) {
        this.resourceLimit = l;
    }

    public double getAnimationPeriod() {
        return this.animationPeriod;
    }
}
