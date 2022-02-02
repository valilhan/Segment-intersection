import java.util.*;
import java.io.*;
//Ilyasov Valihan Group 2
//https://codeforces.com/group/3ZU2JJw8vQ/contest/272963/submission/74503909
public class Sweep {
	/*
	 
	Description of the algorithm:
	In the first step of the algorithm, we sort segments by x coordinate. We have an imaginary straight line (usually vertical) 
	this line covers points of the segments from left to right. When we reach a point of the segment. We add this point to the red-black tree, 
	after adding we check there are a predecessor and a successor,(point below and above from point which was added). 
	We check for intersection the segment for which belongs to this point segments that lie below and above. When the imaginary 
	straight line reached the ending point of the segment. We check for intersection segments that lie below and above.
	When an imaginary straight line reached the last right segment we know what segments are intersected
	
	Importance of application of the algorithm:
	Finding the intersection between two segments is a basic operation in geometric algorithms.
	For instance if we have two polygons and we want to compute the intersection of the polygons 
	we should find intersection between line segments. There is another application as If we have a map of the
	landscape and where could be a lot of rainfall. We solve problems of intersection two segments where
	 rain is one segment and landscape another
	 */
	public static void main(String[] args) throws IOException {
		
		//O(n log n)
		//======================================START=SWEEP LINE ALGORITHM================================================
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));		//String for scanning letter from input
		int n=Integer.parseInt(br.readLine().trim());								//number of segment
		
	    Comparator<Point> comp1 = new Comparator<Point>() {							//comparator for merge
			@Override
		    public int compare(Point p1, Point p2)									
		    {
				if(p1.get_x()==p2.get_x()) {										//if two point have same coordinate than sort by id of segment
					segment segm1=p1.get_segment();
					segment segm2=p2.get_segment();
					return segm1.id-segm2.id;
				}	
				return p1.get_x()-p2.get_x();										
		    }
	     };
	     
		Array<Point> arr_point=new Array<Point>();									//class array which keeps all point
	    
		for(int i=0;i<n;i++) {
			String[] Line=br.readLine().trim().split(" ");

			int xp=Integer.parseInt(Line[0]);										
			int yp=Integer.parseInt(Line[1]);
			int xq=Integer.parseInt(Line[2]);
			int yq=Integer.parseInt(Line[3]);
			if(xp<xq) {																//compare x to coordinates to each other
				segment val=new segment(xp,yp, xq,yq);								//create segment from the given points
				
				Point start=new Point(xp,yp,val);									//create point "start"
				Point end=new Point(xq,yq,val);										//create point "end"
		
				arr_point.add(start);												//add point to the array of points
				arr_point.add(end);													//add point to the array of points
				
				val.add_start(start);
				val.add_end(end);
			}else {																	//else 
				segment val=new segment(xp,yp, xq,yq);								//create segment from the given points
		
				Point start=new Point(xq,yq,val);									//create point "start"
				Point end=new Point(xp,yp,val);										//create point "end"
				
				arr_point.add(start);												//add point to the array of point
				arr_point.add(end);													//add point to the array of point
				
				val.add_start(start);												//add point to the array of points
				val.add_end(end);													//add point to the array of points
			}	

		}
		
		arr_point.MergeSort(0, 2*n-1, comp1);										//sorting array of points by x
		
	    Comparator<Point> comp2 = new Comparator<Point>() {							//comparator for red black tree
			@Override
			public int compare(Point o1, Point o2) {	
				// TODO Auto-generated method stub	
				if(o1.get_y()==o2.get_y()) {										//if two point have same coordinate than sorting by id of segment
					segment segm1=o1.get_segment();									
					segment segm2=o2.get_segment();
					return segm1.id-segm2.id;
				}
				return o1.get_y()-o2.get_y();
			}
	     };
			
	     Tree<Point> tr=new Tree<Point>(comp2);										//Create red black tree
			
	     for(int i=0;i<2*n;i++) {													//add from array every point to the array
	    	 Point point=arr_point.get(i);											//get point from the array
    		 segment segm=point.get_segment();										//get segment which belongs this points
    		 if(segm.check==false) {												//check this point is starting point or ending point of this segment
    			 segm.check=true;													
	    		 int a=tr.insert(point);											//if insert return 1 than we have answer
	    		 if(a==1) break;
	    	 }else if(segm.check==true){ 											//if the point is ending of the segment than delete point from the tree
	    		 Point p=segm.get_start();											//get start point from the segment
	    		 int a=tr.delete(p);												//delete this point
	    		 if(a==1) break;													//if return 1 than we have answer
	    	 }	    	 
	     }
	     if(tr.sb.length()==0) {													//if the string builder doesn't contain any letter than we don not have answer
	    	 System.out.println("NO INTERSECTIONS");
	     }else {																	//if the string builder contains letter than we have answer
	    	 System.out.println("INTERSECTION");					
	    	 System.out.print(tr.sb.toString().trim());	
	     }
	}
	
	//======================================END=SWEEP LINE ALGORITHM================================================
}
class segment{																		//segment
	private int xs;																	
	private int ys;
	private int xe;
	private int ye;
	static  int index;																//count of all segment
	int id;																			//special id for every segment
	Point start;																	//"start" which contains starting Point 
	Point end;																		//"end" which contains ending Point
	int answer=0;
	boolean check=false;
	public segment( int xp, int yp, int xq, int yq) {								
		this.xs=xp;
		this.ys=yp;
		this.xe=xq;
		this.ye=yq;
		index++;																	//count of all segment
		id=index;																	//give special id to the segment
	}
	public void add_start(Point start) {											//add start Point to the segment
		this.start=start;
	}
	public void add_end(Point end) {												//add end Point to the segment
		this.end=end;	
	}
	public Point get_start() {														//get start Point of the segment
		return this.start;
	}
	public Point get_end() {														//get end Point of the segment
		return this.end;
	}
	public int get_xs() {															//get starting point of x of the segment 
		return xs;
	}
	public int get_ys() {															//get starting point of y of the segment
		return ys;
	}
	public int get_xe() {															//get ending point of x of the segment
		return xe;
	}
	public int get_ye() {															//get ending point of y of the segment
		return ye;
	}
}
class Point{
	segment val;																	//Point contains segment for which this point belongs
	int x;																			
	int y;
	public Point(int x, int y,  segment val) {										//constructor which add coordinates and segment
		this.x=x;	
		this.y=y;
		this.val=val;
	}
	public int get_x() {															//get x coordinate of this point
		return x;
	}
	public int get_y() {															//get y coordinate of this point
		return y;
	}
	public segment get_segment() {													//get segment for which this point belongs
		return val;
	}
	public String toString() {														//Show answer
		return val.get_xs()+" "+val.get_ys()+" "+val.get_xe()+" "+val.get_ye();
	}
}
class Node<T>{
	private Node<T> parent;		//parent of the node
	private Node<T> leftNode;	//left child of the node
	private Node<T> rightNode;	//right child of the node
	private boolean color;		// 0 is black
	private T val;
	Node(boolean color, T val){ 	// Constructor O(1)
		this.color=color;
		this.val=val;
		parent = leftNode = rightNode = null;
	}
	Node(){ 					//Constructor for creating nil the null leaf O(1)
		leftNode=null;
		rightNode=null;
		parent=null;
		val=null;
		color=true;
	}
	
