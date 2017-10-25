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
public class VinPreProcessor {


    @Autowired
    Utility utility;

    public void preProcessImage() throws IOException {
        try {
            System.err.println("Vin - preProcessImage");
            Info var2 = null;
            var2 = new Info("original.jpg");
            // MagickImage image = new MagickImage(var2);
            utility.dumpBasicInfo(var2);
            BufferedImage img = null;
            File f = null;
            f = new File("holder.jpg");
            img = ImageIO.read(f);
            //get image width and height
            int width = img.getWidth();
            int height = img.getHeight();
            //convertToGrayScale(img, width, height);
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
        if(height<800){//for inventory
            op.filter("triangle").resize(1280,960);
        }
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
                    /*var8.filter("Triangle").density(300).unsharp(0.25*0.25+8+0.065).dither("None").posterize(136).
                         define("jpeg:fancy-upsampling=off").interlace("none").colorspace("sRGB -strip");//.unsharp(1.5*1+0.7+0.02);
                    */
                    //Use the below for fully greyed out image in black and white instead of the above morphology thicken '1x3>:1,0,1' show:
                    //var8.edge(30.0).blackThreshold(0.0).density(300).unsharp(1.5*1+0.7+0.02).normalize().autoLevel().morphology("thicken","1x3>:1,0,1").
                      //      morphology("close","diamond").fill("black").dither("None").posterize(136).
                        //    define("jpeg:fancy-upsampling=off").interlace("none").colorspace("sRGB").colorspace("gray");
                    var8.colorspace("Gray").colorspace("sRGB -strip").
                            density(300);//.fill("black").unsharp(1.5*1+0.7+0.02).threshold(5).blackThreshold(0d).fill("white").whiteThreshold(0d);//.

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


    public void blurOriginal() throws IOException, InterruptedException, IM4JavaException {
        System.err.println("blurOriginal") ;
        ConvertCmd cmd = new ConvertCmd();
        MogrifyCmd modcmd = new MogrifyCmd();
        // create the operation, add images and operators/options
        IMOperation op = new IMOperation();
        op.addImage("original.jpg");
        op.resize(1280,960);

        op.addImage();
        // execute the operation
        op.openOperation();
        //op.clone(Integer.valueOf(0));
        op.addDynamicOperation(new DynamicOperation() {
            public Operation resolveOperation(Object... obj) throws IM4JavaException {
                if(obj.length > 0) {
                    IMOperation var8 = new IMOperation();
                    //var8.b
                    var8.draw("rectangle 886,37 144,97");
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
        modcmd.run(op, new Object[]{"original.jpg","original.jpg"});
    }



    private void convertToGrayScale(BufferedImage img, int width, int height) {
        //convert to grayscale
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int p = img.getRGB(x,y);
                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;
                //calculate average
                int avg = (r+g+b)/3;
                //replace RGB value with avg
                p = (a<<24) | (avg<<16) | (avg<<8) | avg;
                img.setRGB(x, y, p);
            }
        }
    }



}
