package blue.made.bluegin.core.util;

public interface Matrix 
{
	public int rows();
	
	public int columns();
	
	public double get(int row, int column);
	
	public void set(int row, int column, double value);
	
	public double[][] values();
	
	public Matrix transpose();
	
	public default boolean isSquare()
	{
		return this.rows() == this.columns();
	}
	
	public Matrix part(int row0, int row1, int column0, int column1);
	
	public void copyPart(int torow0, int torow1, int tocolumn0, int tocolumn1, int fromrow, int fromcolumn, Matrix from);
	
	public double det();
	
	public Matrix inverse();
	
	public Vector transform(Vector in);
	
	public Matrix transform(Matrix in);
	
	public static Matrix composition(Matrix a, Matrix b)
	{
		return b.transform(a);
	}
}
