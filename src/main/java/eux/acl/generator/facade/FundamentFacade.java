package eux.acl.generator.facade;

import eux.acl.generator.modell.*;
import eux.acl.generator.service.FilService;
import eux.acl.generator.service.OversettelserService;
import eux.acl.generator.service.UttrekkFraCdmMetaService;
import eux.acl.generator.service.LogiskModellService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static eux.acl.generator.service.FilService.V43;

@Slf4j
public class FundamentFacade {
    private final FilService filService;
    private final UttrekkFraCdmMetaService cdmMetaService;
    private final LogiskModellService logiskModellService;
    private final OversettelserService oversettelserService;
    private final static String OVERSETTELSE_V44 = "4.4";
    private final static String OVERSETTELSE_V43 = "4.3";

    public FundamentFacade() {
        this.filService = new FilService();
        this.cdmMetaService = new UttrekkFraCdmMetaService();
        this.logiskModellService = new LogiskModellService();
        this.oversettelserService = new OversettelserService();
    }

    public void trekkUtEnumsettOgSkrivTilFil(List<String> sedTyper, String cdmVersjon) {
        String lmXml = filService.lesCdmLogiskModellXmlFil(cdmVersjon);
        Map<String, String> tekstIdVsEnumNavn = logiskModellService.uttrekkFraLogiskModell(lmXml);
        String oversettelser = filService.lesOversettelsesfil(cdmVersjon);
        Map<String, String> tekstIdVsTekst = oversettelserService.lagOversettelser(oversettelser, cdmVersjon);
        Set<String> enumtyperPrSed = new HashSet<>();
        Set<String> enumtyper = new HashSet<>();
        sedTyper.forEach(sedType -> {
                    Map<String, Object> cdmMetaSed = filService.lesCdmMetaFilForEnSed(sedType, cdmVersjon);
                    UttrekkFundament uttrekkFundament = cdmMetaService.lagCdmUttrekkFundament(
                            cdmMetaSed, tekstIdVsTekst, tekstIdVsEnumNavn);
                    Map<String, TreeMap<Integer, EnumkodeFundament>> enumNavnVsEnumInnhold = new HashMap<>();
                    cdmMetaService.opprettEnumene(enumNavnVsEnumInnhold, uttrekkFundament);
                    cdmMetaService.fyllInnEnumkodeId(enumNavnVsEnumInnhold, uttrekkFundament);
                    cdmMetaService.fyllInnEnumkodeBeskrivelse(enumNavnVsEnumInnhold, uttrekkFundament);
                    cdmMetaService.fyllInnEnumkoderMedBeskrivelser(enumNavnVsEnumInnhold);
                    enumNavnVsEnumInnhold.forEach((enumtype, enuminnhold) -> {
                        filService.skrivKodeverksfundamentTilFil(enumtype, enuminnhold, sedType, cdmVersjon);
                        enumtyperPrSed.add(enumtype + ": " + sedType);
                        enumtyper.add(enumtype);
                    });
                }
        );
        filService.skrivEnumNavnTilFil(enumtyperPrSed, "enumPrSedListe.txt", cdmVersjon);
        filService.skrivEnumNavnTilFil(enumtyper, "enumListe.txt", cdmVersjon);
    }

    public void trekkUtEuSedFundamentOgSkrivTilFil(List<String> sedTyper, String cdmVersjon) {
        String lmXml = filService.lesCdmLogiskModellXmlFil(cdmVersjon);
        Map<String, String> tekstIdVsEnumtype = logiskModellService.uttrekkFraLogiskModell(lmXml);
        String oversettelser = filService.lesOversettelsesfil(cdmVersjon);
        Map<String, String> tekstIdVsTekst = oversettelserService.lagOversettelser(oversettelser, cdmVersjon);
        sedTyper.forEach(sedType -> {
                    Map<String, Object> cdmMetaSed = filService.lesCdmMetaFilForEnSed(sedType, cdmVersjon);
                    UttrekkFundament uttrekkFundament = cdmMetaService.lagCdmUttrekkFundament(cdmMetaSed, tekstIdVsTekst, tekstIdVsEnumtype);
                    TreeMap<String, EuSedFundament> euSedFundament = cdmMetaService.lagEuSedFundament(uttrekkFundament);
                    filService.skrivEuSedMalFundamentTilFil(euSedFundament, sedType, cdmVersjon);
                }
        );
    }

    public List<String> lagEuSedFraCdmMeta(String sedType, String cdmVersjon) {
        Map<String, Object> cdmMetaSed = filService.lesCdmMetaFilForEnSed(sedType, cdmVersjon);
        UttrekkCdmMeta uttrekkCdmMeta = cdmMetaService.lagUttrekkFraCdmMeta(cdmMetaSed);
        return cdmMetaService.lagEuSedFraCdm(uttrekkCdmMeta);
    }

    public List<String> lagEuSedFraAclMal(String sedType, String cdmVersjon) {
        Map<String, Object> aclMal = filService.lesAclMal(sedType, cdmVersjon);
        return aclMal.keySet().stream()
                .filter(k -> !k.contains("sedGVer") && !k.contains("sedVer"))
                .sorted().toList();
    }

