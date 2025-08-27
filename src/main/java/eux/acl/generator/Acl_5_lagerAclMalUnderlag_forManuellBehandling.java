package eux.acl.generator;

import eux.acl.generator.facade.DiffFacade;
import lombok.extern.slf4j.Slf4j;

import static eux.acl.generator.service.FilService.*;

@Slf4j
public class Acl_5_lagerAclMalUnderlag_forManuellBehandling {

    public static void main(String[] args) {
        DiffFacade diffFacade = new DiffFacade();
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_A, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_DA, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_F, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_H, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_M, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_P, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_R, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_S, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_U, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_X, V43, V44);
    }
}
