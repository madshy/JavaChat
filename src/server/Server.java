package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import acc_info.Account;
import acc_info.Info;

public class Server implements Runnable{
	private Socket socket;
	private ServerSocket serverSocket;
	
	private ArrayList<ChatThread> clients;//保存客户端的线程
	private ArrayList<Info> userList;//保存在线用户
	
	private boolean run;
	
	public Server() throws IOException {
		// TODO Auto-generated constructor stub
		socket = null;
		serverSocket = new ServerSocket(9999);
		clients = new ArrayList<ChatThread>();
		userList = new ArrayList<Info>();
		run = true;
		new Thread(this).start();
	}
	


	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			while (true)
			{
				System.out.println("服务器服务中,等待连接中...");
				socket = serverSocket.accept();
				System.out.println("客户端连接成功,启动线程中...");
				ChatThread ct = new ChatThread(socket, this);
				System.out.println("线程启动成功,线程运行中...");
				ct.start();
				System.out.println("线程运行成功...");
			}
		}catch (Exception ex)
		{
			run = false;
			try{
				serverSocket.close();				
			}catch (Exception e){}
		}
	}
	
	public ArrayList<ChatThread> getClients(){
		return clients;
	}
	
	public ArrayList<Info> getUserList()
	{
		return userList;
	}
	
	public static void main(String[] args) throws Exception
	{
		Server server = new Server();
	}
}
