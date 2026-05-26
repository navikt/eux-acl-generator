package eux.acl.generator.eksekvering.kodeverk;

import eux.acl.generator.eksekvering.Versjoner;
import eux.acl.generator.facade.DiffFacade;

public class Kodeverk_2_lagKodeverksdiffMellomVersjoner extends Versjoner {

	public static void main(String[] args) {
		new DiffFacade().sammenliknerEnumeneForToCdmVersjonerOgKopiererEnumFundamentFilene(FORRIGE_VERSJON, NY_VERSJON);
	}

}
