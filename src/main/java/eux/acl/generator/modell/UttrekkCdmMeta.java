package eux.acl.generator.modell;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Setter
@Getter
@NoArgsConstructor
public class UttrekkCdmMeta {

    private Map<String, String> cdmStiVsMalTagnavn = new HashMap<>();
    private Map<String, String> cdmStiVsTagtype = new HashMap<>();
    private TreeMap<String, String> cdmStiVsIndeks = new TreeMap<>();
    private TreeMap<String, String> cdmStiVsTekstId = new TreeMap<>();
    private TreeMap<String, String> cdmStiVsEnumkodeTekstId = new TreeMap<>();
    private TreeMap<String, String> cdmStiVsEnumkodeId = new TreeMap<>();
    private TreeMap<String, String> cdmStiVsParameterNavn = new TreeMap<>();
    private TreeMap<String, String> cdmStiVsParameterVerdi = new TreeMap<>();
}
