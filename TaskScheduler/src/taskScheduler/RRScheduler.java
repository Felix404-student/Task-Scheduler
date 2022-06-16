package taskScheduler;

import java.util.LinkedList;

/**
 * @author justinbrown
 * Round Robin Scheduler: advances to next task in Ready Queue after set timer (set by constructor)
 */
public class RRScheduler extends BasicScheduler {
	protected BasicPCB runningProcess;	//The process that is currently running.
	public int counter;
	public int currentCount;
	
	public RRScheduler(int num) {
		readyQ = new LinkedList<BasicPCB>();
		counter = num;
		currentCount = 0;
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
	
	/**
	 * After each line is processed, checks if the current process is done
	 * If not, increments the timer. If time is up, cycles to next process in queue
	 * previous current process is added back to the end of the queue
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
		
		currentCount++;
		
		if (currentCount == counter) {
			readyQ.add(runningProcess);
			dispatch();
		}
	}
	
	/**
	 * Selects the next process to become the running process.
	 * Resets the timer to 0
	 */
	public void dispatch() {
		if(readyQ.isEmpty()) {
			runningProcess = null;
			return;
		}
		runningProcess = readyQ.remove();
		currentCount = 0;
	}
	
	public BasicPCB getRunningProcess() {
		return this.runningProcess;
	}
	
}
