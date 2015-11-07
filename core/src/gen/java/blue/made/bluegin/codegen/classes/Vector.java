package blue.made.bluegin.codegen.classes;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

public class Vector extends ClassBuilder
{
	public enum VectorType
	{
		DOUBLE(TypeName.DOUBLE, "d")
		{
		
		},
		FLOAT(TypeName.FLOAT, "f")
		{
			@Override
			String convertTo(String in)
			{
				return "(float) " + in;
			}
		}/*,
			UNSIGNED_BYTE(TypeName.BYTE, "ub")
			{
			@Override
			String convertTo(String in)
			{
				return String.format("(byte) (%s * 255)", in);
			}
			
			@Override
			String convertFrom(String in)
			{
				return String.format("(%s & 0xFF) / 255.0D", in);
			}
			},
			SIGNED_BYTE(TypeName.BYTE, "sb")
			{
			@Override
			String convertTo(String in)
			{
				return String.format("(byte) (%s * 128)", in);
			}
			
			@Override
			String convertFrom(String in)
			{
				return String.format("%s / 128.0D", in);
			}
			}*/;
		
		public final TypeName type;
		public final String abriv;
		
		VectorType(TypeName type, String abriv)
		{
			this.type = type;
			this.abriv = abriv;
		}
		
		String convertFrom(String in)
		{
			return in;
		}
		
		String convertTo(String in)
		{
			return in;
		}
	}
	
	public static final String[] varnames = new String[] { "x", "y", "z", "w" };
	public static final ClassName vectortype = ClassName.get("blue.made.bluegin.core.util", "Vector");
	
	public final int size;
	public final VectorType vectype;
	
	public Vector(int size, VectorType type)
	{
		super(String.format("Vector%d%s", size, type.abriv), "blue.made.bluegin.core.util");
		this.size = size;
		this.vectype = type;
		if (size > varnames.length || size < 1)
		{
			throw new IllegalArgumentException("size must be greater than 0 and less than " + (varnames.length + 1));
		}
	}
	
	@Override
	protected void make()
	{
		this.type.addModifiers(Modifier.PUBLIC);
		this.type.addSuperinterface(vectortype);
		this.makeVars();
		this.makeConstructors();
		this.sizeMethod();
		this.getMethod();
		this.setMethod();
		this.lengthMethod();
		this.valuesMethod();
		this.cloneMethod();
		this.arithMethods("+=", "add");
		this.arithMethods("-=", "subtract");
		this.arithMethods("*=", "multiply");
		this.arithMethods("/=", "divide");
		this.copyMethods();
	}
	
	private void sizeMethod()
	{
		MethodSpec.Builder make = MethodSpec.methodBuilder("size");
		make.addAnnotation(Override.class);
		make.addModifiers(Modifier.PUBLIC);
		make.returns(int.class);
		make.addStatement("return $L", this.size);
		this.type.addMethod(make.build());
	}
	
	private void getMethod()
	{
		MethodSpec.Builder make = MethodSpec.methodBuilder("get");
		make.addAnnotation(Override.class);
		make.addModifiers(Modifier.PUBLIC);
		make.returns(double.class);
		make.addParameter(int.class, "index");
		
		CodeBlock.Builder code = CodeBlock.builder();
		code.beginControlFlow("switch(index)");
		for (int i = 0; i < this.size; i++)
		{
			code.add("case $L:\n", i);
			code.indent();
			code.addStatement("return " + this.vectype.convertFrom("this." + varnames[i]));
			code.unindent();
		}
		code.add("default:\n");
		code.indent();
		code.addStatement("return 0");
		code.unindent();
		code.endControlFlow();
		make.addCode(code.build());
		
		this.type.addMethod(make.build());
	}
	
	private void setMethod()
	{
		MethodSpec.Builder make = MethodSpec.methodBuilder("set");
		make.addAnnotation(Override.class);
		make.addModifiers(Modifier.PUBLIC);
		make.addParameter(int.class, "index");
		make.addParameter(double.class, "value");
		
		CodeBlock.Builder code = CodeBlock.builder();
		code.beginControlFlow("switch(index)");
		for (int i = 0; i < this.size; i++)
		{
			code.add("case $L:\n", i);
			code.indent();
			code.addStatement("this.$L = $L", varnames[i], this.vectype.convertTo("value"));
			code.addStatement("return");
			code.unindent();
		}
		code.endControlFlow();
		make.addCode(code.build());
		
		this.type.addMethod(make.build());
	}
	
	private void lengthMethod()
	{
		MethodSpec.Builder make = MethodSpec.methodBuilder("lengthSqr");
		make.addAnnotation(Override.class);
		make.addModifiers(Modifier.PUBLIC);
		make.returns(double.class);
		
		CodeBlock.Builder code = CodeBlock.builder();
		code.addStatement("double sqr");
		code.addStatement("double sum = 0");
		for (int i = 0; i < this.size; i++)
		{
			code.addStatement("sqr = " + this.vectype.convertFrom("this." + varnames[i]));
			code.addStatement("sqr *= sqr");
			code.addStatement("sum += sqr");
		}
		code.addStatement("return sum");
		make.addCode(code.build());
		
		this.type.addMethod(make.build());
	}
	
