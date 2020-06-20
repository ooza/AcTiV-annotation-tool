/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package activ;

import static activ.ImageController.rect;
import com.googlecode.javacv.FrameGrabber;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Short.SIZE;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
/**
 *
 * @author ramii
 */

public class MainFrame extends javax.swing.JFrame {
    Vector <TextIntervalQuad> v = new Vector();
    Vector <TextLineQuad> v2 = new Vector();
    Rectangle textBoxRect = null;
    
    static final int VIDEO = 1;
    static final int IMAGE = 2;
            
    static int controller = IMAGE;
    
    class VideoThread extends Thread {
        public void run() {
            long startTime;
            long elapsedTime;
            
            if (controller == IMAGE)
                return;
            
            double fr = VideoController.g.getFrameRate();
            for(;VideoController.playing;){
                startTime = System.currentTimeMillis();
                elapsedTime = 0L;
                while ((elapsedTime < (int)((1/fr)*100)) && VideoController.playing) {
                    //perform db poll/check
                    elapsedTime = (new Date()).getTime() - startTime;
                }
                doLoadFrame(VideoController.selectedIndex+1);
            }  
        }
    }
        
    VideoThread videoThread;
    
    class GPanel extends JPanel
    {
        private Rectangle2D[] points = { new Rectangle2D.Double(50, 50,SIZE, SIZE), new Rectangle2D.Double(150, 100,SIZE, SIZE)};
        Rectangle2D s = new Rectangle2D.Double();
        
        ShapeResizeHandler ada = new ShapeResizeHandler();
        
        private double zoom = 1.0;
        private double percent =  0.1; // step
        
        private Image img;
        private BufferedImage bImg;
        
        int h = 0;
        int w = 0;

        class ShapeResizeHandler extends MouseAdapter {
            Rectangle2D r = new Rectangle2D.Double(0,0,SIZE,SIZE);
            private int pos = -1;
            
            public void mouseWheelMoved(MouseWheelEvent mwe) {
                /* mouse wheel forward = zoom in */
                if (mwe.getWheelRotation() < 0)
                    doZoomIn();
                else
                    doZoomOut();
            }
                        
            public void mouseMoved(MouseEvent event){
                if (ImageController.startCapture || ImageController.startColorCapture || ImageController.startBGColorCapture )
                    setCursor (Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                else
                    setCursor (Cursor.getDefaultCursor());
                try{
                    Color c = new Color(bImg.getRGB((int)(event.getX() / zoom), (int)(event.getY() / zoom)));
                    jlColor.setText("Color ("+c.getRed()+", "+c.getGreen()+", "+c.getBlue()+")");
                    jlColor.setForeground(new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue()));
                    jpColor.setBackground(c);
                }catch(Exception e){
                     jlColor.setText("Color (null)");
                     jpColor.setBackground(Color.WHITE);
                }
            }
                    
            public void mousePressed(MouseEvent event) {
                Point p = event.getPoint();
                p.x = (int)(p.x/zoom);
                p.y = (int)(p.y/zoom);
                if (jtpTextPane.getSelectedIndex() == 0){
                    
                    if(!ImageController.squareDrawed)
                        {
                        ImageController.canDraw = true;
                        points[0].setRect(p.x, p.y ,points[0].getWidth(),
                        points[0].getHeight());
                        pos = 1;
                        return;
                    }
                    
                    for (int i = 0; i < points.length; i++) {
                        if (points[i].contains(p)) {
                                pos = i;
                            return;
                        }
                    }
                }else{
                    if (Math.abs(p.x - ImageController.lineX) < 10){
                        pos = 1;
                    }
                }
            }

            public void mouseReleased(MouseEvent event) {
                pos = -1;
                if ((jtpTextPane.getSelectedIndex() == 0) || (ImageController.startScrollTextColorCapture)){
                    if (!ImageController.squareDrawed){
                            ImageController.squareDrawed = true;
                        ImageController.startCapture = false;
                    }
                    else if (ImageController.startColorCapture ||
                            ImageController.startBGColorCapture ||
                            ImageController.startScrollTextColorCapture){
                        
                        Color c = null;
                        try{
                        c = new Color(bImg.getRGB((int)(event.getX() / zoom), (int)(event.getY() / zoom)));
                        }catch(ArrayIndexOutOfBoundsException e){}
                        if (ImageController.startColorCapture)
                            ImageController.textC = c;
                        else if (ImageController.startBGColorCapture)
                            ImageController.bgC = c;
                        else if (ImageController.startScrollTextColorCapture){
                            ImageController.scrollingTextColorPicked = true;
                            XMLHandler.setScrollingTextColor(c);
                        }
                        ImageController.startColorCapture = false;
                        ImageController.startBGColorCapture = false;
                        ImageController.startScrollTextColorCapture = false;
                    }
                }else{
                    ImageController.lineDrawed = true;
                }
                
                setCursor (Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
            @Override
            public void mouseDragged(MouseEvent event){
                
                if (pos == -1)
                    return;
                Point p = event.getPoint();
                
                p.x = (int)(p.x / zoom);
                p.y = (int)(p.y / zoom);
                
                if (jtpTextPane.getSelectedIndex() == 0){
                    
                    /* you shall not pass! */
                    if (p.x - 4 < 0)
                        p.x = -8;
                    if (p.x-4 > w)
                        p.x = w-8;
                    if (p.y - 4< 0)
                        p.y = -8;
                    if (p.y - 4> h)
                        p.y = h-8;
                    
                    points[pos].setRect(p.x, p.y ,points[pos].getWidth(),
                        points[pos].getHeight());
                }else{
                    if(p.x < 0)
                        p.x = 1;
                    if(p.x > w)
                        p.x = w-1;
                    
                    ImageController.lineX = p.x;
                }
                
                repaint();
            }
        }
    
        public GPanel(Image i){
            setImage(i);
            addMouseListener(ada);
            addMouseMotionListener(ada);
        }
        
        public void setImage(Image i){
            img = i;
            h = 0;
            w = 0;
            if (img != null){
                h = img.getHeight(null);
                w = img.getWidth(null);
                bImg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
                bImg.getGraphics().drawImage(img, 0, 0, null);
            }
        }
        
        @Override
        public void paintComponent(Graphics g){
            /* this needs to be done or it will mess things up */
            super.paintComponent(g);
            
            Graphics2D g2D = (Graphics2D) g;
            g2D.scale(zoom, zoom);
            g2D.drawImage(img, 0, 0, this);
            
            /* change the size depending the the zoom value */
            if (img != null)
                setPreferredSize(new Dimension((int)(zoom * w), 
                        (int)(zoom * h)));
            
            /* update UI more precisely the scrollbars*/
            updateUI();
            g2D.setStroke(new BasicStroke(ImageController.lineWidth));
            if (ImageController.drawBoxArea){
                g2D.setColor(ImageController.squareColor2);
                g2D.drawRect(textBoxRect.x, textBoxRect.y, textBoxRect.width, textBoxRect.height);
            }
            
            if (ImageController.drawSquare)
            {
                if (!ImageController.canDraw)
                    return;
                
                for (int i = 0; i < points.length; i++) {
                    g2D.fill(points[i]);
                }
            
                g2D.setColor(ImageController.squareColor);
                
                s.setRect(points[0].getCenterX(), points[0].getCenterY(),
                Math.abs(points[1].getCenterX()-points[0].getCenterX()),
                Math.abs(points[1].getCenterY()- points[0].getCenterY())); 
                jlPoint1XY.setText("Point 1: ("+ (int)points[0].getCenterX()+", "+ 
                        (int)points[0].getCenterY()+")");
                jlTextWidth.setText("Width: ("+ ((int)points[1].getCenterX() - 
                        (int)points[0].getCenterX())+")");
                jlTextHeight.setText("Height: ("+ ((int)points[1].getCenterY()- 
                        (int)points[0].getCenterY())+")");
                g2D.draw(s);
                
                if(ImageController.rect != null){
                    ImageController.rect.x = (int)points[0].getCenterX();
                    ImageController.rect.y = (int)points[0].getCenterY();
                    ImageController.rect.width = (int)points[1].getCenterX() -(int)points[0].getCenterX();
                    ImageController.rect.height = (int)points[1].getCenterY()-(int)points[0].getCenterY();
                }
            }else if (ImageController.drawLine){
                g2D.setColor(ImageController.lineColor);
                g2D.draw(new Line2D.Float(ImageController.lineX, 0, ImageController.lineX, h));
                jlPoint1XY.setText("Line Pos: "+ ImageController.lineX);
                jlTextWidth.setText("");
                jlTextHeight.setText("");
            }
        }    
        
        public void setZoomPercentage(int zoomPercentage) {
            percent = ((double) zoomPercentage) / 100;
        }

        public void originalSize() {
            zoom = 1;
        }

        public void zoomIn() {
            zoom += percent;
        }

        public void zoomOut() {
            zoom -= percent;

            if (zoom < percent) {
                if (percent > 1.0) {
                    zoom = 1.0;
                } else {
                    zoomIn();
                }
            }
        }
    }
    
