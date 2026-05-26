package eux.acl.generator.eksekvering.kodeverk;

import eux.acl.generator.eksekvering.Versjoner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static eux.acl.generator.service.FilService.MAPPE_RESULTAT_KODEVERK;
import static eux.acl.generator.service.FilService.MAPPE_RESULTAT_KODEVERK_TARGET;

@Slf4j
public class Kodeverk_3a__kopierTilResultater extends Versjoner {

	public static void main(String[] args) {
		kopierInnNyeKodeverksfiler(NY_MAPPE_VERSJON);
	}

	public static void kopierInnNyeKodeverksfiler(String versjon) {
		File fraKodeverkDiffTarget = new File(MAPPE_RESULTAT_KODEVERK_TARGET + "/" + versjon);
		File tilKodeverkDiffResources = new File(MAPPE_RESULTAT_KODEVERK);
		try {
			FileUtils.copyDirectoryToDirectory(fraKodeverkDiffTarget, tilKodeverkDiffResources);
			log.info("Kopiert kodeverksendringer. Se: {}/{}", MAPPE_RESULTAT_KODEVERK, NY_MAPPE_VERSJON);
		} catch (IOException e) {
			log.error("Kunne ikke kopiere mappe. mappe={}, inn i mappe={} "
				, fraKodeverkDiffTarget.getAbsolutePath(), tilKodeverkDiffResources.getAbsolutePath(), e);
		}
	}

}
