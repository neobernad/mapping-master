package org.mm.renderer.owlapi;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.AnnotationFactNode;
import org.mm.parser.node.FactNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.OWLAnnotationPropertyNode;
import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLClassDeclarationNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLEquivalentClassesNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLNamedIndividualNode;
import org.mm.parser.node.OWLPropertyAssertionNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.OWLSubclassOfNode;
import org.mm.parser.node.TypeNode;
import org.mm.renderer.InternalRendererException;
import org.mm.renderer.OWLDeclarationRenderer;
import org.mm.renderer.ReferenceRendererConfiguration;
import org.mm.renderer.Renderer;
import org.mm.renderer.RendererException;
import org.mm.rendering.owlapi.OWLAPIRendering;
import org.mm.rendering.owlapi.OWLAnnotationValueRendering;
import org.mm.rendering.owlapi.OWLClassExpressionRendering;
import org.mm.rendering.owlapi.OWLClassRendering;
import org.mm.rendering.owlapi.OWLNamedIndividualRendering;
import org.mm.rendering.owlapi.OWLPropertyAssertionRendering;
import org.mm.rendering.owlapi.OWLPropertyRendering;
import org.mm.ss.SpreadSheetDataSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyAssertionObject;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class OWLAPIRenderer extends ReferenceRendererConfiguration implements Renderer, OWLDeclarationRenderer,
      MappingMasterParserConstants
{
   public static final int NameEncodings[] = { MM_LOCATION, MM_LITERAL, RDF_ID, RDFS_LABEL };
   public static final int ReferenceValueTypes[] = { OWL_CLASS, OWL_NAMED_INDIVIDUAL, OWL_OBJECT_PROPERTY,
         OWL_DATA_PROPERTY, XSD_INT, XSD_STRING, XSD_FLOAT, XSD_DOUBLE, XSD_SHORT, XSD_BOOLEAN, XSD_TIME, XSD_DATETIME,
         XSD_DURATION };
   public static final int PropertyTypes[] = { OWL_OBJECT_PROPERTY, OWL_DATA_PROPERTY };
   public static final int PropertyValueTypes[] = ReferenceValueTypes;
   public static final int DataPropertyValueTypes[] = { XSD_STRING, XSD_BYTE, XSD_SHORT, XSD_INT, XSD_FLOAT, XSD_DOUBLE,
         XSD_BOOLEAN, XSD_TIME, XSD_DATETIME, XSD_DATE, XSD_DURATION };

   private SpreadSheetDataSource dataSource;

   private final OWLAPIObjectFactory objectFactory;
   private final OWLAPIEntityRenderer entityRenderer;
   private final OWLAPIClassExpressionRenderer classExpressionRenderer;
   private final OWLAPIReferenceRenderer referenceRenderer;

   public OWLAPIRenderer(OWLOntology ontology, SpreadSheetDataSource dataSource)
   {
      this.dataSource = dataSource;
      objectFactory = new OWLAPIObjectFactory(ontology);
      referenceRenderer = new OWLAPIReferenceRenderer(dataSource, objectFactory);
      entityRenderer = new OWLAPIEntityRenderer(referenceRenderer, objectFactory);
      classExpressionRenderer = new OWLAPIClassExpressionRenderer(referenceRenderer, objectFactory);
   }

   @Override
   public SpreadSheetDataSource getDataSource()
   {
      return dataSource;
   }

   @Override
   public ReferenceRendererConfiguration getReferenceRendererConfiguration()
   {
      return this;
   }

   @Override
   public Optional<OWLAPIRendering> render(MMExpressionNode mmExpressionNode) throws RendererException
   {
      if (mmExpressionNode.hasOWLClassDeclaration()) {
         return renderOWLClassDeclaration(mmExpressionNode.getOWLClassDeclarationNode());
      } else if (mmExpressionNode.hasOWLIndividualDeclaration()) {
         return renderOWLIndividualDeclaration(mmExpressionNode.getOWLIndividualDeclarationNode());
      } else {
         throw new InternalRendererException("unknown child for node: " + mmExpressionNode.getNodeName());
      }
   }

   @Override
   public Optional<OWLAPIRendering> renderOWLClassDeclaration(OWLClassDeclarationNode declarationNode)
         throws RendererException
   {
      Set<OWLAxiom> axioms = new HashSet<>();
      Optional<OWLClassRendering> declaredClassRendering = entityRenderer.renderOWLClass(declarationNode.getOWLClassNode(), true);
      if (!declaredClassRendering.isPresent()) {
         return Optional.empty();
      }

      OWLClass declaredClass = declaredClassRendering.get().getOWLClass();
      /*
       * By default, the class declaration node will produce one OWL class declaration axiom
       */
      OWLDeclarationAxiom declaredAxiom = objectFactory.getOWLDeclarationAxiom(declaredClass);
      axioms.add(declaredAxiom);

      /*
       * Add any existing axioms in the class rendering object
       */
      axioms.addAll(declaredClassRendering.get().getOWLAxioms());

      /*
       * In case the class declaration node has a sub-class axiom
       */
      if (declarationNode.hasOWLSubclassOfNodes()) {
         for (OWLSubclassOfNode subclassOfNode : declarationNode.getOWLSubclassOfNodes()) {
            for (OWLClassExpressionNode classExpressionNode : subclassOfNode.getClassExpressionNodes()) {
               Optional<OWLClassExpressionRendering> classExpressionRendering =
                     classExpressionRenderer.renderOWLClassExpression(classExpressionNode);
               if (!classExpressionRendering.isPresent()) {
                  continue; // just ignore it
               }
               OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();
               OWLSubClassOfAxiom axiom = objectFactory.getOWLSubClassOfAxiom(declaredClass, classExpression);
               axioms.add(axiom);
               axioms.addAll(classExpressionRendering.get().getOWLAxioms()); // add any existing axioms
            }
         }
      }
      /*
       * In case the class declaration has an equivalent class axiom
       */
      if (declarationNode.hasOWLEquivalentClassesNode()) {
         for (OWLEquivalentClassesNode equivalentClassesNode : declarationNode.getOWLEquivalentClassesNodes()) {
            for (OWLClassExpressionNode node : equivalentClassesNode.getClassExpressionNodes()) {
               Optional<OWLClassExpressionRendering> classExpressionRendering = classExpressionRenderer.renderOWLClassExpression(node);
               if (classExpressionRendering.isPresent()) {
                  OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();
                  OWLEquivalentClassesAxiom axiom = objectFactory.getOWLEquivalentClassesAxiom(declaredClass, classExpression);
                  axioms.add(axiom);
                  axioms.addAll(classExpressionRendering.get().getOWLAxioms()); // add any existing axioms
               }
            }
         }
      }
      /*
       * In case the class declaration has an annotation axiom
       */
      if (declarationNode.hasAnnotationFactNodes()) {
         for (AnnotationFactNode annotationFactNode : declarationNode.getAnnotationFactNodes()) {
            OWLAnnotationPropertyNode node = annotationFactNode.getOWLAnnotationPropertyNode();
            Optional<? extends OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLAnnotationProperty(node);
            OWLAnnotationValueNode annotationValueNode = annotationFactNode.getOWLAnnotationValueNode();
            Optional<OWLAnnotationValueRendering> annotationValueRendering = entityRenderer.renderOWLAnnotationValue(annotationValueNode);
            if (propertyRendering.isPresent() && annotationValueRendering.isPresent()) {
               IRI classIri = declaredClass.getIRI();
               OWLAnnotationProperty property = (OWLAnnotationProperty) propertyRendering.get().getOWLProperty();
               OWLAnnotationValue annotationValue = annotationValueRendering.get().getOWLAnnotationValue();
               OWLAnnotationAssertionAxiom axiom = objectFactory.getOWLAnnotationAssertionAxiom(property, classIri, annotationValue);
               axioms.add(axiom);
            }
         }
      }
      return Optional.of(new OWLAPIRendering(axioms));
   }

   @Override
   public Optional<OWLAPIRendering> renderOWLIndividualDeclaration(OWLIndividualDeclarationNode individualDeclarationNode)
         throws RendererException
   {
      Set<OWLAxiom> axioms = new HashSet<>();
      OWLNamedIndividualNode individualNode = individualDeclarationNode.getOWLIndividualNode();
      Optional<OWLNamedIndividualRendering> declaredIndividualRendering = entityRenderer.renderOWLNamedIndividual(individualNode, true);
      if (!declaredIndividualRendering.isPresent()) {
         return Optional.empty();
      }

      OWLNamedIndividual declaredIndividual = declaredIndividualRendering.get().getOWLNamedIndividual();

      /*
       * By default, the individual declaration node will produce one OWL named
       * individual declaration axiom
       */
      OWLDeclarationAxiom declaredAxiom = objectFactory.getOWLDeclarationAxiom(declaredIndividual);
      axioms.add(declaredAxiom);

      /*
       * Add any existing axioms in the class rendering object
       */
      axioms.addAll(declaredIndividualRendering.get().getOWLAxioms());

      if (individualDeclarationNode.hasFacts()) { // We have a Facts clause
         List<FactNode> factNodes = individualDeclarationNode.getFactNodes();
         Set<OWLAxiom> factsAxioms = processFactsClause(declaredIndividual, factNodes);
         axioms.addAll(factsAxioms);
      }

      if (individualDeclarationNode.hasAnnotations()) { // We have an Annotations clause
         List<AnnotationFactNode> annotationFactNodes = individualDeclarationNode.getAnnotationNodes();
         Set<OWLAxiom> annotationsAxioms = processAnnotationClause(declaredIndividual, annotationFactNodes);
         axioms.addAll(annotationsAxioms);
      }

      if (individualDeclarationNode.hasSameAs()) { // We have a SameAs clause
         List<OWLNamedIndividualNode> otherIndividualNodes = individualDeclarationNode.getOWLSameAsNode().getIndividualNodes();
         Set<OWLAxiom> sameAsAxioms = processSameAsClause(declaredIndividual, otherIndividualNodes);
         axioms.addAll(sameAsAxioms);
      }

      if (individualDeclarationNode.hasDifferentFrom()) { // We have a DifferentFrom clause
         List<OWLNamedIndividualNode> otherIndividualNodes = individualDeclarationNode.getOWLDifferentFromNode().getIndividualNodes();
         Set<OWLAxiom> differentFromAxioms = processDifferentFromClause(declaredIndividual, otherIndividualNodes);
         axioms.addAll(differentFromAxioms);
      }

      if (individualDeclarationNode.hasTypes()) { // We have a Types: clause
         List<TypeNode> typesNode = individualDeclarationNode.getTypesNode().getTypeNodes();
         Set<OWLAxiom> typesAxioms = referenceRenderer.processTypesClause(declaredIndividual, typesNode);
         axioms.addAll(typesAxioms);
      }
      return Optional.of(new OWLAPIRendering(axioms));
   }

   /**
    * Create property assertion axioms for the declared individual.
    */
   private Set<OWLAxiom> processFactsClause(OWLNamedIndividual individual, List<FactNode> factNodes)
         throws RendererException
   {
      Set<OWLAxiom> axioms = new HashSet<>();
      for (FactNode factNode : factNodes) {
         OWLPropertyNode propertyNode = factNode.getOWLPropertyNode();
         Optional<? extends OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLProperty(propertyNode);
         if (propertyRendering.isPresent()) {
            OWLProperty property = propertyRendering.get().getOWLProperty();
            OWLPropertyAssertionNode propertyAssertionNode = factNode.getOWLPropertyAssertionObjectNode();
            adjustAssertionType(propertyAssertionNode, property);
            Optional<OWLPropertyAssertionRendering> propertyAssertionRendering = entityRenderer.renderOWLPropertyAssertion(propertyAssertionNode);
            if (propertyAssertionRendering.isPresent()) {
               OWLPropertyAssertionObject propertyAssertion = propertyAssertionRendering.get().getOWLPropertyAssertionObject();
               if (property instanceof OWLObjectProperty) {
                  OWLObjectProperty op = objectFactory.getOWLObjectProperty(property.getIRI());
                  OWLIndividual value = (OWLNamedIndividual) propertyAssertion;
                  OWLObjectPropertyAssertionAxiom axiom = objectFactory.getOWLObjectPropertyAssertionAxiom(op, individual, value);
                  axioms.add(axiom);
               } else if (property instanceof OWLDataProperty) {
                  OWLDataProperty dp = objectFactory.getOWLDataProperty(property.getIRI());
                  OWLLiteral value = (OWLLiteral) propertyAssertion;
                  OWLDataPropertyAssertionAxiom axiom = objectFactory.getOWLDataPropertyAssertionAxiom(dp, individual, value);
                  axioms.add(axiom);
               }
            }
         }
      }
      return axioms;
   }

   private void adjustAssertionType(OWLPropertyAssertionNode propertyAssertionNode, OWLProperty property)
   {
      if (propertyAssertionNode.hasReferenceNode()) {
         if (property instanceof OWLObjectProperty) {
            propertyAssertionNode.getReferenceNode().updateReferenceType(OWL_NAMED_INDIVIDUAL);
         }
      }
   }

   /**
    * Create annotation assertion axioms for the declared individual.
    */
   private Set<OWLAxiom> processAnnotationClause(OWLNamedIndividual individual, List<AnnotationFactNode> annotationFactNodes)
         throws RendererException
   {
      Set<OWLAxiom> axioms = new HashSet<>();
      for (AnnotationFactNode annotationFact : annotationFactNodes) {
         OWLAnnotationPropertyNode annotationPropertyNode = annotationFact.getOWLAnnotationPropertyNode();
         Optional<? extends OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLAnnotationProperty(annotationPropertyNode);
         if (propertyRendering.isPresent()) {
            OWLAnnotationProperty property = (OWLAnnotationProperty) propertyRendering.get().getOWLProperty();
            OWLAnnotationValueNode annotationValueNode = annotationFact.getOWLAnnotationValueNode();
            Optional<OWLAnnotationValueRendering> annotationValueRendering = entityRenderer.renderOWLAnnotationValue(annotationValueNode);
            if (annotationValueRendering.isPresent()) {
               IRI individualIri = individual.getIRI();
               OWLAnnotationValue value = annotationValueRendering.get().getOWLAnnotationValue();
               OWLAnnotationAssertionAxiom axiom = objectFactory.getOWLAnnotationAssertionAxiom(property, individualIri, value);
               axioms.add(axiom);
            }
         }
      }
      return axioms;
   }

   /**
    * Create same individual axioms for the declared individual.
    */
   private Set<OWLAxiom> processSameAsClause(OWLNamedIndividual individual, List<OWLNamedIndividualNode> otherIndividualNodes)
         throws RendererException
   {
      Set<OWLAxiom> axioms = new HashSet<>();
      for (OWLNamedIndividualNode individualNode : otherIndividualNodes) {
         Optional<OWLNamedIndividualRendering> otherIndividual = entityRenderer.renderOWLNamedIndividual(individualNode, false);
         if (otherIndividual.isPresent()) {
            OWLNamedIndividual sameIndividual = otherIndividual.get().getOWLNamedIndividual();
            OWLSameIndividualAxiom axiom = objectFactory.getOWLSameIndividualAxiom(individual, sameIndividual);
            axioms.add(axiom);
         }
      }
      return axioms;
   }

   /**
    * Create different individuals axioms for the declared individual.
    */
   private Set<OWLAxiom> processDifferentFromClause(OWLNamedIndividual individual, List<OWLNamedIndividualNode> otherIndividualNodes)
         throws RendererException
   {
      Set<OWLAxiom> axioms = new HashSet<>();
      for (OWLNamedIndividualNode individualNode : otherIndividualNodes) {
         Optional<OWLNamedIndividualRendering> otherIndividual = entityRenderer.renderOWLNamedIndividual(individualNode, false);
         if (otherIndividual.isPresent()) {
            OWLNamedIndividual differentIndividual = otherIndividual.get().getOWLNamedIndividual();
            OWLDifferentIndividualsAxiom axiom = objectFactory.getOWLDifferentIndividualsAxiom(individual, differentIndividual);
            axioms.add(axiom);
         }
      }
      return axioms;
   }
}
