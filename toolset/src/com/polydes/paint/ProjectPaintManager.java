package com.polydes.paint;

import com.polydes.paint.app.MainEditor;
import com.polydes.paint.data.stores.Fonts;
import com.polydes.paint.data.stores.Images;
import stencyl.app.ext.PageAddon;
import stencyl.app.ext.PageAddon.ExtensionPageAddon;
import stencyl.app.ext.res.AppResourceLoader;
import stencyl.app.ext.res.AppResources;
import stencyl.core.ext.ProjectDataSupport.ProjectData;
import stencyl.core.ext.app.AppExtension;
import stencyl.core.io.FileHelper;
import stencyl.core.lib.IProject;
import stencyl.sw.app.center.GameLibrary;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class ProjectPaintManager extends ProjectData
{
    private static final AppResources res = AppResourceLoader.getResources("com.polydes.paint");

    private final File fontsFile;
    private final File imagesFile;

    public final Fonts fonts;
    public final Images images;
    private MainEditor mainEditor;

    public ProjectPaintManager(IProject project, AppExtension extension)
    {
        super(project, extension);

        String extensionId = extension.getInfo().getID();

        String extrasDir = project.getLocation("extras", extensionId);
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

        PageAddon paintSidebarPage = new ExtensionPageAddon(extension)
        {
            @Override
            public JPanel getPage()
            {
                return getEditor();
            }
        };

        projectAddons.setAddon(GameLibrary.DASHBOARD_SIDEBAR_PAGE_ADDONS, paintSidebarPage);
    }

    public MainEditor getEditor()
    {
        if(mainEditor == null)
            mainEditor = new MainEditor(this);
        return mainEditor;
    }

    @Override
    public void save()
    {
        fonts.saveChanges(fontsFile);
        images.saveChanges(imagesFile);

        if(mainEditor != null)
            mainEditor.gameSaved();
    }

    @Override
    public void close()
    {
        if(mainEditor != null)
            mainEditor.disposePages();
        fonts.unload();
        images.unload();
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
