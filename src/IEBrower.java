import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import java.awt.event.*;

/*使用了DJnativeswing包，用来渲染phpinfo页面*/
public class IEBrower extends JFrame {
	private static final long serialVersionUID = 4859858380825456963L;
	private JPanel contentPane;
	// private final String URL = "file:///C:/Users/T480/Desktop/a.html";

	/**
	 * Launch the application.
	 */
	public static void show(String html) {
		UIUtils.setPreferredLookAndFeel();
		NativeInterface.open();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IEBrower frame = new IEBrower(html);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		NativeInterface.runEventPump();
	}

	public IEBrower(String html) {
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {// 监听退出事件
			public void windowClosing(WindowEvent e) {
				MainUi.RecoverFrame(); // 使main窗口取消禁用
				dispose();// 释放当前窗口资源，并且设置为不可见
			}
		});
		try {
			setIconImage(new ImageIcon(getClass().getResource("images/logo.png")).getImage());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		} 
		setBounds(200, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JWebBrowser jWebBrowser = new JWebBrowser();
		jWebBrowser.setBounds(0, 0, 884, 561);
		// jWebBrowser.navigate(URL);
		jWebBrowser.setHTMLContent(html);
		jWebBrowser.setButtonBarVisible(false);
		jWebBrowser.setMenuBarVisible(false);
		jWebBrowser.setBarsVisible(false);
		jWebBrowser.setStatusBarVisible(false);
		contentPane.add(jWebBrowser);
	}
}