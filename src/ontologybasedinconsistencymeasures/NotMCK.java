package ontologybasedinconsistencymeasures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

class NotMCK {

	private NotMCK() {
		throw new IllegalStateException("NotMCK");
	}

	public static <T> Set<Set<OWLAxiom>> eliminateNotMCK(ArrayList<Set<OWLAxiom>> mckCandidate) {

		Set<Set<OWLAxiom>> mck = new HashSet<>();

		Collections.sort(mckCandidate, new Comparator<Set<OWLAxiom>>() {
			public int compare(Set<OWLAxiom> axiom1, Set<OWLAxiom> axiom2) {
				return axiom2.size() - axiom1.size();
			}
		});

		for (int i = 0; i < mckCandidate.size(); i++) {

			Set<OWLAxiom> candidateI = mckCandidate.get(i);
			boolean flag = true;

			for (Set<OWLAxiom> mckCand : mck) {

				if (mckCand.containsAll(candidateI)) {
					flag = false;
					break;
				}
			}
			if (flag) {
				mck.add(candidateI);
			}
		}

		return mck;

	}

}
