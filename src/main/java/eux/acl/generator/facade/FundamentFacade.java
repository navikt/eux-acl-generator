package eux.acl.generator.facade;

import eux.acl.generator.modell.*;
import eux.acl.generator.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static eux.acl.generator.service.FilService.V43;

@Slf4j
public class FundamentFacade {
    private final FilService filService;
    private final LogiskModellService logiskModellService;
    private final OversettelserService oversettelserService;
    private final KodeverkService kodeverkService;
    private final EuSedService euSedService;


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
        Map<String, String> tekstIdVsTekst = oversettelserService.lagOversettelser(oversettelser, cdmVersjon);
        sedTyper.forEach(sedType ->
            euSedService.lagEuSedFundamentOgSkrivTilFil(sedType, tekstIdVsEnumtype, tekstIdVsTekst, cdmVersjon));
    }

    public void trekkUtEnumSettFundamentOgSkrivTilFil(List<String> sedTyper, String cdmVersjon) {
        String lmXml = filService.lesCdmLogiskModellXmlFil(cdmVersjon);
        Map<String, String> tekstIdVsEnumNavn = logiskModellService.uttrekkFraLogiskModell(lmXml);
        String oversettelser = filService.lesOversettelsesfil(cdmVersjon);
        Map<String, String> tekstIdVsTekst = oversettelserService.lagOversettelser(oversettelser, cdmVersjon);
//        Set<String> enumtyperPrSed = new HashSet<>();
//        Set<String> enumtyper = new HashSet<>();
        sedTyper.forEach(sedType ->
                kodeverkService.lagEnumFundamentFilOgSkrivTilFil(sedType, tekstIdVsEnumNavn, tekstIdVsTekst, cdmVersjon));
//        filService.skrivEnumNavnTilFil(enumtyperPrSed, "enumPrSedListe.txt", cdmVersjon);
//        filService.skrivEnumNavnTilFil(enumtyper, "enumListe.txt", cdmVersjon);
    }



}
