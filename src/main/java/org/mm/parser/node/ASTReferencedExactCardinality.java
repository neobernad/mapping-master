/* Generated By:JJTree: Do not edit this line. ASTReferencedExactCardinality.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.mm.parser.node;

import org.mm.parser.*;

public
class ASTReferencedExactCardinality extends SimpleNode {

  public boolean hasFiller; /* XXX: Manually added */

  public ASTReferencedExactCardinality(int id) {
    super(id);
  }

  public ASTReferencedExactCardinality(MappingMasterParser p, int id) {
    super(p, id);
  }

  public boolean hasFiller() { /* XXX: Manually added */
    return hasFiller;
  }

  @Override
  public void accept(NodeVisitor visitor) { /* XXX: Manually added */
    visitor.visit(this);
  }
}
/* JavaCC - OriginalChecksum=dd4a4bdf25d6dc608559f37e5394933f (do not edit this line) */
