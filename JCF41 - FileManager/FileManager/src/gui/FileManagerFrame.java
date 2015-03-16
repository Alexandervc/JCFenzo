package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.SwingUtilities;
import util.OSValidator;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class FileManagerFrame extends javax.swing.JFrame {

    private JPanel pnSouth;
    private JTable tbFiles;
    private JTree trFolders;
    private JPanel pnCenter;
    private FileTableModel fileTableModel;
    private FolderTreeModel folderTreeModel;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    public static int DRIVE_NR;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    //    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FileManagerFrame inst = new FileManagerFrame();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public FileManagerFrame() {
        super("File Manager");
        initVariables();
        initGUI();
    }

    private void initGUI() {
        try {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            {
                pnSouth = new JPanel();
                GridLayout pnSouthLayout = new GridLayout(1, 2);
                pnSouthLayout.setColumns(2);
                pnSouthLayout.setHgap(5);
                pnSouthLayout.setVgap(5);
                getContentPane().add(pnSouth, BorderLayout.SOUTH);
                pnSouth.setLayout(pnSouthLayout);
            }
            {
                pnCenter = new JPanel();
                GridLayout pnCenterLayout = new GridLayout(1, 1);
                pnCenterLayout.setHgap(5);
                pnCenterLayout.setVgap(5);
                pnCenterLayout.setColumns(1);
                getContentPane().add(pnCenter, BorderLayout.CENTER);
                pnCenter.setLayout(pnCenterLayout);
                jScrollPane2 = new JScrollPane();
                pnCenter.add(jScrollPane2);
                jScrollPane2.setPreferredSize(new java.awt.Dimension(158, 221));
                {
                    initFolderTree();
                    jScrollPane2.setViewportView(trFolders);
                }
                {
                    jScrollPane1 = new JScrollPane();
                    pnCenter.add(jScrollPane1);
                    jScrollPane1.setPreferredSize(new java.awt.Dimension(158, 221));
                    {
                        initFileTable();
                        jScrollPane1.setViewportView(tbFiles);
                        tbFiles.setFillsViewportHeight(true);
                    }
                }
            }
            pack();
            setSize(400, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFolderTree() {
        File[] drives = File.listRoots();
        folderTreeModel = new FolderTreeModel(drives[DRIVE_NR]);
        trFolders = new JTree(folderTreeModel);
        trFolders.putClientProperty("JTree.lineSyle", "None");
        trFolders.setRootVisible(true);
        trFolders.setShowsRootHandles(true);
        trFolders.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        trFolders.setCellRenderer(new FileNameCellRenderer());

        trFolders.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent arg0) {
                File node = (File) trFolders.getLastSelectedPathComponent();
                if (node == null) {
                    return;
                }
                fileTableModel.setpathName(node);
            }
        });
    }

    private void initFileTable() {
        File[] drives = File.listRoots();
        fileTableModel = new FileTableModel(drives[DRIVE_NR]);
        tbFiles = new JTable();
        tbFiles.setModel(fileTableModel);
        tbFiles.setDefaultRenderer(File.class, new FileCellRenderer());
        tbFiles.setShowHorizontalLines(true);
        tbFiles.setShowVerticalLines(true);
        tbFiles.setPreferredScrollableViewportSize(tbFiles.getPreferredSize());
        tbFiles.setPreferredSize(new java.awt.Dimension(175, 226));
        tbFiles.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent evt) {
                tbFilesMousePressed(evt);
            }
        });

        tbFiles.setGridColor(getBackground());
        tbFiles.setPreferredSize(new java.awt.Dimension(190, 223));
    }

    private void initVariables() {
        DRIVE_NR = OSValidator.isWindows() ? 1 : 0;
    }

    private void tbFilesMousePressed(MouseEvent evt) {
        File selectedFile = (File) fileTableModel.getValueAt(tbFiles.getSelectedRow(), 0);
        if (selectedFile.isDirectory()) {
            fileTableModel.setpathName(selectedFile);
        }
    }
}
