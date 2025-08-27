package eux.acl.old;

import eux.acl.generator.facade.AclMalerFacade;
import eux.acl.generator.facade.FundamentFacade;
import org.junit.jupiter.api.Test;

import java.util.List;

import static eux.acl.generator.service.FilService.*;

public class AclMalerTest {

    /**
     * Leser sed-fundament-filer og genererer mal-filer som Pdf-generatoren så bruker.
     *
     */
    @Test
    public void lager_diff_v43_v44() {

        // Krever at filene i 'fundament'-pakken er generert og plassert der.
        AclMalerFacade pdfMalerFacade = new AclMalerFacade();
        pdfMalerFacade.sammenliknerEuSedCdmVersjonerOgSkriverDiffTilFiler(List.of("F001"), V43, V44);
        pdfMalerFacade.sammenliknerEuSedCdmVersjonerOgSkriverDiffTilFiler(SEDTYPER_A, V43, V44);
        pdfMalerFacade.sammenliknerEuSedCdmVersjonerOgSkriverDiffTilFiler(SEDTYPER_DA, V43, V44);
        pdfMalerFacade.sammenliknerEuSedCdmVersjonerOgSkriverDiffTilFiler(SEDTYPER_F, V43, V44);
        pdfMalerFacade.sammenliknerEuSedCdmVersjonerOgSkriverDiffTilFiler(SEDTYPER_H, V43, V44);
        pdfMalerFacade.sammenliknerEuSedCdmVersjonerOgSkriverDiffTilFiler(SEDTYPER_M, V43, V44);
        pdfMalerFacade.sammenliknerEuSedCdmVersjonerOgSkriverDiffTilFiler(SEDTYPER_P, V43, V44);
        pdfMalerFacade.sammenliknerEuSedCdmVersjonerOgSkriverDiffTilFiler(SEDTYPER_R, V43, V44);
        pdfMalerFacade.sammenliknerEuSedCdmVersjonerOgSkriverDiffTilFiler(SEDTYPER_S, V43, V44);
        pdfMalerFacade.sammenliknerEuSedCdmVersjonerOgSkriverDiffTilFiler(SEDTYPER_U, V43, V44);
        pdfMalerFacade.sammenliknerEuSedCdmVersjonerOgSkriverDiffTilFiler(SEDTYPER_X, V43, V44);
    }

}
