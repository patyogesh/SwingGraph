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

public class NewMenuWindow extends JFrame {

	private JPanel contentPane;
	private JTextField txtPatName;
	private JTextField txtPatID;
	private JTextField txtTestID;

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
	public NewMenuWindow() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblName = new JLabel("Patient Name");
		lblName.setBounds(23, 47, 73, 23);
		panel.add(lblName);
		
		JLabel lblPatient = new JLabel("Patient #");
		lblPatient.setHorizontalAlignment(SwingConstants.CENTER);
		lblPatient.setBounds(23, 78, 73, 23);
		panel.add(lblPatient);
		
		JLabel lblTestNumber = new JLabel("Test Number");
		lblTestNumber.setBounds(23, 109, 73, 23);
		panel.add(lblTestNumber);
		
		txtPatName = new JTextField();
		txtPatName.setBounds(99, 48, 235, 20);
		panel.add(txtPatName);
		txtPatName.setColumns(10);
		
		txtPatID = new JTextField();
		txtPatID.setColumns(10);
		txtPatID.setBounds(99, 79, 235, 20);
		panel.add(txtPatID);
		
		txtTestID = new JTextField();
		txtTestID.setColumns(10);
		txtTestID.setBounds(99, 110, 235, 20);
		panel.add(txtTestID);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton.setIcon(new ImageIcon("C:\\Users\\yogesh\\workspace\\SwingGraph\\icons\\1409026030_add.png"));
		btnNewButton.setBounds(99, 160, 95, 33);
		panel.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/* 
		    	 * Add Patient's info to respective files for every test performed
		    	 */
				if(txtPatName.getText().isEmpty() &&
				   txtPatID.getText().isEmpty() &&
				   txtTestID.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please fill in Patient and Test Info !!");					
				}
				else if(txtPatName.getText().isEmpty() ||
						   txtPatID.getText().isEmpty() ||
						   txtTestID.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please do not leave any field blank !!");
				}
				ObjectSerializationDemo impl = new ObjectSerializationDemo();
		    	impl.writeData(txtPatName.getText(), 
		    				   txtPatID.getText(),
		    				   (int) Long.parseLong(txtTestID.getText()));
		    	dispose();
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setIcon(new ImageIcon("C:\\Users\\yogesh\\workspace\\SwingGraph\\icons\\1409026080_101.png"));
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnCancel.setBounds(239, 160, 95, 33);
		panel.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
	}
}
