/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package activ;

import static activ.XMLHandler.count;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author ramii
 */
public class Statistics {
    static Hashtable hs ;
    static Element root;
    
    public static void Init(){
        try {
            TrakingCalculator.InitTable();
            hs = (Hashtable)TrakingCalculator.TableDeChasse.clone();       
            Iterator<Map.Entry>  it;
            Map.Entry entry;
            
            it = hs.entrySet().iterator();
            while (it.hasNext()) {
                entry = it.next();
                entry.setValue(new Integer(0));
                //System.out.println(entry.getKey().toString() + " " + entry.getValue().toString());
            }
            File fXmlFile = new File(XMLHandler.externalXML);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            
            root = doc.getDocumentElement();
            
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static int framesWithTextBoxes(){
        int c = 0;
        /* static text node we know that there is only one instance of it in the xml */
        Element stNode = (Element) root.getElementsByTagName("staticText").item(0);

        NodeList staticTB = stNode.getChildNodes();
            
        if (staticTB == null)
            return 0;
        
        for(int j = 0; j < staticTB.getLength(); j++){
            Node nd = staticTB.item(j);
            Element e;
               
            if(nd.getNodeType() == Node.ELEMENT_NODE){
                e = (Element)nd;
            }
            else
                continue;
            NodeList childs = e.getChildNodes();
            for(int k = 0; k < childs.getLength(); k++){
                Node n = childs.item(k);
                if(n.getNodeType() == Node.ELEMENT_NODE){
                    if (n.getNodeName() == "aInterval"){
                        Element el = (Element)n;
                        int fs = Integer.parseInt(el.getAttribute("frame_S"));
                        int fe = Integer.parseInt(el.getAttribute("frame_E"));
                        
                        c += fe - fs + 1;
                    }
                }
            }
        }
        return c;  
    }
    
    public static int textboxesCount(){
        int c = 0;
        Element stNode = (Element) root.getElementsByTagName("staticText").item(0);
        NodeList staticTB = stNode.getChildNodes();
        
        for(int i = 0; i < staticTB.getLength();i++){
            Node n = staticTB.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE)
                c++;
        }
        
        return c;
    }
    
    public static int textlinesCount(){
        int c = 0;
        Element stNode = (Element) root.getElementsByTagName("staticText").item(0);
        NodeList staticTB = stNode.getChildNodes();
        for(int j = 0; j < staticTB.getLength(); j++){
            Node nd = staticTB.item(j);
            Element e;
               
            if(nd.getNodeType() == Node.ELEMENT_NODE){
                e = (Element)nd;
            }
            else
                continue;
            
            NodeList childs = e.getChildNodes();
            int fs = 0;
            int x =0, y = 0, w = 0, h = 0;
            int id = Integer.parseInt(e.getAttribute("id"));
            for(int k = 0; k < childs.getLength(); k++){
                Node n = childs.item(k);
                if(n.getNodeType() == Node.ELEMENT_NODE){
                    if (n.getNodeName() == "content"){
                        NodeList nl = n.getChildNodes();
                        for(int l = 0; l < nl.getLength(); l++)
                        {
                            Node no = nl.item(l);
                            if(no.getNodeType() == Node.ELEMENT_NODE){
                                c++;
                            }
                        }
                    }
                }
            }
        }
        
        return c;
    }
    
    public static int wordsAndCharsCount(){
        int c = 0;
        Element stNode =  (Element) root.getElementsByTagName("staticText").item(0);
        Element sctNode = (Element) root.getElementsByTagName("scrollingText").item(0);
        
        NodeList staticTB = stNode.getChildNodes();
        for(int j = 0; j < staticTB.getLength(); j++){
            Node nd = staticTB.item(j);
            Element e;
               
            if(nd.getNodeType() == Node.ELEMENT_NODE){
                e = (Element)nd;
            }
            else
                continue;
            
            NodeList childs = e.getChildNodes();
            int fs = 0;
            int x =0, y = 0, w = 0, h = 0;
            int id = Integer.parseInt(e.getAttribute("id"));
            for(int k = 0; k < childs.getLength(); k++){
                Node n = childs.item(k);
                if(n.getNodeType() == Node.ELEMENT_NODE){
                    if (n.getNodeName() == "content"){
                        NodeList nl = n.getChildNodes();
                        for(int l = 0; l < nl.getLength(); l++)
                        {
                            Node no = nl.item(l);
                            if(no.getNodeType() == Node.ELEMENT_NODE){
                                Element el = (Element)no;
                                String text = el.getAttribute("transcriptionLabel");
                                c += text.split("Space").length;
                                String [] list = text.split(" ");
                                
                                for (String s : list) {
                                    s.replace(" ", ""); // removing spaces, incase ..
                                    Integer I = (Integer)hs.get(s);
                                    if (I == null)
                                        I = 1;
                                    System.out.println("s = '"+s+"' I = "+I);
                                    hs.put(s, I.intValue() + 1);
                                }
                            }
                        }
                    }
                    
                }
            }
        }
        Node n = null;
        if (sctNode == null)
            return c;
        
        NodeList scrollingContent = sctNode.getChildNodes();
        for(int k = 0; k < scrollingContent.getLength(); k++)
        {
            n = scrollingContent.item(k);
            if(n.getNodeType() == Node.ELEMENT_NODE){
                if (n.getNodeName().equals("content"))
                    break;
            }
        }
        if(n != null) 
        {
            NodeList staticTexts = n.getChildNodes();
            Vector vec = new Vector();
            for(int k = 0; k < staticTexts.getLength(); k++){
                if(staticTexts.item(k).getNodeType() == Node.ELEMENT_NODE){
                    vec.add(staticTexts.item(k));
                }
            }
                            
            for(int k = 0; k < vec.size(); k++){
                Element nTmp = (Element)vec.elementAt(k);
                String text = nTmp.getAttribute("transcriptionLabel");
                c += text.split("Space").length;
                
                String [] list = text.split(" ");
                for (String s : list) {
                    s.replace(" ", ""); // removing spaces, incase ..
                    Integer I = (Integer)hs.get(s);
                    //System.out.println("s = '"+s+"' I = "+I);
                    if (I == null)
                        I = 1;
                    hs.put(s, I.intValue() + 1);
                }
            }
        }
        return c;
    }
    
    public static int tickersCount(){
        Element sctNode = (Element) root.getElementsByTagName("scrollingText").item(0);
        if (sctNode != null){
            NodeList nd = sctNode.getChildNodes();
            for(int i = 0; i < nd.getLength(); i++){
                Node n = nd.item(i);
                    if(n.getNodeType() == Node.ELEMENT_NODE){
                        if (n.getNodeName().equals("content")){
                            Element e = (Element)n;
                            return Integer.parseInt(e.getAttribute("nbOftickerInformation"));
                        }
                }
            }
            return 0;
        }
        else
            return 0;
    }
    
    public static void genStats(){
        Init();
        try {
            File file = new File("stats.txt");
 
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
             
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Number of frames with textboxes = "+framesWithTextBoxes()+"\n");
            bw.write("Number of textboxes = "+textboxesCount()+"\nNumber of textlines = "+textlinesCount()+"\n");
            bw.write("Number of words = " + wordsAndCharsCount()+"\n");
            bw.write("Number of tickers = " + tickersCount()+"\n");
            bw.write("Occurence of each character:\n\n");
            Iterator<Map.Entry>  it;
            int nbC = 0;
            Map.Entry entry;    
            
            it = hs.entrySet().iterator();
            while (it.hasNext()) {
                entry = it.next();
                if(!"0".equals(entry.getValue().toString())){
                    bw.write("\t"+entry.getKey().toString() + " " + entry.getValue().toString()+"\n");
                    nbC += Integer.parseInt(entry.getValue().toString());
                }
            }
        
            nbC -= (Integer)hs.get("Space");
        
            bw.write("\nTotal number of characters = "+nbC);
            
            bw.close();
 
 
        } catch (IOException e) {
            e.printStackTrace();
	}
        
        root = null;
    }
}
