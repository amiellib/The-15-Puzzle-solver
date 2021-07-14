import java.io.*;
import java.lang.Math;
import java.util.*;

/**
 *here the program run the algorithm and make a text file output.txt
 */
public class Algorithm
{
	private Status st;
	private int counter;
	private long start_time;
	private long end_time;
	private String[] black;
	private String text;

	/**
	 * normal constructor Algorithm
	 * @param st    [description]
	 * @param black [description]
	 */
	public Algorithm(Status st,String[] black)
	{
		this.st=st;
		this.counter=0;
		this.start_time=0;
		this.end_time=0;
		this.black=black;
		this.text="";

	}
	/**
	 * AstarComparator class
	 */
	static class AstarComparator implements Comparator<Status>
	{
		@Override
		/**
		 * compare the statuses to see what status need to stay in the Pqueue
		 * @param  st         check status
		 * @param  from_p_que status from stack
		 * @return
		 * 		 */
		public  int compare(Status st,Status from_p_que)
		{
			return st.get_pay()+st.heuristic()-from_p_que.get_pay()-from_p_que.heuristic();
		}
	}
	/**
	 * solving the puzzle and send it to make a txt file
	 */
	public void run_algo()
	{
		this.start_time=System.currentTimeMillis();
		if(this.check_black())
		{
			switch(this.st.get_algo())
			{
			case "DFID":this.st=DFID(this.st);
			break;
			case "BFS":
				this.st=BFS();
				break;
			case "DFBnB":
				this.st=DFBnB(this.st);
				break;
			case "A*":
				this.st=A_star();
				break;
			case "IDA*":
				this.st=IDA_star(this.st);
				break;
			default:
				System.out.println("wrong algorithm");
			}
			if(this.st!=null)
				good_print(this.st);
			else
				fail_path();
		}
		else
			black_fail();
	}
	/**
	 * check if all black tiles are in good place
	 * if there are not it return false to give bad black tile outcome
	 * @return boolean
	 */