	private void valuesMethod()
	{
		MethodSpec.Builder make = MethodSpec.methodBuilder("values");
		make.addAnnotation(Override.class);
		make.addModifiers(Modifier.PUBLIC);
		make.returns(double[].class);
		
		CodeBlock.Builder code = CodeBlock.builder();
		code.add("return new double[] { ");
		for (int i = 0; i < this.size; i++)
		{
			if (i != 0)
			{
				code.add(", ");
			}
			code.add(this.vectype.convertFrom("this." + varnames[i]));
		}
		code.add(" };\n");
		make.addCode(code.build());
		
		this.type.addMethod(make.build());
	}
	
	private void arithMethods(String op, String name)
	{
		MethodSpec.Builder make1 = MethodSpec.methodBuilder(name);
		make1.addAnnotation(Override.class);
		make1.addModifiers(Modifier.PUBLIC);
		make1.addParameter(vectortype, "other");
		
		MethodSpec.Builder make2 = MethodSpec.methodBuilder(name);
		make2.addModifiers(Modifier.PUBLIC);
		make2.addParameter(this.mytype, "other");
		
		MethodSpec.Builder make3 = MethodSpec.methodBuilder(name);
		make3.addAnnotation(Override.class);
		make3.addModifiers(Modifier.PUBLIC);
		make3.addParameter(double.class, "value");
		
		MethodSpec.Builder make4 = MethodSpec.methodBuilder(name);
		make4.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
		make4.addParameter(vectortype, "a");
		make4.addParameter(vectortype, "b");
		make4.returns(this.mytype);
		
		for (int i = 0; i < this.size; i++)
		{
			make1.addStatement("this.$L $L other.get($L)", varnames[i], op, i);
			make2.addStatement("this.$L $L other.$L", varnames[i], op, varnames[i]);
			make3.addStatement("this.$L $L value", varnames[i], op);
		}
		
		make4.addStatement("$T out = new $T(a)", this.mytype, this.mytype);
		make4.addStatement("out.$L(b)", name);
		make4.addStatement("return out");
		
		this.type.addMethod(make1.build());
		this.type.addMethod(make2.build());
		this.type.addMethod(make3.build());
		this.type.addMethod(make4.build());
	}
	
	private void copyMethods()
	{
		MethodSpec.Builder make1 = MethodSpec.methodBuilder("copy");
		make1.addAnnotation(Override.class);
		make1.addModifiers(Modifier.PUBLIC);
		make1.addParameter(vectortype, "from");
		
		MethodSpec.Builder make2 = MethodSpec.methodBuilder("copy");
		make2.addModifiers(Modifier.PUBLIC);
		make2.addParameter(this.mytype, "from");
		
		MethodSpec.Builder make3 = MethodSpec.methodBuilder("fill");
		make3.addAnnotation(Override.class);
		make3.addModifiers(Modifier.PUBLIC);
		make3.addParameter(double.class, "value");
		
		for (int i = 0; i < this.size; i++)
		{
			make1.addStatement("this.$L = " + this.vectype.convertTo("from.get(" + i + ")"), varnames[i]);
			make2.addStatement("this.$L = from.$L", varnames[i], varnames[i]);
			make3.addStatement("this.$L = " + this.vectype.convertTo("value"), varnames[i]);
		}
		
		this.type.addMethod(make1.build());
		this.type.addMethod(make2.build());
		this.type.addMethod(make3.build());
	}
	
	private void cloneMethod()
	{
		MethodSpec.Builder make = MethodSpec.methodBuilder("clone");
		make.addAnnotation(Override.class);
		make.addModifiers(Modifier.PUBLIC);
		make.returns(this.mytype);
		make.addStatement("return new $T(this)", this.mytype);
		this.type.addMethod(make.build());
	}
	
	private void makeConstructors()
	{
		MethodSpec.Builder make1 = MethodSpec.constructorBuilder();
		MethodSpec.Builder make2 = MethodSpec.constructorBuilder();
		MethodSpec.Builder make3 = MethodSpec.constructorBuilder();
		MethodSpec.Builder make4 = MethodSpec.constructorBuilder();
		MethodSpec.Builder make5 = MethodSpec.constructorBuilder();
		
		make1.addModifiers(Modifier.PUBLIC);
		make2.addModifiers(Modifier.PUBLIC);
		make3.addModifiers(Modifier.PUBLIC);
		make4.addModifiers(Modifier.PUBLIC);
		make5.addModifiers(Modifier.PUBLIC);
		
		make2.addParameter(this.mytype, "original");
		make3.addParameter(vectortype, "original");
		make4.addParameter(this.vectype.type, "value");
		
		for (int i = 0; i < this.size; i++)
		{
			String varname = varnames[i];
			make1.addStatement("this.$L = $L", varname, varname);
			make1.addParameter(this.vectype.type, varname);
			make4.addStatement("this.$L = value", varname);
		}
		
		make2.addStatement("this.copy(original)");
		make3.addStatement("this.copy(original)");
		make5.addStatement("this(0)");
		
		this.type.addMethod(make1.build());
		this.type.addMethod(make2.build());
		this.type.addMethod(make3.build());
		if (this.size > 1)
		{
			this.type.addMethod(make4.build());
		}
		this.type.addMethod(make5.build());
	}
	
	private void makeVars()
	{
		for (int i = 0; i < this.size; i++)
		{
			this.type.addField(this.vectype.type, varnames[i], Modifier.PUBLIC);
		}
	}
}