	T get() { 					//The method which return the value of the node O(1)
		return val;
	}
	boolean getColor() { 		//The method which return the value of the color of the node O(1)
		return color;
	}
	Node<T> getLeft() {			//Method which return the left child of the node O(1)
		return leftNode;
	}
	Node<T> getRight() {		//Method which return the right child of the node O(1)
		return rightNode;
	}
	Node<T> getParent(){		//Method which return the parent of the node O(1)
		return parent;
	}
	void setParent(Node<T> p){	//Set the parent of the node O(1)
		parent=p;
	}
	void setLeft(Node<T> l) {	//Set the left child of the node  O(1)
		leftNode=l;
	}
	void setRight(Node<T> r) {	//Set the right child of the node O(1)
		rightNode=r;
	}
	void setColor(boolean color) { 	//set color of node
		this.color=color;
	}
	void setValue(T val) {		// Change value of the node to the val
		this.val=val;
	}

}



class Tree<T>{
	Node<T> nil=new Node<T>(); 											// Creating the null leaf
	Node<T> root=nil; 													// Creating the root and assign to the nil
	Comparator<? super T> comp;
	Tree(Comparator<? super T> comp){									//Get the comparator which allows us to compare two elements
		this.comp=comp;
	}
	StringBuilder sb=new StringBuilder();								//Write answer
	
