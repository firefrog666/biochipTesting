package biochipTesting;
//import java.util.Random;

import java.util.Stack;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

//import java.io.FileNotFoundException;
//import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;

import java.util.HashMap;
//import java.util.List;
import java.util.Map;
//import java.util.Map.Entry;



import gurobiILP.*;


public class Graph {
	

	private static final int M = 10000;
	private static final int maxPaths = 2;
	private static final int numberOfJoints = 10;	
	public boolean isSplit;
	
	 
	public HashMap<String,Integer> pathReuslts;
	public ArrayList<ArrayList<Int2>> pathsVertex;
	private ArrayList<Node> nodes;
//	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;
	public Node entrance;
	public Node exit;
	private int width;
	private int height;
	private Map<Integer,Edge> hashEdges;  
	private HashMap<Integer,Node> hashNodes; //Hash Node by its coordinates i*100 + j; 
	
	private HashMap<Integer,Edge> hashTarEdges;
 	
	private HashMap<Integer,HashMap<Edge,String>> hashWVariables;
	private HashMap<Integer,HashMap<Edge,String>> hashFVariables;
	//private HashMap<Edge,String> hashXVariables;
	//private HashMap<Edge, String> hashBinaries;
	private ArrayList<ArrayList<Edge>> paths;
	//private ArrayList<ArrayList<Node>> pathsNode;
	private ArrayList<Edge> brinkL;
	private ArrayList<Edge> brinkR;

	private ArrayList<direction> heads;
	private ArrayList<direction> tails;
	
	private Graph conciseGraph;
	private ArrayList<Graph> subGraphs;
	private HashMap<Int2,Graph> hashSubGraphs;
	private ArrayList<ArrayList<Int2>> concisePaths; 
	
	private ArrayList<ArrayList<Edge>> cuts;
	
	
	
	public Int4 boundingBox;
	public Int2 center;
	public ArrayList<String> ILP;
	public ArrayList<String> variables;
	public ArrayList<Integer> variableTypes;
	public String obj;
	
	
	public ArrayList<Double> splitX; //start at -0.5 end at height + 0.5
	public ArrayList<Double> splitY;
	 
	public Graph(){
		heads = new ArrayList<direction>();
		tails = new ArrayList<direction>();
		ILP = new ArrayList<String>();
		variableTypes = new ArrayList<Integer>();
		variables = new ArrayList<String>();
		isSplit = false;
	}	
	
	
	
	
	public Graph(int w, int h){
		
		hashWVariables = new HashMap<Integer,HashMap<Edge,String>>() ;
		hashFVariables = new HashMap<Integer,HashMap<Edge,String>>() ;
		ILP = new ArrayList<String>();
		//hashXVariables = new HashMap<Edge,String>() ;
		//hashBinaries = new HashMap<Edge, String>();	
		hashEdges = new HashMap<Integer,Edge>();
		hashNodes  = new HashMap<Integer,Node>();
		//hashTarEdges = new HashMap<Integer,Edge>();
		cuts = new ArrayList<ArrayList<Edge>>();
		boundingBox = new Int4(0,0,w-1,h-1); 
		heads = new ArrayList<direction>();
		tails = new ArrayList<direction>();
		
		isSplit = false;
		init(w,h);	
	
	}
	public Graph(int w, int h, boolean cut){
		hashWVariables = new HashMap<Integer,HashMap<Edge,String>>() ;
		hashFVariables = new HashMap<Integer,HashMap<Edge,String>>() ;
		ILP = new ArrayList<String>();
		//hashXVariables = new HashMap<Edge,String>() ;
		//hashBinaries = new HashMap<Edge, String>();	
		hashEdges = new HashMap<Integer,Edge>();
		hashNodes  = new HashMap<Integer,Node>();
		//hashTarEdges = new HashMap<Integer,Edge>();
		cuts = new ArrayList<ArrayList<Edge>>();
		brinkR = new ArrayList<Edge>();
		brinkL = new ArrayList<Edge>();
		initCut(w,h);	
	}
	
	public Dir getDir2Nodes(Node a, Node b){
		if(a.coord.x == b.coord.x){
			if(a.coord.y > b.coord.y)
				return Dir.West;
			else
				return Dir.East;			
		}
		//a.y ==b.y
		else{
			if(a.coord.x < b.coord.x){
				return Dir.North;
			}
			else
				return Dir.South;
			
		}
			
			
	}
	
