package nearestNeigh;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

/**
 * This class is required to be implemented.  Naive approach implementation.
 *
 * @author Jeffrey, Youhan
 */
public class NaiveNN implements NearestNeigh
{
    //list divided by category
    private List<Point> restaurants = new ArrayList<Point>();
    private List<Point> hospitals = new ArrayList<Point>();
    private List<Point> educations = new ArrayList<Point>();

    @Override
    public void buildIndex(List<Point> points) 
    {
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
    }

    @Override
    public List<Point> search(Point searchTerm, int k) 
    {
        List<Point> closest = new ArrayList<>();
        //ArrayList to be returned by the function
        if(searchTerm.cat.equals(Category.RESTAURANT))
        {
        	return naiveApproach(restaurants, searchTerm, k);
        }
        else if(searchTerm.cat.equals(Category.HOSPITAL))
        {
            return naiveApproach(hospitals,searchTerm, k);
        }
        else if(searchTerm.cat.equals(Category.EDUCATION))
        {
        	return naiveApproach(educations, searchTerm, k);
        }
        return null;
    }

    private List<Point> naiveApproach(List<Point> category, Point searchTerm, int k)
    {
    	List<Point> closest = new ArrayList<Point>();
    	long startTime = System.nanoTime();
        Point furthest = category.get(0);
        for(int i = 0; i < k; i++)
    	{
    		closest.add(category.get(i));
    		if(closest.get(i).distTo(searchTerm) > furthest.distTo(searchTerm))
    		{
    			furthest = closest.get(i);
    		}
    	}
    	for(int j = k; j < category.size(); j++)
        {
            if(category.get(j).distTo(searchTerm) < furthest.distTo(searchTerm))
        	{
        		closest.add(category.get(j));
        		for(int i = 0; i < k; i++)
        		{
        			if(closest.get(i).equals(furthest))
        			{
        				closest.remove(i);
        				break;
        			}
        		}
        		furthest = closest.get(0);
        		for(int i = 0; i < k; i++)
        		{
        			if(closest.get(i).distTo(searchTerm) > furthest.distTo(searchTerm))
        			{
        				furthest = closest.get(i);
        			}
        		}
        	}
        }
        long endTime = System.nanoTime();
        double estimatedTime = ((double)(endTime - startTime)) / Math.pow(10, 9);
        System.out.println(estimatedTime);
        return closest;
    }

    @Override
    public boolean addPoint(Point point) 
    {
        // To be implemented.
        if(point.cat.equals(Category.RESTAURANT))
        {
        	if(isPointIn(point))
            {
                return false;
            }
        	restaurants.add(point);
        	return true;
        }
        else if(point.cat.equals(Category.HOSPITAL))
        {
        	if(isPointIn(point))
            {
                return false;
            }
        	hospitals.add(point);
        	return true;
        }
        else if(point.cat.equals(Category.EDUCATION))
        {
        	if(isPointIn(point))
            {
                return false;
            }
        	educations.add(point);
        	return true;
        }
        return false;
    }

    @Override
    public boolean deletePoint(Point point) 
    {
        if(point.cat.equals(Category.RESTAURANT))
        {
	        for(Point r: restaurants)
	        {
	            if(r.equals(point))
	            {
	                restaurants.remove(r);
	                return true;
	            }
	        }
	        return false;
	    }
	    else if(point.cat.equals(Category.HOSPITAL))
        {
	        for(Point h: hospitals)
	        {
	            if(h.equals(point))
	            {
	                hospitals.remove(h);
	                return true;
	            }
	        }
	        return false;
	    }
	    else if(point.cat.equals(Category.EDUCATION))
        {
	        for(Point e: educations)
	        {
	            if(e.equals(point))
	            {
	                educations.remove(e);
	                return true;
	            }
	        }
	        return false;
	    }
        return false;
    }

    @Override
    public boolean isPointIn(Point point) 
    {
        // To be implemented.
        if(point.cat.equals(Category.RESTAURANT))
        {
	        return isPointInCategory(restaurants, point);
	    }
	    else if(point.cat.equals(Category.HOSPITAL))
        {
	        return isPointInCategory(hospitals, point);
	    }
        else if(point.cat.equals(Category.EDUCATION))
        {
	        return isPointInCategory(educations, point);
	    }
        return false;
    }

    private boolean isPointInCategory(List<Point> category, Point point)
    {
    	for(Point c: category)
    	{
    		if(c.equals(point))
    		{
    			return true;
    		}
    	}
    	return false;
    }

}
