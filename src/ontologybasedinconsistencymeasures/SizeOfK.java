package ontologybasedinconsistencymeasures;

import java.util.HashSet;

import org.semanticweb.owlapi.model.OWLAxiom;

class SizeOfK {
	
	public static int sizeK(HashSet<OWLAxiom> ontologyAxiomSet) {
		
		int sizeOfK = ontologyAxiomSet.size();
		System.out.println("Size of K: " + sizeOfK);
		
		return sizeOfK;
		
	}

}
