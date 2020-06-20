
package activ;

import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

/**
 *
 * @author ramii
 */
public class TextIntervalQuad  extends JPanel{
    public JSpinner jsFrameS;
    public JSpinner jsFrameE;
    public JCheckBox jcFrameSChecked;
    public JCheckBox jcFrameEChecked;
    static int nb = 1;
    
    public TextIntervalQuad(){
        super();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Interval "+nb++));
        
        JPanel jp1 = new JPanel();
        jp1.setLayout(new GridLayout(1, 2));
        
        jsFrameS = new JSpinner();
        jp1.add(jsFrameS);
        jcFrameSChecked = new JCheckBox("Fixed");
        jp1.add(jcFrameSChecked);
        
        JPanel jp2 = new JPanel();
        jp2.setLayout(new GridLayout(1, 2));
        jsFrameE = new JSpinner();
        jp2.add(jsFrameE);
        
        jcFrameEChecked = new JCheckBox("Fixed");
        jp2.add(jcFrameEChecked);
        
        add(jp1);
        add(jp2);
    }
    
    public int getFrameS(){
        return (int)jsFrameS.getValue();
    }
    
    public int getFrameE(){
        return (int)jsFrameE.getValue();
    }
}
