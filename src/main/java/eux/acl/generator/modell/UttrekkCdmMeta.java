package eux.acl.generator.modell;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class UttrekkCdmMeta {

    private Map<String, String> cdmStiVsMalTagnavn = new HashMap<>();
    private Map<String, String> cdmStiVsTagtype = new HashMap<>();
    private Map<String, String> cdmStiVsIndeks = new HashMap<>();
    private Map<String, String> cdmStiVsTekstId = new HashMap<>();
    private Map<String, String> cdmStiVsEnumkodeId = new HashMap<>();
    private Map<String, String> cdmStiVsEnumkodeTekstId = new HashMap<>();
}
