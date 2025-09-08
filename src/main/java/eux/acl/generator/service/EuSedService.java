package eux.acl.generator.service;

import eux.acl.generator.modell.CdmUttrekk;
import eux.acl.generator.modell.EuSedCdmVersjonsdiff;
import eux.acl.generator.modell.EuSedFundament;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class EuSedService {
    private final FilService filService;
    private final CdmMetaService cdmMetaService;

    public EuSedService() {
        this.filService = new FilService();
        this.cdmMetaService = new CdmMetaService();
    }

    public void lagEuSedFundamentOgSkrivTilFil(String sedType,
                                               Map<String, String> tekstIdVsEnumNavn,
                                               Map<String, String> tekstIdVsTekst,
                                               String cdmVersjon) {
        Map<String, Object> cdmMetaSed = filService.lesCdmMetaFilForEnSed(sedType, cdmVersjon);
        CdmUttrekk cdmUttrekk = cdmMetaService.lagCdmUttrekk(cdmMetaSed, tekstIdVsTekst, tekstIdVsEnumNavn);
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
        TreeMap<String, EuSedFundament> nyCdmSedVersjon = lagCdmEuSedMedFundament(sedType, cdmNyVersjon);
        TreeMap<String, EuSedFundament>  gammelCdmSedVersjon = lagCdmEuSedMedFundament(sedType, cdmGammelVersjon);
        if (nyCdmSedVersjon == null || gammelCdmSedVersjon == null) {
            return null;
        }
        List<String> cdmNyVersjonListe = nyCdmSedVersjon.keySet().stream().toList();
        List<String> cdmGammelVersjonListe = gammelCdmSedVersjon.keySet().stream().toList();
        List<String> uendredeStierListe = new ArrayList<>();
        List<String> nyeStierListe = new ArrayList<>();
        List<String> utgaatteStierListe = new ArrayList<>();
        cdmNyVersjonListe.forEach(sti -> {
            if (cdmGammelVersjonListe.contains(sti)) {
                uendredeStierListe.add(sti);
            } else {
                nyeStierListe.add(sti);
            }
        });
        cdmGammelVersjonListe.forEach(sti -> {
            if (!uendredeStierListe.contains(sti)) {
                utgaatteStierListe.add(sti);
            }
        });
        return new EuSedCdmVersjonsdiff(uendredeStierListe, nyeStierListe, utgaatteStierListe);
    }

    public TreeMap<String, EuSedFundament>  lagCdmEuSedMedFundament(String sedType, String cdmVersjon) {
        Map<String, EuSedFundament> alleLeddStiVsFundament = filService.lesEuSedFundamentFil(sedType, cdmVersjon);

        return new TreeMap<>(
                alleLeddStiVsFundament.entrySet().stream()
                        .filter(entry ->
                                List.of("tekst", "enum", "dato").contains(entry.getValue().getVerdiType()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }
}