	public void splitGraph(int column, int row){
		double x,y;
		int stepX, stepY;
		splitX = new ArrayList<Double>();
		splitY = new ArrayList<Double>();
		stepX = (int)height / row;
		stepY = (int)width / column;
		
		x = boundingBox.x - 0.5;
		y = boundingBox.y - 0.5;
		
		for(int i = 0; i <= row -1; i ++ ){
			splitX.add(x);
			x += stepX;			
		}
		
		for(int i = 0; i <= column -1; i++){
			splitY.add(y);
			y += stepY;
		}
		
		
		x = boundingBox.s + 0.5;
		y = boundingBox.t + 0.5;
		
		splitX.add(x);
		splitY.add(y);
		
		isSplit = true;
		hashSubGraphs = new HashMap<Int2, Graph>();
		
		try {
			splitGraph();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//split graph into 1 conciseGraph and several subGraph;
	
	private void splitGraph() throws FileNotFoundException, UnsupportedEncodingException{
		int x = splitX.size() -1;
		int y = splitY.size() -1;
		conciseGraph = new Graph(x , y);
		conciseGraph.boundingBox = new Int4(0,0,x-1,y-1);
		conciseGraph.heads = (ArrayList<direction>) this.heads.clone();
		conciseGraph.tails = (ArrayList<direction>) this.tails.clone();
		subGraphs = new ArrayList<Graph>();
		
		
		Double x0,y0,x1,y1; //left down, right up
		
		for(int i = 0; i <= splitX.size()-2; i++){
			for(int j = 0; j <= splitY.size()-2; j++ ){
				Graph subGraph = new Graph();
				subGraph.boundingBox = new Int4(M,M,-M,-M);
				ArrayList<Node> subGraphNodes = new ArrayList<Node>();
				HashMap<Node,Node> gNtosubN = new HashMap<Node,Node>();
				ArrayList<Int4> wallList = new ArrayList<Int4>();
				ArrayList<Int4> holeList = new ArrayList<Int4>();
				
				x0 = splitX.get(i); 
				y0 = splitY.get(j);
				x1 = splitX.get(i+1);
				y1 = splitY.get(j+1);
				
				
				
				for(Node n:nodes){
					
					
					if( n.coord.x > x0 && n.coord.y > y0 && n.coord.x <x1 && n.coord.y < y1){
						Node subNode = new Node(n);
						gNtosubN.put(n, subNode);
						subGraphNodes.add(subNode);						
					}
				}
				
				for(Node n:nodes){
					
					if( n.coord.x > x0 && n.coord.y > y0 && n.coord.x <x1 && n.coord.y < y1){
						if(n == entrance){
							subGraph.entrance = gNtosubN.get(n);
						}
						
						if(n == exit){
							subGraph.exit   = gNtosubN.get(n);
						}
						
						if(n.coord.x < subGraph.boundingBox.x)
							subGraph.boundingBox.x  = n.coord.x;
						if(n.coord.y < subGraph.boundingBox.y)
							subGraph.boundingBox.y  = n.coord.y;
						if(n.coord.x > subGraph.boundingBox.s)
							subGraph.boundingBox.s  = n.coord.x;
						if(n.coord.y > subGraph.boundingBox.t)
							subGraph.boundingBox.t  = n.coord.y;
							
						for(Node adjNode:getJointNodes(n)){							
							if(!( adjNode.coord.x > x0 && adjNode.coord.y > y0 && adjNode.coord.x <x1 && adjNode.coord.y < y1)){
								
								if(adjNode == entrance){
									subGraph.entrance = gNtosubN.get(adjNode);
									
								}
								
								if(adjNode == exit){
									subGraph.exit   = gNtosubN.get(adjNode);
									
								}
								
								if(adjNode.coord.x < subGraph.boundingBox.x)
									subGraph.boundingBox.x  = adjNode.coord.x;
								if(adjNode.coord.y < subGraph.boundingBox.y)
									subGraph.boundingBox.y  = adjNode.coord.y;
								if(adjNode.coord.x > subGraph.boundingBox.s)
									subGraph.boundingBox.s  = adjNode.coord.x;
								if(adjNode.coord.y > subGraph.boundingBox.t)
									subGraph.boundingBox.t  = adjNode.coord.y;
								
								Node brinkNode = new Node(adjNode);
								subGraphNodes.add(brinkNode);
								gNtosubN.put(adjNode, brinkNode);
								Node nSubNode = gNtosubN.get(n); 
								brinkNode.setAdjNodes(nSubNode);
								nSubNode.setAdjNodes(brinkNode);
								Edge e = getEdge(n,adjNode);
								if(e instanceof Wall){
									wallList.add(e.coord);
								}
								else if(e instanceof Hole){
									holeList.add(e.coord);
								}
							}
							else{
								Node adjSubNode = gNtosubN.get(adjNode);
								Node nSubNode = gNtosubN.get(n);								
								nSubNode.setAdjNodes(adjSubNode);
							}
						}
					}
				}
				
				if(gNtosubN.containsKey(entrance))			
						conciseGraph.entrance = conciseGraph.getNode(i, j);
				
				if(gNtosubN.containsKey(exit))			
					conciseGraph.exit = conciseGraph.getNode(i, j);
					
					
				
				subGraph.getHashNodes(subGraphNodes);
				subGraph.getHashEdges(subGraphNodes, wallList, holeList);
				subGraph.center = new Int2(i,j);
				subGraph.width = subGraph.boundingBox.t - subGraph.boundingBox.y;
				subGraph.height = subGraph.boundingBox.s - subGraph.boundingBox.x;
				
				hashSubGraphs.put(subGraph.center, subGraph);
				//if(subGraph.width >= 10)
				//	subGraph.splitGraph(5,5);
				subGraphs.add(subGraph);
			}			
		}
		conciseGraph.setHeadsTails(direction.Source, direction.Terminal);
		try {
			concisePaths = conciseGraph.getPaths();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Graph subGraph:subGraphs){
			subGraph.setHeadsTails(concisePaths);
		
		}
		
		
		
	}
	
	public void getHashNodes(ArrayList<Node> graphNodes){
		hashNodes= new HashMap<Integer,Node>();
		for(Node n: graphNodes){
			hashNodes.put(n.hashValue(),n);
		}
		nodes = new ArrayList<Node>(hashNodes.values());
	}
	
	public void getHashEdges(ArrayList<Node> graphNodes, ArrayList<Int4> wallList, ArrayList<Int4> holeList ){
		hashEdges = new HashMap<Integer,Edge>();
		for(Node n:graphNodes){
			for(Node adjNode : n.getAdjNodes()){
				Edge e = new Edge(adjNode,n);
				if(!hashEdges.containsKey(e.hashValue())){
					hashEdges.put(e.hashValue(),e);
				}
			}
		}
		
		for(Int4 wall : wallList){
			setEdgeWall(wall);
		}
		
		for(Int4 hole:holeList){
			setEdgeHole(hole);
		}
		edges = new ArrayList<Edge>(hashEdges.values());
	}
	
	public void setHeadsTails(ArrayList<ArrayList<Int2>> concisePaths){
		
		Int2 first,second;
		for(ArrayList<Int2> pathVertex:concisePaths){
			//list all the segment in one path
			ArrayList<Int4> segments = new ArrayList<Int4>();
			for(int i = 0; i <= pathVertex.size()-1; i++ ){
				for(int j = i; j <=pathVertex.size()-1;j++){
					first = pathVertex.get(i);
					second = pathVertex.get(j);
					if(!first.equals(second)){
						Int4 segment;
						if(first.leftUnder(second))
							segment = new Int4(first,second);
						else
							segment = new Int4(second,first);
						
						segments.add(segment);
						i = j - 1;
						break;
					}
				}
				
			}
			
			
			//for each segment see if 
			for(int i = 0; i<= segments.size()-1; i++){
				Int4 segment = segments.get(i);
				Int2 temp1 = new Int2();
				Int2 temp2 = new Int2();
				direction head = direction.West;
				direction tail = direction.West;
				if(center.pointOnSegment(segment)){
					if(segment.isHorizontal()){
						setHeadsTails(direction.East, direction.West);
						break;
					}
					else{
						setHeadsTails(direction.North,direction.South);
						break;
					}
				}
				else if(center.pointIsSegVertex(segment)){
					Int4 nextSeg;
					
					if(i == segments.size()-1){
						if(segment.x == center.x && segment.y == center.y){
							temp1.x = segment.s;
							temp1.y = segment.t;
						}
						else{
							temp1.x = segment.x;
							temp1.y = segment.y;
						}
						
						if(temp1.x < center.x)
							head = direction.South;
						if(temp1.x > center.x)
							head = direction.North;
						if(temp1.y < center.y)
							head = direction.West;
						if(temp1.y > center.y)
							head = direction.East;
						
						if(this.exit!= null)
							tail = direction.Terminal;
						else
							tail = direction.Source;
						
						setHeadsTails(head, tail);
						break;			
					}
					else{
						nextSeg= segments.get(i+1);
						if(!(center.pointIsSegVertex(nextSeg))){
							
							if(segment.x == center.x && segment.y == center.y){
								temp1.x = segment.s;
								temp1.y = segment.t;
							}
							else{
								temp1.x = segment.x;
								temp1.y = segment.y;
							}
							
							if(temp1.x < center.x)
								head = direction.South;
							if(temp1.x > center.x)
								head = direction.North;
							if(temp1.y < center.y)
								head = direction.West;
							if(temp1.y > center.y)
								head = direction.East;
							
							if(this.exit!= null)
								tail = direction.Terminal;
							else
								tail = direction.Source;
							
							setHeadsTails(head, tail);
							break;					
						}
						else {
							
							
							
							if(segment.x == center.x && segment.y == center.y){
								temp1.x = segment.s;
								temp1.y = segment.t;
							}
							else{
								temp1.x = segment.x;
								temp1.y = segment.y;
							}
							
							if(nextSeg.x == center.x && nextSeg.y == center.y){
								temp2.x = nextSeg.s;
								temp2.y = nextSeg.t;
							}
							else{
								temp2.x = nextSeg.x;
								temp2.y = nextSeg.y;
							}
							if(temp1.x < center.x)
								head = direction.South;
							if(temp1.x > center.x)
								head = direction.North;
							if(temp1.y < center.y)
								head = direction.West;
							if(temp1.y > center.y)
								head = direction.East;
							
							if(temp2.x < center.x)
								tail = direction.South;
							if(temp2.x > center.x)
								tail = direction.North;
							if(temp2.y < center.y)
								tail = direction.West;
							if(temp2.y > center.y)
								tail = direction.East;
							
							setHeadsTails(head, tail);
							
							
							break;
						}
					}
						
					
					
				}
				
			}
			
		}
	}
	
	public void setHeadsTails(direction head,direction tail){
		// if head and tail are already in the queue, do nothing
		
		if(heads.contains(head)){
			int i = heads.indexOf(head);
			if(tails.get(i) == tail)
				return;			
		}
		
		if(tails.contains(head)){
			int j = tails.indexOf(head);
			if(heads.get(j) == tail)
				return;
		}
		
		//else add tails and head
		
		heads.add(head);
		tails.add(tail);
		
	}
	
	public void setHeads(ArrayList<direction> h){
		heads = h;
	}
	
	public void setTails(ArrayList<direction> t){
		tails = t;
	}
	private Graph extractSubGraph( ArrayList<Node> nodesForSub){
		// edges are from graph
		Graph subGraph = new Graph();
		ArrayList<Edge> subEdges = new ArrayList<Edge>();
		ArrayList<Node> subNodes = new ArrayList<Node>();
		
		for(Node n:nodesForSub){
			Node subNode = new Node(n);
			for(Node adjNode:getCnctNodes(n)){
				if(nodesForSub.contains(adjNode)){
					subNode.setAdjNodes(adjNode);					
					Edge subEdge = new Edge(getEdge(n,adjNode));
					subEdges.add(subEdge);
				}
			}
		}
		
		subGraph.edges = subEdges;
		subGraph.nodes = subNodes;
		return subGraph;
	}
	
	public Graph getSubGraph(Int2 leftLow, Int2 rightUpper){
		return extractSubGraph(nodesForSubGraph(leftLow, rightUpper));
	}
	
	private ArrayList<Node> nodesForSubGraph(Int2 leftLow, Int2 rightUpper){
		ArrayList<Node> nodesForSub = new ArrayList<Node>();
		
		for(Node node:nodes){
			if((leftLow.x <= node.coord.x && node.coord.x <= rightUpper.x)
					&&(leftLow.y <= node.coord.y && node.coord.y <= rightUpper.y)){
				nodesForSub.add(node);
			}
		}
		
		return nodesForSub;
	}
	

	
	
	
	
	public void init(int w, int h){		
		width = w;
		height = h;
		
		nodes = new ArrayList<Node>();
		hashEdges = new HashMap<Integer, Edge>(); 
		
		for(int i = 0; i<= w * h -1; i++){
			Node node = new Node();
			nodes.add(node);
		}
		
		//give number to each Node; Hash nodes	
		
		int id = 0;
		hashNodes = new HashMap<Integer, Node>();
		for(int i = 0; i< height; i++){
			for(int j =0; j < width ; j ++){
				nodes.get(id).setNumber(id);
				nodes.get(id).setCoordinate(i, j);
				hashNodes.put(i*100+j, nodes.get(id));
				id ++;
			}
		}
		
		
		
		//assign joint nodes to each node
		for(int i = 0; i< height; i++){
			for(int j =0; j < width; j ++){
				if(i>=1)
					getNode(i,j).setAdjNodes(getNode(i-1,j));
				if(j>=1)
					getNode(i,j).setAdjNodes(getNode(i,j-1));
				if(j<width-1)
					getNode(i,j).setAdjNodes(getNode(i,j+1));
				if(i<height-1)
					getNode(i,j).setAdjNodes(getNode(i+1,j));
			}
		}
		
		getHashEdges();
		
		
		entrance = nodes.get(0);
		exit = nodes.get(height * width - 1);
		//exit = nodes[2];
	}
	
	public void initCut(int w, int h){
		//Random rnd = new Random();
		//width = rnd.nextInt(upBound)+lowBound;
		//height = rnd.nextInt(upBound)+lowBound;
		
		width = w;
		height = h;
		
		//nodes = new Node[width * height];
		hashEdges = new HashMap<Integer, Edge>(); 
		
		for(int i = 0; i< nodes.size();i++){
			Node node = new Node();
			nodes.add(node);
		}
		
		//give number to each Node; Hash nodes	
		
		int id = 0;
		hashNodes = new HashMap<Integer, Node>();
		for(int i = 0; i< height; i++){
			for(int j =0; j < width ; j ++){
				nodes.get(id).setNumber(id);
				nodes.get(id).setCoordinate(i, j);
				hashNodes.put(i*100+j, nodes.get(id));
				id ++;
			}
		}
		
		
		
		//assign joint nodes to each node
		for(int i = 0; i< height; i++){
			for(int j =0; j < width; j ++){
				if(i>=1)
					getNode(i,j).setAdjNodes(getNode(i-1,j));
				if(j>=1)
					getNode(i,j).setAdjNodes(getNode(i,j-1));
				if(j<width-1)
					getNode(i,j).setAdjNodes(getNode(i,j+1));
				if(i<height-1)
					getNode(i,j).setAdjNodes(getNode(i+1,j));
			}
		}
		
		getHashEdgesCut();
		
		for(Edge edge:edges){
			if((edge.coord.y == 0 &&edge.coord.t ==1 ) 
					|| (edge.coord.x == height-2 && edge.coord.s == height-1 )){
				brinkL.add(edge);
			}
			else if((edge.coord.y == width -2 &&edge.coord.t == width -1  ) ||
					(edge.coord.x == 0 && edge.coord.s == 1 )){
				brinkR.add(edge);
			}
		}
		
		entrance = nodes.get(0);
		exit = nodes.get(height * width - 1);
		//exit = nodes[2];
	}
	

	private<K,T> void replaceHashMap(HashMap<K,T> hashMap, T o, K key){
		hashMap.put(key, o);
	}
	
	private void getHashEdges(){
		
		hashEdges = new HashMap<Integer, Edge>();
		edges = new ArrayList<Edge>();
		int i = 0;
		for(Node node:nodes){
			ArrayList<Node> adjNodes = node.getAdjNodes();
			for(Node adjNode: adjNodes){
				if(node.number < adjNode.number){
					Edge edge = new Edge(i);i++;
					edge.coord.x = node.coord.x;
					edge.coord.y = node.coord.y;
					edge.coord.s = adjNode.coord.x;
					edge.coord.t = adjNode.coord.y;
					if(node.coord.x == adjNode.coord.x)
						edge.isHorizontal = true;
					else
						edge.isHorizontal = false;
					edges.add(edge);
					hashEdges.put(hash2Nodes(adjNode,node), edge);
					
					//hashTarEdges.put(edge.hashValue(), edge);
				}
			}
		}
		
		edges = new ArrayList<Edge>(hashEdges.values());
	}
	
private void getHashEdgesCut(){
		
		hashEdges = new HashMap<Integer, Edge>();
		edges = new ArrayList<Edge>();
		int i = 0;
		for(Node node:nodes){
			ArrayList<Node>adjNodes = node.getAdjNodes();
			for(Node adjNode: adjNodes){
				if(node.number < adjNode.number){
					Edge edge = new Edge(i);i++;
					edge.coord.x = node.coord.x;
					edge.coord.y = node.coord.y;
					edge.coord.s = adjNode.coord.x;
					edge.coord.t = adjNode.coord.y;
					if(node.coord.x == adjNode.coord.x)
						edge.isHorizontal = true;
					else
						edge.isHorizontal = false;
					if(!(edge.isHorizontal && (edge.coord.x == 0 || edge.coord.x == height -1)) && 
							!(edge.isHorizontal == false && (edge.coord.y == 0 || edge.coord.y == width -1)))
						{hashEdges.put(hash2Nodes(adjNode,node), edge);}	
					
				}
			}
		}
		
		edges = new ArrayList<Edge>(hashEdges.values());
	}
	
	
	
	

	
	
	public String setILPObj(){
		String obj = "";
		for(Edge e:getJointEdges(entrance)){
			obj += "+" + hashWVariables.get(e);
		}
		return obj;
	}
	
	
	public ArrayList<Node> getPathNode(Graph g, ArrayList<Int2> pathVertex){
		ArrayList<Node> pathNode = new ArrayList<Node>();
		Int2 first;
		Int2 second;
		
		for(int i = 0; i <= pathVertex.size() -2; i++){
			first = pathVertex.get(i);
			second = pathVertex.get(i+1);
			
			if(first.equals(second)){
				continue;
			}
			
			if(first.x == second.x){
				if(first.y < second.y){
					for(int j = first.y; j<second.y; j++){
						pathNode.add(g.getNode(first.x, j));
					}
				}
				else if(first.y > second.y){
					for(int j = first.y; j>second.y; j--){
						pathNode.add(g.getNode(first.x, j));
					}
				}
			}
			else if(first.y == second.y){
				if(first.x < second.x){
					for(int j = first.x; j<second.x; j++){
						pathNode.add(g.getNode(j, first.y));
					}
				}
				else if(first.x > second.x){
					for(int j = first.x; j>second.x; j--){
						pathNode.add(g.getNode(j, first.y));
					}
				}
			}
			
			
			
		}
			
		
		return pathNode;
	}
	
	public ArrayList<ArrayList<Int2>> getSplitPaths(){
		ArrayList<ArrayList<Int2>> ps = new ArrayList<ArrayList<Int2>>();;
		ArrayList<Int2> p = null;
		ArrayList<Node> pathNode;
		Node node;
		
		Graph graph;
		for(Graph subGraph: subGraphs){			
			try {
				subGraph.pathsVertex =  subGraph.getPaths();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		ArrayList<ArrayList<Int2>> ps1;
		
		for(ArrayList<Int2> cpathVertex : concisePaths){
			pathNode = getPathNode(conciseGraph, cpathVertex);
			node = pathNode.get(0);
			graph = hashSubGraphs.get(new Int2(node.coord.x, node.coord.y));
			ps1  = (ArrayList<ArrayList<Int2>>)graph.pathsVertex.clone();
			for(int i = 1; i<= pathNode.size()-1; i++ ){
				node = pathNode.get(i);
				graph = hashSubGraphs.get(new Int2(node.coord.x, node.coord.y));
				ps1 = joinPathGroups(ps1, graph.pathsVertex);				
		
			}
			ps.addAll(ps1);
		}
		
		
		
		return ps;
	}
	
	public ArrayList<ArrayList<Int2>> joinPathGroups(ArrayList<ArrayList<Int2>> firstG, ArrayList<ArrayList<Int2>> secondG ){
			ArrayList<ArrayList<Int2>> ps =  new ArrayList<ArrayList<Int2>>();
			ArrayList<ArrayList<Int2>> temp;
			ArrayList<ArrayList<Int2>> first = (ArrayList<ArrayList<Int2>>)firstG.clone();
			ArrayList<ArrayList<Int2>> second = (ArrayList<ArrayList<Int2>>)secondG.clone();
			ArrayList<ArrayList<Int2>> collector = new ArrayList<ArrayList<Int2>>();
			
			ArrayList<Integer> secondInclude = new ArrayList<Integer>();
			
			assert(!(first.size() == 0 || second.size() == 0));
			
			int mark = -1;
			ArrayList<Int2> p1,p2,p1Np2;
			
			// for each p in first, find a match in 2
			for( int i = 0; i <= first.size()-1; i++){
				p1 = first.get(i);
				for(int j = 0; j <= second.size()-1; j++){
					p2 = second.get(j);
					if(pathMatch(p1,p2)){
						if(secondInclude.contains(j)){
							mark = j;
							continue;
						}
						else{
							p1Np2 = jointPath(p1,p2);
							ps.add(p1Np2);							
							secondInclude.add(j);
							mark = -2;
							break;
						}
							
					}
				}
				
				if(mark != -2){
					p2=second.get(mark);
					p1Np2 = jointPath(p1,p2);
					ps.add(p1Np2);
					mark = -1;
				}
			}
			
			for(int j = 0; j <= second.size()-1;j++){
				if(secondInclude.contains(j)){
					continue;
				}
				
				p2 = second.get(j);
				for(int i = 0; i <= first.size()-1;i-- ){
					p1 = first.get(i);
					if(pathMatch(p1,p2)){
						p1Np2 = jointPath(p1,p2);
						ps.add(p1Np2);
						break;
					}
				}
			}
				
				
			
			
			return ps;
	}
	
	
	// return if two path can join together	
	
	public boolean pathMatch(ArrayList<Int2> first, ArrayList<Int2> second){
		boolean en = false;
		Node firstHead = getNode(first.get(0));
		Node firstTail = getNode(first.get(first.size()-1));
		Node secondHead = getNode(second.get(0));
		Node secondTail = getNode(second.get(first.size()-1));
		
		if(getEdge(firstTail,secondHead) != null ||
				getEdge(firstTail, secondTail) != null ||
				getEdge(firstHead,secondHead) != null ||
				getEdge(firstHead,secondTail) != null
				)
			en = true;
		
		
		return en ;
		
	}
	
	public <T> void  uniqueElement(ArrayList<T> a){
		ArrayList<T> temp = new ArrayList<T>();
		temp.clear();
		temp.add(a.get(0));
		for(int i = 0; i <= a.size()-1; i ++){
			if(!a.get(i).equals(temp.get(temp.size()-1))){
				temp.add(a.get(i));
			}
		}
		
		a.clear();
		for(T t:temp){
			a.add(t);
		}
	}
	
	public <T> void reverseArray(ArrayList<T> a){
		ArrayList<T> temp = new ArrayList<T>();
		for(int i = a.size()-1; i>=0; i--){
			temp.add(a.get(i));
		}
		
		a.clear();
		for(int i = 0; i <= temp.size()-1; i++){
			a.add(temp.get(i));
		}
	}
	
	public <T> ArrayList<T> join2Array(ArrayList<T> a, ArrayList<T> b){
		ArrayList<T> temp = new ArrayList<T>();
		for(int i = 0; i <= a.size()-1; i ++){
			temp.add(a.get(i));
		}
		
		for(int i = 0; i <= b.size()-1; i++){
			temp.add(b.get(i));
		}
		
		
		return temp;
		
	}
	
	public ArrayList<Int2> jointPath(ArrayList<Int2> first, ArrayList<Int2> second){
		ArrayList<Int2> p = null;
		ArrayList<Int2> firstClone = (ArrayList<Int2>)first.clone();
		ArrayList<Int2> secondClone = (ArrayList<Int2>)second.clone();
		
		uniqueElement(firstClone);
		uniqueElement(secondClone);
		
		
		

		assert(pathMatch(firstClone,secondClone));
		Node firstHead = getNode(firstClone.get(0));
		Node firstTail = getNode(firstClone.get(firstClone.size()-1));
		Node secondHead = getNode(secondClone.get(0));
		Node secondTail = getNode(secondClone.get(secondClone.size()-1));
		Int2 vertex1, vertex2;
		
		if(getEdge(firstTail,secondHead)!= null){
			firstClone.remove(firstClone.size()-1);
			p = join2Array(firstClone,secondClone);
		}
		else if(getEdge(firstTail, secondTail) != null){
			reverseArray(secondClone);
			firstClone.remove(firstClone.size()-1);
			p = join2Array(firstClone,secondClone);			
			
		}
		else if(getEdge(firstHead,secondHead) != null){
			reverseArray(firstClone);
			firstClone.remove(firstClone.size()-1);
			p = join2Array(firstClone,secondClone);	
			
		}
		else if(getEdge(firstHead,secondTail) != null){
			reverseArray(firstClone);
			reverseArray(secondClone);
			firstClone.remove(firstClone.size()-1);
			p = join2Array(firstClone,secondClone);	
		}
		
		
		return p;
	}
	
	public ArrayList<ArrayList<Int2>> getPaths() throws IOException{
		ArrayList<ArrayList<Int2>> ps= new ArrayList<ArrayList<Int2>>();
		
		if(isSplit){
			
			ps = (ArrayList<ArrayList<Int2>>) getSplitPaths().clone();
			return ps;
		}
		
		
		getHeadTailPath();
		writeILP();
		for(int path = 0; path <= maxPaths -1; path++  ){
			ArrayList<Int2> p = new ArrayList<Int2>(); 
			for(int joint = 0; joint <= numberOfJoints -1; joint ++){
				String x = "x" + path + joint;
				String y = "y" + path + joint;
				
				int xValue, yValue;
				xValue = pathReuslts.get(x);
				yValue = pathReuslts.get(y);
				
				p.add(new Int2(xValue,yValue));						
			}
			ps.add(p);
		}
		
		return ps;
	}
	
	public ArrayList<Node> getDirNode(direction dir){
		ArrayList<Node> dirNode = new ArrayList<Node>();
		switch(dir){
		case West:
			for(Node n:nodes){
				if(n.coord.y == boundingBox.y){
					dirNode.add(n);
				}
			}
			break;
			
		case East:
			for(Node n:nodes){
				if(n.coord.y == boundingBox.t){
					dirNode.add(n);
				}
			}
			break;
			
		case North:
			for(Node n:nodes){
				if(n.coord.x == boundingBox.s){
					dirNode.add(n);
				}
			}
			break;
			
		case South:
			for(Node n:nodes){
				if(n.coord.x == boundingBox.x){
					dirNode.add(n);
				}
			}
			break;
			
		case Source:
			dirNode.add(entrance);
			break;
		case Terminal:
			dirNode.add(exit);
			break;
		}
		
		
		return dirNode;
	}
	
	public void getHeadTailPath(){
		variables = new ArrayList<String>();
		variableTypes = new ArrayList<Integer>();
		String x; //x position of a joint 
		String y; //y position of a joint
		
		int numPath = 0;
		for(int i  = 0; i <= heads.size()-1; i++){
			ArrayList<Node> headNodes = getDirNode(heads.get(i));
			ArrayList<Node> tailNodes = getDirNode(heads.get(i));
			numPath += max(headNodes.size()-1,tailNodes.size()-1);			
		}
		
		numPath = max(2,numPath);
		
		
	
		
		String constraint = "";
		
		HashMap<Integer, String> hashX = new HashMap<Integer, String>();
		HashMap<Integer, String> hashY= new HashMap<Integer, String>();
		
		
		
		//init variables
		
		for(int i=0; i <= numPath -1; i++){
			for(int joint =0; joint<= numberOfJoints -1 ; joint++){
				x = "x" + i + joint;
				hashX.put(hash2Int(i,joint), x);
				variables.add(x);
				variableTypes.add(0);
				
				y = "y" + i + joint;
				hashY.put(hash2Int(i,joint), y);
				variables.add(y);
				variableTypes.add(0);				

				
			}
		}
		
		for(int path = 0; path <= numPath -1; path++){
			int countHead = 0;
			int countTail = 0;
			int countHeadGroup = 0;
			int countTailGroup = 0;
			String useOneHead = "";
			String useOneTail = "";
			String useHeadGroup = "";
			String useTailGroup = "";
			String useOneHeadTotal = "";
			String useOneTailTotal = "";
			for(int i  = 0; i <= heads.size()-1; i++){
				ArrayList<Node> headNodes = getDirNode(heads.get(i));
				ArrayList<Node> tailNodes = getDirNode(tails.get(i));
				String headGroup = "headGroup" + "p" + path + "gp" + i;
				String tailGroup = "tailGroup" + "p" + path + "tp" + i;
				variables.add(headGroup);variableTypes.add(1);
				variables.add(tailGroup);variableTypes.add(1);
				
				useHeadGroup += " + "+ headGroup;
				useTailGroup += " + "+ tailGroup;
				
				useOneHead = "";
				useOneTail = "";
				
				ILP.add(headGroup + " - " + tailGroup + " = 0 ");
				
				
				String useHead;
				
				
				constraint = "";
				
				x = hashX.get(hash2Int(path,0));
				y = hashY.get(hash2Int(path,0));
				countTailGroup = 0;
				countHeadGroup = 0;
				for(Node head:headNodes){
					
					useHead = "useHead" + "p" + path+ "gp" + i + "Head" + head.coord.x + head.coord.y ;
					variables.add(useHead);variableTypes.add(1);
					
						
						// y0 = 0, x0 =edge.x
						ILP.add(x + " - " + M +" " + useHead + " <= " + (head.coord.x));
						ILP.add(x + " + " + M + " " +useHead + " >= " + (head.coord.x));
						ILP.add(y + " - " + M + " " +useHead + " <= " + (head.coord.y));
						ILP.add(y + " + " + M + " " +useHead + " >= " + (head.coord.y));
						
					
					countHead++;
					countHeadGroup++;
					useOneHead += " + " + useHead;
					useOneHeadTotal += " + " + useHead;
				}	
				constraint = useOneHead + " + " + headGroup + " = " + countHeadGroup;   
				ILP.add(constraint);
				constraint = "";
			
				x = hashX.get(hash2Int(path,numberOfJoints-1));
				y = hashY.get(hash2Int(path,numberOfJoints-1));
				String useTail;
				
			
				constraint = "";
				for(Node tail:tailNodes){
					useTail = "useTail" + "p" + path + "gp" + i + "tail" + tail.coord.x + tail.coord.y ;
					variables.add(useTail);variableTypes.add(1);

					ILP.add(x + " - " + M +" " + useTail + " <= " + (tail.coord.x));
					ILP.add(x + " + " + M + " " +useTail + " >= " + (tail.coord.x));
					ILP.add(y + " - " + M + " " +useTail + " <= " + (tail.coord.y));
					ILP.add(y + " + " + M + " " +useTail + " >= " + (tail.coord.y));
					
					countTail++;
					countTailGroup++;
					useOneTail  += " + " + useTail;
					useOneTailTotal += " + " + useTail;
				}	
				constraint = useOneTail + " + " + tailGroup + " = " + countTailGroup;   
				ILP.add(constraint);
				constraint = "";
			}
			
				
				
				// 0=<x(j)-x(j-1) <= M * dr
				
			
			
			ILP.add(useOneHeadTotal + " = " + (countHead -1));
			ILP.add(useOneTailTotal + " = " + (countTail -1));
			
			ILP.add(useTailGroup + " > 0");
			ILP.add(useHeadGroup + " > 0");
		}
		
		for(int path = 0; path <= numPath -1; path++ ){
			for( int joint =0; joint <= numberOfJoints-1; joint++ ){
				x = hashX.get(hash2Int(path,joint));
				y = hashY.get(hash2Int(path,joint));
			
				if(joint > 0){
					String lastJointX = hashX.get(hash2Int(path,joint-1));
					String lastJointY = hashY.get(hash2Int(path,joint-1));
					String xEqualslx, yEqualsly;
					xEqualslx = "xEqualslx" + path + joint;
					yEqualsly = "yEqualsly" + path + joint;
					variables.add(xEqualslx);variableTypes.add(1);
					variables.add(yEqualsly);variableTypes.add(1);
					// -M * binary < = x - lx <= M * binary
					// -M * binary < = y - ly <= M * binary
					ILP.add(x + " - " + lastJointX + " + "+M + " "+ xEqualslx + " >= 0"  );
					ILP.add(x + "  - " + lastJointX + " - "+ M + " "+ xEqualslx + " <= 0"  );
					ILP.add(y + " - " + lastJointY + " + "+M + " "+ yEqualsly + " >= 0"  );
					ILP.add(y + "  - " + lastJointY + " - "+M + " "+ yEqualsly + " <= 0"  );
					ILP.add(xEqualslx + " + "  + yEqualsly + " <= 1");					
					
				}
				
				if(joint != 0 && joint != numberOfJoints-1){
					// 0 <= x <= height   0 <= y <= widht 
					ILP.add(x + " >= " + this.boundingBox.x);
					ILP.add(x + " <= " + this.boundingBox.s);
					ILP.add(y + " >= " + this.boundingBox.y);
					ILP.add(y + " <= " + this.boundingBox.t);
			

				}
				
				
				//neither 2 of bend cross each other
				if(joint >= 2 && joint <= numberOfJoints - 2){
					for(int lastJoint = 0; lastJoint <= joint-2; lastJoint++){
						String ux1x2,ux2x1,uy1y2,uy2y1;
						
						ux1x2 = "u" + path +"x" + joint +"x"+ lastJoint; // x1 > x2?
						ux2x1 = "u" + path +"x" + lastJoint +"x"+ joint;
						uy1y2 = "u" + path +"y" + joint +"y"+ lastJoint;
						uy2y1 = "u" + path +"y" + lastJoint +"y"+ joint;
						variables.add(ux1x2);variableTypes.add(1);
						variables.add(ux2x1);variableTypes.add(1);
						variables.add(uy1y2);variableTypes.add(1);
						variables.add(uy2y1);variableTypes.add(1);
						
						String jointLX = hashX.get(hash2Int(path,joint));						
						String jointLY = hashY.get(hash2Int(path,joint));
						String jointRX = hashX.get(hash2Int(path,joint+1));						
						String jointRY = hashY.get(hash2Int(path,joint+1));
						
						String lastJointLX = hashX.get(hash2Int(path,lastJoint));
						String lastJointLY = hashY.get(hash2Int(path,lastJoint));
						String lastJointRX = hashX.get(hash2Int(path,lastJoint+1));
						String lastJointRY = hashY.get(hash2Int(path,lastJoint+1));
					
						
						
						ILP.add(jointLX +" - " + lastJointLX + " - " + M + " " +ux1x2 + " < 0");
						ILP.add(jointRX +" - " + lastJointLX + " - " + M + " " +ux1x2 + " < 0");
						ILP.add(jointLX +" - " + lastJointRX + " - " + M + " " +ux1x2 + " < 0");
						ILP.add(jointRX +" - " + lastJointRX + " - " + M + " " +ux1x2 + " < 0");
						ILP.add(lastJointLX +" - " + jointLX + " - " + M + " " +ux2x1 + " < 0");
						ILP.add(lastJointLX +" - " + jointRX + " - " + M + " " +ux2x1 + " < 0");
						ILP.add(lastJointRX +" - " + jointLX + " - " + M + " " +ux2x1 + " < 0");
						ILP.add(lastJointRX +" - " + jointRX + " - " + M + " " +ux2x1 + " < 0");
						
						ILP.add(jointLY +" - " + lastJointLY + " - " + M + " " +uy1y2 + " < 0");						
						ILP.add(jointRY +" - " + lastJointLY + " - " + M + " " +uy1y2 + " < 0");
						ILP.add(jointLY +" - " + lastJointRY + " - " + M + " " +uy1y2 + " < 0");
						ILP.add(jointRY +" - " + lastJointRY + " - " + M + " " +uy1y2 + " < 0");
						ILP.add(lastJointLY +" - " + jointLY + " - " + M + " " +uy2y1 + " < 0");
						ILP.add(lastJointLY +" - " + jointRY + " - " + M + " " +uy2y1 + " < 0");
						ILP.add(lastJointRY +" - " + jointLY + " - " + M + " " +uy2y1 + " < 0");
						ILP.add(lastJointRY +" - " + jointRY + " - " + M + " " +uy2y1 + " < 0");
						
						ILP.add( ux1x2 + " + "+ ux2x1 + " + " +uy1y2 + " + " + uy2y1 + " <= 3"); 
						
					}
				}//neither 2 of bend cross each other
				
			
				
			}
		
		}
		
		for(Edge edge:edges){
			int edgeX = edge.coord.x;
			int edgeY = edge.coord.y;
			int edgeS = edge.coord.s;
			int edgeT = edge.coord.t;
			int count = 0;
			String binaryL,binaryR,binaryU,binaryD,jointX,jointY,nextJointX,nextJointY;
			if(edge instanceof Hole){
				
				//horizontal wall
				if (edge.coord.x == edge.coord.s){					
					
					
					for(int path = 0; path <= numPath -1 ; path++){
						for(int joint = 0; joint <= numberOfJoints-2; joint++){
							int nextJoint = joint + 1;						
							
							jointX = hashX.get(hash2Int(path,joint));
							jointY = hashY.get(hash2Int(path,joint));
							nextJointX = hashX.get(hash2Int(path,nextJoint));
							nextJointY = hashY.get(hash2Int(path,nextJoint));
							binaryL = "binaryL" +"p" + path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
							variables.add(binaryL);variableTypes.add(1);
							binaryR = "binaryR" + "p" +path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
							variables.add(binaryR);variableTypes.add(1);
							// x0 + M * binaryR >=  s, x1  + M * binaryR >= s							
							// x0  -M * binaryL <=x, x1  - M * binaryL <= x 
							
							ILP.add(jointX +  " - "  + M  +" " + binaryL+ " <= " + edgeX );
							ILP.add(nextJointX +  " - " + M  +" " + binaryL+ " <= " + edgeX);
							ILP.add(jointX +  " + "  + M  +" " + binaryR+ " >= " + edgeS);
							ILP.add(nextJointX  + " + "  + M  +" " + binaryL+ " >= " +  edgeS);
							
							ILP.add(binaryL + " + " + binaryR + " <= 1" );
							
							
							
						}
						
					}
					
				}
				//vertical wall
				else{
					for(int path = 0; path <= numPath -1 ; path++){
						for(int joint = 0; joint <= numberOfJoints-2; joint++){
							int nextJoint = joint + 1;						
							
							jointX = hashX.get(hash2Int(path,joint));
							jointY = hashY.get(hash2Int(path,joint));
							nextJointX = hashX.get(hash2Int(path,nextJoint));
							nextJointY = hashY.get(hash2Int(path,nextJoint));
							binaryU = "binaryU" +"p" + path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
							variables.add(binaryU);variableTypes.add(1);
							binaryD = "binaryD" + "p" +path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
							variables.add(binaryD);variableTypes.add(1);
							// y0 + M * binaryU >=  y, y1  + M * binaryU >= t							
							// y0  -M * binaryD <=y, y1  - M * binaryD <= y 
							
							ILP.add(jointY +  " - "  + M  + " " +binaryD+ " <= " + edgeY );
							ILP.add(nextJointY +  " - " + M  +" " + binaryD+ " <= " + edgeY);
							ILP.add(jointY +  " + "  + M  + " " +binaryU+ " >= " + edgeT);
							ILP.add(nextJointY  + " + "  + M  +" " + binaryU+ " >= " +  edgeT);
							
							ILP.add(binaryU + " + " + binaryD + " <= 1" );
							
							
							
						}
						
					}
				}
					
			}
			else if (edge.coord.x == edge.coord.s){
			//if edge is horizontal
				//edge(i,j,i,j+1)
				
				constraint = "";
				
			
				for(int path = 0; path <= numPath -1 ; path++){
					for(int joint = 0; joint <= numberOfJoints-2; joint++){
						int nextJoint = joint + 1;						
						
						jointX = hashX.get(hash2Int(path,joint));
						jointY = hashY.get(hash2Int(path,joint));
						nextJointX = hashX.get(hash2Int(path,nextJoint));
						nextJointY = hashY.get(hash2Int(path,nextJoint));
						binaryL = "binaryL" +"p" + path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryL);variableTypes.add(1);
						binaryR = "binaryR" + "p" +path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryR);variableTypes.add(1);
						// -M * binary<=x0-i <= M*binary
						// -M * binary <= x1-i <= M*binary
						// y0 <= j + M * binary, y1 + M * binary >= j+1 
						ILP.add(jointX +  " + "  + M  + " " +binaryL+ " >= " + edgeX );
						ILP.add(jointX +  " - " + M  + " " +binaryL+ " <= " + edgeX);
						ILP.add(nextJointX +  " + "  + M  + " " +binaryL+ " >= " + edgeX);
						ILP.add(nextJointX  + " - "  + M  + " " +binaryL+ " <= " +  edgeX);
						
						ILP.add(jointY +   " - " + M + " " +binaryL + " <= "  + edgeY );
						ILP.add(nextJointY   + " + " + M + " " +binaryL + " >= " + edgeT);
						
						ILP.add(jointX  + " + "  + M  + " " +binaryR+ " >= " + edgeX);
						ILP.add(jointX  + " - "  + M  + " " +binaryR+ " <= " + edgeX);
						ILP.add(nextJointX  + " + "  + M  +" " + binaryR+ " >= "  + edgeX);
						ILP.add(nextJointX  + " - "  + M  +" " + binaryR+ " <= "  + edgeX);
						
						ILP.add(nextJointY  + " - " + M + " " +binaryR + " <= " + edgeY );
						ILP.add(jointY  + " + " + M + " " +binaryR + " >= " +  edgeT );
						
						
						constraint += " + " + binaryL + " + " + binaryR;
						count++;
					}
					
				}
				constraint += " <= " + (2*count-1);
				
					
					ILP.add(constraint);
				
				
				constraint = "";
				count = 0;
			}
			//if edge is vertical
			else if(edge.coord.y == edge.coord.t){
				//edge(i,j,i+1,j)
			
				
				for(int path = 0; path <= numPath -1 ; path++){
					for(int joint = 0; joint <= numberOfJoints-2; joint++){
						int nextJoint = joint + 1;
						
						
						jointX = hashX.get(hash2Int(path,joint));
						jointY = hashY.get(hash2Int(path,joint));
						nextJointX = hashX.get(hash2Int(path,nextJoint));
						nextJointY = hashY.get(hash2Int(path,nextJoint));
						binaryU = "binaryU" +"p" +path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryU);variableTypes.add(1);
						binaryD = "binaryD" + "p"+path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryD);variableTypes.add(1);
						// -M * binary<=y0-j <= M*binary
						// -M * binary <= y1-j <= M*binary
						// x0 <= i + M * binary, x1 + M * binary >= i+1 
						ILP.add(jointY  + " + "  + M  + " " +binaryU+ " >= "+ edgeY);
						ILP.add(jointY   + " - "  + M  +" " + binaryU+ " <=  " + edgeY);
						ILP.add(nextJointY  + " + "  + M  +" " + binaryU+ " >= " +  edgeY);
						ILP.add(nextJointY   + " - "  + M  +" " + binaryU+ " <= " + edgeY);
						
						ILP.add(jointX  + " - " + M + " " +binaryU + " <= " + edgeX );
						ILP.add(nextJointX + " + " + M + " " +binaryU + " >= "+ edgeS );
						
						ILP.add(jointY + " + "  + M  + " " +binaryD+ " >= "+ edgeY);
						ILP.add(jointY  + " - "  + M  + " " +binaryD+ " <= " + edgeY);
						ILP.add(nextJointY  + " + "  + M  +" " + binaryD+ " >= " + edgeY);
						ILP.add(nextJointY  + " - "  + M  + " " +binaryD+ " <= "+ edgeY );
						
						ILP.add(nextJointX   + " - " + M +" " + binaryD + " <= "+ edgeX );
						ILP.add(jointX  + " + " + M + " " +binaryD + " >= " + edgeS );
						constraint += " + " + binaryU + " + " + binaryD;
						count ++;
					}
				}
				
				constraint += " <= " + (2*(count) - 1);

					
				ILP.add(constraint);

				constraint = "";
				count = 0 ;
				
			}
			
		}
	
		
		
		//set obj 
		
		obj = "x00 + x01";
	}
	
