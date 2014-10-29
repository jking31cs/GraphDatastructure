import java.util.*;

import com.jking31cs.*;

import com.jking31cs.Vector;
import processing.core.*;
import processing.event.MouseEvent;


public class MyApplet extends PApplet {
	
	Model3D model;
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
	String names="Bobby King";
	
	float dz = -490, rx = -.1832594f, ry = -.6479535f;
	@Override
	public void setup() {
		
		size(800,600, P3D);
		

		drawMode=true;
		editMode=false;
		showSideWalks=false;
		showCorners=false;
		
		bose=loadImage("pics/portrait_bose.jpg");
		bobby=loadImage("pics/portrait_bobby.png");

		model = new Model3D();
		for(int i=0;i<50;i++)
		{
			colorR.add((int)random(0,255));
			colorG.add((int)random(0,30));
			colorB.add((int)random(0,255));
		}
		
	}
	
	public void draw()
	{
		background(255);
		if (drawMode) {
			mouse=new PVector(mouseX, mouseY);
			drawGraph(model.mainGraph);
		} else {
			pushMatrix();
			camera();
			translate(width/2,height/2,dz); // puts origin of model at screen center and moves forward/away by dz
			lights();  // turns on view-dependent lighting
			rotateX(rx); rotateY(ry); // rotates the model around the new origin (center of screen)
			rotateX(PI/2); // rotates frame around X to make X and Y basis vectors parallel to the floor
			mouse=new PVector(mouseX, mouseY);
			drawGraph(model.mainGraph);
			drawGraph(model.z_offset_graph);
			for (Edge e : model.verticalEdges()) drawEdge(e);
			popMatrix();
		}
		
		
		drawStuff();
		  
		//drawGraph(g2);
		  //Dynamically drawing the next vertex and edge while drawing the graph
		  if (drawMode)
		  {
		    if (model.mainGraph.points.size()>0)
		    {
		      Point tempPoint=new Point(mouse.x, mouse.y);
		      drawPoint(tempPoint,-1);
		      Edge tempEdge=new Edge(model.mainGraph.points.get(model.mainGraph.points.size()-1), tempPoint);
		      drawEdge(tempEdge);
		    }
		  }
		  if (editMode) {
			  
			  
			  if (model.mainGraph.points.size()>0 && editModeLeastFound!=-1) {
			      Point tempPoint=new Point(mouse.x, mouse.y);
			      drawPoint(tempPoint,-1);
			      Edge tempEdge=new Edge(model.mainGraph.points.get(editModeLeastFound), tempPoint);
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
	    if (model.mainGraph.points.size()>=3)
	    {
	      leastFound=findClickedVertex(20);
	      if (leastFound!=-1)
	      {
	        addedPoint=new Point(-1,-1);
	        model.addEdge(new Edge(model.mainGraph.points.get(model.mainGraph.points.size()-1), model.mainGraph.points.get(leastFound)));
	      } else
	      {
	        addedPoint=new Point(mouse.x, mouse.y);
	      }
	    } else
	    {
	    	addedPoint=new Point(mouse.x, mouse.y);
	    }
	    if(addedPoint.x!=-1 && addedPoint.y!=-1)
	    	model.addPoint(addedPoint);
	    if (model.mainGraph.points.size()>1 && addedPoint.x!=-1 && addedPoint.y!=-1)
	    {
	      Edge addedEdge=new Edge(model.mainGraph.points.get(model.mainGraph.points.size()-2), model.mainGraph.points.get(model.mainGraph.points.size()-1));
	      //graph.vertices.get(graph.vertices.size()-2).addEdge(addedEdge);
	      model.addEdge(addedEdge);
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
						model.addPoint(new Point(mouse.x, mouse.y));
						model.addEdge(new Edge(model.mainGraph.points.get(editModeLeastFound),
								model.mainGraph.points.get(model.mainGraph.points.size() - 1)));
					} else {
						model.addEdge(new Edge(model.mainGraph.points.get(editModeLeastFound),
								model.mainGraph.points.get(tempLeastFound)));
					}
					editModeClickCount = 0;
					editModeLeastFound = -1;
				}
			}
	    if(showCorners){
	    	List<Corner> allCorners= model.mainGraph.getCorners();
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
	      model.mainGraph.points.get(leastFound).x=mouse.x;
	      model.mainGraph.points.get(leastFound).y=mouse.y;
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
						pushMatrix();
						translate((float) loop.get(j).getPosition(20).x,(float) loop.get(j).getPosition(20).y,50);
						sphere(10);
						popMatrix();
						
					}
					line((float) loop.get(j).getPosition(20).x, (float) loop
							.get(j).getPosition(20).y, (float) loop.get(0)
							.getPosition(20).x, (float) loop.get(0)
							.getPosition(20).y);
					pushMatrix();
					translate((float) loop.get(j).getPosition(20).x,(float) loop.get(j).getPosition(20).y,50);
					sphere(10);
					popMatrix();
					

				}
			}
			if (showCorners) {
				//println(g1.getCorners().size());
				Corner c = g1.getCornerFromIndex(g1.corners.get(cornerToShow));
				drawCorner(c);
				fill(0, 0, 255);
				drawCorner(g1.nextCorner(g1.corners.get(cornerToShow)));
				if (g1.swingCorner(g1.corners.get(cornerToShow)) != null) {
					fill(0, 255, 0);
					drawCorner(g1.swingCorner(g1.corners.get(cornerToShow)));
					fill(255,0,0);
					drawCorner(g1.unSwingCorner(g1.corners.get(cornerToShow)));
				}
			}
		}
			
	}
	public void drawPoint(Point p,int index) {
	    if (drawMode) {
			fill(0,255,0);
			float x = (float) p.x,
					y = (float) p.y;
			ellipse(x, y,20,20);
			fill(0);

			text(""+index, x -5, y +5);
		} else if (editMode) {
			fill(0,255,0);
			pushMatrix();
			translate((float) p.x, (float) p.y, (float) p.z);
			sphere(10);
			popMatrix();
		}

  	}

	public void drawEdge(Edge e)
	  {
	    stroke(0);
	    line((float) e.p1.x, (float) e.p1.y, (float) e.p1.z, (float) e.p2.x, (float) e.p2.y, (float) e.p2.z);
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
	  for (leastIndex=0; leastIndex<model.mainGraph.points.size(); leastIndex++)
	  {
		PVector pointPosition=new PVector((float) model.mainGraph.points.get(leastIndex).x,(float) model.mainGraph.points.get(leastIndex).y);
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
	  for (leastIndex=0; leastIndex<model.mainGraph.points.size(); leastIndex++)
	  {
		PVector pointPosition=new PVector((float) model.mainGraph.points.get(leastIndex).x,(float) model.mainGraph.points.get(leastIndex).y);
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
	  image(bobby,675,20,100,100);
	  fill(0);
	  text(title,5,10);
	  text(instructions,5,550);
	  text(legend,5,570);
	  text(names,675,10);
	}
	
	@Override
	public void mouseMoved() {
		if (keyPressed && key==' ') {
			rx-=PI*(mouseY-pmouseY)/height;
			ry+=PI*(mouseX-pmouseX)/width;
			System.out.println(String.format("rx: %s, ry: %s, dz: %s", rx,ry,dz));
		}
	}	
	
	@Override
	public void mouseWheel(MouseEvent event) {
		dz -= event.getAmount(); 
		System.out.println(String.format("rx: %s, ry: %s, dz: %s", rx,ry,dz));
	}
	

	public static void main(String[] args) {
		PApplet.main("MyApplet");
	}
}
