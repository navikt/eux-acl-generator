package eux.acl.generator.eksekvering.acl;

import eux.acl.generator.facade.DiffFacade;

import static eux.acl.generator.service.FilService.*;

public class Acl_2_sammenliknerSammeCdmOgAclVersjon {

    public static void main(String[] args) {
        sammenliknerEksisterendeVersjonerAvCDMogACL(V43);
    }

    public static void sammenliknerEksisterendeVersjonerAvCDMogACL(String cdmVersjon) {
        DiffFacade diffFacade = new DiffFacade();
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_A, cdmVersjon);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_DA, cdmVersjon);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_F, cdmVersjon);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_H, cdmVersjon);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_M, cdmVersjon);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_P, cdmVersjon);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_R, cdmVersjon);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_S, cdmVersjon);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_U, cdmVersjon);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_X, cdmVersjon);
    }
}
