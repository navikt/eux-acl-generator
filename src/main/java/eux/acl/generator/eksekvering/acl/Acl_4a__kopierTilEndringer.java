package eux.acl.generator.eksekvering.acl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static eux.acl.generator.eksekvering.kodeverk.Kodeverk_1a__kopierTilFundament.kopierInnKodeverkFundament;
import static eux.acl.generator.service.FilService.*;

@Slf4j
public class Acl_4a__kopierTilEndringer {


    public static void main(String[] args) {
        kopierInnAclEndringer(MAPPE_VERSJON_v44);
    }

    public static void kopierInnAclEndringer(String versjon) {
        File fraEuSedFundamentTarget = new File(MAPPE_ENDRINGER_ACL_TARGET + "/" + versjon);
        File tilEuSedFundamentResources = new File(MAPPE_ENDRINGER_ACL);
        try {
            FileUtils.copyDirectoryToDirectory(fraEuSedFundamentTarget, tilEuSedFundamentResources);
            log.info("Kopiert ACL-endringer. Se: {}/{}", MAPPE_ENDRINGER_ACL, versjon);
        } catch (IOException e) {
            log.error("Kunne ikke kopiere mappe. mappe={}, inn i mappe={} "
                    , fraEuSedFundamentTarget.getAbsolutePath(), tilEuSedFundamentResources.getAbsolutePath(), e);
        }
    }
}
