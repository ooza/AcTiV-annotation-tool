
package activ;

import activ.XMLHandler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
        
public class AcTiV {
    public static void main(String[] args) {
        // Show home frame
        /*
        try {
            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
            //UIManager.getSystemLookAndFeelClassName()
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println("Error setting Liquid LAF: " + e);
        }
                */
        
        
         /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                XMLHandler.InitXMLHandler(1, "Aljazeera", "1080p", "00:04:51", 20, 1200);
                try {
                    new MainFrame().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(AcTiV.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
