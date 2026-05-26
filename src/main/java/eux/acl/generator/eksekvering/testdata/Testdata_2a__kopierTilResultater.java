package eux.acl.generator.eksekvering.testdata;

import eux.acl.generator.eksekvering.Versjoner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static eux.acl.generator.service.FilService.MAPPE_RESULTATER_TESTDATAR;
import static eux.acl.generator.service.FilService.MAPPE_RESULTATER_TESTDATA_TARGET;

@Slf4j
public class Testdata_2a__kopierTilResultater extends Versjoner {


	public static void main(String[] args) {
		kopierInnPdfMaler(NY_MAPPE_VERSJON);
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
