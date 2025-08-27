package eux.acl.generator.service;

import eux.acl.generator.modell.EnumkodeFundament;
import eux.acl.generator.modell.EuSedFundament;
import eux.acl.generator.modell.pdf.PdfSedElement;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class PdfService {
    private final FilService filService;

    public PdfService() {
        this.filService = new FilService();
    }

    public void lagPdfMal(String sedType, String cdmVersjon) {
        TreeMap<String, EuSedFundament> euSedFundament = filService.lesEuSedFundamentFil(sedType, cdmVersjon);
        TreeMap<String, PdfSedElement> euSedStiVsSedElement = new TreeMap<>();
        euSedFundament.forEach((euSedSti, fundament) -> {
            euSedStiVsSedElement.put(
                    euSedSti,
                    PdfSedElement.builder()
                            .index(fundament.getIndeks())
                            .prompt(fundament.getLedetekst())
                            .valuetype(fundament.getCdmType())
                            .codework(lesEnumVerdier(fundament.getEnumNavn(), cdmVersjon, euSedSti))
                            .enumName(fundament.getEnumNavn())
                            .build());
        });
        filService.skrivPdfSedMalTilFil(euSedStiVsSedElement, sedType, cdmVersjon);
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
