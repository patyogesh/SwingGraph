package SwingGraph;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.awt.Stroke;

import javafx.stage.FileChooser;

import javax.comm.CommPort;
import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYDataImageAnnotation;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

import com.sun.corba.se.impl.presentation.rmi.DynamicStubImpl;
import com.sun.corba.se.spi.orbutil.fsm.Action;
import com.sun.javafx.scene.control.skin.SliderSkin;

import javax.swing.AbstractAction;
//import test.gui.CurrentState;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.Timer;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenuItem;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JProgressBar;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.PortUnreachableException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

import javax.swing.JTextField;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

import java.awt.Rectangle;

import javax.swing.JDesktopPane;
import javax.swing.JCheckBox;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TestApp extends JFrame implements SerialPortEventListener {

	private static JPanel contentPane;
	
	static JFrame frameNewMenu;
	
	static TestApp testAppFrame;
	JPanel timerDispPanel;
	JSlider sliderMINThreshold;
	JSlider sliderUpperThreshold;
	ValueMarker markerThresholdWindowTop;
	ValueMarker markerThreshold;
	
	SetAxisScaleMenuWindow axisScaleWin;
	SetSliderScaleNewMenuWindow sliderScaleWin;
	
	static SpaceAction spaceAction;
	String selectedPort;
	static boolean serialPortFound = false;
	static boolean patientReady = false;
	static boolean theropistReady = false;
	
	private JLabel TimeElapsed;
	public  Timer  timerCountDown;
	public  Timer  timerToClearOff;
	public  Timer  timerToCalibrate;
	
	boolean clearOffTimerRunning = false;
	boolean calibrationTimerRunning = false;
	
	ActionListener countDownTimeListener;
	ActionListener clearOffTimerListener;
	ActionListener	calibrationTimerListener;
	long countDown = 0;
	public long countElapsed = 0;
	long clearOffCountDown = 0;
	long calibbrationCountDown = 0;
	final static int CALIBRATION_WINDOW_SIZE = 1024;
	double calibratedVal = 0;
	int[][] calibWindow = new int[CALIBRATION_WINDOW_SIZE][2];
	int calibWindowIndex = 0;
	boolean  calibrationDone = false;

	boolean bTimeOver = false;
	JLabel lblTimeRemaining;
	
	static Double DEFAULT_THRESHOLD = 1.0;
	static Double minThresholdMarker = DEFAULT_THRESHOLD;
	static Double maxThresholdMarker = minThresholdMarker + 0.5;
	
	java.util.Hashtable<Integer, JLabel> thresholdTable;
	java.util.Hashtable<Integer, JLabel> thresholdUpperTable;
	
	double xVal = 0;
	static XYPlot xyPlot = null;
    static XYSeries xySeries = null;
    private ChartPanel chartPanel_1;
    
    private DynamicTimeSeriesCollection dataset;
    private JFreeChart chart;
    
    JComboBox<String> comboBox;
    String[] portsAvailable = new String[5];
    
    public enum CurrentState{
		INHALE,
		EXHALE,
		IDLE,
		BREATH_HELD,
		INIT
	};
	CurrentState curr_state;
	CurrentState prev_state;
	long  sample_time_start = 0;
	long  sample_time_end = 0;
	long  sample_time_elapsed = 0;
	long  sample_time_elapsed_counter = 0;
	double cum_volume = 0;
	final static int SAMPLE_CUMULATION_TIMEOUT = 50;
	
	static FileOutputStream sampleLogFileName;
	static PrintStream outSampleLogFileName;
	
	/*
	 * Following fields are all related to Serial communication
	 */
	//for containing the ports that will be found
	static boolean port_found = false;
	private Enumeration ports = null;
	//map the port names to CommPortIdentifiers
	private HashMap portMap = new HashMap();
	

	//this is the object that contains the opened port
	private CommPortIdentifier selectedPortIdentifier = null;
	private SerialPort serialPort = null;

	//input and output streams for sending and receiving data
	private InputStream input = null;
	private OutputStream output = null;

	//just a boolean flag that i use for enabling
	//and disabling buttons depending on whether the program
	//is connected to a serial port or not
	private boolean bConnected = false;

	//the timeout value for connecting with the port
	final static int PORT_CONNECT_TIMEOUT = 2000;
	final static int TREATMENT_DURATION = 5;
	final static int SCREENFUL_DURATION = 30;
	final static int CALIBRATION_DURATION = 4;
	private boolean countDownTimerRunning = false;

	//some ascii values for for certain things
	final static int SPACE_ASCII = 32;
	final static int DASH_ASCII = 45;
	final static int NEW_LINE_ASCII = 10;
	
	//a string for recording what goes on in the program
	//this string is written to the GUI
	String logText = "";
	private JTextField txtSetTimeout;
	
	
	public JLabel lblPrintPatName;
	public JLabel lblPrintPatID;
	public JLabel lblPrintTestID;
	public JLabel lblPrintOngoingSess;
	
	/*
	 * Menu data structures
	 */
	File fileOpened;
	File fileSaved;

	/*
	 * Set look and feel to native Windows platform
	 */
	static void set_native_look_and_feel() {
		try {
			// Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel(
					"com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} 
		catch (UnsupportedLookAndFeelException e) {
			// handle exception
		}
		catch (ClassNotFoundException e) {
			// handle exception
		}
		catch (InstantiationException e) {
			// handle exception
		}
		catch (IllegalAccessException e) {
			// handle exception
		}
	}
	/**
	 * Launch the application.
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		set_native_look_and_feel();
		sampleLogFileName = new FileOutputStream("C:/ABC Files/"+"testNumber_"+"_log"+".txt");
		try {
		outSampleLogFileName = new PrintStream(sampleLogFileName);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {				
					testAppFrame = new TestApp();
					testAppFrame.setVisible(true);
							
					contentPane.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "doEnterAction");
					contentPane.getActionMap().put("doEnterAction", spaceAction);
					
					System.out.println("Object created!! Searching for ports now");
					
					port_found = false;
					
					port_found = testAppFrame.searchForPorts();
					
					if(!port_found) {
						JOptionPane.showMessageDialog(testAppFrame,
							    "NO COM Port Found, Check Bluetooth Connectivity",
							    "Inane warning",
							    JOptionPane.WARNING_MESSAGE);
						return;
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	void stopCountDownTimer() {
		timerCountDown.stop();
		countDownTimerRunning = false;
		timerDispPanel.setBackground(new Color(240, 240, 240));
		bTimeOver = true;
	}
	
	void stopClearOffTimer() {
		timerToClearOff.stop();
		System.out.println("Clearing OUT clearOffTimer");
		clearOffTimerRunning = false;
		/*
		 * This is to clear off the plot so far 
		 */
		xySeries.clear();
	}
/*	
	public void addPoint(Number x, Number y) {
		XYItemRenderer renderer = xyPlot.getRenderer();
		//System.out.println("addPoint y = " + y.doubleValue() + "  minThresholdMarker" + minThresholdMarker);
		if(y.doubleValue() > minThresholdMarker) {
			GradientPaint gPaint = new GradientPaint(2.0f, 6.0f, Color.lightGray, 2.0f, 6.0f, Color.green);
			xyPlot.setBackgroundPaint(gPaint);
			renderer.setSeriesPaint(0, Color.green);


			// Start Timer

			if(!countDownTimerRunning && !bTimeOver) {
				timerCountDown = new Timer(1000, countDownTimeListener);
				timerCountDown.setInitialDelay(0);				
				countDown = TREATMENT_DURATION;
				countElapsed = 0;
				timerCountDown.start();
				countDownTimerRunning = true;
				timerDispPanel.setBackground(new Color(240, 230, 140));
			}
		}
		else {
			GradientPaint gPaint = new GradientPaint(4.0f, 6.0f, Color.lightGray, 3.0f, 6.0f, Color.lightGray);
			xyPlot.setBackgroundPaint(gPaint);
			if(patientReady) {
				renderer.setSeriesPaint(0, Color.blue);
			}
			else {
				renderer.setSeriesPaint(0, Color.black);
			}

			timerDispPanel.setBackground(new Color(240, 240, 240));
			if(bTimeOver)
				bTimeOver = false;
			else
				bTimeOver = true;
		}

		xyPlot.setRenderer(renderer);
		float[] newData = new float[1];
		System.out.println("Plotting  " + y.floatValue());
		newData[0] = y.floatValue();
		dataset.advanceTime();
		dataset.appendData(newData);
	}
*/	
	/* 
	 * Following function is called from Serial Port receive event handler that supplies
	 * X,Y values
	 */
	public void addPoint(Number x, Number y) {
    	XYItemRenderer renderer = xyPlot.getRenderer();

    	Stroke stroke = new BasicStroke(
    			0.05f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    	renderer.setBaseOutlineStroke(stroke);

    	
    	//System.out.println("addPoint y = " + y.doubleValue() + "  minThresholdMarker" + minThresholdMarker);
		if(y.doubleValue() > minThresholdMarker) {
			GradientPaint gPaint = new GradientPaint(2.0f, 6.0f, Color.lightGray, 2.0f, 6.0f, Color.green);
        	xyPlot.setBackgroundPaint(gPaint);
			renderer.setSeriesPaint(0, Color.green);
			
			
			 //Start Timer
			 
			if(!countDownTimerRunning /*&& !bTimeOver */) {
				timerCountDown = new Timer(1000, countDownTimeListener);
				timerCountDown.setInitialDelay(0);				
				countDown = TREATMENT_DURATION;
				countElapsed = 0;
				timerCountDown.start();
				countDownTimerRunning = true;
				timerDispPanel.setBackground(new Color(240, 230, 140));
			}
		}
		else {
			GradientPaint gPaint = new GradientPaint(4.0f, 6.0f, Color.lightGray, 3.0f, 6.0f, Color.lightGray);
          	xyPlot.setBackgroundPaint(gPaint);
          	
          	if(countDownTimerRunning) {
      			stopCountDownTimer();
      		}
          	if(patientReady) {
          		renderer.setSeriesPaint(0, Color.blue);
          	}
          	else {
          		renderer.setSeriesPaint(0, Color.black);
          	}
			
			timerDispPanel.setBackground(new Color(240, 240, 240));
			if(bTimeOver)
				bTimeOver = false;
			else
				bTimeOver = true;
		}
		
		xyPlot.setRenderer(renderer);
        xySeries.add(x, y);
    }

	private ChartPanel drawChart() {

/*		DynamicTimeSeriesCollection dataset = new DynamicTimeSeriesCollection(1, 3, new Second());
        
        dataset.setTimeBase(new Second(0, 0, 0, 21, 9, 2014));
        dataset.addSeries(new float[1], 0, "Samples");
        chart = ChartFactory.createTimeSeriesChart(
            "Samples", "Time", "Samples", dataset, true, true, false);
        xyPlot = chart.getXYPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairLockedOnData(true);
        
        DateAxis Xaxis = (DateAxis) xyPlot.getDomainAxis();
        Xaxis.setAutoRange(true);
        Xaxis.setFixedAutoRange(10000);
        Xaxis.setDateFormatOverride(new SimpleDateFormat("hh:mm:ss"));
        final ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel);
        
        
        ValueAxis Yaxis = xyPlot.getRangeAxis();
        Yaxis.setFixedAutoRange(6.0);

        // Create the renderer
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);

        //NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
        DateAxis domain = (DateAxis) xyPlot.getDomainAxis();
        domain.setVerticalTickLabels(false);
        
        NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
        range.setVerticalTickLabels(false);

        chartPanel_1 = new ChartPanel(chart);
        chartPanel_1.setZoomAroundAnchor(true);
        chartPanel_1.setMaximumDrawHeight(700);
        chartPanel_1.setRefreshBuffer(true);
        chartPanel_1.setLocation(0, 0);
        chartPanel_1.setSize(1200, 519);
        chartPanel_1.setLayout(new BorderLayout(0, 0));
               
        //NumberAxis xAxis = (NumberAxis) xyPlot.getDomainAxis();
        DateAxis xAxis = (DateAxis) xyPlot.getDomainAxis();
        NumberAxis yAxis = (NumberAxis) xyPlot.getRangeAxis();
        
        // Set range for Y axis values
        yAxis.setRange(-3.0, 4.0);
                        	        
        xyPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        xyPlot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
//        xyPlot.addRangeMarker(marker);
        
        return chartPanel_1;*/

        
		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        xySeries = new XYSeries("");

        xySeriesCollection.addSeries(xySeries);
        
        JFreeChart chart = ChartFactory.createScatterPlot("Breathing Co-ordinator", "", "Volume (liters)", xySeriesCollection);

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
        renderer.setSeriesPaint(1, Color.blue);
        
        

        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
        domain.setVerticalTickLabels(false);
        
        NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
        range.setVerticalTickLabels(false);

        chartPanel_1 = new ChartPanel(chart);
        chartPanel_1.setZoomAroundAnchor(true);
        chartPanel_1.setMaximumDrawHeight(700);
        chartPanel_1.setRefreshBuffer(true);
        chartPanel_1.setLocation(0, 0);
        chartPanel_1.setSize(1200, 519);
        chartPanel_1.setLayout(new BorderLayout(0, 0));
               
        NumberAxis xAxis = (NumberAxis) xyPlot.getDomainAxis();
        NumberAxis yAxis = (NumberAxis) xyPlot.getRangeAxis();
        
        // Set range for Y axis values
        yAxis.setRange(-3.0, 4.0);
                        	        
        xyPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        xyPlot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

        
        return chartPanel_1;
        
	}
	
	class SpaceAction extends AbstractAction {	
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Finally spaceAction !!");
			if(! theropistReady) {
				theropistReady = true;
				contentPane.setBackground(Color.GREEN);
			} else {
				theropistReady = false;
				contentPane.setBackground(new Color(240,240,240));
			}
		}
	}
	/**
	 * Create the frame.
	 */
	public TestApp() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1378, 718);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		mnFile.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewMenuWindow nW = new NewMenuWindow();
				nW.setVisible(true);
			}
		});
		mntmNew.setIcon(new ImageIcon("C:\\Users\\yogesh\\workspace\\SwingGraph\\icons\\1408774854_common_new_edit_compose.png"));
		mntmNew.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		mnFile.add(mntmNew);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.setIcon(new ImageIcon("C:\\Users\\yogesh\\workspace\\SwingGraph\\icons\\1408774919_open-file.png"));
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Create a file chooser
				final JFileChooser fc = new JFileChooser("C:\\ABC Files");
				//In response to a button click:
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(TestApp.this);
				
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					fileOpened = fc.getSelectedFile();
					ObjectSerializationDemo impl = new ObjectSerializationDemo();
					
			    	impl.readData(fileOpened.getName(), 1); 
			    	System.out.println("Open ");
			    	System.out.println( impl.getPatName());
			    	System.out.println( impl.getPatID());
			    	System.out.println( impl.getTestID());
			    	
			    	
			    	lblPrintPatName.setText(impl.getPatName());
			    	lblPrintPatID.setText(impl.getPatID());
			    	lblPrintTestID.setText(Integer.toString(impl.getTestID()));
			    	lblPrintOngoingSess.setText(Integer.toString(impl.getBreathHoldTime()));
