package farthestpairfinder;

import javax.swing.JFrame;
import java.awt.*;
import java.util.Random;
import java.util.ArrayList;

public class FarthestPairFinder extends JFrame {

     int pointSize = 12;
     int numPoints = 100;
     
     Point2D[] S = new Point2D[ numPoints ]; //the set S
     Point2D[] farthestPair = new Point2D[ 2 ]; //the two points of the farthest pair
     
     ArrayList<Point2D> convexHull = new ArrayList(); //the vertices of the convex hull of S
     
     Color convexHullColour = Color.white;
     Color genericColour = Color.yellow;

    
    //fills S with random points
    public void makeRandomPoints() {
        Random rand = new Random();
 
        for (int i = 0; i < numPoints; i++) {
            int x = 150 + rand.nextInt(500);
            int y = 150 + rand.nextInt(500);
            S[i] = new Point2D( x, y );            
        }        
    }

    
    public void paint(Graphics g) { 
        g.setColor(Color.black); 
        g.fillRect(0, 0, 800, 800);
        //draw the points in S
        g.setColor(genericColour);
        for(int i = 0; i < S.length; i ++){
            g.setColor(Color.yellow);
            g.fillOval(S[i].x - pointSize/2, S[i].y - pointSize/2, pointSize, pointSize);
        }
        
        //draw the points in the convex hull
        g.setColor(convexHullColour);
        for(int i = 0; i < convexHull.size(); i ++){
            g.fillOval(convexHull.get(i).x - pointSize/2, convexHull.get(i).y - pointSize/2, pointSize, pointSize);
            
            if(i == convexHull.size()-1){
                g.drawLine(convexHull.get(i).x, convexHull.get(i).y, convexHull.get(0).x, convexHull.get(0).y);
            }
            else{
                g.drawLine(convexHull.get(i).x, convexHull.get(i).y, convexHull.get(i+1).x, convexHull.get(i+1).y);
            }
        }
        //draw a red line connecting the farthest pair
        g.setColor(Color.red);
        g.drawLine(farthestPair[0].x, farthestPair[0].y, farthestPair[1].x, farthestPair[1].y);
    }
    
    
    public void findConvexHull() {
        //code this
        //you'll need to make use of the Vector class to help calculate angles in 2D
        double lowest = 0;
        int index = 0;
        
        //find lowest point on screen (largest value for y)
        for(int i = 0; i < S.length; i++){
            if(S[i].y > lowest){
                lowest = S[i].y;
                index = i;
            }
        }
        //add point to convex hull and a point further to the right to form a vector
        convexHull.add(new Point2D(700, S[index].y));
        convexHull.add(S[index]);
        
        //note start point
        Point2D start = S[index];
        
        //continue adding points until start point reappears in arraylist
        while(convexHull.lastIndexOf(start) == 1){
            lowest = 1000;
            int n = convexHull.size();
            
            //create vector between previous point and current convex hull point
            Vector vector1 = convexHull.get(n-2).subtract(convexHull.get(n-1));
            
            for(int i = 0; i < S.length; i++){
                //create vector between current point and potential convex hull point
                Vector vector2 = convexHull.get(n-1).subtract(S[i]);
                
                //calculate angle and compare to see if it's the lowest
                double angle = vector1.getAngle(vector2);
                if(angle < lowest){
                    lowest = angle;
                    index = i;
                    
                }
            }
            //add next point in the convex hull
            convexHull.add(S[index]);
        }
        //remove point used to make horizontal vector with lowest point
        convexHull.remove(0);
        //remove repeated point
        convexHull.remove(convexHull.size()-1);
    }
    
    public void findFarthestPair_EfficientWay() {
        //code this
        //must make use of the convex hull, which will have been calculated by the time this method is called
        double bestDistanceSquared = Double.NEGATIVE_INFINITY;
        
        int k = 2;
        int length = convexHull.size();
        
        //find farthest vertex from the edge made by first and last point
        while (Point2D.area(convexHull.get(1), convexHull.get(k+1), convexHull.get(length-1))
                > Point2D.area(convexHull.get(1), convexHull.get(k), convexHull.get(length-1))) {
            k++;
        }
        
        int j = k;
        
        for (int i = 1; i <= k; i++) {
            //if i and j are antipodal
            if(convexHull.get(i).distanceSquaredTo(convexHull.get(j)) > bestDistanceSquared) {
                farthestPair[0] = convexHull.get(i);
                farthestPair[1] = convexHull.get(j);
                bestDistanceSquared = convexHull.get(i).distanceSquaredTo(convexHull.get(j));
            }
            //look for more points that are antipodal with i
            while (j < length-1 && Point2D.area(convexHull.get(i), convexHull.get(j), convexHull.get(i+1))
                    > Point2D.area(convexHull.get(i), convexHull.get(j+1), convexHull.get(i+1))) {
                j++;
                double distance = convexHull.get(i).distanceSquaredTo(convexHull.get(j));
                
                if (distance > bestDistanceSquared) {
                    farthestPair[0] = convexHull.get(i);
                    farthestPair[1] = convexHull.get(j);
                    bestDistanceSquared = distance;
                }
            }
        }
    }
    
    public void findFarthestPair_BruteForceWay() {
        //without using antipodal points
        double largest = 0;
        
        for(int i = 0; i < convexHull.size(); i++){
            for(int j = i+1; j < convexHull.size(); j++){
                //calculate distance between each pair of points in convex hull
                double distance = Math.sqrt(Math.pow(convexHull.get(i).x - convexHull.get(j).x,2) + Math.pow(convexHull.get(i).y - convexHull.get(j).y,2));
                
                //compare and add points with largest distance to farthestPair
                if(distance > largest){
                    largest = distance;
                    farthestPair[0] = convexHull.get(i);
                    farthestPair[1] = convexHull.get(j);
                }
            }
        }
        
    }
    
   
    public static void main(String[] args) {

        //no changes are needed in main().  Just code the blank methods above.
        
        FarthestPairFinder fpf = new FarthestPairFinder();
        
        fpf.setBackground(Color.BLACK);
        fpf.setSize(800, 800);
        fpf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fpf.makeRandomPoints();
        
        fpf.findConvexHull();
        
        fpf.findFarthestPair_EfficientWay();
        //fpf.findFarthestPair_BruteForceWay();
        
        fpf.setVisible(true); 
    }
}
