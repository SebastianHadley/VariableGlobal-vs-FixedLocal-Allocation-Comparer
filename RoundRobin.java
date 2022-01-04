//RoundRobin.java
//Sebastian Hadley c3349742
//Class used to sort the processes using arrayLists of the processes, and is responsible for outputting data.
import java.util.ArrayList;
class RoundRobin
{
  ArrayList<Process> Processes;
  boolean[] faulted;
  int index,frames,quantum,current_time,finished,free_frames;
  public RoundRobin(ArrayList<Process> p,int f,int q)
  {
    finished = 0;
    Processes = new ArrayList<Process>();
    Processes = p;
    free_frames = f;
    frames = f;
    quantum = q;
    faulted = new boolean[Processes.size()];
    for(int i =0; i < faulted.length; i ++)
    {
      faulted[i] = false;
    }
    index = 0;
    fixedSort();
    variableSort();
  }

  //Fixed Local Replacement:
  public void fixedSort()
  {
    for(int i =0; i < Processes.size(); i++)
    {
      loadMemory();
      switchProcess();
    }
    index = Processes.size() - 1;
    current_time = 6;
    //Loops through sorting process
    while(finished < Processes.size())
    {
      int timestart = current_time;
      int faultcount = 0;
      switchProcess();
      for(int i = 0; i < Processes.size(); i ++)
      {
        if(checkFault(i) == true)
        {
          faulted[i] = false;
        }
        else
        {
          faulted[i] = true;
        }
        if(faulted[i] == true)
        {
          faultcount++;
        }
      }
      if(Processes.get(index).getDone() == true)
      {
        continue;
      }
      if(faultcount == Processes.size() - finished)
      {
        current_time++;
        continue;
      }
      else if(faulted[index] == false)
      {
        loadMemory();
        if(checkFault(index) == false)
        {
          continue;
        }
        current_time = current_time + Processes.get(index).doPage(current_time);
        if(Processes.get(index).getDone() == true)
        {
          setDone();
          finished++;
          continue;
        }
        if(current_time - timestart < quantum)
        {
          if(Processes.get(index).getDone() == false)
          {
            loadMemory();
          }
        }
      }
    }
    System.out.println();
    System.out.println("FIFO - Fixed-Local Replacement:");
    System.out.println("PID"+"\t" +"Process Name"+"\t"+"TurnAround Time"+"\t"+"# Faults"+"\tFault Times");
    for(int i = 0; i < Processes.size(); i++)
    {
      Processes.get(i).setFaultString();
      System.out.println(Processes.get(i).getPID()+"\t"+Processes.get(i).getTitle()+"\t"+ Processes.get(i).getTATime()+"\t\t"+Processes.get(i).getTotalFaults()+"\t\t"+Processes.get(i).getFaultString());
    }
    System.out.println();
    return;
  }

//Variable Global Replacement:
  public void variableSort()
  {
    finished = 0;
    current_time = 0;
    index = 0;
    for(int i = 0; i < Processes.size(); i++)
    {
      Processes.get(i).startGlobalScope();
    }
    for(int i = 0; i < Processes.size(); i++)
    {
      loadFreeMemory();
      free_frames--;
      switchProcess();
    }
    index = Processes.size() - 1;
    current_time = 6;
    while(finished < Processes.size())
    {
      int timestart = current_time;
      int faultcount = 0;
      switchProcess();
      for(int i = 0; i < Processes.size(); i ++)
      {
        if(checkFault(i) == true)
        {
          faulted[i] = false;
        }
        else
        {
          faulted[i] = true;
        }
        if(faulted[i] == true)
        {
          faultcount++;
        }
      }
      if(Processes.get(index).getDone() == true)
      {
        continue;
      }
      if(faultcount == Processes.size() - finished)
      {
        current_time++;
        continue;
      }
      else if(faulted[index] == false)
      {
        int temp = loadFreeMemory();
        if(temp == 1)
        {
          free_frames--;
        }
        if(checkFault(index) == false)
        {
          continue;
        }
        current_time = current_time + Processes.get(index).doPage(current_time);
        if(Processes.get(index).getDone() == true)
        {
          setDone();
          free_frames = free_frames + Processes.get(index).getFrames();
          finished++;
          continue;
        }
        if(current_time - timestart < quantum)
        {
          if(Processes.get(index).getDone() == false)
          {
            loadFreeMemory();
            temp = loadFreeMemory();
            if(temp == 1)
            {
              free_frames--;
            }
          }
        }
      }
    }
    System.out.println();
    System.out.println("FIFO - Variable-Global Replacement:");
    System.out.println("PID"+"\t" +"Process Name"+"\t"+"TurnAround Time"+"\t"+"# Faults"+"\tFault Times");
    for(int i = 0; i < Processes.size(); i++)
    {
      Processes.get(i).setFaultString();
      System.out.println(Processes.get(i).getPID()+"\t"+Processes.get(i).getTitle()+"\t"+ Processes.get(i).getTATime()+"\t\t"+Processes.get(i).getTotalFaults()+"\t\t"+Processes.get(i).getFaultString());
    }
    System.out.println();
    return;
  }


//Switches the Processes in the Round Robin.
public void switchProcess()
{
  if(index == Processes.size()-1)
  {
    index = 0;
  }
  else
  {
    index++;
  }
  return;
}

//Checks if a process is blocked/faulted:
public boolean checkFault(int i)
{
  return Processes.get(i).getReady(current_time);
}
//Sets a process to complete.
public void setDone()
{
  Processes.get(index).isDone(current_time);
}
//Loads a page into a processes main memory.
public void loadMemory()
{
  faulted[index] = true;
  Processes.get(index).fillMemory(current_time);
}
//Variable Version of load memory.
public int loadFreeMemory()
{
  faulted[index] = true;
  return Processes.get(index).variableMemory(current_time,free_frames);
}

}
