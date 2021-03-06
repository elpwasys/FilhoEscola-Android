package br.com.wasys.library.enumerator;

/**
 * 
 * MobileHeader
 * 31 de jul de 2016 13:06:23
 * @autor Everton Luiz Pascke
 */
public enum DeviceHeader {
	TOKEN ("Token"),
	DEVICE_SO ("Device-SO"),
	DEVICE_IMEI ("Device-IMEI"),
	DEVICE_MODEL ("Device-Model"),
	DEVICE_WIDTH ("Device-Width"),
	DEVICE_HEIGHT ("Device-Height"),
	DEVICE_SO_VERSION ("Device-SO-Version"),
	DEVICE_APP_VERSION ("Device-App-Version"),
	DEVICE_TOKEN("Device-Token");
	public String key;
	DeviceHeader(String key) {
		this.key = key;
	}
}
