//Operating Systems Assignment 3
//Sebastian Hadley c3349742
//Main class used to call other functions and get arguements.
import java.util.*;
import java.io.*;
class A3
{
  ArrayList<Process> Processes;
  ArrayList<Integer> process_pages;
  int quantum,frames;
  int counter;
  //Creates an object of A3.
  public static void main(String[] args)
  {
    A3 runner = new A3();
    runner.run(args);
  }
  //To reference non static variables.
  private void run(String[] args)
  {
    counter = 0;
    Processes = new ArrayList<Process>();
    process_pages = new ArrayList<Integer>();
    int total_files = args.length - 2;
    int file_index = args.length;
    frames = Integer.parseInt(args[0]);
    quantum = Integer.parseInt(args[1]);
    for(int i = 2; i < file_index; i++)
    {
      counter++;
      loadFile(args[i],total_files,counter);
    }
    RoundRobin sort = new RoundRobin(Processes,frames,quantum);

  }
  //Loasd the files from the arg in command line.
  private void loadFile(String arg,int total_files,int counter)
  {
    String line_data;
    int c = counter;
    try
    {
      Scanner input_stream = new Scanner(new File(arg));
      while(input_stream.hasNext())
      {
        line_data = input_stream.next();
        if(line_data.length() > 1)
        {
          continue;
        }
        process_pages.add(Integer.parseInt(line_data));
      }
      input_stream.close();
    }
    catch(Exception E)
    {
      System.out.println("Error");
    }
    if(process_pages.size() > 50)
    {
      System.out.println("Maximum of 50 pages allowed within an individual process.");
      System.exit(0);
    }
    int[] add_data = new int[process_pages.size()];
    for(int i = 0; i < process_pages.size(); i++)
    {
      add_data[i] = process_pages.get(i);
    }
    int split_frames;
    split_frames = frames/total_files;
    Processes.add(new Process(add_data,c,split_frames,quantum,arg));
    process_pages.clear();
  }
}
