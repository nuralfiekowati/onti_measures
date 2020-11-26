package ontologybasedinconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLOntologyRenameException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.TimeOutException;

class Drastic {

	private static final Logger logger = Logger.getLogger(Drastic.class);

	private Drastic() {
		throw new IllegalStateException("Drastic");
	}

	public static void idMeasure(OWLReasoner reasoner) throws FileNotFoundException {

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

			TotalTimeExecution.totalTime(startTime);

		} catch (OWLOntologyRenameException | TimeOutException | ReasonerInterruptedException
				| FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e);
		}

	}
}