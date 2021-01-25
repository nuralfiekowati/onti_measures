package ontologybasedinconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
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
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.TimeOutException;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

class MC {

	private static final Logger logger = Logger.getLogger(MC.class);

	static OWLOntologyManager manager6 = OWLManager.createOWLOntologyManager();
	static OWLReasoner reasoner6;
	static OWLReasoner reasoner8;
	static PelletReasoner pelletReasoner6;
	static PelletReasoner pelletReasoner8;
	static AddAxiom addAxiom6;
	static Set<OWLAxiom> axiomsToRemove6;
	static Set<OWLAxiom> ontologyAxiomsCausingUnsatisfiable = new HashSet<>();
	static ArrayList<Integer> explanationSizeList = new ArrayList<>();
	static ArrayList<Integer> consistentSubsetSize = new ArrayList<>();
	static ArrayList<Set<OWLAxiom>> mckCandidate = new ArrayList<>();
	static Set<Set<OWLAxiom>> inconsistentSubset = new HashSet<>();
	static Set<Set<OWLAxiom>> consistentSubset = new HashSet<>();
	static ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();

	static int mcSize;
	static float scSize;
	static float imc;

	private MC() {
		throw new IllegalStateException("MC");
	}

	public static void imcMeasure(Set<Explanation<OWLAxiom>> explanations, Set<OWLAxiom> ontologyAxiomSet,
			ReasonerFactory hermitRf6, OWLReasonerFactory jFactRf6, PelletReasonerFactory pelletRf6,
			ReasonerFactory hermitRf8, OWLReasonerFactory jFactRf8, PelletReasonerFactory pelletRf8,
			Configuration configurationHermit, OWLReasonerConfiguration configurationJFact,
			OWLReasonerConfiguration configurationPellet) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_Mc.txt");
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

			if (!explanations.isEmpty()) {
				for (Set<OWLAxiom> inconsistent : inconsistentSubset) {
					for (Set<OWLAxiom> consistent : consistentSubset) {
						if ((inconsistent.containsAll(consistent)) && (!inconsistent.equals(consistent))) {
							mckCandidate.add(consistent);
						}
					}
				}

			} else {
				mckCandidate.add(ontologyAxiomSet);
			}

			for (Set<OWLAxiom> mck : NotMCK.eliminateNotMCK(mckCandidate)) {
				System.out.println("MCK: " + mck);
			}

			mcSize = NotMCK.eliminateNotMCK(mckCandidate).size();

			// To compute SC(K) in MC Inconsistency Measure
			OWLOntologyManager manager8 = null;
			OWLOntology axiomOntology8 = null;
			Set<OWLAxiom> axiomsToRemove8;
			AddAxiom addAxiom8;

			if (configurationHermit != null) {
				configurationHermit = new Configuration();
				configurationHermit.throwInconsistentOntologyException = false;
			} else if (configurationJFact != null) {
				configurationJFact = new SimpleConfiguration();
			} else if (configurationPellet != null) {
				configurationPellet = new SimpleConfiguration(progressMonitor);
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

				if (hermitRf8 != null) {
					reasoner8 = hermitRf8.createReasoner(axiomOntology8, configurationHermit);

					System.out.println("The axiom: " + theAxiom);
					System.out.println(
							"Is the axiom (" + theAxiom + ") consistent/satisfiable? " + reasoner8.isConsistent());

					if (!reasoner8.isConsistent()) {
						ontologyAxiomsCausingUnsatisfiable.add(theAxiom);
					}
				} else if (jFactRf8 != null) {
					reasoner8 = jFactRf8.createReasoner(axiomOntology8, configurationJFact);

					System.out.println("The axiom: " + theAxiom);
					System.out.println(
							"Is the axiom (" + theAxiom + ") consistent/satisfiable? " + reasoner8.isConsistent());

					if (!reasoner8.isConsistent()) {
						ontologyAxiomsCausingUnsatisfiable.add(theAxiom);
					}
				} else if (pelletRf8 != null) {
					pelletReasoner8 = pelletRf8.createReasoner(axiomOntology8, configurationPellet);

					System.out.println("The axiom: " + theAxiom);
					System.out.println("Is the axiom (" + theAxiom + ") consistent/satisfiable? "
							+ pelletReasoner8.isConsistent());

					if (!pelletReasoner8.isConsistent()) {
						ontologyAxiomsCausingUnsatisfiable.add(theAxiom);
					}
				}

			}

			scSize = ontologyAxiomsCausingUnsatisfiable.size();
			System.out.println("MCsize: " + mcSize);
			System.out.println("SCsize: " + scSize);
			imc = (float) mcSize + scSize - 1;

			System.out.println("7. MC INCONSISTENCY MEASURE I_mc: " + imc);
			System.out.println("***************************************************************");

			TotalTimeExecution.totalTime(startTime);

		} catch (OWLOntologyRenameException | TimeOutException | ReasonerInterruptedException | FileNotFoundException
				| OWLOntologyCreationException e) {
			e.printStackTrace();
			logger.error(e);
		}

	}
}
