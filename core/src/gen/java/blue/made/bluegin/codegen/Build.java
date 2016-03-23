package blue.made.bluegin.codegen;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import blue.made.bluegin.codegen.classes.Vector;

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
	
	public void buildJava(File out) throws IOException
	{
		FileUtils.cleanDirectory(out);
		
		//JsonObject cfg = new JsonParser().parse(new InputStreamReader(Build.class.getResourceAsStream("/cfg/gl.json"))).getAsJsonObject();
		//new GLEnum(cfg).build().writeTo(out);
		for (int size = 1; size <= 4; size++)
		{
			for (Vector.VectorType vectype : Vector.VectorType.values())
			{
				new Vector(size, vectype).build().writeTo(out);
			}
		}
		/*
		for (int size = 1; size <= 4; size++)
		{
			for (Vector.VectorType vectype : Vector.VectorType.values())
			{
				new Matrix(size, vectype).build().writeTo(out);
			}
		}
		*/
	}
	
	public void buildResources(File out) throws IOException
	{
		FileUtils.cleanDirectory(out);
	}
}
