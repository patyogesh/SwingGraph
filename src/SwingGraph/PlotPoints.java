package SwingGraph;


public class PlotPoints {  
	int x;  
	int y;  
	int value;  

	public PlotPoints( int X, int Y, int Value ) {  
		x = X; y = Y; value = Value;  
	}  
	public void setValue( int i ) { value = i; }  
	public int gimmeX() { return x; }  
	public int gimmeY() { return y; }  
	public int gimmeValue(){ return value; }  
}  