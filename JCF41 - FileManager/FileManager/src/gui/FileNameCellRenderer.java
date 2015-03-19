package gui;

import java.awt.Component;
import java.io.File;
import java.util.StringTokenizer;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;

public class FileNameCellRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = 1L;
    protected static FileSystemView fsv = FileSystemView.getFileSystemView();
    private String delim = File.separator;

    @Override
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean sel,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {

        super.getTreeCellRendererComponent(
                tree, value, sel,
                expanded, leaf, row,
                hasFocus);


        // DefaultMutableTreeNode node = (DefaultMutableTreeNode) value; 

        File pathName = (File) value;


        StringTokenizer tokennizer = new StringTokenizer(pathName.getPath(), delim);
        String output = "";
        while (tokennizer.hasMoreTokens()) {
            output = (tokennizer.nextToken().toLowerCase());
        }
        setText(output);
        this.setIcon(fsv.getSystemIcon(pathName));

        return this;
    }
}
