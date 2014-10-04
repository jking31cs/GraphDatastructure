import java.util.List;

import com.jking31cs.Corner;
import com.jking31cs.Edge;
import com.jking31cs.Graph;
import com.jking31cs.Point;
import com.jking31cs.Vector;

import processing.core.*;


public class MyApplet extends PApplet {
	
	Graph g;
	PVector mouse;
	PVector mouseClicked;
	boolean drawMode;
	boolean editMode;
	int editModeClickCount=0;
	int editModeLeastFound=-1;
	int leastFound;
	@Override
	public void setup() {
		
		size(800,600);

		drawMode=true;
		editMode=false;
		
		g = new Graph();
		/*
		Point p1 = new Point(100,100);
		g.addVertex(p1);
		Point p2 = new Point(300,300);
		g.addVertex(p2);
		Point p3 = new Point(100,200);
		g.addVertex(p3);
		Point p4 = new Point(30,75);
		g.addVertex(p4);
		
		g.addEdge(new Edge(p1,p2));
		g.addEdge(new Edge(p2,p3));
		g.addEdge(new Edge(p3,p1));
		g.addEdge(new Edge(p1,p4));
		
		*/
		
	}
	
	public void draw()
	{
		background(255);
		mouse=new PVector(mouseX, mouseY);
		drawGraph();
		
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
	    if (editModeClickCount==0)
	    {
	      editModeLeastFound=findClickedVertex(20, 40);
	    }
	    if (editModeClickCount>0)
	    {

	      println("called");
	      int tempLeastFound=findClickedVertex(20);
	      if (tempLeastFound==-1)
	      {
	        g.addVertex(new Point(mouse.x, mouse.y));
	        g.addEdge(new Edge(g.points.get(editModeLeastFound), g.points.get(g.points.size()-1)));
	      } else
	      {
	        g.addEdge(new Edge(g.points.get(editModeLeastFound), g.points.get(tempLeastFound)));
	      }
	      editModeClickCount=0;
	      editModeLeastFound=-1;
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
	      println("called");
	    }
	  }
	}
	
	
	
	
	
	/*
	 * ALL functions beyond this point are for drawing pusposes 
	 */
	private void drawGraph() {
		int i = 0;
		//Draw Edges
		for (Edge e : g.getEdges()) {
			drawEdge(e);
		}	
		//Draw points.
		for (Point p : g.points) {	
			drawPoint(p,i);
			i++;
		}		
	}
	public void drawPoint(Point p,int index)
	  {
	    fill(0,255,0);
	    ellipse(p.x,p.y,20,20);
	    fill(0);
	    text(""+index,p.x-5,p.y+5);
	    
	  }
	public void drawEdge(Edge e)
	  {
	    stroke(0);
	    line(e.p1.x, e.p1.y, e.p2.x, e.p2.y);
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
		PVector pointPosition=new PVector(g.points.get(leastIndex).x,g.points.get(leastIndex).y);
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
		PVector pointPosition=new PVector(g.points.get(leastIndex).x,g.points.get(leastIndex).y);
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

	public static void main(String[] args) {
		PApplet.main("MyApplet");
	}
}
