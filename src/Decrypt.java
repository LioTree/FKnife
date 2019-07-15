import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Decrypt{
    // AES-128-ECB���ܣ�������ֱ�Ӵ������Ҳ������ްɡ������ð��ҳ����Ҹ�����������ѧ����
	//�²�һ����������phpֱ��openssl_encrypt()�͸㶨�ˡ�����Ȼphp����������õ�����(��)
    public static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("KeyΪ��null");
            return null;
        }
        // �ж�Key�Ƿ�Ϊ16λ
        if (sKey.length() != 16) {
            System.out.print("Key���Ȳ���16λ"); //�ǵú���ĳ�ͼ�λ�����
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        /*��Ы�õ���iv����Ϊ�յ�AES-128-CBC�㷨�����ǲ�֪����ôʵ�֣����Ըĳ���AES-128-ECB*/
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"�㷨/ģʽ/���뷽ʽ" 
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

        return Base64.getEncoder().encodeToString(encrypted);//base64���뷽�㴫��
    }
}


