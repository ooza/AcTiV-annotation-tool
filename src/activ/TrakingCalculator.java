/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package activ;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author ramii
 */
public class TrakingCalculator {
    
    static Hashtable TableDeChasse;
    
    public static void InitTable(){
        TableDeChasse = new Hashtable();
        TableDeChasse.put("SpaceError", 0); TableDeChasse.put("A", 26); TableDeChasse.put("C", 25); TableDeChasse.put("I", 8);
        TableDeChasse.put("Alif_I", 7); TableDeChasse.put("Alif_E", 8);
        TableDeChasse.put("Baa_I", 31); TableDeChasse.put("Baa_B", 18); TableDeChasse.put("Baa_M", 15); TableDeChasse.put("Baa_E", 31);
        TableDeChasse.put("Taaa_I", 31); TableDeChasse.put("Taaa_B", 20); TableDeChasse.put("Taaa_M", 21); TableDeChasse.put("Taaa_E", 30);
        TableDeChasse.put("TaaaClosed_I", 18);  TableDeChasse.put("TaaaClosed_E", 20);
        TableDeChasse.put("Thaa_I", 30); TableDeChasse.put("Thaa_B", 20); TableDeChasse.put("Thaa_M", 21); TableDeChasse.put("Thaa_E", 31);
        TableDeChasse.put("Jiim_I", 26); TableDeChasse.put("Jiim_B", 28); TableDeChasse.put("Jiim_M", 24); TableDeChasse.put("Jiim_E", 26);
        TableDeChasse.put("Haaa_I", 26); TableDeChasse.put("Haaa_B", 28); TableDeChasse.put("Haaa_M", 24); TableDeChasse.put("Haaa_E", 26);
        TableDeChasse.put("Xaa_I", 26); TableDeChasse.put("Xaa_B", 27); TableDeChasse.put("Xaa_M", 24); TableDeChasse.put("Xaa_E", 26);
        TableDeChasse.put("Daal_I", 17); TableDeChasse.put("Daal_E", 17);
        TableDeChasse.put("Thaal_I", 18); TableDeChasse.put("Thaal_E", 19);
        TableDeChasse.put("Raa_I", 15); TableDeChasse.put("Raa_E", 15);
        TableDeChasse.put("Zaay_I", 15); TableDeChasse.put("Zaay_E", 15);
        TableDeChasse.put("Siin_I", 45); TableDeChasse.put("Siin_B", 31); TableDeChasse.put("Siin_M", 33); TableDeChasse.put("Siin_E", 45);
        TableDeChasse.put("Shiin_I", 45); TableDeChasse.put("Shiin_B", 31); TableDeChasse.put("Shiin_M", 32); TableDeChasse.put("Shiin_E", 45);
        TableDeChasse.put("Saad_I", 48); TableDeChasse.put("Saad_B", 35); TableDeChasse.put("Saad_M", 36); TableDeChasse.put("Saad_E", 48);
        TableDeChasse.put("Daad_I", 48); TableDeChasse.put("Daad_B", 35); TableDeChasse.put("Daad_M", 37); TableDeChasse.put("Daad_E", 48);
        TableDeChasse.put("Thaaa_I", 31); TableDeChasse.put("Thaaa_B", 31); TableDeChasse.put("Thaaa_M", 30); TableDeChasse.put("Thaaa_E", 32);
        TableDeChasse.put("Taa_I", 15); TableDeChasse.put("Taa_B", 32); TableDeChasse.put("Taa_M", 33);  TableDeChasse.put("Taa_E", 15);
        TableDeChasse.put("Ayn_I", 26); TableDeChasse.put("Ayn_B", 28); TableDeChasse.put("Ayn_M", 28); TableDeChasse.put("Ayn_E", 26);
        TableDeChasse.put("Ghayn_I", 13); TableDeChasse.put("Ghayn_B", 28); TableDeChasse.put("Ghayn_M", 28); TableDeChasse.put("Ghayn_E", 26);
        TableDeChasse.put("Faa_I", 34); TableDeChasse.put("Faa_B", 24); TableDeChasse.put("Faa_M", 26); TableDeChasse.put("Faa_E", 35);
        TableDeChasse.put("Gaaf_I", 26); TableDeChasse.put("Gaaf_B", 25); TableDeChasse.put("Gaaf_M", 26); TableDeChasse.put("Gaaf_E", 26);
        TableDeChasse.put("Kaaf_I", 31); TableDeChasse.put("Kaaf_B", 22); TableDeChasse.put("Kaaf_M", 22); TableDeChasse.put("Kaaf_E", 32);
        TableDeChasse.put("Laam_I", 24); TableDeChasse.put("Laam_B",12); TableDeChasse.put("Laam_M", 13); TableDeChasse.put("Laam_E", 25);
        TableDeChasse.put("Miim_I", 23); TableDeChasse.put("Miim_B", 22); TableDeChasse.put("Miim_M", 21); TableDeChasse.put("Miim_E", 23);
        TableDeChasse.put("Nuun_I", 26); TableDeChasse.put("Nuun_B", 18); TableDeChasse.put("Nuun_M", 15); TableDeChasse.put("Nuun_E", 26);
        TableDeChasse.put("Haa_I", 17); TableDeChasse.put("Haa_B", 30); TableDeChasse.put("Haa_M", 21); TableDeChasse.put("Haa_E", 19);
        TableDeChasse.put("Waaw_I", 16); TableDeChasse.put("Waaw_E", 16);
        TableDeChasse.put("Yaa_I", 25); TableDeChasse.put("Yaa_B", 19); TableDeChasse.put("Yaa_M", 19); TableDeChasse.put("Yaa_E", 24);
        TableDeChasse.put("Hamza", 16); 
        TableDeChasse.put("HamzaAboveAlif_I", 14); TableDeChasse.put("HamzaAboveAlif_E", 14);
        TableDeChasse.put("AlifBroken_I", 25); TableDeChasse.put("AlifBroken_E", 25);
        
        TableDeChasse.put("HamzaAboveAlifBroken_I", 13); TableDeChasse.put("HamzaAboveAlifBroken_B", 17); TableDeChasse.put("HamzaAboveAlifBroken_M", 17);TableDeChasse.put("HamzaAboveAlifBroken_E", 13);
        TableDeChasse.put("TildAboveAlif_I",16); TableDeChasse.put("TildAboveAlif_E",16);
         
        TableDeChasse.put("HamzaAboveWaaw_E", 11); TableDeChasse.put("HamzaAboveWaaw_I",11); 
        TableDeChasse.put("HamzaUnderAlif_I", 13); TableDeChasse.put("HamzaUnderAlif_E", 13);
        /* le cas de LaamAlif */
        TableDeChasse.put("Laam_EAlif_E", 20);
        TableDeChasse.put("Laam_EHamzaAboveAlif_E", 21); TableDeChasse.put("Laam_IHamzaAboveAlif_I", 21);
        TableDeChasse.put("Laam_EHamzaUnderAlif_E", 21); TableDeChasse.put("Laam_IHamzaUnderAlif_I", 21); 

        TableDeChasse.put("Laam_ETildAboveAlif_E", 22); 
        
        /* Chasse des chiffres*/
        TableDeChasse.put("Digit_0", 21); TableDeChasse.put("Digit_1", 15); TableDeChasse.put("Digit_2", 21); TableDeChasse.put("Digit_3", 21); TableDeChasse.put("Digit_4", 21); TableDeChasse.put("Digit_5", 21);
        TableDeChasse.put("Digit_6", 20);TableDeChasse.put("Digit_7", 21);TableDeChasse.put("Digit_8", 21);TableDeChasse.put("Digit_9", 21);
        
        /* Chasse des ponctuations,   TODO Space_IL! */
        TableDeChasse.put("Point", 8); TableDeChasse.put("Colon", 8); TableDeChasse.put("Comma", 4); TableDeChasse.put("Slash", 12); TableDeChasse.put("Quote", 6); TableDeChasse.put("Space", 12);
        
    }
    
