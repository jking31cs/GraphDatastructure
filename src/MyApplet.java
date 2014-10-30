import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.*;

import com.jking31cs.*;

import com.jking31cs.Vector;
import processing.core.*;
import processing.event.MouseEvent;
import processing.opengl.PGL;
import processing.opengl.PGraphics3D;


public class MyApplet extends PApplet {
	
	Graph3D model;
	PVector mouse;
	boolean drawMode;
	boolean editMode;
	boolean showSideWalks;
	boolean showCorners;
	int editModeClickCount=0;
	int editModeLeastFound=-1;
	int leastFound;
	
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
	private Point showCornerPoint;

	@Override
	public void setup() {
		
		size(800,600, P3D);
		noSmooth();

		drawMode=true;
		editMode=false;
		showSideWalks=false;
		showCorners=false;
		
		bose=loadImage("pics/portrait_bose.jpg");
		bobby=loadImage("pics/portrait_bobby.png");

		model = new Graph3D();
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
			drawGraph(model);
		} else {
			pushMatrix();
			camera();
			translate(width/2,height/2,dz); // puts origin of model at screen center and moves forward/away by dz
//			lights();  // turns on view-dependent lighting
			rotateX(rx); rotateY(ry); // rotates the model around the new origin (center of screen)
			rotateX(PI/2); // rotates frame around X to make X and Y basis vectors parallel to the floor
			mouse=new PVector(mouseX, mouseY);
			drawGraph(model);
			popMatrix();
		}
		
		
		drawStuff();
		  
		//drawGraph(g2);
		  //Dynamically drawing the next vertex and edge while drawing the graph
		  if (drawMode)
		  {
		    if (model.points.size()>0)
		    {
		      Point tempPoint=new Point(mouse.x, mouse.y);
		      drawPoint(tempPoint,-1);
		      Edge tempEdge=new Edge(model.points.get(model.points.size()-1), tempPoint);
		      drawEdge(tempEdge);
		    }
		  }
		  if (editMode) {
			  
			  
			  if (model.points.size()>0 && editModeLeastFound!=-1) {
			      Point tempPoint=new Point(mouse.x, mouse.y);
			      drawPoint(tempPoint,-1);
			      Edge tempEdge=new Edge(model.points.get(editModeLeastFound), tempPoint);
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
	    Point addedPoint = null;
	    if (model.points.size()>=3)
	    {
	      leastFound=findClickedVertex(20);
	      if (leastFound!=-1)
	      {
	        model.addEdge(new Edge(model.points.get(model.points.size() - 2), model.points.get(leastFound)));
	      } else
	      {
	        addedPoint=new Point(mouse.x, mouse.y);
	      }
	    } else
	    {
	    	addedPoint=new Point(mouse.x, mouse.y);
	    }
	    if(addedPoint != null)
	    	model.addVertex(addedPoint);
	    if (model.points.size()/2>1 && addedPoint != null)
	    {
	    	Point prevPoint = model.points.get(model.points.size()-4);
	    	Edge addedEdge=new Edge(prevPoint, addedPoint);
	    	//graph.vertices.get(graph.vertices.size()-2).addEdge(addedEdge);
	    	model.addEdge(addedEdge);
	    }
	  }

	  if (editMode)
	  {
	    if(showCorners){
	    	List<Corner> allCorners= model.getCorners();

			Point clickedPoint = pick(mouseX, mouseY);
	    	Point highlightPoint = model.points.get(0);
			for (Point p : model.points) {
				float distance=(float)new Vector(p.x,p.y,p.z).sub(new Vector(clickedPoint.x, clickedPoint.y, clickedPoint.z)).getMag();
				float minDistance=(float)new Vector(highlightPoint.x,highlightPoint.y,highlightPoint.z).sub(new Vector(clickedPoint.x, clickedPoint.y, clickedPoint.z)).getMag();
				if(distance<minDistance){
					highlightPoint = p;
				}
			}
			showCornerPoint = highlightPoint;
	    }
	    
	  }
	}
	
