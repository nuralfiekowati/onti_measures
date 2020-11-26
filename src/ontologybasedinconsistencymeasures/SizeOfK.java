package ontologybasedinconsistencymeasures;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;

class SizeOfK {

	private SizeOfK() {
		throw new IllegalStateException("SizeOfK");
	}

	public static int sizeK(Set<OWLAxiom> ontologyAxiomSet) {

		int sizeOfK = ontologyAxiomSet.size();
		System.out.println("Size of K: " + sizeOfK);

		return sizeOfK;

	}

}
