package com.cars.image.utility;

import magick.MagickException;
import magick.MagickImage;
import org.im4java.core.Info;
import org.im4java.core.InfoException;
import org.springframework.stereotype.Component;

/**
 * Created by balaajiparthasarathy on 3/9/17.
 */
@Component
public class Utility {

    public boolean identifyPhoneNum(String text) {
        //^[2-9]\d{2}-\d{3}-\d{4}$  --- 212-666-1234
        //((\(\d{3}\) ?)|(\d{3}-))?\d{3}-\d{4}  --- (123) 456-7890 | 123-456-7890
        //^\D?(\d{3})\D?\D?(\d{3})\D?(\d{4})$   --- (111) 222-3333 | 1112223333 | 111-222-3333
        if((text.matches("^[2-9]\\d{2}-\\d{3}-\\d{4}$"))||
                //(text.matches("((\\(\\d{3}\\) ?)|(\\d{3}-))?\\d{3}-\\d{4}"))||
                (text.matches("^\\D?(\\d{3})\\D?\\D?(\\d{3})\\D?(\\d{4})$"))) {
            return true;
        }
        return false;
    }

    public String cleanPhoneNum(String text){
        return text.replaceAll("[^\\d\\s\\(\\).-]", "").
                replaceAll("^[\\)\\s.-]+", "").replaceAll("[\\)\\s\\W.-]+$", "").replaceAll("(\\d{1,}\\s)*", "").
                replaceAll("^\\s+", "").replaceAll("\\s+$", "");

    }


    public void dumpBasicInfo(Info var1) throws InfoException, MagickException {
        System.err.println("Format: " + var1.getImageFormat());
        System.err.println("Width: " + var1.getImageWidth());
        System.err.println("Height: " + var1.getImageHeight());
        System.err.println("Geometry: " + var1.getImageGeometry());
        System.err.println("PageWidth: " + var1.getPageWidth());
        System.err.println("PageHeight: " + var1.getPageHeight());
        System.err.println("PageGeometry: " + var1.getPageGeometry());
        System.err.println("Depth: " + var1.getImageDepth());
        //System.err.println("BG Color: " + image.getBackgroundColor());
        System.err.println("Class: " + var1.getImageClass());
        int var2 = var1.getSceneCount();

        for(int var3 = 0; var3 < var2; ++var3) {
            System.err.println("--------------- " + var3 + " -------------------");
            System.err.println("Format: " + var1.getImageFormat(var3));
            System.err.println("Width: " + var1.getImageWidth(var3));
            System.err.println("Height: " + var1.getImageHeight(var3));
            System.err.println("Geometry: " + var1.getImageGeometry(var3));
            System.err.println("PageWidth: " + var1.getPageWidth(var3));
            System.err.println("PageHeight: " + var1.getPageHeight(var3));
            System.err.println("PageGeometry: " + var1.getPageGeometry(var3));
            System.err.println("Depth: " + var1.getImageDepth(var3));
            System.err.println("Class: " + var1.getImageClass(var3));
        }

    }

}
