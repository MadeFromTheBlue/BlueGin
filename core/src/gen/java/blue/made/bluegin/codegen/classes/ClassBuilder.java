package blue.made.bluegin.codegen.classes;

import com.squareup.javapoet.TypeSpec;

public abstract class ClassBuilder extends TypeBuilder
{
	public ClassBuilder(String name, String pack)
	{
		super(name, pack);
	}
	
	@Override
	protected TypeSpec.Builder makeBuilder(String name)
	{
		return TypeSpec.classBuilder(name);
	}
}
