package org.eprotectioneers.panacea.cs4235.PGPClient.email;

/**
 * This class represents an Exception that being thrown if the digital
 * signature does not matched with payload.
 * @author eProtectioneers
*/
public class PPCA_DigitalSignatureException extends Exception
{
	public PPCA_DigitalSignatureException()
	{
		super ("Fail to authenticate signature! Payload corrupted");
	}
}
