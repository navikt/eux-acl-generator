package eux.acl.generator.eksekvering.acl;

import eux.acl.generator.eksekvering.Versjoner;
import eux.acl.generator.facade.FundamentFacade;

import static eux.acl.generator.modell.SedTyper.*;

public class Acl_1_lagEuSedFundament extends Versjoner {
    public static void main(String[] args) {
        lagEuSedFundament(NY_VERSJON);
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
