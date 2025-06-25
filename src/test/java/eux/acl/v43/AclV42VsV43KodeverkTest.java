package eux.acl.v43;

import com.github.wnameless.json.flattener.JsonFlattener;
import eux.acl.generator.SedType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static eux.acl.v43.ACLv43Test.*;
import static eux.acl.v43.AclV43BasismalerTest.MAPPE_ACLv43_MELLOMBELS;
import static eux.acl.v43.AclV43TestdataTest.MAPPE_CDM_V42_META;
import static eux.acl.v43.AclV43TestdataTest.MAPPE_CDM_V43_META;


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
      for (String s : SedType.A) {
          sammenlikneCdmKodeverk(s, "A");
      }
  }

  @Test
  @Disabled
  public void sammenliknerV42_DA() {
    SedType.DA.stream().forEach(s -> sammenlikneCdmKodeverk(s, "DA"));
  }

  @Test
  @Disabled
  public void sammenliknerCdmKodeverk_F() {
    SedType.F.stream().forEach(s -> sammenlikneCdmKodeverk(s, "F"));
  }

  @Test
  @Ignore
  public void sammenliknerCdmKodeverk_H() {
    SedType.H.stream().forEach(s -> sammenlikneCdmKodeverk(s, "H"));
  }

  @Test
  @Disabled
  public void sammenliknerCdmKodeverk_M() {
    SedType.M.stream().forEach(s -> sammenlikneCdmKodeverk(s, "M"));
  }

  @Test
  @Disabled
  public void sammenliknerCdmKodeverk_P() {
    SedType.P.stream().forEach(s -> sammenlikneCdmKodeverk(s, "P"));
  }

  @Test
  @Disabled
  public void sammenliknerCdmKodeverk_P3000() {
    SedType.P3000.stream().forEach(s -> sammenlikneCdmKodeverk(s, "P"));
  }

  @Test
  @Disabled
  public void sammenliknerCdmKodeverk_R() {
    SedType.R.stream().forEach(s -> sammenlikneCdmKodeverk(s, "R"));
  }

  @Test
  @Disabled
  public void sammenliknerCdmKodeverk_S() {
    SedType.S.stream().forEach(s -> sammenlikneCdmKodeverk(s, "S"));
  }

  @Test
  @Disabled
  public void sammenliknerCdmKodeverk_U() {
    SedType.U.stream().forEach(s -> sammenlikneCdmKodeverk(s, "U"));
  }

  @Test
  @Disabled
  public void sammenliknerCdmKodeverk_X() {
    SedType.X.stream().forEach(s -> sammenlikneCdmKodeverk(s, "X"));
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

