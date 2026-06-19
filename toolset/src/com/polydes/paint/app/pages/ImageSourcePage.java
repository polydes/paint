package com.polydes.paint.app.pages;

import com.polydes.paint.app.editors.image.DrawArea;
import com.polydes.paint.app.editors.image.ImageEditPane;
import com.polydes.paint.data.ImageSource;
import stencyl.app.api.nodes.select.NodeSelection;
import stencyl.app.api.nodes.select.NodeSelectionEvent;
import stencyl.app.api.nodes.select.SelectionType;
import stencyl.core.api.pnodes.DefaultBranch;
import stencyl.core.api.pnodes.DefaultLeaf;

public class ImageSourcePage extends BasicPage
{
	private ImageEditPane editorPane;
	private DrawArea currentEditor;
	
	protected ImageSourcePage(DefaultBranch rootFolder)
	{
		super(rootFolder);
		
		editorPane = new ImageEditPane();
		splitPane.setRightComponent(editorPane);
		currentEditor = null;
	}
	
	@Override
	public void selectionChanged(NodeSelectionEvent<DefaultLeaf, DefaultBranch> e)
	{
		NodeSelection<DefaultLeaf, DefaultBranch> selection = tree.getSelectionState();
		
		if(selection.size() != 1)
			return;
		
		if(currentEditor == null && (selection.getType() == SelectionType.FOLDERS))
			return;
		
		if(currentEditor != null)
		{
			editorPane.removeDrawArea();
		}
		currentEditor = null;
		
		if(selection.getType() == SelectionType.FOLDERS)
		{
			editorPane.showToolbar(false);
		}
		else
		{
			ImageSource toEdit = (ImageSource) selection.firstNode();
			
			if(toEdit.getEditor() == null)
				new DrawArea(toEdit);
			
			currentEditor = (DrawArea) toEdit.getEditor();
		}
		
		if (currentEditor != null)
		{
			editorPane.setDrawArea(currentEditor);
		}
		
		revalidate();
		repaint();
	}
}
