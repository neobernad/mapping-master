/* Generated By:JJTree: Do not edit this line. ASTMMClassSection.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.mm.parser.node;

import org.mm.parser.MappingMasterParser;
import org.mm.parser.NodeVisitor;

public
class ASTClassFrame extends SimpleNode {
  public ASTClassFrame(int id) {
    super(id);
  }

  public ASTClassFrame(MappingMasterParser p, int id) {
    super(p, id);
  }

  @Override
  public void accept(NodeVisitor visitor) { /* XXX: Manually added */
    visitor.visit(this);
  }
}
/* JavaCC - OriginalChecksum=b43e58e39ba95139d9d8e1a88cf50d3f (do not edit this line) */