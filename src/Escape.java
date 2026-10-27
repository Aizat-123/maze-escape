import java.util.Arrays;

/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */
public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int n = w.cells();
        int cols = w.cols();
        int exit = n - 1;

        int[][] graph = Graphs.open(w, broken);

        int deadEnds = 0;
        for (int i = 0; i < n; i++) {
            if (graph[i].length == 1) {
                deadEnds++;
            }
        }

        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        int[] nextDirection = new int[n];
        int[] stack = new int[n];

        Arrays.fill(parent, -1);

        int top = 0;
        int visitedCount = 1;

        stack[0] = 0;
        visited[0] = true;

        while (top >= 0 && !visited[exit]) {
            int current = stack[top];

            if (nextDirection[current] == 4) {
                top--;
                continue;
            }

            int direction = nextDirection[current]++;
            int next = -1;

            if (direction == 0 && current >= cols) {
                next = current - cols;
            } else if (direction == 1 && current % cols < cols - 1) {
                next = current + 1;
            } else if (direction == 2 && current + cols < n) {
                next = current + cols;
            } else if (direction == 3 && current % cols > 0) {
                next = current - 1;
            }

            if (next == -1 || visited[next]) {
                continue;
            }

            boolean open = false;

            for (int neighbour : graph[current]) {
                if (neighbour == next) {
                    open = true;
                    break;
                }
            }

            if (open) {
                visited[next] = true;
                visitedCount++;
                parent[next] = current;
                stack[++top] = next;
            }
        }

        int pathLength = 0;
        for (int cell = exit; cell != -1; cell = parent[cell]) {
            pathLength++;
        }

        int[] path = new int[pathLength];
        int cell = exit;

        for (int i = pathLength - 1; i >= 0; i--) {
            path[i] = cell;
            cell = parent[cell];
        }

        return new Result(path, visitedCount, deadEnds);
    }
}
