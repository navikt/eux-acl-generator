package eux.acl.generator.eksekvering.kodeverk;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static eux.acl.generator.service.FilService.*;

@Slf4j
public class Kodeverk_2a__kopierTilEndringer {

    public static void main(String[] args) {
        kopierInnKodeverksfilerMedEndringer(MAPPE_VERSJON_v44);
    }

    /**
     * Eksisterende filer blir ikke slettet. Eksisterende blir endret og nye blir lagt til.
     *
     */
    public static void kopierInnKodeverksfilerMedEndringer(String versjon) {
        File fraKodeverkDiffTarget = new File(MAPPE_ENDRINGER_KODEVERK_TARGET + "/" + versjon);
        File tilKodeverkDiffResources = new File(MAPPE_ENDRINGER_KODEVERK);
        try {
            FileUtils.copyDirectoryToDirectory(fraKodeverkDiffTarget, tilKodeverkDiffResources);
            log.info("Kopiert kodeverksendringer. Se: {}/{}", MAPPE_ENDRINGER_KODEVERK, MAPPE_VERSJON_v44);
        } catch (IOException e) {
            log.error("Kunne ikke kopiere mappe. mappe={}, inn i mappe={} "
                    , fraKodeverkDiffTarget.getAbsolutePath(), tilKodeverkDiffResources.getAbsolutePath(), e);
        }
    }

}
