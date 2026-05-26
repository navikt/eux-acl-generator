package eux.acl.generator.eksekvering.acl;

import eux.acl.generator.eksekvering.Versjoner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static eux.acl.generator.service.FilService.MAPPE_RESULTATER_ACL;
import static eux.acl.generator.service.FilService.MAPPE_RESULTATER_ACL_TARGET;

@Slf4j
public class Acl_5a__kopierTilResultater extends Versjoner {


	public static void main(String[] args) {
		kopierInnAclEndringer(NY_MAPPE_VERSJON);
	}

	public static void kopierInnAclEndringer(String versjon) {
		File fraTarget = new File(MAPPE_RESULTATER_ACL_TARGET + "/" + versjon);
		File tilResources = new File(MAPPE_RESULTATER_ACL);
		try {
			FileUtils.copyDirectoryToDirectory(fraTarget, tilResources);
			log.info("Kopiert ACL-maler. Se: {}/{}", MAPPE_RESULTATER_ACL, versjon);
		} catch (IOException e) {
			log.error("Kunne ikke kopiere mappe. mappe={}, inn i mappe={} "
				, fraTarget.getAbsolutePath(), tilResources.getAbsolutePath(), e);
		}
	}
}