    public static int trackinit(String str)
    {
        //System.out.println("str = "+str);
        if(str=="")
            return 0;
        InitTable();
        Set ensemble = TableDeChasse.entrySet();
        Iterator it = ensemble.iterator(); 
        
        Set hashtableKeys = TableDeChasse.keySet();
        Iterator K=hashtableKeys.iterator();
        Collection hashtableValues = TableDeChasse.values();
        Iterator V=hashtableValues.iterator();
        Integer i=1;
        Integer I = null;
        
        while (K.hasNext())
        {
            Object ke=K.next();
            Object val=V.next();
            String ch= (String) ke;
            if(str.equals(ch))
                I =(Integer)TableDeChasse.get(ch);
       }
       System.out.println("Returning "+I);
       return I;
    }
    
    public static String visibleText(String text, int BF, int textOffset, int CF)
    {
        String[] textArray = text.split(" ");   
        int textbandWidth = 1433;
        double pixelsPerFrame = 6.77;
        int TWB = (int)(((CF-BF)*pixelsPerFrame)+ (textOffset - textbandWidth)); //TWE: visible Text window end, TWB: Text window Beginning
        if (TWB < 0) {
            TWB = 0;
        }
        int TWE = (int)((CF-BF)*pixelsPerFrame) + textOffset;
        int cursor =0;
        String outputText = "";
        for (int i=0; i < textArray.length; i++) {
            /*System.out.println("Cursor = "+cursor);
            System.out.println("TWE = "+TWE);
            System.out.println("TWB = "+TWB);
            System.out.println("Output = "+outputText);*/
            if (cursor >= TWB) {
                cursor += trackinit(textArray[i]);
                if (cursor > TWE) {
                    break;
                }
                outputText += textArray[i].concat(" ");
            } else {
                cursor += trackinit(textArray[i]);
            }
        }
        //System.out.println("Current Frame: "+CF);
        return outputText;
    }
   public static int tichkerEF(String ch, int BF, int textOffset)
   {
       String[] splitArray = ch.split(" ");
       int EfNum,S=0;
       int textbandWidth = 1433;
       double pixelsPerFrame = 6.770;
       for (int i = 0; i < splitArray.length; i++) {
            S+=trackinit(splitArray[i]);
       }
       EfNum=(int)(((S+textbandWidth)-textOffset)/pixelsPerFrame)+1;
       return EfNum+BF;
   }
      public static void tichkerBF(String text, int[] textInfo)
   {
       String[] splitArray = text.split(" ");
       int VLposition = 220;
       double PPF = 6.77;
       int RF = textInfo[0];
       int offset = textInfo[1];
       int textposition = VLposition + offset;
       textInfo[0] = RF - (int)(textposition /PPF);
       textInfo[1]=(int)(textposition %PPF);
   }
}
