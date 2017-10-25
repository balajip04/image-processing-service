package com.cars.image.service;

import com.cars.image.utility.InventoryImagePreProcessor;
import com.cars.image.utility.Utility;
import com.cars.image.vo.ParserVO;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.PointerPointer;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.MogrifyCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.lept.*;
import static org.bytedeco.javacpp.tesseract.*;

@Component
public class VinTesseractServiceImpl implements VinTesseractService {

        @Autowired
        InventoryImagePreProcessor inventoryImagePreProcessor;
        @Autowired
        Utility utility;

        public ParserVO imageProcessing(String fileName, String original)throws InterruptedException, IOException, IM4JavaException {
        BytePointer outText;
        System.err.println("VinTesseractServiceImpl.imageProcessing");
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
        api.SetVariable("tessedit_char_whitelist", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ");

                //api.GetComponentImages(, true, NULL, NULL);
        // Get OCR result
        outText = api.GetUTF8Text();
        String vin = outText.getString();
        System.out.println("OCR output:\n" + vin);
                BytePointer bp = TessBaseAPIGetBoxText(api,0);
                //System.out.println("***************:" + bp.getString());
                PIXA pixa=null;
                IntPointer ip = null;
                PointerPointer pp = null;
                BOXA boxa = api.GetComponentImages(RIL_WORD,true,pp,pp);
                BOXA boxa1 = api.GetWords(pixa);
                //System.out.println("GetWordImages:" + boxa1.n());
                BOX a;
                int x =0;
                int y =0;
                int w =0;
                int h =0;
                //New Code
                final ResultIterator ri = api.GetIterator();
                int x1 = 0;
                int y1 = 0;
                int x2 = 0;
                int y2 = 0;
                IntPointer coord1 = new IntPointer(x1);
                IntPointer coord2 = new IntPointer(y1);
                IntPointer coord3 = new IntPointer(x2);
                IntPointer coord4 = new IntPointer(y2);
                ri.Begin();
                if (ri !=null) {
                        do {
                                BytePointer word = ri.GetUTF8Text(RIL_WORD);
                                float conf  = ri.Confidence(RIL_WORD);
                                boolean abc = ri.BoundingBox(RIL_WORD,coord1,coord2,coord3,coord4);
                                //System.out.println("coord --- "+coord1.get()+"--"+coord2.get()+"--"+coord3.get()+"--"+coord4.get());
                                if(null!=word){
                                String text = word.getString();
                                }
                        } while (ri.Next(RIL_WORD));
                }
                // Destroy used object and release memory
        api.End();
        outText.deallocate();
        pixDestroy(image);
        return new ParserVO(vin,null,null);
    }
}