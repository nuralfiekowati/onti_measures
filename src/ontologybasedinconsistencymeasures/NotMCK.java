package ontologybasedinconsistencymeasures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

class NotMCK {

	public static <T> HashSet<Set<OWLAxiom>> eliminate_notMCK(ArrayList<Set<OWLAxiom>> MCKcandidate) {

		HashSet<Set<OWLAxiom>> MCK = new HashSet<Set<OWLAxiom>>();

		Collections.sort(MCKcandidate, new Comparator<Set<OWLAxiom>>() {
			public int compare(Set<OWLAxiom> axiom1, Set<OWLAxiom> axiom2) {
				return axiom2.size() - axiom1.size();
			}
		});

		for (int i = 0; i < MCKcandidate.size(); i++) {

			Set<OWLAxiom> candidateI = MCKcandidate.get(i);
			boolean flag = true;

			for (Set<OWLAxiom> MCKcand : MCK) {

				if (MCKcand.containsAll(candidateI)) {
					flag = false;
					break;
				}
			}
			if (flag) {
				MCK.add(candidateI);
			}
		}

		return MCK;

	}

}
