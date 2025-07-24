package eux.acl.generator.service;

import com.github.wnameless.json.flattener.JsonFlattener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class FilService {
    public static final String V43 = "4.3";
    public static final String V44 = "4.4";
    public static final String MAPPE_TARGET_CDM_SED = "target/cdmSed";
    public static final String MAPPE_TARGET_CDM_DIFF = "target/cdmDiff";
    public static final String MAPPE_TARGET_ACL_SED = "target/aclMaler";
    public static final String MAPPE_TARGET_ACL_DIFF = "target/aclDiff";
    public static final String MAPPE_CDM_META = "src/main/resources/input/cdm-meta";
    public static final String MAPPE_CDM_LOGISK_MODELL = "src/main/resources/input/cdm-logisk-modell";
    public static final String MAPPE_OVERSETTELSER = "src/main/resources/input/cdm-oversettelser";
    public static final String MAPPE_ACL = "src/main/resources/acl";
    public static final String FIL_NYE_STIER = "nytt.txt";
    public static final String FIL_UENDREDE_STIER = "uendret.txt";
    public static final String FIL_UTGAATTE_STIER = "utgaatt.txt";
    public static final String FIL_UENDREDE_ACL_STIER = "uendret_acl.txt";
    public static final String FIL_UTGAATTE_ACL_STIER = "utgaatt_acl.txt";
    public static final String FIL_CDM_LOGISK_MODELL = "CDM_LM.xml";
    public static final String FIL_OVERSETTELSER = "no.json";
    public static final String FIL_TEKST_ID_VS_ENUM_NAVN = "cdmTekstIdVsEnumnavn";

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

    public Map<String, Object> lesCdmMetaFilForEnSed(String sedType, String cdmVersjon) {
        String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String cdmMetaSedFilSti = MAPPE_CDM_META + "/" + cdmVersjonTilpasset + "/" + cdmSektor + "/" + sedType + ".json";
        try {
            String cdmMetaSedJson = FileUtils.readFileToString(new File(cdmMetaSedFilSti), UTF_8);
            Map<String, Object> cdmMalFlattened = JsonFlattener.flattenAsMap(cdmMetaSedJson);
            String cdmMetaSedGVer = cdmMalFlattened.get("context.inputs.sedGVer").toString();
            String cdmMetaSedVer = cdmMalFlattened.get("context.inputs.sedVer").toString();
            String cdmMetaSedType = cdmMalFlattened.get("context.inputs.sedName").toString();
            if (!sedType.equals(cdmMetaSedType)) {
                log.error("Sedtype mismatch. Oppgitt sedType={}, CdmMeta sedType={} ", sedType, cdmMetaSedType);
                throw new RuntimeException("Sedtype mismatch. " + sedType + " vs " + cdmMetaSedType);
            }
            if (!cdmVersjon.equals(cdmMetaSedGVer + "." + cdmMetaSedVer)) {
                log.error("Cdmversjon mismatch. Oppgitt versjon={}, Cdminnhold versjon={} ", sedType, cdmMetaSedType);
                throw new RuntimeException("Cdmversjon mismatch. " + cdmVersjon + " vs " + "v" + cdmMetaSedGVer + cdmMetaSedVer);
            }
            return cdmMalFlattened;
        } catch (IOException e) {
            log.error("Fil mangler. fil={}, cdmVersjon={}", cdmMetaSedFilSti, cdmVersjon);
//            throw new RuntimeException(e);
        }
        return null;
    }

    public void skrivCdmSedTilFil(List<String> cdmSedFlattened, String sedType, String cdmVersjon) {
        if (cdmSedFlattened == null) {
            return;
        }
        String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String cdmSedFilSti = MAPPE_TARGET_CDM_SED + "/" + cdmVersjonTilpasset + "/" + cdmSektor + "/" + sedType + ".txt";
        String cdmSedStierFlattened = StringUtils.join(cdmSedFlattened, "\n");
        try {
            FileUtils.writeStringToFile(new File(cdmSedFilSti), cdmSedStierFlattened, "UTF-8");
        } catch (IOException e) {
            log.error("Kunne ikke skrive til fil. fil={}", cdmSedFilSti);
            //            throw new RuntimeException(e);
        }
    }


    public Map<String, Object> lesAclMal(String sedType, String cdmVersjon) {
        String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String aclFilnavn = sedType + "_v" + cdmVersjon + ".json";
        String aclMalFilSti = MAPPE_ACL + "/" + cdmVersjonTilpasset + "/" + aclSektor(cdmSektor) + "/" + aclFilnavn;
        try {
            String aclmalJson = FileUtils.readFileToString(new File(aclMalFilSti), UTF_8);
            return JsonFlattener.flattenAsMap(aclmalJson);
        } catch (IOException e) {
            log.error("Kunne ikke lese fil. fil={}", aclMalFilSti);
            //            throw new RuntimeException(e);
        }
        return null;
    }

    public void skrivAclSedTilFil(List<String> cdmStiVsNavStiListe, String sedType, String cdmVersjon) {
        String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String aclMalFilsti = MAPPE_TARGET_ACL_SED + "/" + cdmVersjonTilpasset + "/" + cdmSektor + "/" + sedType + ".txt";
        String aclMalFlattened = StringUtils.join(cdmStiVsNavStiListe.stream().sorted().toList(), "\n");
        try {
            FileUtils.writeStringToFile(new File(aclMalFilsti), aclMalFlattened, "UTF-8");
        } catch (IOException e) {
            log.error("Kunne ikke skrive til fil. fil={}", aclMalFilsti);
            //            throw new RuntimeException(e);
        }
    }

    public List<String> lesAclMalFraTarget(String sedType, String cdmVersjon) {
        String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");

        String aclMalFilSti = MAPPE_TARGET_ACL_SED + "/" + cdmVersjonTilpasset + "/" + cdmSektor + "/" + sedType + ".txt";
        try {
            String aclFlattened = FileUtils.readFileToString(new File(aclMalFilSti), UTF_8);
            return Arrays.asList(aclFlattened.split("\n"));
        } catch (IOException e) {
            log.error("Kunne ikke lese fil fra target. fil={}", aclMalFilSti);
            //            throw new RuntimeException(e);
        }
        return null;
    }

    public List<String> lesCdmSedFraTarget(String sedType, String cdmVersjon) {
        String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");

        String aclMalFilSti = MAPPE_TARGET_CDM_SED + "/" + cdmVersjonTilpasset + "/" + cdmSektor + "/" + sedType + ".txt";
        try {
            String aclFlattened = FileUtils.readFileToString(new File(aclMalFilSti), UTF_8);
            return Arrays.asList(aclFlattened.split("\n"));
        } catch (IOException e) {
            log.error("Kunne ikke lese fil fra target. fil={}", aclMalFilSti);
            //            throw new RuntimeException(e);
        }
        return null;
    }

    public void skrivCdmEndringerTilFil(List<String> cdmSedStierListe, String sedType, String cdmVersjonNy, String filnavn) {
        String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
        String cdmVersjonTilpasset = "v" + cdmVersjonNy.replace(".", "");
        String filnavnSti = MAPPE_TARGET_CDM_DIFF + "/" + cdmVersjonTilpasset + "/" + cdmSektor + "/" + sedType + "/" + filnavn;
        String cdmSedStierFlattened = StringUtils.join(cdmSedStierListe.stream().sorted().toList(), "\n");
        try {
            FileUtils.writeStringToFile(new File(filnavnSti), cdmSedStierFlattened, "UTF-8");
        } catch (IOException e) {
            log.error("Kunne ikke skrive til fil. fil={}", filnavnSti);
            //            throw new RuntimeException(e);
        }
    }

    public void skrivAclMalerTilFil(List<String> linjer, String sektor, String filnavn, String cdmVersjon) {
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filnavnSti = MAPPE_TARGET_ACL_SED + "/" + cdmVersjonTilpasset + "/" + sektor + "/" + filnavn;
        String cdmSedStierFlattened = StringUtils.join(linjer.stream().sorted().toList(), "\n");
        try {
            FileUtils.writeStringToFile(new File(filnavnSti), cdmSedStierFlattened, "UTF-8");
        } catch (IOException e) {
            log.error("Kunne ikke skrive til fil. fil={}", filnavnSti);
            //            throw new RuntimeException(e);
        }
    }

    public void skrivNyeUnikeStierTilFil(List<String> unikeNyeCdmStier, String filnavn, String cdmVersjonNy) {
        String cdmVersjonTilpasset = "v" + cdmVersjonNy.replace(".", "");
        String filnavnSti = MAPPE_TARGET_CDM_DIFF + "/" + cdmVersjonTilpasset + "/" + filnavn;
        String cdmSedStierFlattened = StringUtils.join(unikeNyeCdmStier.stream().sorted().toList(), "\n");
        try {
            FileUtils.writeStringToFile(new File(filnavnSti), cdmSedStierFlattened, "UTF-8");
        } catch (IOException e) {
            log.error("Kunne ikke skrive til fil. fil={}", filnavnSti);
            //            throw new RuntimeException(e);
        }
    }

    public List<String> lesNyeStierFraTarget(String sedType, String cdmVersjon) {
        String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String aclMalFilSti = MAPPE_TARGET_CDM_DIFF + "/" + cdmVersjonTilpasset + "/" + cdmSektor
                + "/" + sedType + "/" + FIL_NYE_STIER;
        try {
            String aclFlattened = FileUtils.readFileToString(new File(aclMalFilSti), UTF_8);
            return Arrays.asList(aclFlattened.split("\n"));
        } catch (IOException e) {
            log.error("Kunne ikke lese fil fra target. fil={}", aclMalFilSti);
            //            throw new RuntimeException(e);
        }
        return null;
    }

    public List<String> lesUendredeAclStierFraTarget(String sedType, String cdmVersjon) {
        String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String aclMalFilSti = MAPPE_TARGET_CDM_DIFF + "/" + cdmVersjonTilpasset + "/" + cdmSektor
                + "/" + sedType + "/" + FIL_UENDREDE_ACL_STIER;
        try {
            String aclFlattened = FileUtils.readFileToString(new File(aclMalFilSti), UTF_8);
            return Arrays.asList(aclFlattened.split("\n"));
        } catch (IOException e) {
            log.error("Kunne ikke lese fil fra target. fil={}", aclMalFilSti);
            //            throw new RuntimeException(e);
        }
        return null;
    }

    public List<String> lesUtgaatteStierFraTarget(String sedType, String cdmVersjon) {
        String cdmSektor = StringUtils.splitByCharacterType(sedType)[0];
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String aclMalFilSti = MAPPE_TARGET_CDM_DIFF + "/" + cdmVersjonTilpasset + "/" + cdmSektor + "/" + sedType + "/" + FIL_UTGAATTE_STIER;
        try {
            String aclFlattened = FileUtils.readFileToString(new File(aclMalFilSti), UTF_8);
            return Arrays.asList(aclFlattened.split("\n"));
        } catch (IOException e) {
            log.error("Kunne ikke lese fil fra target. fil={}", aclMalFilSti);
            //            throw new RuntimeException(e);
        }
        return null;
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

    public Map<String, String> lesOversettelsesfil(String cdmVersjon) {
        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".", "");
        String filsti = MAPPE_OVERSETTELSER + "/" + cdmVersjonTilpasset + "/" + FIL_OVERSETTELSER;
        Map<String, String> tekstIdVsTekst = new HashMap<>();
        try {
            String oversettelser = FileUtils.readFileToString(new File(filsti), UTF_8);
            List<String> oversatteLinjer = Arrays.asList(oversettelser.split("\n"));
            oversatteLinjer.forEach(line -> {
                        if (line.contains("\"" + cdmVersjon)) {
                            String tekstId = line.split("_")[1];
                            String tekst = line.split(":")[1].replace("\"", "").trim();
                            tekstIdVsTekst.put(tekstId, tekst);
                        }
                    }
            );
            return tekstIdVsTekst;
        } catch (IOException e) {
            log.error("Fant ikke fil med oversettelser. fil={}", filsti);
            //            throw new RuntimeException(e);
        }
        return null;
    }


//    public void skrivCdmIdVsEnumnavnTilFil(List<String> unikeNyeCdmStier, String cdmVersjon) {
//        String cdmVersjonTilpasset = "v" + cdmVersjon.replace(".","");
//        String filnavnSti = MAPPE_CDM_LOGISK_MODELL + "/" + cdmVersjonTilpasset + "/" +  FIL_TEKST_ID_VS_ENUM_NAVN ;
//        String cdmSedStierFlattened = StringUtils.join(unikeNyeCdmStier.stream().sorted().toList(), "\n");
//        try {
//            FileUtils.writeStringToFile(new File(filnavnSti), cdmSedStierFlattened, "UTF-8");
//        } catch (IOException e) {
//            log.error("Kunne ikke skrive til fil. fil={}", filnavnSti);
//            //            throw new RuntimeException(e);
//        }
//    }

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


