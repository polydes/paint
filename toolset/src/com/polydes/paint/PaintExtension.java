package com.polydes.paint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import com.polydes.paint.app.MainEditor;
import com.polydes.paint.data.stores.Fonts;
import com.polydes.paint.data.stores.Images;
import stencyl.app.ext.PageAddon;
import stencyl.app.ext.PageAddon.ExtensionPageAddon;
import stencyl.app.ext.res.AppResourceLoader;
import stencyl.app.ext.res.AppResources;
import stencyl.core.ext.addon.AddonContributor;
import stencyl.core.ext.app.AppExtension;
import stencyl.core.io.FileHelper;
import stencyl.core.lib.IProject;
import stencyl.core.lib.ProjectManager;
import stencyl.sw.app.center.GameLibrary;

public class PaintExtension extends AppExtension
{
	private static AppResources res = AppResourceLoader.getResources("com.polydes.paint");

	public static class PaintManager
	{
		private final IProject project;

		private final String extrasDir;
		private final File fontsFile;
		private final File imagesFile;

		public final Fonts fonts;
		public final Images images;
		private MainEditor mainEditor;

		private AddonContributor projectAddons;

		public PaintManager(IProject project)
		{
			this.project = project;

			String extensionId = PaintExtension.get().getInfo().getID();

			extrasDir = project.getLocation("extras", extensionId);
			File extrasFile = new File(extrasDir);

			if(!extrasFile.exists())
				extrasFile.mkdir();

			fonts = new Fonts();
			images = new Images();

			if(extrasFile.list().length == 0)
				loadDefaults(extrasFile);

			fontsFile = new File(extrasFile, "fonts");
			imagesFile = new File(extrasFile, "images");

			fonts.load(fontsFile);
			images.load(imagesFile);

			projectAddons = new AddonContributor(_instance.getAddons().getAddonContributorId());

			PageAddon paintSidebarPage = new ExtensionPageAddon(PaintExtension.get())
			{
				@Override
				public JPanel getPage()
				{
					return getEditor();
				}
			};

			projectAddons.setAddon(GameLibrary.DASHBOARD_SIDEBAR_PAGE_ADDONS, paintSidebarPage);

			project.getAddonManager().addDataForContributor(projectAddons);
		}

		public MainEditor getEditor()
		{
			if(mainEditor == null)
				mainEditor = new MainEditor(this);
			return mainEditor;
		}

		public void save()
		{
			fonts.saveChanges(fontsFile);
			images.saveChanges(imagesFile);

			if(mainEditor != null)
				mainEditor.gameSaved();
		}

		public void close()
		{
			project.getAddonManager().removeDataForContributor(projectAddons);
			if(mainEditor != null)
				mainEditor.disposePages();
			fonts.unload();
			images.unload();
		}
	}

	private Map<IProject, PaintManager> projectPaintManager = new HashMap<>();

	private static PaintExtension _instance;

	public static PaintExtension get()
	{
		return _instance;
	}

	@Override
	public void onLoad() {
		super.onLoad();

		_instance = this;

		for(IProject project : ProjectManager.getOpenedProjects())
		{
			projectPaintManager.put(project, new PaintManager(project));
		}
	}

	@Override
	public void onUnload() {
		super.onUnload();

		for(IProject project : ProjectManager.getOpenedProjects())
		{
			projectPaintManager.remove(project).close();
		}
	}

	@Override
	public void onGameSave(IProject project) {
		var manager = projectPaintManager.get(project);
		if(manager != null)
			manager.save();
	}

	@Override
	public void onGameOpened(IProject project) {
		projectPaintManager.put(project, new PaintManager(project));
	}

	@Override
	public void onGameClosed(IProject project) {
		var manager = projectPaintManager.remove(project);
		if(manager != null)
			manager.close();
	}

	public JPanel getBlankPanel()
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(new Color(62, 62, 62));
		return panel;
	}

	private static void loadDefaults(File extras)
	{
		File f;
		try
		{
			f = new File(extras, "fonts/Default Font.fnt");
			f.getParentFile().mkdirs();
			if (!f.exists())
				FileHelper.writeStringToFile(f.getAbsolutePath(), res.loadText("defaults/Default Font.fnt"));

			f = new File(extras, "fonts/Default Font.png");
			if (!f.exists())
				FileHelper.writeToPNG(f.getAbsolutePath(),
						res.loadImage("defaults/Default Font.png"));

			f = new File(extras, "images/Default Window.png");
			f.getParentFile().mkdirs();
			if (!f.exists())
				FileHelper.writeToPNG(f.getAbsolutePath(),
						res.loadImage("defaults/Default Window.png"));

			f = new File(extras, "images/Pointer.png");
			if (!f.exists())
				FileHelper.writeToPNG(f.getAbsolutePath(),
						res.loadImage("defaults/Pointer.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
