package SwingGraph;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GradientPaint;

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

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

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

//import test.gui.CurrentState;














import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.PortUnreachableException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

import javax.swing.JTextField;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;

import javax.swing.ImageIcon;

public class TestApp extends JFrame implements SerialPortEventListener {

	private JPanel contentPane;
	static JFrame frameNewMenu;
	
	static TestApp testAppFrame;
	
	String selectedPort;
	static boolean serialPortFound = false;
	
	private JLabel TimeElapsed;
	public  Timer  timer;
	ActionListener countDownTimeListener;
	long countDown = 0;
	long countElapsed = 0;
	boolean bTimeOver = false;
	JLabel lblTimeRemaining;
	
	static Double DEFAULT_THRESHOLD = 1.5;
	static Double thresholdMarker = DEFAULT_THRESHOLD;
	java.util.Hashtable<Integer, JLabel> thresholdTable;
	
	double yVal = 0;
	static XYPlot xyPlot = null;
    static XYSeries xySeries = null;
    private ChartPanel chartPanel_1;
    
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
	
	/*
	 * Following fields are all related to Serial communication
	 */
	//for containing the ports that will be found
	private Enumeration ports = null;
	//map the port names to CommPortIdentifiers
	private HashMap portMap = new HashMap();
	String[] portsAvailable = new String[5];

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
	final static int PORT_CONNECT_TIMEOUT = 200;

	//some ascii values for for certain things
	final static int SPACE_ASCII = 32;
	final static int DASH_ASCII = 45;
	final static int NEW_LINE_ASCII = 10;
	
	static int currInd = -1;

	//a string for recording what goes on in the program
	//this string is written to the GUI
	String logText = "";
	private JTextField txtSetTimeout;
	
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
	 */
	public static void main(String[] args) {
		set_native_look_and_feel();
		
		frameNewMenu = new JFrame ("MyPanel");
        frameNewMenu.setPreferredSize(new Dimension(350, 450));
        frameNewMenu.setLocationRelativeTo(null);;
        frameNewMenu.setResizable(false);
        frameNewMenu.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        //frameNewMenu.getContentPane().add (new MyPanel2());
        frameNewMenu.pack();
        frameNewMenu.setVisible (false);
        
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {				
					testAppFrame = new TestApp();
					testAppFrame.setVisible(true);
					
					System.out.println("Object created!! Searching for ports now");
					testAppFrame.searchForPorts();
					
					testAppFrame.connect();
					
					System.out.println("initIOStream returned" + testAppFrame.initIOStream());
					System.out.println("Initing Listeners");
					testAppFrame.initListener();
					System.out.println("Work Done, Deinit");

					System.out.println("Let's Go home");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void addPoint(Number x, Number y) {
    	XYItemRenderer renderer = xyPlot.getRenderer();
    	System.out.println("addPoint y = " + y.doubleValue() + "thresholdMarker" + thresholdMarker);
		if(y.doubleValue() > thresholdMarker) {
			GradientPaint gPaint = new GradientPaint(2.0f, 6.0f, Color.lightGray, 2.0f, 6.0f, Color.green);
        	xyPlot.setBackgroundPaint(gPaint);
			renderer.setSeriesPaint(0, Color.green);
		}
		else {
			GradientPaint gPaint = new GradientPaint(4.0f, 6.0f, Color.lightGray, 3.0f, 6.0f, Color.lightGray);
          	xyPlot.setBackgroundPaint(gPaint);
			renderer.setSeriesPaint(0, Color.blue);
		}
		    	
		xyPlot.setRenderer(renderer);
        xySeries.add(x, y);
    }

	private ChartPanel drawChart() {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        xySeries = new XYSeries("");

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

        chartPanel_1 = new ChartPanel(chart);
        chartPanel_1.setMaximumDrawHeight(700);
        chartPanel_1.setRefreshBuffer(true);
        chartPanel_1.setLocation(0, 0);
        chartPanel_1.setSize(1200, 519);
        chartPanel_1.setLayout(new BorderLayout(0, 0));
        	       
        NumberAxis xAxis = (NumberAxis) xyPlot.getDomainAxis();
        NumberAxis yAxis = (NumberAxis) xyPlot.getRangeAxis();
        
        /* Set range for Y axis values */
        yAxis.setRange(-1.0, 6.0);
                        	        
        xyPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        xyPlot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
//        xyPlot.addRangeMarker(marker);
        
        return chartPanel_1;
	}
	public static void displayNewWindow() {
        JFrame frame = new JFrame ("MyPanel");
        frame.setPreferredSize(new Dimension(350, 450));
        frame.setLocationRelativeTo(null);;
        frame.setResizable(false);
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        //frame.getContentPane().add (new MyPanel2());
        frame.pack();
        frame.setVisible (true);
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
//				displayNewWindow();
				frameNewMenu.setVisible (true);
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
					System.out.println("Opened " + fileOpened.getName());
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
		
		JMenu mnAbout = new JMenu("About");
		mnAbout.setMnemonic('A');
		mnAbout.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		menuBar.add(mnAbout);
		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic('H');
		mnHelp.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		menuBar.add(mnHelp);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ChartPanel chartPanel = drawChart();
		contentPane.add(chartPanel);
		contentPane.add(chartPanel_1);
		
		final JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"COM 1", "COM 2", "COM 3", "COM 4", "COM 5", "COM 6"}));
		comboBox.setSelectedIndex(2);
		comboBox.setBounds(1220, 123, 101, 27);
		contentPane.add(comboBox);
		
