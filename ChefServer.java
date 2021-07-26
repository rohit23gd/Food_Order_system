import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ChefServer extends JFrame {
	
	private static final int SERVER_PORT1 = 9904;
	private int current_row = 0;
	Object[][] data=new Object[20][2];
	Object[][] data1 = new Object[50][3];
	Object[][] data2= new Object[20][2] ;
	Object[][] data3= new Object[20][4] ;
	String [] column = {"Sr.No","Table Number"};
	String [] column1 = {"Sr.No","Ordered Table","Quantity"};
	String [] column2 = {"Sr.no.","Table"};
	String [] column3 = {"Food Item","quantity","Price","Total"};
	DefaultTableModel Model,Model1,Model2,Model3;
	int billing_n=0;
	int total_items,table_no=0;
	StringBuffer orderc= new StringBuffer("");
	
	public static void main(String[] args) {

				try {
					ChefServer jf = new ChefServer();
					jf.setVisible(true);
					while(true) {
						jf.runsocket();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

	/**
	 * Create the application.
	 **/
	public ChefServer() {
		setForeground(new Color(0, 0, 0));
		setFont(new Font("Arial", Font.BOLD, 20));
		setBackground(new Color(245, 222, 179));
		setType(Type.POPUP);
		setTitle("Annas Cafe");
		getContentPane().setBackground(Color.GRAY);
		initialize();
		
	}
	
	private Object[][] update_chef_order_table(Object [][]temp) {
		try {
	    	Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_order","root","Rohitgd@15");
    	   	 System.out.println("connected to database");  
    	   	Statement stmt = con1.createStatement();
    	   	String sql = "Select * from orders";
    	   	ResultSet rs = null;
    	   	
    	   	rs = stmt.executeQuery(sql);
    	   	
    	   	int i=0;
    	   	while(rs.next()) {
    	   		if(rs.getInt(1) == table_no) {
    	   			System.out.println("Adding..."+rs.getInt(1));
    	   			temp[i][0] = i+1;
    	   			temp[i][1] = rs.getString(2);
    	   			temp[i][2] = rs.getInt(4);
    	   			i++;
    	   			System.out.println("Added!");
    	   		}
    	   	}
    	   	con1.close();
    	 
	    }
	    catch(Exception e1) {
	    	System.out.println(e1);
	    }
		return temp;
	}
	
	private void update_into_pending_order(StringBuffer order) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); 
    	   	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_order","root","Rohitgd@15");
    	   	Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_order","root","Rohitgd@15"); 
    	   	Statement stmt = con.createStatement();
    	   	String sql;
    	   	String sql1;
    	   	ResultSet rs = null;
    	   	
    	   	int t_no = Integer.parseInt(order.substring(0,1));
    	   	order.delete(0, 2);
    	   	while(order.length()>0) {
    	   		System.out.println(order);
    	   		String item_id = order.substring(0,2).toString();
    	   		order.delete(0,2);
    	   		
    	   		System.out.println(order);
    	   		int q = Integer.parseInt(order.substring(0,1));
    	   		order.delete(0,1);
    	   		
    	   		System.out.printf(item_id + " " + q+"\n");
    	   		
    	   		sql = "select * from menu where id =" + "'" + item_id + "'";
    	   		
    	   		rs = stmt.executeQuery(sql);
    	   		
    	   		System.out.println("doing\n");
    	   		
    	   		if(rs.next()) {
    	   			System.out.println("updating...");
    	   			sql1 = "insert into orders(t_no,food_item,price,q) values(?,?,?,?)";
    	   			PreparedStatement stmt1 = con1.prepareStatement(sql1);
    	   			stmt1.setInt(1, t_no);
    	   			stmt1.setString(2, rs.getString("food_name"));
    	   			stmt1.setInt(3, rs.getInt("price"));
    	   			stmt1.setInt(4, q);
    	   			stmt1.executeUpdate();
    	   		}
    	   		System.out.println("updated!");
    	   	}
    	}
    	catch(Exception e1) {
    		 	System.out.println(e1);
    	}
	}
	
	
	private void update_into_accepted_order() {
		try {
			Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_order","root","Rohitgd@15");
			System.out.println("connected to database");  
			Statement st = con1.createStatement();
			  //Copy table
			  int rows = st.executeUpdate("INSERT INTO accepted_order SELECT * FROM orders where t_no = " + "'" + table_no + "'");
			  if (rows == 0){
			  System.out.println("Don't add any row!");
			  }
			  else{
			  System.out.println(rows + " row(s)affected.");
			  }
   	   	con1.close();
		}
    	catch(Exception e1) {
    		 	System.out.println(e1);
    	}
	}
	private void delete_from_pending_order() {
		try {
			Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_order","root","Rohitgd@15");
			System.out.println("connected to database");  
			Statement st = con1.createStatement();
			  //Copy table
			  int rows = st.executeUpdate("DELETE FROM orders where t_no = " + "'" + table_no + "'");
			  if (rows > 0){
			  System.out.println("Data Deleted Successfully");
			  }

   	   	con1.close();
		}
    	catch(Exception e1) {
    		 	System.out.println(e1);
    	}
	}
	//for removing accepted order from JTable
			private Object[][] removeAllRows(int tn,Object[][] data) {
				int num=0;
				int mark=0;
				int k=0;
				Object [][]temp = new Object[20][2];
				for(int i=0;i<current_row;i++) {
			    	num =(int) data[i][1];
			    	if(num!=tn ) {
			    		temp[k][0]=k+1;
			    		temp[k][1]=data[i][1];
			    		k++;
			    	}
			    	else mark++;
			    }
			   if(mark==1) current_row-=mark;
		       return temp;
		}
	
			//Updating table content(Removing accpeted order from the list)
	private Object[][] update_tbl_no(int tn,Object[][] data) {
	        int mark =0;
	        int num;
		    for(int i=0;i<current_row;i++) {
		    	num =(int) data[i][1];
		    	if(num==tn ) {
		    		mark =1;
		    		break;
		    	}
		    }
		    if(mark==0) {
		    	data[current_row][0] = current_row+1;
				data[current_row][1] =tn;
				current_row++;
		    }
		return data;
	}

     //Socket will run after recieving String again and agian
	private void runsocket() {
		try {
	    	System.out.println("SERVER - started!!");
	    	ServerSocket listener1 = new ServerSocket(SERVER_PORT1);
	    	
	    	System.out.println("SERVER - waiting for client connection...");
	    	Socket socket = listener1.accept();   // single socket
	    	System.out.println("SERVER - connected to client!");
	    	
	    	BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    	String order = br.readLine();
	    	System.out.println("Client order pre: " + order);
	    	if(order.indexOf("B")==0) { //String sent for BILLING
	    		data2[billing_n][0]= billing_n+1;
	    		data2[billing_n][1]=Integer.parseInt(order.substring(1,2));
	    		billing_n++;
	    		Model2.setDataVector(data2, column2);
			    Model2.fireTableDataChanged();
	    	}
	    	else { //String sent for Order Placing
	    		orderc = new StringBuffer(order);
		    	System.out.println("Client order: " + order);
		    	StringBuffer or = new StringBuffer(order);
		    	StringBuffer or1 = new StringBuffer(order);
		    	
		    	update_into_pending_order(or); //Update into mysql database
		    	int tablen=Integer.parseInt(or1.substring(0,1));//get the table number from string
		        data =update_tbl_no(tablen,data);//Update in the Jtable T1
		    	
		        Model1.setDataVector(data, column);
			    Model1.fireTableDataChanged(); //Change the JTable content for pending list
		       	total_items = tablen;
	    	}
	    	listener1.close();
	    	socket.close();
	    	System.out.println("socket is closed!!");
	    	
	    }
	    catch(Exception e1) {
	    	System.out.println(e1);
	    }
	
	}
private void billing() { //TO When Customer generates a bill ,bill will  appear here.
	    JFrame jfb = new JFrame();
	    jfb.setForeground(new Color(0, 0, 0));
		jfb.setFont(new Font("Arial", Font.BOLD, 20));
		jfb.setBackground(new Color(245, 222, 179));
		jfb.setType(Type.POPUP);
		jfb.setTitle("BILLS");
		jfb.getContentPane().setBackground(Color.GRAY);
		jfb.setBounds(100, 100, 835, 400);
		jfb.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jfb.getContentPane().setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_1.setBackground(new Color(192, 192, 192));
		panel_1.setBounds(20, 21, 322, 320);

		
		
		panel_1.setLayout(null);
		jfb.getContentPane().add(panel_1);
		JTable t1 = new JTable(Model2);
		t1.setFont(new Font("Arial Black", Font.BOLD, 12));
		t1.setRowHeight(20);
		t1.setToolTipText("Accepted order");
		t1.setSelectionForeground(new Color(128, 0, 0));
		t1.setSelectionBackground(new Color(0, 255, 255));
		t1.setGridColor(new Color(0, 0, 139));
		t1.setForeground(new Color(0, 0, 0));
		t1.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		t1.setBackground(new Color(173, 255, 47));
		t1.addPropertyChangeListener(getName(), null);
	    
	
		JScrollPane p1 = new JScrollPane(t1);
		p1.setBounds(30, 23, 265, 275);
		panel_1.add(p1);
		p1.setBorder(new CompoundBorder());
		
		
		
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_2.setBackground(new Color(192, 192, 192));
		panel_2.setBounds(382, 21, 413, 320);
		
		
		Model3 = new DefaultTableModel(data3,column3);
		panel_2.setLayout(null);
		jfb.getContentPane().add(panel_2);
		JTable t2 = new JTable(Model3);
		t2.setFont(new Font("Arial Black", Font.BOLD, 12));
		t2.setRowHeight(20);
		t2.setToolTipText("Your Bill");
		t2.setSelectionForeground(new Color(128, 0, 0));
		t2.setSelectionBackground(new Color(0, 255, 255));
		t2.setGridColor(new Color(0, 0, 139));
		t2.setForeground(new Color(0, 0, 0));
		t2.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		t2.setBackground(new Color(173, 255, 47));
		t2.addPropertyChangeListener(getName(), null);
		TableColumn tc2 = t2.getColumnModel().getColumn(0);
		tc2.setMinWidth(130);
		tc2.setMaxWidth(130);
		tc2.setPreferredWidth(130);
		
			JScrollPane p2 = new JScrollPane(t2);
			p2.setBounds(39, 24, 341, 243);
			panel_2.add(p2);
			p2.setBorder(new CompoundBorder());
			
			JButton btnNewButton = new JButton("Done");
			btnNewButton.setForeground(Color.BLACK);
			btnNewButton.setBackground(Color.YELLOW);
			btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
			btnNewButton.setBounds(159, 273, 103, 37);
			btnNewButton.addActionListener(new ActionListener() {  //Removing data from accepted order table for permanently
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
							Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_order","root","Rohitgd@15");
							System.out.println("connected to database");  
							Statement st = con1.createStatement();
						  
						    int rows = st.executeUpdate("DELETE FROM accepted_order where t_no = " + "'" + table_no + "'");
						    if (rows > 0){
						         System.out.println("Data Deleted Successfully");
						    }
			   	        	con1.close();
					}
			    	catch(Exception e1) {
			    		 	System.out.println(e1);
			    	}
					int num=0;
					int mark=0;
					int k=0;
					Object [][]temp = new Object[20][2];  //Update the billing history table
					for(int i=0;i<billing_n;i++) {
				    	num =(int) data2[i][1];
				    	if(num!=table_no ) {
				    		temp[k][0]=k+1;
				    		temp[k][1]=data2[i][1];
				    		k++;
				    	}
				    	else mark++;
				    }
				   if(mark==1) current_row-=mark;
			       data2=temp;
					billing_n--;
		    		Model2.setDataVector(data2, column2);
				    Model2.fireTableDataChanged();
					Object temp1[][]=new Object[20][4];
					data3 = temp1;
				    Model3.setDataVector(temp1, column3); //Clearing the Bill Table
				    Model3.fireTableDataChanged();
					
				}
			});
			panel_2.add(btnNewButton);
			jfb.setVisible(true);
			t1.addMouseListener(new MouseAdapter() {//Getting the Bill Details from the Mysql Database
				@Override
				public void mouseClicked(MouseEvent e) {
					
				    int index = t1.getSelectedRow();
				    table_no = (int) data2[index][1];
				    Object [][] temp = new Object[20][4];
				    try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_order","root","Rohitgd@15");    
						Statement stmt = con.createStatement(); 
					
						int i=0,total_amount=0;
						String sql = "Select * from accepted_order where t_no ='"+table_no+"'";
						ResultSet rs = stmt.executeQuery(sql);
						while(rs.next()) {
							temp[i][0] = rs.getString(2);  // item name
							temp[i][1]=  rs.getInt(4);  //item quantity
							temp[i][2] = rs.getInt(3); 	// price
							temp[i][3] = rs.getInt(4)*rs.getInt(3); //total
							total_amount +=rs.getInt(4)*rs.getInt(3);
							i++;
						}
							
						temp[i+2][0]="Total";
						temp[i+2][3]=total_amount;
						data3=temp;
					    Model3.setDataVector(temp, column3);
					    Model3.fireTableDataChanged();
					} catch ( SQLException | ClassNotFoundException e1) {
						e1.printStackTrace();
					}
					
				}
			});
}
private void initialize() {
		
	    Model2 = new DefaultTableModel(data2,column2);
		setBounds(500,400, 835, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		// top panel.
		JPanel panel = new JPanel();
		panel.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0), 1, true), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
		panel.setBackground(new Color(192, 192, 192));
		panel.setBounds(0, 0, 821, 40);
		getContentPane().add(panel);
		panel.setLayout(null);
		

		
		JButton btnNewButton_1 = new JButton("Billing");
		btnNewButton_1.setBackground(new Color(152, 251, 152));
		btnNewButton_1.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0), 1, true), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
		btnNewButton_1.setBounds(58, 7, 104, 30);
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				billing();
			}
		});
		panel.add(btnNewButton_1);
		
		//left panel
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_1.setBounds(29, 61, 363, 277);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		
		//right panel
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_2.setBackground(new Color(192, 192, 192));
		panel_2.setBounds(429, 62, 369, 277);
			
		// table on right panel
		
		Model1 = new DefaultTableModel(data,column);
		final JTable t1 = new JTable(Model1);
		
		t1.setFont(new Font("Arial Black", Font.BOLD, 12));
		t1.setRowHeight(20);
		t1.setToolTipText("Recent Orders");
		t1.setSelectionForeground(new Color(128, 0, 0));
		t1.setSelectionBackground(new Color(0, 255, 255));
		t1.setGridColor(new Color(0, 0, 139));
		t1.setForeground(new Color(0, 0, 0));
		t1.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		t1.setBackground(new Color(173, 255, 47));
		t1.addPropertyChangeListener(getName(), null);
        TableColumn tc1 = t1.getColumnModel().getColumn(0);
		
		tc1.setPreferredWidth(30);
		panel_2.setLayout(null);

		JScrollPane p = new JScrollPane(t1);
		p.setBorder(new CompoundBorder());
		p.setBounds(35, 23, 307,232);
		panel_2.add(p);
		getContentPane().add(panel_2);
		
		
		
		Model = new DefaultTableModel(update_chef_order_table(data1),column1);
		JTable t2 = new JTable(Model) ;
		
		t2.setFont(new Font("Goudy Old Style", Font.BOLD, 18));
		t2.setRowHeight(20);
		t2.setToolTipText("Ordered Items");
		t2.setSelectionForeground(new Color(128, 0, 0));
		t2.setSelectionBackground(new Color(0, 255, 255));
		t2.setGridColor(new Color(0, 0, 139));
		t2.setForeground(new Color(0, 0, 0));
		t2.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		t2.setBackground(new Color(173, 255, 47));
		t2.addPropertyChangeListener(getName(), null);
		TableColumn tc01 = t2.getColumnModel().getColumn(0);
		TableColumn tc02 = t2.getColumnModel().getColumn(1);
		TableColumn tc03 = t2.getColumnModel().getColumn(2);
		tc01.setPreferredWidth(5);
		tc02.setPreferredWidth(150);
		tc03.setPreferredWidth(5);
		panel_1.setLayout(null);
		
		final JScrollPane p1 = new JScrollPane(t2);
		p1.setBorder(new CompoundBorder());
		p1.setBounds(20, 10, 322, 229);
		panel_1.add(p1);
		getContentPane().add(panel_1);
		
		
		JButton btnNewButton_2 = new JButton("Accept Order");
		btnNewButton_2.setForeground(new Color(0, 0, 0));
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_2.setBackground(new Color(255, 215, 0));
		btnNewButton_2.addActionListener(new ActionListener() {//After accepting order detail will be sent to Accepted order. 
			@Override
			public void actionPerformed(ActionEvent e) {
				
				update_into_accepted_order();
				delete_from_pending_order();
				Object [][] temp = new Object[50][3];
			    data = removeAllRows(table_no,data);
			    Model1.setDataVector(data, column);
			    Model1.fireTableDataChanged();
			    data1=temp;
			    Model.setDataVector(data1, column1);
			    Model.fireTableDataChanged();
				
			}
		});
		btnNewButton_2.setBounds(105, 243, 136, 24);
		panel_1.add(btnNewButton_2);
        p1.setVisible(false);    // will be visible is table1 button is click.
        
        // for displaying the order in server table
		t1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				p1.setVisible(true);
			    int index = t1.getSelectedRow();
			    table_no = (int) data[index][1];
			    Object [][] temp = new Object[50][3];
			    data1 = update_chef_order_table(temp);
			    Model.setDataVector(data1, column1);
			    Model.fireTableDataChanged();
			}
		});	
		
	}
}