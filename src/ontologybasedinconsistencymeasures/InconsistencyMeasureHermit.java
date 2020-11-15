package ontologybasedinconsistencymeasures;

import java.io.File;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationException;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.api.ExplanationGeneratorInterruptedException;
import org.semanticweb.owl.explanation.impl.blackbox.checker.InconsistentOntologyExplanationGeneratorFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;
import org.semanticweb.owlapi.reasoner.TimeOutException;

public class InconsistencyMeasureHermit {

	static Set<OWLAxiom> arrayOfExplanation = null;
	static Set<Set<OWLAxiom>> arrayOfExplanationSet = new HashSet<Set<OWLAxiom>>(3000000, 1000000F);
	static HashSet<OWLAxiom> mikAxiomSet = new HashSet<OWLAxiom>();
	static HashSet<OWLAxiom> topBottom = new HashSet<OWLAxiom>();
	static HashSet<OWLClass> mikClassSet = new HashSet<OWLClass>();
	static HashSet<OWLNamedIndividual> mikIndividualSet = new HashSet<OWLNamedIndividual>();
	static HashSet<OWLObjectProperty> mikObjectPropertySet = new HashSet<OWLObjectProperty>();
	static Set<OWLClass> inconsistentClass = null;
	static Set<OWLNamedIndividual> inconsistentIndividual = null;
	static Set<OWLObjectProperty> inconsistentObjectProperty = null;
	static HashSet<OWLAxiom> ontologyAxiomSet = new HashSet<OWLAxiom>(3000000, 1000000F);
	static ReasonerFactory rf3 = new ReasonerFactory();
	static ReasonerFactory rf5 = new ReasonerFactory();
	static ReasonerFactory rf6 = new ReasonerFactory();
	static ReasonerFactory rf8 = new ReasonerFactory();

	public static void main(String[] args) throws Exception {

		try {
			File inputOntologyFile = new File("data/knowledgebaseK1.owl");

			// ReasonerFactory for Hermit
			ReasonerFactory rf = new ReasonerFactory();

			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

			OWLDataFactory df = manager.getOWLDataFactory();

			OWLOntology ontology = manager.loadOntologyFromOntologyDocument(inputOntologyFile);

			// Configuration for Hermit
			Configuration configuration = new Configuration();
			configuration.throwInconsistentOntologyException = false;

			// OWLReasoner for Hermit (the format is also used by JFact as well)
			OWLReasoner reasoner = rf.createReasoner(ontology, configuration);

			System.out.println(
					"Is ontology (file name: " + inputOntologyFile + ") consistent? " + reasoner.isConsistent());

			ExplanationGenerator<OWLAxiom> explainInconsistency = new InconsistentOntologyExplanationGeneratorFactory(
					rf, 1000000000000000000L).createExplanationGenerator(ontology);

			// Set the limit of entailment should be here
			Set<Explanation<OWLAxiom>> explanations = explainInconsistency
					.getExplanations(df.getOWLSubClassOfAxiom(df.getOWLThing(), df.getOWLNothing()), 941);

			System.out.println("Explanation of inconsistency (MI(K)): " + explanations);

			SizeOfK.sizeK(ontologyAxiomSet);
			ManageOWL.owlsetmanager(ontology, ontologyAxiomSet);

			System.out.println("                                   ");
			System.out.println("===============================================================");
			System.out.println("==============INCONSISTENCY MEASURES FOR ONTOLOGY==============");
			System.out.println("===============================================================");

			System.out.println("Explanation of inconsistency (MI(K)): " + explanations);

			// Explanation is M in MI(K), while explanations is MI(K)
			for (Explanation<OWLAxiom> explanation : explanations) {

				// arrayOfExplanation is M in MI(K)
				arrayOfExplanation = explanation.getAxioms();
				System.out.println("-----------------------------------------------------------------------------");
				System.out.println("MI(K) subset: " + arrayOfExplanation);

				// arrayOfExplanationSet is MI(K)set
				arrayOfExplanationSet.add(arrayOfExplanation);
				System.out.println("-----------------------------------------------------------------------------");

				System.out.println("Axioms causing the inconsistency: ");
				for (OWLAxiom causingAxiom : arrayOfExplanation) {
					System.out.println(causingAxiom);
					mikAxiomSet.add(causingAxiom);
					if ((causingAxiom.isBottomEntity() == true) || (causingAxiom.isTopEntity() == true)) {
						topBottom.add(causingAxiom);
					}

					inconsistentClass = (Set<OWLClass>) causingAxiom.getClassesInSignature();
					for (OWLClass theClass : inconsistentClass) {
						mikClassSet.add(theClass);
					}
					inconsistentIndividual = (Set<OWLNamedIndividual>) causingAxiom.getIndividualsInSignature();
					for (OWLNamedIndividual theIndividual : inconsistentIndividual) {

						mikIndividualSet.add(theIndividual);
					}
					inconsistentObjectProperty = (Set<OWLObjectProperty>) causingAxiom.getObjectPropertiesInSignature();
					for (OWLObjectProperty theObjectProperty : inconsistentObjectProperty) {
						mikObjectPropertySet.add(theObjectProperty);
					}
				}

			}

			System.out.println("theClass: " + mikClassSet);

			Drastic.Id_measure(reasoner);
			MI.Imi_measure(ontology, explanations);
			MIc.Imic_measure(arrayOfExplanation, explanations);
			Df.Idf_measure(ontologyAxiomSet, arrayOfExplanation, explanations, rf6, null);
			Problematic.Ip_measure(mikAxiomSet);
			IR.Iir_measure(explanations, ontologyAxiomSet);
			MC.Imc_measure(explanations, ontologyAxiomSet, rf6, null, rf8, null, configuration, null);
			Nc.Inc_measure(ontologyAxiomSet, rf6, null);
			Mv.Imv_measure(mikClassSet, mikIndividualSet, mikObjectPropertySet, ontologyAxiomSet);
			IDmcs.IDmcs_measure(mikClassSet, mikIndividualSet, mikObjectPropertySet, ontologyAxiomSet, rf3, null, rf5,
					null, configuration, null);

			System.out.println("***************************************************************");

		} catch (NoSuchElementException e) {
			System.out.println("NoSuchElementException: " + e.getMessage());
		} catch (InconsistentOntologyException f) {
			System.out.println("InconsistentOntologyException: " + f.getMessage());
		} catch (OWLOntologyCreationException g) {
			System.out.println("InconsistentOntologyException: " + g.getMessage());
		} catch (ExplanationGeneratorInterruptedException h) {
			System.out.println("ExplanationGeneratorInterruptedException: " + h.getMessage());
		} catch (ReasonerInterruptedException i) {
			System.out.println("ReasonerInterruptedException: " + i.getMessage());
		} catch (ExplanationException k) {
			System.out.println("ExplanationException: " + k.getMessage());
		} catch (TimeOutException l) {
			System.out.println("TimeOutException: " + l.getMessage());
		} catch (OutOfMemoryError m) {
			System.out.println("OutOfMemoryError: " + m.getMessage());
		}

	}

}
