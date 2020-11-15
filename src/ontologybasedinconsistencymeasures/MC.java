package ontologybasedinconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyRenameException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.TimeOutException;

class MC {

	static OWLOntologyManager manager6 = OWLManager.createOWLOntologyManager();
	static OWLReasoner reasoner6;
	static OWLReasoner reasoner8;
	static AddAxiom addAxiom6;
	static Set<OWLAxiom> axiomsToRemove6;
	static Set<OWLAxiom> ontologyAxiomsCausingUnsatisfiable = new HashSet<OWLAxiom>();
	static ArrayList<Integer> explanationSizeList = new ArrayList<>();
	static ArrayList<Integer> consistentSubsetSize = new ArrayList<>();
	static ArrayList<Set<OWLAxiom>> mckCandidate = new ArrayList<Set<OWLAxiom>>();
	static HashSet<Set<OWLAxiom>> inconsistentSubset = new HashSet<Set<OWLAxiom>>();
	static HashSet<Set<OWLAxiom>> consistentSubset = new HashSet<Set<OWLAxiom>>();

	static int mcSize;
	static float scSize;
	static float imc;

	public static void Imc_measure(Set<Explanation<OWLAxiom>> explanations, HashSet<OWLAxiom> ontologyAxiomSet,
			ReasonerFactory hermitRf6, OWLReasonerFactory jFactRf6, ReasonerFactory hermitRf8,
			OWLReasonerFactory jFactRf8, Configuration configurationHermit,
			OWLReasonerConfiguration configurationJFact) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_Mc.txt");
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

			if (explanations.size() > 0) {
				for (Set<OWLAxiom> inconsistent : inconsistentSubset) {
					for (Set<OWLAxiom> consistent : consistentSubset) {
						if ((inconsistent.containsAll(consistent) == true)
								&& (inconsistent.equals(consistent) == false)) {
							mckCandidate.add(consistent);
						}
					}
				}

			} else {
				mckCandidate.add(ontologyAxiomSet);
			}

			for (Set<OWLAxiom> mck : NotMCK.eliminate_notMCK(mckCandidate)) {
				System.out.println("MCK: " + mck);
			}

			mcSize = NotMCK.eliminate_notMCK(mckCandidate).size();

			// To compute SC(K) in MC Inconsistency Measure
			try {
				OWLOntologyManager manager8 = OWLManager.createOWLOntologyManager();
				OWLOntology axiomOntology8 = manager8.createOntology();
				Set<OWLAxiom> axiomsToRemove8;
				AddAxiom addAxiom8;

				if (configurationHermit == null) {
					configurationJFact = new SimpleConfiguration();
				} else {
					configurationHermit = new Configuration();
					configurationHermit.throwInconsistentOntologyException = false;
				}

				for (OWLAxiom theAxiom : ontologyAxiomSet) {

					manager8 = OWLManager.createOWLOntologyManager();
					axiomOntology8 = manager8.createOntology();

					axiomsToRemove8 = axiomOntology8.getAxioms();

					if (axiomsToRemove8 != null) {
						manager8.removeAxioms(axiomOntology8, axiomsToRemove8);
					}

					addAxiom8 = new AddAxiom(axiomOntology8, theAxiom);
					manager8.applyChange(addAxiom8);

					if (hermitRf8 == null) {
						reasoner8 = jFactRf8.createReasoner(axiomOntology8, configurationJFact); // for
					} else {
						reasoner8 = hermitRf8.createReasoner(axiomOntology8, configurationHermit); // for
					}

					System.out.println("The axiom: " + theAxiom);
					System.out.println(
							"Is the axiom (" + theAxiom + ") consistent/satisfiable? " + reasoner8.isConsistent());

					if (reasoner8.isConsistent() == false) {
						ontologyAxiomsCausingUnsatisfiable.add(theAxiom);
					}

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
			} catch (OWLOntologyCreationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			scSize = ontologyAxiomsCausingUnsatisfiable.size();
			System.out.println("MCsize: " + mcSize);
			System.out.println("SCsize: " + scSize);
			imc = (float) mcSize + scSize - 1;

			System.out.println("7. MC INCONSISTENCY MEASURE I_mc: " + imc);
			System.out.println("***************************************************************");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLOntologyCreationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		TotalTimeExecution.totalTime(startTime);

	}
}
