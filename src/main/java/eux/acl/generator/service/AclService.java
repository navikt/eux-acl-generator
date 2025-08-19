package eux.acl.generator.service;

import eux.acl.generator.modell.UttrekkCdmMeta;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class AclService {



    public Map<String, String> lagCdmStiVsLedetekst(UttrekkCdmMeta uttrekkCdmMeta, Map<String, String> tekstIdVsTekst) {
        if (uttrekkCdmMeta == null || uttrekkCdmMeta.getCdmStiVsTekstId() == null || tekstIdVsTekst == null) {
            return null;
        }
        Map<String, String> cdmStiVsLedetekst = new HashMap<>();
        uttrekkCdmMeta.getCdmStiVsTekstId().forEach((cdmSti, tekstId) -> {
            if (tekstId == null) {
                log.error("tekstId mangler, cdmSti={}", cdmSti);
            } else {
                cdmStiVsLedetekst.put(cdmSti, tekstIdVsTekst.get(tekstId));
            }
        });
        return cdmStiVsLedetekst;
    }
}
