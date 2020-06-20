package activ;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package stage;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author ZayeneO
 */
public class LabelTranscript {

    public static String falseCarac = "";
    public static String falseCaracAfter = "";
    public static String ltModified = "";
    public static String[] LETTERSIsolee = {"ا", "ب", "ت", "ث", "ج", "ح", "خ", "د", "ذ", "ر", "ز", "س", "ش", "ص", "ض", "ط", "ظ", "ع",
        "غ", "ف", "ق", "ك", "ل", "م", "ن", "ه", "و", "ي", "ء", "آ", "أ", "إ", "ة", "ؤ", "ئ", "ى", "ّ", " ", "0", "1", "2", "3", "4",
        "5", "6", "7", "8", "9", ".", ":", "،", "/", "%", "?", "!", "\"","-", "(",")",""};
    static String nom_lettre[] = {"Alif", "Baa", "Taaa", "Thaa", "Jiim", "Haaa", "Xaa", "Daal", "Thaal", "Raa", "Zaay", "Siin", "Shiin", "Saad", "Daad", "Thaaa", "Taa",
        "Ayn", "Ghayn", "Faa", "Gaaf", "Kaaf", "Laam", "Miim", "Nuun", "Haa", "Waaw", "Yaa", "Hamza", "TildAboveAlif", "HamzaAboveAlif", "HamzaUnderAlif",
        "TaaaClosed", "HamzaAboveWaaw", "HamzaAboveAlifBroken", "AlifBroken", "Chadda", "Space", "Digit_0", "Digit_1", "Digit_2", "Digit_3", "Digit_4",
        "Digit_5", "Digit_6", "Digit_7", "Digit_8", "Digit_9", "Point", "Colon", "Comma", "Slash", "Percent", "QuestionMark", "ExclamationMark", "Quote", "Hyphen","ParenthesisO", "ParenthesisC", "SpaceError"};

