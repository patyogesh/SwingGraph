package SwingGraph;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenu;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.BevelBorder;

public class TestApp2 extends JFrame {

	//private JPanel contentPane;
	private JLayeredPane contentPane;
	double yVal = 0;
	static XYPlot xyPlot = null;
    static XYSeries xySeries = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestApp2 frame = new TestApp2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private ChartPanel drawChart() {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        xySeries = new XYSeries("Air Volume per unit Time");

        xySeriesCollection.addSeries(xySeries);
        
        JFreeChart chart = ChartFactory.createScatterPlot("Breathing Co-ordinator", "Time (ms)", "Volume (liters)", xySeriesCollection);

        // Get the XY Plot from the scatter chart
        xyPlot = (XYPlot) chart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        
        ValueAxis Xaxis = xyPlot.getDomainAxis();
        Xaxis.setAutoRange(true);
        Xaxis.setFixedAutoRange(80.0);
        ValueAxis Yaxis = xyPlot.getRangeAxis();
        Yaxis.setFixedAutoRange(6.0);

        // Create the renderer
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);

        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
        domain.setVerticalTickLabels(false);
        
        NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
        range.setVerticalTickLabels(false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMaximumDrawHeight(700);
        chartPanel.setRefreshBuffer(true);
        chartPanel.setLocation(0, 0);
        chartPanel.setSize(713, 600);
        chartPanel.setLayout(new BorderLayout(0, 0));
        	       
        NumberAxis xAxis = (NumberAxis) xyPlot.getDomainAxis();
        NumberAxis yAxis = (NumberAxis) xyPlot.getRangeAxis();
        
        /* Set range for Y axis values */
        yAxis.setRange(-1.0, 6.0);
        
         /* Draw Threshold Line */ 
        ValueMarker marker = new ValueMarker(1.5);  // position is the value on the axis
        marker.setPaint(Color.black);
        marker.setStroke(new BasicStroke(2));
                	        
        xyPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        xyPlot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        xyPlot.addRangeMarker(marker);
        
        return chartPanel;
	}
	/**
	 * Create the frame.
	 */
	public TestApp2() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 728, 762);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		//contentPane = new JPanel();
		contentPane = new JLayeredPane();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		ChartPanel chartPanel = drawChart();
		contentPane.add(chartPanel, BorderLayout.NORTH);
	}
}
