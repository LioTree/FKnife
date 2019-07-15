import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Vector;
import java.awt.Dialog.ModalExclusionType;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableCellRenderer;

public class MainUi {
	public static JFrame main;
	public static JTable jt;

	public MainUi() {
		// TODO Auto-generated constructor stub
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();
		main = new JFrame("Fknife v1.0 ��̬�����Ƽ���WebԶ�̹���ͻ���"); 
		// System.out.println(System.getProperty("user.dir"));
		try {
			main.setIconImage(new ImageIcon(getClass().getResource("images/logo.png")).getImage());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		main.setSize(1000, 500);
		main.setLocation((d.width - main.getWidth()) / 2, (d.height - main.getHeight()) / 2);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.getContentPane().setLayout(new BorderLayout(0, 0));
		TableUi();
		main.setVisible(true);
	}

	/* �����ݿ��е�webshellȫ����ʾ�ڱ���� */
	public void TableUi() {
		ArrayList<WebShell> webshells = DataBase.selectAll();
		// ����
		String[] columnNames = { "URL", "��������", "�ű�����" };
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

		for (int i = 0; i < webshells.size(); i++) {
			String url = webshells.get(i).getUrl();
			String password = webshells.get(i).getPassword();
			String type = webshells.get(i).getType();

			Object[] data = { url, password, type };
			tableModel.addRow(data);
		}

		// Initializing the JTable
		jt = new JTable(tableModel) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}// ��������༭
		};

		jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// ��ֹ��ѡ
		jt.setFont(new Font("Menu.font", Font.PLAIN, 20)); // ���������С
		jt.setBounds(30, 40, 200, 300);
		jt.setRowHeight(25);

		// adding it to JScrollPane
		JScrollPane sp = new JScrollPane(jt);
		main.add(sp);

		final JPopupMenu popupMenu = new JPopupMenu();
		final JPopupMenu popupMenu2 = new JPopupMenu();

		JMenuItem infoItem = new JMenuItem("�鿴��Ϣ");
		JMenuItem cmdItem = new JMenuItem("�����ն�");
		JMenuItem fileItem = new JMenuItem("�ļ�����");
		JMenuItem insertItem1 = new JMenuItem("�½�");//�����½�
		JMenuItem insertItem2 = new JMenuItem("�½�");
		JMenuItem updateItem = new JMenuItem("�༭");
		JMenuItem deleteItem = new JMenuItem("ɾ��");

		/* ������Ϣ�鿴���Ҽ����� */
		infoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						int[] selection = jt.getSelectedRows();
						String html = "";
						try {
							html = webshells.get(selection[0]).getInfo();
						} catch (Exception a) {
							System.out.printf(a.getMessage());
						}
						IEBrower.show(html);
					}
				}.start();
			}
		});

		/* ���������ն˵��Ҽ����� */
		cmdItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// JOptionPane.showMessageDialog(main, "cmd");
				new Thread() {
					public void run() {
						int[] selection = jt.getSelectedRows();
						ConsoleGui console = new ConsoleGui(webshells.get(selection[0]));
						console.showFrame();
					}
				}.start();
			}
		});
		
		fileItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						int[] selection = jt.getSelectedRows();
						FileGui files = new FileGui(webshells.get(selection[0]));
						files.showFrame();
					}
				}.start();
			}
		});

		/* ����ѡ��״̬���½����Ҽ����� */
		insertItem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// �´��ڴ򿪣��ɴ������ý���
				main.setEnabled(false);
				main.setModalExclusionType(ModalExclusionType.NO_EXCLUDE);

				new Thread() {
					public void run() {
						InsertUi insert = new InsertUi(tableModel, webshells);
					}
				}.start();
			}
		});
		
		/* ����ûѡ��״̬���½����Ҽ����� */
		insertItem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// �´��ڴ򿪣��ɴ������ý���
				main.setEnabled(false);
				main.setModalExclusionType(ModalExclusionType.NO_EXCLUDE);

				new Thread() {
					public void run() {
						InsertUi insert = new InsertUi(tableModel, webshells);
					}
				}.start();
			}
		});

		/* ���ñ༭���Ҽ����� */
		updateItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// �´��ڴ򿪣��ɴ������ý���
				main.setEnabled(false);
				main.setModalExclusionType(ModalExclusionType.NO_EXCLUDE);

				int[] selection = jt.getSelectedRows();
				UpdateUi update = new UpdateUi(tableModel, webshells, selection[0]);
			}
		});

		/* ����ɾ�����Ҽ����� */
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// JOptionPane.showMessageDialog(main, "delete");
				int[] selection = jt.getSelectedRows();
				DataBase.DeleteWebShell(webshells.get(selection[0]));
				tableModel.removeRow(selection[0]);// JTable��ɾ������
				tableModel.fireTableDataChanged();// ����JTable
			}
		});

		popupMenu.add(infoItem);
		popupMenu.add(cmdItem);
		popupMenu.add(fileItem);
		popupMenu.add(insertItem1);
		popupMenu.add(updateItem);
		popupMenu.add(deleteItem);
		popupMenu2.add(insertItem2);
		jt.setComponentPopupMenu(popupMenu);
		sp.setComponentPopupMenu(popupMenu2);
	}

	/* ��ձ�� */
	public static void clearTable() {
		DefaultTableModel cleanModel = (DefaultTableModel) jt.getModel();
		cleanModel.setRowCount(0);
	}

	// ʹmain����ȡ������
	public static void RecoverFrame() {
		main.setEnabled(true);
	}
}
