package eux.acl.generator;

import com.github.wnameless.json.flattener.JsonFlattener;
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

import static eux.acl.generator.service.FilService.*;

public class LageFundamentTest {


    private static final String MAPPE_CDMv43 = "src/test/resources/cdm-v43-meta";
    private static final String MAPPE_ACLv42 = "src/main/resources/sedtemplates/v42";
    public static final String MAPPE_ACLv43_MELLOMBELS = "target/v43Raw";
    public static String JUSTERINGER_FILNAVN_ENDE = "-v43-adjustments.json";
    public static String UENDRET_FILNAVN_ENDE = "-v43-flat.json";

    private static final String UTF_8 = StandardCharsets.UTF_8.name();
    private UttrekkAvKodeverkFraCdmService cdmReader = new UttrekkAvKodeverkFraCdmService();

    /**
     * Sammenlikner CDM og ACL versjoner
     * <p>
     * v4.2 med v4.3. Det som er likt skrives til ei fil,
     * det som kun fins i v4.2 skrives til ei anna fil, og det som kun fins i v4.3 til ei tredje fil.
     * Filene som inneholder justeringer som er manuellt lagt inn ( ..adjustments.json) blir ikke overskrevet.
     * De genereres kun når disse filene ikke finnes fra tidligere.
     */

    public static List<String> SEDTYPER_F_H_S_U() {
        List<String> sedtyper = new ArrayList<>();
        sedtyper.addAll(SEDTYPER_F);
        sedtyper.addAll(SEDTYPER_H);
        sedtyper.addAll(SEDTYPER_S);
        sedtyper.addAll(SEDTYPER_U);
        return sedtyper;
    }

    /**
     * Trekker ut enum-er fra cdm-meta-filer og lager ei fil pr unike enum med info som seinere kan brukes til å
     * generere kodeverksfiler.
     * Genererer ei fil om viser sedtype -> enum-navn
     * Genererer ei fil med unike enum-er
     */

    // Trekk ut landkoder og valuta

    @Test
    public void lager_kodeverkFundament_v44() {
        FundamentFacade aclMalFacade = new FundamentFacade();
        aclMalFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_M, V44);
        aclMalFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_A, V44);
        aclMalFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_DA, V44);
        aclMalFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_F, V44);
        aclMalFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_H, V44);
        aclMalFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_P, V44);
        aclMalFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_R, V44);
        aclMalFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_S, V44);
        aclMalFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_U, V44);
        aclMalFacade.trekkUtEnumsettOgSkrivTilFil(SEDTYPER_X, V44);
    }

    @Test
    public void lager_euSedFundament_v44() {
        FundamentFacade aclMalFacade = new FundamentFacade();
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_A, V44);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_DA, V44);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_F, V44);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_H, V44);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_M, V44);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_P, V44);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_R, V44);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_S, V44);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_U, V44);
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_X, V44);
    }

    @Test
    public void lager_euSedFundament_v43() {
        // NB! Mangler logisk modell for 4.3 - bruker v4.4 !!
        FundamentFacade aclMalFacade = new FundamentFacade();
        aclMalFacade.trekkUtEuSedFundamentOgSkrivTilFil(SEDTYPER_F, V43);
    }

    @Test
    public void kontrollAvGenerertCdmSed_denSammenliknesMedEksisterendeAclSed_v43() {
        FundamentFacade aclMalFacade = new FundamentFacade();
        SEDTYPER_F_H_S_U().forEach(sedType -> aclMalFacade.sammenlikneEuSedFraCdmMedEuSedFraFraAcl(sedType, V43));
    }

    /**
     * Sammenlikner maler generert basert på to ulike CDM versjone.
     * Det som er likt skrives til ei fil.
     * Det som kun fins i ny versjon skrives til egen fil.
     * Det samme gjøres med det som kun fins i gammel versjon.
     *
     * Disse filene dammer grunnlaget for å lage ACL-maler i ny versjon
     */
    @Test
    public void cdmSedMaler_skrivDiffMellomCdmVersjonerTilFiler() {
        FundamentFacade aclMalFacade = new FundamentFacade();
//        aclMalFacade.sammenlikneCdmSedVersjonerOgSkrivDiffTilFiler(SEDTYPER_R, V43, V44); // feil R003-9
        aclMalFacade.sammenlikneCdmSedVersjonerOgSkrivDiffTilFiler(SEDTYPER_P3000, V43, V44);
        aclMalFacade.sammenlikneCdmSedVersjonerOgSkrivDiffTilFiler(SEDTYPER_A, V43, V44);
        aclMalFacade.sammenlikneCdmSedVersjonerOgSkrivDiffTilFiler(SEDTYPER_DA, V43, V44);
        aclMalFacade.sammenlikneCdmSedVersjonerOgSkrivDiffTilFiler(SEDTYPER_M, V43, V44);
        aclMalFacade.sammenlikneCdmSedVersjonerOgSkrivDiffTilFiler(SEDTYPER_P, V43, V44);
        aclMalFacade.sammenlikneCdmSedVersjonerOgSkrivDiffTilFiler(SEDTYPER_X, V43, V44);
    }

    @Test
    public void acl_diff_sammenliknerNyCdmVersjonMedGammelAclVersjon_v43() {
        FundamentFacade aclMalFacade = new FundamentFacade();
        SEDTYPER_F_H_S_U().forEach(sedType -> aclMalFacade.sammenlikneEuSedFraCdmMedEuSedFraFraAcl(sedType, V43));
    }

    /**
     * Genererer SEDer basert på CDMen. Sammenlikner dem med eksisterende ACL-SEDer.
     * Hvis disse er like antar vi at koden som gerererer SEDer basert på CDMen er korrekt.
     * Vi kan da - i neste runde - bruke denne koden til å generere de nye SEDene - basert på ny versjon av CDMen.
     */
    @Test
    public void aclSed_lesSedMalerOgSkrivTilFil_v43() {
        FundamentFacade aclMalFacade = new FundamentFacade();
        SEDTYPER_F_H_S_U().forEach(sedType -> aclMalFacade.lesAclMalOgSkrivDenTilFil(sedType, V43));
    }

    @Test
    public void kodeverk_diff_sammenliknerNyOgGammelCdmVersjon_v43() {
        FundamentFacade aclMalFacade = new FundamentFacade();
        SEDTYPER_F_H_S_U().forEach(sedType -> aclMalFacade.sammenlikneEuSedFraCdmMedEuSedFraFraAcl(sedType, V43));
    }





    /**
     * Sjekker at motoren for å generere SED-maler basert på CDMen er korrekt ved å
     * sammenlikne de eksisterende ACL-SEDene med CDM-SEDer generert av motoren i samme versjon.
     */




//    @Test
//    public void sammenlikneSammeVersjonerAvAclSedMedCdmSed_v43() {
//        CdmMetaFacade aclMalFacade = new CdmMetaFacade();
//        SEDTYPER_F_H_S_U().forEach(sedType -> aclMalFacade.sammenlikneSedFraAclOgSedFraCdm(sedType, V43));
//    }

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
