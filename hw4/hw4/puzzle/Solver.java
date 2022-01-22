package hw4.puzzle;

import java.util.*;

public class Solver {
    private int minMove;
    private List<WorldState> solution = new ArrayList<>();
    int totalAdd;
    private Map<WorldState, Integer> edtgCaches = new HashMap<>();

    private class Node {
        private WorldState currNode;
        private int moveTo;
        private int priority;
        private Node prevNode;

        public Node(WorldState searchNode, int moveTo, Node prevNode) {
            this.currNode = searchNode;
            this.moveTo = moveTo;
            this.prevNode = prevNode;
            this.priority = this.moveTo + searchNode.estimatedDistanceToGoal();
        }
    }

    private class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            int o1Edtg = getEdtg(o1);
            int o2Edtg = getEdtg(o2);
            int o1Priority = o1.moveTo + o1Edtg;
            int o2Priority = o2.moveTo + o2Edtg;
            return o1.priority - o2.priority;
        }
    }

    private int getEdtg(Node sn) {
        if (!edtgCaches.containsKey(sn.currNode)) {
            edtgCaches.put(sn.currNode, sn.currNode.estimatedDistanceToGoal());
        }
        return edtgCaches.get(sn.currNode);
    }
    /**Constructor which solves the puzzle, computing everything
     * necessary for moves() and solution() to not have to solve the
     * problem again. Solves the puzzle using the A* algorithm.
     * Assumes a solution exists. */
    public Solver(WorldState initial) {
        PriorityQueue<Node> fringe = new PriorityQueue<>(new NodeComparator());
        Node searchNode = new Node(initial, 0, null);
        fringe.add(searchNode);
        totalAdd++;
        while (!fringe.isEmpty()) {
            searchNode = fringe.poll();
            // marked.add(searchNode.currNode);
            //solution.add(searchNode.currNode);
            if (searchNode.currNode.isGoal()) {
                minMove = searchNode.moveTo;
                break;
            }
            for (WorldState w : searchNode.currNode.neighbors())  {
                /*
                if (!marked.contains(w)) {
                    fringe.add(new Node(w, searchNode.moveTo + 1, searchNode));
                    totalAdd++;
                }*/
                Node newNode = new Node(w, searchNode.moveTo + 1, searchNode);
                if (searchNode.prevNode != null && w.equals(searchNode.prevNode.currNode)) {
                    continue;
                }
                fringe.add(newNode);
                totalAdd++;
            }
        }

        Stack<WorldState> path = new Stack<>();
        for (Node n = searchNode; n != null; n = n.prevNode) {
            path.push(n.currNode);
        }
        while (!path.isEmpty()) {
            solution.add(path.pop());
        }
    }
    /**Returns the minimum number
     * of moves to solve the puzzle starting at the initial
     * WorldState.*/
    public int moves() {
        return minMove;
    }

    /** Returns a sequence of WorldState from the initial WorldState
     * to the solution. */
    public Iterable<WorldState> solution() {
        return solution;
    }
}
