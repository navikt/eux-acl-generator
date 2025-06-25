package eux.acl.v43;

import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_A;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_DA;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_F;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_H;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_M;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_P;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_R;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_S;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_U;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_X;
import static no.nav.eux.acl.v43.AclV43BasismalerTest.JUSTERINGER_FILNAVN_ENDE;
import static no.nav.eux.acl.v43.AclV43BasismalerTest.MAPPE_ACLv43_MELLOMBELS;
import static no.nav.eux.acl.v43.AclV43BasismalerTest.UENDRET_FILNAVN_ENDE;

import com.github.wnameless.json.unflattener.JsonUnflattener;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;


public class AclV43LageMalerTest {

  public static final String MAPPE_ACLv43_MALER = "src/main/resources/sedtemplates/v43";
  private static final String UTF_8 = StandardCharsets.UTF_8.name();

  @Test
  @Ignore
  public void settSammelMalerForV43_A() {
    SEDTYPER_A.stream()
      .forEach(s -> lagAclV43MalJson(s, "A", "legislationapplicable"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_DA() {
    SEDTYPER_DA.stream().forEach(s -> lagAclV43MalJson(s, "DA", "awod"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_F() {
    SEDTYPER_F.stream().forEach(s -> lagAclV43MalJson(s, "F", "family"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_H() {
    SEDTYPER_H.stream().forEach(s -> lagAclV43MalJson(s, "H", "horizontal"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_M() {
    SEDTYPER_M.stream().forEach(s -> lagAclV43MalJson(s, "M", "miscellaneous"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_P() {
    SEDTYPER_P.stream().forEach(s -> lagAclV43MalJson(s, "P", "pension"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_R() {
    SEDTYPER_R.stream().forEach(s -> lagAclV43MalJson(s, "R", "recovery"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_S() {
    SEDTYPER_S.stream().forEach(s -> lagAclV43MalJson(s, "S", "sickness"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_U() {
    SEDTYPER_U.stream().forEach(s -> lagAclV43MalJson(s, "U", "unemployment"));
  }

  @Test
  @Ignore
  public void settSammelMalerForV43_X() {
    SEDTYPER_X.stream().forEach(s -> lagAclV43MalJson(s, "X", "administrative"));
  }

  private void lagAclV43MalJson(String sedType, String cdmSektor, String templateSector) {
    try {
      String templateBaseFileName =
        MAPPE_ACLv43_MELLOMBELS + "/" + cdmSektor + "/" + sedType + UENDRET_FILNAVN_ENDE;
      List<String> baseTemplateList = FileUtils.readLines(new File(templateBaseFileName), UTF_8);
      String templateAdjustFileName =
        MAPPE_ACLv43_MELLOMBELS + "/" + cdmSektor + "/" + sedType + JUSTERINGER_FILNAVN_ENDE;
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
        new File(MAPPE_ACLv43_MALER + "/" + templateSector + "/" + sedType + "_v4.3.json"),
        euPathNavPathPrettyJson, "UTF-8");
    } catch (Exception e) {
      System.out.println(
        "Error. Kunne ikke lese templatefil " + sedType + " " + e.getLocalizedMessage());
    }
  }

}

