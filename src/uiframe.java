import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;


import java.io.*;
import java.net.*;

public class uiframe extends JFrame{
	private int vetux = 0;
	private String s;
	private Socket socket;
	private Socket usersocket;
	private String user;
	private String firstwordonsearch;
	private String secondwordonsearch;
	private String thirdwordonsearch;
	//to show user's name
	JLabel jlbuser = new JLabel();
	//the  first,second,third line of result is which dictionary:11:baidu 22:youdao 33：bing
	private int firstcode;
	private int secondcode;
	private int thirdcode;
	//IO streams
	protected DataOutputStream toServer;
	protected DataInputStream fromServer;
	
	private JButton mini = new JButton("mini");
	private JButton login = new JButton("登录");
	private JButton regist = new JButton("注册");
	private JLabel title = new JLabel("My Dictionary");
	private JLabel input = new JLabel("Input");
	private JTextField inputtext;
	private JButton search;
	private JList<String> userlist;
	private JLabel username = new JLabel("在线用户列表");
	//JCheck Box
	private JCheckBox baidu;
	private JCheckBox youdao;
	private JCheckBox bing;
	//show results
	private JTextArea firstresult;
	private JTextArea secondresult;
	private JTextArea thirdresult;
	//jlable
	private JLabel jlbfirst;
	private JLabel jlbsecond;
	private JLabel jlbthird;
	//send word card button
	private JButton jbtsendfirstcard;
	private JButton jbtsendsecondcard;
	private JButton jbtsendthirdcard;
	//send all Buttons
	private JButton jbtsendallfirst;
	private JButton jbtsendallsecond;
	private JButton jbtsendallthird;
	//click like button
	private JButton jbtfirstlike;
	private JButton jbtsecondlike;
	private JButton jbtthirdlike;
	//user id to send
	private JTextField jtffirstid;
	private JTextField jtfsecondid;
	private JTextField jtfthirdid;
	//id JLabel
	private JLabel jlbfirstid;
	private JLabel jlbsecondid;
	private JLabel jlbthirdid;
	
