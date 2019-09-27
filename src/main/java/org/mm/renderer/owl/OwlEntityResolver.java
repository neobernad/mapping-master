package org.mm.renderer.owl;

import org.apache.poi.ss.formula.functions.T;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public interface OwlEntityResolver {

   /**
    * Resolves the given entity name and returns the OWL entity object with the specified type.
    * The method will look the entity name in the active ontology and return the found object.
    *
    * @param entityName
    *          The entity name in short form or as a prefixed name string.
    * @param entityType
    *          The entity type following the OWLAPI class hierarchy. The types an be
    *          one of these: {@link OWLClass}, {@link OWLDataProperty},
    *          {@link OWLObjectProperty}. {@link OWLNamedIndividual} or
    *          {@link OWLDatatype}.
    * @return Returns an OWL entity object according to its type.
    * @throws EntityNotFoundException If the entity name does not exist in the ontology.
    */
   <T extends OWLEntity> T resolve(String entityName, final Class<T> entityType)
         throws EntityNotFoundException;

   /**
    * Resolves the given entity name and returns the OWL entity object with the specified type.
    * The method will look the entity name in the active ontology and return the found object.
    * Unlike {@code resolve(String, Class<T>)}, this method does not throw a checked exception
    *
    * @param entityName
    *          The entity name in short form or as a prefixed name string.
    * @param entityType
    *          The entity type following the OWLAPI class hierarchy. The types an be
    *          one of these: {@link OWLClass}, {@link OWLDataProperty},
    *          {@link OWLObjectProperty}. {@link OWLNamedIndividual} or
    *          {@link OWLDatatype}.
    * @return Returns an OWL entity object according to its type.
    */
   <T extends OWLEntity> T resolveUnchecked(String entityName, final Class<T> entityType);

   /**
    * Creates an OWL entity following its given name and type.
    *
    * @param entityName
    *          The entity name in short form or as a prefixed name string.
    * @param entityType
    *          The entity type following the OWLAPI class hierarchy. The types an be
    *          one of these: {@link OWLClass}, {@link OWLDataProperty},
    *          {@link OWLObjectProperty}. {@link OWLNamedIndividual} or
    *          {@link OWLDatatype}.
    * @return Returns an OWL entity object following its given name and type
    * @throws EntityCreationException If the entity creation was failed
    */
   <T extends OWLEntity> T create(String entityName, final Class<T> entityType)
         throws EntityCreationException;

   /**
    * Creates an OWL entity following its given name and type. Unlike {@code create(String, Class<T>)},
    * this method does not throw a checked exception.
    *
    * @param entityName
    *          The entity name in short form or as a prefixed name string.
    * @param entityType
    *          The entity type following the OWLAPI class hierarchy. The types an be
    *          one of these: {@link OWLClass}, {@link OWLDataProperty},
    *          {@link OWLObjectProperty}. {@link OWLNamedIndividual} or
    *          {@link OWLDatatype}.
    * @return Returns an OWL entity object following its given name and type
    */
   <T extends OWLEntity> T createUnchecked(String entityName, final Class<T> entityType);

   /**
    * Checks if the given {@code entityName} has a type of {@code entityType} in the active ontology.
    *
    * @param entityName
    *          The entity name in short form or as a prefixed name string.
    * @param entityType
    *          The entity type following the OWLAPI class hierarchy. The types an be
    *          one of these: {@link OWLClass}, {@link OWLDataProperty},
    *          {@link OWLObjectProperty}. {@link OWLNamedIndividual} or
    *          {@link OWLDatatype}.
    * @return Returns true if the {@code entityName} has the type of {@code entityType}
    *          in the active ontology, or otherwise false.
    */
   <T extends OWLEntity> boolean hasType(String entityName, final Class<T> entityType);
}
