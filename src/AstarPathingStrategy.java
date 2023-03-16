import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;



public class AstarPathingStrategy implements PathingStrategy {

    @Override
    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough, BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors) {
        Map<Point, WorldNode> nodeMap = new HashMap<>();
        PriorityQueue<WorldNode> pq = new PriorityQueue<WorldNode>(new NodeComparator());
        List<Point> path = new ArrayList<>();
        pq.add(new WorldNode(null, start, 0, Math.abs((start.y-end.y) + (start.x-end.y))));
    //    WorldNode current = new WorldNode(null, start, Math.abs((start.y-end.y) + (start.x-end.y)));

        while(!pq.isEmpty()) {
    //        System.out.println("Iteration " + pq.size());
            WorldNode current = pq.poll();
            if (withinReach.test(current.point, end)) {
                return buildPath(current);
            }
            for (Point neighbour : potentialNeighbors.apply(current.point).toArray(Point[]::new)) {
                if (!canPassThrough.test(neighbour)) {
                    continue;
                }

                int gScore = current.g + manhattanDistance(current.point, neighbour);
                WorldNode nodeNeighbour = nodeMap.get(neighbour);
                if(nodeNeighbour == null) {
                    nodeNeighbour = new WorldNode(current, neighbour, Integer.MAX_VALUE, manhattanDistance(neighbour, end));
                    nodeMap.put(neighbour, nodeNeighbour);
                } else if (gScore >= nodeNeighbour.g) {
                    continue;
                }

                nodeNeighbour.previous = current;
                nodeNeighbour.g = gScore;
                nodeNeighbour.f = gScore + nodeNeighbour.h;
                if (!pq.contains(nodeNeighbour)) {
                    pq.add(nodeNeighbour);
                }
            }
        }
        return path;
    }

    private List<Point> buildPath(WorldNode end) {
        List<Point> path = new ArrayList<>();
        WorldNode current = end;
        while (current.previous != null) {
            path.add(0, current.point);
            current = current.previous;
        }
        return path;
    }

    private int manhattanDistance(Point a, Point b) {
        return Math.abs((a.y-b.y) + (a.x-b.y));
    }
}
