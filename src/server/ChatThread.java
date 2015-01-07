package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import chat.ChatUI;
import database.DataBase;
import message.Message;
import acc_info.Account;
import acc_info.Friend;
import acc_info.Info;

public class ChatThread extends Thread{
	private Socket socket = null;
	private Server server;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	public Info account = null;
	private boolean run = true;
	DataBase db = new DataBase();
	public ArrayList<ChatUI> chat = new ArrayList<ChatUI>();
	
	public ChatThread(Socket socket, Server server) throws IOException
	{
		System.out.println("������������߳�");
		this.socket = socket;
		this.server = server;
		
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
	}
	
	public void run()
	{
		try{
			while (run)
			{
				Message msg = (Message)ois.readObject();
				
				/*����Ϣ���д���*/
				switch (msg.getType()) {
					case Message.Type.LOGIN://��¼
						this.login(msg);
						break;
	
					case Message.Type.REGISTER://ע��
						this.register(msg);
						break;
						
					case Message.Type.MESSAGE://��ͨ������Ϣ
						this.message(msg);
						break;
						
					case Message.Type.CHAT://���������
						boolean isHave = false;
						for (ChatThread cht : server.getClients())
						{
							if (msg.getSender().getAccount().equals(cht.account.getAccount()))
							{
								for (ChatUI chu : cht.chat)
								{
									if (chu._own.getAccount().equals(msg.getSender().getAccount()) 
											&& chu._frd.getAccount().equals(msg.getReceiver().getAccount()))
									{
										isHave = true;
										chu.setVisible(true);
										return ;
									}
								}
								if (!isHave)
								{
									cht.chat.add(new ChatUI(msg.getSender(), msg.getReceiver()));
								}
								return;
							}
						}
						break;
						
					case Message.Type.UNCHAT://�ر��������
						System.out.println("��ѯ�رյĽ���");
						for (ChatThread cht : server.getClients())
						{
							if (msg.getSender().getAccount().equals(cht.account.getAccount()))
							{		
								System.out.println("size:" + cht.chat.size());
/**
 * ʹ������forѭ��Ӧ���ر�ע�⣬������Ϊ��ʱ�Ῠ�����쳣
 * ����������򲻻����е��ڶ���forѭ�������棬���廹��Ҫ����
 * �������ʹ�õ���ԭʼ������ʽ�򲻻������
 * ������������ͳ��ڱ�����ֻ��һ�������Ǳ�remove���ˣ�Ȼ���û����
 * �²��������ġ�
 */
								for (ChatUI chu : cht.chat)
								{
									if (chu._own.getAccount().equals(msg.getSender().getAccount()) 
											&& chu._frd.getAccount().equals(msg.getReceiver().getAccount()))
									{
										cht.chat.remove(chu);
										Message sendMsg = new Message();
										sendMsg.setType(Message.Type.CLOSEOK);
										System.out.println("���ͽ����Ϣ");
										oos.writeObject(sendMsg);
										System.out.println("�������");										
									}
								}
							}
						}
						break;
				}
			}
		}catch (Exception ex)
		{
			this.logout();
		}
	}
	
