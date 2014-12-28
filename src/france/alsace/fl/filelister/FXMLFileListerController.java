/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package france.alsace.fl.filelister;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

/**
 *
 * @author Florent
 */
public class FXMLFileListerController implements Initializable {
    @FXML
    private ProgressIndicator progress;
    @FXML
    private TextField textFieldMainFolder;
    @FXML
    private TextField textFieldResultFolder;
    @FXML
    private TextField textFieldFileName;
    @FXML
    private Button runButton;
    @FXML
    private Label labelTime;
    @FXML
    private Label labelFolders;
    @FXML
    private Label labelFiles;
    @FXML
    private RadioButton radioButtonEraseData;
    @FXML
    private ToggleGroup toggleGroup1;
    @FXML
    private RadioButton radioButtonKeepData;
    @FXML
    private Label labelError;
    @FXML
    private ComboBox<String> comboBoxType;
    @FXML
    private CheckBox checkBoxSeparator;
    @FXML
    private Button openButton;
    
    DirectoryChooser directoryChooser = new DirectoryChooser();
    ObservableList<String> options = 
    FXCollections.observableArrayList(
        ".txt",
        ".html"
    );
    
    private final String FINAL_FOLDER_NAME = "FileListerFX";
    private String lastResultFolder = "";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        progress.setVisible(false);
        openButton.setDisable(true);
        this.directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        comboBoxType.setItems(options);
        comboBoxType.setValue(".txt");
    }    

    /**
     * Display the directory chooser
     * @param event 
     */
    @FXML
    private void chooseMainFolder(MouseEvent event) {
        this.directoryChooser.setTitle("Sélectionner le dossier à lister");
        File file = this.directoryChooser.showDialog(this.textFieldMainFolder.getScene().getWindow());
        if (file != null) {
            this.textFieldMainFolder.setText(file.getPath());
        }
    }

    /**
     * Display the directory chooser
     * @param event 
     */
    @FXML
    private void chooseResultFolder(MouseEvent event) {
        this.directoryChooser.setTitle("Sélectionner le dossier pour enregitrer le résultat");
        File file = this.directoryChooser.showDialog(this.textFieldResultFolder.getScene().getWindow());
        if (file != null) {
            this.textFieldResultFolder.setText(file.getPath());
        }
    }

    /**
     * List the files and folders in a file
     * @param event 
     */
    @FXML
    private void listFolder(ActionEvent event) {
        this.labelError.setText("");
        
        if(!textFieldMainFolder.getText().isEmpty() 
                && !textFieldResultFolder.getText().isEmpty()
                && !textFieldFileName.getText().isEmpty()) {
            
            File folder = new File(textFieldMainFolder.getText());
            File resultFolder = new File(textFieldResultFolder.getText());
        
            Lister lister  = new Lister();
            
            if(folder.exists() && resultFolder.exists()) {
                this.progress.setVisible(true);
                this.runButton.setDisable(true);
                this.openButton.setDisable(true);
                
                File outputDir = new File (textFieldResultFolder.getText() + "\\" + FINAL_FOLDER_NAME);
                
                if(!outputDir.exists()) {
                    outputDir.mkdirs();
                }
                
                File output = new File(outputDir.getAbsolutePath() + "\\" + textFieldFileName.getText() + comboBoxType.getValue());
                
                //Run in another thread the Lister with the right parameters
                Thread t = new Thread() {
                    public void run() {
                        long a = System.currentTimeMillis();
                        
                        if(comboBoxType.getValue().equals(".txt")) {
                            lister.execute(folder, output, radioButtonEraseData.isSelected(), Lister.TXT, checkBoxSeparator.isSelected());
                        } else if(comboBoxType.getValue().equals(".html")) {
                            lister.execute(folder, output, radioButtonEraseData.isSelected(), Lister.HTML, checkBoxSeparator.isSelected());
                        }
                        
                        long b = System.currentTimeMillis();
                        
                        lastResultFolder = outputDir.getAbsolutePath();
                        
                        Platform.runLater(new Runnable() {
                            public void run() {
                                progress.setVisible(false);
                                labelTime.setText(b-a + " ms");
                                labelFiles.setText(Integer.toString(lister.getNumberOfFile()));
                                labelFolders.setText(Integer.toString(lister.getNumberOfFolder()));
                                runButton.setDisable(false);
                                openButton.setDisable(false);
                            }
                        });
                    }
                };
                t.start();
   
                //copy the img if the output file type is HTML
                if(comboBoxType.getValue().equals(".html")) {
                    File outputDirImg = new File(outputDir.getAbsolutePath() + "\\img");

                    if(!outputDirImg.exists()) {
                        outputDirImg.mkdirs();
                    }

                    // output images
                    File outputImgFile = new File(outputDirImg.getAbsolutePath() + "\\file.png");
                    File outputImgFolder = new File(outputDirImg.getAbsolutePath() + "\\folder.png");
                    File outputImgTxt = new File(outputDirImg.getAbsolutePath() + "\\txt.png");
                    File outputImgTorrent = new File(outputDirImg.getAbsolutePath() + "\\torrent.png");
                    File outputImgImg = new File(outputDirImg.getAbsolutePath() + "\\img.png");
                    File outputImgWord = new File(outputDirImg.getAbsolutePath() + "\\word.png");
                    File outputImgExcel = new File(outputDirImg.getAbsolutePath() + "\\excel.png");
                    File outputImgPowerpoint = new File(outputDirImg.getAbsolutePath() + "\\powerpoint.png");
                    File outputImgMovie = new File(outputDirImg.getAbsolutePath() + "\\movie.png");
                    File outputImgPdf = new File(outputDirImg.getAbsolutePath() + "\\pdf.png");
                    
                    //initialize input and output streams
                    InputStream inputImgFile = null;
                    InputStream inputImgFolder = null;
                    InputStream inputImgTxt = null;
                    InputStream inputImgTorrent = null;
                    InputStream inputImgImg = null;
                    InputStream inputImgWord = null;
                    InputStream inputImgExcel = null;
                    InputStream inputImgPowerpoint = null;
                    InputStream inputImgMovie = null;
                    InputStream inputImgPdf = null;

                    FileOutputStream fosFile = null;
                    FileOutputStream fosFolder = null;
                    FileOutputStream fosTxt = null;
                    FileOutputStream fosTorrent = null;
                    FileOutputStream fosImg = null;
                    FileOutputStream fosWord = null;
                    FileOutputStream fosExcel = null;
                    FileOutputStream fosPowerpoint = null;
                    FileOutputStream fosMovie = null;
                    FileOutputStream fosPdf = null;

                    try {
                        //create output img files
                        outputImgFile.createNewFile();
                        outputImgFolder.createNewFile();
                        outputImgTxt.createNewFile();
                        outputImgTorrent.createNewFile();
                        outputImgImg.createNewFile();
                        outputImgWord.createNewFile();
                        outputImgExcel.createNewFile();
                        outputImgPowerpoint.createNewFile();
                        outputImgMovie.createNewFile();
                        outputImgPdf.createNewFile();

                        //get the img as a stream
                        inputImgFile = this.getClass().getResourceAsStream("/img/file.png");
                        inputImgFolder = this.getClass().getResourceAsStream("/img/folder.png");
                        inputImgTxt = this.getClass().getResourceAsStream("/img/txt.png");
                        inputImgTorrent = this.getClass().getResourceAsStream("/img/torrent.png");
                        inputImgImg = this.getClass().getResourceAsStream("/img/img.png");
                        inputImgWord = this.getClass().getResourceAsStream("/img/word.png");
                        inputImgExcel = this.getClass().getResourceAsStream("/img/excel.png");
                        inputImgPowerpoint = this.getClass().getResourceAsStream("/img/powerpoint.png");
                        inputImgMovie = this.getClass().getResourceAsStream("/img/movie.png");
                        inputImgPdf = this.getClass().getResourceAsStream("/img/pdf.png");

                        //open a stream on the output file
                        fosFile = new FileOutputStream(outputImgFile);
                        fosFolder = new FileOutputStream(outputImgFolder);
                        fosTxt = new FileOutputStream(outputImgTxt);
                        fosTorrent = new FileOutputStream(outputImgTorrent);
                        fosImg = new FileOutputStream(outputImgImg);
                        fosWord = new FileOutputStream(outputImgWord);
                        fosExcel = new FileOutputStream(outputImgExcel);
                        fosPowerpoint = new FileOutputStream(outputImgPowerpoint);
                        fosMovie = new FileOutputStream(outputImgMovie);
                        fosPdf = new FileOutputStream(outputImgPdf);

                        //copy the file from the input stream to the output stream
                        byte buffer[]=new byte[512*1024];
                        int nbLecture;
                        
                        while( (nbLecture = inputImgFile.read(buffer)) != -1 ) { fosFile.write(buffer, 0, nbLecture); }
                        while( (nbLecture = inputImgFolder.read(buffer)) != -1 ) { fosFolder.write(buffer, 0, nbLecture); }
                        while( (nbLecture = inputImgTxt.read(buffer)) != -1 ) { fosTxt.write(buffer, 0, nbLecture); }
                        while( (nbLecture = inputImgTorrent.read(buffer)) != -1 ) { fosTorrent.write(buffer, 0, nbLecture); }
                        while( (nbLecture = inputImgImg.read(buffer)) != -1 ) { fosImg.write(buffer, 0, nbLecture); }
                        while( (nbLecture = inputImgWord.read(buffer)) != -1 ) { fosWord.write(buffer, 0, nbLecture); }
                        while( (nbLecture = inputImgExcel.read(buffer)) != -1 ) { fosExcel.write(buffer, 0, nbLecture); }
                        while( (nbLecture = inputImgPowerpoint.read(buffer)) != -1 ) { fosPowerpoint.write(buffer, 0, nbLecture); }
                        while( (nbLecture = inputImgMovie.read(buffer)) != -1 ) { fosMovie.write(buffer, 0, nbLecture); }
                        while( (nbLecture = inputImgPdf.read(buffer)) != -1 ) { fosPdf.write(buffer, 0, nbLecture); }

                    } catch (IOException ex) {
                        Logger.getLogger(FXMLFileListerController.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            //close all the streams
                            inputImgFile.close();
                            inputImgFolder.close();
                            inputImgTxt.close();
                            inputImgTorrent.close();
                            inputImgImg.close();
                            inputImgWord.close();
                            inputImgExcel.close();
                            inputImgPowerpoint.close();
                            inputImgMovie.close();
                            inputImgPdf.close();
                            
                            fosFile.close();
                            fosFolder.close();
                            fosTxt.close();
                            fosTorrent.close();
                            fosImg.close();
                            fosWord.close();
                            fosExcel.close();
                            fosPowerpoint.close();
                            fosMovie.close();
                            fosPdf.close();
                        } catch (IOException ex) {
                            Logger.getLogger(FXMLFileListerController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                
            } else {
                this.labelError.setText("Un des dossiers spécifiés n'existe pas !");
            }
        } else {
            this.labelError.setText("Veuillez remplir les trois champs !");
        }
    }

    @FXML
    private void openFolder(ActionEvent event) {
        if(lastResultFolder != "") {
            labelError.setText("");
            try {
                Desktop.getDesktop().open(new File(lastResultFolder));
            } catch (IOException ex) {
                Logger.getLogger(FXMLFileListerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            labelError.setText("Veuillez lister les fichiers avant d'afficher le résultat !");
        }
    }
}
