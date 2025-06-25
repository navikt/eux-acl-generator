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

public class F026F027AclTest43 {

  private static final boolean skrivut = true;
  private static final String UTF_8 = StandardCharsets.UTF_8.name();
  private static final String V43 = "4.3";
  private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

  @Test
  public void f026EuToNavTest() {
    String sector = "family";
    String[] sedTypes = {"F026"};

    euToNavToEuTest(sector, sedTypes, true);
  }

  @Test
  public void f026NavToEuTest() {
    String sector = "family";
    String[] sedTypes = {"F026"};

    navToEuToNavTest(sector, sedTypes);
  }

  @Test
  public void f027EuToNavTest() {
    String sector = "family";
    String[] sedTypes = {"F027"};

    euToNavToEuTest(sector, sedTypes, true);
  }

  @Test
  public void f027NavToEuTest() {
    String sector = "family";
    String[] sedTypes = {"F027"};

    navToEuToNavTest(sector, sedTypes);
  }

  private void euToNavToEuTest(String sector, String[] sedTypes, boolean skipInnhold) {
    Arrays.asList(sedTypes).forEach(sedType -> {
      try {

        String euJsonFileName = "src/test/resources/v43/" + sector + "/" + sedType + "-EU.json";
        String navJsonFileName = "src/test/resources/v43/" + sector + "/" + sedType + "-NAV.json";

        String euSedString = FileUtils.readFileToString(new File(euJsonFileName), UTF_8);
        String navSedString = FileUtils.readFileToString(new File(navJsonFileName), UTF_8);

        // eu -> nav
        JsonNode navSedGenerated = EessiAcl.mapEuToNav(euSedString, V43);
        printSED(navSedGenerated);

        // nav -> eu
        JsonNode euSedGenerated = EessiAcl.mapNavToEu(navSedString, V43);

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

        if (!skipInnhold) {
          Assertions.assertThat(countsInNavSedGenerated).as("1: eu->nav: æøå").isNotZero()
            .isEqualTo(countsInEuSed);
          Assertions.assertThat(countsInEuSedGenerated).as("2: nav->eu: æøå").isNotZero()
            .isEqualTo(countsInNavSed);
        }

        JSONAssert.assertEquals(navSedString, navSedGenerated.toString(), false);
        JSONAssert.assertEquals(euSedString, euSedGenerated.toString(), false);
        System.out.print("Ok\n");

      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

  }

  private void navToEuToNavTest(String sector, String[] sedTypes) {
    Arrays.asList(sedTypes).forEach(sedType -> {
      try {

        String euJsonFileName = "src/test/resources/v43/" + sector + "/" + sedType + "-EU.json";
        String navJsonFileName = "src/test/resources/v43/" + sector + "/" + sedType + "-NAV.json";

        String euSedString = FileUtils.readFileToString(new File(euJsonFileName), UTF_8);
        String navSedString = FileUtils.readFileToString(new File(navJsonFileName), UTF_8);

        // nav -> eu
        JsonNode euSedGenerated = EessiAcl.mapNavToEu(navSedString, V43);

        printSED(euSedGenerated);

        // eu -> nav
        JsonNode navSedGenerated = EessiAcl.mapEuToNav(euSedString, V43);

        printSED(navSedGenerated);

        JSONAssert.assertEquals(euSedString, euSedGenerated.toString(), false);
        JSONAssert.assertEquals(navSedString, navSedGenerated.toString(), false);
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
