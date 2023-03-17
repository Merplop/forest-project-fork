import processing.core.PImage;

import java.util.List;

public class House implements Entity {
    public static final String HOUSE_KEY = "house";
    public static final int HOUSE_NUM_PROPERTIES = 0;
    private final String id;
    private Point position;
    private final List<PImage> images;

    private int imageIndex;

    public House(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
    }

    public static House createHouse(String id, Point position, List<PImage> images) {
        return new House(id, position, images);
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {}

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
