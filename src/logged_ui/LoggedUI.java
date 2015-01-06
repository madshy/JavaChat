package logged_ui;

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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.tools.Tool;

import chat.ChatUI;

public final class LoggedUI extends JFrame{
	UserInfo usrInfo;
	FriendInfo frdInfo;
	Account acc;
	
	Point mouseBefore = null;//For moving window.
	
	/**
	 * Throw NullPointerException if acc is null.
	 * @param acc
	 */
	public LoggedUI(Account acc)
	{
		if (null == acc)
		{
			throw new NullPointerException("To initialize LoggedUI, acc cannot be null.");
		}
		this.acc = acc;
		/*Paths for all images.*/
		String bkgImgPath = "src/image/bkg_logged_ui.png";
		String minImgPath = "src/image/minimize.png";
		String closeImgPath = "src/image/close.png";
		String iconImgPath = "src/image/icon.png";
		
		/*Data for initializing ui.*/
		final Image bkg = Toolkit.getDefaultToolkit().getImage(bkgImgPath);
		Icon ic = new ImageIcon(bkgImgPath);
		Dimension uiDimen = new Dimension(bkg.getWidth(this), bkg.getHeight(this));
		Dimension screenDimen = Toolkit.getDefaultToolkit().getScreenSize();
		
		/*Initializing ui.*/
		setSize(uiDimen);
		setLocation(screenDimen.width - 100 - getWidth(), 0);//100为自定义距离屏幕右边的间距
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(iconImgPath));
		setUndecorated(true);
		//Setting for moving window
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				mouseBefore = null;
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e)
			{
				//Move the client window.
				if (e.getSource() == LoggedUI.this)
				{
					if (null == mouseBefore)
					{
						mouseBefore = e.getLocationOnScreen();
						return ;
					}
					Point clientBefore = LoggedUI.this.getLocation();
					Point mouseNow = e.getLocationOnScreen();
					int offsetX = mouseNow.x - mouseBefore.x;
					int offsetY = mouseNow.y - mouseBefore.y;
					LoggedUI.this.setLocation(clientBefore.x + offsetX, clientBefore.y + offsetY);
					mouseBefore = mouseNow;
				}
			}
		});
		
		Container ct = getContentPane();
	
		//Add min and close buttons in the window
		JButton closeButton = new JButton(new ImageIcon(closeImgPath));
		closeButton.setBounds(uiDimen.width - closeButton.getIcon().getIconWidth(), 
				0, closeButton.getIcon().getIconWidth(), closeButton.getIcon().getIconHeight());
		closeButton.setContentAreaFilled(false);
		closeButton.setBorderPainted(false);
		closeButton.setOpaque(false);
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				closeButton.setContentAreaFilled(true);
				closeButton.setBackground(new Color(255, 0, 0));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				closeButton.setContentAreaFilled(false);

			}
		});
		
		JButton minimizeButton = new JButton(new ImageIcon(minImgPath));
		minimizeButton.setBounds(uiDimen.width - 2 * minimizeButton.getIcon().getIconWidth(), 
				0, minimizeButton.getIcon().getIconWidth(), minimizeButton.getIcon().getIconHeight());
		minimizeButton.setContentAreaFilled(false);
		minimizeButton.setBorderPainted(false);
		minimizeButton.setOpaque(false);
		minimizeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				LoggedUI.this.setExtendedState(JFrame.ICONIFIED);
			}
		});
		minimizeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				minimizeButton.setContentAreaFilled(true);
				minimizeButton.setBackground(new Color(0, 170, 228));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				minimizeButton.setContentAreaFilled(false);
			}
		});
		ct.add(minimizeButton);
		ct.add(closeButton);
		
		//添加用户信息模块
		JLabel accLabel = new JLabel(acc.getAccount());
		JLabel nameLabel = new JLabel(acc.getName());
		usrInfo = new UserInfo(accLabel, nameLabel, uiDimen.width, 150);
		usrInfo.setOpaque(false);//设置透明
		ct.add(usrInfo);
		
		//添加朋友信息模块
		JTabbedPane tabs = new JTabbedPane();
		ArrayList<Friend> friend = acc.getFriendList();
		String[] name = new String[friend.size()];
		for (int i = 0; i < name.length; ++ i)
		{
			System.out.println("\naccount:" + friend.get(i).getAccount() + "\nname:" + friend.get(i).getName());
			name[i] = friend.get(i).getName();
		}
		final JList frdList = new JList(name);
		frdList.setOpaque(false);
		final JList grpList = new JList();
		frdList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (2 == e.getClickCount())//双击进入聊天界面
				{
					Info chat = acc.getFriendList().get(frdList.getSelectedIndex());
					System.out.println("打开聊天界面");
					new ChatUI(acc, chat);
					System.out.println("打开成功");
				}
			}
		});
		JScrollPane jspFrd = new JScrollPane(frdList);
		JScrollPane jspGrp = new JScrollPane(grpList);
		jspFrd.setOpaque(false);
		tabs.addTab("Friends", jspFrd);
		tabs.addTab("Groups", jspGrp);
		tabs.setSize(getWidth(), getHeight() - usrInfo.getHeight());
		tabs.setLocation(0, usrInfo.getHeight());
		tabs.setOpaque(false);
		tabs.setBackground(Color.GREEN);	

		ct.add(tabs);
		
		//Draw background for ui.This step must be the last.
		ct.add(new JPanel(){
			public void paintComponent(Graphics g)
			{
				if (null == g)
				{
					throw new NullPointerException("Graphics g is a null value.");
				}
				if (null == bkg)
				{
					throw new NullPointerException("Image bkg is a null value.");
				}
				g.drawImage(bkg, 0, 0, this);
			}
		});
		
		setVisible(true);
		

		
	}

