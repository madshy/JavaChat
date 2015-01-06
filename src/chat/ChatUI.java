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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;

import logged_ui.LoggedUI;

public final class ChatUI extends JFrame {
	Info _own, _frd;
	JScrollPane chatLog;//�����¼
	JTextArea msg;
	int site = 0;//��ʾ��ǰ��¼����λ��
	
	Point mouseBefore = null;//For moving window.
	
	public ChatUI(Info own, Info frd)
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
//		setVisible(true);//�����������������������Ҫ����ƶ��ſ�����ʾ
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
		
		//���������������������������Ҫ����Ϊ������ȡ��ǰĬ������Ŀ��
		//��Ҫ�������������û��һ���ܺõļ���ֵ�������п���ֲ��
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
//				һ��ʼ���ù��λ�ô�����Ϣ���������
				msg.requestFocus();
			}
		});
		
		JLabel name = new JLabel(frd.getName());
		ChatterInfo frdInfo = new ChatterInfo(name);
		frdInfo.setLocation(0, 0);
		frdInfo.setOpaque(false);
		ct.add(frdInfo);
		
		final JPanel content = new JPanel();
		content.setOpaque(false);
		content.setLayout(null);
		content.setVisible(false);
		chatLog = new JScrollPane(content);
		chatLog.setAutoscrolls(true);
		chatLog.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		chatLog.setBounds(0, frdInfo.getHeight(), 400, (int)jp.getLocation().getY() - jp.getHeight());
		ct.add(chatLog);
		chatLog.setVisible(true);
		
		JButton send = new JButton("����");
		send.setSize(60, 20);
		send.setBorderPainted(false);
		send.setBackground(new Color(0, 170, 228));
		ct.add(send);
		send.setLocation(getWidth() - send.getWidth(), getHeight() - send.getHeight());
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (null != msg)
				{
					JLabel message = new JLabel("");
					int max = -1;
					String text = "<html>";
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
						if (0 == i)
						{
							text = text + part + "<br>";
						}else if (i != msg.getLineCount() - 1)
						{
							text = text + part + "<br>";
						}else
						{
							text = text + part + "<html>";
						}
					}
					message.setText(text);
					FontMetrics fm = message.getFontMetrics(message.getFont());				
					message.setBackground(Color.BLUE);
					content.invalidate();
					content.add(message);
					message.setBounds(0, site, max * fm.getMaxAdvance(), msg.getLineCount() * fm.getHeight());
					site += message.getSize().getHeight();
					content.validate();
					content.setVisible(true);
					msg.setText("");
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
	 * ����������Ϣ
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
}



