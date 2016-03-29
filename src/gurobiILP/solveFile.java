package gurobiILP;
import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import gurobiILP.*;

import java.util.ArrayList;
import java.util.HashMap;

public class solveFile {
	public static HashMap<String,Integer> hashResults;
	
		  public static void solve(String args) {
			
			hashResults = new HashMap<String,Integer>();
			  
		

		    try {
		      GRBEnv env = new GRBEnv();
		      GRBModel model = new GRBModel(env, args);

		      model.optimize();

		      int optimstatus = model.get(GRB.IntAttr.Status);

		      if (optimstatus == GRB.Status.INF_OR_UNBD) {
		        model.getEnv().set(GRB.IntParam.Presolve, 0);
		        model.optimize();
		        optimstatus = model.get(GRB.IntAttr.Status);
		      }

		      if (optimstatus == GRB.Status.OPTIMAL) {
		    	  GRBVar[] vars  = model.getVars();
		    	  for(GRBVar var:vars){
		    		  hashResults.put(var.get(GRB.StringAttr.VarName), (int)var.get(GRB.DoubleAttr.X));
		    	  }
		    	
		        double objval = model.get(GRB.DoubleAttr.ObjVal);
		        System.out.println("Optimal objective: " + objval);
		      } else if (optimstatus == GRB.Status.INFEASIBLE) {
		        System.out.println("Model is infeasible");

		        // Compute and write out IIS
		        model.computeIIS();
		        model.write("model.ilp");
		      } else if (optimstatus == GRB.Status.UNBOUNDED) {
		        System.out.println("Model is unbounded");
		      } else {
		        System.out.println("Optimization was stopped with status = "
		                           + optimstatus);
		      }

		      // Dispose of model and environment
		      model.dispose();
		      env.dispose();

		    } catch (GRBException e) {
		      System.out.println("Error code: " + e.getErrorCode() + ". " +
		          e.getMessage());
		    }
		  }
}


