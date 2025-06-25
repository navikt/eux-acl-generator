package eux.acl.temp;

import com.github.wnameless.json.flattener.JsonFlattener;
import no.nav.eux.acl.v43.CdmReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static no.nav.eux.acl.temp.AclV44BasismalerTest.*;
import static no.nav.eux.acl.v43.ACLv43Test.*;


public class AclV43VsV44KodeverkTest {

  private CdmReader cdmReader = new CdmReader();
  private static final String UTF_8 = StandardCharsets.UTF_8.name();

  /**
   * lager mal basert på den v4.2-versjonen som følger med CDM v4.3 og sammenlikner den med ACL v4.2.
   * Diff skrives til fil.
   */
  @Test
  @Disabled
  public void sammenliknerCdmKodeverk() {
    List.of("F001").forEach(s -> sammenlikneCdmKodeverk(s, "F"));
  }


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
      String cdmMeta43MalFilnavn = CDMv44_MAPPE + "/" + cdmSektor + "/" + sedType + ".json";
      String cdm43SedJson = FileUtils.readFileToString(new File(cdmMeta43MalFilnavn), UTF_8);
      Map<String, Object> cdm43SedFlattened = JsonFlattener.flattenAsMap(cdm43SedJson);
      Map<String, String> cdmV44StiVsKodeverk = cdmReader.lesKodeverkFraCdm(cdm43SedFlattened);
      String cdmMeta42MalFilnavn = CDMv43_MAPPE + "/" + cdmSektor + "/" + sedType + ".json";
      String cdm42SedJson = FileUtils.readFileToString(new File(cdmMeta42MalFilnavn), UTF_8);
      Map<String, Object> cdm42SedFlattened = JsonFlattener.flattenAsMap(cdm42SedJson);
      Map<String, String> cdmV43StiVsKodeverk = cdmReader.lesKodeverkFraCdm(cdm42SedFlattened);
      List<String> kodeverkV44FlatList = cdmV44StiVsKodeverk.keySet().stream()
        .map(k -> k.split("\\.option\\[")[0] + ": " + cdmV44StiVsKodeverk.get(k).toString())
        .toList();
      List<String> kodeverkV43FlatList = cdmV43StiVsKodeverk.keySet().stream()
        .map(k -> k.split("\\.option\\[")[0] + ": " + cdmV43StiVsKodeverk.get(k).toString())
        .toList();
      List<String> kunIv43 = kodeverkV44FlatList.stream()
        .filter(l -> !kodeverkV43FlatList.contains(l))
        .toList();
      List<String> kunIv42 = kodeverkV43FlatList.stream()
        .filter(l -> !kodeverkV44FlatList.contains(l))
        .toList();
      String kodeverkV43FlatBlock = StringUtils.join(kunIv43.stream().sorted().toList(), "\n");
      String kodeverkV42FlatBlock = StringUtils.join(kunIv42.stream().sorted().toList(), "\n");
      FileUtils.writeStringToFile(
        new File(ACLv44_MELLOMBELS_MAPPE + "/" + cdmSektor + "/" + sedType + "-kodeverk-v44.json"),
        kodeverkV43FlatBlock, "UTF-8");
      FileUtils.writeStringToFile(
        new File(ACLv44_MELLOMBELS_MAPPE + "/" + cdmSektor + "/" + sedType + "-kodeverk-v43.json"),
        kodeverkV42FlatBlock, "UTF-8");
    } catch (Exception e) {
      System.out.println("Error: " + sedType + " " + e.getMessage());
    }
  }
}

