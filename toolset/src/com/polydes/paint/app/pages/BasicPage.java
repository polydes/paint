package com.polydes.paint.app.pages;

import stencyl.app.api.nodes.HierarchyModelInterface;
import stencyl.app.api.nodes.select.NodeSelection;
import stencyl.app.api.nodes.select.NodeSelectionEvent;
import stencyl.app.api.nodes.select.NodeSelectionListener;
import stencyl.app.comp.MiniSplitPane;
import stencyl.app.comp.darktree.DarkTree;
import stencyl.core.api.pnodes.DefaultBranch;
import stencyl.core.api.pnodes.DefaultLeaf;
import stencyl.core.api.pnodes.HierarchyModel;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class BasicPage extends JPanel implements NodeSelectionListener<DefaultLeaf, DefaultBranch>
{
	protected Boolean listEditEnabled;
	
	protected MiniSplitPane splitPane;
	protected HierarchyModel<DefaultLeaf,DefaultBranch> folderModel;
	protected HierarchyModelInterface<DefaultLeaf,DefaultBranch> folderModelInterface;
	protected DarkTree<DefaultLeaf,DefaultBranch> tree;
	
	protected NodeSelection<DefaultLeaf,DefaultBranch> selection;
	
	protected BasicPage()
	{
		super(new BorderLayout());
	}
	
	protected BasicPage(final DefaultBranch rootFolder)
	{
		super(new BorderLayout());
		
		folderModel = new HierarchyModel<DefaultLeaf,DefaultBranch>(rootFolder, DefaultLeaf.class, DefaultBranch.class);
		folderModelInterface = new HierarchyModelInterface<>(folderModel);
		tree = new DarkTree<DefaultLeaf,DefaultBranch>(folderModelInterface);
		
		splitPane = new MiniSplitPane();
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(tree);
		
		add(splitPane);
		
		splitPane.setDividerLocation(DarkTree.DEF_WIDTH);

		tree.getSelectionState().addSelectionListener(this);
		
		tree.forceRerender();
	}
	
	public void setListEditEnabled(boolean value)
	{
		if(listEditEnabled == null || listEditEnabled != value)
		{
			listEditEnabled = value;
			if(listEditEnabled)
			{
				tree.setListEditEnabled(true);
			}
			else
			{
				tree.setListEditEnabled(false);
			}
		}
	}
	
	@Override
	public void selectionChanged(NodeSelectionEvent<DefaultLeaf, DefaultBranch> e)
	{
		
	}
}