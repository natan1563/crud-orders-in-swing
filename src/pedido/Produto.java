package pedido;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class Produto extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField txtId;
	private JTextField txtCreatedDate;
	private JTextField txtDescription;
	private JButton btnSave;
	private JButton btnUpdate;
	private JTable tableProduto;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Produto frame = new Produto();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Produto() throws SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtId = new JTextField();
		txtId.setBounds(27, 11, 86, 20);
		contentPane.add(txtId);
		txtId.setColumns(10);
		
		JLabel lblId = new JLabel("ID");
		lblId.setBounds(10, 11, 27, 20);
		contentPane.add(lblId);
		
		JLabel lblCreatedDate = new JLabel("Data Cadastro");
		lblCreatedDate.setBounds(123, 14, 86, 14);
		contentPane.add(lblCreatedDate);
		
		txtCreatedDate = new JTextField();
		txtCreatedDate.setBounds(210, 11, 86, 20);
		contentPane.add(txtCreatedDate);
		txtCreatedDate.setColumns(10);
		
		JLabel lblDescription = new JLabel("Descrição");
		lblDescription.setBounds(10, 50, 46, 14);
		contentPane.add(lblDescription);
		
		txtDescription = new JTextField();
		txtDescription.setBounds(59, 47, 133, 20);
		contentPane.add(txtDescription);
		txtDescription.setColumns(10);
		
		JButton btnNew = new JButton("Novo");
		btnNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtDescription.setEditable(true);
                txtDescription.setText(null);
                txtId.setText(null);
                txtCreatedDate.setText(null);
                txtDescription.requestFocus();  
            }
		});
		btnNew.setBounds(199, 46, 70, 23);
		contentPane.add(btnNew);
		
		JButton btnDelete = new JButton("Excluir");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    try {
			        deleteProduct();
			    } catch (SQLException exception) {
			        exception.printStackTrace();
			    }
			}
		});
		btnDelete.setBounds(338, 46, 86, 23);
		contentPane.add(btnDelete);
		
		btnSave = new JButton("Gravar");
		btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
                try {
                    saveProduct();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
		});
		btnSave.setBounds(266, 46, 76, 23);
		contentPane.add(btnSave);
		
		btnUpdate = new JButton("Atualizar");
		btnUpdate.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) { 
		        try {
		            updateProduct();
		        } catch (SQLException exception) {
		            exception.printStackTrace();
		        }
		    }
		});
		btnUpdate.setBounds(300, 10, 90, 20);
		contentPane.add(btnUpdate);
		
		tableProduto = new JTable();
		tableProduto.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                selectedRow(); 
	            }
        });
		tableProduto.setBounds(10, 99, 414, 122);
		contentPane.add(tableProduto);
		this.listProducts();
	}
	
	private void listProducts() throws SQLException {
		ConexaoBanco conexao = new ConexaoBanco();
		Connection con = conexao.conectar();
		if (con == null) {
			JOptionPane.showMessageDialog(null, "Erro[00]: Falha ao conectar-se a base de dados");
			System.exit(0);
		} else {
			Statement stat = con.createStatement();
			ResultSet resultSet = stat.executeQuery("SELECT * FROM db_pedido.produto");
			String[] modelToRows = new String[]{"Id", "Descrição", "Pontuação"};
			DefaultTableModel modelTable = new DefaultTableModel(null, modelToRows);
			modelTable.addRow(new String[] {"Id", "Descrição", "Cadastro"});
			
			if(resultSet != null) {
				while(resultSet.next()) {
					modelTable.addRow(new String[] {
						String.valueOf(resultSet.getInt("id")),
						resultSet.getString("descricao"),
						resultSet.getString("data_cadastro")
					});
				}
			}
			tableProduto.setModel(modelTable);
		}
	}
	
	private void saveProduct() throws SQLException {
        ConexaoBanco conexao = new ConexaoBanco();
        Connection con = conexao.conectar();
        try {
            con=conexao .conectar();
            if(con ==null){
                JOptionPane.showMessageDialog(null,"Erro[00]: Falha ao conectar-se a base de dados");
                System.exit(0);
            } else {
                Statement stmt = con.createStatement();
                String query="INSERT INTO db_pedido.produto(descricao) VALUES('"+txtDescription.getText()+"')";
                stmt.executeUpdate(query);
                listProducts();
                txtDescription.setText(null);
                disableTextField();
                
            }
        } catch(Exception ex) {
            con.close();
            JOptionPane.showMessageDialog(null,"Erro[01]: Falha ao criar um novo produto"+ex.getMessage());
            System.exit(0);
        }
    }
	
	private void deleteProduct() throws SQLException{
        ConexaoBanco conexao = new ConexaoBanco();
        Connection con = conexao.conectar();
        try {
            if(con ==null) {
                JOptionPane.showMessageDialog(null,"Erro[00]: Falha ao conectar-se a base de dados");
                System.exit(0);
            } else {
                Statement stat = con.createStatement();
                String query = "DELETE FROM db_pedido.produto WHERE id = "+txtId.getText();
                stat.executeUpdate(query);
                listProducts();
            }
        } catch (Exception e) {
            con.close();
            JOptionPane.showMessageDialog(null,"Erro[04] Falha ao tentar remover este produto.");
            System.exit(0);
        }
	}
	
   private void updateProduct() throws SQLException{
        ConexaoBanco conexao = new ConexaoBanco();
        Connection con = conexao.conectar();
        txtDescription.setEditable(true);
        try {
            if(con ==null) {
                JOptionPane.showMessageDialog(null,"Erro[00]: Falha ao conectar-se a base de dados");
                System.exit(0);
            } else {
                Statement stat = con.createStatement();
                String query = "UPDATE db_pedido.produto SET descricao = '"+ txtDescription.getText() + "' WHERE id = "+txtId.getText();
                stat.executeUpdate(query);
                listProducts();
            }
        } catch (Exception e) {
            con.close();
            JOptionPane.showMessageDialog(null,"Erro[03] Falha ao atualizar o produto.");
            System.exit(0);
        }
    }
	
    private void disableTextField() {
        txtDescription.setEditable(false);
        txtId.setEditable(false);
        txtCreatedDate.setEditable(false);
    }
    
    private void selectedRow() {   
        disableTextField();
        DefaultTableModel tableModel = (DefaultTableModel) tableProduto.getModel();
        int row = tableProduto.getSelectedRow();
        if (tableModel.getValueAt(row, 0).toString()!="ID")
        {  txtId.setText(tableModel.getValueAt(row, 0).toString());
           txtDescription.setText(tableModel.getValueAt(row, 1).toString());
           txtCreatedDate.setText(tableModel.getValueAt(row, 2).toString());
        }
    }
	
}