    void doZoomIn(){
        gPanel.zoomIn();
        gPanel.repaint();
        btnZoomValue.setText(((int)(gPanel.zoom * 100))+"%");
    }
    
    void doZoomOut(){
        gPanel.zoomOut();
        gPanel.repaint();
        btnZoomValue.setText(((int)(gPanel.zoom * 100))+"%");
    }
    
    void doZoomReset(){
        gPanel.originalSize();
        gPanel.repaint();
        btnZoomValue.setText("100%");
    }
    
    void doNextFrame(){
        if (jListImages.getSelectedIndex() + 1 < jListImages.getModel().getSize())
            jListImages.setSelectedIndex(jListImages.getSelectedIndex() + 1);
    }
    
    void doNextFewFrames(){
        if (jListImages.getSelectedIndex() + 5 < jListImages.getModel().getSize())
            jListImages.setSelectedIndex(jListImages.getSelectedIndex() + 5);
        else
            jListImages.setSelectedIndex(jListImages.getModel().getSize() - 1);   
    }
    
    void doPrevFrame(){
        if (jListImages.getSelectedIndex() - 1 > 0)
            jListImages.setSelectedIndex(jListImages.getSelectedIndex() - 1);
        else
            jListImages.setSelectedIndex(0);        
    }
    
    void doPrevFewFrames(){
        if (jListImages.getSelectedIndex() - 5 > 0)
            jListImages.setSelectedIndex(jListImages.getSelectedIndex() - 5);
        else
            jListImages.setSelectedIndex(0);         
    }
    
