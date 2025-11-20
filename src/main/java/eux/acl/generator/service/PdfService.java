package eux.acl.generator.service;

import eux.acl.generator.modell.EnumkodeFundament;
import eux.acl.generator.modell.EuSedFundament;
import eux.acl.generator.modell.pdf.PdfSedElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Slf4j
public class PdfService {
    private final FilService filService;

    public PdfService() {
        this.filService = new FilService();
    }

    public void lagPdfMal(String sedType, String cdmVersjon) {
        TreeMap<String, EuSedFundament> euSedFundament = filService.lesEuSedFundamentFil(sedType, cdmVersjon);
        String tittel = euSedFundament.get(sedType + ".sedTitle").getLedetekst();
        euSedFundament.get(sedType + ".sedTitle").setLedetekst(sedType + " - " + tittel);
        TreeMap<String, PdfSedElement> euSedStiVsSedElement = new TreeMap<>();
        Map<String, Integer> indekserVsAntall = new HashMap<>();
        Set<String> indekser = new HashSet<>();
        euSedFundament.forEach((euSedSti, fundament) -> {
            if (!"_uxHiddenGroupContainer".equals(fundament.getCdmType())) {
                if (indekser.contains(fundament.getIndeks())) {
                    indekserVsAntall.putIfAbsent(fundament.getIndeks(), 1);
                    indekserVsAntall.put(fundament.getIndeks(), indekserVsAntall.get(fundament.getIndeks()) + 1);
                } else {
                    indekser.add(fundament.getIndeks());
                    euSedStiVsSedElement.put(
                            StringUtils.removeEnd(euSedSti, ".value[0]"),
                            PdfSedElement.builder()
                                    .index(fundament.getIndeks())
                                    .prompt(fundament.getLedetekst())
                                    .valuetype(fundament.getCdmType())
                                    .codework(lesEnumVerdier(fundament.getEnumNavn(), cdmVersjon, euSedSti))
                                    .enumName(fundament.getEnumNavn())
                                    .build());
                }
            }
        });
        indekserVsAntall.forEach((indeks, antall) -> {
            log.warn("Indeks: '{}': Antall: {} - {}. De overskytende er ignorert", indeks, antall, sedType);
        });
        LinkedHashMap<String, PdfSedElement> euSedStiVsSedElementSortert = sorterOverIndekser(euSedStiVsSedElement);
        filService.skrivPdfSedMalTilFil(euSedStiVsSedElementSortert, sedType, cdmVersjon);
    }

    private LinkedHashMap<String, PdfSedElement> sorterOverIndekser(TreeMap<String, PdfSedElement> euSedStiVsSedElement) {
        List<Map.Entry<String, PdfSedElement>> euSedStiVsSedElementListe = new ArrayList<>(euSedStiVsSedElement.entrySet());
        String[] indexNrs = {".1.", ".2.", ".3.", ".4.", ".5.", ".6.", ".7.", ".8.", ".9.",};
        String[] indexCompareNrs = {".01.", ".02.", ".03.", ".04.", ".05.", ".06.", ".07.", ".08.", ".09."};
        euSedStiVsSedElementListe.sort(Comparator.comparing(entry ->
                StringUtils.replaceEachRepeatedly("." + entry.getValue().getIndex() + ".", indexNrs, indexCompareNrs)
        ));
        LinkedHashMap<String, PdfSedElement> euSedStiVsSedElementSortert = new LinkedHashMap<>();
        euSedStiVsSedElementListe.forEach(element ->
                euSedStiVsSedElementSortert.put(element.getKey(), element.getValue()));
        return euSedStiVsSedElementSortert;
    }

    private NavigableMap<String, String> lesEnumVerdier(String enumNavn, String cdmVersjon, String euSedSti) {
        if (enumNavn == null
                || List.of("EESSIWorldCurrencyType", "EESSIEUEFTACurrencyType",
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

}
