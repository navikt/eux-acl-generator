package eux.acl.generator.facade;

import eux.acl.generator.modell.CdmUttrekk;
import eux.acl.generator.service.FilService;
import eux.acl.generator.service.UttrekkFraCdmMetaService;
import eux.acl.generator.service.UttrekkFraLogiskModell;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static eux.acl.generator.service.FilService.V43;

@Slf4j
public class CdmMetaFacade {
    private final FilService filService;
    private final UttrekkFraCdmMetaService cdmMetaService;
    private final UttrekkFraLogiskModell logiskModellService;

    public CdmMetaFacade() {
        this.filService = new FilService();
        this.cdmMetaService = new UttrekkFraCdmMetaService();
        this.logiskModellService = new UttrekkFraLogiskModell();
    }

    public void lesCdmMetaOgLagCdmSedOgSkrivDenTilFil(String sedType, String cdmVersjon) {
        String lmXml = filService.lesCdmLogiskModellXmlFil(cdmVersjon);
        Map<String, String> tekstIdVsEnumtype = logiskModellService.uttrekkFraLogiskModell(lmXml);
        Map<String, String> tekstIdVsTekst = filService.lesOversettelsesfil(cdmVersjon);

        Map<String, Object> cdmMetaSed = filService.lesCdmMetaFilForEnSed(sedType, cdmVersjon);
        CdmUttrekk cdmUttrekk = cdmMetaService.uttrekkFraCdmMetaFil(cdmMetaSed, sedType);
        cdmMetaService.settInnCdmStiVsEnumtype(cdmUttrekk, tekstIdVsEnumtype);

        // lagEnumFiler - les alle SEDer - sjekk om samme verdi skal srives over -> feil hvis ikke.
        // cdmStiTilEnum vs enum[#].verdi
        // hva med ledetekster?
        // enumNavn vs enumEuVerdiVsTekst

        List<String> cdmSedFlattened =  cdmMetaService.lagCdmSed(cdmUttrekk);
        filService.skrivCdmSedTilFil(cdmSedFlattened, sedType, cdmVersjon);
    }

    public void lesAclMalOgSkrivDenTilFil(String sedType, String cdmVersjon) {
        Map<String, Object> aclMal = filService.lesAclMal(sedType, cdmVersjon);
        List<String> cdmStierVsNavStierListe = aclMal.keySet().stream()
                .filter(k-> !k.contains("sedGVer") && !k.contains("sedVer"))
                .map(k -> k + ": " + aclMal.get(k))
                .toList();
        filService.skrivAclSedTilFil(cdmStierVsNavStierListe, sedType, cdmVersjon);
    }

    public void sammenlikneSedFraAclOgSedFraCdm(String sedType, String cdmVersjon) {
        List<String> aclMalListe = filService.lesAclMalFraTarget(sedType, cdmVersjon);
        List<String> cdmSedListe = filService.lesCdmSedFraTarget(sedType, cdmVersjon);
        List<String> aclSedListe = aclMalListe.stream().map(l -> l.split(":")[0]).toList();
        for (int nr = 0; nr < aclMalListe.size(); nr++) {
            if (!aclSedListe.get(nr).equals(cdmSedListe.get(nr))) {
                log.error(sedType + ": mismatch. Acl={}, Cdm={}", aclSedListe.get(nr), cdmSedListe.get(nr));
                return;
            }
        }
        log.info(sedType + ": OK");
    }

    public void sammenlikneCdmSedVersjonerOgSkriveDiffTilFiler(String sedType, String cdmNyVersjon, String cdmGammelVersjon) {
        List<String> cdmSedNyListe = filService.lesCdmSedFraTarget(sedType, cdmNyVersjon);
        List<String> cdmSedGammelListe = filService.lesCdmSedFraTarget(sedType, cdmGammelVersjon);
        if (cdmSedNyListe == null || cdmSedGammelListe == null) {
            return;
        }
        List<String> uendredeStierListe = new ArrayList<>();
        List<String> nyeStierListe = new ArrayList<>();
        List<String> utgaatteStierListe = new ArrayList<>();
        cdmSedNyListe.forEach(l -> {
            if (cdmSedGammelListe.contains(l)) {
                uendredeStierListe.add(l);
            } else {
                nyeStierListe.add(l);
            }
        });
        cdmSedGammelListe.forEach(l -> {
            if (!uendredeStierListe.contains(l)) {
                utgaatteStierListe.add(l);
            }
        });
        filService.skrivCdmEndringerTilFil(uendredeStierListe, sedType, cdmNyVersjon, FilService.FIL_UENDREDE_STIER);
        filService.skrivCdmEndringerTilFil( nyeStierListe, sedType, cdmNyVersjon, FilService.FIL_NYE_STIER);
        filService.skrivCdmEndringerTilFil(utgaatteStierListe, sedType, cdmNyVersjon, FilService.FIL_UTGAATTE_STIER);
    }

    public void sammenliknerGammelAclSedMedNyCdmVersjonOgSkriverDiffTilFil(String sedType, String nyCdmVersjon, String aclVersjon) {
        List<String> nyCdmSedListe = filService.lesCdmSedFraTarget(sedType, nyCdmVersjon);
        List<String> aclMalListe = filService.lesAclMalFraTarget(sedType, aclVersjon);
        if (nyCdmSedListe == null || aclMalListe == null) {
            return;
        }
        Map<String, String> aclSedStiVsNavSedSti = new HashMap<>();
        aclMalListe.forEach(l -> {
            String[] linje = l.split(": ") ;
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
        filService.skrivCdmEndringerTilFil(aclUendredeStier, sedType, nyCdmVersjon, FilService.FIL_UENDREDE_ACL_STIER);
        filService.skrivCdmEndringerTilFil(aclUtgaatteStier, sedType, nyCdmVersjon, FilService.FIL_UTGAATTE_ACL_STIER);
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
        filService.skrivAclMalerTilFil(aclMalerMed2Kolon, "F", "alle.txt", V43);
    }

    public void lagEiFilMedUnikeNyeCdmStier(List<String> sedTyper, String cdmVersjon, String filnavn) {
        List<String> altNyttPrSedType = new ArrayList<>();
                sedTyper.forEach(sedType ->
                        altNyttPrSedType.addAll(filService.lesNyeStierFraTarget(sedType, cdmVersjon)));
        List<String>  nye = altNyttPrSedType.stream()
                .map(s -> StringUtils.substringAfter(s, ".")).toList();
        List<String> uniktNytt = new HashSet<>(nye).stream().sorted().toList();

        filService.skrivNyeUnikeStierTilFil(uniktNytt, filnavn, cdmVersjon);
    }

    public void lagEiFilMedUnikeUtgaatteAclStier(List<String> sedTyper, String cdmVersjon, String filnavn) {
        List<String> utgaattPrSedType = new ArrayList<>();
        sedTyper.forEach(sedType -> {
            List<String>  utgaatteStierAcl = filService.lesUtgaatteStierFraTarget(sedType, cdmVersjon);
            utgaattPrSedType.addAll(utgaatteStierAcl);
        });
        List<String>  nye = utgaattPrSedType.stream()
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
