/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package france.alsace.fl.filelister;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class permit to list all files and folders in a main folder, and put it in a external file
 * @author Florent
 */
public class Lister {
    
    // flag for type
    public static final int TXT = 1;
    public static final int HTML = 2;
    
    // extensions
    //txt
    public static final int E_TXT = 1;
    //torrent
    public static final int E_TORRENT = 2;
    //images
    public static final int E_JPG = 3;
    public static final int E_JPEG = 4;
    public static final int E_PNG = 5;
    public static final int E_GIF = 6;
    public static final int E_BMP = 7;
    //word
    public static final int E_DOC = 8;
    public static final int E_DOCX = 9;
    //excel
    public static final int E_XLS = 10;
    public static final int E_XLSX = 11;
    //powerpoint
    public static final int E_PPS = 12;
    public static final int E_PPT = 13;
    public static final int E_PPTX = 14;
    //video
    public static final int E_AVI = 15;
    public static final int E_MKV = 16;
    public static final int E_WMV = 17;
    public static final int E_MOV = 18;
    //pdf
    public static final int E_PDF = 19;
    
    private Writer fw;
    private final String EOL = System.getProperty("line.separator");
    private String columnSeparator = "|";
    private int numberOfFile = 0;
    private int numberOfFolder = 0;
    
