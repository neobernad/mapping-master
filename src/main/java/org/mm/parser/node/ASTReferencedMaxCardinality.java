/* Generated By:JJTree: Do not edit this line. ASTReferencedMaxCardinality.java Version 7.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.mm.parser.node;

import org.mm.parser.*;

public
class ASTReferencedMaxCardinality extends SimpleNode {

   public boolean hasFiller; /* XXX: Manually added */

   public ASTReferencedMaxCardinality(int id) {
     super(id);
   }

   public ASTReferencedMaxCardinality(MappingMasterParser p, int id) {
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
/* JavaCC - OriginalChecksum=6c2aabd388e301467721ec095b839657 (do not edit this line) */
