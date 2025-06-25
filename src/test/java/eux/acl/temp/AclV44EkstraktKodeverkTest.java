package eux.acl.temp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.json.flattener.JsonFlattener;
import no.nav.eux.acl.v43.CdmReader;
import no.nav.eux.convert.eused.pdf.Kodesett;
import no.nav.eux.convert.eused.pdf.KodesettKode;
import no.nav.eux.convert.eused.pdf.MalLinjeDeler;
import org.apache.commons.collections15.map.LinkedMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

import static java.util.Arrays.asList;
import static no.nav.eux.acl.v43.ACLv43Test.*;
import static no.nav.eux.acl.v43.AclV43TestdataTest.MAPPE_CDM_V43_META;
import static no.nav.eux.acl.v43.AclV43TestdataTest.OVERSETTELSER_ID_TEKST_SEDTYPE_FIL;


public class AclV44EkstraktKodeverkTest {

    private static final String CDM_MAPPE = "src/test/resources/cdm-v44-meta";
    private static final String OVERSETTELSER_MAPPE = "src/test/resources/cdm-translations";
    private static final String OVERSETTELSER_FIL = "no.json";
    private static final String OVERSETTELSER_VERSJON = "4.3";
    private static final String KODEVERK_RESULTAT_MAPPE = "src/main/resources/sedoversettelser/v44";
    private static final String KODEVERK_FIL_POSTFIX = "_kodeverkOversikt.json";
    private static final String UTF_8 = StandardCharsets.UTF_8.name();
    private final CdmReader cdmReader = new CdmReader();

    /**
     * Trekker ut kodeverk fra CDM og skriver til fil.
     * Skriver ut Eu-navn på kodesett samt den enkelte eu-koden sammen med norsk beskrivelse av den.
     * I tillegge etterlates tomme verdier for nav-navn på kodesett og for nav-kodene i kodesettet.
     * Disse kan fylles inn i ettertid og så bli brukt til å konverteree mellom domener.
     */

    @Test
    @Ignore
    public void trekkUtKodeverkForEnSedtype() throws IOException {
        Map<String, String> tekstIdVsTekst = lagTekstIdVsTekst(hentOversettelserJson());
        skrivKodeverkTilFil(List.of("F001"), tekstIdVsTekst);
    }

