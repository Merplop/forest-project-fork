import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;
public interface Entity {

    public static final int PROPERTY_KEY = 0;
    public static final int PROPERTY_ID = 1;
    public static final int PROPERTY_COL = 2;
    public static final int PROPERTY_ROW = 3;
    public static final int ENTITY_NUM_PROPERTIES = 4;

    void nextImage();
    Point getPosition();

    void setPosition(Point position);
    String getId();

    void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

    PImage getCurrentImage();
    int getImageIndex();

    default String log() {
        return this.getId().isEmpty() ? null :
                String.format("%s %d %d %d", this.getId(), this.getPosition().x, this.getPosition().y, this.getImageIndex());
    }
}
