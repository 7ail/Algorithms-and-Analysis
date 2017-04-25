import java.util.Random;
import java.util.ArrayList;
import java.lang.Math;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public final class scenario2generation
{
	public static void main(String... aArgs)
	{
		double[] latitude = new double[31000];
		double[] longitude = new double[31000];
		Random frandom = new Random();
		double generatedValue;
		double MEAN = -38.0f;
		double VARIANCE = 2.0f;
		int i;
		int j;
		int idx;
		for(idx = 0; idx < 26000;)
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
		for(idx = 0; idx < 26000;)
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

		int[] index1 = new int[1000];
		int[] index2 = new int[1000];
		int[] index3 = new int[1000];
		int[] index4 = new int[1000];
		int[] index5 = new int[1000];

		for(i = 0; i < 1000;)
		{
			j = 0;
			int randomInt = frandom.nextInt(1250);
			for(j = 0; j < index1.length; j++)
			{
				if(index1[j] == randomInt)
				{
					break;
				}
			}
			if(j == index1.length)
			{
				index1[i] = randomInt;
				i++;
			}
		}

		for(i = 0; i < 1000;)
		{
			j = 0;
			int randomInt = frandom.nextInt(1500);
			for(j = 0; j < index2.length; j++)
			{
				if(index2[j] == randomInt)
				{
					break;
				}
			}
			if(j == index2.length)
			{
				index2[i] = randomInt;
				i++;
			}
		}

		for(i = 0; i < 1000;)
		{
			j = 0;
			int randomInt = frandom.nextInt(2000);
			for(j = 0; j < index3.length; j++)
			{
				if(index3[j] == randomInt)
				{
					break;
				}
			}
			if(j == index3.length)
			{
				index3[i] = randomInt;
				i++;
			}
		}

		for(i = 0; i < 1000;)
		{
			j = 0;
			int randomInt = frandom.nextInt(6000);
			for(j = 0; j < index4.length; j++)
			{
				if(index4[j] == randomInt)
				{
					break;
				}
			}
			if(j == index4.length)
			{
				index4[i] = randomInt;
				i++;
			}
		}

		for(i = 0; i < 1000;)
		{
			j = 0;
			int randomInt = frandom.nextInt(26000);
			for(j = 0; j < index5.length; j++)
			{
				if(index5[j] == randomInt)
				{
					break;
				}
			}
			if(j == index5.length)
			{
				index5[i] = randomInt;
				i++;
			}
		}

		//PUTS first 250 int separatefile
		count = 1;
		try(PrintWriter out = new PrintWriter("Scenario2Test1.txt"))
		{
			for(j = 0; j < 250; j++)
			{
				out.println("id" + count + " " + "RESTAURANT " + latitude[j] + " " + longitude[j]);
				count++;
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
		}

		count = 251;
		try(PrintWriter out = new PrintWriter("Scenario2Test1.in"))
		{
			for(j = 250; j < 1250; j++)
			{
				out.println("A " + "id" + count + " " + "RESTAURANT " + latitude[j] + " " + longitude[j]);
				count++;
			}
			for(j = 0; j < index1.length; j++)
			{
				out.println("D " + "id" + index1[j] + " RESTAURANT " + latitude[index1[j] - 1] + " " + longitude[index1[j] - 1]);
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
		}

		count = 1;
		try(PrintWriter out = new PrintWriter("Scenario2Test2.txt"))
		{
			for(j = 0; j < 500; j++)
			{
				out.println("id" + count + " " + "RESTAURANT " + latitude[j] + " " + longitude[j]);
				count++;
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
		}

		count = 501;
		try(PrintWriter out = new PrintWriter("Scenario2Test2.in"))
		{
			for(j = 500; j < 1500; j++)
			{
				out.println("A " + "id" + count + " " + "RESTAURANT " + latitude[j] + " " + longitude[j]);
				count++;
			}
			for(j = 0; j < index2.length; j++)
			{
				out.println("D " + "id" + index2[j] + " RESTAURANT " + latitude[index2[j] - 1] + " " + longitude[index2[j] - 1]);
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
		}

		count = 1;
		try(PrintWriter out = new PrintWriter("Scenario2Test3.txt"))
		{
			for(j = 0; j < 1000; j++)
			{
				out.println("id" + count + " " + "RESTAURANT " + latitude[j] + " " + longitude[j]);
				count++;
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
		}

		count = 1001;
		try(PrintWriter out = new PrintWriter("Scenario2Test3.in"))
		{
			for(j = 1000; j < 2000; j++)
			{
				out.println("A " + "id" + count + " " + "RESTAURANT " + latitude[j] + " " + longitude[j]);
				count++;
			}
			for(j = 0; j < index3.length; j++)
			{
				out.println("D " + "id" + index3[j] + " RESTAURANT " + latitude[index3[j] - 1] + " " + longitude[index3[j] - 1]);
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
		}

		count = 1;
		try(PrintWriter out = new PrintWriter("Scenario2Test4.txt"))
		{
			for(j = 0; j < 5000; j++)
			{
				out.println("id" + count + " " + "RESTAURANT " + latitude[j] + " " + longitude[j]);
				count++;
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
		}

		count = 5001;
		try(PrintWriter out = new PrintWriter("Scenario2Test4.in"))
		{
			for(j = 5000; j < 6000; j++)
			{
				out.println("A " + "id" + count + " " + "RESTAURANT " + latitude[j] + " " + longitude[j]);
				count++;
			}
			for(j = 0; j < index4.length; j++)
			{
				out.println("D " + "id" + index4[j] + " RESTAURANT " + latitude[index4[j] - 1] + " " + longitude[index4[j] - 1]);
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
		}

		count = 1;
		try(PrintWriter out = new PrintWriter("Scenario2Test5.txt"))
		{
			for(j = 0; j < 25000; j++)
			{
				out.println("id" + count + " " + "RESTAURANT " + latitude[j] + " " + longitude[j]);
				count++;
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
		}

		count = 25001;
		try(PrintWriter out = new PrintWriter("Scenario2Test5.in"))
		{
			for(j = 25000; j < 26000; j++)
			{
				out.println("A " + "id" + count + " " + "RESTAURANT " + latitude[j] + " " + longitude[j]);
				count++;
			}
			for(j = 0; j < index5.length; j++)
			{
				out.println("D " + "id" + index5[j] + " RESTAURANT " + latitude[index5[j] - 1] + " " + longitude[index5[j] - 1]);
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
		}
	}
}