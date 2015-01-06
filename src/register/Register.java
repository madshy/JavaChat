package register;

import database.*;
import acc_info.*;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Date;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.html.ImageView;

import login.Login;
import message.Message;

@SuppressWarnings("serial")
public final class Register extends JFrame implements MouseListener, MouseMotionListener{
	Image backgroundImg;
	Container panelContainer;
	//Labels的声明
	JLabel accLabel;
	JLabel accTipLabel;
	JLabel pswFirstLabel;
	JLabel pswTipFirstLabel;
	JLabel pswSecondLabel;
	JLabel pswTipSecondLabel;
	JLabel nameLabel;//昵称标签
	JLabel nameTipLabel;
	JLabel sexLabel;
	JLabel birthLabel;
	JLabel iconLabel;//头像标签
	
	//Fields的声明
	JTextField accField;
	JTextField nameField;
	JPasswordField pswFirstField, pswSecondField;
	
	Dimension screenDimen;//屏幕
	Dimension clientDimen;//界面
	Dimension inputFieldDimen;
	Dimension labelDimen;
	Dimension tipLabelDimen;
	
	//Buttons的声明
	JButton closeButton;
	JButton minimizeButton;
	JButton regButton;
	
	JPanel sexPanel;
	JPanel birthPanel;
	JPanel userIconPanel;
	
	Image[] userIconImg = new Image[14];
	
	@SuppressWarnings("rawtypes")
	JComboBox yearComboBox, monthComboBox, dayComboBox;

//	JList userIconList;
	
	JRadioButton boyRadio;
	JRadioButton girlRadio;
	
	Point mouseBefore = null;
	Point firstLabelSite;
	
	boolean initializedDimen = false;
	boolean initializedButton = false;
	boolean regOkFlag = false;
	boolean accOkFlag = false;
	boolean pswOkFlag = false;
	boolean nameOkFlag = false;
	
	String bgImgPath = "src/image/regbkg.png";
	String iconImgPath = "src/image/icon.png";
	String minImgPath = "src/image/minimize.png";
	String closeImgPath = "src/image/close.png";
	String userIconPathComon = "src/image/user_ic_";

	public Register()
	{
		// TODO Auto-generated constructor stub
		//Initialize frame
		super();
		try{
			backgroundImg = Toolkit.getDefaultToolkit().getImage(bgImgPath);
//			System.out.println(backgroundImg.getWidth(this));
		}catch (Exception e)
		{
			System.out.println("Exception in Toolkit.getDefaultToolkit().getImage(path);");
		}
		
		try
		{
			initDimen();
			initPoint();
			initButton();
			initLabel();
			initField();
			initPanel();
			
		}catch (NullPointerException nullRefer)
		{
			System.out.println(nullRefer.toString());
		}


		setSize(clientDimen);
		setLocation((screenDimen.width - clientDimen.width) / 2,
				(screenDimen.height - clientDimen.height) / 2);
		panelContainer = getContentPane();
		addMouseMotionListener(this);
		addMouseListener(this);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
//				一开始就让光标位置处于账号输入框里面
				accField.requestFocus();
			}
		});
		setIconImage(Toolkit.getDefaultToolkit().getImage(iconImgPath));
		
		panelContainer.add(accLabel);
		panelContainer.add(pswFirstLabel);
		panelContainer.add(pswSecondLabel);
		panelContainer.add(nameLabel);
		panelContainer.add(sexLabel);
		panelContainer.add(birthLabel);
//		panelContainer.add(iconLabel);
		
		panelContainer.add(accField);
		panelContainer.add(pswFirstField);
		panelContainer.add(pswSecondField);
		panelContainer.add(nameField);
		
		panelContainer.add(sexPanel);
		panelContainer.add(birthPanel);
