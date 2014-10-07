import java.util.*;

import com.jking31cs.Corner;
import com.jking31cs.Edge;
import com.jking31cs.Graph;
import com.jking31cs.Point;
import com.jking31cs.Vector;

import processing.core.*;


public class MyApplet extends PApplet {
	
	Graph g;
	Graph g2;
	PVector mouse;
	PVector mouseClicked;
	boolean drawMode;
	boolean editMode;
	boolean showSideWalks;
	boolean showCorners;
	int editModeClickCount=0;
	int editModeLeastFound=-1;
	int leastFound;
	
	int cornerToShow=0;
	
	//Randomized color lookup table
	ArrayList<Integer> colorR=new ArrayList<>();
	ArrayList<Integer> colorG=new ArrayList<>();
	ArrayList<Integer> colorB=new ArrayList<>();
	
	//Instrction's and Pictures
	PImage bose;
	PImage bobby;
	String title="CS 6491, Fall 2014, Title: Graph Datastructure";
	String instructions="Keys: E: Edit Mode(Click near Vertex to create new one) S:Toggle sidewalks in Edit Mode C:Toggle Corners in Edit Mode";
	String legend="Legend in Corner Mode:   Black:Current Corner Blue:Next Corner Green: Swing Corner Red:Unswing Corner";
	String names="Bobby King & Arindam Bose";
	@Override
	public void setup() {
		
		size(800,600);

		drawMode=true;
		editMode=false;
		showSideWalks=false;
		showCorners=false;
		
		bose=loadImage("pics/portrait_bose.jpg");
		bobby=loadImage("pics/portrait_bobby.png");
	
		g = new Graph();
		for(int i=0;i<50;i++)
		{
			colorR.add((int)random(0,255));
			colorG.add((int)random(0,30));
			colorB.add((int)random(0,255));
		}
		/*
		g2=new Graph();
		Point p1 = new Point(100,100);
		g2.addVertex(p1);
		Point p2 = new Point(300,300);
		g2.addVertex(p2);
		Point p3 = new Point(100,200);
		g2.addVertex(p3);
		Point p4 = new Point(30,75);
		g2.addVertex(p4);
		
		g2.addEdge(new Edge(p1,p2));
		g2.addEdge(new Edge(p2,p3));
		g2.addEdge(new Edge(p3,p1));
		g2.addEdge(new Edge(p1,p4));
		g2.addEdge(new Edge(p2,p4));
		*/
		
	}
	
