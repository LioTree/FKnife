import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;


public class Request{
	//这个sendPost是垃圾。。下面的doPost才能用
	//sendPost直接用string去存response，弄得换行符全没了，折腾了半天。。
	//留着这玩意做个纪念23333
	public static String sendPost(String url, String param,String cookie) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Cookie", cookie);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }   
	
	//参考了菜刀源码的doPost，完美处理换行符-_-
	public static String doPost(String url, String param,String cookie) {
		String data = "";
		HttpURLConnection huc;
		try {
			URL u = new URL(url);
			huc = (HttpURLConnection) u.openConnection();
			huc.setConnectTimeout(10000);
			huc.setReadTimeout(10000);
			huc.setDoOutput(true);
			huc.setRequestProperty("Cookie", cookie);
			PrintWriter out = new PrintWriter(huc.getOutputStream());
			out.write(param);
			out.flush();
			out.close();
			boolean normal = (huc.getResponseCode() == 200);
			InputStream is = normal ? huc.getInputStream() : huc.getErrorStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len = -1;
			byte[] b = new byte[1024]; //用byte数组去存response,用String的话会导致换行符变成空格
			while ((len = is.read(b)) != -1) {
				baos.write(b, 0, len);
			}
			data = new String(baos.toByteArray());
		} catch (Exception e) {
			data = e.getMessage();
		}
		return data;
	}
}