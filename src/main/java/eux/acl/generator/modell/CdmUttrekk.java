package eux.acl.generator.modell;


import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Setter
@Getter
public class CdmUttrekk {

    public Map<String, String> cdmStiVsMalTagnavn = new HashMap<>();
    public Map<String, String> cdmStiVsTagtype = new HashMap<>();
    public Map<String, String> cdmStiVsTekstId = new HashMap<>();
    public Map<String, String> cdmStiVsIndeks = new HashMap<>();
    public Map<String, String> cdmStiVsEnumVerdier = new HashMap<>();
    public Map<String, String> cdmStiVsEnumtype = new HashMap<>();
    public Set<String> cdmStiTilEnum = new HashSet<>();
}
