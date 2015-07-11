package blue.made.bluegin.codegen;

import java.io.File;
import java.io.IOException;

public class Build
{
	public static void main(String[] args) throws IOException
	{
		File out = new File(System.getProperty("user.dir"));
		Build build = new Build();
		
		build.buildJava(new File(out, "java/"));
		build.buildResources(new File(out, "resources/"));
	}
	
	private Build()
	{
		
	}
	
	public void buildJava(File out)
	{
		
	}
	
	public void buildResources(File out)
	{
		
	}
}
