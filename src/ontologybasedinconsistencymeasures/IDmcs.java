package ontologybasedinconsistencymeasures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
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
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.TimeOutException;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

class IDmcs {

	private static final Logger logger = Logger.getLogger(IDmcs.class);

	static Set<OWLClass> ontologyClass = null;
	static Set<OWLNamedIndividual> ontologyIndividual = null;
	static Set<OWLObjectProperty> ontologyObjectProperty = null;

	static OWLReasoner reasoner3;
	static OWLReasoner reasoner5;

	static PelletReasoner pelletReasoner3;
	static PelletReasoner pelletReasoner5;

	static ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();

	static float cardOfSignInK;
	static float cardOfSignAxiomMIKUnion;
	static int cardOfMCSesSign;

	private IDmcs() {
		throw new IllegalStateException("IDmcs");
	}

	public static void idMcsMeasure(Set<OWLClass> mikClassSet, Set<OWLNamedIndividual> mikIndividualSet,
			Set<OWLObjectProperty> mikObjectPropertySet, Set<OWLAxiom> ontologyAxiomSet, ReasonerFactory hermitRf3,
			OWLReasonerFactory jFactRf3, PelletReasonerFactory pelletRf3, ReasonerFactory hermitRf5,
			OWLReasonerFactory jFactRf5, PelletReasonerFactory pelletRf5, Configuration configurationHermit,
			OWLReasonerConfiguration configurationJFact, OWLReasonerConfiguration configurationPellet) {

		long startTime = System.currentTimeMillis();

		try {
			File file = new File("outputs/output_IDmcs.txt");
			FileOutputStream fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);

			OWLOntologyManager manager3 = null;
			OWLOntology axiomOntology3 = null;

			AddAxiom addAxiom3;
			Set<OWLAxiom> axiomsToRemove3;

			OWLOntologyManager manager5 = null;
			OWLOntology axiomOntology5 = null;

			AddAxiom addAxiom5;
			Set<OWLAxiom> axiomsToRemove5;
			Set<Set<OWLAxiom>> mcses = new HashSet<>();

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
			System.out.println("Cardinality of signatures in axiom union: " + cardOfSignAxiomMIKUnion);
			System.out.println("Cardinality of signatures in K: " + cardOfSignInKFloat);

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

				if (hermitRf3 != null) {
					reasoner3 = hermitRf3.createReasoner(axiomOntology3); // without configuration

					System.out.println("Is K minus subset M consistent? " + reasoner3.isConsistent());
				} else if (jFactRf3 != null) {
					reasoner3 = jFactRf3.createReasoner(axiomOntology3);

					System.out.println("Is K minus subset M consistent? " + reasoner3.isConsistent());
				} else if (pelletRf3 != null) {
					pelletReasoner3 = pelletRf3.createReasoner(axiomOntology3);

					System.out.println("Is K minus subset M consistent? " + pelletReasoner3.isConsistent());
				}

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

				if (configurationHermit != null) {
					configurationHermit = new Configuration();
					configurationHermit.throwInconsistentOntologyException = false;
				} else if (configurationJFact != null) {
					configurationJFact = new SimpleConfiguration();
				} else if (configurationPellet != null) {
					configurationPellet = new SimpleConfiguration(progressMonitor);
				}

				ArrayList<String> consistentValue = new ArrayList<>();
				for (OWLAxiom Ci : subsetM) {
					System.out.println("Ci: " + Ci);

					manager5.removeAxioms(axiomOntology5, subsetM);
					manager5.addAxiom(axiomOntology5, Ci);

					if (hermitRf5 != null) {
						reasoner5 = hermitRf5.createReasoner(axiomOntology5, configurationHermit);

						System.out.println("Is K minus (subset M minus Ci) consistent? " + reasoner5.isConsistent());

						if (reasoner5.isConsistent()) {
							consistentValue.add("true");
						} else {
							consistentValue.add("false");
						}

					} else if (jFactRf5 != null) {
						reasoner5 = jFactRf5.createReasoner(axiomOntology5, configurationJFact);

						System.out.println("Is K minus (subset M minus Ci) consistent? " + reasoner5.isConsistent());

						if (reasoner5.isConsistent()) {
							consistentValue.add("true");
						} else {
							consistentValue.add("false");
						}

					} else if (pelletRf5 != null) {
						pelletReasoner5 = pelletRf5.createReasoner(axiomOntology5, configurationPellet);

						System.out.println(
								"Is K minus (subset M minus Ci) consistent? " + pelletReasoner5.isConsistent());

						if (pelletReasoner5.isConsistent()) {
							consistentValue.add("true");
						} else {
							consistentValue.add("false");
						}

					}

				}

				if (hermitRf5 != null || jFactRf5 != null) {
					if (reasoner3.isConsistent() && ContainAllFalseQuestion.doesListContainAllFalse(consistentValue)) {
						mcses.add(subsetM);
					}
				} else {
					if (pelletReasoner3.isConsistent()
							&& ContainAllFalseQuestion.doesListContainAllFalse(consistentValue)) {
						mcses.add(subsetM);
					}
				}

			}

			System.out.println("MCSes size: " + mcses.size());

			Set<OWLClass> ontologyClass2 = null;
			Set<OWLNamedIndividual> ontologyIndividual2 = null;
			Set<OWLObjectProperty> ontologyObjectProperty2 = null;

			Set<OWLClass> mcsClassSet = new HashSet<>();
			Set<OWLNamedIndividual> mcsIndividualSet = new HashSet<>();
			Set<OWLObjectProperty> mcsObjectPropertySet = new HashSet<>();

			for (Set<OWLAxiom> mcs : mcses) {
				System.out.println("MCS: " + mcs);
				for (OWLAxiom axiomInMcs : mcs) {

					ontologyClass2 = axiomInMcs.getClassesInSignature();
					ontologyIndividual2 = axiomInMcs.getIndividualsInSignature();
					ontologyObjectProperty2 = axiomInMcs.getObjectPropertiesInSignature();

					for (OWLClass theClass3 : ontologyClass2) {
						mcsClassSet.add(theClass3);
					}
					for (OWLNamedIndividual theIndividual3 : ontologyIndividual2) {
						mcsIndividualSet.add(theIndividual3);
					}
					for (OWLObjectProperty theObjectProperty3 : ontologyObjectProperty2) {
						mcsObjectPropertySet.add(theObjectProperty3);
					}

				}

			}

			int cardOfMCSesSign = mcsClassSet.size() + mcsIndividualSet.size() + mcsObjectPropertySet.size();
			System.out.println("Cardinality of Class in MCS: " + mcsClassSet.size());
			System.out.println("Cardinality of Individual in MCS: " + mcsIndividualSet.size());
			System.out.println("Cardinality of ObjectProperty in MCS: " + mcsObjectPropertySet.size());
			System.out.println("Cardinality of signatures in MCS: " + cardOfMCSesSign);
			System.out.println("Cardinality of signatures in K: " + cardOfSignInKFloat);

			if ((cardOfMCSesSign == 0) && (cardOfSignInKFloat == 0)) {
				System.out.println("10. ID_MCS INCONSISTENCY MEASURE ID_mcs: 0");
			} else {
				System.out.println("10. ID_MCS INCONSISTENCY MEASURE ID_mcs: " + cardOfMCSesSign / cardOfSignInKFloat);
			}
			System.out.println("***************************************************************");

			TotalTimeExecution.totalTime(startTime);

		} catch (OWLOntologyRenameException | TimeOutException | ReasonerInterruptedException
				| OWLOntologyCreationException | FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e);
		}

	}
}
