package eux.acl.generator;

import eux.acl.generator.facade.PdfMalerFacade;
import org.junit.jupiter.api.Test;

import static eux.acl.generator.LageFundamentTest.SEDTYPER_F_H_S_U;
import static eux.acl.generator.service.FilService.*;

public class LagePdfMalerTest {

    /**
     * Leser sed-fundament-filer og genererer mal-filer som Pdf-generatoren så bruker.
     *
     */
    @Test
    public void lager_pdfMaler_v44() {
        // krever at kodeverk og euSedFundament er generert - og flyttet til main/../resources..
        PdfMalerFacade pdfMalerFacade = new PdfMalerFacade();
        pdfMalerFacade.lagPdfMaler(SEDTYPER_A, V44);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_F, V44);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_H, V44);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_DA, V44);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_M, V44);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_P, V44);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_R, V44);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_S, V44);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_U, V44);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_X, V44);
    }


}
