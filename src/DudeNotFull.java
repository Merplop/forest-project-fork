import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends Dude {


    public DudeNotFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        super(id, position, actionPeriod, animationPeriod, resourceLimit, images);
    }

    public static DudeNotFull createDudeNotFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new DudeNotFull(id, position, actionPeriod, animationPeriod, resourceLimit, images);
    }

    @Override
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            this.setResourceCount(this.getResourceCount()+1);
            ((Plant)target).setHealth(((Plant)target).getHealth()-1);
            return true;
        }
        return super.moveTo(world, target, scheduler);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target = world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (target.isEmpty() || !this.moveTo(world, target.get(), scheduler) || !this.transform(world, scheduler, imageStore)) {
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
        if (this.getResourceCount() >= this.getResourceLimit()) {
            DudeFull dude = DudeFull.createDudeFull(this.getId(), this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(), this.getResourceLimit(), this.getImages());

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            dude.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
}
