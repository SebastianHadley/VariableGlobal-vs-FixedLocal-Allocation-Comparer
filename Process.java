//Process.java
//Sebastian Hadley c3349742
//Used to define an object process.
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
public class Process
{
  private int total_pages;
  private ArrayList<Integer> page_list;
  private Queue<Integer> frames_list;
  private ArrayList<Integer> fault_times;
  private boolean done;
  private String process_name,faults;
  private int process_no,totalframes;
  private ArrayList<Integer> frame_indexes;
  private int freetime,page_index,pagedone,frames_done,quantum,ta_time,c_time,frames;
  public Process(int[] p,int process,int frames,int q,String name)
  {
    page_index = 0;
    quantum = q;
    faults = "";
    freetime = 0;
    done = false;
    process_name = name;
    frame_indexes = new ArrayList<Integer>();
    page_list = new ArrayList<Integer>();
    frames_list = new LinkedList<Integer>();
    fault_times = new ArrayList<Integer>();
    totalframes = frames;
    process_no = process;
    pagedone = 0;
    for(int i = 0; i < p.length; i++)
    {
      page_list.add(p[i]);
    }
    total_pages = page_list.size();
  }
  //------Getters------//
  public boolean getReady(int time)
  {
    if(freetime <= time)
    {
      return true;
    }
    else
    return false;
  }
  public int getPID()
  {
    return process_no;
  }

  public boolean getDone()
  {
    if(pagedone == total_pages)
    {
      return true;
    }
    else
    return false;
  }

  public String getFaultString()
  {
    return faults;
  }

  public int getCTime()
  {
    return c_time;

  }

  public String getTitle()
  {
    return process_name;
  }

  public int getTATime()
  {
    return ta_time;
  }

  public int getFrames()
  {
    return frames_list.size();
  }

  public int getTotalFaults()
  {
    return fault_times.size();
  }

  //------Setters------//
  public void startGlobalScope()
  {
    page_index = 0;
    frames_done =0;
    faults = "";
    freetime = 0;
    done = false;
    frames_list.clear();
    fault_times.clear();
    pagedone = 0;
  }
  public void addFrameIndex(int index)
  {
    return;
  }

  public void processFault(int time)
  {
    fault_times.add(time);
    int x = time;
    freetime = x + 6;
  }

  public void setFaultString()
  {
    faults = "{0,";
    if(fault_times.size() == 1)
    {
      faults ="{0}";
      return;
    }
    for(int i = 1; i < fault_times.size()-1;i++)
    {
      faults = faults+" "+fault_times.get(i)+",";
    }
    faults = faults+" "+fault_times.get(fault_times.size()-1);
    faults = faults+"}";
  }
  //sets completion time for a specific process.
  public void isDone(int time)
  {
    c_time = time;
    ta_time = time;
    done = true;
  }
  //Completes A page
  public int doPage(int time)
  {
    int t = time;
    int count = 0;
    pagedone++;
    frames_done++;
    if(frames_done == total_pages)
    {
      page_index++;
      return 1;
    }
    while(frames_list.contains(page_list.get(page_index+1)))
    {
      count++;
      pagedone++;
      page_index++;
      frames_done++;
      if(frames_done == page_list.size())
      {
        break;
      }
      if(count == quantum-1)
      {
        page_index++;
        return count+1;
      }
    }
    page_index++;
    return count+1;
  }

  //Allocates a page to a frame.
  public void fillMemory(int t)
  {
    if(frames_list.contains(page_list.get(page_index)))
    {
      return;
    }
    processFault(t);
    if(frames_list.size() == totalframes)
    {
      frames_list.poll();
    }
    frames_list.add(page_list.get(page_index));
  }


  public int variableMemory(int t,int frame_free)
  {
    if(frames_list.contains(page_list.get(page_index)))
    {
      return 0;
    }
    processFault(t);
    if(frame_free <= 0)
    {
      frames_list.poll();
      frames_list.add(page_list.get(page_index));
      return 0;
    }
    else
    {
      frames_list.add(page_list.get(page_index));
      return 1;
    }
  }
}
