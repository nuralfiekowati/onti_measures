package ontologybasedinconsistencymeasures;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;

class ManageOWL {

	private ManageOWL() {
		throw new IllegalStateException("ManageOWL");
	}

	private static final Logger logger = Logger.getLogger(ManageOWL.class);

	public static void owlSetManager(OWLOntology ontology, Set<OWLAxiom> ontologyAxiomSet) {

		Set<OWLSubClassOfAxiom> owlSubClassOfAxiomSet = ontology.getAxioms(AxiomType.SUBCLASS_OF);
		for (OWLSubClassOfAxiom owlSubClassOf : owlSubClassOfAxiomSet) {
			ontologyAxiomSet.add(owlSubClassOf);
		}

		Set<OWLEquivalentClassesAxiom> owlEquivalentClassesAxiomSet = ontology.getAxioms(AxiomType.EQUIVALENT_CLASSES);
		for (OWLEquivalentClassesAxiom owlEquivalentClasses : owlEquivalentClassesAxiomSet) {
			ontologyAxiomSet.add(owlEquivalentClasses);
		}

		Set<OWLDisjointClassesAxiom> owlDisjointClassesAxiomSet = ontology.getAxioms(AxiomType.DISJOINT_CLASSES);
		for (OWLDisjointClassesAxiom owlDisjointClasses : owlDisjointClassesAxiomSet) {
			ontologyAxiomSet.add(owlDisjointClasses);
		}

		Set<OWLDisjointUnionAxiom> owlDisjointUnionAxiomSet = ontology.getAxioms(AxiomType.DISJOINT_UNION);
		for (OWLDisjointUnionAxiom owlDisjointUnion : owlDisjointUnionAxiomSet) {
			ontologyAxiomSet.add(owlDisjointUnion);
		}

		Set<OWLDifferentIndividualsAxiom> owlDifferentIndividualsAxiomSet = ontology
				.getAxioms(AxiomType.DIFFERENT_INDIVIDUALS);
		for (OWLDifferentIndividualsAxiom owlDifferentIndividuals : owlDifferentIndividualsAxiomSet) {
			ontologyAxiomSet.add(owlDifferentIndividuals);
		}

		Set<OWLSameIndividualAxiom> owlSameIndividualAxiomSet = ontology.getAxioms(AxiomType.SAME_INDIVIDUAL);
		for (OWLSameIndividualAxiom owlSameIndividual : owlSameIndividualAxiomSet) {
			ontologyAxiomSet.add(owlSameIndividual);
		}

		Set<OWLClassAssertionAxiom> owlClassAssertionAxiomSet = ontology.getAxioms(AxiomType.CLASS_ASSERTION);
		for (OWLClassAssertionAxiom owlClassAssertion : owlClassAssertionAxiomSet) {
			ontologyAxiomSet.add(owlClassAssertion);
		}

		Set<OWLObjectPropertyAssertionAxiom> owlObjectPropertyAssertionAxiomSet = ontology
				.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION);
		for (OWLObjectPropertyAssertionAxiom owlObjectPropertyAssertion : owlObjectPropertyAssertionAxiomSet) {
			ontologyAxiomSet.add(owlObjectPropertyAssertion);
		}

		Set<OWLNegativeObjectPropertyAssertionAxiom> owlNegativeObjectPropertyAssertionAxiomSet = ontology
				.getAxioms(AxiomType.NEGATIVE_OBJECT_PROPERTY_ASSERTION);
		for (OWLNegativeObjectPropertyAssertionAxiom owlNegativeObjectPropertyAssertion : owlNegativeObjectPropertyAssertionAxiomSet) {
			ontologyAxiomSet.add(owlNegativeObjectPropertyAssertion);
		}

		Set<OWLSubObjectPropertyOfAxiom> owlSubObjectPropertyOfAxiomSet = ontology
				.getAxioms(AxiomType.SUB_OBJECT_PROPERTY);
		for (OWLSubObjectPropertyOfAxiom owlSubObjectPropertyOf : owlSubObjectPropertyOfAxiomSet) {
			ontologyAxiomSet.add(owlSubObjectPropertyOf);
		}

		Set<OWLEquivalentObjectPropertiesAxiom> owlEquivalentObjectPropertyAxiomSet = ontology
				.getAxioms(AxiomType.EQUIVALENT_OBJECT_PROPERTIES);
		for (OWLEquivalentObjectPropertiesAxiom owlEquivalentObjectProperty : owlEquivalentObjectPropertyAxiomSet) {
			ontologyAxiomSet.add(owlEquivalentObjectProperty);
		}

		Set<OWLDisjointObjectPropertiesAxiom> owlDisjointObjectPropertiesSet = ontology
				.getAxioms(AxiomType.DISJOINT_OBJECT_PROPERTIES);
		for (OWLDisjointObjectPropertiesAxiom owlDisjointObjectProperties : owlDisjointObjectPropertiesSet) {
			ontologyAxiomSet.add(owlDisjointObjectProperties);
		}

