package ontologybasedinconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyRenameException;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.TimeOutException;

class IR {

	private static final Logger logger = Logger.getLogger(IR.class);

	static int kSize;
	static float sizeOfMI;
	static float iir;

	private IR() {
		throw new IllegalStateException("IR");
	}

	public static void iirMeasure(Set<Explanation<OWLAxiom>> explanations, Set<OWLAxiom> ontologyAxiomSet) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_Ir.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			kSize = SizeOfK.sizeK(ontologyAxiomSet);
			sizeOfMI = explanations.size();

			iir = sizeOfMI / kSize;
			System.out.println("Size of MI(K): " + sizeOfMI);
			System.out.println("Size of K: " + kSize);
			if ((sizeOfMI == 0) && (kSize == 0)) {
				System.out.println("6. INCOMPATIBILITY RATIO INCONSISTENCY MEASURE I_ir: 0");
			} else {
				System.out.println("6. INCOMPATIBILITY RATIO INCONSISTENCY MEASURE I_ir: " + iir);
			}
			System.out.println("-----------------------------------------------------------------------------");

			TotalTimeExecution.totalTime(startTime);

		} catch (OWLOntologyRenameException | TimeOutException | ReasonerInterruptedException
				| FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e);
		}

	}

}
