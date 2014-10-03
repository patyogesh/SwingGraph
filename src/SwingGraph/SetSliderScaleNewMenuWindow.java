package SwingGraph;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.ImageIcon;

//import test.ObjectSerializationDemo;


import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SetSliderScaleNewMenuWindow extends JFrame{

	private JPanel contentPane;
	private JTextField textThresholdStart;
	double  thresholdStart;
	
	private JTextField textThresholdEnd;
	double  thresholdEnd;
	
	private JTextField textThresholdScale;
	double  thresholdScale;

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
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					NewMenuWindow frame = new NewMenuWindow();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public SetSliderScaleNewMenuWindow() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 394, 248);
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
				
				setSliderUpdateRequired();
				dispose();
			}
		});
		button.setFont(new Font("Tahoma", Font.BOLD, 14));
		button.setBounds(87, 134, 95, 33);
		panel.add(button);
		
		JButton button_1 = new JButton("Cancel");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		button_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		button_1.setBounds(222, 135, 95, 33);
		panel.add(button_1);
		
		textThresholdScale = new JTextField();
		textThresholdScale.setColumns(10);
		textThresholdScale.setBounds(86, 100, 67, 20);
		panel.add(textThresholdScale);
		
		JLabel lblScale = new JLabel("Scale");
		lblScale.setHorizontalAlignment(SwingConstants.CENTER);
		lblScale.setBounds(10, 99, 73, 23);
		panel.add(lblScale);
	}
}
