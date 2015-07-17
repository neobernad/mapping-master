
package org.mm.parser.node;

import org.mm.parser.ASTName;
import org.mm.parser.ASTOWLClass;
import org.mm.parser.ASTReference;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLClassNode implements TypeNode
{
  private ReferenceNode referenceNode = null;
  private NameNode nameNode = null;

  public OWLClassNode(ASTOWLClass node) throws ParseException
  {
    if (node.jjtGetNumChildren() != 1)  
      throw new InternalParseException("expecting one child node for OWLClass node");
    else {
      Node child = node.jjtGetChild(0);
      if (ParserUtil.hasName(child, "Name")) nameNode = new NameNode((ASTName)child);
      else if (ParserUtil.hasName(child, "Reference")) referenceNode = new ReferenceNode((ASTReference)child);
      else throw new InternalParseException("unexpected child node " + child.toString() + " for " + getNodeName() + " node");
    }
	}

	@Override public String getNodeName()
	{
		return "OWLClass";
	}

	public ReferenceNode getReferenceNode() { return referenceNode; }
  public NameNode getNameNode() { return nameNode; }

  public boolean isName() { return nameNode != null; }
  public boolean isReference() { return referenceNode != null; }

	public String toString()
  { 
    if (isName()) return nameNode.toString();
    else if (isReference()) return referenceNode.toString();
    else return "";
  }

	@Override public boolean isOWLClassNode()
	{
		return true;
	}

	@Override public boolean isOWLPropertyNode()
	{
		return false;
	}

	@Override public boolean isReferenceNode()
	{
		return false;
	}
}