/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package france.alsace.fl.filelister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class permit to recreate a tree with a folder/file. The aim is get the height of
 * the folder without search recursivly the height of all subfile in the folder each time.
 * It's exactly the same class as File (~), except we have add the parameter length.
 * @author Florent
 */
public class FileTree {
    private File file;
    private List<FileTree> filesList;
    private long length;
    
    /**
     * Constructor
     * @param file the initial file who permit to create the entire tree
     */
    public FileTree(File file) {
        this.file = file;
        this.filesList = new ArrayList<>();
        this.constructTree();
        this.length = -1;
    }
    
    /**
     * Search each subfile for the file in paramater of the class, and create a FileTree for each
     */
    private void constructTree() {
        if(file.isFile()) {
            filesList = null;
        } else {
            File[] files = file.listFiles();
            if(files != null) {
                for(File f : files) {
                    FileTree fb = new FileTree(f);
                    filesList.add(fb);
                }
            }
        }
    }
    
    /**
     * Permit to get, or if we don't know yet the length, to calculate it
     * @return the length of the file
     */
    public long length() {
        if(length != -1) {
            return length;
        } else {
            if(file.isFile()) {
                length = file.length();
                return length;
            } else {
                long lengthTmp = 0;
                for(FileTree fb : filesList) {
                    lengthTmp += fb.length();
                }
                length = lengthTmp;
                return length;
            }
        }
    }
    
    /**
     * Override the toString method
     * @return file.toString()
     */
    @Override
    public String toString() {
        return file.toString();
    }
     /**
      * To get the name file
      * @return file.getName()
      */
    public String getName() {
        return file.getName();
    }
    
    /**
     * To get the list of subfiles of the file
     * @return the list of the subfiles
     */
    public List<FileTree> listFiles() {
        return filesList;
    }
    
    /**
     * Permit to know if the file is a file or not (so a directory)
     * @return true if it's a file, false otherwise
     */
    public boolean isFile() {
        return file.isFile();
    }

}
