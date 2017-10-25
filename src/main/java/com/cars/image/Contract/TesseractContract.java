package com.cars.image.Contract;

import com.cars.image.vo.ParserVO;
import org.springframework.stereotype.Component;

/**
 * Created by balaajiparthasarathy on 2/15/17.
 */
@Component
public interface TesseractContract {

    public ParserVO imageProcessing(String fileName, String original, String imageType)
            throws Exception;
}
