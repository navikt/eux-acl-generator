package eux.acl.generator.eksekvering.acl;

import eux.acl.generator.eksekvering.Versjoner;
import eux.acl.generator.facade.DiffFacade;

import static eux.acl.generator.modell.SedTyper.*;

public class Acl_6_sammenliknerAclVersjoner extends Versjoner {

	public static void main(String[] args) {
		sammenliknerToAclVersjonerSkriverDiffTilFiler(FORRIGE_VERSJON, NY_VERSJON);
	}

	public static void sammenliknerToAclVersjonerSkriverDiffTilFiler(String aclVersjonForrige, String aclVersjonNy) {
		DiffFacade diffFacade = new DiffFacade();
		diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_A, aclVersjonForrige, aclVersjonNy);
		diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_DA, aclVersjonForrige, aclVersjonNy);
		diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_F, aclVersjonForrige, aclVersjonNy);
		diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_H, aclVersjonForrige, aclVersjonNy);
		diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_M, aclVersjonForrige, aclVersjonNy);
		diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_P, aclVersjonForrige, aclVersjonNy);
		diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_R, aclVersjonForrige, aclVersjonNy);
		diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_S, aclVersjonForrige, aclVersjonNy);
		diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_U, aclVersjonForrige, aclVersjonNy);
		diffFacade.sammenliknerToAclVersjonerOgSkriverDiffTilFil(SEDTYPER_X, aclVersjonForrige, aclVersjonNy);
	}
}