	private int max(int i, int j) {
		// TODO Auto-generated method stub
		if(i >= j)
			return i;
		else
			return j;
	}




	public void getCuts(){
		variables = new ArrayList<String>();
		variableTypes = new ArrayList<Integer>();
		String x; //x position of a joint 
		String y; //y position of a joint
	
		
		String constraint = "";
		
		HashMap<Integer, String> hashX = new HashMap<Integer, String>();
		HashMap<Integer, String> hashY= new HashMap<Integer, String>();			
		
		//init variables
		
		for(int i=0; i <= maxPaths -1; i++){
			for(int joint =0; joint<= numberOfJoints -1 ; joint++){
				x = "x" + i + joint;
				hashX.put(hash2Int(i,joint), x);
				variables.add(x);
				variableTypes.add(0);
				
				y = "y" + i + joint;
				hashY.put(hash2Int(i,joint), y);
				variables.add(y);
				variableTypes.add(0);				

				
			}
		}
		
		for(int path = 0; path <= maxPaths -1; path++ ){
			for( int joint =0; joint <= numberOfJoints-1; joint++ ){
				x = hashX.get(hash2Int(path,joint));
				y = hashY.get(hash2Int(path,joint));
			
				if(joint > 0){
					String lastJointX = hashX.get(hash2Int(path,joint-1));
					String lastJointY = hashY.get(hash2Int(path,joint-1));
					String xEqualslx, yEqualsly;
					xEqualslx = "xEqualslx" + path + joint;
					yEqualsly = "yEqualsly" + path + joint;
					variables.add(xEqualslx);variableTypes.add(1);
					variables.add(yEqualsly);variableTypes.add(1);
					// -M * binary < = x - lx <= M * binary
					// -M * binary < = y - ly <= M * binary
					ILP.add(x + " - " + lastJointX + " + "+M + " "+ xEqualslx + " >= 0"  );
					ILP.add(x + "  - " + lastJointX + " - "+ M + " "+ xEqualslx + " <= 0"  );
					ILP.add(y + " - " + lastJointY + " + "+M + " "+ yEqualsly + " >= 0"  );
					ILP.add(y + "  - " + lastJointY + " - "+M + " "+ yEqualsly + " <= 0"  );
					ILP.add(xEqualslx + " + "  + yEqualsly + " <= 1");
					
					
					
					
				}
				
				if(joint==0){
					String brink;
					
					int count = 0;
					constraint = "";
					for(Edge edge:brinkL){
						brink = "brink" + "p" + path + "Start" + edge.coord.x + edge.coord.y + edge.coord.s + edge.coord.t;
						variables.add(brink);variableTypes.add(1);
						if(edge.isHorizontal){
							// y0 = 0, x0 =edge.x
							ILP.add(x + " - " + M +" " + brink + " <= " + (edge.coord.x));
							ILP.add(x + " + " + M + " " +brink + " >= " + (edge.coord.x));
							ILP.add(y + " - " + M + " " +brink + " <= " + (edge.coord.y));
							ILP.add(y + " + " + M + " " +brink + " >= " + (edge.coord.y));
							
						}
						else{
							// x0 = 0; y0 = edge.y
							ILP.add(x + " - " + M +" " + brink + " <=  " + (edge.coord.s));
							ILP.add(x + " + " + M +" " + brink + " >= " +(edge.coord.s));
							ILP.add(y + " - " + M +" " + brink + " <= " + (edge.coord.t));
							ILP.add(y + " + " + M +" " + brink + " >= " + (edge.coord.t));
						}
						count++;
						constraint  += " + " + brink;
					}	
					constraint += " <= " + (count - 1);
					ILP.add(constraint);
					constraint = "";
				}
				else if(joint == numberOfJoints -1){
					String brink;
					
					int count = 0;
					constraint = "";
					for(Edge edge:brinkR){
						brink = "brink" + "p" + path + "End" + edge.coord.x + edge.coord.y + edge.coord.s + edge.coord.t;
						variables.add(brink);variableTypes.add(1);

						if(edge.isHorizontal){
							// y0 = s, x0 = t
							ILP.add(x + " - " + M + " " +brink + " <= " + (edge.coord.s));
							ILP.add(x + " + " + M + " " +brink + " >= " + (edge.coord.s));
							ILP.add(y + " - " + M + " " +brink + " <= " + (edge.coord.t));
							ILP.add(y + " + " + M + " " +brink + " >= " + (edge.coord.t));

						}
						else{
							// x0 = x; y0 = y
							ILP.add(x + " - " + M + " " +brink + " <=  " + (edge.coord.x));
							ILP.add(x + " + " + M + " " +brink + " >= " +(edge.coord.x));
							ILP.add(y + " - " + M + " " +brink + " <= " + (edge.coord.y));
							ILP.add(y + " + " + M + " " +brink + " >= " + (edge.coord.y));
						}
						count++;
						constraint  += " + " + brink;
					}	
					constraint += " <= " + (count - 1);
					ILP.add(constraint);
					constraint = "";
				}
				
				// 0=<x(j)-x(j-1) <= M * dr
				else{
					// 0 <= x <= height   0 <= y <= widht 
					ILP.add(x + " >= 0");
					ILP.add(x + " <= " + (height-1));
					ILP.add(y + " >= 0");
					ILP.add(y + " <= " + (width-1));
			

				}
				
				//neither 2 of bend cross each other
				if(joint >= 2 && joint <= numberOfJoints - 2){
					for(int lastJoint = 0; lastJoint <= joint-2; lastJoint++){
						String ux1x2,ux2x1,uy1y2,uy2y1;
						
						ux1x2 = "u" + path +"x" + joint +"x"+ lastJoint; // x1 > x2?
						ux2x1 = "u" + path +"x" + lastJoint +"x"+ joint;
						uy1y2 = "u" + path +"y" + joint +"y"+ lastJoint;
						uy2y1 = "u" + path +"y" + lastJoint +"y"+ joint;
						variables.add(ux1x2);variableTypes.add(1);
						variables.add(ux2x1);variableTypes.add(1);
						variables.add(uy1y2);variableTypes.add(1);
						variables.add(uy2y1);variableTypes.add(1);
						
						String jointLX = hashX.get(hash2Int(path,joint));						
						String jointLY = hashY.get(hash2Int(path,joint));
						String jointRX = hashX.get(hash2Int(path,joint+1));						
						String jointRY = hashY.get(hash2Int(path,joint+1));
						
						String lastJointLX = hashX.get(hash2Int(path,lastJoint));
						String lastJointLY = hashY.get(hash2Int(path,lastJoint));
						String lastJointRX = hashX.get(hash2Int(path,lastJoint+1));
						String lastJointRY = hashY.get(hash2Int(path,lastJoint+1));
					
						
						
						ILP.add(jointLX +" - " + lastJointLX + " - " + M + " " +ux1x2 + " < 0");
						ILP.add(jointRX +" - " + lastJointLX + " - " + M + " " +ux1x2 + " < 0");
						ILP.add(jointLX +" - " + lastJointRX + " - " + M + " " +ux1x2 + " < 0");
						ILP.add(jointRX +" - " + lastJointRX + " - " + M + " " +ux1x2 + " < 0");
						ILP.add(lastJointLX +" - " + jointLX + " - " + M + " " +ux2x1 + " < 0");
						ILP.add(lastJointLX +" - " + jointRX + " - " + M + " " +ux2x1 + " < 0");
						ILP.add(lastJointRX +" - " + jointLX + " - " + M + " " +ux2x1 + " < 0");
						ILP.add(lastJointRX +" - " + jointRX + " - " + M + " " +ux2x1 + " < 0");
						
						ILP.add(jointLY +" - " + lastJointLY + " - " + M + " " +uy1y2 + " < 0");						
						ILP.add(jointRY +" - " + lastJointLY + " - " + M + " " +uy1y2 + " < 0");
						ILP.add(jointLY +" - " + lastJointRY + " - " + M + " " +uy1y2 + " < 0");
						ILP.add(jointRY +" - " + lastJointRY + " - " + M + " " +uy1y2 + " < 0");
						ILP.add(lastJointLY +" - " + jointLY + " - " + M + " " +uy2y1 + " < 0");
						ILP.add(lastJointLY +" - " + jointRY + " - " + M + " " +uy2y1 + " < 0");
						ILP.add(lastJointRY +" - " + jointLY + " - " + M + " " +uy2y1 + " < 0");
						ILP.add(lastJointRY +" - " + jointRY + " - " + M + " " +uy2y1 + " < 0");
						
						ILP.add( ux1x2 + " + "+ ux2x1 + " + " +uy1y2 + " + " + uy2y1 + " <= 3"); 
						
					}
				}//neither 2 of bend cross each other
				
			
				
			}
		
		}
		
		for(Edge edge:edges){
			int edgeX = edge.coord.x;
			int edgeY = edge.coord.y;
			int edgeS = edge.coord.s;
			int edgeT = edge.coord.t;
			int count = 0;
			String binaryL,binaryR,binaryU,binaryD,jointX,jointY,nextJointX,nextJointY;
			if(edge instanceof Hole){
				
				//horizontal wall
				if (edge.coord.x == edge.coord.s){					
					
					
					for(int path = 0; path <= maxPaths -1 ; path++){
						for(int joint = 0; joint <= numberOfJoints-2; joint++){
							int nextJoint = joint + 1;						
							
							jointX = hashX.get(hash2Int(path,joint));
							jointY = hashY.get(hash2Int(path,joint));
							nextJointX = hashX.get(hash2Int(path,nextJoint));
							nextJointY = hashY.get(hash2Int(path,nextJoint));
							binaryL = "binaryL" +"p" + path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
							variables.add(binaryL);variableTypes.add(1);
							binaryR = "binaryR" + "p" +path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
							variables.add(binaryR);variableTypes.add(1);
							// x0 + M * binaryR >=  s, x1  + M * binaryR >= s							
							// x0  -M * binaryL <=x, x1  - M * binaryL <= x 
							
							ILP.add(jointX +  " - "  + M  +" " + binaryL+ " <= " + edgeX );
							ILP.add(nextJointX +  " - " + M  +" " + binaryL+ " <= " + edgeX);
							ILP.add(jointX +  " + "  + M  +" " + binaryR+ " >= " + edgeS);
							ILP.add(nextJointX  + " + "  + M  +" " + binaryL+ " >= " +  edgeS);
							
							ILP.add(binaryL + " + " + binaryR + " <= 1" );
							
							
							
						}
						
					}
					
				}
				//vertical wall
				else{
					for(int path = 0; path <= maxPaths -1 ; path++){
						for(int joint = 0; joint <= numberOfJoints-2; joint++){
							int nextJoint = joint + 1;						
							
							jointX = hashX.get(hash2Int(path,joint));
							jointY = hashY.get(hash2Int(path,joint));
							nextJointX = hashX.get(hash2Int(path,nextJoint));
							nextJointY = hashY.get(hash2Int(path,nextJoint));
							binaryU = "binaryU" +"p" + path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
							variables.add(binaryU);variableTypes.add(1);
							binaryD = "binaryD" + "p" +path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
							variables.add(binaryD);variableTypes.add(1);
							// y0 + M * binaryU >=  y, y1  + M * binaryU >= t							
							// y0  -M * binaryD <=y, y1  - M * binaryD <= y 
							
							ILP.add(jointY +  " - "  + M  + " " +binaryD+ " <= " + edgeY );
							ILP.add(nextJointY +  " - " + M  +" " + binaryD+ " <= " + edgeY);
							ILP.add(jointY +  " + "  + M  + " " +binaryU+ " >= " + edgeT);
							ILP.add(nextJointY  + " + "  + M  +" " + binaryU+ " >= " +  edgeT);
							
							ILP.add(binaryU + " + " + binaryD + " <= 1" );
							
							
							
						}
						
					}
				}
					
			}
			else if (edge.coord.x == edge.coord.s){
			//if edge is horizontal
				//edge(i,j,i,j+1)
				
				constraint = "";
				
			
				for(int path = 0; path <= maxPaths -1 ; path++){
					for(int joint = 0; joint <= numberOfJoints-2; joint++){
						int nextJoint = joint + 1;						
						
						jointX = hashX.get(hash2Int(path,joint));
						jointY = hashY.get(hash2Int(path,joint));
						nextJointX = hashX.get(hash2Int(path,nextJoint));
						nextJointY = hashY.get(hash2Int(path,nextJoint));
						binaryL = "binaryL" +"p" + path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryL);variableTypes.add(1);
						binaryR = "binaryR" + "p" +path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryR);variableTypes.add(1);
						// -M * binary<=x0-i <= M*binary
						// -M * binary <= x1-i <= M*binary
						// y0 <= j + M * binary, y1 + M * binary >= j+1 
						ILP.add(jointX +  " + "  + M  + " " +binaryL+ " >= " + edgeX );
						ILP.add(jointX +  " - " + M  + " " +binaryL+ " <= " + edgeX);
						ILP.add(nextJointX +  " + "  + M  + " " +binaryL+ " >= " + edgeX);
						ILP.add(nextJointX  + " - "  + M  + " " +binaryL+ " <= " +  edgeX);
						
						ILP.add(jointY +   " - " + M + " " +binaryL + " <= "  + edgeY );
						ILP.add(nextJointY   + " + " + M + " " +binaryL + " >= " + edgeT);
						
						ILP.add(jointX  + " + "  + M  + " " +binaryR+ " >= " + edgeX);
						ILP.add(jointX  + " - "  + M  + " " +binaryR+ " <= " + edgeX);
						ILP.add(nextJointX  + " + "  + M  +" " + binaryR+ " >= "  + edgeX);
						ILP.add(nextJointX  + " - "  + M  +" " + binaryR+ " <= "  + edgeX);
						
						ILP.add(nextJointY  + " - " + M + " " +binaryR + " <= " + edgeY );
						ILP.add(jointY  + " + " + M + " " +binaryR + " >= " +  edgeT );
						
						
						constraint += " + " + binaryL + " + " + binaryR;
						count++;
					}
					
				}
				constraint += " <= " + (2*count-1);
				
					
					ILP.add(constraint);
				
				
				constraint = "";
				count = 0;
			}
			//if edge is vertical
			else if(edge.coord.y == edge.coord.t){
				//edge(i,j,i+1,j)
			
				
				for(int path = 0; path <= maxPaths -1 ; path++){
					for(int joint = 0; joint <= numberOfJoints-2; joint++){
						int nextJoint = joint + 1;
						
						
						jointX = hashX.get(hash2Int(path,joint));
						jointY = hashY.get(hash2Int(path,joint));
						nextJointX = hashX.get(hash2Int(path,nextJoint));
						nextJointY = hashY.get(hash2Int(path,nextJoint));
						binaryU = "binaryU" +"p" +path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryU);variableTypes.add(1);
						binaryD = "binaryD" + "p"+path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryD);variableTypes.add(1);
						// -M * binary<=y0-j <= M*binary
						// -M * binary <= y1-j <= M*binary
						// x0 <= i + M * binary, x1 + M * binary >= i+1 
						ILP.add(jointY  + " + "  + M  + " " +binaryU+ " >= "+ edgeY);
						ILP.add(jointY   + " - "  + M  +" " + binaryU+ " <=  " + edgeY);
						ILP.add(nextJointY  + " + "  + M  +" " + binaryU+ " >= " +  edgeY);
						ILP.add(nextJointY   + " - "  + M  +" " + binaryU+ " <= " + edgeY);
						
						ILP.add(jointX  + " - " + M + " " +binaryU + " <= " + edgeX );
						ILP.add(nextJointX + " + " + M + " " +binaryU + " >= "+ edgeS );
						
						ILP.add(jointY + " + "  + M  + " " +binaryD+ " >= "+ edgeY);
						ILP.add(jointY  + " - "  + M  + " " +binaryD+ " <= " + edgeY);
						ILP.add(nextJointY  + " + "  + M  +" " + binaryD+ " >= " + edgeY);
						ILP.add(nextJointY  + " - "  + M  + " " +binaryD+ " <= "+ edgeY );
						
						ILP.add(nextJointX   + " - " + M +" " + binaryD + " <= "+ edgeX );
						ILP.add(jointX  + " + " + M + " " +binaryD + " >= " + edgeS );
						constraint += " + " + binaryU + " + " + binaryD;
						count ++;
					}
				}
				
				constraint += " <= " + (2*(count) - 1);

					
				ILP.add(constraint);

				constraint = "";
				count = 0 ;
				
			}
			
		}
	
		
		
		//set obj 
		
		obj = "x00 + x01";
	}
	
	public void getOneLongestPath(){
		variables = new ArrayList<String>();
		variableTypes = new ArrayList<Integer>();
		String xVariable; //x position of a joint 
		String yVariable; //y position of a joint
	
		
		String constraint = "";
		
		HashMap<Integer, String> hashX = new HashMap<Integer, String>();
		HashMap<Integer, String> hashY= new HashMap<Integer, String>();		 

		String lastJointX;
		String lastJointY ;
		
		String jointLX;						
		String jointLY;
		String jointRX;						
		String jointRY;
		String jointX,jointY,nextJointX,nextJointY;
		String lastJointLX;
		String lastJointLY;
		String lastJointRX;
		String lastJointRY;
		
		
		//init variables
		
		
			for(int joint =0; joint<= numberOfJoints -1 ; joint++){
				xVariable = "x"  + joint;
				hashX.put(joint, xVariable);
				variables.add(xVariable);
				variableTypes.add(0);
				
				yVariable = "y" + joint;
				hashY.put(joint, yVariable);
				variables.add(yVariable);
				variableTypes.add(0);				

				
			}
		
		
		
			for( int joint =0; joint <= numberOfJoints-1; joint++ ){
				String xEqualslx, yEqualsly;
				xVariable = hashX.get(joint);
				yVariable = hashY.get(joint);
			
				if(joint > 0){
					lastJointX = hashX.get(joint-1);
					lastJointY = hashY.get(joint-1);
					
					xEqualslx = "xEqualslx" + joint;
					yEqualsly = "yEqualsly" + joint;
					variables.add(xEqualslx);variableTypes.add(1);
					variables.add(yEqualsly);variableTypes.add(1);
					// -M + " "  * binary < = x - lx <= M + " "  * binary
					// -M + " "  * binary < = y - ly <= M + " "  * binary
					ILP.add(xVariable + " - " + lastJointX + " + "+M + " "  + xEqualslx + " >= 0"  );
					ILP.add(xVariable + "  - " + lastJointX + " - "+ M + " "  + xEqualslx + " <= 0"  );
					ILP.add(yVariable + " - " + lastJointY + " + "+M + " " + yEqualsly + " >= 0"  );
					ILP.add(yVariable + "  - " + lastJointY + " - "+M + " "  + yEqualsly + " <= 0"  );
					ILP.add(xEqualslx + " + "  + yEqualsly + " <= 1");
					
					
					
					
				}
				
				if(joint==0){
					constraint = xVariable + " = "  + entrance.getCoordinate().x;
					ILP.add(constraint);
					constraint = yVariable + " = "  + entrance.getCoordinate().y;
					ILP.add(constraint);
					constraint = "";
							
				}
				else if(joint == numberOfJoints -1){
					constraint = xVariable + " = "  + exit.getCoordinate().x;
					ILP.add(constraint);
					constraint = yVariable + " = "  + exit.getCoordinate().y;
					ILP.add(constraint);
					constraint = "";
					

				}
				// 0=<x(j)-x(j-1) <= M + " "  * dr
				else{
					// 0 <= x <= height   0 <= y <= widht 
					ILP.add(xVariable + " >= 0");
					ILP.add(xVariable + " <= " + (height-1));
					ILP.add(yVariable + " >= 0");
					ILP.add(yVariable + " <= " + (width-1));
			

				}
				
				//neither 2 of bend cross each other
				if(joint >= 2 && joint <= numberOfJoints - 2){
					for(int lastJoint = 0; lastJoint <= joint-2; lastJoint++){
						String ux1x2,ux2x1,uy1y2,uy2y1;
						
						ux1x2 = "u" + "x" + joint +"x"+ lastJoint; // x1 > x2?
						ux2x1 = "u" + "x" + lastJoint +"x"+ joint;
						uy1y2 = "u" + "y" + joint +"y"+ lastJoint;
						uy2y1 = "u" + "y" + lastJoint +"y"+ joint;
						variables.add(ux1x2);variableTypes.add(1);
						variables.add(ux2x1);variableTypes.add(1);
						variables.add(uy1y2);variableTypes.add(1);
						variables.add(uy2y1);variableTypes.add(1);
						
						jointLX = hashX.get(joint);						
						jointLY = hashY.get(joint);
						jointRX = hashX.get(joint+1);						
						jointRY = hashY.get(joint+1);
						
						lastJointLX = hashX.get(lastJoint);
						lastJointLY = hashY.get(lastJoint);
						lastJointRX = hashX.get(lastJoint+1);
						lastJointRY = hashY.get(lastJoint+1);
					
						
						
						ILP.add(jointLX +" - " + lastJointLX + " - " + M + " "  + ux1x2 + " <= -1");
						ILP.add(jointRX +" - " + lastJointLX + " - " + M + " "  + ux1x2 + " <= -1");
						ILP.add(jointLX +" - " + lastJointRX + " - " + M + " "  + ux1x2 + " <= -1");
						ILP.add(jointRX +" - " + lastJointRX + " - " + M + " "  + ux1x2 + " <= -1");
						ILP.add(lastJointLX +" - " + jointLX + " - " + M + " "  + ux2x1 + " <= -1");
						ILP.add(lastJointLX +" - " + jointRX + " - " + M + " "  + ux2x1 + " <= -1");
						ILP.add(lastJointRX +" - " + jointLX + " - " + M + " "  + ux2x1 + " <= -1");
						ILP.add(lastJointRX +" - " + jointRX + " - " + M + " "  + ux2x1 + " <= -1");
						
						ILP.add(jointLY +" - " + lastJointLY + " - " + M + " "  + uy1y2 + " <= -1");						
						ILP.add(jointRY +" - " + lastJointLY + " - " + M + " "  + uy1y2 + " <= -1");
						ILP.add(jointLY +" - " + lastJointRY + " - " + M + " "  + uy1y2 + " <= -1");
						ILP.add(jointRY +" - " + lastJointRY + " - " + M + " "  + uy1y2 + " <= -1");
						ILP.add(lastJointLY +" - " + jointLY + " - " + M + " "  + uy2y1 + " <= -1");
						ILP.add(lastJointLY +" - " + jointRY + " - " + M + " "  + uy2y1 + " <= -1");
						ILP.add(lastJointRY +" - " + jointLY + " - " + M + " "  + uy2y1 + " <= -1");
						ILP.add(lastJointRY +" - " + jointRY + " - " + M + " "  + uy2y1 + " <= -1");
						
						ILP.add( ux1x2 + " + "+ ux2x1 + " + " +uy1y2 + " + " + uy2y1 + " <= 3"); 
						
					}
				}//neither 2 of bend cross each other
				
			
				
			}
			
		obj = "";
		for(Edge edge:edges){
			
			String edgeUsed = "eUsed" + edge.coord.x + edge.coord.y + edge.coord.s+ edge.coord.t;
			variables.add(edgeUsed);variableTypes.add(1);
			int edgeX = edge.coord.x;
			int edgeY = edge.coord.y;
			int edgeS = edge.coord.s;
			int edgeT = edge.coord.t;
			int count = 0;
			String binaryL,binaryR,binaryU,binaryD;
			if(edge instanceof Wall){
				
				//horizontal wall
				if (edge.coord.x == edge.coord.s){		
					
			
					for(int joint = 0; joint <= numberOfJoints-2; joint++){
						int nextJoint = joint + 1;						
						
						jointX = hashX.get(joint);
						jointY = hashY.get(joint);
						nextJointX = hashX.get(nextJoint);
						nextJointY = hashY.get(nextJoint);
						binaryL = "binaryL" +"j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryL);variableTypes.add(1);
						binaryR = "binaryR" + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryR);variableTypes.add(1);
						// x0 + M + " "  * binaryR >=  s, x1  + M + " "  * binaryR >= s							
						// x0  -M + " "  * binaryL <=x, x1  - M + " "  * binaryL <= x 
						
						ILP.add(jointX +  " - "  + M + " "   + binaryL+ " <= " + edgeX );
						ILP.add(nextJointX +  " - " + M + " "   + binaryL+ " <= " + edgeX);
						ILP.add(jointX +  " + "  + M + " "   + binaryR+ " >= " + edgeS);
						ILP.add(nextJointX  + " + "  + M + " "   + binaryL+ " >= " +  edgeS);
						
						ILP.add(binaryL + " + " + binaryR + " <= 1" );
						
						
						
					}
						
				}
					
				
				//vertical wall
				else{
					
					for(int joint = 0; joint <= numberOfJoints-2; joint++){
						int nextJoint = joint + 1;						
						
						jointX = hashX.get(joint);
						jointY = hashY.get(joint);
						nextJointX = hashX.get(nextJoint);
						nextJointY = hashY.get(nextJoint);
						binaryU = "binaryU" + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryU);variableTypes.add(1);
						binaryD = "binaryD" + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryD);variableTypes.add(1);
						// y0 + M + " "  * binaryU >=  y, y1  + M + " "  * binaryU >= t							
						// y0  -M + " "  * binaryD <=y, y1  - M + " "  * binaryD <= y 
						
						ILP.add(jointY +  " - "  + M + " "   + binaryD+ " <= " + edgeY );
						ILP.add(nextJointY +  " - " + M + " "   + binaryD+ " <= " + edgeY);
						ILP.add(jointY +  " + "  + M + " "   + binaryU+ " >= " + edgeT);
						ILP.add(nextJointY  + " + "  + M + " "   + binaryU+ " >= " +  edgeT);
						
						ILP.add(binaryU + " + " + binaryD + " <= 1" );
						
						
						
					}
						
					}
				
					
			}
			else if (edge.coord.x == edge.coord.s){
			//if edge is horizontal
				//edge(i,j,i,j+1)
				
				constraint = "";
				
			
				
					for(int joint = 0; joint <= numberOfJoints-2; joint++){
						int nextJoint = joint + 1;						
						
						jointX = hashX.get(joint);
						jointY = hashY.get(joint);
						nextJointX = hashX.get(nextJoint);
						nextJointY = hashY.get(nextJoint);
						binaryL = "binaryL" + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryL);variableTypes.add(1);
						binaryR = "binaryR"  + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryR);variableTypes.add(1);
						// -M + " "  * binary<=x0-i <= M + " " *binary
						// -M + " "  * binary <= x1-i <= M + " " *binary
						// y0 <= j + M + " "  * binary, y1 + M + " "  * binary >= j+1 
						ILP.add(jointX +  " + "  + M + " "   + binaryL+ " >= " + edgeX );
						ILP.add(jointX +  " - " + M + " "   + binaryL+ " <= " + edgeX);
						ILP.add(nextJointX +  " + "  + M + " "   + binaryL+ " >= " + edgeX);
						ILP.add(nextJointX  + " - "  + M + " "   + binaryL+ " <= " +  edgeX);
						
						ILP.add(jointY +   " - " + M + " "  + binaryL + " <= "  + edgeY );
						ILP.add(nextJointY   + " + " + M + " "  + binaryL + " >= " + edgeT);
						
						ILP.add(jointX  + " + "  + M + " "   + binaryR+ " >= " + edgeX);
						ILP.add(jointX  + " - "  + M + " "   + binaryR+ " <= " + edgeX);
						ILP.add(nextJointX  + " + "  + M + " "   + binaryR+ " >= "  + edgeX);
						ILP.add(nextJointX  + " - "  + M + " "   + binaryR+ " <= "  + edgeX);
						
						ILP.add(nextJointY  + " - " + M + " "  + binaryR + " <= " + edgeY );
						ILP.add(jointY  + " + " + M + " "  + binaryR + " >= " +  edgeT );
						
						
						constraint += " + " + binaryL + " + " + binaryR;
						count++;
					}
					
				
				constraint += " + " + edgeUsed + " <= " + (2*count);
				
					
				ILP.add(constraint);
				
				
				constraint = "";
				count = 0;
				obj +=  " + " + edgeUsed;
			}
			//if edge is vertical
			else if(edge.coord.y == edge.coord.t){
				//edge(i,j,i+1,j)
			
				
					for(int joint = 0; joint <= numberOfJoints-2; joint++){
						int nextJoint = joint + 1;
						
						
						jointX = hashX.get(joint);
						jointY = hashY.get(joint);
						nextJointX = hashX.get(nextJoint);
						nextJointY = hashY.get(nextJoint);
						binaryU = "binaryU"  + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryU);variableTypes.add(1);
						binaryD = "binaryD" + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryD);variableTypes.add(1);
						// -M + " "  * binary<=y0-j <= M + " " *binary
						// -M + " "  * binary <= y1-j <= M + " " *binary
						// x0 <= i + M + " "  * binary, x1 + M + " "  * binary >= i+1 
						ILP.add(jointY  + " + "  + M + " "   + binaryU+ " >= "+ edgeY);
						ILP.add(jointY   + " - "  + M + " "   + binaryU+ " <=  " + edgeY);
						ILP.add(nextJointY  + " + "  + M + " "   + binaryU+ " >= " +  edgeY);
						ILP.add(nextJointY   + " - "  + M + " "   + binaryU+ " <= " + edgeY);
						
						ILP.add(jointX  + " - " + M + " "  + binaryU + " <= " + edgeX );
						ILP.add(nextJointX + " + " + M + " "  + binaryU + " >= "+ edgeS );
						
						ILP.add(jointY + " + "  + M + " "   + binaryD+ " >= "+ edgeY);
						ILP.add(jointY  + " - "  + M + " "   + binaryD+ " <= " + edgeY);
						ILP.add(nextJointY  + " + "  + M + " "   + binaryD+ " >= " + edgeY);
						ILP.add(nextJointY  + " - "  + M + " "   + binaryD+ " <= "+ edgeY );
						
						ILP.add(nextJointX   + " - " + M + " "  + binaryD + " <= "+ edgeX );
						ILP.add(jointX  + " + " + M + " "  + binaryD + " >= " + edgeS );
						constraint += " + " + binaryU + " + " + binaryD;
						count ++;
					}
				
				
				constraint += " + " + edgeUsed + " <= " + (2*count);
				
				
				ILP.add(constraint);

				constraint = "";
				count = 0 ;
				obj +=  " + " + edgeUsed;
			}
			
		}
	
		
		
		//set obj 
		
		
	}
	
	public void getAcyclicILPExactRoute(){
		variables = new ArrayList<String>();
		variableTypes = new ArrayList<Integer>();
		String xVariable; //x position of a joint 
		String yVariable; //y position of a joint
	
		
		String constraint = "";
		
		HashMap<Integer, String> hashX = new HashMap<Integer, String>();
		HashMap<Integer, String> hashY= new HashMap<Integer, String>();		 

		String lastJointX;
		String lastJointY ;
		
		String jointLX;						
		String jointLY;
		String jointRX;						
		String jointRY;
		String jointX,jointY,nextJointX,nextJointY;
		String lastJointLX;
		String lastJointLY;
		String lastJointRX;
		String lastJointRY;
		
		
		//init variables
		
		for(int i=0; i <= maxPaths -1; i++){
			for(int joint =0; joint<= numberOfJoints -1 ; joint++){
				xVariable = "x" + i + joint;
				hashX.put(hash2Int(i,joint), xVariable);
				variables.add(xVariable);
				variableTypes.add(0);
				
				yVariable = "y" + i + joint;
				hashY.put(hash2Int(i,joint), yVariable);
				variables.add(yVariable);
				variableTypes.add(0);				

				
			}
		}
		
		for(int path = 0; path <= maxPaths -1; path++ ){
			for( int joint =0; joint <= numberOfJoints-1; joint++ ){
				String xEqualslx, yEqualsly;
				xVariable = hashX.get(hash2Int(path,joint));
				yVariable = hashY.get(hash2Int(path,joint));
			
				if(joint > 0){
					lastJointX = hashX.get(hash2Int(path,joint-1));
					lastJointY = hashY.get(hash2Int(path,joint-1));
					
					xEqualslx = "xEqualslx" + path + joint;
					yEqualsly = "yEqualsly" + path + joint;
					variables.add(xEqualslx);variableTypes.add(1);
					variables.add(yEqualsly);variableTypes.add(1);
					// -M + " "  * binary < = x - lx <= M + " "  * binary
					// -M + " "  * binary < = y - ly <= M + " "  * binary
					ILP.add(xVariable + " - " + lastJointX + " + "+M + " "  + xEqualslx + " >= 0"  );
					ILP.add(xVariable + "  - " + lastJointX + " - "+ M + " "  + xEqualslx + " <= 0"  );
					ILP.add(yVariable + " - " + lastJointY + " + "+M + " " + yEqualsly + " >= 0"  );
					ILP.add(yVariable + "  - " + lastJointY + " - "+M + " "  + yEqualsly + " <= 0"  );
					ILP.add(xEqualslx + " + "  + yEqualsly + " <= 1");
					
					
					
					
				}
				
				if(joint==0){
					constraint = xVariable + " = "  + entrance.getCoordinate().x;
					ILP.add(constraint);
					constraint = yVariable + " = "  + entrance.getCoordinate().y;
					ILP.add(constraint);
					constraint = "";
							
				}
				else if(joint == numberOfJoints -1){
					constraint = xVariable + " = "  + exit.getCoordinate().x;
					ILP.add(constraint);
					constraint = yVariable + " = "  + exit.getCoordinate().y;
					ILP.add(constraint);
					constraint = "";
					

				}
				// 0=<x(j)-x(j-1) <= M + " "  * dr
				else{
					// 0 <= x <= height   0 <= y <= widht 
					ILP.add(xVariable + " >= 0");
					ILP.add(xVariable + " <= " + (height-1));
					ILP.add(yVariable + " >= 0");
					ILP.add(yVariable + " <= " + (width-1));
			

				}
				
				//neither 2 of bend cross each other
				if(joint >= 2 && joint <= numberOfJoints - 2){
					for(int lastJoint = 0; lastJoint <= joint-2; lastJoint++){
						String ux1x2,ux2x1,uy1y2,uy2y1;
						
						ux1x2 = "u" + path +"x" + joint +"x"+ lastJoint; // x1 > x2?
						ux2x1 = "u" + path +"x" + lastJoint +"x"+ joint;
						uy1y2 = "u" + path +"y" + joint +"y"+ lastJoint;
						uy2y1 = "u" + path +"y" + lastJoint +"y"+ joint;
						variables.add(ux1x2);variableTypes.add(1);
						variables.add(ux2x1);variableTypes.add(1);
						variables.add(uy1y2);variableTypes.add(1);
						variables.add(uy2y1);variableTypes.add(1);
						
						jointLX = hashX.get(hash2Int(path,joint));						
						jointLY = hashY.get(hash2Int(path,joint));
						jointRX = hashX.get(hash2Int(path,joint+1));						
						jointRY = hashY.get(hash2Int(path,joint+1));
						
						lastJointLX = hashX.get(hash2Int(path,lastJoint));
						lastJointLY = hashY.get(hash2Int(path,lastJoint));
						lastJointRX = hashX.get(hash2Int(path,lastJoint+1));
						lastJointRY = hashY.get(hash2Int(path,lastJoint+1));
					
						
						
						ILP.add(jointLX +" - " + lastJointLX + " - " + M + " "  + ux1x2 + " <= -1");
						ILP.add(jointRX +" - " + lastJointLX + " - " + M + " "  + ux1x2 + " <= -1");
						ILP.add(jointLX +" - " + lastJointRX + " - " + M + " "  + ux1x2 + " <= -1");
						ILP.add(jointRX +" - " + lastJointRX + " - " + M + " "  + ux1x2 + " <= -1");
						ILP.add(lastJointLX +" - " + jointLX + " - " + M + " "  + ux2x1 + " <= -1");
						ILP.add(lastJointLX +" - " + jointRX + " - " + M + " "  + ux2x1 + " <= -1");
						ILP.add(lastJointRX +" - " + jointLX + " - " + M + " "  + ux2x1 + " <= -1");
						ILP.add(lastJointRX +" - " + jointRX + " - " + M + " "  + ux2x1 + " <= -1");
						
						ILP.add(jointLY +" - " + lastJointLY + " - " + M + " "  + uy1y2 + " <= -1");						
						ILP.add(jointRY +" - " + lastJointLY + " - " + M + " "  + uy1y2 + " <= -1");
						ILP.add(jointLY +" - " + lastJointRY + " - " + M + " "  + uy1y2 + " <= -1");
						ILP.add(jointRY +" - " + lastJointRY + " - " + M + " "  + uy1y2 + " <= -1");
						ILP.add(lastJointLY +" - " + jointLY + " - " + M + " "  + uy2y1 + " <= -1");
						ILP.add(lastJointLY +" - " + jointRY + " - " + M + " "  + uy2y1 + " <= -1");
						ILP.add(lastJointRY +" - " + jointLY + " - " + M + " "  + uy2y1 + " <= -1");
						ILP.add(lastJointRY +" - " + jointRY + " - " + M + " "  + uy2y1 + " <= -1");
						
						ILP.add( ux1x2 + " + "+ ux2x1 + " + " +uy1y2 + " + " + uy2y1 + " <= 3"); 
						
					}
				}//neither 2 of bend cross each other
				
			
				
			}
		
		}
		int loop = 0;
		for(Edge edge:edges){
			loop++;
			int edgeX = edge.coord.x;
			int edgeY = edge.coord.y;
			int edgeS = edge.coord.s;
			int edgeT = edge.coord.t;
			int count = 0;
			String binaryL,binaryR,binaryU,binaryD;
			if(edge instanceof Wall){
				
				//horizontal wall
				if (edge.coord.x == edge.coord.s){					
					
					
					for(int path = 0; path <= maxPaths -1 ; path++){
						for(int joint = 0; joint <= numberOfJoints-2; joint++){
							int nextJoint = joint + 1;						
							
							jointX = hashX.get(hash2Int(path,joint));
							jointY = hashY.get(hash2Int(path,joint));
							nextJointX = hashX.get(hash2Int(path,nextJoint));
							nextJointY = hashY.get(hash2Int(path,nextJoint));
							binaryL = "binaryL" +"p" + path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
							variables.add(binaryL);variableTypes.add(1);
							binaryR = "binaryR" + "p" +path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
							variables.add(binaryR);variableTypes.add(1);
							// x0 + M + " "  * binaryR >=  s, x1  + M + " "  * binaryR >= s							
							// x0  -M + " "  * binaryL <=x, x1  - M + " "  * binaryL <= x 
							
							ILP.add(jointX +  " - "  + M + " "   + binaryL+ " <= " + edgeX );
							ILP.add(nextJointX +  " - " + M + " "   + binaryL+ " <= " + edgeX);
							ILP.add(jointX +  " + "  + M + " "   + binaryR+ " >= " + edgeS);
							ILP.add(nextJointX  + " + "  + M + " "   + binaryL+ " >= " +  edgeS);
							
							ILP.add(binaryL + " + " + binaryR + " <= 1" );
							
							
							
						}
						
					}
					
				}
				//vertical wall
				else{
					for(int path = 0; path <= maxPaths -1 ; path++){
						for(int joint = 0; joint <= numberOfJoints-2; joint++){
							int nextJoint = joint + 1;						
							
							jointX = hashX.get(hash2Int(path,joint));
							jointY = hashY.get(hash2Int(path,joint));
							nextJointX = hashX.get(hash2Int(path,nextJoint));
							nextJointY = hashY.get(hash2Int(path,nextJoint));
							binaryU = "binaryU" +"p" + path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
							variables.add(binaryU);variableTypes.add(1);
							binaryD = "binaryD" + "p" +path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
							variables.add(binaryD);variableTypes.add(1);
							// y0 + M + " "  * binaryU >=  y, y1  + M + " "  * binaryU >= t							
							// y0  -M + " "  * binaryD <=y, y1  - M + " "  * binaryD <= y 
							
							ILP.add(jointY +  " - "  + M + " "   + binaryD+ " <= " + edgeY );
							ILP.add(nextJointY +  " - " + M + " "   + binaryD+ " <= " + edgeY);
							ILP.add(jointY +  " + "  + M + " "   + binaryU+ " >= " + edgeT);
							ILP.add(nextJointY  + " + "  + M + " "   + binaryU+ " >= " +  edgeT);
							
							ILP.add(binaryU + " + " + binaryD + " <= 1" );
							
							
							
						}
						
					}
				}
					
			}
			else if (edge.coord.x == edge.coord.s){
			//if edge is horizontal
				//edge(i,j,i,j+1)
				
				constraint = "";
				
			
				for(int path = 0; path <= maxPaths -1 ; path++){
					for(int joint = 0; joint <= numberOfJoints-2; joint++){
						int nextJoint = joint + 1;						
						
						jointX = hashX.get(hash2Int(path,joint));
						jointY = hashY.get(hash2Int(path,joint));
						nextJointX = hashX.get(hash2Int(path,nextJoint));
						nextJointY = hashY.get(hash2Int(path,nextJoint));
						binaryL = "binaryL" +"p" + path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryL);variableTypes.add(1);
						binaryR = "binaryR" + "p" +path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryR);variableTypes.add(1);
						// -M + " "  * binary<=x0-i <= M + " " *binary
						// -M + " "  * binary <= x1-i <= M + " " *binary
						// y0 <= j + M + " "  * binary, y1 + M + " "  * binary >= j+1 
						ILP.add(jointX +  " + "  + M + " "   + binaryL+ " >= " + edgeX );
						ILP.add(jointX +  " - " + M + " "   + binaryL+ " <= " + edgeX);
						ILP.add(nextJointX +  " + "  + M + " "   + binaryL+ " >= " + edgeX);
						ILP.add(nextJointX  + " - "  + M + " "   + binaryL+ " <= " +  edgeX);
						
						ILP.add(jointY +   " - " + M + " "  + binaryL + " <= "  + edgeY );
						ILP.add(nextJointY   + " + " + M + " "  + binaryL + " >= " + edgeT);
						
						ILP.add(jointX  + " + "  + M + " "   + binaryR+ " >= " + edgeX);
						ILP.add(jointX  + " - "  + M + " "   + binaryR+ " <= " + edgeX);
						ILP.add(nextJointX  + " + "  + M + " "   + binaryR+ " >= "  + edgeX);
						ILP.add(nextJointX  + " - "  + M + " "   + binaryR+ " <= "  + edgeX);
						
						ILP.add(nextJointY  + " - " + M + " "  + binaryR + " <= " + edgeY );
						ILP.add(jointY  + " + " + M + " "  + binaryR + " >= " +  edgeT );
						
						
						constraint += " + " + binaryL + " + " + binaryR;
						count++;
					}
					
				}
				constraint += " <= " + (2*count-1);
				
					
					ILP.add(constraint);
				
				
				constraint = "";
				count = 0;
			}
			//if edge is vertical
			else if(edge.coord.y == edge.coord.t){
				//edge(i,j,i+1,j)
			
				
				for(int path = 0; path <= maxPaths -1 ; path++){
					for(int joint = 0; joint <= numberOfJoints-2; joint++){
						int nextJoint = joint + 1;
						
						
						jointX = hashX.get(hash2Int(path,joint));
						jointY = hashY.get(hash2Int(path,joint));
						nextJointX = hashX.get(hash2Int(path,nextJoint));
						nextJointY = hashY.get(hash2Int(path,nextJoint));
						binaryU = "binaryU" +"p" +path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryU);variableTypes.add(1);
						binaryD = "binaryD" + "p"+path + "j"+joint +"nj"+ nextJoint +"x"+ edgeX + "y"+edgeY + "s"+edgeS +"t" +edgeT;
						variables.add(binaryD);variableTypes.add(1);
						// -M + " "  * binary<=y0-j <= M + " " *binary
						// -M + " "  * binary <= y1-j <= M + " " *binary
						// x0 <= i + M + " "  * binary, x1 + M + " "  * binary >= i+1 
						ILP.add(jointY  + " + "  + M + " "   + binaryU+ " >= "+ edgeY);
						ILP.add(jointY   + " - "  + M + " "   + binaryU+ " <=  " + edgeY);
						ILP.add(nextJointY  + " + "  + M + " "   + binaryU+ " >= " +  edgeY);
						ILP.add(nextJointY   + " - "  + M + " "   + binaryU+ " <= " + edgeY);
						
						ILP.add(jointX  + " - " + M + " "  + binaryU + " <= " + edgeX );
						ILP.add(nextJointX + " + " + M + " "  + binaryU + " >= "+ edgeS );
						
						ILP.add(jointY + " + "  + M + " "   + binaryD+ " >= "+ edgeY);
						ILP.add(jointY  + " - "  + M + " "   + binaryD+ " <= " + edgeY);
						ILP.add(nextJointY  + " + "  + M + " "   + binaryD+ " >= " + edgeY);
						ILP.add(nextJointY  + " - "  + M + " "   + binaryD+ " <= "+ edgeY );
						
						ILP.add(nextJointX   + " - " + M + " "  + binaryD + " <= "+ edgeX );
						ILP.add(jointX  + " + " + M + " "  + binaryD + " >= " + edgeS );
						constraint += " + " + binaryU + " + " + binaryD;
						count ++;
					}
				}
				
				constraint += " <= " + (2*(count) - 1);

					
				ILP.add(constraint);

				constraint = "";
				count = 0 ;
				
			}
			
		}
	
		
		
		//set obj 
		
		obj = "x00 + x01";
		try {
			writeILP();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<ArrayList<Int2>>  ps = new ArrayList<ArrayList<Int2>>();
		for(int path = 0; path <= maxPaths -1; path++  ){
			ArrayList<Int2> p = new ArrayList<Int2>(); 
			for(int joint = 0; joint <= numberOfJoints -1; joint ++){
				String x = "x" + path + joint;
				String y = "y" + path + joint;
				
				int xValue, yValue;
				xValue = pathReuslts.get(x);
				yValue = pathReuslts.get(y);
				
				p.add(new Int2(xValue,yValue));						
			}
			ps.add(p);
		}
		
		System.out.println("done");
		
		//return ps;
	}
	
	
	public void getAcyclicILP(){
		variables = new ArrayList<String>();
		variableTypes = new ArrayList<Integer>();
		String xVariable;
		String wVariable;
		String yVariable; // if a node is used in a subgraph; 0 used, 1 unused
		String iVariable; // current on each edge
		String vVariable; // voltage on each edge
		String fVariable; // flow on each edge
		String gTVariable; //flow from graph to souper sink
		String sGVariable; //flow from super source to one node in Graph
		String constraint = "";
		
		HashMap<Edge, String> hashWVariables1dim;
		HashMap<Edge,String> hashFVariables1dim; 
		HashMap<Edge, String> hashIVariables1dim;
		HashMap<Edge, String> hashVVariables1dim;	
		HashMap<Node,String> hashYVariables1dim;
		HashMap<Node,String> hashGTVariables1dim = null; //flow from Graph to super sink
		
		ArrayList<String> flowConstraint1dim;
		
		
		
		for(int i = 0; i <= maxPaths - 1; i++){
			hashWVariables1dim = new HashMap<Edge,String>(); 
			hashFVariables1dim= new HashMap<Edge,String>();
			sGVariable = "flowStoG" + i; 
			for(Edge edge:edges){
				wVariable = "w" + i +  edge.coord.x + edge.coord.y + edge.coord.s+edge.coord.t;
				fVariable= "f" + i + edge.coord.x + edge.coord.y + edge.coord.s + edge.coord.t; // flow on each edge
				if(!hashWVariables1dim.containsKey(edge)){
					hashWVariables1dim.put(edge, wVariable);
					hashFVariables1dim.put(edge, fVariable);
					variables.add(wVariable);
					variableTypes.add(1);
					variables.add(fVariable);
					variableTypes.add(0);
				}	
				
			}
			hashWVariables.put(i, hashWVariables1dim);
			hashFVariables.put(i, hashFVariables1dim);
			
			hashYVariables1dim = new HashMap<Node,String>();
			hashGTVariables1dim = new HashMap<Node,String>();
			for(Node node:nodes){
				yVariable = "y" + i + node.coord.x + node.coord.y;
				gTVariable = "gT" + i + node.coord.x + node.coord.y;
				if(!hashYVariables1dim.containsKey(node)){
					hashYVariables1dim.put(node,yVariable);
					hashGTVariables1dim.put(node, gTVariable);
					
					variables.add(yVariable);
					variableTypes.add(1);
					variables.add(gTVariable);
					variableTypes.add(1);
				}
				for(Node cnctNode:getConnectedNodes(node)){
					Edge edge = getEdge(node,cnctNode);
					wVariable = hashWVariables1dim.get(edge); 
					constraint = constraint + wVariable + " + ";					
				}
				
				if(node == entrance || node == exit){
					constraint =constraint + " " + yVariable + " = 1"; 
				}
				else{
					constraint = constraint + "2" + " " + yVariable + " = 2";
				}
				ILP.add(constraint);
				constraint = "";
			}
			
			//set Kirchhoff Circuit Laws to calculate f on each edge
			//each used edge (w(edge) = 1), i(edge) <> 0  
			
			// current conservation
			// f01 + f02 + f03 + f04 - f(g,t)= 0
			String fConstraint = "";			
			flowConstraint1dim = new ArrayList<String>();		
			
			for(Node node:nodes){
				gTVariable = hashGTVariables1dim.get(node);		
				for(Node cnctNode:getConnectedNodes(node)){
					Edge edge = getEdge(node,cnctNode);
					fVariable = hashFVariables1dim.get(edge);
					
					wVariable = hashWVariables1dim.get(edge);
					if(aLeftOrUnderb(node,cnctNode)){
						fVariable = " + " + fVariable;
					}
					else
					{
						fVariable = " - " + fVariable;
					}		
					
					fConstraint +=  " " + fVariable;
				}
				
				if(fConstraint == "")
					continue;
					
				//if node == entrance, flowStoG + other flow - flowGtoT= 0
				//else other flow -flowGtoT = 0
				if(node == entrance){
					fConstraint = fConstraint + " + " + sGVariable + " - " + gTVariable ; 
					fConstraint += " = 0 ";
				}
				else{
					fConstraint = fConstraint + " - " + gTVariable ; 
					fConstraint += " = 0 ";
				}
				
				ILP.add(fConstraint);
				fConstraint = "";
			
			}
			
			// for wVariable on Wall wVariable =0
			// for flow on edge of graph -M * w =<flow <= M * w
			for(Edge edge:edges){
				wVariable = hashWVariables1dim.get(edge);
				fVariable = hashFVariables1dim.get(edge);
				if(edge instanceof Wall){
					constraint = " " + wVariable + " = 0";
					ILP.add(constraint);
					constraint = "";
				}
				
				constraint = fVariable + " + " +  M + " " +wVariable + " >= 0";
				ILP.add(constraint);
				constraint = "";
				constraint = fVariable + " - " + M + " " +wVariable + " <= 0";
				ILP.add(constraint);
				constraint = "";					
			}	
			
			
			
			
			
			// for flow super source to graph. flow >= 0
			constraint = sGVariable + " >= 0";
			ILP.add(constraint);
			constraint = "";
			variables.add(sGVariable);
			variableTypes.add(0);
			
			//maximum flow == number of nodes the subgraph
			// sToG = (1-y0) + (1-y1) + ....
			// sToG + y0 + y1 + ...  = n
			constraint = sGVariable;
			int numNodes = 0;
			for(Node node:nodes){
				numNodes ++;
				yVariable = hashYVariables1dim.get(node);
				constraint += " + " + yVariable; 
			}			
			constraint = constraint + " = " + numNodes;
			ILP.add(constraint);
			constraint = "";
			
//			hashIVariables1dim = new HashMap<Edge,String>();
//			hashVVariables1dim = new HashMap<Edge,String>();
//			for(Edge edge:edges){
//				if(!(edge instanceof Wall)){					
//					iVariable = "i" + i + edge.coord.x + edge.coord.y + edge.coord.s + edge.coord.t;	
//					vVariable = "v" + i + edge.coord.x + edge.coord.y + edge.coord.s + edge.coord.t;
//					if(!hashIVariables1dim.containsKey(edge)){
//						hashIVariables1dim.put(edge, iVariable);
//						hashVVariables1dim.put(edge,vVariable);
//						variables.add(iVariable);
//						
//						variableTypes.add(0);
//						variables.add(vVariable);
//						variableTypes.add(0);
//					}
//					
//				}
//			}
//			
//			//set Kirchhoff Circuit Laws to calculate i on each edge
//			//each used edge (w(edge) = 1), i(edge) <> 0
//			
//			//create iVariable vVariable for every edge  
//			
//			// current conservation
//			// i01 + i02 + i03 + i04 = 0
//			String iConstraint = "";
//			
//			iConstraint1dim = new ArrayList<String>();
//			
//			for(Node node:nodes){
//				if(node != entrance && node != exit){					
//					for(Node cnctNode:getConnectedNodes(node)){
//						Edge edge = getEdge(node,cnctNode);
//						iVariable = hashIVariables1dim.get(edge);
//						wVariable = hashWVariables1dim.get(edge);
//						if(aLeftOrUnderb(node,cnctNode)){
//							iVariable = "+" + iVariable;
//						}
//						else
//						{
//							iVariable = "-" + iVariable;
//						}
//						String key = "i" + edge.coord.x + edge.coord.y + edge.coord.s + edge.coord.t; 
//						constraint = iVariable + "-" + M + wVariable + "<= 0";
//						if(!iConstraint1dim.contains(key)){							
//							ILP.add(constraint);
//							constraint = "";
//						}
//						
//						constraint = iVariable + "+" + M + wVariable + ">= 0";
//						if(!iConstraint1dim.contains(key)){						
//							ILP.add(constraint);
//							constraint = "";
//						}
//						
//						iConstraint +=  iVariable;
//					}
//					
//					if(iConstraint == "")
//						continue;
//						
//					
//					
//					iConstraint += "=0";
//					ILP.add(iConstraint);
//					iConstraint = "";
//				}
//		}
			
		
//			/*
//		// voltage conservation	
//		// v01 + v12 + v23 + v34 = 0;
//		
//		for(int j = 0; j <= width - 1 -1; j++){
//			for(int k = 0; k <= height -1 -1; k++){
//				Node node0,node1,node2,node3;
//				Edge edge0,edge1,edge2,edge3;
//				node0 = getNode(j,k);
//				node1 = getNode(j,k+1);
//				node2 = getNode(j +1,k+1);
//				node3 = getNode(j+1,k);
//				
//				edge0 = getEdge(node0,node1);
//				edge1 = getEdge(node1,node2);
//				edge2 = getEdge(node2,node3);
//				edge3 = getEdge(node3,node0);
//				
//				
//				String iVariable0,vVariable0,wVariable0;
//				String iVariable1,vVariable1,wVariable1;
//				String iVariable2,vVariable2,wVariable2;
//				String iVariable3,vVariable3,wVariable3;
//				
//				//edge0
//				if(!(edge0 instanceof Wall)){
//					iVariable0 = hashIVariables1dim.get(edge0);
//					vVariable0 = hashVVariables1dim.get(edge0);
//					wVariable0 = hashWVariables1dim.get(edge0);
//				}
//				else
//				{
//					iVariable0 = "i" + i +  edge0.coord.x + edge0.coord.y + edge0.coord.s+edge0.coord.t;
//					vVariable0 = "v" + i +  edge0.coord.x + edge0.coord.y + edge0.coord.s+edge0.coord.t;
//					wVariable0 = "w" + i +  edge0.coord.x + edge0.coord.y + edge0.coord.s+edge0.coord.t;
//					hashWVariables1dim.put(edge0,wVariable0); variables.add(wVariable0);variableTypes.add(1);
//					hashIVariables1dim.put(edge0,iVariable0); variables.add(iVariable0);variableTypes.add(0);
//					hashVVariables1dim.put(edge0,vVariable0); variables.add(vVariable0); variableTypes.add(0);
//					constraint = wVariable0 + "=0";
//				}
//				
//				//edge1
//				if(!(edge1 instanceof Wall)){
//					iVariable1= hashIVariables1dim.get(edge1);
//					vVariable1 = hashVVariables1dim.get(edge1);
//					wVariable1 = hashWVariables1dim.get(edge1);
//				}
//				else
//				{
//					iVariable1 = "i" + i +  edge1.coord.x + edge1.coord.y + edge1.coord.s+edge1.coord.t;
//					vVariable1 = "v" + i +  edge1.coord.x + edge1.coord.y + edge1.coord.s+edge1.coord.t;
//					wVariable1 = "w" + i +  edge1.coord.x + edge1.coord.y + edge1.coord.s+edge1.coord.t;
//					hashWVariables1dim.put(edge1,wVariable1); variables.add(wVariable1);variableTypes.add(1);
//					hashIVariables1dim.put(edge1,iVariable1); variables.add(iVariable1);variableTypes.add(0);
//					hashVVariables1dim.put(edge1,vVariable1); variables.add(vVariable1); variableTypes.add(0);
//					constraint = wVariable1 + "=0";
//				}
//				
//				//edge2
//				if(!(edge2 instanceof Wall)){
//					iVariable2= hashIVariables1dim.get(edge2);
//					vVariable2 = hashVVariables1dim.get(edge2);
//					wVariable2 = hashWVariables1dim.get(edge2);
//				}
//				else
//				{
//					iVariable2 = "i" + i +  edge2.coord.x + edge2.coord.y + edge2.coord.s+edge2.coord.t;
//					vVariable2 = "v" + i +  edge2.coord.x + edge2.coord.y + edge2.coord.s+edge2.coord.t;
//					wVariable2 = "w" + i +  edge2.coord.x + edge2.coord.y + edge2.coord.s+edge2.coord.t;
//					hashWVariables1dim.put(edge2,wVariable2); variables.add(wVariable2);variableTypes.add(1);
//					hashIVariables1dim.put(edge2,iVariable2); variables.add(iVariable2);variableTypes.add(0);
//					hashVVariables1dim.put(edge2,vVariable2); variables.add(vVariable2); variableTypes.add(0);
//					constraint = wVariable2 + "=0";
//				}
//				
//				//edge3
//				if(!(edge3 instanceof Wall)){
//					iVariable3= hashIVariables1dim.get(edge3);
//					vVariable3 = hashVVariables1dim.get(edge3);
//					wVariable3 = hashWVariables1dim.get(edge3);
//				}
//				else
//				{
//					iVariable3 = "i" + i +  edge3.coord.x + edge3.coord.y + edge3.coord.s+edge3.coord.t;
//					vVariable3 = "v" + i +  edge3.coord.x + edge3.coord.y + edge3.coord.s+edge3.coord.t;
//					wVariable3 = "w" + i +  edge3.coord.x + edge3.coord.y + edge3.coord.s+edge3.coord.t;
//					hashWVariables1dim.put(edge3,wVariable3); variables.add(wVariable3);variableTypes.add(1);
//					hashIVariables1dim.put(edge3,iVariable3); variables.add(iVariable3);variableTypes.add(0);
//					hashVVariables1dim.put(edge3,vVariable3); variables.add(vVariable3); variableTypes.add(0);
//					constraint = wVariable3 + "=0";
//				}
//				
//				
//				
//				// -M(1-w) <= v <= M(1-w), -M*W <= i <= M*w
//				//v+M*W <= M			
//				//v - M*w >= -M
//				// i+ m*w >=0
//				// i - M*W <= 0
//				
//				String c00 = vVariable0 +  "+" + M + wVariable0 + "<=" + M;
//				String c01 = vVariable0 + "-" + M + wVariable0 + ">=" + "-" + M;
//				String c02 = iVariable0 + "-" + M + wVariable0 + "<=0";
//				String c03 = iVariable0 + "+" + M +  wVariable0 + ">=0";
//				ILP.add(c00);ILP.add(c01);
//				//ILP.add(c02);ILP.add(c03);
//				
//				String c10 = vVariable1 + "+" + M + wVariable1 + "<=" + M;
//				String c11 = vVariable1 + "-" + M + wVariable1 + ">=" + "-" + M;
//				String c12 = iVariable1 + "-" + M + wVariable1 + "<=0";
//				String c13 = iVariable1 + "+" + M +  wVariable1 + ">=0";
//				ILP.add(c10);ILP.add(c11);
//				//ILP.add(c12);ILP.add(c13);
//				
//				String c20 = vVariable2 + "+" + M + wVariable2 + "<=" + M;
//				String c21 = vVariable2 + "-" + M + wVariable2 + ">=" + "-" + M;
//				String c22 = iVariable2 + "-" + M + wVariable2 + "<=0";
//				String c23 = iVariable2 + "+" + M +  wVariable2 + ">=0";
//				ILP.add(c20);ILP.add(c21);
//				//ILP.add(c22);ILP.add(c23);
//				
//				String c30 = vVariable3 + "+" + M + wVariable3 + "<=" + M;
//				String c31 = vVariable3 + "-" + M + wVariable3 + ">=" + "-" + M;
//				String c32 = iVariable3 + "-" + M + wVariable3 + "<=0";
//				String c33 = iVariable3 + "+" + M +  wVariable3 + ">=0";
//				ILP.add(c30);ILP.add(c31);
//				//ILP.add(c32);ILP.add(c33);
//				
//				//v1 + v2 + v3 + v4 = 0
//				
//				constraint = iVariable0 + "+" +  vVariable0 + 
//						"+" + iVariable1 + "+" + vVariable1 +
//						"+" + iVariable2 + "+"  + vVariable2 +
//						"-" + iVariable3 + "-"  + vVariable3 + " = 0";
//				ILP.add(constraint);
//				constraint = "";
//			}
//		}
//		
//		
//		// if w = 1, then i <> 0
//		for(Edge edge:edges){
//			String bVariable = "b" + i + edge.coord.x + edge.coord.y + edge.coord.s+edge.coord.t; // a temp binary variable
//			variables.add(bVariable);
//			variableTypes.add(1);
//			
//			wVariable = hashWVariables1dim.get(edge);
//			iVariable = hashIVariables1dim.get(edge); 
//			//constraint = iVariable + "+" +M + wVariable + ">=0"; ILP.add(constraint);
//			//constraint = iVariable + "-" +M + wVariable + "<=0"; ILP.add(constraint);
//			//i + w -1 >(b-1)M, i+w -1 <bM
//			//i + w -M*b>1-M, i+w - M*b < 1
//			//means i+w - M*b >= 1 - M + 1, i + w - M*b <= 0 
//			constraint = iVariable +"+"+ wVariable + "-" + M + bVariable + ">=" + (1-M +1); ILP.add(constraint);
//			constraint = "";
//			constraint = iVariable + "+" + wVariable + "-" + M + bVariable + "<=0"; ILP.add(constraint);
//			constraint = "";
//		}
//			
//			
//			
//			
//			
	}
		
		obj = "";
		constraint = "";
		for(Edge edge:edges){			
			for(int i = 0; i <= maxPaths -1; i++){
				HashMap<Edge,String> test = hashWVariables.get(i);
				if(!(edge instanceof Wall)){		
					//only init xVarialbe once
					
					wVariable = test.get(edge);
					//wVariable = hashWVariables.get(i).get(edge);
					constraint = constraint +" + " + wVariable;				
				}
				else{
					wVariable = test.get(edge);
					String wConstraint = wVariable + " = 0 ";
					ILP.add(wConstraint);
					wConstraint = "";
				}
					
			}
			if(constraint == "")
				continue;
			
			
			constraint += " >= 1";
			ILP.add(constraint);
			constraint = "";
		}
		
		for(int i =0; i <= maxPaths -1; i++){
			xVariable = "x" + i;
	
			variables.add(xVariable);
			variableTypes.add(1);

			for(Edge edge:edges){				
				wVariable = hashWVariables.get(i).get(edge);
				constraint = constraint +" + " + wVariable;			
			}
 			constraint += " - " + M + " " +xVariable + " <= 0 ";
 			ILP.add(constraint);
 			constraint = "";
 			
 			obj += " + " + xVariable;
 			
		}
		
		
		
	
		
		

	}
	
	public void getAcyclicILPV2(){
		variables = new ArrayList<String>();
		variableTypes = new ArrayList<Integer>();
		String xVariable;
		String wVariable;
		String yVariable; // if a node is used in a subgraph; 0 used, 1 unused
		String iVariable; // current on each edge
		String vVariable; // voltage on each edge
		String fVariable; // flow on each edge
		String gTVariable; //flow from graph to souper sink
		String sGVariable; //flow from super source to one node in Graph
		String constraint = "";
		
		HashMap<Edge, String> hashWVariables1dim;
		HashMap<Edge,String> hashFVariables1dim; 
		HashMap<Edge, String> hashIVariables1dim;
		HashMap<Edge, String> hashVVariables1dim;	
		HashMap<Node,String> hashYVariables1dim;
		HashMap<Node,String> hashGTVariables1dim = null; //flow from Graph to super sink
		HashMap<Node,String> hashNodeIsJoint;	
		HashMap<Node,String> hashJointX;
		HashMap<Node,String> hashJointY;
		
		ArrayList<String> flowConstraint1dim;
		
		
		
		for(int i = 0; i <= maxPaths - 1; i++){
			hashWVariables1dim = new HashMap<Edge,String>(); 
			hashFVariables1dim= new HashMap<Edge,String>();
			sGVariable = "flowStoG" + i; 
			for(Edge edge:edges){
				wVariable = "w" + i +  edge.coord.x + edge.coord.y + edge.coord.s+edge.coord.t;
				fVariable= "f" + i + edge.coord.x + edge.coord.y + edge.coord.s + edge.coord.t; // flow on each edge
				if(!hashWVariables1dim.containsKey(edge)){
					hashWVariables1dim.put(edge, wVariable);
					hashFVariables1dim.put(edge, fVariable);
					variables.add(wVariable);
					variableTypes.add(1);
					variables.add(fVariable);
					variableTypes.add(0);
				}	
				
			}
			hashWVariables.put(i, hashWVariables1dim);
			hashFVariables.put(i, hashFVariables1dim);
			
			hashYVariables1dim = new HashMap<Node,String>();
			hashGTVariables1dim = new HashMap<Node,String>();
			for(Node node:nodes){
				yVariable = "y" + i + node.coord.x + node.coord.y;
				gTVariable = "gT" + i + node.coord.x + node.coord.y;
				if(!hashYVariables1dim.containsKey(node)){
					hashYVariables1dim.put(node,yVariable);
					hashGTVariables1dim.put(node, gTVariable);
					
					variables.add(yVariable);
					variableTypes.add(1);
					variables.add(gTVariable);
					variableTypes.add(1);
				}
				for(Node cnctNode:getConnectedNodes(node)){
					Edge edge = getEdge(node,cnctNode);
					wVariable = hashWVariables1dim.get(edge); 
					constraint = constraint + wVariable + "+";					
				}
				
				if(node == entrance || node == exit){
					constraint =constraint + yVariable + " = 1"; 
				}
				else{
					constraint = constraint + "2" + yVariable + " = 2";
				}
				ILP.add(constraint);
				constraint = "";
			}
			
			//set Kirchhoff Circuit Laws to calculate f on each edge
			//each used edge (w(edge) = 1), i(edge) <> 0  
			
			// current conservation
			// f01 + f02 + f03 + f04 - f(g,t)= 0
			String fConstraint = "";			
			flowConstraint1dim = new ArrayList<String>();		
			
			for(Node node:nodes){
				gTVariable = hashGTVariables1dim.get(node);		
				for(Node cnctNode:getConnectedNodes(node)){
					Edge edge = getEdge(node,cnctNode);
					fVariable = hashFVariables1dim.get(edge);
					
					wVariable = hashWVariables1dim.get(edge);
					if(aLeftOrUnderb(node,cnctNode)){
						fVariable = "+" + fVariable;
					}
					else
					{
						fVariable = "-" + fVariable;
					}		
					
					fConstraint +=  fVariable;
				}
				
				if(fConstraint == "")
					continue;
					
				//if node == entrance, flowStoG + other flow - flowGtoT= 0
				//else other flow -flowGtoT = 0
				if(node == entrance){
					fConstraint = fConstraint + " + " + sGVariable + " - " + gTVariable ; 
					fConstraint += " =0";
				}
				else{
					fConstraint = fConstraint + " - " + gTVariable ; 
					fConstraint += " =0";
				}
				
				ILP.add(fConstraint);
				fConstraint = "";
			
			}
			
			// for wVariable on Wall wVariable =0
			// for flow on edge of graph -M * w =<flow <= M * w
			for(Edge edge:edges){
				wVariable = hashWVariables1dim.get(edge);
				fVariable = hashFVariables1dim.get(edge);
				if(edge instanceof Wall){
					constraint = wVariable + "= 0";
					ILP.add(constraint);
					constraint = "";
				}
				
				constraint = fVariable + " + " +  M + wVariable + ">=0";
				ILP.add(constraint);
				constraint = "";
				constraint = fVariable + " - " + M + wVariable + "<=0";
				ILP.add(constraint);
				constraint = "";					
			}	
			
			
			
			
			
			// for flow super source to graph. flow >= 0
			constraint = sGVariable + " >= 0";
			ILP.add(constraint);
			constraint = "";
			variables.add(sGVariable);
			variableTypes.add(0);
			
			//maximum flow == number of nodes the subgraph
			// sToG = (1-y0) + (1-y1) + ....
			// sToG + y0 + y1 + ...  = n
			constraint = sGVariable;
			int numNodes = 0;
			for(Node node:nodes){
				numNodes ++;
				yVariable = hashYVariables1dim.get(node);
				constraint += " + " + yVariable; 
			}			
			constraint = constraint + " = " + numNodes;
			ILP.add(constraint);
			constraint = "";
			
		
	}
		
		obj = "";
		constraint = "";
		for(Edge edge:edges){			
			for(int i = 0; i <= maxPaths -1; i++){
				HashMap<Edge,String> test = hashWVariables.get(i);
				if(!(edge instanceof Wall)){		
					//only init xVarialbe once
					
					wVariable = test.get(edge);
					//wVariable = hashWVariables.get(i).get(edge);
					constraint = constraint +"+" + wVariable;				
				}
				else{
					wVariable = test.get(edge);
					String wConstraint = wVariable + "=0";
					ILP.add(wConstraint);
					wConstraint = "";
				}
					
			}
			if(constraint == "")
				continue;
			
			
			constraint += ">= 1";
			ILP.add(constraint);
			constraint = "";
		}
		
		for(int i =0; i <= maxPaths -1; i++){
			xVariable = "x" + i;
	
			variables.add(xVariable);
			variableTypes.add(1);

			for(Edge edge:edges){				
				wVariable = hashWVariables.get(i).get(edge);
				constraint = constraint +"+" + wVariable;			
			}
 			constraint += "-" + M + xVariable + "<=0";
 			ILP.add(constraint);
 			constraint = "";
 			
 			obj += "+" + xVariable;
 			
		}
		
		
		
	
		
		

	}
	
	public boolean isInflow(Edge e, Node n){
		Node adjNode = getJointNode(n,e);
		
		//out flow
		if(adjNode.number>n.number)
			return false;
		//in flow
		else 
			return true;		
		
	}
	
	
	

	
	public Dir rotateClkWise(Dir dir){
		switch(dir){
		case East:
			dir = Dir.South;
			break;
		case South:
			dir = Dir.West;
			break;
			
		case West:
			dir = Dir.North;
			break;
		case North:
			dir = Dir.East;
			break;
		}
		
		return dir;
	}
	public Dir rotateAntiClkWise(Dir dir){
		switch(dir){
		case East:
			dir = Dir.North;
			break;
		case South:
			dir = Dir.East;
			break;
			
		case West:
			dir = Dir.South;
			break;
		case North:
			dir = Dir.West;
			break;
		}
		
		return dir;
	}
	
	public Dir reverseDir(Dir dir){
		return rotateClkWise(rotateClkWise(dir));
	}
	
	
	

	public  boolean nodeIsOnEdge(Node node, Edge edge){
		if ((node.coord.x == edge.coord.x && node.coord.y == edge.coord.y) || 
				(node.coord.x == edge.coord.s && node.coord.y == edge.coord.t) )
			return true;
		else
			return false;
	}
	

	

	public int hash4Int(int a,int b, int c, int d){
		assert(a == c || b ==d);
		return a * 1000000 + b*10000+c*100+d;
	}
	
	private int hash2Nodes(Node a, Node b){
		if(a.number < b.number){
			return a.coord.x * 1000000 + a.coord.y * 10000 + b.coord.x * 100 + b.coord.y;
		}
		else{
			return b.coord.x * 1000000 + b.coord.y * 10000 + a.coord.x * 100 + a.coord.y;
		}
	}
	
	private int hash2Int(int a, int b){ 
		return 100*a + b;
	}
	
	private Node getNode(int a, int b){
		return hashNodes.get(a*100+b);
	}
	
	private Node getNode(Int2 a){
		return getNode(a.x,a.y);
	}
	
	private boolean aLeftOrUnderb(Node a, Node b){
		if(a.coord.x < b.coord.x || a.coord.y < b.coord.y )
			return true;
		else
			return false;
	}
	private Edge getEdge(int a, int b){
		return getEdge(nodes.get(a), nodes.get(b));
	}
	
	private Edge getEdge(Node a, Node b){		
		
		return hashEdges.get(hash2Nodes(a,b));
	}
	//return nodes not blocked by wall nor closed valve 
	private ArrayList<Node> getCnctNodes(Node node){
		
		ArrayList<Node> cnctNodes = new ArrayList<Node>();
		ArrayList<Node> adjNodes = node.getAdjNodes();
		Edge edge = null;
		for(Node adjNode:adjNodes){
				
		
			
			if(hashEdges.containsKey(hash2Nodes(adjNode,node))){
				edge = hashEdges.get(hash2Nodes(adjNode,node));
			}
			
			if (edge.on)
				cnctNodes.add(adjNode);
		}
		
		return cnctNodes;
	}
	
	//get neighbour nodes
	private ArrayList<Node> getJointNodes(Node node){
		ArrayList<Node> jointNodes = new ArrayList<Node>();
		ArrayList<Node> adjNodes = node.getAdjNodes();
		for(Node adjNode:adjNodes){
			jointNodes.add(adjNode);
		}
		
		return jointNodes;
	}
	
	//connect means edge between two node is not wall
	private ArrayList<Node> getConnectedNodes(Node node){
		ArrayList<Node> connectedNodes = new ArrayList<Node>();
		ArrayList<Node> adjNodes = node.getAdjNodes();
		for(Node adjNode:adjNodes){
			
			if(!(getEdge(adjNode,node) instanceof Wall))
				connectedNodes.add(adjNode);
		}
		
		return connectedNodes;
	}
	

	
	public boolean pathTest(){
		
		for (int j = 0 ; j < paths.size(); j ++){
			for(Edge e:this.edges){
				e.turnOff();
			}
			for(Edge e:paths.get(j)){
				e.turnOn();
			}
			if (DFSTest() == false)
				return false;
		}
		
		return true;
	}
	
	public boolean cutTest(){
		for (int j = 0 ; j < cuts.size(); j ++){
			for(Edge e:this.edges){
				e.turnOn();
			}
			for(Edge e:cuts.get(j)){
				e.turnOff();
			}
			if (DFSTest() == true)
				return false;
		}
		
		return true;
	}
	
	private int getFlow(Node node){
		int flow = 0;
		ArrayList<Node> cnctNodes = getCnctNodes(node);
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for(Node cnctNode:cnctNodes){
			Edge e = getEdge(node,cnctNode);
			edges.add(e);
		}
		
		for(Edge e:edges){
			flow+= e.weight;
		}
		return flow;
	}
	
	
	//for testing if there is a flow from source to sensor
	public boolean DFSTest(){
		Stack<Node> stack = new Stack<Node>();
		ArrayList<Node> discoveredNodes = new ArrayList<Node>();
		ArrayList<Node> cnctNodes;
		stack.add(entrance);
		Node node;
		Node[] adjNodes;
		
 		
		while(!stack.isEmpty()){
			node = stack.pop();
			
			if(discoveredNodes.contains(node))
				continue;
			else
				discoveredNodes.add(node);
			
			cnctNodes= getCnctNodes(node);
			if(cnctNodes.size() >0){				
				for( Node cnctNode:cnctNodes){											
					if(cnctNode == this.exit)	
						return true;
					else
						stack.add(cnctNode);
				}
			}			
		}
		
		return false;
	}
	
	
	
	private ArrayList<Edge> getJointEdges(Node node){
		ArrayList <Edge> edges = new ArrayList<Edge>();
		ArrayList<Node> jointNodes = getJointNodes(node);
		
		for(Node jointNode:jointNodes){
			Edge e = getEdge(node,jointNode);
			edges.add(e);
		}
		
		return edges;
	}
	
	private Node getJointNode(Node node, Edge e){
		
		ArrayList<Node> nodes = getJointNodes(node);
		for(Node n : nodes){
			if (getEdge(n,node) == e)
				return n;
		}
		 
		System.out.println("there is no node linked by this edge");
		return null;
		
	}
	
	public void setEdgeWall(Int4 wall){
		setEdgeWall(wall.x,wall.y,wall.s,wall.t);
	}
	
	public void setEdgeHole(Int4 hole){
		setEdgeHole(hole.x,hole.y,hole.s,hole.t);
	}
	
	public void setEdgeWall(int x, int y, int s, int t){
		Edge wall = new Wall();
		wall.setCoordinate(x,y,s,t);
		assert(hashEdges.size()>0);
		hashEdges.put(hash4Int(x,y,s,t),wall);	
		//hashTarEdges.remove(wall.hashValue());
		
	}
	
 	public void setHoles(ArrayList<Hole>holes){
		for(Hole hole:holes){
			//setEdgeHole(hole.getCoordinate().x,hole.getCoordinate().y,hole.getCoordinate().s,hole.getCoordinate().t);
			hashEdges.put(hole.hashValue(), hole);
		}
		edges = new ArrayList<Edge>(hashEdges.values());
	}
 	
 	public void setWalls(ArrayList<Wall> walls){
 		for(Wall wall:walls){
 			hashEdges.put(wall.hashValue(), wall);
 			//hashTarEdges.remove(wall.hashValue());
 		}
 		edges = new ArrayList<Edge>(hashEdges.values());
 	}
	
	public void setEdgeHole(int x,int y, int s, int t){
		Edge hole = new Hole();
		hole.setCoordinate(x,y,s,t);
		assert(hashEdges.size()>0);
		hashEdges.put(hash4Int(x,y,s,t),hole);
	}
	
	public Edge getEdge(int x, int y, int s, int t){
		Edge e = hashEdges.get(hash4Int(x,y,s,t));
		return e;
	}

	
	public void writeILP()throws IOException{
	
				
			PrintWriter writer = new PrintWriter("./lp.lp", "UTF-8");
			pathReuslts = new HashMap<String,Integer>();
			String OBJ = "";
			String Constraint = "";
			String Bound = "";
			ArrayList<String> binaryArray = new ArrayList<String>();
			ArrayList<String> generalArray = new ArrayList<String>();
			
			for(int i = 0; i <= variableTypes.size()-1; i++){
				if(variableTypes.get(i) == 1)
					binaryArray.add(variables.get(i));
				else
					generalArray.add(variables.get(i));
			}
			
			String Generals = "";
			//writer.println("Minimize");
			writer.println("Maximize");
			
			
			writer.println("Subject to");
			
			for(String instruction:ILP){
				writer.println(instruction);
			}
		
			//write Binarys and 
		
			writer.println("Binaries");
			for(String binary:binaryArray){
				writer.println(binary);
			}
			
			writer.println("Generals");
			for(String general:generalArray){
				writer.println(general);
			}
			writer.println("END");
			
		
			//System.out.println("this is good");
			writer.flush();
			writer.close();
			
			solveFile.solve("./lp.lp");
			pathReuslts = (HashMap<String, Integer>) solveFile.hashResults.clone();
			//System.out.println("this is a print");
			
//			Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","/home/ga63quk/workspace/goodLuck/Debug/goodLuck","/home/ga63quk/workspace/goodLuck/Debug/test.lp"});
//			
////			ProcessBuilder pb = new ProcessBuilder("/home/ga63quk/workspace/goodLuck/Debug/goodLuck.exe"); 
////					//"/home/ga63quk/workspace/IlpSolverCpp/Release/IlpSolverCpp.exe");
////					//,"/home/ga63quk/workspace/IlpSolverCpp/Release/lp.lp");
////			Process p = pb.start();
//			try {
//				p.waitFor();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
	}
