package com.biao.client;

import com.msgilligan.jsonrpc.JsonRPCException;
import foundation.omni.CurrencyID;
import foundation.omni.OmniValue;
import foundation.omni.rpc.OmniClient;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;

import java.io.IOException;
import java.net.URI;

/**
 * The type My omni client.
 *
 */
public class MyOmniClient extends OmniClient {


    /**
     * Instantiates a new My omni client.
     *
     * @param netParams   the net params
     * @param server      the server
     * @param rpcuser     the rpcuser
     * @param rpcpassword the rpcpassword
     * @throws IOException the io exception
     */
    public MyOmniClient(final NetworkParameters netParams, final URI server, final String rpcuser, final String rpcpassword) throws IOException {
        super(netParams, server, rpcuser, rpcpassword);
    }

    /**
     * Omnifunded send sha 256 hash.
     *
     * @param fromAddress the from address
     * @param toAddress   the to address
     * @param currency    the currency
     * @param amount      the amount
     * @param feeaddress  the feeaddress
     * @return the sha 256 hash
     * @throws JsonRPCException the json rpc exception
     * @throws IOException      the io exception
     */
    public Sha256Hash omnifundedSend(final Address fromAddress, final Address toAddress, final CurrencyID currency, final OmniValue amount, final Address feeaddress) throws JsonRPCException, IOException {
        return (Sha256Hash)this.send("omni_funded_send", Sha256Hash.class, new Object[]{fromAddress, toAddress, currency, amount,feeaddress});
    }


}
