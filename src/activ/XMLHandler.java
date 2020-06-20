
package activ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class Interval{
    static int idCounter = 0;
    int frameS, frameE;
    int id;
    
    public Interval(int fs, int fe)
    {
        idCounter++;
        id = idCounter;
        frameS = fs;
        frameE = fe;
    }
}

class Position{
    int x, y, w, h;
    
    public Position(int px, int py, int width, int height)
    {
        x = px;
        y = py;
        w = width;
        h = height;
    }
}

class TextLine{
    String transcription;
    String transcriptionLabel;
    Rectangle r;
    public TextLine(String text, Rectangle r){
        transcription = text;
        transcriptionLabel = LabelTranscript.dictWord(text);
        this.r = r;
    }
}

class Content{
    TextLine[] lines;
    Color textColor;
    Color bgColor;
    boolean opaqueBG;
    
    public Content(TextLine[] l, Color tC, Color bgC, boolean obg){
        lines = l;
        textColor = tC;
        bgColor = bgC;
        opaqueBG = obg;
    }
}
        
public class XMLHandler {
    public static String externalXML = "";
    public static String XMLFile = "temp.xml";
    static int textBoxCount = 0;
    static int tickerCount = 0;
    
    static Vector staticTexts = new Vector();
    static Vector scrollingTexts = new Vector();
                
    static DocumentBuilderFactory docFactory;
    static DocumentBuilder docBuilder;
    static Element staticText;
    static Element scrollingText;
    static Element bandPos;
    static Element content;
    static Document doc;
        
    static int vid = 0;
    static int count = 13819;
    
    public static void InitXMLHandler(int id, String channel, String resolution, 
            String duration, int fps, int nbFrames)
    {
        vid = id;
        
        try{
            docFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();
            Element root = doc.createElement("video");
            
            doc.appendChild(root);
            
            root.setAttribute("id", id+"");
            root.setAttribute("channel", channel);
            root.setAttribute("resolution", resolution);
            root.setAttribute("duration", duration);
            root.setAttribute("fps", fps+"");
            root.setAttribute("nbOfFrames", nbFrames+"");
                
            staticText = doc.createElement("staticText");
            root.appendChild(staticText);
            staticText.setAttribute("nbOftextBox", "0");
            scrollingText = doc.createElement("scrollingText");
            scrollingText.setAttribute("orientation", "left-right");
            scrollingText.setAttribute("textColor", 
                        ImageController.scrollingTextColor.getRed()+", "+
                        ImageController.scrollingTextColor.getGreen()+", "+
                        ImageController.scrollingTextColor.getBlue());
            scrollingText.setAttribute("bgColor", "@virginTicker(Image)");
            scrollingText.setAttribute("bgType", "opaque");
            scrollingText.setAttribute("runningSpeed", "6,770 pixel/frame");
            
            bandPos = doc.createElement("bandPosition");
            scrollingText.appendChild(bandPos);
            
            content = doc.createElement("content");
            content.setAttribute("nbOftickerInformation", "0");
            scrollingText.appendChild(content);
            root.appendChild(scrollingText);
            
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } 
    }
     
    public static void LoadXMLFile(String fxml){
        int nbRect = 0;
        try {
            File fXmlFile = new File(fxml);
            XMLFile = fxml;
            docFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(fXmlFile);
            
            Element root = doc.getDocumentElement();
            
            
            Element stNode = (Element) root.getElementsByTagName("staticText").item(0);
            Element sctNode = (Element) root.getElementsByTagName("scrollingText").item(0);
            
            textBoxCount = Integer.parseInt(stNode.getAttribute("nbOftextBox"));
            for(int i = 0; i < sctNode.getChildNodes().getLength(); i++){
                Node n = sctNode.getChildNodes().item(i);
                if(n.getNodeType() == Node.ELEMENT_NODE){
                    if (n.getNodeName() == "content"){
                        content = (Element)n;
                        tickerCount = Integer.parseInt(content.getAttribute("nbOftickerInformation"));
                        break;
                    }
                }       
            }
            
            staticText = stNode;
            scrollingText = sctNode;
            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace();
        }
    } 
    public static void setScrollingTextColor(Color c){
        staticText.setAttribute("textColor", c.getRed()+", "+c.getGreen()+", "+c.getBlue());
    }
    
