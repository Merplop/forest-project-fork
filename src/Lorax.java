import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Lorax extends Fairy {

    public static final String LORAX_KEY = "lorax";

    public Lorax(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        super(id, position, actionPeriod, animationPeriod, images);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> loraxTarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(DudeFull.class, DudeNotFull.class)));

        if (loraxTarget.isPresent()) {
            Point tgtPos = loraxTarget.get().getPosition();

            // If Lorax is adjacent to a Dude
            if (this.moveTo(world, loraxTarget.get(), scheduler)) {

                // TODO Add functionality for talking to dude

//                Sapling sapling = Sapling.createSapling(Sapling.SAPLING_KEY + "_" + loraxTarget.get().getId(), tgtPos, imageStore.getImageList(Sapling.SAPLING_KEY), 0);
//
//                world.addEntity(sapling);
//                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, ActivityAction.createActivityAction(this, world, imageStore), this.getActionPeriod());
    }


}
