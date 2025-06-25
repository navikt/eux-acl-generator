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


import com.github.wnameless.json.flattener.JsonFlattener;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

public class AclV43LandkoderTest {

  private static final String MAPPE_CDMv43 = "src/test/resources/cdm-v43-meta";
  private static final String MAPPE_ACLv43 = "src/main/resources/sedtemplates/v43";


  private static final String UTF_8 = StandardCharsets.UTF_8.name();
  private CdmReader cdmReader = new CdmReader();

  /**
   * CDMv43 har ikke navn på landkodesettene. Det betyr at vi ikke uten videre vet hvilket
   * landkodesett som er gyldig for en gitt sti.
   * Koden finner hvilket landkodesett som er brukt og knytter det til stien.
   */

  @Test
  @Ignore
  public void landkodesettV43_A() {
    SEDTYPER_A.stream()
      .forEach(s -> finnStiVsLandkodesett(s, "A", "legislationapplicable"));
  }

  @Test
  @Ignore
  public void landkodesettV43_DA() {
    SEDTYPER_DA.stream().forEach(s -> finnStiVsLandkodesett(s, "DA", "awod"));
  }

  @Test
  @Ignore
  public void landkodesettV43_F() {
    SEDTYPER_F.stream().forEach(s -> finnStiVsLandkodesett(s, "F", "family"));
  }

  @Test
  @Ignore
  public void landkodesettV43_H() {
    SEDTYPER_H.stream().forEach(s -> finnStiVsLandkodesett(s, "H", "horizontal"));
  }

  @Test
  @Ignore
  public void landkodesettV43_M() {
    SEDTYPER_M.stream().forEach(s -> finnStiVsLandkodesett(s, "M", "miscellaneous"));
  }

  @Test
  @Ignore
  public void landkodesettV43_P() {
    SEDTYPER_P.stream().forEach(s -> finnStiVsLandkodesett(s, "P", "pension"));
  }

  @Test
  @Ignore
  public void landkodesettV43_R() {
    SEDTYPER_R.stream().forEach(s -> finnStiVsLandkodesett(s, "R", "recovery"));
  }

  @Test
  @Ignore
  public void landkodesettV43_S() {
    SEDTYPER_S.stream().forEach(s -> finnStiVsLandkodesett(s, "S", "sickness"));
  }

  @Test
  @Ignore
  public void landkodesettV43_U() {
    SEDTYPER_U.stream().forEach(s -> finnStiVsLandkodesett(s, "U", "unemployment"));
  }

  @Test
  @Ignore
  public void landkodesettV43_X() {
    SEDTYPER_X.stream().forEach(s -> finnStiVsLandkodesett(s, "X", "administrative"));
  }

  private void finnStiVsLandkodesett(String sedType, String cdmSektor, String aclSector) {
    String cdmV43TemplateFilnavn = MAPPE_CDMv43 + "/" + cdmSektor + "/" + sedType + ".json";
    String aclV42TemplateFilnavn = MAPPE_ACLv43 + "/" + aclSector + "/" + sedType + "_v4.3.json";
    try {
      String cdm43FileJson = FileUtils.readFileToString(new File(cdmV43TemplateFilnavn), UTF_8);
      Map<String, Object> cdmSedFlattenedMap = JsonFlattener.flattenAsMap(cdm43FileJson);
      String acl43FileJson = FileUtils.readFileToString(new File(aclV42TemplateFilnavn), UTF_8);
      Map<String, Object> aclSedFlattenedMap = JsonFlattener.flattenAsMap(acl43FileJson);
      Map<String, String> euStiVsLandkodesett = cdmReader
        .finnEuStiVsLandkodesett(cdmSedFlattenedMap);
      List<String> aclStiVsLandkodesett = new ArrayList<>();
      euStiVsLandkodesett
        .forEach((k, v) -> aclStiVsLandkodesett.add(aclSedFlattenedMap.get(k) + ": " + v));
      String stiVsLandkodesettSortert = StringUtils.join(
        aclStiVsLandkodesett.stream().sorted().toList(), "\n");
      FileUtils.writeStringToFile(
        new File("target/stiVsLandkodesett/" + cdmSektor + "/" + sedType + ".txt"),
        stiVsLandkodesettSortert, "UTF-8");
    } catch (Exception e) {
      System.out.println("Error. Kunne ikke lese templatefil. " + sedType
                           + " - " + e.getLocalizedMessage());
    }
  }
}

