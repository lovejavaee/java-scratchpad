package com.earldouglas.filtre;

public class AddressCalculator {

	public static long computeAddress(String addressString)
			throws AddressFormatException {
		String[] addressSplit = addressString.split("\\.");
		if (addressSplit.length != 4) {
			throw new AddressFormatException();
		}

		long address = Integer.valueOf(addressSplit[3]);
		address += intValue(addressSplit[2]) * 256L;
		address += intValue(addressSplit[1]) * 65536L;
		address += intValue(addressSplit[0]) * 16777216L;
		return address;
	}

	public static long[] computeAddressRange(String addressString,
			String addressMask) throws AddressFormatException {

		long address = computeAddress(addressString);
		int mask = intValue(addressMask);

		for (int i = mask; i < 32; i++) {
			address /= 2;
		}

		long minAddress = address;
		long maxAddress = address;

		for (int i = mask; i < 32; i++) {
			minAddress *= 2;

			maxAddress *= 2;
			maxAddress += 1;
		}

		return new long[] { minAddress, maxAddress };
	}

	private static int intValue(String intString) throws AddressFormatException {
		try {
			return Integer.valueOf(intString);
		} catch (NumberFormatException numberFormatException) {
			throw new AddressFormatException();
		}
	}
}