	//=======================================START===SEGMENT INTERSECTION==========================================
	
	//O(1)
	public boolean checkPoint(double Ax, double Ay, double Bx, double By, double Cx, double Cy) {
		double dotProduct = (Cx - Ax) * (Bx - Ax) + (Cy - Ay)*(By - Ay);
		if(dotProduct <= (Bx - Ax)*(Bx - Ax) + (By - Ay)*(By - Ay) && (Cy - Ay) * (Bx - Ax) - (Cx - Ax) * (By - Ay)==0 && dotProduct>=0) {
			return true;
		}else {
			return false;
		}
	}
	
	//O(1)
	public boolean CheckIntersection(T val, T node) {					//Check intersection between two segment
		segment val1=((Point) val).get_segment();						//Get segment1 from point1
		segment val2=((Point) node).get_segment();						//Get segment2 from point2
		double StartX_1=val1.get_start().get_x();						//Start point of x of the segment1 
		double StartY_1=val1.get_start().get_y();						//Start point of y of the segment1 
		double EndX_1=val1.get_end().get_x();							//End point of x of the segment1
		double EndY_1=val1.get_end().get_y();							//End point of y of segment1
		
		double StartX_2=val2.get_start().get_x();						//Start point of x of the segment2
		double StartY_2=val2.get_start().get_y();						//Start point of y of the segment2
		double EndX_2=val2.get_end().get_x();							//End point of x of the segment2
		double EndY_2=val2.get_end().get_y();							//End point of y of segment2
		
		double det = (StartX_1 - EndX_1)*(StartY_2 - EndY_2) - (StartY_1 - EndY_1)*(StartX_2 - EndX_2); //Find determinant 
		if(det==0) {													//if det=0 it means that two segment linearly dependent
            if(checkPoint(StartX_1, StartY_1, EndX_1, EndY_1, StartX_2, StartY_2)) {//Check lies Start point of x and y of the segment2 on first segment
            	return true;
            }
            if(checkPoint(StartX_1, StartY_1, EndX_1, EndY_1, EndX_2, EndY_2)) {	//Check lies End point of x and y of the segment2 on first segment
            	return true;
            }
            if(checkPoint(StartX_2, StartY_2, EndX_2, EndY_2, StartX_1, StartY_1)) {//Check lies start point of x and y of the segment1 on second segment
            	return true;
            }
            if(checkPoint(StartX_2, StartY_2, EndX_2, EndY_2, EndX_1, EndY_1)) {	//Check lies start point of x and y of the segment1 on second segment
            	return true;
            }
		}else {																		//Two segment are not linearly dependent
			double Intersection_x=((StartX_1 * EndY_1 - StartY_1 * EndX_1) * (StartX_2 - EndX_2) - (StartX_1 - EndX_1) * (StartX_2 * EndY_2 - StartY_2 * EndX_2)) / det;
			double Intersection_y=((StartX_1 * EndY_1 - StartY_1 * EndX_1) * (StartY_2 - EndY_2) - (StartY_1 - EndY_1) * (StartX_2 * EndY_2 - StartY_2 * EndX_2)) / det;
			
			//Check intersection point lies on both segments
			if(((Intersection_x>=EndX_1 && Intersection_x<=StartX_1) || (Intersection_x<=EndX_1 && Intersection_x>=StartX_1)) && ((Intersection_x>=StartX_2 && Intersection_x<=EndX_2) || (Intersection_x<=StartX_2&& Intersection_x>=EndX_2))) {
	            if(((Intersection_y>=EndY_1 && Intersection_y<=StartY_1) | (Intersection_y<=EndY_1 && Intersection_y>=StartY_1)) && ((Intersection_y>=StartY_2 && Intersection_y<=EndY_2) | (Intersection_y<=StartY_2 && Intersection_y>=EndY_2))){
	            	return true;
	            }
			}
		}	
		return false;
	}
	
