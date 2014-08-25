package SwingGraph;

import javax.swing.*;  
import java.awt.Color;  
import java.awt.Dimension;  
import java.awt.Graphics;  
//import java.awt.Image;  
import java.util.*;  

public class GraphPanel extends JPanel implements Runnable{  

	ArrayList<PlotPoints> pointsAL;  
	PlotPoints[] points;  
	//ImageIcon II;  
	//Image img;   
	int zeroX= 7;  
	int zeroY= 149;  
	int zeroValueX  = 16;  
	int zeroValueY  = 145;  
	int intervalX= 77;  
	int intervalY   = 38;  
	int maxValueY= 120;  
	int valueToPlot = 145;  

	int secondsInGraphing = 0;  
	int numberOfLinesToDraw = 0;  

	Thread runThread;  

	public void newValue(int i ) { valueToPlot = i; }  

	public void run() {  
		while( true) {  
			try {  
				Thread.sleep( 1000); // one update per second  
			} catch( InterruptedException ie ){ /**/ }  

			secondsInGraphing++;  
			numberOfLinesToDraw++;  
			if ( numberOfLinesToDraw > 5 ) numberOfLinesToDraw = 5;  

			for ( int i = 5; i > 0; i-- ) {  
				points[i].setValue( points[i - 1].gimmeValue());  
			}  
			points[0].setValue( valueToPlot);  
			repaint();  
		} // close while block  
	}  
	Dimension size = new Dimension( 500, 300 );  

	public GraphPanel() {  
		setSize( size );  
		setPreferredSize(size);  
		setMinimumSize( size);  
		setMaximumSize( size);  
		setLayout(null);  
		pointsAL = new ArrayList<PlotPoints>();  

		PlotPoints p = new PlotPoints( 401, 145, valueToPlot );  
		pointsAL.add(p);  
		p = new PlotPoints( 324, 145, valueToPlot );  
		pointsAL.add(p);  
		p = new PlotPoints( 247, 145, valueToPlot );  
		pointsAL.add(p);  
		p = new PlotPoints( 170, 145, valueToPlot );  
		pointsAL.add(p);  
		p = new PlotPoints( 93, 145, valueToPlot );  
		pointsAL.add(p);  
		p = new PlotPoints( 16, 145, valueToPlot );  
		pointsAL.add(p);  

		points = pointsAL.toArray( new PlotPoints[0] );  

		runThread = new Thread( this );  
		runThread.setDaemon(true);  
		runThread.start();  
	}  

	public void drawTheBackground( Graphics g) {  
		g.setColor(Color.WHITE);  
		g.fillRect(0, 0, size.width, size.height);  
		g.setColor(Color.lightGray);  
		// first the horizontal lines  
		for( int i = 0; i < 7; i++ ) {  
			g.drawLine(zeroValueX,                 28 + i * intervalY,   
					zeroValueX + intervalX * 6, 28 + i * intervalY);  
		}  
		// now the vertical lines  
		for( int i = 0; i < 8; i++) {  
			g.drawLine(zeroValueX + intervalX * i, 28,   
					zeroValueX + intervalX * i, 28 + 6 * intervalY);  
		} // close for loop  

		g.setColor(Color.white);  
		g.drawString("0", zeroX, zeroY);  

		// draw the seconds axis on the background:  
		int secondsYpos = zeroValueY + (int)(intervalY * 3.5);  
		for ( int i = 0; i < 6; i++ ) {  
			int secondsXpos = zeroValueX + intervalX * ( 5 - i) ;  

			g.drawString( "" + ( secondsInGraphing - i ), secondsXpos, secondsYpos );  
			if ( 1 > ( secondsInGraphing - i)) break;  
		} // close for loop  
	}  

	public void paintComponent(Graphics g){  
		Color oldColor;  
		oldColor = g.getColor();  

		drawTheBackground( g);  

		g.setColor(Color.RED );  

		int index = 0;  
		for( int i = 0; i < 5; i++ ) {  
			g.drawLine(points[i  ].gimmeX(), zeroValueY +   
					( maxValueY - zeroValueY ) * points[i  ].gimmeValue()/zeroValueY,   
					points[i+1].gimmeX(), zeroValueY +   
					( maxValueY - zeroValueY ) * points[i+1].gimmeValue()/zeroValueY);  
			index++;  
			if ( index + 1 > numberOfLinesToDraw ) break;  
		} // close for loop  

		g.drawString("valueToPlot=" + valueToPlot, 40, 40);  
		// restore the old system color whatever it was:  
		g.setColor(oldColor);  
	}  
} // close class GraphPanel  