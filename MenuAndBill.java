import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
class MenuAndBill extends JFrame {
	private JScrollPane p;
	private static final String SERVER_IP = "localhost";
	private static final int SERVER_PORT = 9904;
	int total_amount=0;
	String all_order = "";
	int total_items=0,index,quantity;
	private static int t_no;
	static String type="";

	public static void NewScreen(String food) {
		type=food;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuAndBill jf = new MenuAndBill();
					t_no = Integer.parseInt(JOptionPane.showInputDialog("Order for which Table number"));
					jf.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MenuAndBill() {
	
		setForeground(new Color(0, 0, 0));
		setFont(new Font("Arial", Font.BOLD, 20));
		setBackground(new Color(245, 222, 179));
		setType(Type.POPUP);
		setTitle("Annas Cafe");
		getContentPane().setBackground(Color.GRAY);
		if(type=="BILL") {
			bill_generate();
		}
		else
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initialize(){
		
		setBounds(850,5, 513, 461);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_1.setBackground(new Color(233, 150, 122));
		panel_1.setBounds(10, 10, 481, 404);
		
        String item="";
        if(type=="s") item = "Starters";
        else if(type=="n") item = "Snacks";
        else if(type=="m") item = "Main Course";
        else item = "Dessert";
		Object[][] data= new Object[20][3] ;
		String [] column = {"id",item,"Price"};
		
		// reads menu from  the data from server.
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_order","root","Rohitgd@15");    
			Statement stmt = con.createStatement(); 
			String sql = "Select * from menu where type= '"+type+"'";
			ResultSet rs = stmt.executeQuery(sql);
			
			for(int i=0;rs.next();i++) {
				data[i][0] = rs.getString(1);  // id
				data[i][1] = rs.getString(2);  // item name
				data[i][2] = rs.getInt(4); 	   // price
			}
	     }
	     catch(Exception e1) {
	    	 System.out.println(e1);
	     }
		

		DefaultTableModel Model = new DefaultTableModel(data,column);
		final JTable t1 = new JTable(Model); 

		
		all_order = ""; 
		
		t1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			     index = t1.getSelectedRow();
			     String quanti =JOptionPane.showInputDialog("Enter quantity");
			     if(quanti!=null) {
			    	 quantity = Integer.parseInt(quanti);
				     System.out.println("adding...");
				     all_order = all_order +  data[index][0].toString() + Integer.toString(quantity);
				     total_items ++; 
			     }
			     
			}
		});
		
		
		t1.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		t1.setRowHeight(20);
		
		t1.setSelectionForeground(new Color(128, 0, 0));
		t1.setSelectionBackground(new Color(127, 255, 0));
		t1.setGridColor(new Color(0, 0, 139));
		t1.setForeground(new Color(0, 0, 0));
		t1.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		t1.setBackground(new Color(255, 250, 205));
		t1.addPropertyChangeListener(getName(), null);
		TableColumn tc1 = t1.getColumnModel().getColumn(0);
		tc1.setMinWidth(30);
		tc1.setMaxWidth(50);
		tc1.setPreferredWidth(30);
		panel_1.setLayout(null);
	
		p = new JScrollPane(t1);
		p.setFont(new Font("Goudy Old Style", Font.BOLD, 13));
		p.setBounds(10, 10, 457, 334);
		p.setBorder(new CompoundBorder());
		panel_1.add(p);
		getContentPane().add(panel_1);
		
		
		
		JButton submitbtn = new JButton("Submit");
		submitbtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				all_order = Integer.toString(t_no) + Integer.toString(total_items) + all_order;		// adding total items ordered in front of string.
				sendstring(all_order);
				all_order = "";
				dispose();
			}
		});
		
		
		submitbtn.setBackground(new Color(127, 255, 0));
		submitbtn.setFont(new Font("Goudy Old Style", Font.BOLD, 23));
		submitbtn.setBounds(158, 354, 144, 40);
		panel_1.add(submitbtn);
	}
	
	public static void sendstring(String str) {
		try {
			System.out.println("order string ="+str);
	        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
	        System.out.println("SERVER - connected to client!");
		OutputStreamWriter os = new OutputStreamWriter(socket.getOutputStream());
		
        PrintWriter out = new PrintWriter(os);
        os.write(str);  // data sent to server 	
        os.flush();
        socket.close();
        System.out.println("Socket has been closed");}catch(IOException e1){}
	}
	
	
	
	
	
	
	private void bill_generate() {
		Object[][] data= new Object[20][4] ;
		String [] column = {"Food_item","Quantity","Price","Total"};
		
		// reads menu from  the data from server.
		int i=0;
	try {
		   int tno = Integer.parseInt(JOptionPane.showInputDialog("Order for which Table number!!"));
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_order","root","Rohitgd@15");    
			Statement stmt = con.createStatement(); 
		
				
				String sql = "Select * from accepted_order where t_no ='"+tno+"'";
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()) {
					data[i][0] = rs.getString(2);  // item name
					data[i][1]=  rs.getInt(4);  //item quantity
					data[i][2] = rs.getInt(3); 	// price
					data[i][3] = rs.getInt(4)*rs.getInt(3); //total
					total_amount+=rs.getInt(4)*rs.getInt(3);
					i++;
				}
					
			data[i+2][0]="Total";
			data[i+2][3]=total_amount;
			DefaultTableModel Model = new DefaultTableModel(data,column);
			
			
			
			setBounds(100, 100, 800, 400);
			setForeground(new Color(0, 0, 153));
			setFont(new Font("Dialog", Font.BOLD, 12));
			setTitle("Your Bill");
			getContentPane().setBackground(new Color(255, 215, 0));
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			
			
			
			JPanel panel_1 = new JPanel();
			panel_1.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
			panel_1.setBounds(0,0,800,380);
			getContentPane().add(panel_1);
			panel_1.setBackground(new Color(192, 192, 192));
			panel_1.setLayout(null);
			panel_1.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		
			
			
			final JTable t1 = new JTable(Model); 
			t1.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
			t1.setRowHeight(20);
			
			t1.setSelectionForeground(new Color(128, 0, 0));
			t1.setSelectionBackground(new Color(127, 255, 0));
			t1.setGridColor(new Color(0, 0, 139));
			t1.setForeground(new Color(0, 0, 0));
			t1.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
			t1.setBackground(new Color(255, 250, 205));
			
			
			panel_1.setLayout(null);
			
			JScrollPane p = new JScrollPane(t1);
			p.setBorder(new CompoundBorder());
			p.setBounds(5,5,780,300);
			panel_1.add(p);
			getContentPane().add(panel_1);
			
			JButton btnNewButton = new JButton("We are Done!!");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(total_amount!=0) {
						String str = "B"+Integer.toString(t_no) ;
						sendstring(str);
					}
					
					dispose();
			        
				}
			});
			btnNewButton.setBounds(100,310,200,25);
			btnNewButton.setForeground(new Color(255, 255, 255));
			btnNewButton.setBackground(new Color(128, 0, 128));
			btnNewButton.setFont(new Font("Goudy Old Style", Font.BOLD, 20));
			
			panel_1.add(btnNewButton);
			getContentPane().add(panel_1);
			p.setVisible(true); 
			
	     }
	     catch(Exception e1) {
	    	 System.out.println(e1);
	     }
		
	}
}