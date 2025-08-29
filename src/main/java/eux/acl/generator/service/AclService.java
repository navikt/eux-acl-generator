package eux.acl.generator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.github.wnameless.json.flattener.PrintMode;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import eux.acl.generator.modell.EuSedFundament;
import eux.acl.generator.modell.pdf.PdfSedElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class AclService {

    private final FilService filService;

    public AclService() {
        this.filService = new FilService();
    }

    public List<String> lagEuSedFraAclMal(String sedType, String cdmVersjon) {
        Map<String, Object> aclMal = filService.lesAclMal(sedType, cdmVersjon);
        return aclMal.keySet().stream()
                .filter(k -> !k.contains("sedGVer") && !k.contains("sedVer"))
                .sorted().toList();
    }
    public Map<String, String> hentAclMal(String sedType, String cdmVersjon) {
        Map<String, Object> aclMal = filService.lesAclMal(sedType, cdmVersjon);
        return aclMal.entrySet().stream()
                .filter(k -> !k.getKey().contains("sedGVer") && !k.getKey().contains("sedVer"))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toString()));
    }

    public void lagAclMal(String sektor, String cdmVersjon) {
        List<String> alleAclMalLinjerInnenSektoren = filService.lesAclMalBasisForEnSektorTilFil(sektor, cdmVersjon);
        TreeMap<String, Map<String,String>> sedTypeVsEuSedStiVsAclSedSti = new TreeMap<>();
        alleAclMalLinjerInnenSektoren.forEach( l -> {
            String[] malLinje = l.split(":");
            if (malLinje.length < 3) {
                log.error("Feil i mal-linja: sektor={}, {}", sektor, l);
            }
            String sedType = malLinje[0];
            String euSedSti = sedType + "." + malLinje[1].trim();
            String aclSedSti = malLinje.length < 3 ? "--- ACL-sti mangler ---" : malLinje[2].trim();
            if (!sedTypeVsEuSedStiVsAclSedSti.containsKey(sedType)) {
                sedTypeVsEuSedStiVsAclSedSti.put(sedType, new HashMap<>());
            }
            sedTypeVsEuSedStiVsAclSedSti.get(sedType).put(euSedSti, aclSedSti);
        });
        sedTypeVsEuSedStiVsAclSedSti.forEach((sedType, euSedStiVsAclSedSti) -> {
            String aclMalJson = JsonUnflattener.unflatten(euSedStiVsAclSedSti);
            try {
                String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
                String aclSektor = filService.aclSektor(cdmSektor);
               JsonNode aclMalNode = new JsonMapper().readTree(aclMalJson);
                String aclMalPrettyPrint = new JsonMapper().writerWithDefaultPrettyPrinter().writeValueAsString(aclMalNode);
                filService.skrivAclMalTilFil(aclMalPrettyPrint,  aclSektor + "/" + sedType + "_v" + cdmVersjon,  cdmVersjon);
                log.info("ACL-mal opprettet for {}", sedType);
            } catch (Exception e) {
                log.error("Kunne ikke opprette ACL-mal. sedType={}: {}", sedType, e.getLocalizedMessage());
                e.printStackTrace();
            }
        });


    }

}
