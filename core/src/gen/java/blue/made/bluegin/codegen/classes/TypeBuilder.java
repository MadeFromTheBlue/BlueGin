package blue.made.bluegin.codegen.classes;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

public abstract class TypeBuilder
{
	public TypeSpec.Builder type;
	private String pack;
	private boolean built = false;
	
	public TypeBuilder(String name, String pack)
	{
		this.type = this.makeBuilder(name);
		this.pack = pack;
	}
	
	protected abstract TypeSpec.Builder makeBuilder(String name);
	
	protected abstract void make();
	
	public boolean isBuilt()
	{
		return this.built;
	}
	
	public JavaFile build()
	{
		if (!this.built)
		{
			this.make();
			this.built = true;
		}
		return JavaFile.builder(this.pack, this.type.build()).build();
	}
}
