package eux.acl.generator.facade;

import eux.acl.generator.eksekvering.Versjoner;
import eux.acl.generator.service.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;



@Slf4j
public class FundamentFacade extends Versjoner {
	private final FilService filService;
	private final LogiskModellService logiskModellService;
	private final OversettelserService oversettelserService;
	private final KodeverkService kodeverkService;
	private final EuSedService euSedService;

	private final String v45_workaroundMedV44 = V44;

	public FundamentFacade() {
		this.filService = new FilService();
		this.logiskModellService = new LogiskModellService();
		this.oversettelserService = new OversettelserService();
		this.kodeverkService = new KodeverkService();
		this.euSedService = new EuSedService();
	}

	public void trekkUtEuSedFundamentOgSkrivTilFil(List<String> sedTyper, String cdmVersjon) {
		String lmXml = filService.lesCdmLogiskModellXmlFil(cdmVersjon);
		Map<String, String> tekstIdVsEnumtype = logiskModellService.uttrekkFraLogiskModell(lmXml);
		String oversettelser = filService.lesOversettelsesfil(cdmVersjon);
		Map<String, String> tekstIdVsTekst = oversettelserService.lagOversettelser(oversettelser, v45_workaroundMedV44);
		sedTyper.forEach(sedType ->
			euSedService.lagEuSedFundamentOgSkrivTilFil(sedType, tekstIdVsEnumtype, tekstIdVsTekst, cdmVersjon));
	}

	public void trekkUtEnumSettFundamentOgSkrivTilFil(List<String> sedTyper, String cdmVersjon) {
		String lmXml = filService.lesCdmLogiskModellXmlFil(v45_workaroundMedV44);
		Map<String, String> tekstIdVsEnumNavn = logiskModellService.uttrekkFraLogiskModell(lmXml);
		String oversettelser = filService.lesOversettelsesfil(cdmVersjon);
		Map<String, String> tekstIdVsTekst = oversettelserService.lagOversettelser(oversettelser, v45_workaroundMedV44);
		sedTyper.forEach(sedType ->
			kodeverkService.lagEnumFundamentFilOgSkrivTilFil(sedType, tekstIdVsEnumNavn, tekstIdVsTekst, cdmVersjon));
//        filService.skrivEnumNavnTilFil(enumtyperPrSed, "enumPrSedListe.txt", cdmVersjon);
//        filService.skrivEnumNavnTilFil(enumtyper, "enumListe.txt", cdmVersjon);
	}


}
