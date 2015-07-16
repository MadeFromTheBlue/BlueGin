package blue.made.bluegin.codegen.classes;

import java.util.Map.Entry;

import javax.lang.model.element.Modifier;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

public class GLEnum extends EnumBuilder
{
	public JsonObject cfg;
	
	public GLEnum(JsonObject cfg)
	{
		super("GLEnum", "blue.made.bluegin.core.gl");
		
		this.cfg = cfg.get("enums").getAsJsonObject();
	}
	
	@Override
	protected void make()
	{
		this.type.addModifiers(Modifier.PUBLIC);
		
		this.type.addMethod(MethodSpec.methodBuilder("value").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).returns(int.class).build());
		
		for (Entry<String, JsonElement> e : this.cfg.entrySet())
		{
			TypeSpec.Builder b = TypeSpec.anonymousClassBuilder("");
			MethodSpec.Builder m = MethodSpec.methodBuilder("value").addModifiers(Modifier.PUBLIC).returns(int.class);
			m.addAnnotation(AnnotationSpec.builder(Override.class).build());
			b.addJavadoc("From: ");
			boolean flag = false;
			for (Entry<String, JsonElement> f : e.getValue().getAsJsonObject().get("from").getAsJsonObject().entrySet())
			{
				if (flag)
				{
					b.addJavadoc(", ", f.getKey());
				}
				else
				{
					flag = true;
				}
				b.addJavadoc("$L", f.getKey());
				CodeBlock.Builder block = CodeBlock.builder();
				block.beginControlFlow("if (GLCapabilities.$L)", f.getKey());
				if (f.getValue().isJsonPrimitive())
				{
					block.add("return $L;\n", f.getValue().getAsString().replaceAll("^(0x([0-9]|[a-f]|[A-F])+|[0-9]+).*$", "$1"));
				}
				block.endControlFlow();
				m.addCode(block.build());
			}
			m.addCode("throw new BGGLNotSupportedException($S);", e.getKey().replaceAll("^_?(.*)$", "GL_$1"));
			
			b.addMethod(m.build());
			
			this.type.addEnumConstant(e.getKey(), b.build());
		}
	}
}