package taskScheduler;

/**
 * @author justinbrown
 * A scheduling strategy Based on First Come First Serve (FCFS)
 */
import java.util.*;
public class BasicScheduler {
	protected Queue<BasicPCB> readyQ;	//The data structure that contains processes scheduled based on the scheudler's policies.
	protected BasicPCB runningProcess;	//The process that is currently running.
	protected int tickCount;		//Current time in ticks.
	protected int totalProcesses;	//Total number of processes.
	protected int waitingTimeSum;	//Total amount of waiting time.
	
	/**
	 * Default Constructor used to set up the instance variables.
	 * More importantly to set up the implementation of the Ready Queue.
	 * As the type Queue in Java is an interface, it is a non-constructable
	 * type. It must use a collection like an array, linked list, heap, etc.
	 * to create the data structure.
	 */
	public BasicScheduler() {
		readyQ = new LinkedList<BasicPCB>();
	}
	
	/**
	 * Represents the abstract CPU Tick or Burst.
	 * Consider 1 tick as one unit of time, and for
	 * each 1 tick the current process' needs to be
	 * updated by the method updateRunningProcess().
	 * See further details of that method.
	 */
	public void tick() {
		tickCount++;
		updateRunningProcess();
	}
	
	/**
	 * This is assumed to be called by the method tick(),
	 * and it updates the information about the current
	 * process. Each Tick counts as one unit of time, and 
	 * for each tick the process' program counter advances by 1.
	 * If the process has completed then the dispatcher must be
	 * call to pick the next process. Also depending on the preemptive
	 * nature of the scheduler, the dispatcher may have to be called
	 * before the process has completed. In this event make sure the 
	 * incomplete, former process is added back to the ready queue.
	 */
	public void updateRunningProcess() {
		if(runningProcess == null) {
			dispatch();
			return;
		}
		runningProcess.nextLine();
		
		if(runningProcess.hasCompleted()) {
			runningProcess.setCompletionTick(tickCount);
			waitingTimeSum += (runningProcess.getCompletionTick() - runningProcess.getArrivalTick());
			dispatch();
		}
	}
	
	/**
	 * Selects the next process to become the running process.
	 * This is assumed to be overridden for each type of
	 * process scheduler.
	 */
	public void dispatch() {
		if(readyQ.isEmpty()) {
			runningProcess = null;
			return;
		}
		runningProcess = readyQ.remove();
	}
	
	/**
	 * Used for verification
	 * @return Tick gets the amount of ticks
	 */
	public int getTickCount() {
		return tickCount;
	}

	/**
	 * Determines the scheduler has finished its job. Once the
	 * ready queue is empty there is nothing more the schedule,
	 * so it's done.
	 * @return whether or not the ready queue is empty thus is done
	 */
	public boolean isDone() {
		return readyQ.isEmpty() && runningProcess == null;
	}
	
	/**
	 * Depending on if the scheduler is preemptive or not this may have to call the dispatcher after
	 * adding the new process.
	 * @param p - An instance of a new process
	 */
	public void addProcess(BasicPCB p) {
		totalProcesses++;
		readyQ.add(p);
	}
	
	public double getAverageWaitingTime() {
		return (double)waitingTimeSum / totalProcesses;
	}
	
	public BasicPCB getRunningProcess() {
		return this.runningProcess;
	}
}
