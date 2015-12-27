/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ave;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author rzl
 */
public class testing extends javax.swing.JFrame {

    /**
     * Creates new form testing
     */
    LinkedList<Ave> mk = new LinkedList<>();
    LinkedList<Eva> pb = new LinkedList<>();
    LinkedList<Aev> sl = new LinkedList<>();
    private int index_mk = 0;
    
    Connection con;
    Statement state;
    ResultSet set;
    
    public testing() {
        initComponents();
        
        String serverName = "localhost";
        String myDatabase = "ave";
        String url = "jdbc:mysql://" + serverName + "/" + myDatabase; //a JDBC url
        String uname = "root";
        String pass = "";
        String driverName = "org.gjt.mm.mysql.Driver"; //JDBC
        
        try {
            Class.forName(driverName);
            con = DriverManager.getConnection(url, uname, pass);
            System.out.println("Connected");
            
            String query = "SELECT * FROM mata_kuliah";
        
            state = con.createStatement();
            set = state.executeQuery(query);
            
            while (set.next()) {                
                mk.add(new Ave(
                        set.getInt(1),
                        set.getString(2)));
                int id = set.getInt("id");
                String name = set.getString("matkul");
                combo_matkul.addItem(name);
            }
            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Not Connected");
        }
        
        showMatkul();
        parsePokba(tbl_matkul, "pokok_bahasan", "mata_kuliah", "id_matkul", "matkul");
        parseSoal();
    }
    
    void showMatkul() {
        for (int i = 0; i < mk.size(); i++) {
            ((DefaultTableModel)this.tbl_matkul.getModel()).setRowCount(i+1);
            tbl_matkul.setValueAt(mk.get(i).getMatkul(), i, 0);
        }
    }
    
    void showPokba() {
        ((DefaultTableModel)this.tbl_pokba.getModel()).setRowCount(0);
        for (int i = 0; i < pb.size(); i++) {
            ((DefaultTableModel)this.tbl_pokba.getModel()).setRowCount(i+1);
            tbl_pokba.setValueAt(pb.get(i).getPokba(), i, 0);
        }
    }
    
    void showSoal() {
        ((DefaultTableModel)this.tbl_soal.getModel()).setRowCount(0);
        for (int i = 0; i < sl.size(); i++) {
            ((DefaultTableModel)this.tbl_soal.getModel()).setRowCount(i+1);
            tbl_soal.setValueAt(sl.get(i).getDesk(), i, 0);
            tbl_soal.setValueAt(sl.get(i).getJwbn_a(), i, 1);
            tbl_soal.setValueAt(sl.get(i).getJwbn_b(), i, 2);
            tbl_soal.setValueAt(sl.get(i).getJwbn_c(), i, 3);
            tbl_soal.setValueAt(sl.get(i).getJwbn_d(), i, 4);
            tbl_soal.setValueAt(sl.get(i).getKunci_jwbn(), i, 5);
        }
    }
    