//		panelContainer.add(userIconPanel);
		
		panelContainer.add(accTipLabel);
		panelContainer.add(pswTipFirstLabel);
		panelContainer.add(pswTipSecondLabel);
		panelContainer.add(nameTipLabel);
		
		
		panelContainer.add(closeButton);
		panelContainer.add(minimizeButton);
		panelContainer.add(regButton);
		panelContainer.add(new MyPanel());


		setUndecorated(true);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * User-define JPanel to draw background.
	 * Throw NullPointerException if backgroundImg is null.
	 */
	class MyPanel extends JPanel
	{
		@Override
		public void paintComponent(Graphics g)
		{
			if (null == backgroundImg)
			{
				throw new NullPointerException("To call paintComponent() in class MYpanel, "
						+ "backgroundImg must not be null.");
			}
			super.paintComponent(g);
			g.drawImage(backgroundImg, 0, 0, null);

		}
	}
	
	/**
	 * Initialize all dimension members.
	 * Throw NullPointerException if backgroundImg is null.
	 */
	private void initDimen()
	{
		if (null == backgroundImg)
		{
			throw new NullPointerException("To call initDimen(),backgroundImg must not be null.");
		}
		try
		{
		Icon ic = new ImageIcon(bgImgPath);
		}catch (Exception e)
		{
			System.out.println("Exception in Icon ic = new ImageIcon(path)");
		}
		clientDimen = new Dimension(backgroundImg.getWidth(this), backgroundImg.getHeight(this));
		screenDimen = Toolkit.getDefaultToolkit().getScreenSize();

		labelDimen = new Dimension(60, 20);
		inputFieldDimen = new Dimension(130, 20);
		tipLabelDimen = new Dimension(200, 20);
		
		initializedDimen = true;
	}
	
	/**
	 * Initialize all point members.
	 * Throw NullPointerException if initDimen() is not called.
	 */
	private void initPoint()
	{
		firstLabelSite = new Point(100, 100);
	}
	
	/**
	 * Initialize all button members.
	 * Throw NullPointerException if initDimen() is not called
	 * or firstLabelSite is null.
	 */
	private void initButton()
	{
		if (!initializedDimen || null == firstLabelSite)
		{
			throw new NullPointerException("Function initDimen() must called before initButton()");
		}
		
		regButton = new JButton("注册");
//		regButton.setLocation(regButtonSite);
		regButton.setBackground(new Color(0, 170, 228));
		regButton.setSize(inputFieldDimen.width / 2, inputFieldDimen.height);
		regButton.setLocation(firstLabelSite.x + (labelDimen.width + inputFieldDimen.width + tipLabelDimen.width) / 2,
				firstLabelSite.y + 10 * labelDimen.height);
		regButton.addActionListener(new MyActionListener());
		regButton.setEnabled(false);
		
		//关闭和最小化的设置
		closeButton = new JButton(new ImageIcon(closeImgPath));
		closeButton.setBounds(clientDimen.width - closeButton.getIcon().getIconWidth(), 
				0, closeButton.getIcon().getIconWidth(), closeButton.getIcon().getIconHeight());
		closeButton.setContentAreaFilled(false);
		closeButton.setBorderPainted(false);
		closeButton.setOpaque(false);
		closeButton.addActionListener(new MyActionListener());
		closeButton.addMouseListener(this);
		
		minimizeButton = new JButton(new ImageIcon(minImgPath));
		minimizeButton.setBounds(clientDimen.width - 2 * minimizeButton.getIcon().getIconWidth(), 
				0, minimizeButton.getIcon().getIconWidth(), minimizeButton.getIcon().getIconHeight());
		minimizeButton.setContentAreaFilled(false);
		minimizeButton.setBorderPainted(false);
		minimizeButton.setOpaque(false);
		minimizeButton.addActionListener(new MyActionListener());
		minimizeButton.addMouseListener(this);
	}
	
	/**
	 * Initialize all label members.
	 * Throw NullPointerException if initDimen() is not called
	 * or firstLabelSite is null.
	 */
	private void initLabel()
	{
		if (!initializedDimen || null == firstLabelSite)
		{
			throw new NullPointerException("Function initDimen() must called before initLabel() "
					+ "and firstLabelSite cannot be null.");
		}
		Font font = new Font("宋体", Font.PLAIN, 12);
		

		accLabel = new JLabel("账号");
		accLabel.setFont(font);
		accLabel.setSize(labelDimen);
		accLabel.setLocation(firstLabelSite);
		
		pswFirstLabel = new JLabel("密码");
		pswFirstLabel.setFont(font);
		pswFirstLabel.setSize(labelDimen);
		pswFirstLabel.setLocation(firstLabelSite.x, firstLabelSite.y + labelDimen.height); 
		
		pswSecondLabel = new JLabel("确认密码");
		pswSecondLabel.setFont(font);
		pswSecondLabel.setSize(labelDimen);
		pswSecondLabel.setLocation(firstLabelSite.x, firstLabelSite.y + 2 * labelDimen.height);
		
		nameLabel = new JLabel("昵称");
		nameLabel.setFont(font);
		nameLabel.setSize(labelDimen);
		nameLabel.setLocation(firstLabelSite.x, firstLabelSite.y + 3 * labelDimen.height);
		
		sexLabel = new JLabel("性别");
		sexLabel.setFont(font);
		sexLabel.setSize(labelDimen);
		sexLabel.setLocation(firstLabelSite.x, firstLabelSite.y + 4 * labelDimen.height);
		
		birthLabel = new JLabel("生日");
		birthLabel.setFont(font);
		birthLabel.setSize(labelDimen);
		birthLabel.setLocation(firstLabelSite.x, firstLabelSite.y + 5 * labelDimen.height);
		
		iconLabel = new JLabel("头像");
		iconLabel.setFont(font);
		iconLabel.setSize(labelDimen);
		iconLabel.setLocation(firstLabelSite.x, firstLabelSite.y + 6 * labelDimen.height);
		
		accTipLabel = new JLabel("*请输入账号");
		accTipLabel.setFont(font);
		accTipLabel.setSize(tipLabelDimen);
		accTipLabel.setLocation(firstLabelSite.x + labelDimen.width + inputFieldDimen.width, firstLabelSite.y);
		
		pswTipFirstLabel = new JLabel("*请输入密码");
		pswTipFirstLabel.setFont(font);
		pswTipFirstLabel.setSize(tipLabelDimen);
		pswTipFirstLabel.setLocation(firstLabelSite.x + labelDimen.width + inputFieldDimen.width,
				firstLabelSite.y + labelDimen.height);
		
		pswTipSecondLabel = new JLabel("*请确认密码");
		pswTipSecondLabel.setFont(font);
		pswTipSecondLabel.setSize(tipLabelDimen);
		pswTipSecondLabel.setLocation(firstLabelSite.x + labelDimen.width + inputFieldDimen.width,
				firstLabelSite.y + 2 * labelDimen.height);
		
		nameTipLabel = new JLabel("*请输入昵称");
		nameTipLabel.setFont(font);
		nameTipLabel.setSize(tipLabelDimen);
		nameTipLabel.setLocation(firstLabelSite.x + labelDimen.width + inputFieldDimen.width,
				firstLabelSite.y + 3 * labelDimen.height);
	}
	
	/**
	 * Initialize all field members.
	 * Throw NullPointerException if initDimen() is not called
	 * or firstLabelSite is null.
	 */
	private void initField()
	{
		if (!initializedDimen || null == firstLabelSite)
		{
			throw new NullPointerException("Function initDimen() must called before initField() "
					+ "and firstLabelSite cannot be null.");
		}
//		Color color = new Color(0, 170, 228)
		Color color = new Color(150, 170, 228);
		accField = new JTextField();
		accField.setSize(inputFieldDimen);
		accField.setLocation(firstLabelSite.x + labelDimen.width,
				firstLabelSite.y);
		accField.setBackground(color);
		accField.addMouseListener(this);
		
		pswFirstField = new JPasswordField();
		pswFirstField.setSize(inputFieldDimen);
		pswFirstField.setLocation(firstLabelSite.x + labelDimen.width,
				firstLabelSite.y + inputFieldDimen.height);
		pswFirstField.setBackground(color);
		pswFirstField.addMouseListener(this);
		
		pswSecondField = new JPasswordField();
		pswSecondField.setSize(inputFieldDimen);
		pswSecondField.setLocation(firstLabelSite.x + labelDimen.width,
				firstLabelSite.y + 2 * inputFieldDimen.height);
		pswSecondField.setBackground(color);
		pswSecondField.addMouseListener(this);
		
		nameField = new JTextField();
		nameField.setSize(inputFieldDimen);
		nameField.setLocation(firstLabelSite.x + labelDimen.width,
				firstLabelSite.y + 3 * inputFieldDimen.height);
		nameField.setBackground(color);
		nameField.addMouseListener(this);
	}
	
	/**
	 * A assist function to judge whether user can register or not.
	 */
	private void judgeRegCondition()
	{
		regOkFlag = accOkFlag && pswOkFlag && nameOkFlag;
		if (!regOkFlag)
		{
			regButton.setEnabled(false);
		}
		else
		{
			regButton.setEnabled(true);
		}
	}
		
	/**
	 * Initialize module choosing user's sex and choosing user's birthday.
	 * Throw NullPointerException if firstLabelSite is not initialized
	 * or initDimen() is not called.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initPanel()
	{
		if (null == firstLabelSite)
		{
			throw new NullPointerException("Member firstLabel must be initialized "
					+ "before calling initChooseSex().");
		}
		Font font = new Font("宋体", Font.PLAIN, 12);
		Color color = new Color(120, 120, 120);
		
/*****************Initialize sexPanel.**************/ 
		sexPanel = new JPanel();
		sexPanel.setSize(inputFieldDimen);
		sexPanel.setLocation(firstLabelSite.x + labelDimen.width
				, firstLabelSite.y + 4 * labelDimen.height);
		sexPanel.setLayout(new GridLayout(1, 2));
		sexPanel.setOpaque(false);
		
		boyRadio = new JRadioButton("男");
		boyRadio.setFont(font);
		boyRadio.setSelected(true);
		boyRadio.setOpaque(false);
		
		girlRadio = new JRadioButton("女");
		girlRadio.setFont(font);
		girlRadio.setOpaque(false);
		
		ButtonGroup sexGroup = new ButtonGroup();
		sexGroup.add(boyRadio);
		sexGroup.add(girlRadio);
		
		sexPanel.add(boyRadio);
		sexPanel.add(girlRadio);
		
