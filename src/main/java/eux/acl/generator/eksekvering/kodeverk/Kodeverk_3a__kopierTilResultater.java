package eux.acl.generator.eksekvering.kodeverk;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static eux.acl.generator.service.FilService.*;

@Slf4j
public class Kodeverk_3a__kopierTilResultater {

    public static void main(String[] args) {
        kopierInnNyeKodeverksfiler(MAPPE_VERSJON_v44);
    }

    public static void kopierInnNyeKodeverksfiler(String versjon) {
        File fraKodeverkDiffTarget = new File(MAPPE_RESULTAT_KODEVERK_TARGET + "/" + versjon);
        File tilKodeverkDiffResources = new File(MAPPE_RESULTAT_KODEVERK);
        try {
            FileUtils.copyDirectoryToDirectory(fraKodeverkDiffTarget, tilKodeverkDiffResources);
            log.info("Kopiert kodeverksendringer. Se: {}/{}", MAPPE_RESULTAT_KODEVERK, MAPPE_VERSJON_v44);
        } catch (IOException e) {
            log.error("Kunne ikke kopiere mappe. mappe={}, inn i mappe={} "
                    , fraKodeverkDiffTarget.getAbsolutePath(), tilKodeverkDiffResources.getAbsolutePath(), e);
        }
    }

}