		Set<OWLInverseObjectPropertiesAxiom> owlInverseObjectPropertiesAxiomSet = ontology
				.getAxioms(AxiomType.INVERSE_OBJECT_PROPERTIES);
		for (OWLInverseObjectPropertiesAxiom owlInverseObjectProperties : owlInverseObjectPropertiesAxiomSet) {
			ontologyAxiomSet.add(owlInverseObjectProperties);
		}

		Set<OWLObjectPropertyDomainAxiom> owlObjectPropertyDomainAxiomSet = ontology
				.getAxioms(AxiomType.OBJECT_PROPERTY_DOMAIN);
		for (OWLObjectPropertyDomainAxiom owlObjectPropertyDomain : owlObjectPropertyDomainAxiomSet) {
			ontologyAxiomSet.add(owlObjectPropertyDomain);
		}

		Set<OWLObjectPropertyRangeAxiom> owlObjectPropertyRangeAxiomSet = ontology
				.getAxioms(AxiomType.OBJECT_PROPERTY_RANGE);
		for (OWLObjectPropertyRangeAxiom owlObjectPropertyRange : owlObjectPropertyRangeAxiomSet) {
			ontologyAxiomSet.add(owlObjectPropertyRange);
		}

		Set<OWLFunctionalObjectPropertyAxiom> owlFunctionalObjectPropertyAxiomSet = ontology
				.getAxioms(AxiomType.FUNCTIONAL_OBJECT_PROPERTY);
		for (OWLFunctionalObjectPropertyAxiom owlFunctionalObjectProperty : owlFunctionalObjectPropertyAxiomSet) {
			ontologyAxiomSet.add(owlFunctionalObjectProperty);
		}

		Set<OWLInverseFunctionalObjectPropertyAxiom> owlInverseFunctionalObjectPropertyAxiomSet = ontology
				.getAxioms(AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY);
		for (OWLInverseFunctionalObjectPropertyAxiom owlInverseFunctionalObjectProperty : owlInverseFunctionalObjectPropertyAxiomSet) {
			ontologyAxiomSet.add(owlInverseFunctionalObjectProperty);
		}

		Set<OWLReflexiveObjectPropertyAxiom> owlReflexiveObjectPropertyAxiomSet = ontology
				.getAxioms(AxiomType.REFLEXIVE_OBJECT_PROPERTY);
		for (OWLReflexiveObjectPropertyAxiom owlReflexiveObjectProperty : owlReflexiveObjectPropertyAxiomSet) {
			ontologyAxiomSet.add(owlReflexiveObjectProperty);
		}

		Set<OWLIrreflexiveObjectPropertyAxiom> owlIrreflexiveObjectPropertyAxiomSet = ontology
				.getAxioms(AxiomType.IRREFLEXIVE_OBJECT_PROPERTY);
		for (OWLIrreflexiveObjectPropertyAxiom owlIrreflexiveObjectProperty : owlIrreflexiveObjectPropertyAxiomSet) {
			ontologyAxiomSet.add(owlIrreflexiveObjectProperty);
		}

		Set<OWLSymmetricObjectPropertyAxiom> owlSymmetricObjectPropertyAxiomSet = ontology
				.getAxioms(AxiomType.SYMMETRIC_OBJECT_PROPERTY);
		for (OWLSymmetricObjectPropertyAxiom owlSymmetricObjectProperty : owlSymmetricObjectPropertyAxiomSet) {
			ontologyAxiomSet.add(owlSymmetricObjectProperty);
		}

		Set<OWLAsymmetricObjectPropertyAxiom> owlAsymmetricObjectPropertyAxiomSet = ontology
				.getAxioms(AxiomType.ASYMMETRIC_OBJECT_PROPERTY);
		for (OWLAsymmetricObjectPropertyAxiom owlAsymmetricObjectProperty : owlAsymmetricObjectPropertyAxiomSet) {
			ontologyAxiomSet.add(owlAsymmetricObjectProperty);
		}

		Set<OWLTransitiveObjectPropertyAxiom> owlTransitiveObjectPropertyAxiomSet = ontology
				.getAxioms(AxiomType.TRANSITIVE_OBJECT_PROPERTY);
		for (OWLTransitiveObjectPropertyAxiom owlTransitiveObjectProperty : owlTransitiveObjectPropertyAxiomSet) {
			ontologyAxiomSet.add(owlTransitiveObjectProperty);
		}

		Set<OWLAxiom> theSet = new HashSet<>(3000000, 1000000F);
		theSet.addAll(ontologyAxiomSet);

		logger.info("----------------------------------------------------------------");

