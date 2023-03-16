import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public abstract class Moveable implements Active {

    private String id;
    private Point position;
    private double actionPeriod;
    private int imageIndex;
    private List<PImage> images;
    public PathingStrategy pathingStrategy;
    public List<Point> path;

    public Moveable(String id, Point position, List<PImage> images, double actionPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.actionPeriod = actionPeriod;
        this.pathingStrategy = new AstarPathingStrategy();
    }

    public abstract Point nextPosition(WorldModel world, Point destPos);

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point position) {this.position = position;}

    @Override
    public double getActionPeriod() {
        return this.actionPeriod;
    }

    public void nextImage() {
        this.imageIndex = this.imageIndex + 1;
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            return true;
        } else {
            // check if there is a path
            if (this.path == null || this.path.isEmpty()) {
                this.path = this.pathingStrategy.computePath(
                        this.getPosition(),
                        target.getPosition(),
                        p -> !world.isOccupied(p) && world.withinBounds(p),
                        (p1, p2) -> p1.adjacent(p2),
                        PathingStrategy.CARDINAL_NEIGHBORS
                );
            }

            // follow the path
            if (!this.path.isEmpty()) {
                Point nextPos = this.path.remove(0);
                if (!world.isOccupied(nextPos)) {
                    world.moveEntity(scheduler, this, nextPos);
                }
         //       return false;
            } //else {
                // no path found, stop moving
            return false;
            //}
        }
    }

    public String getId() {
        return this.id;
    }

    @Override
    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex % this.images.size());
    }

    public List<PImage> getImages() {
        return this.images;
    }

    public int getImageIndex() {return this.imageIndex;}
}
