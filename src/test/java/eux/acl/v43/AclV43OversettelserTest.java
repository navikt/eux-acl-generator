package eux.acl.v43;

import static java.util.Arrays.asList;
import static no.nav.eux.acl.v43.ACLv43Test.*;
import static no.nav.eux.acl.v43.AclV43TestdataTest.MAPPE_CDM_V43_META;
import static no.nav.eux.acl.v43.AclV43TestdataTest.OVERSETTELSER_CDM_FIL;
import static no.nav.eux.acl.v43.AclV43TestdataTest.OVERSETTELSER_ID_TEKST_SEDTYPE_FIL;
import static no.nav.eux.acl.v43.AclV43TestdataTest.OVERSETTELSER_MAPPE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.json.flattener.JsonFlattener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import no.nav.eux.convert.eused.pdf.MalLinjeDeler;
import org.apache.commons.collections15.map.LinkedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.junit.Test;


public class AclV43OversettelserTest {

  public static final String MAPPE_SED_OVERSETTELSERv43 = "src/main/resources/sedoversettelser/v43";
  private CdmReader cdmReader = new CdmReader();
  private static final String UTF_8 = StandardCharsets.UTF_8.name();

  /**
   * CDM 4.3 inneholder ca 1000 kopier av landkodene. Dette fordi samtlige landkoder listes hver gang de anvendes.
   * Det er kun landkodene som består av 2 eller 4 bokstaver (de andre består av tall). De kan derfor skilles fra andre koder.
   * I testen leses alle landkodene og det undersøkes om samme landkode har samme landnavn overalt hvor den er brukt.
   * Landkoder med alternative landnavn samles opp i egen liste.
   * Testen genererer til sist et landkodesett med alle de landkoder og landnavn som er funnet.
   * Landkodesettet lagres i ei json-fil som Pdf-generatoren kan beytte seg av.
   */
  @Test
  @Ignore
  public void lagFilMedLandkodeVsLandnavn() throws IOException {
    TreeMap<String, String> kodeVsTekst = new TreeMap<>();
    Set<String> kodeSomHarAnnenTekst = new HashSet<>();
    Map<String, String> tekstIdVsTekst = lagTekstIdVsTekst(hentCdm43OversettelserJson());
    finnLandkoderOgLandnavn(SEDTYPER_A, tekstIdVsTekst, kodeVsTekst, kodeSomHarAnnenTekst);
    finnLandkoderOgLandnavn(SEDTYPER_DA, tekstIdVsTekst, kodeVsTekst, kodeSomHarAnnenTekst);
    finnLandkoderOgLandnavn(SEDTYPER_H, tekstIdVsTekst, kodeVsTekst, kodeSomHarAnnenTekst);
    finnLandkoderOgLandnavn(SEDTYPER_F, tekstIdVsTekst, kodeVsTekst, kodeSomHarAnnenTekst);
    finnLandkoderOgLandnavn(SEDTYPER_M, tekstIdVsTekst, kodeVsTekst, kodeSomHarAnnenTekst);
    finnLandkoderOgLandnavn(SEDTYPER_P, tekstIdVsTekst, kodeVsTekst, kodeSomHarAnnenTekst);
    finnLandkoderOgLandnavn(SEDTYPER_R, tekstIdVsTekst, kodeVsTekst, kodeSomHarAnnenTekst);
    finnLandkoderOgLandnavn(SEDTYPER_S, tekstIdVsTekst, kodeVsTekst, kodeSomHarAnnenTekst);
    finnLandkoderOgLandnavn(SEDTYPER_U, tekstIdVsTekst, kodeVsTekst, kodeSomHarAnnenTekst);
    finnLandkoderOgLandnavn(SEDTYPER_X, tekstIdVsTekst, kodeVsTekst, kodeSomHarAnnenTekst);
    skrivMapTilJsonFil(kodeVsTekst, MAPPE_SED_OVERSETTELSERv43 + "/land_kodeVsNavn.json");
    Assertions.assertThat(kodeVsTekst.size()).isEqualTo(284);
    Assertions.assertThat(kodeSomHarAnnenTekst.size()).isEqualTo(0);
  }

