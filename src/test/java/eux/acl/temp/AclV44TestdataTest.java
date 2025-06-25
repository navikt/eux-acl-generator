package eux.acl.temp;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import no.nav.eux.acl.v43.CdmReader;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static no.nav.eux.acl.v43.ACLv43Test.*;


public class AclV44TestdataTest {

  public static final String MAPPE_CDM_V43_META = "src/test/resources/cdm-v44-meta";
  public static final String MAPPE_TESTDATA = "src/test/resources/v44";

  private CdmReader cdmReader = new CdmReader();
  private static final String UTF_8 = StandardCharsets.UTF_8.name();

  /**
   * lager testdata. Testene utføres ved å konvertere fra navSED til euSED og tilbake, eller motsatt.
   * Når det testes på denne måten er det mulig å generere testdataene automatisk.
   */
  @Test
  @Ignore
  public void lagerSederPopulertMedTestdataV44() {
    lagSedMedTestdata("F001", "F", "family", 0);
  }

  @Test
  @Ignore
  public void lagerSederPopulertMedTestdataV43_A() {
    SEDTYPER_A.stream().forEach(s -> lagSedMedTestdata(s, "A", "legislationapplicable", 1));
  }

  @Test
  @Ignore
  public void lagerSederPopulertMedTestdataV43_DA() {
    SEDTYPER_DA.stream().forEach(s -> lagSedMedTestdata(s, "DA", "awod", 2));
  }

  @Test
  @Ignore
  public void lagerSederPopulertMedTestdataV43_F() {
    SEDTYPER_F.stream().forEach(s -> lagSedMedTestdata(s, "F", "family", 2));
  }

  @Test
  @Ignore
  public void lagerSederPopulertMedTestdataV43_H() {
    SEDTYPER_H.stream().forEach(s -> lagSedMedTestdata(s, "H", "horizontal", 2));
  }

  @Test
  @Ignore
  public void lagerSederPopulertMedTestdataV43_M() {
    SEDTYPER_M.stream().forEach(s -> lagSedMedTestdata(s, "M", "miscellaneous", 7));
  }

  @Test
  @Ignore
  public void lagerSederPopulertMedTestdataV43_P() {
    SEDTYPER_P.stream().forEach(s -> lagSedMedTestdata(s, "P", "pension", 11));
  }

  @Test
  @Ignore
  public void lagerSederPopulertMedTestdataV43_R() {
    SEDTYPER_R.stream().forEach(s -> lagSedMedTestdata(s, "R", "recovery", 16));
  }

  @Test
  @Ignore
  public void lagerSederPopulertMedTestdataV43_S() {
    SEDTYPER_S.stream().forEach(s -> lagSedMedTestdata(s, "S", "sickness", 0));
  }

  @Test
  @Ignore
  public void lagerSederPopulertMedTestdataV43_U() {
    SEDTYPER_U.stream().forEach(s -> lagSedMedTestdata(s, "U", "unemployment", 1));
  }

  @Test
  @Ignore
  public void lagerSederPopulertMedTestdataV43_X() {
    SEDTYPER_X.stream().forEach(s -> lagSedMedTestdata(s, "X", "administrative", 1));
  }

  @Test
  @Ignore
  public void kodeverkTest() {
    lagSedMedTestdata("A005", "A", "administrative", 0);
    lagSedMedTestdata("A002", "A", "administrative", 0);

  }

  private void lagSedMedTestdata(String sedType, String cdmSektor,
                                 String templateSektor, int enumVerdiNr) {
    try {
      String cdmMetaMalFilnavn = MAPPE_CDM_V43_META + "/" + cdmSektor + "/" + sedType + ".json";
      String cdmSedJson = FileUtils.readFileToString(new File(cdmMetaMalFilnavn), UTF_8);
      Map<String, Object> cdmMalFlattened = JsonFlattener.flattenAsMap(cdmSedJson);
      Map<String, String> stiVsType = cdmReader.lesMalFraCdm(cdmMalFlattened, sedType);
      Map<String, String> stiVsKodeverk = cdmReader.lesKodeverkFraCdm(cdmMalFlattened);
      Map<String, String> testMalVsVerdier = new HashMap<>();
      stiVsType.keySet().stream().forEach(k -> {
        String type = stiVsType.get(k);
        if (type.equals("_uxRadioButton")
          || type.equals("_uxMultiSelect")
          || type.equals("_uxHiddenArrayContainer")
          || type.equals("_uxMultiCheckbox")) {
          if (stiVsKodeverk.get(k + ".option[" + enumVerdiNr + "]") == null) {
            testMalVsVerdier.put(k, stiVsKodeverk.get(k + ".option[0]"));
          } else {
            testMalVsVerdier.put(k, stiVsKodeverk.get(k + ".option[" + enumVerdiNr + "]"));
          }
        } else if (type.equals("_uxTextInput")
          || type.equals("_uxTextAreaInput")
          || type.equals("_rinaIRAutocomplete")) {
          testMalVsVerdier.put(k, "æøå");
        } else if (type.equals("_uxDateInput")) {
          testMalVsVerdier.put(k, "2001-01-01");
        } else if (type.equals("")) {
          if (k.equals(sedType + ".sedGVer")) {
            testMalVsVerdier.put(k, "4");
          }
          if (k.equals(sedType + ".sedVer")) {
            testMalVsVerdier.put(k, "4");
          }
        } else {
          testMalVsVerdier.put(k, stiVsType.get(k));
        }
      });
      String[] toJson = {""};
      testMalVsVerdier.forEach((p, t) -> {
        toJson[0] += ",\"" + p + "\":\"" + t + "\"";
      });
      String euPathValueFlatJson = "{" + toJson[0].replaceFirst(",", "") + "}";
      String euPathValueJson = JsonUnflattener.unflatten(euPathValueFlatJson);
      ObjectMapper om = new ObjectMapper();
      JsonNode euPathValueNode = om.readTree(euPathValueJson);
      JsonNode json = om.readValue(euPathValueNode, JsonNode.class);
      String euPathValuePrettyJson = om.writerWithDefaultPrettyPrinter().writeValueAsString(json);
      FileUtils.writeStringToFile(
        new File(MAPPE_TESTDATA + "/" + templateSektor + "/" + sedType + "-EU.json"),
        euPathValuePrettyJson, "UTF-8");


    } catch (Exception e) {
      System.out.println("Error: " + sedType + " " + e.getMessage());
    }
  }
}

