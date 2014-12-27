package acc_info;

import java.awt.Image;
import java.util.ArrayList;

//ук╨епео╒
public class Account extends Info {
	private String psw;
	private boolean sex;
	ArrayList<Friend> friends;
	ArrayList<Info> groups;
	
	/**
	 * The parameter of sex is a boolean, true boy, false girl.
	 * @param acc
	 * @param psw
	 * @param name
	 * @param sex
	 * @param birthday
	 */
	public Account()
	{
//		super(acc, name, birthday);
//		this.sex = sex;
//		this.psw = psw;
		friends = new ArrayList<Friend>();
		groups = new ArrayList<Info>();
	}
	
	public String getPsw()
	{
		return psw;
	}
	
	public void setPsw(String psw)
	{
		this.psw = psw;
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
	 * Get the list of friends.
	 * <br/>This list cannot be changed by means of changing the return value.
	 * @return ArrayList&ltFriend&gt
	 */
	public final ArrayList<Friend> getFriendList()
	{
		return friends;
	}
	
	/**
	 * Get the list of groups.
	 * <br/>This list cannot be changed by means of changing the return value.
	 * @return ArrayList&ltInfo&gt
	 */
	public final ArrayList<Info> getGroupList()
	{
		return groups;
	}
	
	/**
	 * Add friend to the list of friends.
	 * Return true if success,return false else.
	 * @param frd
	 * @return boolean
	 */
	public boolean addFriend(Friend frd)
	{
		return friends.add(frd);
	}
	
	/**
	 * Delete friend from the list of friends.
	 * Return true if success,return false else.
	 * @param frd
	 * @return boolean
	 */
	public boolean deleteFriend(Friend frd)
	{
		return friends.remove(frd);
	}
	
	/**
	 * Remove all friends.
	 */
	public void deleteAllFrd()
	{
		friends.clear();
	}
	
	/**
	 * Add group to the list of groups.
	 * Return true if success,return false else.
	 * @param frd
	 * @return boolean
	 */
	public boolean addGroup(Group grp)
	{
		return groups.add(grp);
	}
	
	/**
	 * Delete group from the list of groups.
	 * Return true if success,return false else.
	 * @param frd
	 * @return boolean
	 */
	public boolean deleteGroup(Group grp)
	{
		return groups.remove(grp);
	}
	
	/**
	 * Remove all groups.
	 */
	public void deleteAllGrp()
	{
		groups.clear();
	}
}
