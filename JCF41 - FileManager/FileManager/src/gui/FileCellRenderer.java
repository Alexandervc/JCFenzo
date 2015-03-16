package gui;

import java.awt.Component;
import java.io.File;
import javax.swing.JTable;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableCellRenderer;

public class FileCellRenderer extends javax.swing.table.DefaultTableCellRenderer
        implements TableCellRenderer {

    protected static FileSystemView fsv = FileSystemView.getFileSystemView();

    @Override
    public Component getTableCellRendererComponent(JTable arg0, Object arg1,
            boolean arg2, boolean arg3, int arg4, int arg5) {



        super.getTableCellRendererComponent(
                arg0, arg1, arg2,
                arg3, arg4, arg5);
        File f = (File) arg1;
        this.setText(f.getName());
        this.setIcon(fsv.getSystemIcon(f));
        return this;
    }
}