/***********Initialize birthPanel*************/
		birthPanel = new JPanel();
		birthPanel.setLayout(null);
		birthPanel.setLocation(firstLabelSite.x + labelDimen.width
				, firstLabelSite.y + 5 * labelDimen.height);
		birthPanel.setSize(inputFieldDimen);
		birthPanel.setBackground(color);
		
		//Get now time information.
		Date time = new Date(new java.util.Date().getTime());
		String timeStr[] = time.toString().split("-");
		
		//Initialize ComboBoxs according to information got just now.
		Integer year[] = new Integer[100];
		Integer month[] = new Integer[12];
		Vector<Integer> day = new Vector<Integer>(28, 1);//because it must can be changed.		
		for (int i = 0; i < 100; ++i)//initialize yearComboBox
		{
			year[i] = Integer.parseInt(timeStr[0]) - i;
		}
		for (int i = 0; i < 12; ++ i)//
		{
			month[i] = i + 1; 
		}
		for (int i = 0; i < 28; ++ i)//
		{
			day.add(i + 1);
		}
				
		yearComboBox = new JComboBox(year);
		yearComboBox.setSize(50, 20);
		yearComboBox.setLocation(0, 0);
		yearComboBox.setFont(font);
		yearComboBox.setBackground(color);
		
		monthComboBox = new JComboBox(month);
		monthComboBox.setSize(40, 20);
		monthComboBox.setFont(font);
		monthComboBox.setLocation(yearComboBox.getSize().width, 0);
		monthComboBox.setBackground(color);
		
		dayComboBox = new JComboBox(day);
		dayComboBox.setSize(40, 20);
		dayComboBox.setFont(font);
		dayComboBox.setLocation(yearComboBox.getSize().width + monthComboBox.getSize().width, 0);
		dayComboBox.setBackground(color);
		dayComboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e)
			{
				int y = ((Integer)yearComboBox.getSelectedItem()).intValue();
				int m = ((Integer)monthComboBox.getSelectedItem()).intValue();
				
				if (4 == m || 6 == m || 9 == m || 11 == m)//months having 30 days.
				{
					if (day.size() == 31)
					{
						day.setSize(30);
					}
					else if (day.size() != 30)
					{
						for (int i = day.size(); day.size() != 30; ++ i)
						{
							day.add(i + 1);
						}
					}
				}
				else if (2 == m)//需要考虑平年或者闰年
				{
					if ((y % 4 == 0 && y % 100 != 0) || y % 400 == 0)
					{
						day.setSize(28);
					}
					else
					{
						if (day.size() == 28)
						{
							day.add(29);
						}
						else
						{
							day.setSize(29);
						}
					}
				}
				else//months having 31 days.
				{
					for (int i = day.size(); day.size() != 31; ++ i)
					{
						day.add(i + 1);
					}
				}
			}
		});
		
		birthPanel.add(yearComboBox);
		birthPanel.add(monthComboBox);
		birthPanel.add(dayComboBox);
		
