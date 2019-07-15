import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Decrypt{
    // AES-128-ECB加密，这玩意直接从网上找不会重修吧。。。好吧我承认我根本不懂密码学。。
	//吐槽一波，这玩意php直接openssl_encrypt()就搞定了。。果然php是世界上最好的语言(逃)
    public static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位"); //记得后面改成图形化界面
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        /*冰蝎用的是iv向量为空的AES-128-CBC算法，但是不知道怎么实现，所以改成了AES-128-ECB*/
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式" 
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

        return Base64.getEncoder().encodeToString(encrypted);//base64编码方便传输
    }
}


