package org.eprotectioneers.panacea.cs4235.PGPClient.email;

/**
 * This class is a exception class to get thrown
 * if a signature does not matched with payload.
 * @author eProtectioneers
*/
public class PPCA_DigitalSignatureException extends Exception
{
	/**
	 * Constructor
	 */
	public PPCA_DigitalSignatureException()
	{
		//Exception message
		super ("Fail to authenticate signature! Payload corrupted");
	}
}
