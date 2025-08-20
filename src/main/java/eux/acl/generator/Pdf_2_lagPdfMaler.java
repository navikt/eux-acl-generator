package eux.acl.generator;

import eux.acl.generator.facade.FundamentFacade;
import eux.acl.generator.facade.PdfMalerFacade;

import static eux.acl.generator.service.FilService.*;

public class Pdf_2_lagPdfMaler {


    public static void main(String[] args) {

        PdfMalerFacade pdfMalerFacade = new PdfMalerFacade();
        pdfMalerFacade.lagPdfMaler(SEDTYPER_A, V44);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_DA, V44);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_F, V44);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_H, V44);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_M, V44);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_P, V44);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_R, V44);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_S, V44);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_U, V44);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_X, V44);
    }

}
