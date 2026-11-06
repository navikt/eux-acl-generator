package eux.acl.generator.eksekvering.testdata;

import eux.acl.generator.eksekvering.Versjoner;

import static eux.acl.generator.eksekvering.acl.Acl_1_lagEuSedFundament.lagEuSedFundament;
import static eux.acl.generator.eksekvering.acl.Acl_1a__kopierInnEuSedOgKodeverkFundament.kopierInnEuSedFundament;
import static eux.acl.generator.eksekvering.kodeverk.Kodeverk_1a__kopierTilFundament.kopierInnKodeverkFundament;

public class Testdata_1_lagEuSedFundament extends Versjoner {


	static class LagSedFundament {

	public static void main(String[] args) {
		lagEuSedFundament(NY_VERSJON);
	}
	}

	static class KopierSedFundamentFraTargetTilResultater {

		public static void main(String[] args) {
			kopierInnEuSedFundament(NY_MAPPE_VERSJON);
		}

	}

}
