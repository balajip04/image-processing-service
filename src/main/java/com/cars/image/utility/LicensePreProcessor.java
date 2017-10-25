package com.cars.image.utility;

import magick.MagickException;
import org.im4java.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by balaajiparthasarathy on 2/28/17.
 */

@Component
public class LicensePreProcessor {


    @Autowired
    Utility utility;

    public void preProcessImage() throws IOException {
        try {
            System.err.println("License - preProcessImage");
            Info var2 = null;
            var2 = new Info("original.jpg");
            utility.dumpBasicInfo(var2);
            BufferedImage img = null;
            File f = null;
            f = new File("holder.jpg");
            img = ImageIO.read(f);
            //get image width and height
            int width = img.getWidth();
            int height = img.getHeight();
            f = new File("output.jpg");
            ImageIO.write(img, "jpg", f);
            //scale image
            imageMigick(width, height);
        }catch(IOException e){
            System.out.println(e);
        }catch (InfoException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IM4JavaException e) {
            e.printStackTrace();
        } catch (MagickException e) {
            e.printStackTrace();
        }
    }

    private void imageMigick(int width, int height) throws IOException, InterruptedException, IM4JavaException {
        System.err.println("imageMigick") ;
        ConvertCmd cmd = new ConvertCmd();
        MogrifyCmd modcmd = new MogrifyCmd();
        CompositeCmd composite = new CompositeCmd();
        // create the operation, add images and operators/options
        IMOperation op = new IMOperation();
        op.addImage("output.jpg");
        op.addImage();
        // execute the operation
        op.openOperation();
        //op.clone(Integer.valueOf(0));
        op.addDynamicOperation(new DynamicOperation() {
            public Operation resolveOperation(Object... obj) throws IM4JavaException {
                System.err.println("height - "+height) ;
                if(obj.length > 0) {
                    System.err.println("width - "+width) ;
                    IMOperation var8 = new IMOperation();
                    var8.colorspace("Gray").
                            colorspace("sRGB -strip").
                            density(600).
                            layers("flatten").
                            brightnessContrast(0d,100d).
                            morphology("thicken","1x3>:1,0,1").
                            normalize().resize(640,480).
                            morphology("close","diamond");
                    return var8;
                } else {
                    return null;
                }
            }
        });
        op.closeOperation();
        op.appendHorizontally();
        op.addImage();
        // execute the operation
        modcmd.run(op, new Object[]{"output.jpg","output.jpg"});
    }

}
