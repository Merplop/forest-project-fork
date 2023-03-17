public abstract class Action {

    private final Entity entity;
    private final WorldModel world;
    private final ImageStore imageStore;
    private final int repeatCount;

    public Action(Entity entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }
    public abstract void executeAction(EventScheduler scheduler);
    public Entity getEntity() {return this.entity;}
    public WorldModel getWorld() {return this.world;}
    public ImageStore getImageStore() {return this.imageStore;}
    public int getRepeatCount() {return this.repeatCount;}

}
