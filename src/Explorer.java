import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Explorer {

    char[][] map;

    LinkedList<Node> KeyLocations = new LinkedList<>();
    LinkedList<LinkedList<Node>> totalPaths = new LinkedList<>();

    public Explorer() {
    }

    public void scan(char[][] map) {
        this.map = map;

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                char c = map[y][x];
                if (c != DungeonMap.OPEN && c != DungeonMap.WALL) {
                    //found an item
                    Node n = new Node(x, y, c);
                    KeyLocations.add(n);
                }
            }
        }
        System.out.println(KeyLocations);
    }

    int[][] moves = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};


    public void findPaths() {
        for(Node from : KeyLocations) {
            for(Node to : KeyLocations) {
                if(from.equals(to)) continue;
                LinkedList<Node> path = aStar(from, to);
                totalPaths.add(path);
                System.out.println(path);
            }
        }
    }

    public LinkedList<Node> aStar(Node start, Node end) {
        boolean[][] visited = new boolean[map.length][map[0].length];
        LinkedList<Node> result = new LinkedList<>();
        PriorityQueue<Node> test = new PriorityQueue<>();

        test.add(start);
        start.calcH(end);
        visited[start.y][start.x] = true;

        while (!test.isEmpty()) {
            Node current = test.poll();
            if (current.equals(end)) {
                // restore list
                while (current != null) {
                    result.addFirst(current);
                    current = current.parent;
                }
                return result;
            }
            for(int i = 0; i < moves.length; i++) {
                int nextX = current.x + moves[i][0];
                int nextY = current.y + moves[i][1];
                char nextC = map[nextY][nextX];
                if(!visited[nextY][nextX] && nextC != DungeonMap.WALL) {
                    //found an empty node
                    visited[nextY][nextX] = true;
                    Node next = new Node(nextX , nextY , nextC);
                    next.g = current.g + 1;
                    next.calcH(end);
                    next.parent = current;
                    test.add(next);
                }

            }

        }

        return null;
    }

   static class Edge implements Comparable<Edge> {
        LinkedList<Node> path;

        public Edge(LinkedList<Node> path) {
            this.path = path;
        }

        public String key(){
            assert path.peekFirst() != null;
            return path.peekFirst().item + "->" + path.peekLast().item;
        }

        @Override
        public int compareTo(@NotNull Explorer.Edge o) {
            return 0;
        }

        @Override
       public String toString() {
            return key() + "[" + this.path.size() + "]";
        }
    }

    public void findMST(){
        Set<Character> visited = new HashSet<>();
        Map<String, Edge> mst = new HashMap<>();


        Node current = KeyLocations.peekFirst();
        assert current != null;
        visited.add(current.item);

        PriorityQueue<Edge> pq = new PriorityQueue<>();
        for (LinkedList<Node> edge : totalPaths) {
            assert edge.peek() != null;
            if (edge.peek().equals(current)) {
                pq.add(new Edge(edge));
            }
        }

        while (!pq.isEmpty()) {
            Edge edge = pq.poll();
            assert edge.path.peekLast() != null;
            char item = edge.path.peekLast().item;
            assert edge.path.peekLast() != null;
            if(visited.contains(edge.path.peekLast().item)) continue;
            visited.add(item);
            mst.put(edge.key(), edge);
            assert edge.path.peekLast() != null;
            visited.add(edge.path.peekLast().item);
            for(LinkedList<Node> nextEdges : totalPaths){
                assert nextEdges.peekFirst() != null;
                if (nextEdges.peekFirst().item == item) {
                    pq.add(new Edge(nextEdges));
                }
            }
        }
        System.out.println(mst.values());
    }
}
