package eux.acl.temp;

import com.github.wnameless.json.unflattener.JsonUnflattener;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static no.nav.eux.acl.temp.AclV44BasismalerTest.*;
import static no.nav.eux.acl.v43.ACLv43Test.*;


public class AclV44LageMalerTest {

  public static final String MAPPE_ACLv44_MALER = "src/main/resources/sedtemplates/v44";
  private static final String UTF_8 = StandardCharsets.UTF_8.name();

  @Test
  @Ignore
  public void settSammelMalerForEnSed() {
    List.of("F001").stream()
            .forEach(s -> lagAclV44MalJson(s, "F", "family"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_A() {
    SEDTYPER_A.stream().forEach(s -> lagAclV44MalJson(s, "A", "legislationapplicable"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_DA() {
    SEDTYPER_DA.stream().forEach(s -> lagAclV44MalJson(s, "DA", "awod"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_F() {
    SEDTYPER_F.stream().forEach(s -> lagAclV44MalJson(s, "F", "family"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_H() {
    SEDTYPER_H.stream().forEach(s -> lagAclV44MalJson(s, "H", "horizontal"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_M() {
    SEDTYPER_M.stream().forEach(s -> lagAclV44MalJson(s, "M", "miscellaneous"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_P() {
    SEDTYPER_P.stream().forEach(s -> lagAclV44MalJson(s, "P", "pension"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_R() {
    SEDTYPER_R.stream().forEach(s -> lagAclV44MalJson(s, "R", "recovery"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_S() {
    SEDTYPER_S.stream().forEach(s -> lagAclV44MalJson(s, "S", "sickness"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_U() {
    SEDTYPER_U.stream().forEach(s -> lagAclV44MalJson(s, "U", "unemployment"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_X() {
    SEDTYPER_X.stream().forEach(s -> lagAclV44MalJson(s, "X", "administrative"));
  }

  private void lagAclV44MalJson(String sedType, String cdmSektor, String templateSector) {
    try {
      String templateBaseFileName = ACLv44_MELLOMBELS_MAPPE + "/" + cdmSektor + "/" + sedType + MAL_FLAT_FILNAVN_ENDE;
      List<String> baseTemplateList = FileUtils.readLines(new File(templateBaseFileName), UTF_8);
      String templateAdjustFileName =
              ACLv44_MELLOMBELS_MAPPE + "/" + cdmSektor + "/" + sedType + MAL_NYTT_FILNAVN_ENDE;
      List<String> appendTemplateList =
        FileUtils.readLines(new File(templateAdjustFileName), UTF_8);
      List<String> completeTemplateList = new ArrayList<>();
      completeTemplateList.addAll(baseTemplateList);
      completeTemplateList.addAll(appendTemplateList);
      List<String> completeTemplateListSorted = completeTemplateList.stream().sorted().toList();
      Map<String, String> euPathVsNavPath = new LinkedHashMap<>();
      completeTemplateListSorted.stream().forEach(l -> {
        String[] euPathNavPath = l.split(": ");
        if (euPathNavPath.length == 1) {
          euPathVsNavPath.put(euPathNavPath[0], "");
        } else {
          euPathVsNavPath.put(euPathNavPath[0], euPathNavPath[1]);
        }
      });
      String[] toJson = {""};
      euPathVsNavPath.forEach((p, t) -> toJson[0] += ",\"" + p + "\":\"" + t + "\"");
      String euPathNavPathFlatJson = "{" + toJson[0].replaceFirst(",", "") + "}";
      String euPathNavPathJson = JsonUnflattener.unflatten(euPathNavPathFlatJson);
      ObjectMapper om = new ObjectMapper();
      JsonNode euPathNavPathNode = om.readTree(euPathNavPathJson);
      JsonNode json = om.readValue(euPathNavPathNode, JsonNode.class);
      String euPathNavPathPrettyJson = om.writerWithDefaultPrettyPrinter().writeValueAsString(json);
      FileUtils.writeStringToFile(
        new File(MAPPE_ACLv44_MALER + "/" + templateSector + "/" + sedType + "_v4.4.json"),
        euPathNavPathPrettyJson, "UTF-8");
    } catch (Exception e) {
      System.out.println(
        "Error. Kunne ikke lese templatefil " + sedType + ": " + e.getLocalizedMessage());
    }
  }

}

