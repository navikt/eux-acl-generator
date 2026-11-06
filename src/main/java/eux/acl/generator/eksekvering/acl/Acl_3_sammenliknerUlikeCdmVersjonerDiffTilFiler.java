package eux.acl.generator.eksekvering.acl;

import eux.acl.generator.eksekvering.Versjoner;
import eux.acl.generator.facade.DiffFacade;

import static eux.acl.generator.modell.SedTyper.*;

public class Acl_3_sammenliknerUlikeCdmVersjonerDiffTilFiler extends Versjoner {

	public static void main(String[] args) {
		sammenlikerToUlikeCdmVersjoner(FORRIGE_VERSJON, NY_VERSJON);
	}

	public static void sammenlikerToUlikeCdmVersjoner(String eldreVersjon, String nyereVersjon) {
		DiffFacade diffFacade = new DiffFacade();
		diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_A, eldreVersjon, nyereVersjon);
		diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_DA, eldreVersjon, nyereVersjon);
		diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_F, eldreVersjon, nyereVersjon);
		diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_H, eldreVersjon, nyereVersjon);
		diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_M, eldreVersjon, nyereVersjon);
		diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_P, eldreVersjon, nyereVersjon);
		diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_P3000, eldreVersjon, nyereVersjon);
		diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_R, eldreVersjon, nyereVersjon);
		diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_S, eldreVersjon, nyereVersjon);
		diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_U, eldreVersjon, nyereVersjon);
		diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_X, eldreVersjon, nyereVersjon);
	}
}
