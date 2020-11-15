package ontologybasedinconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.TimeOutException;

class Drastic {

	public static void Id_measure(OWLReasoner reasoner) throws FileNotFoundException {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_drastic.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			if (reasoner.isConsistent()) {
				System.out.println("1. DRASTIC INCONSISTENCY MEASURE I_d: " + 0);
				System.out.println("-----------------------------------------------------------------------------");
			} else {
				System.out.println("1. DRASTIC INCONSISTENCY MEASURE I_d: " + 1);
				System.out.println("-----------------------------------------------------------------------------");
			}
		} catch (TimeOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReasonerInterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TotalTimeExecution.totalTime(startTime);

	}
}