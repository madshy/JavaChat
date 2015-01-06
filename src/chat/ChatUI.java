package chat;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import logged_ui.LoggedUI;

public final class ChatUI extends JFrame {
	Account _own, _frd;
	JScrollPane chatLog;//聊天记录
	JTextArea msg;
	
	Point mouseBefore = null;//For moving window.
	
	public ChatUI(Account own, Account frd)
	{
		if (null == own || null == frd)
		{
			throw new NullPointerException("AccountInfo own and frd cannot be null.");
		}
		_own = own;
		_frd = frd;
		/*Paths for all images.*/
		String bkgImgPath = "src/image/chat_bkg.png";
		String minImgPath = "src/image/minimize.png";
		String closeImgPath = "src/image/close.png";
		String iconImgPath = "src/image/icon.png";
		
		/*Data for initializing ui.*/
		final Image bkg = Toolkit.getDefaultToolkit().getImage(bkgImgPath);
		Icon ic = new ImageIcon(bkgImgPath);
		Dimension uiDimen = new Dimension(bkg.getWidth(this), bkg.getHeight(this));
		Dimension screenDimen = Toolkit.getDefaultToolkit().getScreenSize();
		
		setSize(uiDimen);
		setLocation((screenDimen.width - uiDimen.width) / 2, (screenDimen.height - uiDimen.height) / 2);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(iconImgPath));
		setUndecorated(true);
//		setVisible(true);//如果有了这个，则后面的组件需要光标移动才可以显示
//		setLayout(null);
		
		//Setting for moving window
		addMouseListener(new MouseAdapter() {
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
				if (e.getSource() == ChatUI.this)
				{
					if (null == mouseBefore)
					{
						mouseBefore = e.getLocationOnScreen();
						return ;
					}
					Point clientBefore = ChatUI.this.getLocation();
					Point mouseNow = e.getLocationOnScreen();
					int offsetX = mouseNow.x - mouseBefore.x;
					int offsetY = mouseNow.y - mouseBefore.y;
					ChatUI.this.setLocation(clientBefore.x + offsetX, clientBefore.y + offsetY);
					mouseBefore = mouseNow;
				}
			}
		});
		
		Container ct = getContentPane();
//		ct.setLayout(null);
					
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
				ChatUI.this.setExtendedState(JFrame.ICONIFIED);
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
		
		//行数和列数，这里的列数不满足要求，因为不会求取当前默认字体的宽度
		//主要是这里的列数并没有一个很好的计算值，不具有可移植性
		msg = new JTextArea(5, 45);
		msg.setLineWrap(true);
		msg.setOpaque(false);
		JPanel jp = new JPanel();
		JScrollPane jsp = new JScrollPane(msg);	
		jp.add(jsp);
		ct.add(jp);
		jp.setBounds(0, getHeight() - 120 - msg.getHeight(), ChatUI.this.getWidth(), 100);
		jp.setOpaque(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
//				一开始就让光标位置处于信息输入框里面
				msg.requestFocus();
			}
		});
		
		JLabel name = new JLabel(frd.getName());
		ChatterInfo frdInfo = new ChatterInfo(name);
		frdInfo.setLocation(0, 0);
		frdInfo.setOpaque(false);
		ct.add(frdInfo);
		
		JButton send = new JButton("发送");
		send.setSize(60, 20);
		send.setBorderPainted(false);
		send.setBackground(new Color(0, 170, 228));
		ct.add(send);
		send.setLocation(getWidth() - send.getWidth(), getHeight() - send.getHeight());
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
				setBounds(0, 0, bkg.getWidth(this), bkg.getHeight(this));
				g.drawImage(bkg, 0, 0, this);
				ChatUI.this.setVisible(true);
			}
		});
	}

	/**
	 * 聊天对象的信息
	 * @author shy
	 *
	 */
	class ChatterInfo extends JPanel{
		JLabel _name;
		
		public ChatterInfo(JLabel name) {
			if (null == name)
			{
				throw new NullPointerException("Parameters icon and name cannot be null.");
			}

			_name = name;
//			setLayout(null);
			setSize(ChatUI.this.getWidth(), 60);
//			_name.setSize(100, 20);
//			add(_name);
//			_name.setLocation(_icon.getWidth(this) + 5, (getHeight() - _name.getHeight()) / 2);
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawString(_name.getText(), 60, (getHeight() - _name.getHeight()) / 2);
		}
	}
	
//	public static void main(String[] args)
//	{
////		Account acc = new Account("752825526", "madshy1994", "madshy", true, "1994-05-09");
//		Account acc = new Account();
//		acc.setAccount("7528255256");
//		acc.setPsw("madshy1994");
//		acc.setName("madshy");
//		acc.setSex(true);
//		acc.setBirthday("1994-05-09");
//		
//		Account frd = acc;
////		Account frd = new Account("2624501247", "madshy1994", "madshy", true, "1994-05-09");
//		ChatUI chu = new ChatUI(acc, frd);
//		chu.setVisible(true);
//	}
}



