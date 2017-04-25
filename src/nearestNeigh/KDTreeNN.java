package nearestNeigh;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.lang.Math;

/**
 * This class is required to be implemented.  Kd-tree implementation.
 *
 * @author Jeffrey, Youhan
 */
public class KDTreeNN implements NearestNeigh
{
    //Tracks the root of each tree
    private TwoDimensionalNode restaurantRoot;
    private TwoDimensionalNode hospitalRoot;
    private TwoDimensionalNode educationRoot;
    //Tracks the 
    private HashMap<String, Integer> visitedNodes;
    int XDIMSPLIT = 1;
    int YDIMSPLIT = 2;
    
    @Override
    public void buildIndex(List<Point> points) 
    {
        List<Point> restaurants = new ArrayList<>();
    	List<Point> hospitals = new ArrayList<>();
    	List<Point> educations = new ArrayList<>();
        //Sort points into respective ArrayList
        for(Point p: points)
        {
            if(p.cat.equals(Category.RESTAURANT))
            {
                restaurants.add(p);
            }
            else if(p.cat.equals(Category.HOSPITAL))
            {
                hospitals.add(p);
            }
            else if(p.cat.equals(Category.EDUCATION))
            {
                educations.add(p);
            }
        }
        //Builds the trees for each respective category
        if(restaurants.size() != 0)
        {
            restaurantRoot = buildTree(restaurants, XDIMSPLIT);
        }
        if(hospitals.size() != 0)
        {
            hospitalRoot = buildTree(hospitals, XDIMSPLIT);
        }
        if(educations.size() != 0)
        {
            educationRoot = buildTree(educations, XDIMSPLIT);
        }
    }
 
    @Override
    public List<Point> search(Point searchTerm, int k) 
    {
        //Closest will be the retuned List of closest points
        List<Point> closest = new ArrayList<Point>();
        TwoDimensionalNode currentNode;
        visitedNodes = new HashMap<String, Integer>();
        Point furthestPoint;
        Point previousPoint;
        long startTime = System.nanoTime();
        if(k > 0)
        {
            //Starts the search at the estimated closest leaf
            currentNode = traverse(getRoot(searchTerm), searchTerm);
            //Adds it to closest
            closest.add(currentNode.point);
            previousPoint = currentNode.point;
            //Traverse backwards
            currentNode = currentNode.previousNode;
            //Prepopulate the closest array
            while(closest.size() != k)
            {
            	//check if we were at the root
            	if(currentNode == null)
            	{
            		break;
            	}
            	//checks if the current node only has one child
            	else if(currentNode.left == null || currentNode.right == null)
            	{
            		closest.add(currentNode.point);
            	}
            	//checks if we have been to the left
            	else if(currentNode.left.point.equals(previousPoint))
            	{
            		int i;
                    //Check if we've been to the right side
            		for(i = 0; i < closest.size(); i++)
            		{
            			if(currentNode.right.point.equals(closest.get(i)))
            			{
            				break;
            			}
            		}
            		//if we have been to both left and right just add the current node to the closest array
            		if(i != closest.size())
            		{
            			closest.add(currentNode.point);
            		}
            		//else traverse the right side and add current node to closest
            		else
            		{
            			currentNode = traverse(currentNode.right, searchTerm);
	            		closest.add(currentNode.point);
            		}
            	}
            	//Checks if we have been to the right
            	else if(currentNode.right.point.equals(previousPoint))
            	{
            		int j;
            		for(j = 0; j < closest.size(); j++)
            		{
            			//Check if we've been to the left side
            			if(currentNode.left.point.equals(closest.get(j)))
            			{
            				break;
            			}
            		}
            		//if we have been to both left and right just add the current node to the closest array
            		if(j != closest.size())
            		{
            			closest.add(currentNode.point);
            		}
            		//else traverse the left side and add current node to closest
            		else
            		{
            			currentNode = traverse(currentNode.left, searchTerm);
	            		closest.add(currentNode.point);
            		}
            	}

            	previousPoint = currentNode.point;
            	currentNode = currentNode.previousNode;
            }
            //Finds the furthest point in the closest array
            furthestPoint = closest.get(0);
            for(Point c: closest)
            {
            	//adds all the points in the closest array into a hashmap tracking visited nodes
            	visitedNodes.put(c.id, 0);
            	if(c.distTo(searchTerm) > furthestPoint.distTo(searchTerm))
            	{
            		furthestPoint = c;
            	}
            }
            //Recursively check the nearestNeigbour on the way back to the root
            closest = checkingNearestNeighbour(closest, currentNode, searchTerm, furthestPoint);
        }
        long endTime = System.nanoTime();
        double estimatedTime = ((double)(endTime - startTime)) / Math.pow(10, 9);
        System.out.println(estimatedTime);
        return closest;
    }