  @Test
  @Ignore
  public void lagFilMedValutakodeVsValutanavn() throws IOException {
    TreeMap<String, String> kodeVsTekst = new TreeMap<>();
    Set<String> koderSomHarAnnenTekst = new HashSet<>();
    Map<String, String> tekstIdVsTekst = lagTekstIdVsTekst(hentCdm43OversettelserJson());
    finnValutakoderOgValutanavn(SEDTYPER_M, tekstIdVsTekst, kodeVsTekst, koderSomHarAnnenTekst);
    finnValutakoderOgValutanavn(SEDTYPER_A, tekstIdVsTekst, kodeVsTekst, koderSomHarAnnenTekst);
    finnValutakoderOgValutanavn(SEDTYPER_DA, tekstIdVsTekst, kodeVsTekst, koderSomHarAnnenTekst);
    finnValutakoderOgValutanavn(SEDTYPER_F, tekstIdVsTekst, kodeVsTekst, koderSomHarAnnenTekst);
    finnValutakoderOgValutanavn(SEDTYPER_H, tekstIdVsTekst, kodeVsTekst, koderSomHarAnnenTekst);
    finnValutakoderOgValutanavn(SEDTYPER_P, tekstIdVsTekst, kodeVsTekst, koderSomHarAnnenTekst);
    finnValutakoderOgValutanavn(SEDTYPER_R, tekstIdVsTekst, kodeVsTekst, koderSomHarAnnenTekst);
    finnValutakoderOgValutanavn(SEDTYPER_S, tekstIdVsTekst, kodeVsTekst, koderSomHarAnnenTekst);
    finnValutakoderOgValutanavn(SEDTYPER_U, tekstIdVsTekst, kodeVsTekst, koderSomHarAnnenTekst);
    finnValutakoderOgValutanavn(SEDTYPER_X, tekstIdVsTekst, kodeVsTekst, koderSomHarAnnenTekst);
    skrivMapTilJsonFil(kodeVsTekst, MAPPE_SED_OVERSETTELSERv43 + "/valuta_kodeVsNavn.json");
    Assertions.assertThat(kodeVsTekst.size()).isEqualTo(159);
    Assertions.assertThat(koderSomHarAnnenTekst.size()).isEqualTo(2);
  }

  /**
   * Leser SED-fil fra CDM 4.3 og lager ei fil med indekser og norske ledetekster
   * samt norske navn på (nummererte) kodeverkskoder.
   */
  @Test
  @Ignore
  public void lagOversettelserForEnSedtype() throws IOException {
    Map<String, String> tekstIdVsTekst = lagTekstIdVsTekst(hentCdm43OversettelserJson());
    skrivOversettelserTilFil(List.of("H003"), tekstIdVsTekst);
  }

  /**
   * Leser SED-filer fra CDM 4.3 og lager ei fil for hver SED med indekser og norske ledetekster
   * samt norske navn på (nummererte) kodeverkskoder.
   */
  @Test
  @Ignore
  public void lagEiFilPrSedMedAclStiVsKodeVsKodenavn() throws IOException {
    Map<String, String> tekstIdVsTekst = lagTekstIdVsTekst(hentCdm43OversettelserJson());
    skrivOversettelserTilFil(SEDTYPER_A, tekstIdVsTekst);
    skrivOversettelserTilFil(SEDTYPER_DA, tekstIdVsTekst);
    skrivOversettelserTilFil(SEDTYPER_F, tekstIdVsTekst);
    skrivOversettelserTilFil(SEDTYPER_H, tekstIdVsTekst);
    skrivOversettelserTilFil(SEDTYPER_M, tekstIdVsTekst);
    skrivOversettelserTilFil(SEDTYPER_P, tekstIdVsTekst);
    skrivOversettelserTilFil(SEDTYPER_P3000, tekstIdVsTekst);
    skrivOversettelserTilFil(SEDTYPER_R, tekstIdVsTekst);
    skrivOversettelserTilFil(SEDTYPER_S, tekstIdVsTekst);
    skrivOversettelserTilFil(SEDTYPER_U, tekstIdVsTekst);
    skrivOversettelserTilFil(SEDTYPER_X, tekstIdVsTekst);
  }

