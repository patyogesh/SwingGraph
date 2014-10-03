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
import javax.swing.JSeparator;

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
	
	public double getYEnd() {
		return yEnd;
	}
	
	public double getXStart() {
		return xStart;
	}
	
	public double getXEnd() {
		return xEnd;
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
		
		JButton btnNewButton = new JButton("Ok");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton.setIcon(null);
		btnNewButton.setBounds(100, 147, 95, 33);
		panel.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				yStart = Double.parseDouble(txtYStart.getText());
				yEnd = Double.parseDouble(txtYEnd.getText());
				
				xStart = Double.parseDouble(txtXStart.getText());
				xEnd = Double.parseDouble(txtXEnd.getText());
				
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
