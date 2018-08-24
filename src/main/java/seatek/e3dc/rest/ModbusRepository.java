package seatek.e3dc.rest;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.AbstractModbusMaster;
import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster;
import com.ghgande.j2mod.modbus.io.AbstractModbusTransport;
import com.ghgande.j2mod.modbus.procimg.Register;

@Component
public class ModbusRepository {
	AbstractModbusMaster master;

	@PostConstruct
	public void init() {

		master = new ModbusTCPMaster("192.168.1.41", 502);
		try {
			master.connect();
			AbstractModbusTransport trans = master.getTransport();
		} catch (Exception e) {
			throw new IllegalStateException("Could not start modbus client", e);
		}

	}

	public void shutdown() {

	}

	public int getHausleistung() {
		int ref = 40072;
		return readInt(ref, 1);
	}

	private int readInt(int ref, int count) {
		// int count = 1;
		Register[] mulReg;
		try {
			mulReg = master.readMultipleRegisters(ref - 1, count);
		} catch (ModbusException e) {
			throw new RuntimeException(e);
		}

		return mulReg[0].getValue();
	}

	public int getSolarleistung() {
		// TODO Auto-generated method stub
		return readInt(40068, 1);
	}

	public int getNetzleistung() {
		// TODO Auto-generated method stub
		int possiblyNegative = readInt(40074, 1);
		if((possiblyNegative&0xF000)>0) {
			possiblyNegative^=0xFFFF;
			possiblyNegative++;
			possiblyNegative*=-1;

		}


		return possiblyNegative;
	}

	public Measurement getMeasurement() {
		return new Measurement.MeasurementBuilder().hausleistung(getHausleistung()).netzleistung(getNetzleistung())
				.solarleistung(getSolarleistung()).build();

	}
}
/**
 * address name description unit type len factor offset role room poll wp 40002
 * Modbus_Firmware Modbus-Firmware-Version uint16be 1 1 0 value true false 40004
 * Hersteller Hersteller string 16 1 0 value true false 40020 Modell Modell
 * string 16 1 0 value true false 40036 Seriennummer Seriennummer string 16 1 0
 * value true false 40052 Firmware Firmware Release string 16 1 0 value true
 * false 40068 PV_Leistung Photovoltaik-Leistung in Watt W int32sw 2 1 0 value
 * true false 40070 Batterie_Leistung Batterie-Leistung in Watt W int32sw 2 1 0
 * value true false 40072 Hausverbrauch_Leistung Hausverbrauchs-Leistung in Watt
 * W int32sw 2 1 0 value true false 40074 Netz_Leistung Leistung am
 * Netzübergabepunkt in Watt W int32sw 2 1 0 value true false 40082
 * Autarkie_Eigenverbrauch Autarkie und Eigenverbrauch in Prozent uint16be 1 1 0
 * value true false 40083 Batterie_SOC Batterie-SOC in Prozent % uint16be 1 1 0
 * value true false 40084 Emergency_Power_Status Emergency-Power Status uint16be
 * 1 1 0 value true false 40085 EMS_Status EMS-Status uint16be 1 1 0 value true
 * false 40096 DC_String_1_Voltage Spannung String 1 in Volt V uint16be 1 1 0
 * value true false 40097 DC_String_2_Voltage Spannung String 2 in Volt V
 * uint16be 1 1 0 value true false 40098 DC_String_3_Voltage Spannung String 3
 * in Volt V uint16be 1 1 0 value true false 40099 DC_String_1_Current Strom
 * String 1 in Ampere A uint16be 1 1 0 value true false 40100
 * DC_String_2_Current Strom String 2 in Ampere A uint16be 1 1 0 value true
 * false 40101 DC_String_3_Current Strom String 3 in Ampere A uint16be 1 1 0
 * value true false 40102 DC_String_1_Power Leistung String 1 in Watt W uint16be
 * 1 1 0 value true false 40103 DC_String_2_Power Leistung String 2 in Watt W
 * uint16be 1 1 0 value true false 40104 DC_String_3_Power Leistung String 3 in
 * Watt W uint16be 1 1 0 value true false
 */
