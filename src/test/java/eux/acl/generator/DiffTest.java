package eux.acl.generator;

import com.github.wnameless.json.flattener.JsonFlattener;
import eux.acl.generator.facade.DiffFacade;
import eux.acl.generator.facade.FundamentFacade;
import eux.acl.generator.old.UttrekkAvKodeverkFraCdmService;
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

public class DiffTest {


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
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_A, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_DA, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_F, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_H, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_M, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_P, V43);
//        diffFacade.sammenliknerEuSedFraCdmMedEuSedFraAcl(SEDTYPER_P3000, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_R, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_S, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_U, V43);
        diffFacade.sammenliknerCdmEuSedMedAclEuSed(SEDTYPER_X, V43);
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

    @Test
    public void sammenlikner_nyCdmVersjon_med_gammelAclVersjon_skriverNyAclVersjonTilTilFiler() {
        DiffFacade diffFacade = new DiffFacade();
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_A, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_DA, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_F, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_H, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_M, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_P, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_R, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_S, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_U, V43, V44);
        diffFacade.sammenliknerGammelAclMalMedNyCdmSedOgSkriverNyAclMalBasisTilFiler(SEDTYPER_X, V43, V44);
    }

    // sammenlikne enum innhold i ulike versjoner
    // kodeverk - stier der kodeverket er endret
    @Test
    public void sammenlikner_kodeverkCdm_iUlikeVersjoner_skriverDiffTilFiler() {
        DiffFacade diffFacade = new DiffFacade();
        diffFacade.sammenliknerEnumeneForToCdmVersjonerOgKopiererEnumFundamentFilene(V43, V44);
    }

}
