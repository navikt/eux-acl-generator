package eux.acl.generator.eksekvering.kodeverk;

import eux.acl.generator.eksekvering.Versjoner;
import eux.acl.generator.facade.ResultaterFacade;

public class Kodeverk_3_lagDeNyeKodeverksfileneTilACLen extends Versjoner {


	/**
	 * Manuell justering av (for lange) enumkoder?
	 * <p>
	 * Kopier aktuelle enum-filer og legg dem i /endringer/kodeverk/../manuelleJusteringer - pakka.
	 * (Disse filene blir aldri overskrevet, men de blir lest)
	 * Endre 'enumkode' i disse filene til ønsket verdi - og: Kjør.
	 *
	 */
	public static void main(String[] args) {
		new ResultaterFacade().lagNyeEnumfilerForAcl(NY_VERSJON);
	}

}
