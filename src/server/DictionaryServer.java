package Server;
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.*;

public class DictionaryServer{
	public static int messageCount = -1;
	public static String []destUser = new String[100];
	public static String []sourceUser = new String[100];
	public static String []card_word = new String[100];
	public static String []card_meaning = new String[100];
	public static int []card_Source = new int[100];
	
	public static void main(String[] args){
		new DictionaryServer();
	}
	
	public DictionaryServer(){
		try {
			
			//Create a server socket
			ServerSocket serverSocket = new ServerSocket(8000);
			
			
			
			//Number a client
			int clientNo = 1;
			
			while(true){
				//Listen for a new connection request
				Socket socket = serverSocket.accept();
				
				InetAddress inetAddress = socket.getInetAddress();
			
				//Create a new thread for the connection
				HandleAClient task = new HandleAClient(socket,inetAddress.getHostAddress());
				
				//Start the new thread
				new Thread(task).start();
				
				//Increment clientNo
				clientNo ++;
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

class HandleAClient implements Runnable{
	private Socket socket;//A connected socket
	private String IP_Address;
	
	/** Construct a thread */
	public HandleAClient(Socket socket, String IP_Address){
		this.socket = socket;
		this.IP_Address = IP_Address;
	}
	
	public void run(){
		try {
			// Create data input and output streams
			DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
			DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
			
			//Continuously serve the client
			while(true){
				
				//Receive operation from the client
				//'operation' keep listening constantly
				int operation = inputFromClient.readInt();
				
				//////////System.out.println(operation + "!!!");
				
				if(operation == 1){// Log in
					
					//Receive the account and password from the client
					String account = inputFromClient.readUTF();
					String password = inputFromClient.readUTF();
					
					//////////System.out.println(account + " " + password);
					
					int LogInState;// To indicate the state of log in where 0 stands for account not existing
					//1 stands for wrong password
					//2 stands for log in successfully
	
					//Log in operations in database
					try {
						LogInState = DictionaryDB.LogIn(account, password, IP_Address);
						outputToClient.writeInt(LogInState);
						outputToClient.flush();
						
						if(LogInState == 2){
							//Create another server socket
							ServerSocket serverSocket1 = new ServerSocket(8001);
							Socket socket1 = serverSocket1.accept();///////////////////////////////////////
							InetAddress inetAddress1 = socket1.getInetAddress();
							HandleAClient1 task1 = new HandleAClient1(socket1,inetAddress1.getHostAddress());
							new Thread(task1).start();
						}
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
				else if(operation == 2){// Register
					//Receive the account and password from the client
					String account = inputFromClient.readUTF();
					String password = inputFromClient.readUTF();
					
					//////////System.out.println(account + " " + password);
					
					int RegisterState;// To indicate the state of register where 0 stands for register successfully
					//1 stands for account already exists
					
					//Register operations in database
					try {
						RegisterState = DictionaryDB.Register(account, password,IP_Address);
						outputToClient.writeInt(RegisterState);
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				else if(operation == 3){// Search
					String word = inputFromClient.readUTF();
					
					try {
						int a = DictionaryDB.numOfBaidu(word);
						int b = DictionaryDB.numOfYoudao(word);
						int c = DictionaryDB.numOfBing(word);
					    crawl.Search(word);//
					    
					    if(a >= b){
					    	if(b >= c){//a,b,c
					    		outputToClient.writeInt(11);
					    		outputToClient.writeUTF(crawl.resBaidu);
					    		outputToClient.writeInt(a);
					    		
					    		outputToClient.writeInt(22);
					    		outputToClient.writeUTF(crawl.resYoudao);
					    		outputToClient.writeInt(b);
					    		
					    		outputToClient.writeInt(33);
					    		outputToClient.writeUTF(crawl.resBing);
					    		outputToClient.writeInt(c);
					    	}
					    	
					    	else{
					    		if(a >= c){//a,c,b
					    			outputToClient.writeInt(11);
						    		outputToClient.writeUTF(crawl.resBaidu);
						    		outputToClient.writeInt(a);
						    		
						    		outputToClient.writeInt(33);
						    		outputToClient.writeUTF(crawl.resBing);
						    		outputToClient.writeInt(c);
						    		
						    		outputToClient.writeInt(22);
						    		outputToClient.writeUTF(crawl.resYoudao);
						    		outputToClient.writeInt(b);
					    		}
					    		
					    		else{//c,a,b
					    			outputToClient.writeInt(33);
						    		outputToClient.writeUTF(crawl.resBing);
						    		outputToClient.writeInt(c);
						    		
						    		outputToClient.writeInt(11);
						    		outputToClient.writeUTF(crawl.resBaidu);
						    		outputToClient.writeInt(a);
					    			
					    			outputToClient.writeInt(22);
						    		outputToClient.writeUTF(crawl.resYoudao);
						    		outputToClient.writeInt(b);
					    		}
					    	}
					    }
					    
					    else{
					    	if(a >= c){//b,a,c
					    		outputToClient.writeInt(22);
					    		outputToClient.writeUTF(crawl.resYoudao);
					    		outputToClient.writeInt(b);
					    		
					    		outputToClient.writeInt(11);
					    		outputToClient.writeUTF(crawl.resBaidu);
					    		outputToClient.writeInt(a);
					    		
					    		outputToClient.writeInt(33);
					    		outputToClient.writeUTF(crawl.resBing);
					    		outputToClient.writeInt(c);
					    	}
					    	
					    	else{
					    		if(b >= c){//b,c,a
					    			outputToClient.writeInt(22);
						    		outputToClient.writeUTF(crawl.resYoudao);
						    		outputToClient.writeInt(b);
					    			
					    			outputToClient.writeInt(33);
						    		outputToClient.writeUTF(crawl.resBing);
						    		outputToClient.writeInt(c);
						    		
						    		outputToClient.writeInt(11);
						    		outputToClient.writeUTF(crawl.resBaidu);
						    		outputToClient.writeInt(a);
					    		}
					    		
					    		else{//c,b,a
					    			outputToClient.writeInt(33);
						    		outputToClient.writeUTF(crawl.resBing);
						    		outputToClient.writeInt(c);
					    			
					    			outputToClient.writeInt(22);
						    		outputToClient.writeUTF(crawl.resYoudao);
						    		outputToClient.writeInt(b);
						    		
						    		outputToClient.writeInt(11);
						    		outputToClient.writeUTF(crawl.resBaidu);
						    		outputToClient.writeInt(a);
					    		}
					    	}
					    }
					} catch (ClassNotFoundException | SQLException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				else if(operation == 4){// like
					String userName = inputFromClient.readUTF();
					String word = inputFromClient.readUTF();
					
					int flag = inputFromClient.readInt();
					
					////////System.out.println(userName + word + flag);
					if(flag == 11){//like the result from baidu
						try {
							DictionaryDB.LikeBaidu(word,userName);
							
							outputToClient.writeInt(DictionaryDB.numOfBaidu(word));
						} catch (ClassNotFoundException | SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if(flag == 22){//like the result from youdao
						try {
							DictionaryDB.LikeYoudao(word,userName);
							
							outputToClient.writeInt(DictionaryDB.numOfYoudao(word));
						} catch (ClassNotFoundException | SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else if (flag == 33){//like the result from bing
						try {
							DictionaryDB.LikeBing(word,userName);
							
							outputToClient.writeInt(DictionaryDB.numOfBing(word));
						} catch (ClassNotFoundException | SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else{
						//do nothing
						////////System.out.println("Error 002!");
					}
				}
				else if(operation == 5){//send pics
					////////System.out.println("send card");
					DictionaryServer.messageCount ++;
					DictionaryServer.sourceUser[DictionaryServer.messageCount] = inputFromClient.readUTF();
					DictionaryServer.destUser[DictionaryServer.messageCount] = inputFromClient.readUTF();
					DictionaryServer.card_word[DictionaryServer.messageCount] = inputFromClient.readUTF();
					DictionaryServer.card_Source[DictionaryServer.messageCount] = inputFromClient.readInt();
					DictionaryServer.card_meaning[DictionaryServer.messageCount] = inputFromClient.readUTF();
					////////System.out.println(DictionaryServer.card_word[DictionaryServer.messageCount]);
				}
				else if(operation == 6){//log out
					String account = inputFromClient.readUTF();
					try {
						DictionaryDB.LogOut(account);
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(operation == 7){//
					try {
						int count = DictionaryDB.UserOnLine();
						
						String suser = inputFromClient.readUTF();
						String cword = inputFromClient.readUTF();
						int csource = inputFromClient.readInt();
						String cmeaning = inputFromClient.readUTF();
						
						for(int i = 0;i < count;i ++){
							if(!DictionaryDB.userOnLine[i].equals(suser)){
								DictionaryServer.messageCount ++;
								DictionaryServer.sourceUser[DictionaryServer.messageCount] = suser;
								DictionaryServer.destUser[DictionaryServer.messageCount] = DictionaryDB.userOnLine[i];
								DictionaryServer.card_word[DictionaryServer.messageCount] = cword;
								DictionaryServer.card_Source[DictionaryServer.messageCount] = csource;
								DictionaryServer.card_meaning[DictionaryServer.messageCount] = cmeaning;
							}
							
						}
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					//do nothing
					//////////System.out.println("Errorr 001!");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class HandleAClient1 implements Runnable{
	private Socket socket;//A connected socket
	private String IP_Address;
	private String UserName;
	
	/** Construct a thread */
	public HandleAClient1(Socket socket, String IP_Address){
		this.socket = socket;
		this.IP_Address = IP_Address;
		UserName = null;
	}
	
	public void run(){
		try {//告诉客户端当前在线用户数量与在线用户列表
			//create the input/output stream
			DataInputStream inputFromClient1 = new DataInputStream(socket.getInputStream());
			DataOutputStream outputToClient1 = new DataOutputStream(socket.getOutputStream());
			
			UserName = inputFromClient1.readUTF();//建立socket与用户名的对应
			//////////System.out.println("UserName: " + UserName);
			
			while(true){
				try {
					Thread.currentThread().sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				outputToClient1.writeInt(0);
				
			    int count = DictionaryDB.UserOnLine();
				outputToClient1.writeInt(count);
				
				for(int i = 0;i < count;i ++){
					String userName = DictionaryDB.userOnLine[i];
					outputToClient1.writeUTF(userName);
				}
				
				if(DictionaryServer.messageCount >= 0){
					////////System.out.println("Message Count:" + DictionaryServer.messageCount);
					////////System.out.println("陶堃傻逼");
					
					outputToClient1.writeInt(1);
					int flag = -1;
					for(int i = 0;i <= DictionaryServer.messageCount;i ++){
						if(UserName.equals(DictionaryServer.destUser[i])){
							flag = i;
							break;
						}
					}
					
					////////System.out.println("flag :" + flag);
					
					if(flag != -1){
						outputToClient1.writeInt(1);
						outputToClient1.writeUTF(DictionaryServer.card_word[flag]);
						outputToClient1.writeUTF(DictionaryServer.card_meaning[flag]);
						outputToClient1.writeUTF(DictionaryServer.sourceUser[flag]);
						outputToClient1.writeInt(DictionaryServer.card_Source[flag]);
						
						////////System.out.println(UserName);
						////////System.out.println(DictionaryServer.destUser[flag]);
						////////System.out.println(DictionaryServer.sourceUser[flag]);
						////////System.out.println(DictionaryServer.card_word[flag]);
						
						for(int i = flag;i < DictionaryServer.messageCount;i ++){
							DictionaryServer.destUser[i] = DictionaryServer.destUser[i + 1];
							DictionaryServer.card_word[i] = DictionaryServer.card_word[i + 1];
							DictionaryServer.card_meaning[i] = DictionaryServer.card_meaning[i + 1];
							DictionaryServer.card_Source[i] = DictionaryServer.card_Source[i + 1];
							DictionaryServer.sourceUser[i] = DictionaryServer.sourceUser[i + 1];
						}
						
						DictionaryServer.messageCount --;
						
						////////System.out.println("Message Count" + DictionaryServer.messageCount);
					}
					else{
						outputToClient1.writeInt(0);
					}
				}
				
				//outputToClient1.flush();
			}
		} catch (ClassNotFoundException | SQLException | IOException e1) {
			// TODO Auto-generated catch block
			try {
				DictionaryDB.LogOut(UserName);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			e1.printStackTrace();
		}
	}
}