	public uiframe(){
		//initial
		user = "";
		inputtext = new JTextField("");
		inputtext.setHorizontalAlignment(JTextField.LEFT);
		search = new JButton("Search");
		baidu = new JCheckBox("百度");
		youdao = new JCheckBox("有道");
		bing = new JCheckBox("必应");
		userlist = new JList<String>();
		firstresult = new JTextArea(3,30);
		secondresult = new JTextArea(3,30);
		thirdresult = new JTextArea(3,30);
		jbtfirstlike = new JButton("赞");
		jbtsecondlike = new JButton("赞");
		jbtthirdlike = new JButton("赞");
		jbtsendfirstcard = new JButton("发送单词卡");
		jbtsendsecondcard = new JButton("发送单词卡");
		jbtsendthirdcard = new JButton("发送单词卡");
		jlbfirst = new JLabel("百度");
		jlbsecond = new JLabel("有道");
		jlbthird = new JLabel("必应");
		jtffirstid = new JTextField("",15);
		jtfsecondid = new JTextField("",15);
		jtfthirdid = new JTextField("",15);
		jlbfirstid = new JLabel("ID");
		jlbsecondid = new JLabel("ID");
		jlbthirdid = new JLabel("ID");
		userlist.setFixedCellWidth(200);
		userlist.setForeground(Color.RED);
		userlist.setBackground(Color.WHITE);
		userlist.setSelectionForeground(Color.BLUE);
		userlist.setSelectionBackground(Color.PINK);
		userlist.setVisibleRowCount(3);
		jbtsendallfirst = new JButton("群发");
		jbtsendallsecond = new JButton("群发");
		jbtsendallthird = new JButton("群发");
		
		//jtffirstid.setSize(100);
		try{
			socket = new Socket("localhost",8000);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
		}
		catch(IOException ex){
			
		}
		Dimension preferredSize = new Dimension(300,100);
		Font myfont = new Font("微软雅黑",Font.PLAIN,14);
		firstresult.setFont(myfont);
		firstresult.setBounds(8,1,12,16);
		firstresult.setEditable(false);
		
		secondresult.setFont(myfont);
		secondresult.setBounds(8,10,12,16);
		secondresult.setEditable(false);
		
		thirdresult.setFont(myfont);
		thirdresult.setBounds(8,10,12,16);
		thirdresult.setEditable(false);
		
		//login register and title line
		JPanel titleline = new JPanel();
		titleline.setLayout(new FlowLayout());
		titleline.add(title,BorderLayout.WEST);
		titleline.add(login,BorderLayout.CENTER);
		titleline.add(regist,BorderLayout.EAST);
		
		//inputline
		JPanel inputline = new JPanel();
		inputline.setLayout(new BorderLayout(5,0));
		inputline.add(input,BorderLayout.WEST);
		inputline.add(inputtext,BorderLayout.CENTER);
		JPanel twobuttons = new JPanel();
		twobuttons.add(search);
		twobuttons.add(mini);
		inputline.add(twobuttons,BorderLayout.EAST);
		
		//checkbox line
		JPanel checkboxline = new JPanel();
		checkboxline.setLayout(new FlowLayout());
		checkboxline.add(baidu,BorderLayout.WEST);
		checkboxline.add(youdao,BorderLayout.CENTER);
		checkboxline.add(bing,BorderLayout.EAST);
		
		//the top three line
		JPanel topthreeline = new JPanel();
		topthreeline.setLayout(new BorderLayout());
		topthreeline.add(titleline,BorderLayout.NORTH);
		topthreeline.add(inputline,BorderLayout.CENTER);
		topthreeline.add(checkboxline,BorderLayout.SOUTH);
		
		//online users list row
		JPanel userrow = new JPanel();
		userrow.setLayout(new BorderLayout());
		userrow.add(username,BorderLayout.NORTH);
		userrow.add(new JScrollPane(userlist,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),BorderLayout.CENTER);
		
		//first line of search one
		JPanel firstlineone = new JPanel();
		firstlineone.setLayout(new FlowLayout());
		firstlineone.add(jlbfirst);
		firstresult.setLineWrap(true);
		firstresult.setWrapStyleWord(true);
		firstlineone.add(new JScrollPane(firstresult,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		
		//second line of search one
		JPanel secondlineone = new JPanel();
		secondlineone.setLayout(new FlowLayout());
		secondlineone.add(jlbfirstid);
		secondlineone.add(jtffirstid);
		secondlineone.add(jbtsendallfirst);
		secondlineone.add(jbtsendfirstcard);
		secondlineone.add(jbtfirstlike);
		
		//first line of search two
		JPanel firstlinetwo = new JPanel();
		firstlinetwo.setLayout(new FlowLayout());
		firstlinetwo.add(jlbsecond);
		secondresult.setLineWrap(true);
		secondresult.setWrapStyleWord(true);
		firstlinetwo.add(new JScrollPane(secondresult,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
							
		//second line of search two
		JPanel secondlinetwo = new JPanel();
		secondlinetwo.setLayout(new FlowLayout());
		secondlinetwo.add(jlbsecondid);
		secondlinetwo.add(jtfsecondid);
		secondlinetwo.add(jbtsendallsecond);
		secondlinetwo.add(jbtsendsecondcard);
		secondlinetwo.add(jbtsecondlike);
				
		//first line of search three
		JPanel firstlinethree = new JPanel();
		firstlinethree.setLayout(new FlowLayout());
		thirdresult.setLineWrap(true);
		thirdresult.setWrapStyleWord(true);
		firstlinethree.add(jlbthird);
		firstlinethree.add(new JScrollPane(thirdresult,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		
		//second line of search three
		JPanel secondlinethree = new JPanel();
		secondlinethree.setLayout(new FlowLayout());
		secondlinethree.add(jlbthirdid);
		secondlinethree.add(jtfthirdid);
		secondlinethree.add(jbtsendallthird);
		secondlinethree.add(jbtsendthirdcard);
		secondlinethree.add(jbtthirdlike);
		
		//search one
		JPanel searchone = new JPanel();
		searchone.setLayout(new BorderLayout());
		searchone.add(firstlineone,BorderLayout.NORTH);
		searchone.add(secondlineone,BorderLayout.CENTER);
		
		//search two
		JPanel searchtwo = new JPanel();
		searchtwo.setLayout(new BorderLayout());
		searchtwo.add(firstlinetwo,BorderLayout.NORTH);
		searchtwo.add(secondlinetwo,BorderLayout.CENTER);
		
		//search three
		JPanel searchthree = new JPanel();
		searchthree.setLayout(new BorderLayout());
		searchthree.add(firstlinethree,BorderLayout.NORTH);
		searchthree.add(secondlinethree,BorderLayout.CENTER);
		
		//the search area
		JPanel resultpanel = new JPanel();
		resultpanel.setLayout(new BorderLayout(5,0));
		resultpanel.add(searchone,BorderLayout.NORTH);
		resultpanel.add(searchtwo,BorderLayout.CENTER);
		resultpanel.add(searchthree,BorderLayout.SOUTH);
		
		//the total control
		this.setLayout(new BorderLayout(5,0));
		add(topthreeline,BorderLayout.NORTH);
		add(userrow,BorderLayout.WEST);
		add(resultpanel,BorderLayout.CENTER);
		
		class sendallfirstlistener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					toServer.writeInt(7);
					toServer.writeUTF(user);
					toServer.writeUTF(firstwordonsearch);
					toServer.writeInt(firstcode);
					toServer.writeUTF(firstresult.getText());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		
		}
		jbtsendallfirst.addActionListener(new sendallfirstlistener());
		class sendallsecondlistener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					toServer.writeInt(7);
					toServer.writeUTF(user);
					toServer.writeUTF(secondwordonsearch);
					toServer.writeInt(secondcode);
					toServer.writeUTF(secondresult.getText());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		
		}
		jbtsendallsecond.addActionListener(new sendallsecondlistener());
		class sendallthirdlistener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					toServer.writeInt(7);
					toServer.writeUTF(user);
					toServer.writeUTF(thirdwordonsearch);
					toServer.writeInt(thirdcode);
					toServer.writeUTF(thirdresult.getText());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		
		}
		jbtsendallthird.addActionListener(new sendallthirdlistener());
		//minibutton listener
		class miniListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				miniframe miniui = new miniframe();
				setVisible(false);
			}
			
		}
		mini.addActionListener(new miniListener());
		//searchbutton listener
		class SearchListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				s = inputtext.getText();
				int onsearchcount = 0;
				boolean firstrecvinneed = false;
				boolean secondrecvinneed = false;
				boolean thirdrecvinneed = false;
				int firstrecvnum = 0;
				int secondrecvnum = 0;
				int thirdrecvnum = 0;
				int firstlikenum = 0;
				int secondlikenum = 0;
				int thirdlikenum = 0;
				String firstrecvdic = "";
				String secondrecvdic = "";
				String thirdrecvdic = "";
				String firstrecvmean = "";
				String secondrecvmean = "";
				String thirdrecvmean = "";
				
				firstwordonsearch = s;
				secondwordonsearch = s;
				thirdwordonsearch = s;
				
				try{
					toServer.writeInt(3);
					toServer.writeUTF(s);
					//int recvnum = fromServer.readInt();
					//if server in search mode
					if(true){
						firstrecvnum = fromServer.readInt();
						firstrecvmean = fromServer.readUTF();
						firstlikenum = fromServer.readInt();
						secondrecvnum = fromServer.readInt();
						secondrecvmean = fromServer.readUTF();
						secondlikenum = fromServer.readInt();
						thirdrecvnum = fromServer.readInt();
						thirdrecvmean = fromServer.readUTF();
						thirdlikenum = fromServer.readInt();
						
						
						
						System.out.print(thirdrecvmean);
						//first receive num
						if(firstrecvnum == 11 && baidu.isSelected() == true){
							firstrecvdic = "百度";
							firstcode = firstrecvnum;
							firstrecvinneed = true;
							onsearchcount ++;
						}
						else if(firstrecvnum == 22 && youdao.isSelected() == true){
							firstrecvdic = "有道";
							firstcode = firstrecvnum;
							firstrecvinneed = true;
							onsearchcount ++;
						}
						else if(firstrecvnum == 33 && bing.isSelected() == true){
							firstrecvdic = "必应";
							firstcode = firstrecvnum;
							firstrecvinneed = true;
							onsearchcount ++;
						}
						System.out.println("\n" + onsearchcount);
						//second receive num
						if(secondrecvnum == 11 && baidu.isSelected() == true){
							secondrecvdic = "百度";
							if(onsearchcount == 1)
								secondcode = secondrecvnum;
							if (onsearchcount == 0)
								firstcode = secondrecvnum;
							secondrecvinneed = true;
							onsearchcount ++;
						}
						else if(secondrecvnum == 22 && youdao.isSelected() == true){
							secondrecvdic = "有道";
							if(onsearchcount == 1)
								secondcode = secondrecvnum;
							if (onsearchcount == 0)
								firstcode = secondrecvnum;
							secondrecvinneed = true;
							onsearchcount ++;
						}
						else if(secondrecvnum == 33 && bing.isSelected() == true){
							secondrecvdic = "必应";
							if(onsearchcount == 1)
								secondcode = secondrecvnum;
							if (onsearchcount == 0)
								firstcode = secondrecvnum;
							secondrecvinneed = true;
							onsearchcount ++;
						}
						
						//third receive num
						if(thirdrecvnum == 11 && baidu.isSelected() == true){
							thirdrecvdic = "百度";
							if(onsearchcount == 2)
								thirdcode = thirdrecvnum;
							if (onsearchcount == 1)
								secondcode = thirdrecvnum;
							thirdrecvinneed = true;
							onsearchcount ++;
						}
						else if(thirdrecvnum == 22 && youdao.isSelected() == true){
							thirdrecvdic = "有道";
							if(onsearchcount == 2)
								thirdcode = thirdrecvnum;
							if (onsearchcount == 1)
								secondcode = thirdrecvnum;
							thirdrecvinneed = true;
							onsearchcount ++;
						}
						else if(thirdrecvnum == 33 && bing.isSelected() == true){
							thirdrecvdic = "必应";
							if(onsearchcount == 2)
								thirdcode = thirdrecvnum;
							if (onsearchcount == 1)
								secondcode = thirdrecvnum;
							thirdrecvinneed = true;
							onsearchcount ++;
						}
						if(onsearchcount == 0){
							firstcode = 0;
							secondcode = 0;
							thirdcode = 0;
						}
						if(onsearchcount == 1){
							secondcode = 0;
							thirdcode = 0;
						}
						if(onsearchcount == 2){
							thirdcode = 0;
						}
						//the first one need to print to the ui
						if(firstrecvinneed == true){
							jlbfirst.setText(firstrecvdic);
							firstresult.setText(firstrecvmean);
							jbtfirstlike.setText("赞" + firstlikenum);
							//the second one need to print to the ui
							if(secondrecvinneed == true){
								jlbsecond.setText(secondrecvdic);
								secondresult.setText(secondrecvmean);
								jbtsecondlike.setText("赞" + secondlikenum);
								//the third one need to print on the ui
								if(thirdrecvinneed == true){
									jlbthird.setText(thirdrecvdic);
									thirdresult.setText(thirdrecvmean);
									jbtthirdlike.setText("赞" + thirdlikenum);
								}
								//the third one need not to print on the ui
								else{
									jlbthird.setText("");
									thirdresult.setText("");
									jbtthirdlike.setText("赞");
								}
							}
							//the second one need not to print to the ui
							else{
								//the third one need to print on the ui
								if(thirdrecvinneed == true){
									jlbsecond.setText(thirdrecvdic);
									secondresult.setText(thirdrecvmean);
									jbtsecondlike.setText("赞" + thirdlikenum);
									jlbthird.setText("");
									thirdresult.setText("");
									jbtthirdlike.setText("赞");
								}
								//the third one need not to print on the ui
								else{
									jlbsecond.setText("");
									secondresult.setText("");
									jbtsecondlike.setText("赞");
									jlbthird.setText("");
									thirdresult.setText("");
									jbtthirdlike.setText("赞");
								}
							}
						}
						//the first one need not to print to the ui
						else{
							//the second one need to print to the ui
							if(secondrecvinneed == true){
								jlbfirst.setText(secondrecvdic);
								firstresult.setText(secondrecvmean);
								jbtfirstlike.setText("赞" + secondlikenum);
								//the third one need to print on the ui
								if(thirdrecvinneed == true){
									jlbsecond.setText(thirdrecvdic);
									secondresult.setText(thirdrecvmean);
									jbtsecondlike.setText("赞" + thirdlikenum);
									jlbthird.setText("");
									thirdresult.setText("");
								}
								//the third one need not to print on the ui
								else{
									jlbsecond.setText("");
									secondresult.setText("");
									jlbthird.setText("");
									thirdresult.setText("");
								}
							}
							//the second one need not to print to the ui
							else{
								//the third one need to print on the ui
								if(thirdrecvinneed == true){
									jlbfirst.setText(thirdrecvdic);
									firstresult.setText(thirdrecvmean);
									jlbsecond.setText("");
									secondresult.setText("");
									jlbthird.setText("");
									thirdresult.setText("");
								}
								//the third one need not to print on the ui
								else{
									jlbfirst.setText("");
									firstresult.setText("");
									jlbsecond.setText("");
									secondresult.setText("");
									jlbthird.setText("");
									thirdresult.setText("");
								}
							}
						}
					}
					else
						;
					
				}
				catch(IOException ex){
					
				}
			}
		}
		search.addActionListener(new SearchListener());
		//login listener
		class LoginListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(login.getText().equals("登录")){
					JFrame loginframe = new JFrame();
					loginframe.setSize(250,150);
					loginframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					loginframe.setLocationRelativeTo(null);
					loginframe.setVisible(true);
				
