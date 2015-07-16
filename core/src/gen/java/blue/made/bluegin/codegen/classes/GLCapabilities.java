package blue.made.bluegin.codegen.classes;

import javax.lang.model.element.Modifier;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GLCapabilities extends ClassBuilder
{
	public JsonArray cfg;
	
	public GLCapabilities(JsonObject cfg)
	{
		super("GLCapabilities", "blue.made.bluegin.core.gl");
		
		this.cfg = cfg.get("groups").getAsJsonArray();
	}
	
	@Override
	protected void make()
	{
		this.type.addModifiers(Modifier.PUBLIC);
		
		this.cfg.forEach((JsonElement e) -> {
			String name = e.getAsString();
			this.type.addField(boolean.class, name, Modifier.PUBLIC, Modifier.STATIC);
		});
	}
}