	//=======================================END===SEGMENT INTERSECTION==========================================
	
	
	
	//O(log n)
	public int insert(T val) {
		Node<T> newNode=new Node<T>(false,val);				//Create new node with red and value as "val"
		Node<T> next=root; 									
		Node<T> prev=nil;
		while(next!=nil) {
			prev=next;										//Assign previous node as next node
			if(comp.compare(next.get(), val)<0) {			//if next.get() less than val go to left child else go to right child
				next=next.getRight();						//go to the right child
			}else {
				next=next.getLeft();						//go to the left child
			}
		}
		if(prev==nil) {										//if previous node is nil 
			root=newNode;									//Assign new root
		}
		newNode.setParent(prev);							//set parent of "newNode"
		if(prev!=nil) {
			if(comp.compare(prev.get(), val)<0	) {			
				prev.setRight(newNode);						//set right child of previous node as "newNode"
			}else {
				prev.setLeft(newNode);						//set left child of previous node as "newNode"
			}
		}
		newNode.setLeft(nil);								//set for new node left child as nil and right child as nil
		newNode.setRight(nil);
		Balance(newNode);									//fix if we have two red node together
		
		Node<T> pred=predecessor(newNode);					//find the predecessor for the new node
		Node<T> succ=successor(newNode);					//find the successor for the new node
		if(succ!=nil && CheckIntersection(succ.get(),newNode.get())) {	//if successor was found than check intersection between successor and new node
			//Show segment in right order and return the end of the function
			if(((Point) newNode.get()).get_segment().id<((Point) succ.get()).get_segment().id) {
				sb.append(newNode.get().toString()+"\n");	
				sb.append(succ.get().toString()+"\n");				
			}else {
				sb.append(succ.get().toString()+"\n");
				sb.append(newNode.get().toString()+"\n");
			}
			
			return 1;


		}
		if(pred!=nil && CheckIntersection(pred.get(),newNode.get())) { //if predecessor was found than check intersection between predecessor and new node
			//Show segment in right order and return the end of the function
			if(((Point) newNode.get()).get_segment().id<((Point) pred.get()).get_segment().id) {
				sb.append(newNode.get().toString()+"\n");
				sb.append(pred.get().toString()+"\n");	
			}else {
				sb.append(pred.get().toString()+"\n");
				sb.append(newNode.get().toString()+"\n");
			}
			
			return 1;
			
		}
		return 0;
	}
	
	//O(log n)
	public void Balance(Node<T> newNode) {
		while(newNode.getParent().getColor()==false) {						//while parent of newNode is red fix tree
			if(newNode.getParent()==newNode.getParent().getParent().getLeft()) {
				Node<T> uncle=newNode.getParent().getParent().getRight();	//right uncle of newNode
				if(uncle.getColor()==false) {
					uncle.setColor(true);									//set uncle as black
					newNode.getParent().setColor(true);						//set parent as black
					newNode.getParent().getParent().setColor(false);		//set grandparent as red
					newNode=newNode.getParent().getParent();				//Repeat step for grandparent
				}
				else {														//Do following if uncle of newNode is black
					
					//Left Right case when new Node is right child of parent
					
					if(newNode==newNode.getParent().getRight()) {			//if newNode is right child of parent
						newNode=newNode.getParent();						//
						LeftRotate(newNode);								//Do case  left left
						newNode.getParent().setColor(true);					//set parent as black 
						newNode.getParent().getParent().setColor(false);    //set grandparent as red
						RightRotate(newNode.getParent().getParent());		//Right rotation
					}else {													//Do following if new node is left child of parent
						//Case left left
						newNode.getParent().setColor(true);					//set color parent as black
						newNode.getParent().getParent().setColor(false);	//set grandparent as red
						RightRotate(newNode.getParent().getParent());		//Right rotation
					}
				}
			}else {
				Node<T> uncle=newNode.getParent().getParent().getRight();	//left uncle of newNode
				if(uncle.getColor()==false) {
					uncle.setColor(true);									//set uncle as black
					newNode.getParent().setColor(true);						//set parent as black
					newNode.getParent().getParent().setColor(false);		//set grandparent as red
					newNode=newNode.getParent().getParent();				//Repeat step for grandparent
				}else {
					//Case right left
					if(newNode==newNode.getParent().getLeft()) {			//if new node is left child of parent
						newNode=newNode.getParent();						
						RightRotate(newNode);								//right rotation	
						newNode.getParent().setColor(true);					//set color 
						newNode.getParent().getParent().setColor(false);	//
						LeftRotate(newNode.getParent().getParent());		//Left rotation
					}else {													//Do following if new node is right child of parent
						//Case right right
						newNode.getParent().setColor(true);					//set color of parent as black
						newNode.getParent().getParent().setColor(false);	//set grandparent as red
						LeftRotate(newNode.getParent().getParent());		//Right rotation
					}

				}
			}
		}
		root.setColor(true);
	}
	
