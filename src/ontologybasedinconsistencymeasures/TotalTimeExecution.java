package ontologybasedinconsistencymeasures;

class TotalTimeExecution {
	
	public static void totalTime(long startTime) {
		
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("                                 ");
		System.out.println("Total time execution: " + totalTime + " milliseconds.");
		
	}

}
