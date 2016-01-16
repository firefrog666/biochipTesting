package biochipTesting;
import java.util.Random;

import java.util.Stack;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class Graph {
	
	private static final int upBound = 10; 
	private static final int lowBound = 3; 
	private static final int M = 10000;
	
	
	
	Node[] nodes;
	List<Edge> edges;
	Node entrance;
	Node exit;
	int width;
	int height;
	Map<Integer,Edge> hashEdges;  
	HashMap<Integer,Node> hashNodes; //Hash Node by its coordinates i*100 + j; 
	
	HashMap<Integer,Edge> hashTarEdges;
 	
	HashMap<Integer,HashMap<Edge,String>> hashWVariables;
	HashMap<Edge,String> hashXVariables;
	HashMap<Edge, String> hashBinaries;
	ArrayList<ArrayList<Edge>> paths;
	ArrayList<ArrayList<Node>> pathsNode;
	ArrayList<ArrayList<Edge>> cuts;
	ArrayList<String> ILP;
	ArrayList<String> variables;
	ArrayList<Integer> variableTypes;
	String obj;
	 

	
	public Graph(int w, int h){
		
		hashWVariables = new HashMap<Integer,HashMap<Edge,String>>() ;
		ILP = new ArrayList<String>();
		hashXVariables = new HashMap<Edge,String>() ;
		hashBinaries = new HashMap<Edge, String>();	
		hashEdges = new HashMap<Integer,Edge>();
		hashNodes  = new HashMap<Integer,Node>();
		hashTarEdges = new HashMap<Integer,Edge>();
		cuts = new ArrayList<ArrayList<Edge>>();
		init(w,h);	
	}
	
	public void findPaths(){
		pathsNode = new ArrayList<ArrayList<Node>>();
		pathsNode.add(detourWalk(Dir.East,hashTarEdges));
		pathsNode.add(detourWalk(Dir.North,hashTarEdges));
		ArrayList<Node> path = new ArrayList<Node>();
		Entry<Integer, Edge> entry;
 		while(hashTarEdges.size()>0){ 			
 			entry = hashTarEdges.entrySet().iterator().next();
 			path.clear();			
			Edge tarEdge = hashTarEdges.remove(entry.getKey());
			Node a = getNode(tarEdge.coord.x,tarEdge.coord.y);
			Node b = getNode(tarEdge.coord.s,tarEdge.coord.t);
			path.add(b);path.add(a);
			DFS(a,entrance,path,b,hashTarEdges);
			reverseList(path);
			DFS(b,exit,path,a,hashTarEdges);
			pathsNode.add((ArrayList<Node>) path.clone());
			for(Node n:path){
				hashTarEdges.remove(n);
			}
			
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
		//hashWVariables = new HashMap<Edge,String>() ;
		hashBinaries = new HashMap<Edge, String>();
		cuts = new ArrayList<ArrayList<Edge>>();
		paths =new ArrayList<ArrayList<Edge>>();
		
		
		init3_3();
		//findCuts();
	}	
	
	
	
	public void init(int w, int h){
		//Random rnd = new Random();
		//width = rnd.nextInt(upBound)+lowBound;
		//height = rnd.nextInt(upBound)+lowBound;
		
		width = w;
		height = h;
		
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
		hashMap.put(key, o);
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
	
	private boolean aLeftOrUnderb(Node a, Node b){
		if(a.coord.x < b.coord.x || a.coord.y < b.coord.y )
			return true;
		else
			return false;
	}
	public Edge getEdge(int a, int b){
		return getEdge(nodes[a], nodes[b]);
	}
	
	public Edge getEdge(Node a, Node b){		
		
		return hashEdges.get(hash2Nodes(a,b));
	}
	//return nodes not blocked by wall nor closed valve 
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
	
	//connect means edge between two node is not wall
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
	
	public ArrayList<Edge> findCriticalPath(){
		ArrayList<Edge> critPath = new ArrayList<Edge>();
		return critPath;
	}
	
	public void findCuts(ArrayList<Edge> criticalPath){
		
		ArrayList<Node> S = new ArrayList<Node>();
		ArrayList<Node> Sbrink = new ArrayList<Node>();
		ArrayList<Node> SbrnkCache = new ArrayList<Node>();		
		ArrayList<Edge> cut = new ArrayList<Edge>();
		int nextEdgeP;
		
		Node start;
		
 		start = entrance;
		S.add(start);
		Sbrink.add(start);
		for(int i = 0; i <= criticalPath.size()-1; i ++){
			nextEdgeP = i;
			cut = new ArrayList<Edge>();
			// what will happen if I change S during the loop
			for(Node node:Sbrink){				
				for(Edge e:findCutOfNode(node, S,SbrnkCache,criticalPath,nextEdgeP))
					cut.add(e);		
				
			}
			S.addAll(SbrnkCache);
			Sbrink.clear();
			Sbrink.addAll(SbrnkCache);
			SbrnkCache.clear();
			cuts.add(cut);
			
		}		
	}
	
	public void setEdgeWall(int x, int y, int s, int t){
		Edge wall = new Wall();
		wall.setCoordinate(x,y,s,t);
		assert(hashEdges.size()>0);
		hashEdges.put(hash4Int(x,y,s,t),wall);	
		hashTarEdges.remove(wall.hashValue());
		
	}
	
 	public void setHoles(ArrayList<Hole>holes){
		for(Hole hole:holes){
			//setEdgeHole(hole.getCoordinate().x,hole.getCoordinate().y,hole.getCoordinate().s,hole.getCoordinate().t);
			hashEdges.put(hole.hashValue(), hole);
		}
	}
 	
 	public void setWalls(ArrayList<Wall> walls){
 		for(Wall wall:walls){
 			hashEdges.put(wall.hashValue(), wall);
 			hashTarEdges.remove(wall.hashValue());
 		}
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
	
	
	public ArrayList<Edge> findCutOfNode(Node node,ArrayList<Node> S, ArrayList<Node> sBrinkCache, 
											ArrayList<Edge> critPath,int nextEdgeP){
		ArrayList<Edge> cutEdges = new ArrayList<Edge>();
		for(Node adjNode:node.getAdjNodes()){
			if(!S.contains(adjNode)){
				Edge e = getEdge(node,adjNode);
				
				if(adjNode == exit){
					if(!sBrinkCache.contains(node))
						sBrinkCache.add(node);
					cutEdges.add(e);
					
					continue;
				}
				
				if(critPath.contains(e)){
					if(e != critPath.get(nextEdgeP)){
						if(!sBrinkCache.contains(node))
							sBrinkCache.add(node);
						continue;
					}
					// e == nextEdge
					else{
						if(e instanceof Hole){
							S.add(adjNode);
							cutEdges.addAll(findCutOfNode(adjNode,S,sBrinkCache,critPath,nextEdgeP++));
							continue;
						 }
						else{
							if(!sBrinkCache.contains(adjNode))
								sBrinkCache.add(adjNode);
							cutEdges.add(e);
						}
					}
			
				}
				
				if(e instanceof Hole){
					S.add(adjNode);
					cutEdges.addAll( findCutOfNode(adjNode,S,sBrinkCache,critPath,nextEdgeP++));
					continue;
				}
				else{
					if(!sBrinkCache.contains(adjNode))
						sBrinkCache.add(adjNode);
					cutEdges.add(e);
					continue;
				}
				
			}
			
		}
		return cutEdges;
	}
	
	
	public String setILPObj(){
		String obj = "";
		for(Edge e:getJointEdges(entrance)){
			obj += "+" + hashWVariables.get(e);
		}
		return obj;
	}
	
//	public void getILPContrains(){
//		variables = new ArrayList<String>();
//		variableTypes = new ArrayList<Integer>();
//		String constrain = "";
//		for(Node node:nodes){
//			constrain = "";
//			if(node != entrance & node != exit){
//				for(Edge e:getJointEdges(node)){
//					String variable = "x" + e.number; 
//					String binary = "y" +e.number;
//					if(!hashWVariables.containsKey(e)){
//						hashWVariables.put(e, variable);
//						hashBinaries.put(e, binary);
//						variables.add(variable);
//						variableTypes.add(0);
//						variables.add(binary);
//						variableTypes.add(1);
//					}
//					if(e instanceof Wall)
//						continue;
//					
//					if(isInflow(e,node))
//						constrain += "+" + variable;
//					else
//						constrain += "-" + variable;
//				}
//				constrain += "= 0";
//				ILP.add(constrain);
//			}
//			
//		}
//		
//		//flow >= 1 for every edge connect to entrance and exit
//		
//		for(Edge e: getJointEdges(entrance) ){
//			constrain = "";
//			String varialbe = hashWVariables.get(e);
//			
//			constrain = varialbe + ">=1";	
//			ILP.add(constrain);
//		}
//		
//		for(Edge e:getJointEdges(exit)){
//			constrain = "";
//			String variable = hashWVariables.get(e);
//			constrain = variable + ">=1";
//			ILP.add(constrain);
//			
//		}
//		//for every edge except extrance and exit 
//		ArrayList<Edge> internalEdges =  new ArrayList<Edge>();
//		
//		for(Edge e:edges){
//			internalEdges.add(e);		
//		}
//		
//		for(Edge e:getJointEdges(entrance)){
//			internalEdges.remove(e);
//		}
//		
//		for(Edge e:getJointEdges(exit)){
//			internalEdges.remove(e);
//		}
//		
//		for(Edge e:internalEdges){
//			constrain = "";
//			String variable = hashWVariables.get(e);
//			String binary = hashBinaries.get(e);
//			constrain = variable+"+ 1000"+binary+" >=1";
//			ILP.add(constrain);
//			constrain = variable + "+1000"+ binary +"<= 999";
//			ILP.add(constrain);		
//		}
//		obj = setILPObj();
//		
//	}
	
	public void getAcyclicILP(){
		variables = new ArrayList<String>();
		variableTypes = new ArrayList<Integer>();
		String xVariable;
		String wVariable;
		String yVariable; 
		String iVariable; // current on each edge
		String vVariable; // voltage on each edge 
		String constraint = "";
		HashMap<Edge, String> hashWVariables1dim;
		HashMap<Edge, String> hashIVariables1dim;
		HashMap<Edge, String> hashVVariables1dim;	
		HashMap<Node,String> hashYVariables1dim;
		ArrayList<String> iConstraint1dim;
		
		for(int i = 0; i <= height -1 + width-1 - 1; i++){
			hashWVariables1dim = new HashMap<Edge,String>(); 
			for(Edge edge:edges){
				if(!(edge instanceof Wall)){						
					
					wVariable = "w" + i +  edge.coord.x + edge.coord.y + edge.coord.s+edge.coord.t;	
					
					if(!hashWVariables1dim.containsKey(edge)){
						hashWVariables1dim.put(edge, wVariable);
						variables.add(wVariable);
						variableTypes.add(1);
					}
					
				}
			}
			hashWVariables.put(i, hashWVariables1dim);
			hashYVariables1dim = new HashMap<Node,String>();
			for(Node node:nodes){
				yVariable = "y" + i + node.coord.x + node.coord.y;
				if(!hashYVariables1dim.containsKey(node)){
					variables.add(yVariable);
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
			
			
			hashIVariables1dim = new HashMap<Edge,String>();
			hashVVariables1dim = new HashMap<Edge,String>();
			for(Edge edge:edges){
				if(!(edge instanceof Wall)){					
					iVariable = "i" + i + edge.coord.x + edge.coord.y + edge.coord.s + edge.coord.t;	
					vVariable = "v" + i + edge.coord.x + edge.coord.y + edge.coord.s + edge.coord.t;
					if(!hashIVariables1dim.containsKey(edge)){
						hashIVariables1dim.put(edge, iVariable);
						hashVVariables1dim.put(edge,vVariable);
						variables.add(iVariable);
						
						variableTypes.add(0);
						variables.add(vVariable);
						variableTypes.add(0);
					}
					
				}
			}
			
			//set Kirchhoff Circuit Laws to calculate i on each edge
			//each used edge (w(edge) = 1), i(edge) <> 0
			
			//create iVariable vVariable for every edge  
			
			// current conservation
			// i01 + i02 + i03 + i04 = 0
			String iConstraint = "";
			
			iConstraint1dim = new ArrayList<String>();
			
			for(Node node:nodes){
				if(node != entrance && node != exit){					
					for(Node cnctNode:getConnectedNodes(node)){
						Edge edge = getEdge(node,cnctNode);
						iVariable = hashIVariables1dim.get(edge);
						wVariable = hashWVariables1dim.get(edge);
						if(aLeftOrUnderb(node,cnctNode)){
							iVariable = "+" + iVariable;
						}
						else
						{
							iVariable = "-" + iVariable;
						}
						String key = "i" + edge.coord.x + edge.coord.y + edge.coord.s + edge.coord.t; 
						constraint = iVariable + "-" + M + wVariable + "<= 0";
						if(!iConstraint1dim.contains(key)){							
							ILP.add(constraint);
							constraint = "";
						}
						
						constraint = iVariable + "+" + M + wVariable + ">= 0";
						if(!iConstraint1dim.contains(key)){						
							ILP.add(constraint);
							constraint = "";
						}
						
						iConstraint +=  iVariable;
					}
					
					iConstraint += "=0";
					ILP.add(iConstraint);
					iConstraint = "";
				}
		}
			
		// voltage conservation	
		// v01 + v12 + v23 + v34 = 0;
		
		for(int j = 0; j <= width - 1 -1; j++){
			for(int k = 0; k <= height -1 -1; k++){
				Node node0,node1,node2,node3;
				Edge edge0,edge1,edge2,edge3;
				node0 = getNode(j,k);
				node1 = getNode(j,k+1);
				node2 = getNode(j +1,k+1);
				node3 = getNode(j+1,k);
				
				edge0 = getEdge(node0,node1);
				edge1 = getEdge(node1,node2);
				edge2 = getEdge(node2,node3);
				edge3 = getEdge(node3,node0);
				
				
				String iVariable0,vVariable0,wVariable0;
				String iVariable1,vVariable1,wVariable1;
				String iVariable2,vVariable2,wVariable2;
				String iVariable3,vVariable3,wVariable3;
				
				//edge0
				if(!(edge0 instanceof Wall)){
					iVariable0 = hashIVariables1dim.get(edge0);
					vVariable0 = hashVVariables1dim.get(edge0);
					wVariable0 = hashWVariables1dim.get(edge0);
				}
				else
				{
					iVariable0 = "i" + i +  edge0.coord.x + edge0.coord.y + edge0.coord.s+edge0.coord.t;
					vVariable0 = "i" + i +  edge0.coord.x + edge0.coord.y + edge0.coord.s+edge0.coord.t;
					wVariable0 = "i" + i +  edge0.coord.x + edge0.coord.y + edge0.coord.s+edge0.coord.t;
					hashWVariables1dim.put(edge0,wVariable0); variables.add(wVariable0);variableTypes.add(1);
					hashIVariables1dim.put(edge0,iVariable0); variables.add(iVariable0);variableTypes.add(0);
					hashVVariables1dim.put(edge0,vVariable0); variables.add(vVariable0); variableTypes.add(0);
					constraint = wVariable0 + "=0";
				}
				
				//edge1
				if(!(edge1 instanceof Wall)){
					iVariable1= hashIVariables1dim.get(edge1);
					vVariable1 = hashVVariables1dim.get(edge1);
					wVariable1 = hashWVariables1dim.get(edge1);
				}
				else
				{
					iVariable1 = "i" + i +  edge1.coord.x + edge1.coord.y + edge1.coord.s+edge1.coord.t;
					vVariable1 = "i" + i +  edge1.coord.x + edge1.coord.y + edge1.coord.s+edge1.coord.t;
					wVariable1 = "i" + i +  edge1.coord.x + edge1.coord.y + edge1.coord.s+edge1.coord.t;
					hashWVariables1dim.put(edge1,wVariable1); variables.add(wVariable1);variableTypes.add(1);
					hashIVariables1dim.put(edge1,iVariable1); variables.add(iVariable1);variableTypes.add(0);
					hashVVariables1dim.put(edge1,vVariable1); variables.add(vVariable1); variableTypes.add(0);
					constraint = wVariable1 + "=0";
				}
				
				//edge2
				if(!(edge2 instanceof Wall)){
					iVariable2= hashIVariables1dim.get(edge2);
					vVariable2 = hashVVariables1dim.get(edge2);
					wVariable2 = hashWVariables1dim.get(edge2);
				}
				else
				{
					iVariable2 = "i" + i +  edge2.coord.x + edge2.coord.y + edge2.coord.s+edge2.coord.t;
					vVariable2 = "i" + i +  edge2.coord.x + edge2.coord.y + edge2.coord.s+edge2.coord.t;
					wVariable2 = "i" + i +  edge2.coord.x + edge2.coord.y + edge2.coord.s+edge2.coord.t;
					hashWVariables1dim.put(edge2,wVariable2); variables.add(wVariable2);variableTypes.add(1);
					hashIVariables1dim.put(edge2,iVariable2); variables.add(iVariable2);variableTypes.add(0);
					hashVVariables1dim.put(edge2,vVariable2); variables.add(vVariable2); variableTypes.add(0);
					constraint = wVariable2 + "=0";
				}
				
				//edge3
				if(!(edge3 instanceof Wall)){
					iVariable3= hashIVariables1dim.get(edge3);
					vVariable3 = hashVVariables1dim.get(edge3);
					wVariable3 = hashWVariables1dim.get(edge3);
				}
				else
				{
					iVariable3 = "i" + i +  edge3.coord.x + edge3.coord.y + edge3.coord.s+edge3.coord.t;
					vVariable3 = "i" + i +  edge3.coord.x + edge3.coord.y + edge3.coord.s+edge3.coord.t;
					wVariable3 = "i" + i +  edge3.coord.x + edge3.coord.y + edge3.coord.s+edge3.coord.t;
					hashWVariables1dim.put(edge3,wVariable3); variables.add(wVariable3);variableTypes.add(1);
					hashIVariables1dim.put(edge3,iVariable3); variables.add(iVariable3);variableTypes.add(0);
					hashVVariables1dim.put(edge3,vVariable3); variables.add(vVariable3); variableTypes.add(0);
					constraint = wVariable3 + "=0";
				}
				
				// -M(1-w) <= v <= M(1-w), -M*W <= i <= M*w
				//v+M*W <= M			
				//v - M*w >= -M
				// i+ m*w >=0
				// i - M*W <= 0
				
				String c00 = vVariable0 +  "+" + M + wVariable0 + "<=" + M;
				String c01 = vVariable0 + "-" + M + wVariable0 + ">=" + "-" + M;
				String c02 = iVariable0 + "-" + M + wVariable0 + "<=0";
				String c03 = iVariable0 + "+" + M +  wVariable0 + ">=0";
				ILP.add(c00);ILP.add(c01);
				//ILP.add(c02);ILP.add(c03);
				
				String c10 = vVariable1 + "+" + M + wVariable1 + "<=" + M;
				String c11 = vVariable1 + "-" + M + wVariable1 + ">=" + "-" + M;
				String c12 = iVariable1 + "-" + M + wVariable1 + "<=0";
				String c13 = iVariable1 + "+" + M +  wVariable1 + ">=0";
				ILP.add(c10);ILP.add(c11);
				//ILP.add(c12);ILP.add(c13);
				
				String c20 = vVariable2 + "+" + M + wVariable2 + "<=" + M;
				String c21 = vVariable2 + "-" + M + wVariable2 + ">=" + "-" + M;
				String c22 = iVariable2 + "-" + M + wVariable2 + "<=0";
				String c23 = iVariable2 + "+" + M +  wVariable2 + ">=0";
				ILP.add(c20);ILP.add(c21);
				//ILP.add(c22);ILP.add(c23);
				
				String c30 = vVariable3 + "+" + M + wVariable3 + "<=" + M;
				String c31 = vVariable3 + "-" + M + wVariable3 + ">=" + "-" + M;
				String c32 = iVariable3 + "-" + M + wVariable3 + "<=0";
				String c33 = iVariable3 + "+" + M +  wVariable3 + ">=0";
				ILP.add(c30);ILP.add(c31);
				//ILP.add(c32);ILP.add(c33);
				
				//v1 + v2 + v3 + v4 = 0
				
				constraint = iVariable0 + "+" +  vVariable0 + 
						"+" + iVariable1 + "+" + vVariable1 +
						"+" + iVariable2 + "+"  + vVariable2 +
						"-" + iVariable3 + "-"  + vVariable3 + " = 0";
				ILP.add(constraint);
				constraint = "";
			}
		}
		
		
		// if w = 1, then i <> 0
		for(Edge edge:edges){
			String bVariable = "b" + i + edge.coord.x + edge.coord.y + edge.coord.s+edge.coord.t; // a temp binary variable
			variables.add(bVariable);
			variableTypes.add(1);
			
			wVariable = hashWVariables1dim.get(edge);
			iVariable = hashIVariables1dim.get(edge); 
			//constraint = iVariable + "+" +M + wVariable + ">=0"; ILP.add(constraint);
			//constraint = iVariable + "-" +M + wVariable + "<=0"; ILP.add(constraint);
			//i + w -1 >(b-1)M, i+w -1 <bM
			//i + w -M*b>1-M, i+w - M*b < 1
			//means i+w - M*b >= 1 - M + 1, i + w - M*b <= 0 
			constraint = iVariable +"+"+ wVariable + "-" + M + bVariable + ">=" + (1-M +1); ILP.add(constraint);
			constraint = "";
			constraint = iVariable + "+" + wVariable + "-" + M + bVariable + "<=0"; ILP.add(constraint);
			constraint = "";
		}
			
			
			
			
			
	}
		
		
		obj = "";
		for(Edge edge:edges){			
			for(int i = 0; i <= height-1 + width-1 -1; i++){
				if(!(edge instanceof Wall)){		
					//only init xVarialbe once
					HashMap<Edge,String> test = hashWVariables.get(i);
					wVariable = test.get(edge);
					//wVariable = hashWVariables.get(i).get(edge);
					constraint = constraint +"+" + wVariable;				
				}
			}
			constraint += ">= 1";
			ILP.add(constraint);
			constraint = "";
		}
		
		for(int i =0; i <= height-1 + width-1 -1; i++){
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
		
		
		
	
		
		
		
		//set obj
//		obj += "+" + xVariable;
//		//xVariable = "x" + edge.coord.x + edge.coord.y + edge.coord.s + edge.coord.t;
//		if(!hashXVariables.containsKey(edge)){
//			hashXVariables.put(edge, xVariable);
//			variables.add(xVariable);
//			variableTypes.add(1);
//		constraint += "-" + M + xVariable + "<=0";
//		ILP.add(constraint);
//		constraint = "";
////		}
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

			}
			
			path.add(exit);
			pathEdges = pathNodeToEdge(path);
			for(Edge e:pathEdges){
				targetEdges.remove(e.hashValue());
			
			}
		
		return path;
		
	}
	
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
			return false;
			
		
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
	
	public ArrayList<Node> dirOptions(Node node, ArrayList<Node> path){
		ArrayList <Node> dirOptions;
		ArrayList<Node> wrongDirs = new ArrayList<Node>();
		Node n;
		dirOptions = getConnectedNodes(node);		
		//Node[] nodes = dirOptions.toArray(new Node[dirOptions.size()]);		
		if(path.size()>1){
			dirOptions.remove(path.get(path.size()-2));
		}
		
		//dirOptions.removeAll(wrongDirs);
		
		return dirOptions;
	}
	
	public void DFS(Node start, Node end, ArrayList<Node> path,Node critNode, HashMap<Integer,Edge> tarEdges){
		
		Dir[] dirEndToCrit = new Dir[2];
		
		
		Stack<Node> stack = new Stack<Node>();
		
		ArrayList<Node> checkPoints = new ArrayList<Node>();
		ArrayList<ArrayList<Node>> checkPointsOption = new  ArrayList<ArrayList<Node>>();
		
		ArrayList<Node> cnctNodes;
		ArrayList<Node> popStack = new ArrayList<Node>();
		stack.add(start);
		Node node = start;
		Node checkPoint;
		ArrayList<Node> options  = new ArrayList<Node>();
		
		boolean stepBack = false;
		if(end.coord.x < critNode.coord.x ){
			dirEndToCrit[0] = Dir.South;
		}
		else if (end.coord.x == critNode.coord.x ){
			dirEndToCrit[0] = Dir.Middle;
		}
		else
			dirEndToCrit[0] = Dir.North;
		
		
		if(end.coord.y < critNode.coord.y ){
			dirEndToCrit[1] = Dir.West;
		}
		else if (end.coord.y == critNode.coord.y ){
			dirEndToCrit[1] = Dir.Middle;
		}
		else
			dirEndToCrit[1] = Dir.East;
		
		if(node == end){
			if(!path.contains(node))
				path.add(node);			
			return;
			
		}
			
		cnctNodes = dirOptions(start,path);		
	
		if(cnctNodes.size()>1){
			checkPoints.add(start);
			node = dfsNodeDirBestGuess(node, cnctNodes, tarEdges);
			cnctNodes.remove(node);
			checkPointsOption.add(cnctNodes);
		}
	
		
 		
		while(node != end){
			
			cnctNodes = dirOptions(node,path);	
			
			
			if(dirEndToCrit[0] == Dir.South || dirEndToCrit[1] == Dir.West){
				
				if(node.coord.x >= critNode.coord.x && node.coord.y>= critNode.coord.y)
					stepBack = true;
			}
			else if (dirEndToCrit[0] == Dir.North || dirEndToCrit[1] == Dir.East){
				if(node.coord.x <= critNode.coord.x && node.coord.y <= critNode.coord.y)
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
				
				node = dfsNodeDirBestGuess(node, cnctNodes, tarEdges);
				cnctNodes.remove(node);
				checkPointsOption.add(cnctNodes);
			}
				
		}
		path.add(end);
		
	}
	
	public Node dfsNodeDirBestGuess(Node node, ArrayList<Node> cnctNodes, HashMap<Integer,Edge> tarEdges){
		
		for(Node cnctNode: cnctNodes){
			if(cnctNode.coord.x < node.coord.x){
				for(int i = cnctNode.coord.x; i>=1; i--){
					if(tarEdges.containsKey(hash4Int(i-1,cnctNode.coord.y,i,cnctNode.coord.y))){
						return cnctNode;
					}
				}
			}
			else if(cnctNode.coord.x > node.coord.x){
				for(int i = cnctNode.coord.x; i<=height -1; i++){
					if(tarEdges.containsKey(hash4Int(i,cnctNode.coord.y,i+1,cnctNode.coord.y))){
						return cnctNode;
					}
				}
			}
			
			if(cnctNode.coord.y < node.coord.y){
				for(int j = cnctNode.coord.y; j>=1; j--){
					if(tarEdges.containsKey(hash4Int(cnctNode.coord.x,j-1,cnctNode.coord.x,j))){
						return cnctNode;
					}
				}
			}
			else if(cnctNode.coord.y > node.coord.y){
				for(int j = cnctNode.coord.y; j<=width -1; j++){
					if(tarEdges.containsKey(hash4Int(cnctNode.coord.x,j,cnctNode.coord.x,j+1))){
						return cnctNode;
					}
				}
			}			
			
		}
		
		return cnctNodes.get(cnctNodes.size()-1);
		
	}
	
}
	
	

