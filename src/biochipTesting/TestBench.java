package biochipTesting;
//import gurobiILP.*;

import java.util.ArrayList;

import java.util.Stack;

//import gurobi.GRBException;

public class TestBench {
	private static final int WIDTH = 4;
	private static final int HEIGHT = 3;
	
	
	public static void main(String arg[]) {
		

		Graph graph;
 		graph = new Graph(WIDTH,HEIGHT);
 		graph.setEdgeHole(1, 1, 2, 1);
 		graph.findCuts();
 		graph.findPaths();
 		
		
		if(!graph.pathTest())
			System.out.println("there is at least one SA0 fault in this chip");
		else if(!graph.cutTest())
			System.out.println("there is at least one SA1 falut in this chip");
		
		else 
			System.out.println("this chip is perfect!");
	
		
		
//		
		
	}
	
}
