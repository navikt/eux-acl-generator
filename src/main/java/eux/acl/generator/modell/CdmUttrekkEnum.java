package eux.acl.generator.modell;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.TreeMap;

@Builder
@Setter
@Getter
public class CdmUttrekkEnum {

    private Map<String, String> cdmStiVsEnumkodeTekst;
    private Map<String, String> cdmStiVsEnumkodeId;
    private TreeMap<String, String> cdmStiVsTekstId;


}
