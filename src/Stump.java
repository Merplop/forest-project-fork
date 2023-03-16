import processing.core.PImage;

import java.util.List;

public class Stump implements Entity {
    public static final String STUMP_KEY = "stump";
    public static final int STUMP_NUM_PROPERTIES = 0;
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Stump(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
    }

    public static Stump createStump(String id, Point position, List<PImage> images) {
        return new Stump(id, position, images);
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

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
    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex % this.images.size());
    }

    @Override
    public int getImageIndex() {
        return this.imageIndex;
    }

}
