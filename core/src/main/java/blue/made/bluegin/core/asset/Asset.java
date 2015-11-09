package blue.made.bluegin.core.asset;

public class Asset
{
	public final String asset;
	
	public Asset(String file)
	{
		this.asset = file;
	}
	
	public FileAsset getFileAsset(String extension)
	{
		return new FileAsset(this.asset + "." + extension);
	}
}
