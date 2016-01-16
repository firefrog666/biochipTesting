package gurobiILP;

import java.util.ArrayList;
import java.util.HashMap;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import gurobiILP.*;
public class Enviroment {
	ArrayList<Constrain> constrains;
	ArrayList<Var> vars;
	HashMap<String,GRBVar> hashGrbVars;
	Objective obj;
	GRBEnv env;
	GRBModel model;
	ArrayList<GRBVar> grbVars;
	GRBLinExpr expr;
	
	
	public Enviroment() throws GRBException{
	    env   = new GRBEnv("Ilp.log");
	    model = new GRBModel(env);	
	    hashGrbVars = new HashMap<String, GRBVar>();
		vars = new ArrayList<Var>();
		constrains = new ArrayList<Constrain>();
		obj = new Objective();
		obj.maximize = false;
	}
	
	public void run() throws GRBException{
		
		model.update();
		
		// set objective
		expr = new GRBLinExpr();
		for(int i = 0; i < obj.varibleNames.size();i++){
			expr.addTerm((double)(obj.cofficient.get(i)),hashGrbVars.get(obj.varibleNames.get(i)) );	
		}
	
		if(obj.maximize)
			model.setObjective(expr,GRB.MAXIMIZE);
		else
			model.setObjective(expr,GRB.MINIMIZE);
		
		// add constrains
		int counter = 0;
		for(Constrain constrain:constrains){
			expr = new GRBLinExpr();
			for(int i =0; i<constrain.varibleNames.size();i++){
				expr.addTerm(constrain.cofficient.get(i), hashGrbVars.get(constrain.varibleNames.get(i)));
			}
			switch(constrain.operators){
			case "=":
				model.addConstr(expr, GRB.EQUAL, constrain.bound, "c" +counter );
				break;
			case "<=":
				model.addConstr(expr, GRB.LESS_EQUAL, constrain.bound, "c" +counter );
				break;
			case ">=":
				model.addConstr(expr, GRB.GREATER_EQUAL, constrain.bound, "c" +counter );
				break;
			}
			counter++;
		}
		
		//Optimize model
		model.optimize();
		
		
		for(Var var:vars){
			GRBVar grbVar = hashGrbVars.get(var.name);
			System.out.println(grbVar.get(GRB.StringAttr.VarName) + 
					 " " + grbVar.get(GRB.DoubleAttr.X));
		}
		
//		System.out.println(x1.get(GRB.StringAttr.VarName)
//              + " " +x2.get(GRB.DoubleAttr.X));
		
		model.dispose();
		env.dispose();
	}

	public void setGrbVars() throws GRBException{
		grbVars = new ArrayList<GRBVar>();
		
		for(Var var:vars){
			GRBVar temp = model.addVar(var.lowbound, var.upbound, 0.0, GRB.INTEGER, var.name);
			hashGrbVars.put(var.name, temp);
			grbVars.add(temp);
		}
		
	}
	
