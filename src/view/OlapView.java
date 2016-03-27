package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OlapView {

	private JFrame frmAquaticProduce;
	private QueryBuilder query;
	private JTextField locationTextField;
	private JTextField typeTextField;
	private JTextField equipmentTextField;
	private int lastLocation;
	private final String[] keys = new String[]{"hpq_hh.mun","hpq_hh.brgy","hpq_hh.id","hpq_aquani.aquani_type","hpq_aquaequip.equip_type"};
	private final String[] labels = new String[]{"volume", "municipality", "barangay", "household", "aquani_type", "equipment_type"};
	private ArrayList<String> columnLabels;
	
	public OlapView() {
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OlapView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OlapView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OlapView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OlapView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
		columnLabels = new ArrayList();
		query = new QueryBuilder()
					.addSelect("sum(total_aquani_volume) as " + labels[0])
					.addFrom("cube")
					.addCondition("hpq_hh_id", "null")
					.addCondition("hpq_aquani_id", "null")
					.addCondition("hpq_aquaequip_id", "null");
		columnLabels.add(labels[0]);
		initialize();
		this.frmAquaticProduce.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAquaticProduce = new JFrame();
		frmAquaticProduce.setTitle("Aquatic Produce");
		frmAquaticProduce.setBounds(100, 100, 650, 485);
		frmAquaticProduce.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("Drilldown/Rollup/Slice/Dice");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JButton executeButton = new JButton("Execute");
		executeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DefaultDAO dao = new DefaultDAO();
				new ResultFrame(query.build(), columnLabels).setVisible(true);
			}
		});
		
		JScrollPane queryScrollPane = new JScrollPane();
		
		JLabel lblNewLabel_1 = new JLabel("Location Dimension");
		
		JLabel lblNewLabel_2 = new JLabel("Aquani Type Dimension");
		
		JLabel lblAquaniEquipmentDimension = new JLabel("Aquani Equipment Dimension");
		JTextPane tpQuery = new JTextPane();
		tpQuery.setText(query.build());
		queryScrollPane.setViewportView(tpQuery);
		
		JComboBox locationComboBox = new JComboBox();
		lastLocation = 0;
		locationComboBox.setModel(new DefaultComboBoxModel(new String[] {"None", "Municipality", "Barangay", "Household"}));
		locationComboBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(lastLocation != 0){
					if(lastLocation > locationComboBox.getSelectedIndex()){
						query.removeGrouping(keys[lastLocation-1]);
						query.removeSelect(keys[lastLocation-1] + " as " + labels[lastLocation + 1]);
						columnLabels.remove(labels[lastLocation + 1]);
					}
					query.addCondition("hpq_hh_id", "null");
					query.removeCondition(keys[lastLocation-1]);
				}else{
					query.addFrom("hpq_hh");
				}
				if(locationComboBox.getSelectedIndex() != 0){
					query.removeCondition("hpq_hh_id");
					query.addGrouping(keys[locationComboBox.getSelectedIndex()-1]);
					query.addSelect(keys[locationComboBox.getSelectedIndex()-1] + " as " + labels[lastLocation + 1]);
					columnLabels.add(labels[lastLocation + 1]);
				}else{
					query.removeFrom("hpq_hh");
				}
                tpQuery.setText(query.build());
                lastLocation = locationComboBox.getSelectedIndex();
			}
        });
		JLabel lblNewLabel_3 = new JLabel("Value");
		
		locationTextField = new JTextField();
		locationTextField.setColumns(10);
		
		JButton addLocationButton = new JButton("Add Condition");
		addLocationButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!locationTextField.getText().equals("") && !((String)locationComboBox.getSelectedItem()).equals("None")){
					query.addCondition(keys[locationComboBox.getSelectedIndex()-1], locationTextField.getText());
	                tpQuery.setText(query.build());
				}
			}
		});
		
		JComboBox typeComboBox = new JComboBox();
		typeComboBox.setModel(new DefaultComboBoxModel(new String[] {"None", "Aquani Type"}));
		typeComboBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(typeComboBox.getSelectedIndex() != 0){
					query.addFrom("hpq_aquani");
					query.removeCondition("hpq_aquani_id");
	                query.addGrouping(keys[3]);
					query.addSelect(keys[3] + " as " + labels[4]);
					columnLabels.add(labels[4]);
				}else{
					query.removeFrom("hpq_aquani");
					query.addCondition("hpq_aquani_id", "null");
					query.removeCondition(keys[3]);
	                query.removeGrouping(keys[3]);
					query.removeSelect(keys[3] + " as " + labels[4]);
					columnLabels.remove(labels[4]);
				}
                tpQuery.setText(query.build());
			}
        });
		typeTextField = new JTextField();
		typeTextField.setColumns(10);
		
		JLabel label = new JLabel("Value");
		
		JButton addTypeButton = new JButton("Add Condition");
		addTypeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!typeTextField.getText().equals("") && !((String)typeComboBox.getSelectedItem()).equals("None")){
					query.addCondition("hpq_aquani.id", typeTextField.getText());
	                tpQuery.setText(query.build());
				}
			}
		});
		
		equipmentTextField = new JTextField();
		equipmentTextField.setColumns(10);
		
		JLabel label_1 = new JLabel("Value");
		
		JComboBox equipmentComboBox = new JComboBox();
		equipmentComboBox.setModel(new DefaultComboBoxModel(new String[] {"None", "Aquani Equipment Type"}));
		equipmentComboBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(equipmentComboBox.getSelectedIndex() != 0){
					query.addFrom("hpq_aquaequip");
					query.removeCondition("hpq_aquaequip_id");
	                query.addGrouping(keys[4]);
					query.addSelect(keys[4] + " as " + labels[5]);
					columnLabels.add(labels[5]);
				}else{
					query.removeFrom("hpq_aquaniequip");
					query.addCondition("hpq_aquaequip_id", "null");
	                query.removeGrouping(keys[4]);
					query.removeCondition(keys[4]);
					query.removeSelect(keys[4] + " as " + labels[5]);
					columnLabels.remove(labels[5]);
				}
                tpQuery.setText(query.build());
			}
        });
		

		JButton addEquipmentButton = new JButton("Add Condition");
		addEquipmentButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!equipmentTextField.getText().equals("") && !((String)equipmentComboBox.getSelectedItem()).equals("None")){
					query.addCondition("hpq_aquaequip.id", equipmentTextField.getText());
	                tpQuery.setText(query.build());
				}
			}
		});
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				query = new QueryBuilder()
						.addSelect("sum(total_aquani_volume) as " + labels[0])
						.addFrom("cube")
						.addCondition("hpq_hh_id", "null")
						.addCondition("hpq_aquani_id", "null")
						.addCondition("hpq_aquaequip_id", "null");
				columnLabels.clear();
				columnLabels.add(labels[0]);
				tpQuery.setText(query.build());
				locationComboBox.setSelectedIndex(0);
				typeComboBox.setSelectedIndex(0);
				equipmentComboBox.setSelectedIndex(0);
			}
        });
		GroupLayout groupLayout = new GroupLayout(frmAquaticProduce.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_1)
						.addComponent(lblNewLabel_2)
						.addComponent(lblAquaniEquipmentDimension)
						.addComponent(equipmentComboBox, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(locationComboBox, Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(typeComboBox, Alignment.LEADING, 0, 174, Short.MAX_VALUE))
						.addComponent(lblNewLabel)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblNewLabel_3, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(locationTextField)
								.addComponent(addLocationButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(typeTextField)
								.addComponent(addTypeButton, GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
							.addGroup(groupLayout.createSequentialGroup()
								.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(equipmentTextField))
							.addGroup(groupLayout.createSequentialGroup()
								.addGap(40)
								.addComponent(addEquipmentButton, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE))))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(166)
							.addComponent(executeButton)
							.addGap(18)
							.addComponent(btnClear))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(30)
							.addComponent(queryScrollPane, GroupLayout.PREFERRED_SIZE, 401, GroupLayout.PREFERRED_SIZE)))
					.addGap(19))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblNewLabel)
							.addGap(11)
							.addComponent(lblNewLabel_1)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(locationComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel_3)
								.addComponent(locationTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(addLocationButton)
							.addGap(18)
							.addComponent(lblNewLabel_2)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(typeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(9)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(label)
								.addComponent(typeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(addTypeButton)
							.addGap(21)
							.addComponent(lblAquaniEquipmentDimension)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(equipmentComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(9)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(label_1)
								.addComponent(equipmentTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(queryScrollPane))
					.addGap(9)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(addEquipmentButton)
						.addComponent(executeButton)
						.addComponent(btnClear))
					.addContainerGap())
		);
		frmAquaticProduce.getContentPane().setLayout(groupLayout);
	}
}
