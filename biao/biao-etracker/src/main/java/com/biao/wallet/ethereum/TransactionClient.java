package com.biao.wallet.ethereum;

import com.biao.business.DepositService;
import com.biao.constant.Constant;
import com.biao.init.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.response.NoOpProcessor;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

@Configuration
public class TransactionClient {


    private static Logger logger = LoggerFactory.getLogger(TransactionClient.class);

    private volatile static Web3j web3j;
    private volatile static Admin admin;

    private static BigDecimal defaultGasPrice = BigDecimal.valueOf(5);

    public static String createNewAccount() {
        String password = Environment.password;
        try {
            admin = Admin.build(new HttpService(Environment.RPC_URL));
            NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
            String address = newAccountIdentifier.getAccountId();
            return address;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取余额
     *
     * @param address 钱包地址
     * @return 余额
     */
    public static BigInteger getBalance(String address) {
        BigInteger balance = null;
        try {
            web3j = Web3j.build(new HttpService(Environment.RPC_URL));
            EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            balance = ethGetBalance.getBalance();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("address " + address + " balance " + balance + "wei");
        return balance;
    }

    public static BigInteger getBalance(Web3j web3j, String address) {
        BigInteger balance = null;
        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            balance = ethGetBalance.getBalance();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("address " + address + " balance " + balance + "wei");
        return balance;
    }

    /**
     * 生成一个普通交易对象
     *
     * @param fromAddress 放款方
     * @param toAddress   收款方
     * @param nonce       交易序号
     * @param gasPrice    gas 价格
     * @param gasLimit    gas 数量
     * @param value       金额
     * @return 交易对象
     */
    private static Transaction makeTransaction(String fromAddress, String toAddress,
                                               BigInteger nonce, BigInteger gasPrice,
                                               BigInteger gasLimit, BigInteger value) {
        Transaction transaction;
        transaction = Transaction.createEtherTransaction(fromAddress, nonce, gasPrice, gasLimit, toAddress, value);
        return transaction;
    }

    /**
     * 获取普通交易的gas上限
     *
     * @param transaction 交易对象
     * @return gas 上限
     */
    private static BigInteger getTransactionGasLimit(Transaction transaction) {
        BigInteger gasLimit = BigInteger.ZERO;
        try {
            web3j = Web3j.build(new HttpService(Environment.RPC_URL));
            EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
            gasLimit = ethEstimateGas.getAmountUsed();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gasLimit;
    }

    /**
     * 获取账号交易次数 nonce
     *
     * @param address 钱包地址
     * @return nonce
     */
    private static BigInteger getTransactionNonce(String address) {
        BigInteger nonce = BigInteger.ZERO;
        try {
            web3j = Web3j.build(new HttpService(Environment.RPC_URL));
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send();
            nonce = ethGetTransactionCount.getTransactionCount();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nonce;
    }

    /**
     * 发送一个普通交易
     *
     * @return 交易 Hash
     */
    public static String sendTransaction(String fromAddress, String password, String toAddress, BigDecimal amount) {
        BigInteger unlockDuration = BigInteger.valueOf(60L);
        String txHash = null;
        try {
            admin = Admin.build(new HttpService(Environment.RPC_URL));
            //是否在同步区块
            web3j = Web3j.build(new HttpService(Environment.RPC_URL));
            PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(fromAddress, password, unlockDuration).send();
            if (personalUnlockAccount.accountUnlocked()) {
                BigInteger value = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
               /* Transaction transaction = makeTransaction(fromAddress, toAddress,
                        null, null, null, value);
                //不是必须的 可以使用默认值
                BigInteger gasLimit = getTransactionGasLimit(transaction);*/
                //不是必须的 缺省值就是正确的值
                BigInteger nonce = getTransactionNonce(fromAddress);
                //该值为大部分矿工可接受的gasPrice
                BigInteger gasPrice = Convert.toWei(defaultGasPrice, Convert.Unit.GWEI).toBigInteger();
                Transaction transaction = makeTransaction(fromAddress, toAddress,
                        nonce, gasPrice,
                        Constant.GAS_LIMIT, value);
                EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).send();
                txHash = ethSendTransaction.getTransactionHash();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("tx hash " + txHash);
        return txHash;
    }


    public static String sendETH(String fromAddress, String toAddress, BigDecimal amount) {
        logger.info("rpc_url: {}, from: {},  to: {}, amount: {}" , Environment.RPC_URL,fromAddress,toAddress,amount);

        web3j = Web3j.build(new HttpService(Environment.RPC_URL));
        admin = Admin.build(new HttpService(Environment.RPC_URL));
        String txhash = "";
        try {
            PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(fromAddress, Environment.password).send();
            logger.info(personalUnlockAccount.toString());
            if (!personalUnlockAccount.accountUnlocked()) {
                System.out.println("unlock fail");
                return "";
            }
            Transfer transfer = new Transfer(web3j, new ClientTransactionManager(web3j, fromAddress, new NoOpProcessor(web3j)));
            TransactionReceipt receipt = transfer.sendFunds(toAddress, amount, Convert.Unit.ETHER).send();
            txhash = receipt.getTransactionHash();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return txhash;
    }

    // TODO: 2018/3/13 使用 web3j.ethSendRawTransaction() 发送交易 需要用私钥自签名交易
}
