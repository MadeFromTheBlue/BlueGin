package blue.made.bluegin.codegen.classes;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import blue.made.bluegin.codegen.classes.Vector.VectorType;

public class Matrix extends ClassBuilder
{
	public static final ClassName matrixtype = ClassName.get("blue.made.bluegin.core.util", "Matrix");
	
	public final int size;
	public final VectorType vectype;
	public final ClassName nmat;
	
	public Matrix(int size, VectorType type)
	{
		super(String.format("Matrix%d%s", size, type.abriv), "blue.made.bluegin.core.util");
		this.size = size;
		this.vectype = type;
		if (size > 9 || size < 1)
		{
			throw new IllegalArgumentException("size must be greater than 0 and less than 9");
		}
		this.nmat = ClassName.get("blue.made.bluegin.core.util", "Matrixn" + this.vectype.abriv);
	}
	
	public String varName(int row, int column)
	{
		return String.format("v%d%d", row, column);
	}

	@Override
	protected void make() 
	{
		this.type.addModifiers(Modifier.PUBLIC);
		this.type.addSuperinterface(matrixtype);
		this.makeVars();
		this.makeConstructors();
		this.sizeMethods();
		this.transposeMethod();
		this.createMethods();
	}
	
	private void makeVars()
	{
		for (int i = 0; i < this.size; i++)
		{
			for (int j = 0; j < this.size; j++)
			{
				this.type.addField(this.vectype.type, this.varName(i, j), Modifier.PUBLIC);
			}
		}
		this.type.addField(TypeName.BOOLEAN, "extendIdentity", Modifier.PRIVATE);
	}
	
	private void makeConstructors()
	{
		MethodSpec.Builder make1 = MethodSpec.constructorBuilder();		
		make1.addModifiers(Modifier.PRIVATE);	
		make1.addStatement("this.extendIdentity = true");
		this.type.addMethod(make1.build());
		
		MethodSpec.Builder make2 = MethodSpec.constructorBuilder();
		make2.addModifiers(Modifier.PUBLIC);
		make2.addParameter(matrixtype, "original");
		make2.addStatement("this()"); //TODO copy extendIdentity
		for (int i = 0; i < this.size; i++)
		{
			for (int j = 0; j < this.size; j++)
			{
				make2.addStatement("this.$L = " + this.vectype.convertTo("original.get(" + i + ", " + j + ")"), this.varName(i, j), i, j);
			}
		}
		this.type.addMethod(make2.build());
		
		MethodSpec.Builder make3 = MethodSpec.constructorBuilder();
		make3.addModifiers(Modifier.PUBLIC);
		for (int i = 0; i < this.size; i++)
		{
			for (int j = 0; j < this.size; j++)
			{
				make3.addParameter(this.vectype.type, this.varName(i, j));
				make3.addStatement("this.$L = $L", this.varName(i, j), this.varName(i, j));
			}
		}
		this.type.addMethod(make3.build());
	}
	
	private void sizeMethods()
	{
		MethodSpec.Builder make1 = MethodSpec.methodBuilder("rows");
		make1.addAnnotation(Override.class);
		make1.addModifiers(Modifier.PUBLIC);
		make1.returns(int.class);
		make1.addStatement("return $L", this.size);
		this.type.addMethod(make1.build());
		
		MethodSpec.Builder make2 = MethodSpec.methodBuilder("columns");
		make2.addAnnotation(Override.class);
		make2.addModifiers(Modifier.PUBLIC);
		make2.returns(int.class);
		make2.addStatement("return $L", this.size);
		this.type.addMethod(make2.build());
		
		MethodSpec.Builder make3 = MethodSpec.methodBuilder("isSquare");
		make3.addAnnotation(Override.class);
		make3.addModifiers(Modifier.PUBLIC);
		make3.returns(boolean.class);
		make3.addStatement("return true");
		this.type.addMethod(make3.build());
	}
	
	private void transposeMethod()
	{
		MethodSpec.Builder make = MethodSpec.methodBuilder("transpose");
		make.addAnnotation(Override.class);
		make.addModifiers(Modifier.PUBLIC);
		make.returns(this.mytype);
		make.addStatement("$T out = new $T()", this.mytype, this.mytype);
		make.addStatement("out.extendIdentity = this.extendIdentity");
		for (int i = 0; i < this.size; i++)
		{
			for (int j = 0; j < this.size; j++)
			{
				make.addStatement("out.$L = this.$L", this.varName(i, j), this.varName(j, i));
			}
		}
		make.addStatement("return out");
		this.type.addMethod(make.build());
	}
	
	private void createMethods()
	{
		MethodSpec.Builder make1 = MethodSpec.methodBuilder("zero");
		make1.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
		make1.returns(this.mytype);
		make1.addStatement("$T out = new $T()", this.mytype, this.mytype);
		make1.addStatement("out.extendIdentity = false");
		make1.addStatement("return out");
		this.type.addMethod(make1.build());
		
		MethodSpec.Builder make2 = MethodSpec.methodBuilder("identity");
		make2.addModifiers(Modifier.PUBLIC, Modifier.STATIC);
		make2.returns(this.mytype);
		make2.addStatement("$T out = new $T()", this.mytype, this.mytype);
		make2.addStatement("out.extendIdentity = true");
		for (int i = 0; i < this.size; i++)
		{
			make2.addStatement("out.$L = 1", this.varName(i, i));
		}
		make2.addStatement("return out");
		this.type.addMethod(make2.build());
	}
}