    void doLoadImagesFromFile(File f){
         try {
                for (File path : DirectoryReader.listFiles(f.getAbsolutePath())) {
                    if (!ImageController.contains(path.getAbsolutePath()))
                        ImageController.add(path.getAbsolutePath());
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            DefaultListModel tmp = (DefaultListModel)jListImages.getModel();
            tmp.clear();
            for(int i = 0; i < ImageController.count(); i++){
                tmp.addElement((new File(ImageController.elementAt(i))).getName());
            }
            jsFrameSlider.setMaximum(ImageController.count());
            jsFrameSlider.setValue(0);
            controller = IMAGE;
    }
    
    void doLoadImages(){
        /*
         * Shows a filechooser dialog and displays the selected files in the 
         * Modal of the jListImages and add them to the StaticImage class.
         */
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setMultiSelectionEnabled(true);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            doLoadImagesFromFile(chooser.getSelectedFile());
        }
    }
    
    void doDeleteImage(int i){
        DefaultListModel model = (DefaultListModel)(jListImages.getModel());
        if ((i < 0) || (i > model.getSize()))
            return;
        model.remove(i);
        ImageController.delete(i);
        jsFrameSlider.setMaximum(ImageController.count());
    }
    
    /* notify components about image changes */
    void updateComponentsImageIndex(int i){
        jlFrameW.setText("F. Width:"+gPanel.img.getWidth(null));
        jlFrameH.setText("F. Height:"+gPanel.img.getHeight(null));
        gPanel.repaint();
        
        if (controller == IMAGE)
            btnCurrentFrame.setText(i+1 + " / " + ImageController.count());
        else
             btnCurrentFrame.setText(i+1 + " / " + VideoController.g.getLengthInFrames());
        
        for(int j = 0;j < v.size(); j++){
            TextIntervalQuad t = v.elementAt(j);
            if (!t.jcFrameSChecked.isSelected())
                t.jsFrameS.setValue((jListImages.getSelectedIndex()+1));
            if (!t.jcFrameEChecked.isSelected())
                t.jsFrameE.setValue((jListImages.getSelectedIndex()+1));
        }
        
        if(!cbSTFrameStartFixed.isSelected())
            jsSTFrameStart.setValue((jListImages.getSelectedIndex()+1));
        
        jsFrameSlider.setValue(i);
    }
    
    void doLoadImage(int i){
        try{
            if (i < 0)
                return;
            gPanel.setImage(ImageIO.read(new File(ImageController.elementAt(i))));
            updateComponentsImageIndex(i);
            
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    void doAddStaticText(){
        String [] lines = jtpText.getText().split("\\n");
        TextLine [] tl  = new TextLine[lines.length];
        
        for(int i = 0; i < lines.length; i++){
            tl[i] = new TextLine(lines[i].replaceAll("\n", "").replaceAll("\r", ""), v2.elementAt(i).r);
        }
        
        Interval [] I = new Interval[v.size()];
        
        for(int i = 0; i < v.size(); i++){
            TextIntervalQuad t = v.elementAt(i);
            I[i] = new Interval(t.getFrameS(), t.getFrameE());
        }
        
        Position p = new Position(textBoxRect.x, textBoxRect.y,
            textBoxRect.width, textBoxRect.height);
        
        Content c = new Content(tl, ImageController.textC, ImageController.bgC, jcbOpaque.isSelected());
        XMLHandler.AddStaticText(I, p, c, 1);
        XMLHandler.save();
        
        // clear vectors
        v = null;
        v2 = null;
        
        DefaultTreeModel model = (DefaultTreeModel) jTree1.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        
        root = (DefaultMutableTreeNode) root.getChildAt(0);
        
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode();
        root.add(newNode);
        newNode.setUserObject("Text ID "+(XMLHandler.textBoxCount)+" Intervals = "+1);
        DefaultMutableTreeNode textNode = new DefaultMutableTreeNode();
        textNode.setUserObject("TextBox");
        for(int j = 0; j < tl.length; j++)
        {
            DefaultMutableTreeNode trapscription = new DefaultMutableTreeNode();
            trapscription.setUserObject(tl[j].transcription+" x = "+c.lines[j].r.x+" y = "+c.lines[j].r.y+" width = "+c.lines[j].r.width+" height = "+c.lines[j].r.height);
            textNode.add(trapscription);
        }
        
        newNode.add(textNode);
        
        for(int i = 0; i < I.length; i++){
            DefaultMutableTreeNode interv = new DefaultMutableTreeNode();
            DefaultMutableTreeNode id = new DefaultMutableTreeNode();
            DefaultMutableTreeNode fs = new DefaultMutableTreeNode();
            fs.setUserObject("Frame_S "+I[i].frameS);
            DefaultMutableTreeNode fe = new DefaultMutableTreeNode();
            fe.setUserObject("Frame_E "+I[i].frameE);
            interv.setUserObject("aInterval id "+(i+1));
            interv.add(id);
            interv.add(fs);
            interv.add(fe);
            newNode.add(interv);
        }

        /* Text Color */
        DefaultMutableTreeNode textColorNode = new DefaultMutableTreeNode();
        textColorNode.setUserObject("Text Color");
        /* this text bloc is just for variable visibility */
        {
            DefaultMutableTreeNode colorNodeR = new DefaultMutableTreeNode();
            DefaultMutableTreeNode colorNodeG = new DefaultMutableTreeNode();
            DefaultMutableTreeNode colorNodeB = new DefaultMutableTreeNode();
            colorNodeR.setUserObject("R: "+ImageController.textC.getRed());
            colorNodeG.setUserObject("G: "+ImageController.textC.getGreen());
            colorNodeB.setUserObject("B: "+ImageController.textC.getBlue());
            textColorNode.add(colorNodeR);
            textColorNode.add(colorNodeG);
            textColorNode.add(colorNodeB);
        }
        
        newNode.add(textColorNode);
        
        /* Background Color */
        DefaultMutableTreeNode bgColorNode = new DefaultMutableTreeNode();
        bgColorNode.setUserObject("BG Color");
        if (jcbOpaque.isSelected())
        {
            DefaultMutableTreeNode colorNodeR = new DefaultMutableTreeNode();
            DefaultMutableTreeNode colorNodeG = new DefaultMutableTreeNode();
            DefaultMutableTreeNode colorNodeB = new DefaultMutableTreeNode();
            colorNodeR.setUserObject("Red: "+ImageController.bgC.getRed());
            colorNodeG.setUserObject("Blue: "+ImageController.bgC.getBlue());
            colorNodeB.setUserObject("Green: "+ImageController.bgC.getGreen());
            bgColorNode.add(colorNodeR);
            bgColorNode.add(colorNodeG);
            bgColorNode.add(colorNodeB);
        }else{
            DefaultMutableTreeNode transparent = new DefaultMutableTreeNode();
            transparent.setUserObject("Transparent");
            bgColorNode.add(transparent);
        }
        
        newNode.add(bgColorNode);
        
        DefaultMutableTreeNode coordinatesNode = new DefaultMutableTreeNode();
        coordinatesNode.setUserObject("Coordinates");
        DefaultMutableTreeNode xNode = new DefaultMutableTreeNode();
        DefaultMutableTreeNode yNode = new DefaultMutableTreeNode();
        DefaultMutableTreeNode wNode = new DefaultMutableTreeNode();
        DefaultMutableTreeNode hNode = new DefaultMutableTreeNode();
        
        xNode.setUserObject("X: "+p.x);
        yNode.setUserObject("Y: "+p.y);
        wNode.setUserObject("W: "+p.w);
        hNode.setUserObject("H: "+p.h);
        
        coordinatesNode.add(xNode);
        coordinatesNode.add(yNode);
        coordinatesNode.add(wNode);
        coordinatesNode.add(hNode);
        
        newNode.add(coordinatesNode);
        
        model.reload(root);
    }
    
    void doAddScrollingText(){
        String trans = jtaScrollingTextValue.getText().replaceAll("\n", "");
        XMLHandler.addScrollingText((int)jsSTFrameStart.getValue(), 
                (int)jsOffset.getValue(), trans, LabelTranscript.dictWord(trans));
        
        XMLHandler.save();
        
        DefaultTreeModel model = (DefaultTreeModel) jTree1.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        
        root = (DefaultMutableTreeNode) root.getChildAt(1);
        DefaultMutableTreeNode st = new DefaultMutableTreeNode();
        st.setUserObject("Text ID "+(XMLHandler.tickerCount));
        root.add(st);
        
        DefaultMutableTreeNode txt = new DefaultMutableTreeNode();
        txt.setUserObject("Transcription "+(int)jsSTFrameStart.getValue());
        st.add(txt);
        
        DefaultMutableTreeNode fs = new DefaultMutableTreeNode();
        st.setUserObject("Frame Start "+(int)jsSTFrameStart.getValue());
        st.add(fs);
        
        DefaultMutableTreeNode offset = new DefaultMutableTreeNode();
        st.setUserObject("Offset "+(int)jsOffset.getValue());
        st.add(offset);
        
    }
    
    /* videos and frames stuff */
    
    void doLoadVideoFromFile(File file){

        controller = VIDEO;
        DefaultListModel tmp = (DefaultListModel)jListImages.getModel();
        tmp.clear();
        
        VideoController.init(file.getAbsolutePath());
        
        for(long i = 0; i < VideoController.g.getLengthInFrames(); i++){
            tmp.addElement("Frame "+(i+1));
        }
        jsFrameSlider.setMaximum(VideoController.g.getLengthInFrames());
        jsFrameSlider.setValue(0);
    }
    
    void doLoadVideo(){
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileFilter(new FileNameExtensionFilter("MP4 Videos", "mp4"));
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            doLoadVideoFromFile(chooser.getSelectedFile());
        }
    }
    void doLoadFrame(int i){
        /* can we load the frame ? */
        
        if (VideoController.g == null)
            return;
        
        if ((i < 0) || (i == VideoController.selectedIndex) || (i > VideoController.g.getLengthInFrames()))
            return;
              
        try {
            /* this idea gets less performant if the user skips more than 
            800 frames and it would be better to use setFrameNumber instead. */
            if(( i > VideoController.selectedIndex) && (i - VideoController.selectedIndex < 800))
                for(int j = VideoController.selectedIndex; j < i-1; j++){
                    VideoController.g.grabFrame();
                }
            else
                VideoController.g.setFrameNumber(i);
            
            VideoController.selectedIndex = i;
            gPanel.setImage(
                    VideoController.g.grab().getBufferedImage()
            );
        } catch (FrameGrabber.Exception ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        updateComponentsImageIndex(i-1);
    }
    
    void doSaveXML(){
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            XMLHandler.XMLFile = chooser.getSelectedFile().getAbsolutePath();
            XMLHandler.save();
        }
    }
    
    JPanel pnl;
    GPanel gPanel;
    JScrollPane jp;
    /**
     * @throws java.io.IOException
     */
    public MainFrame() {
        initComponents();
        DefaultTreeModel model = (DefaultTreeModel) jTree1.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.setUserObject("Video");
        
        DefaultMutableTreeNode staticNode = new DefaultMutableTreeNode();
        staticNode.setUserObject("Static Text");
        root.add(staticNode);
        
        DefaultMutableTreeNode scrollingNode = new DefaultMutableTreeNode();
        scrollingNode.setUserObject("Scrolling Text");
        root.add(scrollingNode);
        
        
        model.reload(root);
        pnl = new JPanel(new GridLayout(1, 1));
        gPanel = new GPanel(/*ImageIO.read(new File("logo.jpg"))*/null);
        JScrollPane scrollPanel; 
        
        scrollPanel = new JScrollPane(gPanel);
        scrollPanel.setAutoscrolls(true);
        pnl.add(scrollPanel);
        panelImage.setLayout(new GridLayout(1, 1));
        panelImage.add(pnl);
        
        jListImages.setModel(new DefaultListModel());
        
        btnFramePrev.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        btnFrameSkipB.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        btnFramePlay.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        btnFrameNext.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        btnFrameSkipF.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        btnCurrentFrame.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        
        btnZoomIn.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        btnZoomOut.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        btnZoomValue.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        
        
        btnCaptureText.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        btnCaptureTextColor.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        btnBGTextColor.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        btnAdd.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        
        btnAddImg.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        btnAddVideo.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        
        btnGoto.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        btnConf.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        
        jbtnHide.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        jbtnScrollingTextColor.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        btnAddScrollingText.putClientProperty( "JButton.buttonType", "segmentedTextured" );
        
        btnFramePrev.putClientProperty( "JButton.segmentPosition", "first" );
        btnFrameSkipB.putClientProperty( "JButton.segmentPosition", "middle" );
        btnFramePlay.putClientProperty( "JButton.segmentPosition", "middle" );
        btnCurrentFrame.putClientProperty( "JButton.segmentPosition", "middle" );
        btnFrameSkipF.putClientProperty( "JButton.segmentPosition", "middle" );
        btnFrameNext.putClientProperty( "JButton.segmentPosition", "last" );
        
        btnZoomIn.putClientProperty( "JButton.segmentPosition", "first" );
        btnZoomValue.putClientProperty( "JButton.segmentPosition", "middle" );
        btnZoomOut.putClientProperty( "JButton.segmentPosition", "last" );
        
        btnGoto.putClientProperty( "JButton.segmentPosition", "first" );
        btnConf.putClientProperty( "JButton.segmentPosition", "last" );
        
        btnAddImg.putClientProperty( "JButton.segmentPosition", "first" );
        btnAddVideo.putClientProperty( "JButton.segmentPosition", "last" );
        
        ImageController.startCapture = false;
        
        jtaScrollingTextValue.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        jtpText.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        
        jpIntervals.setLayout(new BoxLayout(jpIntervals, BoxLayout.Y_AXIS));
        jpLines.setLayout(new BoxLayout(jpLines, BoxLayout.Y_AXIS));
        jTabbedPane2.setEnabledAt(1, false);
        jTabbedPane2.setEnabledAt(2, false);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jMenuItem5 = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        ActionPanel = new javax.swing.JPanel();
        jtpTextPane = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jpTextProp = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel11 = new javax.swing.JPanel();
        btnCaptureText = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtpText = new javax.swing.JTextArea();
        jLabel24 = new javax.swing.JLabel();
        btnCaptureTextColor = new javax.swing.JButton();
        btnBGTextColor = new javax.swing.JButton();
        jcbOpaque = new javax.swing.JCheckBox();
        btnAdd = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jToolBar5 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btnAdd1 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jpIntervals = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jpLines = new javax.swing.JPanel();
        btnAdd2 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jsSTFrameStart = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        cbSTFrameStartFixed = new javax.swing.JCheckBox();
        btnAddScrollingText = new javax.swing.JButton();
        jsOffset = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jpScrollingTextProp = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jbtnScrollingTextColor = new javax.swing.JButton();
        jsBandX = new javax.swing.JSpinner();
        jsBandY = new javax.swing.JSpinner();
        jsBandH = new javax.swing.JSpinner();
        jsBandW = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jbtnHide = new javax.swing.JButton();
        jcbOpaqueScrollingTextBG = new javax.swing.JCheckBox();
        jScrollPane6 = new javax.swing.JScrollPane();
        jtaScrollingTextValue = new javax.swing.JTextArea();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jListImages = new javax.swing.JList();
        jToolBar2 = new javax.swing.JToolBar();
        btnAddImg = new javax.swing.JButton();
        btnAddVideo = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jToolBar1 = new javax.swing.JToolBar();
        btnFramePrev = new javax.swing.JButton();
        btnFrameSkipB = new javax.swing.JButton();
        btnCurrentFrame = new javax.swing.JButton();
        btnFramePlay = new javax.swing.JButton();
        btnFrameSkipF = new javax.swing.JButton();
        btnFrameNext = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnZoomIn = new javax.swing.JButton();
        btnZoomValue = new javax.swing.JButton();
        btnZoomOut = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JToolBar.Separator();
        btnGoto = new javax.swing.JButton();
        btnConf = new javax.swing.JButton();
        panelImage = new javax.swing.JPanel();
        jToolBar4 = new javax.swing.JToolBar();
        jlPoint1XY = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jlTextWidth = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jlTextHeight = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        jlFrameW = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jlFrameH = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JToolBar.Separator();
        jpColor = new javax.swing.JPanel();
        jlColor = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jsFrameSlider = new javax.swing.JSlider();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem6 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenu3 = new javax.swing.JMenu();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jMenuItem5.setText("jMenuItem5");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AcTiV - GT Software");
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

        jtpTextPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jtpTextPaneStateChanged(evt);
            }
        });

