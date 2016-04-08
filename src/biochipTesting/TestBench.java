package biochipTesting;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import gurobi.GRBException;
import gurobiILP.*;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ArrayList;

import java.util.Stack;

//import gurobi.GRBException;

public class TestBench {
	private static int WIDTH = 45;
	private static int HEIGHT = 45;
	private static int maxPaths = 2;
			
	private static final String dataPath =  "/home/ga63quk/workspace/biochipTestGit/biochipTesting/src/data.xml";
	
	ArrayList<ArrayList<Int2>> pathsVertex;
		
	public static void main(String arg[]) throws GRBException, FileNotFoundException, UnsupportedEncodingException {
		
		//printSystem();
		ArrayList<Wall> walls = new ArrayList<Wall>();
		ArrayList<Hole> holes = new ArrayList<Hole>();
		
		//readXml(dataPath,walls,holes);
		Graph graph;
		graph = new Graph(WIDTH,HEIGHT);
		graph.setHoles(holes);
 		graph.setWalls(walls);
 		graph.setHeadsTails(direction.Source, direction.Terminal);
 		graph.splitGraph(9, 9);
 		try {
			graph.getPaths(maxPaths);
			graph.findMorePaths();
			//graph.splitFindMorePaths();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//graph.setHeadsTails(direction.Source, direction.Terminal);
		//graph.test();
		
 		Graph cutGraph = graph.getCutGraph();
 		cutGraph.setHeadsTails(direction.East, direction.West);
 		cutGraph.setHeadsTails(direction.North,direction.South);
 		cutGraph.splitGraph(5, 5);
/*		graph.getAcyclicILPExactRoute();
		graph.setHeadsTails(direction.Source, direction.Terminal);
		graph.splitRow = 5;
		graph.splitCol = 5;
*/		
		try {
			cutGraph.getPaths(maxPaths);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
//		graph.splitY = new ArrayList<Double>();
//		graph.splitX = new ArrayList<Double>();
//		graph.splitY.add(-0.5);graph.splitY.add(1.5);graph.splitY.add(3.5);
//		graph.splitX.add(-0.5);graph.splitX.add(1.5);graph.splitX.add(3.5);
		//graph.splitGraph();
		
 		//graph = new Graph(WIDTH,HEIGHT, true); //generate cut
// 		graph.setHoles(holes);
// 		graph.setWalls(walls);
// 		
// 		//graph.findCuts(graph.findCriticalPath());
// 		//graph.findPaths();
// 		graph.getAcyclicILP();
// 		
// 		//graph.getAcyclicILPExactRoute();
// 		//graph.getCuts();
// 		//graph.getOneLongestPath();
// 		Enviroment env = new Enviroment();
//		//acyclicPathILP();
//		
//		
//		ArrayList<String> varNames = graph.variables;
//		ArrayList<Integer> varTypes = graph.variableTypes;
//		
//		env.setVars(varNames,varTypes);
//		//env.setContrains(graph.ILP);
//		env.setObjective(graph.obj);
//		env.writeFile(graph.ILP);
		//env.run();
 		
		
		if(!graph.pathTest())
			System.out.println("there is at least one SA0 fault in this chip");
		else if(!graph.cutTest())
			System.out.println("there is at least one SA1 falut in this chip");
		
		else 
			System.out.println("this chip is perfect!");
	
		
		
//		
		
	}
	
	public static void printSystem(){
		/* Total number of processors or cores available to the JVM */
	    System.out.println("Available processors (cores): " + 
	        Runtime.getRuntime().availableProcessors());

	    /* Total amount of free memory available to the JVM */
	    System.out.println("Free memory (bytes): " + 
	        Runtime.getRuntime().freeMemory());

	    /* This will return Long.MAX_VALUE if there is no preset limit */
	    long maxMemory = Runtime.getRuntime().maxMemory();
	    /* Maximum amount of memory the JVM will attempt to use */
	    System.out.println("Maximum memory (bytes): " + 
	        (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));

	    /* Total memory currently available to the JVM */
	    System.out.println("Total memory available to JVM (bytes): " + 
	        Runtime.getRuntime().totalMemory());

	    /* Get a list of all filesystem roots on this system */
	    File[] roots = File.listRoots();

	    /* For each filesystem root, print some info */
	    for (File root : roots) {
	      System.out.println("File system root: " + root.getAbsolutePath());
	      System.out.println("Total space (bytes): " + root.getTotalSpace());
	      System.out.println("Free space (bytes): " + root.getFreeSpace());
	      System.out.println("Usable space (bytes): " + root.getUsableSpace());
	    }
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

					}
				}
		    } 
			catch (Exception e) {		    	
		    	e.printStackTrace();
		    }
		  
		
			
		}
	
	
	
	
	
}
