package acc_info;

import java.awt.Image;
import java.util.ArrayList;

public class Group extends Info{
	ArrayList<Account> members;
	ArrayList<ChatLog> chatLog;
	Info []manager;
	
	/**
	 * Numbers of managers.
	 */
	private final int MAX = 5;
	private int nowNum = 0;
	
	/**
	 * Person who create this group
	 */
	Account creater;
	
	public Group()
	{
//		super(acc, name, birthday);
//		this.creater = creater;
		manager = new Info[MAX];
		members = new ArrayList<Account>();
		chatLog = new ArrayList<ChatLog>();
	}
	
	/**
	 * Get the list of members.
	 * <br/>This list cannot be changed by means of changing the return value.
	 * @return ArrayList&ltAccount&gt
	 */
	public final ArrayList<Account> getMemberList()
	{
		return members;
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
	 * Add manager.
	 * Return false if numbers of manager equal to MAX,false else.
	 * @param mng
	 * @return boolean
	 */
	public boolean addManager(final Info mng)
	{
		/*Full and cannot add*/
		if (MAX == nowNum)
		{
			return false;
		}
		manager[nowNum ++] = mng;
		return true;
	}
	
	/**
	 * Delete mng's situation of manager.
	 * Return true if success,false else.
	 * @param mng
	 * @return boolean
	 */
	public boolean deleteManager(final Info mng)
	{
		int index;
		for (index = 0; index < nowNum; ++ index)
		{
			if (mng == manager[index])
			{
				break;
			}
		}
		/*The given account is not a manager*/
		if (index == nowNum)
		{
			return false;
		}
		
		/*Remove the manager situation of mng*/
		for (int i = index; i < nowNum - 1; ++ i)
		{
			/*The target is the last one*/
			if (index == nowNum - 1)
			{
				break;
			}
			manager[i] = manager[i + 1];
		}
		-- nowNum;
		return true;
	}
	
	/**
	 * Add member to the list of members.
	 * Return true if success, false else.
	 * @param cl
	 * @return boolean
	 */
	public boolean addMember(Account cl)
	{
		return members.add(cl);
	}
	
	/**
	 * Delete member from the list of members.
	 * Return true if success, false else.
	 * @param cl
	 * @return boolean
	 */
	public boolean deleteMember(Account cl)
	{
		return members.remove(cl);
	}
	
	/**
	 * Delete all chat logs.
	 */
	public void deleteAllMember()
	{
		members.clear();
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
