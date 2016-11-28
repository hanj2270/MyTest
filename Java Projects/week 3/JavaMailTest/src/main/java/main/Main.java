package main;

import java.util.HashMap;
import java.util.Scanner;

import mailUtils.EmlPaserUtils;

public class Main {

	private static HashMap<String, String> data=new HashMap<>();  
	//保存数据的容器,包括password/certPath/emlPath
	/** 
	 * @Description: 命令行交互函数
	 * @param args    设定文件 
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		System.out.println("欢迎使用邮件读取程序");
		hint();					//命令提示列表
		initialize();			//初始化内存
		Scanner in=new Scanner(System.in);
		String readline;
		while((readline=in.nextLine())!=null){
			String[] cmd=readline.split(" ");
			switch(cmd[0]){
			case "-h":
				menu();
				break;
			case "-f":
				if(cmd.length==3){
					file(cmd[1],cmd[2]);
				}else{
					System.out.println("请正确的输入证书路径和密码");
				}
				break;
			case "-r":
				if(cmd.length==2){
					read(cmd[1]);
				}else{
					System.out.println("请正确的输入文件路径");
				};
				break;
			case "-v":
				System.out.println("目前程序版本:1.0");
				break;	
			case "-q":
				System.out.println("退出");
				return;	
			default:
				System.out.println("不支持的命令,请重新输入");
				break;
			}
		}
		
	}
	/** 
	* @Description: 程序启动时的自动提示
	*/
	private static void hint(){
		System.out.println("-h:  获取所有命令行功能");
		System.out.println("-v:  获取程序版本");
		System.out.println("-q:  退出");

	}
	
	private static void menu(){
		System.out.println("-f [filepath] [password]: 指定一张包含私钥的证书进行邮件解析");
		System.out.println("-r [filepath]: 运用私钥解析指定邮件 ");	
	}
	

	private static void initialize(){
		data.put("password", null);	//私钥证书密码
		data.put("pfxpath",null);   //私钥证书地址
		data.put("emlpath", null);  //邮件地址
	}
	
	/** 
	* @Description: 指定证书文件
	* @param path   文件路径 
	*/
	public static void file(String path,String password){
		if(path.contains(".pfx")){                            //确认证书类别,并把路径放入存储容器
			if(password==null){
				System.out.println("pfx类型证书需要密钥,请输入 -f [path] [password]");
				return;
			}
			data.put("certPath", path);
			data.put("password", password);
		}else{
			System.out.println("该文件不是可解析的证书文件,请重新指定");
		}
	}
	
	/** 
	* @Description:  解析加密邮件
	* @param path    设定文件 
	*/
	public static void read(String path){
		try {
			EmlPaserUtils.parserFile(path);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("邮件解析失败");
		}
	}
}
