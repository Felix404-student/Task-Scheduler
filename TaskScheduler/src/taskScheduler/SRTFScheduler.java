package taskScheduler;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author justinbrown
 * Shortest Remaining Time First Scheduler: sorts Ready Queue by number of lines of code LEFT
 * Sorts on each process addition, can preempt current process with new process
 */
public class SRTFScheduler extends BasicScheduler {
	protected BasicPCB runningProcess;			//The process that is currently running.
	protected Queue<BasicPCB> readyQ;
	
	public SRTFScheduler() {
		readyQ = new LinkedList<BasicPCB>();
	}

	
	/**
	 * If the queue is empty, just adds the new process to it
	 * If the queue is NOT empty, checks if the new process has shorter time remaining
	 * than the current process, in which case throws current process back into queue
	 * and new process becomes current.
	 * If new process is not shorter, adds to queue.
	 * Calls sortQ to sort the processes still in the queue by lines remaining.
	 * @param p - An instance of a new process
	 */
	public void addProcess(BasicPCB p) {
		totalProcesses++;
		if (readyQ == null) {
			readyQ.add(p);
			return;
		}
		if (runningProcess != null && (runningProcess.getTotalLines() - runningProcess.getProgramCounter())
				- (p.getTotalLines()) > 0) {
			readyQ.add(runningProcess);
			runningProcess = p;
		} else {
			readyQ.add(p);
		}
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
	 * Sorts the LinkedList by lines remaining, then casts back to a queue
	 */
	public void sortQ() {
		LinkedList<BasicPCB> readyList = (LinkedList<BasicPCB>)readyQ;
		
		readyList.sort(new Comparator<BasicPCB>(){
		    @Override
	        public int compare(BasicPCB a,BasicPCB b){
		    	return ((a.getTotalLines() - a.getProgramCounter()) - (b.getTotalLines() - b.getProgramCounter()));
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