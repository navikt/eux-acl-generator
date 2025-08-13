package eux.acl.generator.modell;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.TreeMap;

@Builder
@Setter
@Getter
public class UttrekkFundament {

    private Map<String, String> cdmStiVsMalTagnavn;
    private Map<String, String> cdmStiVsTagtype;
    private Map<String, String> cdmStiVsIndeks;
    private Map<String, String> cdmStiVsLedetekst;
    private TreeMap<String, String> cdmStiVsEnumNavn;
    private Map<String, String> cdmStiVsEnumkodeTekst;
    private Map<String, String> cdmStiVsEnumkodeId;
    private TreeMap<String, String> cdmStiVsTekstId;
    private Map<String, String> cdmStiVsTekstlengde;

}