					//account label
					JLabel account = new JLabel("账号");
					//password label
					JLabel password = new JLabel("密码");
					//where to enter your account
					JTextField accountfield = new JTextField();
					//where th enter your password
					JPasswordField passwordfield = new JPasswordField();
					//login button
					JButton logbut = new JButton("登录");
					JButton canbut = new JButton("取消");
					JPanel lastlineoflogin = new JPanel();
					lastlineoflogin.add(logbut);
					lastlineoflogin.add(canbut);
					JPanel wordpanel = new JPanel();
					wordpanel.setLayout(new GridLayout(2,2));
					
					loginframe.setLayout(new BorderLayout());
					loginframe.add(wordpanel,BorderLayout.CENTER);
					wordpanel.add(account);
					wordpanel.add(accountfield);
					wordpanel.add(password);
					wordpanel.add(passwordfield);
					loginframe.add(lastlineoflogin,BorderLayout.SOUTH);

				
					//login button actionlistener
					class LogbutListener implements ActionListener{
						public void actionPerformed(ActionEvent arg0) {
							// TODO Auto-generated method stub
							String accountstr = accountfield.getText();
							String passwordstr = String.valueOf(passwordfield.getPassword());
							try{
								toServer.writeInt(1);
								toServer.writeUTF(accountstr);
								toServer.writeUTF(passwordstr);
								int recvnum = fromServer.readInt();
								//if successfully login
								if(recvnum == 2){
									user = accountstr;
									titleline.remove(login);
									titleline.remove(regist);
									titleline.remove(title);
									jlbuser.setText(user);
									titleline.validate();
									titleline.updateUI();
									titleline.add(jlbuser);
									titleline.add(login);
									login.setText("下线");
									usersocket = new Socket("localhost",8001);
									HandleAnothersocket task = new HandleAnothersocket(usersocket);
									new Thread(task).start();
									loginframe.dispose();
									
								}
								//if cant login,show the warning frame
								else{
									JFrame warningframe = new JFrame();
									warningframe.setSize(200,100);
									warningframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
									warningframe.setLocationRelativeTo(null);
									warningframe.setVisible(true);
									JLabel warning = new JLabel();
									JButton okbut = new JButton("确定");
									if(recvnum == 0)
										warning.setText("Invalid account!");
									if(recvnum == 1)
										warning.setText("Wrong password!");
									class okbutListener implements ActionListener{
										public void actionPerformed(ActionEvent arg0) {
											warningframe.dispose();
										}
									}
									okbut.addActionListener(new okbutListener());
									warningframe.setLayout(new GridLayout(1,1));
									warningframe.add(warning);
									warningframe.add(okbut);
								}
								
							}
							catch(IOException ex){
								System.err.println(ex);
							}
							
						}
					
					}
					logbut.addActionListener(new LogbutListener());
				
