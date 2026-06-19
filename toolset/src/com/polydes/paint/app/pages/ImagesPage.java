package com.polydes.paint.app.pages;

import com.polydes.paint.data.stores.Images;

public class ImagesPage extends ImageSourcePage
{
	public ImagesPage(Images images)
	{
		super(images);
		
		setListEditEnabled(true);
		folderModel.setUniqueLeafNames(true);
	}
}