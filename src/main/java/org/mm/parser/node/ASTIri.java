/* Generated By:JJTree: Do not edit this line. ASTIRI.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.mm.parser.node;

import org.mm.parser.MappingMasterParser;
import org.mm.parser.NodeVisitor;

public
class ASTIri extends SimpleNode {

  public String iri; /* XXX: Manually added */

  public int entityType; /* XXX: Manually added */

  public ASTIri(int id) {
    super(id);
  }

  public ASTIri(MappingMasterParser p, int id) {
    super(p, id);
  }

  public String getValue() { /* XXX: Manually added */
     return iri;
  }

  public int getEntityType() { /* XXX: Manually added */
     return entityType;
   }

  @Override
  public void accept(NodeVisitor visitor) { /* XXX: Manually added */
    visitor.visit(this);
  }
}
/* JavaCC - OriginalChecksum=9e03ea45ea9e0cc81bde199dde2455ba (do not edit this line) */
