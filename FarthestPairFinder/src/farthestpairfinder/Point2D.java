package farthestpairfinder;

import java.awt.*;

public class Point2D {
    
    int x, y;
    boolean visited; //might need this in the convex hull finding algorithm
    Color color;
    
    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
        this.visited = false;
        this.color = Color.yellow;
    }
    
    //Returns the vector that stretches between this and other.
    public Vector subtract( Point2D other ) {
        return new Vector( this.x - other.x, this.y - other.y);
    }
    public static double area(Point2D a, Point2D b, Point2D c) {
        return (b.x-a.x)*(c.y-a.y) - (b.y-a.y)*(c.x-a.x);
    }
    public double distanceSquaredTo(Point2D other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return dx*dx + dy*dy;
    }
}