	public void draw()
	{
		background(255);
		mouse=new PVector(mouseX, mouseY);
		drawGraph(g);
		
		drawStuff();
		//drawGraph(g2);
		  //Dynamically drawing the next vertex and edge while drawing the graph
		  if (drawMode)
		  {
		    if (g.points.size()>0)
		    {
		      Point tempPoint=new Point(mouse.x, mouse.y);
		      drawPoint(tempPoint,-1);
		      Edge tempEdge=new Edge(g.points.get(g.points.size()-1), tempPoint);
		      drawEdge(tempEdge);
		    }
		  }
		  if (editMode)
		  {
		    if (g.points.size()>0 && editModeLeastFound!=-1)
		    {
		      Point tempPoint=new Point(mouse.x, mouse.y);
		      drawPoint(tempPoint,-1);
		      Edge tempEdge=new Edge(g.points.get(editModeLeastFound), tempPoint);
		      drawEdge(tempEdge);
		      editModeClickCount++;
		      //canAdd=true;
		    }
		  }
	}
	
	
	
	
	/*
	 * ALL functions beyond this point are for event handling
	 */
	public void mouseClicked()
	{
	  //Add new vertices and edges onclick
	  if (drawMode)
	  {
	    Point addedPoint;
	    if (g.points.size()>=3)
	    {
	      leastFound=findClickedVertex(20);
	      if (leastFound!=-1)
	      {
	        addedPoint=new Point(-1,-1);
	        g.addEdge(new Edge(g.points.get(g.points.size()-1), g.points.get(leastFound)));
	      } else
	      {
	        addedPoint=new Point(mouse.x, mouse.y);
	      }
	    } else
	    {
	    	addedPoint=new Point(mouse.x, mouse.y);
	    }
	    if(addedPoint.x!=-1 && addedPoint.y!=-1)
	    	g.addVertex(addedPoint);
	    if (g.points.size()>1 && addedPoint.x!=-1 && addedPoint.y!=-1)
	    {
	      Edge addedEdge=new Edge(g.points.get(g.points.size()-2), g.points.get(g.points.size()-1));
	      //graph.vertices.get(graph.vertices.size()-2).addEdge(addedEdge);
	      g.addEdge(addedEdge);
	    }
	  }

	  if (editMode)
	  {
			if (!showCorners) {
				if (editModeClickCount == 0) {
					editModeLeastFound = findClickedVertex(20, 40);
				}
				if (editModeClickCount > 0) {

					// println("called");
					int tempLeastFound = findClickedVertex(20);
					if (tempLeastFound == -1) {
						g.addVertex(new Point(mouse.x, mouse.y));
						g.addEdge(new Edge(g.points.get(editModeLeastFound),
								g.points.get(g.points.size() - 1)));
					} else {
						g.addEdge(new Edge(g.points.get(editModeLeastFound),
								g.points.get(tempLeastFound)));
					}
					editModeClickCount = 0;
					editModeLeastFound = -1;
				}
			}
	    if(showCorners){
	    	List<Corner> allCorners= g.getCorners();
	    	Corner minCorner=allCorners.get(0);
	    	int minFound=0;
	    	for(int i=0;i<allCorners.size();i++){
	    		float distance=(float)allCorners.get(i).getPosition(20).sub(new Vector(mouseX,mouseY)).getMag();
	    		float minDistance=(float)minCorner.getPosition(20).sub(new Vector(mouseX,mouseY)).getMag();
	    		if(distance<minDistance){
	    			minFound=i;
	    			minCorner=allCorners.get(i);
	    		}
	    	}
	    	cornerToShow=minFound;
	    }
	    
	  }
	}
	
	//This method switches input modes
	public void keyReleased()
	{
	  if (key == 'e')
	  {
	    //println("called");
	    drawMode=false;
	    editMode=true;
	  }
	  if (key == 'd')
	  {
	    drawMode=true;
	    editMode=false;
	  }
	  if (key == 's')
	  {
	    showSideWalks=!showSideWalks;
	  }
	  if (key == 'c')
	  {
	    showCorners=!showCorners;
	  }
	}
	
	public void mouseDragged()
	{
	  //Once initital drawing mode is over it switches to this mode
	  if (editMode)
	  {
	    int leastFound=findClickedVertex(20);
	    if (leastFound!=-1)
	    {
	      g.points.get(leastFound).x=mouse.x;
	      g.points.get(leastFound).y=mouse.y;
	    }
	  }
	}
	
	
	
	
	
