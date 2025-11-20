package eux.acl.generator.eksekvering.acl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static eux.acl.generator.eksekvering.kodeverk.Kodeverk_1a__kopierTilFundament.kopierInnKodeverkFundament;
import static eux.acl.generator.service.FilService.*;

@Slf4j
public class Acl_1a__kopierInnEuSedOgKodeverkFundament {


    public static void main(String[] args) {
        kopierInnKodeverkFundament(MAPPE_VERSJON_v43);
        kopierInnKodeverkFundament(MAPPE_VERSJON_v44);
        kopierInnEuSedFundament(MAPPE_VERSJON_v43);
        kopierInnEuSedFundament(MAPPE_VERSJON_v44);
    }

    public static void kopierInnEuSedFundament(String versjon) {
        File fraEuSedFundamentTarget = new File(MAPPE_FUNDAMENT_EUSED_TARGET + "/" + versjon);
        File tilEuSedFundamentResources = new File(MAPPE_FUNDAMENT_EUSED);
        try {
            FileUtils.copyDirectoryToDirectory(fraEuSedFundamentTarget, tilEuSedFundamentResources);
            log.info("Kopiert euSedFundament. Se: {}/{}", MAPPE_FUNDAMENT_EUSED, versjon);
        } catch (IOException e) {
            log.error("Kunne ikke kopiere mappe. mappe={}, inn i mappe={} "
                    , fraEuSedFundamentTarget.getAbsolutePath(), tilEuSedFundamentResources.getAbsolutePath(), e);
        }
    }
}
