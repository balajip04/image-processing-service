package com.cars.image.Contract;

import com.cars.image.service.InventoryImageTesseractService;
import com.cars.image.service.LicensePlateTesseractService;
import com.cars.image.service.VinTesseractService;
import com.cars.image.utility.*;
import com.cars.image.vo.ParserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TesseractContractImpl implements TesseractContract {

        @Autowired
        private InventoryImageTesseractService inventoryImageTesseractService;

        @Autowired
        private LicensePlateTesseractService licensePlateTesseractService;

        @Autowired
        private VinTesseractService vinTesseractService ;

        @Autowired
        private VinPreProcessor vinPreProcessor;

        @Autowired
        private LicensePreProcessor licensePreProcessor;

        @Autowired
        private InventoryImagePreProcessor inventoryImagePreProcessor;

        public ParserVO imageProcessing(String fileName, String original, String imageType)
                throws Exception{
                if(imageType.equalsIgnoreCase(ImageTypeEnum.InventoryImage.toString())){
                        inventoryImagePreProcessor.preProcessImage();
                        return inventoryImageTesseractService.imageProcessing(fileName,original);

                }else if(imageType.equalsIgnoreCase(ImageTypeEnum.LicensePlate.toString())){
                        licensePreProcessor.preProcessImage();
                        return licensePlateTesseractService.imageProcessing(fileName,original);

                }else if(imageType.equalsIgnoreCase(ImageTypeEnum.Vin.toString())){
                        vinPreProcessor.preProcessImage();
                        return vinTesseractService.imageProcessing(fileName,original);

                }else{
                        return new ParserVO("Not a valid Image Type");

                }

        }

}