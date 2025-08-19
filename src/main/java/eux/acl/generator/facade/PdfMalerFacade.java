package eux.acl.generator.facade;


import eux.acl.generator.service.PdfService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class PdfMalerFacade {
    private final PdfService pdfService;

    public PdfMalerFacade() {
        this.pdfService = new PdfService();
    }

    public void lagPdfMaler(List<String> sedTyper, String cdmVersjon) {
        pdfService.lagPdfMaler(sedTyper, cdmVersjon);

    }


}
