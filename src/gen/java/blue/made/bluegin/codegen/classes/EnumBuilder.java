package blue.made.bluegin.codegen.classes;

import com.squareup.javapoet.TypeSpec;

public abstract class EnumBuilder extends TypeBuilder
{
	public EnumBuilder(String name, String pack)
	{
		super(name, pack);
	}
	
	@Override
	protected TypeSpec.Builder makeBuilder(String name)
	{
		return TypeSpec.enumBuilder(name);
	}
}