    public void sammenlikneEuSedFraCdmMedEuSedFraFraAcl(String sedType, String cdmVersjon) {
        var cdmSedListe = lagEuSedFraCdmMeta(sedType, cdmVersjon);
        var aclSedListe = lagEuSedFraAclMal(sedType, cdmVersjon);
        for (int nr = 0; nr < aclSedListe.size(); nr++) {
            if (!aclSedListe.get(nr).equals(cdmSedListe.get(nr))) {
                log.error(sedType + ": mismatch. \nAcl={}\nCdm={}", aclSedListe.get(nr), cdmSedListe.get(nr));
                return;
            }
        }
        if (cdmSedListe.size() != aclSedListe.size()) {
            log.error(sedType + ": mismatch. Ulikt linjeantall. Acl={}, Cdm={}"
                    , aclSedListe.size(), cdmSedListe.size());
        } else {
            log.info(sedType + ": OK");
        }
    }

    public EuSedCdmVersjonsdiff diffMellomUlikeCdmSedVersjoner(String sedType, String cdmGammelVersjon, String cdmNyVersjon) {
        List<String> cdmNyVersjonListe = lagEuSedFraCdmMeta(sedType, cdmNyVersjon);
        List<String> cdmGammelVersjonListe = lagEuSedFraCdmMeta(sedType, cdmGammelVersjon);
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

    public void sammenlikneCdmSedVersjonerOgSkrivDiffTilFiler(List<String> sedTyper, String cdmGammelVersjon, String cdmNyVersjon) {
        Map<String, List<String>> sektorVsNytt = new HashMap<>();
        sedTyper.forEach(sedType -> {
            EuSedCdmVersjonsdiff diff = diffMellomUlikeCdmSedVersjoner(sedType, cdmGammelVersjon, cdmNyVersjon);
            filService.skrivDiffTilFil(diff.getUendredeStierListe(), sedType, cdmNyVersjon, FilService.FIL_UENDREDE_STIER);
            filService.skrivDiffTilFil(diff.getNyeStierListe(), sedType, cdmNyVersjon, FilService.FIL_NYE_STIER);
            filService.skrivDiffTilFil(diff.getUtgaatteStierListe(), sedType, cdmNyVersjon, FilService.FIL_UTGAATTE_STIER);

            String sektor = StringUtils.splitByCharacterType(sedType)[0];
            if (!sektorVsNytt.containsKey(sektor)) {
                sektorVsNytt.put(sektor, new ArrayList<>());
            }
            sektorVsNytt.get(sektor).addAll(diff.getNyeStierListe());
        });
        sektorVsNytt.forEach((sektor, nyttListe) -> {
            filService.skrivDiffTilFil(nyttListe, sektor + "-nytt", cdmNyVersjon, FilService.FIL_NYE_STIER);
        });
    }

    // ---
    public void lesAclMalOgSkrivDenTilFil(String sedType, String cdmVersjon) {
        Map<String, Object> aclMal = filService.lesAclMal(sedType, cdmVersjon);
        List<String> cdmStierVsNavStierListe = aclMal.keySet().stream()
                .filter(k -> !k.contains("sedGVer") && !k.contains("sedVer"))
                .map(k -> k + ": " + aclMal.get(k))
                .toList();
        filService.skrivAclSedTilFil(cdmStierVsNavStierListe, sedType, cdmVersjon);
    }

//    public void sammenlikneSedFraAclOgSedFraCdm(String sedType, String cdmVersjon) {
//        List<String> aclMalListe = filService.lesAclMalFraTarget(sedType, cdmVersjon);
//        List<String> cdmSedListe = filService.lesCdmSedFraTarget(sedType, cdmVersjon);
//        List<String> aclSedListe = aclMalListe.stream().map(l -> l.split(":")[0]).toList();
//        for (int nr = 0; nr < aclMalListe.size(); nr++) {
//            if (!aclSedListe.get(nr).equals(cdmSedListe.get(nr))) {
//                log.error(sedType + ": mismatch. Acl={}, Cdm={}", aclSedListe.get(nr), cdmSedListe.get(nr));
//                return;
//            }
//        }
//        log.info(sedType + ": OK");
//    }

    public void sammenliknerGammelAclSedMedNyCdmVersjonOgSkriverDiffTilFil(String sedType, String nyCdmVersjon, String aclVersjon) {
        List<String> nyCdmSedListe = filService.lesCdmSedFraTarget(sedType, nyCdmVersjon);
        List<String> aclMalListe = filService.lesAclMalFraTarget(sedType, aclVersjon);
        if (nyCdmSedListe == null || aclMalListe == null) {
            return;
        }
        Map<String, String> aclSedStiVsNavSedSti = new HashMap<>();
        aclMalListe.forEach(l -> {
            String[] linje = l.split(": ");
            aclSedStiVsNavSedSti.put(linje[0].trim(), linje[1].trim());
        });
        List<String> aclUtgaatteStier = new ArrayList<>();
        List<String> aclUendredeStier = new ArrayList<>();
        aclSedStiVsNavSedSti.keySet().forEach(s -> {
            if (nyCdmSedListe.contains(s)) {
                aclUendredeStier.add(s + ": " + aclSedStiVsNavSedSti.get(s));
            } else {
                aclUtgaatteStier.add(s + ": " + aclSedStiVsNavSedSti.get(s));
            }
        });
        filService.skrivDiffTilFil(aclUendredeStier, sedType, nyCdmVersjon, FilService.FIL_UENDREDE_ACL_STIER);
        filService.skrivDiffTilFil(aclUtgaatteStier, sedType, nyCdmVersjon, FilService.FIL_UTGAATTE_ACL_STIER);
    }

    public void skrivBasisForNyeAclMalerTilSammeFil(List<String> sedTyper, String cdmVersjon, String aclVersjon) {
        List<String> aclMaler = new ArrayList<>();
        sedTyper.forEach(sedType -> {
            List<String> cdmNyeSedStier = filService.lesNyeStierFraTarget(sedType, cdmVersjon);
            List<String> aclUendredeStier = filService.lesUendredeAclStierFraTarget(sedType, aclVersjon);
            if (aclUendredeStier != null) {
                aclMaler.addAll(aclUendredeStier);
                aclMaler.addAll(cdmNyeSedStier);
            }
        });
        List<String> aclMalerMed2Kolon = aclMaler.stream()
                .map(l -> l.replaceFirst("\\.", ":"))
                .toList();
        filService.skrivAclMalerTilFil(aclMalerMed2Kolon, "H", "alle.txt", V43);
    }

    public void lagEiFilMedUnikeNyeCdmStier(List<String> sedTyper, String cdmVersjon, String filnavn) {
        List<String> altNyttPrSedType = new ArrayList<>();
        sedTyper.forEach(sedType ->
                altNyttPrSedType.addAll(filService.lesNyeStierFraTarget(sedType, cdmVersjon)));
        List<String> nye = altNyttPrSedType.stream()
                .map(s -> StringUtils.substringAfter(s, ".")).toList();
        List<String> uniktNytt = new HashSet<>(nye).stream().sorted().toList();

        filService.skrivNyeUnikeStierTilFil(uniktNytt, filnavn, cdmVersjon);
    }

    public void lagEiFilMedUnikeUtgaatteAclStier(List<String> sedTyper, String cdmVersjon, String filnavn) {
        List<String> utgaattPrSedType = new ArrayList<>();
        sedTyper.forEach(sedType -> {
            List<String> utgaatteStierAcl = filService.lesUtgaatteStierFraTarget(sedType, cdmVersjon);
            utgaattPrSedType.addAll(utgaatteStierAcl);
        });
        List<String> nye = utgaattPrSedType.stream()
                .map(s -> StringUtils.substringAfter(s, ".")).toList();
        List<String> uniktNytt = new HashSet<>(nye).stream().sorted().toList();

        filService.skrivNyeUnikeStierTilFil(uniktNytt, filnavn, cdmVersjon);
    }







    /*
Sammenlikne gammel versjon i ny, tilsendt CDM-versjon
    1: Fra CDMen: Trekk ut alle eu-sed-stiene i forrige versjon - på jsonpath-format
    2: Les acl-malene i forrige versjon på jsonPath-format
    3: Sammenlikne forrige versjon av ACl og CDM. Skal være like, skriv en ut diff.
        Hvis det er forskjeller er det nok også forskjeller i ny versjon av CDMen
---

Sammenlikne kodeverket i ny og gammel CDM-versjon:
    8: Trekk ut alle enum-er fra forrige CDM-versjon med tekst
    9: Trekk ut alle enum-er fra ny CDM-versjon med tekst
    10: Sammenlikne.
        - Skriv ut enum-navn og beskrivelser i begge versjonene når det er en endring
        - Skriv kun ny når den gamle ikke finnes
        - Skriv kun gammel når den utgår.
    11: Bruk acl for å gå fra EU-sti til nav-sti til nav-enum
    12: Rediger manuelt
    13: Lag nye enum-filer? ACL-en må endres manuelt....
---

Lag ny ACL
    4: Fra CDMen: Trekk ut alle eu-sed-stiene i ny versjon - på jsonpath-format
    5: Sammenlikne  eu-stier i ny CDM-versjon med eu-stiene i gammel acl-versjon. Skriv diff til filer.
        - ei fil med det som er likt: eu-sti vs nav-sti
        - ei fil med det som kun fins i gammel versjon: eu-sti vs nav-sti
        - ei fil med det som er nytt: kun eu-sti
    6: Manuell jobb: filene med nye stier (kopieres?) og fylles inn med nav-stier
    7: Lage nye acl-maler: Filene med det som er likt settes sammen med de manuelt utfylte filene og nye acl-maler lages.

---
    8: Finn objekter på tvers av SEDer innen en sektor - bruk nav-SEDene.
    9: Lag Java-objekter av disse oblektene. les inn verier med EU/NAV-stiene


     */

}