        jTabbedPane2.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        btnCaptureText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/shape_handles.png"))); // NOI18N
        btnCaptureText.setText("Capture Text");
        btnCaptureText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCaptureTextActionPerformed(evt);
            }
        });

        jtpText.setColumns(20);
        jtpText.setRows(5);
        jScrollPane1.setViewportView(jtpText);

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Colors");

        btnCaptureTextColor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/paintbrush.png"))); // NOI18N
        btnCaptureTextColor.setText("Text");
        btnCaptureTextColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCaptureTextColorActionPerformed(evt);
            }
        });

        btnBGTextColor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/color_swatch.png"))); // NOI18N
        btnBGTextColor.setText("Back G.");
        btnBGTextColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBGTextColorActionPerformed(evt);
            }
        });

        jcbOpaque.setSelected(true);
        jcbOpaque.setText("Opaque Background");
        jcbOpaque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbOpaqueActionPerformed(evt);
            }
        });

        btnAdd.setText("Next");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCaptureText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(btnCaptureTextColor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBGTextColor, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcbOpaque, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCaptureText, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnBGTextColor, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCaptureTextColor, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbOpaque)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Text", jPanel11);

        jToolBar5.setFloatable(false);
        jToolBar5.setRollover(true);

        jButton1.setText("Add");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar5.add(jButton1);

        jButton2.setText("Remove");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar5.add(jButton2);

        btnAdd1.setText("Next");
        btnAdd1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdd1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpIntervalsLayout = new javax.swing.GroupLayout(jpIntervals);
        jpIntervals.setLayout(jpIntervalsLayout);
        jpIntervalsLayout.setHorizontalGroup(
            jpIntervalsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 222, Short.MAX_VALUE)
        );
        jpIntervalsLayout.setVerticalGroup(
            jpIntervalsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 494, Short.MAX_VALUE)
        );

        jScrollPane5.setViewportView(jpIntervals);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane5)
                    .addComponent(btnAdd1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jToolBar5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAdd1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Intervals", jPanel12);

        javax.swing.GroupLayout jpLinesLayout = new javax.swing.GroupLayout(jpLines);
        jpLines.setLayout(jpLinesLayout);
        jpLinesLayout.setHorizontalGroup(
            jpLinesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 222, Short.MAX_VALUE)
        );
        jpLinesLayout.setVerticalGroup(
            jpLinesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 509, Short.MAX_VALUE)
        );

        jScrollPane7.setViewportView(jpLines);

        btnAdd2.setText("Next");
        btnAdd2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdd2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane7)
                    .addComponent(btnAdd2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAdd2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Lines", jPanel13);

        javax.swing.GroupLayout jpTextPropLayout = new javax.swing.GroupLayout(jpTextProp);
        jpTextProp.setLayout(jpTextPropLayout);
        jpTextPropLayout.setHorizontalGroup(
            jpTextPropLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        jpTextPropLayout.setVerticalGroup(
            jpTextPropLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jpTextProp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 13, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpTextProp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jtpTextPane.addTab("Static Text", jPanel5);

        jLabel4.setText("Frame Start :");

        cbSTFrameStartFixed.setText("Fixed");

        btnAddScrollingText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/tag.png"))); // NOI18N
        btnAddScrollingText.setText("Add");
        btnAddScrollingText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddScrollingTextActionPerformed(evt);
            }
        });

        jLabel5.setText("Offset :");

        jpScrollingTextProp.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Settings");

        jbtnScrollingTextColor.setText("Capture Text Color");
        jbtnScrollingTextColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnScrollingTextColorActionPerformed(evt);
            }
        });

        jLabel7.setText("X");

        jLabel8.setText("Y");

        jLabel9.setText("Width");

        jLabel10.setText("Height");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Band:");

        jbtnHide.setText("Hide");
        jbtnHide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnHideActionPerformed(evt);
            }
        });

        jcbOpaqueScrollingTextBG.setSelected(true);
        jcbOpaqueScrollingTextBG.setText("Opaque Background");

        javax.swing.GroupLayout jpScrollingTextPropLayout = new javax.swing.GroupLayout(jpScrollingTextProp);
        jpScrollingTextProp.setLayout(jpScrollingTextPropLayout);
        jpScrollingTextPropLayout.setHorizontalGroup(
            jpScrollingTextPropLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpScrollingTextPropLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpScrollingTextPropLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpScrollingTextPropLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jpScrollingTextPropLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpScrollingTextPropLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jsBandX, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jsBandY, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jpScrollingTextPropLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(7, 7, 7)
                                .addComponent(jsBandH))
                            .addGroup(jpScrollingTextPropLayout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jsBandW))))
                    .addGroup(jpScrollingTextPropLayout.createSequentialGroup()
                        .addComponent(jcbOpaqueScrollingTextBG, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnScrollingTextColor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jpScrollingTextPropLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnHide)))
                .addContainerGap())
        );
        jpScrollingTextPropLayout.setVerticalGroup(
            jpScrollingTextPropLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpScrollingTextPropLayout.createSequentialGroup()
                .addGroup(jpScrollingTextPropLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnHide))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnScrollingTextColor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbOpaqueScrollingTextBG, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpScrollingTextPropLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jsBandX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jsBandY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpScrollingTextPropLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jsBandW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpScrollingTextPropLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jsBandH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jtaScrollingTextValue.setColumns(20);
        jtaScrollingTextValue.setRows(5);
        jScrollPane6.setViewportView(jtaScrollingTextValue);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnAddScrollingText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbSTFrameStartFixed, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jsOffset, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jsSTFrameStart))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jpScrollingTextProp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane6)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbSTFrameStartFixed))
                .addGap(6, 6, 6)
                .addComponent(jsSTFrameStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jsOffset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpScrollingTextProp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddScrollingText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jtpTextPane.addTab("Scrolling Text", jPanel7);

        javax.swing.GroupLayout ActionPanelLayout = new javax.swing.GroupLayout(ActionPanel);
        ActionPanel.setLayout(ActionPanelLayout);
        ActionPanelLayout.setHorizontalGroup(
            ActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ActionPanelLayout.createSequentialGroup()
                .addComponent(jtpTextPane)
                .addContainerGap())
        );
        ActionPanelLayout.setVerticalGroup(
            ActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtpTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jListImages.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListImagesValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(jListImages);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        btnAddImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/image_add.png"))); // NOI18N
        btnAddImg.setToolTipText("Add Images");
        btnAddImg.setFocusable(false);
        btnAddImg.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAddImg.setMargin(new java.awt.Insets(1, 14, 2, 14));
        btnAddImg.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddImg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddImgActionPerformed(evt);
            }
        });
        jToolBar2.add(btnAddImg);

        btnAddVideo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/camera_add.png"))); // NOI18N
        btnAddVideo.setToolTipText("Add Video");
        btnAddVideo.setFocusable(false);
        btnAddVideo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAddVideo.setMargin(new java.awt.Insets(1, 14, 2, 14));
        btnAddVideo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddVideoActionPerformed(evt);
            }
        });
        jToolBar2.add(btnAddVideo);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Frames", jPanel1);

        jToolBar3.setRollover(true);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane2.setViewportView(jTree1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Transcriptions Labels", jPanel3);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnFramePrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/control_start_blue.png"))); // NOI18N
        btnFramePrev.setFocusable(false);
        btnFramePrev.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFramePrev.setMargin(new java.awt.Insets(1, 14, 2, 14));
        btnFramePrev.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFramePrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFramePrevActionPerformed(evt);
            }
        });
        jToolBar1.add(btnFramePrev);

        btnFrameSkipB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/control_rewind_blue.png"))); // NOI18N
        btnFrameSkipB.setFocusable(false);
        btnFrameSkipB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFrameSkipB.setMargin(new java.awt.Insets(1, 14, 2, 14));
        btnFrameSkipB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFrameSkipB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFrameSkipBActionPerformed(evt);
            }
        });
        jToolBar1.add(btnFrameSkipB);

        btnCurrentFrame.setText("0 / 0");
        btnCurrentFrame.setFocusable(false);
        btnCurrentFrame.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCurrentFrame.setMargin(new java.awt.Insets(1, 14, 2, 14));
        btnCurrentFrame.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnCurrentFrame);

        btnFramePlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/control_play_blue.png"))); // NOI18N
        btnFramePlay.setFocusable(false);
        btnFramePlay.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFramePlay.setMargin(new java.awt.Insets(1, 14, 2, 14));
        btnFramePlay.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFramePlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFramePlayActionPerformed(evt);
            }
        });
        jToolBar1.add(btnFramePlay);

        btnFrameSkipF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/control_fastforward_blue.png"))); // NOI18N
        btnFrameSkipF.setFocusable(false);
        btnFrameSkipF.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFrameSkipF.setMargin(new java.awt.Insets(1, 14, 2, 14));
        btnFrameSkipF.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFrameSkipF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFrameSkipFActionPerformed(evt);
            }
        });
        jToolBar1.add(btnFrameSkipF);

        btnFrameNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/control_end_blue.png"))); // NOI18N
        btnFrameNext.setFocusable(false);
        btnFrameNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFrameNext.setMargin(new java.awt.Insets(1, 14, 2, 14));
        btnFrameNext.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFrameNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFrameNextActionPerformed(evt);
            }
        });
        jToolBar1.add(btnFrameNext);

        jSeparator1.setMinimumSize(new java.awt.Dimension(50, 0));
        jToolBar1.add(jSeparator1);

        btnZoomIn.setText("+");
        btnZoomIn.setFocusable(false);
        btnZoomIn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnZoomIn.setMargin(new java.awt.Insets(1, 14, 2, 14));
        btnZoomIn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnZoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomInActionPerformed(evt);
            }
        });
        jToolBar1.add(btnZoomIn);

        btnZoomValue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/zoom.png"))); // NOI18N
        btnZoomValue.setText("100%");
        btnZoomValue.setFocusable(false);
        btnZoomValue.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnZoomValue.setMargin(new java.awt.Insets(1, 14, 2, 14));
        btnZoomValue.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnZoomValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomValueActionPerformed(evt);
            }
        });
        jToolBar1.add(btnZoomValue);

        btnZoomOut.setText("-");
        btnZoomOut.setFocusable(false);
        btnZoomOut.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnZoomOut.setMargin(new java.awt.Insets(1, 14, 2, 14));
        btnZoomOut.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnZoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomOutActionPerformed(evt);
            }
        });
        jToolBar1.add(btnZoomOut);
        jToolBar1.add(jSeparator11);

        btnGoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/arrow_turn_right.png"))); // NOI18N
        btnGoto.setFocusable(false);
        btnGoto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGoto.setMargin(new java.awt.Insets(1, 14, 2, 14));
        btnGoto.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGotoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnGoto);

        btnConf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/wrench.png"))); // NOI18N
        btnConf.setFocusable(false);
        btnConf.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnConf.setMargin(new java.awt.Insets(1, 14, 2, 14));
        btnConf.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnConf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfActionPerformed(evt);
            }
        });
        jToolBar1.add(btnConf);

        javax.swing.GroupLayout panelImageLayout = new javax.swing.GroupLayout(panelImage);
        panelImage.setLayout(panelImageLayout);
        panelImageLayout.setHorizontalGroup(
            panelImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelImageLayout.setVerticalGroup(
            panelImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);

        jlPoint1XY.setText("Point 1: ");
        jToolBar4.add(jlPoint1XY);
        jToolBar4.add(jSeparator2);

        jlTextWidth.setText("Width: ");
        jToolBar4.add(jlTextWidth);
        jToolBar4.add(jSeparator6);

        jlTextHeight.setText("Height:");
        jToolBar4.add(jlTextHeight);
        jToolBar4.add(jSeparator8);

        jlFrameW.setText("F. Width:");
        jToolBar4.add(jlFrameW);
        jToolBar4.add(jSeparator7);

        jlFrameH.setText("F. Height:");
        jToolBar4.add(jlFrameH);
        jToolBar4.add(jSeparator10);

        jlColor.setText("Color = (null)");

        javax.swing.GroupLayout jpColorLayout = new javax.swing.GroupLayout(jpColor);
        jpColor.setLayout(jpColorLayout);
        jpColorLayout.setHorizontalGroup(
            jpColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlColor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jpColorLayout.setVerticalGroup(
            jpColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jlColor, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
        );

        jToolBar4.add(jpColor);

        jsFrameSlider.setMinimum(1);
        jsFrameSlider.setMinorTickSpacing(5);
        jsFrameSlider.setPaintTicks(true);
        jsFrameSlider.setSnapToTicks(true);
        jsFrameSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jsFrameSliderMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jsFrameSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jsFrameSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jMenu1.setText("File");

        jMenuItem1.setText("Load Images");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem12.setText("Load Video");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem12);

        jMenuItem13.setText("Export Frames From Video");
        jMenu1.add(jMenuItem13);

        jMenuItem17.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem17.setText("Load XML");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem17);

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem11.setText("Save XML As");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem11);

        jMenuItem14.setText("Generate XML Frames");
        jMenuItem14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem14MouseClicked(evt);
            }
        });
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem14);

        jMenuItem15.setText("Generate TextBox Images");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem15);

        jMenuItem16.setText("Statistics");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem16);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenu5.setText("Frames");

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setText("Previous");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem7);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setText("Skip Few Previous");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem8);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem9.setText("Next");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem9);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem10.setText("Skip Few Next");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem10);

        jMenuBar1.add(jMenu5);

        jMenu4.setText("View");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Zoom In");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Zoom Out");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem3);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Reset");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem4);
        jMenu4.add(jSeparator4);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        jMenuItem6.setText("Hide Action Bar");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem6);
        jMenu4.add(jSeparator3);

        jMenuBar1.add(jMenu4);

        jMenu3.setText("About");
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jToolBar4, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ActionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ActionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(panelImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        
    }//GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        
    }//GEN-LAST:event_formMouseReleased

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        doLoadImages();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        doZoomIn();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void btnFramePrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFramePrevActionPerformed
        doPrevFrame();
    }//GEN-LAST:event_btnFramePrevActionPerformed

    private void btnFrameSkipBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFrameSkipBActionPerformed
        doPrevFewFrames();
    }//GEN-LAST:event_btnFrameSkipBActionPerformed

    private void btnFrameSkipFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFrameSkipFActionPerformed
        doNextFewFrames();
    }//GEN-LAST:event_btnFrameSkipFActionPerformed

    private void btnFrameNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFrameNextActionPerformed
        doNextFrame();
    }//GEN-LAST:event_btnFrameNextActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        doPrevFrame();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        doPrevFewFrames();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        doNextFrame();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        doNextFewFrames();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        doZoomOut();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        doZoomReset();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jListImagesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListImagesValueChanged
        /* if we are playing video then ignore the request */
        if (VideoController.playing)
            return;
        if (controller == IMAGE)
            doLoadImage(jListImages.getSelectedIndex());
        else
            doLoadFrame(jListImages.getSelectedIndex()+1);
        
        jListImages.ensureIndexIsVisible(jListImages.getSelectedIndex());
    }//GEN-LAST:event_jListImagesValueChanged

    private void btnAddImgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddImgActionPerformed
        doLoadImages();
    }//GEN-LAST:event_btnAddImgActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        doSaveXML();
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void btnZoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomOutActionPerformed
        doZoomOut();
    }//GEN-LAST:event_btnZoomOutActionPerformed

    private void btnZoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomInActionPerformed
        doZoomIn();
    }//GEN-LAST:event_btnZoomInActionPerformed

    private void btnZoomValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomValueActionPerformed
        doZoomReset();
    }//GEN-LAST:event_btnZoomValueActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        doLoadVideo();
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jsFrameSliderMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jsFrameSliderMouseReleased
        jListImages.setSelectedIndex(jsFrameSlider.getValue());
    }//GEN-LAST:event_jsFrameSliderMouseReleased

    private void btnFramePlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFramePlayActionPerformed
        if (VideoController.g == null)
            return;
        if (VideoController.playing)
        {
            btnFramePlay.setIcon(new ImageIcon("src\\resources\\control_play_blue.png"));
            VideoController.playing = false;
            jListImages.setSelectedIndex(VideoController.selectedIndex);
        }else{
            btnFramePlay.setIcon(new ImageIcon("src\\resources\\control_pause_blue.png"));
            VideoController.playing = true;
            videoThread = new VideoThread();
            videoThread.start();
        }
        
       
    }//GEN-LAST:event_btnFramePlayActionPerformed

    private void btnAddVideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddVideoActionPerformed
        doLoadVideo();
    }//GEN-LAST:event_btnAddVideoActionPerformed

    private void btnConfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfActionPerformed
        new OptionsFrame();
    }//GEN-LAST:event_btnConfActionPerformed

    private void btnGotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGotoActionPerformed
        int x = Integer.parseInt(JOptionPane.showInputDialog(null, null, "Enter frame number", JOptionPane.INFORMATION_MESSAGE));
        doLoadFrame(x);
        jListImages.ensureIndexIsVisible(x);
    }//GEN-LAST:event_btnGotoActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        jtpTextPane.setVisible(!jtpTextPane.isVisible());
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem14MouseClicked
        
    }//GEN-LAST:event_jMenuItem14MouseClicked

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        XMLHandler.doGenerateFrames();
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        XMLHandler.generateImgs();
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        Statistics.genStats();
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jtpTextPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jtpTextPaneStateChanged
        if (evt.getSource() instanceof JTabbedPane) {
            JTabbedPane pane = (JTabbedPane) evt.getSource();
            if (pane.getSelectedIndex() == 1)
            {
                ImageController.drawSquare  = false;
                ImageController.drawLine    = true;
            }else{
                ImageController.drawSquare  = true;
                ImageController.drawLine    = false;
            }
        }
    }//GEN-LAST:event_jtpTextPaneStateChanged

    private void jbtnHideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnHideActionPerformed
        if (!ImageController.scrollingTextColorPicked)
        JOptionPane.showMessageDialog(null, "Please select scrolling text color.");
        else{
            jpScrollingTextProp.setVisible(false);
            XMLHandler.setScrollingTextProperties(jcbOpaqueScrollingTextBG.isSelected(),
                (int)jsBandX.getValue(),
                (int)jsBandY.getValue(),
                (int)jsBandW.getValue(),
                (int)jsBandH.getValue());
        }
    }//GEN-LAST:event_jbtnHideActionPerformed

    private void jbtnScrollingTextColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnScrollingTextColorActionPerformed
        ImageController.startScrollTextColorCapture = true;
    }//GEN-LAST:event_jbtnScrollingTextColorActionPerformed

    private void btnAddScrollingTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddScrollingTextActionPerformed
        doAddScrollingText();
    }//GEN-LAST:event_btnAddScrollingTextActionPerformed

    private void btnAdd2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdd2ActionPerformed
        ImageController.drawBoxArea = false;

        for(int i = 0; i < v.size(); i++){
            jpIntervals.remove(v.elementAt(i));
        }
        for(int i = 0; i < v2.size(); i++){
            jpLines.remove(v2.elementAt(i));
        }
        doAddStaticText();
        jtpText.setText("");
        jTabbedPane2.setEnabledAt(0, true);
        jTabbedPane2.setSelectedIndex(0);
        jTabbedPane2.setEnabledAt(2, false);

        v  = new Vector();
        v2 = new Vector();

    }//GEN-LAST:event_btnAdd2ActionPerformed

    private void btnAdd1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdd1ActionPerformed
        boolean good = true;
        for(int i = 0; i < v.size(); i++){
            TextIntervalQuad t = v.elementAt(i);
            if (t.getFrameE() < t.getFrameS()){
                JOptionPane.showMessageDialog(null, "Invalid interval #"+(i+1));
                good = false;
            }
        }
        if(good){
            System.out.println(jtpText.getLineCount());
            v2 = new Vector();
            ImageController.drawBoxArea = true;
            if(jtpText.getLineCount() == 1){
                TextLineQuad tlq = new TextLineQuad();
                tlq.r = new Rectangle(textBoxRect);
                v2.add(tlq);
            }
            else
            for(int i = 0; i < jtpText.getLineCount(); i++){
                TextLineQuad t = new TextLineQuad();
                v2.add(t);
                jpLines.add(t);
                jpLines.repaint();
            }
        }
        jTabbedPane2.setEnabledAt(2, true);
        jTabbedPane2.setSelectedIndex(2);
        jTabbedPane2.setEnabledAt(1, false);
    }//GEN-LAST:event_btnAdd1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        TextIntervalQuad t = v.elementAt(v.size()-1);
        v.remove(t);
        jpIntervals.remove(t);

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        TextIntervalQuad t = new TextIntervalQuad();
        v.add(t);
        jpIntervals.add(t);
        jpIntervals.repaint();
        repaint();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if (ImageController.textC == null)
        JOptionPane.showMessageDialog(null, "Please select the text color.");
        else if ((ImageController.bgC == null) && (jcbOpaque.isSelected()))
        JOptionPane.showMessageDialog(null, "Please select the background color.");
        else if ("".equals(jtpText.getText()))
        JOptionPane.showMessageDialog(null, "Empty input.");
        else{
            // disable rect recording
            ImageController.setRectElement(null);
            ImageController.startCapture = false;
            ImageController.squareDrawed = false;
            ImageController.canDraw      = false;

            jTabbedPane2.setEnabledAt(1, true);
            jTabbedPane2.setSelectedIndex(1);
            jTabbedPane2.setEnabledAt(0, false);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void jcbOpaqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbOpaqueActionPerformed
        if (jcbOpaque.isSelected())
        btnBGTextColor.setEnabled(true);
        else
        btnBGTextColor.setEnabled(false);
    }//GEN-LAST:event_jcbOpaqueActionPerformed

    private void btnBGTextColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBGTextColorActionPerformed
        ImageController.startBGColorCapture = true;
    }//GEN-LAST:event_btnBGTextColorActionPerformed

    private void btnCaptureTextColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCaptureTextColorActionPerformed
        ImageController.startColorCapture = true;
    }//GEN-LAST:event_btnCaptureTextColorActionPerformed

    private void btnCaptureTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCaptureTextActionPerformed
        ImageController.drawSquare   = true;
        ImageController.startCapture =  true;
        ImageController.squareDrawed = false;
        ImageController.canDraw      = false;
        textBoxRect = new Rectangle();
        ImageController.setRectElement(textBoxRect);
    }//GEN-LAST:event_btnCaptureTextActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            XMLHandler.LoadXMLFile(chooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ActionPanel;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAdd1;
    private javax.swing.JButton btnAdd2;
    private javax.swing.JButton btnAddImg;
    private javax.swing.JButton btnAddScrollingText;
    private javax.swing.JButton btnAddVideo;
    private javax.swing.JButton btnBGTextColor;
    private javax.swing.JButton btnCaptureText;
    private javax.swing.JButton btnCaptureTextColor;
    private javax.swing.JButton btnConf;
    private javax.swing.JButton btnCurrentFrame;
    private javax.swing.JButton btnFrameNext;
    private javax.swing.JButton btnFramePlay;
    private javax.swing.JButton btnFramePrev;
    private javax.swing.JButton btnFrameSkipB;
    private javax.swing.JButton btnFrameSkipF;
    private javax.swing.JButton btnGoto;
    private javax.swing.JButton btnZoomIn;
    private javax.swing.JButton btnZoomOut;
    private javax.swing.JButton btnZoomValue;
    private javax.swing.JCheckBox cbSTFrameStartFixed;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jListImages;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator10;
    private javax.swing.JToolBar.Separator jSeparator11;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JTree jTree1;
    private javax.swing.JButton jbtnHide;
    private javax.swing.JButton jbtnScrollingTextColor;
    private javax.swing.JCheckBox jcbOpaque;
    private javax.swing.JCheckBox jcbOpaqueScrollingTextBG;
    private javax.swing.JLabel jlColor;
    private javax.swing.JLabel jlFrameH;
    private javax.swing.JLabel jlFrameW;
    private javax.swing.JLabel jlPoint1XY;
    private javax.swing.JLabel jlTextHeight;
    private javax.swing.JLabel jlTextWidth;
    private javax.swing.JPanel jpColor;
    private javax.swing.JPanel jpIntervals;
    private javax.swing.JPanel jpLines;
    private javax.swing.JPanel jpScrollingTextProp;
    private javax.swing.JPanel jpTextProp;
    private javax.swing.JSpinner jsBandH;
    private javax.swing.JSpinner jsBandW;
    private javax.swing.JSpinner jsBandX;
    private javax.swing.JSpinner jsBandY;
    private javax.swing.JSlider jsFrameSlider;
    private javax.swing.JSpinner jsOffset;
    private javax.swing.JSpinner jsSTFrameStart;
    private javax.swing.JTextArea jtaScrollingTextValue;
    private javax.swing.JTextArea jtpText;
    private javax.swing.JTabbedPane jtpTextPane;
    private javax.swing.JPanel panelImage;
    // End of variables declaration//GEN-END:variables
}