		int owlSubClassOfAxiomSetSize = owlSubClassOfAxiomSet.size();
		int owlEquivalentClassesAxiomSetSize = owlEquivalentClassesAxiomSet.size();
		int owlDisjointClassesAxiomSetSize = owlDisjointClassesAxiomSet.size();
		int owlDisjointUnionAxiomSetSize = owlDisjointUnionAxiomSet.size();
		int owlDifferentIndividualsAxiomSetSize = owlDifferentIndividualsAxiomSet.size();
		int owlSameIndividualAxiomSetSize = owlSameIndividualAxiomSet.size();
		int owlClassAssertionAxiomSetSize = owlClassAssertionAxiomSet.size();
		int owlObjectPropertyAssertionAxiomSetSize = owlObjectPropertyAssertionAxiomSet.size();
		int owlNegativeObjectPropertyAssertionAxiomSetSize = owlNegativeObjectPropertyAssertionAxiomSet.size();
		int owlSubObjectPropertyOfAxiomSetSize = owlSubObjectPropertyOfAxiomSet.size();
		int owlEquivalentObjectPropertyAxiomSetSize = owlEquivalentObjectPropertyAxiomSet.size();
		int owlDisjointObjectPropertiesSetSize = owlDisjointObjectPropertiesSet.size();
		int owlInverseObjectPropertiesAxiomSetSize = owlInverseObjectPropertiesAxiomSet.size();
		int owlObjectPropertyDomainAxiomSetSize = owlObjectPropertyDomainAxiomSet.size();
		int owlObjectPropertyRangeAxiomSetSize = owlObjectPropertyRangeAxiomSet.size();
		int owlFunctionalObjectPropertyAxiomSetSize = owlFunctionalObjectPropertyAxiomSet.size();
		int owlInverseFunctionalObjectPropertyAxiomSetSize = owlInverseFunctionalObjectPropertyAxiomSet.size();
		int owlReflexiveObjectPropertyAxiomSetSize = owlReflexiveObjectPropertyAxiomSet.size();
		int owlIrreflexiveObjectPropertyAxiomSetSize = owlIrreflexiveObjectPropertyAxiomSet.size();
		int owlSymmetricObjectPropertyAxiomSetSize = owlSymmetricObjectPropertyAxiomSet.size();
		int owlAsymmetricObjectPropertyAxiomSetSize = owlAsymmetricObjectPropertyAxiomSet.size();
		int owlTransitiveObjectPropertyAxiomSetSize = owlTransitiveObjectPropertyAxiomSet.size();

		logger.info("OWLSubClassOfAxiomSetSize: " + owlSubClassOfAxiomSetSize);
		logger.info("OWLEquivalentClassesAxiomSetSize: " + owlEquivalentClassesAxiomSetSize);
		logger.info("OWLDisjointClassesAxiomSetSize: " + owlDisjointClassesAxiomSetSize);
		logger.info("OWLDisjointUnionAxiomSetSize: " + owlDisjointUnionAxiomSetSize);
		logger.info("OWLDifferentIndividualsAxiomSetSize: " + owlDifferentIndividualsAxiomSetSize);
		logger.info("OWLSameIndividualAxiomSetSize: " + owlSameIndividualAxiomSetSize);
		logger.info("OWLClassAssertionAxiomSetSize: " + owlClassAssertionAxiomSetSize);
		logger.info("OWLObjectPropertyAssertionAxiomSetSize: " + owlObjectPropertyAssertionAxiomSetSize);
		logger.info(
				"OWLNegativeObjectPropertyAssertionAxiomSetSize: " + owlNegativeObjectPropertyAssertionAxiomSetSize);
		logger.info("OWLSubObjectPropertyOfAxiomSetSize: " + owlSubObjectPropertyOfAxiomSetSize);
		logger.info("OWLEquivalentObjectPropertyAxiomSetSize: " + owlEquivalentObjectPropertyAxiomSetSize);
		logger.info("OWLDisjointObjectPropertiesSetSize: " + owlDisjointObjectPropertiesSetSize);
		logger.info("OWLInverseObjectPropertiesAxiomSetSize: " + owlInverseObjectPropertiesAxiomSetSize);
		logger.info("OWLObjectPropertyDomainAxiomSetSize: " + owlObjectPropertyDomainAxiomSetSize);
		logger.info("OWLObjectPropertyRangeAxiomSetSize: " + owlObjectPropertyRangeAxiomSetSize);
		logger.info("OWLFunctionalObjectPropertyAxiomSetSize: " + owlFunctionalObjectPropertyAxiomSetSize);
		logger.info(
				"OWLInverseFunctionalObjectPropertyAxiomSetSize: " + owlInverseFunctionalObjectPropertyAxiomSetSize);
		logger.info("OWLReflexiveObjectPropertyAxiomSetSize: " + owlReflexiveObjectPropertyAxiomSetSize);
		logger.info("OWLIrreflexiveObjectPropertyAxiomSetSize: " + owlIrreflexiveObjectPropertyAxiomSetSize);
		logger.info("OWLSymmetricObjectPropertyAxiomSetSize: " + owlSymmetricObjectPropertyAxiomSetSize);
		logger.info("OWLAsymmetricObjectPropertyAxiomSetSize: " + owlAsymmetricObjectPropertyAxiomSetSize);
		logger.info("OWLTransitiveObjectPropertyAxiomSetSize: " + owlTransitiveObjectPropertyAxiomSetSize);

		logger.info("----------------------------------------------------------------");

	}

}
