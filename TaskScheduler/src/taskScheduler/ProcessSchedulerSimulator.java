package taskScheduler;

/**
 * @author justinbrown
 * The Front End that Simulates 4 Process Scheduling Strategies
 */
import java.util.*;
import java.io.*;

public class ProcessSchedulerSimulator {
	private Queue<BasicPCB> jobQ; 		//Simulates the job queue
	private BasicScheduler scheduler;	//The scheduler to be simulated
	private PrintWriter fileWriter;
	public static final int MAX_JOBS = 100;
	public static final int MAX_LINE_AMT = 100;
	public static final int ARRIVAL_TIME = 10;
	public static final int MAX_TICKS = 10000;
	/**
	 * Constructor for the process simulator
	 * @param aScheduler - sets the scheduler strategy
	 * @param aFilename - sets the file name to be printed
	 */
	public ProcessSchedulerSimulator(BasicScheduler aScheduler, String aFilename) {
		scheduler = aScheduler;
		jobQ = new LinkedList<BasicPCB>();
		try {
			fileWriter = new PrintWriter(new FileOutputStream(aFilename));
		}
		catch(Exception e) {
			println(e.toString());
		}
	}
	
	/**
	 * Creates a random queue of jobs / processes. If the parameter hasFixedSeed is true then it will always
	 * choose the same random values. Otherwise it will always be semi-random.
	 * @param hasFixedSeed
	 */
	public void populateRandomJobQ(boolean hasFixedSeed) {
		Random r;
		if(hasFixedSeed)
			r = new Random(100);//Fixed seed is 100 for debugging
		else
			r = new Random();//Non-fixed seed
		
		int currArrivalTick = 0;//Keeps a running total of arrival times to best spread out the jobs
		for(int i=0;i<MAX_JOBS;i++) {
			int lineAmt = r.nextInt(MAX_LINE_AMT)+1;//Offset by 1 was .nextInt starts from 0
			BasicPCB newJob = new BasicPCB(i,lineAmt,currArrivalTick);			
			jobQ.add(newJob);
			currArrivalTick += r.nextInt(ARRIVAL_TIME)+1;//Same offset same reason, but this updates the NEXT arrival time
		}
	}
	
	/**
	 * Runs the current scheduler strategy by adding processes from the job queue to the scheduler's
	 * ready queue, and simulating the processor's tick. 
	 */
	public void runScheduler() {
		//Starts with the first job by removing it initially from the job queue
		BasicPCB currJob = jobQ.remove();
		//Simulates the processor's tick
		for(int i=0;i<MAX_TICKS;i++) {
			//When a current job isn't null and its randomly assigned arrival time matches
			//The tick at variable i then a new process is added from the job queue to the ready queue
			if(currJob != null && i == currJob.getArrivalTick()) {
				println("ADDED JOB: "+currJob);
				scheduler.addProcess(currJob);
				if(!jobQ.isEmpty())
					currJob = jobQ.remove();//Set the current job and Wait for the next arrival
			}
			//Progresses the scheduler's time unit (tick) by one
			scheduler.tick();
			//Ends the for-loop early in case it has processed all jobs
			if(jobQ.isEmpty() && scheduler.isDone())//Both the Job Queue and the Ready Queue are empty
				break;
			//Prints the current info
			println("----------------------\nTICK: "+i+"\n----------------------");
			println("Current Running Process: " + scheduler.getRunningProcess());

		}
		//Prints out the average waiting time for the scheduler
		println("Average Wait Time: "+scheduler.getAverageWaitingTime());
		fileWriter.close();//Close the output stream once the scheduler has completed
	}
	
	/**
	 * Prints parameter s both to console and to a file
	 * @param s
	 */
	public void println(String s) {
		System.out.println(s);
		fileWriter.println(s);
	}
	
	public static void main(String[] args) {
		//Run the simulator for the 4 strategies
		ProcessSchedulerSimulator p = new ProcessSchedulerSimulator(new BasicScheduler(),"FCFS.txt");
		p.populateRandomJobQ(true);//This assumes a fixed random seed for debugging. Set to "false" for a non-fixed seed.
		p.runScheduler();
		
		p = new ProcessSchedulerSimulator(new SJFScheduler(),"SJF.txt");
		p.populateRandomJobQ(true);//This assumes a fixed random seed for debugging. Set to "false" for a non-fixed seed.
		p.runScheduler();
		
		p = new ProcessSchedulerSimulator(new SRTFScheduler(),"SRTF.txt");
		p.populateRandomJobQ(true);//This assumes a fixed random seed for debugging. Set to "false" for a non-fixed seed.
		p.runScheduler();
		
		p = new ProcessSchedulerSimulator(new RRScheduler(10),"RR.txt");
		p.populateRandomJobQ(true);//This assumes a fixed random seed for debugging. Set to "false" for a non-fixed seed.
		p.runScheduler();
		
		
	}

}
