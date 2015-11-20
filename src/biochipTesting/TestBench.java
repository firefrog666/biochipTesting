package biochipTesting;
//import gurobiILP.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

import java.util.Stack;

//import gurobi.GRBException;

public class TestBench {
	private static int WIDTH ;
	private static int HEIGHT;
	private static final String dataPath =  "/home/ga63quk/workspace/biochipTestGit/biochipTesting/src/data.xml";
	
	
	public static void main(String arg[]) {
		ArrayList<Wall> walls = new ArrayList<Wall>();
		ArrayList<Hole> holes = new ArrayList<Hole>();
		
		readXml(dataPath,walls,holes);
		Graph graph;
 		graph = new Graph(WIDTH,HEIGHT);
 		graph.setHoles(holes);
 		graph.setWalls(walls);
 		
 		//graph.findCuts(graph.findCriticalPath());
 		graph.findPaths();
 		
		
		if(!graph.pathTest())
			System.out.println("there is at least one SA0 fault in this chip");
		else if(!graph.cutTest())
			System.out.println("there is at least one SA1 falut in this chip");
		
		else 
			System.out.println("this chip is perfect!");
	
		
		
//		
		
	}
	
	public static void readXml(String path, ArrayList<Wall> walls, ArrayList<Hole> holes){
		int x,y,s,t;
		try {

				File fXmlFile = new File(path);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
						
				//optional, but recommended
				//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();
	
				System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
				NodeList biochips = doc.getElementsByTagName("bioChip");
				Node biochip = biochips.item(0);
				Element eBiochip = (Element)biochip;
				String stemp = eBiochip.getAttribute("width");
				WIDTH =Integer.parseInt(eBiochip.getAttribute("width"));
				HEIGHT = Integer.parseInt(eBiochip.getAttribute("height"));
				
				//processing walls		
				NodeList nList = doc.getElementsByTagName("wall");						
				System.out.println("----------------------------");
	
				for (int temp = 0; temp < nList.getLength(); temp++) {
	
					Node nNode = nList.item(temp);
							
					System.out.println("\nCurrent Element :" + nNode.getNodeName());
							
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	
						Element eElement = (Element) nNode;
						Wall wall = new Wall();
						x =Integer.parseInt(eElement.getAttribute("x"));
						y =Integer.parseInt(eElement.getAttribute("y"));
						s =Integer.parseInt(eElement.getAttribute("s"));
						t =Integer.parseInt(eElement.getAttribute("t"));
						wall.setCoordinate(x, y, s, t);
						walls.add(wall);
						
						
						System.out.println(nNode.getNodeName() + " id : " + eElement.getAttribute("id"));
						System.out.println(nNode.getNodeName() + " x : " + eElement.getAttribute("x"));
						System.out.println(nNode.getNodeName() + " y : " + eElement.getAttribute("y"));
						System.out.println(nNode.getNodeName() + " s : " + eElement.getAttribute("s"));
						System.out.println(nNode.getNodeName() + " t : " + eElement.getAttribute("t"));
						
//						System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
//						System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
//						System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
//						System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
	
					}
				}
				
				//processing holes
				nList = doc.getElementsByTagName("hole");						
				System.out.println("----------------------------");
	
				for (int temp = 0; temp < nList.getLength(); temp++) {
	
					Node nNode = nList.item(temp);
							
					System.out.println("\nCurrent Element :" + nNode.getNodeName());
							
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	
						Element eElement = (Element) nNode;
						Hole hole = new Hole();
						x =Integer.parseInt(eElement.getAttribute("x"));
						y =Integer.parseInt(eElement.getAttribute("y"));
						s =Integer.parseInt(eElement.getAttribute("s"));
						t =Integer.parseInt(eElement.getAttribute("t"));
						hole.setCoordinate(x, y, s, t);
						holes.add(hole);
						
						
						System.out.println(nNode.getNodeName() + " id : " + eElement.getAttribute("id"));
						System.out.println(nNode.getNodeName() + " x : " + eElement.getAttribute("x"));
						System.out.println(nNode.getNodeName() + " y : " + eElement.getAttribute("y"));
						System.out.println(nNode.getNodeName() + " s : " + eElement.getAttribute("s"));
						System.out.println(nNode.getNodeName() + " t : " + eElement.getAttribute("t"));
						
//						System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
//						System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
//						System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
//						System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
	
					}
				}
		    } 
			catch (Exception e) {		    	
		    	e.printStackTrace();
		    }
		  
		
	}
	
}
