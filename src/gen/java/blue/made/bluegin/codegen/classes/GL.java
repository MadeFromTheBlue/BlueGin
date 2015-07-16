package blue.made.bluegin.codegen.classes;

import java.util.Map.Entry;

import javax.lang.model.element.Modifier;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.javapoet.MethodSpec;

public class GL extends ClassBuilder
{
	public JsonObject cfg;
	
	public GL(JsonObject cfg)
	{
		super("GL", "blue.made.bluegin");
		
		this.cfg = cfg.get("funcs").getAsJsonObject();
	}
	
	@Override
	protected void make()
	{
		this.type.addModifiers(Modifier.PUBLIC);
		
		for (Entry<String, JsonElement> e : this.cfg.entrySet())
		{
			MethodSpec.Builder b = MethodSpec.methodBuilder(e.getKey());
			this.makeFunc(e.getValue().getAsJsonObject(), b);
			this.type.addMethod(b.build());
		}
	}
	
	protected void makeFunc(JsonObject func, MethodSpec.Builder b)
	{
		b.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
		b.returns(void.class);
	}
}
