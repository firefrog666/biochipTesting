package biochipTesting;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import javax.swing.SpringLayout.Constraints;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class Graph {
	
	private static final int upBound = 10; 
	private static final int lowBound = 3; 
	
	Node[] nodes;
	List<Edge> edges;
	Node entrance;
	Node exit;
	int width;
	int height;
	Map<Integer,Edge> hashEdges;  
	HashMap<Integer,Node> hashNodes; //Hash Node by its coordinates i*100 + j; 
	
	HashMap<Integer,Edge> hashTarEdges;
 	
	HashMap<Edge,String> hashVariables;
	HashMap<Edge, String> hashBinaries;
	ArrayList<ArrayList<Edge>> paths;
	ArrayList<ArrayList<Node>> pathsNode;
	ArrayList<ArrayList<Edge>> cuts;
	ArrayList<String> ILP;
	ArrayList<String> variables;
	ArrayList<Integer> variableTypes;
	String obj;
	 

	
	public Graph(){
		
		hashVariables = new HashMap<Edge,String>() ;
		hashBinaries = new HashMap<Edge, String>();	
		hashEdges = new HashMap<Integer,Edge>();
		hashNodes  = new HashMap<Integer,Node>();
		hashTarEdges = new HashMap<Integer,Edge>();
		randomInit();
		
		pathsNode = new ArrayList<ArrayList<Node>>();
		pathsNode.add(detourWalk(Dir.East,hashTarEdges));
		pathsNode.add(detourWalk(Dir.North,hashTarEdges));
		
		//Set<Integer> keySet;
		//Integer[] keySetInt;
		ArrayList<Node> path = new ArrayList<Node>();
		Entry<Integer, Edge> entry;
 		while(hashTarEdges.size()>0){ 			
 			entry = hashTarEdges.entrySet().iterator().next();
 			path.clear();			
			Edge tarEdge = hashTarEdges.remove(entry.getKey());
			Node a = getNode(tarEdge.coord.x,tarEdge.coord.y);
			Node b = getNode(tarEdge.coord.s,tarEdge.coord.t);
			path.add(b);path.add(a);
			DFS(a,entrance,path,b);
			reverseList(path);
			DFS(b,exit,path,a);
			pathsNode.add(path);
			 
		}
		
			
	}
	
	public <T> void reverseList(ArrayList<T> list){
		ArrayList<T> stack = (ArrayList<T>) list.clone();
		list.clear();
		
		for(int i = stack.size()-1; i>=0; i--){
			list.add(stack.get(i));
		}
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
		
		width = 4;
		height = 3;
		
		nodes = new Node[width * height];
		hashEdges = new HashMap<Integer, Edge>(); 
		
		for(int i = 0; i< nodes.length;i++){
			nodes[i] = new Node();
		}
		
		//give number to each Node; Hash nodes	
		
		int id = 0;
		hashNodes = new HashMap<Integer, Node>();
		for(int i = 0; i< height; i++){
			for(int j =0; j < width ; j ++){
				nodes[id].setNumber(id);
				nodes[id].setCoordinate(i, j);
				hashNodes.put(i*100+j, nodes[id]);
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
		
		Wall aWall = new Wall();
		aWall.setCoordinate(1, 0, 1, 1);
		aWall.isHorizontal = true;
		hashEdges.replace(aWall.hashValue(),aWall);
		hashTarEdges.remove(aWall.hashValue());
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
	
	public<K,T> void replaceHashMap(HashMap<K,T> hashMap, T o, K key){
		hashMap.replace(key, o);
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

					
					hashTarEdges.put(edge.hashValue(), edge);
				}
			}
		}
	}
	
	public int hash2Nodes(Node a, Node b){
		if(a.number < b.number){
			return a.coord.x * 1000000 + a.coord.y * 10000 + b.coord.x * 100 + b.coord.y;
		}
		else{
			return b.coord.x * 1000000 + b.coord.y * 10000 + a.coord.x * 100 + a.coord.y;
		}
	}
	
	public Node getNode(int a, int b){
		return hashNodes.get(a*100+b);
	}
	
	public Edge getEdge(int a, int b){
		return getEdge(nodes[a], nodes[b]);
	}
	
	public Edge getEdge(Node a, Node b){		
		
		return hashEdges.get(hash2Nodes(a,b));
	}
	
	public ArrayList<Node> getCnctNodes(Node node){
		
		ArrayList<Node> cnctNodes = new ArrayList<Node>();
		Node[] adjNodes = node.getAdjNodes();
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
		Node a;
		Node b;
		for(int i = 0; i< pathNode.size() -1; i ++){
			int j = i+1;
			a = pathNode.get(i);
			b = pathNode.get(j);
			Edge e = getEdge(a,b);
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
		for(Node adjNode:node.getAdjNodes()){
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
			path = greedyWalk(targetEdgesVert,Dir.North);
			paths.add(path);
		}
		
		while(!targetEdgesHori.isEmpty()){
			path = greedyWalk(targetEdgesHori, Dir.East);
			paths.add(path);
		}
		
		return paths;
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
	
	
	
	
//	if(node.coordinate.x == targetEdge.coordinate.x){
//		nextNode = move1Step(node,Direction.East);
//		while(nextNode != null){
//			path.add(nextNode);
//			
//			for(Direction d:Direction.values()){
//				if(d == Direction.South || d != Direction.North){
//					Node n = move1Step(node,dir);
//					if( n != null)
//						checkPoints.add(node);
//				}
//			}
//			
//			if(nodeIsOnEdge(nextNode,targetEdge)){
//				nextStart = move1Step(nextNode,Direction.East);
//				return nextStart;
//			}
//		}
//	}
	public  boolean nodeIsOnEdge(Node node, Edge edge){
		if ((node.coord.x == edge.coord.x && node.coord.y == edge.coord.y) || 
				(node.coord.x == edge.coord.s && node.coord.y == edge.coord.t) )
			return true;
		else
			return false;
	}
	

	
	public void move(ArrayList<Node> path, ArrayList<Node> checkPoints, Dir dir){
		Node node = path.get(path.size()-1);
		Node nextNode = move1Step(node, dir);
		while(nextNode != null){
			path.add(nextNode);
			for(Dir d:Dir.values()){
				if(d != dir){
					Node n = move1Step(node,dir);
					if( n != null)
						checkPoints.add(node);
				}
			}
			
		}
	}
	
	public Edge findNextTargetEdge(HashMap<Integer,Edge> targetEdges, Node start, Dir dir){
		Edge targetEdge = null;
		switch(dir){
		case East:
			for(int j = start.coord.y; j < width; j ++){
				Edge e = targetEdges.get(start.coord.x*100 + j);
				if( e != null){
					return e;
				}
			}
			Node node = hashNodes.get((start.coord.x + 1)*100 + width -1);
			if(node == exit)
				return null;
			else{
				return findNextTargetEdge(targetEdges, node, Dir.West);
			}
			
		case West:
			for(int j = start.coord.y; j >= 0 ; j --){
				Edge e = targetEdges.get(start.coord.x*100 + j);
				if( e != null){
					return e;
				}
			}
			node = hashNodes.get((start.coord.x + 1)*100 + 0 -1);
			if(node == exit)
				return null;
			else{
				return findNextTargetEdge(targetEdges, node, Dir.East);
			}
			
		case North:
			for(int i = start.coord.x; i < height ; i++){
				Edge e = targetEdges.get(i * 100 + start.coord.y);
				if( e != null){
					return e;
				}
			}
			node = hashNodes.get(0*100 + start.coord.y + 1);
			if(node == exit)
				return null;
			else{
				return findNextTargetEdge(targetEdges, node, Dir.South);
			}
			
		case South:
			for(int i = start.coord.x; i >=0 ; i--){
				Edge e = targetEdges.get(i * 100 + start.coord.y);
				if( e != null){
					return e;
				}
			}
			node = hashNodes.get((height -1 )*100 + start.coord.y + 1);
			if(node == exit)
				return null;
			else{
				return findNextTargetEdge(targetEdges, node, Dir.North);
			}
			
		}
		
		
		return targetEdge;
	}
	
	
	public ArrayList<Edge> greedyWalk(HashMap<Integer,Edge> targetEdges,Dir dir){
		ArrayList<Edge> path = new ArrayList<Edge>();
		Node nextNode;
		switch(dir){
		case East:
			Edge nextEdge = findNextTargetEdge(targetEdges, entrance, Dir.East);
			if(nextEdge == null)
				return null;
			//nextNode = greedyNodeToEdge(path, targetEdges,nextEdge,entrance,Direction.East);
			break;
		case North:			
			break;
		}
			
		
		
		return path;
	}
	

	
	
	
	public boolean dirContainEdge(Node node, Dir searchDir, HashMap<Integer,Edge> targetEdges){
		Dir d = reverseDir(searchDir);
		return reverseDirContainEdge(node,d,targetEdges);
		
	}
	
	public int hash4Int(int a,int b, int c, int d){
		assert(a == c || b ==d);
		return a * 1000000 + b*10000+c*100+d;
	}
	
	public boolean reverseDirContainEdge(Node node, Dir searchDir,HashMap<Integer,Edge> targetEdges){
		
		switch(searchDir){
		case North:
			for(int i = 0; i <= node.coord.x -1;i++){
				for (int j = 0; j<= width-2; j++){
					if(targetEdges.containsKey(hash4Int(i,j,i,j+1))){
						return true;
					}
				}
			}
			break;
		case East:
			for(int j = 0; j <= node.coord.y -1;j++){
				for (int i = 0; i <= height-2; i++){
					if(targetEdges.containsKey(hash4Int(i,j,i+1,j))){
						return true;
					}
				}
			}
			break;
		case South:		
			for(int i = node.coord.x+1; i <= height -1;i++){
				for (int j = 0; j<= width -2; j++){
					if(targetEdges.containsKey(hash4Int(i,j,i,j+1))){
						return true;
					}
				}
			}
			break;
		case West:
			for(int i = 0; i <= height -2;i++){
				for (int j = node.coord.y +1; j<= width -1; j++){
					if(targetEdges.containsKey(hash4Int(i,j,i+1,j))){
						return true;
					}
				}
			}
			break;
		default:
			break;
		
		}
		
			
	    return false;
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
	
	public Dir selectCheckPointDir(ArrayList<Node> path, ArrayList<Node> checkPoints, ArrayList<ArrayList<Dir>> checkPointDirList,
			 HashMap<Integer,Edge> targetEdges,Dir searchDir){
		Dir dir = null;
		Node checkPoint = checkPoints.get(checkPoints.size()-1);
		
		ArrayList<Dir> checkPointDirs = checkPointDirList.get(checkPointDirList.size()-1);
		
		Node nextNode;
		
		if(checkPointDirs.size() == 1){
			checkPointDirList.remove(checkPointDirList.size()-1);
			checkPoints.remove(checkPoints.size()-1);
			dir = checkPointDirs.get(0);
			return dir;
		}
		Dir dir1 = checkPointDirs.get(0);
		Dir dir2 = checkPointDirs.get(1);
		//with 2 options
		for(Dir d:checkPointDirs){
			if(d == searchDir || d == reverseDir(searchDir)){
				checkPointDirs.remove(d);
				return d;
			}				
		}
		
		for(Dir d:checkPointDirs){
			if(dirContainEdge(checkPoint,d,targetEdges)){				
					checkPointDirs.remove(d);
					return d;
			}
		}
			
		
		
		
		return dir;
	}
	
	public ArrayList<Dir> nodeDirOptions(Node node){
		ArrayList<Dir> dirOptions = new ArrayList<Dir>();
		for(Dir d:Dir.values()){
			if(thereIsWay(node,d)){
				dirOptions.add(d);
			}
		}
		return dirOptions; 
	}
	
	
	public ArrayList<Node>  detourWalk(Dir startDir,HashMap<Integer,Edge> targetEdges){
		ArrayList<Node> path = new ArrayList<Node>();
		ArrayList<Edge>pathEdges;
		ArrayList<Node> popPath = new ArrayList<Node>();
		ArrayList<Edge> popPathEdge = new ArrayList<Edge>();
		ArrayList<Node> checkPoints = new ArrayList<Node>();
		ArrayList<ArrayList<Dir>> checkPointDirs = new ArrayList<ArrayList<Dir>>();
		ArrayList<Dir> dirOptions;
		Node checkPoint;
		HashMap<Integer,Edge> tarEdgeClone = (HashMap<Integer, Edge>) targetEdges.clone();
		
		Node node = entrance;
		Node nextNode;
		Edge newEdge;
		Dir nextStepDir = startDir;
		Dir startDirTemp = startDir; 
		Dir pathDirReverse = reverseDir(startDir);
		path.add(entrance);
		
			while(move1Step(node,nextStepDir) != exit){				
				node = move1Step(node,nextStepDir);
				//go into path
				if(path.contains(node)){
					popPath.clear();
					popPathEdge.clear();
					checkPoint = checkPoints.get(checkPoints.size()-1);
					stackPopUtilCheckPoint(path,checkPoint,popPath);
					popPathEdge = pathNodeToEdge(popPath);
					for(Edge e:popPathEdge){
						tarEdgeClone.put(e.hashValue(), e);
					}
					
					nextStepDir = selectCheckPointDir(path,checkPoints,checkPointDirs,tarEdgeClone,startDir);
					node = checkPoint;
					pathDirReverse = reverseDir(nextStepDir);
					
//					==[=[=[[=[=[=[[
				}			
				//no edge in startDir
				else if(!thereIsWay(node,startDirTemp)){
					for(Dir d:nodeDirOptions(node)){
						if(d != pathDirReverse && d != startDirTemp){
							if(d == startDir || d == reverseDir(startDir)){
								startDirTemp =d;
								nextStepDir = d;
								break;
							}													
						}
					}
					
					if(!thereIsWay(node,nextStepDir)){
						for(Dir d:nodeDirOptions(node)){
							if(d != pathDirReverse && d != startDirTemp){								
								if(dirContainEdge(node,d,tarEdgeClone)){
									nextStepDir = d;
									break;
								}						
							}
						}
					}
					
					//didn't find a dir contains targetEdge, choose a random direction
					if(!thereIsWay(node,nextStepDir)){
						for(Dir d:nodeDirOptions(node)){
							if(d != pathDirReverse && d != nextStepDir){
								
								nextStepDir = d;
								break;
							}
						}
					}
					
					//add node to path, add check point and check point directions
					
					path.add(node);
					newEdge = getEdge(path.get(path.size()-2), path.get(path.size()-1));
					tarEdgeClone.remove(newEdge.coord.x*100 + newEdge.coord.y);
					dirOptions = nodeDirOptions(node);
					dirOptions.remove(nextStepDir);
					dirOptions.remove(pathDirReverse);
					
					pathDirReverse = getDir2Nodes(move1Step(node,nextStepDir),node);
					if(dirOptions.size()>0){
						checkPoints.add(node);
						checkPointDirs.add(dirOptions);
					}
					
				}
				//there is edge in dir
				else if(thereIsWay(node,startDirTemp)){
					path.add(node);
					nextStepDir = startDirTemp;
					newEdge = getEdge(path.get(path.size()-2), path.get(path.size()-1));
					tarEdgeClone.remove(newEdge.coord.x*100 + newEdge.coord.y);
					dirOptions = nodeDirOptions(node);
					dirOptions.remove(startDirTemp);
					dirOptions.remove(pathDirReverse);
					
					pathDirReverse = getDir2Nodes(move1Step(node,startDirTemp),node);
					if(dirOptions.size()>0){
						checkPoints.add(node);
						checkPointDirs.add(dirOptions);
					}
				}
//				else if(thereIsWay(node,nextStepDir)){
//					path.add(node);
//					newEdge = getEdge(path.get(path.size()-2), path.get(path.size()-1));
//					tarEdgeClone.remove(newEdge.coordinate.x*100 + newEdge.coordinate.y);
//					dirOptions = nodeDirOptions(node);
//					dirOptions.remove(nextStepDir);
//					dirOptions.remove(pathDirReverse);
//					
//					pathDirReverse = getDir2Nodes(move1Step(node,nextStepDir),node);
//					if(dirOptions.size()>0){
//						checkPoints.add(node);
//						checkPointDirs.add(dirOptions);
//					}
//				}
			}
			
			path.add(exit);
			pathEdges = pathNodeToEdge(path);
			for(Edge e:pathEdges){
				targetEdges.remove(e.hashValue());
			
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
	public Node move1Step(Node start,Dir dir){
		Node nextNode;
		switch(dir){
		case East:
			nextNode = hashNodes.get(start.coord.x*100 + start.coord.y +1);
			if(nextNode != null)
				return nextNode;
			break;
		case West:
			nextNode = hashNodes.get(start.coord.x*100 + start.coord.y -1);
			if(nextNode != null)
				return nextNode;
			break;
		case North:
			nextNode = hashNodes.get((start.coord.x+1)*100 + start.coord.y);
			if(nextNode != null)
				return nextNode;
			break;
		case South:
			nextNode = hashNodes.get((start.coord.x-1)*100 + start.coord.y);
			if(nextNode != null)
				return nextNode;
			break;
		
		}
		
		return null;
	}
	public boolean thereIsWay(Node a, Node b){
		Edge e = getEdge(a,b);
		if(e instanceof Wall)
			return false;
		else
			return true;
	}
	public boolean thereIsWay(Node a, Dir dir){
		Node b;
		Edge e = null;
		switch(dir){
		case East:
			b = getNode(a.coord.x, a.coord.y+1);
			if(b == null)
				return false;
			else{
				e = getEdge(a,b);
				if(e == null || e instanceof Wall)
					return false;
			}
			break;
		case North:
			b = getNode(a.coord.x+1, a.coord.y);
			if(b == null)
				return false;
			else{
				e = getEdge(a,b);
				if(e == null || e instanceof Wall)
					return false;
			}
			break;
		case South:
			b = getNode(a.coord.x-1, a.coord.y);
			if(b == null)
				return false;
			else{
				e = getEdge(a,b);
				if(e == null || e instanceof Wall)
					return false;
			}
			break;
		case West:
			b = getNode(a.coord.x, a.coord.y-1);
			if(b == null)
				return false;
			else{
				e = getEdge(a,b);
				if(e == null || e instanceof Wall)
					return false;
			}
			break;
		default:
			break;
		
		}
		return true;
	}
	
	
	
	public boolean go(Node start, ArrayList<Node> stack, ArrayList<Node> checkPoints, Dir dir){
		
		boolean isCheckPoint = false;
		
		if(!thereIsWay(move1Step(start,dir),start))
			return false;
		// there is a way
		else{
			stack.add(start);
			for(Dir d : Dir.values()){
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
	
	public <T> void stackPopUtilCheckPoint(ArrayList<T> stack, T checkPoint,ArrayList<T> popStack){
		T o = stack.remove(stack.size()-1);
		popStack.add(o);
		if(o == checkPoint){
			stack.add(o);
			//popStack.remove(o);
		}
		else
			stackPopUtilCheckPoint(stack,checkPoint,popStack);
	}

	
	public void pathThroughEdge(Edge edge){
		
	}
	
	
	
	public void DFS(Node start, Node end, ArrayList<Node> path,Node critNode){
		
		Dir[] dirEndToCrit = new Dir[2];
		
		
		Stack<Node> stack = new Stack<Node>();
		//ArrayList<Node> path = new ArrayList<Node>();
		ArrayList<Node> checkPoints = new ArrayList<Node>();
		ArrayList<ArrayList<Node>> checkPointsOption = new  ArrayList<ArrayList<Node>>();
		ArrayList<Node> discoveredNodes = new ArrayList<Node>();
		ArrayList<Node> cnctNodes;
		ArrayList<Node> popStack = new ArrayList<Node>();
		stack.add(start);
		Node node = start;
		Node checkPoint;
		ArrayList<Node> options  = new ArrayList<Node>();
		
		boolean stepBack = false;
		if(end.coord.x < critNode.coord.x ){
			dirEndToCrit[0] = Dir.North;
		}
		else if (end.coord.x == critNode.coord.x ){
			dirEndToCrit[0] = Dir.Middle;
		}
		else
			dirEndToCrit[0] = Dir.South;
		
		
		if(end.coord.y <= critNode.coord.y ){
			dirEndToCrit[1] = Dir.West;
		}
		else if (end.coord.y == critNode.coord.y ){
			dirEndToCrit[1] = Dir.Middle;
		}
		else
			dirEndToCrit[1] = Dir.West;
		
		
		
		cnctNodes = getConnectedNodes(start);		
		Node[] nodes = cnctNodes.toArray(new Node[cnctNodes.size()]);		
		for(int i = 0; i < cnctNodes.size();i++){
			Node n = nodes[i];
			if(path.contains(n)){
				cnctNodes.remove(n);
			}
		}
		if(cnctNodes.size()>1){
			checkPoints.add(start);
			node = cnctNodes.remove(cnctNodes.size()-1);
			checkPointsOption.add(cnctNodes);
		}
		
		//path.add(start);
		
 		
		while(node != end){
			
			cnctNodes = getConnectedNodes(node);
			nodes = cnctNodes.toArray(new Node[cnctNodes.size()]);
			for(int i = 0; i < cnctNodes.size();i++){
				Node n = nodes[i];
				if(path.contains(n)){
					cnctNodes.remove(n);
				}
			}
			
			
			if(dirEndToCrit[0] == Dir.West || dirEndToCrit[1] == Dir.South){
				
				if(node.coord.x >= critNode.coord.x && node.coord.y>= node.coord.y)
					stepBack = true;
			}
			else if (dirEndToCrit[0] == Dir.East || dirEndToCrit[1] == Dir.South){
				if(node.coord.x <= critNode.coord.x && node.coord.y <= node.coord.y)
					stepBack = true;
			}
			
			
			if(path.contains(node))
				stepBack = true;
			
			if(stepBack){
				checkPoint = checkPoints.get(checkPoints.size()-1);
				stackPopUtilCheckPoint(path, checkPoint, popStack);
				options = checkPointsOption.get(checkPointsOption.size()-1);
				node = options.remove(options.size()-1);
				if(options.size() == 0){
					checkPointsOption.remove(options);
					checkPoints.remove(checkPoint);
				}
				stepBack = false;
				continue;
			}
			
			if(cnctNodes.size() == 0){
				checkPoint = checkPoints.get(checkPoints.size()-1);
				stackPopUtilCheckPoint(path, checkPoint, popStack);
				options = checkPointsOption.get(checkPointsOption.size()-1);
				node = options.remove(options.size()-1);
				if(options.size() == 0){
					checkPointsOption.remove(options);
					checkPoints.remove(checkPoint);
				}
			}
			else if (cnctNodes.size() == 1){
				path.add(node);
				node = cnctNodes.get(0);
			}
			else if(cnctNodes.size() >1){
				path.add(node);
				checkPoints.add(node);
				//cnctNodes.remove(node);
				node = cnctNodes.remove(cnctNodes.size()-1);
				checkPointsOption.add(cnctNodes);
			}
				
		}
		path.add(end);
		//return false;
	}
}
	
	

