import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import com.*;
import com.alee.laf.WebLookAndFeel;
import javax.swing.plaf.FontUIResource;

public class Main {
	public static void main(String args[]) throws Exception {
		/*��weblaf����������壬��Ȼ���Ļ���С����*/
		WebLookAndFeel.globalControlFont = new FontUIResource("����", 0, 16);
        WebLookAndFeel.globalTooltipFont = new FontUIResource("����", 0, 16);
        WebLookAndFeel.globalAlertFont = new FontUIResource("����", 0, 16);
        WebLookAndFeel.globalMenuFont = new FontUIResource("����", 0, 16);
        WebLookAndFeel.globalAcceleratorFont = new FontUIResource("����", 0, 16);
        WebLookAndFeel.globalTitleFont = new FontUIResource("����", 0, 16);
        WebLookAndFeel.globalTextFont = new FontUIResource("����", 0, 16);
        
        //ʹ�� weblaf 
		UIManager.setLookAndFeel(WebLookAndFeel.class.getCanonicalName());
		DataBase.connect(); // �������ݿ�
		DataBase.create_table(); // ���������webshell�����½�һ��
		//��������
		MainUi ui = new MainUi();
	}
}
