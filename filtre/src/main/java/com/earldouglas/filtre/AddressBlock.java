package com.earldouglas.filtre;

public class AddressBlock {

	private long begin;
	private long end;

	/**
	 * Supported formats are:
	 * 
	 * <ul>
	 * <li>Single address in the format 127.0.0.1</li>
	 * <li>Address with mask in the format 127.0.0.1/32</li>
	 * </ul>
	 */
	public AddressBlock(String addressString) throws AddressFormatException {
		String[] addressMaskSplit = addressString.split("/");
		if (addressMaskSplit.length < 1 || addressMaskSplit.length > 2) {
			throw new AddressFormatException();
		}

		if (addressMaskSplit.length == 1) {
			long address = AddressCalculator
					.computeAddress(addressMaskSplit[0]);
			begin = address;
			end = address;
		} else if (addressMaskSplit.length == 2) {
			long[] addressRange = AddressCalculator.computeAddressRange(
					addressMaskSplit[0], addressMaskSplit[1]);
			begin = addressRange[0];
			end = addressRange[1];
		} else {
			throw new AddressFormatException();
		}
	}

	public boolean contains(long address) {
		return address >= begin && address <= end;
	}
}
