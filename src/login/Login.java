package login;

//Icon img = new ImageIcon("path");
//ToolTip
import database.*;
import acc_info.*;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import logged_ui.LoggedUI;
import message.Message;
import register.Register;

public final class Login {
	JFrame frame;//��¼����Ŀ��
	MyPanel background;
	private Toolkit tk;
	
//	Just for move the client window and must be here.
//	because it must be shared between class MyMouseListener
//	and class MyMouseMotionListener
//	but this just in this way
//	In the way used in Register.java is a another way
//	to realize it,there is no need to have the following member.
	Point mouseBefore = null;
		
	//some flags for function.
	boolean initializedDimen = false;
	boolean initializedPoint = false;

	//Label for account and password
	JLabel accLabel;
	JLabel pswLabel;
	
	//Field for account and password
	JTextField accField;//�û��˺�������
	JPasswordField pswField;//����������
	
	//Button for login and register
	JButton loginButton;
	JButton regButton;
	//Button for user-defined close and minimize
	JButton closeButton;
	JButton minimizeButton;
	
//	//CheckBox for remember password
//	Checkbox rememberPsw;
	
	//dimensions in different area
	Dimension screenDimen;
	Dimension clientDimen;
	Dimension userIconDimen;
	Dimension inputFieldDimen;
	Dimension labelDimen;
	
	//Point for need
	Point clientSite;
	Point userIconSite;
	Point accLabelSite;
	Point pswLabelSite;
	Point accFieldSite;
	Point pswFieldSite;
	Point loginButtonSite;
	Point regButtonSite;
//	Point rememberPswSite;
	
	//Path for all image
	String bgImgPath = "src/image/logbkg.png";
	String iconImgPath = "src/image/icon.png";
	String minImgPath = "src/image/minimize.png";
	String closeImgPath = "src/image/close.png";
	
	public Login()
	{
		this(null);
	}
	