//	public boolean reverseDirContainEdge(Node node, Dir searchDir,HashMap<Integer,Edge> targetEdges){
//		
//		switch(searchDir){
//		case North:
//			for(int i = 0; i <= node.coord.x -1;i++){
//				for (int j = 0; j<= width-2; j++){
//					if(targetEdges.containsKey(hash4Int(i,j,i,j+1))){
//						return true;
//					}
//				}
//			}
//			break;
//		case East:
//			for(int j = 0; j <= node.coord.y -1;j++){
//				for (int i = 0; i <= height-2; i++){
//					if(targetEdges.containsKey(hash4Int(i,j,i+1,j))){
//						return true;
//					}
//				}
//			}
//			break;
//		case South:		
//			for(int i = node.coord.x+1; i <= height -1;i++){
//				for (int j = 0; j<= width -2; j++){
//					if(targetEdges.containsKey(hash4Int(i,j,i,j+1))){
//						return true;
//					}
//				}
//			}
//			break;
//		case West:
//			for(int i = 0; i <= height -2;i++){
//				for (int j = node.coord.y +1; j<= width -1; j++){
//					if(targetEdges.containsKey(hash4Int(i,j,i+1,j))){
//						return true;
//					}
//				}
//			}
//			break;
//		default:
//			break;
//		
//		}
//		
//			
//	    return false;
//	}
	

	
	
	
	