    /**
     * List all the subfolders and files in the main folder
     * @param folder    the folder for recursive search
     * @param output    the file to write in
     * @param doErase   erase the output file if exist or continue to fill in the same file 
     * @param type      type of output file
     * @param separator indicate if the separator beetween column appear
     */
    public void execute(File folder, File output, boolean doErase, int type, boolean separator) {
        
        //reste numbers of file and folder
        this.numberOfFile = 0;
        this.numberOfFolder = 0;
        
        // display the right separator
        if(separator) {
            this.columnSeparator = "|";
        } else {
            if(type==Lister.TXT) {
                this.columnSeparator = " ";
            } else if(type==Lister.HTML) {
                this.columnSeparator = "&nbsp;";
            }
        }
        
        try {
            //choose if we need to erase the old data or keep it
            if(output.exists()) {
                fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output, !doErase), "UTF-8"));
            } else {
                fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF-8"));
            }
            
            try {
                if(type==Lister.TXT) {
                    appendToFileEOL(folder.toString());
                    appendToFileEOL("# " + folder.getName());
                } else if(type==Lister.HTML) {
                    appendToFileEOL("<!doctype html>");
                    appendToFileEOL("<html lang=\"fr\">");
                    appendToFileEOL("<head>");
                    appendToFileEOL("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
                    appendToFileEOL("<style type=\"text/css\">");
                    appendToFileEOL(".img { height: 20px; vertical-align: bottom; margin: 1px 0px 1px 0px; }");
                    appendToFileEOL(".blank { margin-left: 20px; }");
                    appendToFileEOL("</style>");
                    appendToFileEOL("<title>Liste des dossiers et fichiers dans " + folder.toString() + "</title>");
                    appendToFileEOL("</head>");
                    appendToFileEOL("<body>");
                    appendToFileEOL(folder.toString() + "<br />");
                    appendToFileEOL("<img src=\"img/folder.png\" class=\"img\" />&nbsp;" + folder.getName() + "<br />");

                }
                
                listFolder(folder, 1, type); // tab parameter is not optional because of the recursivity
            } finally {
                if(type==Lister.TXT) {
                    
                } else if(type==Lister.HTML) {
                    appendToFileEOL("</body>");
                    appendToFileEOL("</html>");
                }
                fw.flush();
                fw.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Lister.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Add a string in the file and make \r\n (Carriage Return and Line Feed)
     * @param s     the string to add in the file
     */
    private void appendToFileEOL(String s) {
        try {
            fw.write(s + EOL);
        } catch (IOException ex) {
            Logger.getLogger(Lister.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Add a string in the file
     * @param s     the string to add in the file
     */
    private void appendToFile(String s) {
        try {
            fw.write(s);
        } catch (IOException ex) {
            Logger.getLogger(Lister.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Recursive method to list all the folders and files in the main folder
     * @param folder    folder to search in
     * @param tab       number of tabulation which indicate the depth of the file
     * @param type      type of output file
     */
    private void listFolder(File folder, int tab, int type) {
        
        int tab2 = tab+1;
        File[] list = folder.listFiles();
        if(list != null) {
            for(File f : list) {
                if(f.isFile()) { // if the File is a file
                    //display the right number of "blank"
                    for(int i = 0; i<tab; i++) {
                        if(type==Lister.TXT) {
                            appendToFile(columnSeparator + "   ");
                        } else if(type==Lister.HTML) {
                            appendToFile(columnSeparator + "<span class=\"blank\"></span>");
                        }
                    }
                    //display the file name
                    if(type==Lister.TXT) {
                        appendToFileEOL(f.getName());
                    } else if(type==Lister.HTML) {
                        switch(getExtension(f.getName())) {
                            case Lister.E_TXT: appendToFileEOL("<img src=\"img/txt.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            case Lister.E_TORRENT: appendToFileEOL("<img src=\"img/torrent.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            case Lister.E_JPG: appendToFileEOL("<img src=\"img/img.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            case Lister.E_JPEG: appendToFileEOL("<img src=\"img/img.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            case Lister.E_PNG: appendToFileEOL("<img src=\"img/img.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            case Lister.E_GIF: appendToFileEOL("<img src=\"img/img.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            case Lister.E_BMP: appendToFileEOL("<img src=\"img/img.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            case Lister.E_DOC: appendToFileEOL("<img src=\"img/word.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            case Lister.E_DOCX: appendToFileEOL("<img src=\"img/word.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            case Lister.E_XLS: appendToFileEOL("<img src=\"img/excel.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            case Lister.E_XLSX: appendToFileEOL("<img src=\"img/excel.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            case Lister.E_PPS: appendToFileEOL("<img src=\"img/powerpoint.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            case Lister.E_PPT: appendToFileEOL("<img src=\"img/powerpoint.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            case Lister.E_PPTX: appendToFileEOL("<img src=\"img/powerpoint.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            case Lister.E_AVI: appendToFileEOL("<img src=\"img/movie.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            case Lister.E_MKV: appendToFileEOL("<img src=\"img/movie.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            case Lister.E_WMV: appendToFileEOL("<img src=\"img/movie.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            case Lister.E_MOV: appendToFileEOL("<img src=\"img/movie.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            case Lister.E_PDF: appendToFileEOL("<img src=\"img/pdf.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />"); break;
                            default: appendToFileEOL("<img src=\"img/file.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />");
                        }
                    }
                    
                    numberOfFile++;
                } else { //if the File is in reality a folder
                    //display the right number of "blank"
                    for(int i = 0; i<tab; i++) {
                        if(type==Lister.TXT) {
                            appendToFile(columnSeparator + "   ");
                        } else if(type==Lister.HTML) {
                            appendToFile(columnSeparator + "<span class=\"blank\"></span>");
                        }
                    }
                    //display the folder name
                    if(type==Lister.TXT) {
                        appendToFileEOL("# " + f.getName());
                    } else if(type==Lister.HTML) {
                        appendToFileEOL("<img src=\"img/folder.png\" class=\"img\" />&nbsp;" + f.getName() + "<br />");
                    }
                    
                    numberOfFolder++;
                    listFolder(f, tab2, type);
                }
            }
        }
    }

    /**
     * Get the number of file in the folder
     * @return the number of file in the folder
     */
    public int getNumberOfFile() {
        return numberOfFile;
    }

    /**
     * Get the number of subfolder in the folder
     * @return the number of subfolder in the folder
     */
    public int getNumberOfFolder() {
        return numberOfFolder;
    }
    
    /**
     * Return the extension of a file
     * @param fileName  Name of the file, with the extension
     * @return the extension of the file
     */
    public int getExtension(String fileName) {
        StringTokenizer st = new StringTokenizer(fileName, ".");
        String s = null;
        //get the last token, i.e. the extension, in a string
        while(st.hasMoreTokens()) {
            s = st.nextToken();
        }
        
        //return the right extension code
        switch(s.toLowerCase()) {
            case "txt": return Lister.E_TXT;
            case "torrent": return Lister.E_TORRENT;
            case "jpg": return Lister.E_JPG;
            case "jpeg": return Lister.E_JPEG;
            case "png": return Lister.E_PNG;
            case "gif": return Lister.E_GIF;
            case "bmp": return Lister.E_BMP;
            case "doc": return Lister.E_DOC;
            case "docx": return Lister.E_DOCX;
            case "xls": return Lister.E_XLS;
            case "xlsx": return Lister.E_XLSX;
            case "pps": return Lister.E_PPS;
            case "ppt": return Lister.E_PPT;
            case "pptx": return Lister.E_PPTX;
            case "avi": return Lister.E_AVI;
            case "mkv": return Lister.E_MKV;
            case "wmv": return Lister.E_WMV;
            case "mov": return Lister.E_MOV;
            case "pdf": return Lister.E_PDF;
            default: return 0;
            
        }
    }
}