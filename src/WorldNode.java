import java.util.Comparator;

public class WorldNode {

    public Point point;
    public WorldNode previous;
    public int g;
    public int h;

    public int f;

    public WorldNode(WorldNode prev, Point p, int g, int h) {
        this.previous = prev;
        this.point = p;
        this.g = g;
        this.h = h;
        this.f = this.h + this.g;
    }
}

class NodeComparator implements Comparator<WorldNode> {
    @Override
    public int compare(WorldNode o1, WorldNode o2) {
        return o1.f - o2.f;
    }
}
