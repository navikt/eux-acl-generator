package eux.acl.generator.eksekvering.kodeverk;

import eux.acl.generator.eksekvering.Versjoner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static eux.acl.generator.service.FilService.MAPPE_FUNDAMENT_KODEVERK;
import static eux.acl.generator.service.FilService.MAPPE_FUNDAMENT_KODEVERK_TARGET;

@Slf4j
public class Kodeverk_1a__kopierTilFundament extends Versjoner {

	public static void main(String[] args) {
		kopierInnKodeverkFundament(NY_MAPPE_VERSJON);
	}

	public static void kopierInnKodeverkFundament(String versjon) {
		File fraKodeverkFundamentTarget = new File(MAPPE_FUNDAMENT_KODEVERK_TARGET + "/" + versjon);
		File tilKodeverkFundamentResources = new File(MAPPE_FUNDAMENT_KODEVERK);
		try {
			FileUtils.copyDirectoryToDirectory(fraKodeverkFundamentTarget, tilKodeverkFundamentResources);
			log.info("Kopiert kodeverkFundament. Se: {}/{}", MAPPE_FUNDAMENT_KODEVERK, versjon);
		} catch (IOException e) {
			log.error("Kunne ikke kopiere mappe. mappe={}, inn i mappe={} "
				, fraKodeverkFundamentTarget.getAbsolutePath(), tilKodeverkFundamentResources.getAbsolutePath(), e);
		}
	}
}
