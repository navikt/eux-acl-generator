package eux.acl.v43;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import no.nav.eux.acl.EessiAcl;
import no.nav.eux.rina.cpi.service.EessiAclClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.internal.StringUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static no.nav.eux.rina.cpi.service.EessiAclClient.*;

@ExtendWith(MockitoExtension.class)
public class H001AdresserTest {

  private static final String MAPPE_ACLv43_TESTDATA_SED = "src/test/resources/v43";
  private static final String UTF_8 = StandardCharsets.UTF_8.name();
  private static final String V42 = "4.2";
  private static final String V43 = "4.3";


    @Test
    public void euToNavTest() {
      String sector = "horizontal";
      String sedType = "H001";
      String euJsonFileName =
              MAPPE_ACLv43_TESTDATA_SED + "/" + sector + "/" + sedType + "-EU.json";
      try {
        String origEuSed = FileUtils.readFileToString(new File(euJsonFileName), UTF_8);
        String aclSed = EessiAcl.mapEuToNav(origEuSed).toString();
        String navSed = EessiAclClient.justerAclSedFraRina(aclSed, V43);
        navSed.matches("nav");
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

  @Test
  public void navToEuTest() {
    String sector = "horizontal";
    String sedType = "H001";
    String fileName =
            MAPPE_ACLv43_TESTDATA_SED + "/" + sector + "/" + sedType + "-NAV.json";
    try {
      String origNavSed = FileUtils.readFileToString(new File(fileName), UTF_8);
      String aclSed = EessiAclClient.fraNavSedTilEuSed(origNavSed, V43);
      aclSed.matches("eu");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }


  }

  private void euToNavKorrigertAdresseTest(String sector, List<String> sedTypes) {
    sedTypes.forEach(sedType -> {
      try {
        String euJsonFileName =
                MAPPE_ACLv43_TESTDATA_SED + "/" + sector + "/" + sedType + "-EU.json";
        String origEuSed = FileUtils.readFileToString(new File(euJsonFileName), UTF_8);
        // eu -> nav -> justertNav
        // 1: eu -> acl
        JsonNode navSedGenerated = EessiAcl.mapEuToNav(origEuSed, V43);
        // 2: iso2 -> iso3
        String navSedIso3 = justerAclSedFraRina(navSedGenerated.toString(), "nav");
        // 3:
        Map<String, Object> navSedFlattened = JsonFlattener.flattenAsMap(navSedIso3
                .replace("\\\"", "${quotationMARK}")
                .replace("\\", "\\\\"));

        // Sorter!!? - ikke nødvendig - blir sortert ved flattening ...
            // h001AdresseFraRinaKonvertert()
        Map<String, String> navSedFlattenedKorrigert = new HashMap<>();
        int[] adresseIndeks = {-1};
        String[] adresseAktuellSekvens = {""};
        int[] requestIndeks = {-1};
        String[] requestAktuellSekvens = {""};
        navSedFlattened.forEach((k, v) -> {

          if (k.matches("nav.bruker.meldinger\\[\\d+].adresser\\[\\d+].*")) {
            String[] indekserISti = StringUtils.substringsBetween(k, "[", "]");
            String denneSekvensen = String.join(",", indekserISti);
            if (!denneSekvensen.equals(adresseAktuellSekvens[0])) {
              adresseIndeks[0]++;
              adresseAktuellSekvens[0] = denneSekvensen;
            }
            navSedFlattenedKorrigert.put(k.replaceAll("nav.bruker.meldinger\\[\\d+].adresser\\[\\d+]"
                    , "nav.bruker.adresse[" + adresseIndeks[0] + "]"), v.toString());

          } else if (k.matches("nav.bruker.meldinger\\[\\d+].anmodet.adresse.type")) {
            String[] indekser = StringUtils.substringsBetween(k, "[", "]");
            String denneSekvensen = String.join(",", indekser);
            if (!denneSekvensen.equals(requestAktuellSekvens[0])) {
              requestIndeks[0]++;
              requestAktuellSekvens[0] = denneSekvensen;
            }
            navSedFlattenedKorrigert.put("horisontal.bruker.anmodning.adresse[" + requestIndeks[0] + "].type", v.toString());
          } else {
            navSedFlattenedKorrigert.put(k, v.toString());
          }
        });
        String aclSedFlattened = "{"
                + StringUtil.join(navSedFlattenedKorrigert.entrySet().stream()
                .map(d -> "\""
                        + d.getKey() + "\": \"" + d.getValue() + "\"").toList(), ",")
                + "}";
        String navSedAdressekorrigert = JsonUnflattener.unflatten(aclSedFlattened)
                .replace("${quotationMARK}", "\\\"");

        // kontrollerer
        System.out.print(sedType + ": ");
        Map<String, Object> origFlattened = JsonFlattener.flattenAsMap(origEuSed);
        List<String> origFlatList = origFlattened.keySet().stream()
                .map(k -> k + ": " + origFlattened.get(k).toString())
                .toList();
        List<String> origFlatListSorted = origFlatList.stream().sorted().toList();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }


}

