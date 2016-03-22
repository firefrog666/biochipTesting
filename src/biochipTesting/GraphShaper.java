package biochipTesting;

import java.util.ArrayList;

public class GraphShaper {
	
	public	Graph graph;
	public Graph conciseGraph;
	
	public ArrayList<Graph> subGraphs;
	public ArrayList<ArrayList<Int2>> paths;
	public ArrayList<Int2> leftLow;
	public ArrayList<Int2> upperRight;
	
	public GraphShaper(Graph g){
		graph = g;
		subGraphs = new ArrayList<Graph>();
	}
	
	public void splitGraph(Graph targetG, ArrayList<Graph> graphs, int row, int column){
		
	}
	
	private void getSubGraphs(){
		for(int i = 0; i<= leftLow.size()-1; i ++){
			Graph subGraph = graph.getSubGraph(leftLow.get(i),upperRight.get(i) );
			subGraphs.add(subGraph);
		}
		Int2 first;
		Int2 second;
		Int2 center;
		Int2 temp;
		boolean reverse = false;
		for(Graph subGraph:subGraphs){			
			for(ArrayList<Int2> pathVertex : paths){
				for(int i = 0 ; i <= pathVertex.size()-2; i ++){
					first = pathVertex.get(i);
					second = pathVertex.get(i+1);
					while(second.equals(first)){
						i++;
						second = pathVertex.get(i);
					}
						
					if(first.x > second.x || first.y > second.y){
						temp = first;
						first = second;
						second = temp;
						reverse = true;
					}
					
					center = subGraph.center;
					if(first.x == center.x && second.x == center.x){
						if(first.y < center. y && second.y > center.y){
							subGraph.setHeadsTails(direction.West, direction.East);
						}
						
						else if(first.y <center.y && second.y == center.y){
							if(reverse){
								for(int j = i+1; j <= pathVertex.size()-1; j++){
									temp = pathVertex.get(j);
									if(temp.y > center.y){
										subGraph.setHeadsTails(direction.East, direction.West);
										break;
									}
									if(temp.x > center.x){
										subGraph.setHeadsTails(direction.East, direction.North);
										break;
									}
									if(temp.x < center.x){
										subGraph.setHeadsTails(direction.East, direction.South);
										break;
									}
								}
							}
							
						}
						
					
					}
					else if(first.y == center.y && second.y == center.y){
						
					}
				
			}
		}
		}
	}
	
 
	

}
