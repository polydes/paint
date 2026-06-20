package com.polydes.paint.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import com.polydes.paint.ProjectPaintManager;
import com.polydes.paint.app.pages.FontsPage;
import com.polydes.paint.app.pages.ImagesPage;

import stencyl.app.comp.StatusBar;
import stencyl.app.ext.res.AppResourceLoader;
import stencyl.app.ext.res.AppResources;
import stencyl.app.lnf.Theme;

public class MainEditor extends JPanel
{
	private static AppResources res = AppResourceLoader.getResources("com.polydes.paint");
	
	private static final int BUTTON_WIDTH = 70;
	private static final int BUTTON_HEIGHT = 57;

	public final ProjectPaintManager manager;

	private ButtonGroup buttonGroup;
	private JToggleButton fontsButton;
	private JToggleButton imagesButton;

	private JPanel buttonBar;
	private JPanel currentPage;
	private JPanel blank;

	private FontsPage fontsPage;
	private ImagesPage imagesPage;
	
	public static final Color SIDEBAR_COLOR = new Color(62, 62, 62);

	public MainEditor(ProjectPaintManager manager)
	{
		super(new BorderLayout());

		this.manager = manager;

		fontsPage = new FontsPage(manager.fonts);
		imagesPage = new ImagesPage(manager.images);

		add(createVerticalButtonBar(), BorderLayout.WEST);

		blank = new JPanel(new BorderLayout());
		blank.setBackground(new Color(43, 43, 43));
		blank.add(StatusBar.createStatusBar(), BorderLayout.SOUTH);
		
		currentPage = blank;
		
		add(blank);
	}
	
	private JPanel createVerticalButtonBar()
	{
		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.setBackground(SIDEBAR_COLOR);
		buttonPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0x333333)));
		
		buttonBar = new JPanel();
		
		buttonBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0x333333)));
		
		buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.Y_AXIS));
		buttonBar.setBackground(SIDEBAR_COLOR);
		buttonBar.setMaximumSize(new Dimension(BUTTON_WIDTH,1000));
		buttonBar.setMinimumSize(new Dimension(BUTTON_WIDTH,100));
		
		//---
		
		buttonGroup = new ButtonGroup();
		
		fontsButton = createButton("Fonts", res.loadIcon("main/fonts.png"));
		imagesButton = createButton("Images", res.loadIcon("main/images.png"));
		
		buttonBar.add(fontsButton);
		buttonBar.add(imagesButton);
		
		buttonPanel.add(buttonBar, BorderLayout.NORTH);
		buttonPanel.add(StatusBar.createStatusBar(), BorderLayout.SOUTH);
		
		return buttonPanel;
	}
	
	public JToggleButton createButton(String name, ImageIcon icon)
	{
		JToggleButton button = new JToggleButton()
		{
			@Override
			public void paintComponent(Graphics g)
			{
				if(!isSelected())
				{
					super.paintComponent(g);
					setForeground(Theme.TEXT_COLOR.darker());
					return;
				}
				
				g.setColor(new Color(0x666666));
				setForeground(Theme.TEXT_COLOR);
				g.fillRect(0, 0, getWidth(), getHeight());
				super.paintComponent(g);
			}
		};
		
		button.setIconTextGap(8);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setBorder
		(
			BorderFactory.createCompoundBorder
			(
				BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0x454545)), 
				BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0x333333))
			)
		);
		
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.setVerticalAlignment(SwingConstants.CENTER);
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		button.setVerticalTextPosition(SwingConstants.BOTTOM);
		button.setForeground(Theme.TEXT_COLOR.darker());		
		
		button.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		button.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		
		button.setAction
		(
			new AbstractAction(name, icon)
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					switchToPage(e.getActionCommand());
				}
			}
		);
		
		buttonGroup.add(button);
				
		return button;
	}
	
	public void switchToPage(String pageName)
	{
		if(currentPage != null)
			remove(currentPage);
		
		if(pageName.equals("Fonts"))
		{
			currentPage = fontsPage;
			fontsButton.setSelected(true);			
		}
		else if(pageName.equals("Images"))
		{
			currentPage = imagesPage;
			imagesButton.setSelected(true);
		}
		
		add(currentPage, BorderLayout.CENTER);
		
		revalidate();
		repaint();
	}
	
	public void disposePages()
	{
		fontsPage = null;
		imagesPage = null;
	}

	public void gameSaved()
	{
		revalidate();
		repaint();
	}
}