	public Login(Account acc)
	{
		initToolkit();
		
		try{
			initDimen();
			initPoints();
			initLabel();
			initButton();
		}catch (NullPointerException nullRefer)
		{
			System.out.println(nullRefer.toString());
		}
		

		//���ǲ�֪��Ϊʲô������������ڣ�������޷�����
		background = new MyPanel(bgImgPath);
		Icon ic = new ImageIcon(bgImgPath);

		frame = new JFrame();
//		frame.setLayout(null);���������Ļ�����ͼƬ�Ͳ�����ʾ������Ҳ������
		
		//size
		frame.setSize(clientDimen);
		frame.setResizable(false);
		
		//site
		frame.setLocation(clientSite);
		
		//icon
		//if not exist, the panel will not be drawed and why?
		Image img = Toolkit.getDefaultToolkit().getImage(iconImgPath);
		frame.setIconImage(img);
		frame.addWindowListener(new MyWindowListener());
		frame.addMouseListener(new MyMouseListener());
		frame.addMouseMotionListener(new MyMouseMotionAdapter());
		
		Container ct = frame.getContentPane();		
		
//		�˺�����������
		accField = new JTextField();
		accField.setLocation(accFieldSite);
		accField.setSize(inputFieldDimen);
		if (null != acc)
		{
			accField.setText(acc.getAccount().toString());
		}
		
		pswField = new JPasswordField();
		pswField.setSize(inputFieldDimen);
		pswField.setLocation(pswFieldSite);	
		if (null != acc){
			pswField.setText(acc.getPsw().toString());
		}
		
		loginButton.addActionListener(new MyActionListener());
		regButton.addActionListener(new MyActionListener());
		

		ct.add(closeButton);
		ct.add(minimizeButton);
		ct.add(accField);
		ct.add(pswField);
		ct.add(accLabel);
		ct.add(pswLabel);
		ct.add(loginButton);
		ct.add(regButton);
		ct.add(background);

		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);//��ȥ�߿�
		frame.setVisible(true);

		
	}
	
	/**
	 * Initialize tk(Toolkit)
	 */
	private void initToolkit()
	{
		tk = Toolkit.getDefaultToolkit();
	}
	
	/**Initialize all dimension members,
	 * throw an exception if tk(Toolkit)
	 *  is not initialized.
	 */
	private void initDimen()
	{
		if (null == tk)
		{
			throw new NullPointerException("Member tk(Toolkit) of the class Login"
					+ " is not initialized.");
		}
		clientDimen = new Dimension(600, 375);
		userIconDimen = new Dimension(130, 115);
		inputFieldDimen = new Dimension(120, 20);
		labelDimen = new Dimension(50, 20);
		screenDimen = tk.getScreenSize();
		
		initializedDimen = true;
	}
	
	/**
	 * Initialize all site(point) members.
	 * Throw an exception if function initDimen()
	 * is not called.
	 */
	private void initPoints()
	{
		if (!initializedDimen)
		{
			throw new NullPointerException("Members xxxDimen of class Login "
					+ "is not called.");
		}
		
		clientSite = new Point((screenDimen.width - clientDimen.width)/2,
				(screenDimen.height - clientDimen.height)/2);
		userIconSite = new Point(100, (clientDimen.height - userIconDimen.height) / 2);
		accLabelSite = new Point((userIconSite.x + userIconDimen.width + 50), userIconSite.y);
		pswLabelSite = new Point(accLabelSite.x, accLabelSite.y + labelDimen.height);
		accFieldSite = new Point(accLabelSite.x + labelDimen.width, accLabelSite.y);
		pswFieldSite = new Point(accFieldSite.x, accFieldSite.y + inputFieldDimen.height);
//		rememberPswSite = new Point(pswFieldSite.x + inputFieldDimen.width, pswFieldSite.y);
		
		//�ѵ�¼��ע��ŵ���������棬ע�������½����
		loginButtonSite = new Point(pswFieldSite.x, pswFieldSite.y + 20);
		regButtonSite = new Point(pswFieldSite.x + inputFieldDimen.width / 2, pswFieldSite.y + 20);
		
		initializedPoint = true;
		
	}
	
	/**
	 * Initialize all labels,
	 * Throw NullPointerException if the function initPoints() is not called.
	 */
	private void initLabel()
	{
		if (!initializedPoint)
		{
			throw new NullPointerException("Members xxxSite of class Login is not initialized."
					+ "The function initPoints() must be called."
					+ "Exception in function initLabel()");
		}
		
		accLabel = new JLabel("�˺�");
		accLabel.setSize(labelDimen);
		accLabel.setLocation(accLabelSite);
		
		pswLabel = new JLabel("����");
		pswLabel.setSize(labelDimen);
		pswLabel.setLocation(pswLabelSite);
	}
	
	/**
	 * Initialize the login button and the register button.
	 * Throw NullPointerException if the function initPoints() is not called.
	 */
	private void initButton()
	{
		if (!initializedPoint)
		{
			throw new NullPointerException("Members xxxSite of class Login is not initialized."
					+ "The function initPoints() must be called."
					+ "Exception in function initButton()");
		}
		
		//��¼��ע�������
		loginButton = new JButton("��¼");
		loginButton.setLocation(loginButtonSite);
		loginButton.setSize(inputFieldDimen.width / 2, inputFieldDimen.height);
		loginButton.setBackground(new Color(0, 170, 228));
		loginButton.setBorderPainted(true);
		loginButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				Account acc = new Account();
				acc.setAccount(accField.getText().toString());
				acc.setPsw(new String(pswField.getPassword()));
				msg.setType(Message.Type.LOGIN);
				msg.setContent(acc);
				
				try{
					Socket socket = new Socket("localhost", 9999);
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
					oos.writeObject(msg);
					Message receiveMsg = (Message)ois.readObject();
					
					switch(receiveMsg.getType())
					{
						case Message.Type.WRONGPSW:
							JOptionPane.showMessageDialog(frame, "������������µ�¼");
							socket.close();
							return ;
							
						case Message.Type.NOACCOUNT:
							JOptionPane.showMessageDialog(frame, "û�и��˺�");
							socket.close();
							return;

						
						default:
							new LoggedUI(((Account)receiveMsg.getContent()));
							System.out.println("��½�ɹ�");
							frame.dispose();
					}
				}catch (Exception ex)
				{
					ex.printStackTrace();
					JOptionPane.showMessageDialog(frame, "���������쳣");
					System.exit(-1);
				}
			}
		});
		
		regButton = new JButton("ע��");
		regButton.setLocation(regButtonSite);
		regButton.setBackground(new Color(0, 170, 228));
		regButton.setSize(inputFieldDimen.width / 2, inputFieldDimen.height);
		regButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.dispose();
				new Register();
			}
		});
		
		//�رպ���С��������
		closeButton = new JButton(new ImageIcon(closeImgPath));
		closeButton.setBounds(clientDimen.width - closeButton.getIcon().getIconWidth(), 
				0, closeButton.getIcon().getIconWidth(), closeButton.getIcon().getIconHeight());
		closeButton.setContentAreaFilled(false);
		closeButton.setBorderPainted(false);
		closeButton.setOpaque(false);
		closeButton.addActionListener(new MyActionListener());
		closeButton.addMouseListener(new MyMouseListener());
		
		minimizeButton = new JButton(new ImageIcon(minImgPath));
		minimizeButton.setBounds(clientDimen.width - 2 * minimizeButton.getIcon().getIconWidth(), 
				0, minimizeButton.getIcon().getIconWidth(), minimizeButton.getIcon().getIconHeight());
		minimizeButton.setContentAreaFilled(false);
		minimizeButton.setBorderPainted(false);
		minimizeButton.setOpaque(false);
		minimizeButton.addActionListener(new MyActionListener());
		minimizeButton.addMouseListener(new MyMouseListener());
	}
	
	public static void main(String[] args)
	{
		@SuppressWarnings("unused")
		Login lg = new Login();
	}
	
	//�Զ������
	@SuppressWarnings("serial")
	class MyPanel extends JPanel
	{
		public String imgSource;
		public MyPanel(String s)
		{
			imgSource = s;
			tk = Toolkit.getDefaultToolkit();
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Image img = tk.getImage(imgSource);
			g.drawImage(img, 0, 0, null);
		}
	}
	
	//�Զ�������¼��ļ�����
	class MyMouseListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource() == closeButton)
			{
				closeButton.setContentAreaFilled(true);
				closeButton.setBackground(new Color(255, 0, 0));
			}
			if (e.getSource() == minimizeButton)
			{
				minimizeButton.setContentAreaFilled(true);
				minimizeButton.setBackground(new Color(0, 170, 228));
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource() == closeButton)
			{
				closeButton.setContentAreaFilled(false);
			}
			if (e.getSource() == minimizeButton)
			{
				minimizeButton.setContentAreaFilled(false);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			mouseBefore = null;
		}
		
	}
	
	//�Զ�����궯��������
	class MyMouseMotionAdapter extends MouseMotionAdapter
	{
		@Override
		public void mouseDragged(MouseEvent e)
		{
			//Move the client window.
			if (e.getSource() == frame)
			{
				if (null == mouseBefore)
				{
					mouseBefore = e.getLocationOnScreen();
					return ;
				}
				Point clientBefore = frame.getLocation();
				Point mouseNow = e.getLocationOnScreen();
				int offsetX = mouseNow.x - mouseBefore.x;
				int offsetY = mouseNow.y - mouseBefore.y;
				frame.setLocation(clientBefore.x + offsetX, clientBefore.y + offsetY);
				mouseBefore = mouseNow;
			}
		}
	}
	
//	�Զ��尴ť������
	class MyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			//����رհ�ť
			if (e.getSource() == closeButton)
			{
				System.exit(0);
			}
			//�����С����ť
			if (e.getSource() == minimizeButton)
			{
				frame.setExtendedState(JFrame.ICONIFIED);
			}
			//���ע�ᰴť
			if (e.getSource() == regButton)
			{
				System.out.println(accField.getText().toString());
			}
//			�����¼��ť
			if (e.getSource() == loginButton)
			{	
				//�������ݵĺϷ��Լ��
//				�˺�����Ϊ��Ϊ���֣����Ϊ10
//				���ҵ�һ�����ֲ�����Ϊ0
				String pattern = ".{6,9}";
				Scanner scanner = new Scanner(accField.getText().toString());
				if (!scanner.hasNext(pattern))
				{
					System.out.println("wrong input");
				}
				else
				{
					System.out.println(accField.getText().toString());
				}
				scanner.close();
				
			}
		}
		
	}
	
	class MyWindowListener extends WindowAdapter{
		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
//			һ��ʼ���ù��λ�ô����˺����������
			accField.requestFocus();
		}
		
	}
}
	




