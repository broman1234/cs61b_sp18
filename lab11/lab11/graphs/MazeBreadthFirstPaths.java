package lab11.graphs;

import java.util.LinkedList;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs(int v) {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
        Queue<Integer> q = new LinkedList<>();
        q.add(v);
        marked[v] = true;
        announce();

        if (v == t) {
            targetFound = true;
        }

        if (targetFound) {
            return;
        }

        while (!q.isEmpty()) {
            int out = q.poll();
            for (int w : maze.adj(out)) {
                if (!marked[w]) {
                    edgeTo[w] = out;
                    announce();
                    distTo[w] = distTo[out] + 1;
                    marked[w] = true;
                    announce();
                    if (w == t) {
                        targetFound = true;
                    }
                    if (targetFound) {
                        return;
                    }
                    q.add(w);
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs(s);
    }
}

