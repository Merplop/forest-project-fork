public class ActivityAction extends Action {

    public ActivityAction(Active entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        super(entity, world, imageStore, repeatCount);
    }

    public static ActivityAction createActivityAction(Active entity, WorldModel world, ImageStore imageStore) {
        return new ActivityAction(entity, world, imageStore, 0);
    }

    @Override
    public void executeAction(EventScheduler scheduler) {
        ((Active)this.getEntity()).executeActivity(this.getWorld(), this.getImageStore(), scheduler);
    }
}
