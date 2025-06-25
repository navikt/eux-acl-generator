package eux.acl.v43;

import com.github.wnameless.json.flattener.JsonFlattener;
import no.nav.eux.acl.EessiAcl;
import no.nav.eux.rina.cpi.service.EessiAclClient;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static no.nav.eux.rina.cpi.service.EessiAclClient.*;

@ExtendWith(MockitoExtension.class)
public class ACLv43Test {

  private static final String MAPPE_ACLv43_TESTDATA_SED = "src/test/resources/v43";
  private static final String UTF_8 = StandardCharsets.UTF_8.name();
  private static final String V42 = "4.2";
  private static final String V43 = "4.3";


  public static final List<String> SEDTYPER_A = List.of("A001", "A002", "A003", "A004", "A005"
    , "A006", "A007", "A008", "A009", "A010", "A011", "A012");
  public static final List<String> SEDTYPER_DA = List.of("DA031", "DA040", "DA041", "DA042"
    , "DA043", "DA044", "DA046", "DA047", "DA048", "DA049", "DA050", "DA051", "DA052", "DA053"
    , "DA054", "DA055", "DA057", "DA058", "DA059", "DA060", "DA061", "DA062", "DA063", "DA073A"
    , "DA073C", "DA073R", "DA074");
  public static final List<String> SEDTYPER_F = List.of("F001", "F002", "F003", "F004", "F005"
    , "F016", "F017", "F018", "F019", "F020", "F021", "F022", "F023", "F026", "F027");
  public static List<String> SEDTYPER_H = List.of("H001", "H002", "H003", "H004", "H005", "H006"
    , "H010", "H011", "H012", "H020", "H021", "H061", "H062", "H065", "H066", "H070", "H120"
    , "H121", "H130", "H131");
  public static final List<String> SEDTYPER_M = List.of("M030", "M040", "M050", "M051", "M052"
    , "M053");
  public static final List<String> SEDTYPER_P = List.of("P1000", "P1100", "P2000", "P2100", "P2200"
    , "P4000", "P5000", "P6000", "P7000", "P8000", "P9000", "P10000", "P11000", "P12000", "P13000"
    , "P14000", "P15000");

  public static final List<String> SEDTYPER_P3000 = List.of("P3000_AT", "P3000_BE", "P3000_BG"
    , "P3000_CH", "P3000_CY", "P3000_CZ", "P3000_DE", "P3000_DK", "P3000_EE", "P3000_EL"
    , "P3000_ES", "P3000_FI", "P3000_FR", "P3000_HR", "P3000_HU", "P3000_IE", "P3000_IS"
    , "P3000_IT", "P3000_LI", "P3000_LT", "P3000_LU", "P3000_LV", "P3000_MT", "P3000_NL"
    , "P3000_NO", "P3000_PL", "P3000_PT", "P3000_RO", "P3000_SE", "P3000_SI", "P3000_SK"
    , "P3000_UK");

  public static final List<String> SEDTYPER_R = List.of("R001", "R002", "R003", "R004", "R005", "R006"
    , "R008", "R009");
  public static final List<String> SEDTYPER_S = List.of("S001", "S003", "S005", "S040"
    , "S041", "S044", "S045", "S046", "S047", "S048", "S055", "S056", "S057");
  public static final List<String> SEDTYPER_U = List.of("U001", "U001CB", "U002", "U003"
    , "U004", "U005", "U006", "U007", "U008", "U009", "U010", "U011", "U012", "U013", "U014"
    , "U015", "U016", "U017", "U018", "U019", "U020", "U021", "U023", "U024", "U025", "U026"
    , "U027", "U029");
  public static final List<String> SEDTYPER_X = List.of("X001", "X002", "X003", "X004"
    , "X005", "X006", "X007", "X008", "X009", "X010", "X011", "X012", "X013", "X050", "X100");

