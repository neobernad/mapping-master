/* Generated By:JJTree: Do not edit this line. ASTOWLObjectMaxCardinality.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.mm.parser.node;

import org.mm.parser.MappingMasterParser;
import org.mm.parser.NodeVisitor;

public
class ASTObjectMaxCardinality extends SimpleNode {

  public int cardinality; /* XXX: Manually added */
  public boolean hasExplicitClassExpression; /* XXX: Manually added */

  public ASTObjectMaxCardinality(int id) {
    super(id);
  }

  public ASTObjectMaxCardinality(MappingMasterParser p, int id) {
    super(p, id);
  }

  public int getCardinality() { /* XXX: Manually added */
     return cardinality;
  }

  public boolean hasExplicitClassExpression() { /* XXX: Manually added */
     return hasExplicitClassExpression;
  }

  @Override
  public void accept(NodeVisitor visitor) { /* XXX: Manually added */
    visitor.visit(this);
  }
}
/* JavaCC - OriginalChecksum=68388638f9d042f286415876f1ed14c7 (do not edit this line) */