public class Node implements Comparable<Node>{

    public int x, y;

    public char item;

    public boolean visited;

    public int g;

    public int h;

    public Node parent;

    public Node(int x, int y, char item) {
        this.x = x;
        this.y = y;
        this.item = item;
        this.visited = false;
    }

    public int calcH(Node goal){
       h=Math.abs(this.x - goal.x) + Math.abs(this.y - goal.y);
       return h;
    }

    public int f() {return g + h ;}

    public java.lang.String toString() {
        return " " + item + "(" + x + "," + y + ")";
    }

    @Override
    public int compareTo(Node o){
        return Integer.compare(this.h, o.h);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Node other)) return false;
        return this.x == other.x && this.y == other.y && this.item == other.item;
    }

    @Override
    public int hashCode() {
        return x*y + h + item;
    }
}
