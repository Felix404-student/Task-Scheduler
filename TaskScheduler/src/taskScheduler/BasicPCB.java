package taskScheduler;

/**
 * @author justinbrown
 * Used to represent a basic process control block
 */
public class BasicPCB implements Comparable<BasicPCB>{
	private int id;				//Process ID
	private int programCounter;	//Current line of code
	private int totalLines;		//Total lines of code the process will run
	private int priority;		//Priority of the process
	private int arrivalTick;	//When the process arrived
	private int completionTick;	//When the process has completed
	
	public BasicPCB(int id, int aLineAmt, int anArrivalTick) {
		this.setId(id);
		this.programCounter = 0;
		this.setArrivalTick(anArrivalTick);
		this.setTotalLines(aLineAmt);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int aPriority) {
		this.priority = aPriority;
	}
	
	public int getArrivalTick() {
		return arrivalTick;
	}

	public void setArrivalTick(int arrivalTick) {
		this.arrivalTick = arrivalTick;
	}

	public int getCompletionTick() {
		return completionTick;
	}

	public void setCompletionTick(int completionTick) {
		this.completionTick = completionTick;
	}

	public int getTotalLines() {
		return totalLines;
	}

	public void setTotalLines(int totalLines) {
		this.totalLines = totalLines;
	}
	
	public int getProgramCounter() {
		return this.programCounter;
	}
	
	public int compareTo(BasicPCB p) {
		return priority - p.getPriority();
	}
	
	public boolean hasCompleted() {
		return totalLines - programCounter <=0;
	}
	
	public void nextLine() {
		programCounter++;
	}
	
	public String toString() {
		return "Process ID: "+this.id +" Program Counter: "+this.programCounter +" Total Lines: "+this.totalLines;
	}
	
}