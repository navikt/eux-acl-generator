package eux.acl.generator.eksekvering.testdata;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static eux.acl.generator.service.FilService.*;

@Slf4j
public class Testdata_2a__kopierTilResultater {


    public static void main(String[] args) {
        kopierInnPdfMaler(MAPPE_VERSJON_v44);
    }

    public static void kopierInnPdfMaler(String versjon) {
        File fraEuSedFundamentTarget = new File(MAPPE_RESULTATER_TESTDATA_TARGET + "/" + versjon);
        File tilEuSedFundamentResources = new File(MAPPE_RESULTATER_TESTDATAR);
        try {
            FileUtils.copyDirectoryToDirectory(fraEuSedFundamentTarget, tilEuSedFundamentResources);
            log.info("Kopiert testdata. Se: {}/{}", MAPPE_RESULTATER_TESTDATAR, versjon);
        } catch (IOException e) {
            log.error("Kunne ikke kopiere mappe. mappe={}, inn i mappe={} "
                    , fraEuSedFundamentTarget.getAbsolutePath(), tilEuSedFundamentResources.getAbsolutePath(), e);
        }
    }
}
