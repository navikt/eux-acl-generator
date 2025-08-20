package eux.acl.generator.service;

import eux.acl.generator.modell.EnumkodeFundament;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
public class ResultaterService {
    private final FilService filService;

    public ResultaterService() {
        this.filService = new FilService();
    }

    public void opprettEnumFilForAcl(String filnavn, String cdmVersjon) {
        TreeMap<Integer, EnumkodeFundament> nyEnumFundament = filService.lesNyEnumFundamentFil(filnavn, cdmVersjon);
        TreeMap<Integer, EnumkodeFundament> endretEnumFundament = filService.lesManueltEndretEnumFundamentFil(filnavn, cdmVersjon);
        List<String> aclEnum = new ArrayList<>();
        nyEnumFundament.keySet().forEach(kodeNr -> {
            if (endretEnumFundament != null
                    && endretEnumFundament.containsKey(kodeNr)
                    && endretEnumFundament.get(kodeNr).getEnumkode() != null) {
                aclEnum.add(endretEnumFundament.get(kodeNr).getEnumkode() + "=" + nyEnumFundament.get(kodeNr).getEnumkodeId());
            } else {
                aclEnum.add(nyEnumFundament.get(kodeNr).getEnumkode() + "=" + nyEnumFundament.get(kodeNr).getEnumkodeId());
            }
        });
        // TODO: Les mappingfil (_enumFilnavnMapping.txt) med nytt filnavn - ikke nødvendig i denne versjonen (ingen navnekollisjoner)
        filService.opprettEnumfil(aclEnum, filnavn, cdmVersjon);
    }


}
