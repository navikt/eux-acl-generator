package eux.acl.generator.eksekvering.acl;

import eux.acl.generator.facade.DiffFacade;

import static eux.acl.generator.modell.SedTyper.*;
import static eux.acl.generator.service.FilService.*;

public class Acl_6_sammenliknerAclVersjoner {

    public static void main(String[] args) {
        sammenliknerToAclVersjonerSkriverDiffTilFiler(V43, V44);
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