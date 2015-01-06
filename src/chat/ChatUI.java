package chat;

import acc_info.*;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.xml.ws.handler.MessageContext.Scope;

import server.Server;
import logged_ui.LoggedUI;
import message.Message;

public final class ChatUI extends JFrame{
	public Info _own, _frd;
	JScrollPane chatLog;//聊天记录
	JTextArea msg;
	int site = 0;//表示当前记录到达位置
	int max = -1;
	
	JPanel content = new JPanel();
	
	private Socket socket = null;
	public ObjectOutputStream oos = null;
	public ObjectInputStream ois = null;
	
	private Receiver receiver = new Receiver();
	
	Point mouseBefore = null;//For moving window.
	
	public ChatUI(Info own, Info frd)
	{
		if (null == own || null == frd)
		{
			throw new NullPointerException("Account Info own and frd cannot be null.");
		}
		_own = own;
		_frd = frd;
		
		try {
			socket = new Socket("localhost", 9999);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
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
		jp.setBounds(0, getHeight() - 120 - msg.getHeight(), getWidth(), 100);
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
		
		content.setOpaque(false);
		content.setLayout(null);
		content.setVisible(false);
		chatLog = new JScrollPane(content);
		chatLog.setAutoscrolls(true);
		chatLog.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		chatLog.setBounds(0, frdInfo.getHeight(), 400, (int)(jp.getLocation().getY() - jp.getHeight()));
		ct.add(chatLog);
		chatLog.setVisible(true);
		
		JButton send = new JButton("发送");
		send.setSize(60, 20);
		send.setBorderPainted(false);
		send.setBackground(new Color(0, 170, 228));
		ct.add(send);
		send.setLocation(getWidth() - send.getWidth(), getHeight() - send.getHeight());
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (!msg.getText().equals(""))
				{					
					ChatContent cc = new ChatContent();
					for (int i = 0; i < msg.getLineCount(); ++ i)
					{
						String part = null;
						try {
							int len = msg.getLineEndOffset(i) - msg.getLineStartOffset(i);
							part = msg.getText(msg.getLineStartOffset(i), len);
							max = max > len ? max : len;
						} catch (BadLocationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (i != msg.getLineCount() - 1)
						{
							cc.append(part);
						}
						else
						{
							cc.appendAndCommit(part);
						}
					}
					Message sendMsg = new Message();
					sendMsg.setSender(_own);
					sendMsg.setReceiver(_frd);
					sendMsg.setType(Message.Type.MESSAGE);
					sendMsg.setContent(cc.getContent());
					
					try{
						oos.writeObject(sendMsg);
					}catch (Exception exception)
					{
						exception.printStackTrace();
					}
					receiver.start();
					
				}else{
					JOptionPane.showMessageDialog(ChatUI.this, "发送内容不能为空");
				}
			}
		});
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
		setVisible(true);
		
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
			g.drawString(_name.getText(), 5, (getHeight() - _name.getHeight()) / 2);
		}
	}

	class Receiver extends Thread{
		@Override
		public void run()
		{
			try{
				while (true)
				{
					Message receMsg = (Message)ChatUI.this.ois.readObject();
					switch(receMsg.getType())
					{
					case Message.Type.ONLINE:
						String text = (String)receMsg.getContent();
						JLabel message = new JLabel("我说:" + text);
						FontMetrics fm = message.getFontMetrics(message.getFont());				
						message.setBackground(Color.BLUE);
						content.invalidate();
						content.add(message);
						message.setBounds(0, site, max * fm.getMaxAdvance(), msg.getLineCount() * fm.getHeight());
						site += message.getSize().getHeight();
						content.validate();
						content.setVisible(true);
						msg.setText("");
						break;
						
					default:
						JOptionPane.showMessageDialog(ChatUI.this, "不能发信息给离线好友");
					}
				}
			}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public class ChatContent implements Serializable{
		String content = "<html>";
		public void append(String s)
		{
			content = content + s + "<br>";
		}
		public void appendAndCommit(String s)
		{
			content = content + s + "<html>";
		}
		public String getContent()
		{
			return content;
		}
	}
}