	public void setVars(ArrayList<String> varNames, ArrayList<Integer> varTypes){
		for(int i = 0; i< varNames.size();i++){
			Var var = new Var();
			//variable
			if(varTypes.get(i) == 0){
				var.lowbound = -100;
				var.upbound = 100;
				var.name = varNames.get(i);
			}
			//binary
			else{
				var.lowbound = 0;
				var.upbound = 1;
				var.name = varNames.get(i);
			}
			vars.add(var);	
			
		}
		
		try {
			setGrbVars();
		} catch (GRBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setContrains(ArrayList<String> instructions){
		for(String instruction:instructions){
			setConstrain(instruction);
		}
	}
	 
	public void setConstrain(String instruction){
		Constrain constrain = new Constrain();
		parseString(instruction,constrain);
		constrains.add(constrain);
	}
	 
	public void setObjective(String instruction){
		parseString(instruction,obj);
	}
	  
	 
	  
	public  void parseString(String instruction,Constrain constrain){
		 
		  String coef = "";
		  String variable = "";
		  String operator = "";
		  String bound = "";
		  
	
		  String state = "init";
		 
		  
		  for(int i =0 ; i < instruction.length(); i ++){
			  char c = instruction.charAt(i);
			  
			  switch(state){
			  
			  	case "init":
			  		if( c >= 48 & c <58 ){			 
			  			coef = coef + c;
						//System.out.println("now " + state);
						//System.out.println(c);
					}
			  		else if ( (c >= 64 & c <91) || (c>= 97 & c < 122) ){
						
						state = "variables";	
						variable = variable + c;
						if(!coef.isEmpty()){	
							if (coef.equals("-"))
								constrain.cofficient.add(-1);
							else
								constrain.cofficient.add(Integer.parseInt(coef));						
			  			}
			  			else
			  				constrain.cofficient.add(1);
						
						if(i == instruction.length()-1){								
							if(!variable.isEmpty())
								 constrain.varibleNames.add(variable);
						}
						//System.out.println("now " + state);			 
						//System.out.println(c);
					}
			  		else if(c == '-'){
			  			coef += "-";
			  		}
			  		
					break;
			  	case "cofficient":
			  		if( c >= 48 & c <58 ){			 
			  			coef = coef + c;
						//System.out.println("now " + state);
						//System.out.println(c);
					}
			  		else if ( (c >= 64 & c <91) || (c>= 97 & c < 122) ){
						
						state = "variables";	
						variable = variable + c;
						if(!coef.isEmpty()){	
							if (coef.equals("-"))
								constrain.cofficient.add(-1);
							else
								constrain.cofficient.add(Integer.parseInt(coef));						
			  			}
			  			else
			  				constrain.cofficient.add(1);
						
						if(i == instruction.length()-1){								
							if(!variable.isEmpty())
								 constrain.varibleNames.add(variable);
						}
						//System.out.println("now " + state);			 
						//System.out.println(c);
					}	  		
			  		
					break;
			  	case "variables":
			  		
			  		
			  		if ( (c >= 64 & c <91) || (c>= 97 & c < 122) || (c >= 48 & c <58) ){
						
						state = "variables";
						
//						if(!coef.isEmpty()){	
//							if (coef.equals("-"))
//								constrain.cofficient.add(-1);
//							else
//								constrain.cofficient.add(Integer.parseInt(coef));						
//			  			}
//			  			else
//			  				constrain.cofficient.add(1);
						variable = variable + c;
						
						if(i == instruction.length()-1){
							if(!variable.isEmpty())
								 constrain.varibleNames.add(variable);
						}
//						System.out.println("now " + state);			 
//						System.out.println(c);
					}
			  		else if  (c == '+' || c == '-'){
			  			state = "cofficient";
			  			if(!variable.isEmpty())
							 constrain.varibleNames.add(variable);
			  			
						
			  			coef = "";
						variable = "";
						if(c == '-')
							coef = coef + '-'; 
						//System.out.println("now " + state);
						//System.out.println(c);
			  		}
			  		else if (c == '=') {
			  			state = "bound";
			  			constrain.operators = "=";
			  			if(!variable.isEmpty())
							 constrain.varibleNames.add(variable);
			  			
			  		}
			  		else if(c == '<'){
			  			state = "< waiting for =";
			  			if(!variable.isEmpty())
							 constrain.varibleNames.add(variable);
			  			
			  		}
			  		else if(c == '>'){
			  			state = "> waiting for =";
			  			if(!variable.isEmpty())
							 constrain.varibleNames.add(variable);
			  			
			  		}
			  		break;
			  		
			  	case "< waiting for =":
			  		state = "bound";
			  		if (c == '=')
						 constrain.operators = "<=";
					else
						 constrain.operators = "<";
					if(c >= 48 & c <58 )
						bound = bound + c;
					break;
			  	
			  	case "> waiting for =":
			  		state = "bound";
			  		if (c == '=')
						 constrain.operators = ">=";
					else
						 constrain.operators = ">";
					if(c >= 48 & c <58 )
						bound = bound + c;
					break;
					
			  	case "bound":		  		
			  		if (c >= 48 & c <58 || c == '-' || c == '+')
			  			bound = bound + c;
			  		if(i == instruction.length()-1)
						 constrain.bound = Integer.parseInt(bound);
			  		break;
				  
			  }
			  
			  
			 
			
					  
		  }
		  state = "";
		  
	  }
	
}
