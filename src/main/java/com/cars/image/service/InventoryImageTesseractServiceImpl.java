package com.cars.image.service;

import com.cars.image.utility.InventoryImagePreProcessor;
import com.cars.image.utility.Utility;
import com.cars.image.vo.ParserVO;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.tesseract;
import org.im4java.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.lept.*;
import static org.bytedeco.javacpp.tesseract.*;

@Component
public class InventoryImageTesseractServiceImpl implements InventoryImageTesseractService {

    @Autowired
    InventoryImagePreProcessor inventoryImagePreProcessor;

    @Autowired
    Utility utility;

    public ParserVO imageProcessing(String fileName, String original) throws InterruptedException, IOException, IM4JavaException {
        BytePointer outText;
        System.err.println("BasicTesseract.imageProcessing");
        TessBaseAPI api = new TessBaseAPI();
        // Initialize tesseract-ocr with English, without specifying tessdata path
        if (api.Init(".", "ENG") != 0) {
            //if(api.Init("", "ENG", OEM_TESSERACT_CUBE_COMBINED)!=0)   {
            System.err.println("Could not initialize tesseract.");
            System.exit(1);
        }
        // Open input image with leptonica library
        PIX image = pixRead(fileName);
        api.SetImage(image);
        api.SetPageSegMode(PSM_SINGLE_BLOCK);

        //api.GetComponentImages(, true, NULL, NULL);
        // Get OCR result
        outText = api.GetUTF8Text();
        String parsedText = outText.getString();
        System.out.println("OCR output:\n" + parsedText);
        BytePointer bp = TessBaseAPIGetBoxText(api, 0);
        //System.out.println("***************:" + bp.getString());
        PIXA pixa = null;
        IntPointer ip = null;
        PointerPointer pp = null;
        BOXA boxa = api.GetComponentImages(RIL_WORD, true, pp, pp);
        BOXA boxa1 = api.GetWords(pixa);
        //System.out.println("GetWordImages:" + boxa1.n());
        BOX a;
        int x = 0;
        int y = 0;
        int w = 0;
        int h = 0;
        List<String> outputList = new ArrayList<String>();
        //New Code
        final tesseract.ResultIterator ri = api.GetIterator();
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        IntPointer coord1 = new IntPointer(x1);
        IntPointer coord2 = new IntPointer(y1);
        IntPointer coord3 = new IntPointer(x2);
        IntPointer coord4 = new IntPointer(y2);
        ri.Begin();
        if (ri != null) {
            do {
                BytePointer word = ri.GetUTF8Text(RIL_WORD);
                BytePointer wordBlock = ri.GetUTF8Text(RIL_BLOCK);
                String tempText = wordBlock.getString();
                    float conf = ri.Confidence(RIL_WORD);
                    boolean abc = ri.BoundingBox(RIL_WORD, coord1, coord2, coord3, coord4);
                    //System.out.println("coord --- "+coord1.get()+"--"+coord2.get()+"--"+coord3.get()+"--"+coord4.get());
                    if (null != word) {
                        String text = word.getString();
                        //System.out.println("is numeric --- " + ri.WordIsNumeric());
                        //System.out.println("Confidence --- " + conf);
                        //System.out.println("Word in string --- " + text);
                        text = utility.cleanPhoneNum(text);
                        if (utility.identifyPhoneNum(text)) {
                            outputList.add(text);
                            ConvertCmd cmd = new ConvertCmd();
                            IMOperation op = new IMOperation();
                            op.addImage("original.jpg");
                            op.resize(1280, 960);
                            op.fill("white");
                            op.draw("rectangle " + coord1.get() + "," + coord2.get() + " " + coord3.get() + "," + coord4.get());
                            op.resize(640, 480);
                            op.addImage("original.jpg");
                            cmd.run(op);
                            break;
                        }/*else if(StringUtils.isNotEmpty(text) && StringUtils.isNotEmpty(tempText) && tempText.contains(text)){
                            System.out.println("tempText text - "+tempText + "---"+text);
                            outputList.add(text);
                            ConvertCmd cmd = new ConvertCmd();
                            IMOperation op = new IMOperation();
                            op.addImage("original.jpg");
                            op.resize(1280, 960);
                            op.fill("white");
                            op.draw("rectangle " + coord1.get() + "," + coord2.get() + " " + coord3.get() + "," + coord4.get());
                            op.resize(640, 480);
                            op.addImage("original.jpg");
                            cmd.run(op);
                        }*/
                    }
            } while (ri.Next(RIL_WORD));
        }
        // Destroy used object and release memory
        api.End();
        outText.deallocate();
        pixDestroy(image);
        ParserVO parserVO = new ParserVO();
        for (String s : outputList) {
            System.out.println("List - " + s);
        }
        parserVO.setPhoneNumber(outputList);
        parserVO.setParsedText(parsedText);
        return parserVO;
    }


    public static void main(String args[]) {
        String text = "680 E. Napier Avenue\n" +
                "e Benton Harbor. MI 49022\n" +
                "(877)244-3864\n" +
                "\n" +
                "\\7 > ‘ L    j\n" +
                "_ .l' “.§ If‘ > WE?” '\\ A ‘ V ‘\n" +
                "\n" +
                "H; .}‘W;‘;i-' v; \\ a; v\n" +
                "\n" +
                "-   > w\n" +
                "www.signatu reautomotivegrou p.com";
        text = text.replaceAll("[^\\d\\s]", "").
                replaceAll("^[\\)\\s.-]+", "").replaceAll("[\\)\\s\\W.-]+$", "").
                replaceAll("^\\s+", "").replaceAll("\\s+$", "").replaceAll("(\\d\\s){1,9}","").replaceAll("[^(\\d){1,9}]","");
        System.out.println(text);

        if ((text.matches("^[2-9]\\d{2}-\\d{3}-\\d{4}$")) ||
                (text.matches("((\\(\\d{3}\\) ?)|(\\d{3}-))?\\d{3}-\\d{4}")) ||
                (text.matches("^\\D?(\\d{3})\\D?\\D?(\\d{3})\\D?(\\d{4})$"))) {
            System.out.println("Matched");
        } else {
            System.out.println(" Not Matched");
        }


    }
}