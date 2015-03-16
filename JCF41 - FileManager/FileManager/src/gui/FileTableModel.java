package gui;

import java.io.File;
import java.util.Date;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;

public class FileTableModel extends AbstractTableModel {

    private static final int NAME = 0;
    private static final int SIZE = 1;
    private static final int TYPE = 2;
    private static final int MODIFIED = 3;
    protected static FileSystemView fsv = FileSystemView.getFileSystemView();
    private File pathName;
    private String[] columnNames = {"Name", "Size", "Type", "Modified"};

    public FileTableModel(File pathName) {
        this.pathName = pathName;
    }

    //@Override
    public String getColumnName(int c) {
        return columnNames[c];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        if (pathName.isDirectory()) {
            return pathName.list().length;
        }
        return 1;
    }

    @Override
    public Object getValueAt(int row, int col) {
        File targetFile = pathName;
        Object r = null;

        if (pathName.isDirectory()) {
            String[] fileNames = pathName.list();
            targetFile = new File(pathName.getPath(), fileNames[row]);
        }

        switch (col) {
            case NAME:
                r = targetFile;
                break;
            case SIZE:
                r = convertFileStorage(targetFile.length());
                break;
            case TYPE:
                r = fsv.getSystemTypeDescription(targetFile);
                break;
            case MODIFIED:
                r = new Date(targetFile.lastModified());
                break;
        }
        return r;
    }

    public void setpathName(File pathName) {
        this.pathName = pathName;
        fireTableDataChanged();
    }

    private String convertFileStorage(long bytes) {
        long kb = bytes / 1024;
        long mb = kb / 1024;
        long gb = mb / 1024;
        if (gb > 0) {
            return gb + "Gb";
        }
        if (mb > 0) {
            return mb + "Mb";
        }
        if (kb > 0) {
            return kb + "Kb";
        }
        if (bytes > 0) {
            return bytes + "";
        }
        return "";
    }
}
