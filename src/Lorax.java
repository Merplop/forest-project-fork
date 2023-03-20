import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Lorax extends Fairy {

    public static final String LORAX_KEY = "lorax";
    public static final int LORAX_ANIMATION_PERIOD = 1;
    public static final int LORAX_ACTION_PERIOD = 1;

    public Lorax(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        super(id, position, actionPeriod, animationPeriod, images);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> loraxTarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(DudeFull.class, DudeNotFull.class)));
        Point behind = new Point(this.getPosition().x-1, this.getPosition().y);
        int random = (int)(Math.random()*10);

        if (loraxTarget.isPresent()) {
            Point tgtPos = loraxTarget.get().getPosition();

            if (!world.isOccupied(behind) && random == 1) { // random 1/10 chance to spawn sapling in adjacent tile
                Sapling sapling = Sapling.createSapling(Sapling.SAPLING_KEY + "_" + loraxTarget.get().getId(), behind, imageStore.getImageList(Sapling.SAPLING_KEY), 0);
                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }

            // If Lorax is adjacent to a Dude
            if (this.moveTo(world, loraxTarget.get(), scheduler)) {

                Player loraxPlayer = new Player("loraxLine.wav");
                Thread sound = new Thread(loraxPlayer);
                sound.start();
            }

        }

        scheduler.scheduleEvent(this, ActivityAction.createActivityAction(this, world, imageStore), this.getActionPeriod());
    }

    public static Lorax createLorax(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new Lorax(id, position, actionPeriod, animationPeriod, images);
    }


}
