package eux.acl.generator.eksekvering.acl;

import eux.acl.generator.eksekvering.Versjoner;
import eux.acl.generator.facade.DiffFacade;
import lombok.extern.slf4j.Slf4j;

import static eux.acl.generator.modell.SedTyper.*;

@Slf4j
public class Acl_4_sammenliknerUlikCdmOgAclVersjonerDiffTilFiler extends Versjoner {

	public static void main(String[] args) {
		sammenliknerUlikeCdmOgAclVersjonerOgSkriverDiffTilFiler(FORRIGE_VERSJON, NY_VERSJON);
	}

	public static void sammenliknerUlikeCdmOgAclVersjonerOgSkriverDiffTilFiler(String dagensVersjon, String nyVersjon) {
		DiffFacade diffFacade = new DiffFacade();
		diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_A, dagensVersjon, nyVersjon);
		diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_DA, dagensVersjon, nyVersjon);
		diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_F, dagensVersjon, nyVersjon);
		diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_H, dagensVersjon, nyVersjon);
		diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_M, dagensVersjon, nyVersjon);
		diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_P, dagensVersjon, nyVersjon);
		diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_R, dagensVersjon, nyVersjon);
		diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_S, dagensVersjon, nyVersjon);
		diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_U, dagensVersjon, nyVersjon);
		diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_X, dagensVersjon, nyVersjon);
	}

}
