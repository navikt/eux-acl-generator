package eux.acl.generator.modell;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Setter
@Getter
public class UttrekkFundament {

    private Map<String, String> cdmStiVsMalTagnavn;
    private Map<String, String> cdmStiVsTagtype;
    private Map<String, String> cdmStiVsIndeks;
    private Map<String, String> cdmStiVsLedetekst;
    private Map<String, String> cdmStiVsEnumtype;
    private Map<String, String> cdmStiVsEnumkodeTekst;
    private Map<String, String> cdmStiVsEnumkodeId;
    private Map<String, String> cdmStiVsTekstId;

}
