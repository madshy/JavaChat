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
		System.out.println("启动了聊天的线程");
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
				
				/*分信息进行处理*/
				switch (msg.getType()) {
					case Message.Type.LOGIN://登录
						this.login(msg);
						break;
	
					case Message.Type.REGISTER://注册
						this.register(msg);
						break;
						
					case Message.Type.MESSAGE://普通聊天信息
						this.message(msg);
						break;
						
					case Message.Type.CHAT://打开聊天界面
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
						
					case Message.Type.UNCHAT://关闭聊天界面
						System.out.println("查询关闭的界面");
						for (ChatThread cht : server.getClients())
						{
							if (msg.getSender().getAccount().equals(cht.account.getAccount()))
							{		
								System.out.println("size:" + cht.chat.size());
/**
 * 使用这种for循环应该特别注意，当容器为空时会卡死出异常
 * 所以这里的则不会运行到第二个for循环的外面，具体还需要测试
 * 但是如果使用的是原始遍历形式则不会出问题
 * 所以这里问题就出在本来就只有一个，但是被remove掉了，然后就没有了
 * 猜测是这样的。
 */
								for (ChatUI chu : cht.chat)
								{
									if (chu._own.getAccount().equals(msg.getSender().getAccount()) 
											&& chu._frd.getAccount().equals(msg.getReceiver().getAccount()))
									{
										cht.chat.remove(chu);
										Message sendMsg = new Message();
										sendMsg.setType(Message.Type.CLOSEOK);
										System.out.println("发送结果信息");
										oos.writeObject(sendMsg);
										System.out.println("发送完毕");										
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
	 * 处理登录信息
	 * @param msg
	 * @throws Exception
	 */
	private void login(Message msg) throws Exception{
		System.out.println("准备登录，连接数据库ing");
		try{
			db.connect();
		}catch(Exception excpt){
			System.out.println("连接数据库出错");
			excpt.printStackTrace();
		}
		System.out.println("连接成功");
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
					System.out.println("密码正确,获取账号中...");
					Account account = new Account();
					account.setAccount(rst.getString("account"));
					account.setPsw(psw);
					account.setName(rst.getString("name"));
					account.setSex(rst.getBoolean("sex"));
					account.setBirthday(rst.getString("birthday"));
					
					//查询朋友账号
					ResultSet rstFrdAcc = db.query("select frd_acc from friend where account = '" + account.getAccount() + "'");
					try{
						//查询朋友具体信息
						while (rstFrdAcc.next()){
							ResultSet rstFrd = db.query("select * from account where account = '" + rstFrdAcc.getString(1) + "'");
							try{
								while (rstFrd.next()) {
									//这里的friend不能放到外面定义，由于java是引用型的，所以每次引用一个对象。
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
					/*将该线程放入clients的集合中*/
					server.getClients().add(this);
					/*将当前账号添加到userlist中*/
					server.getUserList().add(this.account);
					sendMsg.setType(Message.Type.USERLIST);
					sendMsg.setContent(account);
					System.out.println("准备发送信息...");
					this.sendMessage(sendMsg);
					System.out.println("发送信息成功...");
					
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
	 * 处理注册信息
	 * @param msg
	 * @throws Exception
	 */
	private void register(Message msg) throws Exception{
		System.out.println("注册ing,准备连接数据库");
		try{
			db.connect();
			System.out.println("数据库连接成功");
		}catch(Exception excpt){
			System.out.println("数据库连接失败");
		}

		Account account = (Account)msg.getContent();
		String acc = account.getAccount();
		String psw = account.getPsw();
		String name = account.getName();
		String birth = account.getBirthday();
		char sex = account.getSex() ? 'M' : 'F';
//		System.out.println("acc\tpsw\tname\tbirth\tsex");
//		System.out.println(acc + "\t" + psw + "\t" + name + "\t" + birth + "\t" + sex);
		System.out.println("准备把数据写入数据库中");
//		db.update("insert into account(account,password,name,sex,birthday) "
//				+ "values(" + acc + "," + psw + "," + name + "," + sex + "," + birth + ")");
		db.update("insert into account(account,password,name,sex,birthday) "
				+ "values('" + acc + "','" + psw + "','" + name + "','" + sex + "','" + birth + "')");
		System.out.println("成功写入数据库,准备返回信息给客户端");
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
		System.out.println("发送完毕");
		
/*************************添加好友**************************/
		//注意：对应的数据库需要两个关键字，不然就无法添加第二个好友
		ResultSet rst = db.query("select * from account where account <> '" + acc + "'");
		try
		{
			while (rst.next())
			{
				//这里的两个String同样无法放到循环外面，等到有时间的时候可以测试一下为什么。
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
	 * 处理下线信息
	 */
	private void logout()
	{
		
	}
	
	/**
	 * 处理普通的聊天信息
	 * @param msg
	 * @throws Exception
	 */
	private void message(Message msg) throws  Exception{
		boolean isExist = false;
		//判断接受者是否在线，如果不在线则无法发送信息
		for (ChatThread chatThread : server.getClients()){
			if (chatThread.account.getAccount().equals(msg.getReceiver().getAccount()))
			{
				isExist = true;
				for (ChatUI iterator : chat)
				{
					//聊天界面已经存在，直接处理信息
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
	 * 把信息分发给各个客户端
	 * @param msg 消息内容
	 * @param to 目标客户端
	 * @throws Exception
	 */
	private void sendMessage(Message msg) throws Exception{
//		System.out.println("Account:" + ((Account)msg.getContent()).getAccount());
		for (ChatThread ct : server.getClients()){
			/*如果没有这个，记得是出现异常的，但是还是不知道为什么*/
			if (ct.account.getAccount().equals(((Account)msg.getContent()).getAccount()))
			{
				System.out.println("发送信息...");
				try{
					ct.oos.writeObject(msg);
					System.out.println("发送完毕");
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}	
		}
	}
}
