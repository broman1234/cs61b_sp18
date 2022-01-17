package lab11.graphs;

import java.util.Random;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int[] pathTo;

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        // TODO: Your code here!
        pathTo = new int[maze.V()];
        /*
        Random rand = new Random();
        int startX = rand.nextInt(maze.N());
        int startY = rand.nextInt(maze.N());
        int s = maze.xyTo1D(startX, startY);
        */
        int s = 0;
        pathTo[s] = s;
        distTo[s] = 0;
        detectCycle(s);
    }

    // Helper methods go here
    public void detectCycle(int source) {
        marked[source] = true;
        announce();

        for (int w : maze.adj(source)) {
            if (!marked[w]) {
                pathTo[w] = source;
                distTo[w] = distTo[source] + 1;
                detectCycle(w);
                return;
            } else if (pathTo[source] != w) {
                edgeTo[w] = source;
                int parent = pathTo[source];
                edgeTo[source] = parent;
                int t = parent;
                while (t != w) {
                    parent = pathTo[t];
                    edgeTo[t] = parent;
                    t = parent;
                }
                announce();
                return;
            }
        }
    }
}

