package activ;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Vector;

/*
 * this class is used to store temporary data concerning the requried data 
 */

public class ImageController {
    
    static boolean drawSquare = false;
    
    /* handeling resizing and drawing */
    static boolean startCapture = false;
    static boolean squareDrawed = true;
    static boolean canDraw      = false;
    
    static Color squareColor = Color.YELLOW;
    static Color squareColor2 = Color.RED;
    static Color lineColor = Color.YELLOW;
    static Color textC;
    static Color bgC;
    
    static Color scrollingTextColor = Color.WHITE;
    
    static boolean startColorCapture = false;
    static boolean startBGColorCapture = false;
    static boolean startScrollTextColorCapture = false;
    
    static int lineWidth = 1;
    
    static boolean scrollingTextColorPicked = false;
    
    static boolean drawLine = false;
    static boolean lineDrawed = false;
    
    static boolean drawBoxArea = false;
    
    static int lineX = 220;
 
    static Rectangle rect = null;
    
    static Vector frames = new Vector();
    
    static void setRectElement(Rectangle r){
        rect = r;
        System.out.println("Setting rec "+r);
    }
    
    static void add(String s){
        frames.add(s);
    }
    
    static String elementAt(int i){
        return (String)frames.get(i);
    }
    
    static void delete(String s){
        frames.remove(s);
    }
    
    static void delete(int i){
        frames.remove(i);
    }
    
    static void indexOf(String s){
        frames.indexOf(s);
    }
    
    static boolean contains(String s){
        return frames.contains(s);
    }
    
    static int count(){
        return frames.size();
    }
}
