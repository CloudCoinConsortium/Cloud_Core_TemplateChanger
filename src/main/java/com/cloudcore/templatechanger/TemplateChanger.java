package com.cloudcore.templatechanger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;


public class TemplateChanger {



    public static final String RootPath = Paths.get("").toAbsolutePath().toString() + File.separator;
    public static String TEMPLATEFolder = RootPath + "Acccounts" + File.separator + "Template" + File.separator + "Templates" + File.separator;
    public static String CONFIGFILEPATH = RootPath + "Acccounts" + File.separator + "Logs" + File.separator;

    public static String file_name = "config.txt";

    public TemplateChanger() {
        createDirectories();

    }

    public static void createDirectories() {
        try {
            Files.createDirectories(Paths.get(RootPath));
            Files.createDirectories(Paths.get(TEMPLATEFolder));
            Files.createDirectories(Paths.get(CONFIGFILEPATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Methods */
    /**
     * Asks the user for instructions on how to export CloudCoins to new
     * location.
     *
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     * @throws java.util.concurrent.ExecutionException
     */
    public void template() throws IOException, InterruptedException, ExecutionException {

        KeyboardReader reader = new KeyboardReader();

        // Ask for Bacck up.
        System.out.println("Do you want to add new Template?");
        System.out.println("1 => Pick a file");
        System.out.println("2 => Exit");

        int userChoice = reader.readInt();

        if (userChoice < 1 || userChoice > 1) {
            if (userChoice == 2) {
                System.out.println("User have cancel template change process. Exiting...");
            } else {
                System.out.println("invalid Choice. No template selected. Exiting...");
            }
            System.exit(0);
        } else {

            FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
            dialog.setMode(FileDialog.LOAD);
            dialog.setFilenameFilter(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jpg") || name.endsWith(".jpeg");
                }
            });
            dialog.setVisible(true);
            String file = dialog.getFile();
            if (file != null) {
                copyFile(new File(dialog.getDirectory() + dialog.getFile()), new File(TEMPLATEFolder + File.separator + dialog.getFile()));
                updateFile(dialog.getFile());
                System.out.println("File has been copied to Template Folder Successfully");
            } else {
                System.out.println("No File has been choose. Please try again");
            }
            System.exit(0);
        }
    }

    public static void updateFile(String fileName) {
        try {
            String name, extension;
            name = fileName.split("\\.")[0];
            extension = fileName.split("\\.")[1];
            name = name.replaceAll("\\D+", "");
            String fileContent = readFile(CONFIGFILEPATH + file_name);
            JSONObject jSONObject;
            if (fileContent.isEmpty()) {
                jSONObject = new JSONObject();
            } else {
                jSONObject = new JSONObject(fileContent);
            }
            jSONObject.put("export_" + name + "_" + extension, fileName);
            Files.write(Paths.get(CONFIGFILEPATH + file_name), jSONObject.toString().getBytes());
        } catch (IOException ex) {
            Logger.getLogger(TemplateChanger.class.getName()).log(Level.SEVERE, null, ex);
        }catch (JSONException jx){

        }
    }

    public static String readFile(String filePath) {

        InputStream is;

        try {
            is = new FileInputStream(filePath);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }

            return sb.toString();

        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        return "";
    }

    public static String getFileContnet(String folderPath) {
        File folder = new File(folderPath);
        String commandFile = "";
        if (folder.isDirectory()) {

            File[] filenames = folder.listFiles();

            if (null != filenames) {
                for (File file : filenames) {
                    if (file.isFile()) {
                        commandFile = readFile(file.getAbsolutePath());
                        break;
                    }
                }
            }
        }
        return commandFile;
    }

    public static boolean copyFile(File sourceFile, File destinationDirectory) {

        try {
            if (sourceFile.isDirectory()) {
                if (!destinationDirectory.exists()) {
                    destinationDirectory.mkdir();
                }

                String[] children = sourceFile.list();
                for (String children1 : children) {
                    copyFile(new File(sourceFile, children1), new File(destinationDirectory, children1));
                }
            } else {

                OutputStream out;
                try (InputStream in = new FileInputStream(sourceFile)) {
                    out = new FileOutputStream(destinationDirectory);
                    // Copy the bits from instream to outstream
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                }
                out.close();
//                sourceFile.delete();
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

}

