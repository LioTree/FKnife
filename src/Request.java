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
	//���sendPost���������������doPost������
	//sendPostֱ����stringȥ��response��Ū�û��з�ȫû�ˣ������˰��졣��
	//������������������23333
	public static String sendPost(String url, String param,String cookie) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // �򿪺�URL֮�������
            URLConnection conn = realUrl.openConnection();
            // ����ͨ�õ���������
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Cookie", cookie);
            // ����POST�������������������
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // ��ȡURLConnection�����Ӧ�������
            out = new PrintWriter(conn.getOutputStream());
            // �����������
            out.print(param);
            // flush������Ļ���
            out.flush();
            // ����BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("���� POST ��������쳣��"+e);
            e.printStackTrace();
        }
        //ʹ��finally�����ر��������������
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
	
	//�ο��˲˵�Դ���doPost�����������з�-_-
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
			byte[] b = new byte[1024]; //��byte����ȥ��response,��String�Ļ��ᵼ�»��з���ɿո�
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