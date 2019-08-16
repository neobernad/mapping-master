package org.mm.renderer.owl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Declaration;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ShiftDirectiveTest extends OwlRendererTest {

   @Before
   public void setUp() throws OWLOntologyCreationException {
      createEmptyWorkbook();
      createEmptyOWLOntology();
   }

   @Test
   public void shouldShiftTowardUpDirection() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");  // A1
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A4(mm:shiftUp)");
      // Assert
      assertThat(result, hasSize(1));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.CAR)));
   }

   @Test
   public void shouldShiftTowardDownDirection() {
      // Arrange
      createCell("Sheet1", 1, 4, "Car");  // A4
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1(mm:shiftDown)");
      // Assert
      assertThat(result, hasSize(1));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.CAR)));
   }

   @Test
   public void shouldShiftTowardLeftDirection() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");  // A1
      // Act
      Set<OWLAxiom> result = evaluate("Class: @D1(mm:shiftLeft)");
      // Assert
      assertThat(result, hasSize(1));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.CAR)));
   }

   @Test
   public void shouldShiftTowardRightDirection() {
      // Arrange
      createCell("Sheet1", 4, 1, "Car");  // D1
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1(mm:shiftRight)");
      // Assert
      assertThat(result, hasSize(1));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.CAR)));
   }
}
