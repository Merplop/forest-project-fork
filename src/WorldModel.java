import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel {
    public int numRows;
    public int numCols;
    public Background[][] background;
    public Entity[][] occupancy;
    public Set<Entity> entities;

    public WorldModel() {

    }

    /**
     * Helper method for testing. Don't move or modify this method.
     */
    public List<String> log(){
        List<String> list = new ArrayList<>();
        for (Entity entity : entities) {
            String log = entity.log();
            if(log != null) list.add(log);
        }
        return list;
    }

    public void parseSapling(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Sapling.SAPLING_NUM_PROPERTIES) {
            int health = Integer.parseInt(properties[Sapling.SAPLING_HEALTH]);
            Sapling entity = Sapling.createSapling(id, pt, imageStore.getImageList(Sapling.SAPLING_KEY), health);
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Sapling.SAPLING_KEY, Sapling.SAPLING_NUM_PROPERTIES));
        }
    }

    public void parseDude(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Dude.DUDE_NUM_PROPERTIES) {
            DudeNotFull entity = DudeNotFull.createDudeNotFull(id, pt, Double.parseDouble(properties[Dude.DUDE_ACTION_PERIOD]), Double.parseDouble(properties[Dude.DUDE_ANIMATION_PERIOD]), Integer.parseInt(properties[Dude.DUDE_LIMIT]), imageStore.getImageList(Dude.DUDE_KEY));
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Dude.DUDE_KEY, Dude.DUDE_NUM_PROPERTIES));
        }
    }

    public void parseFairy(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Fairy.FAIRY_NUM_PROPERTIES) {
            Fairy entity = Fairy.createFairy(id, pt, Double.parseDouble(properties[Fairy.FAIRY_ACTION_PERIOD]), Double.parseDouble(properties[Fairy.FAIRY_ANIMATION_PERIOD]), imageStore.getImageList(Fairy.FAIRY_KEY));
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Fairy.FAIRY_KEY, Fairy.FAIRY_NUM_PROPERTIES));
        }
    }

    public void parseTree(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Tree.TREE_NUM_PROPERTIES) {
            Tree entity = Tree.createTree(id, pt, Double.parseDouble(properties[Tree.TREE_ACTION_PERIOD]), Double.parseDouble(properties[Tree.TREE_ANIMATION_PERIOD]), Integer.parseInt(properties[Tree.TREE_HEALTH]), imageStore.getImageList(Tree.TREE_KEY));
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Tree.TREE_KEY, Tree.TREE_NUM_PROPERTIES));
        }
    }

    public void parseObstacle(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Obstacle.OBSTACLE_NUM_PROPERTIES) {
            Obstacle entity = Obstacle.createObstacle(id, pt, Double.parseDouble(properties[Obstacle.OBSTACLE_ANIMATION_PERIOD]), imageStore.getImageList(Obstacle.OBSTACLE_KEY));
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Obstacle.OBSTACLE_KEY, Obstacle.OBSTACLE_NUM_PROPERTIES));
        }
    }

    public void parseHouse(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == House.HOUSE_NUM_PROPERTIES) {
            House entity = House.createHouse(id, pt, imageStore.getImageList(House.HOUSE_KEY));
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", House.HOUSE_KEY, House.HOUSE_NUM_PROPERTIES));
        }
    }

    public void parseStump(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Stump.STUMP_NUM_PROPERTIES) {
            Stump entity = Stump.createStump(id, pt, imageStore.getImageList(Stump.STUMP_KEY));
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Stump.STUMP_KEY, Stump.STUMP_NUM_PROPERTIES));
        }
    }

    public void tryAddEntity(Entity entity) {
        if (isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        addEntity(entity);
    }

    public boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < numRows && pos.x >= 0 && pos.x < numCols;
    }

    public boolean isOccupied(Point pos) {
        return withinBounds(pos) && getOccupancyCell(pos) != null;
    }

    public Optional<Entity> findNearest(Point pos, List<Class> kinds) {
        List<Entity> ofType = new LinkedList<>();
        for (Class c : kinds) {
            for (Entity entity : entities) {
                if (c.isInstance(entity)) {
                    ofType.add(entity);
                }
            }
        }

        return pos.nearestEntity(ofType);
    }

    /*
           Assumes that there is no entity currently occupying the
           intended destination cell.
        */
    public void addEntity(Entity entity) {
        if (withinBounds(entity.getPosition())) {
            setOccupancyCell(entity.getPosition(), entity);
            entities.add(entity);
        }
    }

    public void moveEntity(EventScheduler scheduler, Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (withinBounds(pos) && !pos.equals(oldPos)) {
            setOccupancyCell(oldPos, null);
            Optional<Entity> occupant = getOccupant(pos);
            occupant.ifPresent(target -> removeEntity(scheduler, target));
            setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }

    public void removeEntity(EventScheduler scheduler, Entity entity) {
        scheduler.unscheduleAllEvents(entity);
        removeEntityAt(entity.getPosition());
    }

    public void removeEntityAt(Point pos) {
        if (withinBounds(pos) && getOccupancyCell(pos) != null) {
            Entity entity = getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            entities.remove(entity);
            setOccupancyCell(pos, null);
        }
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (isOccupied(pos)) {
            return Optional.of(getOccupancyCell(pos));
        } else {
            return Optional.empty();
        }
    }

    public Entity getOccupancyCell(Point pos) {
        return occupancy[pos.y][pos.x];
    }

    public void setOccupancyCell(Point pos, Entity entity) {
        occupancy[pos.y][pos.x] = entity;
    }

    public void load(Scanner saveFile, ImageStore imageStore, Background defaultBackground){
        parseSaveFile(saveFile, imageStore, defaultBackground);
        if(background == null){
            background = new Background[numRows][numCols];
            for (Background[] row : background)
                Arrays.fill(row, defaultBackground);
        }
        if(occupancy == null){
            occupancy = new Entity[numRows][numCols];
            entities = new HashSet<>();
        }
    }

    public void parseSaveFile(Scanner saveFile, ImageStore imageStore, Background defaultBackground){
        String lastHeader = "";
        int headerLine = 0;
        int lineCounter = 0;
        while(saveFile.hasNextLine()){
            lineCounter++;
            String line = saveFile.nextLine().strip();
            if(line.endsWith(":")){
                headerLine = lineCounter;
                lastHeader = line;
                switch (line){
                    case "Backgrounds:" -> background = new Background[numRows][numCols];
                    case "Entities:" -> {
                        occupancy = new Entity[numRows][numCols];
                        entities = new HashSet<>();
                    }
                }
            }else{
                switch (lastHeader){
                    case "Rows:" -> numRows = Integer.parseInt(line);
                    case "Cols:" -> numCols = Integer.parseInt(line);
                    case "Backgrounds:" -> parseBackgroundRow(line, lineCounter-headerLine-1, imageStore);
                    case "Entities:" -> parseEntity(line, imageStore);
                }
            }
        }
    }

    public void parseBackgroundRow(String line, int row, ImageStore imageStore) {
        String[] cells = line.split(" ");
        if(row < numRows){
            int rows = Math.min(cells.length, numCols);
            for (int col = 0; col < rows; col++){
                background[row][col] = new Background(cells[col], imageStore.getImageList(cells[col]));
            }
        }
    }

    public void parseEntity(String line, ImageStore imageStore) {
        String[] properties = line.split(" ", Entity.ENTITY_NUM_PROPERTIES + 1);
        if (properties.length >= Entity.ENTITY_NUM_PROPERTIES) {
            String key = properties[Entity.PROPERTY_KEY];
            String id = properties[Entity.PROPERTY_ID];
            Point pt = new Point(Integer.parseInt(properties[Entity.PROPERTY_COL]), Integer.parseInt(properties[Entity.PROPERTY_ROW]));

            properties = properties.length == Entity.ENTITY_NUM_PROPERTIES ?
                    new String[0] : properties[Entity.ENTITY_NUM_PROPERTIES].split(" ");

            switch (key) {
                case Obstacle.OBSTACLE_KEY -> parseObstacle(properties, pt, id, imageStore);
                case Dude.DUDE_KEY -> parseDude(properties, pt, id, imageStore);
                case Fairy.FAIRY_KEY -> parseFairy(properties, pt, id, imageStore);
                case House.HOUSE_KEY -> parseHouse(properties, pt, id, imageStore);
                case Tree.TREE_KEY -> parseTree(properties, pt, id, imageStore);
                case Sapling.SAPLING_KEY -> parseSapling(properties, pt, id, imageStore);
                case Stump.STUMP_KEY -> parseStump(properties, pt, id, imageStore);
                case Lorax.LORAX_KEY -> parseFairy(properties, pt, id, imageStore);
                default -> throw new IllegalArgumentException("Entity key is unknown");
            }
        }else{
            throw new IllegalArgumentException("Entity must be formatted as [key] [id] [x] [y] ...");
        }
    }

    public Background getBackgroundCell(Point pos) {
        return background[pos.y][pos.x];
    }

    public void setBackgroundCell(Point pos, Background background) {
        this.background[pos.y][pos.x] = background;
    }

    public Optional<PImage> getBackgroundImage(Point pos) {
        if (withinBounds(pos)) {
            return Optional.of(getBackgroundCell(pos).getCurrentImage());
        } else {
            return Optional.empty();
        }
    }
}