    @Test
    public void landkoderFraEuxDomenetTilAclDomenet() throws IOException {
        String navJsonFileName = "src/test/resources/landkoder/navSedMedLandkoderEuxTilRina.json";
        String navSed = FileUtils.readFileToString(new File(navJsonFileName), UTF_8);

        String euSed = fraNavSedTilEuSed(navSed, "4.2");
        Assertions.assertThat(euSed).doesNotContain("null");
        Assertions.assertThat(euSed).doesNotContain("\"XX\"");
        Assertions.assertThat(euSed).doesNotContain("\"ZZ\"");
        Assertions.assertThat(euSed).doesNotContain("\"YUG\"");
        Assertions.assertThat(euSed).contains("\"YU\"");
        Assertions.assertThat(euSed).doesNotContain("\"YMD\"");
        Assertions.assertThat(euSed).doesNotContain("\"YD\"");
        Assertions.assertThat(euSed).contains("\"YDYE\"");
        Assertions.assertThat(euSed).doesNotContain("\"GBR\"");
        Assertions.assertThat(euSed).contains("\"UK\"");
        Assertions.assertThat(euSed).doesNotContain("\"GRC\"");
        Assertions.assertThat(euSed).contains("\"EL\"");
    }

    @Test
    public void landkoderFraAclDomenetTilEuxDomenet() throws IOException {
        String euJsonFileName = "src/test/resources/landkoder/euSedMedLandkoderRinaTilEux.json";
        String euSed = FileUtils.readFileToString(new File(euJsonFileName), UTF_8);

        String aclSedIso2 = mapEuSedToNav(euSed, "4.2").toString();
        String navSedIso3 = justerAclSedFraRina(aclSedIso2, "nav");
        Assertions.assertThat(navSedIso3).contains("\"");
        Assertions.assertThat(navSedIso3).doesNotContain("\"null\"");
        Assertions.assertThat(navSedIso3).doesNotContain("\"XX\"");
        Assertions.assertThat(navSedIso3).contains("\"YUG\"");
        Assertions.assertThat(navSedIso3).contains("\"YUCS\"");
        Assertions.assertThat(navSedIso3).contains("\"GBR\"");
        Assertions.assertThat(navSedIso3).contains("\"GB\"");
        Assertions.assertThat(navSedIso3).contains("\"GRC\"");
        Assertions.assertThat(navSedIso3).contains("\"GR\"");
        Assertions.assertThat(navSedIso3).contains("\"ANHH\"");
        Assertions.assertThat(navSedIso3).contains("\"ANT\"");
        Assertions.assertThat(navSedIso3).contains("\"ZRCD\"");
        Assertions.assertThat(navSedIso3).contains("\"ZAR\"");
        Assertions.assertThat(navSedIso3).doesNotContain("\"UK\"");
        Assertions.assertThat(navSedIso3).doesNotContain("\"EL\"");
    }
  @Test
  public void navToEuToNavTest() {
      List<String> sedTyper = List.of(
      "H001"
    );
    navToEuToNavTest("horizontal", sedTyper);
  }

  @Test
  public void euToNavToEuTest() {
    List<String> sedTyper = List.of(
      "H001"
    );
    euToNavToEuTest("horizontal", sedTyper);
  }

  @Test
  public void euToNavToEuTest_A() {
    euToNavToEuTest("legislationapplicable", SEDTYPER_A);
  }

  @Test
  public void euToNavToEuTest_DA() {
    euToNavToEuTest("awod", SEDTYPER_DA);
  }

  @Test
  public void euToNavToEuTest_F() {
    euToNavToEuTest("family", SEDTYPER_F);
  }

  @Test
  public void euToNavToEuTest_H() {
    euToNavToEuTest("horizontal", SEDTYPER_H);
  }

  @Test
  public void euToNavToEuTest_M() {
    euToNavToEuTest("miscellaneous", SEDTYPER_M);
  }

  @Test
  public void euToNavToEuTest_P() {
    euToNavToEuTest("pension", SEDTYPER_P);
  }

  @Test
  public void euToNavToEuTest_R() {
    euToNavToEuTest("recovery", SEDTYPER_R);
  }

  @Test
  public void euToNavToEuTest_S() {
    euToNavToEuTest("sickness", SEDTYPER_S);
  }

  @Test
  public void euToNavToEuTest_U() {
    euToNavToEuTest("unemployment", SEDTYPER_U);
  }

  @Test
  public void euToNavToEuTest_X() {
    euToNavToEuTest("administrative", SEDTYPER_X);
  }