//	public static void main(String[] args)
//	{
////		Account acc = new Account("752825526", "madshy1994", "madshy", true, "1994-05-09");
//		
//		Account acc = new Account();
//		acc.setAccount("7528255256");
//		acc.setPsw("madshy1994");
//		acc.setName("madshy");
//		acc.setSex(true);
//		acc.setBirthday("1994-05-09");
//		
//		LoggedUI lgu = new LoggedUI(acc);
//		lgu.setVisible(true);
//		
//	}
}

//用户信息
final class UserInfo extends JPanel{
	JLabel accInfo, nameInfo;
	
	public UserInfo(JLabel accInfo, JLabel nameInfo, Dimension size)
	{
		this(accInfo, nameInfo, size.width, size.height);
	}
	
	public UserInfo(JLabel accInfo, JLabel nameInfo, int sizeX, int sizeY)
	{
		setLayout(null);
		this.accInfo = accInfo;
		this.nameInfo = nameInfo;
		this.accInfo.setOpaque(false);
		this.nameInfo.setOpaque(false);
		setSize(sizeX, sizeY);
		

		Dimension labelDimen = new Dimension(100, 20);
		add(this.accInfo);
		this.accInfo.setSize(labelDimen);
		add(this.nameInfo);
		this.nameInfo.setSize(labelDimen);
		
		int offsetX = (getWidth() - labelDimen.width) / 2;
		int offsetY = (getHeight() - 2 * labelDimen.height) / 2;//2个标签
		this.accInfo.setLocation(offsetX, offsetY);
		this.nameInfo.setLocation(offsetX, offsetY + labelDimen.height);
		
		setVisible(true);
	}
}

//好友信息
final class FriendInfo extends JFrame{
	JTabbedPane tabs;
	JList frdList, grpList;
	
	public FriendInfo(JTabbedPane tabs, JList frdList, JList grpList, Dimension size)
	{
		this(tabs, frdList, grpList, size.width, size.height);
	}
	
	public FriendInfo(JTabbedPane tabs, JList frdList, JList grpList, int sizeX, int sizeY)
	{
		this.tabs = tabs;
		this.frdList = frdList;
		this.grpList = grpList;
		this.setSize(sizeX, sizeY);
		setVisible(true);
	}
}


