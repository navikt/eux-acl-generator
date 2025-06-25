package eux.acl.temp;

import com.github.wnameless.json.flattener.JsonFlattener;
import no.nav.eux.acl.v43.CdmReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static no.nav.eux.acl.v43.ACLv43Test.*;

public class AclV44BasismalerTest {

  static final String CDMv44_MAPPE = "src/test/resources/cdm-v44-meta";
  static final String CDMv43_MAPPE = "src/test/resources/cdm-v43-meta";
  private static final String ACLv43_MAPPE = "src/main/resources/sedtemplates/v43";
  private static final String ACLv43_MAL_FILNAVN_ENDEX = "_v4.3.json";
  public static final String ACLv44_MELLOMBELS_MAPPE = "target/v44Raw";
  public static String MAL_NYTT_FILNAVN_ENDE = "-v44-nytt.json";
  public static String MAL_FLAT_FILNAVN_ENDE = "-v44-flat.json";
  private static final String MAL_UTGAAR_FILNAVN_ENDE = "-v44-utgaar.json";


  private static final String UTF_8 = StandardCharsets.UTF_8.name();
  private CdmReader cdmReader = new CdmReader();

  /**
   * Sammenlikner v4.2 med v4.3. Det som er likt skrives til ei fil,
   * det som kun fins i v4.2 skrives til ei anna fil, og det som kun fins i v4.3 til ei tredje fil.
   * Filene som inneholder justeringer som er manuellt lagt inn ( ..adjustments.json) blir ikke overskrevet.
   * De genereres kun når disse filene ikke finnes fra tidligere.
   */

  @Test
  @Ignore
  public void malutgangspunktForV43() {
    List.of("F001").stream().forEach(s -> lagUtgangspunktForV44Mal(s, "F", "family"));
  }

  @Test
  @Ignore
  public void malutgangspunktForV43_A() {
    SEDTYPER_A.stream()
      .forEach(s -> lagUtgangspunktForV44Mal(s, "A", "legislationapplicable"));
  }

  @Test
  @Ignore
  public void malutgangspunktForV43_DA() {
    SEDTYPER_DA.stream().forEach(s -> lagUtgangspunktForV44Mal(s, "DA", "awod"));
  }

  @Test
  @Ignore
  public void malutgangspunktForV43_F() {
    SEDTYPER_F.stream().forEach(s -> lagUtgangspunktForV44Mal(s, "F", "family"));
  }

  @Test
  @Ignore
  public void malutgangspunktForV43_H() {
    SEDTYPER_H.stream().forEach(s -> lagUtgangspunktForV44Mal(s, "H", "horizontal"));
  }

  @Test
  @Ignore
  public void malutgangspunktForV43_M() {
    SEDTYPER_M.stream().forEach(s -> lagUtgangspunktForV44Mal(s, "M", "miscellaneous"));
  }

  @Test
  @Ignore
  public void malutgangspunktForV43_P() {
    SEDTYPER_P.stream().forEach(s -> lagUtgangspunktForV44Mal(s, "P", "pension"));
  }

  @Test
  @Ignore
  public void malutgangspunktForV43_R() {
    SEDTYPER_R.stream().forEach(s -> lagUtgangspunktForV44Mal(s, "R", "recovery"));
  }

  @Test
  @Ignore
  public void malutgangspunktForV43_S() {
    SEDTYPER_S.stream().forEach(s -> lagUtgangspunktForV44Mal(s, "S", "sickness"));
  }

  @Test
  @Ignore
  public void malutgangspunktForV43_U() {
    SEDTYPER_U.stream().forEach(s -> lagUtgangspunktForV44Mal(s, "U", "unemployment"));
  }

  @Test
  @Ignore
  public void malutgangspunktForV43_X() {
    SEDTYPER_X.stream().forEach(s -> lagUtgangspunktForV44Mal(s, "X", "administrative"));
  }

  private void lagUtgangspunktForV44Mal(String sedType, String cdmSektor, String aclSector) {
    String cdmV43TemplateFilnavn = CDMv44_MAPPE + "/" + cdmSektor + "/" + sedType + ".json";
    String aclV42TemplateFilnavn = ACLv43_MAPPE + "/" + aclSector + "/" + sedType + ACLv43_MAL_FILNAVN_ENDEX;
    try {
      String aclMal42Json = FileUtils.readFileToString(new File(aclV42TemplateFilnavn), UTF_8);
      Map<String, Object> acl42StiVsNavSti = JsonFlattener.flattenAsMap(aclMal42Json);
      String cdm43FileJson = FileUtils.readFileToString(new File(cdmV43TemplateFilnavn), UTF_8);
      Map<String, Object> cdmSedFlattenedMap = JsonFlattener.flattenAsMap(cdm43FileJson);
      Map<String, String> cdm43StiVsType = cdmReader.lesMalFraCdm(cdmSedFlattenedMap, sedType);
      // Lag (cdm) base-mal kun med stier som også fins i acl-en.
      List<String> cdmFlatList = cdm43StiVsType.keySet().stream()
        .filter(k -> acl42StiVsNavSti.containsKey(k))
        .map(k -> k + ": " + acl42StiVsNavSti.get(k).toString())
        .toList();
      // Sammenlikne:
      List<String> kunIAcl = acl42StiVsNavSti.keySet().stream()
        .filter(k -> !cdm43StiVsType.containsKey(k))
        .map(k -> k + ": " + acl42StiVsNavSti.get(k))
        .toList();
      List<String> kunICdm = cdm43StiVsType.keySet().stream()
        .filter(k -> !acl42StiVsNavSti.containsKey(k))
        .map(k -> k + ": ")
        .toList();
      List<String> cdmAlleStierFlatList = new ArrayList<>();
      cdmAlleStierFlatList.addAll(cdmFlatList);
      cdmAlleStierFlatList.addAll(kunICdm);
      String cdmMalAlleEuStier = StringUtils.join(cdmAlleStierFlatList.stream()
                                                    .sorted().toList(), "\n");
      String kunAclFlatList = StringUtils.join(kunIAcl.stream().sorted().toList(), "\n");
      String kunCdmFlatList = StringUtils.join(kunICdm.stream().sorted().toList(), "\n");
      FileUtils.writeStringToFile(
        new File(ACLv44_MELLOMBELS_MAPPE + "/" + cdmSektor + "/" + sedType + MAL_FLAT_FILNAVN_ENDE),
        cdmMalAlleEuStier, "UTF-8");
      FileUtils.writeStringToFile(
        new File(ACLv44_MELLOMBELS_MAPPE + "/" + cdmSektor + "/" + sedType + MAL_UTGAAR_FILNAVN_ENDE),
        kunAclFlatList, "UTF-8");
      String templateAdjustmentsFile =
        ACLv44_MELLOMBELS_MAPPE + "/" + cdmSektor + "/" + sedType + MAL_NYTT_FILNAVN_ENDE;
      if (Files.notExists(Paths.get(templateAdjustmentsFile))) {
        FileUtils.writeStringToFile(new File(templateAdjustmentsFile), kunCdmFlatList, "UTF-8");
      }
      if (kunIAcl.size() == 0 && kunICdm.size() == 0) {
        System.out.println(sedType + ": OK");
      } else {
        System.out.println(sedType + ": diff " + kunIAcl.size() + " - " + kunICdm.size());
      }
    } catch (Exception e) {
      System.out.println("Error. Kunne ikke lese templatefil. " + sedType
                           + " - " + e.getLocalizedMessage());
    }
  }

}

