import processing.core.PImage;

import java.util.List;

public class Skull implements Entity{

    public static final String SKULL_KEY = "skull";
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;

    public Skull(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
    }


    @Override
    public void nextImage() {
        imageIndex++;
    }

    @Override
    public Point getPosition() {
        return position;
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
    }

    @Override
    public PImage getCurrentImage() {
        return this.images.get(imageIndex % images.size());
    }

    @Override
    public int getImageIndex() {
        return imageIndex;
    }

    public static Skull createSkull(String id, Point position, List<PImage> images) {
        return new Skull(id, position, images);
    }
}
