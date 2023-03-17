
public class Lorax extends Moveable implements Active, Animated{


    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

    }

    @Override
    public double getAnimationPeriod() {
        //TODO
        return 0;
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

    }

    @Override
    public Point nextPosition(WorldModel world, Point destPos) {
        return null;
    }
}
