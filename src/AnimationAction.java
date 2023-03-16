public class AnimationAction extends Action {


    public AnimationAction(Animated entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        super(entity, world, imageStore, repeatCount);
    }

    public static AnimationAction createAnimationAction(Animated entity, int repeatCount) {
        return new AnimationAction(entity, null, null, repeatCount);
    }


    @Override
    public void executeAction(EventScheduler scheduler) {
        this.getEntity().nextImage();

        if (this.getRepeatCount() != 1) {
            scheduler.scheduleEvent(this.getEntity(), AnimationAction.createAnimationAction((Animated)(this.getEntity()), Math.max(this.getRepeatCount() - 1, 0)), ((Animated)this.getEntity()).getAnimationPeriod());
        }
    }
}
