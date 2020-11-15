package ontologybasedinconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;

class IR {

	static int Ksize;
	static float sizeOfMI;
	static float I_ir;

	public static void Iir_measure(Set<Explanation<OWLAxiom>> explanations, HashSet<OWLAxiom> ontologyAxiomSet) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_Ir.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			Ksize = SizeOfK.sizeK(ontologyAxiomSet);
			sizeOfMI = explanations.size();

			I_ir = sizeOfMI / Ksize;
			System.out.println("Size of MI(K): " + sizeOfMI);
			System.out.println("Size of K: " + Ksize);
			if ((sizeOfMI == 0) && (Ksize == 0)) {
				System.out.println("6. INCOMPATIBILITY RATIO INCONSISTENCY MEASURE I_ir: 0");
			} else {
				System.out.println("6. INCOMPATIBILITY RATIO INCONSISTENCY MEASURE I_ir: " + I_ir);
			}
			System.out.println("-----------------------------------------------------------------------------");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TotalTimeExecution.totalTime(startTime);

	}

}
