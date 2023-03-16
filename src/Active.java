public interface Active extends Entity {

    void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    double getActionPeriod();
}
