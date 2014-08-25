package SwingGraph;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.border.StrokeBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;





















import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.sun.corba.se.spi.orbutil.fsm.Action;
import com.sun.glass.events.KeyEvent;
import com.sun.javafx.charts.Legend;
import com.sun.prism.paint.Paint;
import com.sun.xml.internal.bind.v2.runtime.Location;



import com.sun.xml.internal.ws.api.Component;







/*
 * Add Graph to swing app
 */
import java.io.*;
import java.util.StringTokenizer;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/*
 * End Add Graph to swing app
 */
public class SwingGraph extends ApplicationFrame {

    private static final String TITLE = "Dynamic Series";
    private static final String START = "Start";
    private static final String STOP = "Stop";
    private static final float MINMAX = 100;
    private static final int COUNT = 2 * 60;
    private static final int FAST = 100;
    private static final int SLOW = FAST * 5;
    private static final Random random = new Random();
    private Timer timer;

    public SwingGraph(final String title) {
        super(title);
        final DynamicTimeSeriesCollection dataset =
            new DynamicTimeSeriesCollection(1, COUNT, new Second());
        dataset.setTimeBase(new Second(0, 0, 0, 1, 1, 2011));
        dataset.addSeries(gaussianData(), 0, "Gaussian data");
        JFreeChart chart = createChart(dataset);

        final JButton run = new JButton(STOP);
        run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cmd = e.getActionCommand();
                if (STOP.equals(cmd)) {
                    timer.stop();
                    run.setText(START);
                } else {
                    timer.start();
                    run.setText(STOP);
                }
            }
        });

        final JComboBox combo = new JComboBox();
        combo.addItem("Fast");
        combo.addItem("Slow");
        combo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Fast".equals(combo.getSelectedItem())) {
                    timer.setDelay(FAST);
                } else {
                    timer.setDelay(SLOW);
                }
            }
        });

        this.add(new ChartPanel(chart), BorderLayout.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(run);
        btnPanel.add(combo);
        this.add(btnPanel, BorderLayout.SOUTH);

        timer = new Timer(FAST, new ActionListener() {

            float[] newData = new float[1];

            @Override
            public void actionPerformed(ActionEvent e) {
                newData[0] = randomValue();
                dataset.advanceTime();
                dataset.appendData(newData);
            }
        });
    }

    private float randomValue() {
        return (float) (random.nextGaussian() * MINMAX / 3);
    }

    private float[] gaussianData() {
        float[] a = new float[COUNT];
        for (int i = 0; i < a.length; i++) {
            a[i] = randomValue();
        }
        return a;
    }

    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            TITLE, "hh:mm:ss", "milliVolts", dataset, true, true, false);
        final XYPlot plot = result.getXYPlot();
        ValueAxis domain = plot.getDomainAxis();
        domain.setAutoRange(true);
        ValueAxis range = plot.getRangeAxis();
        range.setRange(-MINMAX, MINMAX);
        return result;
    }

    public void start() {
        timer.start();
    }

    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                SwingGraph demo = new SwingGraph(TITLE);
                demo.pack();
                RefineryUtilities.centerFrameOnScreen(demo);
                demo.setVisible(true);
                demo.start();
            }
        });
    }
}

/*
 * SwingGraph
 */
