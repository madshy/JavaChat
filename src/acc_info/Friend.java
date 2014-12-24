package acc_info;

import java.awt.Image;
import java.util.ArrayList;

public class Friend extends Info{
	ArrayList<ChatLog> chatLog;
	private boolean sex;
	
	public Friend()
	{
//		super(acc, name, birthday);
		this.sex = sex;
		chatLog = new ArrayList<ChatLog>();
	}
	
	/**
	 * Get the list of chatlog.
	 * <br/>This list cannot be changed by means of changing the return value.
	 * @return ArrayList&ltChatLog&gt
	 */
	public final ArrayList<ChatLog> getChatLog()
	{
		return chatLog;
	}
	
	/**
	 * Get type of sex of this account
	 * <br/> True if boy and girl else.
	 * @return
	 */
	public boolean getSex()
	{
		return sex;
	}
	
	/**
	 * Set type of sex of this account
	 * <br/> True if boy and girl else.
	 * @return
	 */
	public void setSex(boolean sex)
	{
		this.sex = sex;
	}
	
	/**
	 * Add chat log to the list of chatLogs.
	 * Return true if success, false else.
	 * @param cl
	 * @return boolean
	 */
	public boolean addChatLog(ChatLog cl)
	{
		return chatLog.add(cl);
	}
	
	/**
	 * Delete chat log from the list of chatLogs.
	 * Return true if success, false else.
	 * @param cl
	 * @return boolean
	 */
	public boolean deleteChatLog(ChatLog cl)
	{
		return chatLog.remove(cl);
	}
	
	/**
	 * Delete all chat logs.
	 */
	public void deleteAllLog()
	{
		chatLog.clear();
	}
}