//package logged_ui;
//
//import java.awt.Container;
//import java.awt.Dimension;
//import java.awt.Graphics;
//import java.awt.GridLayout;
//import java.awt.Image;
//import java.awt.Toolkit;
//import java.awt.event.WindowEvent;
//import java.awt.event.WindowListener;
//
//import javax.swing.Icon;
//import javax.swing.ImageIcon;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//
//public final class LoggedUI extends JFrame{
//	String[] tabNameStr = {"friends", "groups"};
//	JPanel infoPanel;//信息面板，容纳个人信息和好友信息
//	JPanel accInfoPanel;//个人信息面板
//	JPanel frdInfoPanel;//好友信息面板
//	
//	String bkgImgPath = "src/image/bkg_logged_ui.png";
//	String minImgPath = "src/image/minimize.png";
//	String closeImgPath = "src/image/close.png";
//	String iconImgPath = "src/image/icon.png";
//	String userIconPath;
//	
//	Image userIcon;
//	Image bkg;
//	JLabel accLabel, nameLabel;
//	
//	boolean initializedLabel = false;
//	
//	public LoggedUI()
//	{
//		bkg = Toolkit.getDefaultToolkit().getImage(bkgImgPath);
//		Icon ic = new ImageIcon(bkgImgPath);
//		Dimension uiDimen = new Dimension(bkg.getWidth(this), bkg.getHeight(this));
//		Dimension screenDimen = Toolkit.getDefaultToolkit().getScreenSize();
//		userIcon = Toolkit.getDefaultToolkit().getImage("src/image/user_ic_1.png");//仅仅是测试使用！
//		try
//		{
//			initLabel();
//			initPanel();
//		}catch (Exception e)
//		{
//			System.out.println(e.toString());
//		}
//		
//
//
//
//		setSize(uiDimen);
//		setLocation(screenDimen.width - 100 - getWidth(), 0);//100为自定义距离屏幕右边的间距
//		setResizable(false);
//		setIconImage(Toolkit.getDefaultToolkit().getImage(iconImgPath));
//		Container ct = getContentPane();
//		setUndecorated(true);
//		ct.add(infoPanel);
//		
//		setVisible(true);
//	}
//	
//	/**
//	 * Initialize labals.
//	 * 
//	 */
//	private void initLabel()
//	{
//		Dimension labelDimen = new Dimension(130, 20);
//		
//		accLabel = new JLabel();
//		accLabel.setText("752825526");
//		accLabel.setSize(labelDimen);
////		accLabel.setLocation(150, 50);
//		
//		nameLabel = new JLabel();
//		nameLabel.setText("MaDshy");
//		nameLabel.setSize(labelDimen);
////		nameLabel.setLocation(150, 70);
//		
//		initializedLabel = true;
//	}
//	
//	/**
//	 * Initialize panels.
//	 * Throw NullPointerException if userIcon of bkg is null,
//	 * of initLabel is not called.
//	 */
//	private void initPanel()
//	{
//		infoPanel = new JPanel()
//		{
//			public void paintComponent(Graphics g)
//			{
//				if (null == bkg)
//				{
//					throw new NullPointerException("The bkg is not initialized.");
//				}
//				g.drawImage(bkg, 0, 0, this);
//			}
//		};
//		infoPanel.setLayout(null);
//		setSize(bkg.getWidth(this), bkg.getHeight(this));
//		
////		accInfoPanel = new JPanel();
//////	accInfoPanel.setLayout(null);
////		accInfoPanel.setSize(infoPanel.getWidth(), infoPanel.getHeight() / 4);
//
////		JPanel iconPanel = new JPanel()
////		{
////			public void paintComponent(Graphics g)
////			{
////				if (null == userIcon)
////				{
////					throw new NullPointerException("There is no userIcon in infoPanel.");
////				}
////				setSize(userIcon.getWidth(this), userIcon.getHeight(this));	
////				g.drawImage(userIcon, 0, 0, this);
////			}
////		};
//
//		
//		accInfoPanel = new JPanel()
//		{
//			public void paintComponent(Graphics g)
//			{
//				if (null == userIcon)
//				{
//					throw new NullPointerException("There is no userIcon in infoPanel.");
//				}
//				g.drawImage(userIcon, 0, 0, this);			
//			}
//		};
//		accInfoPanel.setLayout(null);
//		accInfoPanel.setSize(infoPanel.getWidth(), infoPanel.getHeight() / 4);
//		
//
//		
//		if (!initializedLabel){
//			throw new NullPointerException("Label must be initialized.");
//		}
//
//		
//		frdInfoPanel = new JPanel();
//		frdInfoPanel.setSize(infoPanel.getWidth(), 3 * infoPanel.getHeight() / 4);
//		
//		accInfoPanel.add(accLabel);
//		accLabel.setLocation(150, 50);
//		accInfoPanel.add(nameLabel);
//		nameLabel.setLocation(150, 70);
//		infoPanel.add(accInfoPanel);
//		
//
//	}
//	
//	public static void main(String[] args)
//	{
//		LoggedUI lg = new LoggedUI();
//	}
//}

//@Override
//public void windowClosed(WindowEvent arg0) {
//	// TODO Auto-generated method stub
//	
//}
//
//@Override
//public void windowClosing(WindowEvent arg0) {
//	// TODO Auto-generated method stub
//	
//}
//
//@Override
//public void windowIconified(WindowEvent e) {
//	// TODO Auto-generated method stub
//	
//}	
