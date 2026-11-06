package eux.acl.generator.eksekvering.testdata;

import eux.acl.generator.eksekvering.Versjoner;

import static eux.acl.generator.eksekvering.kodeverk.Kodeverk_1_lagKodeverkFundament.lagKodeverkFundament;
import static eux.acl.generator.eksekvering.kodeverk.Kodeverk_1a__kopierTilFundament.kopierInnKodeverkFundament;

public class Testdata_1_lagKodeverkFundament extends Versjoner {

	static class LagKodeverkFundament {

		public static void main(String[] args) {
			lagKodeverkFundament(NY_VERSJON);
		}
	}

	static class KopierKodeverkfundamentFraTargetTilResultater {

		public static void main(String[] args) {
			kopierInnKodeverkFundament(NY_MAPPE_VERSJON);
		}

	}

}
