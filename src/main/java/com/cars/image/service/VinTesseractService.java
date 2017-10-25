package com.cars.image.service;

import com.cars.image.vo.ParserVO;
import org.springframework.stereotype.Component;

/**
 * Created by balaajiparthasarathy on 2/15/17.
 */
@Component
public interface VinTesseractService {

    public ParserVO imageProcessing(String fileName, String original)
            throws Exception;
}