    //Adds a point to the KDTree
    @Override
    public boolean addPoint(Point point) 
    {
        TwoDimensionalNode leaf, node;
        //Checks for duplicate
        if(isPointIn(point))
        {
            return false;
        }
        else
        {
            //Traverses to the closest leaf
            leaf = traverse(getRoot(point), point);
            //Checks the dimsplit type
            if(leaf.xDimSplit())
            {
                //Creates a new node
                node = ((XDimNode)leaf).nextLevelNode(point);
                //Sets left or right depending on the longitude comparison
                if(leaf.splitCoord > point.lon)
                {
                    leaf.left = node;
                }
                else
                {
                	leaf.right = node;
                }
            }
            else
            {
                //Creates a new node
                node = ((YDimNode)leaf).nextLevelNode(point);
                //Sets left or right depending on the latitude comparison
                if(leaf.splitCoord > point.lat)
                {
                    leaf.left = node;
                }
                else
                {
                	leaf.right = node;
                }
            }
            //Set the left and right null
            node.left = null;
            node.right = null;
            //Reference it back to the previous leaf
            node.previousNode = leaf;
            return true;
        }
    }
    
    //Delets the point from the KDTree
    @Override
    public boolean deletePoint(Point point) 
    {
        TwoDimensionalNode nodeToBeDeleted = getNode(getRoot(point), point);
        TwoDimensionalNode newSubRoot;
        //Checks if node exist
        if(nodeToBeDeleted == null)
        {
            return false;
        }
        else
        {
            //Creates a new ArrayList of all nodes that come after the node to be deleted
            List<Point> subTree = new ArrayList<>();
            //Puts all the points into an arraylist
            subTree = getArrayOfSubTree(subTree, nodeToBeDeleted);
            //Checks if the subtree is more than zero
            if(subTree.size() > 0)
            {
            	//Checks if it is a root point
            	if(nodeToBeDeleted.previousNode != null)
            	{
            		//Makes the sub tree depending on the dimsplit of the previousnode
            		if(!nodeToBeDeleted.previousNode.xDimSplit())
		            {
		            	newSubRoot = buildTree(subTree, XDIMSPLIT);
		            }
		            else
		            {
		            	newSubRoot = buildTree(subTree, YDIMSPLIT);
		            }
		            //Sets the previous node left or right to the new subroot
		            if(nodeToBeDeleted.previousNode.left != null && nodeToBeDeleted.previousNode.left.point.equals(nodeToBeDeleted.point))
	            	{
	            		nodeToBeDeleted.previousNode.left = newSubRoot;
	            	}
	            	else if(nodeToBeDeleted.previousNode.right != null && nodeToBeDeleted.previousNode.right.point.equals(nodeToBeDeleted.point))
	            	{
	            		nodeToBeDeleted.previousNode.right = newSubRoot;
	            	}
            	}
            	//Rebuilds whole tree if it is root
            	else
            	{
            		newSubRoot = buildTree(subTree, XDIMSPLIT);
            	}
            	//sets the new sub root's previous node to the currentNode's previousNode
            	newSubRoot.previousNode = nodeToBeDeleted.previousNode;
            	return true;
            }
            //If it is a leaf node
            else
            {
            	//Set the previousNode left or right to null
            	if(nodeToBeDeleted.previousNode.left != null && nodeToBeDeleted.previousNode.left.point.equals(nodeToBeDeleted.point))
            	{
            		nodeToBeDeleted.previousNode.left = null;
            	}
            	else if(nodeToBeDeleted.previousNode.right != null && nodeToBeDeleted.previousNode.right.point.equals(nodeToBeDeleted.point))
            	{
            		nodeToBeDeleted.previousNode.right = null;
            	}
            	return true;
            }
        }
    }

