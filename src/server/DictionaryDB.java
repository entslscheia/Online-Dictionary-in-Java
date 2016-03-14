package Server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DictionaryDB{ 
	public static String[] userOnLine = new String[100];//at most 100 users are on line at the same time
	
	public static void Initial() throws SQLException,ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/dictionarydb","scott","tiger");
	
		Statement statement = connection.createStatement();
		
		statement.execute("drop table if exists User");
		statement.execute("drop table if exists Likes");
		statement.execute("drop table if exists UserLike");
		statement.executeUpdate("create table User(Account char(50) not null, Password char(50) not null, State int not null, IP char(50) not null, constraint pkUser primary key (Account))");
		statement.executeUpdate("create table Likes(Word char(50) not null, Baidu int not null, Youdao int not null, Bing int not null, constraint pkLikes primary key (Word))");
		statement.executeUpdate("create table UserLike(Account char(50) not null, Word char(50) not null, Source int not null)");
		
		connection.close();
	}
	
	public static int Register(String account, String password, String address) throws SQLException,ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/dictionarydb","scott","tiger");
		
		Statement statement = connection.createStatement();
		
		ResultSet resultSet = statement.executeQuery("select * from User where Account = " + "'" + account + "'");
		
		if(!resultSet.next()){//resultSet.next() is false means that there is no result
			statement.executeUpdate("insert into User values(" + "'" + account + "'" + "," + "'" + password + "'" 
		+ "," + 0 + "," + "'" + address + "'" + ")");
			
			connection.close();
			return 0;
		}
		else{
			//提示账号已经存在
			//System.out.println("error: Account already exists!");
			
			connection.close();
			return 1;
		}
	}
	
	public static int LogIn(String account, String password, String address) throws SQLException,ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/dictionarydb","scott","tiger");
		
		Statement statement = connection.createStatement();
		
		ResultSet resultSet = statement.executeQuery("select * from User where Account = " + "'" + account + "'");
		
		if(!resultSet.next()){
			//账户不存在
			
			connection.close();
			
			return 0;//该账户不存在
		}
		else{
			if(!resultSet.getString(2).equals(password)){//incorrect password
				connection.close();
				
				return 1;//标志账户密码不正确
			}
				
			else {
				//修改数据库中该用户的状态为在线
				//0 stands for off line, while 1 stands for on line
				statement.executeUpdate("update User set State = 1 where Account = " + "'" + account + "'");
				//更新用户的IP地址
				statement.executeUpdate("update User set IP = " + "'" + address + "'" + " where Account = " + "'" + account + "'");
				
				connection.close();
				return 2;//标志登录成功
			}
		}
	}
	
	public static synchronized void LogOut(String account) throws SQLException,ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/dictionarydb","scott","tiger");
		
		Statement statement = connection.createStatement();
		
		statement.executeUpdate("update User set State = 0 where Account = " + "'" + account + "'");
	}
	
	public static int UserOnLine() throws SQLException,ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/dictionarydb","scott","tiger");
		
		Statement statement = connection.createStatement();
		
		int countOfOnLine = 0;//indicate the count of the users who are on line
		
		ResultSet resultSet = statement.executeQuery("select Account from User where State = 1");
		while(resultSet.next()){
			countOfOnLine ++;
			userOnLine[countOfOnLine - 1] = resultSet.getString(1);
		}
		
		for(int i = countOfOnLine;i < 100;i ++)
			userOnLine[i] = null;
		
		connection.close();
		
		return countOfOnLine;
	}
	
	public static synchronized void LikeBaidu(String word, String account) throws SQLException,ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/dictionarydb","scott","tiger");
		
		Statement statement = connection.createStatement();
		
		ResultSet resultSet0 = statement.executeQuery("select * from UserLike where Word = " + "'" + word 
				+ "'" + " and Account = " + "'" + account + "'" + " and Source = 1");
		
		if(!resultSet0.next()){//如果该用户没有给该单词点赞过，则给该单词点赞，并记录这次点赞
			ResultSet resultSet = statement.executeQuery("select * from Likes where Word = " + "'" + word + "'");
			if(!resultSet.next())
			{
				statement.executeUpdate("insert into Likes values(" + "'" + word + "'" + ",1,0,0)");
				//statement.executeUpdate("insert into Likes values('fuck',1,0,0)");
			}
			else{
				int num = resultSet.getInt(2);
				num ++;
				statement.executeUpdate("update Likes set Baidu = " + num + " where Word =" + "'" + word + "'");
			}
			
			statement.executeUpdate("insert into UserLike values(" + "'" + account + "'" + ", '" + word + "', 1)");
		}
		
		else{
			ResultSet resultSet = statement.executeQuery("select * from Likes where Word = " + "'" + word + "'");
			resultSet.next();
			int num = resultSet.getInt(2);
			num --;
			statement.executeUpdate("update Likes set Baidu = " + num + " where Word =" + "'" + word + "'");
			statement.executeUpdate("delete from UserLike where Account = " + "'" + account + "'" + " and Source = 1");
		}
		
		connection.close();
	}
	
	public static synchronized void  LikeYoudao(String word, String account) throws SQLException,ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/dictionarydb","scott","tiger");
		
		Statement statement = connection.createStatement();
		
		ResultSet resultSet0 = statement.executeQuery("select * from UserLike where Word = " + "'" + word 
				+ "'" + " and Account = " + "'" + account + "'" + " and Source = 2");
		
		if(!resultSet0.next()){//如果该用户没有给该单词点赞过，则给该单词点赞，并记录这次点赞
			ResultSet resultSet = statement.executeQuery("select * from Likes where Word = " + "'" + word + "'");
			if(!resultSet.next())
			{
				statement.executeUpdate("insert into Likes values(" + "'" + word + "'" + ",0,1,0)");
			}
			else{
				int num = resultSet.getInt(3);
				num ++;
				statement.executeUpdate("update Likes set Youdao = " + num + " where Word =" + "'" + word + "'");
			}
			
			statement.executeUpdate("insert into UserLike values(" + "'" + account + "'" + ", '" + word + "', 2)");
		}
		else{//点赞过
			ResultSet resultSet = statement.executeQuery("select * from Likes where Word = " + "'" + word + "'");
			resultSet.next();
			int num = resultSet.getInt(3);
			num --;
			statement.executeUpdate("update Likes set Youdao = " + num + " where Word =" + "'" + word + "'");
			statement.executeUpdate("delete from UserLike where Account = " + "'" + account + "'" + " and Source = 2");
		}
		
		connection.close();
	}
	
	public static synchronized void LikeBing(String word, String account) throws SQLException,ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/dictionarydb","scott","tiger");
		
		Statement statement = connection.createStatement();
		
		ResultSet resultSet0 = statement.executeQuery("select * from UserLike where Word = " + "'" + word 
				+ "'" + " and Account = " + "'" + account + "'" + " and Source = 3");
		
		if(!resultSet0.next()){//如果该用户没有给该单词点赞过，则给该单词点赞，并记录这次点赞
			ResultSet resultSet = statement.executeQuery("select * from Likes where Word = " + "'" + word + "'");
			if(!resultSet.next())
			{
				statement.executeUpdate("insert into Likes values(" + "'" + word + "'" + ",0,0,1)");
			}
			else{
				int num = resultSet.getInt(4);
				num ++;
				statement.executeUpdate("update Likes set Bing = " + num + " where Word =" + "'" + word + "'");
			}
			
			statement.executeUpdate("insert into UserLike values(" + "'" + account + "'" + ", '" + word + "', 3)");
		}
		
		else{
			ResultSet resultSet = statement.executeQuery("select * from Likes where Word = " + "'" + word + "'");
			resultSet.next();
			int num = resultSet.getInt(4);
			num --;
			statement.executeUpdate("update Likes set Bing = " + num + " where Word =" + "'" + word + "'");
			statement.executeUpdate("delete from UserLike where Account = " + "'" + account + "'" + " and Source = 3");
		}
		
		connection.close();
	}
	
	public static int numOfBaidu(String word) throws SQLException,ClassNotFoundException{//返回word百度翻译的点赞数
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/dictionarydb","scott","tiger");
		
		Statement statement = connection.createStatement();
		
		ResultSet resultSet = statement.executeQuery("select * from Likes where Word = " + "'" + word + "'");
		
		
		if(!resultSet.next())
		{
			connection.close();
			return 0;
		}
		else{
			int res= resultSet.getInt(2);
			connection.close();
			return res;
		}
		
	}
	
	public static int numOfYoudao(String word) throws SQLException,ClassNotFoundException{//返回word有道翻译的点赞数
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/dictionarydb","scott","tiger");
		
		Statement statement = connection.createStatement();
		
		ResultSet resultSet = statement.executeQuery("select * from Likes where Word = " + "'" + word + "'");
	
		
		if(!resultSet.next())
		{
			connection.close();
			return 0;
		}
		else{
			int res = resultSet.getInt(3);
			connection.close();
			return res;
		}
		
	}
	
	public static int numOfBing(String word) throws SQLException,ClassNotFoundException{//返回word必应翻译的点赞数
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/dictionarydb","scott","tiger");
		
		Statement statement = connection.createStatement();
		
		ResultSet resultSet = statement.executeQuery("select * from Likes where Word = " + "'" + word + "'");
		
		
		if(!resultSet.next())
		{
			connection.close();
			return 0;
		}
		else{
			int res = resultSet.getInt(4);
			connection.close();
			return res;
		}
	}
}