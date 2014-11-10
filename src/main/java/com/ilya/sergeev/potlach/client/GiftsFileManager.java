package com.ilya.sergeev.potlach.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class GiftsFileManager
{
	public static GiftsFileManager get() throws IOException
	{
		return new GiftsFileManager();
	}
	
	private Path targetDir_ = Paths.get("gifts");
	
	private GiftsFileManager() throws IOException
	{
		if (!Files.exists(targetDir_))
		{
			Files.createDirectories(targetDir_);
		}
	}
	
	private Path getImagePath(Gift gift)
	{
		assert (gift != null);
		
		return targetDir_.resolve("gift" + gift.getId()); //FIXME+ ".jpg");
	}
	
	public boolean hasGiftData(Gift gift)
	{
		Path source = getImagePath(gift);
		return Files.exists(source);
	}
	
	public void copyGiftData(Gift gift, OutputStream out) throws IOException
	{
		Path source = getImagePath(gift);
		if (!Files.exists(source))
		{
			throw new FileNotFoundException("Unable to find the referenced image file for giftId:" + gift.getId());
		}
		Files.copy(source, out);
	}
	
	public void saveGiftData(Gift gift, InputStream imageData) throws IOException
	{
		assert (imageData != null);
		
		Path target = getImagePath(gift);
		Files.copy(imageData, target, StandardCopyOption.REPLACE_EXISTING);
	}
	
}
