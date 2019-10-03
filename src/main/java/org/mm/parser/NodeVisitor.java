package org.mm.parser;

import org.mm.parser.node.ASTAnnotation;
import org.mm.parser.node.ASTAnnotationAssertion;
import org.mm.parser.node.ASTAnnotationProperty;
import org.mm.parser.node.ASTAnnotationValue;
import org.mm.parser.node.ASTArgument;
import org.mm.parser.node.ASTBooleanLiteral;
import org.mm.parser.node.ASTBuiltInFunction;
import org.mm.parser.node.ASTCardinalityValue;
import org.mm.parser.node.ASTClass;
import org.mm.parser.node.ASTClassAssertion;
import org.mm.parser.node.ASTClassDeclaration;
import org.mm.parser.node.ASTClassExpressionCategory;
import org.mm.parser.node.ASTClassFrame;
import org.mm.parser.node.ASTDataAllValuesFrom;
import org.mm.parser.node.ASTDataExactCardinality;
import org.mm.parser.node.ASTDataHasValue;
import org.mm.parser.node.ASTDataMaxCardinality;
import org.mm.parser.node.ASTDataMinCardinality;
import org.mm.parser.node.ASTDataProperty;
import org.mm.parser.node.ASTDataSomeValuesFrom;
import org.mm.parser.node.ASTDifferentFrom;
import org.mm.parser.node.ASTEquivalentClasses;
import org.mm.parser.node.ASTFact;
import org.mm.parser.node.ASTFloatLiteral;
import org.mm.parser.node.ASTIndividualDeclaration;
import org.mm.parser.node.ASTIndividualFrame;
import org.mm.parser.node.ASTIntegerLiteral;
import org.mm.parser.node.ASTIri;
import org.mm.parser.node.ASTLiteral;
import org.mm.parser.node.ASTLiteralValue;
import org.mm.parser.node.ASTName;
import org.mm.parser.node.ASTNamedIndividual;
import org.mm.parser.node.ASTObjectAllValuesFrom;
import org.mm.parser.node.ASTObjectComplement;
import org.mm.parser.node.ASTObjectExactCardinality;
import org.mm.parser.node.ASTObjectHasValue;
import org.mm.parser.node.ASTObjectIntersection;
import org.mm.parser.node.ASTObjectMaxCardinality;
import org.mm.parser.node.ASTObjectMinCardinality;
import org.mm.parser.node.ASTObjectOneOf;
import org.mm.parser.node.ASTObjectProperty;
import org.mm.parser.node.ASTObjectSomeValuesFrom;
import org.mm.parser.node.ASTObjectUnion;
import org.mm.parser.node.ASTObjectValue;
import org.mm.parser.node.ASTProperty;
import org.mm.parser.node.ASTPropertyAssertion;
import org.mm.parser.node.ASTPropertyValue;
import org.mm.parser.node.ASTReference;
import org.mm.parser.node.ASTReferenceNotation;
import org.mm.parser.node.ASTRestrictionCategory;
import org.mm.parser.node.ASTRuleExpression;
import org.mm.parser.node.ASTSameAs;
import org.mm.parser.node.ASTSourceSpecification;
import org.mm.parser.node.ASTStringLiteral;
import org.mm.parser.node.ASTSubclassOf;
import org.mm.parser.node.ASTTransformationRule;
import org.mm.parser.node.ASTUntypedExactCardinality;
import org.mm.parser.node.ASTUntypedHasValue;
import org.mm.parser.node.ASTUntypedMaxCardinality;
import org.mm.parser.node.ASTUntypedMinCardinality;
import org.mm.parser.node.ASTValue;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public interface NodeVisitor {

   void visit(ASTAnnotation node);

   void visit(ASTAnnotationAssertion node);

   void visit(ASTAnnotationProperty node);

   void visit(ASTAnnotationValue node);

   void visit(ASTArgument node);

   void visit(ASTBooleanLiteral node);

   void visit(ASTBuiltInFunction node);

   void visit(ASTClass node);

   void visit(ASTClassAssertion node);

   void visit(ASTClassDeclaration node);

   void visit(ASTClassExpressionCategory node);

   void visit(ASTClassFrame node);

   void visit(ASTDataAllValuesFrom node);

   void visit(ASTDataExactCardinality node);

   void visit(ASTDataHasValue node);

   void visit(ASTDataMaxCardinality node);

   void visit(ASTDataMinCardinality node);

   void visit(ASTCardinalityValue node);

   void visit(ASTDataProperty node);

   void visit(ASTDataSomeValuesFrom node);

   void visit(ASTDifferentFrom node);

   void visit(ASTEquivalentClasses node);

   void visit(ASTFact node);

   void visit(ASTFloatLiteral node);

   void visit(ASTIndividualDeclaration node);

   void visit(ASTIndividualFrame node);

   void visit(ASTIntegerLiteral node);

   void visit(ASTIri node);

   void visit(ASTLiteral node);

   void visit(ASTLiteralValue node);

   void visit(ASTName node);

   void visit(ASTNamedIndividual node);

   void visit(ASTObjectAllValuesFrom node);

   void visit(ASTObjectComplement node);

   void visit(ASTObjectExactCardinality node);

   void visit(ASTUntypedExactCardinality node);

   void visit(ASTObjectHasValue node);

   void visit(ASTUntypedHasValue node);

   void visit(ASTObjectIntersection node);

   void visit(ASTObjectMaxCardinality node);

   void visit(ASTUntypedMaxCardinality node);

   void visit(ASTObjectMinCardinality node);

   void visit(ASTUntypedMinCardinality node);

   void visit(ASTObjectOneOf node);

   void visit(ASTObjectProperty node);

   void visit(ASTObjectSomeValuesFrom node);

   void visit(ASTObjectUnion node);

   void visit(ASTObjectValue node);

   void visit(ASTProperty node);

   void visit(ASTPropertyAssertion node);

   void visit(ASTPropertyValue node);

   void visit(ASTReference node);

   void visit(ASTReferenceNotation node);

   void visit(ASTRestrictionCategory node);

   void visit(ASTRuleExpression node);

   void visit(ASTSameAs node);

   void visit(ASTSourceSpecification node);

   void visit(ASTStringLiteral node);

   void visit(ASTSubclassOf node);

   void visit(ASTTransformationRule node);

   void visit(ASTValue node);
}
