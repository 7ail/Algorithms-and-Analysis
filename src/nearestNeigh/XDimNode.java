package nearestNeigh;

public class XDimNode extends TwoDimensionalNode
{
	//Constructor
	public XDimNode(Point point)
	{
		super(point.lon, point);
	}

	//Tracks if it has been x-dim or y-dim splitted
	@Override
	public boolean xDimSplit()
	{
		return true;
	}

	//Creates the next level node
	public YDimNode nextLevelNode(Point point)
	{
		return new YDimNode(point);
	}
}
