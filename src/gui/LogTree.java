package gui;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class LogTree extends JTree {

	private DefaultMutableTreeNode rootNode;
	
	public LogTree() {
		
		
		rootNode = new DefaultMutableTreeNode("Requests");
		DefaultTreeModel model = new DefaultTreeModel(rootNode);

		setModel(model);
	}
}
