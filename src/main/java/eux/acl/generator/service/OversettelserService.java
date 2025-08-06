package eux.acl.generator.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OversettelserService {

    public Map<String, String> lagOversettelser(String ordliste, String cdmVersjon) {
        Map<String, String> tekstIdVsTekst = new HashMap<>();
        List<String> oversatteLinjer = Arrays.asList(ordliste.split("\n"));
        oversatteLinjer.forEach(linje -> {
                    if (linje.contains("\"" + cdmVersjon)) {
                        String tekstId = linje.split("_")[1].toLowerCase();
                        String tekst = linje.split(":")[1].split("\"")[1].trim();
                        tekstIdVsTekst.put(tekstId, tekst);
                    }
                }
        );
        return tekstIdVsTekst;
    }
}
