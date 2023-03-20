import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public abstract class Plant implements Transformable, Animated {

    private final String id;
    private Point position;
    private List<PImage> images;
    private int health;
    private final int healthLimit;
    private int imageIndex;

    private final double actionPeriod;
    private final double animationPeriod;

    public Plant(String id, Point position, List<PImage> images, int health, double actionPeriod, double animationPeriod, int healthLimit) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.health = health;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.healthLimit = healthLimit;
    }

    public int getHealth() {return this.health;}
    public int getHealthLimit() {return this.healthLimit;}

    public void setHealth(int h) {this.health = h;}

    @Override
    public abstract boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore);

    public Point getPosition() {return this.position;}

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, ActivityAction.createActivityAction(this, world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this, AnimationAction.createAnimationAction(this, 0), this.getAnimationPeriod());
    }

    @Override
    public double getAnimationPeriod() {
        return this.animationPeriod;
    }

    @Override
    public double getActionPeriod() {
        return this.actionPeriod;
    }

    @Override
    public void nextImage() {
        this.imageIndex++;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex % this.images.size());
    }

    public void setImageSet(List<PImage> images) {
        this.images = images;
    }
    @Override
    public String log(){
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.x, this.position.y, this.imageIndex);
    }

    public List<PImage> getImages() {
        return this.images;
    }

    public int getImageIndex() {
        return this.imageIndex;
    }
}
