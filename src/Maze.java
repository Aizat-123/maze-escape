import java.util.*;

/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        int[] firstCell = w.a();
        int[] secondCell = w.b();

        DisjointSet dsu = new DisjointSet(w.cells());
        int[] broken = new int[w.cells() - 1];
        int brokenCount = 0;

        for (int i = 0; i < w.count(); i++) {
            if (dsu.union(firstCell[i], secondCell[i])) {
                broken[brokenCount++] = i;

                if (brokenCount == broken.length) {
                    break;
                }
            }
        }

        return broken;
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        int[] firstCell = w.a();
        int[] secondCell = w.b();
        int[] wallCost = w.cost();

        Integer[] order = new Integer[w.count()];

        for (int i = 0; i < order.length; i++) {
            order[i] = i;
        }

        Arrays.sort(order, Comparator.comparingInt(i -> wallCost[i]));

        DisjointSet dsu = new DisjointSet(w.cells());
        int[] broken = new int[w.cells() - 1];
        int brokenCount = 0;

        for (int wallIndex : order) {
            if (dsu.union(firstCell[wallIndex], secondCell[wallIndex])) {
                broken[brokenCount++] = wallIndex;

                if (brokenCount == broken.length) {
                    break;
                }
            }
        }

        return broken;
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        int n = w.cells();

        int[] firstCell = w.a();
        int[] secondCell = w.b();
        int[] wallCost = w.cost();

        List<Integer>[] edges = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            edges[i] = new ArrayList<>();
        }

        for (int i = 0; i < w.count(); i++) {
            edges[firstCell[i]].add(i);
            edges[secondCell[i]].add(i);
        }

        boolean[] visited = new boolean[n];

        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(
                Comparator.comparingInt(i -> wallCost[i])
        );

        visited[0] = true;
        priorityQueue.addAll(edges[0]);

        long totalCost = 0;
        int visitedCount = 1;

        while (!priorityQueue.isEmpty() && visitedCount < n) {
            int wallIndex = priorityQueue.poll();

            int a = firstCell[wallIndex];
            int b = secondCell[wallIndex];

            int nextCell = visited[a] ? b : a;

            if (visited[nextCell]) {
                continue;
            }

            visited[nextCell] = true;
            visitedCount++;
            totalCost += wallCost[wallIndex];

            for (int edgeIndex : edges[nextCell]) {
                priorityQueue.add(edgeIndex);
            }
        }

        return totalCost;
    }
}
