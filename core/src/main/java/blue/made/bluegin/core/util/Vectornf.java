package blue.made.bluegin.core.util;

public class Vectornf implements Vector
{
	public float[] vector;
	
	public Vectornf(float... vector)
	{
		this.vector = vector;
	}
	
	public Vectornf(Vector vector)
	{
		this.vector = new float[vector.size()];
		for (int i = 0; i < vector.size(); i++)
		{
			this.vector[i] = (float) vector.get(i);
		}
	}
	
	public Vectornf(int size, float value)
	{
		this.vector = new float[size];
		this.fill(value);
	}
	
	public Vectornf(int size)
	{
		this.vector = new float[size];
	}
	
	@Override
	public int size() 
	{
		return this.vector.length;
	}

	@Override
	public double get(int index) 
	{
		if (index >= 0 && index < vector.length)
		{
			return this.vector[index];
		}
		return 0;
	}

	@Override
	public void set(int index, double value) 
	{
		if (index >= 0 && index < vector.length)
		{
			this.vector[index] = (float) value;
		}
	}

	@Override
	public double[] values() 
	{
		double[] out = new double[vector.length];
		for (int i = 0; i < vector.length; i++)
		{
			out[i] = vector[i];
		}
		return out;
	}

	@Override
	public double lengthSqr() 
	{
		double sum = 0;
		for (int i = 0; i < vector.length; i++)
		{
			sum += vector[i] * vector[i];
		}
		return sum;
	}

	@Override
	public Vectornf clone() 
	{
		return new Vectornf(this.vector.clone());
	}

	@Override
	public void copy(Vector from) 
	{
		this.vector = new float[from.size()];
		for (int i = 0; i < from.size(); i++)
		{
			this.vector[i] = (float) from.get(i);
		}
	}

	@Override
	public void add(Vector other) 
	{
		for (int i = 0; i < vector.length; i++)
		{
			this.vector[i] += other.get(i);
		}
	}

	@Override
	public void subtract(Vector other) 
	{
		for (int i = 0; i < vector.length; i++)
		{
			this.vector[i] -= other.get(i);
		}
	}

	@Override
	public void multiply(Vector other) 
	{
		for (int i = 0; i < vector.length; i++)
		{
			this.vector[i] *= other.get(i);
		}
	}

	@Override
	public void divide(Vector other) 
	{
		for (int i = 0; i < vector.length; i++)
		{
			this.vector[i] /= other.get(i);
		}
	}

	@Override
	public void add(double value)
	{
		for (int i = 0; i < vector.length; i++)
		{
			this.vector[i] += value;
		}
	}

	@Override
	public void subtract(double value) 
	{
		for (int i = 0; i < vector.length; i++)
		{
			this.vector[i] -= value;
		}	
	}

	@Override
	public void multiply(double value) 
	{
		for (int i = 0; i < vector.length; i++)
		{
			this.vector[i] *= value;
		}
	}

	@Override
	public void divide(double value) 
	{
		for (int i = 0; i < vector.length; i++)
		{
			this.vector[i] /= value;
		}
	}

	@Override
	public void fill(double value) 
	{
		for (int i = 0; i < vector.length; i++)
		{
			this.vector[i] = (float) value;
		}
	}

}
