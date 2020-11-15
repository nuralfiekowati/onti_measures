package ontologybasedinconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashSet;

import org.semanticweb.owlapi.model.OWLAxiom;

class Problematic {

	static float cardOfAxiomMIUnion;

	public static void Ip_measure(HashSet<OWLAxiom> MIKAxiomSet) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_problematic.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			for (OWLAxiom MIKAxiom : MIKAxiomSet) {
				System.out.println("Axiom of M in MI(K) : " + MIKAxiom);
			}

			cardOfAxiomMIUnion = MIKAxiomSet.size();

			System.out.println("5. PROBLEMATIC INCONSISTENCY MEASURE I_p: " + cardOfAxiomMIUnion);
			System.out.println("-----------------------------------------------------------------------------");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TotalTimeExecution.totalTime(startTime);

	}

}