//	public Dir selectCheckPointDir(ArrayList<Node> path, ArrayList<Node> checkPoints, ArrayList<ArrayList<Dir>> checkPointDirList,
//			 HashMap<Integer,Edge> targetEdges,Dir searchDir){
//		Dir dir = null;
//		Node checkPoint = checkPoints.get(checkPoints.size()-1);
//		
//		ArrayList<Dir> checkPointDirs = checkPointDirList.get(checkPointDirList.size()-1);
//		
//		Node nextNode;
//		
//		if(checkPointDirs.size() == 1){
//			checkPointDirList.remove(checkPointDirList.size()-1);
//			checkPoints.remove(checkPoints.size()-1);
//			dir = checkPointDirs.get(0);
//			return dir;
//		}
//		Dir dir1 = checkPointDirs.get(0);
//		Dir dir2 = checkPointDirs.get(1);
//		//with 2 options
//		for(Dir d:checkPointDirs){
//			if(d == searchDir || d == reverseDir(searchDir)){
//				checkPointDirs.remove(d);
//				return d;
//			}				
//		}
//		
//		for(Dir d:checkPointDirs){
//			if(dirContainEdge(checkPoint,d,targetEdges)){				
//					checkPointDirs.remove(d);
//					return d;
//			}
//		}
//			
//		
//		
//		
//		return dir;
//	}
	