//					System.out.println("Opened " + fileOpened.getName());
				}
			}
		});
		mntmOpen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		mnFile.add(mntmOpen);
		
		JSeparator separator_4 = new JSeparator();
		mnFile.add(separator_4);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setIcon(new ImageIcon("C:\\Users\\yogesh\\workspace\\SwingGraph\\icons\\1408774958_save_24.png"));
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Create a file chooser
				final JFileChooser fc = new JFileChooser();
				//In response to a button click:
				int returnVal = fc.showSaveDialog(TestApp.this);
				
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					fileSaved = fc.getSelectedFile();
					System.out.println("Saved " + fileSaved.getName());
				}
			}
		});
		mntmSave.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save As");
		mntmSaveAs.setIcon(new ImageIcon("C:\\Users\\yogesh\\workspace\\SwingGraph\\icons\\1408774994_Save-as.png"));
		mntmSaveAs.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		mnFile.add(mntmSaveAs);
		
		JSeparator separator_5 = new JSeparator();
		mnFile.add(separator_5);
		
		JMenuItem mntmPrint = new JMenuItem("Print");
		mntmPrint.setIcon(new ImageIcon("C:\\Users\\yogesh\\workspace\\SwingGraph\\icons\\1408775078_Print_24x24.png"));
		mntmPrint.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		mnFile.add(mntmPrint);
		
		JSeparator separator_8 = new JSeparator();
		mnFile.add(separator_8);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Connect");
		mntmNewMenuItem.setIcon(new ImageIcon("C:\\Users\\yogesh\\workspace\\SwingGraph\\icons\\1409029290_Connect.png"));
		mntmNewMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		mnFile.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(testAppFrame.getConnected() == false) {
					//By default select one of the ports available
					int connectStatus = testAppFrame.connect(portsAvailable[1]);
					if(connectStatus == 1) {
						JOptionPane.showMessageDialog(testAppFrame,
							    "NO COM Port Found, Check Bluetooth Connectivity",
							    "Inane warning",
							    JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		
		JMenuItem mntmDisconnect = new JMenuItem("Disconnect");
		mntmDisconnect.setIcon(new ImageIcon("C:\\Users\\yogesh\\workspace\\SwingGraph\\icons\\1409029350_Disconnect.png"));
		mntmDisconnect.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		mnFile.add(mntmDisconnect);
		mntmDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(testAppFrame.getConnected() == true)
					testAppFrame.disconnect();
			}
		});
		
		JSeparator separator_6 = new JSeparator();
		mnFile.add(separator_6);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setIcon(new ImageIcon("C:\\Users\\yogesh\\workspace\\SwingGraph\\icons\\1408775305_Close.png"));
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mntmExit.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		mntmExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnEdit = new JMenu("Edit");
		mnEdit.setMnemonic('E');
		mnEdit.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		menuBar.add(mnEdit);
		
		JMenu mnAbout = new JMenu("Setup");
		mnAbout.setMnemonic('A');
		mnAbout.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		menuBar.add(mnAbout);
		
		JMenuItem mntmSetAxis = new JMenuItem("Set Axis Scale");
		mntmSetAxis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				axisScaleWin.setVisible(true);
			}
		});
		mnAbout.add(mntmSetAxis);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Set Slider Scale");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sliderScaleWin.setVisible(true);
			}
		});
		mnAbout.add(mntmNewMenuItem_1);
		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic('H');
		mnHelp.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		menuBar.add(mnHelp);
		contentPane = new JPanel();	

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
			
		JLabel lblSetMin = new JLabel("Set MIN");
		lblSetMin.setHorizontalAlignment(SwingConstants.CENTER);
		lblSetMin.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSetMin.setBounds(1245, 196, 59, 23);
		contentPane.add(lblSetMin);
		
		JLabel lblSetMax = new JLabel("Set MAX");
		lblSetMax.setBounds(1245, 359, 59, 23);
		contentPane.add(lblSetMax);
		lblSetMax.setHorizontalAlignment(SwingConstants.CENTER);
		lblSetMax.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		sliderScaleWin = new SetSliderScaleNewMenuWindow();
		sliderScaleWin.setDefaultParams(0.0, 3.5, 0.5);
		
		axisScaleWin = new SetAxisScaleMenuWindow();
		axisScaleWin.setDefaultYParams(-4.0, 3.0);
		
		ChartPanel chartPanel = drawChart();
		contentPane.add(chartPanel);
		contentPane.add(chartPanel_1);
		
		
		spaceAction = new SpaceAction();
