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

class Df {

	static OWLOntologyManager manager6 = OWLManager.createOWLOntologyManager();
	static OWLReasoner reasoner6;
	static AddAxiom addAxiom6;
	static Set<OWLAxiom> axiomsToRemove6;
	static ArrayList<Integer> explanationSizeList = new ArrayList<>();
	static ArrayList<Integer> consistentSubsetSize = new ArrayList<>();
	static HashSet<Set<OWLAxiom>> consistentSubset = new HashSet<Set<OWLAxiom>>();
	static HashSet<Set<OWLAxiom>> inconsistentSubset = new HashSet<Set<OWLAxiom>>();
	static ArrayList<Float> RiKarray = new ArrayList<>();

	static int iPlus1;
	static float Msize;
	static float Csize;
	static float MsizePlusCsize;
	static float RiK;
	static float OneMinusRiK;
	static float RiKDivi;
	static float total = 1;
	static float sizeOfM;

	public static void Idf_measure(HashSet<OWLAxiom> ontologyAxiomSet, Set<OWLAxiom> arrayOfExplanation,
			Set<Explanation<OWLAxiom>> explanations, ReasonerFactory hermitRf6, OWLReasonerFactory jFactRf6) {

		long startTime = System.currentTimeMillis();
		int Ksize = SizeOfK.sizeK(ontologyAxiomSet);

		try {

			File file = new File("outputs/output_Df.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			OWLOntology axiomOntology6 = manager6.createOntology();

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

			for (int i = 0; i < Ksize; i++) {

				iPlus1 = i + 1;

				Msize = Collections.frequency(explanationSizeList, iPlus1);
				Csize = Collections.frequency(consistentSubsetSize, iPlus1);
				System.out.println("Msize of " + iPlus1 + ": " + Msize);
				System.out.println("Csize of " + iPlus1 + ": " + Csize);
				MsizePlusCsize = Msize + Csize;
				if (Msize == 0) {
					RiK = 0;
				} else {
					RiK = Msize / MsizePlusCsize;
					System.out.println("Lalala");
				}

				System.out.println("R" + iPlus1 + "(K): " + Msize + "/" + MsizePlusCsize + "= " + RiK);
				RiKDivi = RiK / (float) iPlus1;
				System.out.println("RiKDivi: " + RiKDivi);
				OneMinusRiK = (float) 1 - RiKDivi;
				System.out.println("(1 - RiK): " + OneMinusRiK);

				RiKarray.add(OneMinusRiK);
			}

			System.out.println("Number of one minus RiK: " + RiKarray.size());
			for (float value : RiKarray) {
				System.out.println("Each value in one minus RiK: " + value);
				total *= value;
			}
			System.out.println("Total of multiplication of one minus RiK: " + total);
			float idf = (float) 1 - total;
			System.out.println("1 - Total of multiplication of one minus RiK: " + idf);
			System.out.println("4. Df INCONSISTENCY MEASURE I_df: " + idf);
			System.out.println("-----------------------------------------------------------------------------");
		} catch (OWLOntologyRenameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeOutException f) {
			// TODO Auto-generated catch block
			f.printStackTrace();
		} catch (ReasonerInterruptedException g) {
			// TODO Auto-generated catch block
			g.printStackTrace();
		} catch (OWLOntologyCreationException h) {
			// TODO Auto-generated catch block
			h.printStackTrace();
		} catch (FileNotFoundException i) {
			i.printStackTrace();
		}

		TotalTimeExecution.totalTime(startTime);

	}
}
