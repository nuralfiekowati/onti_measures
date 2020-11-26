package ontologybasedinconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;

class MIc {

	private static final Logger logger = Logger.getLogger(MIc.class);

	static float onePerSizeOfM;
	static float sumOfSize = 0;
	static float sizeOfM;
	static ArrayList<Integer> explanationSizeList = new ArrayList<>();

	private MIc() {
		throw new IllegalStateException("MIc");
	}

	public static void imicMeasure(Set<OWLAxiom> arrayOfExplanation, Set<Explanation<OWLAxiom>> explanations) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_MIc.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			for (int i = 0; i < explanations.size(); i++) {
				System.out.println("M in MI(K): " + arrayOfExplanation);
				sizeOfM = arrayOfExplanation.size();
				System.out.println("M size: " + sizeOfM);
				explanationSizeList.add((int) sizeOfM);
				onePerSizeOfM = (float) 1 / sizeOfM;
				System.out.println("One per M size: " + onePerSizeOfM);
				sumOfSize = sumOfSize + onePerSizeOfM;

				System.out.println("Sum of one per M size: " + sumOfSize);
			}

			System.out.println("3. MI^C-INCONSISTENCY MEASURE I_mic: " + sumOfSize);
			System.out.println("-----------------------------------------------------------------------------");

			TotalTimeExecution.totalTime(startTime);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e);
		}

	}
}
