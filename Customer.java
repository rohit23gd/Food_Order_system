import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
public class Customer {

	private JFrame frmAnnasCafe;
	static MenuAndBill fms = new  MenuAndBill();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Customer window = new Customer();
					window.frmAnnasCafe.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Customer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmAnnasCafe = new JFrame();
		frmAnnasCafe.setTitle("Annas Cafe");
		frmAnnasCafe.getContentPane().setBackground(new Color(255, 215, 0));
		frmAnnasCafe.getContentPane().setForeground(new Color(255, 255, 255));
		frmAnnasCafe.setBounds(5,5, 835, 400);
		frmAnnasCafe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAnnasCafe.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Welcome To Food\r\n Ordering System");
		lblNewLabel.setBounds(130, -11,597, 125);
		lblNewLabel.setBackground(new Color(165, 42, 42));
		lblNewLabel.setForeground(new Color(255, 255, 0));
		lblNewLabel.setFont(new Font("Monotype Corsiva", Font.BOLD,30));
		frmAnnasCafe.getContentPane().add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Starters");
		btnNewButton.setBounds(42, 166, 175, 57);
		btnNewButton.setForeground(new Color(0, 0, 0));
		btnNewButton.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				
				fms.NewScreen("s");
			}
		});
		btnNewButton.setBackground(new Color(255, 255, 0));
		btnNewButton.setFont(new Font("Goudy Old Style", Font.BOLD,20));
		frmAnnasCafe.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Snacks");
		btnNewButton_1.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				
				fms.NewScreen("n");
			}
		});
		btnNewButton_1.setBounds(227, 166, 175, 57);
		btnNewButton_1.setForeground(new Color(0, 0, 0));
		btnNewButton_1.setBackground(new Color(255, 255, 0));
		btnNewButton_1.setFont(new Font("Goudy Old Style", Font.BOLD, 20));
		frmAnnasCafe.getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Main Course");
		btnNewButton_2.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				fms.NewScreen("m");
			}
		});
		btnNewButton_2.setBounds(412, 166, 175, 57);
		btnNewButton_2.setForeground(new Color(0, 0, 0));
		btnNewButton_2.setBackground(new Color(255, 255, 0));
		btnNewButton_2.setFont(new Font("Goudy Old Style", Font.BOLD, 20));
		frmAnnasCafe.getContentPane().add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Dessert");
		btnNewButton_3.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				fms.NewScreen("d");
			}
		});
		btnNewButton_3.setBounds(597, 166, 175, 57);
		btnNewButton_3.setForeground(new Color(0, 0, 0));
		btnNewButton_3.setBackground(new Color(255, 255, 0));
		btnNewButton_3.setFont(new Font("Goudy Old Style", Font.BOLD, 20));
		frmAnnasCafe.getContentPane().add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Generate Bill");
		btnNewButton_4.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				fms.NewScreen("BILL");
			}
		});
		btnNewButton_4.setBounds(310, 249, 217, 49);
		btnNewButton_4.setForeground(new Color(255, 255, 255));
		btnNewButton_4.setBackground(new Color(0, 255, 0));
		btnNewButton_4.setFont(new Font("Goudy Old Style", Font.BOLD, 20));
		frmAnnasCafe.getContentPane().add(btnNewButton_4);
		
		JLabel lblNewLabel_2 = new JLabel("*Select the items to place order*");
		lblNewLabel_2.setBackground(new Color(0, 255, 0));
		lblNewLabel_2.setBounds(160, 77, 480, 76);
		frmAnnasCafe.getContentPane().add(lblNewLabel_2);
		lblNewLabel_2.setForeground(new Color(255, 255, 255));
		lblNewLabel_2.setFont(new Font("Script MT Bold", Font.BOLD, 30));
		
		
		JLabel background;
		ImageIcon img = new ImageIcon("C:\\Users\\rohit\\eclipse-workspace\\SCE\\src\\Image_source\\food_order5.jpg");
		
		Image i = img.getImage();
		Image new_img = i.getScaledInstance(835,400,Image.SCALE_SMOOTH);
		img = new ImageIcon(new_img);
		background = new JLabel("",img,JLabel.CENTER);
		background.setBounds(-11, -38, 853, 444);
		frmAnnasCafe.getContentPane().add(background);
	}
}
