package ontologybasedinconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyRenameException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.TimeOutException;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

class Nc {

	private static final Logger logger = Logger.getLogger(Nc.class);

	static OWLOntologyManager manager6 = null;
	static OWLReasoner reasoner6;
	static PelletReasoner pelletReasoner6;
	static AddAxiom addAxiom6;
	static Set<OWLAxiom> axiomsToRemove6;
	static ArrayList<Integer> consistentSubsetSize = new ArrayList<>();
	static Set<Set<OWLAxiom>> inconsistentSubset = new HashSet<>();
	static Set<Set<OWLAxiom>> consistentSubset = new HashSet<>();

	static int sizeOfK;
	static int maxOfSizeK;

	private Nc() {
		throw new IllegalStateException("Nc");
	}

	public static void incMeasure(Set<OWLAxiom> ontologyAxiomSet, ReasonerFactory hermitRf6,
			OWLReasonerFactory jFactRf6, PelletReasonerFactory pelletRf6) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_Nc.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			OWLOntology axiomOntology6 = null;

			for (Set<OWLAxiom> s : PowerSetCount.powerSet(ontologyAxiomSet)) {
				manager6 = OWLManager.createOWLOntologyManager();
				axiomOntology6 = manager6.createOntology();

				axiomsToRemove6 = axiomOntology6.getAxioms();

				if (axiomsToRemove6 != null) {
					manager6.removeAxioms(axiomOntology6, axiomsToRemove6);
				}

				for (OWLAxiom axiomOfS : s) {
					addAxiom6 = new AddAxiom(axiomOntology6, axiomOfS);
					manager6.applyChange(addAxiom6);
				}

				if (hermitRf6 != null) {
					reasoner6 = hermitRf6.createReasoner(axiomOntology6); // without configuration

					System.out.println("C: " + s);
					System.out.println("Is C consistent? " + reasoner6.isConsistent());

					if (reasoner6.isConsistent()) {
						consistentSubset.add(s);
						consistentSubsetSize.add(s.size());
					} else {
						inconsistentSubset.add(s);
					}
				} else if (jFactRf6 != null) {
					reasoner6 = jFactRf6.createReasoner(axiomOntology6);

					System.out.println("C: " + s);
					System.out.println("Is C consistent? " + reasoner6.isConsistent());

					if (reasoner6.isConsistent()) {
						consistentSubset.add(s);
						consistentSubsetSize.add(s.size());
					} else {
						inconsistentSubset.add(s);
					}
				} else if (pelletRf6 != null) {
					pelletReasoner6 = pelletRf6.createReasoner(axiomOntology6);

					System.out.println("C: " + s);
					System.out.println("Is C consistent? " + pelletReasoner6.isConsistent());

					if (pelletReasoner6.isConsistent()) {
						consistentSubset.add(s);
						consistentSubsetSize.add(s.size());
					} else {
						inconsistentSubset.add(s);
					}
				}
			}

			System.out.println("Size of consistent subset size: " + consistentSubsetSize.size());

			if (!consistentSubsetSize.isEmpty()) {
				int maxOfSizeK = Collections.max(consistentSubsetSize);
				System.out.println("Size of K: " + SizeOfK.sizeK(ontologyAxiomSet));
				System.out.println("Max of size K: " + maxOfSizeK);
				float inc = (float) SizeOfK.sizeK(ontologyAxiomSet) - (float) maxOfSizeK;
				System.out.println("8. NC INCONSISTENCY MEASURE I_nc: " + inc);
				System.out.println("-----------------------------------------------------------------------------");
			} else {
				System.out.println("Size of K: " + SizeOfK.sizeK(ontologyAxiomSet));
				float inc = (float) SizeOfK.sizeK(ontologyAxiomSet);
				System.out.println("8. NC INCONSISTENCY MEASURE I_nc: " + inc);
				System.out.println("-----------------------------------------------------------------------------");
			}

			TotalTimeExecution.totalTime(startTime);

		} catch (OWLOntologyRenameException | TimeOutException | ReasonerInterruptedException
				| OWLOntologyCreationException | FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e);
		}

	}

}