	//O(1)
	public void LeftRotate(Node<T> parent) {				
		
		Node<T> child=parent.getRight();	
		if(child==nil) return;//right child of parent
		parent.setRight(child.getLeft());					///set right child of parent as left subtree of "child"
		
		if(child.getLeft()!=nil) {							//left child of "child" is not nil
			child.getLeft().setParent(parent);				//set parent of left child of "child"
		}
		if(parent.getParent()==nil) {
			root=child;
		}
		else if(parent==parent.getParent().getLeft()) {		//if parent is left child of grandparent
			parent.getParent().setLeft(child);				//set left child of grandparent as "child"
		}
		else if(parent==parent.getParent().getRight()) { 	//if parent is right child of grandparent 
			parent.getParent().setRight(child);				//set right child of grandparent as "child" 
		}
		child.setParent(parent.getParent());				//set parent of "child" as grandparent
		child.setLeft(parent);								//set left child of "child" as parent
		parent.setParent(child);							//set parent of "parent" as "child"
	}
	
	//O(1)
	public void RightRotate(Node<T> parent) {
		
		Node<T> child=parent.getLeft();	
		if(child==nil) return;//left child of parent
		parent.setLeft(child.getRight());					//set left child of parent as right child of "child"
		
		if(child.getRight()!=nil) {
			child.getRight().setParent(parent);				//set right child of "child" as parent
		}
		if(parent.getParent()==nil) {						//if grandparent equals nil
			root=child;										//Assign root as child
		}
		else if(parent==parent.getParent().getLeft()) {		//if parent is left child of grandparent
			parent.getParent().setLeft(child);				//set left child of grandparent as "child"
		}
		else if(parent==parent.getParent().getRight()) { 	//if parent is right child of grandparent 
			parent.getParent().setRight(child);				//set right child of grandparent as "child" 
		}
		child.setParent(parent.getParent());				//set parent of "child" as grandparent
		child.setRight(parent);								//set right child of "child" as parent
		parent.setParent(child);							//set parent of "parent" as "child"
	}
	
	//O(log n)
	public Node<T> successor(Node<T> y) {					//find the element which follows after "y' 
		if(y==nil) return nil;
		if(y.getRight()!=nil) {								//if y has right child	
			Node<T> next=y.getRight();						//right subtree		
			Node<T> prev=nil;
			while(next!=nil) {							
				prev=next;
				next=next.getLeft();						//go deep to the left	
			}
			return prev;									//return the most left child from right subtree
		}else {												//Do following if y doesn't have right subtree
			Node<T> parent=y.getParent();					//parent of y
			if(y==y.getParent().getLeft()) {				//Do following if y is left child of parent	
				return parent;								//return parent - predecessor
			}else {
				Node<T> child=y;						
				while(child==parent.getRight()) {			//Do following until "child" is  right child of "parent"
					child=parent;
					parent=parent.getParent();				//Go up
				}	
				return parent;								//return parent  - predecessor
			}
		}
	}
	
