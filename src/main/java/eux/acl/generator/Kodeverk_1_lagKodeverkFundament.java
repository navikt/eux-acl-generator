package eux.acl.generator;

import eux.acl.generator.facade.FundamentFacade;
import static eux.acl.generator.service.FilService.*;

public class Kodeverk_1_lagKodeverkFundament {


    public static void main(String[] args) {

        FundamentFacade fundamentFacade = new FundamentFacade();
        String cdmVersjon = V43;
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_M, cdmVersjon);
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_A, cdmVersjon);
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_DA, cdmVersjon);
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_F, cdmVersjon);
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_H, cdmVersjon);
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_P, cdmVersjon);
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_R, cdmVersjon);
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_S, cdmVersjon);
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_U, cdmVersjon);
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_X, cdmVersjon);

        cdmVersjon = V44;
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_M, cdmVersjon);
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_A, cdmVersjon);
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_DA, cdmVersjon);
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_F, cdmVersjon);
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_H, cdmVersjon);
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_P, cdmVersjon);
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_R, cdmVersjon);
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_S, cdmVersjon);
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_U, cdmVersjon);
        fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_X, cdmVersjon);
    }

}
