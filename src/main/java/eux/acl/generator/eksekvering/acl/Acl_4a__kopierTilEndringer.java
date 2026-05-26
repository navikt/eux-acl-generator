package eux.acl.generator.eksekvering.acl;

import eux.acl.generator.eksekvering.Versjoner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static eux.acl.generator.service.FilService.MAPPE_ENDRINGER_ACL;
import static eux.acl.generator.service.FilService.MAPPE_ENDRINGER_ACL_TARGET;

@Slf4j
public class Acl_4a__kopierTilEndringer extends Versjoner {

	public static void main(String[] args) {
		kopierInnAclEndringer(NY_MAPPE_VERSJON);
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
