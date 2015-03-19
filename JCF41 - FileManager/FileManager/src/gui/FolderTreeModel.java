package gui;

import java.io.File;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class FolderTreeModel implements TreeModel {

    private File root;

    public FolderTreeModel(File root) {
        this.root = root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        File pathName = (File) parent;
        String[] fileNames = pathName.list();
        return new File(pathName.getPath(), fileNames[index]);
    }

    @Override
    public int getChildCount(Object parent) {
        int result = 0;
        File pathName = (File) parent;
        if (pathName.isDirectory()) {
            String[] fileNames = pathName.list();
            result = fileNames.length;
        }
        return result;
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        File pathName = (File) parent;
        File kind = (File) child;
        if (pathName.isDirectory()) {
            String[] fileNames = pathName.list();
            for (int i = 0; i < fileNames.length; i++) {
                if (pathName.compareTo(kind) == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public boolean isLeaf(Object node) {
        return ((File) node).isFile();
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        // TODO Auto-generated method stub	
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        // TODO Auto-generated method stub
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        // TODO Auto-generated method stub	
    }
}
