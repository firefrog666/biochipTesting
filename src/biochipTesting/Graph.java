package biochipTesting;
import java.util.Random;
import java.util.Stack;

import javax.swing.SpringLayout.Constraints;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Graph {
	
	private static final int upBound = 10; 
	private static final int lowBound = 3; 
	
	Node[] nodes;
	List<Edge> edges;
	Node entrance;
	Node exit;
	int width;
	int height;
	HashMap<Integer,Edge> hashEdges;  
	HashMap<Integer,Node> hashNodes; //Hash Node by its coordinates i*100 + j; 
	HashMap<Integer,Edge> hashEdgesHori; // hash horizontal edges by coordinate of left down point
	HashMap<Integer,Edge> hashEdgesVert; // hash vertical edges by coordinate of left down point
 	
	HashMap<Edge,String> hashVariables;
	HashMap<Edge, String> hashBinaries;
	ArrayList<ArrayList<Edge>> paths;
	ArrayList<ArrayList<Edge>> cuts;
	ArrayList<String> ILP;
	ArrayList<String> variables;
	ArrayList<Integer> variableTypes;
	String obj;
	

	
	public Graph(){
		
		hashVariables = new HashMap<Edge,String>() ;
		hashBinaries = new HashMap<Edge, String>();
		hashEdgesHori = new HashMap<Integer,Edge>();
		hashEdgesVert = new HashMap<Integer,Edge>();
		hashEdges = new HashMap<Integer,Edge>();
		hashNodes  = new HashMap<Integer,Node>();
		randomInit();
		//init3_3();		
	}

	public Graph(int i){
		//creat a default grid with the size of i
		//assume i = 3
		hashVariables = new HashMap<Edge,String>() ;
		hashBinaries = new HashMap<Edge, String>();
		cuts = new ArrayList<ArrayList<Edge>>();
		paths =new ArrayList<ArrayList<Edge>>();
		ILP = new ArrayList<String>();
		
		init3_3();
		//findCuts();
	}	
	
	public void randomInit(){
		Random rnd = new Random();
		width = rnd.nextInt(upBound)+lowBound;
		height = rnd.nextInt(upBound)+lowBound;
		
		width = 3;
		height = 3;
		
		nodes = new Node[width * height];
		hashEdges = new HashMap<Integer, Edge>(); 
		
		for(int i = 0; i< nodes.length;i++){
			nodes[i] = new Node();
		}
		
		//give number to each Node; Hash nodes	
		
		int id = 0;
		hashNodes = new HashMap<Integer, Node>();
		for(int i = 0; i< width; i++){
			for(int j =0; j < height ; j ++){
				nodes[id].setNumber(id);
				nodes[id].setCoordinate(i, j);
				hashNodes.put(i*100+j, nodes[id]);
				id ++;
			}
		}
		
		//assign joint nodes to each node
		for(int i = 0; i< width; i++){
			for(int j =0; j < height ; j ++){
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
		
		
		entrance = nodes[0];
		exit = nodes[height * width - 1];
	}
	
	public void init3_3(){
		width = 3;
		height = 3;
		nodes = new Node[9];
		hashEdges = new HashMap<Integer, Edge>();
		
		for(int j = 0;j < nodes.length;j++){
			nodes[j] = new Node(j);
		}
		
		nodes[0].setAdjNodes(nodes[3], nodes[1]);		
		nodes[1].setAdjNodes(nodes[0],nodes[4],nodes[2]);
		nodes[2].setAdjNodes(nodes[1],nodes[5]);
		nodes[3].setAdjNodes(nodes[0],nodes[4],nodes[6]);
		nodes[4].setAdjNodes(nodes[1],nodes[3],nodes[5],nodes[7]);
		nodes[5].setAdjNodes(nodes[2],nodes[4],nodes[8]);
		nodes[6].setAdjNodes(nodes[3],nodes[7]);
		nodes[7].setAdjNodes(nodes[4],nodes[6],nodes[8]);
		nodes[8].setAdjNodes(nodes[7],nodes[5]);
		
		getHashEdges();
		
		
//		for (int j =0; j< 3; j++ ){
//			k = rnd.nextInt(edges.size()-1);
//			edges.get(k).setSA0();
//			
//		}
		
		edges.get(1).setSA0();
		
		entrance = nodes[0];
		exit = nodes[8];
	}
	
	public void getHashEdges(){
		
		hashEdges = new HashMap<Integer, Edge>();
		edges = new ArrayList<Edge>();
		int i = 0;
		for(Node node:nodes){
			Node[] adjNodes = node.getAdjNodes();
			for(Node adjNode: adjNodes){
				if(node.number < adjNode.number){
					Edge edge = new Edge(i);i++;
					edge.coordinate.x = node.coordinate.x;
					edge.coordinate.y = node.coordinate.y;
					edge.coordinate.s = adjNode.coordinate.x;
					edge.coordinate.t = adjNode.coordinate.y;
					if(node.coordinate.x == adjNode.coordinate.x)
						edge.isHorizontal = true;
					else
						edge.isHorizontal = false;
					edges.add(edge);
					hashEdges.put(adjNode.number * 10 + node.number, edge);
					if(edge.isHorizontal)
						hashEdgesHori.put(edge.coordinate.x * 100 + edge.coordinate.y, edge);
					else
						hashEdgesVert.put(edge.coordinate.x*100 + edge.coordinate.y, edge);
				}
			}
		}
	}
	
	public Node getNode(int a, int b){
		return hashNodes.get(a*100+b);
	}
	
	public Edge getEdge(int a, int b){
		return getEdge(nodes[a], nodes[b]);
	}
	
	public Edge getEdge(Node a, Node b){
		int bigNumber, smallNumber;
		if(a.number < b.number){
			bigNumber = b.number; 
			smallNumber =a.number;
		}				
		else{
			bigNumber = a.number; 
			smallNumber =b.number;					
		}
		
		return hashEdges.get(bigNumber *10 + smallNumber);
	}
	
	public ArrayList<Node> getCnctNodes(Node node){
		int bigNumber, smallNumber;
		ArrayList<Node> cnctNodes = new ArrayList<Node>();
		Node[] adjNodes = node.getAdjNodes();
		Edge edge = null;
		for(Node adjNode:adjNodes){
				
			if(adjNode.number < node.number){
				bigNumber = node.number; 
				smallNumber =adjNode.number;
			}				
			else{
				bigNumber = adjNode.number; 
				smallNumber =node.number;					
			}
			
			if(hashEdges.containsKey(bigNumber * 10 + smallNumber)){
				edge = hashEdges.get(bigNumber * 10 + smallNumber);
			}
			
			if (edge.on)
				cnctNodes.add(adjNode);
		}
		
		return cnctNodes;
	}
	
	public ArrayList<Node> getJointNodes(Node node){
		ArrayList<Node> jointNodes = new ArrayList<Node>();
		Node[] adjNodes = node.getAdjNodes();
		for(Node adjNode:adjNodes){
			jointNodes.add(adjNode);
		}
		
		return jointNodes;
	}
	
	//connect means edge between two node is note wall
	public ArrayList<Node> getConnectedNodes(Node node){
		ArrayList<Node> connectedNodes = new ArrayList<Node>();
		Node[] adjNodes = node.getAdjNodes();
		for(Node adjNode:adjNodes){
			
			if(!(getEdge(adjNode,node) instanceof Wall))
				connectedNodes.add(adjNode);
		}
		
		return connectedNodes;
	}
public void findPathsTest(){
		
		Node node = this.entrance;
		Node nextNode;
		Edge edge;
		paths = new ArrayList<ArrayList<Edge>>();
		ArrayList<Edge> path ;
		
		for(int i =1; i< this.height ; i++){
			node = this.entrance;
			path = new ArrayList<Edge>();
			node =moveNorthK(node,i,path);
			node =moveEastK(node,this.width-1,path);
			node =moveNorthK(node,this.height-i-1,path);
			paths.add(path);
		}
		
		
		for (int i =1; i< this.width ;i++){
			node = this.entrance;
			path = new ArrayList<Edge>();
			node =moveEastK(node,i,path);
			node =moveNorthK(node,this.height-1,path);
			node =moveEastK(node,this.width-i-1,path);
			paths.add(path);
		}			
		
	}
	
	public void findCutsTest(){
		cuts = new ArrayList<ArrayList<Edge>>();
		ArrayList<Edge> cut ;
		
		cut= new ArrayList<Edge>();
		cut.add(this.getEdge(0, 1));
		cut.add(this.getEdge(0, 3));
		cuts.add(cut);
		
		cut= new ArrayList<Edge>();
		cut.add(this.getEdge(3, 6));
		cut.add(this.getEdge(3, 4));
		cut.add(this.getEdge(4, 1));
		cut.add(this.getEdge(1, 2));
		cuts.add(cut);
		
		cut= new ArrayList<Edge>();
		cut.add(this.getEdge(7, 6));
		cut.add(this.getEdge(7, 4));
		cut.add(this.getEdge(4, 5));
		cut.add(this.getEdge(5, 2));
		cuts.add(cut);
		
		cut= new ArrayList<Edge>();
		cut.add(this.getEdge(7, 8));
		cut.add(this.getEdge(8, 5));
		cuts.add(cut);
		
		
	}
	
	public Node moveNorthK( Node node,int k, ArrayList<Edge> path){
		Node nextNode = null;
		
		Edge edge;
		for(int i =0; i<k; i++){
			nextNode = moveNorth(node);
			edge = this.getEdge(node, nextNode);
			path.add(edge);
			node = nextNode;
		}
		return nextNode;
	
	}
	
	
	
	public Node moveEastK( Node node, int k, ArrayList<Edge> path){
		Node nextNode = null;
		
		Edge edge;
		for(int i =0; i<k; i++){
			nextNode = moveEast(node);
			edge = this.getEdge(node, nextNode);
			path.add(edge);
			node = nextNode;
		}
		return nextNode;
	}
	
	public Node moveNorth(Node node){
		Node nNode = null;
		int i,j;
		j = node.number%3;
		i = (int) Math.floor(node.number/3);
		if(i == this.height-1)
			return nNode;
		else
			return this.nodes[(i+1)*3 + j];		
	}
	
	public Node moveEast(Node node){
		Node nNode = null;
		int i,j;
		j = node.number%3;
		i = (int) Math.floor(node.number/3);
		if(j == this.width-1)
			return nNode;
		else
			return this.nodes[i*3 + j+1];		
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
	
	public int getFlow(Node node){
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
	
	public boolean DFS(Node start, Node end){
		Stack<Node> stack = new Stack<Node>();
		ArrayList<Node> discoveredNodes = new ArrayList<Node>();
		ArrayList<Node> cnctNodes;
		stack.add(start);
		Node node;
		
		
 		
		while(!stack.isEmpty()){
			node = stack.pop();
			
			if(discoveredNodes.contains(node))
				continue;
			else
				discoveredNodes.add(node);
			
			cnctNodes= getConnectedNodes(node);
			if(cnctNodes.size() >0){				
				for( Node cnctNode:cnctNodes){											
						if(cnctNode == end)	
							return true;
						else
							stack.add(cnctNode);
				}
			}			
		}
		
		return false;
	}
	
	public ArrayList<Edge> getJointEdges(Node node){
		ArrayList <Edge> edges = new ArrayList<Edge>();
		ArrayList<Node> jointNodes = getJointNodes(node);
		
		for(Node jointNode:jointNodes){
			Edge e = getEdge(node,jointNode);
			edges.add(e);
		}
		
		return edges;
	}
	
	public Node getJointNode(Node node, Edge e){
		
		ArrayList<Node> nodes = getJointNodes(node);
		for(Node n : nodes){
			if (getEdge(n,node) == e)
				return n;
		}
		
		System.out.println("there is no node linked by this edge");
		return null;
		
	}
	
	//given the direction and flow of each edge, find each paths 
	public void findPathsOLD(){
		
		Stack<Node> stack = new Stack<Node>();		
		ArrayList<Edge> path = new ArrayList<Edge>();
		ArrayList<Node> pathNode = new ArrayList<Node>();
		ArrayList<Node> cnctNodes;		
		Node node;
		Node start = exit;

		
		
		while(getFlow(entrance)!=0){	
			for(Edge e:getJointEdges(entrance)){
				if(e.weight>0)
					start = getJointNode(entrance,e);
				else
					start = exit;
			}
			
			stack.push(start);
			
			while(!stack.isEmpty()){
				node = stack.pop();
				
				if(node == exit){
					pathNode.add(node);
					path = pathNodeToEdge(pathNode);				
					for(Edge e:path){
						e.weight--;
						if(e.weight>getFlow(entrance)){
							pathNode.remove(node);						
							continue;
						}
					}
					
					break;
				}
				if(pathNode.contains(node))
					continue;
				
				pathNode.add(node);
				
				
				
				cnctNodes = getCnctNodes(node);
				if(cnctNodes.size()>0){
					for(Node cnctNode:cnctNodes)
						stack.add(cnctNode);			
				}
				else{
					pathNode.remove(node);
				}
			}
			// if is a valid path
			if(pathNode.get(pathNode.size()-1) == exit){
				path = pathNodeToEdge(pathNode);
				paths.add(path);
			}
		}		
	}
	
	public ArrayList<Edge> pathNodeToEdge(ArrayList<Node> pathNode){
		ArrayList<Edge> pathEdge = new ArrayList<Edge>();
		for(int i = 0; i< pathNode.size() -1; i ++){
			int j = i+1;
			Edge e = getEdge(i,j);
			pathEdge.add(e);
		}
		return pathEdge;
	}
		
	
	public ArrayList<Node> pathEdgeToNode(ArrayList<Edge> pathEdge){
		ArrayList<Node> pathNode = new ArrayList<Node>();
		Node start = entrance;
		Node end;
		
		pathNode.add(start);
		for(Edge e: pathEdge){
			end = getJointNode(start,e);
			pathNode.add(end);
			start = end;
		}
		return pathNode;
		
		
	}
	
	
	
	public void findCuts(){
		//ArrayList<Edge> pathEdge = pathNodeToEdge(criticalPath);
		ArrayList<Node> S = new ArrayList<Node>();
		ArrayList<Node> Sbrink = new ArrayList<Node>();
		ArrayList<Node> SbrnkTemp = new ArrayList<Node>();
		//ArrayList<Node> T;
		ArrayList<Edge> cut = new ArrayList<Edge>();
		Node start;
		
		start = entrance;
		S.add(start);
		Sbrink.add(start);
		for(int i = 0; i < (width -1  + height -1 ); i ++){
			cut.clear();
			// what will happen if I change S during the loop
			for(Node node:Sbrink){				
				for(Edge e:findCutOfNode(node, S,SbrnkTemp))
					cut.add(e);		
				
			}
			S.addAll(SbrnkTemp);
			Sbrink.clear();
			Sbrink.addAll(SbrnkTemp);
			SbrnkTemp.clear();
			cuts.add(cut);
			
		}		
	}
	
	
	
	public ArrayList<Edge> findCutOfNode(Node node,ArrayList<Node> S, ArrayList<Node> SBrinkTemp){
		ArrayList<Edge> cutEdges = new ArrayList<Edge>();
		for(Node adjNode:node.adjNodes){
			if(!S.contains(adjNode)){
				Edge e = getEdge(node,adjNode);
				if(e instanceof Hole){
					S.add(adjNode);
					cutEdges.addAll(findCutOfNode(adjNode,S,SBrinkTemp));
				}
				else{
					if(!SBrinkTemp.contains(adjNode))
						SBrinkTemp.add(adjNode);
					cutEdges.add(e);
				}
				
			}
		}	
		
		return cutEdges;
	}
	
	
	public String setILPObj(){
		String obj = "";
		for(Edge e:getJointEdges(entrance)){
			obj += "+" + hashVariables.get(e);
		}
		return obj;
	}
	
	public void getILPContrains(){
		variables = new ArrayList<String>();
		variableTypes = new ArrayList<Integer>();
		String constrain = "";
		for(Node node:nodes){
			constrain = "";
			if(node != entrance & node != exit){
				for(Edge e:getJointEdges(node)){
					String variable = "x" + e.number;
					String binary = "y" +e.number;
					if(!hashVariables.containsKey(e)){
						hashVariables.put(e, variable);
						hashBinaries.put(e, binary);
						variables.add(variable);
						variableTypes.add(0);
						variables.add(binary);
						variableTypes.add(1);
					}
					if(e instanceof Wall)
						continue;
					
					if(isInflow(e,node))
						constrain += "+" + variable;
					else
						constrain += "-" + variable;
				}
				constrain += "= 0";
				ILP.add(constrain);
			}
			
		}
		
		//flow >= 1 for every edge connect to entrance and exit
		
		for(Edge e: getJointEdges(entrance) ){
			constrain = "";
			String varialbe = hashVariables.get(e);
			
			constrain = varialbe + ">=1";	
			ILP.add(constrain);
		}
		
		for(Edge e:getJointEdges(exit)){
			constrain = "";
			String variable = hashVariables.get(e);
			constrain = variable + ">=1";
			ILP.add(constrain);
			
		}
		//for every edge except extrance and exit 
		ArrayList<Edge> internalEdges =  new ArrayList<Edge>();
		
		for(Edge e:edges){
			internalEdges.add(e);		
		}
		
		for(Edge e:getJointEdges(entrance)){
			internalEdges.remove(e);
		}
		
		for(Edge e:getJointEdges(exit)){
			internalEdges.remove(e);
		}
		
		for(Edge e:internalEdges){
			constrain = "";
			String variable = hashVariables.get(e);
			String binary = hashBinaries.get(e);
			constrain = variable+"+ 1000"+binary+" >=1";
			ILP.add(constrain);
			constrain = variable + "+1000"+ binary +"<= 999";
			ILP.add(constrain);		
		}
		obj = setILPObj();
		
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
	
	
	
	public ArrayList<ArrayList<Edge>> findPaths(){
		ArrayList<ArrayList<Edge>> paths = new ArrayList<ArrayList<Edge>>();
		ArrayList<Edge> path = new ArrayList<Edge>();
		HashMap<Integer,Edge> targetEdgesHori = new HashMap<Integer,Edge>();
		HashMap<Integer,Edge> targetEdgesVert = new HashMap<Integer,Edge>();
		
		while(!targetEdgesVert.isEmpty()){
			path = greedyWalk(targetEdgesVert,Direction.North);
			paths.add(path);
		}
		
		while(!targetEdgesHori.isEmpty()){
			path = greedyWalk(targetEdgesHori, Direction.East);
			paths.add(path);
		}
		
		return paths;
	}
	
	public Node greedyNodeToEdge(ArrayList<Edge>path, HashMap<Integer,Edge> targetEdges, Edge nextEdge,Node start,Direction dir){
		Node nextNode = null;
		
		return nextNode;		
	}
	
	public Edge findNextTargetEdge(HashMap<Integer,Edge> targetEdges, Node start, Direction dir){
		Edge targetEdge = null;
		switch(dir){
		case East:
			for(int j = start.coordinate.y; j < width; j ++){
				Edge e = targetEdges.get(start.coordinate.x*100 + j);
				if( e != null){
					return e;
				}
			}
			Node node = hashNodes.get((start.coordinate.x + 1)*100 + width -1);
			if(node == exit)
				return null;
			else{
				return findNextTargetEdge(targetEdges, node, Direction.West);
			}
			
		case West:
			for(int j = start.coordinate.y; j >= 0 ; j --){
				Edge e = targetEdges.get(start.coordinate.x*100 + j);
				if( e != null){
					return e;
				}
			}
			node = hashNodes.get((start.coordinate.x + 1)*100 + 0 -1);
			if(node == exit)
				return null;
			else{
				return findNextTargetEdge(targetEdges, node, Direction.East);
			}
			
		case North:
			for(int i = start.coordinate.x; i < height ; i++){
				Edge e = targetEdges.get(i * 100 + start.coordinate.y);
				if( e != null){
					return e;
				}
			}
			node = hashNodes.get(0*100 + start.coordinate.y + 1);
			if(node == exit)
				return null;
			else{
				return findNextTargetEdge(targetEdges, node, Direction.South);
			}
			
		case South:
			for(int i = start.coordinate.x; i >=0 ; i--){
				Edge e = targetEdges.get(i * 100 + start.coordinate.y);
				if( e != null){
					return e;
				}
			}
			node = hashNodes.get((height -1 )*100 + start.coordinate.y + 1);
			if(node == exit)
				return null;
			else{
				return findNextTargetEdge(targetEdges, node, Direction.North);
			}
			
		}
		
		
		return targetEdge;
	}
	
	
	public ArrayList<Edge> greedyWalk(HashMap<Integer,Edge> targetEdges,Direction dir){
		ArrayList<Edge> path = new ArrayList<Edge>();
		Node nextNode;
		switch(dir){
		case East:
			Edge nextEdge = findNextTargetEdge(targetEdges, entrance, Direction.East);
			if(nextEdge == null)
				return null;
			nextNode = greedyNodeToEdge(path, targetEdges,nextEdge,entrance,Direction.East);
			break;
		case North:			
			break;
		}
			
		
		
		return path;
	}
	
	public void sortEdges(ArrayList<Edge> edges, ArrayList<Edge> targetEdgesHori, ArrayList<Edge> targetEdgesVert){
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public ArrayList<Edge>  detourWalk(Boolean toNorth,ArrayList<Edge> targetEdges){
		ArrayList<Edge> path = new ArrayList<Edge>();
		ArrayList<Node> stack = new ArrayList<Node>();
		Node node = entrance;
		Node nextNode;
		Edge edge;
		
		if(toNorth){
			
		}
		else{
			
		}
		return path;
		
	}
	
//	public void goHorizontal1Step(Boolean arrowEast, Boolean arrowNorth, Node node){
//		if(arrowEast){
//			Node nextNode = moveEast(node);
//			Edge edge = getEdge(node,nextNode);
//			//if(edge instanceof Wall)
//				
//		}
//	}
	public Node move1Step(Node start,Direction dir){
		Node node = new Node();
		return node;
	}
	public boolean thereIsWay(Node a, Node b){
		Edge e = getEdge(a,b);
		if(e instanceof Wall)
			return false;
		else
			return true;
	}
	
	public boolean go(Node start, ArrayList<Node> stack, ArrayList<Node> checkPoints, Direction dir){
		
		boolean isCheckPoint = false;
		
		if(!thereIsWay(move1Step(start,dir),start))
			return false;
		// there is a way
		else{
			stack.add(start);
			for(Direction d : Direction.values()){
				if(thereIsWay(move1Step(start,d),start)){				
					if(d != dir)
						isCheckPoint =  true;							
				}
			}
			if(isCheckPoint)
				checkPoints.add(start);
		}
		return true;
	}
	
	public <T> void stackPopUtilCheckPoint(ArrayList<T> stack, T checkPoint){
		T o = stack.remove(stack.size()-1);
		if(o == checkPoint)
			stack.add(o);
		else
			stackPopUtilCheckPoint(stack,checkPoint);
	}
	public void goHorizontal(Direction horDirection, Direction verDirection, Node start, ArrayList<Node> path, 
			ArrayList<Edge> targetEdges){
 		ArrayList<Node> checkPoints = new ArrayList<Node>();
		Boolean thereIsaWay = true;
		Node checkPoint;
		
		HashMap<Integer, Edge> hashEdgeByCoordinate = new HashMap<Integer,Edge>();
		
		switch(horDirection){
		case East:	
			thereIsaWay = go(start,path,checkPoints,Direction.East);
			while(thereIsaWay){
				thereIsaWay = go(start,path,checkPoints,Direction.East);
			}
			checkPoint = checkPoints.remove(checkPoints.size());
			stackPopUtilCheckPoint(path, checkPoint);
			// there is no way East, go up or down?
			//if there is target edges down, aim for down
			
			for(int i  = 0; i < start.coordinate.y - 1; i++){
				if(hashEdgeByCoordinate.containsKey(i)){
					 go(start, path, checkPoints,Direction.South);
					 
					 break;
				}
			}
			
				
			//if(hashEdgeByCoordinate)
			
			break;
		case West:
			break;
		default:
			break;
		}
			
		 
		
		// if hit the brink go up
		// if hit the wall go up or down
		// if get the longitude of exit, go to the latitude of exit
	}
	
	public void goVertical(){
		
	}
	
	
}
	
	

