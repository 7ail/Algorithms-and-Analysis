package nearestNeigh;

public class YDimNode extends TwoDimensionalNode
{
	//Constructor
	public YDimNode(Point point)
	{
		super(point.lat, point);
	}

	//Tracks if it has been x-dim or y-dim splitted
	@Override
	public boolean xDimSplit()
	{
		return false;
	}

	//Creates the next level node
	public XDimNode nextLevelNode(Point point)
	{
		return new XDimNode(point);
	}
}
