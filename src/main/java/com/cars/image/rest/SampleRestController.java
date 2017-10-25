package com.cars.image.rest;

import com.cars.image.Contract.TesseractContract;
import com.cars.image.service.InventoryImageTesseractServiceImpl;
import com.cars.image.utility.Encoder;
import com.cars.image.utility.ImageTypeEnum;
import com.cars.image.vo.ParserVO;
import org.apache.commons.lang3.StringUtils;
import org.im4java.core.IM4JavaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cars.framework.config.ConfigService;
import com.cars.framework.config.feature.FeatureFlag;

import javax.imageio.ImageIO;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.*;
import static java.nio.file.StandardCopyOption.*;

import java.nio.file.*;
import java.util.List;
import java.util.Map;

import static org.bytedeco.javacpp.lept.pixRead;

/**
 * Sample REST service using Spring's REST API
 */
// @Restcontroller = @Controller + @ResponseBody
@RestController
public class SampleRestController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ConfigService configService;

	@Autowired
	private FeatureFlag featureFlag;

	@Autowired
    TesseractContract tesseractContract;


	@RequestMapping(value = "/info", method = RequestMethod.GET, produces = "text/plain")
	public String info() throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("/info called on " + getClass().getName());
		}

		return "This is the rest interface for the image-publishing-gateway1.0 application";
	}



	@RequestMapping(value = "/image/{imageType}", method = RequestMethod.POST)
	public @ResponseBody ParserVO imageProcessor(InputStream stream,@PathVariable String imageType) {
		System.err.println("Stream - " + stream);
        System.err.println("imageType - " + imageType);

        OutputStream outputStream = null;
		OutputStream originalOutputStream = null;
		try {
                outputStream =
                        new FileOutputStream(new File("holder.jpg"));
                int read = 0;
                byte[] bytes = new byte[1024];
                while ((read = stream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                Path source = FileSystems.getDefault().getPath( "holder.jpg");
                Path target = FileSystems.getDefault().getPath( "original.jpg");
                Files.copy(source, target, REPLACE_EXISTING);
                if(null==imageType || StringUtils.isEmpty(imageType)){
                    imageType = "InventoryImage";
                }
                ParserVO parservo = tesseractContract.imageProcessing("output.jpg", "original.jpg", imageType);
            File initialFile;
            if(imageType.equalsIgnoreCase(ImageTypeEnum.InventoryImage.toString()))
                initialFile = new File("original.jpg");
            else
                initialFile = new File("output.jpg");
                InputStream targetStream = new FileInputStream(initialFile);
                // Prepare buffered image.
                BufferedImage img = ImageIO.read(targetStream);

                // Create a byte array output stream.
                ByteArrayOutputStream bao = new ByteArrayOutputStream();

                // Write to output stream
                ImageIO.write(img, "jpg", bao);
                parservo.setImage(bao.toByteArray());
                return parservo;
            } catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IM4JavaException e) {
				e.printStackTrace();
			} catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    // outputStream.flush();
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        }
		return null;
	}




}
