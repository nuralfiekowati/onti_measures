package ontologybasedinconsistencymeasures;

import java.io.File;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
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

public class InconsistencyMeasureHermiT {

	private static final Logger logger = Logger.getLogger(InconsistencyMeasureHermiT.class);

	static Set<OWLAxiom> arrayOfExplanation = null;
	static Set<Set<OWLAxiom>> arrayOfExplanationSet = new HashSet<>(3000000, 1000000F);
	static Set<OWLAxiom> mikAxiomSet = new HashSet<>();
	static Set<OWLAxiom> topBottom = new HashSet<>();
	static Set<OWLClass> mikClassSet = new HashSet<>();
	static Set<OWLNamedIndividual> mikIndividualSet = new HashSet<>();
	static Set<OWLObjectProperty> mikObjectPropertySet = new HashSet<>();
	static Set<OWLClass> inconsistentClass = null;
	static Set<OWLNamedIndividual> inconsistentIndividual = null;
	static Set<OWLObjectProperty> inconsistentObjectProperty = null;
	static Set<OWLAxiom> ontologyAxiomSet = new HashSet<>(3000000, 1000000F);
	static ReasonerFactory rf3 = new ReasonerFactory();
	static ReasonerFactory rf5 = new ReasonerFactory();
	static ReasonerFactory rf6 = new ReasonerFactory();
	static ReasonerFactory rf8 = new ReasonerFactory();

	public static void main(String[] args) throws Exception {

		BasicConfigurator.configure();

		try {
			File inputOntologyFile = new File("data/knowledgebaseK4.owl");

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

			logger.info("Is ontology (file name: " + inputOntologyFile + ") consistent? " + reasoner.isConsistent());

			ExplanationGenerator<OWLAxiom> explainInconsistency = new InconsistentOntologyExplanationGeneratorFactory(
					rf, 1000000000000000000L).createExplanationGenerator(ontology);

			// Set the limit of entailment should be here
			Set<Explanation<OWLAxiom>> explanations = explainInconsistency
					.getExplanations(df.getOWLSubClassOfAxiom(df.getOWLThing(), df.getOWLNothing()), 941);

			logger.info("Explanation of inconsistency (MI(K)): " + explanations);

			SizeOfK.sizeK(ontologyAxiomSet);
			ManageOWL.owlSetManager(ontology, ontologyAxiomSet);

			logger.info("                                   ");
			logger.info("===============================================================");
			logger.info("=========================ONTI MEASURES=========================");
			logger.info("===============================================================");

			logger.info("Explanation of inconsistency (MI(K)): " + explanations);

			// Explanation is M in MI(K), while explanations is MI(K)
			for (Explanation<OWLAxiom> explanation : explanations) {

				// arrayOfExplanation is M in MI(K)
				arrayOfExplanation = explanation.getAxioms();
				logger.info("-----------------------------------------------------------------------------");
				logger.info("MI(K) subset: " + arrayOfExplanation);

				// arrayOfExplanationSet is MI(K)set
				arrayOfExplanationSet.add(arrayOfExplanation);
				logger.info("-----------------------------------------------------------------------------");

				logger.info("Axioms causing the inconsistency: ");
				for (OWLAxiom causingAxiom : arrayOfExplanation) {
					logger.info(causingAxiom);
					mikAxiomSet.add(causingAxiom);
					if ((causingAxiom.isBottomEntity()) || (causingAxiom.isTopEntity())) {
						topBottom.add(causingAxiom);
					}

					inconsistentClass = causingAxiom.getClassesInSignature();
					for (OWLClass theClass : inconsistentClass) {
						mikClassSet.add(theClass);
					}
					inconsistentIndividual = causingAxiom.getIndividualsInSignature();
					for (OWLNamedIndividual theIndividual : inconsistentIndividual) {

						mikIndividualSet.add(theIndividual);
					}
					inconsistentObjectProperty = causingAxiom.getObjectPropertiesInSignature();
					for (OWLObjectProperty theObjectProperty : inconsistentObjectProperty) {
						mikObjectPropertySet.add(theObjectProperty);
					}
				}

			}

			logger.info("theClass: " + mikClassSet);

			Drastic.idMeasure(reasoner);
			MI.imiMeasure(ontology, explanations);
			MIc.imicMeasure(arrayOfExplanation, explanations);
			Df.idfMeasure(ontologyAxiomSet, arrayOfExplanation, explanations, rf6, null, null);
			Problematic.ipMeasure(mikAxiomSet);
			IR.iirMeasure(explanations, ontologyAxiomSet);
			MC.imcMeasure(explanations, ontologyAxiomSet, rf6, null, null, rf8, null, null, configuration, null, null);
			Nc.incMeasure(ontologyAxiomSet, rf6, null, null);
			Mv.imvMeasure(mikClassSet, mikIndividualSet, mikObjectPropertySet, ontologyAxiomSet);
			IDmcs.idMcsMeasure(mikClassSet, mikIndividualSet, mikObjectPropertySet, ontologyAxiomSet, rf3, null, null,
					rf5, null, null, configuration, null, null);

			logger.info("***************************************************************");

		} catch (NoSuchElementException e) {
			logger.error("NoSuchElementException: " + e.getMessage());
		} catch (InconsistentOntologyException f) {
			logger.error("InconsistentOntologyException: " + f.getMessage());
		} catch (OWLOntologyCreationException g) {
			logger.error("InconsistentOntologyException: " + g.getMessage());
		} catch (ExplanationGeneratorInterruptedException h) {
			logger.error("ExplanationGeneratorInterruptedException: " + h.getMessage());
		} catch (ReasonerInterruptedException i) {
			logger.error("ReasonerInterruptedException: " + i.getMessage());
		} catch (ExplanationException k) {
			logger.error("ExplanationException: " + k.getMessage());
		} catch (TimeOutException l) {
			logger.error("TimeOutException: " + l.getMessage());
		} catch (OutOfMemoryError m) {
			logger.error("OutOfMemoryError: " + m.getMessage());
		}

	}

}