//	public ArrayList<Dir> nodeDirOptions(Node node){
//		ArrayList<Dir> dirOptions = new ArrayList<Dir>();
//		for(Dir d:Dir.values()){
//			if(thereIsWay(node,d)){
//				dirOptions.add(d);
//			}
//		}
//		return dirOptions; 
//	}
	
	
//	public ArrayList<Node>  detourWalk(Dir startDir,HashMap<Integer,Edge> targetEdges){
//		ArrayList<Node> path = new ArrayList<Node>();
//		ArrayList<Edge>pathEdges;
//		ArrayList<Node> popPath = new ArrayList<Node>();
//		ArrayList<Edge> popPathEdge = new ArrayList<Edge>();
//		ArrayList<Node> checkPoints = new ArrayList<Node>();
//		ArrayList<ArrayList<Dir>> checkPointDirs = new ArrayList<ArrayList<Dir>>();
//		ArrayList<Dir> dirOptions;
//		Node checkPoint;
//		HashMap<Integer,Edge> tarEdgeClone = (HashMap<Integer, Edge>) targetEdges.clone();
//		
//		Node node = entrance;
//		Node nextNode;
//		Edge newEdge;
//		Dir nextStepDir = startDir;
//		Dir startDirTemp = startDir; 
//		Dir pathDirReverse = reverseDir(startDir);
//		path.add(entrance);
//		
//			while(move1Step(node,nextStepDir) != exit){				
//				node = move1Step(node,nextStepDir);
//				//go into path
//				if(path.contains(node)){
//					popPath.clear();
//					popPathEdge.clear();
//					checkPoint = checkPoints.get(checkPoints.size()-1);
//					stackPopUtilCheckPoint(path,checkPoint,popPath);
//					popPathEdge = pathNodeToEdge(popPath);
//					for(Edge e:popPathEdge){
//						tarEdgeClone.put(e.hashValue(), e);
//					}
//					
//					nextStepDir = selectCheckPointDir(path,checkPoints,checkPointDirs,tarEdgeClone,startDir);
//					node = checkPoint;
//					pathDirReverse = reverseDir(nextStepDir);
//
//				}			
//				//no edge in startDir
//				else if(!thereIsWay(node,startDirTemp)){
//					for(Dir d:nodeDirOptions(node)){
//						if(d != pathDirReverse && d != startDirTemp){
//							if(d == startDir || d == reverseDir(startDir)){
//								startDirTemp =d;
//								nextStepDir = d;
//								break;
//							}													
//						}
//					}
//					
//					if(!thereIsWay(node,nextStepDir)){
//						for(Dir d:nodeDirOptions(node)){
//							if(d != pathDirReverse && d != startDirTemp){								
//								if(dirContainEdge(node,d,tarEdgeClone)){
//									nextStepDir = d;
//									break;
//								}						
//							}
//						}
//					}
//					
//					//didn't find a dir contains targetEdge, choose a random direction
//					if(!thereIsWay(node,nextStepDir)){
//						for(Dir d:nodeDirOptions(node)){
//							if(d != pathDirReverse && d != nextStepDir){
//								
//								nextStepDir = d;
//								break;
//							}
//						}
//					}
//					
//					//add node to path, add check point and check point directions
//					
//					path.add(node);
//					newEdge = getEdge(path.get(path.size()-2), path.get(path.size()-1));
//					tarEdgeClone.remove(newEdge.coord.x*100 + newEdge.coord.y);
//					dirOptions = nodeDirOptions(node);
//					dirOptions.remove(nextStepDir);
//					dirOptions.remove(pathDirReverse);
//					
//					pathDirReverse = getDir2Nodes(move1Step(node,nextStepDir),node);
//					if(dirOptions.size()>0){
//						checkPoints.add(node);
//						checkPointDirs.add(dirOptions);
//					}
//					
//				}
//				//there is edge in dir
//				else if(thereIsWay(node,startDirTemp)){
//					path.add(node);
//					nextStepDir = startDirTemp;
//					newEdge = getEdge(path.get(path.size()-2), path.get(path.size()-1));
//					tarEdgeClone.remove(newEdge.coord.x*100 + newEdge.coord.y);
//					dirOptions = nodeDirOptions(node);
//					dirOptions.remove(startDirTemp);
//					dirOptions.remove(pathDirReverse);
//					
//					pathDirReverse = getDir2Nodes(move1Step(node,startDirTemp),node);
//					if(dirOptions.size()>0){
//						checkPoints.add(node);
//						checkPointDirs.add(dirOptions);
//					}
//				}
//
//			}
//			
//			path.add(exit);
//			pathEdges = pathNodeToEdge(path);
//			for(Edge e:pathEdges){
//				targetEdges.remove(e.hashValue());
//			
//			}
//		
//		return path;
//		
//	}
	
