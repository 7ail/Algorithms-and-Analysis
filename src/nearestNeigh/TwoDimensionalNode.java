package nearestNeigh;
import java.util.ArrayList;
//Abstract class defining the Node class
public abstract class TwoDimensionalNode
{
	
	//Stores the x-dim or y-dim split value
	public double splitCoord;
	//Stores the actual point itself
	public Point point;
	//Stores the parent node to this node
	public TwoDimensionalNode previousNode;
	//Stores the left child of this node
	public TwoDimensionalNode left;
	//Stores the right child of this node
	public TwoDimensionalNode right;

	//Constructor
	public TwoDimensionalNode (double splitCoord, Point point)
	{
		this.splitCoord = splitCoord;
		this.point = point;
	}

	//returns left node
	public TwoDimensionalNode getLeft()
	{
		return left;
	}

	//returns right node
	public TwoDimensionalNode getRight()
	{
		return right;
	}

	//returns parent node
	public TwoDimensionalNode getPrevious()
	{
		return previousNode;
	}
	
	public abstract boolean xDimSplit();
}