    void parsePokba(final JTable tabel, final String tabel_1, final String tabel_2, final String id_1, final String id_2) {
        
        
        MouseListener selCell;
        selCell = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pb.clear();
                int row = tabel.rowAtPoint(e.getPoint());
                int col = tabel.columnAtPoint(e.getPoint());
                String value = tabel.getValueAt(row, col).toString();
                
                j_text.setText(value);
                
                String query = "SELECT * FROM "+tabel_1+" JOIN "+tabel_2+" WHERE "+tabel_2+".id="+tabel_1+"."+id_1+" AND "+tabel_2+"."+id_2+"='"+value+"'";
                try {
                    state = con.createStatement();
                    set = state.executeQuery(query);
                    combo_pokba.removeAllItems();
                    while(set.next()) {
                        pb.add(new Eva(
                                set.getInt(1),
                                set.getString(2),
                                set.getInt(3)
                        ));
                        String name = set.getString("pokba");
                        combo_pokba.addItem(name);
                    }
                    //System.out.println("tos");
                } catch (SQLException ex) {
                    
                    //System.out.println("tis");
                }
                
                showPokba();
                
            }
        };
        tabel.addMouseListener(selCell);
    }
    
    void parseSoal() {
        MouseListener selectedCell;
        selectedCell = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sl.clear();
                int row = tbl_pokba.rowAtPoint(e.getPoint());
                int col = tbl_pokba.columnAtPoint(e.getPoint());
                String value = tbl_pokba.getValueAt(row, col).toString();
                
                pokba_text.setText(value);
                
                String query = "SELECT * FROM soal JOIN pokok_bahasan WHERE pokok_bahasan.id=soal.id_pokba AND pokok_bahasan.pokba='"+value+"'";
                try {
                    state = con.createStatement();
                    set = state.executeQuery(query);
                                        
                    while (set.next()) {                        
                        sl.add(new Aev(
                                set.getInt(1),
                                set.getString(2),
                                set.getString(3),
                                set.getString(4),
                                set.getString(5),
                                set.getString(6),
                                set.getString(7),
                                set.getInt(8)
                        ));
                    }
                    //System.out.println("kos");
                    
                } catch (SQLException ex) {
                    Logger.getLogger(testing.class.getName()).log(Level.SEVERE, null, ex);
                    //System.out.println("kis");
                }
                showSoal();
            }
        };
        tbl_pokba.addMouseListener(selectedCell);
    }    
    
    void addMatkul(final String input, final String tabel, final String kolom) throws SQLException {
        String query = "INSERT INTO "+tabel+"("+kolom+")"+" VALUES (?)";
        
        PreparedStatement ins = con.prepareStatement(query);
        ins.setString(1, input);
        ins.execute();
    }
    
    void addPokba(final String input, final int k) throws SQLException {
        
        //System.out.println(k);
        String query = "INSERT INTO pokok_bahasan(`pokba`, `id_matkul`)"+" VALUES (?,?)";
        
        PreparedStatement ins = con.prepareStatement(query);
        ins.setString(1, input);
        ins.setInt(2, k);
        ins.execute();
    }
    
    void addSoal(String desk, String ja, String jb, String jc, String jd, String kunci, int k) throws SQLException {
        //System.out.println(k);
        String query = "INSERT INTO soal(`deskripsi`, `j_a`, `j_b`, `j_c`, `j_d`, `kunci`, `id_pokba`) "+"VALUES (?,?,?,?,?,?,?)";
        
        PreparedStatement ins = con.prepareStatement(query);
        ins.setString(1, desk);
        ins.setString(2, ja);
        ins.setString(3, jb);
        ins.setString(4, jc);
        ins.setString(5, jd);
        ins.setString(6, kunci);
        ins.setInt(7, k);
        ins.execute();
    }
    
    void newMatkul() {
        j_text.setText("");
    }
    
    void newPokba() {
        pokba_text.setText("");
    }
    
    void newSoal() {
        inp_desk.setText("");
        inp_a.setText("");
        inp_b.setText("");
        inp_c.setText("");
        inp_d.setText("");
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pilihan_jawaban = new javax.swing.ButtonGroup();
        tab = new javax.swing.JTabbedPane();
        panel_matkul = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_matkul = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_pokba = new javax.swing.JTable();
        j_save = new javax.swing.JButton();
        j_update = new javax.swing.JButton();
        j_view = new javax.swing.JButton();
        j_text = new javax.swing.JTextField();
        j_new = new javax.swing.JButton();
        pokba_save = new javax.swing.JButton();
        pokba_update = new javax.swing.JButton();
        pokba_view = new javax.swing.JButton();
        pokba_text = new javax.swing.JTextField();
        pokba_new = new javax.swing.JButton();
        combo_matkul = new javax.swing.JComboBox<>();
        panel_soal = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_soal = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        inp_desk = new javax.swing.JTextArea();
        inp_a = new javax.swing.JTextField();
        inp_b = new javax.swing.JTextField();
        inp_c = new javax.swing.JTextField();
        inp_d = new javax.swing.JTextField();
        soal_new = new javax.swing.JButton();
        soal_save = new javax.swing.JButton();
        soal_update = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        combo_pokba = new javax.swing.JComboBox<>();
        combo_kunci = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tbl_matkul.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Matkul"
            }
        ));
        tbl_matkul.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_matkulMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_matkul);

        tbl_pokba.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Pokba"
            }
        ));
        jScrollPane2.setViewportView(tbl_pokba);

        j_save.setText("Save");
        j_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j_saveActionPerformed(evt);
            }
        });

        j_update.setText("update");
        j_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j_updateActionPerformed(evt);
            }
        });

        j_view.setText("view");
        j_view.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j_viewActionPerformed(evt);
            }
        });

        j_new.setText("New");
        j_new.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j_newActionPerformed(evt);
            }
        });

        pokba_save.setText("Save");
        pokba_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pokba_saveActionPerformed(evt);
            }
        });

        pokba_update.setText("update");
        pokba_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pokba_updateActionPerformed(evt);
            }
        });

        pokba_view.setText("view");
        pokba_view.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pokba_viewActionPerformed(evt);
            }
        });

        pokba_new.setText("New");
        pokba_new.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pokba_newActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_matkulLayout = new javax.swing.GroupLayout(panel_matkul);
        panel_matkul.setLayout(panel_matkulLayout);
        panel_matkulLayout.setHorizontalGroup(
            panel_matkulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_matkulLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_matkulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel_matkulLayout.createSequentialGroup()
                        .addComponent(j_text, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_matkulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panel_matkulLayout.createSequentialGroup()
                                .addComponent(j_update, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(j_new))
                            .addGroup(panel_matkulLayout.createSequentialGroup()
                                .addComponent(j_view, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(j_save)))))
                .addGap(18, 18, 18)
                .addGroup(panel_matkulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel_matkulLayout.createSequentialGroup()
                        .addGroup(panel_matkulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(combo_matkul, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pokba_text, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panel_matkulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(panel_matkulLayout.createSequentialGroup()
                                .addComponent(pokba_view, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pokba_save))
                            .addGroup(panel_matkulLayout.createSequentialGroup()
                                .addComponent(pokba_update, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pokba_new)))))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        panel_matkulLayout.setVerticalGroup(
            panel_matkulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_matkulLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_matkulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel_matkulLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addGroup(panel_matkulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(j_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel_matkulLayout.createSequentialGroup()
                                .addGroup(panel_matkulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(j_new)
                                    .addComponent(j_update))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel_matkulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(j_save)
                                    .addComponent(j_view)))))
                    .addGroup(panel_matkulLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addGroup(panel_matkulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panel_matkulLayout.createSequentialGroup()
                                .addComponent(pokba_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(combo_matkul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_matkulLayout.createSequentialGroup()
                                .addGroup(panel_matkulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(pokba_new)
                                    .addComponent(pokba_update))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel_matkulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(pokba_save)
                                    .addComponent(pokba_view))))))
                .addContainerGap(203, Short.MAX_VALUE))
        );

        tab.addTab("Matkul & Pokba", panel_matkul);

        tbl_soal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Deskripsi", "Pilihan A", "Pilihan B", "Pilihan C", "Pilihan D", "Kunci Jawaban"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tbl_soal);

        inp_desk.setColumns(20);
        inp_desk.setRows(5);
        jScrollPane4.setViewportView(inp_desk);

        inp_d.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inp_dActionPerformed(evt);
            }
        });

        soal_new.setText("New");
        soal_new.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soal_newActionPerformed(evt);
            }
        });

        soal_save.setText("Save");
        soal_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soal_saveActionPerformed(evt);
            }
        });

        soal_update.setText("Update");

        jButton4.setText("?");

        combo_kunci.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "a", "b", "c", "d" }));

        jLabel1.setText("A");

        jLabel2.setText("B");

        jLabel3.setText("C");

        jLabel4.setText("D");

        javax.swing.GroupLayout panel_soalLayout = new javax.swing.GroupLayout(panel_soal);
        panel_soal.setLayout(panel_soalLayout);
        panel_soalLayout.setHorizontalGroup(
            panel_soalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_soalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_soalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(panel_soalLayout.createSequentialGroup()
                        .addGroup(panel_soalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                            .addComponent(combo_pokba, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_soalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panel_soalLayout.createSequentialGroup()
                                .addGroup(panel_soalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(panel_soalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel_soalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(inp_b, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(inp_d, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panel_soalLayout.createSequentialGroup()
                                        .addComponent(inp_a, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(combo_kunci, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(panel_soalLayout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inp_c, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(68, 68, 68))))
                    .addGroup(panel_soalLayout.createSequentialGroup()
                        .addComponent(soal_new, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(soal_save, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(soal_update, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_soalLayout.setVerticalGroup(
            panel_soalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_soalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panel_soalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_soalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inp_a, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(combo_kunci, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1))
                    .addComponent(combo_pokba, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_soalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panel_soalLayout.createSequentialGroup()
                        .addGroup(panel_soalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inp_b, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_soalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inp_c, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_soalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inp_d, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addComponent(jScrollPane4))
                .addGap(18, 46, Short.MAX_VALUE)
                .addGroup(panel_soalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(soal_new)
                    .addComponent(soal_save)
                    .addComponent(soal_update)
                    .addComponent(jButton4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tab.addTab("Soal", panel_soal);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tab)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tab)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void j_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j_updateActionPerformed
        
    }//GEN-LAST:event_j_updateActionPerformed

    private void tbl_matkulMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_matkulMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tbl_matkulMouseClicked

    private void j_viewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j_viewActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_j_viewActionPerformed

    private void inp_dActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inp_dActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inp_dActionPerformed

    private void pokba_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pokba_updateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pokba_updateActionPerformed

    private void pokba_viewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pokba_viewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pokba_viewActionPerformed

    private void j_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j_saveActionPerformed
        // TODO add your handling code here:
        String matkul_get = j_text.getText();
        String tabel = "mata_kuliah";
        String kolom = "matkul";
        try {
            addMatkul(matkul_get, tabel, kolom);
            
            Ave inp = new Ave();
            inp.setMatkul(matkul_get);
            this.mk.add(inp);
            showMatkul();
            System.out.println("berhasil");
        } catch (SQLException ex) {
            Logger.getLogger(testing.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("gagal");
        }
    }//GEN-LAST:event_j_saveActionPerformed

    private void pokba_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pokba_saveActionPerformed
        // TODO add your handling code here:
        String val = (String) combo_matkul.getSelectedItem();
        String pokba_get = pokba_text.getText();
        
        try {
            int index = 0;
            String query = "SELECT id FROM mata_kuliah WHERE matkul='"+val+"'";
            set = state.executeQuery(query);
            if (set.next()) {
                index = set.getInt(1);
            }
            addPokba(pokba_get, index);
            
            Eva inp = new Eva();
            inp.setPokba(pokba_get);
            this.pb.add(inp);
            showPokba();
            System.out.println("berhasil");
        } catch (SQLException ex) {
            Logger.getLogger(testing.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("gagal");
        }
    }//GEN-LAST:event_pokba_saveActionPerformed

    private void soal_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soal_saveActionPerformed
        // TODO add your handling code here:
        String val = (String) combo_pokba.getSelectedItem();
        String desk_get = inp_desk.getText();
        String ja_get = inp_a.getText();
        String jb_get = inp_b.getText();
        String jc_get = inp_c.getText();
        String jd_get = inp_d.getText();
        String kunci = (String) combo_kunci.getSelectedItem();
        int id_pokba = 0;
        
        try {
            String query = "SELECT id FROM pokok_bahasan WHERE pokba='"+val+"'";
            set = state.executeQuery(query);
            if (set.next()) {
                id_pokba = set.getInt(1);
            }
            addSoal(desk_get, ja_get, jb_get, jc_get, jd_get, kunci, id_pokba);
            
            Aev inp = new Aev();
            inp.setDesk(desk_get);
            inp.setJwbn_a(ja_get);
            inp.setJwbn_b(jb_get);
            inp.setJwbn_c(jc_get);
            inp.setJwbn_d(jd_get);
            inp.setKunci_jwbn(kunci);
            this.sl.add(inp);
            showSoal();
        } catch (SQLException ex) {
            Logger.getLogger(testing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_soal_saveActionPerformed

    private void j_newActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j_newActionPerformed
        // TODO add your handling code here:
        newMatkul();
    }//GEN-LAST:event_j_newActionPerformed

    private void pokba_newActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pokba_newActionPerformed
        // TODO add your handling code here:
        newPokba();
    }//GEN-LAST:event_pokba_newActionPerformed

    private void soal_newActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soal_newActionPerformed
        // TODO add your handling code here:
        newSoal();
    }//GEN-LAST:event_soal_newActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(testing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(testing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(testing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(testing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new testing().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> combo_kunci;
    private javax.swing.JComboBox<String> combo_matkul;
    private javax.swing.JComboBox<String> combo_pokba;
    private javax.swing.JTextField inp_a;
    private javax.swing.JTextField inp_b;
    private javax.swing.JTextField inp_c;
    private javax.swing.JTextField inp_d;
    private javax.swing.JTextArea inp_desk;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton j_new;
    private javax.swing.JButton j_save;
    private javax.swing.JTextField j_text;
    private javax.swing.JButton j_update;
    private javax.swing.JButton j_view;
    private javax.swing.JPanel panel_matkul;
    private javax.swing.JPanel panel_soal;
    private javax.swing.ButtonGroup pilihan_jawaban;
    private javax.swing.JButton pokba_new;
    private javax.swing.JButton pokba_save;
    private javax.swing.JTextField pokba_text;
    private javax.swing.JButton pokba_update;
    private javax.swing.JButton pokba_view;
    private javax.swing.JButton soal_new;
    private javax.swing.JButton soal_save;
    private javax.swing.JButton soal_update;
    private javax.swing.JTabbedPane tab;
    private javax.swing.JTable tbl_matkul;
    private javax.swing.JTable tbl_pokba;
    private javax.swing.JTable tbl_soal;
    // End of variables declaration//GEN-END:variables
}
