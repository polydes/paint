package com.polydes.paint.data.stores;

import java.io.File;

public class Images extends ImageStore
{
	public Images()
	{
		super("Images");
	}

	@Override
	public void load(File file)
	{
		super.imagesLoad(file);
	}

	@Override
	public void saveChanges(File file)
	{
		super.imagesSave(file);
	}
}
