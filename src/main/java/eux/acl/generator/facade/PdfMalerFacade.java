package eux.acl.generator.facade;

import eux.acl.generator.modell.*;
import eux.acl.generator.modell.pdf.PdfSedElement;
import eux.acl.generator.service.FilService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class PdfMalerFacade {
    private final FilService filService;
    private final static String OVERSETTELSE_V44 = "4.4";
    private final static String OVERSETTELSE_V43 = "4.3";

    public PdfMalerFacade() {
        this.filService = new FilService();
    }

    public void lagPdfMaler(List<String> sedTyper, String cdmVersjon) {
        sedTyper.forEach(sedType -> {
                    TreeMap<String, EuSedFundament> euSedFundament = filService.lesEuSedFundamentFil(sedType, cdmVersjon);
                    TreeMap<String, PdfSedElement> euSedStiVsSedElement = new TreeMap<>();
                    euSedFundament.forEach((euSedSti, element) -> {
                        euSedStiVsSedElement.put(
                                euSedSti,
                                PdfSedElement.builder()
                                        .index(element.getIndeks())
                                        .prompt(element.getLedetekst())
                                        .valuetype(element.getCdmType())
                                        .codework(lesEnumVerdier(element.getEnumNavn(), cdmVersjon, euSedSti))
                                        .enumName(element.getEnumNavn())
                                        .build());
                    });
                    filService.skrivPdfSedMalTilFil(euSedStiVsSedElement, sedType, cdmVersjon);
                }
        );
    }

    private NavigableMap<String, String> lesEnumVerdier(String enumNavn, String cdmVersjon, String euSedSti) {
        if (enumNavn == null) {
            return null;
        }
        if (List.of("EESSIWorldCurrencyType", "EESSIEUEFTACurrencyType",
                    "EESSIEUEFTACountryType", "EESSINationalityMultipleType", "EESSINationalityType",
                    "EESSIWorldCountryHistoricType", "EESSIWorldCountryType")
                .contains(enumNavn)) {
            return null;
        }
        TreeMap<Integer, EnumkodeFundament> enumFundament = filService.lesEnumFundamentFil(enumNavn, cdmVersjon);
        NavigableMap<String, String> enumkodeVsEnumBeskrivelse = new TreeMap<>();
        enumFundament.forEach((enumkodeNr, enumInnhold) ->
                enumkodeVsEnumBeskrivelse.put(enumInnhold.getEnumkodeId(), enumInnhold.getEnumkodeBeskrivelse()));
        return enumkodeVsEnumBeskrivelse;
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