	private boolean check_black()
	{
		if(!this.black[0].isEmpty())
			for(String str: this.black)
			{
				int item=Integer.parseInt(str)-1;
				if(this.st.get_matrix(item/this.st.get_m(),item%this.st.get_m())==item+1)
					continue;
				else
				{
					return false;
				}
			}
		return true;
	}
	/**
	 * run BFS algorithm to solve the puzzle
	 * @return the goal status or null if no path
	 */
	private Status BFS()
	{
		HashSet<String> visited = new HashSet<String>();
		Queue<Status> que1=new LinkedList<Status>();
		que1.add(this.st);
		while(!que1.isEmpty())
		{
			if(this.st.get_open())
				System.out.println(que1.toString());
			Status st_que = que1.remove();
			visited.add(st_que.intToString());
			this.counter++;
			if(st_que.goal())
				return st_que;
			else
				for(Status next_status:st_que.go())
				{
					if(!visited.contains(next_status.intToString()))
					{
						que1.add(next_status);
					}
				}
		}
		return null;
	}
	/**
	 * run DFID algorithm to solve the puzzle
	 * @param  st the first status of the puzzle
	 * @return   the goal status or null if no path
	 */
	private  Status DFID(Status st)
	{
		this.counter++;
		Hashtable<String,Integer> visitedD = new Hashtable<String,Integer>(100);
		for (int h=1;h<Integer.MAX_VALUE;h++ )
		{
			Status result=Limited_DFS(st,h,visitedD);
			if(!result.get_bool())
			{
				return result;
			}
		}
		return null;
	}
	/**
	 * Recursive part of the algorithm DFID
	 * @param  st       currnt node to check
	 * @param  limit    deep to go
	 * @param  visitedD the visited Hashtable
	 * @return  Status
	 */
	private  Status Limited_DFS(Status st,int limit,Hashtable<String,Integer> visitedD)
	{

		if(st.goal())
			return st;
		else
			if(limit==0)
			{
				st.set_bool(true);
				return st;
			}
			else
			{
				visitedD.put(st.intToString(),0);
				boolean  is_cut_off=false;
				for(Status next_st :st.go())
				{
					if(!visitedD.contains(next_st.intToString()))
					{
						Status result=Limited_DFS(next_st,limit-1,visitedD);
						this.counter++;
						if(result.get_bool())
						{
							is_cut_off=true;
						}
						else
						{
							return result;
						}
					}
				}
				if(is_cut_off)
				{
					st.set_bool(true);
					return st;
				}
				else
				{
					st.set_bool(false);
					return st;
				}
			}
	}
	/**
	 * run IDA* algorithm to solve the puzzle
	 * @param  st first node of the puzzle
	 * @return  the goal status or null if no path
	 */
	private  Status IDA_star(Status st)
	{
		Hashtable<String,Status> visitedH = new Hashtable<String,Status>(100);
		Stack<Status> stack = new Stack<Status>();
		Status check_st;
		int t=st.heuristic();
		boolean with_open=st.get_open();
		this.counter++;
		for(int h=1;h<Integer.MAX_VALUE;h++)
		{
			st.set_bool(false);
			int minF=Integer.MAX_VALUE;
			visitedH.put(st.intToString(),st);
			stack.push(st);
			while(!stack.isEmpty())
			{
				check_st=stack.pop();
				if(check_st.get_bool())
				{
					visitedH.remove(check_st.intToString());
				}
				else
				{
					check_st.set_bool(true);
					stack.push(check_st);

					for(Status next_st :check_st.go())
					{

						int g=next_st.heuristic()+next_st.get_pay();
						if(with_open)
							System.out.println(stack.toString());
						this.counter++;
						if(g>t)
						{
							minF=Math.min(minF,g);
							continue;
						}
						if(visitedH.contains(next_st.intToString()))
						{
							if(visitedH.get(next_st.intToString()).get_bool())
							{
								continue;
							}
							else
							{
								if(visitedH.get(next_st.intToString()).get_pay()+visitedH.get(next_st.intToString()).heuristic()>g)
								{
									visitedH.remove(next_st.intToString());
									Stack<Status> temp = new Stack<Status>();
									boolean del=false;
									while (!del)
									{
										if(stack.peek().intToString().equals(next_st.intToString()))
										{
											stack.pop();
											del=true;
										}
										else
										{
											temp.push(stack.pop());
										}
									}
									while(!temp.isEmpty())
									{

										stack.push(temp.pop());
									}
								}
								else
								{
									continue;
								}
							}
						}
						if(next_st.goal())
						{
							return next_st;
						}
						stack.push(next_st);
						visitedH.put(next_st.intToString(),next_st);
					}
				}
			}
			t=minF;
		}
		return null;
	}
	/**
	 * run DFBnB algorithm to solve the puzzle
	 * @param  st  first status of the puzzle
	 * @return    the goal status or null if no path
	 */
	private  Status DFBnB(Status st)
	{
		Hashtable<String,Status> visitedBnB = new Hashtable<String,Status>(100);
		Stack<Status> stack = new Stack<Status>();
		stack.push(st);
		Status result=null;
		int t=st.get_DFBnB();//need to check
		Status next_st;
		boolean with_open=st.get_open();
		counter++;
		while(!stack.isEmpty())
		{
			//    st.set_bool(false);
			if(with_open)
				System.out.println(stack.toString());
			Status check_st=stack.pop();
			if(check_st.get_bool())
			{
				visitedBnB.remove(check_st.intToString());
			}
			else
			{
				check_st.set_bool(true);
				if(check_st.go()!=null)
				{
					ArrayList<Status> arr_st=check_st.go();
					AstarComparator comp =new AstarComparator();
					Collections.sort(arr_st,comp);
					for(int i=0;i<arr_st.size();i++)
					{
						counter++;
						next_st=arr_st.get(i);
						if(next_st.get_pay()+next_st.heuristic()>=t)
						{
							while(arr_st.size()>i)
							{
								arr_st.remove(i);
							}
						}
						else
							if(visitedBnB.contains(next_st.intToString()))
							{
								if(visitedBnB.get(next_st.intToString()).get_bool())
									arr_st.remove(i);
								else
								{
									if(next_st.get_pay()+next_st.heuristic()<=visitedBnB.get(next_st.intToString()).get_pay()+visitedBnB.get(next_st.intToString()).heuristic())
										arr_st.remove(i);
									else
									{
										Stack<Status> temp = new Stack<Status>();
										boolean del=false;
										while (!del)
										{
											if(stack.peek().intToString().equals(next_st.intToString()))
											{
												stack.pop();
												del=true;
											}
											else
											{
												temp.push(stack.pop());
											}
										}
										while(!temp.isEmpty())
										{

											stack.push(temp.pop());
										}
										visitedBnB.remove(next_st.intToString());
									}
								}
							}
							else
								if(next_st.goal())
								{
									t=next_st.get_pay();
									result=next_st;
									while(i<arr_st.size())
									{
										arr_st.remove(i);
									}
								}

					}
					for(int i=arr_st.size()-1;i>=0;i--)
					{
						visitedBnB.put(arr_st.get(i).intToString(),arr_st.get(i));
						stack.push(arr_st.get(i));
					}
				}
				else
					continue;
			}
		}
		return result;
	}
	/**
	 * run A* algorithm to solve the puzzle
	 * @return the goal status or null if no path
	 */
	private  Status A_star()
	{
		//HashSet<String> visited = new HashSet<String>(100);
		Status st_pQueue;
		Hashtable<String,Integer> visitedQ = new Hashtable<String,Integer>(100);
		AstarComparator comp =new AstarComparator();
		PriorityQueue <Status> pQueue = new PriorityQueue <Status> (100,comp);
		pQueue.add(this.st);
		visitedQ.put(this.st.intToString(),this.st.get_pay()+this.st.heuristic());
		this.counter++;
		while(!pQueue.isEmpty())
		{
			if(this.st.get_open())
				System.out.println(pQueue.toString());
			st_pQueue=pQueue.poll();
			visitedQ.put(st_pQueue.intToString(),st_pQueue.get_pay()+st_pQueue.heuristic());
			if(st_pQueue.goal())
			{
				return st_pQueue;
			}
			for(Status next_status:st_pQueue.go())
			{
				if(!visitedQ.containsKey(next_status.intToString()))
				{
					this.counter++;
					pQueue.add(next_status);
				}
				else
				{
					String int_to_string=next_status.intToString();
					if(visitedQ.containsKey(int_to_string)&&visitedQ.get(int_to_string)>next_status.get_pay()+next_status.heuristic())
					{
						pQueue.remove(next_status);
						pQueue.add(next_status);
						visitedQ.replace(int_to_string,next_status.get_pay()+next_status.heuristic());
					}
				}
			}
		}
		return null;
	}
	private void black_fail()
	{
		this.text="no path"+"\n"+"Num: 1";
		make_txt();
	}
	/**
	 * if the algorithm find a path it will get here and make a text String for the outpput.txt
	 * @param st the info of the path+the cost
	 */
	private void good_print(Status st)
	{
		String path=(st.get_path().replaceFirst("-",""));
		this.text=path+"\n"+"Num: "+this.counter+"\n"+"Cost: "+st.get_pay();
		if(st.get_time().equals("with time"))
		{
			this.end_time=System.currentTimeMillis();
			this.text=this.text+"\n"+(this.end_time - this.start_time)/1000.0+" seconds";
		}
		make_txt();
	}
	/**
	 * if the algorithm did not find a valid path it will go the a fail text outcome
	 */
	private void fail_path()
	{
		this.text="no path"+"\n"+"Num: "+this.counter;
		make_txt();
	}
	/**
	 * make the output.txt file
	 */
	public void make_txt()
	{
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("output.txt"));
			out.write(this.text);
			out.close();
		}
		catch (IOException e)
		{
			System.out.println("Exception");
		}

	}

}
