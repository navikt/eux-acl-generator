package eux.acl.generator.facade;

import eux.acl.generator.modell.*;
import eux.acl.generator.service.FilService;
import eux.acl.generator.service.LogiskModellService;
import eux.acl.generator.service.OversettelserService;
import eux.acl.generator.service.CdmMetaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static eux.acl.generator.service.FilService.V43;
import static eux.acl.generator.service.FilService.V44;

@Slf4j
public class DiffFacade {
    private final FilService filService;
    private final CdmMetaService cdmMetaService;
    private final LogiskModellService logiskModellService;
    private final OversettelserService oversettelserService;
    private final static String OVERSETTELSE_V44 = "4.4";
    private final static String OVERSETTELSE_V43 = "4.3";

    public DiffFacade() {
        this.filService = new FilService();
        this.cdmMetaService = new CdmMetaService();
        this.logiskModellService = new LogiskModellService();
        this.oversettelserService = new OversettelserService();
    }

    public void sammenliknerEuSedFraCdmMedEuSedFraAcl(List<String> sedTyper, String cdmVersjon) {
        sedTyper.forEach(sedType -> {
            var cdmSedListe = euSedFraFundament(sedType, cdmVersjon);
            var aclSedListe = lagEuSedFraAclMal(sedType, cdmVersjon);
            for (int nr = 0; nr < aclSedListe.size(); nr++) {
                if (!aclSedListe.get(nr).equals(cdmSedListe.get(nr))) {
                    log.error(sedType + " mismatch:\nAcl={}\nCdm={}", aclSedListe.get(nr), cdmSedListe.get(nr));
                    throw new RuntimeException("Mismatch mellom CDM- og ACL-Sed - se loggen");
                }
            }
            if (cdmSedListe.size() != aclSedListe.size()) {
                log.error(sedType + " mismatch. Ulikt linjeantall. Acl={}, Cdm={}"
                        , aclSedListe.size(), cdmSedListe.size());
                throw new RuntimeException("Mismatch mellom CDM- og ACL-Sed - se loggen");
            } else {
                log.info(sedType + ": OK");
            }
        });
    }