/***************Initialize userIconPanel*******************/
//		userIconPanel = new JPanel();
//		for (int i = 1; i <= userIconImg.length; ++ i)
//		{
//			userIconImg[i - 1] = Toolkit.getDefaultToolkit().getImage(userIconPathComon + i + ".png");
//		}
//		userIconComboBox = new JComboBox(userIconImg);
//		userIconComboBox.setLocation(firstLabelSite.x + labelDimen.width
//				, firstLabelSite.y + 6 * labelDimen.height);

	}
	
	public static void main(String[] args)
	{
		Register reg = new Register();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		//Move the client window.
		if (e.getSource() == this)
		{
			if (null == mouseBefore)
			{
				mouseBefore = e.getLocationOnScreen();
				return ;
			}
			Point clientBefore = getLocation();
			Point mouseNow = e.getLocationOnScreen();
			int offsetX = mouseNow.x - mouseBefore.x;
			int offsetY = mouseNow.y - mouseBefore.y;
			setLocation(clientBefore.x + offsetX, clientBefore.y + offsetY);
			mouseBefore = mouseNow;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

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
		
		if (e.getSource() == accField)
		{
			Color colorFore = accTipLabel.getForeground();
			System.out.println(colorFore);
			accTipLabel.setForeground(new Color(51, 51, 51));
			accTipLabel.setText("*请输入账号");
		}
		if (e.getSource() == pswFirstField)
		{
			pswTipFirstLabel.setForeground(new Color(51, 51, 51));
			pswTipFirstLabel.setText("*请输入密码");
		}
		if (e.getSource() == pswSecondField)
		{
			pswTipSecondLabel.setForeground(new Color(51, 51, 51));
			pswTipSecondLabel.setText("*请确认密码");
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
		
		//鼠标移出域时对文本域和密码域进行实时检测
		if (e.getSource() == accField)
		{
			//输入数据的合法性检测
//			账号至少为六为数字，最多为12位
//			并且第一个数字不可以为0
			String pattern = "[1-9][0-9]{5,11}";
			Scanner scanner = new Scanner(accField.getText().toString());
			if (!scanner.hasNext(pattern))
			{
				accTipLabel.setText("*第一位不能为零且个数大于6小于13");
				accTipLabel.setForeground(Color.RED);
				accField.requestFocus(true);
				accOkFlag = false;//必须有，不然后面对的改成错的就尴尬了。
			}
			else
			{
				accTipLabel.setForeground(new Color(51, 51, 51));
				accTipLabel.setText("*OK");
				accOkFlag = true;
			}
			scanner.close();
			judgeRegCondition();
		}
		if (e.getSource() == pswFirstField)
		{
			//密码长度为6-16位,不能包含空白字符
			String pattern = "\\w{6,16}";
			Scanner scanner = new Scanner(new String(pswFirstField.getPassword()));
			if (!scanner.hasNext(pattern))
			{
				pswTipFirstLabel.setText("*密码长度为6-16位,且没有空格");
				pswTipFirstLabel.setForeground(Color.RED);
				pswTipFirstLabel.requestFocus(true);
				pswOkFlag = false;
			}
			else
			{
				pswTipFirstLabel.setForeground(new Color(51, 51, 51));
				pswTipFirstLabel.setText("*OK");
			}
			scanner.close();
			judgeRegCondition();
		}
		if (e.getSource() == pswSecondField)
		{
			//不能用getPassword().toString().
			//不方便直接使用getPassword().
			String first = new String(pswFirstField.getPassword());
			String second = new String(pswSecondField.getPassword());
			if (!first.equals(second))
			{
				pswTipSecondLabel.setText("*两次输入不一致");
				pswTipSecondLabel.setForeground(Color.RED);
				pswTipSecondLabel.requestFocus(true);
				pswOkFlag = false;
			}
			else if (first.length() != 0)
			{
				pswTipSecondLabel.setForeground(new Color(51, 51, 51));
				pswTipSecondLabel.setText("*OK");
				pswOkFlag = true;
				
			}
			judgeRegCondition();
		}
		if (e.getSource() == nameField)
		{
			if (nameField.getText().length() > 8)
			{
				nameTipLabel.setText("*昵称长度不能大于8");
				nameTipLabel.setForeground(Color.RED);
				nameTipLabel.requestFocus(true);
				nameOkFlag = false;
			}
			else if (0 == nameField.getText().length())
			{
				nameTipLabel.setText("*昵称不能为空");
				nameTipLabel.setForeground(Color.RED);
				nameTipLabel.requestFocus(true);
				nameOkFlag = false;
			}
			else
			{
				nameTipLabel.setForeground(new Color(51, 51, 51));
				nameTipLabel.setText("*OK");
				nameOkFlag = true;
			}
			judgeRegCondition();
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
	
	class MyActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource() == nameField)
			{
//				去掉昵称中的空格，但是内容不为空，否则会自动throw NullPointerException
				if (null != nameField.getText())
				{
					nameField.getText().trim();
				}
			}
			//点击关闭按钮
			if (e.getSource() == closeButton)
			{
				System.exit(0);
			}
			//点击最小化按钮
			if (e.getSource() == minimizeButton)
			{
				setExtendedState(JFrame.ICONIFIED);
			}
//			点击注册按钮
			if (e.getSource() == regButton)
			{
				Message msg = new Message();
				Account acc = new Account();
				acc.setAccount(accField.getText().toString());
				acc.setPsw(new String(pswFirstField.getPassword()));
				acc.setName(nameField.getText());
				acc.setSex(boyRadio.isSelected());
				acc.setBirthday(((Integer)yearComboBox.getSelectedItem()).toString() + "-"
						+ ((Integer)monthComboBox.getSelectedItem()).toString() + "-"
						+ ((Integer)dayComboBox.getSelectedItem()).toString());
				msg.setType(Message.Type.REGISTER);
				msg.setContent(acc);
				
				try{
					Socket socket = new Socket("localhost", 9999);
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
					oos.writeObject(msg);
					Message receiveMsg = (Message)ois.readObject();
					
					switch(receiveMsg.getType())
					{
						case Message.Type.REGISTERFAIL:
							JOptionPane.showMessageDialog(Register.this, "注册失败");
							socket.close();
							return ;
						
						default:
							System.out.println("注册成功");
							new Login(acc);
							Register.this.setVisible(false);
					}
				}catch (Exception ex)
				{
					ex.printStackTrace();
					JOptionPane.showMessageDialog(Register.this, "网络连接异常");
					System.exit(-1);
				}
			}
		}

	}
}
