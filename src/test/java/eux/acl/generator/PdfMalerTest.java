package eux.acl.generator;

import eux.acl.generator.eksekvering.Versjoner;
import eux.acl.generator.facade.ResultaterFacade;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static eux.acl.generator.modell.SedTyper.*;

public class PdfMalerTest extends Versjoner {

    /**
     * Leser sed-fundament-filer og genererer mal-filer som Pdf-generatoren så bruker.
     *
     */
    @Test
    @Disabled
    public void lager_pdfMal_v44() {
        // krever at kodeverk og euSedFundament er generert - og flyttet til main/../resources..
        ResultaterFacade pdfMalerFacade = new ResultaterFacade();
        pdfMalerFacade.lagPdfMaler(List.of("F002"), V44);
    }

    @Test
    @Disabled
    public void lager_pdfMaler_v44() {
        // krever at kodeverk og euSedFundament er generert - og flyttet til main/../resources..
        ResultaterFacade pdfMalerFacade = new ResultaterFacade();
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
    @Test
    @Disabled
    public void lager_pdfMaler_v43() {
        // krever at kodeverk og euSedFundament er generert - og flyttet til main/../resources..
        ResultaterFacade pdfMalerFacade = new ResultaterFacade();
        pdfMalerFacade.lagPdfMaler(SEDTYPER_A, V43);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_F, V43);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_H, V43);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_DA, V43);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_M, V43);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_P, V43);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_R, V43);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_S, V43);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_U, V43);
        pdfMalerFacade.lagPdfMaler(SEDTYPER_X, V43);
    }

}