    //Checks if the point is in the KDTree
    @Override
    public boolean isPointIn(Point point) 
    {
        TwoDimensionalNode node = null;
        node = getNode(getRoot(point), point);
        if(node == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    //Recursively check the nearestNeigbour on the way back to the root
    private List<Point> checkingNearestNeighbour(List<Point> closest, TwoDimensionalNode currentNode, Point searchTerm, Point furthestPoint)
    {
    	int i, j;
    	Point boundaryCheck;
    	if(currentNode != null && !visitedNodes.containsKey(currentNode.point.id))
    	{
    		//Checks if current Node is closer to search Term than the furthest in array
            if(currentNode.point.distTo(searchTerm) < furthestPoint.distTo(searchTerm))
	    	{
	    		for(Point c: closest)
	            {
	            	//removes furthest from array list
	            	if(c.equals(furthestPoint))
	            	{
	            		closest.remove(c);
	            		break;
	            	}
	            }
	            //adds the point to the array list
	    		closest.add(currentNode.point);
	    		//Sets the new furthest point
	    		furthestPoint = closest.get(0);
	    		for(Point c: closest)
		        {
		        	if(c.distTo(searchTerm) > furthestPoint.distTo(searchTerm))
		        	{
		        		furthestPoint = c;
		        	}
		        }
	    	}
	    	//Put the ID of the point into the hashmap to track visited nodes
	    	visitedNodes.put(currentNode.point.id, 0);
	    	//Checks if currentNode.left is not null
	    	if(currentNode.left != null)
	    	{
		    	//Checks if we have been to that node
		    	if(!visitedNodes.containsKey(currentNode.left.point.id))
		    	{
		    		//Checks currentNode type
		    		if(currentNode.xDimSplit())
		    		{
		    			//Compute boundary check to see if a closer point may be on the opposite quadrant
		    			boundaryCheck = new Point("test", Category.RESTAURANT, searchTerm.lat, currentNode.splitCoord);
		    			if(boundaryCheck.distTo(searchTerm) < furthestPoint.distTo(searchTerm))
		    			{
		    				currentNode = traverse(currentNode.left, searchTerm);
		    			}
		    		}
		    		else
		    		{
		    			boundaryCheck = new Point("test", Category.RESTAURANT, currentNode.splitCoord, searchTerm.lon);
		    			if(boundaryCheck.distTo(searchTerm) < furthestPoint.distTo(searchTerm))
		    			{
		    				currentNode = traverse(currentNode.left, searchTerm);
		    			}
		    		}
		    		closest = checkingNearestNeighbour(closest, currentNode, searchTerm, furthestPoint);
		    	}
		    	//Checks if currentNode.left is not null
		    	if(currentNode.right != null)
		    	{
		    		//Checks if we have been to that node
		    		if(!visitedNodes.containsKey(currentNode.right.point.id))
		    		{
		    			//Checks currentNode type
		    			if(currentNode.xDimSplit())
		    			{
		    				//Compute boundary check to see if a closer point may be on the opposite quadrant
		    				boundaryCheck = new Point("test", Category.RESTAURANT, searchTerm.lat, currentNode.splitCoord);
		    				if(boundaryCheck.distTo(searchTerm) < furthestPoint.distTo(searchTerm))
		    				{
		    					currentNode = traverse(currentNode.right, searchTerm);
		    				}
		    			}
		    			else
		    			{
		    				boundaryCheck = new Point("test", Category.RESTAURANT, currentNode.splitCoord, searchTerm.lon);
		    				if(boundaryCheck.distTo(searchTerm) < furthestPoint.distTo(searchTerm))
		    				{
		    					currentNode = traverse(currentNode.right, searchTerm);
		    				}
		    			}
		    			closest = checkingNearestNeighbour(closest, currentNode, searchTerm, furthestPoint);
		    		}
		    	}
	    	}
    	}
    	if(currentNode != null)
    	{
    		currentNode = currentNode.previousNode;
            closest = checkingNearestNeighbour(closest, currentNode, searchTerm, furthestPoint);
    	}
    	return closest;
    }

    //Builds the tree
    private TwoDimensionalNode buildTree(List<Point> points, int dimsplit)
    {
    	TwoDimensionalNode root = null;
    	List<Point> leftArray = new ArrayList<>();
    	List<Point> rightArray = new ArrayList<>();
    	int index;
    	//Checks if points are null
    	if(points != null)
    	{
    		//gets the mid point (Value may be truncated)
    		index = ((points.size() - 1)/2);
    		//Sorts them depending on the dimsplit type
    		if(dimsplit == XDIMSPLIT)
    		{
    			//Sorts them by longitude
            	points = sortByAscendingLon(points);
            	//Sets the root of the tree
            	root = new XDimNode(points.get(index));
    		}
    		else if(dimsplit == YDIMSPLIT)
    		{
    			points = sortByAscendingLat(points);
    			root = new YDimNode(points.get(index));
    		}
            //Sets the previousNode to null
            root.previousNode = null;
            //Splits the array into two (left and right side)
            //Everything before the index
        	for(int i = 0; i < index; i++)
            {
                leftArray.add(points.get(i));
            }
            //Passes them to two different recursive functions
            for(int j = index + 1; j < points.size(); j++)
            {
                rightArray.add(points.get(j));
            }
            root = settingLeftNode(root, leftArray);
            root = settingRightNode(root, rightArray);
            return root;
    	}
    	return root;
    }

    //Sets the left node of the root
    private TwoDimensionalNode settingLeftNode(TwoDimensionalNode root, List<Point> points)
    {
        TwoDimensionalNode leftNode;
        List<Point> leftArray = new ArrayList<>();
        List<Point> rightArray = new ArrayList<>();
        int index;
        //Checks if array is not empty
        if(points.size() > 0)
        {
            //Gets median index
            index = ((points.size() - 1)/2);
            //Checks DimSplit type
            if(root.xDimSplit())
            {
                points = sortByAscendingLat(points);
                leftNode = ((XDimNode)root).nextLevelNode(points.get(index));
            }
            else
            {
                points = sortByAscendingLon(points);
                leftNode = ((YDimNode)root).nextLevelNode(points.get(index));
            }
            //Populates left and right array with everything but index
            for(int i = 0; i < index; i++)
            {
                leftArray.add(points.get(i));
            }
            for(int j = index + 1; j < points.size(); j++)
            {
                rightArray.add(points.get(j));
            }
            //ties current node to previousnode
            leftNode.previousNode = root;
            //recursively build the rest of the tree
            leftNode = settingLeftNode(leftNode, leftArray);
            leftNode = settingRightNode(leftNode, rightArray);
            //makes the previousNode.right this node.
            root.left = leftNode;
            return root;
        }
        else
        {
            root.left = null;
            return root;
        }
    }

    //Sets the right node of the root
    private TwoDimensionalNode settingRightNode(TwoDimensionalNode root, List<Point> points)
    {
        TwoDimensionalNode rightNode;
        List<Point> leftArray = new ArrayList<>();
        List<Point> rightArray = new ArrayList<>();
        int index;
        //Checks if array is not empty
        if(points.size() > 0)
        {
            //Gets median index
            index = ((points.size() - 1)/2);
            //Checks DimSplit type
            if(root.xDimSplit())
            {
                points = sortByAscendingLat(points);
                rightNode = ((XDimNode)root).nextLevelNode(points.get(index));
            }
            else
            {
                points = sortByAscendingLon(points);
                rightNode = ((YDimNode)root).nextLevelNode(points.get(index));
            }
            //Populates left and right array with everything but index
            for(int i = 0; i < index; i++)
            {
                leftArray.add(points.get(i));
            }
            for(int j = index + 1; j < points.size(); j++)
            {
                rightArray.add(points.get(j));
            }
            //ties current node to previousnode
            rightNode.previousNode = root;
            //recursively build the rest of the tree
            rightNode = settingLeftNode(rightNode, leftArray);
            rightNode = settingRightNode(rightNode, rightArray);
            //makes the previousNode.right this node.
            root.right = rightNode;
            return root;
        }
        else
        {
            root.right = null;
            return root;
        }
    }

    //Gets all the children of the node that we want to delete
    private List<Point> getArrayOfSubTree(List<Point> points, TwoDimensionalNode root)
    {
        if(root != null)
        {
        	points = getArrayOfRestOfSubTree(points, root.left);
        	points = getArrayOfRestOfSubTree(points, root.right);
        }
        return points;
    }

    //recursive part of the getArrayOfSubTree function
    private List<Point> getArrayOfRestOfSubTree(List<Point> points, TwoDimensionalNode root)
    {
        if(root != null)
        {
            points.add(root.point);
            points = getArrayOfRestOfSubTree(points, root.left);
            points = getArrayOfRestOfSubTree(points, root.right);
        }
        return points;
    }

    //Returns to root of the individual trees depending on the point category
    private TwoDimensionalNode getRoot(Point point)
    {
        if(point.cat.equals(Category.RESTAURANT))
        {
            return restaurantRoot;
        }
        else if(point.cat.equals(Category.HOSPITAL))
        {
            return hospitalRoot;
        }
        else if(point.cat.equals(Category.EDUCATION))
        {
            return educationRoot;
        }
        return null;
    }

    //Returns the node that is equal to the search node
    private TwoDimensionalNode getNode(TwoDimensionalNode root, Point point)
    {
    	if(root != null)
    	{
    		if(root.point.equals(point))
	    	{
	    		return root;
	    	}
	    	else
	    	{
	    		if(root.xDimSplit())
	    		{
	    			if(root.splitCoord > point.lon)
	    			{
	    				return getNode(root.left, point);
	    			}
	    			else
	    			{
	    				return getNode(root.right, point);
	    			}
	    		}
	    		else
	    		{
	    			if(root.splitCoord > point.lat)
	    			{
	    				return getNode(root.left, point);
	    			}
	    			else
	    			{
	    				return getNode(root.right, point);
	    			}
	    		}
	    	}
    	}
    	return null;
    }

    //Traverses to the expected closest leaf of the tree to the search point
    private TwoDimensionalNode traverse(TwoDimensionalNode root, Point point)
    {
    	//Starts at root
    	TwoDimensionalNode closestPoint = root;
    	//Checks if root is null
        if(closestPoint != null)
        {
            //Checks which dimension did it split on
            if(closestPoint.xDimSplit())
            {
                //compares lon if the point was split on lon
                if(closestPoint.splitCoord > point.lon)
                {
                	
                	//Checks if left side is not null
                	if(closestPoint.left != null)
                    {
                		//Traverse if it is not null
                		closestPoint = traverse(closestPoint.left, point);
                	}
                }
                else
                {
                	//Checks if right side is not null
                	if(closestPoint.right != null)
                    {
                		//Traverse if it is not null
                		closestPoint = traverse(closestPoint.right, point);
                	}
                }
            }
            else
            {
                //Compares lon if the point was split on lat
                if(closestPoint.splitCoord > point.lat)
                {
                    //Checks if left side is not null
                    if(closestPoint.left != null)
                    {
                    	//Traverse if it is not null
                    	closestPoint = traverse(closestPoint.left, point);
                    }
                }
                else
                {
                	//Checks if right side is not null
                	if(closestPoint.right != null)
                    {
                		//Traverse if it is not null
                		closestPoint = traverse(closestPoint.right, point);
                	}
                }
            }
        }
        return closestPoint;
    }

    //Sorting algorithm for Points in ascending latitude value
    private List<Point> sortByAscendingLat(List<Point> points)
    {
        List<Point> sortedByLat = new ArrayList<>();
        int sizeOfArray = points.size();
        for(int i = 0; i < sizeOfArray; i++)
        {
            Point lowestLat = points.get(0);
            for(Point p: points)
            {
                if(p.lat < lowestLat.lat)
                {
                    lowestLat = p;
                }
            }
            sortedByLat.add(lowestLat);
            points.remove(lowestLat);
        }
        return sortedByLat;
    }

    //Sorting algorithm for points in ascending longitude value
    private List<Point> sortByAscendingLon(List<Point> points)
    {
        List<Point> sortedByLon = new ArrayList<>();
        int sizeOfArray = points.size();
        for(int i = 0; i < sizeOfArray; i++)
        {
            Point lowestLon = points.get(0);
            for(Point p: points)
            {
                if(p.lon < lowestLon.lon)
                {
                    lowestLon = p;
                }
            }
            sortedByLon.add(lowestLon);
            points.remove(lowestLon);
        }
        return sortedByLon;
    }
}