  private void euToNavToEuTest(String sector, List<String> sedTypes) {
    sedTypes.forEach(sedType -> {
      try {
        String euJsonFileName =
                MAPPE_ACLv43_TESTDATA_SED + "/" + sector + "/" + sedType + "-EU.json";
        String origEuSed = FileUtils.readFileToString(new File(euJsonFileName), UTF_8);
        // eu -> nav
        String aclSed = mapEuSedToNav(origEuSed, V43).toString();
        String navSedGenerert = EessiAclClient.justerAclSedFraRina(aclSed, "nav");
        // nav -> eu
        String euSedGenerert = EessiAclClient.fraNavSedTilEuSed(navSedGenerert, V43);
        // kontrollerer
        System.out.print(sedType + ": ");
        Map<String, Object> origFlattened = JsonFlattener.flattenAsMap(origEuSed);
        List<String> origFlatList = origFlattened.keySet().stream()
          .map(k -> k + ": " + origFlattened.get(k).toString())
          .toList();
        List<String> origFlatListSorted = origFlatList.stream().sorted().toList();
        Map<String, Object> generatedFlattened = JsonFlattener.flattenAsMap(
                euSedGenerert.toString());
        List<String> generatedFlatList = generatedFlattened.keySet().stream()
          .map(k -> k + ": " + generatedFlattened.get(k).toString())
          .toList();
        List<String> generatedFlatListSorted = generatedFlatList.stream().sorted().toList();
        boolean suksess = true;
        for (int n = 0; n < origFlatListSorted.size(); n++) {
          if (!origFlatListSorted.get(n).equals(generatedFlatListSorted.get(n))) {
            System.out.print("Feil\n");
            System.out.println(" Forventet: " + origFlatListSorted.get(n));
            System.out.println("      Fikk: " + generatedFlatListSorted.get(n));
            suksess = false;
            throw new RuntimeException("Feil - Responsen matchet ikke utgangspunktet");
//          break;
          }
        }
        if (suksess) {
          System.out.print("Ok\n");
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  private void navToEuToNavTest(String sector, List<String> sedTypes) {
    sedTypes.forEach(sedType -> {
      try {
          String navJsonFileName =
                  MAPPE_ACLv43_TESTDATA_SED + "/" + sector + "/" + sedType + "-NAV.json";
          String origNavSed = FileUtils.readFileToString(new File(navJsonFileName), UTF_8);
          // nav -> eu
          String euSedGenerert = EessiAclClient.fraNavSedTilEuSed(origNavSed, V43);
          // eu -> nav
          String aclSedGenerert = EessiAcl.mapEuToNav(euSedGenerert, V43).toString();
          String navSedGenerert = EessiAclClient.justerAclSedFraRina(aclSedGenerert, "nav");
        // kontrollerer
        System.out.print(sedType + ": ");
        Map<String, Object> origNavFlattened = JsonFlattener.flattenAsMap(origNavSed);
        List<String> origNavFlatList = origNavFlattened.keySet().stream()
                .map(k -> k + ": " + origNavFlattened.get(k).toString())
                .toList();
        List<String> origNavFlatListSorted = origNavFlatList.stream().sorted().toList();
        Map<String, Object> generatedNavFlattened = JsonFlattener.flattenAsMap(
                navSedGenerert);
        List<String> generatedNavFlatList = generatedNavFlattened.keySet().stream()
                .map(k -> k + ": " + generatedNavFlattened.get(k).toString())
                .toList();
        List<String> generatedNavFlatListSorted = generatedNavFlatList.stream().sorted().toList();
//        String[] flattenedListe = {""};
//        generatedNavFlatListSorted.forEach(k -> flattenedListe[0] = flattenedListe[0] + k +"\n");
        boolean suksess = true;
        for (int n = 0; n < origNavFlatListSorted.size(); n++) {
          if (!origNavFlatListSorted.get(n).equals(generatedNavFlatListSorted.get(n))) {
            System.out.print("Feil\n");
            System.out.println(" Forventet: " + origNavFlatListSorted.get(n));
            System.out.println("      Fikk: " + generatedNavFlatListSorted.get(n));
            suksess = false;
            throw new RuntimeException("Feil - Responsen matchet ikke utgangspunktet");
//            break;
          }
        }
        if (suksess) {
          System.out.print("Ok\n");
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

}

