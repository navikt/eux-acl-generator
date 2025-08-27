package eux.acl.generator;

import eux.acl.generator.facade.DiffFacade;

import static eux.acl.generator.service.FilService.*;

public class Acl_3_sammenliknerCdmSedMedAclSed {

    public static void main(String[] args) {
        DiffFacade diffFacade = new DiffFacade();
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_A, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_DA, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_F, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_H, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_M, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_P, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_R, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_S, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_U, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_X, V43);
    }
}
