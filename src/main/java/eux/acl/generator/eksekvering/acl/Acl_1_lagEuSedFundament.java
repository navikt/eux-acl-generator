package eux.acl.generator.eksekvering.acl;

import eux.acl.generator.facade.FundamentFacade;

import static eux.acl.generator.service.FilService.*;
import static eux.acl.generator.service.FilService.SEDTYPER_X;

public class Acl_1_lagEuSedFundament {
    public static void main(String[] args) {
        lagEuSedFundament(V43);
        lagEuSedFundament(V44);
    }

    public static void lagEuSedFundament(String cdmVersjon) {
        FundamentFacade fundamentFacade = new FundamentFacade();
        fundamentFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_A, cdmVersjon);
        fundamentFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_DA, cdmVersjon);
        fundamentFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_F, cdmVersjon);
        fundamentFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_H, cdmVersjon);
        fundamentFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_M, cdmVersjon);
        fundamentFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_P, cdmVersjon);
        fundamentFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_R, cdmVersjon);
        fundamentFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_S, cdmVersjon);
        fundamentFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_U, cdmVersjon);
        fundamentFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_X, cdmVersjon);
    }
}
