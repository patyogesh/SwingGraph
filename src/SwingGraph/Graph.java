
package SwingGraph;

import javax.swing.*;  
import java.awt.Color;  
import java.awt.Dimension;  
import SwingGraph.GraphPanel;  

public class Graph {  
	String version = "2007.06.15.0017";  
	GraphPanel panel;  

	public Graph() { /**/ }  
	// change the value to be graphed using this update function  
	public void update( int i ) { panel.newValue( i); }  

	public void go() {  
		/* uncomment this if you have the windows classic L&F installed 
try { 
UIManager.setLookAndFeel( 
"com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"); 
} catch( Exception ignored ){  
System.out.println("Couldn't switch to windows Classic Look and Feel"); 
} 
		 */  
		JFrame.setDefaultLookAndFeelDecorated(false);  
		JFrame frame = new JFrame("Graph Component Test version " + version );  
		Dimension dim = new Dimension( 508, 330 );  
		frame.setSize(dim);  
		frame.setMaximumSize(dim);  
		frame.setMinimumSize(dim);  
		frame.setPreferredSize(dim);  
		frame.setLocation( 100,100 );  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		frame.setVisible(true);  
		frame.setLayout(null);  
		frame.setBackground(Color.black);  
		frame.setForeground(Color.black);  

		panel = new GraphPanel();  
		frame.add(panel);  
		panel.setBackground(Color.WHITE);  
	} // close go()  

	public static void main(String[] args) {  
		// we're not invoking this in some kind of invokeLater function  
		// because we don't have any text related components and we   
		// don't monkey with any components not on the EDT so....  
		Graph g = new Graph();  
		g.go();  
		g.update(100);  

		for( int i = 4; i < 12; i++ ) {  
			try {  
				Thread.sleep(2000);  
			} catch(Exception e) { /**/}  
			g.update(i * 50 );  
		}   

	} // close main()  
} // close class Graph  