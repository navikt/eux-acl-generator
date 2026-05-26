package eux.acl.generator.eksekvering.kodeverk;

import eux.acl.generator.eksekvering.Versjoner;
import eux.acl.generator.facade.FundamentFacade;

import static eux.acl.generator.modell.SedTyper.*;

public class Kodeverk_1_lagKodeverkFundament extends Versjoner {


	public static void main(String[] args) {
		lagKodeverkFundament(NY_VERSJON);
	}

	public static void lagKodeverkFundament(String cdmVersjon) {
		FundamentFacade fundamentFacade = new FundamentFacade();
		fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_A, cdmVersjon);
		fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_DA, cdmVersjon);
		fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_F, cdmVersjon);
		fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_H, cdmVersjon);
		fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_M, cdmVersjon);
		fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_P, cdmVersjon);
		fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_R, cdmVersjon);
		fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_S, cdmVersjon);
		fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_U, cdmVersjon);
		fundamentFacade.trekkUtEnumSettFundamentOgSkrivTilFil(SEDTYPER_X, cdmVersjon);
	}

}
