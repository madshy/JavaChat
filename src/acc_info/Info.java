package acc_info;

import java.io.Serializable;

/**
 * Base information of account
 * @author shy
 *
 */
public class Info implements Serializable {
	String acc;
	String name;
	String birthday;
	
//	Info(String acc, String name, String birthday)
//	{
//		this.acc = acc;
//		this.name = name;
//		this.birthday = birthday;
//	}
	
	public void setAccount(String acc)
	{
		this.acc = acc;
	}
	
	public String getAccount()
	{
		return acc;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
}

/**
 * Chat log.
 * @author shy
 *
 */
class ChatLog implements Serializable{
	Info acc;
	String log;
	
	public Info getAcc()
	{
		return acc;
	}
	
	public String getLog()
	{
		return log;
	}
}
