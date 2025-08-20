package eux.acl.generator;

import eux.acl.generator.facade.FundamentFacade;
import static eux.acl.generator.service.FilService.*;

public class Kodeverk_1_lagKodeverkFundament {


    public static void main(String[] args) {

        FundamentFacade fundamentFacade = new FundamentFacade();
        String cdmVersjon = V43;
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_M, cdmVersjon);
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_A, cdmVersjon);
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_DA, cdmVersjon);
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_F, cdmVersjon);
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_H, cdmVersjon);
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_P, cdmVersjon);
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_R, cdmVersjon);
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_S, cdmVersjon);
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_U, cdmVersjon);
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_X, cdmVersjon);

        cdmVersjon = V44;
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_M, cdmVersjon);
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_A, cdmVersjon);
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_DA, cdmVersjon);
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_F, cdmVersjon);
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_H, cdmVersjon);
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_P, cdmVersjon);
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_R, cdmVersjon);
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_S, cdmVersjon);
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_U, cdmVersjon);
        fundamentFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_X, cdmVersjon);
    }

}
