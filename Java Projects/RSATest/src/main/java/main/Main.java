package main;

import ioUtils.CertPrintUtils;
import ioUtils.CertReadUtils;
import ioUtils.FileIOUtils;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Scanner;

import encrption.EncrptionUtils;

public class Main {

	private static HashMap<String, String> data=new HashMap<>();  
	//保存数据的容器,包括inputpath\outputpath\certpath\certtype\password\signature六项
	/** 
	 * @Description: 命令行交互函数
	 * @param args    设定文件 
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		System.out.println("欢迎使用RSA签名/验签程序");
		hint();                                          //命令提示列表
		initialize();
		Scanner in=new Scanner(System.in);
		String readline;
		while((readline=in.nextLine())!=null){
			String[] cmd=readline.split(" ");
			switch(cmd[0]){
			case "-s":
				sign();
				break;
			case "-v":
				verify();
				break;
			case "-e":
				explore();
				break;
			case "-f":
				if(cmd.length==3){
					file(cmd[1],cmd[2]);
				}else{
					file(cmd[1],null);
				}
				break;
			case "-i":
				inputFile(cmd[1]);
				break;
			case "-o":
				outputFile(cmd[1]);
				break;
			case "-q":
				System.out.println("q");
				return;	
			default:
				System.out.println("不支持的命令,请重新输入");
				break;
			}
			hint();
		}
		
	}
	/** 
	* @Description: 程序启动时的自动提示
	*/
	private static void hint(){
		System.out.println("请输入想要执行的命令:");
		System.out.println("-s:  签名");
		System.out.println("-v:  验证");
		System.out.println("-e:  解析并展示证书扩展项");
		System.out.println("-f:  用于操作的PFX证书或CER证书");
		System.out.println("-i:  指定输入文件");
		System.out.println("-o:  指定输出文件");
		System.out.println("-q:  退出");

	}
		
	/** 
	* @Description: 初始化内存
	*/
	private static void initialize(){
		data.put("inputPath", null);   //输入文件路径
		data.put("outputPath", null);  //输出文件路径
		data.put("certPath", null);    //选定证书的路径
		data.put("certType", null);    //选定证书的类型
		data.put("password", null);    //密码
		data.put("text", null);        //原文
		data.put("signString", null);  //签名字符串
	}
	/** 
	* @Description: 签名函数
	*/
	public static void sign(){
		//首先判断已经指定了pfx证书
		if(data.get("certType")==null||data.get("certPath")==null){
			System.out.println("无可用的私钥进行签名,请用-f指定正确的操作证书");
		}else if(data.get("text").equals("")){
			System.out.println("没有需要加密的原文,请用-i指定输入文件");           //判断有加密原文
		}else{
			System.out.println("正在签名请稍候...");
			String text=data.get("text");
			String path=data.get("certPath");
			String password=data.get("password");                              //获得密钥证书和原文
			KeyStore keyStore=CertReadUtils.getKsformPfx(path, password);
			PrivateKey priKey=CertReadUtils.getPriKeyFromKS(keyStore, password);
			String signature=EncrptionUtils.sign(priKey, text);                //签名
			data.put("signString", signature);
			if(data.get("outputPath")!=null){
				FileIOUtils.writeFile(data.get("outputPath"), signature);
			}
			System.out.println("签名已完成");
		}
	}
	
	/** 
	* @Description: 验签函数
	*/
	public static void verify(){
		Boolean bool;
		if(data.get("signString")==null){                                     //必须有一个签名
			System.out.println("没有签名可供验签");
		}if(data.get("certPath")==null){                                      //必须有一个证书
			System.out.println("无可用的公钥进行验签,请用-f指定正确的操作证书");   //crt证书验签
		}else if(data.get("certType").equals("crt")){
			System.out.println("正在验签...");
			PublicKey pubKey=CertReadUtils.getPuKFromCrt(data.get("certPath"), data.get("password"));
			bool=EncrptionUtils.verify(pubKey, data.get("text"), data.get("signString"));
			System.out.println(bool?"验签成功":"验签失败,签名与证书不对应");
		}else{                                                                //pfx证书验签
			KeyStore keyStore=CertReadUtils.getKsformPfx(data.get("certPath"), data.get("password"));
			PublicKey pubKey=CertReadUtils.getPukfrommKs(keyStore, data.get("password"));
			bool=EncrptionUtils.verify(pubKey, data.get("text"), data.get("signString"));
			System.out.println(bool?"验签成功":"验签失败,签名与证书不对应");
		}	
	}
	
	/** 
	* @Description: 展示证书内容
	*/
	public static void explore(){
		if(data.get("certPath")==null){
			System.out.println("请用-f指定一个证书进行展示");
		}else if(data.get("certType").equals("crt")){
			CertPrintUtils.printCRT(data.get("certPath"));
		}else{
			CertPrintUtils.printPFX(data.get("certPath"), data.get("password"));
		}
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
			data.put("certType", "pfx");
			data.put("password", password);
		}else if(path.contains(".crt")){
			data.put("certPath", path);
			data.put("certType", "crt");
			data.put("password", password);
		}else{
			System.out.println("该文件不是可解析的证书文件,请重新指定");
		}
	}
	
	/** 
	* @Description: 指定输入文件,读取输入明文
	* @param path   输入文件路径
	*/
	public static void inputFile(String path){
		System.out.println("设定读取路径...");
		data.put("inputPath", path);
		String text=FileIOUtils.readFile(path);
		if(text!=null){
			data.put("text", text);
		}
	}
	
	/** 
	* @Description: 指定写入文件路径,写入签名
	* @param path   写入文件路径 
	*/
	public static void outputFile(String path){
		System.out.println("设定写入路径...");
		data.put("outputPath", path);
		if(data.get("signString")!=null){                               //如果有签名,将签名写入文件
			FileIOUtils.writeFile(path,data.get("signString"));
		}
	}
}
