package ontologybasedinconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

class Nc {

	static OWLOntologyManager manager6 = OWLManager.createOWLOntologyManager();
	static OWLReasoner reasoner6;
	static AddAxiom addAxiom6;
	static Set<OWLAxiom> axiomsToRemove6;
	static ArrayList<Integer> consistentSubsetSize = new ArrayList<>();
	static HashSet<Set<OWLAxiom>> inconsistentSubset = new HashSet<Set<OWLAxiom>>();
	static HashSet<Set<OWLAxiom>> consistentSubset = new HashSet<Set<OWLAxiom>>();

	static int sizeOfK, maxOfSizeK;

	public static void Inc_measure(HashSet<OWLAxiom> ontologyAxiomSet, ReasonerFactory hermitRf6,
			OWLReasonerFactory jFactRf6) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_Nc.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			OWLOntology axiomOntology6 = manager6.createOntology();

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

				if (hermitRf6 == null) {
					reasoner6 = jFactRf6.createReasoner(axiomOntology6);
				} else {
					reasoner6 = hermitRf6.createReasoner(axiomOntology6);
				}

				System.out.println("C: " + s);
				System.out.println("Is C consistent? " + reasoner6.isConsistent());

				if (reasoner6.isConsistent() == true) {
					consistentSubset.add(s);
					consistentSubsetSize.add(s.size());
				}

				if (reasoner6.isConsistent() == false) {
					inconsistentSubset.add(s);
				}
			}

			System.out.println("Size of consistent subset size: " + consistentSubsetSize.size());

			if (consistentSubsetSize.size() != 0) {
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

		} catch (OWLOntologyRenameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReasonerInterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TotalTimeExecution.totalTime(startTime);

	}

}