    @Test
    @Ignore
    public void lagKodeverkForACLForEnSedtype() throws IOException {
        List<String> kodesettMappingLinjer = new ArrayList<>();
        String sedType = "F001";
        String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
        String kodeverkFilnavn = KODEVERK_RESULTAT_MAPPE + "/" + cdmSektor + "/" + sedType + "_kodeverkOppdatert.json";
        try {
            String kodeverkJson = FileUtils.readFileToString(new File(kodeverkFilnavn), UTF_8);
            List<Kodesett> kodesettListe = new ObjectMapper().readValue(kodeverkJson, new TypeReference<>() {
            });
            kodesettListe.forEach(k -> {
                String kodesettMappingLinje = "." + k.kodesettNavnINav() + "=" + k.kodesettNavnRina();
                kodesettMappingLinjer.add(kodesettMappingLinje);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ObjectMapper om = new ObjectMapper();
            String kodeverkMappingFilnavn = KODEVERK_RESULTAT_MAPPE + "/" + cdmSektor + "/" + sedType + "_kodeverkMappingACL.json";
            FileUtils.writeStringToFile(new File(kodeverkMappingFilnavn), kodesettMappingLinjer.toString(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String hentOversettelserJson() throws IOException {
        String cdmOversettelserFilnavn = OVERSETTELSER_MAPPE + "/" + OVERSETTELSER_FIL;
        return FileUtils.readFileToString(new File(cdmOversettelserFilnavn), UTF_8);
    }
    @NotNull
    private Map<String, String> lagTekstIdVsTekst(String cdmOversettelserJson) throws JsonProcessingException {
        Iterator<Entry<String, JsonNode>> linje = new ObjectMapper().readTree(cdmOversettelserJson)
                .at("/forms").fields();
        Map<String, String> idVsTekst = new HashMap<>();
        while (linje.hasNext()) {
            Entry<String, JsonNode> nesteLinje = linje.next();
            String k = nesteLinje.getKey();
            String v = nesteLinje.getValue().textValue();
            if (k.startsWith(OVERSETTELSER_VERSJON)) {
                idVsTekst.put(k, v);
            }
        }
        return idVsTekst;
    }

    private void skrivKodeverkTilFil(List<String> sedTyper, Map<String, String> tekstIdVsTekst) {
        Set<String> unikeKodesett = new HashSet<>();
        sedTyper.forEach(sedType -> {
            LinkedMap<String, Object> sedCdmFlattened = getSedCdmFlattened(sedType);
            Map<String, String> cdmStiVsEnumNavn = cdmReader.lagCdmStiVsEnumNavn(sedCdmFlattened);
            LinkedMap<String, String> cdmStiVsIndeks = cdmReader.lagCdmStiVsIndeks(sedCdmFlattened);
            Map<String, TreeMap<String, String>> cdmStiVsKoderOgKodenavn = cdmReader
                    .lagCdmStiVsKoderOgKodenavn(sedCdmFlattened, tekstIdVsTekst);
            List<Kodesett> kodesettListe = new ArrayList<>();
            cdmStiVsIndeks.forEach((cdmSti, v) -> {
                if (cdmStiVsEnumNavn.containsKey(cdmSti) && !unikeKodesett.contains(cdmStiVsEnumNavn.get(cdmSti))) {
                    List<KodesettKode> kodesettKoder = new ArrayList<>();
                    if (cdmStiVsKoderOgKodenavn.get(cdmSti) != null) {
                        cdmStiVsKoderOgKodenavn.get(cdmSti).forEach((euKode, navNavn) -> {
                            KodesettKode kodesettKode = new KodesettKode(euKode, navNavn, "");
                            kodesettKoder.add(kodesettKode);
                        });
                    }
                    Kodesett kodesett = new Kodesett(cdmStiVsEnumNavn.get(cdmSti), "", kodesettKoder);
                    kodesettListe.add(kodesett);
                    unikeKodesett.add(cdmStiVsEnumNavn.get(cdmSti));
                }
            });
            String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
            skrivKodeverkFil(kodesettListe
                    , KODEVERK_RESULTAT_MAPPE + "/" + cdmSektor + "/" + sedType + KODEVERK_FIL_POSTFIX);
        });
    }

    private LinkedMap<String, Object> getSedCdmFlattened(String sedType) {
        String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
        String cdmSedFilnavn = CDM_MAPPE + "/" + cdmSektor + "/" + sedType + ".json";
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
        skrivMapTilJsonFil(kodeVsTekst, KODEVERK_RESULTAT_MAPPE + "/land_kodeVsNavn.json");
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
        skrivMapTilJsonFil(kodeVsTekst, KODEVERK_RESULTAT_MAPPE + "/valuta_kodeVsNavn.json");
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
        skrivOversettelserTilFil(List.of("F001"), tekstIdVsTekst);
    }

    @Test
    @Ignore
    public void lagKodeverkForEnSedtype() throws IOException {
        Map<String, String> tekstIdVsTekst = lagTekstIdVsTekst(hentCdm43OversettelserJson());
        skrivKodeverkTilFil(List.of("F001"), tekstIdVsTekst);
    }

    /**
     * Leser SED-filer fra CDM 4.3 og lager ei fil for hver SED med indekser og norske ledetekster
     * samt norske navn på (nummererte) kodeverkskoder.
     */
    @Test
    @Ignore
    public void lagEiFilPrSedMedAclStiVsKodeVsKodenavn() throws IOException {
        Map<String, String> tekstIdVsTekst = lagTekstIdVsTekst(hentCdm43OversettelserJson());
//    skrivOversettelserTilFil(SEDTYPER_A, tekstIdVsTekst);
//    skrivOversettelserTilFil(SEDTYPER_DA, tekstIdVsTekst);
        skrivOversettelserTilFil(List.of("F001"), tekstIdVsTekst);
//    skrivOversettelserTilFil(SEDTYPER_H, tekstIdVsTekst);
//    skrivOversettelserTilFil(SEDTYPER_M, tekstIdVsTekst);
//    skrivOversettelserTilFil(SEDTYPER_P, tekstIdVsTekst);
//    skrivOversettelserTilFil(SEDTYPER_P3000, tekstIdVsTekst);
//    skrivOversettelserTilFil(SEDTYPER_R, tekstIdVsTekst);
//    skrivOversettelserTilFil(SEDTYPER_S, tekstIdVsTekst);
//    skrivOversettelserTilFil(SEDTYPER_U, tekstIdVsTekst);
//    skrivOversettelserTilFil(SEDTYPER_X, tekstIdVsTekst);
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
            LinkedMap<String, Object> sedCdmFlattened = getSedCdmFlattened(sedType);
            LinkedMap<String, String> cdmStiVsIndeks = cdmReader.lagCdmStiVsIndeks(sedCdmFlattened);
            Map<String, String> cdmStiVsTekst = cdmReader.lagCdmStiVsTekst(sedCdmFlattened,
                    tekstIdVsTekst);
            Map<String, String> cdmStiVsType = cdmReader.lagCdmStiVsType(sedCdmFlattened);
            Map<String, String> cdmStiVsEnumNavn = cdmReader.lagCdmStiVsEnumNavn(sedCdmFlattened);
            Map<String, TreeMap<String, String>> cdmStiVsKoderVsKodenavn = cdmReader
                    .lagCdmStiVsKoderOgKodenavn(sedCdmFlattened, tekstIdVsTekst);
            Map<String, String> cdmStiVsAclSti = lagCdmStiVsAclSti(sedCdmFlattened);
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
                    , KODEVERK_RESULTAT_MAPPE + "/" + cdmSektor + "/" + sedType + "_oversettelser.json");
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

    private void skrivKodeverkFil(List<Kodesett> kodesettListe, String filStiNavn) {
        try {
            ObjectMapper om = new ObjectMapper();
            String kodeVsTekst = om.writeValueAsString(kodesettListe);
            JsonNode kodeVsTekstNode = om.valueToTree(kodesettListe);
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
            Map<String, Object> cdmSedFlattened = getSedCdmFlattened(sedType);
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
            Map<String, Object> cdmSedFlattened = getSedCdmFlattened(sedType);
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
        String cdmTranslationsFilnavn = OVERSETTELSER_MAPPE + "/" + OVERSETTELSER_FIL;
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