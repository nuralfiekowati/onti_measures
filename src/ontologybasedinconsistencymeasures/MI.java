package ontologybasedinconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

class MI {

	private static final Logger logger = Logger.getLogger(MI.class);

	private MI() {
		throw new IllegalStateException("MI");
	}

	public static void imiMeasure(OWLOntology ontology, Set<Explanation<OWLAxiom>> explanations) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_MI.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			float sizeOfMI = explanations.size();
			System.out.println("Explanation size: " + explanations.size());

			System.out.println("2. MI-INCONSISTENCY MEASURE I_mi: " + sizeOfMI);
			System.out.println("-----------------------------------------------------------------------------");

			TotalTimeExecution.totalTime(startTime);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e);
		}

	}
}
