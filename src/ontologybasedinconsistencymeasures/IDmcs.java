package ontologybasedinconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyRenameException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.TimeOutException;

class IDmcs {

	static Set<OWLClass> ontologyClass = null;
	static Set<OWLNamedIndividual> ontologyIndividual = null;
	static Set<OWLObjectProperty> ontologyObjectProperty = null;

	static OWLReasoner reasoner3, reasoner5;

	static float cardOfSignInK, cardOfSignAxiomMIKUnion;
	static int cardOfMCSesSign;

	public static void IDmcs_measure(HashSet<OWLClass> MIKClassSet, HashSet<OWLNamedIndividual> MIKIndividualSet,
			HashSet<OWLObjectProperty> MIKObjectPropertySet, HashSet<OWLAxiom> ontologyAxiomSet,
			ReasonerFactory hermitRf3, OWLReasonerFactory jFactRf3, ReasonerFactory hermitRf5,
			OWLReasonerFactory jFactRf5, Configuration configurationHermit,
			OWLReasonerConfiguration configurationJFact) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_IDmcs.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			OWLOntologyManager manager3 = OWLManager.createOWLOntologyManager();
			OWLOntology axiomOntology3 = manager3.createOntology();

			AddAxiom addAxiom3;
			Set<OWLAxiom> axiomsToRemove3;

			OWLOntologyManager manager5 = OWLManager.createOWLOntologyManager();
			OWLOntology axiomOntology5 = manager3.createOntology();

			AddAxiom addAxiom5;
			Set<OWLAxiom> axiomsToRemove5;
			Set<Set<OWLAxiom>> MCSes = new HashSet<Set<OWLAxiom>>();

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

			for (Set<OWLAxiom> subsetM : PowerSetCount.powerSet(ontologyAxiomSet)) {

				manager3 = OWLManager.createOWLOntologyManager();
				axiomOntology3 = manager3.createOntology();

				// Condition 1 of MCS

				axiomsToRemove3 = axiomOntology3.getAxioms();

				if (axiomsToRemove3 != null) {
					manager3.removeAxioms(axiomOntology3, axiomsToRemove3);
				}

				for (OWLAxiom axiomOfK : ontologyAxiomSet) {
					addAxiom3 = new AddAxiom(axiomOntology3, axiomOfK);
					manager3.applyChange(addAxiom3);
				}

				System.out.println("========================================");

				System.out.println("M: " + subsetM);

				manager3.removeAxioms(axiomOntology3, subsetM);

				if (hermitRf3 == null) {
					reasoner3 = jFactRf3.createReasoner(axiomOntology3);
				} else {
					reasoner3 = hermitRf3.createReasoner(axiomOntology3);
				}

				System.out.println("Is K minus subset M consistent? " + reasoner3.isConsistent());

				// Condition 2 of MCS

				manager5 = OWLManager.createOWLOntologyManager();
				axiomOntology5 = manager5.createOntology();

				axiomsToRemove5 = axiomOntology5.getAxioms();

				if (axiomsToRemove5 != null) {
					manager5.removeAxioms(axiomOntology5, axiomsToRemove5);
				}

				for (OWLAxiom axiomOfK : ontologyAxiomSet) {
					addAxiom5 = new AddAxiom(axiomOntology5, axiomOfK);
					manager5.applyChange(addAxiom5);
				}

				if (configurationHermit == null) {
					configurationJFact = new SimpleConfiguration();
				} else {
					configurationHermit = new Configuration();
					configurationHermit.throwInconsistentOntologyException = false;
				}

				ArrayList<String> consistentValue = new ArrayList<String>();
				for (OWLAxiom Ci : subsetM) {
					System.out.println("Ci: " + Ci);

					manager5.removeAxioms(axiomOntology5, subsetM);
					manager5.addAxiom(axiomOntology5, Ci);

					if (hermitRf5 == null) {
						reasoner5 = jFactRf3.createReasoner(axiomOntology5, configurationJFact);
					} else {
						reasoner5 = hermitRf3.createReasoner(axiomOntology5, configurationHermit);
					}

					System.out.println("Is K minus (subset M minus Ci) consistent? " + reasoner5.isConsistent());

					if (reasoner5.isConsistent() == true) {
						consistentValue.add("true");
					} else if (reasoner5.isConsistent() == false) {
						consistentValue.add("false");
					}

				}

				if (reasoner3.isConsistent() == true) {
					if (ContainAllFalseQuestion.doesListContainAllFalse(consistentValue) == true) {
						MCSes.add(subsetM);
					}
				}
			}

			System.out.println("MCSes size: " + MCSes.size());

			Set<OWLClass> ontologyClass2 = null;
			Set<OWLNamedIndividual> ontologyIndividual2 = null;
			Set<OWLObjectProperty> ontologyObjectProperty2 = null;

			HashSet<OWLClass> MCSClassSet = new HashSet<OWLClass>();
			HashSet<OWLNamedIndividual> MCSIndividualSet = new HashSet<OWLNamedIndividual>();
			HashSet<OWLObjectProperty> MCSObjectPropertySet = new HashSet<OWLObjectProperty>();

			for (Set<OWLAxiom> MCS : MCSes) {
				System.out.println("MCS: " + MCS);
				for (OWLAxiom axiomInMcs : MCS) {

					ontologyClass2 = (Set<OWLClass>) axiomInMcs.getClassesInSignature();
					ontologyIndividual2 = (Set<OWLNamedIndividual>) axiomInMcs.getIndividualsInSignature();
					ontologyObjectProperty2 = (Set<OWLObjectProperty>) axiomInMcs.getObjectPropertiesInSignature();

					for (OWLClass theClass3 : ontologyClass2) {
						MCSClassSet.add(theClass3);
					}
					for (OWLNamedIndividual theIndividual3 : ontologyIndividual2) {
						MCSIndividualSet.add(theIndividual3);
					}
					for (OWLObjectProperty theObjectProperty3 : ontologyObjectProperty2) {
						MCSObjectPropertySet.add(theObjectProperty3);
					}

				}

			}

			int cardOfMCSesSign = MCSClassSet.size() + MCSIndividualSet.size() + MCSObjectPropertySet.size();
			System.out.println("Cardinality of Class in MCS: " + MCSClassSet.size());
			System.out.println("Cardinality of Individual in MCS: " + MCSIndividualSet.size());
			System.out.println("Cardinality of ObjectProperty in MCS: " + MCSObjectPropertySet.size());
			System.out.println("Cardinality of signatures in MCS: " + cardOfMCSesSign);
			System.out.println("Cardinality of signatures in K: " + cardOfSignInK);

			if ((cardOfMCSesSign == 0) && (cardOfSignInK == 0)) {
				System.out.println("10. ID_MCS INCONSISTENCY MEASURE ID_mcs: 0");
			} else {
				System.out.println("10. ID_MCS INCONSISTENCY MEASURE ID_mcs: " + cardOfMCSesSign / cardOfSignInK);
			}
			System.out.println("***************************************************************");
		} catch (OWLOntologyRenameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReasonerInterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TotalTimeExecution.totalTime(startTime);

	}
}