	//O(log n)
	public Node<T> predecessor(Node<T> y) {					//find the element which situated before "y"
		if(y==nil) return nil;			
		if(y.getLeft()!=nil) {								//Do following if y doesn't have left subtree
			Node<T> next=y.getLeft();						//left subtree
			Node<T> prev=nil;
			while(next!=nil) {
				prev=next;
				next=next.getRight();
			}
			return prev;									//return the most right child from left subtree
		}else {												//Do following if y doesn't have left subtree
			Node<T> parent=y.getParent();					//parent of "child"
			if(y==parent.getRight()) {						//if child if right subtree of "parent"
				return parent;								//return parent - predecessor
			}else {
				Node<T> child=y;
				while(child==parent.getLeft()) {			//Do following until "child" is left child of "parent"
					child=parent;							
					parent=parent.getParent();				//Go up
				}
				return parent;								//return parent  - predecessor
			}
		}
	}
	
	//O(log n)
	public Node<T> find(T val){								//find the value in the tree
		Node<T> next=root; 									
		Node<T> prev=nil;
		boolean check=false;								//if check = false than element didn't found else check ==true than element found
		while(next!=nil) {
			prev=next;										//Assign previous node as next node
			if(comp.compare(prev.get(), val)==0) {			//Element found?
				check=true;									//set check as true
				break;
			}
			else if(comp.compare(next.get(), val)<0) {		//if next.get() less than val go to left child else go to right child
				next=next.getRight();						//go to right child
			}else {
				next=next.getLeft();						//go to left child
			}
		}
		if(check) return prev;								//if element was founded than return element
		else{
			return nil;										//return nothing
		}
	}
	
	//O(log n)
	public int delete(T val) {								//delete element
		Node<T> find=find(val);								//find element in the tree which we want to delete
		if(find==nil) return 0;
		Node<T> succ1=successor(find);						//find the successor and predecessor
		Node<T>	pred1=predecessor(find);
		if(succ1!=nil && pred1!=nil && succ1!=pred1 && CheckIntersection(succ1.get(),pred1.get())) { //Do following if successor and predecessor have intersection 	
			
			//Display segments which have intersection

			if(((Point) succ1.get()).get_segment().id<((Point) pred1.get()).get_segment().id) {
				sb.append(succ1.get().toString()+"\n");
				sb.append(pred1.get().toString()+"\n");	
			}else {
				sb.append(pred1.get().toString()+"\n");
				sb.append(succ1.get().toString()+"\n");
			}

			return 1;
		}
		
		Node<T> child;											
		boolean color=find.getColor();						//color of element which we want to delete
		if(find.getLeft()==nil) {							//Do following if element have right subtree
			child=find.getRight();							//right child of deleting element
			deleteNode(find,find.getRight());				//Delete node "find" 
		}else if(find.getRight()==nil){						//Do following if element have left subtree
			child=find.getLeft();							//left child of deleting element
			deleteNode(find,find.getLeft());				//Delete node "find"
		}else {												//Do following if "find" element have left and right subtree
			Node<T> succ=successor(find);					//Find the sibling of deleting element
			color=succ.getColor();							//Take color of sibling
			child=succ.getRight();							//child of sibling 
			if(succ.getParent()==find) {					//sibling have parent as deleting node
				child.setParent(succ);						//child of sibling will be parent
			}
			else {
				deleteNode(succ,succ.getRight());			//delete node successor
				succ.setRight(find.getRight());				//Set right child of sibling as deleting element which have right child
				succ.getRight().setParent(succ);			//Set parent of right child of sibling as sibling 
			}
			deleteNode(find,succ);							//Delete find element and replace by sibling
			succ.setLeft(find.getLeft());					//set left child of find 
			succ.getLeft().setParent(succ);					//set parent of left  child of sibling  as sibling
			succ.setColor(find.getColor());					//set color of sibling as color of deleting node
		}
		if(color==true) {									//if color of sibling or deleting element was black	
			//Solve double black node problem
			if(child!=nil) {						
			Fix(child);
			}
		}
		return 0;
	}
	
