import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;

public class UpdateUi{
	public static JFrame update;
	JPanel jp;
	private JTextField url,password;
	JButton button = new JButton("ȷ��");
	WebShell new_webshell;
		
	public UpdateUi(DefaultTableModel tableModel,ArrayList<WebShell> webshells,int selection_row) {
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();
		update = new JFrame("�༭webshell");
		try {
			update.setIconImage(new ImageIcon(getClass().getResource("images/logo.png")).getImage());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		update.setSize(300, 200);
		update.setLocation((d.width - update.getWidth()) / 2,
				(d.height - update.getHeight()) / 2);
		update.getContentPane().setLayout(new BorderLayout(0, 0));
		
		update.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		update.addWindowListener(new WindowAdapter() {//�����˳��¼�
		public void windowClosing(WindowEvent e) {
			MainUi.RecoverFrame(); //ʹmain����ȡ������
			update.dispose();//�ͷŵ�ǰ������Դ����������Ϊ���ɼ�
		}
		});
		
		jp = new JPanel();
		
		JLabel LabelUrl = new JLabel("Url:");
        JLabel LabelPass = new JLabel("��������:");
        String old_url = webshells.get(selection_row).getUrl();
        String old_pass = webshells.get(selection_row).getPassword();
		this.url = new JTextField(old_url);
		this.password = new JTextField(old_pass);
		
		/*ȷ��button����*/
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String new_url = url.getText();
				String new_pass = password.getText();
				webshells.get(selection_row).setUrl(new_url);
				webshells.get(selection_row).setPassword(new_pass);
				DataBase.update(webshells.get(selection_row));
				Object[] data = {new_url,new_pass,webshells.get(selection_row).getType()};
				tableModel.setValueAt(new_url,selection_row,0);
				tableModel.setValueAt(new_pass,selection_row,1);
				tableModel.setValueAt(webshells.get(selection_row).getType(),selection_row,2);
				tableModel.fireTableDataChanged();//����JTable
				update.dispose();//�ͷŵ�ǰ������Դ����������Ϊ���ɼ�
		}
		});
		
		jp.add(LabelUrl);
		jp.add(url);
		jp.add(LabelPass);
		jp.add(password);
		jp.add(button);
		
		update.add(jp);
		update.pack();
		update.setVisible(true);
	}
}