//	public Node move1Step(Node start,Dir dir){
//		Node nextNode;
//		switch(dir){
//		case East:
//			nextNode = hashNodes.get(start.coord.x*100 + start.coord.y +1);
//			if(nextNode != null)
//				return nextNode;
//			break;
//		case West:
//			nextNode = hashNodes.get(start.coord.x*100 + start.coord.y -1);
//			if(nextNode != null)
//				return nextNode;
//			break;
//		case North:
//			nextNode = hashNodes.get((start.coord.x+1)*100 + start.coord.y);
//			if(nextNode != null)
//				return nextNode;
//			break;
//		case South:
//			nextNode = hashNodes.get((start.coord.x-1)*100 + start.coord.y);
//			if(nextNode != null)
//				return nextNode;
//			break;
//		
//		}
//		
//		return null;
//	}
//	public boolean thereIsWay(Node a, Node b){
//		Edge e = getEdge(a,b);
//		if(e instanceof Wall)
//			return false;
//		else
//			return true;
//	}
//	public boolean thereIsWay(Node a, Dir dir){
//		Node b;
//		Edge e = null;
//		switch(dir){
//		case East:
//			b = getNode(a.coord.x, a.coord.y+1);
//			if(b == null)
//				return false;
//			else{
//				e = getEdge(a,b);
//				if(e == null || e instanceof Wall)
//					return false;
//			}
//			break;
//		case North:
//			b = getNode(a.coord.x+1, a.coord.y);
//			if(b == null)
//				return false;
//			else{
//				e = getEdge(a,b);
//				if(e == null || e instanceof Wall)
//					return false;
//			}
//			break;
//		case South:
//			b = getNode(a.coord.x-1, a.coord.y);
//			if(b == null)
//				return false;
//			else{
//				e = getEdge(a,b);
//				if(e == null || e instanceof Wall)
//					return false;
//			}
//			break;
//		case West:
//			b = getNode(a.coord.x, a.coord.y-1);
//			if(b == null)
//				return false;
//			else{
//				e = getEdge(a,b);
//				if(e == null || e instanceof Wall)
//					return false;
//			}
//			break;
//		default:
//			return false;
//			
//		
//		}
//		return true;
//	}
//	
//	
//	
//	public boolean go(Node start, ArrayList<Node> stack, ArrayList<Node> checkPoints, Dir dir){
//		
//		boolean isCheckPoint = false;
//		
//		if(!thereIsWay(move1Step(start,dir),start))
//			return false;
//		// there is a way
//		else{
//			stack.add(start);
//			for(Dir d : Dir.values()){
//				if(thereIsWay(move1Step(start,d),start)){				
//					if(d != dir)
//						isCheckPoint =  true;							
//				}
//			}
//			if(isCheckPoint)
//				checkPoints.add(start);
//		}
//		return true;
//	}
	
//	public <T> void stackPopUtilCheckPoint(ArrayList<T> stack, T checkPoint,ArrayList<T> popStack){
//		T o = stack.remove(stack.size()-1);
//		popStack.add(o);
//		if(o == checkPoint){
//			stack.add(o);
//			//popStack.remove(o);
//		}
//		else
//			stackPopUtilCheckPoint(stack,checkPoint,popStack);
//	}
//
//	
//	public void pathThroughEdge(Edge edge){
//		
//	}
//	
//	public ArrayList<Node> dirOptions(Node node, ArrayList<Node> path){
//		ArrayList <Node> dirOptions;
//		ArrayList<Node> wrongDirs = new ArrayList<Node>();
//		Node n;
//		dirOptions = getConnectedNodes(node);		
//		//Node[] nodes = dirOptions.toArray(new Node[dirOptions.size()]);		
//		if(path.size()>1){
//			dirOptions.remove(path.get(path.size()-2));
//		}
//		
//		//dirOptions.removeAll(wrongDirs);
//		
//		return dirOptions;
//	}
//	
//	public void DFS(Node start, Node end, ArrayList<Node> path,Node critNode, HashMap<Integer,Edge> tarEdges){
//		
//		Dir[] dirEndToCrit = new Dir[2];
//		
//		
//		Stack<Node> stack = new Stack<Node>();
//		
//		ArrayList<Node> checkPoints = new ArrayList<Node>();
//		ArrayList<ArrayList<Node>> checkPointsOption = new  ArrayList<ArrayList<Node>>();
//		
//		ArrayList<Node> cnctNodes;
//		ArrayList<Node> popStack = new ArrayList<Node>();
//		stack.add(start);
//		Node node = start;
//		Node checkPoint;
//		ArrayList<Node> options  = new ArrayList<Node>();
//		
//		boolean stepBack = false;
//		if(end.coord.x < critNode.coord.x ){
//			dirEndToCrit[0] = Dir.South;
//		}
//		else if (end.coord.x == critNode.coord.x ){
//			dirEndToCrit[0] = Dir.Middle;
//		}
//		else
//			dirEndToCrit[0] = Dir.North;
//		
//		
//		if(end.coord.y < critNode.coord.y ){
//			dirEndToCrit[1] = Dir.West;
//		}
//		else if (end.coord.y == critNode.coord.y ){
//			dirEndToCrit[1] = Dir.Middle;
//		}
//		else
//			dirEndToCrit[1] = Dir.East;
//		
//		if(node == end){
//			if(!path.contains(node))
//				path.add(node);			
//			return;
//			
//		}
//			
//		cnctNodes = dirOptions(start,path);		
//	
//		if(cnctNodes.size()>1){
//			checkPoints.add(start);
//			node = dfsNodeDirBestGuess(node, cnctNodes, tarEdges);
//			cnctNodes.remove(node);
//			checkPointsOption.add(cnctNodes);
//		}
//	
//		
// 		
//		while(node != end){
//			
//			cnctNodes = dirOptions(node,path);	
//			
//			
//			if(dirEndToCrit[0] == Dir.South || dirEndToCrit[1] == Dir.West){
//				
//				if(node.coord.x >= critNode.coord.x && node.coord.y>= critNode.coord.y)
//					stepBack = true;
//			}
//			else if (dirEndToCrit[0] == Dir.North || dirEndToCrit[1] == Dir.East){
//				if(node.coord.x <= critNode.coord.x && node.coord.y <= critNode.coord.y)
//					stepBack = true;
//			}
//			
//			
//			if(path.contains(node))
//				stepBack = true;
//			
//			if(stepBack){
//				checkPoint = checkPoints.get(checkPoints.size()-1);
//				stackPopUtilCheckPoint(path, checkPoint, popStack);
//				options = checkPointsOption.get(checkPointsOption.size()-1);
//				node = options.remove(options.size()-1);
//				if(options.size() == 0){
//					checkPointsOption.remove(options);
//					checkPoints.remove(checkPoint);
//				}
//				stepBack = false;
//				continue;
//			}
//			
//			if(cnctNodes.size() == 0){
//				checkPoint = checkPoints.get(checkPoints.size()-1);
//				stackPopUtilCheckPoint(path, checkPoint, popStack);
//				options = checkPointsOption.get(checkPointsOption.size()-1);
//				node = options.remove(options.size()-1);
//				if(options.size() == 0){
//					checkPointsOption.remove(options);
//					checkPoints.remove(checkPoint);
//				}
//			}
//			else if (cnctNodes.size() == 1){
//				path.add(node);
//				node = cnctNodes.get(0);
//			}
//			else if(cnctNodes.size() >1){
//				path.add(node);
//				checkPoints.add(node);
//				
//				node = dfsNodeDirBestGuess(node, cnctNodes, tarEdges);
//				cnctNodes.remove(node);
//				checkPointsOption.add(cnctNodes);
//			}
//				
//		}
//		path.add(end);
//		
//	}
	