	//O(1)
	public void deleteNode(Node<T> parent, Node<T> child) {			//delete node which is parent and replace for his place node child
		if(parent.getParent()==nil) {								//if parent is root
			root=child;												//assign new root as child of parent	
		}
		else {
			if(parent==parent.getParent().getLeft()) {				//if parent is left child of his parent
				parent.getParent().setLeft(child);					//set left child of his parent as child
			}else {													//id parent is right child of his parent
				parent.getParent().setRight(child);					//set right child of his parent as child
			}
		}
		child.setParent(parent.getParent());						//set parent of child as parent of his parent
	}
	
	//O(log n)
	public void Fix(Node<T> node) {
		if(node==nil) return;
		while(node != root && node.getColor()==true) {				//Do following until double black node is black
			if(node==node.getParent().getLeft()) {					//Do following if double black node is left child of his parent	
				Node<T> brother=node.getParent().getRight();		//brother of black node
				
				if(brother.getColor()==false) {						//if brother has black color
					brother.setColor(true);							//set color of brother as black
					node.getParent().setColor(false);				//set color of parent of brother as red
					LeftRotate(node.getParent());					//Do left rotate
					brother=node.getParent().getRight();			//go to grandparent and repeat steps
				}
				if(brother==nil) return;
				if(brother.getLeft().getColor()==true && brother.getRight().getColor()==true) {		//if brother has left child and right child which is black
					brother.setColor(false);						//set color of brother as red
					node=node.getParent();							//set black node as parent of black node
				}else {												//if brother has left child or right child which is not black
					if(brother.getRight().getColor()==true) {		//if right child of brother is black
						brother.getLeft().setColor(true);			//set left child of brother as black
						brother.setColor(false);					//set color of brother as red
						RightRotate(brother);						//Do right rotate
						brother=node.getParent().getRight();		//set brother as grandparent of brother for doing rotation
					}
					brother.setColor(node.getParent().getColor());	//set color of grandparent of brother or brother as black node parent
					node.getParent().setColor(true);				//set parent of double black node as black
					brother.getRight().setColor(true);				//set color of right child of black node as black
					LeftRotate(node.getParent());					//Do left rotate
					node=root;										//double black is root
				}
			}else {													//Do following if double black node is right child of his parent
				Node<T> brother=node.getParent().getLeft();			//brother of black node

				if(brother.getColor()==false) {						//if brother has black color			
					brother.setColor(true);							//set color of brother as red
					node.getParent().setColor(false);				//set color of parent of brother as black
					RightRotate(node.getParent());					//Do right rotate
					brother=node.getParent().getLeft();				//go to grandparent and repeat steps
				}
				if(brother==nil) return;
				if(brother.getLeft().getColor()==true && brother.getRight().getColor()==true) { //if brother has left child and right child which is black
					brother.setColor(false);						//set color of brother as red
					node=node.getParent();							//set black node as parent of black node
				}else {												//if brother has left child or right child which is not black
					if(brother.getLeft().getColor()==true) {		//if left child of brother is black
						brother.getRight().setColor(true);			//set right child of brother as black
						brother.setColor(false);					//set color of brother as red
						LeftRotate(brother);						//Do left rotate
						brother=node.getParent().getLeft();			//set brother as grandparent of brother for doing rotation
					}
					brother.setColor(node.getParent().getColor());	//set color of grandparent of brother or brother as black node parent
					node.getParent().setColor(true);				//set parent of double black node as black
					brother.getLeft().setColor(true);				//set color of right child of black node as black
					RightRotate(node.getParent());					//Do right rotate
					node=root;										//double black is root
				}
			}
		}
		node.setColor(true);										//set color of black node as black
	}

	//O(log n)
	public void removeMax() {										//remove max element from the tree
		T max=max();												//find max element from tree
		delete(max);												//delete max element
	}
	
	//O(log n)
	public void removeMin() {										//remove min element from the tree
		T min=min();												//find min element from the tree
		delete(min);												//delete min element
	}
	
