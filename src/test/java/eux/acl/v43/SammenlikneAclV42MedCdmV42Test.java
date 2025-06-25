package eux.acl.v43;

import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_A;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_DA;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_F;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_H;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_P;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_P3000;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_R;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_S;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_U;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_X;

import com.github.wnameless.json.flattener.JsonFlattener;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

public class SammenlikneAclV42MedCdmV42Test {

  private static final String MAPPE_CDMv42 = "src/test/resources/cdm-v42-meta";
  private static final String MAPPE_ACLv42 = "src/main/resources/sedtemplates/v42";
  private static final String MAPPE_ACLv42_VS_CDMv42 = "target/v42-acl-vs-cdm";

  private CdmReader cdmReader = new CdmReader();
  private static final String UTF_8 = StandardCharsets.UTF_8.name();

  /**
   * Sammenlikner strukturen i v4.2 fra CDM med strukturen i v4.2 fra eksisterende ACL (kodeverksverdier sjekkes ikke)
   * Setter sammen en mal basert på den v4.2-versjonen som følger med CDM v4.3 og sammenlikner med ACL v4.2.
   * Diff skrives til fil.
   */
  @Test
  @Ignore
  public void sammenliknerV42_A() {
    SEDTYPER_A.stream().forEach(s -> sammenliknerV42CdmOgAclMal(s, "A", "legislationapplicable"));
  }

  @Test
  @Ignore
  public void sammenliknerV42_DA() {
    SEDTYPER_DA.stream().forEach(s -> sammenliknerV42CdmOgAclMal(s, "DA", "awod"));
  }

  @Test
  @Ignore
  public void sammenliknerV42_F() {
    SEDTYPER_F.stream().forEach(s -> sammenliknerV42CdmOgAclMal(s, "F", "family"));
  }

  @Test
  @Ignore
  public void sammenliknerV42_H() {
    SEDTYPER_H.stream().forEach(s -> sammenliknerV42CdmOgAclMal(s, "H", "horizontal"));
  }

  @Test
  @Ignore
  public void sammenliknerV42_P() {
    SEDTYPER_P.stream().forEach(s -> sammenliknerV42CdmOgAclMal(s, "P", "pension"));
  }

  @Test
  @Ignore
  public void sammenliknerV42_P3000() {
    SEDTYPER_P3000.stream().forEach(s -> sammenliknerV42CdmOgAclMal(s, "P", "pension"));
  }

  @Test
  @Ignore
  public void sammenliknerV42_R() {
    SEDTYPER_R.stream().forEach(s -> sammenliknerV42CdmOgAclMal(s, "R", "recovery"));
  }

  @Test
  @Ignore
  public void sammenliknerV42_S() {
    SEDTYPER_S.stream().forEach(s -> sammenliknerV42CdmOgAclMal(s, "S", "sickness"));
  }

  @Test
  @Ignore
  public void sammenliknerV42_U() {
    SEDTYPER_U.stream().forEach(s -> sammenliknerV42CdmOgAclMal(s, "U", "unemployment"));
  }

  @Test
  @Ignore
  public void sammenliknerV42_X() {
    SEDTYPER_X.stream().forEach(s -> sammenliknerV42CdmOgAclMal(s, "X", "administrative"));
  }

  private void sammenliknerV42CdmOgAclMal(String sedType, String cdmSektor,
                                          String templateSektor) {
    try {
      String aclMalFilnavn = MAPPE_ACLv42 + "/" + templateSektor + "/" + sedType + "_v4.2.json";
      String aclMalJson = FileUtils.readFileToString(new File(aclMalFilnavn), UTF_8);
      Map<String, Object> aclMalStiVsNavSti = JsonFlattener.flattenAsMap(aclMalJson);
      String cdmMetaMalFilnavn = MAPPE_CDMv42 + "/" + cdmSektor + "/" + sedType + ".json";
      String cdmSedJson = FileUtils.readFileToString(new File(cdmMetaMalFilnavn), UTF_8);
      Map<String, Object> cdmMalFlattened = JsonFlattener.flattenAsMap(cdmSedJson);
      Map<String, String> cdmMalStiVsType = cdmReader.lesMalFraCdm(cdmMalFlattened, sedType);
      List<String> cdmMalStier = cdmMalStiVsType.keySet().stream().sorted().toList();
      List<String> aclMalStier = aclMalStiVsNavSti.keySet().stream().sorted().toList();
      List<String> stierSomKunAclMalenHar = aclMalStiVsNavSti
        .keySet().stream()
        .filter(s -> !cdmMalStier.contains(s))
        .map(s -> s + ": " + aclMalStiVsNavSti.get(s))
        .toList();
      List<String> stierSomKunCdmMalenHar = cdmMalStiVsType
        .keySet().stream()
        .filter(s -> !aclMalStier.contains(s))
        .toList();
      if (stierSomKunAclMalenHar.size() == 0 && stierSomKunCdmMalenHar.size() == 0) {
        System.out.println(sedType + ": OK");
      } else {
        System.out.println(sedType + ": diff " + stierSomKunAclMalenHar.size() + " - "
                             + stierSomKunCdmMalenHar.size());
      }
      String stierKunICdm = StringUtils.join(stierSomKunCdmMalenHar, "\n");
      String stierKunIAcl = StringUtils.join(stierSomKunAclMalenHar, "\n");
      FileUtils.writeStringToFile(
        new File(MAPPE_ACLv42_VS_CDMv42 + "/" + cdmSektor + "/" + sedType + "-kun-i-CDM.json"),
        stierKunICdm, "UTF-8");
      FileUtils.writeStringToFile(
        new File(MAPPE_ACLv42_VS_CDMv42 + "/" + cdmSektor + "/" + sedType + "-kun-i-ACL.json"),
        stierKunIAcl, "UTF-8");
    } catch (Exception e) {
      System.out.println("Error: " + sedType + " " + e.getMessage());
    }
  }
}

