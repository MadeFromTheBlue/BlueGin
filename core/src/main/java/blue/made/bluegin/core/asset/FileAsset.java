package blue.made.bluegin.core.asset;

import java.io.File;
import java.io.InputStream;

public class FileAsset
{
	static AssetLoader loader;
	
	public final String file;
	
	public FileAsset(FileAsset parent, String file)
	{
		this(parent.file + file);
	}
	
	public FileAsset(String file)
	{
		this.file = file.replaceAll("\\\\", "/");
		if (!file.startsWith("/"))
		{
			file = "/" + file;
		}
	}
	
	public boolean isDirectory()
	{
		return this.file.endsWith("/");
	}
	
	/**
	 * Gets the file or directory represented by this asset.
	 * 
	 * @return A file that contains this asset.
	 */
	public File getFile()
	{
		File dir = loader.assetDir;
		return new File(dir, this.file);
	}
	
	public FileAsset getParent()
	{
		String pfile = this.file;
		if (this.isDirectory())
		{
			pfile = pfile.substring(0, pfile.length() - 1);
		}
		int split = this.file.lastIndexOf('/');
		if (split == -1)
		{
			pfile = "/";
		}
		else
		{
			pfile = pfile.substring(0, split + 1);
		}
		return new FileAsset(pfile);
	}
	
	public FileAsset[] getAssets()
	{
		if (!this.isDirectory())
		{
			return null;
		}
		File[] files = this.getFile().listFiles();
		FileAsset[] assets = new FileAsset[files.length];
		for (int i = 0; i < files.length; i++)
		{
			String name = files[i].getName();
			if (files[i].isDirectory())
			{
				name += "/";
			}
			assets[i] = new FileAsset(this, name);
		}
		return assets;
	}
	
	public String pathFromGameDir()
	{
		return "/assets" + this.file;
	}
	
	/**
	 * Uses the {@link AssetLoader} to create an {@link InputStream} for this
	 * asset.
	 * 
	 * @return An {@link InputStream} for this asset or null if this asset is a
	 *         directory or could not be found.
	 * @see AssetLoader#getInput(FileAsset)
	 */
	public InputStream getInput()
	{
		return loader.getInput(this);
	}
	
	@Override
	public String toString()
	{
		return this.file;
	}
	
	@Override
	public int hashCode()
	{
		return this.pathFromGameDir().hashCode();
	}
	
	@Override
	public boolean equals(Object x)
	{
		if (x == this)
		{
			return true;
		}
		if (x instanceof FileAsset)
		{
			FileAsset asset = (FileAsset) x;
			if (asset.file.equals(this.file))
			{
				return true;
			}
			return this.pathFromGameDir().equals(asset.pathFromGameDir());
		}
		return false;
	}
}
