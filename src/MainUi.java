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
		main = new JFrame("Fknife v1.0 动态二进制加密Web远程管理客户端"); 
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

	/* 把数据库中的webshell全部显示在表格中 */
	public void TableUi() {
		ArrayList<WebShell> webshells = DataBase.selectAll();
		// 列名
		String[] columnNames = { "URL", "访问密码", "脚本类型" };
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
			}// 表格不允许被编辑
		};

		jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 禁止多选
		jt.setFont(new Font("Menu.font", Font.PLAIN, 20)); // 设置字体大小
		jt.setBounds(30, 40, 200, 300);
		jt.setRowHeight(25);

		// adding it to JScrollPane
		JScrollPane sp = new JScrollPane(jt);
		main.add(sp);

		final JPopupMenu popupMenu = new JPopupMenu();
		final JPopupMenu popupMenu2 = new JPopupMenu();

		JMenuItem infoItem = new JMenuItem("查看信息");
		JMenuItem cmdItem = new JMenuItem("虚拟终端");
		JMenuItem fileItem = new JMenuItem("文件管理");
		JMenuItem insertItem1 = new JMenuItem("新建");//两个新建
		JMenuItem insertItem2 = new JMenuItem("新建");
		JMenuItem updateItem = new JMenuItem("编辑");
		JMenuItem deleteItem = new JMenuItem("删除");

		/* 设置信息查看的右键监听 */
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

		/* 设置虚拟终端的右键监听 */
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

		/* 设置选中状态下新建的右键监听 */
		insertItem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 新窗口打开，旧窗口设置禁用
				main.setEnabled(false);
				main.setModalExclusionType(ModalExclusionType.NO_EXCLUDE);

				new Thread() {
					public void run() {
						InsertUi insert = new InsertUi(tableModel, webshells);
					}
				}.start();
			}
		});
		
		/* 设置没选中状态下新建的右键监听 */
		insertItem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 新窗口打开，旧窗口设置禁用
				main.setEnabled(false);
				main.setModalExclusionType(ModalExclusionType.NO_EXCLUDE);

				new Thread() {
					public void run() {
						InsertUi insert = new InsertUi(tableModel, webshells);
					}
				}.start();
			}
		});

		/* 设置编辑的右键监听 */
		updateItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 新窗口打开，旧窗口设置禁用
				main.setEnabled(false);
				main.setModalExclusionType(ModalExclusionType.NO_EXCLUDE);

				int[] selection = jt.getSelectedRows();
				UpdateUi update = new UpdateUi(tableModel, webshells, selection[0]);
			}
		});

		/* 设置删除的右键监听 */
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// JOptionPane.showMessageDialog(main, "delete");
				int[] selection = jt.getSelectedRows();
				DataBase.DeleteWebShell(webshells.get(selection[0]));
				tableModel.removeRow(selection[0]);// JTable中删除改行
				tableModel.fireTableDataChanged();// 更新JTable
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

	/* 清空表格 */
	public static void clearTable() {
		DefaultTableModel cleanModel = (DefaultTableModel) jt.getModel();
		cleanModel.setRowCount(0);
	}

	// 使main窗口取消禁用
	public static void RecoverFrame() {
		main.setEnabled(true);
	}
}
