package eux.acl.generator.service;

import eux.acl.generator.modell.CdmUttrekk;
import eux.acl.generator.modell.EuSedCdmVersjonsdiff;
import eux.acl.generator.modell.EuSedFundament;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class EuSedService {
    private final FilService filService;
    private final CdmMetaService cdmMetaService;

    public EuSedService() {
        this.filService = new FilService();
        this.cdmMetaService = new CdmMetaService();
    }

    public void lagEuSedFundamentOgSkrivTilFil(String sedType,
                                               Map<String, String> tekstIdVsEnumtype,
                                               Map<String, String> tekstIdVsTekst,
                                               String cdmVersjon) {
        Map<String, Object> cdmMetaSed = filService.lesCdmMetaFilForEnSed(sedType, cdmVersjon);
        CdmUttrekk cdmUttrekk = cdmMetaService.lagCdmUttrekk(cdmMetaSed, tekstIdVsTekst, tekstIdVsEnumtype);
        TreeMap<String, EuSedFundament> euSedFundament = cdmMetaService.lagEuSedFundament(cdmUttrekk);
        filService.skrivEuSedMalFundamentTilFil(euSedFundament, sedType, cdmVersjon);
    }

    public void leggTilNyeDiffer(String sedType, EuSedCdmVersjonsdiff diff, Map<String, List<String>> sektorVsNyeStier) {
        String sektor = StringUtils.splitByCharacterType(sedType)[0];
        if (!sektorVsNyeStier.containsKey(sektor)) {
            sektorVsNyeStier.put(sektor, new ArrayList<>());
        }
        sektorVsNyeStier.get(sektor).addAll(diff.getNyeStierListe());
    }

    public void skrivDiffOppsummeringTilEiFil(Map<String, List<String>> sektorVsNyeStier, String nyVersjon) {
        sektorVsNyeStier.forEach((sektor, nyttListe) -> {
            filService.skrivDiffTilFil(nyttListe, sektor + "-sektor", nyVersjon, FilService.FIL_MED_ENDRINGER_STIER);
        });
    }

    public EuSedCdmVersjonsdiff diffMellomUlikeCdmSedVersjoner(String sedType, String cdmGammelVersjon, String cdmNyVersjon) {
        List<String> cdmNyVersjonListe = lagCdmEuSedFraFundament(sedType, cdmNyVersjon);
        List<String> cdmGammelVersjonListe = lagCdmEuSedFraFundament(sedType, cdmGammelVersjon);
        if (cdmNyVersjonListe == null || cdmGammelVersjonListe == null) {
            return null;
        }
        List<String> uendredeStierListe = new ArrayList<>();
        List<String> nyeStierListe = new ArrayList<>();
        List<String> utgaatteStierListe = new ArrayList<>();
        cdmNyVersjonListe.forEach(l -> {
            if (cdmGammelVersjonListe.contains(l)) {
                uendredeStierListe.add(l);
            } else {
                nyeStierListe.add(l);
            }
        });
        cdmGammelVersjonListe.forEach(l -> {
            if (!uendredeStierListe.contains(l)) {
                utgaatteStierListe.add(l);
            }
        });
        return new EuSedCdmVersjonsdiff(uendredeStierListe, nyeStierListe, utgaatteStierListe);
    }

    public List<String> lagCdmEuSedFraFundament(String sedType, String cdmVersjon) {
        Map<String, EuSedFundament> euSedFundament = filService.lesEuSedFundamentFil(sedType, cdmVersjon);
        return euSedFundament.keySet().stream().sorted().toList();
    }
}
