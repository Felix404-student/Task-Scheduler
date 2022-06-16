package taskScheduler;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author justinbrown
 * Shortest Job First Scheduler: sorts Ready Queue by total lines of code
 * Sorts Queue on each process addition
 */
public class SJFScheduler extends BasicScheduler{
	protected Queue<BasicPCB> readyQ;
	protected BasicPCB runningProcess;	//The process that is currently running.
	
	public SJFScheduler() {
		readyQ = new LinkedList<BasicPCB>();
	}
	
	/**
	 * Adds process to queue
	 * Calls sortQ to sort readyQ based on total lines left
	 * @param p - An instance of a new process
	 */
	public void addProcess(BasicPCB p) {
		totalProcesses++;
		readyQ.add(p);
		sortQ();
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
	 * Casts readyQ down to a LinkedList from a Queue
	 * Sorts the LinkedList by total lines left, then casts back to a queue
	 */
	public void sortQ() {
		LinkedList<BasicPCB> readyList = (LinkedList<BasicPCB>)readyQ;
		readyList.sort(new Comparator<BasicPCB>(){
		    @Override
	        public int compare(BasicPCB a,BasicPCB b){
		    	return (a.getTotalLines() - b.getTotalLines());
	        }
	    });
		Queue<BasicPCB> aQ = new LinkedList<BasicPCB>(readyList);
		readyQ = aQ;
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
	
	
	public BasicPCB getRunningProcess() {
		return this.runningProcess;
	}
	
}