    public static void setScrollingTextProperties(boolean opaque, int x, int y, int w, int h){
        if (opaque)
            scrollingText.setAttribute("bgType", "opaque");
        else
            scrollingText.setAttribute("bgType", "transparent");

        bandPos.setAttribute("x", x+"");
        bandPos.setAttribute("y", y+"");
        bandPos.setAttribute("width", w+"");
        bandPos.setAttribute("height", h+"");
    }
    
    public static void AddStaticText(Interval [] a, Position p, Content c, int nbInterval){
            textBoxCount++;
            Element textBox = doc.createElement("textBox");
            textBox.setAttribute("id", textBoxCount+"");
            textBox.setAttribute("nbOfaInterval", nbInterval+"");
            
            for(int i = 0; i < a.length; i++){
                Element interval = doc.createElement("aInterval");
                interval.setAttribute("id", (i+1)+"");
                interval.setAttribute("frame_S", a[i].frameS+"");
                interval.setAttribute("frame_E", a[i].frameE+"");
                textBox.appendChild(interval);
            }
            
            
            Element position = doc.createElement("position");
            position.setAttribute("x", p.x+"");
            position.setAttribute("y", p.y+"");
            position.setAttribute("width", p.w+"");
            position.setAttribute("height", p.h+"");
            textBox.appendChild(position);
            
            Element content = doc.createElement("content");
            content.setAttribute("nbTextLines", c.lines.length+"");
            content.setAttribute("textColor", c.textColor.getRed()+", "+
                    c.textColor.getGreen()+", "+
                    c.textColor.getBlue());
            content.setAttribute("bgColor", c.textColor.getRed()+", "+c.textColor.getGreen()+", "+c.textColor.getBlue());
            
            if (c.opaqueBG)
                content.setAttribute("bgType", "opaque");
            else
                content.setAttribute("bgType", "transparent ");
            
            textBox.appendChild(content);
            for(int i = 0; i < c.lines.length; i++){
                Element textLine = doc.createElement("textLine");
                textLine.setAttribute("id", (i+1)+"");
                textLine.setAttribute("transcription", c.lines[i].transcription);
                textLine.setAttribute("transcriptionLabel", c.lines[i].transcriptionLabel);
                textLine.setAttribute("x", c.lines[i].r.x+"");
                textLine.setAttribute("y", c.lines[i].r.y+"");
                textLine.setAttribute("width", c.lines[i].r.width+"");
                textLine.setAttribute("height",c.lines[i].r.height+"");
                content.appendChild(textLine);
            }
            textBox.appendChild(content);
            staticTexts.add(textBox);
            staticText.appendChild(textBox);
            staticText.setAttribute("nbOftextBox", textBoxCount+"");
        }
    
    public static void addScrollingText(int frameS, int offset, String transcription, String transcriptionLabel){
        Element tickerInformation = doc.createElement("tickerInformation");
        tickerCount++;
        tickerInformation.setAttribute("id", ""+tickerCount);
        tickerInformation.setAttribute("frame_S", ""+frameS);
        tickerInformation.setAttribute("offset", ""+offset);
        tickerInformation.setAttribute("transcription", transcription);
        tickerInformation.setAttribute("transcriptionLabel",
                transcriptionLabel);
        
        content.appendChild(tickerInformation);
        content.setAttribute("nbOftickerInformation", ""+tickerCount);
        scrollingTexts.add(tickerInformation);
    }
    