  /**
   * Skriver ut i hvilke SEDer de ulike tekstkodene er brukt.
   */
  @Test
  @Ignore
  public void tekstIdVsTekstOgHvorAnvent() throws IOException {
    Map<String, List> alleIdVsSedtyper = new HashMap<>();
    Map<String, String> alleIdVsTekst = new HashMap<>();
    hentTekster(SEDTYPER_A, "A", alleIdVsTekst, alleIdVsSedtyper);
    hentTekster(SEDTYPER_DA, "DA", alleIdVsTekst, alleIdVsSedtyper);
    hentTekster(SEDTYPER_F, "F", alleIdVsTekst, alleIdVsSedtyper);
    hentTekster(SEDTYPER_H, "H", alleIdVsTekst, alleIdVsSedtyper);
    hentTekster(SEDTYPER_M, "M", alleIdVsTekst, alleIdVsSedtyper);
    hentTekster(SEDTYPER_P, "P", alleIdVsTekst, alleIdVsSedtyper);
    hentTekster(SEDTYPER_R, "R", alleIdVsTekst, alleIdVsSedtyper);
    hentTekster(SEDTYPER_S, "S", alleIdVsTekst, alleIdVsSedtyper);
    hentTekster(SEDTYPER_U, "U", alleIdVsTekst, alleIdVsSedtyper);
    hentTekster(SEDTYPER_X, "X", alleIdVsTekst, alleIdVsSedtyper);
    List<String> idVsTekstOgSedtype = lagIdVsTekstOgSedtype(alleIdVsTekst, alleIdVsSedtyper);
    FileUtils.writeStringToFile(
      new File(OVERSETTELSER_MAPPE + "/" + OVERSETTELSER_ID_TEKST_SEDTYPE_FIL),
      StringUtils.join(idVsTekstOgSedtype, "\n"), "UTF-8");
  }

  private void skrivOversettelserTilFil(List<String> sedTyper, Map<String, String> tekstIdVsTekst) {
    sedTyper.forEach(sedType -> {
      LinkedMap<String, Object> sedCdm43Flattened = getSedCdm43Flattened(sedType);
      LinkedMap<String, String> cdmStiVsIndeks = cdmReader.lagCdmStiVsIndeks(sedCdm43Flattened);
      Map<String, String> cdmStiVsTekst = cdmReader.lagCdmStiVsTekst(sedCdm43Flattened,
                                                                     tekstIdVsTekst);
      Map<String, String> cdmStiVsType = cdmReader.lagCdmStiVsType(sedCdm43Flattened);
      Map<String, TreeMap<String, String>> cdmStiVsKoderVsKodenavn = cdmReader
        .lagCdmStiVsKoderOgKodenavn(sedCdm43Flattened, tekstIdVsTekst);
      Map<String, String> cdmStiVsAclSti = lagCdmStiVsAclSti(sedCdm43Flattened);
      LinkedMap<String, MalLinjeDeler> cdmStiVsIndeksTekstTypeEnum = new LinkedMap<>();
      cdmStiVsIndeks.forEach((cdmSti, v) -> {
        MalLinjeDeler linje = MalLinjeDeler.builder()
          .index(v)
          .prompt(cdmStiVsTekst.get(cdmSti))
          .valuetype(cdmStiVsType.get(cdmSti))
          .codework(cdmStiVsKoderVsKodenavn.get(cdmSti)).build();
        cdmStiVsIndeksTekstTypeEnum.put(cdmSti, linje);
      });
      LinkedMap<String, MalLinjeDeler> aclStiVsIndeksOgTekstOgEnum = new LinkedMap<>();
      cdmStiVsIndeksTekstTypeEnum
        .forEach((cdmSti, v) -> aclStiVsIndeksOgTekstOgEnum.put(cdmStiVsAclSti.get(cdmSti)
                                                                  .replace(".value[0]", ""), v));
      String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
      skrivMapWithMapsTilJsonFil(aclStiVsIndeksOgTekstOgEnum
        , MAPPE_SED_OVERSETTELSERv43 + "/" + cdmSektor + "/" + sedType + "_oversettelser.json");
    });
  }

