import java.io.*;
import java.lang.Math;
import java.util.*;

/**
 *this class is how the puzzle and the info of it will be
 */
public class Status
{
	private int[][] matrix;
	private int empty_spot;
	private String algo;
	private String time;
	private boolean open;
	private int n;
	private int m;
	private int pay;
	private String path;
	private HashMap<Integer, String> cost = new HashMap<Integer, String>();
	private char last_move;
	private boolean bool;
	private int DFBnB;
	/**
	 * copy constractour
	 * @param algo          name of the algorithm to use
	 * @param time          to show time at the end or not
	 * @param n             size row
	 * @param m             size column
	 * @param cost          hasmmap cost of the costs
	 * @param matrix        the matrix
	 * @param empty_spot    where the empty spot is
	 * @param DFBnB_counter the counter if DFBnB is the algorithm to see if more then 12 tiles are not black and set the max
	 * @param open          if to print while running the algorithm
	 */
	public Status(String algo,String time,int n,int m,HashMap<Integer, String> cost,int[][] matrix,int empty_spot,int DFBnB_counter ,boolean open)
	{
		if(DFBnB_counter>12)
			this.DFBnB=Integer.MAX_VALUE;
		else
		{
			//as you said in mail i wanted to put heuristic*10 should work for all 12! is really big
			this.DFBnB=1;
			for(int i=2;i<=DFBnB_counter;i++)
				this.DFBnB*=i;
		}
		this.time=time;
		this.algo=algo;
		this.n=n;
		this.m=m;
		this.cost=cost;
		this.matrix=matrix;
		this.empty_spot=empty_spot-1;
		this.pay=0;
		this.path="";
		this.last_move='0';
		this.cost=cost;
		this.bool=false;
		this.open=open;
	}
	/**
	 *
	 * @param st        currnt status
	 * @param direction direction to move the tile to empty spot
	 */
	public Status(Status st,char direction)
	{
		this.DFBnB=st.DFBnB;
		this.time=st.time;
		this.bool=false;
		this.n=st.n;
		this.m=st.m;
		this.algo=st.algo;
		this.cost=st.cost;
		this.matrix=new int[st.n][st.m];
		switch(direction)
		{
		case 'U':
			this.last_move='U';
			this.empty_spot=st.empty_spot+st.m;
			break;
		case 'D':
			this.last_move='D';
			this.empty_spot=st.empty_spot-st.m;
			break;
		case 'R':
			this.last_move='R';
			this.empty_spot=st.empty_spot-1;
			break;
		case 'L':
			this.last_move='L';
			this.empty_spot=st.empty_spot+1;
			break;
			//making sure that you will not try to crash my program
		default:
			this.empty_spot=st.empty_spot;
		}
		for(int i=0;i<st.n;i++)
			for(int j=0;j<st.m;j++)
				this.matrix[i][j]=st.matrix[i][j];
		int temp_num= this.matrix[(this.empty_spot)/this.m][(this.empty_spot)%this.m];
		this.matrix[(this.empty_spot)/this.m][(this.empty_spot)%this.m]=this.matrix[(st.empty_spot)/this.m][(st.empty_spot)%this.m];
		this.matrix[(st.empty_spot)/this.m][(st.empty_spot)%this.m]=temp_num;
		this.path=st.path+"-"+st.matrix[(this.empty_spot)/this.m][(this.empty_spot)%this.m]+direction;
		//making sure that you will not try to crash my program
		if(this.cost.get(this.matrix[(this.empty_spot)/this.m][(this.empty_spot)%this.m]).equals("_"))
			this.pay=st.pay+Integer.parseInt(this.cost.get(st.matrix[(this.empty_spot)/this.m][(this.empty_spot)%this.m]));
		else
			this.pay=st.pay;
	}
	/**
	 * use manhattan heuristic on the status
	 * @return manhattan heuristic result
	 */
	public int heuristic()
	{
		int x,y,h=0;
		for(int i=0;i<this.n;i++)
			for(int j=0;j<this.m;j++)
			{

				if(!cost.get(matrix[i][j]).equals("_")&&!cost.get(matrix[i][j]).equals("Black"))
				{

					x=Math.abs((matrix[i][j]-1)/this.m-i);
					y=Math.abs((matrix[i][j]-1)%this.m-j);

					h+=(x+y)*(Integer.parseInt(cost.get(matrix[i][j])));
				}
			}
		return h;
	}
	/**
	 * check if the currnt status matrix is goal
	 * @return boolean
	 */
	public  boolean goal()
	{
		for(int i=0;i<this.n;i++)
			for(int j=0;j<this.m;j++)
				if(this.matrix[i][j]!=i*(this.m)+j+1)
					return false;
		return true;
	}
	/**
	 * check if the move is vaild to check
	 * @return ArrayList of the vaild status after a move for check
	 */
	public ArrayList <Status> go()
	{
		ArrayList <Status> st=new ArrayList<Status>();
		if(this.empty_spot%this.m!=this.m-1 && this.last_move!='R')
			if(!this.cost.get(this.matrix[(this.empty_spot)/this.m][(this.empty_spot)%this.m+1]).equals("Black"))
				st.add(new Status(this,'L'));
		if((this.empty_spot)/this.m<this.n-1 && this.last_move!='D')
			if(!this.cost.get(this.matrix[(this.empty_spot)/this.m+1][(this.empty_spot)%this.m]).equals("Black"))
				st.add(new Status(this,'U'));
		if(this.empty_spot%this.m!=0 && this.last_move!='L')
			if(!this.cost.get(this.matrix[(this.empty_spot)/this.m][(this.empty_spot)%this.m-1]).equals("Black"))
				st.add(new Status(this,'R'));
		if(this.empty_spot/this.m!=0 && this.last_move!='U')
			if(!this.cost.get(this.matrix[(this.empty_spot)/this.m-1][(this.empty_spot)%this.m]).equals("Black"))
				st.add(new Status(this,'D'));
		return st;
	}
	/**
	 *  get cutoff/isout check
	 * @return this.bool
	 */
	public boolean get_bool()
	{
		return this.bool;
	}
	/**
	 * set cutoff/isout check
	 * @param bool what to swap cutoff/isout to
	 */
	public void set_bool(boolean bool)
	{
		this.bool=bool;
	}
	/**
	 * create unique string of the matrix
	 * @return String
	 */
	public String intToString()
	{
		String str="";
		for(int i=0;i<this.n;i++)
			for(int j=0;j<this.m;j++)
				str+=this.matrix[i][j]+"-";
		return str;
	}
	/**
	 * get the cost till now
	 * @return int of the pay
	 */
	public int get_pay()
	{
		return this.pay;
	}
	/**
	 * get the path to get to this status
	 * @return string of the path
	 */
	public String get_path()
	{
		return this.path;
	}
	/**
	 * give you the index of the empty tile
	 * @return empty_spot
	 */
	public int get_empty_spot()
	{
		return this.empty_spot;
	}
	/**
	 * get the algorithm
	 * @return String of the algorithm
	 */
	public String get_algo()
	{
		return this.algo;
	}
	/**
	 * get the size of the column
	 * @return int
	 */
	public int get_m()
	{
		return this.m;
	}
	/**
	 * get the size of the row
	 * @return int
	 */
	public int get_n()
	{
		return this.n;
	}
	/**
	 * return the tile numer in a the given place
	 * @param  n row
	 * @param  m column
	 * @return   int
	 */
	public int get_matrix(int n,int m)
	{
		return this.matrix[n][m];
	}
	/**
	 * if to show time at the output file
	 * @return String
	 */
	public String get_time()
	{
		return this.time;
	}
	/**
	 * if to print when the algorithm run
	 * @return boolean
	 */
	public boolean get_open()
	{
		return this.open;
	}
	/**
	 * make a string of the matrix
	 * @return String
	 */
	public String toString()
	{
		return this.intToString();
	}
	public int get_DFBnB()
	{
		return this.DFBnB;
	}
}