	/**
	 * �����¼��Ϣ
	 * @param msg
	 * @throws Exception
	 */
	private void login(Message msg) throws Exception{
		System.out.println("׼����¼���������ݿ�ing");
		try{
			db.connect();
		}catch(Exception excpt){
			System.out.println("�������ݿ����");
			excpt.printStackTrace();
		}
		System.out.println("���ӳɹ�");
		String acc = ((Account)msg.getContent()).getAccount();
		ResultSet rst = db.query("select * from account where account = '" + acc + "'");
		String psw = null;
		Message sendMsg = new Message();
		try {
			if(rst.next())
			{
				psw = rst.getString("password");
				if (psw.equals(new String(((Account)msg.getContent()).getPsw())))
				{
					System.out.println("������ȷ,��ȡ�˺���...");
					Account account = new Account();
					account.setAccount(rst.getString("account"));
					account.setPsw(psw);
					account.setName(rst.getString("name"));
					account.setSex(rst.getBoolean("sex"));
					account.setBirthday(rst.getString("birthday"));
					
					//��ѯ�����˺�
					ResultSet rstFrdAcc = db.query("select frd_acc from friend where account = '" + account.getAccount() + "'");
					try{
						//��ѯ���Ѿ�����Ϣ
						while (rstFrdAcc.next()){
							ResultSet rstFrd = db.query("select * from account where account = '" + rstFrdAcc.getString(1) + "'");
							try{
								while (rstFrd.next()) {
									//�����friend���ܷŵ����涨�壬����java�������͵ģ�����ÿ������һ������
									Friend friend = new Friend();
									friend.setAccount(rstFrd.getString("account"));		
									friend.setName(rstFrd.getString("name"));
									friend.setBirthday(rstFrd.getString("birthday"));
									friend.setSex((rstFrd.getString("sex")).equals("M")? true : false);
									account.addFriend(friend);
								}
							}catch (SQLException e)
							{
								e.printStackTrace();
							}
						}
					}catch (SQLException e)
					{
						e.printStackTrace();
					}
					this.account = account;
					/*�����̷߳���clients�ļ�����*/
					server.getClients().add(this);
					/*����ǰ�˺���ӵ�userlist��*/
					server.getUserList().add(this.account);
					sendMsg.setType(Message.Type.USERLIST);
					sendMsg.setContent(account);
					System.out.println("׼��������Ϣ...");
					this.sendMessage(sendMsg);
					System.out.println("������Ϣ�ɹ�...");
					
				}
				else
				{
					sendMsg.setType(Message.Type.WRONGPSW);
					oos.writeObject(sendMsg);
					run = false;
					socket.close();
					return ;
				}
			}
			else
			{
				sendMsg.setType(Message.Type.NOACCOUNT);
				oos.writeObject(sendMsg);
				run = false;
				socket.close();
				
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * ����ע����Ϣ
	 * @param msg
	 * @throws Exception
	 */
	private void register(Message msg) throws Exception{
		System.out.println("ע��ing,׼���������ݿ�");
		try{
			db.connect();
			System.out.println("���ݿ����ӳɹ�");
		}catch(Exception excpt){
			System.out.println("���ݿ�����ʧ��");
		}

		Account account = (Account)msg.getContent();
		String acc = account.getAccount();
		String psw = account.getPsw();
		String name = account.getName();
		String birth = account.getBirthday();
		char sex = account.getSex() ? 'M' : 'F';
//		System.out.println("acc\tpsw\tname\tbirth\tsex");
//		System.out.println(acc + "\t" + psw + "\t" + name + "\t" + birth + "\t" + sex);
		System.out.println("׼��������д�����ݿ���");
//		db.update("insert into account(account,password,name,sex,birthday) "
//				+ "values(" + acc + "," + psw + "," + name + "," + sex + "," + birth + ")");
		db.update("insert into account(account,password,name,sex,birthday) "
				+ "values('" + acc + "','" + psw + "','" + name + "','" + sex + "','" + birth + "')");
		System.out.println("�ɹ�д�����ݿ�,׼��������Ϣ���ͻ���");
		Message sendMsg = new Message();		
		sendMsg.setType(Message.Type.REGISTER);
//		this.sendMessage(sendMsg);
//		try{
//			oos.writeObject(sendMsg);
//		}catch(Exception e)
//		{
//			e.printStackTrace();
//		}
		oos.writeObject(sendMsg);
		System.out.println("�������");
		
/*************************��Ӻ���**************************/
		//ע�⣺��Ӧ�����ݿ���Ҫ�����ؼ��֣���Ȼ���޷���ӵڶ�������
		ResultSet rst = db.query("select * from account where account <> '" + acc + "'");
		try
		{
			while (rst.next())
			{
				//���������Stringͬ���޷��ŵ�ѭ�����棬�ȵ���ʱ���ʱ����Բ���һ��Ϊʲô��
				String frdAcc = rst.getString("account");
				String frdName = rst.getString("name");
				db.update("insert into friend(account, frd_acc, name) values('"
						+ acc + "','" + frdAcc + "','" + frdName + "')");
				db.update("insert into friend(account, frd_acc, name) values('"
						+ frdAcc + "','" + acc + "','" + name + "')");
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
}
	
	/**
	 * ����������Ϣ
	 */
	private void logout()
	{
		
	}
	
	/**
	 * ������ͨ��������Ϣ
	 * @param msg
	 * @throws Exception
	 */
	private void message(Message msg) throws  Exception{
		boolean isExist = false;
		//�жϽ������Ƿ����ߣ�������������޷�������Ϣ
		for (ChatThread chatThread : server.getClients()){
			if (chatThread.account.getAccount().equals(msg.getReceiver().getAccount()))
			{
				isExist = true;
				for (ChatUI iterator : chat)
				{
					//��������Ѿ����ڣ�ֱ�Ӵ�����Ϣ
					if (iterator._own.equals(msg.getSender())
							&& iterator._frd.equals(msg.getReceiver()))
					{
						for (ChatUI inItr : chat)
						{
							Message sendMessage = new Message();
							sendMessage.setType(Message.Type.ONLINE);
							sendMessage.setContent(msg.getContent());
							sendMessage.setSender(msg.getSender());
							sendMessage.setReceiver(msg.getReceiver());
							System.out.println((String)msg.getContent());
							oos.writeObject(sendMessage);
							return;
						}
					}
				}
				chat.add(new ChatUI(msg.getSender(), msg.getReceiver()));
				chatThread.chat.add(new ChatUI(msg.getReceiver(), msg.getSender()));
				Message sendMessage = new Message();
				sendMessage.setType(Message.Type.ONLINE);
				oos.writeObject(sendMessage);
			}
		}
		if (!isExist)
		{
			Message sendMsg = new Message();
			sendMsg.setType(Message.Type.OFFLINE);
			oos.writeObject(sendMsg);
		}
	}
	
	/**
	 * ����Ϣ�ַ��������ͻ���
	 * @param msg ��Ϣ����
	 * @param to Ŀ��ͻ���
	 * @throws Exception
	 */
	private void sendMessage(Message msg) throws Exception{
//		System.out.println("Account:" + ((Account)msg.getContent()).getAccount());
		for (ChatThread ct : server.getClients()){
			/*���û��������ǵ��ǳ����쳣�ģ����ǻ��ǲ�֪��Ϊʲô*/
			if (ct.account.getAccount().equals(((Account)msg.getContent()).getAccount()))
			{
				System.out.println("������Ϣ...");
				try{
					ct.oos.writeObject(msg);
					System.out.println("�������");
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}	
		}
	}
}
