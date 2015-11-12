package gurobiILP;

import java.util.ArrayList;

import gurobi.*; 
public class Constrain {
	public ArrayList<Integer> cofficient;
	public ArrayList<String> varibleNames;
	//public ArrayList<GRBVar> grbVars;	
	public String operators;
	public Integer bound;
	
	public  Constrain(){
		cofficient = new ArrayList<Integer>();
		varibleNames = new ArrayList<String>();
		//grbVars = new ArrayList<GRBVar>(); ;
		
	}
}