	/*
	 * ALL functions beyond this point are for drawing pusposes 
	 */
	private void drawGraph(Graph g1) {
		int i = 0;
		//Draw Edges
		for (Edge e : g1.getEdges()) {
			drawEdge(e);
		}	
		//Draw points.
		for (Point p : g1.points) {	
			drawPoint(p,i);
			i++;
		}	
		
		if (editMode) {
			if (showSideWalks) {
				// Draw all corners
				for (Corner c : g1.getCorners()) {
					drawCorner(c);
				}

				// Draw all sidewalks
				int z = 0;
				List<Double> areas = g1.faceAreas();
				for (Map.Entry<Integer, ArrayList<Corner>> entry : g1.getSideWalkPaths().entrySet()) {
					List<Corner> loop = entry.getValue();
					int j = 0;
					stroke(colorR.get(z), colorG.get(z), colorB.get(z));
					z++;
					for (j = 0; j < loop.size() - 1; j++) {
						line((float) loop.get(j).getPosition(20).x,
								(float) loop.get(j).getPosition(20).y,
								(float) loop.get(j + 1).getPosition(20).x,
								(float) loop.get(j + 1).getPosition(20).y);
					}
					line((float) loop.get(j).getPosition(20).x, (float) loop
							.get(j).getPosition(20).y, (float) loop.get(0)
							.getPosition(20).x, (float) loop.get(0)
							.getPosition(20).y);
					stroke(0);
					if (areas.get(entry.getKey()) != null) {
						text(areas.get(entry.getKey()).toString(), (float) loop.get(0)
								.getPosition(50).x, (float) loop.get(0)
								.getPosition(50).y);
					}

				}
			}
			if (showCorners) {
				//println(g1.getCorners().size());
				Corner c = g1.getCornerFromIndex(g1.corners.get(cornerToShow));
				drawCorner(c);
				fill(0, 0, 255);
				drawCorner(g1.nextCorner(g1.corners.get(cornerToShow)));
				fill(0, 255, 0);
				drawCorner(g1.swingCorner(g1.corners.get(cornerToShow)));
				fill(255,0,0);
				drawCorner(g1.unSwingCorner(g1.corners.get(cornerToShow)));
			}
		}
			
	}
	public void drawPoint(Point p,int index)
	  {
	    fill(0,255,0);
		float x = (float) p.x,
		       y = (float) p.y;
	    ellipse(x, y,20,20);
	    fill(0);

	    text(""+index, x -5, y +5);
	    
	  }
	public void drawEdge(Edge e)
	  {
	    stroke(0);
	    line((float) e.p1.x, (float) e.p1.y, (float) e.p2.x, (float) e.p2.y);
	  }
	
	public void drawCorner(Corner c)
	{
		Vector cornerPosition=c.getPosition(20);
		ellipse((float)cornerPosition.x,(float)cornerPosition.y,5,5);
	}
	
	//This function looks at the current mouse position and returns the index of the vertex on it(distance to register is set to "reg")
	int findClickedVertex(int reg)
	{
	  float dist;
	  int leastIndex;
	  int leastFound=-1;
	  //Loop to find which vertex is shorest distance from the click
	  for (leastIndex=0; leastIndex<g.points.size(); leastIndex++)
	  {
		PVector pointPosition=new PVector((float) g.points.get(leastIndex).x,(float) g.points.get(leastIndex).y);
	    dist=PVector.sub(mouse, pointPosition).mag();
	    //println(dist);
	    if (dist<reg)
	    {
	      leastFound=leastIndex;
	      break;
	    }
	  }
	  return leastFound;
	}
	
	//This function looks at the current mouse position and returns the index of the vertex between reg1 and reg2 distance from it
	int findClickedVertex(int reg1, int reg2)
	{
	  float dist;
	  int leastIndex;
	  int leastFound=-1;
	  //Loop to find which vertex is shorest distance from the click
	  for (leastIndex=0; leastIndex<g.points.size(); leastIndex++)
	  {
		PVector pointPosition=new PVector((float) g.points.get(leastIndex).x,(float) g.points.get(leastIndex).y);
		dist=PVector.sub(mouse, pointPosition).mag();
	    //println(dist);
	    if (dist>reg1 && dist<reg2)
	    {
	      leastFound=leastIndex;
	      break;
	    }
	  }
	  return leastFound;
	}
	
	
	void drawStuff()
	{
	  image(bose,700,20,100,100);
	  image(bobby,595,20,100,100);
	  fill(0);
	  text(title,5,10);
	  text(instructions,5,550);
	  text(legend,5,570);
	  text(names,600,10);
	}
	

	public static void main(String[] args) {
		PApplet.main("MyApplet");
	}
}
