package eux.acl.v43;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.eux.acl.EessiAcl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PensionAclTest43 {

  private static final boolean skrivut = true;
  private static final String UTF_8 = StandardCharsets.UTF_8.name();
  private static final String V43 = "4.3";
  private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

  @Test
  public void pension_euToNavTest() {
    String sector = "pension";
    String[] sedTypes = {"P1000", "P1100", "P2000", "P2100", "P2200", "P4000", "P5000", "P5000C", "P6000",
            "P7000B", "P8000", "P9000", "P10000", "P11000", "P12000", "P13000", "P14000", "P15000"};

    euToNavToEuTest(sector, sedTypes, false, true);
  }

  @Test
  public void pensjon_navToEuTest() {
    String sector = "pension";
    String[] sedTypes = {"P5000C"};

    navToEuToNavTest(sector, sedTypes, false);
  }

  @Test
  public void pension3000_xx_euToNavTest() {
    String sector = "pension";
    String[] sedTypes = {
      "P3000_IE", "P3000_HU", "P3000_LT", "P3000_IS", "P3000_UK", "P3000_NO",
      "P3000_IT", "P3000_SI", "P3000_MT", "P3000_BE", "P3000_EE", "P3000_AT", "P3000_BG",
      "P3000_LI", "P3000_DK", "P3000_SE", "P3000_FI", "P3000_PL", "P3000_DE", "P3000_ES",
      "P3000_PT", "P3000_LV", "P3000_SK", "P3000_NL", "P3000_RO", "P3000_FR", "P3000_EL",
      "P3000_HR", "P3000_CY", "P3000_LU", "P3000_CH", "P3000_CZ"
    };

    euToNavToEuTest(sector, sedTypes, true, false);
  }

  private void euToNavToEuTest(String sector, String[] sedTypes, boolean isP3000path, boolean skipInnhold) {
    Arrays.asList(sedTypes).forEach(sedType -> {
      try {

        String psector = sector;
        if (isP3000path) {
          psector += "/P3000";
        }

        String euJsonFileName = "src/test/resources/v43/" + psector + "/" + sedType + "-EU.json";
        String navJsonFileName = "src/test/resources/v43/" + psector + "/" + sedType + "-NAV.json";
        //System.out.println("psector: " + psector + ", euJson: " + euJsonFileName + ", navJson: " + navJsonFileName);

        String euSedString = FileUtils.readFileToString(new File(euJsonFileName), UTF_8);
        String navSedString = FileUtils.readFileToString(new File(navJsonFileName), UTF_8);

        // eu -> nav
        JsonNode navSedGenerated = EessiAcl.mapEuToNav(euSedString, V43);
        printSED(navSedGenerated);

        // nav -> eu
        JsonNode euSedGenerated = EessiAcl.mapNavToEu(navSedString, V43);

        /* --- Genererer filer som kan sammenliknes for å finne feil
        String[] euOrigFlattened = {"-------------  EU orig ---------------\n"};
        Map<String, Object> flattenedEuOrig = JsonFlattener.flattenAsMap(euSedString);
        flattenedEuOrig.keySet().stream().sorted().forEach(k -> {
          euOrigFlattened[0] +=  k + " : " + flattenedEuOrig.get(k) +"\n";
        });

        Map<String, Object> flattenedEuGenerert = JsonFlattener.flattenAsMap(euSedGenerated.toString());
        String[] euGenerertFlattened = {"-------------  EU generert ---------------\n"};
        flattenedEuGenerert.keySet().stream().sorted().forEach(k -> {
          euGenerertFlattened[0] +=  k + " : " + flattenedEuGenerert.get(k) +"\n";
        });

        FileUtils.writeStringToFile(new File("EU-orig.json"), euOrigFlattened[0], "UTF-8");
        FileUtils.writeStringToFile(new File("EU-generert.json"), euGenerertFlattened[0], "UTF-8");
        --- */

        int countsInEuSed = StringUtils.countMatches(euSedString, "æøå");
        int countsInNavSed = StringUtils.countMatches(navSedString, "æøå");
        int countsInEuSedGenerated = StringUtils.countMatches(euSedGenerated.toString(), "æøå");
        int countsInNavSedGenerated = StringUtils.countMatches(navSedGenerated.toString(), "æøå");

        if (skrivut) {
          System.out.println(
            "countsInNavSedGenerated: " + countsInNavSedGenerated + ", countsInEuSed: "
              + countsInEuSed + ", countsInEuSedGenerated: " + countsInEuSedGenerated
              + ", countsInNavSed: " + countsInNavSed);
        }
        System.out.print(sedType + ": ");

        if (isP3000path) {

          int pinCountsInEuSed = StringUtils.countMatches(euSedString, "1256478932566956563262");
          int pinCuntsInNavSed = StringUtils.countMatches(navSedString, "1256478932566956563262");
          int pinCountsInEuSedGenerated = StringUtils
            .countMatches(euSedGenerated.toString(), "1256478932566956563262");
          int pinCountsInNavSedGenerated = StringUtils
            .countMatches(navSedGenerated.toString(), "1256478932566956563262");

          Assertions.assertThat(pinCountsInNavSedGenerated).as("1: eu->nav: 1256478932566956563262")
            .isNotZero().isEqualTo(pinCountsInEuSed);
          Assertions.assertThat(pinCountsInEuSedGenerated).as("1: eu->nav: 1256478932566956563262")
            .isNotZero().isEqualTo(pinCuntsInNavSed);
          Assertions.assertThat(countsInNavSedGenerated).as("1: eu->nav: æøå").isZero()
            .isEqualTo(countsInEuSed);
          Assertions.assertThat(countsInEuSedGenerated).as("2: nav->eu: æøå").isZero()
            .isEqualTo(countsInNavSed);

        } else if (!skipInnhold) {
          Assertions.assertThat(countsInNavSedGenerated).as("1: eu->nav: æøå").isNotZero()
            .isEqualTo(countsInEuSed);
          Assertions.assertThat(countsInEuSedGenerated).as("2: nav->eu: æøå").isNotZero()
            .isEqualTo(countsInNavSed);
        }

        JSONAssert.assertEquals(navSedGenerated.toString(), navSedString, false);
        JSONAssert.assertEquals(euSedGenerated.toString(), euSedString, false);
        System.out.print("Ok\n");

      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

  }

  private void navToEuToNavTest(String sector, String[] sedTypes, boolean isP3000path) {
    Arrays.asList(sedTypes).forEach(sedType -> {
      try {

        String psector = sector;
        if (isP3000path) {
          psector += "/P3000";
        }

        String euJsonFileName = "src/test/resources/v43/" + psector + "/" + sedType + "-EU.json";
        String navJsonFileName = "src/test/resources/v43/" + psector + "/" + sedType + "-NAV.json";
        //System.out.println("psector: " + psector + ", euJson: " + euJsonFileName + ", navJson: " + navJsonFileName);

        String euSedString = FileUtils.readFileToString(new File(euJsonFileName), UTF_8);
        String navSedString = FileUtils.readFileToString(new File(navJsonFileName), UTF_8);

        // nav -> eu
        JsonNode euSedGenerated = EessiAcl.mapNavToEu(navSedString, V43);

        printSED(euSedGenerated);

        // eu -> nav
        JsonNode navSedGenerated = EessiAcl.mapEuToNav(euSedString, V43);

        printSED(navSedGenerated);

        JSONAssert.assertEquals(navSedGenerated.toString(), navSedString, false);
        JSONAssert.assertEquals(euSedGenerated.toString(), euSedString, false);
        System.out.print("Ok\n");

      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

  }


  private void printSED(JsonNode jsonNode) throws Exception {
    if (skrivut) {
      System.out.println("-----------------------------------------------------------------------");
      System.out.println(JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));
      System.out.println("-----------------------------------------------------------------------");
    }
  }

}