		final JSlider slider = new JSlider(0, 7, 1);
		
		slider.setSnapToTicks(true);
		slider.setPaintTicks(true);
		slider.setMajorTickSpacing(1);
//		slider.setMinorTickSpacing(1);
		slider.setValue(3);
		slider.setPaintLabels(true);
		slider.setMaximum(7);
		slider.setOrientation(SwingConstants.VERTICAL);
		slider.setBounds(1244, 189, 59, 123);
		
		thresholdTable = new java.util.Hashtable<Integer, JLabel>();
		thresholdTable.put(new Integer(7), new JLabel("3.5"));
		thresholdTable.put(new Integer(6), new JLabel("3.0"));
		thresholdTable.put(new Integer(5), new JLabel("2.5"));
		thresholdTable.put(new Integer(4), new JLabel("2.0"));
		thresholdTable.put(new Integer(3), new JLabel("1.5"));
		thresholdTable.put(new Integer(2), new JLabel("1.0"));
		thresholdTable.put(new Integer(1), new JLabel("0.5"));
		thresholdTable.put(new Integer(0), new JLabel("0"));
		slider.setLabelTable(thresholdTable);

		final ValueMarker marker = new ValueMarker(DEFAULT_THRESHOLD);  // position is the value on the axis
		/* Draw Threshold Line */ 
        marker.setPaint(Color.black);
        marker.setStroke(new BasicStroke(2));
        marker.setValue(DEFAULT_THRESHOLD);
        xyPlot.addRangeMarker(marker);
        
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				JLabel sliderMoveVal = thresholdTable.get(slider.getValue());
				thresholdMarker = Double.parseDouble(sliderMoveVal.getText());
				System.out.println("Threshold Changed to " + thresholdMarker);
				marker.setValue(thresholdMarker);
			}
		});
		
		
        
		contentPane.add(slider);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(332, 534, 13, 117);
		contentPane.add(separator);
		
		JLabel lblSelectSerialPort = new JLabel("Select Port");
		lblSelectSerialPort.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSelectSerialPort.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectSerialPort.setBounds(1210, 98, 94, 27);
		contentPane.add(lblSelectSerialPort);
		
		JLabel lblAdjustThreshold = new JLabel("Set Threshold");
		lblAdjustThreshold.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblAdjustThreshold.setHorizontalAlignment(SwingConstants.CENTER);
		lblAdjustThreshold.setBounds(1221, 167, 94, 27);
		contentPane.add(lblAdjustThreshold);
		
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
		
		JLabel lblType = new JLabel("Type");
		lblType.setBounds(10, 71, 80, 14);
		panel.add(lblType);
		
		JLabel lblOngoingSession = new JLabel("Ongoing Session");
		lblOngoingSession.setBounds(10, 96, 112, 14);
		panel.add(lblOngoingSession);
		
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
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Breath Hold Time Remaining", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3.setBounds(1003, 530, 197, 121);
		contentPane.add(panel_3);
		
		lblTimeRemaining = new JLabel("00");
		lblTimeRemaining.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimeRemaining.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblTimeRemaining.setBounds(48, 21, 95, 78);
		panel_3.add(lblTimeRemaining);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setBounds(1144, 530, 13, 117);
		contentPane.add(separator_3);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(612, 534, 13, 117);
		contentPane.add(separator_1);
		separator_1.setOrientation(SwingConstants.VERTICAL);
		
		JLabel lblBreathHoldTime = new JLabel("Breath Hold Time");
		lblBreathHoldTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblBreathHoldTime.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblBreathHoldTime.setBounds(1210, 323, 123, 27);
		contentPane.add(lblBreathHoldTime);
		
		txtSetTimeout = new JTextField();
		txtSetTimeout.setBounds(1220, 346, 59, 27);
		contentPane.add(txtSetTimeout);
		txtSetTimeout.setColumns(10);
		
		JLabel lblSeconds = new JLabel("Seconds");
		lblSeconds.setHorizontalAlignment(SwingConstants.CENTER);
		lblSeconds.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSeconds.setBounds(1272, 346, 69, 27);
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
				
				if(countDown == 0) {
					timer.stop();
					bTimeOver = true;
					
				}else {
					countDown--;
					countElapsed++;
				}
				
			}
		};
		
		JButton btnNewButton = new JButton("START");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				timer = new Timer(1000, countDownTimeListener);
				timer.setInitialDelay(0);
				countDown = Long.parseLong(txtSetTimeout.getText());
				countElapsed = 0;
				timer.start();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnNewButton.setBounds(1210, 459, 111, 60);
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
	}
	
	public void searchForPorts()
	{
		ports = CommPortIdentifier.getPortIdentifiers();
		int i = 0;
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
				System.out.println(curPort.getName() + "  Ports Found!! Now Connect");
			}
		}
	}


	//connect to the selected port in the combo box
	//pre: ports are already found by using the searchForPorts method
	//post: the connected comm port is stored in commPort, otherwise,
	//an exception is generated
	public void connect()
	{
		selectedPort = "COM3";
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
		}
		catch (PortInUseException e)
		{
			logText = selectedPort + " is in use. (" + e.toString() + ")";
		}
		catch (Exception e)
		{
			logText = "Failed to open " + selectedPort + "(" + e.toString() + ")";
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

		if(s1.contains("P")) {
			if(s1.contains("P-")) {
				//System.out.print("Negative Number received");
				isNegative = true;
				prev_state = curr_state;
				curr_state = CurrentState.INHALE;
			}
			else {
				isNegative = false;
				prev_state = curr_state;
				curr_state = CurrentState.EXHALE;
				//System.out.print("Positive Number received");
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
				p = 0.00 - p;
			}
			//System.out.print("\n" + p);

			double dp = (p/60);
			double volume = dp/(60*55.57);
			
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
								
				if(cum_volume > 0)
					testAppFrame.addPoint(yVal += 0.2, cum_volume);		
				else
					testAppFrame.addPoint(yVal += 0.2, 0);
				if(currInd == -1) {
					currInd = 0;
				}
				sample_time_elapsed = 0;
				sample_time_start = System.nanoTime();
			}

			/* Case 2: Switching from inhale to exhale */

			if(cum_volume != 0 &&
			   ((prev_state == CurrentState.INHALE && curr_state == CurrentState.EXHALE) ||
				(prev_state == CurrentState.EXHALE && curr_state == CurrentState.INHALE)))
			{
				cum_volume -= volume;
				//cum_volume = 0;
				sample_time_elapsed_counter += SAMPLE_CUMULATION_TIMEOUT;

				//valuePooListDoub.add(cum_volume);
				if(cum_volume > 0)
					testAppFrame.addPoint(yVal += 0.2, cum_volume);
				else
					testAppFrame.addPoint(yVal += 0.2, 0);
				if(currInd == -1) {
					currInd = 0;
				}
				//cum_volume = 0;
				sample_time_elapsed = 0;
				sample_time_start = System.nanoTime();
				//TODO: This value of cum_volume should be sent to plot and then reset with new volume
				//cum_volume = volume;
			}
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
}
