import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DudeFull extends Dude {

    public DudeFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        super(id, position, actionPeriod, animationPeriod, resourceLimit, images);
        this.setResourceLimit(resourceLimit);
        this.setResourceCount(resourceLimit);
    }

    public static DudeFull createDudeFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new DudeFull(id, position, actionPeriod, animationPeriod, resourceLimit, images);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(House.class)));

        if (fullTarget.isPresent() && this.moveTo(world, fullTarget.get(), scheduler)) {
            this.transform(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this, ActivityAction.createActivityAction(this, world, imageStore), this.getActionPeriod());
        }
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, ActivityAction.createActivityAction(this, world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this, AnimationAction.createAnimationAction(this, 0), this.getAnimationPeriod());
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        DudeNotFull dude = DudeNotFull.createDudeNotFull(this.getId(), this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(), this.getResourceLimit(), this.getImages());

        world.removeEntity(scheduler, this);

        world.addEntity(dude);
        dude.scheduleActions(scheduler, world, imageStore);
        return true;
    }
}
