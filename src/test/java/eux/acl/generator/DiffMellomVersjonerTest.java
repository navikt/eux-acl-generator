package eux.acl.generator;

import com.github.wnameless.json.flattener.JsonFlattener;
import eux.acl.generator.facade.DiffFacade;
import eux.acl.generator.facade.FundamentFacade;
import eux.acl.generator.service.UttrekkAvKodeverkFraCdmService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import org.junit.jupiter.api.Test;


import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static eux.acl.generator.FundamentTest.SEDTYPER_F_H_S_U;
import static eux.acl.generator.service.FilService.*;

public class DiffMellomVersjonerTest {


    private static final String MAPPE_CDMv43 = "src/test/resources/cdm-v43-meta";
    private static final String MAPPE_ACLv42 = "src/main/resources/sedtemplates/v42";
    public static final String MAPPE_ACLv43_MELLOMBELS = "target/v43Raw";
    public static String JUSTERINGER_FILNAVN_ENDE = "-v43-adjustments.json";
    public static String UENDRET_FILNAVN_ENDE = "-v43-flat.json";

    private static final String UTF_8 = StandardCharsets.UTF_8.name();
    private UttrekkAvKodeverkFraCdmService cdmReader = new UttrekkAvKodeverkFraCdmService();


    /**
     * Sjekker at motoren for å generere SED-maler fra CDMen er korrekt ved å
     * sammenlikne genererte CDM-SEDer med eksisterende ACL-SEDer i samme versjon.
     */

    @Test
    public void sammenlikner_CDM_euSed_ACL_euSed_forrigeVersjon() {
        DiffFacade diffFacade = new DiffFacade();
        diffFacade.sammenliknerEuSedFraCdmMedEuSedFraAcl(SEDTYPER_A, V43);
        diffFacade.sammenliknerEuSedFraCdmMedEuSedFraAcl(SEDTYPER_DA, V43);
        diffFacade.sammenliknerEuSedFraCdmMedEuSedFraAcl(SEDTYPER_F, V43);
        diffFacade.sammenliknerEuSedFraCdmMedEuSedFraAcl(SEDTYPER_H, V43);
        diffFacade.sammenliknerEuSedFraCdmMedEuSedFraAcl(SEDTYPER_M, V43);
        diffFacade.sammenliknerEuSedFraCdmMedEuSedFraAcl(SEDTYPER_P, V43);
//        diffFacade.sammenliknerEuSedFraCdmMedEuSedFraAcl(SEDTYPER_P3000, V43);
        diffFacade.sammenliknerEuSedFraCdmMedEuSedFraAcl(SEDTYPER_R, V43);
        diffFacade.sammenliknerEuSedFraCdmMedEuSedFraAcl(SEDTYPER_S, V43);
        diffFacade.sammenliknerEuSedFraCdmMedEuSedFraAcl(SEDTYPER_U, V43);
        diffFacade.sammenliknerEuSedFraCdmMedEuSedFraAcl(SEDTYPER_X, V43);
    }

    /**
     * Sammenlikner SEDer generert i to ulike CDM versjoner.
     * Det som er uendret, nytt og utgått skrives til hver sin fil.
     *
     */
    @Test
    public void sammenlikner_euSedCdm_iUlikeVersjoner_skriverDiffTilFiler() {
        DiffFacade diffFacade = new DiffFacade();
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_A, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_DA, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_F, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_H, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_M, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_P, V43, V44);
//        diffFacade.sammenlikneCdmSedVersjonerOgSkrivDiffTilFiler(SEDTYPER_P3000, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_R, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_S, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_U, V43, V44);
        diffFacade.sammenliknerCdmSedVersjonerOgSkriverDiffTilFiler(SEDTYPER_X, V43, V44);
    }

    // sammenlikne enum innhold i ulike versjoner
    // kodeverk - stier der kodeverket er endret
    @Test
    public void sammenlikner_kodeverkCdm_iUlikeVersjoner_skriverDiffTilFiler() {
        DiffFacade diffFacade = new DiffFacade();
        diffFacade.sammenliknerKodeverkForToCdmVersjonerOgSkriverDiffTilFil(V43, V44);
    }





    /**
     * Lage ACL - hjelp til den manuelle jobben:
     * Nye mappinger mellom euSed og aclSed må lages manuelt.
     * Genererer filer der uendrede mappinger mellom euSED-stier og aclSED-stier er fylt inn.
     * Nye euSed-stier er også fylt inn. Men her må aclSED-stiene fylles inn manuelt.
     *
     * Format (listet pr sektor):
     * <sedType>:<euSed-sti>:<aclSed-sti>
     *     Like euSed-stier i ulike sedTyper kan da sammenstilles (i Excel) - og kanskje gir aclSed-stien seg selv...
     *
     */
    @Test
    public void aclSed_lesSedMalerOgSkrivTilFil_v43() {
        FundamentFacade aclMalFacade = new FundamentFacade();
        SEDTYPER_F.forEach(sedType -> aclMalFacade.lesAclMalOgSkrivDenTilFil(sedType, V43));
    }









    /**
     * Lager CDM-SED-maler for v4.4 - brukes som grunnlag for å sammenlikne CDM v4.3 og CDM v4.4
     */





    @Test
    public void aclSedMaler_skrivDiffMellomAclMalerOgNyCdmVersjonTilFiler() {
        FundamentFacade aclMalFacade = new FundamentFacade();
        SEDTYPER_F_H_S_U().forEach(sedType -> aclMalFacade
                .sammenliknerGammelAclSedMedNyCdmVersjonOgSkriverDiffTilFil(sedType, V44, V43));
    }

    /**
     * Brukes til å fylle ut nye felt samtidig i alle SEDene innen samme sektor
     */
    @Test
    public void alleAclSedBasisMalerTilSammeFil_kolon_etter_sedtype() {
        FundamentFacade aclMalFacade = new FundamentFacade();
        aclMalFacade.skrivBasisForNyeAclMalerTilSammeFil(SEDTYPER_H, V44, V44);
    }

    /**
     * Kodeverk -------------
     * <p>
     * Lage cdmSti til kodeverktype - Oversett til AclSti vs kodeverktype
     * Bytt ut siste ledd i AclSti med kodeverktype.
     */
    @Test
    public void henterLokasjonIdVsEnumNavnForAlleSed() {
        FundamentFacade aclMalFacade = new FundamentFacade();
// finn cdmStierVsDdmId

//        Map<String, String> cdmIdVsEnumNavn =  aclMalFacade.uttrekkFraLogiskModell(V44);
    }

    @Test
    public void kodeverkDiffMellomToCdmVersjoner() {
        FundamentFacade aclMalFacade = new FundamentFacade();
//        Map<String, String> lokasjonIdVsEnumNavn =  aclMalFacade.uttrekkFraLogiskModell(V44);
    }

    // -------------------

    @Test
    public void nyeStierSomErFellesForMangeSEDer() {
        FundamentFacade aclMalFacade = new FundamentFacade();
        aclMalFacade.lagEiFilMedUnikeNyeCdmStier(SEDTYPER_F, V44, "unikeNye_F_stier.txt");
    }

    @Test
    public void utgaatteStierSomErFellesForMangeSEDer() {
        FundamentFacade aclMalFacade = new FundamentFacade();
        aclMalFacade.lagEiFilMedUnikeUtgaatteAclStier(SEDTYPER_F, V44, "unikeUtgaatte_F_stier.txt");
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
