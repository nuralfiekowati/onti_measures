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
import org.semanticweb.owl.explanation.api.Explanation;
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

class Df {

	private static final Logger logger = Logger.getLogger(Df.class);

	static OWLOntologyManager manager6 = OWLManager.createOWLOntologyManager();
	static OWLReasoner reasoner6;
	static PelletReasoner pelletReasoner6;
	static AddAxiom addAxiom6;
	static Set<OWLAxiom> axiomsToRemove6;
	static ArrayList<Integer> explanationSizeList = new ArrayList<>();
	static ArrayList<Integer> consistentSubsetSize = new ArrayList<>();
	static Set<Set<OWLAxiom>> consistentSubset = new HashSet<>();
	static Set<Set<OWLAxiom>> inconsistentSubset = new HashSet<>();
	static ArrayList<Float> riKarray = new ArrayList<>();

	static int iPlus1;
	static float mSize;
	static float cSize;
	static float mSizePlusCsize;
	static float riK;
	static float oneMinusRiK;
	static float riKDivi;
	static float total = 1;
	static float sizeOfM;

	private Df() {
		throw new IllegalStateException("Df");
	}

	public static void idfMeasure(Set<OWLAxiom> ontologyAxiomSet, Set<OWLAxiom> arrayOfExplanation,
			Set<Explanation<OWLAxiom>> explanations, ReasonerFactory hermitRf6, OWLReasonerFactory jFactRf6,
			PelletReasonerFactory pelletRf6) {

		long startTime = System.currentTimeMillis();
		int kSize = SizeOfK.sizeK(ontologyAxiomSet);

		try {

			File file = new File("outputs/output_Df.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			OWLOntology axiomOntology6 = null;

			for (int i = 0; i < explanations.size(); i++) {
				sizeOfM = arrayOfExplanation.size();
				explanationSizeList.add((int) sizeOfM);
			}

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

			for (int i = 0; i < kSize; i++) {

				iPlus1 = i + 1;

				mSize = Collections.frequency(explanationSizeList, iPlus1);
				cSize = Collections.frequency(consistentSubsetSize, iPlus1);
				System.out.println("Msize of " + iPlus1 + ": " + mSize);
				System.out.println("Csize of " + iPlus1 + ": " + cSize);
				mSizePlusCsize = mSize + cSize;
				if (mSize == 0) {
					riK = 0;
				} else {
					riK = mSize / mSizePlusCsize;
				}

				System.out.println("R" + iPlus1 + "(K): " + mSize + "/" + mSizePlusCsize + "= " + riK);
				riKDivi = riK / (float) iPlus1;
				System.out.println("RiKDivi: " + riKDivi);
				oneMinusRiK = (float) 1 - riKDivi;
				System.out.println("(1 - RiK): " + oneMinusRiK);

				riKarray.add(oneMinusRiK);
			}

			System.out.println("Number of one minus RiK: " + riKarray.size());
			for (float value : riKarray) {
				System.out.println("Each value in one minus RiK: " + value);
				total *= value;
			}
			System.out.println("Total of multiplication of one minus RiK: " + total);
			float idf = (float) 1 - total;
			System.out.println("1 - Total of multiplication of one minus RiK: " + idf);
			System.out.println("4. Df INCONSISTENCY MEASURE I_df: " + idf);
			System.out.println("-----------------------------------------------------------------------------");

			TotalTimeExecution.totalTime(startTime);

		} catch (OWLOntologyRenameException | TimeOutException | ReasonerInterruptedException
				| OWLOntologyCreationException | FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e);
		}

	}
}
