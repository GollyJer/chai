package ciir.jfoley.chai.xml.list;

import ciir.jfoley.chai.collections.list.AChaiList;
import ciir.jfoley.chai.xml.XNode;
import org.w3c.dom.NodeList;

/**
 * A wrapper around a NodeList that behaves like a list! Yay!
 * @author jfoley.
 */
public class NodeListWrapper extends AChaiList<XNode> {
	NodeList nodelist;
	public NodeListWrapper(NodeList nl) {
		this.nodelist = nl;
	}
	@Override
	public XNode get(int i) {
		return new XNode(nodelist.item(i));
	}

	@Override
	public int size() {
		return nodelist.getLength();
	}
}