	//O(log n)
	public T min(){													//find min element from the tree
		Node<T> next=root;											
		Node<T> prev=nil;
		while(next!=nil) {											//Until next element is not empty node
			prev=next;
			next=next.getLeft();									//go to the left child
		}
		return prev.get();											//return min element
	}
	
	//O(log n);
	public T max(){													//find max element from the tree
		Node<T> next=root;						
		Node<T> prev=nil;
		while(next!=nil) {											//Until next element is not empty node
			prev=next;	
			next=next.getRight();									//go to the right child
		}
		return prev.get();											//return max element
	}
	
	//O(n)
	public void display() {											//Display tree in order
		Node<T> x=root;
		inorder(x);													//Recursive function
	}
	//O(n)
    public void inorder(Node<T> x){
        if(x==nil){													//Until x is not empty node
            return;
        }else{
            inorder(x.getLeft());									//go to the left child
            System.out.print(x.get());								//show element and show color of this element
            if(x.getColor()) {
            	System.out.print("B ");
            }else {
            	System.out.print("R ");
            }	
            inorder(x.getRight());									//go to the right child
        }
    }
}


class Array<T>{
	ArrayList<T> arr;												//Array list which contains 
	Array(){
		arr = new ArrayList<T>();									
	}
	public T get(int i) {											//get i element
		return arr.get(i);
	}
	public void add(T val) {										//add element
		arr.add(val);
	}
	public void set(int i, T val) {									//set element
		arr.set(i, val);
	}
	public void show() {											//show elements
		for(int i=0;i<arr.size();i++) {							
			T val=arr.get(i);
			System.out.println(val);
		}
	}
	//===============================START=MERGE SORTING====================================================
	//O(n*log n)
	void MergeSort(int l, int r, Comparator<? super T> comp ) {		//sort array using merge sort algorithm
		if(l>=r) {													//If size of array is one than return
			return;
		}else {														//if size of array is not one than do following
			int mid=(r+l)/2;										//find center of the array
																	//Solve problem by dividing array on two part
			MergeSort(l, mid,comp);									//Divide array on two array from start to center
			MergeSort(mid+1,r,comp);								//and center to end
			Merge(l, mid, r,comp);									//When we define two part of array merge them
		}
	}
	
	void Merge(int l, int m, int r, Comparator<? super T> comp) {	
		ArrayList<T> left=new ArrayList<T>(m-l+1);					//array contains left part of the array
		ArrayList<T> right= new ArrayList<T>(r-m);					//array contains right pray of the array
		for(int z=l;z<=m;z++) {
			left.add(this.arr.get(z));								//add to the left part elements
		}
		for(int z=m+1;z<=r;z++) {
			right.add(this.arr.get(z));								//add to the right part elements
		}
		int i=0;													//index for the left part
		int j=0;													//index for the right part
		int k=l;													//index of the starting point of the given array of size r-l+1
		for(;k<=r;k++) {											//Until we end given array do following
			if(i<m-l+1 && j<r-m) {									//Until we have space in left or right array do following
				if(comp.compare(left.get(i), right.get(j))<0) {		//if elements from left array less than element from right array
					arr.set(k, left.get(i++));						//than put for the given array this element
				}else {												//else if element from right array less than from left array
					arr.set(k, right.get(j++));						//than put for the given array this element
				}
			}else {
				break;							
			}
		}
		while(i<m-l+1) {
			arr.set(k++,left.get(i++));								//put in the given array leave elements from the left array
		}
		while(j<r-m) {
			arr.set(k++,right.get(j++));							//put in the given array leave elements from the right array
		}
	}
	//===============================END=MERGE SORTING====================================================
	
	
/*
	Is it in-place or out-of-place?
	The typical implementation of the Merge algorithm is not in-place because it uses
	additional space for storing elements.Function merge use left and right array 
	which after combines in one sorting array.
	
	Is it stable?
    Merge sort algorithm is stable because elements that have equals value save the same order after sorting.
    Because (comp.compare(left.get(i), right.get(j))<0) we never consider case when two element equals to each other, since
    is not matter for us.
	
 */
	

}
