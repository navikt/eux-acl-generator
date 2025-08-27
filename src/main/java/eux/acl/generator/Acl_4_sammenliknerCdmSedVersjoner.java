package eux.acl.generator;

import eux.acl.generator.facade.DiffFacade;

import static eux.acl.generator.service.FilService.*;
import static eux.acl.generator.service.FilService.V43;

public class Acl_4_sammenliknerCdmSedVersjoner {

    public static void main(String[] args) {
        DiffFacade diffFacade = new DiffFacade();
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_A, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_DA, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_F, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_H, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_M, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_P, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_R, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_S, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_U, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_X, V43, V44);

    }
}
