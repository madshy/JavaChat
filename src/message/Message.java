package message;

import java.io.Serializable;

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
		/**
		 * ∆’Õ®–≈œ¢
		 */
		public static final int MESSAGE = 4;
		public static final int LOGOUT = 5;
//		public static final int ALL = 6;
		public static final int USERLIST = 7;
	}
	
	private int type;
	private Object content;
	private String receiver;
	private String sender;
	
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
	
	public void setReceiver(String receiver)
	{
		this.receiver = receiver;
	}
	
	public String getReceiver()
	{
		return receiver;
	}
	
	public void setSender(String sender)
	{
		this.sender = sender;
	}
	
	public String getSender()
	{
		return sender;
	}
}
