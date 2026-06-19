package com.polydes.paint.app.pages;

import com.polydes.paint.data.stores.Fonts;

public class FontsPage extends BitmapFontPage
{
	public FontsPage(Fonts fonts)
	{
		super(fonts);
		setListEditEnabled(true);
		folderModel.setUniqueLeafNames(true);
	}
}