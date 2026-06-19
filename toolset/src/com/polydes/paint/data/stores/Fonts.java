package com.polydes.paint.data.stores;

import java.io.File;

public class Fonts extends FontStore
{
	public Fonts()
	{
		super("Fonts");
	}
	
	@Override
	public void load(File file)
	{
		super.fontsLoad(file);
	}
	
	@Override
	public void saveChanges(File file)
	{
		super.fontsSave(file);
	}
}
