import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

        if (loraxTarget.isEmpty()) {
            scheduler.scheduleEvent(this, ActivityAction.createActivityAction(this, world, imageStore), this.getActionPeriod());
            return;
        }

        // Makes the grass greener
        // Kinda looks janky because it only targets grass.png but oh well
        Stream.concat(
                PathingStrategy.CARDINAL_NEIGHBORS.apply(getPosition()),
                Stream.of(this.getPosition())
        )
        .filter(world::withinBounds)
        .filter(p -> world.getBackgroundCell(p).id.equals("grass"))
        .forEach(p -> {
            world.setBackgroundCell(p, new Background("grass_greener", imageStore.getImageList("grass_greener")));
        });

        // Visits every cardinal tree and turns it into a rainbow tree
        PathingStrategy.CARDINAL_NEIGHBORS.apply(getPosition())
        .filter(world::withinBounds)
        .map(world::getOccupancyCell)
        .filter(ent -> ent != null)
        .forEach(ent -> {
            if (ent instanceof Tree tree){
                tree.setImageSet(imageStore.getImageList("rainbow_tree"));
            }
            else if (ent instanceof Fairy fairy) {
                fairy.setImageSet(imageStore.getImageList("fairy_hyper"));
                fairy.setActionPeriod(0.15);
            }
        });



        Point tgtPos = loraxTarget.get().getPosition();

        Point behind = new Point(this.getPosition().x-1, this.getPosition().y);

        if (!world.isOccupied(behind) && Math.random() <= 0.1) { // random 1/10 chance to spawn sapling in adjacent tile
            Sapling sapling = Sapling.createSapling(Sapling.SAPLING_KEY + "_" + loraxTarget.get().getId(), behind, imageStore.getImageList(Sapling.SAPLING_KEY), 0);
            world.addEntity(sapling);
            sapling.scheduleActions(scheduler, world, imageStore);
        }

        // If Lorax is adjacent to a Dude
        if (this.moveTo(world, loraxTarget.get(), scheduler)) {
            Player loraxPlayer = new Player("loraxLine.wav");
            Thread sound = new Thread(loraxPlayer);
            sound.start();

            world.addEntity(new Skull(Skull.SKULL_KEY, tgtPos, imageStore.getImageList(Skull.SKULL_KEY)));
        }

        scheduler.scheduleEvent(this, ActivityAction.createActivityAction(this, world, imageStore), this.getActionPeriod());
    }

    public static Lorax createLorax(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new Lorax(id, position, actionPeriod, animationPeriod, images);
    }


}