    public static String dictWord(String Word) {
        String ligne = "";
        StringBuffer s = new StringBuffer();

        int ind = -1;

        ligne = Word;
        String c = null;

        String t = Word;

        if (t.contains("�?".subSequence(0, "�?".length()))) {
            t = t.replace("�?".subSequence(0, "�?".length()), "ف".subSequence(0, "ف".length()));
        }
        for (int j = 0; j < t.length(); j++) {

            c = String.valueOf(t.charAt(j));

            //  StringTokenizer st = new StringTokenizer(t); 
            //  t=st.nextToken();
            ind = ARRAY.find2(LETTERSIsolee, c);
            if (ind != -1) {
                if (j == 0) {
                    if ((String.valueOf(t.charAt(j)).equals("ا")) || (String.valueOf(t.charAt(j)).equals("د")) || (String.valueOf(t.charAt(j)).equals("ذ"))
                            || (String.valueOf(t.charAt(j)).equals("ر")) || (String.valueOf(t.charAt(j)).equals("ز")) || (String.valueOf(t.charAt(j)).equals("و"))
                            || (String.valueOf(t.charAt(j)).equals("آ")) || (String.valueOf(t.charAt(j)).equals("أ")) || (String.valueOf(t.charAt(j)).equals("إ"))) {
                        s.append(" ").append(nom_lettre[ind]).append("_I");
                    } else if ((String.valueOf(t.charAt(j)).equals("0")) || (String.valueOf(t.charAt(j)).equals("1")) || (String.valueOf(t.charAt(j)).equals("2")) || (String.valueOf(t.charAt(j)).equals("3"))
                            || (String.valueOf(t.charAt(j)).equals("4")) || (String.valueOf(t.charAt(j)).equals("5")) || (String.valueOf(t.charAt(j)).equals("6")) || (String.valueOf(t.charAt(j)).equals("7"))
                            || (String.valueOf(t.charAt(j)).equals("8")) || (String.valueOf(t.charAt(j)).equals("9")) || (String.valueOf(t.charAt(j)).equals(".")) || (String.valueOf(t.charAt(j)).equals(":"))
                            || (String.valueOf(t.charAt(j)).equals("،")) || (String.valueOf(t.charAt(j)).equals("/")) || (String.valueOf(t.charAt(j)).equals("%")) || (String.valueOf(t.charAt(j)).equals("?"))
                            || (String.valueOf(t.charAt(j)).equals("!")) || (String.valueOf(t.charAt(j)).equals("\""))|| (String.valueOf(t.charAt(j)).equals("-"))|| (String.valueOf(t.charAt(j)).equals("("))
                            || (String.valueOf(t.charAt(j)).equals(")"))) {
                        s.append(" ").append(nom_lettre[ind]);
                    } else {
                        s.append(" ").append(nom_lettre[ind]).append("_B");
                    }
                } else if ((j > 0) && (j < t.length() - 1)) {

                    if ((String.valueOf(t.charAt(j)).equals(" "))) {
                        s.append(" ").append(nom_lettre[ind]);
                        //System.out.print(s.charAt(s.length()-1));
                    } /*
                     else if(((String.valueOf(t.charAt(j)).equals("ب"))||(String.valueOf(t.charAt(j)).equals("ت"))||(String.valueOf(t.charAt(j)).equals("ث"))
                     ||(String.valueOf(t.charAt(j)).equals("ج"))||(String.valueOf(t.charAt(j)).equals("ح"))||(String.valueOf(t.charAt(j)).equals("خ"))
                     ||(String.valueOf(t.charAt(j)).equals("س"))||(String.valueOf(t.charAt(j)).equals("ش"))||(String.valueOf(t.charAt(j)).equals("ص"))
                     ||(String.valueOf(t.charAt(j)).equals("ض"))||(String.valueOf(t.charAt(j)).equals("ط"))||(String.valueOf(t.charAt(j)).equals("ظ"))
                     ||(String.valueOf(t.charAt(j)).equals("ع"))||(String.valueOf(t.charAt(j)).equals("غ"))||(String.valueOf(t.charAt(j)).equals("ف"))
                     ||(String.valueOf(t.charAt(j)).equals("ق"))||(String.valueOf(t.charAt(j)).equals("ك"))||(String.valueOf(t.charAt(j)).equals("ل"))
                     ||(String.valueOf(t.charAt(j)).equals("م"))||(String.valueOf(t.charAt(j)).equals("ن"))||(String.valueOf(t.charAt(j)).equals("ه"))
                     ||(String.valueOf(t.charAt(j)).equals("ي"))||(String.valueOf(t.charAt(j)).equals("ئ")))&&(String.valueOf(t.charAt(j-1)).equals(" ")))
                     {
                          
                     // String newsuffix= String.valueOf(s.charAt(s.length()-1)).replace("M", "B");
                     //s=s.replace(s.charAt(s.length()-2), s.charAt(s.length()-1), "_B");
                     //System.out.print(s.toString());  
                                                  
                     }
                     */ else if ((String.valueOf(t.charAt(j)).equals("ّ"))) {

                        if (String.valueOf(s.charAt(s.length() - 1)).equals("E")) {
                            s = s.delete(s.length() - 2, s.length());
                            s.append("Chadda_E");
                        } else if (String.valueOf(s.charAt(s.length() - 1)).equals("M")) {
                            s = s.delete(s.length() - 2, s.length());
                            s.append("Chadda_M");
                        } else if (String.valueOf(s.charAt(s.length() - 1)).equals("B")) {
                            s = s.delete(s.length() - 2, s.length());
                            s.append("Chadda_B");
                        } else if (String.valueOf(s.charAt(s.length() - 1)).equals("I")) {
                            s = s.delete(s.length() - 2, s.length());
                            s.append("Chadda_I");
                        }
                    } else if ((String.valueOf(t.charAt(j)).equals("0")) || (String.valueOf(t.charAt(j)).equals("1")) || (String.valueOf(t.charAt(j)).equals("2")) || (String.valueOf(t.charAt(j)).equals("3"))
                            || (String.valueOf(t.charAt(j)).equals("4")) || (String.valueOf(t.charAt(j)).equals("5")) || (String.valueOf(t.charAt(j)).equals("6")) || (String.valueOf(t.charAt(j)).equals("7"))
                            || (String.valueOf(t.charAt(j)).equals("8")) || (String.valueOf(t.charAt(j)).equals("9")) || (String.valueOf(t.charAt(j)).equals(".")) || (String.valueOf(t.charAt(j)).equals(":"))
                            || (String.valueOf(t.charAt(j)).equals("،")) || (String.valueOf(t.charAt(j)).equals("/")) || (String.valueOf(t.charAt(j)).equals("%")) || (String.valueOf(t.charAt(j)).equals("?"))
                            || (String.valueOf(t.charAt(j)).equals("!")) || (String.valueOf(t.charAt(j)).equals("\""))|| (String.valueOf(t.charAt(j)).equals("-"))
                            || (String.valueOf(t.charAt(j)).equals("("))|| (String.valueOf(t.charAt(j)).equals(")"))) {
                        s.append(" ").append(nom_lettre[ind]);
                    } else if (String.valueOf(t.charAt(j)).equals("ء")) {
                        s.append(" ").append(nom_lettre[ind]);
                    } else if (!(String.valueOf(t.charAt(j))).equals("ء") && !(String.valueOf(t.charAt(j))).equals("0") && !(String.valueOf(t.charAt(j))).equals("1") && !(String.valueOf(t.charAt(j))).equals("2")
                            && !(String.valueOf(t.charAt(j))).equals("3") && !(String.valueOf(t.charAt(j))).equals("4") && !(String.valueOf(t.charAt(j))).equals("5") && !(String.valueOf(t.charAt(j))).equals("6")
                            && !(String.valueOf(t.charAt(j))).equals("7") && !(String.valueOf(t.charAt(j))).equals("8") && !(String.valueOf(t.charAt(j))).equals("9") && !(String.valueOf(t.charAt(j))).equals(" ")
                            && !(String.valueOf(t.charAt(j))).equals(".") && !(String.valueOf(t.charAt(j))).equals(":") && !(String.valueOf(t.charAt(j))).equals("،") && !(String.valueOf(t.charAt(j))).equals("/")
                            && !(String.valueOf(t.charAt(j))).equals("%") && !(String.valueOf(t.charAt(j))).equals("?") && !(String.valueOf(t.charAt(j))).equals("!") && !(String.valueOf(t.charAt(j))).equals("\"")
                            && !(String.valueOf(t.charAt(j))).equals("-")&& !(String.valueOf(t.charAt(j))).equals("(")&& !(String.valueOf(t.charAt(j))).equals(")")) {

                        if ((String.valueOf(t.charAt(j)).equals("ا")) && (String.valueOf(t.charAt(j - 1)).equals("ل"))) { //String a=String.valueOf(s.charAt(s.length()-1)PrtO);
                            //System.out.print(s.toString());
                            s = s.delete(s.length() - 7, s.length());
                            s.append(" ").append("Laam_EAlif_E");
                        } else if ((String.valueOf(t.charAt(j)).equals("أ")) && (String.valueOf(t.charAt(j - 1)).equals("ل"))) { //String a=String.valueOf(s.charAt(s.length()-1));

                            s = s.delete(s.length() - 7, s.length());
                            s.append(" ").append("Laam_EHamzaAboveAlif_E");

                        } else if ((String.valueOf(t.charAt(j)).equals("إ")) && (String.valueOf(t.charAt(j - 1)).equals("ل"))) { //String a=String.valueOf(s.charAt(s.length()-1));

                            s = s.delete(s.length() - 7, s.length());

                            s.append(" ").append("Laam_EHamzaUnderAlif_E");
                        } else if ((String.valueOf(t.charAt(j)).equals("آ")) && (String.valueOf(t.charAt(j - 1)).equals("ل"))) { //String a=String.valueOf(s.charAt(s.length()-1));

                            s = s.delete(s.length() - 7, s.length());
                            s.append(" ").append("Laam_ETildAboveAlif_E");
                        } else if ((((String.valueOf(t.charAt(j)).equals("ا")) || (String.valueOf(t.charAt(j)).equals("ى")) || (String.valueOf(t.charAt(j)).equals("د")) || (String.valueOf(t.charAt(j)).equals("ذ"))
                                || (String.valueOf(t.charAt(j)).equals("ر")) || (String.valueOf(t.charAt(j)).equals("ز")) || (String.valueOf(t.charAt(j)).equals("و"))
                                || (String.valueOf(t.charAt(j)).equals("آ")) || (String.valueOf(t.charAt(j)).equals("أ")) || (String.valueOf(t.charAt(j)).equals("إ"))
                                || (String.valueOf(t.charAt(j)).equals("ؤ")))) && ((String.valueOf(t.charAt(j - 1)).equals("ا")) || (String.valueOf(t.charAt(j - 1)).equals("د")) || (String.valueOf(t.charAt(j - 1)).equals("ذ"))
                                || (String.valueOf(t.charAt(j - 1)).equals("ر")) || (String.valueOf(t.charAt(j - 1)).equals("ز")) || (String.valueOf(t.charAt(j - 1)).equals("و"))
                                || (String.valueOf(t.charAt(j - 1)).equals("آ")) || (String.valueOf(t.charAt(j - 1)).equals("ّ")) || (String.valueOf(t.charAt(j - 1)).equals("أ")) || (String.valueOf(t.charAt(j - 1)).equals("إ"))
                                || (String.valueOf(t.charAt(j - 1)).equals("ؤ"))) && (!String.valueOf(t.charAt(j - 1)).equals("ل"))) {
                            s.append(" ").append(nom_lettre[ind]).append("_I");
                        } else if (((((!String.valueOf(t.charAt(j)).equals("ا")) || (!String.valueOf(t.charAt(j)).equals("د")) || (!String.valueOf(t.charAt(j)).equals("ذ"))
                                || (!String.valueOf(t.charAt(j)).equals("ر")) || (!String.valueOf(t.charAt(j)).equals("ز")) || (!String.valueOf(t.charAt(j)).equals("و"))
                                || (!String.valueOf(t.charAt(j)).equals("آ")) || (!String.valueOf(t.charAt(j)).equals("أ")) || (!String.valueOf(t.charAt(j)).equals("إ"))
                                || (!String.valueOf(t.charAt(j)).equals("ؤ"))))) && ((String.valueOf(t.charAt(j - 1)).equals("ا")) || (String.valueOf(t.charAt(j - 1)).equals("د")) || (String.valueOf(t.charAt(j - 1)).equals("ذ"))
                                || (String.valueOf(t.charAt(j - 1)).equals("ر")) || (String.valueOf(t.charAt(j - 1)).equals("ز")) || (String.valueOf(t.charAt(j - 1)).equals("و"))
                                || (String.valueOf(t.charAt(j - 1)).equals("آ")) || (String.valueOf(t.charAt(j - 1)).equals("أ")) || (String.valueOf(t.charAt(j - 1)).equals("إ"))
                                || (String.valueOf(t.charAt(j - 1)).equals("ؤ")) || (String.valueOf(t.charAt(j - 1)).equals("ّ") && String.valueOf(t.charAt(j - 2)).equals("ر")) || (String.valueOf(t.charAt(j - 1)).equals("ّ") && String.valueOf(t.charAt(j - 2)).equals("ز")))) {
                            s.append(" ").append(nom_lettre[ind]).append("_B");
                        } else if (((((String.valueOf(t.charAt(j)).equals("ا")) || (String.valueOf(t.charAt(j)).equals("ى")) || (String.valueOf(t.charAt(j)).equals("د")) || (String.valueOf(t.charAt(j)).equals("ذ"))
                                || (String.valueOf(t.charAt(j)).equals("ر")) || (String.valueOf(t.charAt(j)).equals("ز")) || (String.valueOf(t.charAt(j)).equals("و"))
                                || (String.valueOf(t.charAt(j)).equals("آ")) || (String.valueOf(t.charAt(j)).equals("أ")) || (String.valueOf(t.charAt(j)).equals("إ"))
                                || (String.valueOf(t.charAt(j)).equals("ؤ"))))) && ((!String.valueOf(t.charAt(j - 1)).equals("ا")) || (!String.valueOf(t.charAt(j - 1)).equals("د")) || (!String.valueOf(t.charAt(j - 1)).equals("ذ"))
                                || (!String.valueOf(t.charAt(j - 1)).equals("ر")) || (!String.valueOf(t.charAt(j - 1)).equals("ز")) || (!String.valueOf(t.charAt(j - 1)).equals("و"))
                                || (!String.valueOf(t.charAt(j - 1)).equals("آ")) || (!String.valueOf(t.charAt(j - 1)).equals("أ")) || (!String.valueOf(t.charAt(j - 1)).equals("إ"))
                                || (!String.valueOf(t.charAt(j - 1)).equals("ؤ")) || (!String.valueOf(t.charAt(j - 1)).equals("ى")) || (!String.valueOf(t.charAt(j - 1)).equals("ّ")))) {
                            s.append(" ").append(nom_lettre[ind]).append("_E");
                        } else if (((((!String.valueOf(t.charAt(j)).equals("ا")) || (!String.valueOf(t.charAt(j)).equals("ى")) || (!String.valueOf(t.charAt(j)).equals("د")) || (!String.valueOf(t.charAt(j)).equals("ذ"))
                                || (!String.valueOf(t.charAt(j)).equals("ر")) || (!String.valueOf(t.charAt(j)).equals("ز")) || (!String.valueOf(t.charAt(j)).equals("و"))
                                || (!String.valueOf(t.charAt(j)).equals("آ")) || (!String.valueOf(t.charAt(j)).equals("أ")) || (!String.valueOf(t.charAt(j)).equals("إ"))
                                || (!String.valueOf(t.charAt(j)).equals("ؤ"))))) && ((!String.valueOf(t.charAt(j - 1)).equals("ا")) || (!String.valueOf(t.charAt(j - 1)).equals("د")) || (!String.valueOf(t.charAt(j - 1)).equals("ذ"))
                                || (!String.valueOf(t.charAt(j - 1)).equals("ر")) || (!String.valueOf(t.charAt(j - 1)).equals("ز")) || (!String.valueOf(t.charAt(j - 1)).equals("و"))
                                || (!String.valueOf(t.charAt(j - 1)).equals("آ")) || (!String.valueOf(t.charAt(j - 1)).equals("أ")) || (!String.valueOf(t.charAt(j - 1)).equals("إ"))
                                || (!String.valueOf(t.charAt(j - 1)).equals("ؤ")) || (!String.valueOf(t.charAt(j - 1)).equals("ى")))) {
                            s.append(" ").append(nom_lettre[ind]).append("_M");
                        }

                    }
                } else if (j == t.length() - 1) {
                    /*Chadda labels*/
                    if ((String.valueOf(t.charAt(j)).equals("ّ"))) {

                        if (String.valueOf(s.charAt(s.length() - 1)).equals("E")) {
                            s = s.delete(s.length() - 2, s.length());
                            s.append("Chadda_E");
                        } else if (String.valueOf(s.charAt(s.length() - 1)).equals("M")) {
                            s = s.delete(s.length() - 2, s.length());
                            s.append("Chadda_E");
                        } else if (String.valueOf(s.charAt(s.length() - 1)).equals("B")) {
                            s = s.delete(s.length() - 2, s.length());
                            s.append("Chadda_I");
                        } else if (String.valueOf(s.charAt(s.length() - 1)).equals("I")) {
                            s = s.delete(s.length() - 2, s.length());
                            s.append("Chadda_I");
                        }
                    } /*Space label*/ else if ((String.valueOf(t.charAt(j)).equals(" "))) {
                        s.append(" ").append("Space");
                    } /* Digit labels*/ else if ((String.valueOf(t.charAt(j)).equals("0")) || (String.valueOf(t.charAt(j)).equals("1")) || (String.valueOf(t.charAt(j)).equals("2")) || (String.valueOf(t.charAt(j)).equals("3"))
                            || (String.valueOf(t.charAt(j)).equals("4")) || (String.valueOf(t.charAt(j)).equals("5")) || (String.valueOf(t.charAt(j)).equals("6")) || (String.valueOf(t.charAt(j)).equals("7"))
                            || (String.valueOf(t.charAt(j)).equals("8")) || (String.valueOf(t.charAt(j)).equals("9")) || (String.valueOf(t.charAt(j)).equals(".")) || (String.valueOf(t.charAt(j)).equals(":"))
                            || (String.valueOf(t.charAt(j)).equals("،")) || (String.valueOf(t.charAt(j)).equals("/")) || (String.valueOf(t.charAt(j)).equals("%")) || (String.valueOf(t.charAt(j)).equals("?"))
                            || (String.valueOf(t.charAt(j)).equals("!")) || (String.valueOf(t.charAt(j)).equals("\""))|| (String.valueOf(t.charAt(j)).equals("-"))
                            ||(String.valueOf(t.charAt(j))).equals("(")||(String.valueOf(t.charAt(j))).equals(")")) {
                        s.append(" ").append(nom_lettre[ind]);
                    } /*Hamza label*/ else if (String.valueOf(t.charAt(j)).equals("ء")) {
                        s.append(" ").append(nom_lettre[ind]);
                    } /*Others char labels*/ else if (!(String.valueOf(t.charAt(j))).equals("ء") && !(String.valueOf(t.charAt(j))).equals("0") && !(String.valueOf(t.charAt(j))).equals("1") && !(String.valueOf(t.charAt(j))).equals("2")
                            && !(String.valueOf(t.charAt(j))).equals("3") && !(String.valueOf(t.charAt(j))).equals("4") && !(String.valueOf(t.charAt(j))).equals("5") && !(String.valueOf(t.charAt(j))).equals("6")
                            && !(String.valueOf(t.charAt(j))).equals("7") && !(String.valueOf(t.charAt(j))).equals("8") && !(String.valueOf(t.charAt(j))).equals("9") && !(String.valueOf(t.charAt(j))).equals(" ") 
                            && !(String.valueOf(t.charAt(j))).equals(".") && !(String.valueOf(t.charAt(j))).equals(":") && !(String.valueOf(t.charAt(j))).equals("،") && !(String.valueOf(t.charAt(j))).equals("/")
                            && !(String.valueOf(t.charAt(j))).equals("%") && !(String.valueOf(t.charAt(j))).equals("?") && !(String.valueOf(t.charAt(j))).equals("!") && !(String.valueOf(t.charAt(j))).equals("\"")
                            && !(String.valueOf(t.charAt(j))).equals("-")&& !(String.valueOf(t.charAt(j))).equals("(")&& !(String.valueOf(t.charAt(j))).equals(")")) {

                        if ((String.valueOf(t.charAt(j)).equals("ا")) && (String.valueOf(t.charAt(j - 1)).equals("ل"))) { //String a=String.valueOf(s.charAt(s.length()-1));

                            s = s.delete(s.length() - 7, s.length());
                            s.append(" ").append("Laam_EAlif_E");
                        } else if ((String.valueOf(t.charAt(j)).equals("أ")) && (String.valueOf(t.charAt(j - 1)).equals("ل"))) { //String a=String.valueOf(s.charAt(s.length()-1));

                            s = s.delete(s.length() - 7, s.length());
                            s.append(" ").append("Laam_EHamzaAboveAlif_E");

                        } else if ((String.valueOf(t.charAt(j)).equals("إ")) && (String.valueOf(t.charAt(j - 1)).equals("ل"))) { //String a=String.valueOf(s.charAt(s.length()-1));

                            s = s.delete(s.length() - 7, s.length());
                            s.append(" ").append("Laam_EHamzaUnderAlif_E");
                        } else if ((String.valueOf(t.charAt(j)).equals("آ")) && (String.valueOf(t.charAt(j - 1)).equals("ل"))) { //String a=String.valueOf(s.charAt(s.length()-1));

                            s = s.delete(s.length() - 7, s.length());
                            s.append(" ").append("Laam_ETildAboveAlif_E");
                        } else if ((((String.valueOf(t.charAt(j)).equals("ة")) || (String.valueOf(t.charAt(j)).equals("ا")) || (String.valueOf(t.charAt(j)).equals("د")) || (String.valueOf(t.charAt(j)).equals("ذ"))
                                || (String.valueOf(t.charAt(j)).equals("ر")) || (String.valueOf(t.charAt(j)).equals("ز")) || (String.valueOf(t.charAt(j)).equals("و"))
                                || (String.valueOf(t.charAt(j)).equals("آ")) || (String.valueOf(t.charAt(j)).equals("أ")) || (String.valueOf(t.charAt(j)).equals("إ"))
                                || (String.valueOf(t.charAt(j)).equals("ؤ")))) && ((String.valueOf(t.charAt(j - 1)).equals("ا")) || (String.valueOf(t.charAt(j - 1)).equals("د")) || (String.valueOf(t.charAt(j - 1)).equals("ذ"))
                                || (String.valueOf(t.charAt(j - 1)).equals("ر")) || (String.valueOf(t.charAt(j - 1)).equals("ز")) || (String.valueOf(t.charAt(j - 1)).equals("و"))
                                || (String.valueOf(t.charAt(j - 1)).equals("آ")) || (String.valueOf(t.charAt(j - 1)).equals("ّ")) || (String.valueOf(t.charAt(j - 1)).equals("أ")) || (String.valueOf(t.charAt(j - 1)).equals("إ"))
                                || (String.valueOf(t.charAt(j - 1)).equals("ؤ"))) && (!String.valueOf(t.charAt(j - 1)).equals("ل"))) {
                            s.append(" ").append(nom_lettre[ind]).append("_I");
                        } else if (((((!String.valueOf(t.charAt(j)).equals("ة")) || (!String.valueOf(t.charAt(j)).equals("ا")) || (!String.valueOf(t.charAt(j)).equals("د")) || (!String.valueOf(t.charAt(j)).equals("ذ"))
                                || (!String.valueOf(t.charAt(j)).equals("ر")) || (!String.valueOf(t.charAt(j)).equals("ز")) || (!String.valueOf(t.charAt(j)).equals("و"))
                                || (!String.valueOf(t.charAt(j)).equals("آ")) || (!String.valueOf(t.charAt(j)).equals("أ")) || (!String.valueOf(t.charAt(j)).equals("إ"))
                                || (!String.valueOf(t.charAt(j)).equals("ؤ"))))) && ((String.valueOf(t.charAt(j - 1)).equals("ا")) || (String.valueOf(t.charAt(j - 1)).equals("د")) || (String.valueOf(t.charAt(j - 1)).equals("ذ"))
                                || (String.valueOf(t.charAt(j - 1)).equals("ر")) || (String.valueOf(t.charAt(j - 1)).equals("ز")) || (String.valueOf(t.charAt(j - 1)).equals("و"))
                                || (String.valueOf(t.charAt(j - 1)).equals("آ")) || (String.valueOf(t.charAt(j - 1)).equals("أ")) || (String.valueOf(t.charAt(j - 1)).equals("إ"))
                                || (String.valueOf(t.charAt(j - 1)).equals("ؤ")))) {
                            s.append(" ").append(nom_lettre[ind]).append("_I");
                        } else if (((((String.valueOf(t.charAt(j)).equals("ة")) || (String.valueOf(t.charAt(j)).equals("ا")) || (String.valueOf(t.charAt(j)).equals("د")) || (String.valueOf(t.charAt(j)).equals("ذ"))
                                || (String.valueOf(t.charAt(j)).equals("ر")) || (String.valueOf(t.charAt(j)).equals("ز")) || (String.valueOf(t.charAt(j)).equals("و"))
                                || (String.valueOf(t.charAt(j)).equals("آ")) || (String.valueOf(t.charAt(j)).equals("أ")) || (String.valueOf(t.charAt(j)).equals("إ"))
                                || (String.valueOf(t.charAt(j)).equals("ؤ"))))) && ((!String.valueOf(t.charAt(j - 1)).equals("ا")) || (!String.valueOf(t.charAt(j - 1)).equals("د")) || (!String.valueOf(t.charAt(j - 1)).equals("ذ"))
                                || (!String.valueOf(t.charAt(j - 1)).equals("ر")) || (!String.valueOf(t.charAt(j - 1)).equals("ز")) || (!String.valueOf(t.charAt(j - 1)).equals("و"))
                                || (!String.valueOf(t.charAt(j - 1)).equals("آ")) || (!String.valueOf(t.charAt(j - 1)).equals("ّ")) || (!String.valueOf(t.charAt(j - 1)).equals("أ")) || (!String.valueOf(t.charAt(j - 1)).equals("إ"))
                                || (!String.valueOf(t.charAt(j - 1)).equals("ؤ"))) && (!String.valueOf(t.charAt(j - 1)).equals("ل"))) {
                            s.append(" ").append(nom_lettre[ind]).append("_E");
                        } else if (((((!String.valueOf(t.charAt(j)).equals("ة")) || (!String.valueOf(t.charAt(j)).equals("ا")) || (!String.valueOf(t.charAt(j)).equals("د")) || (!String.valueOf(t.charAt(j)).equals("ذ"))
                                || (!String.valueOf(t.charAt(j)).equals("ر")) || (!String.valueOf(t.charAt(j)).equals("ز")) || (!String.valueOf(t.charAt(j)).equals("و"))
                                || (!String.valueOf(t.charAt(j)).equals("آ")) || (!String.valueOf(t.charAt(j)).equals("أ")) || (!String.valueOf(t.charAt(j)).equals("إ"))
                                || (!String.valueOf(t.charAt(j)).equals("ؤ"))))) && ((!String.valueOf(t.charAt(j - 1)).equals("ا")) || (!String.valueOf(t.charAt(j - 1)).equals("د")) || (!String.valueOf(t.charAt(j - 1)).equals("ذ"))
                                || (!String.valueOf(t.charAt(j - 1)).equals("ر")) || (!String.valueOf(t.charAt(j - 1)).equals("ز")) || (!String.valueOf(t.charAt(j - 1)).equals("و"))
                                || (!String.valueOf(t.charAt(j - 1)).equals("آ")) || (!String.valueOf(t.charAt(j - 1)).equals("أ")) || (!String.valueOf(t.charAt(j - 1)).equals("إ"))
                                || (!String.valueOf(t.charAt(j - 1)).equals("ؤ")))) {
                            s.append(" ").append(nom_lettre[ind]).append("_E");
                        }
                    }
                }
            }
        }
        return s.toString();
    }
public static String labelsTotext (String str)
{
     String[] splitArray = str.split(" ");
     StringBuffer chaine = new StringBuffer();
     int index = -1;
     String ch = null;
     
     
     for (int j = 0; j < splitArray.length; j++) {
         
         
//         if(splitArray[j].equals("Laam_EAlif_E")) 
//         {
//             System.out.println("hola!");
//             splitArray[j] = splitArray[j].replace("Laam_EAlif_E", "Laam_E Alif_E");
//             
//         }
         if (splitArray[j].charAt(splitArray[j].length() - 1) == 'B') splitArray[j] = splitArray[j].replace("_B", "");
         else if(splitArray[j].charAt(splitArray[j].length() - 1) == 'M') splitArray[j] = splitArray[j].replace("_M", "");
         else if(splitArray[j].charAt(splitArray[j].length() - 1) == 'E') splitArray[j] = splitArray[j].replace("_E", "");
         else if(splitArray[j].charAt(splitArray[j].length() - 1) == 'I') splitArray[j] = splitArray[j].replace("_I", "");
         
         ch=String.valueOf(splitArray[j]);
         index = ARRAY.find2(nom_lettre, ch);
         System.out.println(splitArray[j]);
         if (index != -1)
          {
           if(String.valueOf(splitArray[j]).contains("Alif")||String.valueOf(splitArray[j]).contains("Baa")||String.valueOf(splitArray[j]).contains("Taaa")
            ||String.valueOf(splitArray[j]).contains("Thaa")||String.valueOf(splitArray[j]).contains("Jiim")||String.valueOf(splitArray[j]).contains("Haaa")
            ||String.valueOf(splitArray[j]).contains("Xaa")||String.valueOf(splitArray[j]).contains("Daal")||String.valueOf(splitArray[j]).contains("Thaal")
            ||String.valueOf(splitArray[j]).contains("Raa")||String.valueOf(splitArray[j]).contains("Zaay")||String.valueOf(splitArray[j]).contains("Siin")
            ||String.valueOf(splitArray[j]).contains("Shiin")||String.valueOf(splitArray[j]).contains("Saad")||String.valueOf(splitArray[j]).contains("Daad")
            ||String.valueOf(splitArray[j]).contains("Thaaa")||String.valueOf(splitArray[j]).contains("Taa")||String.valueOf(splitArray[j]).contains("Ayn")
            ||String.valueOf(splitArray[j]).contains("Ghayn")||String.valueOf(splitArray[j]).contains("Faa")||String.valueOf(splitArray[j]).contains("Gaaf")
            ||String.valueOf(splitArray[j]).contains("Kaaf")||String.valueOf(splitArray[j]).contains("Laam")||String.valueOf(splitArray[j]).contains("Miim")
            ||String.valueOf(splitArray[j]).contains("Nuun")||String.valueOf(splitArray[j]).contains("Haa")||String.valueOf(splitArray[j]).contains("Waaw")
            ||String.valueOf(splitArray[j]).contains("Yaa")||String.valueOf(splitArray[j]).contains("Hamza")||String.valueOf(splitArray[j]).contains("TildAboveAlif")
            ||String.valueOf(splitArray[j]).contains("HamzaAboveAlif")||String.valueOf(splitArray[j]).contains("HamzaUnderAlif")||String.valueOf(splitArray[j]).contains("TaaaClosed")
            ||String.valueOf(splitArray[j]).contains("HamzaAboveWaaw")||String.valueOf(splitArray[j]).contains("HamzaAboveAlifBroken")||String.valueOf(splitArray[j]).contains("AlifBroken")
            ||String.valueOf(splitArray[j]).contains("Chadda")||String.valueOf(splitArray[j]).contains("Space")||String.valueOf(splitArray[j]).contains("Digit_0")
            ||String.valueOf(splitArray[j]).contains("Digit_1")||String.valueOf(splitArray[j]).contains("Digit_2")||String.valueOf(splitArray[j]).contains("Digit_3")
            ||String.valueOf(splitArray[j]).contains("Digit_4")||String.valueOf(splitArray[j]).contains("Digit_5")||String.valueOf(splitArray[j]).contains("Digit_6")
            ||String.valueOf(splitArray[j]).contains("Digit_7")||String.valueOf(splitArray[j]).contains("Digit_8")||String.valueOf(splitArray[j]).contains("Digit_9")
            ||String.valueOf(splitArray[j]).contains("Point")||String.valueOf(splitArray[j]).contains("Colon")||String.valueOf(splitArray[j]).contains("Comma")
            ||String.valueOf(splitArray[j]).contains("Slash")||String.valueOf(splitArray[j]).contains("Percent")||String.valueOf(splitArray[j]).contains("QuestionMark")
            ||String.valueOf(splitArray[j]).contains("ExclamationMark")||String.valueOf(splitArray[j]).contains("Quote")||String.valueOf(splitArray[j]).contains("Hyphen")||String.valueOf(splitArray[j]).contains(""))
            /*le cas de LaamAlif*/
        chaine.append(LETTERSIsolee[index]);
           
            }
         if (String.valueOf(splitArray[j]).contains("LaamAlif"))
                     chaine.append("لا");
        else if (String.valueOf(splitArray[j]).contains("LaamHamzaAboveAlif"))
                     chaine.append("لأ");
        else if (String.valueOf(splitArray[j]).contains("LaamHamzaUnderAlif"))
                     chaine.append("لإ");
        else if (String.valueOf(splitArray[j]).contains("LaamTildAboveAlif"))
                     chaine.append("لآ");

     }
            
     
    return chaine.toString();
}
    public static String dictLine(String ch) {
        //String  str=" ";
//        String []tempStr=ch.split(" ");
//        String chModified=" ";
//        for (int i = 0; i < tempStr.length; i++) {
//            if(tempStr[i].equals("&quot;"))
//                    {
//                       tempStr[i]=tempStr[i].replace("&quot;", "\"");
//                        
//                    }
//        }
//        for (int i = 0; i < tempStr.length; i++) {
//            chModified += tempStr[i].concat(" ");
//        }
        String str=dictWord(ch);
        String[] splitArray = str.split(" ");
//Alif Daal Thaal Raa Zaay  TaaaClosed HamzaAboveWaaw AlifBroken Waaw TildAboveAlif HamzaAboveAlif HamzaUnderAlif
        for (int i = 0; i < splitArray.length-1; i++) {
            if (splitArray[i].equals("Space")||splitArray[i].equals("Comma")||splitArray[i].equals("Colon")||splitArray[i].equals("Quote")||splitArray[i].equals("Hyphen")||splitArray[i].equals("PrtO")||splitArray[i].equals("PrtC")) {
                if (splitArray[i - 1].contains("Alif") || splitArray[i - 1].contains("Daal") || splitArray[i - 1].contains("Thaal") || splitArray[i - 1].contains("Raa")
                        || splitArray[i - 1].contains("Zaay") || splitArray[i - 1].contains("TaaaClosed") || splitArray[i - 1].contains("Waaw") || splitArray[i - 1].contains("HamzaAboveWaaw")
                        || splitArray[i - 1].contains("AlifBroken") || splitArray[i - 1].contains("TildAboveAlif") || splitArray[i - 1].contains("HamzaAboveAlif") || splitArray[i - 1].contains("HamzaAboveAlif")) {

                    // System.out.println("opaaaa");
                    if ((splitArray[i - 2].charAt(splitArray[i - 2].length() - 1) == 'E') || (splitArray[i - 2].charAt(splitArray[i - 2].length() - 1) == 'I')) {
                        if (splitArray[i - 1].charAt(splitArray[i - 1].length() - 1) == 'B') {
                            splitArray[i - 1] = splitArray[i - 1].replace("_B", "_I");
                        }
                    } else {
                        if (splitArray[i - 1].charAt(splitArray[i - 1].length() - 1) == 'M') {
                            splitArray[i - 1] = splitArray[i - 1].replace("_M", "_E");
                        }
                    }

                } 
                
                else
                {
                    falseCarac = splitArray[i - 1];
                    if ((falseCarac.charAt(falseCarac.length() - 1) == 'M') && (falseCarac.charAt(falseCarac.length() - 2) == '_')) {
                        splitArray[i - 1] = splitArray[i - 1].replace("_M", "_E");
                    }
                    else if ((falseCarac.charAt(falseCarac.length() - 1) == 'B') && (falseCarac.charAt(falseCarac.length() - 2) == '_'))
                        splitArray[i - 1] = splitArray[i - 1].replace("_B", "_I");
                    
                }
                //if(i<)
                falseCaracAfter = splitArray[i + 1];
                    if ((falseCaracAfter.charAt(falseCaracAfter.length() - 1) == 'M') && (falseCaracAfter.charAt(falseCaracAfter.length() - 2) == '_')) {
                        splitArray[i + 1] = splitArray[i + 1].replace("_M", "_B");
                    }
                    else if ((falseCaracAfter.charAt(falseCaracAfter.length() - 1) == 'E') && (falseCaracAfter.charAt(falseCaracAfter.length() - 2) == '_')&&(!falseCaracAfter.contains("&")))
                    {
                        splitArray[i + 1] = splitArray[i + 1].replace("_E", "_I");
                    }

                //System.out.println(falseCarac.charAt(falseCarac.length() - 2)+" "+falseCarac.charAt(falseCarac.length() - 1));
                //System.out.println(falseCarac);

            }
            
        }
       // str ="";
        for (int i = 0; i < splitArray.length; i++) {
            ltModified += splitArray[i].concat(" ");
        }

        return ltModified;
        
    }
}