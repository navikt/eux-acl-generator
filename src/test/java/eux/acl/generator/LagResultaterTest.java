package eux.acl.generator;

import eux.acl.generator.facade.ResultaterFacade;
import org.junit.jupiter.api.Test;

import static eux.acl.generator.service.FilService.*;

public class LagResultaterTest {

    @Test
    public void lagKodeverksfiler() {
        ResultaterFacade resultaterFacade = new ResultaterFacade();
        resultaterFacade.lagNyeEnumfilerForAcl(V44);
    }

    @Test
    public void lagACL_malerV44() {
        ResultaterFacade resultaterFacade = new ResultaterFacade();
        resultaterFacade.lagAclMaler("A", V44);
        resultaterFacade.lagAclMaler("DA", V44);
        resultaterFacade.lagAclMaler("F", V44);
        resultaterFacade.lagAclMaler("H", V44);
        resultaterFacade.lagAclMaler("M", V44);
        resultaterFacade.lagAclMaler("P", V44);
        resultaterFacade.lagAclMaler("R", V44);
        resultaterFacade.lagAclMaler("S", V44);
        resultaterFacade.lagAclMaler("U", V44);
        resultaterFacade.lagAclMaler("X", V44);
    }

    @Test
    public void lag_PDF_maler_v44() {
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

}
