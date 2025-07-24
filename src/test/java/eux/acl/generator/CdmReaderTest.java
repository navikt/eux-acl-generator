package eux.acl.generator;

import com.github.wnameless.json.flattener.JsonFlattener;
import eux.acl.generator.service.UttrekkAvKodeverkFraCdmService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CdmReaderTest {


    private static final String MAPPE_CDMv43 = "src/test/resources/cdm-v43-meta";
    private static final String MAPPE_ACLv42 = "src/main/resources/sedtemplates/v42";
    public static final String MAPPE_ACLv43_MELLOMBELS = "target/v43Raw";
    public static String JUSTERINGER_FILNAVN_ENDE = "-v43-adjustments.json";
    public static String UENDRET_FILNAVN_ENDE = "-v43-flat.json";

    private static final String UTF_8 = StandardCharsets.UTF_8.name();
    private UttrekkAvKodeverkFraCdmService cdmReader = new UttrekkAvKodeverkFraCdmService();

    /**
     * Sammenlikner v4.2 med v4.3. Det som er likt skrives til ei fil,
     * det som kun fins i v4.2 skrives til ei anna fil, og det som kun fins i v4.3 til ei tredje fil.
     * Filene som inneholder justeringer som er manuellt lagt inn ( ..adjustments.json) blir ikke overskrevet.
     * De genereres kun når disse filene ikke finnes fra tidligere.
     */

    @Test
//  @Ignore
    public void malutgangspunktForV43_A() {
        SedType.A.stream()
                .forEach(s -> lagUtgangspunktForV43Mal(s, "A", "legislationapplicable"));
    }

    @org.junit.Test
    @Ignore
    public void malutgangspunktForV43_DA() {
        SedType.DA.stream().forEach(s -> lagUtgangspunktForV43Mal(s, "DA", "awod"));
    }

    @org.junit.Test
    @Ignore
    public void malutgangspunktForV43_F() {
        SedType.F.stream().forEach(s -> lagUtgangspunktForV43Mal(s, "F", "family"));
    }

    @org.junit.Test
    @Ignore
    public void malutgangspunktForV43_H() {
        SedType.H.stream().forEach(s -> lagUtgangspunktForV43Mal(s, "H", "horizontal"));
    }

    @org.junit.Test
    @Ignore
    public void malutgangspunktForV43_M() {
        SedType.M.stream().forEach(s -> lagUtgangspunktForV43Mal(s, "M", "miscellaneous"));
    }

    @org.junit.Test
    @Ignore
    public void malutgangspunktForV43_P() {
        SedType.P.stream().forEach(s -> lagUtgangspunktForV43Mal(s, "P", "pension"));
    }

    @org.junit.Test
    @Ignore
    public void malutgangspunktForV43_R() {
        SedType.R.stream().forEach(s -> lagUtgangspunktForV43Mal(s, "R", "recovery"));
    }

    @org.junit.Test
    @Ignore
    public void malutgangspunktForV43_S() {
        SedType.S.stream().forEach(s -> lagUtgangspunktForV43Mal(s, "S", "sickness"));
    }

    @org.junit.Test
    @Ignore
    public void malutgangspunktForV43_U() {
        SedType.U.stream().forEach(s -> lagUtgangspunktForV43Mal(s, "U", "unemployment"));
    }

    @org.junit.Test
    @Ignore
    public void malutgangspunktForV43_X() {
        SedType.X.stream().forEach(s -> lagUtgangspunktForV43Mal(s, "X", "administrative"));
    }

    private void lagUtgangspunktForV43Mal(String sedType, String cdmSektor, String aclSector) {
        String cdmV43TemplateFilnavn = MAPPE_CDMv43 + "/" + cdmSektor + "/" + sedType + ".json";
        String aclV42TemplateFilnavn = MAPPE_ACLv42 + "/" + aclSector + "/" + sedType + "_v4.2.json";
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
                    new File(MAPPE_ACLv43_MELLOMBELS + "/" + cdmSektor + "/" + sedType + UENDRET_FILNAVN_ENDE),
                    cdmMalAlleEuStier, "UTF-8");
            FileUtils.writeStringToFile(
                    new File(MAPPE_ACLv43_MELLOMBELS + "/" + cdmSektor + "/" + sedType + "-v42-diff.json"),
                    kunAclFlatList, "UTF-8");
            String templateAdjustmentsFile =
                    MAPPE_ACLv43_MELLOMBELS + "/" + cdmSektor + "/" + sedType + JUSTERINGER_FILNAVN_ENDE;
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


    @Test
    public void enTest() {

    }
}