//		contentPane.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "doEnterAction");
//		contentPane.getActionMap().put("doEnterAction", spaceAction);
			
		markerThresholdWindowTop = new ValueMarker(maxThresholdMarker);  // position is the value on the axis
		/* Draw Threshold Line */ 
        markerThresholdWindowTop.setPaint(Color.black);
        markerThresholdWindowTop.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 
                										   1.0f, new float[] {10.0f, 6.0f}, 0.0f));
        markerThresholdWindowTop.setValue(maxThresholdMarker);
        xyPlot.addRangeMarker(markerThresholdWindowTop);
     
		markerThreshold = new ValueMarker(DEFAULT_THRESHOLD);  // position is the value on the axis
		/* Draw Threshold Line */ 
        markerThreshold.setPaint(Color.black);
        markerThreshold.setStroke(new BasicStroke(2));
        markerThreshold.setValue(DEFAULT_THRESHOLD);
        xyPlot.addRangeMarker(markerThreshold);
		
		sliderMINThreshold = new JSlider(0, 7, 1);
		sliderMINThreshold.setToolTipText("Solid Line");
		
		sliderMINThreshold.setSnapToTicks(true);
		sliderMINThreshold.setPaintTicks(true);
		sliderMINThreshold.setMajorTickSpacing(1);
