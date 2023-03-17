import processing.core.PImage;

import java.util.List;

public class Obstacle implements Animated {

    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_ANIMATION_PERIOD = 0;
    public static final int OBSTACLE_NUM_PROPERTIES = 1;

    private final double animationPeriod;
    private Point position;
    private final String id;

    private final List<PImage> images;
    private int imageIndex;

    public Obstacle(String id, Point position, double animationPeriod, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.animationPeriod = animationPeriod;
        this.images = images;
    }

    public static Obstacle createObstacle(String id, Point position, double animationPeriod, List<PImage> images) {
        return new Obstacle(id, position, animationPeriod, images);
    }

    @Override
    public double getAnimationPeriod() {
        return this.animationPeriod;
    }

    @Override
    public void nextImage() {
        this.imageIndex++;
    }

    @Override
    public Point getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, AnimationAction.createAnimationAction(this, 0), this.getAnimationPeriod());
    }

    @Override
    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex % this.images.size());
    }

    @Override
    public int getImageIndex() {
        return this.imageIndex;
    }

    @Override
    public String log(){
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.x, this.position.y, this.imageIndex);
    }
}