    public static void doGenerateFrames(){
        // reading number of frames 
        /*int count = 0;
        if (MainFrame.controller == MainFrame.IMAGE)
            count = ImageController.count();
        else
            count = VideoController.g.getLengthInFrames();*/
        try{
            File fXmlFile = new File(externalXML);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();
            
            Element root = doc.getDocumentElement();
            
            System.out.println(root.getNodeName());
            
            count = Integer.parseInt(root.getAttribute("nbOfFrames"));
            
            /* static text node we know that there is only one instance of it in the xml */
            Element stNode = (Element) root.getElementsByTagName("staticText").item(0);
            Element sctNode = (Element) root.getElementsByTagName("scrollingText").item(0);
            // statix text boxes list
            NodeList staticTB = stNode.getChildNodes();
            System.out.println("this video have "+staticTB.getLength());
            for(int i = 0; i < count; i++){
                System.out.println("generating frame"+i);
                /* creating output xml file */
                DocumentBuilderFactory docFactory;
                DocumentBuilder docBuilder;
                Document document;
            
                docFactory = DocumentBuilderFactory.newInstance();
                docBuilder = docFactory.newDocumentBuilder();
                document = docBuilder.newDocument();
                
                Element xmlRoot = document.createElement("video");
                document.appendChild(xmlRoot);
                xmlRoot.setAttribute("id", ""+vid);

                Element frame = document.createElement("Frame");
                frame.setAttribute("id", i+"");
                xmlRoot.appendChild(frame);
                
                Element st = document.createElement("staticText");
                Element sct = document.createElement("scrollingText");
                frame.appendChild(st);
                frame.appendChild(sct);
                st.setAttribute("nbTextBox", "0");
                Element content = document.createElement("content");
                sct.appendChild(content);
                int visibleTickerCount = 1;
                        
                int c = 1;
                /* checking static texts */
                for(int j = 0; j < staticTB.getLength(); j++){
                    /* does out frame exists in one of the intervals ? */
                    boolean exist = false;
                    Node nd = staticTB.item(j);
                    Element e;
                    
                    if(nd.getNodeType() == Node.ELEMENT_NODE){
                        e = (Element)document.importNode(nd, true);
                    }
                    else
                        continue;
                    
                    NodeList childs = e.getChildNodes();
                    Vector v = new Vector();
                    for(int k = 0; k < childs.getLength(); k++){
                        Node n = childs.item(k);
                        if(n.getNodeType() == Node.ELEMENT_NODE){
                            if (n.getNodeName() == "aInterval"){
                               // System.out.println("Founded");
                                //System.exit(0);
                                v.add(n);
                            }
                        }
                    }
                    
                    for(int k = 0; k < v.size(); k++){
                        Element tmp = (Element)v.elementAt(k);
                        int fs = Integer.parseInt(tmp.getAttribute("frame_S"));
                        int fe = Integer.parseInt(tmp.getAttribute("frame_E"));
                        //System.out.println("frame_S = "+fs+"frame_E = "+fe);
                        if ((i >= fs) && (i <= fe)){
                            exist = true;
                            //System.out.println("Exists!!!");
                        }
                        //e.removeChild(tmp);
                    }
                    
                    if (exist){
                        c++;
                        st.setAttribute("nbTextBox", c+"");
                        Element emp = document.createElement("textBox");
                        for(int k = 0; k < e.getChildNodes().getLength(); k++ ){
                            Node n = e.getChildNodes().item(k);
                            if(n.getNodeType() == Node.ELEMENT_NODE){
                                if (!n.getNodeName().equals("aInterval"))
                                    emp.appendChild(n);
                            }
                        }
                        st.appendChild(emp);
                    }
                }
                
                Node n = null;
                
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
                        String text = nTmp.getAttribute("transcription");
                        String textLbl = nTmp.getAttribute("transcriptionLabel");
                        int offset = Integer.parseInt(nTmp.getAttribute("offset"));
                        int frameS = Integer.parseInt(nTmp.getAttribute("frame_S"));
                        
                        int [] tab = {frameS, offset};
                        TrakingCalculator.tichkerBF(textLbl, tab);
                        
                        if ((i >= tab[0]) && (i < TrakingCalculator.tichkerEF(textLbl, tab[0], tab[1])))
                        {
                            System.out.println("k = "+k);
                            String trans = TrakingCalculator.visibleText(textLbl, tab[0], tab[1], i);
                            LabelTranscript lt = new LabelTranscript();
                            System.out.println(textLbl);
                            System.out.println(trans);
                            Element visibleTicker = document.createElement("visibleTicker");
                            visibleTicker.setAttribute("id", visibleTickerCount+"");
                            visibleTicker.setAttribute("transcription", lt.labelsTotext(trans+"SpaceError"));
                            visibleTicker.setAttribute("transcriptionLabel", trans);
                            content.appendChild(visibleTicker);
                            visibleTickerCount++;
                        }
                    }
                }
                /*
                */
                
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(new File("frames/frame"+i+".xml"));
                transformer.transform(source, result);
            }
                    
        }catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(XMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(XMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void generateFrames(){
        try {            
            int count = 0;
            if (MainFrame.controller == MainFrame.IMAGE)
                count = ImageController.count();
            else
                count = VideoController.g.getLengthInFrames();
            
            System.out.println(count);
            FrameLoading frm = new FrameLoading();
            frm.setVisible(true);
            for(int i = 0; i < count; i++){
                frm.setMessage("Generating frame "+i+" / "+count);
                frm.setValue((int)(i/count)*100);
                DocumentBuilderFactory docFactory;
                DocumentBuilder docBuilder;
                Document doc;
            
                docFactory = DocumentBuilderFactory.newInstance();
                docBuilder = docFactory.newDocumentBuilder();
                doc = docBuilder.newDocument();
                
                Element root = doc.createElement("video");
                doc.appendChild(root);
                root.setAttribute("id", ""+vid);

                Element frame = doc.createElement("Frame");
                frame.setAttribute("id", i+"");
                root.appendChild(frame);
            
                Element staticTextNode = doc.createElement("staticText");
                frame.appendChild(staticTextNode);
                staticTextNode.setAttribute("nbTextBox", "0");
                int c = 0;
                for(int j = 0; j < staticTexts.size(); j++)
                {
                    Element e =(Element)doc.importNode((Node)staticTexts.elementAt(j), true);
                    Element interval = (Element)e.getChildNodes().item(0);
                    //System.out.println(interval.getAttribute("frame_S"));
                    //System.out.println(interval.getAttribute("frame_E"));
                    int fs = Integer.parseInt(interval.getAttribute("frame_S"));
                    int fe = Integer.parseInt(interval.getAttribute("frame_E"));
                    if ((i >= fs) && (i < fe))
                    {
                        c++;                  
                        /* interval is the first child of textbox so we remove with
                            index 0
                        */
                        e.removeChild(e.getChildNodes().item(0));
                        staticTextNode.setAttribute("nbTextBox", c+"");
                        staticTextNode.appendChild(e);
                    }
                }
                try{
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new File("frames/frame"+i+".xml"));
 
                    transformer.transform(source, result);
                }catch (TransformerException tfe) {
                    tfe.printStackTrace();
                }  
            }
            frm.dispose();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public static void generateImgs(){
        try{
            File fXmlFile = new File(externalXML);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            
            Element root = doc.getDocumentElement();
            
            /* static text node we know that there is only one instance of it in the xml */
            Element stNode = (Element) root.getElementsByTagName("staticText").item(0);
            //Element sctNode = (Element) root.getElementsByTagName("scrollingText").item(0);
            // statix text boxes list
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
                        if (n.getNodeName() == "aInterval"){
                            Element tmp = (Element)n;
                            fs = Integer.parseInt(tmp.getAttribute("frame_S"));
                        }
                        else if (n.getNodeName() == "position"){
                            Element tmp = (Element)n;
                            x = Integer.parseInt(tmp.getAttribute("x"));
                            y = Integer.parseInt(tmp.getAttribute("y"));
                            h = Integer.parseInt(tmp.getAttribute("height"));
                            w = Integer.parseInt(tmp.getAttribute("width"));
                        }
                    }
                }
                
                BufferedImage img = null;
                if (MainFrame.controller == MainFrame.IMAGE)
                    img = ImageIO.read(new File(ImageController.elementAt(fs)));
                else{
                    VideoController.g.setFrameNumber(fs);
                    img = VideoController.g.grab().getBufferedImage();
                }
                
                BufferedImage img2 = img.getSubimage(x, y, w, h);
                File outputfile = new File("textbox"+id+".png");
                ImageIO.write(img2, "png", outputfile);
            }            
        }catch(Exception e){ e.printStackTrace();}
    }
       
    public static void save(){
            try{
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(XMLFile));
 
		transformer.transform(source, result);
            }catch (TransformerException tfe) {
		tfe.printStackTrace();
            }   
        }
}