//		slider.setMinorTickSpacing(1);
		sliderMINThreshold.setValue(3);
		sliderMINThreshold.setPaintLabels(true);
		sliderMINThreshold.setMaximum(7);
		sliderMINThreshold.setOrientation(SwingConstants.VERTICAL);
		sliderMINThreshold.setBounds(1245, 222, 59, 123);
		contentPane.add(sliderMINThreshold);
		
		thresholdTable = new java.util.Hashtable<Integer, JLabel>();

		double scaleUnit = sliderScaleWin.getThresholdStart();
		double scaleInterval = sliderScaleWin.getThresholdScale();
		
		thresholdTable.put(new Integer(0), new JLabel(Double.toString(scaleUnit)));
		scaleUnit += scaleInterval;
		
		thresholdTable.put(new Integer(1), new JLabel(Double.toString(scaleUnit)));
		scaleUnit += scaleInterval;
		
		thresholdTable.put(new Integer(2), new JLabel(Double.toString(scaleUnit)));
		scaleUnit += scaleInterval;
		
		thresholdTable.put(new Integer(3), new JLabel(Double.toString(scaleUnit)));
		scaleUnit += scaleInterval;
		
		thresholdTable.put(new Integer(4), new JLabel(Double.toString(scaleUnit)));
		scaleUnit += scaleInterval;
		
		thresholdTable.put(new Integer(5), new JLabel(Double.toString(scaleUnit)));
		scaleUnit += scaleInterval;
		
		thresholdTable.put(new Integer(6), new JLabel(Double.toString(scaleUnit)));
		scaleUnit += scaleInterval;
		
		thresholdTable.put(new Integer(7), new JLabel(Double.toString(scaleUnit)));
			
		sliderMINThreshold.setLabelTable(thresholdTable);
		sliderMINThreshold.setEnabled(false);
		
		sliderMINThreshold.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				JLabel sliderMoveVal = thresholdTable.get(sliderMINThreshold.getValue());
				minThresholdMarker = Double.parseDouble(sliderMoveVal.getText());
				
				System.out.println("Threshold Changed to " + minThresholdMarker);
				
				markerThreshold.setValue(minThresholdMarker);
			}
		});
		
		/* 
		 * Upper (Dotted) threshold adjustment
		 */
		sliderUpperThreshold = new JSlider(0, 7, 3);
		sliderUpperThreshold.setToolTipText("Dash Line");
		sliderUpperThreshold.setSnapToTicks(true);
		sliderUpperThreshold.setPaintTicks(true);
		sliderUpperThreshold.setPaintLabels(true);
		sliderUpperThreshold.setOrientation(SwingConstants.VERTICAL);
		sliderUpperThreshold.setMajorTickSpacing(1);
		sliderUpperThreshold.setEnabled(false);
		sliderUpperThreshold.setBounds(1245, 382, 59, 123);
		contentPane.add(sliderUpperThreshold);
		
		thresholdUpperTable = new java.util.Hashtable<Integer, JLabel>();
		
		scaleUnit = sliderScaleWin.getThresholdStart();
		scaleInterval = sliderScaleWin.getThresholdScale();
		
		thresholdUpperTable.put(new Integer(0), new JLabel(Double.toString(scaleUnit)));
		scaleUnit += scaleInterval;
		
		thresholdUpperTable.put(new Integer(1), new JLabel(Double.toString(scaleUnit)));
		scaleUnit += scaleInterval;
		
		thresholdUpperTable.put(new Integer(2), new JLabel(Double.toString(scaleUnit)));
		scaleUnit += scaleInterval;
		
		thresholdUpperTable.put(new Integer(3), new JLabel(Double.toString(scaleUnit)));
		scaleUnit += scaleInterval;
		
		thresholdUpperTable.put(new Integer(4), new JLabel(Double.toString(scaleUnit)));
		scaleUnit += scaleInterval;
		
		thresholdUpperTable.put(new Integer(5), new JLabel(Double.toString(scaleUnit)));
		scaleUnit += scaleInterval;
		
		thresholdUpperTable.put(new Integer(6), new JLabel(Double.toString(scaleUnit)));
		scaleUnit += scaleInterval;
		
		thresholdUpperTable.put(new Integer(7), new JLabel(Double.toString(scaleUnit)));
		
		sliderUpperThreshold.setLabelTable(thresholdUpperTable);
		sliderUpperThreshold.setEnabled(false);

		sliderUpperThreshold.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				JLabel sliderMoveVal = thresholdUpperTable.get(sliderUpperThreshold.getValue());
				maxThresholdMarker = Double.parseDouble(sliderMoveVal.getText());
				
				System.out.println("Threshold Changed to " + maxThresholdMarker);
							
				markerThresholdWindowTop.setValue(maxThresholdMarker);
			}
		});              
		     
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(332, 534, 13, 117);
		contentPane.add(separator);
		
		JLabel lblSelectSerialPort = new JLabel("Select Port");
		lblSelectSerialPort.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSelectSerialPort.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectSerialPort.setBounds(1210, 98, 94, 27);
		contentPane.add(lblSelectSerialPort);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Patient Info", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(66, 530, 256, 121);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Patient Name");
		lblNewLabel.setBounds(10, 21, 80, 14);
		panel.add(lblNewLabel);
		
		JLabel lblPatient = new JLabel("Patient #");
		lblPatient.setBounds(10, 46, 80, 14);
		panel.add(lblPatient);
		
		JLabel lblType = new JLabel("Test Number");
		lblType.setBounds(10, 71, 80, 14);
		panel.add(lblType);
		
		JLabel lblOngoingSession = new JLabel("Ongoing Session");
		lblOngoingSession.setBounds(10, 96, 112, 14);
		panel.add(lblOngoingSession);
		
		lblPrintPatName = new JLabel("_________________");
		lblPrintPatName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPrintPatName.setBounds(100, 21, 146, 14);
		panel.add(lblPrintPatName);
		
		lblPrintPatID = new JLabel("_________________");
		lblPrintPatID.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPrintPatID.setBounds(100, 46, 146, 14);
		panel.add(lblPrintPatID);
		
		lblPrintTestID = new JLabel("_________________");
		lblPrintTestID.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPrintTestID.setBounds(100, 71, 146, 14);
		panel.add(lblPrintTestID);
		
		lblPrintOngoingSess = new JLabel("_________________");
		lblPrintOngoingSess.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPrintOngoingSess.setBounds(100, 96, 146, 14);
		panel.add(lblPrintOngoingSess);
		
		JPanel panel_1 = new JPanel();

		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Section 2", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(346, 530, 256, 121);
		contentPane.add(panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Section 3", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.setBounds(624, 530, 169, 121);
		contentPane.add(panel_2);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(996, 530, 13, 117);
		contentPane.add(separator_2);
		
		timerDispPanel = new JPanel();
		timerDispPanel.setBackground(new Color(240, 240, 240));
		timerDispPanel.setLayout(null);
		timerDispPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Breath Hold Time Remaining", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		timerDispPanel.setBounds(1003, 530, 197, 121);
		contentPane.add(timerDispPanel);
		
		lblTimeRemaining = new JLabel("00");
		lblTimeRemaining.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimeRemaining.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblTimeRemaining.setBounds(48, 21, 95, 78);
		timerDispPanel.add(lblTimeRemaining);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setBounds(1144, 530, 13, 117);
		contentPane.add(separator_3);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(612, 534, 13, 117);
		contentPane.add(separator_1);
		separator_1.setOrientation(SwingConstants.VERTICAL);
		
		JLabel lblBreathHoldTime = new JLabel("Breath Hold Time");
		lblBreathHoldTime.setHorizontalAlignment(SwingConstants.LEFT);
		lblBreathHoldTime.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblBreathHoldTime.setBounds(1209, 524, 123, 27);
		contentPane.add(lblBreathHoldTime);
		
		txtSetTimeout = new JTextField();
		txtSetTimeout.setBounds(1209, 549, 59, 27);
		contentPane.add(txtSetTimeout);
		txtSetTimeout.setColumns(10);
		
		JLabel lblSeconds = new JLabel("Seconds");
		lblSeconds.setHorizontalAlignment(SwingConstants.CENTER);
		lblSeconds.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSeconds.setBounds(1264, 549, 69, 27);
		contentPane.add(lblSeconds);
		
		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Section 4", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_4.setBounds(817, 530, 169, 121);
		contentPane.add(panel_4);
		
		JSeparator separator_7 = new JSeparator();
		separator_7.setBounds(805, 534, 14, 117);
		contentPane.add(separator_7);
		separator_7.setOrientation(SwingConstants.VERTICAL);
		
		countDownTimeListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				lblTimeRemaining.setText(String.valueOf(countDown));
				System.out.println("Coming here to print TimeRemaining");
				if(countDown == 0) {
					stopCountDownTimer();
				}else {
					countDown--;
					countElapsed++;
				}
				
			}
		};
		
		clearOffTimerListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(clearOffCountDown == 0) {
					stopClearOffTimer();
				} else {
					clearOffCountDown --;
				}
			}
		};
		
		calibrationTimerListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(calibbrationCountDown == 0) {
					timerToCalibrate.stop();
					calibrationTimerRunning = false;
				} else {
					System.out.print("\n Down to " + calibbrationCountDown);
					calibbrationCountDown --;
				}
				
			}
		};
		
		final JToggleButton btnNewButton = new JToggleButton("Patient \r\nReady");
		
		btnNewButton.setForeground(new Color(34, 139, 34));
		btnNewButton.setIcon(null);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				timerCountDown = new Timer(1000, countDownTimeListener);
