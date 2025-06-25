package eux.acl.v43;

import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_A;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_DA;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_F;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_H;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_M;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_P;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_P3000;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_R;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_S;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_U;
import static no.nav.eux.acl.v43.ACLv43Test.SEDTYPER_X;
import static no.nav.eux.acl.v43.AclV43BasismalerTest.MAPPE_ACLv43_MELLOMBELS;
import static no.nav.eux.acl.v43.AclV43TestdataTest.MAPPE_CDM_V42_META;
import static no.nav.eux.acl.v43.AclV43TestdataTest.MAPPE_CDM_V43_META;

import com.github.wnameless.json.flattener.JsonFlattener;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


public class AclV42VsV43KodeverkTest {

  private CdmReader cdmReader = new CdmReader();
  private static final String UTF_8 = StandardCharsets.UTF_8.name();

  /**
   * lager mal basert på den v4.2-versjonen som følger med CDM v4.3 og sammenlikner den med ACL v4.2.
   * Diff skrives til fil.
   */
  @Test
  @Disabled
  public void sammenliknerCdmKodeverk_A() {
    SEDTYPER_A.stream().forEach(s -> sammenlikneCdmKodeverk(s, "A"));
  }

  @Test
  @Disabled
  public void sammenliknerV42_DA() {
    SEDTYPER_DA.stream().forEach(s -> sammenlikneCdmKodeverk(s, "DA"));
  }

  @Test
  @Disabled
  public void sammenliknerCdmKodeverk_F() {
    SEDTYPER_F.stream().forEach(s -> sammenlikneCdmKodeverk(s, "F"));
  }

  @Test
  @Ignore
  public void sammenliknerCdmKodeverk_H() {
    SEDTYPER_H.stream().forEach(s -> sammenlikneCdmKodeverk(s, "H"));
  }

  @Test
  @Disabled
  public void sammenliknerCdmKodeverk_M() {
    SEDTYPER_M.stream().forEach(s -> sammenlikneCdmKodeverk(s, "M"));
  }

  @Test
  @Disabled
  public void sammenliknerCdmKodeverk_P() {
    SEDTYPER_P.stream().forEach(s -> sammenlikneCdmKodeverk(s, "P"));
  }

  @Test
  @Disabled
  public void sammenliknerCdmKodeverk_P3000() {
    SEDTYPER_P3000.stream().forEach(s -> sammenlikneCdmKodeverk(s, "P"));
  }

  @Test
  @Disabled
  public void sammenliknerCdmKodeverk_R() {
    SEDTYPER_R.stream().forEach(s -> sammenlikneCdmKodeverk(s, "R"));
  }

  @Test
  @Disabled
  public void sammenliknerCdmKodeverk_S() {
    SEDTYPER_S.stream().forEach(s -> sammenlikneCdmKodeverk(s, "S"));
  }

  @Test
  @Disabled
  public void sammenliknerCdmKodeverk_U() {
    SEDTYPER_U.stream().forEach(s -> sammenlikneCdmKodeverk(s, "U"));
  }

  @Test
  @Disabled
  public void sammenliknerCdmKodeverk_X() {
    SEDTYPER_X.stream().forEach(s -> sammenlikneCdmKodeverk(s, "X"));
  }

  private void sammenlikneCdmKodeverk(String sedType, String cdmSektor) {
    try {
      String cdmMeta43MalFilnavn = MAPPE_CDM_V43_META + "/" + cdmSektor + "/" + sedType + ".json";
      String cdm43SedJson = FileUtils.readFileToString(new File(cdmMeta43MalFilnavn), UTF_8);
      Map<String, Object> cdm43SedFlattened = JsonFlattener.flattenAsMap(cdm43SedJson);
      Map<String, String> cdmV43StiVsKodeverk = cdmReader.lesKodeverkFraCdm(cdm43SedFlattened);
      String cdmMeta42MalFilnavn = MAPPE_CDM_V42_META + "/" + cdmSektor + "/" + sedType + ".json";
      String cdm42SedJson = FileUtils.readFileToString(new File(cdmMeta42MalFilnavn), UTF_8);
      Map<String, Object> cdm42SedFlattened = JsonFlattener.flattenAsMap(cdm42SedJson);
      Map<String, String> cdmV42StiVsKodeverk = cdmReader.lesKodeverkFraCdm(cdm42SedFlattened);
      List<String> kodeverkV43FlatList = cdmV43StiVsKodeverk.keySet().stream()
        .map(k -> k.split("\\.option\\[")[0] + ": " + cdmV43StiVsKodeverk.get(k).toString())
        .toList();
      List<String> kodeverkV42FlatList = cdmV42StiVsKodeverk.keySet().stream()
        .map(k -> k.split("\\.option\\[")[0] + ": " + cdmV42StiVsKodeverk.get(k).toString())
        .toList();
      List<String> kunIv43 = kodeverkV43FlatList.stream()
        .filter(l -> !kodeverkV42FlatList.contains(l))
        .toList();
      List<String> kunIv42 = kodeverkV42FlatList.stream()
        .filter(l -> !kodeverkV43FlatList.contains(l))
        .toList();
      String kodeverkV43FlatBlock = StringUtils.join(kunIv43.stream().sorted().toList(), "\n");
      String kodeverkV42FlatBlock = StringUtils.join(kunIv42.stream().sorted().toList(), "\n");
      FileUtils.writeStringToFile(
        new File(MAPPE_ACLv43_MELLOMBELS + "/" + cdmSektor + "/" + sedType + "-kodeverk-v43.json"),
        kodeverkV43FlatBlock, "UTF-8");
      FileUtils.writeStringToFile(
        new File(MAPPE_ACLv43_MELLOMBELS + "/" + cdmSektor + "/" + sedType + "-kodeverk-v42.json"),
        kodeverkV42FlatBlock, "UTF-8");
    } catch (Exception e) {
      System.out.println("Error: " + sedType + " " + e.getMessage());
    }
  }
}