	//This method switches input modes
	public void keyReleased() {
	  	if (key == 'e') {
	    	//println("called");
			drawMode=false;
			editMode=true;
	  	}
	  	if (key == 'd') {
			drawMode=true;
			editMode=false;
	  	}
	  	if (key == 's') {
	    	showSideWalks=!showSideWalks;
	  	}
	  	if (key == 'c') {
	    	showCorners=!showCorners;
	  	}
		if (Character.isDigit(key) && editMode && showCorners
				&& Integer.parseInt(Character.toString(key)) < model.points.size()) {
			showCornerPoint = model.points.get(Integer.parseInt(Character.toString(key)));
			System.out.println(model.points.get(Integer.parseInt(Character.toString(key))));
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
	      model.points.get(leastFound).x=mouse.x;
	      model.points.get(leastFound).y=mouse.y;
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
				List<Corner> cornersToShow = new ArrayList<>();
				for (Corner c : model.getCorners()) {
					if (c.getCommonPoint().equals(showCornerPoint)) cornersToShow.add(c);
				}
				for (Corner c : cornersToShow) {
					Integer index = g1.getCorners().indexOf(c);
					fill(0, 0, 255);
					drawCorner(c);
//					drawCorner(g1.nextCorner(g1.corners.get(index)));
//					if (g1.swingCorner(g1.corners.get(index)) != null) {
//						fill(0, 255, 0);
//						drawCorner(g1.swingCorner(g1.corners.get(index)));
//						fill(255,0,0);
//						drawCorner(g1.unSwingCorner(g1.corners.get(index)));
//					}
				}
//				Corner c = g1.getCornerFromIndex(g1.corners.get(cornerToShow));
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

			text("" + index, x - 5, y + 5);
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
		pushMatrix();
		translate((float) cornerPosition.x, (float) cornerPosition.y, (float) cornerPosition.z);
		sphere(5);
		popMatrix();
	}
	
	//This function looks at the current mouse position and returns the index of the vertex on it(distance to register is set to "reg")
	int findClickedVertex(int reg)
	{
	  float dist;
	  int leastIndex;
	  int leastFound=-1;
	  //Loop to find which vertex is shorest distance from the click
	  for (leastIndex=0; leastIndex<model.points.size(); leastIndex++)
	  {
		PVector pointPosition=new PVector((float) model.points.get(leastIndex).x,(float) model.points.get(leastIndex).y);
		if (pointPosition.z != 0) continue;
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
	  for (leastIndex=0; leastIndex<model.points.size(); leastIndex++)
	  {
		PVector pointPosition=new PVector((float) model.points.get(leastIndex).x,(float) model.points.get(leastIndex).y);
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
		}
	}	
	
	@Override
	public void mouseWheel(MouseEvent event) {
		dz -= event.getAmount(); 
	}

	public Point pick(int mX, int mY)
	{
		PGL pgl = beginPGL();
		FloatBuffer depthBuffer = ByteBuffer.allocateDirect(1 << 2).order(ByteOrder.
				nativeOrder()).asFloatBuffer();
		pgl.readPixels(mX, height - mY - 1, 1, 1, PGL.DEPTH_COMPONENT, PGL.FLOAT, depthBuffer);
		float depthValue = depthBuffer.get(0);
		depthBuffer.clear();
		endPGL();

		//get 3d matrices
		PGraphics3D p3d = (PGraphics3D)g;
		PMatrix3D proj = p3d.projection.get();
		PMatrix3D modelView = p3d.modelview.get();
		PMatrix3D modelViewProjInv = proj; modelViewProjInv.apply( modelView ); modelViewProjInv.invert();

		float[] viewport = {0, 0, p3d.width, p3d.height};

		float[] normalized = new float[4];
		normalized[0] = ((mX - viewport[0]) / viewport[2]) * 2.0f - 1.0f;
		normalized[1] = ((height - mY - viewport[1]) / viewport[3]) * 2.0f - 1.0f;
		normalized[2] = depthValue * 2.0f - 1.0f;
		normalized[3] = 1.0f;

		float[] unprojected = new float[4];

		modelViewProjInv.mult( normalized, unprojected );
		Point point = new Point(unprojected[0] / unprojected[3], unprojected[1] / unprojected[3], unprojected[2] / unprojected[3]);
		return point;
	}
	

	public static void main(String[] args) {
		PApplet.main("MyApplet");
	}
}
