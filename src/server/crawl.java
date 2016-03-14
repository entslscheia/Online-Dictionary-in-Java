package Server;
import java.io.*;
import java.net.*;

public class crawl {
	public static String resBaidu;
	public static String resYoudao;
	public static String resBing;
	
	public static void Search(String word) throws InterruptedException{
		//String word="look";
		Runnable A=new youdao(word);
		Runnable B=new baidu(word);
		Runnable C=new bing(word);
		Thread thread1=new Thread(A);
		Thread thread2=new Thread(B);
		Thread thread3=new Thread(C);
		thread1.start();
		thread2.start();
		thread3.start();
		while(thread1.isAlive() || thread2.isAlive() || thread3.isAlive()){}
		/*System.out.println(((youdao)A).gettranslation());
		System.out.println(((baidu)B).gettranslation());
		System.out.println(((bing)C).gettranslation());*/
		
		resBaidu = ((baidu)B).gettranslation();
		resYoudao = ((youdao)A).gettranslation();
		resBing = ((bing)C).gettranslation();

	}
}
	class youdao implements Runnable{
		private String word;
		private String translation;
		public youdao(String word){
			this.word=word.replace(' ', '+');
			translation=" ";
		}
		public  void run(){
			String message="";
			try{
				URL url=new URL("http://dict.youdao.com/search?q="+word+"&keyfrom=dict.index");	
				InputStream input=url.openStream();
				BufferedReader read=new BufferedReader(new InputStreamReader(input,"UTF-8"));
				
				while(read.readLine()!=null){
					String s=read.readLine();
					if(s.matches(".*<div class=\"trans-container\">.*")){
						read.readLine();
						read.readLine();
						s=read.readLine();
						while(s.matches(".*<li>.*</li>.*")){
							int begin=0,end=0,i=0;
							for(;s.charAt(i)!=-1;i++){
								if(s.charAt(i)=='>'){
									begin=i+1;
									break;
								}
							}
							for(;s.charAt(i)!=-1;i++){
								if(s.charAt(i)=='<'){
									end=i;
									break;
								}
							}
							message=message+s.substring(begin, end)+" ";
							s=read.readLine();	
						}
						translation=message;
						//System.out.println(translation);
						break;
					}			
				}	
			}
			
			catch(MalformedURLException ex){
				//该词库中无该单词
			}
			catch(IOException e){
				//大概不会出现这种情况
			}
		}
		public String gettranslation(){
			if(translation==" ")
				return "未查到该单词";
			else
				return translation;
		}
	}
	
	class baidu implements Runnable{
		private String word;
		private String translation;
		public baidu(String word){
			this.word=word.replace(' ', '+');
			translation=" ";
		}
		public  void run(){
			String message="";
			try{
				URL url=new URL("http://cidian.baidu.com/s?wd="+word);	
				InputStream input=url.openStream();
				BufferedReader read=new BufferedReader(new InputStreamReader(input,"UTF-8"));
				
				while(read.readLine()!=null){
					String s=read.readLine();
					if(s.matches(".*<div class=\"tab en-simple-means dict-en-simplemeans-english\" id=\"en-simple-means\">.*")){
						
						s=s.replaceAll("</span></p><p><strong>", " ");
						s=s.replaceAll("</span></p></div><p><span>.*", "");
						s=s.replaceAll("<div><p><strong>", "");
						s=s.replaceAll("</strong><span>", "");
						s=s.replaceAll("<.*>", "");
						s=s.replaceFirst("			", "");
						message=s;
						translation=message;
						//System.out.println(translation);
						break;
					}			
				}		
			}
			catch(MalformedURLException ex){
				//该词库中无该单词
			}
			catch(IOException e){
				//大概不会出现这种情况
			}
			
		}
		public String gettranslation(){
			if(translation==" ")
				return "未查到该单词";
			else
				return translation;
		}
	}
	
	
	class bing implements Runnable{
		private String word;
		private String translation;
		public bing(String word){
			this.word=word.replace(' ', '+');
			translation=" ";
		}
		public  void run(){
			try{
				URL url=new URL("http://cn.bing.com/dict/search?q="+word+"&go=&qs=bs&form=CM");	
				InputStream input=url.openStream();
				BufferedReader read=new BufferedReader(new InputStreamReader(input,"UTF-8"));
				
				while(read.read()!=-1){
					String s=read.readLine();
					if(s.matches(".*<meta name=\"description\".*")){
						int i=s.indexOf("]，");
						if(i<0){break;}
						s=s.substring(i+2);
						i=s.indexOf("]，");
						s=s.substring(i+2);
						i=s.indexOf("\"");
						translation=s.substring(0, i);
						//System.out.println(translation);
						break;
					}	
				}		
			}
			catch(MalformedURLException ex){
				//该词库中无该单词
			}
			catch(IOException e){
				//大概不会出现这种情况
			}
		}
		public String gettranslation(){
			if(translation==" ")
				return "未查到该单词";
			else
				return translation;
		}
	}
	