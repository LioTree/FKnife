import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import com.*;
import com.alee.laf.WebLookAndFeel;
import javax.swing.plaf.FontUIResource;

public class Main {
	public static void main(String args[]) throws Exception {
		/*给weblaf添加中文字体，不然中文会是小方框*/
		WebLookAndFeel.globalControlFont = new FontUIResource("宋体", 0, 16);
        WebLookAndFeel.globalTooltipFont = new FontUIResource("宋体", 0, 16);
        WebLookAndFeel.globalAlertFont = new FontUIResource("宋体", 0, 16);
        WebLookAndFeel.globalMenuFont = new FontUIResource("宋体", 0, 16);
        WebLookAndFeel.globalAcceleratorFont = new FontUIResource("宋体", 0, 16);
        WebLookAndFeel.globalTitleFont = new FontUIResource("宋体", 0, 16);
        WebLookAndFeel.globalTextFont = new FontUIResource("宋体", 0, 16);
        
        //使用 weblaf 
		UIManager.setLookAndFeel(WebLookAndFeel.class.getCanonicalName());
		DataBase.connect(); // 连接数据库
		DataBase.create_table(); // 如果不存在webshell表则新建一个
		//打开主界面
		MainUi ui = new MainUi();
	}
}
