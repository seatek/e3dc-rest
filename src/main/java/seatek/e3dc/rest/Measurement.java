package seatek.e3dc.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {
	private int solarleistung;
	private int netzleistung;
	private int hausleistung;
	private int batterieleistung;
	private int batteriekapazität;
	public enum Field {
		SOLAR_POWER(40068),LINE_POWER(40074),POWER_CONSUMPTION(40072),BATTERY_POWER(40070),BATTERY_LEVEL(40083);

		private final int modbusField;
		public int getModbusField() {
			return modbusField;
		}
		private Field(int modbusField) {
			this.modbusField = modbusField;
		}
	}
}
