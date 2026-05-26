package eux.acl.generator.eksekvering.acl;

import eux.acl.generator.eksekvering.Versjoner;
import eux.acl.generator.facade.DiffFacade;

import static eux.acl.generator.modell.SedTyper.*;

public class Acl_2_sammenliknerSammeCdmOgAclVersjon extends Versjoner {

	public static void main(String[] args) {
		// Disse skal være like når alt er korrekt
		sammenliknerForrigeVersjonerAvCDMogACL(FORRIGE_VERSJON);
	}

	public static void sammenliknerForrigeVersjonerAvCDMogACL(String forrigeCdmVersjon) {
		DiffFacade diffFacade = new DiffFacade();
		diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_A, forrigeCdmVersjon);
		diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_DA, forrigeCdmVersjon);
		diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_F, forrigeCdmVersjon);
		diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_H, forrigeCdmVersjon);
		diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_M, forrigeCdmVersjon);
		diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_P, forrigeCdmVersjon);
		diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_R, forrigeCdmVersjon);
		diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_S, forrigeCdmVersjon);
		diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_U, forrigeCdmVersjon);
		diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_X, forrigeCdmVersjon);
	}
}
