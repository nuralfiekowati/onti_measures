package ontologybasedinconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLAxiom;

class Problematic {

	private static final Logger logger = Logger.getLogger(Problematic.class);

	static float cardOfAxiomMIUnion;

	private Problematic() {
		throw new IllegalStateException("Problematic");
	}

	public static void ipMeasure(Set<OWLAxiom> mikAxiomSet) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_problematic.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			for (OWLAxiom mikAxiom : mikAxiomSet) {
				System.out.println("Axiom of M in MI(K) : " + mikAxiom);
			}

			cardOfAxiomMIUnion = mikAxiomSet.size();

			System.out.println("5. PROBLEMATIC INCONSISTENCY MEASURE I_p: " + cardOfAxiomMIUnion);
			System.out.println("-----------------------------------------------------------------------------");

			TotalTimeExecution.totalTime(startTime);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e);
		}

	}

}
