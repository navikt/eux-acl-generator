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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.nav.eux.convert.eused.pdf.MalLinjeDeler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

public class AclV43NyeFeltTest {

  public static final String MAPPE_SED_ENDRETv43 = "src/test/resources/acl-v42-v43-final-diff";
  public static final String MAPPE_SED_TEKST_ENDRINGERv43 = "target";
  public static final String MAPPE_SED_TEKSTERv43 = "src/main/resources/sedoversettelser/v43";
  private static final String UTF_8 = StandardCharsets.UTF_8.name();

  @Test
  @Ignore
  public void nyeFeltTest() {
    List<String> nyeFelt = new ArrayList<>();
    SEDTYPER_A.forEach(sedType -> skrivLinjerMedEndringerTilFil(nyeFelt, sedType));
    SEDTYPER_DA.forEach(sedType -> skrivLinjerMedEndringerTilFil(nyeFelt, sedType));
    SEDTYPER_F.forEach(sedType -> skrivLinjerMedEndringerTilFil(nyeFelt, sedType));
    SEDTYPER_H.forEach(sedType -> skrivLinjerMedEndringerTilFil(nyeFelt, sedType));
    SEDTYPER_M.forEach(sedType -> skrivLinjerMedEndringerTilFil(nyeFelt, sedType));
    SEDTYPER_P.forEach(sedType -> skrivLinjerMedEndringerTilFil(nyeFelt, sedType));
    SEDTYPER_R.forEach(sedType -> skrivLinjerMedEndringerTilFil(nyeFelt, sedType));
    SEDTYPER_S.forEach(sedType -> skrivLinjerMedEndringerTilFil(nyeFelt, sedType));
    SEDTYPER_U.forEach(sedType -> skrivLinjerMedEndringerTilFil(nyeFelt, sedType));
    SEDTYPER_X.forEach(sedType -> skrivLinjerMedEndringerTilFil(nyeFelt, sedType));
    String tilFil = StringUtils.join(nyeFelt, "\n");
    String filStiNavn = MAPPE_SED_TEKST_ENDRINGERv43 + "/AlleEndringerV43.json";
    try {
      FileUtils.writeStringToFile(new File(filStiNavn), tilFil, "UTF-8");
    } catch (IOException e) {
      System.out.println("Error: " + e.getLocalizedMessage());
    }
    nyeFelt.forEach(System.out::println);
  }

  private void skrivLinjerMedEndringerTilFil(List<String> nyeFelt, String sedType) {
    List<String> sedNyeFelt = new ArrayList<>();
    String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
    String endretFilStiNavn =
      MAPPE_SED_ENDRETv43 + "/" + cdmSektor + "/" + sedType + "-v43-adjustments.json";
    String tekstFilStiNavn =
      MAPPE_SED_TEKSTERv43 + "/" + cdmSektor + "/" + sedType + "_oversettelser.json";
    if (new File(endretFilStiNavn).exists()) {
      try {
        nyeFelt.add(sedType + " -----------------------");
        String endringerFlatEuStiVsNavSti = FileUtils.readFileToString(new File(endretFilStiNavn),
                                                                       UTF_8);
        String[] linjer = endringerFlatEuStiVsNavSti.split("\n");
        Map<String, String> euVsNavStier = new HashMap<>();
          Arrays.stream(linjer).forEach(l-> {
            String euStiNavSti[] = l.split(":");
            euVsNavStier.put(euStiNavSti[0], euStiNavSti[1]);
          });

        String teksterJson = FileUtils.readFileToString(new File(tekstFilStiNavn), UTF_8);
        Map<String, MalLinjeDeler> malLinjer = new ObjectMapper()
          .readValue(teksterJson, new TypeReference<>() {
          });
        euVsNavStier.forEach((k,v) -> {
          MalLinjeDeler linjedeler = malLinjer.get(k.replace(".value[0]", ""));
          sedNyeFelt.add(linjedeler.index() + linjedeler.prompt() + " - " + v);
        });
        nyeFelt.addAll(sedNyeFelt.stream().sorted().toList());
      } catch (IOException e) {
        System.out.println("Error: " + e.getLocalizedMessage());
      }
    }
  }

}