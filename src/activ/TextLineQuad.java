package activ;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

class MouseEvt implements MouseListener{
    Rectangle r;
    public MouseEvt(Rectangle r){
        this.r = r;
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
        ImageController.drawSquare   = true;
        ImageController.startCapture =  true;
        ImageController.squareDrawed = false;
        ImageController.canDraw      = false;
        ImageController.setRectElement(r);
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
    
}

public class TextLineQuad  extends JPanel{
    public JLabel pos;
    public JLabel dim;
    public JButton btn;
    static int nb = 1;
    public Rectangle r = null;
    
    public TextLineQuad(){
        super();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Text Line"+nb++));
        btn = new JButton("Update Coordinates");
        r = new Rectangle();
        btn.addMouseListener(new MouseEvt(r));
        add(btn);
    }
}