  private void skrivMapTilJsonFil(TreeMap<String, String> kodeVsTekstSortert, String filStiNavn)
    throws IOException {
    ObjectMapper om = new ObjectMapper();
    JsonNode kodeVsTekstNode = om.valueToTree(kodeVsTekstSortert);
    String landkoderJson = om.writerWithDefaultPrettyPrinter().writeValueAsString(kodeVsTekstNode);
    FileUtils.writeStringToFile(new File(filStiNavn), landkoderJson, "UTF-8");
  }

  private void skrivMapWithMapsTilJsonFil(LinkedMap<String, MalLinjeDeler> sedFlatstiVsVerdi,
                                          String filStiNavn) {
    try {
      ObjectMapper om = new ObjectMapper();
      JsonNode kodeVsTekstNode = om.valueToTree(sedFlatstiVsVerdi);
      String landkoderJson = om.writerWithDefaultPrettyPrinter()
        .writeValueAsString(kodeVsTekstNode);
      FileUtils.writeStringToFile(new File(filStiNavn), landkoderJson, "UTF-8");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Set<String> finnLandkoderOgLandnavn(List<String> sedTyper,
                                             Map<String, String> tekstIdVsTekst,
                                             TreeMap<String, String> kodeVsTekst,
                                             Set<String> kodeMedAnnenTekst) {
    sedTyper.forEach(sedType -> {
      System.out.print(sedType + ", ");
      Map<String, Object> cdmSedFlattened = getSedCdm43Flattened(sedType);
      Map<String, String> cdmStiVsEnumKode = new HashMap<>();
      Map<String, String> cdmStiVsEnumTekst = new HashMap<>();
      cdmSedFlattened.forEach((cdmSti, verdi) -> {
        cdmReader.leggTilCdmStiVsEnumKode(cdmSti, verdi.toString(), cdmStiVsEnumKode);
        cdmReader.leggTilCdmStiVsEnumTekst(cdmSti, verdi.toString(), tekstIdVsTekst,
                                           cdmStiVsEnumTekst);
      });
      cdmStiVsEnumKode.forEach((cdmSti, enumKode) -> {
        if (StringUtils.isAlpha(enumKode) && enumKode.length() != 3) {
          lagEnumKodeVsEnumTekst(cdmSti, enumKode, cdmStiVsEnumTekst, kodeVsTekst,
                                 kodeMedAnnenTekst);
        }
      });
    });
    System.out.print("\n");
    return kodeMedAnnenTekst;
  }

  public Set<String> finnValutakoderOgValutanavn(List<String> sedTyper,
                                                 Map<String, String> tekstIdVsTekst,
                                                 TreeMap<String, String> kodeVsTekst,
                                                 Set<String> kodeMedAnnenTekst) {
    sedTyper.forEach(sedType -> {
      System.out.print(sedType + ", ");
      Map<String, Object> cdmSedFlattened = getSedCdm43Flattened(sedType);
      Map<String, String> cdmStiVsEnumKode = new HashMap<>();
      Map<String, String> cdmStiVsEnumTekst = new HashMap<>();
      cdmSedFlattened.forEach((cdmSti, verdi) -> {
        cdmReader.leggTilCdmStiVsEnumKode(cdmSti, verdi.toString(), cdmStiVsEnumKode);
        cdmReader.leggTilCdmStiVsEnumTekst(cdmSti, verdi.toString(), tekstIdVsTekst,
                                           cdmStiVsEnumTekst);
      });
      cdmStiVsEnumKode.forEach((cdmSti, enumKode) -> {
        if (StringUtils.isAlpha(enumKode) && enumKode.length() == 3) {
          lagEnumKodeVsEnumTekst(cdmSti, enumKode, cdmStiVsEnumTekst, kodeVsTekst,
                                 kodeMedAnnenTekst);
        }
      });
    });
    System.out.print("\n");
    return kodeMedAnnenTekst;
  }

  private static void lagEnumKodeVsEnumTekst(String k, String v,
                                             Map<String, String> cdmStiVsEnumTekst,
                                             TreeMap<String, String> kodeVsTekst,
                                             Set<String> kodeMedAnnenTekst) {
    if (kodeVsTekst.get(v) == null || kodeVsTekst.get(v).equals(cdmStiVsEnumTekst.get(k))) {
      kodeVsTekst.put(v, cdmStiVsEnumTekst.get(k));
    } else {
      kodeMedAnnenTekst.add(v + ": " + cdmStiVsEnumTekst.get(k));
    }
  }

  private LinkedMap<String, Object> getSedCdm43Flattened(String sedType) {
    String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
    String cdmSedFilnavn = MAPPE_CDM_V43_META + "/" + cdmSektor + "/" + sedType + ".json";
    try {
      String cdmSedJson = FileUtils.readFileToString(new File(cdmSedFilnavn), UTF_8);
      // Leses som tekst for å beholde (indeks)rekkefølgen
      String cdmSedFlattenedJson = JsonFlattener.flatten(cdmSedJson);
      LinkedMap<String, Object> cdmStiVsVerdi = new LinkedMap<>();
      Arrays.stream(cdmSedFlattenedJson.
                      replace("{", "")
                      .replace("}", "")
                      .split(","))
        .forEach(linje -> {
          String cdmSti = StringUtils.substringBefore(linje, ":").replace("\"", "");
          String verdi = StringUtils.substringAfter(linje, ":").replace("\"", "");
          cdmStiVsVerdi.put(cdmSti, verdi);
        });
      return cdmStiVsVerdi;
    } catch (IOException e) {
      System.out.println("Error - Fil: " + cdmSedFilnavn + ". " + e.getLocalizedMessage());
      throw new RuntimeException(e);
    }
  }

  private void hentTekster(List<String> sedtyper, String cdmSektor
    , Map<String, String> alleIdVsTekst, Map<String, List> alleIdVsSedtyper) {
    sedtyper.forEach(s -> {
      Map<String, String> uoversatteIdVsTekstPrSed = henteTeksterForSed(s, cdmSektor);
      leggTilTekster(alleIdVsTekst, uoversatteIdVsTekstPrSed);
      leggTilSedtypen(alleIdVsSedtyper, s, uoversatteIdVsTekstPrSed);
    });
  }

  @NotNull
  private List<String> lagIdVsTekstOgSedtype(Map<String, String> alleIdVsTekst,
                                             Map<String, List> alleIdVsSedtyper) {
    List<String> idVsTekstOgSedtyper = new ArrayList<>();
    alleIdVsTekst.keySet().forEach(i -> {
      String sedtyper = StringUtils.join(alleIdVsSedtyper.get(i), " ");
      idVsTekstOgSedtyper.add(i + " : " + alleIdVsTekst.get(i));
      idVsTekstOgSedtyper.add(i + " :-> " + sedtyper);
    });
    return idVsTekstOgSedtyper.stream().sorted().toList();
  }

  private void leggTilSedtypen(Map<String, List> alleIdVsSedtyper, String s,
                               Map<String, String> uoversatteIdVsTekstPrSed) {
    uoversatteIdVsTekstPrSed.keySet().forEach(k -> {
      if (!alleIdVsSedtyper.containsKey(k)) {
        alleIdVsSedtyper.put(k, new ArrayList());
      }
      if (!alleIdVsSedtyper.get(k).contains(s)) {
        alleIdVsSedtyper.get(k).add(s);
      }
    });
  }

  private void leggTilTekster(Map<String, String> alleIdVsTekst,
                              Map<String, String> uoversatteIdVsTekstPrSed) {
    uoversatteIdVsTekstPrSed.forEach((k, v) -> alleIdVsTekst.put(k, v));
  }

  private Map<String, String> henteTeksterForSed(String sedType, String cdmSektor) {
    try {
      List<String> idListeSed = hentTekstIderFraSed(sedType, cdmSektor);
      String cdm43OversettelserJson = hentCdm43OversettelserJson();
      Map<String, String> uoversatteIdVsTekst = lagTekstIdVsTekst(cdm43OversettelserJson);
      Map<String, String> idVsTekst = lagIdVsTekst(idListeSed, uoversatteIdVsTekst);
      return idVsTekst;
    } catch (Exception e) {
      System.out.println("Error: " + sedType + " " + e.getMessage());
      return null;
    }
  }

  @NotNull
  private Map<String, String> lagTekstIdVsTekst(String cdm43OversettelserJson)
    throws JsonProcessingException {
    Iterator<Entry<String, JsonNode>> ii = new ObjectMapper().readTree(cdm43OversettelserJson)
      .at("/forms").fields();
    Map<String, String> alleIdVsTekst = new HashMap<>();
    while (ii.hasNext()) {
      Entry<String, JsonNode> nn = ii.next();
      String k = nn.getKey();
      String v = nn.getValue().textValue();
      if (k.startsWith("4.3")) {
        alleIdVsTekst.put(k, v);
      }
    }
    return alleIdVsTekst;
  }

  @NotNull
  private Map<String, String> lagIdVsTekst(List<String> idListeSed,
                                           Map<String, String> alleIdVsTekst) {
    Map<String, String> idVsTekstSed = new HashMap<>();
    idListeSed.forEach(i -> {
      String tekstId = i.replaceFirst("forms\\.", "");
      if (alleIdVsTekst.get(tekstId) != null) {
        idVsTekstSed.put(tekstId, alleIdVsTekst.get(tekstId));
      }
    });
    return idVsTekstSed;
  }

  private String hentCdm43OversettelserJson() throws IOException {
    String cdmTranslationsFilnavn = OVERSETTELSER_MAPPE + "/" + OVERSETTELSER_CDM_FIL;
    String cdm43translationJson = FileUtils.readFileToString(new File(cdmTranslationsFilnavn),
                                                             UTF_8);
    return cdm43translationJson;
  }

  private List<String> hentTekstIderFraSed(String sedType, String cdmSektor) throws IOException {
    String cdmMeta43MalFilnavn = MAPPE_CDM_V43_META + "/" + cdmSektor + "/" + sedType + ".json";
    String cdm43SedJson = FileUtils.readFileToString(new File(cdmMeta43MalFilnavn), UTF_8);
    Map<String, Object> cdm43SedFlattened = JsonFlattener.flattenAsMap(cdm43SedJson);

    List<String> tekstIdListe = cdmReader.lesTekstIderForEnSed(cdm43SedFlattened);
    return tekstIdListe;
  }

  public Map<String, String> lagCdmStiVsAclSti(Map<String, Object> cdmSedFlattened) {
    Map<String, String> cdmPathVsKey = new HashMap<>();
    Map<String, String> cdmPathVsType = new HashMap<>();
    Map<String, String> cdmFlatstiVsVsAclFlatsti = new HashMap<>();
    cdmSedFlattened.keySet().stream().sorted().forEach(k -> {
      if (k.contains("context.key")) {
        String path = k.replace(".context.key", "");
        cdmPathVsKey.put(path, cdmSedFlattened.get(k).toString());
      }
      if (k.contains("context.type")) {
        String path = k.replace(".context.type", "");
        String type = cdmSedFlattened.get(k).toString();
        cdmPathVsType.put(path, type);
      }
      if (k.endsWith("context.inputs.sedTitle")) {
        String sedType = cdmSedFlattened.get(k.replace(".sedTitle", ".sedName")).toString();
        cdmFlatstiVsVsAclFlatsti.put("sedTitle", sedType + ".sedTitle");
      }
    });
    cdmPathVsType.keySet()
      .forEach(
        (k) -> {
          String[] cdmPathLevels = k.split("\\.");
          String[] cdmPathRaw = {""};
          String[] aclPathBuild = {""};
          asList(cdmPathLevels).forEach(child -> {
            cdmPathRaw[0] += "." + child;
            String cdmPath = cdmPathRaw[0].replaceFirst(".", "");
            String type = cdmPathVsType.get(cdmPath);
            if ("_rinaArray".equals(type)
              || "_uxMultiCheckbox".equals(type)
              || "_uxHiddenArrayContainer".equals(type)
              || "_uxMultiSelect".equals(type)) {
              aclPathBuild[0] += "." + cdmPathVsKey.get(cdmPath) + "[0]";
            } else if (cdmPathVsKey.get(cdmPath) == null) {
              // unnlat å sette inn null i stien
            } else {
              aclPathBuild[0] += "." + cdmPathVsKey.get(cdmPath);
            }
          });
          cdmFlatstiVsVsAclFlatsti.put(k, aclPathBuild[0].replaceFirst(".", ""));
        }
      );
    return cdmFlatstiVsVsAclFlatsti;
  }
}