package eux.acl.generator.eksekvering.acl;

import eux.acl.generator.facade.DiffFacade;

import static eux.acl.generator.service.FilService.*;
import static eux.acl.generator.service.FilService.V43;

public class Acl_3_sammenliknerUlikeCdmVersjonerDiffTilFiler {

    public static void main(String[] args) {
        sammenlikerToUlikeCdmVersjoner(V43, V44);
    }

    public static void sammenlikerToUlikeCdmVersjoner(String eldreVersjon, String nyereVersjon) {
        DiffFacade diffFacade = new DiffFacade();
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_A, eldreVersjon, nyereVersjon);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_DA, eldreVersjon, nyereVersjon);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_F, eldreVersjon, nyereVersjon);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_H, eldreVersjon, nyereVersjon);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_M, eldreVersjon, nyereVersjon);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_P, eldreVersjon, nyereVersjon);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_R, eldreVersjon, nyereVersjon);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_S, eldreVersjon, nyereVersjon);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_U, eldreVersjon, nyereVersjon);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_X, eldreVersjon, nyereVersjon);
    }
}
