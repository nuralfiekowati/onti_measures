package ontologybasedinconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Set;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

class MI {

	public static void Imi_measure(OWLOntology ontology, Set<Explanation<OWLAxiom>> explanations) {

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
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TotalTimeExecution.totalTime(startTime);

	}
}
