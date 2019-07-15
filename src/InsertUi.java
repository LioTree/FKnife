import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;

public class InsertUi{
	public static JFrame insert;
	JPanel jp;
	private JTextField url,password;
	WebShell new_webshell;
	JButton button = new JButton("确认");
		
	public InsertUi(DefaultTableModel tableModel,ArrayList<WebShell> webshells) {
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();
		insert = new JFrame("新建webshell");
		try {
			insert.setIconImage(new ImageIcon(getClass().getResource("images/logo.png")).getImage());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		insert.setSize(300, 200);
		insert.setLocation((d.width - insert.getWidth()) / 2, 
				(d.height - insert.getHeight()) / 2);
		insert.getContentPane().setLayout(new BorderLayout(0, 0));
		
		insert.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		insert.addWindowListener(new WindowAdapter() {//监听退出事件
		public void windowClosing(WindowEvent e) {
			MainUi.RecoverFrame(); //使main窗口取消禁用
			insert.dispose();//释放当前窗口资源，并且设置为不可见
		}
		});
		
		jp = new JPanel();
		
		
		JLabel LabelUrl = new JLabel("Url:");
        JLabel LabelPass = new JLabel("访问密码:");
		this.url = new JTextField(20);
		this.password = new JTextField(18);
		
		/*确认button监听*/
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String new_url = url.getText();
				String new_pass = password.getText();
				new_webshell = new WebShell(new_url,new_pass);
				int id = DataBase.getNextId();
				new_webshell.setId(id);
				DataBase.insert(new_webshell);
				MainUi.RecoverFrame(); //使main窗口取消禁用
				Object[] data = {new_url,new_pass,new_webshell.getType()};
            	tableModel.addRow(data);
            	tableModel.fireTableDataChanged();//更新JTable
            	webshells.add(new_webshell);
				insert.dispose();//释放当前窗口资源，并且设置为不可见
		}
		});
		
		jp.add(LabelUrl);
		jp.add(url);
		jp.add(LabelPass);
		jp.add(password);
		jp.add(button);
		
		insert.add(jp);
		insert.pack();
		insert.setVisible(true);
	}
}




