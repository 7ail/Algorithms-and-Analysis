import java.util.Random;
import java.util.ArrayList;
import java.lang.Math;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public final class scenario1generation
{
	public static void main(String... aArgs){
		double[] latitude = new double[31100];
		double[] longitude = new double[31100];
		Random frandom = new Random();
		double generatedValue;
		double MEAN = -38.0f;
		double VARIANCE = 2.0f;
		int i;
		int idx;
		for(idx = 0; idx < 31000;)
		{
			generatedValue = MEAN + frandom.nextGaussian() * VARIANCE;
			generatedValue = Math.round(generatedValue * 10000000000.0);
			generatedValue = generatedValue/10000000000.0;
			for(i = 0; i < latitude.length; i++)
			{
				if(generatedValue == latitude[i])
				{
					System.out.println("DUPLICATE");
					break;
				}
			}
			if(i == latitude.length)
			{
				latitude[idx] = generatedValue;
				idx++;
			}
		}

		MEAN = 144.0f;
		VARIANCE = 4.0f;
		for(idx = 0; idx < 31000;)
		{
			generatedValue = MEAN + frandom.nextGaussian() * VARIANCE;
			generatedValue = Math.round(generatedValue * 10000000000.0);
			generatedValue = generatedValue/10000000000.0;
			for(i = 0; i < longitude.length; i++)
			{
				if(generatedValue == longitude[i])
				{
					System.out.println("DUPLICATE");
					break;
				}
			}
			if(i == longitude.length)
			{
				longitude[idx] = generatedValue;
				idx++;
			}
		}
		int count = 1;

		//PUTS first 25000 into a separate file
		try(PrintWriter out = new PrintWriter("25000.txt"))
		{
			for(int j = 0; j < 25000; j++)
			{
				out.println("id" + count + " " + "RESTAURANT " + latitude[j] + " " + longitude[j]);
				count++;
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
		}

		//PUTS next 5000 into a separate file
		try(PrintWriter out = new PrintWriter("5000.txt"))
		{
			for(int j = 25000; j < 30000; j++)
			{
				out.println("id" + count + " " + "RESTAURANT " + latitude[j] + " " + longitude[j]);
				count++;
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
		}

		//PUTS next 1000 int a sperate file
		try(PrintWriter out = new PrintWriter("1000.txt"))
		{
			for(int j = 30000; j < 31000; j++)
			{
				out.println("id" + count + " " + "RESTAURANT " + latitude[j] + " " + longitude[j]);
				count++;
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
		}
	}
}