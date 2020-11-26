package ontologybasedinconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

class Mv {

	private static final Logger logger = Logger.getLogger(Mv.class);

	static Set<OWLClass> ontologyClass = null;
	static Set<OWLNamedIndividual> ontologyIndividual = null;
	static Set<OWLObjectProperty> ontologyObjectProperty = null;

	static float cardOfSignInK;
	static float cardOfSignAxiomMIKUnion;

	private Mv() {
		throw new IllegalStateException("Mv");
	}

	public static void imvMeasure(Set<OWLClass> mikClassSet, Set<OWLNamedIndividual> mikIndividualSet,
			Set<OWLObjectProperty> mikObjectPropertySet, Set<OWLAxiom> ontologyAxiomSet) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_Mv.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			int cardOfMIKClassSet = mikClassSet.size();
			int cardOfMIKIndividualSet = mikIndividualSet.size();
			int cardOfMIKObjectPropertySet = mikObjectPropertySet.size();

			System.out.println("MIKClassSet Size: " + cardOfMIKClassSet);
			System.out.println("MIKIndividualSet Size: " + cardOfMIKIndividualSet);
			System.out.println("MIKObjectPropertySet Size: " + cardOfMIKObjectPropertySet);

			int cardOfSignAxiomMIKUnion = cardOfMIKClassSet + cardOfMIKIndividualSet + cardOfMIKObjectPropertySet;
			float cardOfSignAxiomMIKUnionFloat = (float) cardOfSignAxiomMIKUnion;

			for (OWLAxiom theAxiom : ontologyAxiomSet) {
				ontologyClass = theAxiom.getClassesInSignature();
				ontologyIndividual = theAxiom.getIndividualsInSignature();
				ontologyObjectProperty = theAxiom.getObjectPropertiesInSignature();

				for (OWLClass theClass2 : ontologyClass) {
					mikClassSet.add(theClass2);
				}
				for (OWLNamedIndividual theIndividual2 : ontologyIndividual) {
					mikIndividualSet.add(theIndividual2);
				}
				for (OWLObjectProperty theObjectProperty2 : ontologyObjectProperty) {

					mikObjectPropertySet.add(theObjectProperty2);
				}
			}

			int cardOfSignInK = mikClassSet.size() + mikIndividualSet.size() + mikObjectPropertySet.size();
			float cardOfSignInKFloat = (float) cardOfSignInK;

			System.out.println("Cardinality of signatures in axiom union: " + cardOfSignAxiomMIKUnionFloat);
			System.out.println("Cardinality of signatures in K: " + cardOfSignInKFloat);
			if ((cardOfSignAxiomMIKUnionFloat == 0) && (cardOfSignInKFloat == 0)) {
				System.out.println("9. MV INCONSISTENCY MEASURE I_mv: 0 ");
			} else {
				System.out.println(
						"9. MV INCONSISTENCY MEASURE I_mv: " + cardOfSignAxiomMIKUnionFloat / cardOfSignInKFloat);
			}
			System.out.println("***************************************************************");

			TotalTimeExecution.totalTime(startTime);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e);
		}

	}

}
