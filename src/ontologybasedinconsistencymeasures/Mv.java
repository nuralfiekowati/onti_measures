package ontologybasedinconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

class Mv {

	static Set<OWLClass> ontologyClass = null;
	static Set<OWLNamedIndividual> ontologyIndividual = null;
	static Set<OWLObjectProperty> ontologyObjectProperty = null;

	static float cardOfSignInK, cardOfSignAxiomMIKUnion;

	public static void Imv_measure(HashSet<OWLClass> MIKClassSet, HashSet<OWLNamedIndividual> MIKIndividualSet,
			HashSet<OWLObjectProperty> MIKObjectPropertySet, HashSet<OWLAxiom> ontologyAxiomSet) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_Mv.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			int cardOfMIKClassSet = MIKClassSet.size();
			int cardOfMIKIndividualSet = MIKIndividualSet.size();
			int cardOfMIKObjectPropertySet = MIKObjectPropertySet.size();

			System.out.println("MIKClassSet Size: " + cardOfMIKClassSet);
			System.out.println("MIKIndividualSet Size: " + cardOfMIKIndividualSet);
			System.out.println("MIKObjectPropertySet Size: " + cardOfMIKObjectPropertySet);

			float cardOfSignAxiomMIKUnion = cardOfMIKClassSet + cardOfMIKIndividualSet + cardOfMIKObjectPropertySet;

			for (OWLAxiom theAxiom : ontologyAxiomSet) {
				ontologyClass = (Set<OWLClass>) theAxiom.getClassesInSignature();
				ontologyIndividual = (Set<OWLNamedIndividual>) theAxiom.getIndividualsInSignature();
				ontologyObjectProperty = (Set<OWLObjectProperty>) theAxiom.getObjectPropertiesInSignature();

				for (OWLClass theClass2 : ontologyClass) {
					MIKClassSet.add(theClass2);
				}
				for (OWLNamedIndividual theIndividual2 : ontologyIndividual) {
					MIKIndividualSet.add(theIndividual2);
				}
				for (OWLObjectProperty theObjectProperty2 : ontologyObjectProperty) {

					MIKObjectPropertySet.add(theObjectProperty2);
				}
			}

			float cardOfSignInK = MIKClassSet.size() + MIKIndividualSet.size() + MIKObjectPropertySet.size();
			System.out.println("Cardinality of signatures in axiom union: " + cardOfSignAxiomMIKUnion);
			System.out.println("Cardinality of signatures in K: " + cardOfSignInK);
			if ((cardOfSignAxiomMIKUnion == 0) && (cardOfSignInK == 0)) {
				System.out.println("9. MV INCONSISTENCY MEASURE I_mv: 0 ");
			} else {
				System.out.println("9. MV INCONSISTENCY MEASURE I_mv: " + cardOfSignAxiomMIKUnion / cardOfSignInK);
			}
			System.out.println("***************************************************************");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TotalTimeExecution.totalTime(startTime);

	}

}