//				timer.setInitialDelay(0);
//				countDown = Long.parseLong(txtSetTimeout.getText());
//				countElapsed = 0;
//				timerCountDown.start();
//				timerDispPanel.setBackground(new Color(240, 230, 140));
				XYItemRenderer renderer = xyPlot.getRenderer();
				
				renderer.setSeriesPaint(0, Color.blue);

				if(patientReady == false) {
					btnNewButton.setText("NOT Ready");
					patientReady = true;
				}
				else {
					btnNewButton.setText("Patient \r\nReady");
					patientReady = false;
				}
				contentPane.requestFocus();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton.setBounds(1210, 587, 142, 60);
		contentPane.add(btnNewButton);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setForeground(new Color(50, 205, 50));
		progressBar.setValue(80);
		progressBar.setStringPainted(true);
		progressBar.setBounds(1249, 44, 50, 27);
		contentPane.add(progressBar);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\yogesh\\workspace\\SwingGraph\\icons\\empty34 (1).png"));
		lblNewLabel_1.setBounds(1244, 27, 108, 60);
		contentPane.add(lblNewLabel_1);
		

		
		final JCheckBox chckbxNewCheckBox = new JCheckBox("Adjust Threshold(s)");
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(chckbxNewCheckBox.isSelected()) {
					sliderMINThreshold.setEnabled(true);
					sliderUpperThreshold.setEnabled(true);
				} else {
					sliderMINThreshold.setEnabled(false);
					sliderUpperThreshold.setEnabled(false);
				}
				
			}
		});
		chckbxNewCheckBox.setFont(new Font("Tahoma", Font.BOLD, 11));
		chckbxNewCheckBox.setBounds(1206, 170, 146, 23);
		contentPane.add(chckbxNewCheckBox);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "        ", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3.setBounds(1210, 200, 142, 156);
		contentPane.add(panel_3);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "            ", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_5.setBounds(1210, 363, 142, 156);
		contentPane.add(panel_5);
}
	
	public void drawSliderMin(double start, double scale) 
	{		
		double scaleInterval = scale;
		
		if(!thresholdTable.isEmpty()) {
	
			System.out.println("--------RE DRAWING Min--------");
			double scaleUnit = start;
			thresholdTable.clear();
			contentPane.remove(sliderMINThreshold);
			
			sliderMINThreshold = new JSlider(0, 7, 1);
			sliderMINThreshold.setToolTipText("Solid Line");
			sliderMINThreshold.setSnapToTicks(true);
			sliderMINThreshold.setPaintTicks(true);
			sliderMINThreshold.setMajorTickSpacing(1);
//			slider.setMinorTickSpacing(1);
			sliderMINThreshold.setValue(3);
			sliderMINThreshold.setPaintLabels(true);
			sliderMINThreshold.setMaximum(7);
			sliderMINThreshold.setOrientation(SwingConstants.VERTICAL);
			sliderMINThreshold.setBounds(1245, 222, 59, 123);
			contentPane.add(sliderMINThreshold);
			
			thresholdTable = new java.util.Hashtable<Integer, JLabel>();
			
			thresholdTable.put(0, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdTable.put(1, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdTable.put(2, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdTable.put(3, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdTable.put(4, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdTable.put(5, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdTable.put(6, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdTable.put(7, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
		
			sliderMINThreshold.setLabelTable(thresholdTable);
			sliderMINThreshold.setEnabled(false);
			sliderMINThreshold.setVisible(true);
			
			sliderMINThreshold.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					JLabel sliderMoveVal = thresholdTable.get(sliderMINThreshold.getValue());
					minThresholdMarker = Double.parseDouble(sliderMoveVal.getText());
					
					System.out.println("Threshold Changed to " + minThresholdMarker);
					
					markerThreshold.setValue(minThresholdMarker);
				}
			});
			System.out.println("--------RE DRAWING Min DONE--------");
		}
		
		if(!thresholdUpperTable.isEmpty()) {
			
			System.out.println("--------RE DRAWING Max--------");
			double scaleUnit = start;
			thresholdUpperTable.clear();
			contentPane.remove(sliderUpperThreshold);
			
			sliderUpperThreshold = new JSlider(0, 7, 3);
			sliderUpperThreshold.setToolTipText("Dash Line");
			sliderUpperThreshold.setSnapToTicks(true);
			sliderUpperThreshold.setPaintTicks(true);
			sliderUpperThreshold.setPaintLabels(true);
			sliderUpperThreshold.setOrientation(SwingConstants.VERTICAL);
			sliderUpperThreshold.setMajorTickSpacing(1);
			sliderUpperThreshold.setEnabled(false);
			sliderUpperThreshold.setBounds(1245, 382, 59, 123);
			contentPane.add(sliderUpperThreshold);
			
			thresholdUpperTable = new java.util.Hashtable<Integer, JLabel>();
			
			thresholdUpperTable.put(0, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdUpperTable.put(1, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdUpperTable.put(2, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdUpperTable.put(3, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdUpperTable.put(4, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdUpperTable.put(5, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdUpperTable.put(6, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdUpperTable.put(7, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
		
			sliderUpperThreshold.setLabelTable(thresholdUpperTable);
			sliderUpperThreshold.setEnabled(false);
			sliderUpperThreshold.setVisible(true);
			
			sliderUpperThreshold.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					JLabel sliderMoveVal = thresholdUpperTable.get(sliderUpperThreshold.getValue());
					maxThresholdMarker = Double.parseDouble(sliderMoveVal.getText());
					
					System.out.println("Threshold Changed to " + maxThresholdMarker);
								
					markerThresholdWindowTop.setValue(maxThresholdMarker);
				}
			});              
			System.out.println("--------RE DRAWING Max DONE--------");
		}
	}
	
	public void drawSliderMax(double start, double scale) 
	{		
		double scaleInterval = scale;
		
		if(!thresholdUpperTable.isEmpty()) {
			
			System.out.println("--------RE DRAWING Max--------");
			double scaleUnit = start;
			thresholdUpperTable.clear();
			contentPane.remove(sliderUpperThreshold);
			
			sliderUpperThreshold = new JSlider(0, 7, 3);
			sliderUpperThreshold.setToolTipText("Dash Line");
			sliderUpperThreshold.setSnapToTicks(true);
			sliderUpperThreshold.setPaintTicks(true);
			sliderUpperThreshold.setPaintLabels(true);
			sliderUpperThreshold.setOrientation(SwingConstants.VERTICAL);
			sliderUpperThreshold.setMajorTickSpacing(1);
			sliderUpperThreshold.setEnabled(false);
			sliderUpperThreshold.setBounds(1245, 382, 59, 123);
			contentPane.add(sliderUpperThreshold);
			
			thresholdUpperTable = new java.util.Hashtable<Integer, JLabel>();
			
			thresholdUpperTable.put(0, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdUpperTable.put(1, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdUpperTable.put(2, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdUpperTable.put(3, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdUpperTable.put(4, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdUpperTable.put(5, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdUpperTable.put(6, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
			scaleUnit += scaleInterval;
			thresholdUpperTable.put(7, new JLabel(String.copyValueOf(Double.toString(scaleUnit).toCharArray(), 0, 3)));
		
			sliderUpperThreshold.setLabelTable(thresholdUpperTable);
			sliderUpperThreshold.setEnabled(false);
			
			sliderUpperThreshold.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					JLabel sliderMoveVal = thresholdUpperTable.get(sliderUpperThreshold.getValue());
					maxThresholdMarker = Double.parseDouble(sliderMoveVal.getText());
					
					System.out.println("Threshold Changed to " + maxThresholdMarker);
								
					markerThresholdWindowTop.setValue(maxThresholdMarker);
				}
			});              
			System.out.println("--------RE DRAWING Max DONE--------");
		}
	}
	
	
	
	public boolean searchForPorts()
	{
		ports = CommPortIdentifier.getPortIdentifiers();
		int i = 0;
		
		if(false == ports.hasMoreElements()) {
			System.out.println(" No Ports Found");
			//custom title, warning icon

			return false;
		}
		else {
			
			while (ports.hasMoreElements())
			{
				CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();

				//get only serial ports
				if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL)
				{
					portMap.put(curPort.getName(), curPort);
					// Add the available port to a comboList for user selection
					portsAvailable[i] = curPort.getName();
					i++;
					//serialPortFound = true;
					System.out.println(curPort.getName() + "  Ports Found!!");
				}
			}
			
			if(i == 0) {				
				return false;
			}
			
			
			comboBox = new JComboBox();

			comboBox.setModel(new DefaultComboBoxModel(portsAvailable));
			comboBox.setSelectedIndex(2);
			comboBox.setBounds(1220, 123, 101, 27);
			comboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(testAppFrame.getConnected() == false) {
						int connectStatus = testAppFrame.connect(comboBox.getSelectedItem().toString());
						if(connectStatus == 1) {
							JOptionPane.showMessageDialog(testAppFrame,
								    "NO COM Port Found, Check Bluetooth Connectivity",
								    "Inane warning",
								    JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			});
			contentPane.add(comboBox);
			
			return true;
		}
	}


	//connect to the selected port in the combo box
	//pre: ports are already found by using the searchForPorts method
	//post: the connected comm port is stored in commPort, otherwise,
	//an exception is generated
	public int connect(String connectTo)
	{
		
		if(connectTo.isEmpty()) {
			return 1;
		}
		else {
			selectedPort = connectTo;
		}
		System.out.println("Selected " + selectedPort);
		
		selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);

		sample_time_end = sample_time_elapsed = 0;

		CommPort commPort = null;

		try
		{
			System.out.println("Trying to connect now");
			//the method below returns an object of type CommPort
			commPort = selectedPortIdentifier.open("ABC_Device", PORT_CONNECT_TIMEOUT);
			//the CommPort object can be casted to a SerialPort object
			
			serialPort = (SerialPort)commPort;

			//for controlling GUI elements
			setConnected(true);
			serialPort.setSerialPortParams(9600,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			//logging
			logText = selectedPort + " opened successfully! CONNECTED !!";
			System.out.println(logText);

			sample_time_start = System.nanoTime();
			curr_state = CurrentState.INIT;
			calibbrationCountDown = CALIBRATION_DURATION;
			
			System.out.println("initIOStream returned" + testAppFrame.initIOStream());
			System.out.println("Initing Listeners");
						
			testAppFrame.initListener();
			System.out.println("Work Done, Deinit");
			return 0;
		}
		catch (PortInUseException e)
		{
			logText = selectedPort + " is in use. (" + e.toString() + ")";
			
			JOptionPane.showMessageDialog(testAppFrame,
					selectedPort + " is in use. (" + e.toString() + ")",
				    "Inane warning",
				    JOptionPane.WARNING_MESSAGE);
			return -1;
		}
		catch (Exception e)
		{
			logText = "Failed to open " + selectedPort + "(" + e.toString() + ")";
			
			JOptionPane.showMessageDialog(testAppFrame,
					"Failed to open " + selectedPort + "(" + e.toString() + ")",
				    "Inane warning",
				    JOptionPane.WARNING_MESSAGE);
			return -1;
		}
	}

	//open the input and output streams
	//pre: an open port
	//post: initialized intput and output streams for use to communicate data
	public boolean initIOStream()
	{
		//return value for whather opening the streams is successful or not
		boolean successful = false;

		try {
			//
			input = serialPort.getInputStream();
			output = serialPort.getOutputStream();
			//writeData(0, 0);

			successful = true;
			System.out.println("initIOStream Success");
			return successful;
		}
		catch (IOException e) {
			logText = "I/O Streams failed to open. (" + e.toString() + ")";
			return successful;
		}
	}


	//starts the event listener that knows whenever data is available to be read
	//pre: an open serial port
	//post: an event listener for the serial port that knows when data is recieved
	public void initListener()
	{
		try
		{
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
			logText = "initListener DONE";
		}
		catch (TooManyListenersException e)
		{
			logText = "Too many listeners. (" + e.toString() + ")";
		}
		System.out.println(logText);
	}

	//disconnect the serial port
	//pre: an open serial port
	//post: clsoed serial port
	public void disconnect()
	{
		//close the serial port
		try
		{
			writeData(0, 0);

			serialPort.removeEventListener();
			serialPort.close();
			input.close();
			output.close();
			setConnected(false);

			if(countDownTimerRunning) {
				stopCountDownTimer();
			}
			
			if(clearOffTimerRunning) {
				stopClearOffTimer();
			}
			
			if(clearOffTimerRunning) {
				timerToCalibrate.stop();
			}
			
			sampleLogFileName.close();
			outSampleLogFileName.close();
			logText = "Disconnected.";
		}
		catch (Exception e)
		{
			logText = "Failed to close " + serialPort.getName() + "(" + e.toString() + ")";
		}
		System.out.println(logText);
	}

	final public boolean getConnected()
	{
		return bConnected;
	}

	public void setConnected(boolean bConnected)
	{
		this.bConnected = bConnected;
	}

	//what happens when data is received
	//pre: serial event is triggered
	//post: processing on the data it reads
	public void serialEvent(SerialPortEvent evt) {
		if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE)
		{
			try
			{
				//System.out.print("Data Available ");
				BufferedReader br = new BufferedReader(new InputStreamReader(input));
				String recvdMsg = new String();
				recvdMsg = br.readLine();
				//System.out.print("\nRead ->>>" + recvdMsg);

				parseRecvdMsg(recvdMsg);
				
				if(calibbrationCountDown > 0 && calibrationTimerRunning == false) {
					//System.out.print("\nRead ->>>" + recvdMsg + " And Calibration STARTED");
					timerToCalibrate = new Timer(1000, calibrationTimerListener);
					timerToCalibrate.setInitialDelay(0);				
					calibbrationCountDown = CALIBRATION_DURATION;
					timerToCalibrate.start();
					calibrationTimerRunning = true;
					
				}
				
				if(clearOffTimerRunning == false) {
					//System.out.print("\nRead ->>>" + recvdMsg + " And Timer STARTED");
					timerToClearOff = new Timer(1000, clearOffTimerListener);
					timerToClearOff.setInitialDelay(0);				
					clearOffCountDown = SCREENFUL_DURATION;
					timerToClearOff.start();
					clearOffTimerRunning = true;
				}
				
			}
			catch (Exception e)
			{
				logText = "Failed to read data. (" + e.toString() + ")";
			}
		}
	}

	
	public void parseRecvdMsg(String s1) {

		/*
		 * Start Web Server to send serial port data to
		 */
		StringBuffer s1_number = new StringBuffer();
		char c;
		boolean isNegative = false;
		/*
		 * Handle pressure sample
		 */
		if(s1.contains("P")) {
			if(s1.contains("P-")) {
				isNegative = true;
				prev_state = curr_state;
				curr_state = CurrentState.INHALE;
			}
			else {
				isNegative = false;
				prev_state = curr_state;
				curr_state = CurrentState.EXHALE;
			}
			for(int i = 0; i<s1.length(); i++) {
				c = s1.charAt(i);
				if(Character.isDigit(c) == true) {
					s1_number.append(c);
				}
			}
			/*
			 * Convert received pressure value in volume (liters)
			 */
			double p = Double.parseDouble(s1_number.toString());

			if(isNegative == true) {
				p = (-1) * p;
			}
			System.out.print("\n p --> " + p);
			if(calibbrationCountDown > 0) {
				calibWindow[calibWindowIndex][0] = (int)p;			
				p -= p;
				//System.out.print(/*"\t" + p +*/ "\t" + calibWindow[(int)Math.abs(p)][1]  + "\t"+isNegative + "\t" + p);
				
			} else {
				if(!calibrationDone) {
					int sign = 1;
					double min = (-1);// * CALIBRATION_WINDOW_SIZE;
					for(int i = 0; i < CALIBRATION_WINDOW_SIZE; i++ ) {
						if(calibWindow[i][1] == 1) {
							
						}
						
						if(calibWindow[i][0] < min) {
							min = calibWindow[i][0];
							calibratedVal = calibWindow[i][0];
														
						}
					}
					calibrationDone = true;			
					
					p -= calibratedVal;
					System.out.println("---- Calibration DONE ---- " + calibratedVal);
				}
				else {
					p -= calibratedVal;
					System.out.print("Post Calibration" + p);
				}
			}

			

			
			double dp = (p/60);
			double volume = dp/(60*55.57);
			
			System.out.print("\t" + volume);
			cum_volume += volume;

			sample_time_end = System.nanoTime();
			sample_time_elapsed = (sample_time_end - sample_time_start)/1000000;
			/*
			 * Reset cum_volume in case of SAMPLE_CUMULATION_TIMEOUT sec of cumulation OR
			 * Switching from inhale to exhale OR
			 * Switching from exhale to inhale
			 */
			/*
			 * Case 1: SAMPLE_CUMULATION_TIMEOUT sec timeout
			 */	
			
			if( (sample_time_elapsed >= SAMPLE_CUMULATION_TIMEOUT) )
			{
				sample_time_elapsed_counter += SAMPLE_CUMULATION_TIMEOUT;
				//System.out.print(",TO," + sample_time_elapsed_counter + "," + cum_volume + "\n");
								
				testAppFrame.addPoint(xVal += 0.2, cum_volume);
				
				/* 
				 * Log the current Date, timestamp and beathing data
				 */
				String timeStamp = new SimpleDateFormat("MM/dd/yyyy [HH:mm:ss]\t").format(Calendar.getInstance().getTime());
				outSampleLogFileName.append(timeStamp).
									 append(String.valueOf(cum_volume)).
									 append("\n");
				
				// Commenting following if..else block to allow -ve value plot
//				if(cum_volume > 0)
//					testAppFrame.addPoint(xVal += 0.2, cum_volume);		
//				else
//					testAppFrame.addPoint(xVal += 0.2, 0);
				
				sample_time_elapsed = 0;
				sample_time_start = System.nanoTime();
			}

			/* Case 2: Switching from inhale to exhale */
//			if(volume == 0.0) {
//				cum_volume = 0;
//			}
			if(cum_volume != 0 &&
			   ((prev_state == CurrentState.INHALE && curr_state == CurrentState.EXHALE) /*||
				(prev_state == CurrentState.EXHALE && curr_state == CurrentState.INHALE)*/))
			{
				cum_volume -= volume;
				cum_volume = 0;
				sample_time_elapsed_counter += SAMPLE_CUMULATION_TIMEOUT;
				
				testAppFrame.addPoint(xVal += 0.2, cum_volume);
				
				/* 
				 * Log the current Date, timestamp and beathing data
				 */
				String timeStamp = new SimpleDateFormat("MM/dd/yyyy [HH:mm:ss]\t").format(Calendar.getInstance().getTime());
				outSampleLogFileName.append(timeStamp).
									 append(String.valueOf(cum_volume)).
									 append("\n");

				// Commenting following if..else block to allow -ve value plot
//				if(cum_volume > 0)
//					testAppFrame.addPoint(xVal += 0.2, cum_volume);
//				else
//					testAppFrame.addPoint(xVal += 0.2, 0);
				
				//cum_volume = 0;
				sample_time_elapsed = 0;
				sample_time_start = System.nanoTime();
				//TODO: This value of cum_volume should be sent to plot and then reset with new volume
				//cum_volume = volume;
			}
		}
		/*
		 * Handle battery status indicator sample
		 */
		else if (s1.contains("b")) {
			//TODO:  Update battery status at front end
		}
	}

	//method that can be called to send data
	//pre: open serial port
	//post: data sent to the other device
	public void writeData(int leftThrottle, int rightThrottle)
	{
		try
		{
			output.write(leftThrottle);
			output.flush();
			//this is a delimiter for the data
			output.write(DASH_ASCII);
			output.flush();

			output.write(rightThrottle);
			output.flush();
			//will be read as a byte so it is a space key
			output.write(SPACE_ASCII);
			output.flush();
		}
		catch (Exception e)
		{
			logText = "Failed to write data. (" + e.toString() + ")";
		}
	}
	
	public class SetSliderScaleNewMenuWindow extends JFrame{

		private JPanel contentPane;
		private JTextField textThresholdStart;
		double  thresholdStart;
		
		private JTextField textThresholdEnd;
		double  thresholdEnd;
		
		private JTextField textThresholdScale;
		double  thresholdScale;
		
		private JTextField textThresholdVal;
		double  thresholdVal;

		boolean sliderUpdateRequired;
		
		public void setDefaultParams(double start, double end, double scale) {
			thresholdStart = start;
			thresholdEnd = end;
			thresholdScale = scale;
			sliderUpdateRequired = false;
		}
		
		public void setThresholdStart(double start) {
			thresholdStart = start;
		}
		
		public double getThresholdStart() {
			return thresholdStart;
		}
		
		public void setThresholdEnd(double end) {
			thresholdEnd = end;
		}
		
		public double getThresholdEnd() {
			return thresholdEnd;
		}
		
		public void setThresholdScale(double scale) {
			thresholdScale = scale;
		}
		
		public double getThresholdScale() {
			return thresholdScale;
		}
		
		public void setSliderUpdateRequired() {
			sliderUpdateRequired = true;
		}
		
		public boolean getSliderUpdateRequired() {
			return sliderUpdateRequired;
		}
		
		public void setThresholdValue(double val) {
			thresholdVal = val;
		}
		
		public double getThresholdValue() {
			return thresholdVal;
		}
		/**
		 * Launch the application.
		 */
//		public static void main(String[] args) {
//			EventQueue.invokeLater(new Runnable() {
//				public void run() {
//					try {
//						NewMenuWindow frame = new NewMenuWindow();
//						frame.setVisible(true);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			});
//		}

		/**
		 * Create the X-Y Axis range config window
		 */


		/**
		 * Create the Slider Scale config window
		 */
		public SetSliderScaleNewMenuWindow() {
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			setBounds(100, 100, 398, 279);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(new BorderLayout(0, 0));
			setContentPane(contentPane);
			
			JPanel panel = new JPanel();
			contentPane.add(panel, BorderLayout.CENTER);
			panel.setLayout(null);
			
			JLabel lblBreathingThresholdLimits = new JLabel("Breathing Threshold Limits");
			lblBreathingThresholdLimits.setHorizontalAlignment(SwingConstants.CENTER);
			lblBreathingThresholdLimits.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblBreathingThresholdLimits.setBounds(70, 0, 221, 23);
			panel.add(lblBreathingThresholdLimits);
			
			JLabel label_1 = new JLabel("Start");
			label_1.setHorizontalAlignment(SwingConstants.CENTER);
			label_1.setBounds(10, 34, 73, 23);
			panel.add(label_1);
			
			textThresholdStart = new JTextField();
			textThresholdStart.setColumns(10);
			textThresholdStart.setBounds(86, 35, 67, 20);
			panel.add(textThresholdStart);
			
			JLabel label_2 = new JLabel("End");
			label_2.setHorizontalAlignment(SwingConstants.CENTER);
			label_2.setBounds(10, 65, 73, 23);
			panel.add(label_2);
			
			textThresholdEnd = new JTextField();
			textThresholdEnd.setColumns(10);
			textThresholdEnd.setBounds(86, 66, 67, 20);
			panel.add(textThresholdEnd);
			
			JButton button = new JButton("Ok");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					setThresholdStart(Double.parseDouble(textThresholdStart.getText()));
					setThresholdEnd(Double.parseDouble(textThresholdEnd.getText()));
					setThresholdScale(Double.parseDouble(textThresholdScale.getText()));
					
					setThresholdValue(Double.parseDouble(textThresholdVal.getText()));
					minThresholdMarker = Double.parseDouble(textThresholdVal.getText());
					markerThreshold.setValue(minThresholdMarker);
					
					drawSliderMin(sliderScaleWin.getThresholdStart(), sliderScaleWin.getThresholdScale());
					dispose();
				}
			});
			button.setFont(new Font("Tahoma", Font.BOLD, 14));
			button.setBounds(86, 173, 95, 33);
			panel.add(button);
			
			JButton button_1 = new JButton("Cancel");
			button_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
			button_1.setFont(new Font("Tahoma", Font.BOLD, 13));
			button_1.setBounds(221, 174, 95, 33);
			panel.add(button_1);
			
			textThresholdScale = new JTextField();
			textThresholdScale.setColumns(10);
			textThresholdScale.setBounds(86, 100, 67, 20);
			panel.add(textThresholdScale);
			
			JLabel lblScale = new JLabel("Scale");
			lblScale.setHorizontalAlignment(SwingConstants.CENTER);
			lblScale.setBounds(10, 99, 73, 23);
			panel.add(lblScale);
			
			JLabel lblThreshold = new JLabel("Threshold");
			lblThreshold.setHorizontalAlignment(SwingConstants.CENTER);
			lblThreshold.setBounds(10, 131, 73, 23);
			panel.add(lblThreshold);
			
			textThresholdVal = new JTextField();
			textThresholdVal.setColumns(10);
			textThresholdVal.setBounds(86, 132, 67, 20);
			panel.add(textThresholdVal);
		}
	}

	public class SetAxisScaleMenuWindow extends JFrame {

		private JPanel contentPane;
		
		private JTextField txtYStart;
		private  double yStart;
		
		private JTextField txtYEnd;
		private  double yEnd;
		
		private JTextField txtXStart;
		private  double xStart;
		
		private JTextField txtXEnd;
		private  double xEnd;

		public double getYStart() {
			return yStart;
		}
		public void setYStart(double val) {
			yStart = val;
		}
		
		public double getYEnd() {
			return yEnd;
		}
		public void setYEnd(double val) {
			yEnd = val;
		}
		
		public double getXStart() {
			return xStart;
		}
		public void setXStart(double val) {
			xStart = val;
		}
		
		public double getXEnd() {
			return xEnd;
		}
		public void setXEnd(double val) {
			xEnd = val;
		}
		public void setDefaultYParams(double low, double high)
		{
			yStart = low;
			yEnd = high;
		}

		/**
		 * Create the frame.
		 */
		public SetAxisScaleMenuWindow() {
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			setBounds(100, 100, 426, 257);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(new BorderLayout(0, 0));
			setContentPane(contentPane);
			
			JPanel panel = new JPanel();
			contentPane.add(panel, BorderLayout.CENTER);
			panel.setLayout(null);
			
			JLabel lblName = new JLabel("Start");
			lblName.setHorizontalAlignment(SwingConstants.CENTER);
			lblName.setBounds(23, 47, 73, 23);
			panel.add(lblName);
			
			JLabel lblPatient = new JLabel("End");
			lblPatient.setHorizontalAlignment(SwingConstants.CENTER);
			lblPatient.setBounds(23, 78, 73, 23);
			panel.add(lblPatient);
			
			txtYStart = new JTextField();
			txtYStart.setBounds(99, 48, 67, 20);
			panel.add(txtYStart);
			txtYStart.setColumns(10);
			
			txtYEnd = new JTextField();
			txtYEnd.setColumns(10);
			txtYEnd.setBounds(99, 79, 67, 20);
			panel.add(txtYEnd);
			
			JButton btnAxisConfigOkButton = new JButton("Ok");
			btnAxisConfigOkButton.setFont(new Font("Tahoma", Font.BOLD, 14));
			btnAxisConfigOkButton.setIcon(null);
			btnAxisConfigOkButton.setBounds(100, 147, 95, 33);
			panel.add(btnAxisConfigOkButton);
			btnAxisConfigOkButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(!txtYStart.getText().isEmpty())
						setYStart(Double.parseDouble(txtYStart.getText()));
					
					if(!txtYEnd.getText().isEmpty())
						setYEnd(Double.parseDouble(txtYEnd.getText()));
					
					if(!txtXStart.getText().isEmpty())
						setXStart(Double.parseDouble(txtXStart.getText()));
					
					if(!txtXEnd.getText().isEmpty())
						setXEnd(Double.parseDouble(txtXEnd.getText()));		
					
					NumberAxis xAxis = (NumberAxis) xyPlot.getDomainAxis();
			        NumberAxis yAxis = (NumberAxis) xyPlot.getRangeAxis();
			        
//					xAxis.setRange(getXStart(), getXEnd());
			        yAxis.setRange(getYStart(), getYEnd());
			        System.out.println("Range Set");
					
			    	dispose();
				}
			});
			
			JButton btnCancel = new JButton("Cancel");
			btnCancel.setIcon(new ImageIcon("C:\\Users\\yogesh\\workspace\\SwingGraph\\icons\\1409026080_101.png"));
			btnCancel.setFont(new Font("Tahoma", Font.BOLD, 13));
			btnCancel.setBounds(235, 148, 95, 33);
			panel.add(btnCancel);
			
			JLabel label = new JLabel("Start");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setBounds(214, 47, 73, 23);
			panel.add(label);
			
			txtXStart = new JTextField();
			txtXStart.setColumns(10);
			txtXStart.setBounds(290, 48, 67, 20);
			panel.add(txtXStart);
			
			JLabel label_1 = new JLabel("End");
			label_1.setHorizontalAlignment(SwingConstants.CENTER);
			label_1.setBounds(214, 78, 73, 23);
			panel.add(label_1);
			
			txtXEnd = new JTextField();
			txtXEnd.setColumns(10);
			txtXEnd.setBounds(290, 79, 67, 20);
			panel.add(txtXEnd);
			
			JLabel lblYAxis = new JLabel("Y Axis");
			lblYAxis.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblYAxis.setHorizontalAlignment(SwingConstants.CENTER);
			lblYAxis.setBounds(83, 13, 73, 23);
			panel.add(lblYAxis);
			
			JLabel lblXAxis = new JLabel("X Axis");
			lblXAxis.setHorizontalAlignment(SwingConstants.CENTER);
			lblXAxis.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblXAxis.setBounds(261, 13, 73, 23);
			panel.add(lblXAxis);
			
			JSeparator separator = new JSeparator();
			separator.setOrientation(SwingConstants.VERTICAL);
			separator.setBounds(199, 13, 21, 102);
			panel.add(separator);
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					dispose();
				}
			});
		}
	}
}



