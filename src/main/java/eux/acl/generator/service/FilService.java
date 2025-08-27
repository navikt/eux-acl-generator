package eux.acl.generator.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.wnameless.json.flattener.JsonFlattener;
import eux.acl.generator.modell.EnumkodeFundament;
import eux.acl.generator.modell.EuSedFundament;
import eux.acl.generator.modell.pdf.PdfSedElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class FilService {
    public static final String V43 = "4.3";
    public static final String V44 = "4.4";
    public static final String MAPPE_TARGET_FUNDAMENT_KODEVERK = "target/fundament/kodeverk";
    public static final String MAPPE_TARGET_FUNDAMENT_SEDMALER = "target/fundament/sedmaler";
    public static final String MAPPE_TARGET_DIFF_KODEVERK = "target/diff/kodeverk";
    public static final String MAPPE_TARGET_PDF_SED_MAL = "target/sedmaler/pdf";
    public static final String MAPPE_TARGET_CDM_DIFF = "target/diff/aclMaler";
    public static final String MAPPE_TARGET_RESULTAT_KODEVERK = "target/resultater/kodeverk";
    public static final String MAPPE_TARGET_RESULTAT_ACL = "target/resultater/aclmaler";
    public static final String MAPPE_CDM_META = "src/main/resources/input/cdm-meta";
    public static final String MAPPE_CDM_LOGISK_MODELL = "src/main/resources/input/cdm-logisk-modell";
    public static final String MAPPE_OVERSETTELSER = "src/main/resources/input/cdm-oversettelser";
    public static final String MAPPE_EKSISTERENDE_ACL = "src/main/resources/acl/maler";
    public static final String MAPPE_FUNDAMENT_MALER = "src/main/resources/fundament/maler";
    public static final String MAPPE_FUNDAMENT_KODEVERK = "src/main/resources/fundament/kodeverk";
    public static final String MAPPE_DIFF_KODEVERK = "src/main/resources/diff/kodeverk";
    public static final String MAPPE_DIFF_ACL_MALER = "src/main/resources/diff/aclMaler";
    public static final String MAPPE_RESULTAT_ACL_MALER_FLAT = "src/main/resources/resultater/aclMalerFlattened";
    public static final String FIL_MED_ENDRINGER_STIER = "endringer.txt";
    public static final String FIL_MED_UENDREDE_STIER = "uendret.txt";
    public static final String FIL_MED_UTGAATTE_STIER = "utgaatt.txt";
    public static final String FIL_UENDREDE_ACL_STIER = "uendret_acl.txt";
    public static final String FIL_UTGAATTE_ACL_STIER = "utgaatt_acl.txt";
    public static final String FIL_NY_ACL_MAL_STIER = "nyAclMal_basis.txt";
    public static final String FIL_ENDRET_ENUM_STIER = "endret_enum.txt";
    public static final String FIL_CDM_LOGISK_MODELL = "CDM_LM.xml";
    public static final String FIL_OVERSETTELSER = "no.json";
    public static final String FIL_ACL_MALER_I_SEKTOREN_FLATTENED = "aclMalerFlattened.txt";
    public static final String MANUELLE_JUSTERINGER = "manuelleJusteringer";

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

    public static final List<String> SEDTYPER_R = List.of("R001", "R002",
            "R003", "R004", "R005", "R006", "R008", "R009");
    public static final List<String> SEDTYPER_S = List.of("S001", "S003", "S005", "S040"
            , "S041", "S044", "S045", "S046", "S047", "S048", "S055", "S056", "S057");
    public static final List<String> SEDTYPER_U = List.of("U001", "U001CB", "U002", "U003"
            , "U004", "U005", "U006", "U007", "U008", "U009", "U010", "U011", "U012", "U013", "U014"
            , "U015", "U016", "U017", "U018", "U019", "U020", "U021", "U023", "U024", "U025", "U026"
            , "U027", "U029");
    public static final List<String> SEDTYPER_X = List.of("X001", "X002", "X003", "X004"
            , "X005", "X006", "X007", "X008", "X009", "X010", "X011", "X012", "X013", "X050", "X100");

    // les resources/meta
    public Map<String, Object> lesCdmMetaFilForEnSed(String sedType, String cdmVersjon) {
        String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String cdmMetaSedFilSti = MAPPE_CDM_META + "/" + cdmVersjonTilpasset + "/" + cdmSektor + "/" + sedType + ".json";
        try {
            String cdmMetaSedJson = FileUtils.readFileToString(new File(cdmMetaSedFilSti), UTF_8);
            Map<String, Object> cdmMalFlattened = JsonFlattener.flattenAsMap(cdmMetaSedJson.replace("\ufeff", ""));
            String cdmMetaSedGVer = cdmMalFlattened.get("context.inputs.sedGVer").toString();
            String cdmMetaSedVer = cdmMalFlattened.get("context.inputs.sedVer").toString();
            String cdmMetaSedType = cdmMalFlattened.get("context.inputs.sedName").toString();
            if (!sedType.equals(cdmMetaSedType)) {
                log.error("Sedtype mismatch. Oppgitt sedType={}, CdmMeta sedType={} ", sedType, cdmMetaSedType);
                throw new RuntimeException("Sedtype mismatch. " + sedType + " vs " + cdmMetaSedType);
            }
            if (!cdmVersjon.equals(cdmMetaSedGVer + "." + cdmMetaSedVer)) {
                log.error("Cdmversjon mismatch. Oppgitt versjon={}, Cdm-innhold versjon={} ", sedType, cdmMetaSedType);
                throw new RuntimeException("Cdm-versjon mismatch. " + cdmVersjon + " vs " + "v" + cdmMetaSedGVer + cdmMetaSedVer);
            }
            return cdmMalFlattened;
        } catch (JsonParseException e) {
            log.error("Json-feil i fil. fil={}, cdmVersjon={}", cdmMetaSedFilSti, cdmVersjon);
        } catch (IOException e) {
            log.error("Fil mangler. fil={}, cdmVersjon={}", cdmMetaSedFilSti, cdmVersjon);
        }
        return null;
    }

    // til target
    public void skrivEuSedMalFundamentTilFil(TreeMap<String, EuSedFundament> euSedFundament, String sedType, String cdmVersjon) {
        if (euSedFundament == null) {
            return;
        }
        String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String cdmSedFilSti = MAPPE_TARGET_FUNDAMENT_SEDMALER + "/" + cdmVersjonTilpasset + "/" + cdmSektor + "/" + sedType + ".json";
        try {
            String euSedJson = new JsonMapper().convertValue(euSedFundament, JsonNode.class).toPrettyString();
            FileUtils.writeStringToFile(new File(cdmSedFilSti), euSedJson, "UTF-8");
            log.info("{} - euSedFundament opprettet", sedType);
        } catch (JsonProcessingException e) {
            log.error("kunne ikke konvertere euSedFundament til Json. sedType={} ", sedType);
        } catch (IOException e) {
            log.error("Kunne ikke skrive til fil. fil={}", cdmSedFilSti);
        }
    }

    public Map<String, Object> lesAclMal(String sedType, String cdmVersjon) {
        String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String aclFilnavn = sedType + "_v" + cdmVersjon + ".json";
        String aclMalFilSti = MAPPE_EKSISTERENDE_ACL + "/" + cdmVersjonTilpasset + "/" + aclSektor(cdmSektor) + "/" + aclFilnavn;
        try {
            String aclmalJson = FileUtils.readFileToString(new File(aclMalFilSti), UTF_8);
            return JsonFlattener.flattenAsMap(aclmalJson);
        } catch (IOException e) {
            log.error("Kunne ikke lese fil. fil={}", aclMalFilSti);
        }
        return null;
    }

    public void skrivDiffTilFil(List<String> cdmSedStierListe, String sedType, String cdmVersjonNy, String filnavn) {
        String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
        String cdmVersjonTilpasset = "v" + cdmVersjonNy.replace(".", "");
        String filnavnSti = MAPPE_TARGET_CDM_DIFF + "/" + cdmVersjonTilpasset + "/" + cdmSektor + "/" + sedType + "/" + filnavn;
        String cdmSedStierFlattened = StringUtils.join(cdmSedStierListe.stream().sorted().toList(), "\n");
        try {
            FileUtils.writeStringToFile(new File(filnavnSti), cdmSedStierFlattened, "UTF-8");
        } catch (IOException e) {
            log.error("Kunne ikke skrive til fil. fil={}", filnavnSti);
        }
    }

    public void skrivAclMalBasisPrSektorTilFil(List<String> linjer, String sektor, String cdmVersjon) {
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filnavnSti = MAPPE_TARGET_CDM_DIFF + "/" + cdmVersjonTilpasset + "/" + sektor + "_" + FIL_ACL_MALER_I_SEKTOREN_FLATTENED;
        String cdmSedStierFlattened = StringUtils.join(linjer.stream().sorted().toList(), "\n");
        try {
            FileUtils.writeStringToFile(new File(filnavnSti), cdmSedStierFlattened, "UTF-8");
        } catch (IOException e) {
            log.error("Kunne ikke skrive til fil. fil={}", filnavnSti);
        }
    }

    public void skrivAclMalTilFil(String aclSedJson, String sedType, String cdmVersjon) {
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filnavnSti = MAPPE_TARGET_RESULTAT_ACL + "/" + cdmVersjonTilpasset + "/" + sedType + ".json";
        try {
            FileUtils.writeStringToFile(new File(filnavnSti), aclSedJson, "UTF-8");
        } catch (IOException e) {
            log.error("Kunne ikke skrive til fil. fil={}", filnavnSti);
            //            throw new RuntimeException(e);
        }
    }

    public List<String> lesAclMalBasisForEnSektorTilFil(String sektor, String cdmVersjon) {
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filnavnSti = MAPPE_RESULTAT_ACL_MALER_FLAT + "/" + cdmVersjonTilpasset + "/" + sektor +
                "_" + FIL_ACL_MALER_I_SEKTOREN_FLATTENED;
        try {
            String aclMalerForEnSektor = FileUtils.readFileToString(new File(filnavnSti), UTF_8);
            return Arrays.asList(aclMalerForEnSektor.split("\n"));
        } catch (IOException e) {
            log.error("Kunne ikke lese fil. fil={}", filnavnSti);
        }
        return null;
    }

    public void skrivKodeverksfundamentTilFil(String enumtype
            , TreeMap<Integer, EnumkodeFundament> enumInnhold
            , String sedType
            , String cdmVersjon) {
        String kodeverksfilJson = new JsonMapper().convertValue(enumInnhold, JsonNode.class).toPrettyString();
        String sektor = StringUtils.splitByCharacterType(sedType)[0];
        String filPrSektor = sektor + "/" + enumtype + ".json";
        String filerSamlet = enumtype + ".json";
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filnavnStiPrSektor = MAPPE_TARGET_FUNDAMENT_KODEVERK + "/" + cdmVersjonTilpasset + "/" + filPrSektor;
        String filnavnStiFells = MAPPE_TARGET_FUNDAMENT_KODEVERK + "/" + cdmVersjonTilpasset + "/" + filerSamlet;
        try {
            FileUtils.writeStringToFile(new File(filnavnStiPrSektor), kodeverksfilJson, "UTF-8");
        } catch (IOException e) {
            log.error("Kunne ikke skrive til fil. fil={}", filnavnStiPrSektor);
        }
        try {
            FileUtils.writeStringToFile(new File(filnavnStiFells), kodeverksfilJson, "UTF-8");
        } catch (IOException e) {
            log.error("Kunne ikke skrive til fil. fil={}", filnavnStiFells);
        }
    }

    public void skrivNyeKodeverksfundamentTilFil(
            TreeMap<Integer, EnumkodeFundament> enumInnhold
            , String enumNavn
            , String cdmVersjon) {
        String kodeverksfilJson = new JsonMapper().convertValue(enumInnhold, JsonNode.class).toPrettyString();
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String stiNavnEnumFil = MAPPE_TARGET_DIFF_KODEVERK + "/" + cdmVersjonTilpasset + "/nyeOgEndrede/" + enumNavn + ".json";
        try {
            FileUtils.writeStringToFile(new File(stiNavnEnumFil), kodeverksfilJson, "UTF-8");
        } catch (IOException e) {
            log.error("Kunne ikke skrive til fil. fil={}", stiNavnEnumFil);
        }
    }

    public void skrivKodeverkOppsummeringTilEiFil(List<String> diffKodeverkOppsummeringLinjer, String filnavn, String cdmVersjon) {
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filStiNavn = MAPPE_TARGET_DIFF_KODEVERK + "/" + cdmVersjonTilpasset + "/nyeOgEndrede/" + filnavn + ".txt";
        String tekstMedDiff = StringUtils.join(diffKodeverkOppsummeringLinjer, "\n");
        try {
            FileUtils.writeStringToFile(new File(filStiNavn), tekstMedDiff, "UTF-8");
        } catch (IOException e) {
            log.error("Kunne ikke skrive til fil. fil={}", filStiNavn);
        }
    }

    public void skrivPdfSedMalTilFil(TreeMap<String, PdfSedElement> euSedStiVsPdfSedElement, String sedType, String cdmVersjon) {
        String sektor = StringUtils.splitByCharacterType(sedType)[0];
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filnavnSti = MAPPE_TARGET_PDF_SED_MAL + "/"
                + cdmVersjonTilpasset + "/" + sektor + "/" + sedType + "_oversettelser.json";
        String pdfSedMalJson = new JsonMapper().convertValue(euSedStiVsPdfSedElement, JsonNode.class).toPrettyString();
        try {
            FileUtils.writeStringToFile(new File(filnavnSti), pdfSedMalJson, "UTF-8");
            log.info("Pdf-mal laget for " + sedType);
        } catch (IOException e) {
            log.error("Kunne ikke skrive til fil. fil={}", filnavnSti);
        }
    }

    public String lesCdmLogiskModellXmlFil(String cdmVersjon) {
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filsti = MAPPE_CDM_LOGISK_MODELL + "/" + cdmVersjonTilpasset + "/" + FIL_CDM_LOGISK_MODELL;
        try {
            return FileUtils.readFileToString(new File(filsti), UTF_8);
        } catch (IOException e) {
            log.error("Fant ikke fil med logisk modell. fil={}", filsti);
            //            throw new RuntimeException(e);
        }
        return null;
    }

    public String lesOversettelsesfil(String cdmVersjon) {
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filsti = MAPPE_OVERSETTELSER + "/" + cdmVersjonTilpasset + "/" + FIL_OVERSETTELSER;
        Map<String, String> tekstIdVsTekst = new HashMap<>();
        try {
            return FileUtils.readFileToString(new File(filsti), UTF_8);
        } catch (IOException e) {
            log.error("Fant ikke fil med oversettelser. fil={}", filsti);
            //            throw new RuntimeException(e);
        }
        return null;
    }

    public TreeMap<String, EuSedFundament> lesEuSedFundamentFil(String sedType, String cdmVersjon) {
        String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filsti = MAPPE_FUNDAMENT_MALER + "/" + cdmVersjonTilpasset + "/" + cdmSektor + "/" + sedType + ".json";
        try {
            String euSedFundamentJson = FileUtils.readFileToString(new File(filsti), UTF_8);
            return new JsonMapper().readValue(euSedFundamentJson, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Kunne ikke konvertere json-fil til Java-objekt. filsti={}", filsti);
        } catch (IOException e) {
            log.error("Fant ikke fil. filsti={}", filsti);
            //            throw new RuntimeException(e);
        }
        return null;
    }

    public List<String> lesEndredeOgNyeEnumNavn(String cdmVersjon) {
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filsti = MAPPE_DIFF_KODEVERK + "/" + cdmVersjonTilpasset;
        File[] files = new File(filsti).listFiles();
        if (files != null) {
            return Arrays.stream(files)
                    .filter(File::isFile)
                    .map(f -> f.getName().replace(".json", ""))
                    .toList();
        }
        log.error("Fant ikke mappe, eller mappa var tom. filsti={}", filsti);
        return null;
    }

    public List<String> lesEnumFundamentFilnavnene(String cdmVersjon) {
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filsti = MAPPE_FUNDAMENT_KODEVERK + "/" + cdmVersjonTilpasset;
        File[] files = new File(filsti).listFiles();
        if (files != null) {
            return Arrays.stream(files)
                    .filter(File::isFile)
                    .map(f -> f.getName().replace(".json", ""))
                    .toList();
        }
        log.error("Fant ikke mappe, eller mappa var tom. filsti={}", filsti);
        return null;
    }

    public TreeMap<Integer, EnumkodeFundament> lesEnumFundamentFil(String enumFilnavn, String cdmVersjon) {
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filsti = MAPPE_FUNDAMENT_KODEVERK + "/" + cdmVersjonTilpasset + "/" + enumFilnavn + ".json";
        try {
            String euSedFundamentJson = FileUtils.readFileToString(new File(filsti), UTF_8);
            return new JsonMapper().readValue(euSedFundamentJson, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Kunne ikke konvertere json-fil til Java-objekt. filsti={}", filsti);
        } catch (IOException e) {
            log.error("Fant ikke enum-fil. filsti={}", filsti);
        }
        return null;
    }

    public List<String> lesNavneneTilEndredeKodeverksfiler(String cdmVersjon) {
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filsti = MAPPE_DIFF_KODEVERK + "/" + cdmVersjonTilpasset;
        File[] files = new File(filsti).listFiles();
        if (files != null) {
            return Arrays.stream(files)
                    .filter(f -> f.isFile() && f.getName().endsWith(".json"))
                    .map(f -> f.getName().replace(".json", ""))
                    .toList();
        } else {
            log.error("Fant ikke mappe, eller mappa var tom. filsti={}", filsti);
            return null;
        }
    }

    public List<String> lesNavneneTilAlleKodeverksfiler(String cdmVersjon) {
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filsti = MAPPE_FUNDAMENT_KODEVERK + "/" + cdmVersjonTilpasset;
        File[] files = new File(filsti).listFiles();
        if (files != null) {
            return Arrays.stream(files)
                    .filter(f -> f.isFile() && f.getName().endsWith(".json"))
                    .map(f -> f.getName().replace(".json", ""))
                    .toList();
        } else {
            log.error("Fant ikke mappe, eller mappa var tom. filsti={}", filsti);
            return null;
        }
    }

    public TreeMap<Integer, EnumkodeFundament> lesNyEnumFundamentFil(String enumNavn, String cdmVersjon) {
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filsti = MAPPE_DIFF_KODEVERK + "/" + cdmVersjonTilpasset + "/" + enumNavn + ".json";
        try {
            String enumFundamentJson = FileUtils.readFileToString(new File(filsti), UTF_8);
            return new JsonMapper().readValue(enumFundamentJson, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Kunne ikke konvertere json-fil til Java-objekt. filsti={}", filsti);
        } catch (IOException e) {
            log.error("Fant ikke enum-fil. filsti={}", filsti);
        }
        return null;
    }

    public TreeMap<Integer, EnumkodeFundament> lesManueltEndretEnumFundamentFil(String enumNavn, String cdmVersjon) {
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filsti = MAPPE_DIFF_KODEVERK + "/" + cdmVersjonTilpasset + "/" + MANUELLE_JUSTERINGER + "/" + enumNavn + ".json";
        if (FileUtils.isRegularFile(new File(filsti))) {
            try {
                String enumFundamentJson = FileUtils.readFileToString(new File(filsti), UTF_8);
                return new JsonMapper().readValue(enumFundamentJson, new TypeReference<>() {
                });
            } catch (IOException e) {
                log.info("Kunne ikke lese manuelt endret enum-fil: {}", filsti);
            }
        }
        return null;
    }

    public void opprettAclEnumfil(List<String> nyAclEnumLinjer, String enumNavn, String cdmVersjon) {
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filsti = MAPPE_TARGET_RESULTAT_KODEVERK + "/" + cdmVersjonTilpasset + "/" + enumNavn + ".properties";
        try {
            String nyAclEnum = StringUtils.join(nyAclEnumLinjer, "\n");
            FileUtils.writeStringToFile(new File(filsti), nyAclEnum, "UTF-8");
            log.info("Enum-fil opprettet: {}", enumNavn);
        } catch (IOException e) {
            log.error("Kunne ikke skrive til fil. fil={}", filsti);
        }
    }

    private String aclSektor(String cdmSektor) {
        switch (cdmSektor) {
            case "X":
                return "administrative";
            case "DA":
                return "awod";
            case "F":
                return "family";
            case "H":
                return "horizontal";
            case "A":
                return "legislationapplicable";
            case "M":
                return "miscellaneous";
            case "P":
                return "pension";
            case "R":
                return "recovery";
            case "S":
                return "sickness";
            case "U":
                return "unemployment";
            default:
                return null;
        }
    }
}


