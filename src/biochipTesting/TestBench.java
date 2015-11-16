package biochipTesting;
//import gurobiILP.*;

import java.util.ArrayList;

import java.util.Stack;

//import gurobi.GRBException;

public class TestBench {
	
	
	
	public static void main(String arg[]) {
		
//		Enviroment env = new Enviroment();
//		
//		

//		ArrayList<String> varNames = graph.variables;
//		ArrayList<Integer> varTypes = graph.variableTypes;
//		
//		env.setVars(varNames,varTypes);
//		env.setContrains(graph.ILP);
//		env.setObjective(graph.obj);
//		env.run();
		Graph graph;
 		graph = new Graph();
		//graph.getILPContrains();
		//graph.findPathsTest();
		//graph.findCutsTest();
		
		if(!graph.pathTest())
			System.out.println("there is at least one SA0 fault in this chip");
		else if(!graph.cutTest())
			System.out.println("there is at least one SA1 falut in this chip");
		
		else 
			System.out.println("this chip is perfect!");
	
		
		
//		 try {    	
//
//		      // Dispose of model and environment
//		    	
//		    	ArrayList<String> varNames =new ArrayList<String>();
//		    	varNames.add("x1");
//		    	varNames.add("x2");
//		    	varNames.add("y");
//		    	ArrayList<Integer> varTypes = new ArrayList<Integer>();
//		    	varTypes.add(1);
//		    	varTypes.add(1);
//		    	varTypes.add(0);    	
//		    	env.setVars(varNames,varTypes);
//		    
//		    	
//		    	ArrayList<String> instructions =new ArrayList<String>();
//		    	instructions.add("x1 + x2 = 0");
//		    	instructions.add("x1 + 100y >= 1");
//		    	env.setContrains(instructions);
//		    	
//		    	env.setObjective("x1+y");
//		    	
//		    	env.run();
//		    	
//
//		    } catch (GRBException e) {
//		      System.out.println("Error code: " + e.getErrorCode() + ". " +
//		                         e.getMessage());
//		    }
		
	}
	
}
