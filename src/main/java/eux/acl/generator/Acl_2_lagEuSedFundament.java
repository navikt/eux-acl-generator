package eux.acl.generator;

import eux.acl.generator.facade.FundamentFacade;

import static eux.acl.generator.service.FilService.*;
import static eux.acl.generator.service.FilService.SEDTYPER_X;

public class Acl_2_lagEuSedFundament {
    public static void main(String[] args) {

        FundamentFacade fundamentFacade = new FundamentFacade();
        String cdmVersjon = V43;
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

        cdmVersjon = V44;
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