    public void sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(List<String> sedTyper, String cdmGammelVersjon, String cdmNyVersjon) {
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
            filService.skrivDiffTilFil(nyttListe, sektor + "-sektor", cdmNyVersjon, FilService.FIL_NYE_STIER);
        });
    }

    private EuSedCdmVersjonsdiff diffMellomUlikeCdmSedVersjoner(String sedType, String cdmGammelVersjon, String cdmNyVersjon) {
        List<String> cdmNyVersjonListe = euSedFraFundament(sedType, cdmNyVersjon);
        List<String> cdmGammelVersjonListe = euSedFraFundament(sedType, cdmGammelVersjon);
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

    public void sammenliknerKodeverkForToCdmVersjonerOgSkriverDiffTilFil(String cdmVersjonGml, String cdmVersjonNy) {
        List<String> oppsummeringTilFil = new ArrayList<>();
        List<String> enumNavnEndredeFiler = new ArrayList<>();
        List<String> enumNavnFilerNy = filService.lesEnumFundamentFilnavnene(cdmVersjonNy);
        List<String> enumNavnFilerGml = filService.lesEnumFundamentFilnavnene(cdmVersjonGml);
        List<String> enumNavnFilerFelles = enumNavnFilerNy.stream().filter(enumNavnFilerGml::contains).toList();
        List<String> enumNavnNyeFiler = enumNavnFilerNy.stream().filter(n -> !enumNavnFilerGml.contains(n)).toList();
        List<String> enumNavnUtgaatteFiler = enumNavnFilerGml.stream().filter(n -> !enumNavnFilerNy.contains(n)).toList();
        enumNavnFilerFelles.forEach(enumNavn -> {
            List<String> enumkodeLinjerNy = enumkoderFundamentTilEnumkoderListet(enumNavn, cdmVersjonNy);
            List<String> enumkodeLinjerGml = enumkoderFundamentTilEnumkoderListet(enumNavn, cdmVersjonGml);
            List<String> enumkoderNye = enumkodeLinjerNy.stream().filter(k -> !enumkodeLinjerGml.contains(k)).toList();
            List<String> enumkoderUtgaatt = enumkodeLinjerGml.stream().filter(k -> !enumkodeLinjerNy.contains(k)).toList();
            if (!enumkoderNye.isEmpty() || !enumkoderUtgaatt.isEmpty()) {
                oppsummeringTilFil.add("---");
                oppsummeringTilFil.add(enumNavn);
                oppsummeringTilFil.add("--- Nye:");
                oppsummeringTilFil.addAll(enumkoderNye);
                oppsummeringTilFil.add("---Utgått:");
                oppsummeringTilFil.addAll(enumkoderUtgaatt);
                enumNavnEndredeFiler.add(enumNavn);
            }
        });
        oppsummeringTilFil.add("========================");
        oppsummeringTilFil.add("--- Nye filer:");
        oppsummeringTilFil.addAll(enumNavnNyeFiler);
        oppsummeringTilFil.add("--- Utgåtte filer:");
        oppsummeringTilFil.addAll(enumNavnUtgaatteFiler);
        oppsummeringTilFil.add("---");
        filService.skrivKodeverkDiffTilEiFil(oppsummeringTilFil, cdmVersjonNy);
        // skriv nyeOgEndretKodeverksfiler til fil
        List<String> enumNavnEndredeOgNyeFiler = new ArrayList<>(enumNavnNyeFiler);
        enumNavnEndredeOgNyeFiler.addAll(enumNavnEndredeFiler);
        enumNavnEndredeOgNyeFiler.forEach(enumNavn -> {
            TreeMap<Integer, EnumkodeFundament> enumInnhold = filService.lesEnumFundamentFil(enumNavn, cdmVersjonNy);
            filService.skrivKodeverksfundamentTilFil(enumInnhold, enumNavn, "nyeOgEndrede", cdmVersjonNy);
        });
    }

    private List<String> enumkoderFundamentTilEnumkoderListet(String enumNavn, String cdmVersjonNy) {
        TreeMap<Integer, EnumkodeFundament> enumIdVsEnumkodeInfoNy = filService.lesEnumFundamentFil(enumNavn, cdmVersjonNy);
        return enumIdVsEnumkodeInfoNy.values().stream()
                .map(f -> f.getEnumkodeId() + " " + f.getEnumkodeBeskrivelse())
                .toList();
    }

    public void diffMellomEnumTilFiler(List<String> sedTyper, String cdmVersjonGml, String cdmVersjonNy) {

    }
//
//        Map<String, List<EnumkodeFundament>> enumIdVsEnumInnholdNy = new HashMap<>();
//        Map<String, String> enumNavnFelles = new HashMap<>();
//        Set<String> kunIGml = new HashSet<>();
//        Set<String> kunINy = new HashSet<>();

    // Vis SED - Vis stier. vis enumNavn
    // Finn enumer brukt av Sedtypene. skriv diff til filer /v44/F/EESSINationality_diff.txt
//        sedTyper.forEach(sedType -> {
//            Map<String, EuSedFundament>euSedStiVsFundamentEnumNy = new HashMap<>();
//            Map<String, EuSedFundament>euSedStiVsFundamentEnumGml = new HashMap<>();
//            Map<String, EuSedFundament> euSedStiVsFundamentNy = filService.lesEuSedFundamentFil(sedType, cdmVersjonNy);
//            euSedStiVsFundamentEnumNy = euSedStiVsKunEnumFundament(euSedStiVsFundamentNy);
//            Map<String, EuSedFundament> euSedStiVsFundamentGml = filService.lesEuSedFundamentFil(sedType, cdmVersjonGml);
//            euSedStiVsFundamentEnumGml = euSedStiVsKunEnumFundament(euSedStiVsFundamentGml);
//            // euSedStierVsFundament i ny versjon som har enumer som også fins i gammel versjon.
//            Set<String> enumNavnNy =  plukkUtEumNavn(euSedStiVsFundamentEnumNy);
//            Set<String> enumNavnGml =  plukkUtEumNavn(euSedStiVsFundamentEnumGml);
//
//            // if enumNavn også fins i gammel:
//            //      les filer fra ulike versjoner - skriv til fil
//
//            Map<String, EuSedFundament> euSedStiVsFundamentFellesNy =
//                    euSedStiVsFundamentMedFellesEnumer(euSedStiVsFundamentEnumNy, enumNavnGml) ;
//            // Hnet alle enumer som det er aktuelt å sammenlikne med, v1 og v2
//            Map<String, Map<Integer, EnumkodeFundament>>
//                    enumNavnVsEnumkodeNrVsEnumkodeInfoNy = hentEnumNavnVsEnumkodeNrVsEnumkodeInfo(enumNavnNy, cdmVersjonNy);
//            Map<String, Map<Integer, EnumkodeFundament>>
//                    enumNavnVsEnumkodeNrVsEnumkodeInfoGml = hentEnumNavnVsEnumkodeNrVsEnumkodeInfo(enumNavnGml, cdmVersjonGml);
//
//            euSedStiVsFundamentFellesNy.forEach((euSedSti, fundament) -> {
//
//
//
//            });

//            enumNavnNy.forEach((enumNavnId, enumNavn) -> {
//                if (!enumNavnGml.containsKey(enumNavnId)) {
//                    kunINy.add(sedType + " - " + enumNavnId + " - " + enumNavn);
//                }
//            });
//            enumNavnGml.forEach((enumNavnId, enumNavn) -> {
//                if (!enumNavnNy.containsKey(enumNavnId)) {
//                    kunIGml.add(sedType + " - " + enumNavnId + " - " + enumNavn);
//                }
//            });
//
//            Map<String, List<String>> enumNavnVsEnumkodeLinjerNy = new HashMap<>();
//
//            euSedStiVsFundamentFellesNy.forEach((enumNavnId, enumNavn) -> {
//                Map<Integer, EnumkodeFundament> enumkodeNrVsEnumkodeInfo = filService.lesEnumFundamentFil(enumNavn, cdmVersjonNy);
//                enumkodeNrVsEnumkodeInfo.forEach((enumkodeNr, enumkodeInfo) -> {
//                    String enumkodeId = enumkodeInfo.getEnumkodeId();
//                    String enumkodeBeskrivelse = enumkodeInfo.getEnumkodeBeskrivelse();
//                    if (!enumNavnVsEnumkodeLinjerNy.containsKey(enumNavn)) {
//                        enumNavnVsEnumkodeLinjerNy.put(enumNavn, new ArrayList<>());
//                    }
//                    enumNavnVsEnumkodeLinjerNy.get(enumNavn).add(enumkodeId + " " + enumkodeBeskrivelse);
//                });
//            });
//            Map<String, List<String>> enumNavnVsEnumkodeLinjerGml = new HashMap<>();
//            enumNavnFelles.forEach((enumNavnId, enumNavn) -> {
//                Map<Integer, EnumkodeFundament> enumkodeNrVsEnumkodeInfo = filService.lesEnumFundamentFil(enumNavn, cdmVersjonGml);
//                enumkodeNrVsEnumkodeInfo.forEach((enumkodeNr, enumkodeInfo) -> {
//                    String enumkodeId = enumkodeInfo.getEnumkodeId();
//                    String enumkodeBeskrivelse = enumkodeInfo.getEnumkodeBeskrivelse();
//                    if (!enumNavnVsEnumkodeLinjerGml.containsKey(enumNavn)) {
//                        enumNavnVsEnumkodeLinjerGml.put(enumNavn, new ArrayList<>());
//                    }
//                    enumNavnVsEnumkodeLinjerGml.get(enumNavn).add(enumkodeId + " " + enumkodeBeskrivelse);
//                });
//            });
//            enumNavnVsEnumkodeLinjerNy.forEach((enumNavn, enumkodeLinjerNy) -> {
//                List<String> linjerGml = enumNavnVsEnumkodeLinjerGml.get(enumNavn);
//                enumkodeLinjerNy.forEach(linje -> {
//                    if (!linjerGml.contains(linje)) {
//                        log.error(sedType + " Ny  har denne i {}: {}", enumNavn, linje);
//                    }
//                });
//                linjerGml.forEach(linje -> {
//                    if (!enumkodeLinjerNy.contains(linje)) {
//                        log.error(sedType + " Gml har denne i {}: {}", enumNavn, linje);
//                    }
//                });
//
//            });
    ;


//        kunINy.forEach((enumIdNavn) -> {
//            log.warn("Fins kun i versjon {}: enum: {} ", cdmVersjonNy, enumIdNavn);
//        });
//        kunIGml.forEach((enumIdNavn) -> {
//            log.warn("Fins kun i versjon {}: enum: {} ", cdmVersjonGml, enumIdNavn);
//        });
    // de som er i begge

//    }

//}

    private Map<String, Map<Integer, EnumkodeFundament>> hentEnumNavnVsEnumkodeNrVsEnumkodeInfo(Set<String> enumNavnSet, String cdmVersjon) {
        Map<String, Map<Integer, EnumkodeFundament>> enumNavnVsEnumkodeNrVsEnumkodeInfo = new HashMap<>();
        enumNavnSet.forEach(enumNavn ->
                enumNavnVsEnumkodeNrVsEnumkodeInfo.put(enumNavn, filService.lesEnumFundamentFil(enumNavn, cdmVersjon)));
        return enumNavnVsEnumkodeNrVsEnumkodeInfo;
    }

    // flytt ned
    private Map<String, EuSedFundament> euSedStiVsKunEnumFundament(Map<String, EuSedFundament> euSedStiVsEuSedFundament) {
        return euSedStiVsEuSedFundament.entrySet().stream()
                .filter(entry -> "enum".equals(entry.getValue().getVerdiType()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Set<String> plukkUtEumNavn(Map<String, EuSedFundament> euSedStiVsFundament) {
        return euSedStiVsFundament.values().stream().map(EuSedFundament::getEnumNavnId).collect(Collectors.toSet());
    }


    private Map<String, EuSedFundament> euSedStiVsFundamentMedFellesEnumer(Map<String, EuSedFundament> euSedStiVsFundamentNy,
                                                                           Set<String> enumNavn) {
        return euSedStiVsFundamentNy.entrySet().stream()
                .filter(entry -> enumNavn
                        .contains(entry.getValue().getEnumNavnId()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    private List<String> euSedFraFundament(String sedType, String cdmVersjon) {
        Map<String, EuSedFundament> euSedFundament = filService.lesEuSedFundamentFil(sedType, cdmVersjon);
        return euSedFundament.keySet().stream().sorted().toList();
    }

    private List<String> lagEuSedFraAclMal(String sedType, String cdmVersjon) {
        Map<String, Object> aclMal = filService.lesAclMal(sedType, cdmVersjon);
        return aclMal.keySet().stream()
                .filter(k -> !k.contains("sedGVer") && !k.contains("sedVer"))
                .sorted().toList();
    }

    // -------------


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