//	public Node dfsNodeDirBestGuess(Node node, ArrayList<Node> cnctNodes, HashMap<Integer,Edge> tarEdges){
//		
//		for(Node cnctNode: cnctNodes){
//			if(cnctNode.coord.x < node.coord.x){
//				for(int i = cnctNode.coord.x; i>=1; i--){
//					if(tarEdges.containsKey(hash4Int(i-1,cnctNode.coord.y,i,cnctNode.coord.y))){
//						return cnctNode;
//					}
//				}
//			}
//			else if(cnctNode.coord.x > node.coord.x){
//				for(int i = cnctNode.coord.x; i<=height -1; i++){
//					if(tarEdges.containsKey(hash4Int(i,cnctNode.coord.y,i+1,cnctNode.coord.y))){
//						return cnctNode;
//					}
//				}
//			}
//			
//			if(cnctNode.coord.y < node.coord.y){
//				for(int j = cnctNode.coord.y; j>=1; j--){
//					if(tarEdges.containsKey(hash4Int(cnctNode.coord.x,j-1,cnctNode.coord.x,j))){
//						return cnctNode;
//					}
//				}
//			}
//			else if(cnctNode.coord.y > node.coord.y){
//				for(int j = cnctNode.coord.y; j<=width -1; j++){
//					if(tarEdges.containsKey(hash4Int(cnctNode.coord.x,j,cnctNode.coord.x,j+1))){
//						return cnctNode;
//					}
//				}
//			}			
//			
//		}
//		
//		return cnctNodes.get(cnctNodes.size()-1);
//		
//	}
	
//	public void move(ArrayList<Node> path, ArrayList<Node> checkPoints, Dir dir){
//	Node node = path.get(path.size()-1);
//	Node nextNode = move1Step(node, dir);
//	while(nextNode != null){
//		path.add(nextNode);
//		for(Dir d:Dir.values()){
//			if(d != dir){
//				Node n = move1Step(node,dir);
//				if( n != null)
//					checkPoints.add(node);
//			}
//		}
//		
//	}
//}

//public Edge findNextTargetEdge(HashMap<Integer,Edge> targetEdges, Node start, Dir dir){
//	Edge targetEdge = null;
//	switch(dir){
//	case East:
//		for(int j = start.coord.y; j < width; j ++){
//			Edge e = targetEdges.get(start.coord.x*100 + j);
//			if( e != null){
//				return e;
//			}
//		}
//		Node node = hashNodes.get((start.coord.x + 1)*100 + width -1);
//		if(node == exit)
//			return null;
//		else{
//			return findNextTargetEdge(targetEdges, node, Dir.West);
//		}
//		
//	case West:
//		for(int j = start.coord.y; j >= 0 ; j --){
//			Edge e = targetEdges.get(start.coord.x*100 + j);
//			if( e != null){
//				return e;
//			}
//		}
//		node = hashNodes.get((start.coord.x + 1)*100 + 0 -1);
//		if(node == exit)
//			return null;
//		else{
//			return findNextTargetEdge(targetEdges, node, Dir.East);
//		}
//		
//	case North:
//		for(int i = start.coord.x; i < height ; i++){
//			Edge e = targetEdges.get(i * 100 + start.coord.y);
//			if( e != null){
//				return e;
//			}
//		}
//		node = hashNodes.get(0*100 + start.coord.y + 1);
//		if(node == exit)
//			return null;
//		else{
//			return findNextTargetEdge(targetEdges, node, Dir.South);
//		}
//		
//	case South:
//		for(int i = start.coord.x; i >=0 ; i--){
//			Edge e = targetEdges.get(i * 100 + start.coord.y);
//			if( e != null){
//				return e;
//			}
//		}
//		node = hashNodes.get((height -1 )*100 + start.coord.y + 1);
//		if(node == exit)
//			return null;
//		else{
//			return findNextTargetEdge(targetEdges, node, Dir.North);
//		}
//		
//	}
//	
//	
//	return targetEdge;
//}


//public ArrayList<Edge> greedyWalk(HashMap<Integer,Edge> targetEdges,Dir dir){
//	ArrayList<Edge> path = new ArrayList<Edge>();
//	Node nextNode;
//	switch(dir){
//	case East:
//		Edge nextEdge = findNextTargetEdge(targetEdges, entrance, Dir.East);
//		if(nextEdge == null)
//			return null;
//		//nextNode = greedyNodeToEdge(path, targetEdges,nextEdge,entrance,Direction.East);
//		break;
//	case North:			
//		break;
//	}
//		
//	
//	
//	return path;
//}





//public boolean dirContainEdge(Node node, Dir searchDir, HashMap<Integer,Edge> targetEdges){
//	Dir d = reverseDir(searchDir);
//	return reverseDirContainEdge(node,d,targetEdges);
//	
//}
//	
//	public void findPaths(){
//	pathsNode = new ArrayList<ArrayList<Node>>();
//	pathsNode.add(detourWalk(Dir.East,hashTarEdges));
//	pathsNode.add(detourWalk(Dir.North,hashTarEdges));
//	ArrayList<Node> path = new ArrayList<Node>();
//	Entry<Integer, Edge> entry;
//		while(hashTarEdges.size()>0){ 			
//			entry = hashTarEdges.entrySet().iterator().next();
//			path.clear();			
//		Edge tarEdge = hashTarEdges.remove(entry.getKey());
//		Node a = getNode(tarEdge.coord.x,tarEdge.coord.y);
//		Node b = getNode(tarEdge.coord.s,tarEdge.coord.t);
//		path.add(b);path.add(a);
//		DFS(a,entrance,path,b,hashTarEdges);
//		reverseList(path);
//		DFS(b,exit,path,a,hashTarEdges);
//		pathsNode.add((ArrayList<Node>) path.clone());
//		for(Node n:path){
//			hashTarEdges.remove(n);
//		}
//		
//	}
//}
//
//private <T> void reverseList(ArrayList<T> list){
//	ArrayList<T> stack = (ArrayList<T>) list.clone();
//	list.clear();
//	
//	for(int i = stack.size()-1; i>=0; i--){
//		list.add(stack.get(i));
//	}
//}



//public Graph(int i){
//	//creat a default grid with the size of i
//	//assume i = 3
//	//hashWVariables = new HashMap<Edge,String>() ;
//	hashBinaries = new HashMap<Edge, String>();
//	cuts = new ArrayList<ArrayList<Edge>>();
//	paths =new ArrayList<ArrayList<Edge>>();
//	
//	
//	init3_3();
//	//findCuts();
//}	
	
//	public void init3_3(){
//	width = 3;
//	height = 3;
//	nodes = new Node[9];
//	hashEdges = new HashMap<Integer, Edge>();
//	
//	for(int j = 0;j < nodes.length;j++){
//		nodes[j] = new Node(j);
//	}
//	
//	nodes[0].setAdjNodes(nodes[3], nodes[1]);		
//	nodes[1].setAdjNodes(nodes[0],nodes[4],nodes[2]);
//	nodes[2].setAdjNodes(nodes[1],nodes[5]);
//	nodes[3].setAdjNodes(nodes[0],nodes[4],nodes[6]);
//	nodes[4].setAdjNodes(nodes[1],nodes[3],nodes[5],nodes[7]);
//	nodes[5].setAdjNodes(nodes[2],nodes[4],nodes[8]);
//	nodes[6].setAdjNodes(nodes[3],nodes[7]);
//	nodes[7].setAdjNodes(nodes[4],nodes[6],nodes[8]);
//	nodes[8].setAdjNodes(nodes[7],nodes[5]);
//	
//	getHashEdges();
//	
//	
////	for (int j =0; j< 3; j++ ){
////		k = rnd.nextInt(edges.size()-1);
////		edges.get(k).setSA0();
////		
////	}
//	
//	edges.get(1).setSA0();
//	
//	entrance = nodes[0];
//	exit = nodes[8];
//}
//
	
//	public void findPathsTest(){
//	
//	Node node = this.entrance;
//	Node nextNode;
//	Edge edge;
//	paths = new ArrayList<ArrayList<Edge>>();
//	ArrayList<Edge> path ;
//	
//	for(int i =1; i< this.height ; i++){
//		node = this.entrance;
//		path = new ArrayList<Edge>();
//		node =moveNorthK(node,i,path);
//		node =moveEastK(node,this.width-1,path);
//		node =moveNorthK(node,this.height-i-1,path);
//		paths.add(path);
//	}
//	
//	
//	for (int i =1; i< this.width ;i++){
//		node = this.entrance;
//		path = new ArrayList<Edge>();
//		node =moveEastK(node,i,path);
//		node =moveNorthK(node,this.height-1,path);
//		node =moveEastK(node,this.width-i-1,path);
//		paths.add(path);
//	}			
//	
//}
//
//public void findCutsTest(){
//	cuts = new ArrayList<ArrayList<Edge>>();
//	ArrayList<Edge> cut ;
//	
//	cut= new ArrayList<Edge>();
//	cut.add(this.getEdge(0, 1));
//	cut.add(this.getEdge(0, 3));
//	cuts.add(cut);
//	
//	cut= new ArrayList<Edge>();
//	cut.add(this.getEdge(3, 6));
//	cut.add(this.getEdge(3, 4));
//	cut.add(this.getEdge(4, 1));
//	cut.add(this.getEdge(1, 2));
//	cuts.add(cut);
//	
//	cut= new ArrayList<Edge>();
//	cut.add(this.getEdge(7, 6));
//	cut.add(this.getEdge(7, 4));
//	cut.add(this.getEdge(4, 5));
//	cut.add(this.getEdge(5, 2));
//	cuts.add(cut);
//	
//	cut= new ArrayList<Edge>();
//	cut.add(this.getEdge(7, 8));
//	cut.add(this.getEdge(8, 5));
//	cuts.add(cut);
//	
//	
//}

//private Node moveNorthK( Node node,int k, ArrayList<Edge> path){
//	Node nextNode = null;
//	
//	Edge edge;
//	for(int i =0; i<k; i++){
//		nextNode = moveNorth(node);
//		edge = this.getEdge(node, nextNode);
//		path.add(edge);
//		node = nextNode;
//	}
//	return nextNode;
//
//}
//
//
//
//private Node moveEastK( Node node, int k, ArrayList<Edge> path){
//	Node nextNode = null;
//	
//	Edge edge;
//	for(int i =0; i<k; i++){
//		nextNode = moveEast(node);
//		edge = this.getEdge(node, nextNode);
//		path.add(edge);
//		node = nextNode;
//	}
//	return nextNode;
//}
//
//private Node moveNorth(Node node){
//	Node nNode = null;
//	int i,j;
//	j = node.number%3;
//	i = (int) Math.floor(node.number/3);
//	if(i == this.height-1)
//		return nNode;
//	else
//		return this.nodes[(i+1)*3 + j];		
//}
//
//private Node moveEast(Node node){
//	Node nNode = null;
//	int i,j;
//	j = node.number%3;
//	i = (int) Math.floor(node.number/3);
//	if(j == this.width-1)
//		return nNode;
//	else
//		return this.nodes[i*3 + j+1];		
//}
	
	//given the direction and flow of each edge, find each paths 
//	private void findPathsOLD(){
//		
//		Stack<Node> stack = new Stack<Node>();		
//		ArrayList<Edge> path = new ArrayList<Edge>();
//		ArrayList<Node> pathNode = new ArrayList<Node>();
//		ArrayList<Node> cnctNodes;		
//		Node node;
//		Node start = exit;
//
//		
//		
//		while(getFlow(entrance)!=0){	
//			for(Edge e:getJointEdges(entrance)){
//				if(e.weight>0)
//					start = getJointNode(entrance,e);
//				else
//					start = exit;
//			}
//			
//			stack.push(start);
//			
//			while(!stack.isEmpty()){
//				node = stack.pop();
//				
//				if(node == exit){
//					pathNode.add(node);
//					path = pathNodeToEdge(pathNode);				
//					for(Edge e:path){
//						e.weight--;
//						if(e.weight>getFlow(entrance)){
//							pathNode.remove(node);						
//							continue;
//						}
//					}
//					
//					break;
//				}
//				if(pathNode.contains(node))
//					continue;
//				
//				pathNode.add(node);
//				
//				
//				
//				cnctNodes = getCnctNodes(node);
//				if(cnctNodes.size()>0){
//					for(Node cnctNode:cnctNodes)
//						stack.add(cnctNode);			
//				}
//				else{
//					pathNode.remove(node);
//				}
//			}
//			// if is a valid path
//			if(pathNode.get(pathNode.size()-1) == exit){
//				path = pathNodeToEdge(pathNode);
//				paths.add(path);
//			}
//		}		
//	}
	
//	private ArrayList<Edge> pathNodeToEdge(ArrayList<Node> pathNode){
//		ArrayList<Edge> pathEdge = new ArrayList<Edge>();
//		Node a;
//		Node b;
//		for(int i = 0; i< pathNode.size() -1; i ++){
//			int j = i+1;
//			a = pathNode.get(i);
//			b = pathNode.get(j);
//			Edge e = getEdge(a,b);
//			pathEdge.add(e);
//		}
//		return pathEdge;
//	}
		
	
//	private ArrayList<Node> pathEdgeToNode(ArrayList<Edge> pathEdge){
//		ArrayList<Node> pathNode = new ArrayList<Node>();
//		Node start = entrance;
//		Node end;
//		
//		pathNode.add(start);
//		for(Edge e: pathEdge){
//			end = getJointNode(start,e);
//			pathNode.add(end);
//			start = end;
//		}
//		return pathNode;
//		
//		
//	}
	
//	private ArrayList<Edge> findCriticalPath(){
//		ArrayList<Edge> critPath = new ArrayList<Edge>();
//		return critPath;
//	}
	
//	public void findCuts(ArrayList<Edge> criticalPath){
//		
//		ArrayList<Node> S = new ArrayList<Node>();
//		ArrayList<Node> Sbrink = new ArrayList<Node>();
//		ArrayList<Node> SbrnkCache = new ArrayList<Node>();		
//		ArrayList<Edge> cut = new ArrayList<Edge>();
//		int nextEdgeP;
//		
//		Node start;
//		
// 		start = entrance;
//		S.add(start);
//		Sbrink.add(start);
//		for(int i = 0; i <= criticalPath.size()-1; i ++){
//			nextEdgeP = i;
//			cut = new ArrayList<Edge>();
//			// what will happen if I change S during the loop
//			for(Node node:Sbrink){				
//				for(Edge e:findCutOfNode(node, S,SbrnkCache,criticalPath,nextEdgeP))
//					cut.add(e);		
//				
//			}
//			S.addAll(SbrnkCache);
//			Sbrink.clear();
//			Sbrink.addAll(SbrnkCache);
//			SbrnkCache.clear();
//			cuts.add(cut);
//			
//		}		
//	}
	
//	public ArrayList<Edge> findCutOfNode(Node node,ArrayList<Node> S, ArrayList<Node> sBrinkCache, 
//	ArrayList<Edge> critPath,int nextEdgeP){
//ArrayList<Edge> cutEdges = new ArrayList<Edge>();
//for(Node adjNode:node.getAdjNodes()){
//if(!S.contains(adjNode)){
//Edge e = getEdge(node,adjNode);
//
//if(adjNode == exit){
//if(!sBrinkCache.contains(node))
//sBrinkCache.add(node);
//cutEdges.add(e);
//
//continue;
//}
//
//if(critPath.contains(e)){
//if(e != critPath.get(nextEdgeP)){
//if(!sBrinkCache.contains(node))
//sBrinkCache.add(node);
//continue;
//}
//// e == nextEdge
//else{
//if(e instanceof Hole){
//S.add(adjNode);
//cutEdges.addAll(findCutOfNode(adjNode,S,sBrinkCache,critPath,nextEdgeP++));
//continue;
//}
//else{
//if(!sBrinkCache.contains(adjNode))
//sBrinkCache.add(adjNode);
//cutEdges.add(e);
//}
//}
//
//}
//
//if(e instanceof Hole){
//S.add(adjNode);
//cutEdges.addAll( findCutOfNode(adjNode,S,sBrinkCache,critPath,nextEdgeP++));
//continue;
//}
//else{
//if(!sBrinkCache.contains(adjNode))
//sBrinkCache.add(adjNode);
//cutEdges.add(e);
//continue;
//}
//
//}
//
//}
//return cutEdges;
//}
	
//	public void getILPContrains(){
//	variables = new ArrayList<String>();
//	variableTypes = new ArrayList<Integer>();
//	String constrain = "";
//	for(Node node:nodes){
//		constrain = "";
//		if(node != entrance & node != exit){
//			for(Edge e:getJointEdges(node)){
//				String variable = "x" + e.number; 
//				String binary = "y" +e.number;
//				if(!hashWVariables.containsKey(e)){
//					hashWVariables.put(e, variable);
//					hashBinaries.put(e, binary);
//					variables.add(variable);
//					variableTypes.add(0);
//					variables.add(binary);
//					variableTypes.add(1);
//				}
//				if(e instanceof Wall)
//					continue;
//				
//				if(isInflow(e,node))
//					constrain += "+" + variable;
//				else
//					constrain += "-" + variable;
//			}
//			constrain += "= 0";
//			ILP.add(constrain);
//		}
//		
//	}
//	
//	//flow >= 1 for every edge connect to entrance and exit
//	
//	for(Edge e: getJointEdges(entrance) ){
//		constrain = "";
//		String varialbe = hashWVariables.get(e);
//		
//		constrain = varialbe + ">=1";	
//		ILP.add(constrain);
//	}
//	
//	for(Edge e:getJointEdges(exit)){
//		constrain = "";
//		String variable = hashWVariables.get(e);
//		constrain = variable + ">=1";
//		ILP.add(constrain);
//		
//	}
//	//for every edge except extrance and exit 
//	ArrayList<Edge> internalEdges =  new ArrayList<Edge>();
//	
//	for(Edge e:edges){
//		internalEdges.add(e);		
//	}
//	
//	for(Edge e:getJointEdges(entrance)){
//		internalEdges.remove(e);
//	}
//	
//	for(Edge e:getJointEdges(exit)){
//		internalEdges.remove(e);
//	}
//	
//	for(Edge e:internalEdges){
//		constrain = "";
//		String variable = hashWVariables.get(e);
//		String binary = hashBinaries.get(e);
//		constrain = variable+"+ 1000"+binary+" >=1";
//		ILP.add(constrain);
//		constrain = variable + "+1000"+ binary +"<= 999";
//		ILP.add(constrain);		
//	}
//	obj = setILPObj();
//	
//}
}
	
	

