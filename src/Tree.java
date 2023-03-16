import processing.core.PImage;

import java.util.List;

public class Tree extends Plant {

    public static final String TREE_KEY = "tree";
    public static final int TREE_ANIMATION_PERIOD = 0;
    public static final int TREE_ACTION_PERIOD = 1;
    public static final int TREE_HEALTH = 2;
    public static final int TREE_NUM_PROPERTIES = 3;

    public static final double TREE_ANIMATION_MAX = 0.600;
    public static final double TREE_ANIMATION_MIN = 0.050;
    public static final double TREE_ACTION_MAX = 1.400;
    public static final double TREE_ACTION_MIN = 1.000;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;


    public Tree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        super(id, position, images, health, actionPeriod, animationPeriod, 0);

    }

    public static Tree createTree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        return new Tree(id, position, actionPeriod, animationPeriod, health, images);
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (super.getHealth() <= 0) {
            Entity stump = Stump.createStump(Stump.STUMP_KEY + "_" + this.getId(), this.getPosition(), imageStore.getImageList(Stump.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (!this.transform(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this, ActivityAction.createActivityAction(this, world, imageStore), this.getActionPeriod());
        }
    }
}
