/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package activ;

import com.googlecode.javacv.FFmpegFrameGrabber;
import com.googlecode.javacv.FrameGrabber;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ramii
 */
public class VideoController {

    static Vector <Image> vImg = new Vector();
    static FFmpegFrameGrabber g;
    static int selectedIndex = 0;
    static boolean playing = false;
    
    static double videoSpeed = 1;
            
    static void init(String url){
        try {
            g = new FFmpegFrameGrabber(url);
            g.start();
            g.setFrameNumber(1);
        } catch (FrameGrabber.Exception ex) {
            Logger.getLogger(VideoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static void stop(){
        try {
            g.stop();
        } catch (FrameGrabber.Exception ex) {
            Logger.getLogger(VideoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
