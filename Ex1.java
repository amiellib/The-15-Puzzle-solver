
import java.io.*;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.*;
public class Ex1
{

	/**
	 * here we will get a text file and make from it the puzzle
	 * @param  args
	 */
	public static void main(String args[])
	{
		HashMap<Integer, String> cost = new HashMap<Integer, String>();
		long startTime=0,endTime=0;
		int empty_spot=0;
		int temp,counter=0;
		boolean bool;
		//read the file
		try
		{
			Scanner linReader = new Scanner(new File("input.txt"));
			String algo = linReader.nextLine();
			String time = linReader.nextLine();
			String open = linReader.nextLine();
			if(open.equals("with open"))
				bool=true;
			else
				bool=false;
			String size=linReader.nextLine();
			String[] size_in_string = size.split("x");
			int size_n=Integer.parseInt(size_in_string[0]);
			int size_m=Integer.parseInt(size_in_string[1]);
			//sort black tiles
			String temp_black=linReader.nextLine();
			temp_black=temp_black.replaceAll(" ", "");//if not empty
			temp_black=temp_black.replace("Black:","");
			String[] black=temp_black.split(",");
			//sort red tiles
			String temp_red=linReader.nextLine();
			temp_red=temp_red.replaceAll(" ", "");//if not empty
			temp_red=temp_red.replace("Red:","");
			String[] red=temp_red.split(",");
			int[][]matrix=new int[size_n][size_m];
			for(int i=0;i<size_n;i++)
			{
				String raw_data=linReader.nextLine();
				String[] raw=raw_data.split(",");
				for(int j=0;j<size_m;j++)
				{
					counter++;
					matrix[i][j]=i*(size_m)+j;
					if(raw[j].equals("_"))
					{
						empty_spot=i*(size_m)+j+1;
					}
					else
						matrix[i][j]=Integer.parseInt(raw[j]);
					cost.put(matrix[i][j],"1");
				}
			}
			for(int r=0;r<red.length;r++)
			{
				if(!red[r].equals(""))
					cost.put(Integer.parseInt(red[r]),"30");
			}
			for(int b=0;b<black.length;b++)
			{
				if(!black[b].equals(""))
				{
					cost.put(Integer.parseInt(black[b]),"Black");
					counter--;
				}
			}
			matrix[(empty_spot-1)/size_m][(empty_spot-1)%size_m]=size_n*size_m;
			cost.put(size_n*size_m,"_");
			linReader.close();
			Status status;
			if(algo.equals("DFBnB"))
				status=new Status(algo,time,size_n,size_m,cost,matrix,empty_spot,counter,bool);
			else
				status=new Status(algo,time,size_n,size_m,cost,matrix,empty_spot,15,bool);
			Algorithm a=new Algorithm(status,black);
			a.run_algo();
		}

		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
}
