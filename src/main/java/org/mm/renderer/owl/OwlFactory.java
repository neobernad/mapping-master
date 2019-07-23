package org.mm.renderer.owl;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import org.mm.renderer.internal.IriValue;
import org.mm.renderer.internal.LiteralValue;
import org.mm.renderer.internal.PlainLiteralValue;
import org.mm.renderer.internal.PropertyName;
import org.mm.renderer.internal.QName;
import org.mm.renderer.internal.ReferencedValue;
import org.mm.renderer.internal.Value;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class OwlFactory {

   private final OwlEntityResolver entityResolver;

   private final OWLDataFactory owlDataFactory = OWLManager.getOWLDataFactory();

   public OwlFactory(@Nonnull OwlEntityResolver entityResolver) {
      this.entityResolver = checkNotNull(entityResolver);
   }

   public OWLClass getOWLClass(Value value) {
      if (value instanceof QName) {
         return fetchOWLClass(((QName) value).getString());
      } else if (value instanceof ReferencedValue) {
         return createOWLClass(((ReferencedValue) value).getString());
      } else if (value instanceof IriValue) {
         return createOWLClass(((IriValue) value).getString());
      }
      throw new RuntimeException("Programming error: Creating OWL class using "
            + value.getClass() + " is not yet implemented");
   }

   private OWLClass fetchOWLClass(String prefixedName) {
      return entityResolver.resolveUnchecked(prefixedName, OWLClass.class);
   }

   private OWLClass createOWLClass(String prefixedName) {
      return entityResolver.createUnchecked(prefixedName, OWLClass.class);
   }

   private OWLClass createOWLClass(IRI classIri) {
      return owlDataFactory.getOWLClass(classIri);
   }

   public OWLProperty getOWLProperty(Value value) {
      if (value instanceof QName) {
         return fetchOWLProperty(((QName) value).getString());
      } else if (value instanceof ReferencedValue) {
         return createOWLProperty((ReferencedValue) value);
      }
      throw new RuntimeException("Programming error: Creating OWL property using "
            + value.getClass() + " is not yet implemented");
   }

   private OWLProperty fetchOWLProperty(String prefixedName) {
      return entityResolver.resolveUnchecked(prefixedName, OWLProperty.class);
   }

   private OWLProperty createOWLProperty(ReferencedValue referencedValue) {
      if (referencedValue instanceof PropertyName) {
         PropertyName propertyName = (PropertyName) referencedValue;
         if (propertyName.isDataProperty()) {
            return createOWLDataProperty(propertyName.getString());
         } else if (propertyName.isObjectProperty()) {
            return createOWLObjectProperty(propertyName.getString());
         } else if (propertyName.isAnnotationProperty()) {
            return createOWLAnnotationProperty(propertyName.getString());
         }
      }
      throw new RuntimeException("Programming error: Unknown property type");
   }

   public OWLDataProperty getOWLDataProperty(Value value) {
      if (value instanceof QName) {
         return fetchOWLDataProperty(((QName) value).getString());
      } else if (value instanceof ReferencedValue) {
         return createOWLDataProperty(((ReferencedValue) value).getString());
      } else if (value instanceof IriValue) {
         return createOWLDataProperty(((IriValue) value).getString());
      }
      throw new RuntimeException("Programming error: Creating OWL data property using "
            + value.getClass() + " is not yet implemented");
   }

   private OWLDataProperty fetchOWLDataProperty(String prefixedName) {
      return entityResolver.resolveUnchecked(prefixedName, OWLDataProperty.class);
   }

   private OWLDataProperty createOWLDataProperty(String prefixedName) {
      return entityResolver.createUnchecked(prefixedName, OWLDataProperty.class);
   }

   private OWLDataProperty createOWLDataProperty(IRI propertyIri) {
      return owlDataFactory.getOWLDataProperty(propertyIri);
   }

   public OWLObjectProperty getOWLObjectProperty(Value value) {
      if (value instanceof QName) {
         return fetchOWLObjectProperty(((QName) value).getString());
      } else if (value instanceof ReferencedValue) {
         return createOWLObjectProperty(((ReferencedValue) value).getString());
      } else if (value instanceof IriValue) {
         return createOWLObjectProperty(((IriValue) value).getString());
      }
      throw new RuntimeException("Programming error: Creating OWL data property using "
            + value.getClass() + " is not yet implemented");
   }

   private OWLObjectProperty fetchOWLObjectProperty(String prefixedName) {
      return entityResolver.resolveUnchecked(prefixedName, OWLObjectProperty.class);
   }

   private OWLObjectProperty createOWLObjectProperty(String prefixedName) {
      return entityResolver.createUnchecked(prefixedName, OWLObjectProperty.class);
   }

   private OWLObjectProperty createOWLObjectProperty(IRI propertyIri) {
      return owlDataFactory.getOWLObjectProperty(propertyIri);
   }

   public OWLAnnotationProperty getOWLAnnotationProperty(Value value) {
      if (value instanceof QName) {
         return fetchOWLAnnotationProperty(((QName) value).getString());
      } else if (value instanceof ReferencedValue) {
         return createOWLAnnotationProperty(((ReferencedValue) value).getString());
      } else if (value instanceof IriValue) {
         return createOWLAnnotationProperty(((IriValue) value).getString());
      }
      throw new RuntimeException("Programming error: Creating OWL data property using "
            + value.getClass() + " is not yet implemented");
   }

   private OWLAnnotationProperty fetchOWLAnnotationProperty(String prefixedName) {
      return entityResolver.resolveUnchecked(prefixedName, OWLAnnotationProperty.class);
   }

   private OWLAnnotationProperty createOWLAnnotationProperty(String prefixedName) {
      return entityResolver.createUnchecked(prefixedName, OWLAnnotationProperty.class);
   }

   private OWLAnnotationProperty createOWLAnnotationProperty(IRI propertyIri) {
      return owlDataFactory.getOWLAnnotationProperty(propertyIri);
   }

   public OWLNamedIndividual getOWLNamedIndividual(Value value) {
      if (value instanceof QName) {
         return fetchOWLNamedIndividual(((QName) value).getString());
      } else if (value instanceof ReferencedValue) {
         return createOWLNamedIndividual(((ReferencedValue) value).getString());
      } else if (value instanceof IriValue) {
         return createOWLNamedIndividual(((IriValue) value).getString());
      }
      throw new RuntimeException("Programming error: Creating OWL data property using "
            + value.getClass() + " is not yet implemented");
   }

   private OWLNamedIndividual fetchOWLNamedIndividual(String prefixedName) {
      return entityResolver.resolveUnchecked(prefixedName, OWLNamedIndividual.class);
   }

   private OWLNamedIndividual createOWLNamedIndividual(String prefixedName) {
      return entityResolver.createUnchecked(prefixedName, OWLNamedIndividual.class);
   }

   private OWLNamedIndividual createOWLNamedIndividual(IRI propertyIri) {
      return owlDataFactory.getOWLNamedIndividual(propertyIri);
   }

   public OWLAnnotationValue getOWLAnnotationValue(Value value) {
      if (value instanceof IriValue) {
         return getIri((IriValue) value);
      } else if (value instanceof LiteralValue) {
         return getOWLTypedLiteral((LiteralValue) value);
      } else if (value instanceof PlainLiteralValue) {
         return getOWLPlainLiteral((PlainLiteralValue) value);
      }
      throw new RuntimeException("Programming error: Creating OWL annotation using "
            + value.getClass() + " is not yet implemented");
   }

   private OWLAnnotationValue getIri(IriValue value) {
      return IRI.create(value.getString());
   }

   public OWLLiteral getOWLLiteral(@Nonnull Value value) {
      if (value instanceof LiteralValue) {
         return getOWLTypedLiteral((LiteralValue) value);
      } else if (value instanceof PlainLiteralValue) {
         return getOWLPlainLiteral((PlainLiteralValue) value);
      }
      throw new RuntimeException("Programming error: Creating OWL literal using "
            + value.getClass() + " is not yet implemented");
   }

   public OWLLiteral getOWLTypedLiteral(@Nonnull LiteralValue literal) {
      final String lexicalString = literal.getString();
      final String datatype = literal.getDatatype();
      return owlDataFactory.getOWLLiteral(lexicalString, getOWLDatatype(datatype));
   }

   private OWLDatatype getOWLDatatype(final String datatype) {
      return entityResolver.resolveUnchecked(datatype, OWLDatatype.class);
   }

   public OWLLiteral getOWLPlainLiteral(@Nonnull PlainLiteralValue literal) {
      final String lexicalString = literal.getString();
      final Optional<String> language = literal.getLanguage();
      if (language.isPresent()) {
         return owlDataFactory.getOWLLiteral(lexicalString, language.get());
      } else {
         return owlDataFactory.getOWLLiteral(lexicalString, OWL2Datatype.RDF_PLAIN_LITERAL);
      }
   }

   /*
    * Public methods to create a various number of OWL expression and axioms
    */

   public OWLAnnotation createOWLAnnotation(OWLAnnotationProperty property, OWLAnnotationValue value) {
      return owlDataFactory.getOWLAnnotation(property, value);
   }

   public OWLDeclarationAxiom createOWLDeclarationAxiom(OWLEntity entity) {
      return owlDataFactory.getOWLDeclarationAxiom(entity);
   }

   public OWLObjectComplementOf createOWLObjectComplementOf(OWLClassExpression ce) {
      return owlDataFactory.getOWLObjectComplementOf(ce);
   }

   public OWLObjectIntersectionOf createOWLObjectIntersectionOf(Set<OWLClassExpression> ces) {
      return owlDataFactory.getOWLObjectIntersectionOf(ces);
   }

   public OWLObjectUnionOf createOWLObjectUnionOf(Set<OWLClassExpression> ces) {
      return owlDataFactory.getOWLObjectUnionOf(ces);
   }

   public OWLObjectOneOf createOWLObjectOneOf(Set<OWLNamedIndividual> inds) {
      return owlDataFactory.getOWLObjectOneOf(inds);
   }

   public OWLSubClassOfAxiom createOWLSubClassOfAxiom(OWLClassExpression child,
         OWLClassExpression parent) {
      return owlDataFactory.getOWLSubClassOfAxiom(child, parent);
   }

   public OWLSubObjectPropertyOfAxiom createOWLSubObjectPropertyOfAxiom(
         OWLObjectPropertyExpression child, OWLObjectPropertyExpression parent) {
      return owlDataFactory.getOWLSubObjectPropertyOfAxiom(child, parent);
   }

   public OWLSubDataPropertyOfAxiom createOWLSubDataPropertyOfAxiom(OWLDataPropertyExpression child,
         OWLDataPropertyExpression parent) {
      return owlDataFactory.getOWLSubDataPropertyOfAxiom(child, parent);
   }

   public OWLEquivalentClassesAxiom createOWLEquivalentClassesAxiom(Set<OWLClassExpression> ces) {
      return owlDataFactory.getOWLEquivalentClassesAxiom(ces);
   }

   public OWLEquivalentClassesAxiom createOWLEquivalentClassesAxiom(OWLClassExpression ce1,
         OWLClassExpression ce2) {
      return owlDataFactory.getOWLEquivalentClassesAxiom(ce1, ce2);
   }

   public OWLAnnotationAssertionAxiom createOWLAnnotationAssertionAxiom(OWLEntity entity,
         OWLAnnotation annotation) {
      return owlDataFactory.getOWLAnnotationAssertionAxiom(entity.getIRI(), annotation);
   }

   public OWLAnnotationAssertionAxiom createOWLAnnotationAssertionAxiom(
         OWLAnnotationSubject subject, OWLAnnotation annotation) {
      return owlDataFactory.getOWLAnnotationAssertionAxiom(subject, annotation);
   }

   public OWLClassAssertionAxiom createOWLClassAssertionAxiom(OWLClassExpression ce,
         OWLNamedIndividual ind) {
      return owlDataFactory.getOWLClassAssertionAxiom(ce, ind);
   }

   public OWLObjectPropertyAssertionAxiom createOWLObjectPropertyAssertionAxiom(
         OWLObjectProperty op, OWLIndividual source, OWLIndividual target) {
      return owlDataFactory.getOWLObjectPropertyAssertionAxiom(op, source, target);
   }

   public OWLDataPropertyAssertionAxiom createOWLDataPropertyAssertionAxiom(OWLDataProperty dp,
         OWLIndividual source, OWLLiteral target) {
      return owlDataFactory.getOWLDataPropertyAssertionAxiom(dp, source, target);
   }

   public OWLSameIndividualAxiom createOWLSameIndividualAxiom(OWLIndividual ind1,
         OWLIndividual ind2) {
      return owlDataFactory.getOWLSameIndividualAxiom(ind1, ind2);
   }

   public OWLDifferentIndividualsAxiom createOWLDifferentIndividualsAxiom(OWLIndividual ind1,
         OWLIndividual ind2) {
      return owlDataFactory.getOWLDifferentIndividualsAxiom(ind1, ind2);
   }

   public OWLObjectAllValuesFrom createOWLObjectAllValuesFrom(OWLObjectProperty op,
         OWLClassExpression ce) {
      return owlDataFactory.getOWLObjectAllValuesFrom(op, ce);
   }

   public OWLObjectSomeValuesFrom createOWLObjectSomeValuesFrom(OWLObjectProperty op,
         OWLClassExpression ce) {
      return owlDataFactory.getOWLObjectSomeValuesFrom(op, ce);
   }

   public OWLDataAllValuesFrom createOWLDataAllValuesFrom(OWLDataProperty dp, OWLDatatype dt) {
      return owlDataFactory.getOWLDataAllValuesFrom(dp, dt);
   }

   public OWLDataSomeValuesFrom createOWLDataSomeValuesFrom(OWLDataProperty dp, OWLDatatype dt) {
      return owlDataFactory.getOWLDataSomeValuesFrom(dp, dt);
   }

   public OWLObjectExactCardinality createOWLObjectExactCardinality(int cardinality,
         OWLObjectProperty op) {
      return owlDataFactory.getOWLObjectExactCardinality(cardinality, op);
   }

   public OWLObjectExactCardinality createOWLObjectExactCardinality(int cardinality,
         OWLObjectProperty property, OWLClassExpression classExpression) {
      return owlDataFactory.getOWLObjectExactCardinality(cardinality, property, classExpression);
   }

   public OWLDataExactCardinality createOWLDataExactCardinality(int cardinality,
         OWLDataProperty property, OWLDatatype datatype) {
      return owlDataFactory.getOWLDataExactCardinality(cardinality, property, datatype);
   }

   public OWLObjectMaxCardinality createOWLObjectMaxCardinality(int cardinality,
         OWLObjectProperty op) {
      return owlDataFactory.getOWLObjectMaxCardinality(cardinality, op);
   }

   public OWLObjectMaxCardinality createOWLObjectMaxCardinality(int cardinality,
         OWLObjectProperty property, OWLClassExpression classExpression) {
      return owlDataFactory.getOWLObjectMaxCardinality(cardinality, property, classExpression);
   }

   public OWLDataMaxCardinality createOWLDataMaxCardinality(int cardinality,
         OWLDataProperty property, OWLDatatype datatype) {
      return owlDataFactory.getOWLDataMaxCardinality(cardinality, property, datatype);
   }

   public OWLObjectMinCardinality createOWLObjectMinCardinality(int cardinality,
         OWLObjectProperty op) {
      return owlDataFactory.getOWLObjectMinCardinality(cardinality, op);
   }

   public OWLObjectMinCardinality createOWLObjectMinCardinality(int cardinality,
         OWLObjectProperty property, OWLClassExpression classExpression) {
      return owlDataFactory.getOWLObjectMinCardinality(cardinality, property, classExpression);
   }

   public OWLDataMinCardinality createOWLDataMinCardinality(int cardinality,
         OWLDataProperty property, OWLDatatype datatype) {
      return owlDataFactory.getOWLDataMinCardinality(cardinality, property, datatype);
   }

   public OWLObjectHasValue createOWLObjectHasValue(OWLObjectProperty op, OWLNamedIndividual ind) {
      return owlDataFactory.getOWLObjectHasValue(op, ind);
   }

   public OWLDataHasValue createOWLDataHasValue(OWLDataProperty dp, OWLLiteral lit) {
      return owlDataFactory.getOWLDataHasValue(dp, lit);
   }
}
