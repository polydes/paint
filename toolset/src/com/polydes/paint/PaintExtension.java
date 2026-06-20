package com.polydes.paint;

import stencyl.core.ext.ProjectDataSupport;
import stencyl.core.ext.app.AppExtension;
import stencyl.core.lib.IProject;

public class PaintExtension extends AppExtension
{
	private final ProjectDataSupport<ProjectPaintManager> projectPaintManager =
			new ProjectDataSupport<>(this, ProjectPaintManager::new);

	@Override
	public void onLoad() {
		super.onLoad();
		projectPaintManager.onLoad();
	}

	@Override
	public void onUnload() {
		super.onUnload();
		projectPaintManager.onUnload();
	}

	@Override
	public void onGameSave(IProject project) {
		projectPaintManager.onGameSave(project);
	}

	@Override
	public void onGameOpened(IProject project) {
		projectPaintManager.onGameOpened(project);
	}

	@Override
	public void onGameClosed(IProject project) {
		projectPaintManager.onGameClosed(project);
	}
}
