package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ResultFrame extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	private JScrollPane scrollPane;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ResultFrame frame = new ResultFrame();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public ResultFrame(String query, ArrayList<String> labels) {
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ResultFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ResultFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ResultFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ResultFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
		
		
		setTitle("Query Result");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		this.model = new DefaultTableModel() {

			public boolean isFocusable(int rowIndex, int mColIndex) {
				return false;
			}

			public boolean isCellSelectable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		this.model.setColumnIdentifiers(labels.toArray());
        this.table.setModel(model);
		scrollPane.setViewportView(table);
		uploadResults(query,labels);
	}

	public void uploadResults(String query, ArrayList<String> labels){
		try {
			PreparedStatement pstmt;
			try(Connection conn = DBConnector.getConnection()){
				pstmt = conn.prepareStatement(query);

//				long start = System.currentTimeMillis();
	            ResultSet result = pstmt.executeQuery();
//	            long end = System.currentTimeMillis();
//				System.out.println(1.0*(end - start)/1000);
//				System.out.println();
				while (result.next()) {
					model.setRowCount(model.getRowCount() + 1);
					for (int i = 0; i < labels.size(); i++){
						model.setValueAt(result.getObject(labels.get(i)), model.getRowCount() - 1, i);
					}
				}
			}
			pstmt.close();
			scrollPane.repaint();
			scrollPane.revalidate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
