package com.company.Parsers;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;

/**
 * Created by admin on 25.04.17.
 */
public class ReadWriteFile {

    public ReadWriteFile(){
    }

    public String downloadContentJson(String urlFile) throws IOException {

        FileInputStream fis = null;
        HWPFDocument doc = null;
        BufferedReader br = null;
        String resultContent = "";


        try {

            fis = new FileInputStream(urlFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (urlFile.contains(".docx")) {
            XWPFDocument document = new XWPFDocument(fis);
            XWPFWordExtractor wordex = new XWPFWordExtractor(document);
            resultContent += wordex.getText();
            return resultContent;

        } else if (urlFile.contains(".doc")) {

            doc = new HWPFDocument(fis);
            WordExtractor extractor = new WordExtractor(doc);
            String[] fileData = extractor.getParagraphText();

            for (String paragraph : fileData) {
                resultContent += paragraph;
            }
            return resultContent;
        } else if (urlFile.contains(".txt")) {
            br = new BufferedReader(new FileReader(urlFile));

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                resultContent += sCurrentLine;
            }
            doc = null;
            fis = null;
            br = null;
            urlFile = null;
            return resultContent;

        }

        return null;
    }

    public boolean writeJsonToFile(String urlFile, String inputData) throws IOException {

        //Blank Document
        XWPFDocument document= new XWPFDocument();
        //Write the Document in file system
        FileOutputStream out = new FileOutputStream(new File(urlFile));

        //create Paragraph
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run=paragraph.createRun();
        run.setText(inputData);

        try {
            document.write(out);
            out.close();
            document = null;
            paragraph = null;
            out = null;
            run = null;
            return true;
        } catch (IOException e) {
            out.close();
            e.printStackTrace();
        }


        return false;
    }

}