/*public class SwingGraph extends JFrame {

    // The XYSeries we intend to add our values to
    XYSeries xySeries = null;
    static XYPlot xyPlot = null;

    public SwingGraph() throws HeadlessException {
        super("USB Plot");

        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        xySeries = new XYSeries("Value");

        xySeriesCollection.addSeries(xySeries);
        ChartPanel chartPanel = constructChart(xySeriesCollection);

        add(chartPanel, BorderLayout.PAGE_END);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    
    public void addPoint(Number x, Number y) {
    	XYItemRenderer renderer = xyPlot.getRenderer();
        
    	//if(y.doubleValue() > 2.0)
    		
    	if(y.doubleValue() >= 1.0) {
    		System.out.println(y.toString() + "Setting RED");
    		GradientPaint gPaint = new GradientPaint(0.0f, 6.0f, Color.lightGray, 2.0f, 6.0f, Color.green);
        	xyPlot.setBackgroundPaint(gPaint);
          	renderer.setSeriesPaint(0, Color.red);
  		}
          else {
        	  GradientPaint gPaint = new GradientPaint(4.0f, 6.0f, Color.lightGray, 3.0f, 6.0f, Color.lightGray);
          	xyPlot.setBackgroundPaint(gPaint);
        	System.out.println(y.toString() + "Setting BLUE");
  			renderer.setSeriesPaint(0, Color.blue);
  		}
    	
    	//xyPlot.setBackgroundPaint(Color.yellow);
    	//IntervalMarker iMarker = new IntervalMarker(0.0, 2.0, Color.LIGHT_GRAY);
    	//xyPlot.addRangeMarker(iMarker);
    	
    	
    	xyPlot.setRenderer(renderer);
    	
        xySeries.add(x, y);
    }
   
    private ChartPanel constructChart(XYSeriesCollection xySeriesCollection) {
        // Construct the scatter plot from the provided xySeriesCollection
        JFreeChart chart = ChartFactory.createScatterPlot("Title", "X Axis", "Y Axis", xySeriesCollection);

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
        xyPlot.setRenderer(renderer);

        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
        domain.setVerticalTickLabels(false);
        
        NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
        range.setVerticalTickLabels(false);

        ChartPanel chartPanel = new ChartPanel(chart);
        
        ValueMarker marker = new ValueMarker(2);  // position is the value on the axis
        marker.setPaint(Color.DARK_GRAY);
        marker.setStroke(new BasicStroke(3));
        
        xyPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);        
        xyPlot.addRangeMarker(marker);
        
        return chartPanel;       
    }
   
    public static void main(String args[]) throws Exception {
    	List<Double> returnList = new ArrayList<>();
    
  	   	for(double i = 0.0; i < 3.0; i+= 0.1) {
    		returnList.add(i);
    		if(i==0.0) {
    			returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	
    		}
    	}
    	for(double i = 3.0; i >= 0.0; i-= 0.1) {
    		returnList.add(i);
    		if(i==0.0) {
    			returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    	    	returnList.add(0.0);
    		}
    	}
    	
    	
        returnList.add(6.0);
        
        SwingGraph frame = new SwingGraph();
        frame.setVisible(true);
        double count = 0;

        while (true) {
            Thread.sleep(1000);
        	
            List<Double> values = returnList;

            for (Double val : values) {
            	
                if(val >= 0)
                	frame.addPoint(count += 0.4, val);
            }
        }

    }

}*/
/*
 * SwingGraph
 */


/*import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import com.sun.prism.paint.Color;


public class SwingGraph {
    static TimeSeries ts = new TimeSeries("data", "TIME", "VALUE");

    public static void main(String[] args) throws InterruptedException {
        gen myGen = new gen();
        new Thread(myGen).start();

        TimeSeriesCollection dataset = new TimeSeriesCollection(ts);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "GraphTest",
            "Time",
            "Value",
            dataset,
            true,
            true,
            false
        );
        final XYPlot plot = chart.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);

        JFrame frame = new JFrame("GraphTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ChartPanel label = new ChartPanel(chart);
        frame.getContentPane().add(label);
        //Suppose I add combo boxes and buttons here later

        frame.pack();
        frame.setVisible(true);
    }

    static class gen implements Runnable {
        private Random randGen = new Random();

        public void run() {
            while(true) {
                int num = randGen.nextInt(4);
                //System.out.println(num);
                
                RegularTimePeriod rtp = new Second();
                ts.addOrUpdate(rtp, num);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
            }
        }
    }

}
*/

/*package SwingGraph;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.*;
import org.jfree.ui.*;

public class SwingGraph extends JFrame
{
    public SwingGraph(String s)
    {
        super(s);
        XYDataset xyDataset = createDataset();
        JFreeChart jfreechart = createChart(xyDataset);
        ChartPanel chartpanel = new ChartPanel(jfreechart);
        chartpanel.setPreferredSize(new Dimension(800, 400));
        setContentPane(chartpanel);
    }
    
    private static XYDataset createDataset()
    {
        XYSeries sinSeries = new XYSeries("Sin");
        XYSeries cosSeries = new XYSeries("Cos");
        for (double x = 0.0; x < 720; x += 10)
        {
            sinSeries.add(x, Math.sin(Math.toRadians(x)));
            cosSeries.add(x, Math.cos(Math.toRadians(x)));
        }
        XYSeriesCollection seriesCollection = new XYSeriesCollection();
        seriesCollection.addSeries(sinSeries);
        seriesCollection.addSeries(cosSeries);
        return seriesCollection;
    }
    
    private static JFreeChart createChart(XYDataset xydataset)
    {
        JFreeChart jfreechart = ChartFactory.createXYLineChart("Sin Curve Demo", "Angle (Deg)", "Y", xydataset, PlotOrientation.VERTICAL, true, true, false);
        jfreechart.setBackgroundPaint(Color.white);
        XYPlot xyplot = (XYPlot)jfreechart.getPlot();
        xyplot.setBackgroundPaint(Color.white);
        xyplot.setDomainGridlinePaint(Color.gray);
        xyplot.setRangeGridlinePaint(Color.gray);
        return jfreechart;
    }
    
    public static JPanel createDemoPanel()
    {
        JFreeChart jfreechart = createChart(createDataset());
        return new ChartPanel(jfreechart);
    }
    
    public static void main(String args[])
    {
        SwingGraph sinCosChart = new SwingGraph("Sin & Cos Curve Demo");
        sinCosChart.pack();
        sinCosChart.setVisible(true);
    }
}*/