package SwingGraph;



import java.awt.Dimension;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JFrame;
import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.XYDataset;


public class TimeSeriesChart {

    private static XYDataset createDataset() {
        return new AbstractXYDataset() {

            private static final int N = 16;
            private final long t = new Date().getTime();

            @Override
            public int getSeriesCount() {
                return 1;
            }

            @Override
            public Comparable getSeriesKey(int series) {
                return "Data";
            }

            @Override
            public int getItemCount(int series) {
                return N;
            }

            @Override
            public Number getX(int series, int item) {
                return Double.valueOf(t + item * 1000 * 3600 * 24);
            }

            @Override
            public Number getY(int series, int item) {
                return Math.pow(item, 1.61);
            }
        };
    }

    private static JFreeChart createChart(final XYDataset dataset) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "Test", "Day", "Value", dataset, false, false, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis domain = (DateAxis) plot.getDomainAxis();
        domain.setDateFormatOverride(DateFormat.getDateInstance());
        //domain.setDateFormatOverride(DateFormat.getDateTimeInstance());
        return chart;
    }

    public static void main(String[] args) {

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        XYDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart) {

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 300);
            }
        };
        f.add(chartPanel);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}