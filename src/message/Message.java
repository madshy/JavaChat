package message;

import java.io.Serializable;

import acc_info.Info;

public class Message implements Serializable {
	/**
	 * All kinds of message.
	 * <br/>These messages are used to communicate between client and server.
	 */
	public static final class Type{
		public static final int LOGIN = 0;
		public static final int REGISTER = 1;
		public static final int LOGINFAIL = 2;
		public static final int REGISTERFAIL = 3;
		/**该账号不存在**/
		public static final int NOACCOUNT = 4;
		/**密码错误**/
		public static final int WRONGPSW = 5;
		/**
		 * 普通信息
		 */
		public static final int MESSAGE = 6;
		public static final int LOGOUT = 7;
		public static final int USERLIST = 8;
		public static final int OFFLINE = 9;//不在线
		public static final int ONLINE = 10;//在线
		public static final int CHAT = 11;
		public static final int UNCHAT = 12;
		public static final int CLOSEFAIL = 13;
		public static final int CLOSEOK = 14;
		public static final int LOGIN_OK = 15;//登录成功
	}
	
	private int type;
	private Object content;
	private Info receiver;
	private Info sender;
	
	public void setType(int type)
	{
		this.type = type;
	}
	
	public int getType()
	{
		return type;
	}
	
	public void setContent(Object content)
	{
		this.content = content;
	}
	
	public Object getContent()
	{
		return content;
	}
	
	public void setReceiver(Info receiver)
	{
		this.receiver = receiver;
	}
	
	public Info getReceiver()
	{
		return receiver;
	}
	
	public void setSender(Info sender)
	{
		this.sender = sender;
	}
	
	public Info getSender()
	{
		return sender;
	}
}
