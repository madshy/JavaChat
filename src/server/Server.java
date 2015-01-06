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
	
	private ArrayList<ChatThread> clients;//����ͻ��˵��߳�
	private ArrayList<Info> userList;//���������û�
	
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
				System.out.println("������������,�ȴ�������...");
				socket = serverSocket.accept();
				System.out.println("�ͻ������ӳɹ�,�����߳���...");
				ChatThread ct = new ChatThread(socket, this);
				System.out.println("�߳������ɹ�,�߳�������...");
				ct.start();
				System.out.println("�߳����гɹ�...");
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