					//cancel button listener
					class CancelListener implements ActionListener{

						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							loginframe.dispose();
						}
					
					}
					canbut.addActionListener(new CancelListener());	
				}
				else{
					try{
						toServer.writeInt(6);
						toServer.writeUTF(user);
						titleline.remove(jlbuser);
						titleline.add(regist);
						titleline.updateUI();
						user = "";
						userlist.setModel(new DefaultListModel<String>());
						usersocket.close();
						login.setText("登录");
					}
					catch(IOException ex){}
				}
			}
		}
		login.addActionListener(new LoginListener());
		//register listener
		class RegistListener implements ActionListener{
			public void actionPerformed(ActionEvent arg0) {
				JFrame registframe = new JFrame();
				registframe.setSize(250,150);
				registframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				registframe.setLocationRelativeTo(null);
				registframe.setVisible(true);
				
				//account label
				JLabel account = new JLabel("账号");
				//password label
				JLabel password = new JLabel("密码");
				//ensure password label
				JLabel passwordensure = new JLabel("确认密码");
				//where to enter your account
				JTextField accountfield = new JTextField();
				//where to enter your password
				JPasswordField passwordfield = new JPasswordField();
				//where to ensuer your password
				JPasswordField passwordensurefield = new JPasswordField();
				//login button
				JButton regbut = new JButton("注册");
				JButton canbut = new JButton("取消");
				regbut.setPreferredSize(preferredSize);
				canbut.setPreferredSize(preferredSize);
				registframe.setLayout(new GridLayout(4,2));
				registframe.add(account);
				registframe.add(accountfield);
				registframe.add(password);
				registframe.add(passwordfield);
				registframe.add(passwordensure);
				registframe.add(passwordensurefield);
				registframe.add(regbut);
				registframe.add(canbut);
				//cancel button listener
				class CancelListener implements ActionListener{

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						registframe.dispose();
					}
					
				}
				canbut.addActionListener(new CancelListener());	
				//regist button actionlistener
				class RegbutListener implements ActionListener{
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						String accountstr = accountfield.getText();
						String passwordstr = String.valueOf(passwordfield.getPassword());
						String paswordensurestr = String.valueOf(passwordensurefield.getPassword());
						System.out.println(passwordstr);
						System.out.println(paswordensurestr);
						if(passwordstr.compareTo(paswordensurestr) != 0){
							JFrame failreg = new JFrame();
							failreg.setSize(250,150);
							failreg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
							failreg.setLocationRelativeTo(null);
							failreg.setVisible(true);
							JLabel successful = new JLabel("输入密码不同!");
							JButton okbut = new JButton("确认!");
							JPanel okbutpanel = new JPanel();
							okbutpanel.add(okbut);
							failreg.setLayout(new GridLayout(2,1));
							failreg.add(successful);
							failreg.add(okbutpanel);
							class oktbutlistener implements ActionListener{
								public void actionPerformed(ActionEvent arg0) {
									failreg.dispose();
									//registframe.dispose();
								}
							}
							okbut.addActionListener(new oktbutlistener());
						}
						else{
						try{
							toServer.writeInt(2);
							toServer.writeUTF(accountstr);
							toServer.writeUTF(passwordstr);
							int recvnum = fromServer.readInt();
							//if successfully reg
							if(recvnum == 0){
								JFrame successfulreg = new JFrame();
								successfulreg.setSize(200,150);
								successfulreg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
								successfulreg.setLocationRelativeTo(null);
								successfulreg.setVisible(true);
								JLabel successful = new JLabel("注册成功!");
								JButton okbut = new JButton("确认!");
								JPanel okbutpanel = new JPanel();
								okbutpanel.add(okbut);
								//okbut.setPreferredSize(preferredSize);
								successfulreg.setLayout(new GridLayout(2,1));
								successfulreg.add(successful);
								successfulreg.add(okbutpanel);
								class oktbutlistener implements ActionListener{
									public void actionPerformed(ActionEvent arg0) {
										successfulreg.dispose();
										registframe.dispose();
									}
								}
								okbut.addActionListener(new oktbutlistener());
								
							}
							//if cant regist,show the warning frame
							else{
								JFrame warningframe = new JFrame();
								warningframe.setSize(200,150);
								warningframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
								warningframe.setLocationRelativeTo(null);
								warningframe.setVisible(true);
								JLabel warning = new JLabel();
								JButton okbut = new JButton("确认");
								//okbut.setPreferredSize(preferredSize);
								warning.setText("账号已存在!");
								class okbutListener implements ActionListener{
									public void actionPerformed(ActionEvent arg0) {
										warningframe.dispose();
									}
								}
								okbut.addActionListener(new okbutListener());
								JPanel okbutpanel = new JPanel();
								okbutpanel.setLayout(new FlowLayout());
								okbutpanel.add(okbut);
								warningframe.setLayout(new GridLayout(2,1));
								warningframe.add(warning);
								warningframe.add(okbutpanel);
							}
						}
						catch(IOException ex){
							System.err.println(ex);
						}
						}
						
					}
					
				}
				regbut.addActionListener(new RegbutListener());
			}
			
		}
		regist.addActionListener(new RegistListener());
		//first like listener
		class firstlikeListener implements ActionListener{
			public void actionPerformed(ActionEvent arg0) {
				try{
					if(firstcode != 0 && user != ""){
						toServer.writeInt(4);
						toServer.writeUTF(user);
						toServer.writeUTF(firstwordonsearch);
						toServer.writeInt(firstcode);
						jbtfirstlike.setText("赞" + fromServer.readInt());
					}
				}
				catch(IOException ex){}
			}
		}
		jbtfirstlike.addActionListener(new firstlikeListener());
		//second like listener
		class secondlikeListener implements ActionListener{
			public void actionPerformed(ActionEvent arg0) {
				try{
					if(secondcode != 0&&user != ""){
						toServer.writeInt(4);
						toServer.writeUTF(user);
						toServer.writeUTF(secondwordonsearch);
						toServer.writeInt(secondcode);
						jbtsecondlike.setText("赞" + fromServer.readInt());
					}
				}
				catch(IOException ex){}
			}
		}
		jbtsecondlike.addActionListener(new secondlikeListener());
		//third like listener
		class thirdlikeListener implements ActionListener{
			public void actionPerformed(ActionEvent arg0) {
				try{
					if(thirdcode != 0&&user != ""){
						toServer.writeInt(4);
						toServer.writeUTF(user);
						toServer.writeUTF(thirdwordonsearch);
						toServer.writeInt(thirdcode);
						jbtthirdlike.setText("赞" + fromServer.readInt());
					}
				}
				catch(IOException ex){}
			}
		}
		jbtthirdlike.addActionListener(new thirdlikeListener());
		//first send word card listener
		class firstsendwordcardlistener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try{
				toServer.writeInt(5);
				toServer.writeUTF(user);
				toServer.writeUTF(jtffirstid.getText());
				toServer.writeUTF(firstwordonsearch);
				toServer.writeInt(firstcode);
				toServer.writeUTF(firstresult.getText());
				}
				catch(IOException ex){}
			}
			
		}
		jbtsendfirstcard.addActionListener(new firstsendwordcardlistener());
		//second send word card listener
		class secondsendwordcardlistener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try{
				toServer.writeInt(5);
				toServer.writeUTF(user);
				toServer.writeUTF(jtfsecondid.getText());
				toServer.writeUTF(secondwordonsearch);
				toServer.writeInt(secondcode);
				toServer.writeUTF(secondresult.getText());
				}
				catch(IOException ex){}
			}
			
		}
		jbtsendsecondcard.addActionListener(new secondsendwordcardlistener());
		//third send word card listener
		class thirdsendwordcardlistener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try{
					toServer.writeInt(5);
					toServer.writeUTF(user);
					toServer.writeUTF(jtfthirdid.getText());
					toServer.writeUTF(thirdwordonsearch);
					toServer.writeInt(thirdcode);
					toServer.writeUTF(thirdresult.getText());
					}
					catch(IOException ex){}
				}		
			}
		jbtsendthirdcard.addActionListener(new thirdsendwordcardlistener());
		//windowslistener event
		class ourwindowlistener implements WindowListener{

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				if(user != ""){
					try{
						toServer.writeInt(6);
						toServer.writeUTF(user);
						vetux = 0;
						user = "";
					}
					catch(IOException ex){}
				}
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		}

	}
	public static void main(String[] args){
		uiframe frame = new uiframe();
		frame.setTitle("Dictionary");
		frame.setSize(680,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
	class HandleAnothersocket implements Runnable{
		private Socket socket;
		private DataOutputStream toServertwo;
		private DataInputStream fromServertwo;
		
		public HandleAnothersocket(Socket socket){
			this.socket = socket;
		}
		@Override
		public void run() {
			
			int usercount = 0;
			
			// TODO Auto-generated method stub
			try {
				toServertwo = new DataOutputStream(socket.getOutputStream());
				fromServertwo = new DataInputStream(socket.getInputStream());
				toServertwo.writeUTF(user);
				while(true){
					if(fromServertwo.readInt() == 0){
						if(user != ""){
							usercount = fromServertwo.readInt();
							DefaultListModel<String> dlm = new DefaultListModel<String>();
							for(int i = 0;i < usercount;i ++){
								String onlineuser = fromServertwo.readUTF();
								if(onlineuser.equals(user) == false)
									dlm.addElement(onlineuser);
							}
							userlist.setModel(dlm);
						}
					}
					else {
						if(fromServertwo.readInt() == 1){
						String sendword;
						String word = fromServertwo.readUTF();
						String meaning = fromServertwo.readUTF();
						String usrname = fromServertwo.readUTF();
						int dicid = fromServertwo.readInt();
						
						switch(dicid){
						case(11):sendword = "baidu:\n" + word + ":" + meaning +"\n" + usrname;break;
						case(22):sendword = "youdao:\n" + word + ":" + meaning +"\n" + usrname;break;
						case(33):sendword = "bing:\n" + word + ":" + meaning +"\n" + usrname;break;
						default:sendword = "";break;
						}
						System.out.println(sendword);
						int width = 300;
						int height = 300;
						Font newfont = new Font("宋体",Font.BOLD,16);
						BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
						Graphics2D g2 = (Graphics2D)bi.getGraphics();
						g2.setBackground(Color.YELLOW);
						g2.clearRect(0, 0, width, height);
						g2.setPaint(Color.BLACK);
						
						FontRenderContext context = g2.getFontRenderContext();  
				        Rectangle2D bounds = newfont.getStringBounds(sendword, context);  
				        double x = (width - bounds.getWidth()) / 2 - 100;  
				        double y = (height - bounds.getHeight()) / 2 - 100;  
				        double ascent = -bounds.getY();  
				        double baseY = y + ascent;  
				        sendword = "hello!world!";                                                                                                                                             
				        g2.drawString(usrname + "向你发来单词卡", 20, 80);
				        g2.drawString("单词：" + word, 20, 100);
				        switch(dicid){
						case(11):g2.drawString("baidu:", 20, 120);break;
						case(22):g2.drawString("youdao:", 20, 120);break;
						case(33):g2.drawString("bing:", 20, 120);break;
						default:break;
						}
				        File file = new File("image/" + user + usrname + word + dicid);
				        char[] wordsinmeaning;
				        wordsinmeaning = meaning.toCharArray();
				        char[] stringoneline = new char[25];
				        int wordindex = 0;
				        while(wordindex < (wordsinmeaning.length/24)*24){
				        	for(int i = 0;wordindex < wordsinmeaning.length && i <24;i ++){
				        		stringoneline[i] = wordsinmeaning[i + wordindex];
				        	}
				        	String thischar30 = new String(stringoneline);
				        	g2.drawString(thischar30, 20, 140 + 20*(wordindex/24));
				        	wordindex = wordindex + 24;
				        }
				        char[] stringonline = new char[25];
				        for(int i = 0;i + wordindex < wordsinmeaning.length;i ++){
				        	stringonline[i] = wordsinmeaning[i + wordindex];
			        	}
				        String newone = new String(stringonline);
				        g2.drawString(newone, 20, 140 + 20*(wordindex/24));
						ImageIO.write(bi, "jpg", file);
						
						
						JFrame displayimage = new JFrame();
						displayimage.setLayout(new FlowLayout());
						JLabel jbli = new JLabel();
						Icon icon = new ImageIcon("image/" + user + usrname + word + dicid);
						jbli.setIcon(icon);
						jbli.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());
						displayimage.add(jbli,new Integer(Integer.MIN_VALUE));
						displayimage.setTitle("单词卡");
						displayimage.setSize(300,300);
						displayimage.setLocationRelativeTo(null);
						displayimage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						displayimage.setVisible(true);
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class miniframe extends JFrame{
		private String s = "";
		private JTextField miniinput;
		private JButton minisearch;
		private JButton minitonormal;
		private JTextArea miniresult;
		public miniframe(){
			super.setVisible(false);
			miniinput = new JTextField();
			minisearch = new JButton("search");
			minitonormal = new JButton("Normal");
			miniresult = new JTextArea();
			JPanel minititle = new JPanel();
			minititle.setLayout(new BorderLayout());
			minititle.add(miniinput,BorderLayout.CENTER);
			miniresult.setLineWrap(true);
			miniresult.setWrapStyleWord(true);
			JPanel twopanel = new JPanel();
			twopanel.add(minisearch);
			twopanel.add(minitonormal);
			minititle.add(twopanel,BorderLayout.EAST);
			setLayout(new BorderLayout());
			add(minititle,BorderLayout.NORTH);
			add(new JScrollPane(miniresult,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),BorderLayout.CENTER);
			setTitle("MiniDictionary");
			setSize(300,200);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLocationRelativeTo(null);
			setVisible(true);
			class minisearchlistener implements ActionListener{
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					s = miniinput.getText();
					try {
						toServer.writeInt(3);
						toServer.writeUTF(s);
						int firstrecvnum = fromServer.readInt();
						String firstrecvmean = fromServer.readUTF();
						int firstlikenum = fromServer.readInt();
						int secondrecvnum = fromServer.readInt();
						String secondrecvmean = fromServer.readUTF();
						int secondlikenum = fromServer.readInt();
						int thirdrecvnum = fromServer.readInt();
						String thirdrecvmean = fromServer.readUTF();
						int thirdlikenum = fromServer.readInt();
						miniresult.setText(firstrecvmean);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
			}
			minisearch.addActionListener(new minisearchlistener());
			class minitonormallistener implements ActionListener{

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					dispose();
					uiframe.this.setVisible(true);
				}
				
			}
			minitonormal.addActionListener(new minitonormallistener());
		}
		
	}
}